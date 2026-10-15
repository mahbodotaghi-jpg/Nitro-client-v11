package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import com.nitro.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoEat extends Module {
    private final NumberSetting hunger = add(new NumberSetting("Eat below", 14, 1, 19, 1));
    private boolean eating;
    private int prev = -1;

    public AutoEat() { super("AutoEat", "Eats hotbar food when hunger drops below the threshold.", Category.MISC); }

    private static boolean good(ItemStack s) {
        return s.get(DataComponentTypes.FOOD) != null && !s.isOf(Items.CHORUS_FRUIT) && !s.isOf(Items.ROTTEN_FLESH)
                && !s.isOf(Items.SPIDER_EYE) && !s.isOf(Items.POISONOUS_POTATO) && !s.isOf(Items.PUFFERFISH);
    }

    private void stop() {
        mc.options.useKey.setPressed(false);
        if (prev != -1 && mc.player != null) Util.select(prev);
        prev = -1; eating = false;
    }

    @Override public void onDisable() { if (eating) stop(); }

    @Override public void onTick() {
        var p = mc.player;
        if (p == null) return;
        boolean hungry = p.getHungerManager().getFoodLevel() <= hunger.getInt();
        if (!eating) {
            if (!hungry || p.isUsingItem() || mc.currentScreen != null) return;
            int s = Util.hotbar(AutoEat::good);
            if (s == -1) return;
            prev = Util.selected(); Util.select(s); eating = true;
        }
        if (!hungry || !good(p.getMainHandStack()) || mc.currentScreen != null) { stop(); return; }
        mc.options.useKey.setPressed(true);
    }
}
