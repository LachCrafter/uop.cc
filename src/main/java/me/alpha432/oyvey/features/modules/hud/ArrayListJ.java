package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ArrayListJ extends Module {
    public enum RenderMode {Left, Down, Up}
    public enum ColorMode {Category, Sync, Future}

    private final Setting<RenderMode> renderMode = register(new Setting<>("Render", RenderMode.Left));
    private final Setting<ColorMode> colorMode = register(new Setting<>("Color", ColorMode.Category));
    private final Setting<Boolean> prefix = register(new Setting<>("Prefix", false));

    public ArrayListJ() {
        super("ArrayListJ", "Renders your current modules", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        uop.moduleManager.sortModules(true);

        for (int i = 0; i < uop.moduleManager.sortedModules.size(); i++) {
            Module module = uop.moduleManager.sortedModules.get(i);
            String moduleString = module.getFullArrayString() + ChatFormatting.RESET;

            if (prefix.getValue()) {
                moduleString = renderMode.getValue() == RenderMode.Left ? ">" + moduleString : moduleString + "<";
            }

            int x = (renderMode.getValue() == RenderMode.Left)
                    ? 2
                    : this.renderer.scaledWidth - 2 - this.renderer.getStringWidth(moduleString);
            int y = calculateYPosition(i);
            int color = getColorForModule(i, module);

            this.renderer.drawString(moduleString, x, y, color, true);
        }
    }

    private int calculateYPosition(int index) {
        int baseY = renderMode.getValue() == RenderMode.Left ? 11 : (renderMode.getValue() == RenderMode.Up ? 2 : this.renderer.scaledHeight - 10);
        int offset = index * 9 * (renderMode.getValue() == RenderMode.Down ? -1 : 1);
        return baseY + offset;
    }

    private int getColorForModule(int index, Module module) {
        switch (colorMode.getValue()) {
            case Category:
                return getCategoryColor(module.getCategory());
            case Sync:
                return ClickGui.getInstance().rainbow.getValue()
                        ? getRainbowColor(index)
                        : ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            case Future:
                return getFutureColor(index);
            default:
                return 0xffffff;
        }
    }

    private int getRainbowColor(int index) {
        return ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up
                ? ColorUtil.rainbow(index * ClickGui.getInstance().rainbowHue.getValue()).getRGB()
                : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
    }

    private int getFutureColor(int index) {
        switch (index % 4) {
            case 1:
                return 0xf5a9b8;
            case 3:
                return 0xffffff;
            default:
                return 0x5bcefa;
        }
    }

    public static int getCategoryColor(Category category) {
        switch (category) {
            case COMBAT:
                return 0xff4040;
            case MISC:
                return 0x00ff00;
            case RENDER:
                return 0xff80ff;
            case MOVEMENT:
                return 0x0040ff;
            case PLAYER:
                return 0x80c0ff;
            case CLIENT:
                return 0x408080;
            case HUD:
                return 0x408040;
            default:
                return 0xffffff;
        }
    }
}
