package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
    private final NumberSetting speed = add(new NumberSetting("Speed", 1.0, 0.1, 5, 0.1));
    private OtherClientPlayerEntity cam;

    public Freecam() { super("Freecam", "Detach the camera and fly around while your body stays put.", Category.RENDER); }

    @Override public void onEnable() {
        if (mc.player == null || mc.world == null) { setEnabled(false); return; }
        cam = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
        cam.copyPositionAndRotation(mc.player);
        cam.setInvisible(true);
        cam.setId(-4242);
        mc.world.addEntity(cam);
        mc.setCameraEntity(cam);
    }

    @Override public void onDisable() {
        if (cam != null && mc.world != null) {
            mc.setCameraEntity(mc.player);
            mc.world.removeEntity(cam.getId(), Entity.RemovalReason.DISCARDED);
        }
        cam = null;
    }

    private boolean down(KeyBinding k) {
        return InputUtil.isKeyPressed(mc.getWindow().getHandle(), KeyBindingHelper.getBoundKeyOf(k).getCode());
    }

    // Runs before input is processed so the real player does not move while the camera does.
    @Override public void onPreTick() {
        if (cam == null || mc.player == null) return;
        var o = mc.options;
        double rad = Math.toRadians(mc.player.getYaw());
        Vec3d fwd = new Vec3d(-Math.sin(rad), 0, Math.cos(rad));
        Vec3d right = new Vec3d(-Math.cos(rad), 0, -Math.sin(rad));
        Vec3d mv = Vec3d.ZERO;
        if (mc.currentScreen == null) {
            if (down(o.forwardKey)) mv = mv.add(fwd);
            if (down(o.backKey)) mv = mv.subtract(fwd);
            if (down(o.rightKey)) mv = mv.add(right);
            if (down(o.leftKey)) mv = mv.subtract(right);
            if (down(o.jumpKey)) mv = mv.add(0, 1, 0);
            if (down(o.sneakKey)) mv = mv.add(0, -1, 0);
        }
        if (mv.lengthSquared() > 0) mv = mv.normalize().multiply(speed.get());
        for (KeyBinding k : new KeyBinding[]{o.forwardKey, o.backKey, o.leftKey, o.rightKey, o.jumpKey, o.sneakKey, o.sprintKey})
            k.setPressed(false);

        Vec3d np = cam.getPos().add(mv);
        cam.setPosition(np);
        cam.prevX = cam.lastRenderX = np.x;
        cam.prevY = cam.lastRenderY = np.y;
        cam.prevZ = cam.lastRenderZ = np.z;
        cam.setYaw(mc.player.getYaw());
        cam.setPitch(mc.player.getPitch());
        cam.prevYaw = cam.getYaw();
        cam.prevPitch = cam.getPitch();
    }
}
