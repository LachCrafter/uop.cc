package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FontMod extends Module {

    private static FontMod INSTANCE = new FontMod();

    public Setting<String> fontName = register(new Setting<>("FontName", "Arial", "Name of the font."));
    public Setting<Boolean> antiAlias = register(new Setting<>("AntiAlias", true, "Smoother font."));
    public Setting<Boolean> fractionalMetrics = register(new Setting<>("Metrics", true, "Thinner font."));
    public Setting<Integer> fontSize = register(new Setting<>("Size", 18, 12, 30, "Size of the font."));
    public Setting<Integer> fontStyle = register(new Setting<>("Style", 0, 0, 3, "Style of the font."));
    public Setting<Boolean> minecraft = register(new Setting<>("Minecraft", false, "Overrides Minecraft font."));

    private boolean reloadFont = false;

    public FontMod() {
        super("CustomFont", "Custom font for all of the clients' text. Use the font command.", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static FontMod getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FontMod();
        }
        return INSTANCE;
    }

    public static boolean checkFont(String fontName, boolean displayMessage) {
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String font : availableFonts) {
            if (!displayMessage && font.equals(fontName)) {
                return true;
            }
            if (displayMessage) {
                Command.sendMessage(font);
            }
        }
        return false;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Setting<?> setting = event.getSetting();
        if (event.getStage() == 2 && setting != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage(ChatFormatting.RED + "That font doesn't exist.");
                event.setCanceled(true);
                return;
            }
            reloadFont = true;
        }
    }

    @Override
    public void onTick() {
        if (reloadFont) {
            uop.textManager.init(false);
            reloadFont = false;
        }
    }

    @Override
    public String getDisplayInfo() {
        return fontName.getValue();
    }
}
