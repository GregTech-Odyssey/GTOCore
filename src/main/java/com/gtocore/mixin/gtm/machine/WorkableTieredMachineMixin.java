package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

@org.spongepowered.asm.mixin.Mixin(com.gregtechceu.gtceu.api.machine.WorkableTieredMachine.class)
public abstract class WorkableTieredMachineMixin implements IWorkInSpaceMachine, IEnhancedRecipeLogicMachine {

    @Unique
    private ISpaceWorkspaceMachine gto$workspaceProvider;

    @Override
    public ISpaceWorkspaceMachine getWorkspaceProvider() {
        return gto$workspaceProvider;
    }

    @Override
    public void setWorkspaceProvider(ISpaceWorkspaceMachine iSpaceWorkspaceMachine) {
        gto$workspaceProvider = iSpaceWorkspaceMachine;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (PlanetApi.API.isSpace(self().getLevel()) && !this.canWorkInSpace()) {
            getEnhancedRecipeLogic().gtolib$setIdleReason(IdleReason.CANNOT_WORK_IN_SPACE.reason());
            return false;
        }
        return true;
    }
}
