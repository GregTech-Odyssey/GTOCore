package com.gtocore.mixin.ae2.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.blockentity.grid.AENetworkBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingBlockEntity.class)
public abstract class CraftingBlockEntityMixin extends AENetworkBlockEntity {

    @Shadow(remap = false)
    public abstract AbstractCraftingUnitBlock<?> getUnitBlock();

    public CraftingBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected Item getItemFromBlockEntity() {
        return getUnitBlock().type.getItemFromType();
    }
}
