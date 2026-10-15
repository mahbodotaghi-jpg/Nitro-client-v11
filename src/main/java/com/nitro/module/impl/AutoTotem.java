package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 20, 1));
    private int timer;

    public AutoTotem() { super("AutoTotem", "Keeps a totem in your offhand.", Category.COMBAT); }

    @Override public void onEnable() { timer = 0; }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (Util.isTotem(p.getOffHandStack())) { timer = 0; return; }
        if (timer++ < delay.getInt()) return;
        int i = Util.inv(Util::isTotem);
        if (i == -1) return;
        mc.interactionManager.clickSlot(p.playerScreenHandler.syncId, Util.menuSlot(i), 40, SlotActionType.SWAP, p);
        timer = 0;
    }
}
