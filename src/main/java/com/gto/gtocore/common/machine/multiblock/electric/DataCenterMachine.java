package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.part.GTOPartAbility;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.DataBankMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class DataCenterMachine extends DataBankMachine {

    public DataCenterMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    protected int calculateEnergyUsage() {
        int receivers = 0;
        int transmitters = 0;
        int regulars = 0;

        for (IMultiPart part : this.getParts()) {
            Block block = part.self().getBlockState().getBlock();
            if (PartAbility.OPTICAL_DATA_RECEPTION.isApplicable(block)) {
                ++receivers;
            }

            if (PartAbility.OPTICAL_DATA_TRANSMISSION.isApplicable(block)) {
                ++transmitters;
            }

            if (GTOPartAbility.ExDATA_ACCESS.isApplicable(block)) {
                ++regulars;
            }
        }

        int OpticalHatches = receivers + transmitters;
        int eutOpticalHatch = receivers + transmitters > 10 ? GTValues.VA[9] : GTValues.VA[11];
        int eutDataHatch = regulars > 25 ? GTValues.VA[11] : GTValues.VA[13];
        return OpticalHatches * eutOpticalHatch + regulars * eutDataHatch;
    }
}
