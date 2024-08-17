package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;

public class Welcomer extends Module {

    public Setting<String> welcomerSetting = register(new Setting<>("Welcomer", "hi $n"));
    public Setting<Boolean> center = register(new Setting<>("Center", true));
    public Setting<Integer> posX = register(new Setting<>("X", 2, 0, 1000, v -> !center.getValue()));
    public Setting<Integer> posY = register(new Setting<>("Y", 2, 0, 500));

    public Welcomer() {
        super("Welcomer", "makes you not feel lonely", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        String welcomeMessage = getFormattedWelcomeMessage();

        int xPosition = calculateXPosition(welcomeMessage);

        if (ClickGui.getInstance().rainbow.getValue()) {
            renderRainbowText(welcomeMessage, xPosition, posY.getValue());
        } else {
            int color = ColorUtil.toRGBA(
                    ClickGui.getInstance().red.getValue(),
                    ClickGui.getInstance().green.getValue(),
                    ClickGui.getInstance().blue.getValue()
            );
            this.renderer.drawString(welcomeMessage, xPosition, posY.getValue(), color, true);
        }
    }

    private String getFormattedWelcomeMessage() {
        String message = welcomerSetting.getValue();
        message = message.replace("$c", ClickGui.getInstance().command.getValue());
        message = message.replace("$n", mc.player.getDisplayNameString());
        message = message.replace("$s", mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "singleplayer");
        message = message.replace("$t", MathUtil.getShortTimeOfDay());
        message = message.replace("$v", ClickGui.getInstance().version);
        return message;
    }

    private int calculateXPosition(String message) {
        if (center.getValue()) {
            return (this.renderer.scaledWidth - this.renderer.getStringWidth(message)) / 2;
        }
        return posX.getValue();
    }

    private void renderRainbowText(String text, int x, int y) {
        if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
            this.renderer.drawString(text, x, y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
        } else {
            float offsetX = 0.0F;
            int hueShift = 1;
            for (char c : text.toCharArray()) {
                int color = ColorUtil.rainbow(hueShift * ClickGui.getInstance().rainbowHue.getValue()).getRGB();
                this.renderer.drawString(String.valueOf(c), x + offsetX, y, color, true);
                offsetX += this.renderer.getStringWidth(String.valueOf(c));
                hueShift++;
            }
        }
    }
}
