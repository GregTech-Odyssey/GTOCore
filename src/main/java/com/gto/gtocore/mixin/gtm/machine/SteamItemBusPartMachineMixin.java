package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamItemBusPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SteamItemBusPartMachine.class)
public class SteamItemBusPartMachineMixin {

    @ModifyConstant(method = "<init>", remap = false, constant = @Constant(intValue = 1))
    private static int modifyConsume(int constant) {
        return 3;
    }
}
