package com.gtocore.mixin.gtm.chemical;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.registry.registrate.BuilderBase;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Material.Builder.class)
public abstract class MaterialBuilderMixin extends BuilderBase<Material> {

    @Unique
    private static final Set<String> gtolib$TOOL = Set.of(
            "flint",
            "wood",
            "rubber",
            "polyethylene",
            "polytetrafluoroethylene",
            "silicone_rubber",
            "styrene_butadiene_rubber",
            "polybenzimidazole",
            "wrought_iron",
            "steel",
            "damascus_steel",
            "cobalt_brass",
            "vanadium_steel",
            "tungsten_steel",
            "hsse",
            "naquadah_alloy",
            "duranium",
            "neutronium");

    protected MaterialBuilderMixin(ResourceLocation id) {
        super(id);
    }

    @Inject(method = "toolStats", at = @At("HEAD"), remap = false, cancellable = true)
    private void toolStats(ToolProperty toolProperty, CallbackInfoReturnable<Material.Builder> cir) {
        if (!id.getNamespace().equals(GTOCore.MOD_ID) && !gtolib$TOOL.contains(id.getPath())) cir.setReturnValue((Material.Builder) (Object) this);
    }
}
