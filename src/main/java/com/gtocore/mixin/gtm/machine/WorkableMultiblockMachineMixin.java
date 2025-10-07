package com.gtocore.mixin.gtm.machine;

import com.gtocore.common.machine.multiblock.part.ae.MEDualOutputPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEOutputBusPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEOutputHatchPartMachine;

import com.gtolib.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.server.level.ServerLevel;

import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(WorkableMultiblockMachine.class)
public abstract class WorkableMultiblockMachineMixin extends MultiblockControllerMachine implements IWorkableMultiController, IExtendedRecipeCapabilityHolder {

    @Shadow(remap = false)
    @Final
    protected List<ISubscription> traitSubscriptions;
    @Unique
    private List<RecipeHandlerList> gtolib$distinct;
    @Unique
    private Map<RecipeCapability<?>, List<IRecipeHandler<?>>> gtolib$input;
    @Unique
    private List<RecipeHandlerList> gtolib$output;
    @Unique
    private boolean gtolib$isItemOutput;
    @Unique
    private boolean gtolib$isFluidOutput;
    @Unique
    private boolean gtolib$isDualOutput;
    @Unique
    private Int2ReferenceOpenHashMap<RecipeHandlerList> gtolib$outputColorMap;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(MetaMachineBlockEntity holder, Object[] args, CallbackInfo ci) {
        gtolib$distinct = Collections.emptyList();
        gtolib$input = Map.of();
        gtolib$output = Collections.emptyList();
        gtolib$outputColorMap = new Int2ReferenceOpenHashMap<>();
    }

    protected WorkableMultiblockMachineMixin(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        if (gtolib$isDualOutput || getVoidingMode().canVoid(capability)) return true;
        if (capability == ItemRecipeCapability.CAP) {
            return gtolib$isItemOutput;
        } else return gtolib$isFluidOutput;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void onStructureFormedAfter() {
        super.onStructureFormedAfter();
        this.arrangeAll();
    }

    @Redirect(method = "onStructureFormed", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/machine/feature/multiblock/IMultiPart;getRecipeHandlers()Ljava/util/List;"), remap = false)
    private List<RecipeHandlerList> gtolib$getRecipeHandlers(IMultiPart part) {
        if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
            enhancedRecipeLogicMachine.onPartScan(part);
        }
        if (gtolib$isItemOutput && gtolib$isFluidOutput) {
            gtolib$isDualOutput = true;
        } else {
            if (part instanceof MEDualOutputPartMachine) {
                gtolib$isItemOutput = true;
                gtolib$isFluidOutput = true;
                gtolib$isDualOutput = true;
            } else if (part instanceof MEOutputBusPartMachine) {
                gtolib$isItemOutput = true;
            } else if (part instanceof MEOutputHatchPartMachine) {
                gtolib$isFluidOutput = true;
            }
        }
        return part.getRecipeHandlers();
    }

    @Inject(method = "onStructureInvalid", at = @At("TAIL"), remap = false)
    private void onStructureInvalid(CallbackInfo ci) {
        gtolib$isItemOutput = false;
        gtolib$isFluidOutput = false;
        gtolib$isDualOutput = false;
    }

    @Override
    @SuppressWarnings("all")
    public boolean isDualMEOutput(@NotNull GTRecipe recipe) {
        if (gtolib$isDualOutput) return true;
        if (gtolib$isItemOutput || recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {
            return gtolib$isFluidOutput || recipe.outputs.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty();
        }
        return false;
    }

    @Override
    public void addHandlerList(RecipeHandlerList handler) {
        if (handler == RecipeHandlerList.NO_DATA) return;
        IO io = handler.getHandlerIO();
        getCapabilitiesProxy().computeIfAbsent(io, i -> new ObjectArrayList<>()).add(handler);
        var inner = getCapabilitiesFlat().computeIfAbsent(io, i -> new Reference2ObjectOpenHashMap<>(handler.handlerMap.size()));
        for (ObjectIterator<Reference2ObjectMap.Entry<RecipeCapability<?>, List<IRecipeHandler<?>>>> it = handler.handlerMap.reference2ObjectEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            var entryList = entry.getValue();
            inner.computeIfAbsent(entry.getKey(), c -> new ObjectArrayList<>(entryList.size())).addAll(entryList);
        }
        if (this instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine && (handler.hasCapability(ItemRecipeCapability.CAP) || handler.hasCapability(FluidRecipeCapability.CAP))) {
            traitSubscriptions.add(handler.subscribe(() -> enhancedRecipeLogicMachine.onContentChanges(handler)));
            if (getLevel() instanceof ServerLevel serverLevel) {
                TaskHandler.enqueueServerTask(serverLevel, () -> enhancedRecipeLogicMachine.onContentChanges(handler), 0);
            }
        }
    }

    @Override
    public Int2ReferenceOpenHashMap<RecipeHandlerList> gtolib$getOutputColorMap() {
        return this.gtolib$outputColorMap;
    }

    @Override
    public void gtolib$setInput(final List<RecipeHandlerList> distinct) {
        this.gtolib$distinct = distinct;
    }

    @Override
    public @NotNull List<RecipeHandlerList> gtolib$getInput() {
        return this.gtolib$distinct;
    }

    @Override
    public void gtolib$setInputFlat(final Map<RecipeCapability<?>, List<IRecipeHandler<?>>> input) {
        this.gtolib$input = input;
    }

    @Override
    public @NotNull Map<RecipeCapability<?>, List<IRecipeHandler<?>>> gtolib$getInputFlat() {
        return this.gtolib$input;
    }

    @Override
    public void gtolib$setOutput(final List<RecipeHandlerList> output) {
        this.gtolib$output = output;
    }

    @Override
    public @NotNull List<RecipeHandlerList> gtolib$getOutput() {
        return this.gtolib$output;
    }
}
