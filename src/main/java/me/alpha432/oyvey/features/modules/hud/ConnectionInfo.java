package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.uop;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ConnectionInfo extends Module {
    public enum RenderMode {Up, Down}

    private final Setting<RenderMode> rendermode = register(new Setting<>("Render", RenderMode.Up));

    public ConnectionInfo() {
        super("ConnectionInfo", "Displays connection info", Category.HUD, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int color = getColor();
        String msString = formatMsString();
        String tpsString = formatTpsString();
        String lagString = formatLagString();

        drawString(msString, 2, color);
        drawString(tpsString, 11, color);
        drawString(lagString, 20, color);
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

    private String formatMsString() {
        return ChatFormatting.WHITE + "" + uop.serverManager.getPing() + ChatFormatting.RESET + " ms";
    }

    private String formatTpsString() {
        return ChatFormatting.WHITE + "" + Math.round(uop.serverManager.getTPS()) + ChatFormatting.RESET + " tps";
    }

    private String formatLagString() {
        if (uop.serverManager.isLagging()) {
            return ChatFormatting.WHITE + "" + Math.floor(uop.serverManager.serverRespondingTime() / 100.0f) / 10.0f + "s" + ChatFormatting.RESET + " lag";
        }
        return "";
    }

    private void drawString(String text, int yOffset, int color) {
        int yPos = rendermode.getValue() == RenderMode.Up ? yOffset : this.renderer.scaledHeight - 10 - (yOffset - 2);
        int xPos = this.renderer.scaledWidth - 2 - this.renderer.getStringWidth(text);
        this.renderer.drawString(text, xPos, yPos, color, true);
    }
}
