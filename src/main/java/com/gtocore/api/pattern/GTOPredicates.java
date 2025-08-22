package com.gtocore.api.pattern;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.block.MEStorageCoreBlock;
import com.gtocore.common.block.WirelessEnergyUnitBlock;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.machines.ManaMachine;

import com.gtolib.utils.FunctionContainer;
import com.gtolib.utils.GTOUtils;

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
import com.gregtechceu.gtceu.common.data.GTMachines;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;

import static com.gtocore.common.block.BlockMap.*;
import static com.gtolib.api.GTOValues.*;

public final class GTOPredicates {

    public static TraceabilityPredicate glass() {
        return tierBlock(GLASSMAP, GLASS_TIER);
    }

    public static TraceabilityPredicate machineCasing() {
        return tierBlock(MACHINECASINGMAP, MACHINE_CASING_TIER);
    }

    public static TraceabilityPredicate integralFramework() {
        return tierBlock(INTEGRALFRAMEWORKMAP, INTEGRAL_FRAMEWORK_TIER);
    }

    public static TraceabilityPredicate absBlocks() {
        return Predicates.blocks(ABS_CASING);
    }

    public static TraceabilityPredicate light() {
        return Predicates.blocks(LIGHT);
    }

    public static TraceabilityPredicate autoIOAbilities(GTRecipeType... recipeType) {
        return Predicates.autoAbilities(recipeType, false, false, true, true, true, true);
    }

    public static TraceabilityPredicate autoLaserAbilities(GTRecipeType... recipeType) {
        TraceabilityPredicate predicate = Predicates.autoAbilities(recipeType, false, false, true, true, true, true);
        for (GTRecipeType type : recipeType) {
            if (type.getMaxInputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(0)).or(Predicates.abilities(PartAbility.INPUT_LASER).setMaxGlobalLimited(2).setPreviewCount(1));
                break;
            } else if (type.getMaxOutputs(EURecipeCapability.CAP) > 0) {
                predicate = predicate.or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(0)).or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(2).setPreviewCount(1));
                break;
            }
        }
        return predicate;
    }

    public static TraceabilityPredicate autoGCYMAbilities(GTRecipeType... recipeType) {
        return Predicates.autoAbilities(recipeType, false, false, true, true, true, true).or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(8).setPreviewCount(1)).or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1)).or(Predicates.blocks(ManaMachine.MANA_AMPLIFIER_HATCH.getBlock()).setMaxGlobalLimited(1));
    }

    public static TraceabilityPredicate autoAccelerateAbilities(GTRecipeType... recipeType) {
        return Predicates.autoAbilities(recipeType).or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1));
    }

    public static TraceabilityPredicate autoThreadLaserAbilities(GTRecipeType... recipeType) {
        return autoLaserAbilities(recipeType).or(Predicates.abilities(GTOPartAbility.THREAD_HATCH).setMaxGlobalLimited(1)).or(Predicates.abilities(GTOPartAbility.OVERCLOCK_HATCH).setMaxGlobalLimited(1)).or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1)).or(Predicates.blocks(GTOMachines.WIRELESS_ENERGY_INTERFACE_HATCH.getBlock()).setMaxGlobalLimited(1));
    }

    public static TraceabilityPredicate tierBlock(Int2ObjectMap<Supplier<?>> map, String tierType) {
        Block[] blocks = new Block[map.size()];
        int index = 0;
        var list = new ArrayList<>(map.int2ObjectEntrySet());
        list.sort(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey));
        for (Int2ObjectMap.Entry<Supplier<?>> entry : list) {
            blocks[index] = (Block) entry.getValue().get();
            index++;
        }
        return new TraceabilityPredicate(state -> {
            BlockState blockState = state.getBlockState();
            for (Int2ObjectMap.Entry<Supplier<?>> entry : map.int2ObjectEntrySet()) {
                if (blockState.is((Block) entry.getValue().get())) {
                    int tier = entry.getIntKey();
                    int type = state.getMatchContext().getOrPut(tierType, tier);
                    if (type != tier) {
                        state.setError(new PatternStringError("gtocore.machine.pattern.error.tier"));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }, () -> BlockInfo.fromBlock(blocks[0]), () -> blocks).addTooltips(Component.translatable("gtocore.machine.pattern.error.tier"));
    }

    public static TraceabilityPredicate RotorBlock(int tier) {
        return new TraceabilityPredicate(new SimplePredicate(state -> {
            MetaMachine machine = MetaMachine.getMachine(state.getTileEntity());
            if (machine instanceof IRotorHolderMachine holder && machine.getDefinition().getTier() >= tier) {
                return state.getWorld().getBlockState(state.getPos().relative(holder.self().getFrontFacing())).isAir();
            }
            state.setError(new PatternStringError("gtceu.multiblock.pattern.clear_amount_3"));
            return false;
        }, () -> BlockInfo.fromBlockState(GTMachines.ROTOR_HOLDER[tier].defaultBlockState()), () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream().filter(b -> b instanceof MetaMachineBlock metaMachineBlock && metaMachineBlock.getDefinition().getTier() >= tier).toArray(Block[]::new))).addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_3"));
    }

    public static TraceabilityPredicate MEStorageCore() {
        return containerBlock(() -> new FunctionContainer<>(0D, (data, state) -> {
            if (state.getBlockState().getBlock() instanceof MEStorageCoreBlock block) {
                data += block.getCapacity();
            }
            return data;
        }), "MEStorageCore", ME_STORAGE_CORE);
    }

    public static TraceabilityPredicate craftingStorageCore() {
        return containerBlock(() -> new FunctionContainer<>(new double[2], (data, state) -> {
            if (state.getBlockState().getBlock() instanceof MEStorageCoreBlock block) {
                data[0] += block.getCapacity();
                data[1]++;
            }
            return data;
        }), "CraftingStorageCore", CRAFTING_STORAGE_CORE);
    }

    public static TraceabilityPredicate wirelessEnergyUnit() {
        return containerBlock(() -> new FunctionContainer<>(new ArrayList<WirelessEnergyUnitBlock>(), (data, state) -> {
            if (state.getBlockState().getBlock() instanceof WirelessEnergyUnitBlock block) {
                data.add(block);
            }
            return data;
        }), "wirelessEnergyUnit", WIRELESS_ENERGY_UNIT).setPreviewCount(1);
    }

    public static TraceabilityPredicate fissionComponent() {
        return containerBlock(() -> new FunctionContainer<>(new int[4], (integer, state) -> {
            Block block = state.getBlockState().getBlock();
            if (block == GTOBlocks.FISSION_FUEL_COMPONENT.get()) {
                integer[0]++;
                integer[2] += GTOUtils.adjacentBlock(side -> {
                    var pos = state.pos.relative(side);
                    return state.blockStateCache.computeIfAbsent(pos.asLong(), k -> state.world.getBlockState(pos)).getBlock();
                }, GTOBlocks.FISSION_FUEL_COMPONENT.get());
            } else if (block == GTOBlocks.FISSION_COOLER_COMPONENT.get() && GTOUtils.adjacentBlock(side -> {
                var pos = state.pos.relative(side);
                return state.blockStateCache.computeIfAbsent(pos.asLong(), k -> state.world.getBlockState(pos)).getBlock();
            }, GTOBlocks.FISSION_FUEL_COMPONENT.get()) > 1) {
                integer[1]++;
                integer[3] += GTOUtils.adjacentBlock(side -> {
                    var pos = state.pos.relative(side);
                    return state.blockStateCache.computeIfAbsent(pos.asLong(), k -> state.world.getBlockState(pos)).getBlock();
                }, GTOBlocks.FISSION_COOLER_COMPONENT.get());
            }
            return integer;
        }), "fissionComponent", GTOBlocks.FISSION_FUEL_COMPONENT.get(), GTOBlocks.FISSION_COOLER_COMPONENT.get()).setPreviewCount(1);
    }

    public static TraceabilityPredicate countBlock(String name, Block... blocks) {
        return containerBlock(() -> new FunctionContainer<>(0, (integer, state) -> ++integer), name, blocks);
    }

    public static <T> TraceabilityPredicate containerBlock(Supplier<FunctionContainer<T, MultiblockState>> containerSupplier, String name, Block... blocks) {
        TraceabilityPredicate predicate = Predicates.blocks(blocks);
        return new TraceabilityPredicate(new SimplePredicate(state -> {
            if (predicate.test(state)) {
                FunctionContainer<T, MultiblockState> container = state.getMatchContext().getOrPut(name, containerSupplier.get());
                container.apply(state);
                return true;
            }
            return false;
        }, () -> BlockInfo.fromBlock(blocks[0]), () -> predicate.common.stream().map(p -> p.candidates).filter(Objects::nonNull).map(Supplier::get).flatMap(Arrays::stream).toArray(Block[]::new)));
    }
}
