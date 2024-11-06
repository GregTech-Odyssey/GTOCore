package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Mixin;

import java.util.EnumSet;

@Mixin(MEHatchPartMachine.class)
public abstract class MEHatchPartMachineMixin extends FluidHatchPartMachine implements IGridConnectedMachine {

    public MEHatchPartMachineMixin(IMachineBlockEntity holder, int tier, IO io, long initialCapacity, int slots, Object... args) {
        super(holder, tier, io, initialCapacity, slots, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (holder.self().getPersistentData().getBoolean("isAllFacing")) {
            getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
        }
    }
}
