package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.config.GTOConfig;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 0)
public class MinecraftMixin {

    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void createTitle(CallbackInfoReturnable<String> ci) {
        if (GTOConfig.INSTANCE != null) {
            ci.setReturnValue("GregTech Odyssey [" + (GTOConfig.INSTANCE.dev ? "Dev" : GTOConfig.INSTANCE.gameDifficulty) + " Mode]");
        } else {
            ci.setReturnValue("GregTech Odyssey");
        }
    }
}
