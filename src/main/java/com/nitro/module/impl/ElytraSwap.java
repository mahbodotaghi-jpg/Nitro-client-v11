package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.util.Util;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class ElytraSwap extends Module {
    public ElytraSwap() { super("ElytraSwap", "Swaps between chestplate and elytra, then turns off.", Category.COMBAT); }

    private static boolean isChest(ItemStack s) {
        EquippableComponent e = s.get(DataComponentTypes.EQUIPPABLE);
        return e != null && e.slot() == EquipmentSlot.CHEST;
    }

    @Override public void onEnable() {
        var p = mc.player;
        if (p != null && mc.interactionManager != null) {
            boolean wearing = p.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
            int i = Util.inv(s -> wearing ? isChest(s) && !s.isOf(Items.ELYTRA) : s.isOf(Items.ELYTRA));
            if (i != -1) {
                int id = p.playerScreenHandler.syncId, slot = Util.menuSlot(i);
                mc.interactionManager.clickSlot(id, slot, 0, SlotActionType.PICKUP, p);
                mc.interactionManager.clickSlot(id, 6, 0, SlotActionType.PICKUP, p);
                mc.interactionManager.clickSlot(id, slot, 0, SlotActionType.PICKUP, p);
            }
        }
        setEnabled(false);
    }
}
