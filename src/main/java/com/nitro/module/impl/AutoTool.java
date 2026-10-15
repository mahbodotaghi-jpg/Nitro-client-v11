package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoTool extends Module {
    private int prev = -1;

    public AutoTool() { super("AutoTool", "Switches to the fastest hotbar tool while mining.", Category.MISC); }

    @Override public void onDisable() { back(); }

    private void back() {
        if (prev != -1 && mc.player != null) Util.select(prev);
        prev = -1;
    }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.world == null) return;
        boolean mining = mc.options.attackKey.isPressed() && mc.currentScreen == null
                && mc.crosshairTarget instanceof BlockHitResult h && h.getType() == HitResult.Type.BLOCK;
        if (!mining) { back(); return; }
        BlockState st = mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos());
        int best = -1;
        float bs = p.getInventory().getStack(Util.selected()).getMiningSpeedMultiplier(st);
        for (int i = 0; i < 9; i++) {
            float sp = p.getInventory().getStack(i).getMiningSpeedMultiplier(st);
            if (sp > bs) { bs = sp; best = i; }
        }
        if (best != -1) {
            if (prev == -1) prev = Util.selected();
            Util.select(best);
        }
    }
}
