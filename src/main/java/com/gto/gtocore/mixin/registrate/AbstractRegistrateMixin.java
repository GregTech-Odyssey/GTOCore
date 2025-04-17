package com.gto.gtocore.mixin.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractRegistrate.class)
public class AbstractRegistrateMixin {

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;debug(Lorg/apache/logging/log4j/Marker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private void acceptLoggerDebug(org.apache.logging.log4j.Logger logger, org.apache.logging.log4j.Marker marker, String format, Object arg1, Object arg2, Object arg3) {}
}
