package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.TankValvePartMachine;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TankValvePartMachine.class)
public class TankValvePartMachineMixin extends MultiblockPartMachine {

    public TankValvePartMachineMixin(IMachineBlockEntity holder) {
        super(holder);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private Boolean shouldAutoIO() {
        if (!isFormed()) return false;
        return getFrontFacing() == Direction.DOWN;
    }
}
