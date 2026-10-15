package com.nitro.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import java.util.function.Predicate;

public final class Util {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static int restore = -1;

    private Util() {}

    public static int hotbar(Predicate<ItemStack> f) {
        for (int i = 0; i < 9; i++) if (f.test(mc.player.getInventory().getStack(i))) return i;
        return -1;
    }
    /** Inventory index 0..35 (0-8 hotbar). */
    public static int inv(Predicate<ItemStack> f) {
        for (int i = 0; i < 36; i++) if (f.test(mc.player.getInventory().getStack(i))) return i;
        return -1;
    }
    /** Slot id inside the player screen handler. */
    public static int menuSlot(int invIndex) { return invIndex < 9 ? 36 + invIndex : invIndex; }

    public static int selected() { return mc.player.getInventory().selectedSlot; }
    public static void select(int s) { mc.player.getInventory().selectedSlot = s; }

    /** Select a slot for this tick only; it is restored at the start of the next tick. */
    public static void temp(int s) { if (restore == -1) restore = selected(); select(s); }
    public static void preTick() {
        if (restore != -1 && mc.player != null) { select(restore); restore = -1; }
    }

    public static void use(int hotbarSlot) {
        temp(hotbarSlot);
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
    }
    public static void attack(Entity e) {
        mc.interactionManager.attackEntity(mc.player, e);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
    public static boolean isTotem(ItemStack s) { return s.isOf(Items.TOTEM_OF_UNDYING); }

    /** A hotbar slot whose item is not glowstone (an empty slot counts). Prefers the current slot. */
    public static int nonGlowstone() {
        if (!mc.player.getInventory().getStack(selected()).isOf(Items.GLOWSTONE)) return selected();
        return hotbar(s -> !s.isOf(Items.GLOWSTONE));
    }
}
