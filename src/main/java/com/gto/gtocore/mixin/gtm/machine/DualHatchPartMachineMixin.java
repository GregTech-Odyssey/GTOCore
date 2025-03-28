package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import net.minecraftforge.fluids.FluidType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DualHatchPartMachine.class)
public class DualHatchPartMachineMixin extends ItemBusPartMachine {

    public DualHatchPartMachineMixin(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void init(IMachineBlockEntity holder, int tier, IO io, Object[] args, CallbackInfo ci) {
        setDistinct(true);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getTankCapacity(int initialCapacity, int tier) {
        return 4 * FluidType.BUCKET_VOLUME * (1 << tier);
    }

    /**
     * @author .
     * @reason .
     */
    @Override
    @Overwrite(remap = false)
    public int getInventorySize() {
        return getTier() * getTier();
    }
}
