package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;
import com.gregtechceu.gtceu.utils.memoization.MemoizedSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import earth.terrarium.adastra.common.constants.PlanetConstants;
import earth.terrarium.adastra.common.registry.ModSoundEvents;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.EXPORT_FLUIDS;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.INPUT_ENERGY;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;

public interface ISpacePredicateMachine extends ISpaceWorkspaceMachine, ICleanroomProvider {

    @NotNull
    MultiblockState getMultiblockState();

    @Nullable
    Collection<IWorkInSpaceMachine> getSpaceMachines();

    void setSpaceMachines(@Nullable Collection<IWorkInSpaceMachine> spaceMachines);

    default void onFormed() {
        if (getSpaceMachines() != null) {
            this.getSpaceMachines().forEach(receiver -> {
                receiver.setCleanroom(null);
                receiver.setWorkspaceProvider(null);
            });
            setSpaceMachines(null);
        }
        setSpaceMachines(getMultiblockState().getMatchContext().getOrDefault("spaceMachine", Collections.emptyList()));
        getSpaceMachines().forEach(receiver -> {
            receiver.setCleanroom(this);
            receiver.setWorkspaceProvider(this);
            if (receiver instanceof IEnhancedRecipeLogicMachine enhanced) {
                enhanced.getRecipeLogic().updateTickSubscription();
            }
        });
    }

    default void onInvalid() {
        if (getSpaceMachines() != null) {
            this.getSpaceMachines().forEach(receiver -> {
                receiver.setCleanroom(null);
                receiver.setWorkspaceProvider(null);
            });
            setSpaceMachines(null);
        }
    }

    default void provideOxygen() {
        /// Oxygen and Temperature
        /// @see earth.terrarium.adastra.common.blockentities.machines.OxygenDistributorBlockEntity#tickOxygen
        if (getLevel() instanceof ServerLevel level) {
            Set<BlockPos> positions = new ObjectOpenHashSet<>(getMultiblockState().getMatchContext().getOrDefault("spaceMachinePhotovoltaicSupp", Collections.emptyList()));

            OxygenApi.API.setOxygen(level, positions, true);
            TemperatureApi.API.setTemperature(level, positions, PlanetConstants.COMFY_EARTH_TEMPERATURE);
            this.resetLastDistributedBlocks(positions);

            if (getOffsetTimer() % 200 == 0) {
                level.playSound(null, getPos().above(3).relative(getFrontFacing(), 15), ModSoundEvents.OXYGEN_OUTTAKE.get(), SoundSource.BLOCKS, 0.2f, 1);
            } else if (getOffsetTimer() % 100 == 0) {
                level.playSound(null, getPos().above(3).relative(getFrontFacing(), 15), ModSoundEvents.OXYGEN_INTAKE.get(), SoundSource.BLOCKS, 0.2f, 1);
            }
        }
    }

    Direction getFrontFacing();

    BlockPos getPos();

    Level getLevel();

    long getOffsetTimer();

    void resetLastDistributedBlocks(Set<BlockPos> positions);

    MemoizedSupplier<TraceabilityPredicate> innerBlockPredicate = GTMemoizer.memoize(() -> new BlockPredicate(blockWorldState -> {
        if (blockWorldState.getTileEntity() instanceof MetaMachineBlockEntity machineBlockEntity) {
            var machine = machineBlockEntity.getMetaMachine();
            if (isMachineBanned(machine)) {
                blockWorldState.setError(MultiblockState.BANNED_ERROR);
                return false;
            }
            if (machine instanceof IWorkInSpaceMachine spaceMachine) {
                blockWorldState.getMatchContext().getOrCreate("spaceMachine", ReferenceOpenHashSet::new).add(spaceMachine);
            }
        }
        return true;
    }, null, null));
    MemoizedSupplier<TraceabilityPredicate> photovoltaicPlantSupplyingPredicate = GTMemoizer.memoize(() -> {
        var abilities = abilities(EXPORT_FLUIDS).or(abilities(INPUT_ENERGY)).or(blocks(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.get()));
        return new BlockPredicate(blockWorldState -> {
            if (abilities.test(blockWorldState)) {
                if (blockWorldState.getTileEntity() instanceof MetaMachineBlockEntity mbe && mbe.getMetaMachine() instanceof MultiblockPartMachine spaceMachine) {
                    blockWorldState.getMatchContext().getOrCreate("spaceMachinePhotovoltaicSupp", ObjectOpenHashSet::new).add(spaceMachine.getPos());
                }
                return true;
            }
            return false;
        }, () -> BlockInfo.fromBlock(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.get()), abilities(EXPORT_FLUIDS).common.getFirst().candidates).or(abilities(INPUT_ENERGY)).or(blocks(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.get()));
    });

    static boolean isMachineBanned(MetaMachine machine) {
        return machine instanceof ISpaceWorkspaceMachine || machine instanceof ICleanroomProvider;
    }

    class BlockPredicate extends TraceabilityPredicate {

        BlockPredicate(Predicate<MultiblockState> predicate, Supplier<BlockInfo> blockInfo, @Nullable Supplier<Block[]> candidates) {
            super(predicate, blockInfo, candidates);
        }

        @Override
        public boolean testOnly() {
            return true;
        }

        @Override
        public boolean isAny() {
            return false;
        }

        @Override
        public boolean isAir() {
            return false;
        }
    }
}
