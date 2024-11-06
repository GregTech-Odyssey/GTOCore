package com.gto.gtocore.mixin.gtm.gui;

import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.GTRecipeWidget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BooleanSupplier;

@Mixin(GTRecipeWidget.class)
public class GTRecipeWidgetMixin {

    @ModifyArg(method = "addButtons", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/gui/widget/PredicatedButtonWidget;<init>(IIIILcom/lowdragmc/lowdraglib/gui/texture/IGuiTexture;Ljava/util/function/Consumer;Ljava/util/function/BooleanSupplier;Z)V"), index = 6, remap = false)
    public BooleanSupplier booleanSupplier(BooleanSupplier predicate) {
        return () -> ConfigHolder.INSTANCE.dev.debug;
    }

    @ModifyArg(method = "addButtons", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/gui/widget/PredicatedButtonWidget;<init>(IIIILcom/lowdragmc/lowdraglib/gui/texture/IGuiTexture;Ljava/util/function/Consumer;Ljava/util/function/BooleanSupplier;Z)V"), index = 7, remap = false)
    public boolean aBoolean(boolean defaultVisibility) {
        return ConfigHolder.INSTANCE.dev.debug;
    }
}
