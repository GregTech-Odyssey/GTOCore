package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TagPrefix.class)
public abstract class TagPrefixMixin {

    @Shadow(remap = false)
    @Final
    public String name;

    @Inject(method = "doGenerateItem(Lcom/gregtechceu/gtceu/api/data/chemical/material/Material;)Z", at = @At("RETURN"), remap = false, cancellable = true)
    public void doGenerateItem(Material material, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            String tagPrefix = this.name;
            switch (tagPrefix) {
                case "crushedOre":
                    cir.setReturnValue(!material.hasFlag(GTOMaterialFlags.DISABLE_CRUSHED));
                    break;
                case "purifiedOre":
                    cir.setReturnValue(!material.hasFlag(GTOMaterialFlags.DISABLE_CRUSHED_PURIFIED));
                    break;
                case "refinedOre":
                    cir.setReturnValue(!material.hasFlag(GTOMaterialFlags.DISABLE_CRUSHED_REFINED));
                    break;
                case "impureDust":
                    cir.setReturnValue(!material.hasFlag(GTOMaterialFlags.DISABLE_DUST_IMPURE));
                    break;
                case "pureDust":
                    cir.setReturnValue(!material.hasFlag(GTOMaterialFlags.DISABLE_DUST_PURE));
                    break;
            }
        }
    }
}
