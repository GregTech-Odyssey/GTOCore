package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;

import com.gtolib.api.machine.trait.CustomRecipeLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public class Extension extends AbstractSpaceStation implements ILargeSpaceStationMachine {

    protected Core core;

    public Extension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        shouldShowReadyText = false;
    }

    public Extension(MetaMachineBlockEntity metaMachineBlockEntity, @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction) {
        super(metaMachineBlockEntity, positionFunction);
        shouldShowReadyText = false;
    }

    @Override
    public @Nullable Core getRoot() {
        return core;
    }

    @Override
    public void setRoot(@Nullable Core root) {
        core = root;
    }

    @Override
    public ConnectType getConnectType() {
        return ConnectType.MODULE;
    }

    @Override
    public long getEUt() {
        return VA[LuV];
    }

    @Override
    public boolean isWorkspaceReady() {
        return getRoot() != null && getRoot().isWorkspaceReady();
    }

    @Override
    protected void tickReady() {
        tickNonCoreModule();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onFormed();
        markDirty(true);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        onInvalid();
        markDirty(true);
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        markDirty(true);
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        ILargeSpaceStationMachine.super.customText(list);
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, false);
    }
}
