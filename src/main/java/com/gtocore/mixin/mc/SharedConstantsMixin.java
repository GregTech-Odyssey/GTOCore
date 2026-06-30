package com.gtocore.mixin.mc;

import net.minecraft.SharedConstants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {

    @Inject(method = "isAllowedChatCharacter", at = @At("RETURN"), cancellable = true)
    private static void isAllowedCharacter(char character, CallbackInfoReturnable<Boolean> returnInfo) {
        returnInfo.setReturnValue(character >= ' ' && character != 127);
        returnInfo.cancel();
    }
}
