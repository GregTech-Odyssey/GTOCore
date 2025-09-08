package com.gtocore.mixin.ae2.stacks;

import com.gtocore.api.ae2.AE2Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.stacks.AEKeyType;
import appeng.parts.automation.StackWorldBehaviors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(StackWorldBehaviors.class)
public class StackWorldBehaviorsMixin {

    @Inject(at = @At("RETURN"), method = "createExternalStorageStrategies", cancellable = true, remap = false)
    private static void createExternalStorageStrategies(ServerLevel level, BlockPos fromPos, Direction fromSide, CallbackInfoReturnable<Map<AEKeyType, ExternalStorageStrategy>> cir) {
        cir.setReturnValue(AE2Utils.AEKeyTypeMapBetter(cir.getReturnValue()));
    }
}
