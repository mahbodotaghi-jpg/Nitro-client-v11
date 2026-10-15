package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.hit.EntityHitResult;

public class AutoHitCrystal extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 10, 1));
    private int t;

    public AutoHitCrystal() { super("AutoHitCrystal", "Attacks the end crystal you are looking at.", Category.COMBAT); }

    @Override public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        if (!(mc.crosshairTarget instanceof EntityHitResult r) || !(r.getEntity() instanceof EndCrystalEntity e)) { t = 0; return; }
        if (t++ < delay.getInt()) return;
        t = 0;
        Util.attack(e);
    }
}
