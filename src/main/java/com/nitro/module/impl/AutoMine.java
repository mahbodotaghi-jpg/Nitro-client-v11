package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import net.minecraft.util.hit.HitResult;

public class AutoMine extends Module {
    public AutoMine() { super("AutoMine", "Holds attack while you look at a block.", Category.MISC); }

    @Override public void onDisable() { mc.options.attackKey.setPressed(false); }

    @Override public void onTick() {
        boolean block = mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK;
        mc.options.attackKey.setPressed(block && mc.currentScreen == null);
    }
}
