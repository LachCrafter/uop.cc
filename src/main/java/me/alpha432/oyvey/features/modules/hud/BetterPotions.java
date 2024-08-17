package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.uop;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import java.util.List;

public class BetterPotions extends Module {

    enum ListPos {TopRight, Side, Hotbar}
    Setting<ListPos> listpos = register(new Setting<>("Position", ListPos.TopRight));
    enum importantPotionMode {All, Important, Oldfag}
    Setting<importantPotionMode> importantpotionmode = register(new Setting<>("Show", importantPotionMode.All));
    Setting<Boolean> sync = register(new Setting<>("Sync", false));

    public BetterPotions() {
        super("BetterPotions", "Potions list but better", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;

        List<PotionEffect> effects = new ArrayList<>(mc.player.getActivePotionEffects());
        int offsetY = 0;

        for (PotionEffect potionEffect : effects) {
            int posX = 0;
            int posY = 0;

            switch (listpos.getValue()) {
                case TopRight:
                    String potionStr = uop.potionManager.getColoredPotionString(potionEffect);
                    posX = this.renderer.scaledWidth - this.renderer.getStringWidth(potionStr) - 2;
                    posY = 2 + offsetY;
                    break;
                case Side:
                    posX = 2;
                    posY = 236 + offsetY;
                    break;
                case Hotbar:
                    potionStr = uop.potionManager.getColoredPotionString(potionEffect);
                    posX = (this.renderer.scaledWidth - this.renderer.getStringWidth(potionStr)) / 2;
                    posY = this.renderer.scaledHeight - 85 - offsetY;
                    break;
            }

            if (drawPotion(potionEffect, posX, posY)) {
                offsetY += 9;
            }
        }
    }

    private boolean drawPotion(PotionEffect potionEffect, int posX, int posY) {
        String potionString = potionEffect.getPotion().getName();
        String str = uop.potionManager.getColoredPotionString(potionEffect);
        int color = getColor(potionEffect);

        switch (importantpotionmode.getValue()) {
            case All:
                this.renderer.drawString(str, posX, posY, color, true);
                return true;
            case Important:
                if (isImportantPotion(potionString)) {
                    this.renderer.drawString(str, posX, posY, color, true);
                    return true;
                }
                return false;
            case Oldfag:
                if (isOldfagPotion(potionString)) {
                    this.renderer.drawString(str, posX, posY, color, true);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private int getColor(PotionEffect potionEffect) {
        if (sync.getValue()) {
            return ClickGui.getInstance().rainbow.getValue()
                    ? ColorUtil.toRGBA(ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()))
                    : ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        }
        return potionEffect.getPotion().getLiquidColor();
    }

    private boolean isImportantPotion(String potionString) {
        switch (potionString) {
            case "effect.resistance":
            case "effect.fireResistance":
            case "effect.absorption":
            case "effect.regeneration":
            case "effect.wither":
                return false;
            default:
                return true;
        }
    }

    private boolean isOldfagPotion(String potionString) {
        switch (potionString) {
            case "effect.resistance":
            case "effect.fireResistance":
            case "effect.regeneration":
                return false;
            default:
                return true;
        }
    }
}
