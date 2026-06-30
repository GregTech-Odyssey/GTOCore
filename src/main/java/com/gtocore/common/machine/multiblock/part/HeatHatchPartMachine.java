package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.machine.part.IHeatContainerPart;

import com.gtolib.api.machine.heat.HeatHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import com.gto.datasynclib.annotations.SaveToDisk;
import lombok.Getter;

public class HeatHatchPartMachine extends MultiblockPartMachine implements IHeatContainerPart, IExplosionMachine {

    @Getter
    @SaveToDisk
    private final HeatHandler heatContainer;

    public HeatHatchPartMachine(MetaMachineBlockEntity holder, long maxTemperature, double heatCapacity, double baseTransferRate) {
        super(holder);
        heatContainer = new HeatHandler(holder, maxTemperature, heatCapacity, baseTransferRate, 0.01);
        heatContainer.setSideIOCondition(s -> s == getFrontFacing());
        heatContainer.addChangedListener(() -> {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine machine) machine.getRecipeLogic().updateTickSubscription();
            }
        });
    }

    @Override
    public void onLoad() {
        super.onLoad();
        heatContainer.onLoad();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        heatContainer.onUnLoad();
    }
}
