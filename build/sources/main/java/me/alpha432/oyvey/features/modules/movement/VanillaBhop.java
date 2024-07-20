package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class VanillaBhop extends Module {

    public VanillaBhop() {
        super("VanillaBhop", "horizon anarchy :D :D :D", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player.onGround) mc.player.jump();
    }
} 
