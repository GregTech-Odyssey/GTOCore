package com.gto.gtocore.mixin.gtm.chemical;

import com.gto.gtocore.api.data.chemical.material.GTOMaterial;
import com.gto.gtocore.client.renderer.item.MaterialsColorMap;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.world.item.Rarity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Material.class)
public class MaterialMixin implements GTOMaterial {

    @Unique
    private long gTOCore$mass;

    @Unique
    private Rarity gtocore$rarity;

    @Override
    public Rarity gtocore$rarity() {
        return gtocore$rarity;
    }

    @Override
    public void gtocore$setRarity(Rarity rarity) {
        this.gtocore$rarity = rarity;
    }

    @Inject(method = "getMass", at = @At("HEAD"), remap = false, cancellable = true)
    private void gmass(CallbackInfoReturnable<Long> cir) {
        if (gTOCore$mass > 0) cir.setReturnValue(gTOCore$mass);
    }

    @Inject(method = "getMass", at = @At("RETURN"), remap = false)
    private void smass(CallbackInfoReturnable<Long> cir) {
        if (gTOCore$mass == 0) gTOCore$mass = cir.getReturnValue();
    }

    @Inject(method = "getMaterialRGB()I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialRGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide() && MaterialsColorMap.MaterialColors.containsKey(this)) {
            cir.setReturnValue(MaterialsColorMap.MaterialColors.get(this).get());
        }
    }

    @Inject(method = "getMaterialARGB(I)I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialARGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide() && MaterialsColorMap.MaterialColors.containsKey(this)) {
            cir.setReturnValue(MaterialsColorMap.MaterialColors.get(this).get());
        }
    }
}
