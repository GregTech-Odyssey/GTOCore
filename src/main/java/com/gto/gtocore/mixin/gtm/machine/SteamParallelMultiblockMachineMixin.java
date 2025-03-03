package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamEnergyRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SteamParallelMultiblockMachine.class)
public final class SteamParallelMultiblockMachineMixin extends WorkableMultiblockMachine {

    private SteamParallelMultiblockMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "onStructureFormed", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/machine/multiblock/WorkableMultiblockMachine;onStructureFormed()V", shift = At.Shift.AFTER), remap = false, cancellable = true)
    private void onStructureFormed(CallbackInfo ci) {
        ci.cancel();
        var handlers = capabilitiesProxy.get(IO.IN, FluidRecipeCapability.CAP);
        if (handlers == null) return;
        var itr = handlers.iterator();
        while (itr.hasNext()) {
            if (itr.next() instanceof NotifiableFluidTank tank && tank.getMachine() instanceof SteamHatchPartMachine) {
                itr.remove();
                capabilitiesProxy.put(IO.IN, EURecipeCapability.CAP, List.of(new SteamEnergyRecipeHandler(tank, 2)));
                return;
            }
        }
    }
}
