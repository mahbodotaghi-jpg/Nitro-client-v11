package com.nitro.module.impl;

import com.nitro.module.Category;
import com.nitro.module.Module;

public class NameProtect extends Module {
    public static NameProtect INSTANCE;

    public NameProtect() {
        super("NameProtect", "Hides your username in all rendered text (chat, tab, nametags).", Category.RENDER);
        INSTANCE = this;
    }

    public static String replace(String s) {
        if (INSTANCE == null || !INSTANCE.isEnabled() || s == null) return s;
        var mc = net.minecraft.client.MinecraftClient.getInstance();
        String n = mc.getSession().getUsername();
        return n == null || n.isEmpty() || !s.contains(n) ? s : s.replace(n, "Protected");
    }
}
