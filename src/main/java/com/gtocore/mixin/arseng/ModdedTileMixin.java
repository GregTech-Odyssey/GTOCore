package com.gtocore.mixin.arseng;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import gripe._90.arseng.block.entity.MESourceJarBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ MESourceJarBlockEntity.class })
public class ModdedTileMixin extends BlockEntity {

    public ModdedTileMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @WrapMethod(method = "updateBlock", remap = false)
    private boolean gto$fixChunk(Operation<Boolean> original) {
        if (this.level != null && this.level.isLoaded(this.getBlockPos())) return original.call();
        return false;
    }
}
