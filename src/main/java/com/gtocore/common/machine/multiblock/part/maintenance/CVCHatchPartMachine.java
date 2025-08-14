package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CVCHatchPartMachine extends ACMHatchPartMachine implements IVacuumMachine {

    public CVCHatchPartMachine(MetaMachineBlockEntity metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Override
    public int getVacuumTier() {
        return 4;
    }
}
