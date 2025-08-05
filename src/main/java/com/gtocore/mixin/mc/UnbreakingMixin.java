package com.gtocore.mixin.mc;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;

import committee.nova.mods.avaritia.common.item.misc.InfinityTotemItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DigDurabilityEnchantment.class)
public class UnbreakingMixin {

    @Inject(method = "shouldIgnoreDurabilityDrop", at = @At("HEAD"), cancellable = true)
    private static void gto$shouldIgnoreDurabilityDrop(ItemStack stack, int level, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof InfinityTotemItem && random.nextFloat() < 0.6F) {
            cir.setReturnValue(false);
        }
    }
}
