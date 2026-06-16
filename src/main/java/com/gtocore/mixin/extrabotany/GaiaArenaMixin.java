package com.gtocore.mixin.extrabotany;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import io.github.lounode.extrabotany.api.gaia.GaiaArena;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GaiaArena.class)
public class GaiaArenaMixin {

    @Inject(method = "checkFeasibility", at = @At("HEAD"), cancellable = true, remap = false)
    private static void gtocore$whitelistMythicBotany(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isEmpty()) {
            return;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if ("mythicbotany".equals(id.getNamespace())) {
            cir.setReturnValue(true);
        }
    }
}
