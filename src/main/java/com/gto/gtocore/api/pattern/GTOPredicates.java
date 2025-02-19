package com.gto.gtocore.api.pattern;

import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.block.WirelessEnergyUnitBlock;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.utils.FunctionContainer;
import com.gto.gtocore.utils.GTOUtils;

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

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.gto.gtocore.api.GTOValues.GLASS_TIER;
import static com.gto.gtocore.api.GTOValues.MACHINE_CASING_TIER;
import static com.gto.gtocore.common.block.BlockMap.GLASSMAP;
import static com.gto.gtocore.common.block.BlockMap.MACHINECASINGMAP;

public interface GTOPredicates {

    static TraceabilityPredicate glass() {
        return tierBlock(GLASSMAP, GLASS_TIER);
    }

    static TraceabilityPredicate machineCasing() {
        return tierBlock(MACHINECASINGMAP, MACHINE_CASING_TIER);
    }

    static TraceabilityPredicate absBlocks() {
        return Predicates.blocks(GTOBlocks.ABS_BLACK_CASING.get(), GTOBlocks.ABS_BLUE_CASING.get(), GTOBlocks.ABS_BROWN_CASING.get(), GTOBlocks.ABS_GREEN_CASING.get(), GTOBlocks.ABS_GREY_CASING.get(), GTOBlocks.ABS_LIME_CASING.get(), GTOBlocks.ABS_ORANGE_CASING.get(), GTOBlocks.ABS_RAD_CASING.get(), GTOBlocks.ABS_WHITE_CASING.get(), GTOBlocks.ABS_YELLOW_CASING.get(), GTOBlocks.ABS_CYAN_CASING.get(), GTOBlocks.ABS_MAGENTA_CASING.get(), GTOBlocks.ABS_PINK_CASING.get(), GTOBlocks.ABS_PURPLE_CASING.get(), GTOBlocks.ABS_LIGHT_BULL_CASING.get(), GTOBlocks.ABS_LIGHT_GREY_CASING.get());
    }

    static TraceabilityPredicate autoLaserAbilities(GTRecipeType... recipeType) {
        TraceabilityPredicate predicate = Predicates.autoAbilities(recipeType, false, false, true, true, true, true);
        for (GTRecipeType type : recipeType) {
            if (type.getMaxInputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1)).or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(1).setPreviewCount(1));
                break;
            } else if (type.getMaxOutputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1)).or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(1).setPreviewCount(1));
                break;
            }
        }
        return predicate;
    }

    static TraceabilityPredicate autoAccelerateAbilities(GTRecipeType... recipeType) {
        return Predicates.autoAbilities(recipeType).or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1));
    }

    static TraceabilityPredicate autoThreadLaserAbilities(GTRecipeType... recipeType) {
        return autoLaserAbilities(recipeType).or(Predicates.abilities(GTOPartAbility.THREAD_HATCH).setMaxGlobalLimited(1)).or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1));
    }

    static TraceabilityPredicate tierBlock(Map<Integer, Supplier<?>> map, String tierType) {
        BlockInfo[] blockInfos = new BlockInfo[map.size()];
        int index = 0;
        for (Supplier<?> blockSupplier : map.values()) {
            Block block = (Block) blockSupplier.get();
            blockInfos[index] = BlockInfo.fromBlockState(block.defaultBlockState());
            index++;
        }
        return new TraceabilityPredicate(state -> {
            BlockState blockState = state.getBlockState();
            for (Map.Entry<Integer, Supplier<?>> entry : map.entrySet()) {
                if (blockState.is((Block) entry.getValue().get())) {
                    int tier = entry.getKey();
                    int type = state.getMatchContext().getOrPut(tierType, tier);
                    if (type != tier) {
                        state.setError(new PatternStringError("gtocore.machine.pattern.error.tier"));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }, () -> blockInfos).addTooltips(Component.translatable("gtocore.machine.pattern.error.tier"));
    }

    static TraceabilityPredicate RotorBlock(int tier) {
        return new TraceabilityPredicate(new SimplePredicate(state -> {
            Level level = state.getWorld();
            BlockPos pos = state.getPos();
            MetaMachine machine = MetaMachine.getMachine(level, pos);
            if (machine instanceof IRotorHolderMachine holder && machine.getDefinition().getTier() >= tier) {
                return level.getBlockState(pos.relative(holder.self().getFrontFacing())).isAir();
            }
            return false;
        }, () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream().filter(b -> b instanceof MetaMachineBlock metaMachineBlock && metaMachineBlock.getDefinition().getTier() >= tier).map(BlockInfo::fromBlock).toArray(BlockInfo[]::new))).addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_3"));
    }

    static TraceabilityPredicate wirelessEnergyUnit() {
        return containerBlock(() -> new FunctionContainer<>(new ArrayList<WirelessEnergyUnitBlock>(), (data, state) -> {
            if (state.getBlockState().getBlock() instanceof WirelessEnergyUnitBlock block) {
                data.add(block);
            }
            return data;
        }), "wirelessEnergyUnit", GTOBlocks.LV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.MV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.HV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.EV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.IV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.LUV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.ZPM_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UHV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UEV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UIV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.UXV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.OPV_WIRELESS_ENERGY_UNIT.get(), GTOBlocks.MAX_WIRELESS_ENERGY_UNIT.get()).setPreviewCount(1);
    }

    static TraceabilityPredicate fissionComponent() {
        return containerBlock(() -> new FunctionContainer<>(new int[4], (integer, state) -> {
            Block block = state.getBlockState().getBlock();
            if (block == GTOBlocks.FISSION_FUEL_COMPONENT.get()) {
                integer[0]++;
                integer[2] += GTOUtils.adjacentBlock(state.world, state.getPos(), GTOBlocks.FISSION_FUEL_COMPONENT.get());
            } else if (block == GTOBlocks.FISSION_COOLER_COMPONENT.get()) {
                integer[1]++;
                integer[3] += GTOUtils.adjacentBlock(state.world, state.getPos(), GTOBlocks.FISSION_COOLER_COMPONENT.get());
            }
            return integer;
        }), "fissionComponent", GTOBlocks.FISSION_FUEL_COMPONENT.get(), GTOBlocks.FISSION_COOLER_COMPONENT.get()).setPreviewCount(1);
    }

    static TraceabilityPredicate countBlock(String name, Block... blocks) {
        return containerBlock(() -> new FunctionContainer<>(0, (integer, state) -> ++integer), name, blocks);
    }

    private static <T> TraceabilityPredicate containerBlock(Supplier<FunctionContainer<T, MultiblockState>> containerSupplier, String name, Block... blocks) {
        TraceabilityPredicate predicate = Predicates.blocks(blocks);
        return new TraceabilityPredicate(new SimplePredicate(state -> {
            if (predicate.test(state)) {
                FunctionContainer<T, MultiblockState> container = state.getMatchContext().getOrPut(name, containerSupplier.get());
                container.apply(state);
                return true;
            }
            return false;
        }, () -> predicate.common.stream().map(p -> p.candidates).filter(Objects::nonNull).map(Supplier::get).flatMap(Arrays::stream).toArray(BlockInfo[]::new)));
    }
}
