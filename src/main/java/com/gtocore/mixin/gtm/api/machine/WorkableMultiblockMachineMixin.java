package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;
import com.gtolib.api.machine.feature.multiblock.IEnhancedMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.server.level.ServerLevel;

import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorkableMultiblockMachine.class)
public abstract class WorkableMultiblockMachineMixin extends MultiblockControllerMachine implements IWorkInSpaceMachine {

    @Shadow(remap = false)
    @Final
    protected List<ISubscription> traitSubscriptions;

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

    protected WorkableMultiblockMachineMixin(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return getVoidingMode().canVoid(capability);
    }

    @Inject(method = "onStructureFormed", at = @At("TAIL"), remap = false)
    private void onPartScan(CallbackInfo ci) {
        for (var part : parts) {
            if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
                enhancedRecipeLogicMachine.onPartScan(part);
            }
        }
    }

    @Override
    public void addHandlerList(RecipeHandlerUnit handler) {
        if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine && (handler.itemHandlers.length > 0 || handler.fluidHandlers.length > 0)) {
            traitSubscriptions.add(handler.subscribe(() -> enhancedRecipeLogicMachine.onContentChanges(handler)));
            if (getLevel() instanceof ServerLevel serverLevel) {
                TaskHandler.enqueueTask(serverLevel, () -> enhancedRecipeLogicMachine.onContentChanges(handler), 0);
            }
        }
    }
}
