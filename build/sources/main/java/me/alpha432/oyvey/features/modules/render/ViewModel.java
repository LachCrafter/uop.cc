package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class ViewModel extends Module {
    private static ViewModel INSTANCE = new ViewModel();
    
    public Setting<Float> TranslateX = this.register(new Setting<Float>("TranslateX", 0.0f, -3.0f, 3.0f));
    public Setting<Float> TranslateY = this.register(new Setting<Float>("TranslateY", 0.0f, -3.0f, 3.0f));
    public Setting<Float> TranslateZ = this.register(new Setting<Float>("TranslateZ", 0.0f, -3.0f, 3.0f));
    public Setting<Float> ScaleX = this.register(new Setting<Float>("ScaleX", 1.0f, -3.0f, 3.0f));
    public Setting<Float> ScaleY = this.register(new Setting<Float>("ScaleY", 1.0f, -3.0f, 3.0f));
    public Setting<Float> ScaleZ = this.register(new Setting<Float>("ScaleZ", 1.0f, -3.0f, 3.0f));
    public Setting<Float> RotateX = this.register(new Setting<Float>("RotateX", 0.0f, -180.0f, 180.0f));
    public Setting<Float> RotateY = this.register(new Setting<Float>("RotateY", 0.0f, -180.0f, 180.0f));
    public Setting<Float> RotateZ = this.register(new Setting<Float>("RotateZ", 0.0f, -180.0f, 180.0f));
    
    public ViewModel() {
        super("ViewModel", "Changes your ViewModel", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }
    
    

    public static ViewModel getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

