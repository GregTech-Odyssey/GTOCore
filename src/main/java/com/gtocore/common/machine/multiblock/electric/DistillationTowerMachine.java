package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistillationTower;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.capability.IFluidHandler;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DistillationTowerMachine extends ElectricMultiblockMachine implements IDistillationTower {

    @Getter
    private final List<IFluidHandler> fluidOutputs = new ArrayList<>();

    public DistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparingInt(p -> p.self().getPos().getY());
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (addOutputs()) return;
        onStructureInvalid();
    }

    @Override
    public void onStructureInvalid() {
        fluidOutputs.clear();
        super.onStructureInvalid();
    }

    @Override
    public int getYOffset() {
        return 1;
    }
}
