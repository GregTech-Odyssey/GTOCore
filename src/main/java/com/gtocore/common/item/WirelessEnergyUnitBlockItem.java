package com.gtocore.common.item;

import com.gtocore.common.block.WirelessEnergyUnitBlock;
import com.gtocore.common.machine.multiblock.storage.WirelessEnergySubstationMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import org.jetbrains.annotations.NotNull;

public class WirelessEnergyUnitBlockItem extends BlockItem {

    public WirelessEnergyUnitBlockItem(WirelessEnergyUnitBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull WirelessEnergyUnitBlock getBlock() {
        return (WirelessEnergyUnitBlock) super.getBlock();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isCrouching()) {
            if (stack.getItem() instanceof WirelessEnergyUnitBlockItem blockItem &&
                    context.getLevel() instanceof ServerLevel serverLevel &&
                    context.getPlayer() instanceof ServerPlayer serverPlayer &&
                    serverLevel.getBlockEntity(context.getClickedPos()) instanceof MetaMachineBlockEntity mbe &&
                    mbe.getMetaMachine() instanceof WirelessEnergySubstationMachine machine) {
                var substituted = machine.substituteBlocks(blockItem.getBlock(), stack.getCount(), serverPlayer);
                if (substituted > 0) {
                    stack.shrink(substituted);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.onItemUseFirst(stack, context);
    }
}
