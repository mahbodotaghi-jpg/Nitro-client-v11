package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;

public class AutoJumpReset extends Module {
    public AutoJumpReset() { super("AutoJumpReset", "Jumps right after you take a hit to cut knockback.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.currentScreen != null) return;
        if (p.hurtTime > 0 && p.hurtTime >= p.maxHurtTime - 2 && p.isOnGround()) p.jump();
    }
}
