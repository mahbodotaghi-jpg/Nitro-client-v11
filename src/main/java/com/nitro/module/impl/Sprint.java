package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", "Sprints whenever you move forward.", Category.MISC); }

    @Override public void onTick() {
        var p = mc.player;
        if (p != null && p.input.movementForward > 0 && !p.horizontalCollision && !p.isSneaking()
                && p.getHungerManager().getFoodLevel() > 6) p.setSprinting(true);
    }
}
