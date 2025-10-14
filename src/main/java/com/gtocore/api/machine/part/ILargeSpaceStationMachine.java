package com.gtocore.api.machine.part;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.machines.SpaceMultiblock;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.AbstractSpaceStation;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.Core;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.ISpacePredicateMachine;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.feature.multiblock.ICustomHighlightMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;
import com.gregtechceu.gtceu.utils.memoization.MemoizedSupplier;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.custom;
import static com.gtocore.api.machine.part.ILargeSpaceStationMachine.ConnectType.*;

public interface ILargeSpaceStationMachine extends ICustomHighlightMachine, ISpacePredicateMachine {

    MultiblockControllerMachine self();

    @Override
    default Set<CleanroomType> getTypes() {
        if (getRoot() != null) {
            return getRoot().getTypes();
        }
        return Collections.emptySet();
    }

    default void markDirty(boolean dirty) {
        if (getRoot() != null) getRoot().markDirty(dirty);
        else Core.NETWORK.getOrDefault(getLevel().dimension().location(), Set.of()).forEach(core -> core.markDirty(dirty));
    }

    @Nullable
    Core getRoot();

    void setRoot(@Nullable Core root);

    default void tickNonCoreModule() {
        if (getOffsetTimer() % 40 == 0) {
            AbstractSpaceStation self = (AbstractSpaceStation) self();

            if (getRoot() != null && getRoot().getReadyCount() > 0) {
                provideOxygen();
            } else {
                self.clearOxygenBlocks();
            }

            self.updateSpaceMachines();
        }
    }

    default Set<BlockPos> getModulePositions() {
        AbstractSpaceStation self = (AbstractSpaceStation) self();
        if (self.getPositionFunction() != null) return self.getPositionFunction().apply(self);
        return Collections.emptySet();
    }

    ConnectType getConnectType();

    long getEUt();

    default Set<ILargeSpaceStationMachine> getConnectedModules() {
        if (getLevel() == null) return Collections.emptySet();

        Set<ILargeSpaceStationMachine> machines = new ReferenceOpenHashSet<>();
        for (BlockPos pos : getModulePositions()) {
            var blockEntity = getLevel().getBlockEntity(pos);
            if (blockEntity instanceof MetaMachineBlockEntity metaMachineBlockEntity) {
                var machine = metaMachineBlockEntity.getMetaMachine();
                if (machine instanceof ILargeSpaceStationMachine largeSpaceStationMachine && largeSpaceStationMachine.isFormed()) {
                    machines.add(largeSpaceStationMachine);
                }
            }
        }
        return machines;
    }

    @Override
    default List<ForgeClientEvent.HighlightNeed> getCustomHighlights() {
        int color = getConnectType().color;
        var l = getModulePositions().stream()
                .map(pos -> new ForgeClientEvent.HighlightNeed(pos, pos, color))
                .toList();
        if (getRoot() != null) {
            l = new ArrayList<>(l);
            l.add(new ForgeClientEvent.HighlightNeed(getRoot().self().getPos(), getRoot().self().getPos(), 0xFFFFFF));
        }
        return l;
    }

    default Recipe getRecipe() {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;
        if (getRoot() == null || !getRoot().isWorkspaceReady())
            return null;

        return ((IEnhancedRecipeLogicMachine) self()).getRecipeBuilder().duration(200)
                .buildRawRecipe();
    }

    default void customText(@NotNull List<Component> list) {
        list.add(Component.translatable("gui.ae2.PowerUsageRate", "%s EU/t".formatted(getEUt())).withStyle(ChatFormatting.YELLOW));
        if (getRoot() != null) {
            list.add(Component.translatable("gui.ae2.AttachedTo", "[" + getRoot().getPos().toShortString() + "]"));
            getRoot().customText(list);
        } else list.add(Component.translatable("theoneprobe.ae2.p2p_unlinked"));
    }

    enum ConnectType {

        CONJUNCTION(0xFFFF00, () -> blocks(GTOBlocks.TITANIUM_ALLOY_INTERNAL_FRAME.get())
                .or(checkIsConjunction).or(checkIsModule)),
        MODULE(0x00FFFF, () -> blocks(GTOBlocks.TITANIUM_ALLOY_INTERNAL_FRAME.get())
                .or(checkIsConjunction)),
        CORE(0xFF0000, () -> blocks(GTOBlocks.TITANIUM_ALLOY_INTERNAL_FRAME.get())
                .or(checkIsConjunction));

        public final int color;
        public final MemoizedSupplier<TraceabilityPredicate> traceabilityPredicate;

        ConnectType(int color, Supplier<TraceabilityPredicate> predicateSupplier) {
            this.color = color;
            this.traceabilityPredicate = GTMemoizer.memoize(predicateSupplier);
        }

        static boolean check(MultiblockState state, ConnectType type) {
            if (state.getTileEntity() instanceof MetaMachineBlockEntity m && m.getMetaMachine() instanceof ILargeSpaceStationMachine machine) {
                return machine.getConnectType() == type;
            }
            return false;
        }
    }

    TraceabilityPredicate checkIsModule = custom(state -> check(state, MODULE),
            () -> BlockInfo.fromBlock(SpaceMultiblock.SPACE_STATION_EXTENSION_MODULE.getBlock()),
            null);
    TraceabilityPredicate checkIsConjunction = custom(state -> check(state, CONJUNCTION),
            () -> BlockInfo.fromBlock(SpaceMultiblock.SPACE_STATION_DOCKING_MODULE.getBlock()),
            null);

    Int2ObjectOpenHashMap<Function<AbstractSpaceStation, Set<BlockPos>>> positionFunctionMap = new Int2ObjectOpenHashMap<>();

    static Function<AbstractSpaceStation, Set<BlockPos>> twoWayPositionFunction(final int distance) {
        return positionFunctionMap.computeIfAbsent(distance,
                i -> (AbstractSpaceStation machine) -> {
                    var pos = machine.getPos();
                    var fFacing = machine.getFrontFacing();
                    var uFacing = machine.getUpwardsFacing();
                    boolean isFlipped = machine.isFlipped();
                    var hallwayCenter = pos.relative(fFacing, 2).relative(RelativeDirection.LEFT.getRelative(fFacing, uFacing, isFlipped), distance);
                    return Set.of(hallwayCenter.relative(fFacing, 2),
                            hallwayCenter.relative(fFacing.getOpposite(), 2),
                            hallwayCenter.relative(RelativeDirection.UP.getRelative(fFacing, uFacing, isFlipped), 2),
                            hallwayCenter.relative(RelativeDirection.DOWN.getRelative(fFacing, uFacing, isFlipped), 2));
                });
    }
}
