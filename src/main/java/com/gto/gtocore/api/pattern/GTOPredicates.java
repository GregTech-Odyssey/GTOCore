package com.gto.gtocore.api.pattern;

import com.gto.gtocore.api.pattern.util.IValueContainer;
import com.gto.gtocore.api.pattern.util.SimpleValueContainer;
import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GTOPredicates {

    public static final TraceabilityPredicate STELLAR_CONTAINMENT = createTierPredicate(GTOBlocks.SCMAP, "stellar_containment_tier");
    public static final TraceabilityPredicate POWER_MODULE = createTierPredicate(GTOBlocks.SEPMMAP, "power_module_tier");
    public static final TraceabilityPredicate COMPONENT_ASSEMBLY_LINE_CASING = createTierPredicate(GTOBlocks.CALMAP, "component_assembly_line_casing_tier");

    private static TraceabilityPredicate createTierPredicate(Map<Integer, Supplier<?>> map, String tierType) {
        BlockInfo[] blockInfos = new BlockInfo[map.size()];
        int index = 0;
        for (Supplier<?> blockSupplier : map.values()) {
            Block block = (Block) blockSupplier.get();
            blockInfos[index++] = BlockInfo.fromBlockState(block.defaultBlockState());
        }
        return new TraceabilityPredicate(blockWorldState -> {
            BlockState blockState = blockWorldState.getBlockState();
            for (Map.Entry<Integer, Supplier<?>> entry : map.entrySet()) {
                if (blockState.is((Block) entry.getValue().get())) {
                    int tier = entry.getKey();
                    int type = blockWorldState.getMatchContext().getOrPut(tierType, tier);
                    if (type != tier) {
                        blockWorldState.setError(new PatternStringError("gtocore.machine.pattern.error.tier"));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }, () -> blockInfos).addTooltips(Component.translatable("gtocore.machine.pattern.error.tier"));
    }

    public static TraceabilityPredicate autoLaserAbilities(GTRecipeType... recipeType) {
        TraceabilityPredicate predicate = Predicates.autoAbilities(recipeType, false, false, true, true, true, true);
        for (GTRecipeType type : recipeType) {
            if (type.getMaxInputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.INPUT_ENERGY)
                        .setMaxGlobalLimited(2).setPreviewCount(1))
                        .or(Predicates.abilities(PartAbility.INPUT_LASER)
                                .setMaxGlobalLimited(1).setPreviewCount(1));
                break;
            } else if (type.getMaxOutputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.OUTPUT_ENERGY)
                        .setMaxGlobalLimited(2).setPreviewCount(1))
                        .or(Predicates.abilities(PartAbility.OUTPUT_LASER)
                                .setMaxGlobalLimited(1).setPreviewCount(1));
                break;
            }
        }
        return predicate;
    }

    public static TraceabilityPredicate countBlock(String name, Block... blocks) {
        TraceabilityPredicate inner = Predicates.blocks(blocks);
        Predicate<MultiblockState> predicate = state -> {
            if (inner.test(state)) {
                IValueContainer<?> currentContainer = state.getMatchContext().getOrPut(name + "Value",
                        new SimpleValueContainer<>(0, (integer, block, tierType) -> ++integer));
                currentContainer.operate(state.getBlockState().getBlock(), null);
                return true;
            }
            return false;
        };
        BlockInfo[] candidates = inner.common.stream()
                .map(p -> p.candidates)
                .filter(Objects::nonNull)
                .map(Supplier::get)
                .flatMap(Arrays::stream)
                .toArray(BlockInfo[]::new);
        return new TraceabilityPredicate(new SimplePredicate(predicate, () -> candidates));
    }

    public static TraceabilityPredicate RotorBlock(int tier) {
        return new TraceabilityPredicate(
                new SimplePredicate(state -> {
                    Level world = state.getWorld();
                    BlockPos pos = state.getPos();
                    if (MetaMachine.getMachine(world, pos) instanceof IRotorHolderMachine holder) {
                        return world.getBlockState(pos.relative(holder.self().getFrontFacing())).isAir();
                    }
                    return false;
                }, () -> PartAbility.ROTOR_HOLDER.getAllBlocks()
                        .stream()
                        .filter(b -> b instanceof MetaMachineBlock metaMachineBlock && metaMachineBlock.getDefinition().getTier() >= tier)
                        .map(BlockInfo::fromBlock)
                        .toArray(BlockInfo[]::new)))
                .addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_3"));
    }
}
