package com.gtocore.mixin.farmersdelight;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;

@Mixin(CuttingBoardBlockEntity.class)
public abstract class CuttingBoardBlockEntityMixin extends SyncedBlockEntity {

    public CuttingBoardBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(method = "removeItem", at = @At("RETURN"), remap = false)
    public void removeItem(CallbackInfoReturnable<ItemStack> cir) {
        if (cir.getReturnValue().getItem() != ModItems.ONION.get()) return;
        if (GTValues.RNG.nextFloat() > 0.05) return;
        if (!Arrays.toString(Thread.currentThread().getStackTrace()).contains("processStoredItemUsingTool")) return;
        var pos = worldPosition.below();
        var block = level.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN) level.setBlockAndUpdate(pos, Blocks.CRYING_OBSIDIAN.defaultBlockState());
    }
}
