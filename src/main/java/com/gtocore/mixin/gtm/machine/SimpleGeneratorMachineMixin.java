package com.gtocore.mixin.gtm.machine;

import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.handler.IO;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collections;

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

    @Override
    protected NotifiableFluidTank createExportFluidHandler(Object... args) {
        return new NotifiableFluidTank((SimpleGeneratorMachine) (Object) this, Collections.emptyList(), IO.OUT);
    }
}
