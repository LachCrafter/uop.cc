package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

public class CornerClip extends Module {

    private static CornerClip INSTANCE = new CornerClip();
    public final Setting<Integer> timeout = this.register(new Setting<Integer>("Timeout", 5, 1, 10));
    public final Setting<Boolean> disablee = this.register(new Setting<Boolean>("AutoDisable", true));
    public int disableThingy;
    
    public CornerClip() {
        super("CornerClip", "Draws the corner clip", Category.MOVEMENT, true, false, false);
    }

    public static CornerClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CornerClip();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (CornerClip.nullCheck()) {
            return;
        }
        if (CornerClip.INSTANCE.movingByKeys()) {
            this.disable();
            return;
        }
        if (CornerClip.mc.world.getCollisionBoxes(CornerClip.mc.player, CornerClip.mc.player.getEntityBoundingBox().grow(0.01, 0, 0.01)).size() < 2) {
            CornerClip.mc.player.setPosition(CornerClip.roundToClosest(CornerClip.mc.player.posX, Math.floor(CornerClip.mc.player.posX) + 0.301, Math.floor(CornerClip.mc.player.posX) + 0.699), CornerClip.mc.player.posY, CornerClip.roundToClosest(CornerClip.mc.player.posZ, Math.floor(CornerClip.mc.player.posZ) + 0.301, Math.floor(CornerClip.mc.player.posZ) + 0.699));
        } else if (CornerClip.mc.player.ticksExisted % this.timeout.getValue().intValue() == 0) {
            CornerClip.mc.player.setPosition(CornerClip.mc.player.posX + MathHelper.clamp(CornerClip.roundToClosest(CornerClip.mc.player.posX, Math.floor(CornerClip.mc.player.posX) + 0.241, Math.floor(CornerClip.mc.player.posX) + 0.759) - CornerClip.mc.player.posX, -0.03, 0.03), CornerClip.mc.player.posY, CornerClip.mc.player.posZ + MathHelper.clamp(CornerClip.roundToClosest(CornerClip.mc.player.posZ, Math.floor(CornerClip.mc.player.posZ) + 0.241, Math.floor(CornerClip.mc.player.posZ) + 0.759) - CornerClip.mc.player.posZ, -0.03, 0.03));
            CornerClip.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(CornerClip.mc.player.posX, CornerClip.mc.player.posY, CornerClip.mc.player.posZ, true));
            CornerClip.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(CornerClip.roundToClosest(CornerClip.mc.player.posX, Math.floor(CornerClip.mc.player.posX) + 0.23, Math.floor(CornerClip.mc.player.posX) + 0.77), CornerClip.mc.player.posY, CornerClip.roundToClosest(CornerClip.mc.player.posZ, Math.floor(CornerClip.mc.player.posZ) + 0.23, Math.floor(CornerClip.mc.player.posZ) + 0.77), true));
            if (this.disablee.getValue().booleanValue()) {
                disableThingy++;
            } else {
                disableThingy = 0;
            }
        }
        if (disableThingy >= 2 && this.disablee.getValue().booleanValue()) {
            disableThingy = 0;
            this.disable();
        }
    }

    private boolean movingByKeys() {
        return CornerClip.mc.gameSettings.keyBindForward.isKeyDown() || CornerClip.mc.gameSettings.keyBindBack.isKeyDown() || CornerClip.mc.gameSettings.keyBindLeft.isKeyDown() || CornerClip.mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static double roundToClosest(double num, double low, double high) {
        double d1 = num - low;
        double d2 = high - num;
        if (d2 > d1) {
            return low;
        } else {
            return high;
        }
    }
}
