package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.LoggerUtil;
import org.apache.logging.log4j.core.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class RusherCapes extends Module {

    private static RusherCapes INSTANCE;

    public static ArrayList<String> lines = new ArrayList<>();

    public RusherCapes() {
        super("RusherCapes", "rusherhack capes im going insane", Module.Category.RENDER, false, false, false);
        this.setInstance();
        getCapeList();
    }

    @Override
    public void onEnable() {
        lines.clear();
        getCapeList();
    }

    @Override
    public void onDisable() {
        lines.clear();
    }

    public static RusherCapes getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new RusherCapes();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static String getCape(UUID uuid) {
        String uuidString = uuid.toString();
        String capeID = "none";
        for (String line : lines) {
            if (line.contains(uuidString)) {
                return line.substring(37);
            }
        }
        return capeID;
    }

    public void getCapeList() {
        try {
            URL url = new URL("https://auth.rusherhack.net/getcapes.php?new=yessir");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

        } catch (Exception e) {
            LoggerUtil.error(e);
        }
    }
}
