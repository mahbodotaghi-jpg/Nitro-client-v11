package com.nitro.module.impl;

import com.nitro.mixin.MinecraftClientAccessor;
import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;

public class FastPlace extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 4, 1));

    public FastPlace() { super("FastPlace", "Removes the delay between item uses and block placements.", Category.MISC); }

    @Override public void onTick() {
        var acc = (MinecraftClientAccessor) mc;
        if (acc.getItemUseCooldown() > delay.getInt()) acc.setItemUseCooldown(delay.getInt());
    }
}
