package com.gtocore.mixin.ae2.crafting;

import com.gtocore.integration.ae.PatternContainerGroupHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import appeng.api.implementations.blockentities.PatternContainerGroup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternContainerGroup.class, remap = false)
public class PatternContainerGroupMixin {

    @Inject(method = "fromMachine", at = @At("HEAD"), cancellable = true)
    private static void gto$fromMachine(Level level, BlockPos pos, Direction side,
                                        CallbackInfoReturnable<PatternContainerGroup> cir) {
        PatternContainerGroup group = PatternContainerGroupHelper.fromMachine(level, pos, "");
        if (group != null) {
            cir.setReturnValue(group);
        }
    }
}
