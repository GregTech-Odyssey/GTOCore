package com.gtocore.mixin.arseng;

import net.minecraftforge.common.util.LazyOptional;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.injection.At;

@org.spongepowered.asm.mixin.Mixin(gripe._90.arseng.block.entity.SourceAcceptorBlockEntity.class)
public class SourceAcceptorBlockEntityMixin {

    @WrapOperation(method = "invalidateCaps", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/util/LazyOptional;invalidate()V"), remap = false)
    private void gto$fixNullPointer(LazyOptional instance, Operation<Void> original) {
        if (instance != null) {
            original.call(instance);
        }
    }
}
