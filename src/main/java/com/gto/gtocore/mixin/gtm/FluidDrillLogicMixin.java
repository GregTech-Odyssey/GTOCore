package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.machine.trait.IFluidDrillLogic;
import com.gto.gtocore.common.machine.multiblock.electric.DrillingControlCenterMachine;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.machine.trait.FluidDrillLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidDrillLogic.class)
public class FluidDrillLogicMixin extends RecipeLogic implements IFluidDrillLogic {

    @Unique
    private DrillingControlCenterMachine gtocore$cache;

    public FluidDrillLogicMixin(IRecipeLogicMachine machine) {
        super(machine);
    }

    @Inject(method = "getFluidToProduce(Lcom/gregtechceu/gtceu/api/data/worldgen/bedrockfluid/FluidVeinWorldEntry;)I", at = @At("RETURN"), remap = false, cancellable = true)
    private void getFluidToProduce(FluidVeinWorldEntry entry, CallbackInfoReturnable<Integer> cir) {
        DrillingControlCenterMachine machine = getNetMachine();
        if (machine != null) cir.setReturnValue((int) (cir.getReturnValue() * machine.getMultiplier()));
    }

    @Override
    @SuppressWarnings("all")
    public DrillingControlCenterMachine getNetMachineCache() {
        return gtocore$cache;
    }

    @Override
    @SuppressWarnings("all")
    public void setNetMachineCache(DrillingControlCenterMachine cache) {
        this.gtocore$cache = cache;
    }

    @Override
    public void onMachineUnLoad() {
        super.onMachineUnLoad();
        removeNetMachineCache();
    }
}
