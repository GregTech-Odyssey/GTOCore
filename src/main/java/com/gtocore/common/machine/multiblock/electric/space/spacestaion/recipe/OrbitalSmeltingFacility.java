package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;

import com.gtolib.api.machine.trait.CoilTrait;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;

import net.minecraft.core.BlockPos;

import java.util.Set;

public class OrbitalSmeltingFacility extends RecipeExtension implements ICoilMachine {

    private final CoilTrait coilTrait;

    public OrbitalSmeltingFacility(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        this.coilTrait = new CoilTrait(this, true, true);
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        return ILargeSpaceStationMachine.twoWayPositionFunction(41).apply(this);
    }
}
