package com.gto.gtocore.mixin.gtmt;

import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import com.hepdd.gtmthings.api.machine.IWirelessEnergyContainerHolder;
import com.hepdd.gtmthings.common.block.machine.electric.WirelessEnergyInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WirelessEnergyInterface.class)
public abstract class WirelessEnergyInterfaceMixin implements IWirelessEnergyContainerHolder {

    @Shadow(remap = false)
    @Final
    public NotifiableEnergyContainer energyContainer;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void updateEnergy() {
        long currentStored = this.energyContainer.getEnergyStored();
        if (currentStored > 0L) {
            ExtendWirelessEnergyContainer container = (ExtendWirelessEnergyContainer) getWirelessEnergyContainer();
            if (container != null) {
                int loss = container.getLoss();
                container.setLoss(loss + 100);
                long changeEnergy = container.addEnergy(currentStored, (WirelessEnergyInterface) (Object) this);
                container.setLoss(loss);
                if (changeEnergy > 0L) {
                    this.energyContainer.setEnergyStored(currentStored - changeEnergy);
                }
            }
        }
    }
}
