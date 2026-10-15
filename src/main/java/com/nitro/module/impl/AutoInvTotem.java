package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.SlotActionType;

public class AutoInvTotem extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 20, 1));
    private int t;

    public AutoInvTotem() { super("AutoInvTotem", "While your inventory is open, moves a totem to the offhand.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (!(mc.currentScreen instanceof InventoryScreen)) { t = 0; return; }
        if (Util.isTotem(p.getOffHandStack())) return;
        if (t++ < delay.getInt()) return;
        t = 0;
        int i = Util.inv(Util::isTotem);
        if (i == -1) return;
        mc.interactionManager.clickSlot(p.playerScreenHandler.syncId, Util.menuSlot(i), 40, SlotActionType.SWAP, p);
    }
}
