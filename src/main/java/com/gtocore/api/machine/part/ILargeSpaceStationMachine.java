package com.gtocore.api.machine.part;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.machines.MultiBlockH;
import com.gtocore.common.machine.multiblock.electric.space.ISpacePredicateMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.AbstractSpaceStation;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.Core;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.feature.multiblock.ICustomHighlightMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;
import com.gregtechceu.gtceu.utils.memoization.MemoizedSupplier;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.custom;
import static com.gtocore.api.machine.part.ILargeSpaceStationMachine.ConnectType.*;

public interface ILargeSpaceStationMachine extends ICustomHighlightMachine, ISpacePredicateMachine {

    MultiblockControllerMachine self();

    @Nullable
    Core getRoot();

    void setRoot(@Nullable Core root);

    default void onWork() {
        if (getOffsetTimer() % 40 == 0) {
            provideOxygen();
            ((AbstractSpaceStation) self()).updateSpaceMachines();
        }
    }

    Set<BlockPos> getModulePositions();

    ConnectType getConnectType();

    long getEUt();

    default Set<ILargeSpaceStationMachine> getConnectedModules() {
        if (getLevel() == null) return Collections.emptySet();
        return getModulePositions().stream()
                .map(pos -> getLevel().getBlockEntity(pos))
                .filter(Objects::nonNull)
                .filter(blockEntity -> blockEntity instanceof MetaMachineBlockEntity)
                .map(blockEntity -> ((MetaMachineBlockEntity) blockEntity).getMetaMachine())
                .filter(machine -> machine instanceof ILargeSpaceStationMachine)
                .map(machine -> (ILargeSpaceStationMachine) machine)
                .filter(IMultiController::isFormed)
                .collect(Collectors.toSet());
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
            () -> BlockInfo.fromBlock(MultiBlockH.SPACE_STATION_EXTENSION_MODULE.getBlock()),
            null);
    TraceabilityPredicate checkIsConjunction = custom(state -> check(state, CONJUNCTION),
            () -> BlockInfo.fromBlock(MultiBlockH.SPACE_STATION_DOCKING_MODULE.getBlock()),
            null);
}
