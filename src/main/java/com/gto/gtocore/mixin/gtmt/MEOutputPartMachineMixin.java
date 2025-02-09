package com.gto.gtocore.mixin.gtmt;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;

import net.minecraft.core.Direction;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.appeng.MEOutputPartMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(MEOutputPartMachine.class)
public abstract class MEOutputPartMachineMixin extends DualHatchPartMachine implements IGridConnectedMachine {

    protected MEOutputPartMachineMixin(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    @Inject(method = "onLoad", at = @At("TAIL"), remap = false)
    private void onLoad(CallbackInfo ci) {
        if (holder.self().getPersistentData().getBoolean("isAllFacing")) {
            getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
        }
    }
}
