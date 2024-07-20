package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.misc.ToolTips;
import me.alpha432.oyvey.features.modules.render.GUIBackground;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Util;
import me.alpha432.oyvey.uop;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiScreen.class})
public class MixinGuiScreen
        extends Gui {
    @Inject(method = {"renderToolTip"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ToolTips.getInstance().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            ToolTips.getInstance().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
    }
    
    @Inject(method = {"drawDefaultBackground"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void drawDefaultBackgroundHook(CallbackInfo info) {
        if (GUIBackground.getINSTANCE().isOn() && Util.mc.world != null) {
            if (GUIBackground.getINSTANCE().gradient.getValue()) {
                RenderUtil.drawGradientRect(0, 0, uop.textManager.scaledWidth, uop.textManager.scaledHeight + 1, ColorUtil.toRGBA(GUIBackground.getINSTANCE().red.getValue(), GUIBackground.getINSTANCE().green.getValue(), GUIBackground.getINSTANCE().blue.getValue(), GUIBackground.getINSTANCE().alpha.getValue()), ColorUtil.toRGBA(GUIBackground.getINSTANCE().red2.getValue(), GUIBackground.getINSTANCE().green2.getValue(), GUIBackground.getINSTANCE().blue2.getValue(), GUIBackground.getINSTANCE().alpha2.getValue()), true);
            }
            if (!GUIBackground.getINSTANCE().vanilla.getValue()) info.cancel();
        }
    }
}

