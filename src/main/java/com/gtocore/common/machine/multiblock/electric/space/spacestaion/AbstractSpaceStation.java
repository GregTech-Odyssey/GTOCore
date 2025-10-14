package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtolib.api.machine.feature.IWorkInSpaceMachine;
import com.gtolib.api.machine.feature.multiblock.ICustomHighlightMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractSpaceStation extends ElectricMultiblockMachine implements ISpacePredicateMachine, ICustomHighlightMachine {

    @Nullable
    private Collection<IWorkInSpaceMachine> spaceMachines;
    @DescSynced
    private final Set<BlockPos> lastDistributedBlocks = new ObjectOpenHashSet<>();
    private final @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction;

    @Persisted
    protected int ready;
    @Nullable
    private TickableSubscription tickSubscription = null;
    boolean shouldShowReadyText = true;

    AbstractSpaceStation(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        this.positionFunction = null;
    }

    AbstractSpaceStation(MetaMachineBlockEntity metaMachineBlockEntity, @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction) {
        super(metaMachineBlockEntity);
        this.positionFunction = positionFunction;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (tickSubscription == null) {
            tickSubscription = subscribeServerTick(this::tickReady);
        }
    }

    protected void tickReady() {
        if (getOffsetTimer() % 20 == 0) {
            if (getRecipeLogic().isWorking()) {
                int oldReady = ready;
                ready = Math.min(20, ready + 1);
                if (ready == 10 && oldReady < 10) updateSpaceMachines();
            } else ready = Math.max(0, ready - 1);
            if (ready == 0) {
                clearOxygenBlocks();
            }
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubscription != null) {
            tickSubscription.unsubscribe();
            tickSubscription = null;
        }
    }

    @Override
    public @Nullable Collection<IWorkInSpaceMachine> getSpaceMachines() {
        return spaceMachines;
    }

    @Override
    public void setSpaceMachines(@Nullable Collection<IWorkInSpaceMachine> spaceMachines) {
        synchronized (this) {
            this.spaceMachines = spaceMachines;
        }
    }

    @Override
    public void resetLastDistributedBlocks(Set<BlockPos> positions) {
        lastDistributedBlocks.removeAll(positions);
        clearOxygenBlocks();
        lastDistributedBlocks.addAll(positions);
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        clearOxygenBlocks();
    }

    public void clearOxygenBlocks() {
        OxygenApi.API.removeOxygen(getLevel(), lastDistributedBlocks);
        TemperatureApi.API.removeTemperature(getLevel(), lastDistributedBlocks);
        lastDistributedBlocks.clear();
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        attachHighlightConfigurators(configuratorPanel);
    }

    public void updateSpaceMachines() {
        if (spaceMachines != null) {
            this.spaceMachines.forEach(m -> m.getRecipeLogic().updateTickSubscription());
        }
    }

    @Override
    public boolean isWorkspaceReady() {
        return ready >= 10;
    }

    @Override
    public void regressRecipe(RecipeLogic recipeLogic) {
        super.regressRecipe(recipeLogic);
        ready = 0;
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        if (shouldShowReadyText) list.add(Component.translatable("gtocore.machine.spacestation.ready", Math.min(ready * 10, 100)).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean isClean() {
        return isWorkspaceReady();
    }

    public int getReadyCount() {
        return ready;
    }

    public @Nullable Function<AbstractSpaceStation, Set<BlockPos>> getPositionFunction() {
        return positionFunction;
    }
}
