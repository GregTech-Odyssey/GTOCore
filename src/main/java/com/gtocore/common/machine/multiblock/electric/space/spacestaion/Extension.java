package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;

import com.gtolib.api.machine.trait.CustomRecipeLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public class Extension extends AbstractSpaceStation implements ILargeSpaceStationMachine {

    protected Core core;

    public Extension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
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
    public Set<BlockPos> getModulePositions() {
        var pos = getPos();
        var fFacing = getFrontFacing();
        var uFacing = getUpwardsFacing();
        var hallwayCenter = pos.relative(fFacing, 2).relative(RelativeDirection.LEFT.getRelative(fFacing, uFacing, isFlipped()), 59);
        return Set.of(hallwayCenter.relative(fFacing, 2),
                hallwayCenter.relative(fFacing.getOpposite(), 2),
                hallwayCenter.relative(RelativeDirection.UP.getRelative(fFacing, uFacing, isFlipped()), 2),
                hallwayCenter.relative(RelativeDirection.DOWN.getRelative(fFacing, uFacing, isFlipped()), 2));
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
    public boolean onWorking() {
        onWork();
        return super.onWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onFormed();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        onInvalid();
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        if (getRoot() != null) {
            getRoot().refreshModules();
        }
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
