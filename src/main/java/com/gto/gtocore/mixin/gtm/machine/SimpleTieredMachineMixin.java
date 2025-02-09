package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.ILockableRecipe;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleTieredMachine.class)
public class SimpleTieredMachineMixin extends WorkableTieredMachine {

    public SimpleTieredMachineMixin(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return false;
    }

    @Inject(method = "attachConfigurators", at = @At(value = "TAIL"), remap = false)
    private void attachConfigurators(ConfiguratorPanel configuratorPanel, CallbackInfo ci) {
        ILockableRecipe.attachRecipeLockable(configuratorPanel, getRecipeLogic());
    }
}
