package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.features.modules.client.ClickGui;

public class Watermark extends Module {

    public Setting<Boolean> versionNumber = register(new Setting<>("Version", false));
    public Setting<Integer> posX = register(new Setting<>("X", 2, 0, 1000));
    public Setting<Integer> posY = register(new Setting<>("Y", 2, 0, 500));

    public Watermark() {
        super("Watermark", "says hi to you", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        String text = ClickGui.getInstance().command.getPlannedValue();
        int x = posX.getValue();
        int y = posY.getValue();
        int color = getColor();

        if (ClickGui.getInstance().rainbow.getValue()) {
            renderRainbowText(text, x, y);
        } else {
            this.renderer.drawString(text, x, y, color, true);
        }

        if (versionNumber.getValue()) {
            renderVersion(text, x, y);
        }
    }

    private int getColor() {
        return ColorUtil.toRGBA(
                ClickGui.getInstance().red.getValue(),
                ClickGui.getInstance().green.getValue(),
                ClickGui.getInstance().blue.getValue()
        );
    }

    private void renderRainbowText(String text, int x, int y) {
        if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
            this.renderer.drawString(text, x, y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
        } else {
            float offsetX = 0.0F;
            int[] hueShift = {1};
            for (char c : text.toCharArray()) {
                this.renderer.drawString(String.valueOf(c), x + offsetX, y, ColorUtil.rainbow(hueShift[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                offsetX += this.renderer.getStringWidth(String.valueOf(c));
                hueShift[0]++;
            }
        }
    }

    private void renderVersion(String text, int x, int y) {
        String versionText = " " + ClickGui.getInstance().version;
        int versionX = x + this.renderer.getStringWidth(text);
        this.renderer.drawString(versionText, versionX, y, ColorUtil.toRGBA(255, 255, 255), true);
    }
}
