package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.uop;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;

public class DebugCrosshair extends Module {
    private static DebugCrosshair INSTANCE = new DebugCrosshair();
    
    public DebugCrosshair() {
        super("DebugCrosshair", "uses the f3 crosshair", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }
    
    @Override
    public void onRender2D(Render2DEvent event) {
        if (mc.gameSettings.thirdPersonView != 0) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(uop.textManager.scaledWidth / 2), (float)(uop.textManager.scaledHeight / 2), 0);
        Entity entity = mc.getRenderViewEntity();
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * mc.getRenderPartialTicks(), -1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.getRenderPartialTicks(), 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-1.0F, -1.0F, -1.0F);
        OpenGlHelper.renderDirections(10);
        GlStateManager.popMatrix();
    }

    public static DebugCrosshair getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DebugCrosshair();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

