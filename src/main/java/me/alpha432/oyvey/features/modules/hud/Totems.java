package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Totems extends Module {

    private static final ItemStack TOTEM_ITEMSTACK = new ItemStack(Items.TOTEM_OF_UNDYING);
    public Setting<Boolean> show0 = register(new Setting<>("Show0", true));

    public Totems() {
        super("Totems", "Displays the number of totems you have", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int totemCount = getTotemCount();

        if (totemCount > 0 || show0.getValue()) {
            renderTotems(totemCount);
        }
    }

    private int getTotemCount() {
        int count = mc.player.inventory.mainInventory.stream()
                .filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING)
                .mapToInt(ItemStack::getCount)
                .sum();

        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            count += mc.player.getHeldItemOffhand().getCount();
        }

        return count;
    }

    private void renderTotems(int totemCount) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;

        GlStateManager.enableTexture2D();

        int x = calculateXPosition(width);
        int y = calculateYPosition(height);

        GlStateManager.enableDepth();
        RenderUtil.itemRender.zLevel = 200.0F;
        RenderUtil.itemRender.renderItemAndEffectIntoGUI(TOTEM_ITEMSTACK, x, y);
        RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, TOTEM_ITEMSTACK, x, y, "");
        RenderUtil.itemRender.zLevel = 0.0F;

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        this.renderer.drawStringWithShadow(
                String.valueOf(totemCount),
                (x + 19 - 2 - this.renderer.getStringWidth(String.valueOf(totemCount))),
                (y + 9),
                0xFFFFFF
        );

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private int calculateXPosition(int screenWidth) {
        return screenWidth / 2 - 189 + 180 + 2;
    }

    private int calculateYPosition(int screenHeight) {
        int baseY = screenHeight - 55;
        if (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) {
            baseY -= 10;
        }
        return baseY;
    }
}
