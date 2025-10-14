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
            try {
                // 1. 使用字符串获取 Class 对象，避免直接引用
                Class<?> clientHandlerClass = Class.forName("com.gtocore.client.ClientSideHandler");

                // 2. 使用字符串获取 Method 对象
                Method clientInitMethod = clientHandlerClass.getMethod("runClientSideInitialization");

                // 3. 调用静态方法
                clientInitMethod.invoke(null); // 第一个参数是 null 因为方法是静态的

            } catch (Exception ignored) {}
        }
    }
}
