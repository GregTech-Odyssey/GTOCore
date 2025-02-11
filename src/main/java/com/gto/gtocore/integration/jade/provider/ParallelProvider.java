package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Optional;

public final class ParallelProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("parallel")) {
            int parallel = blockAccessor.getServerData().getInt("parallel");
            if (parallel > 1) {
                Component parallels = Component.literal(FormattingUtil.formatNumbers(parallel)).withStyle(ChatFormatting.DARK_PURPLE);
                String key = "gtceu.multiblock.parallel";
                if (blockAccessor.getServerData().getBoolean("exact")) key += ".exact";
                iTooltip.add(Component.translatable(key, parallels));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity) {
            MetaMachine machine = blockEntity.getMetaMachine();
            int parallel = 0;
            if (machine instanceof IMultiController controller) {
                if (controller instanceof IParallelMachine parallelHatch) {
                    parallel = getRecipeParallel(controller);
                    if (parallel > 1) {
                        compoundTag.putBoolean("exact", true);
                    } else {
                        parallel = parallelHatch.getParallel();
                    }
                } else {
                    Optional<IParallelHatch> parallelHatch = controller.getParallelHatch();
                    if (parallelHatch.isPresent()) {
                        parallel = getRecipeParallel(controller);
                        if (parallel > 1) {
                            compoundTag.putBoolean("exact", true);
                        } else {
                            parallel = parallelHatch.get().getCurrentParallel();
                        }
                    }
                }
            }
            if (parallel > 1) compoundTag.putInt("parallel", parallel);
        }
    }

    private static int getRecipeParallel(IMultiController controller) {
        int parallel = 0;
        if (controller instanceof IRecipeLogicMachine rlm && rlm.getRecipeLogic().isActive() && rlm.getRecipeLogic().getLastRecipe() != null) {
            parallel = rlm.getRecipeLogic().getLastRecipe().parallels;
        }
        return parallel;
    }

    @Override
    public ResourceLocation getUid() {
        return GTCEu.id("parallel_info");
    }
}
