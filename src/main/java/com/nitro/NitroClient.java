package com.nitro;

import com.nitro.gui.ClickGui;
import com.nitro.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NitroClient implements ClientModInitializer {
    public static ModuleManager modules;
    private static KeyBinding openGui;

    @Override public void onInitializeClient() {
        modules = new ModuleManager();
        openGui = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.nitro.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.nitro"));
        ClientTickEvents.START_CLIENT_TICK.register(mc -> modules.preTick());
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            modules.tick();
            while (openGui.wasPressed()) mc.setScreen(new ClickGui());
        });
    }
}
