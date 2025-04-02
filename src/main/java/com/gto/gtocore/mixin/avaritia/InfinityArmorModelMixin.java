package com.gto.gtocore.mixin.avaritia;

import committee.nova.mods.avaritia.client.model.InfinityArmorModel;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InfinityArmorModel.class)
public class InfinityArmorModelMixin {

    @Redirect(method = "renderToBuffer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/Object;)V", remap = false))
    private void renderToBuffer(Logger instance, Object object) {}
}
