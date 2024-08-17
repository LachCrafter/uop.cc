package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {

    private static ClickGui INSTANCE = new ClickGui();
    public final String version = uop.MODVER;

    public Setting<Boolean> toggleMsg = register(new Setting<>("ToggleMessage", true));
    public Setting<String> prefix = register(new Setting<>("Prefix", "."));
    public Setting<String> command = register(new Setting<>("Command", "uop.cc"));
    public Setting<TextUtil.Color> bracketColor = register(new Setting<>("BracketColor", TextUtil.Color.YELLOW));
    public Setting<TextUtil.Color> commandColor = register(new Setting<>("NameColor", TextUtil.Color.YELLOW));
    public Setting<String> commandBracket = register(new Setting<>("Bracket", ""));
    public Setting<String> commandBracket2 = register(new Setting<>("Bracket2", ":"));

    public Setting<Boolean> customFov = register(new Setting<>("CustomFov", false));
    public Setting<Float> fov = register(new Setting<>("Fov", 150.0f, -180.0f, 180.0f));
    public Setting<Integer> red = register(new Setting<>("Red", 0, 0, 255));
    public Setting<Integer> green = register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = register(new Setting<>("Alpha", 180, 0, 255));
    public Setting<Integer> alpha = register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = register(new Setting<>("HRainbowMode", rainbowMode.Static, v -> rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = register(new Setting<>("ARainbowMode", rainbowModeArray.Static, v -> rainbow.getValue()));
    public Setting<Integer> rainbowHue = register(new Setting<>("Delay", 240, 0, 600, v -> rainbow.getValue()));
    public Setting<Float> rainbowBrightness = register(new Setting<>("Brightness", 150.0f, 1.0f, 255.0f, v -> rainbow.getValue()));
    public Setting<Float> rainbowSaturation = register(new Setting<>("Saturation", 150.0f, 1.0f, 255.0f, v -> rainbow.getValue()));

    private OyVeyGui click;

    public ClickGui() {
        super("Client", "Client settings", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (customFov.getValue()) {
            mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.getValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            uop.commandManager.setClientMessage(getCommandMessage());
            if (event.getSetting().equals(prefix)) {
                uop.commandManager.setPrefix(prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + uop.commandManager.getPrefix());
            }
            uop.colorManager.setColor(red.getPlannedValue(), green.getPlannedValue(), blue.getPlannedValue(), hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        uop.colorManager.setColor(red.getValue(), green.getValue(), blue.getValue(), hoverAlpha.getValue());
        uop.commandManager.setPrefix(prefix.getValue());
        uop.commandManager.setClientMessage(getCommandMessage());
    }

    @Override
    public void onTick() {
        if (!(mc.currentScreen instanceof OyVeyGui)) {
            disable();
        }
    }

    public String getCommandMessage() {
        return TextUtil.coloredString(commandBracket.getPlannedValue(), bracketColor.getPlannedValue())
                + TextUtil.coloredString(command.getPlannedValue(), commandColor.getPlannedValue())
                + TextUtil.coloredString(commandBracket2.getPlannedValue(), bracketColor.getPlannedValue());
    }

    public enum rainbowMode {
        Static,
        Sideway
    }

    public enum rainbowModeArray {
        Static,
        Up
    }
}
