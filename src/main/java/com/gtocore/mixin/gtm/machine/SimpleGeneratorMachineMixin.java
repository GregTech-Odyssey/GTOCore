package com.gtocore.mixin.gtm.machine;

import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SimpleGeneratorMachine.class)
public class SimpleGeneratorMachineMixin extends WorkableTieredMachine {

    public SimpleGeneratorMachineMixin(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected long getMaxInputOutputAmperage() {
        return GTOUtils.getGeneratorAmperage(getTier());
    }

    @Override
    public long getOverclockVoltage() {
        return GTValues.V[getTier()] * getMaxInputOutputAmperage();
    }
}
