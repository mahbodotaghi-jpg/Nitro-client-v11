package com.nitro.mixin;

import com.nitro.module.impl.Hitbox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    private void nitro$margin(CallbackInfoReturnable<Float> cir) {
        Hitbox h = Hitbox.INSTANCE;
        if (h != null && h.isEnabled() && (Object) this instanceof PlayerEntity
                && (Object) this != MinecraftClient.getInstance().player) {
            cir.setReturnValue((float) h.expand.get());
        }
    }
}
