package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;

public class Hitbox extends Module {
    public static Hitbox INSTANCE;
    public final NumberSetting expand = add(new NumberSetting("Expand", 0.3, 0, 1, 0.05));

    public Hitbox() {
        super("Hitbox", "Enlarges other players' targeting hitbox (client-side aim).", Category.COMBAT);
        INSTANCE = this;
    }
}
