package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class AutoLoot extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 10, 1));
    private int t;

    public AutoLoot() { super("AutoLoot", "Shift-clicks everything out of an open chest or shulker.", Category.MISC); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (!(mc.currentScreen instanceof HandledScreen<?> hs)) { t = 0; return; }
        ScreenHandler h = hs.getScreenHandler();
        if (!(h instanceof GenericContainerScreenHandler) && !(h instanceof ShulkerBoxScreenHandler)) return;
        if (t++ < delay.getInt()) return;
        t = 0;
        int n = h.slots.size() - 36;
        for (int i = 0; i < n; i++) {
            if (!h.getSlot(i).getStack().isEmpty()) {
                mc.interactionManager.clickSlot(h.syncId, i, 0, SlotActionType.QUICK_MOVE, p);
                return;
            }
        }
    }
}
