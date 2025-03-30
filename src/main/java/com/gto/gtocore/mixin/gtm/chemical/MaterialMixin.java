package com.gto.gtocore.mixin.gtm.chemical;

import com.gto.gtocore.api.data.chemical.material.GTOMaterial;
import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet;
import com.gto.gtocore.client.renderer.item.MaterialsColorMap;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;

import net.minecraft.world.item.Rarity;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(Material.class)
public abstract class MaterialMixin implements GTOMaterial {

    @Shadow(remap = false)
    @Final
    private @NotNull MaterialProperties properties;

    @Shadow(remap = false)
    public abstract ImmutableList<MaterialStack> getMaterialComponents();

    @Unique
    private long gTOCore$mass;

    @Unique
    private int gTOCore$temp;

    @Unique
    private Rarity gtocore$rarity;

    @Unique
    private boolean gtocore$glow;

    @Override
    public Rarity gtocore$rarity() {
        return gtocore$rarity;
    }

    @Override
    public void gtocore$setRarity(Rarity rarity) {
        this.gtocore$rarity = rarity;
    }

    @Override
    public boolean gtocore$glow() {
        return gtocore$glow;
    }

    @Override
    public void gtocore$setGlow() {
        this.gtocore$glow = true;
    }

    @Override
    public MaterialProperties gtocore$getProperties() {
        return properties;
    }

    @Override
    public int gtocore$temp() {
        return gTOCore$temp;
    }

    @Override
    public void gtocore$setTemp(int temp) {
        gTOCore$temp = temp;
    }

    @Inject(method = "getMaterialIconSet", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialIconSet(CallbackInfoReturnable<MaterialIconSet> cir) {
        if ((Object) this == GTOMaterials.Neutron) {
            cir.setReturnValue(GTOMaterialIconSet.NEUTRONIUM);
        }
    }

    @Inject(method = "getMass", at = @At("HEAD"), remap = false, cancellable = true)
    private void gmass(CallbackInfoReturnable<Long> cir) {
        if (getMaterialComponents() == null || gTOCore$mass > 0) cir.setReturnValue(gTOCore$mass);
    }

    @Inject(method = "getMass", at = @At("RETURN"), remap = false, cancellable = true)
    private void smass(CallbackInfoReturnable<Long> cir) {
        if (gTOCore$mass == 0) {
            gTOCore$mass = Math.max(10, Math.min(Integer.MAX_VALUE, cir.getReturnValue()));
            cir.setReturnValue(gTOCore$mass);
        }
    }

    @Inject(method = "getMaterialRGB()I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialRGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide()) {
            Supplier<Integer> supplier = MaterialsColorMap.MaterialColors.get(this);
            if (supplier == null) return;
            cir.setReturnValue(supplier.get());
        }
    }

    @Inject(method = "getMaterialARGB(I)I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialARGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide()) {
            Supplier<Integer> supplier = MaterialsColorMap.MaterialColors.get(this);
            if (supplier == null) return;
            cir.setReturnValue(supplier.get());
        }
    }
}
