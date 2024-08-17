package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.ColorUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.uop;

import java.awt.Color;

public class Radar extends Module {

    private final Setting<Integer> posX = register(new Setting<>("X", 2, 0, 1000));
    private final Setting<Integer> posY = register(new Setting<>("Y", 11, 0, 1000));
    private int count = 0;

    public Radar() {
        super("Radar", "Displays a radar of nearby players", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        count = 0;
        int x = posX.getValue();
        int y = posY.getValue();
        Color mainColor = getMainColor();

        drawRadarBackground(x, y);
        applyTransformations(x, y);

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != mc.player) {
                drawPlayerOnRadar(player, x, y, mainColor);
            }
        }

        resetTransformations(x, y);
        drawCenterMarker(x, y);
        drawRadarBorder(x, y);
    }

    private Color getMainColor() {
        if (ClickGui.getInstance().rainbow.getValue()) {
            return ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue());
        }
        return new Color(
                ClickGui.getInstance().red.getValue(),
                ClickGui.getInstance().green.getValue(),
                ClickGui.getInstance().blue.getValue(),
                255
        );
    }

    private void drawRadarBackground(int x, int y) {
        RenderUtil.drawRect(x, y, x + 100, y + 100, 0x80000000L); // Draws the radar background
    }

    private void applyTransformations(int x, int y) {
        int centerX = x + 50;
        int centerY = y + 50;

        GL11.glTranslatef(centerX, centerY, 0.0f);
        GL11.glRotatef(-mc.player.rotationYaw, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-centerX, -centerY, 0.0f);
    }

    private void resetTransformations(int x, int y) {
        int centerX = x + 50;
        int centerY = y + 50;

        GL11.glTranslatef(centerX, centerY, 0.0f);
        GL11.glRotatef(mc.player.rotationYaw, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-centerX, -centerY, 0.0f);
    }

    private void drawPlayerOnRadar(EntityPlayer player, int x, int y, Color mainColor) {
        int differenceX = (int) Math.floor(mc.player.posX - player.posX);
        int differenceY = (int) Math.floor(mc.player.posZ - player.posZ);

        if (Math.abs(differenceX) + Math.abs(differenceY) < 49) {
            int color = uop.friendManager.isFriend(player) ? ColorUtil.toRGBA(85, 255, 255) : ColorUtil.toRGBA(mainColor);
            RenderUtil.drawRect(x + 50 + differenceX, y + 50 + differenceY, x + 51 + differenceX, y + 51 + differenceY, color);
            count++;
        }
    }

    private void drawCenterMarker(int x, int y) {
        RenderUtil.drawRect(x + 49, y + 49, x + 51, y + 51, ColorUtil.toRGBA(255, 0, 0));
    }

    private void drawRadarBorder(int x, int y) {
        int borderColor = ColorUtil.toRGBA(64, 64, 64, 255);
        RenderUtil.drawRect(x, y, x + 101, y + 1, borderColor);
        RenderUtil.drawRect(x, y, x + 1, y + 101, borderColor);
        RenderUtil.drawRect(x + 100, y, x + 101, y + 101, borderColor);
        RenderUtil.drawRect(x, y + 100, x + 101, y + 101, borderColor);
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(count);
    }
}
