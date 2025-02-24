package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResearchManager.class)
public class ResearchManagerMixin {

    @Inject(method = "getDefaultResearchStationItem", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getDefaultResearchStationItem(int cwut, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = null;
        if (cwut > 2000000000) {
            stack = GTOItems.MICROCOSM.asStack();
        } else if (cwut > 4194304) {
            stack = GTOItems.OBSIDIAN_MATRIX.asStack();
        } else if (cwut > 131072) {
            stack = GTOItems.ATOMIC_ARCHIVES.asStack();
        } else if (cwut > 2048) {
            stack = GTOItems.NEURAL_MATRIX.asStack();
        } else if (cwut > 256) {
            stack = GTOItems.QUANTUM_DISK.asStack();
        }
        if (stack == null) return;
        cir.setReturnValue(stack);
    }
}
