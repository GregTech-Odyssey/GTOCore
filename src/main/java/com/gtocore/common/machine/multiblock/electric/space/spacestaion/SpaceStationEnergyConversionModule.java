package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import static com.gregtechceu.gtceu.api.GTValues.UIV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public class SpaceStationEnergyConversionModule extends Extension implements ISpaceServiceMachine {

    public SpaceStationEnergyConversionModule(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    public long getEUt() {
        return VA[UIV];
    }
}
