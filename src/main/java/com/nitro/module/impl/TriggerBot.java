package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.hit.EntityHitResult;

public class TriggerBot extends Module {
    private final NumberSetting charge = add(new NumberSetting("Min charge", 0.9, 0.1, 1, 0.05));

    public TriggerBot() { super("TriggerBot", "Attacks the living entity under your crosshair at full charge.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.currentScreen != null) return;
        if (!(mc.crosshairTarget instanceof EntityHitResult r) || !(r.getEntity() instanceof LivingEntity e)) return;
        if (e == p || !e.isAlive() || e instanceof ArmorStandEntity) return;
        if (p.getAttackCooldownProgress(0.5f) < charge.get()) return;
        Util.attack(e);
    }
}
