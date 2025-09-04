package com.gtocore.mixin.gtm.chemical;

import com.gtocore.client.renderer.item.MaterialsColorMap;

import com.gtolib.api.data.chemical.material.GTOMaterial;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;

import net.minecraft.world.item.Rarity;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.IntSupplier;

@Mixin(Material.class)
public abstract class MaterialMixin implements GTOMaterial {

    @Shadow(remap = false)
    @Final
    private @NotNull MaterialProperties properties;

    @Unique
    private int gtolib$temp;

    @Unique
    private Rarity gtolib$rarity;

    @Unique
    private boolean gtolib$glow;

    @Override
    public Rarity gtolib$rarity() {
        return gtolib$rarity;
    }

    @Override
    public void gtolib$setRarity(Rarity rarity) {
        this.gtolib$rarity = rarity;
    }

    @Override
    public boolean gtolib$glow() {
        return gtolib$glow;
    }

    @Override
    public void gtolib$setGlow() {
        this.gtolib$glow = true;
    }

    @Override
    public MaterialProperties gtolib$getProperties() {
        return properties;
    }

    @Override
    public int gtolib$temp() {
        return gtolib$temp;
    }

    @Override
    public void gtolib$setTemp(int temp) {
        gtolib$temp = temp;
    }

    @Inject(method = "getMaterialRGB()I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialRGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide()) {
            IntSupplier supplier = MaterialsColorMap.MaterialColors.get(this);
            if (supplier == null) return;
            cir.setReturnValue(supplier.getAsInt());
        }
    }

    @Inject(method = "getMaterialARGB(I)I", at = @At("HEAD"), remap = false, cancellable = true)
    private void getMaterialARGB(CallbackInfoReturnable<Integer> cir) {
        if (GTCEu.isClientSide()) {
            IntSupplier supplier = MaterialsColorMap.MaterialColors.get(this);
            if (supplier == null) return;
            cir.setReturnValue(supplier.getAsInt());
        }
    }
}
