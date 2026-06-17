package com.gtocore.mixin.botania;

import com.gtolib.api.blockentity.IObserved;

import com.gregtechceu.gtceu.core.ILevel;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
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
    private long gtocore$lastLazyTick;

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

    @Inject(method = "commonTick", at = @At("HEAD"), remap = false, cancellable = true)
    private static void cancelCommonTick(Level level, BlockPos worldPosition, BlockState state, SpecialFlowerBlockEntity self, CallbackInfo ci) {
        var self1 = (SpecialFlowerBlockEntityMixin) (Object) self;
        if (self1 != null) {
            self1.gtocore$lastLazyTick = level.getGameTime();
            if (self1.gtolib$isPosInCache(self1.getBlockPos()) && self1.gtocore$lastLazyTick % 20 != 9) {
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean gtolib$isPosInCache(BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            var cache = ((ILevel) serverLevel).gtceu$getMultiblockWorldSavedData();
            if (cache != null) {
                var states = cache.getControllersInChunk(serverLevel.getChunkAt(pos).getPos().toLong());
                if (states != null) {
                    var p = pos.asLong();
                    for (var structure : states) {
                        if (structure.blockEntityCache.contains(p)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onObserved() {
        gtocore$observed = true;
    }
}
