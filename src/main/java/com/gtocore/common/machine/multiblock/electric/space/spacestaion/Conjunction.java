package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;

import com.gtolib.api.machine.trait.CustomRecipeLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.HV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public class Conjunction extends AbstractSpaceStation implements ILargeSpaceStationMachine {

    protected Core core;

    public Conjunction(MetaMachineBlockEntity metaMachineBlockEntity) {
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
        var hallwayCenter = pos.relative(fFacing, 2).relative(RelativeDirection.LEFT.getRelative(fFacing, uFacing, isFlipped()), 11);
        ImmutableSet.Builder<BlockPos> builder = ImmutableSet.builder();
        for (RelativeDirection dir : RelativeDirection.values()) {
            if (dir == RelativeDirection.RIGHT) continue;
            var newFFacing = dir.getRelative(fFacing, uFacing, isFlipped());
            var newUFacing = RelativeDirection.UP.getRelative(newFFacing, uFacing, isFlipped());
            var shiftedPos = hallwayCenter.relative(newFFacing, dir == RelativeDirection.LEFT ? 8 : 5);
            builder.add(shiftedPos.relative(RelativeDirection.UP.getRelative(newFFacing, newUFacing, isFlipped()), 2));
            builder.add(shiftedPos.relative(RelativeDirection.DOWN.getRelative(newFFacing, newUFacing, isFlipped()), 2));
            builder.add(shiftedPos.relative(RelativeDirection.LEFT.getRelative(newFFacing, newUFacing, isFlipped()), 2));
            builder.add(shiftedPos.relative(RelativeDirection.RIGHT.getRelative(newFFacing, newUFacing, isFlipped()), 2));
        }
        return builder.build();
    }

    @Override
    public ConnectType getConnectType() {
        return ConnectType.CONJUNCTION;
    }

    @Override
    public long getEUt() {
        return VA[HV];
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
