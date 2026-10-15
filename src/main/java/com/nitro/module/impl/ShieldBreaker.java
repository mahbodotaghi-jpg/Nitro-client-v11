package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.hit.EntityHitResult;

public class ShieldBreaker extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 2, 0, 10, 1));
    private int t;

    public ShieldBreaker() { super("ShieldBreaker", "Swaps to an axe and hits a player who is blocking.", Category.COMBAT); }

    @Override public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        if (!(mc.crosshairTarget instanceof EntityHitResult r) || !(r.getEntity() instanceof PlayerEntity target) || !target.isBlocking()) { t = 0; return; }
        if (t++ < delay.getInt()) return;
        int axe = Util.hotbar(s -> s.getItem() instanceof AxeItem);
        if (axe == -1) return;
        t = 0;
        Util.temp(axe);
        Util.attack(target);
    }
}
