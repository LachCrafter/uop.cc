package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

public class CornerClip extends Module {
    public CornerClip() {
        super("CornerClip", "Draws the corner clip", Category.MOVEMENT, true, false, false);
    }
    public Setting<Boolean> autoDisable = this.register(new Setting("Auto Disable", true));

    public double roundToClosest(double num, double low, double high) {
        double d1 = num - low;
        double d2 = high - num;
        return d2 > d1 ? low : high;
    }

    public boolean isMoving() {
        return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck() || autoDisable.getValue() && isMoving()) {
            return;
        }
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().grow(0.01, 0, 0.01)).size() < 2) {
            mc.player.setPosition(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.301, Math.floor(mc.player.posX) + 0.699), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.301, Math.floor(mc.player.posZ) + 0.699));
        } else if (mc.player.ticksExisted % 5 == 0) {
            mc.player.setPosition(mc.player.posX + MathHelper.clamp(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.241, Math.floor(mc.player.posX) + 0.759) - mc.player.posX, -0.03, 0.03), mc.player.posY, mc.player.posZ + MathHelper.clamp(roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.241, Math.floor(mc.player.posZ) + 0.759) - mc.player.posZ, -0.03, 0.03));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.23, Math.floor(mc.player.posX) + 0.77), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.23, Math.floor(mc.player.posZ) + 0.77), true));
        }
    }
}
