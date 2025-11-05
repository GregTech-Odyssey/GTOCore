package com.gtocore.mixin.botania;

import com.gtolib.api.blockentity.IObserved;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

@Mixin(SpecialFlowerBlockEntity.class)
public class SpecialFlowerBlockEntityMixin extends BlockEntity implements IObserved {

    @Unique
    private int gtocore$lastSync;

    @Unique
    private boolean gtocore$observed;

    public SpecialFlowerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "sync", at = @At("HEAD"), remap = false, cancellable = true)
    private void sync(CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            var tick = serverLevel.getServer().getTickCount();
            if (tick - gtocore$lastSync > (gtocore$observed ? 5 : 200)) {
                gtocore$observed = false;
                gtocore$lastSync = tick;
                return;
            }
        }
        ci.cancel();
    }

    @Override
    public void onObserved() {
        gtocore$observed = true;
    }
}
