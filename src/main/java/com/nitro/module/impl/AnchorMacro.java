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

public class AnchorMacro extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 2, 0, 10, 1));
    private int t;

    public AnchorMacro() { super("AnchorMacro", "Charges and detonates the anchor you look at.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK) return;
        BlockState st = mc.world.getBlockState(hit.getBlockPos());
        if (!st.isOf(Blocks.RESPAWN_ANCHOR)) return;
        if (t++ < delay.getInt()) return;
        t = 0;
        int slot = st.get(RespawnAnchorBlock.CHARGES) == 0
                ? Util.hotbar(s -> s.isOf(Items.GLOWSTONE))
                : Util.nonGlowstone();
        if (slot == -1) return;
        Util.temp(slot);
        mc.interactionManager.interactBlock(p, Hand.MAIN_HAND, hit);
        p.swingHand(Hand.MAIN_HAND);
    }
}
