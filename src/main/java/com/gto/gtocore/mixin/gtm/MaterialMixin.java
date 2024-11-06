package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.client.renderer.item.TagPrefixColor;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Material.class)
public class MaterialMixin {

    @Shadow(remap = false)
    public String getName() {
        return null;
    }

    @Inject(method = "getMaterialRGB()I", at = @At("HEAD"), remap = false, cancellable = true)
    public void getMaterialRGB(CallbackInfoReturnable<Integer> cir) {
        if (TagPrefixColor.MaterialColors.containsKey(getName())) {
            cir.setReturnValue(TagPrefixColor.MaterialColors.get(getName()).get());
        }
    }

    @Inject(method = "getMaterialARGB(I)I", at = @At("HEAD"), remap = false, cancellable = true)
    public void getMaterialARGB(CallbackInfoReturnable<Integer> cir) {
        if (TagPrefixColor.MaterialColors.containsKey(getName())) {
            cir.setReturnValue(TagPrefixColor.MaterialColors.get(getName()).get());
        }
    }
}
