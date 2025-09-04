package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import com.gregtechceu.gtceu.utils.cache.BlockEntityDirectionCache;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.blockentity.AEBaseBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEBaseBlockEntity.class)
public class AEBaseBlockEntityMixin extends BlockEntity implements IDirectionCacheBlockEntity {

    @Unique
    private BlockEntityDirectionCache gtolib$directionCache;

    public AEBaseBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(BlockEntityType blockEntityType, BlockPos pos, BlockState blockState, CallbackInfo ci) {
        gtolib$directionCache = BlockEntityDirectionCache.create();
    }

    @Override
    public BlockEntityDirectionCache gtolib$getDirectionCache() {
        return gtolib$directionCache;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        gtolib$directionCache.clearCache();
    }

    @Override
    public void clearRemoved() {
        gtolib$directionCache.clearCache();
        super.clearRemoved();
    }
}
