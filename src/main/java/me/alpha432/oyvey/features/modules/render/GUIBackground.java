package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class GUIBackground extends Module {
    private static GUIBackground INSTANCE = new GUIBackground();
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));
    public Setting<Boolean> gradient = this.register(new Setting<Boolean>("Gradient", false));
    public Setting<Integer> red = this.register(new Setting<Integer>("TopRed", 255, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> green = this.register(new Setting<Integer>("TopGreen", 255, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> blue = this.register(new Setting<Integer>("TopBlue", 255, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("TopAlpha", 128, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> red2 = this.register(new Setting<Integer>("BottomRed", 0, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> green2 = this.register(new Setting<Integer>("BottomGreen", 0, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> blue2 = this.register(new Setting<Integer>("BottomBlue", 0, 0, 255, v -> gradient.getValue()));
    public Setting<Integer> alpha2 = this.register(new Setting<Integer>("BottomAlpha", 0, 0, 255, v -> gradient.getValue()));
    
    public GUIBackground() {
        super("GUIBackground", "changes minecraft background", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static GUIBackground getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GUIBackground();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

