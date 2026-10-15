package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import net.minecraft.text.Text;

public class AutoLog extends Module {
    private final NumberSetting health = add(new NumberSetting("Health (half hearts)", 6, 1, 19, 1));

    public AutoLog() { super("AutoLog", "Disconnects when your health drops to the threshold.", Category.MISC); }

    @Override public void onTick() {
        var p = mc.player;
        var net = mc.getNetworkHandler();
        if (p == null || net == null) return;
        float hp = p.getHealth() + p.getAbsorptionAmount();
        if (hp <= health.get()) {
            net.getConnection().disconnect(Text.literal("[Nitro] AutoLog at " + hp + " health"));
            setEnabled(false);
        }
    }
}
