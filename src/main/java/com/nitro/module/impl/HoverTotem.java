package com.nitro.module.impl;

import com.nitro.mixin.HandledScreenAccessor;
import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class HoverTotem extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 20, 1));
    private int t;

    public HoverTotem() { super("HoverTotem", "Hover a totem in any container to send it to the offhand.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (!(mc.currentScreen instanceof HandledScreen<?> hs)) { t = 0; return; }
        Slot s = ((HandledScreenAccessor) (Object) hs).getFocusedSlot();
        if (s == null || !Util.isTotem(s.getStack()) || Util.isTotem(p.getOffHandStack())) return;
        if (t++ < delay.getInt()) return;
        t = 0;
        mc.interactionManager.clickSlot(hs.getScreenHandler().syncId, s.id, 40, SlotActionType.SWAP, p);
    }
}
