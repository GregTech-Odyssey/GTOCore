package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.IServerTickMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;

import net.minecraft.nbt.CompoundTag;

import com.hepdd.gtmthings.common.block.machine.multiblock.part.appeng.MEOutputPartMachine;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(WorkableMultiblockMachine.class)
public abstract class WorkableMultiblockMachineMixin extends MultiblockControllerMachine implements IWorkableMultiController, IMEOutputMachine, IServerTickMachine {

    @Shadow(remap = false)
    public abstract @NotNull GTRecipeType getRecipeType();

    @Shadow(remap = false)
    @Final
    protected List<ISubscription> traitSubscriptions;

    @Unique
    private boolean gTOCore$isItemOutput;

    @Unique
    private boolean gTOCore$isFluidOutput;

    @Unique
    private boolean gTOCore$isDualOutput;

    @Unique
    private boolean gTOCore$isGridOnline;

    @Unique
    private boolean gTOCore$isGrid;

    @Unique
    private Set<IGridConnectedMachine> gTOCore$grid;

    protected WorkableMultiblockMachineMixin(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean gtocore$cancel() {
        if (gTOCore$isGridOnline || !gTOCore$isGrid) return false;
        if (gTOCore$grid == null) return true;
        for (IGridConnectedMachine machine : gTOCore$grid) {
            if (!machine.isOnline()) return true;
        }
        gTOCore$isGridOnline = true;
        gTOCore$grid = null;
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        if (capability == ItemRecipeCapability.CAP && gTOCore$isItemOutput) {
            return true;
        } else if (capability == FluidRecipeCapability.CAP && gTOCore$isFluidOutput) {
            return true;
        }
        return self().getDefinition().getRecipeOutputLimits().containsKey(capability);
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        tag.putBoolean("isGrid", gTOCore$isGrid);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        gTOCore$isGrid = tag.getBoolean("isGrid");
    }

    @Inject(method = "onStructureFormed", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), remap = false)
    private void onContentChanges(CallbackInfo ci, @Local RecipeHandlerList list) {
        traitSubscriptions.add(list.subscribe(() -> {
            if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
                enhancedRecipeLogicMachine.onContentChanges(list);
            }
        }));
    }

    @Inject(method = "onStructureFormed", at = @At(value = "TAIL"), remap = false)
    private void onStructureFormed(CallbackInfo ci) {
        gTOCore$isGrid = false;
        for (IMultiPart part : getParts()) {
            if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
                enhancedRecipeLogicMachine.onPartScan(part);
            }
            if (part instanceof IGridConnectedMachine gridConnectedMachine) {
                if (gTOCore$grid == null) {
                    gTOCore$grid = new ObjectOpenHashSet<>();
                    gTOCore$isGrid = true;
                }
                gTOCore$grid.add(gridConnectedMachine);
            }
            if (gTOCore$isItemOutput && gTOCore$isFluidOutput) {
                gTOCore$isDualOutput = true;
                continue;
            }
            if (part instanceof MEOutputPartMachine) {
                gTOCore$isItemOutput = true;
                gTOCore$isFluidOutput = true;
                gTOCore$isDualOutput = true;
            } else if (part instanceof MEOutputBusPartMachine) {
                gTOCore$isItemOutput = true;
            } else if (part instanceof MEOutputHatchPartMachine) {
                gTOCore$isFluidOutput = true;
            }
        }
    }

    @Inject(method = "onStructureInvalid", at = @At(value = "TAIL"), remap = false)
    private void onStructureInvalid(CallbackInfo ci) {
        gTOCore$isItemOutput = false;
        gTOCore$isFluidOutput = false;
        gTOCore$isDualOutput = false;
    }

    @Override
    public boolean gTOCore$DualMEOutput(@NotNull GTRecipe recipe) {
        if (gTOCore$isDualOutput) return true;
        if (gTOCore$isItemOutput || recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, List.of()).isEmpty()) {
            return gTOCore$isFluidOutput || recipe.outputs.getOrDefault(FluidRecipeCapability.CAP, List.of()).isEmpty();
        }
        return false;
    }

    @Override
    public boolean gTOCore$DualMEOutput(boolean hasItem, boolean hasFluid) {
        if (gTOCore$isDualOutput) return true;
        if (gTOCore$isItemOutput || hasItem) {
            return gTOCore$isFluidOutput || hasFluid;
        }
        return false;
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return getDefinition().isAlwaysTryModifyRecipe();
    }

    @Override
    public boolean regressWhenWaiting() {
        return !getDefinition().isGenerator();
    }
}
