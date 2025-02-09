package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FluidHatchPartMachine.class)
public class FluidHatchPartMachineMixin extends TieredIOPartMachine {

    public FluidHatchPartMachineMixin(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier, io);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getTankCapacity(int initialCapacity, int tier) {
        return initialCapacity * (1 << tier);
    }
}
