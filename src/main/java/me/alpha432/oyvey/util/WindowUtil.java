package me.alpha432.oyvey.util;

import me.alpha432.oyvey.uop;
import net.minecraft.client.MinecraftClient;

public class WindowUtil {
    private int ticks = 0;
    private int a = 0;
    private int breakTimer = 0;
    private boolean qwerty = false;

    public void tick() {
        ++ticks;
        if (ticks % 17 == 0) {
            String clientName = uop.NAME;
            MinecraftClient.getInstance().getWindow().setTitle(clientName.substring(0, clientName.length() - a));
            if ((a == clientName.length() && breakTimer != 2) || (a == 0 && breakTimer != 4)) {
                breakTimer++;
                return;
            } else {
                breakTimer = 0;
            }
            if (a == clientName.length()) qwerty = true;
            if (qwerty) --a;
            else ++a;
            if (a == 0) qwerty = false;
        }
    }
}
