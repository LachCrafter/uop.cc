package me.alpha432.oyvey.features;

import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.manager.TextManager;
import me.alpha432.oyvey.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Feature implements Util {
    protected List<Setting> settings = new ArrayList<>();
    protected TextManager renderer = uop.textManager;
    private String name;

    public Feature() {}

    public Feature(String name) {
        this.name = name;
    }

    public static boolean nullCheck() {
        return mc.player == null;
    }

    public static boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }

    public String getName() {
        return this.name;
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public boolean isEnabled() {
        return this instanceof Module && ((Module) this).isOn();
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public Setting register(Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && mc.currentScreen instanceof OyVeyGui) {
            OyVeyGui.getInstance().updateModule((Module) this);
        }
        return setting;
    }

    public void unregister(Setting settingIn) {
        this.settings.removeIf(setting -> setting.equals(settingIn));
        if (this instanceof Module && mc.currentScreen instanceof OyVeyGui) {
            OyVeyGui.getInstance().updateModule((Module) this);
        }
    }

    public Setting getSettingByName(String name) {
        return this.settings.stream()
                .filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void reset() {
        this.settings.forEach(setting -> setting.setValue(setting.getDefaultValue()));
    }

    public void clearSettings() {
        this.settings.clear();
    }
}
