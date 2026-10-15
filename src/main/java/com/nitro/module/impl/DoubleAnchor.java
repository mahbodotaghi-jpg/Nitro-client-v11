package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class DoubleAnchor extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 2, 0, 10, 1));
    private int t, step;
    private BlockPos base;

    public DoubleAnchor() {
        super("DoubleAnchor", "Charges an anchor, stacks a second on top, charges it and detonates both.", Category.COMBAT);
    }

    @Override public void onEnable() { t = 0; step = 0; base = null; }

    private void click(int slot, BlockHitResult hit) {
        if (slot == -1) return;
        Util.temp(slot);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (t++ < delay.getInt()) return;
        t = 0;

        if (step == 0) {
            if (mc.crosshairTarget instanceof BlockHitResult h && h.getType() == HitResult.Type.BLOCK
                    && mc.world.getBlockState(h.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                base = h.getBlockPos(); step = 1;
            }
            return;
        }
        BlockState bs = mc.world.getBlockState(base), ts = mc.world.getBlockState(base.up());
        if (!bs.isOf(Blocks.RESPAWN_ANCHOR) && step < 4) { setEnabled(false); return; }
        BlockHitResult baseTop = new BlockHitResult(new Vec3d(base.getX() + .5, base.getY() + 1, base.getZ() + .5), Direction.UP, base, false);
        BlockHitResult topTop = new BlockHitResult(new Vec3d(base.getX() + .5, base.getY() + 2, base.getZ() + .5), Direction.UP, base.up(), false);
        int glow = Util.hotbar(s -> s.isOf(Items.GLOWSTONE));

        switch (step) {
            case 1 -> { if (bs.get(RespawnAnchorBlock.CHARGES) > 0) step = 2; else click(glow, baseTop); }
            case 2 -> { if (ts.isOf(Blocks.RESPAWN_ANCHOR)) step = 3; else click(Util.hotbar(s -> s.isOf(Items.RESPAWN_ANCHOR)), baseTop); }
            case 3 -> {
                if (!ts.isOf(Blocks.RESPAWN_ANCHOR)) step = 2;
                else if (ts.get(RespawnAnchorBlock.CHARGES) > 0) step = 4;
                else click(glow, topTop);
            }
            case 4 -> {
                if (ts.isOf(Blocks.RESPAWN_ANCHOR)) click(Util.nonGlowstone(), topTop);
                else if (bs.isOf(Blocks.RESPAWN_ANCHOR)) click(Util.nonGlowstone(), baseTop);
                else setEnabled(false);
            }
        }
    }
}
