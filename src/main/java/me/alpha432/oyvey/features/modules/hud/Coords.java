package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;

public class Coords extends Module {

    public Setting<Integer> decimals = register(new Setting<>("Decimals", 0, -1, 2));
    public Setting<Boolean> nether = register(new Setting<>("Nether", true));

    public Coords() {
        super("Coords", "Shows coordinates on screen", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
        float netherMultiplier = inHell ? 8.0f : 0.125f;
        int color = getColor();

        String coords = formatCoords(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (nether.getValue() && !mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("The End")) {
            coords += " [" + formatCoords(mc.player.posX * netherMultiplier, mc.player.posZ * netherMultiplier) + "]";
        }

        this.renderer.drawString(coords, 2, this.renderer.scaledHeight - 9, color, true);
    }

    private int getColor() {
        if (ClickGui.getInstance().rainbow.getValue()) {
            return ColorUtil.toRGBA(ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()));
        } else {
            return ColorUtil.toRGBA(
                    ClickGui.getInstance().red.getValue(),
                    ClickGui.getInstance().green.getValue(),
                    ClickGui.getInstance().blue.getValue()
            );
        }
    }

    private String formatCoords(double x, double y, double z) {
        return String.format("XYZ %s %s %s", formatCoord(x), formatCoord(y), formatCoord(z));
    }

    private String formatCoords(double x, double z) {
        return String.format("%s %s", formatCoord(x), formatCoord(z));
    }

    private String formatCoord(double n) {
        n = Math.round(n * Math.pow(10, decimals.getValue())) / Math.pow(10, decimals.getValue());
        if (decimals.getValue() <= 0) {
            return String.valueOf((int) n);
        } else {
            return String.format("%." + decimals.getValue() + "f", n);
        }
    }
}
