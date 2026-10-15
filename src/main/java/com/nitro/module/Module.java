package com.nitro.module;

import com.nitro.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public final String name, description;
    public final Category category;
    public final List<NumberSetting> settings = new ArrayList<>();
    public int key = -1;
    private boolean enabled;

    protected Module(String name, String description, Category category) {
        this.name = name; this.description = description; this.category = category;
    }

    public boolean isEnabled() { return enabled; }
    public void toggle() { setEnabled(!enabled); }
    public void setEnabled(boolean e) {
        if (e == enabled) return;
        enabled = e;
        if (e) onEnable(); else onDisable();
    }
    protected <T extends NumberSetting> T add(T s) { settings.add(s); return s; }

    public void onEnable() {}
    public void onDisable() {}
    /** Runs at the start of every client tick (before input is processed). */
    public void onPreTick() {}
    /** Runs at the end of every client tick. */
    public void onTick() {}
}
