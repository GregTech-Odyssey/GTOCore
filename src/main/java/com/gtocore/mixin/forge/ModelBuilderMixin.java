package com.gtocore.mixin.forge;

import net.minecraftforge.client.model.generators.ModelBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModelBuilder.class, remap = false)
public class ModelBuilderMixin {

    @Redirect(method = "texture(Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraftforge/client/model/generators/ModelBuilder;",
              at = @At(value = "INVOKE", target = "Lcom/google/common/base/Preconditions;checkArgument(ZLjava/lang/String;Ljava/lang/Object;)V"))
    private void dontCheck(boolean b, String errorMessageTemplate, Object p1) {
        if (!b) System.out.printf((errorMessageTemplate) + "%n", p1);
    }
}
