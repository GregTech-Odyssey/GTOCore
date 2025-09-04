package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.feature.IDroneInteractionMachine;
import com.gtolib.api.machine.multiblock.DroneControlCenterMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.misc.Drone;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MaintenanceHatchPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaintenanceHatchPartMachine.class)
public abstract class MaintenanceHatchPartMachineMixin extends TieredPartMachine implements IMaintenanceMachine, IDroneInteractionMachine {

    protected MaintenanceHatchPartMachineMixin(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Shadow(remap = false)
    public abstract void fixAllMaintenanceProblems();

    @Shadow(remap = false)
    protected int timeActive;

    @Unique
    private DroneControlCenterMachine gtolib$cache;

    @Unique
    @SuppressWarnings("all")
    public DroneControlCenterMachine getNetMachineCache() {
        return gtolib$cache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setNetMachineCache(DroneControlCenterMachine cache) {
        gtolib$cache = cache;
    }

    @Override
    public void calculateMaintenance(IMaintenanceMachine maintenanceMachine, int duration) {
        if (maintenanceMachine.isFullAuto()) return;
        var pa = 1;
        for (var c : getControllers()) {
            pa += c.getParts().size();
        }
        timeActive = MathUtil.saturatedCast((long) (timeActive + (duration * getDurationMultiplier() * pa * pa)));
        var value = timeActive - MINIMUM_MAINTENANCE_TIME;
        if (value > 0) {
            timeActive = value;
            if (GTValues.RNG.nextBoolean()) {
                causeRandomMaintenanceProblems();
                maintenanceMachine.setTaped(false);
            }
        }
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        if (hasMaintenanceProblems()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MAINTENANCE_BROKEN.reason());
                }
            }
            return null;
        }
        var durationMultiplier = getDurationMultiplier();
        if (durationMultiplier != 1) {
            recipe.duration = Math.max(1, (int) (recipe.duration * durationMultiplier));
        }
        return recipe;
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/common/machine/multiblock/part/MaintenanceHatchPartMachine;consumeDuctTape(Lnet/minecraftforge/items/IItemHandler;I)Z"), remap = false, cancellable = true)
    private void update(CallbackInfo ci) {
        DroneControlCenterMachine centerMachine = getNetMachine();
        if (centerMachine != null) {
            Drone drone = getFirstUsableDrone();
            if (drone != null && drone.start(10, getNumMaintenanceProblems() << 6, GTOValues.MAINTAINING)) {
                fixAllMaintenanceProblems();
                ci.cancel();
            }
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        removeNetMachineCache();
    }
}
