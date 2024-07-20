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
    
    public Setting<Integer> posX = register(new Setting("X", 2, 0, 1000));
    public Setting<Integer> posY = register(new Setting("Y", 11, 0, 1000));
    int count = 0;
    
    public Radar() {
        super("Radar", "big awesome", Category.HUD, true, false, false);
    }

    public void onRender2D(Render2DEvent event) {
        count = 0;
        Color maincolor = new Color((ClickGui.getInstance()).red.getValue(), (ClickGui.getInstance()).green.getValue(), (ClickGui.getInstance()).blue.getValue(), 255);
        if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
            maincolor = ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue());
        }
        int x = posX.getValue();
        int y = posY.getValue();
        RenderUtil.drawRect(x, y, x + 100, y + 100, 2147483648L);
        
        int dispX = (x + 50);
        int dispY = (y + 50);
        GL11.glTranslatef((float) dispX, (float) dispY, 0.0f);
        GL11.glRotatef(-mc.player.rotationYaw, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float) (-dispX), (float) (-dispY), 0.0f);
            
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player) continue;
            count++;
            int differenceX = (int)Math.floor(mc.player.posX) - (int)Math.floor(player.posX);
            int differenceY = (int)Math.floor(mc.player.posZ) - (int)Math.floor(player.posZ);
            if ((Math.abs(differenceX) + Math.abs(differenceY)) < 49) {
                RenderUtil.drawRect(x + 50 + differenceX, y + 50 + differenceY, x + 51 + differenceX, y + 51 + differenceY, uop.friendManager.isFriend(player) ? ColorUtil.toRGBA(85, 255, 255) : ColorUtil.toRGBA(maincolor));
            }
        }
        
        GL11.glTranslatef((float) dispX, (float) dispY, 0.0f);
        GL11.glRotatef(mc.player.rotationYaw, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float) (-dispX), (float) (-dispY), 0.0f);
        
        RenderUtil.drawRect(x + 49, y + 49, x + 51, y + 51, ColorUtil.toRGBA(255, 0, 0));
        
        RenderUtil.drawRect(x, y, x + 101, y + 1, ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect(x, y, x + 1, y + 101, ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect(x + 101, y + 101, x + 100, y + 1, ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect(x + 101, y + 101, x + 1, y + 100, ColorUtil.toRGBA(64, 64, 64, 255));
        
    }
    
    @Override
    public String getDisplayInfo() {
        return String.valueOf(count);
    }
}
