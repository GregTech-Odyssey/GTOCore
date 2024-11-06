package com.gto.gtocore.mixin.gtm.fix;

import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.utils.GTUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorkableElectricMultiblockMachine.class)
public abstract class WorkableElectricMultiblockMachineMixin {

    @Shadow(remap = false)
    protected EnergyContainerList energyContainer;

    @Shadow(remap = false)
    public abstract EnergyContainerList getEnergyContainer();

    /**
     * @author mod_author
     * @reason fix
     */
    @Overwrite(remap = false)
    public long getOverclockVoltage() {
        if (this.energyContainer == null) {
            this.energyContainer = this.getEnergyContainer();
        }
        long voltage;
        long amperage;
        if (energyContainer.getInputVoltage() > energyContainer.getOutputVoltage()) {
            voltage = energyContainer.getInputVoltage();
            amperage = energyContainer.getInputAmperage();
        } else {
            voltage = energyContainer.getOutputVoltage();
            amperage = energyContainer.getOutputAmperage();
        }

        if (amperage == 1) {
            // amperage is 1 when the energy is not exactly on a tier
            // the voltage for recipe search is always on tier, so take the closest lower tier
            if (voltage > Integer.MAX_VALUE) return NumberUtils.getVoltageFromFakeTier(NumberUtils.getFakeVoltageTier(voltage));
            return GTValues.V[GTUtil.getFloorTierByVoltage(voltage)];
        } else {
            // amperage != 1 means the voltage is exactly on a tier
            // ignore amperage, since only the voltage is relevant for recipe search
            // amps are never > 3 in an EnergyContainerList
            return voltage;
        }
    }
}
