package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;

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
}
