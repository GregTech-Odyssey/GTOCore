package com.gtocore.integration.jade.provider;

import com.gtocore.common.machine.multiblock.part.ParallelHatchPartMachine;

import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeHelper;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
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
        long parallel = blockAccessor.getServerData().getLong("parallel");
        if (parallel > 1) {
            long batchParallel = blockAccessor.getServerData().getLong("batch_parallel");
            Component parallels = Component.literal(FormattingUtil.formatNumbers(parallel)).withStyle(ChatFormatting.DARK_PURPLE);
            String key = "gtceu.multiblock.parallel";
            if (blockAccessor.getServerData().getBoolean("exact")) key += ".exact";
            iTooltip.add(Component.translatable(key, parallels)
                    .append(batchParallel > 1 ?
                            Component.translatable("gtceu.multiblock.batch_parallel_multiplier", Component.literal("×%s".formatted(batchParallel)).withStyle(ChatFormatting.DARK_BLUE)) :
                            Component.empty()));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity) {
            MetaMachine machine = blockEntity.getMetaMachine();
            if (machine instanceof ParallelHatchPartMachine parallelHatchPartMachine) {
                compoundTag.putLong("parallel", parallelHatchPartMachine.getCurrentParallelLong());
                return;
            }
            if (machine instanceof SimpleGeneratorMachine) return;
            if (machine.getDefinition() instanceof MultiblockMachineDefinition multiblockMachineDefinition && multiblockMachineDefinition.isGenerator()) return;
            long[] parallels = getRecipeParallel(machine);
            long parallel = parallels[0];
            long batchParallel = parallels[1];
            if (parallel > 0) {
                compoundTag.putBoolean("exact", true);
            }
            long originParallel = 1L;
            if (machine instanceof IMultiController controller) {
                if (controller instanceof IParallelMachine parallelHatch) {
                    originParallel = parallelHatch.getParallelLong();
                } else {
                    Optional<IParallelHatch> parallelHatch = controller.getParallelHatch();
                    if (parallelHatch.isPresent()) {
                        originParallel = ((ParallelHatchPartMachine) parallelHatch.get()).getCurrentParallelLong();
                    }
                }
            }
            if (parallel > 1) compoundTag.putLong("parallel", parallel);
            else if (originParallel > 1) compoundTag.putLong("parallel", originParallel);
            if (batchParallel > 1) compoundTag.putLong("batch_parallel", batchParallel);
            else if (parallel / originParallel > 1) compoundTag.putLong("batch_parallel", parallel / originParallel);
        }
    }

    private static long[] getRecipeParallel(MetaMachine machine) {
        long[] parallel = new long[] { 0L, 0L };
        if (machine instanceof IRecipeLogicMachine rlm && rlm.getRecipeLogic().isActive() && rlm.getRecipeLogic().getLastRecipe() instanceof Recipe recipe) {
            parallel[0] = RecipeHelper.getParallel(recipe);
            parallel[1] = recipe.batchParallels;
        }
        return parallel;
    }

    @Override
    public ResourceLocation getUid() {
        return GTCEu.id("parallel_info");
    }
}
