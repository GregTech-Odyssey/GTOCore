package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.feature.ISpaceWorkspaceMachine;
import com.gtolib.api.machine.feature.IWorkInSpaceMachine;
import com.gtolib.api.machine.feature.multiblock.IEnhancedMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.info.RecipeInfo;
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

import java.util.ArrayList;
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
    public boolean canVoidRecipeOutputs(RecipeInfo capability) {
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
    public void addHandlerList(RecipeHandlerUnit unit) {
        if (unit == RecipeHandlerUnit.NO_DATA || unit.handlerIO == IO.NONE || unit.allHandlers.length == 0) return;
        getCapabilitiesProxy().computeIfAbsent(unit.handlerIO, i -> new ArrayList<>()).add(unit);
        var list = getCapabilitiesFlat().computeIfAbsent(unit.handlerIO, i -> new ArrayList<>());
        for (var handler : unit.allHandlers) {
            if (list.contains(handler)) continue;
            list.add(handler);
        }
        if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine && (unit.itemHandlers.length > 0 || unit.fluidHandlers.length > 0)) {
            traitSubscriptions.add(unit.subscribe(() -> enhancedRecipeLogicMachine.onContentChanges(unit)));
            if (getLevel() instanceof ServerLevel serverLevel) {
                TaskHandler.enqueueTask(serverLevel, () -> enhancedRecipeLogicMachine.onContentChanges(unit));
            }
        }
    }
}
