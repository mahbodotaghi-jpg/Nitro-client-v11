package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ElytraGlide extends Module {
    private boolean wasJump;

    public ElytraGlide() { super("ElytraGlide", "Press jump in mid-air to open your equipped elytra.", Category.MISC); }

    @Override public void onTick() {
        var p = mc.player;
        var net = mc.getNetworkHandler();
        if (p == null || net == null) return;
        boolean jump = mc.options.jumpKey.isPressed();
        if (jump && !wasJump && !p.isOnGround() && !p.isFallFlying() && !p.isTouchingWater() && !p.hasVehicle()
                && p.getVelocity().y < 0 && p.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            net.sendPacket(new ClientCommandC2SPacket(p, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            p.startFallFlying();
        }
        wasJump = jump;
    }
}
