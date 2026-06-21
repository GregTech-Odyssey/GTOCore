package com.gtocore.mixin.modernfix;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import org.embeddedt.modernfix.render.SimpleItemModelView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SimpleItemModelView.class)
public abstract class SimpleItemModelViewMixin {

    @Shadow(remap = false)
    protected abstract boolean isCorrectDirectionForType(Direction direction);

    @Redirect(method = "getQuads", at = @At(value = "INVOKE", target = "Lorg/embeddedt/modernfix/render/SimpleItemModelView;isCorrectDirectionForType(Lnet/minecraft/core/Direction;)Z", ordinal = 0, remap = false))
    private boolean isCorrectDirectionForType(SimpleItemModelView instance, Direction direction, @Local(argsOnly = true) BlockState state) {
        return (state != null && state.getBlock() instanceof MetaMachineBlock) || isCorrectDirectionForType(direction);
    }
}
