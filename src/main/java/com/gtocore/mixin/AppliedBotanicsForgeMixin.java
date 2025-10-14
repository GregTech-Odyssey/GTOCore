package com.gtocore.mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLLoader;

import appbot.forge.AppliedBotanicsForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Method;
import java.util.function.Supplier;

@Mixin(value = AppliedBotanicsForge.class, remap = false)
public abstract class AppliedBotanicsForgeMixin {

    @Redirect(
              method = "<init>",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraftforge/fml/DistExecutor;safeRunWhenOn(Lnet/minecraftforge/api/distmarker/Dist;Ljava/util/function/Supplier;)V"))
    private void injectAndReplaceClientInit(Dist dist, Supplier<DistExecutor.SafeRunnable> toRun) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            appbot.forge.client.AppliedBotanicsClient.initialize();
        }
    }
}
