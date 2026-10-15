package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.item.Items;

public class AutoFirework extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 40, 5, 200, 5));
    private int t;

    public AutoFirework() { super("AutoFirework", "Uses a hotbar firework at an interval while gliding.", Category.MISC); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null || !p.isFallFlying()) { t = 0; return; }
        if (t++ < delay.getInt()) return;
        int s = Util.hotbar(st -> st.isOf(Items.FIREWORK_ROCKET));
        if (s == -1) return;
        t = 0;
        Util.use(s);
    }
}
