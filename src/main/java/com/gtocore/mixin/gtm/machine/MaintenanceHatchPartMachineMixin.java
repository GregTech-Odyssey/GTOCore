package com.gtocore.mixin.gtm.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.feature.IDroneInteractionMachine;
import com.gtolib.api.machine.feature.multiblock.IDroneControlCenterMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.misc.Drone;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableTieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MaintenanceHatchPartMachine;

import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaintenanceHatchPartMachine.class)
public abstract class MaintenanceHatchPartMachineMixin extends WorkableTieredPartMachine implements IMaintenanceMachine, IDroneInteractionMachine {

    @Shadow(remap = false)
    protected int timeActive;
    @Unique
    private IDroneControlCenterMachine gtolib$cache;

    protected MaintenanceHatchPartMachineMixin(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Shadow(remap = false)
    public abstract void fixAllMaintenanceProblems();

    @Unique
    @SuppressWarnings("all")
    public IDroneControlCenterMachine getNetMachineCache() {
        return gtolib$cache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setNetMachineCache(IDroneControlCenterMachine cache) {
        gtolib$cache = cache;
    }

    @Override
    public void calculateMaintenance(IMaintenanceMachine maintenanceMachine, int duration) {
        if (maintenanceMachine.isFullAuto()) return;
        var pa = getController().getParts().length;
        timeActive = MathUtil.saturatedCast((long) (timeActive + (duration * getDurationMultiplier() * GTOCore.difficulty * pa)));
        var value = ((float) timeActive / MINIMUM_MAINTENANCE_TIME) - 0.7;
        if (GTValues.RNG.nextFloat() <= value && !GTOCore.isEasy()) {
            timeActive = 0;
            causeRandomMaintenanceProblems();
            maintenanceMachine.setTaped(false);
        }
    }

    @Inject(method = "fixMaintenanceProblems", at = @At("HEAD"), remap = false)
    private void fixMaintenanceProblems(Player entityPlayer, CallbackInfo ci) {
        timeActive = 0;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable GTRecipe modifyRecipe(IWorkableMultiController controller, @NotNull GTRecipe recipe) {
        if (hasMaintenanceProblems()) {
            ((IEnhancedRecipeLogic) controller.getRecipeLogic()).gtolib$setIdleReason(IdleReason.MAINTENANCE_BROKEN.reason());
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
        IDroneControlCenterMachine centerMachine = getNetMachine();
        if (centerMachine != null) {
            var eu = getNumMaintenanceProblems() << 6;
            Drone drone = getFirstUsableDrone(d -> d.getCharge() >= eu);
            if (drone != null && drone.start(10, eu, GTOValues.MAINTAINING)) {
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
