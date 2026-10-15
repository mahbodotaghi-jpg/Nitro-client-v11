package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.ActionResult;
import java.util.ArrayList;
import java.util.List;

public class CrystalOptimizer extends Module {
    private final List<Entity> pending = new ArrayList<>();

    public CrystalOptimizer() {
        super("CrystalOptimizer", "Removes a crystal client-side the moment you hit it.", Category.COMBAT);
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (isEnabled() && world.isClient && entity instanceof EndCrystalEntity) pending.add(entity);
            return ActionResult.PASS;
        });
    }

    @Override public void onTick() {
        if (mc.world == null) { pending.clear(); return; }
        for (Entity e : pending) mc.world.removeEntity(e.getId(), Entity.RemovalReason.KILLED);
        pending.clear();
    }
}
