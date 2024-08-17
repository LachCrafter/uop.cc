package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.alpha432.oyvey.features.modules.client.ClickGui;

public class Dashboard extends Module {

    public enum DashName {Uop, Medication, Viknet, Gothmoney}

    public Setting<DashName> dashname = register(new Setting<>("Name", DashName.Uop));
    public Setting<Boolean> sync = register(new Setting<>("Sync", false));
    public Setting<Integer> posX = register(new Setting<>("X", 2, 0, 1000));
    public Setting<Integer> posY = register(new Setting<>("Y", 200, 0, 1000));

    public Dashboard() {
        super("Dashboard", "Displays some info for PvP", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;

        int totems = getTotemCount();
        String totemText = totems + " totem" + (totems != 1 ? "s" : "");

        int sets = getSetCount();
        String setText = sets + " set" + (sets != 1 ? "s" : "");

        String name = getNameForDashboard();
        int nameColor = getColorForName();

        renderName(name, nameColor);
        renderStats(totemText, setText);
    }

    private int getTotemCount() {
        int totems = mc.player.inventory.mainInventory.stream()
                .filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING)
                .mapToInt(ItemStack::getCount)
                .sum();

        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += mc.player.getHeldItemOffhand().getCount();
        }
        return totems;
    }

    private int getSetCount() {
        int armorPieces = mc.player.inventory.mainInventory.stream()
                .filter(this::isDiamondArmor)
                .mapToInt(ItemStack::getCount)
                .sum();

        armorPieces += mc.player.inventory.armorInventory.stream()
                .filter(this::isDiamondArmor)
                .mapToInt(ItemStack::getCount)
                .sum();

        return armorPieces / 4;
    }

    private boolean isDiamondArmor(ItemStack itemStack) {
        return itemStack.getItem() == Items.DIAMOND_HELMET ||
                itemStack.getItem() == Items.DIAMOND_CHESTPLATE ||
                itemStack.getItem() == Items.DIAMOND_LEGGINGS ||
                itemStack.getItem() == Items.DIAMOND_BOOTS;
    }

    private String getNameForDashboard() {
        switch (dashname.getValue()) {
            case Medication:
                return "medication.eu";
            case Viknet:
                return "viknet.se";
            case Gothmoney:
                return "goth.money";
            default:
                return "uop.cc";
        }
    }

    private int getColorForName() {
        switch (dashname.getValue()) {
            case Medication:
                return ColorUtil.toRGBA(192, 0, 255);
            case Viknet:
                return ColorUtil.toRGBA(0, 255, 0);
            case Gothmoney:
                return ColorUtil.toRGBA(16, 16, 16);
            default:
                return ColorUtil.toRGBA(255, 255, 0);
        }
    }

    private void renderName(String name, int nameColor) {
        if (sync.getValue()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                renderRainbowName(name);
            } else {
                int syncColor = ColorUtil.toRGBA(
                        ClickGui.getInstance().red.getValue(),
                        ClickGui.getInstance().green.getValue(),
                        ClickGui.getInstance().blue.getValue()
                );
                this.renderer.drawString(name, posX.getValue(), posY.getValue(), syncColor, true);
            }
        } else {
            this.renderer.drawString(name, posX.getValue(), posY.getValue(), nameColor, true);
        }
    }

    private void renderRainbowName(String name) {
        if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
            this.renderer.drawString(name, posX.getValue(), posY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
        } else {
            float offset = 0.0F;
            for (char c : name.toCharArray()) {
                int color = ColorUtil.rainbow((int) (offset * ClickGui.getInstance().rainbowHue.getValue())).getRGB();
                this.renderer.drawString(String.valueOf(c), posX.getValue() + offset, posY.getValue(), color, true);
                offset += this.renderer.getStringWidth(String.valueOf(c));
            }
        }
    }

    private void renderStats(String totemText, String setText) {
        this.renderer.drawString(totemText, posX.getValue(), posY.getValue() + 9, ColorUtil.toRGBA(255, 255, 255), true);
        this.renderer.drawString(setText, posX.getValue(), posY.getValue() + 18, ColorUtil.toRGBA(255, 255, 255), true);
    }
}
