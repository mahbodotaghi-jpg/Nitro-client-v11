package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class MaceSwap extends Module {
    private final NumberSetting minFall = add(new NumberSetting("Min fall (blocks)", 1.5, 0, 10, 0.5));

    public MaceSwap() {
        super("MaceSwap", "Swaps to the mace when you hit something while falling, then back.", Category.COMBAT);
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (isEnabled() && world.isClient && mc.player != null && mc.player.fallDistance >= minFall.get()) {
                int m = Util.hotbar(s -> s.isOf(Items.MACE));
                if (m != -1) Util.temp(m);
            }
            return ActionResult.PASS;
        });
    }
}
