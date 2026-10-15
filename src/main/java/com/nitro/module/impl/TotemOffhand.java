package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TotemOffhand extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 1, 0, 20, 1));
    private int t;

    public TotemOffhand() { super("TotemOffhand", "Swaps a hotbar totem into the offhand without opening your inventory.", Category.COMBAT); }

    @Override public void onTick() {
        var p = mc.player;
        var net = mc.getNetworkHandler();
        if (p == null || net == null) return;
        if (Util.isTotem(p.getOffHandStack())) { t = 0; return; }
        if (t++ < delay.getInt()) return;
        int h = Util.hotbar(Util::isTotem);
        if (h == -1) return;
        t = 0;
        int cur = Util.selected();
        net.sendPacket(new UpdateSelectedSlotC2SPacket(h));
        net.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        net.sendPacket(new UpdateSelectedSlotC2SPacket(cur));
    }
}
