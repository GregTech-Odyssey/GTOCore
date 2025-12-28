package com.gtocore.mixin.pipez;

import de.maxhenkel.pipez.blocks.tileentity.PipeLogicTileEntity;
import de.maxhenkel.pipez.blocks.tileentity.types.FluidPipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidPipeType.class)
public class FluidPipeTypeMixin {

    @Inject(method = "tick", at = @At("HEAD"), remap = false, cancellable = true)
    private void tick(PipeLogicTileEntity tileEntity, CallbackInfo ci) {
        if (tileEntity.getLevel().getGameTime() % 20 != 10) ci.cancel();
    }
}
