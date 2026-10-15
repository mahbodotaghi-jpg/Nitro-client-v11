package com.nitro.module;

import com.nitro.module.impl.*;
import com.nitro.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> modules = new ArrayList<>();
    private final boolean[] wasDown = new boolean[512];

    public ModuleManager() {
        // Combat
        modules.add(new AnchorMacro());
        modules.add(new AutoHitCrystal());
        modules.add(new AutoInvTotem());
        modules.add(new AutoJumpReset());
        modules.add(new AutoTotem());
        modules.add(new CrystalOptimizer());
        modules.add(new DoubleAnchor());
        modules.add(new ElytraSwap());
        modules.add(new Hitbox());
        modules.add(new HoverTotem());
        modules.add(new TotemOffhand());
        modules.add(new MaceSwap());
        modules.add(new ShieldBreaker());
        modules.add(new TriggerBot());
        // Render
        modules.add(new Freecam());
        modules.add(new NameProtect());
        // Misc
        modules.add(new AutoEat());
        modules.add(new AutoFirework());
        modules.add(new AutoLog());
        modules.add(new AutoLoot());
        modules.add(new AutoMine());
        modules.add(new AutoTool());
        modules.add(new ElytraGlide());
        modules.add(new FastPlace());
        modules.add(new Sprint());
        // Donut: intentionally empty
    }

    public List<Module> in(Category c) {
        return modules.stream().filter(m -> m.category == c).toList();
    }

    public void preTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        Util.preTick();
        for (Module m : modules) if (m.isEnabled()) m.onPreTick();
    }

    public void tick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        long handle = mc.getWindow().getHandle();
        for (Module m : modules) {
            if (m.key > 0 && m.key < wasDown.length && mc.currentScreen == null) {
                boolean down = InputUtil.isKeyPressed(handle, m.key);
                if (down && !wasDown[m.key]) m.toggle();
                wasDown[m.key] = down;
            }
            if (m.isEnabled()) m.onTick();
        }
    }
}
