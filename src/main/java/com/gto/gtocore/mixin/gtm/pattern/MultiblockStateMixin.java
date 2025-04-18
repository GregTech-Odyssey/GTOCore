package com.gto.gtocore.mixin.gtm.pattern;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiblockState.class)
public class MultiblockStateMixin {

    @Inject(method = "onBlockStateChanged", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMultiController;checkPatternWithLock()Z"), remap = false, cancellable = true)
    private void onBlockStateChangedHook(BlockPos pos, BlockState state, CallbackInfo ci, @Local IMultiController controller) {
        // 当结构已经不成型时, 忽略后续的方块更新检查
        if (!controller.isFormed()) {
            ci.cancel();
        }
    }
}
