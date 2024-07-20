package me.alpha432.oyvey.manager;

import com.google.gson.JsonElement;
import com.google.gson.*;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Bind;
import me.alpha432.oyvey.features.setting.EnumConverter;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.LoggerUtil;
import me.alpha432.oyvey.util.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigManager implements Util {
    public ArrayList<Feature> features = new ArrayList<>();

    public String config = "uop/config/";

    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        String str;
        switch (setting.getType()) {
            case "Boolean":
                setting.setValue(Boolean.valueOf(element.getAsBoolean()));
                return;
            case "Double":
                setting.setValue(Double.valueOf(element.getAsDouble()));
                return;
            case "Float":
                setting.setValue(Float.valueOf(element.getAsFloat()));
                return;
            case "Integer":
                setting.setValue(Integer.valueOf(element.getAsInt()));
                return;
            case "String":
                str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                return;
            case "Bind":
                setting.setValue((new Bind.BindConverter()).doBackward(element));
                return;
            case "Enum":
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                } catch (Exception exception) {
                    LoggerUtil.error("Failed to convert enum value for setting: " + setting.getName(), exception);
                }
                return;
        }
        LoggerUtil.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
    }

    private static void loadFile(JsonObject input, Feature feature) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String settingName = entry.getKey();
            JsonElement element = entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    uop.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                } catch (Exception e) {
                    LoggerUtil.error("Failed to add friend: " + settingName, e);
                }
                continue;
            }
            boolean settingFound = false;
            for (Setting setting : feature.getSettings()) {
                if (settingName.equals(setting.getName())) {
                    try {
                        setValueFromJson(feature, setting, element);
                    } catch (Exception e) {
                        LoggerUtil.error("Failed to set value from JSON for setting: " + setting.getName(), e);
                    }
                    settingFound = true;
                    break;
                }
            }
            if (!settingFound) {
                LoggerUtil.warn("Setting not found: " + settingName + " for feature: " + feature.getName());
            }
        }
    }

    public void loadConfig(String name) {
        File configDir = new File("uop/" + name + "/");
        if (configDir.exists() && configDir.isDirectory()) {
            this.config = "uop/" + name + "/";
        } else {
            this.config = "uop/config/";
        }
        uop.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                loadSettings(feature);
            } catch (IOException e) {
                LoggerUtil.error("Failed to load settings for feature: " + feature.getName(), e);
            }
        }
        saveCurrentConfig();
    }

    public boolean configExists(String name) {
        File configDir = new File("uop/" + name + "/");
        return configDir.exists() && configDir.isDirectory();
    }

    public void saveConfig(String name) {
        this.config = "uop/" + name + "/";
        File path = new File(this.config);
        if (!path.exists()) {
            path.mkdir();
        }
        uop.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                saveSettings(feature);
            } catch (IOException e) {
                LoggerUtil.error("Failed to save settings for feature: " + feature.getName(), e);
            }
        }
        saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("uop/currentconfig.txt");
        try {
            if (!currentConfig.exists()) {
                currentConfig.createNewFile();
            }
            try (FileWriter writer = new FileWriter(currentConfig)) {
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("uop", ""));
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to save current config", e);
        }
    }

    public String loadCurrentConfig() {
        File currentConfig = new File("uop/currentconfig.txt");
        String name = "config";
        try (Scanner reader = new Scanner(currentConfig)) {
            if (currentConfig.exists()) {
                while (reader.hasNextLine()) {
                    name = reader.nextLine();
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to load current config", e);
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for (Feature feature : this.features) {
            feature.reset();
        }
        if (saveConfig) {
            saveConfig(name);
        }
    }

    public void saveSettings(Feature feature) throws IOException {
        JsonObject object = new JsonObject();
        File directory = new File(this.config + getDirectory(feature));
        if (!directory.exists()) {
            directory.mkdir();
        }
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path outputFile = Paths.get(featureName);
        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(writeSettings(feature));
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)))) {
            writer.write(json);
        }
    }

    public void init() {
        this.features.addAll(uop.moduleManager.modules);
        this.features.add(uop.friendManager);
        String name = loadCurrentConfig();
        loadConfig(name);
        LoggerUtil.info("Config loaded.");
    }

    private void loadSettings(Feature feature) throws IOException {
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path featurePath = Paths.get(featureName);
        if (!Files.exists(featurePath)) {
            return;
        }
        loadPath(featurePath, feature);
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        try (InputStream stream = Files.newInputStream(path)) {
            try {
                loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), feature);
            } catch (IllegalStateException e) {
                LoggerUtil.error("Bad Config File for: " + feature.getName() + ". Resetting...", e);
                loadFile(new JsonObject(), feature);
            }
        }
    }

    public JsonObject writeSettings(Feature feature) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                object.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
            } else if (setting.isStringSetting()) {
                String str = (String) setting.getValue();
                setting.setValue(str.replace(" ", "_"));
                object.addProperty(setting.getName(), str);
            } else {
                try {
                    object.add(setting.getName(), jp.parse(setting.getValueAsString()));
                } catch (Exception e) {
                    LoggerUtil.error("Failed to write setting: " + setting.getName() + " for feature: " + feature.getName(), e);
                }
            }
        }
        return object;
    }

    public String getDirectory(Feature feature) {
        String directory = "";
        if (feature instanceof Module) {
            directory += ((Module) feature).getCategory().getName() + "/";
        }
        return directory;
    }
}
