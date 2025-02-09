package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.IMEOutputMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.appeng.MEOutputPartMachine;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorkableMultiblockMachine.class)
public abstract class WorkableMultiblockMachineMixin extends MultiblockControllerMachine implements IWorkableMultiController, IMEOutputMachine {

    @Unique
    private boolean gTOCore$isItemOutput;

    @Unique
    private boolean gTOCore$isFluidOutput;

    protected WorkableMultiblockMachineMixin(IMachineBlockEntity holder) {
        super(holder);
    }

    @Redirect(method = "onStructureFormed", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/machine/trait/IRecipeHandlerTrait;addChangedListener(Ljava/lang/Runnable;)Lcom/lowdragmc/lowdraglib/syncdata/ISubscription;", ordinal = 0), remap = false)
    private ISubscription onContentChanges(IRecipeHandlerTrait<?> instance, Runnable runnable) {
        return instance.addChangedListener(() -> {
            getRecipeLogic().updateTickSubscription();
            if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
                enhancedRecipeLogicMachine.onContentChanges();
            }
        });
    }

    @Inject(method = "onStructureFormed", at = @At(value = "INVOKE", target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private void onStructureFormed(CallbackInfo ci, @Local IMultiPart part) {
        if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
            enhancedRecipeLogicMachine.onPartScan(part);
        }
        if (gTOCore$isItemOutput && gTOCore$isFluidOutput) return;
        if (part instanceof MEOutputPartMachine) {
            gTOCore$isItemOutput = true;
            gTOCore$isFluidOutput = true;
        } else if (part instanceof MEOutputBusPartMachine) {
            gTOCore$isItemOutput = true;
        } else if (part instanceof MEOutputHatchPartMachine) {
            gTOCore$isFluidOutput = true;
        }
    }

    @Inject(method = "onStructureInvalid", at = @At(value = "TAIL"), remap = false)
    private void onStructureInvalid(CallbackInfo ci) {
        gTOCore$isItemOutput = false;
        gTOCore$isFluidOutput = false;
    }

    @Override
    public boolean gTOCore$isItemOutput() {
        return gTOCore$isItemOutput;
    }

    @Override
    public boolean gTOCore$isFluidOutput() {
        return gTOCore$isFluidOutput;
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return getDefinition().isAlwaysTryModifyRecipe();
    }

    @Override
    public boolean dampingWhenWaiting() {
        return !getDefinition().isGenerator();
    }
}
