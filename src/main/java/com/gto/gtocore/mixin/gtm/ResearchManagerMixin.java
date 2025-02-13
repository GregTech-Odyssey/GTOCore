package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.init.GTOItems;

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
        if (cwut > 512) cir.setReturnValue(GTOItems.QUANTUM_DISK.asStack());
    }
}
