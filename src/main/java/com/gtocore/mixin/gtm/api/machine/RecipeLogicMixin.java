package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.*;
import com.gtolib.api.recipe.modifier.ParallelCache;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = RecipeLogic.class, remap = false)
public abstract class RecipeLogicMixin extends MachineTrait implements IEnhancedRecipeLogic {

    @Unique
    private Reference2ReferenceOpenHashMap<Recipe, RecipeHandlerList> gtolib$recipeCache;
    @Unique
    private ParallelCache gtolib$parallelCache;
    @Unique
    private RecipeBuilder gtolib$recipeBuilder;
    @Unique
    private AsyncRecipeOutputTask gtolib$asyncRecipeOutputTask;
    @Unique
    @DescSynced
    private Component gtolib$reason;

    protected RecipeLogicMixin(MetaMachine machine) {
        super(machine);
    }

    @Shadow
    @Final
    public IRecipeLogicMachine machine;

    @Shadow
    @Final
    protected Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches;

    @Shadow
    public abstract Map<RecipeCapability<?>, Object2IntMap<?>> getChanceCaches();

    @Shadow
    @Nullable
    protected GTRecipe lastRecipe;

    @Shadow
    @Nullable
    protected GTRecipe lastOriginRecipe;

    @Shadow
    public abstract void setupRecipe(GTRecipe recipe);

    @Shadow
    protected RecipeLogic.Status status;

    @Override
    public Reference2ReferenceOpenHashMap<Recipe, RecipeHandlerList> gtolib$getRecipeCache() {
        return gtolib$recipeCache;
    }

    @Override
    public void gtolib$setAsyncRecipeOutputTask(AsyncRecipeOutputTask task) {
        gtolib$asyncRecipeOutputTask = task;
    }

    @Override
    public AsyncRecipeOutputTask gtolib$getAsyncRecipeOutputTask() {
        return gtolib$asyncRecipeOutputTask;
    }

    @Override
    public void onMachineUnLoad() {
        AsyncRecipeOutputTask.removeAsyncLogic(this);
    }

    @Override
    public void gtolib$setIdleReason(Component reason) {
        this.gtolib$reason = reason;
    }

    @Override
    public Component gtolib$getIdleReason() {
        return gtolib$reason;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(IRecipeLogicMachine machine, CallbackInfo ci) {
        gtolib$recipeCache = new Reference2ReferenceOpenHashMap<>();
        gtolib$parallelCache = new ParallelCache();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @NotNull
    public Iterator<GTRecipe> searchRecipe() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void handleSearchingRecipes(@NotNull Iterator<GTRecipe> matches) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void findAndHandleRecipe() {
        lastRecipe = null;
        lastOriginRecipe = null;
        var matches = RecipeType.searchIterator(machine.getRecipeType(), machine, recipe -> RecipeRunner.checkTier(machine, recipe) && RecipeRunner.checkConditions(machine, recipe));
        while (matches.hasNext()) {
            GTRecipe match = matches.next();
            if (match == null) continue;
            var modified = machine.fullModifyRecipe(match.copy());
            if (modified != null) {
                if (matchRecipe(modified)) {
                    setupRecipe(modified);
                }
                if (lastRecipe != null && status == RecipeLogic.Status.WORKING) {
                    lastOriginRecipe = match;
                    return;
                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    protected boolean matchRecipe(GTRecipe r) {
        var recipe = (Recipe) r;
        return RecipeRunner.matchTickRecipe(machine, recipe) && RecipeRunner.matchRecipe(machine, recipe);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    protected boolean checkConditions(GTRecipe recipe) {
        return RecipeRunner.checkConditions(machine, (Recipe) recipe);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    protected boolean handleRecipeIO(GTRecipe recipe, IO io) {
        if (io == IO.OUT && machine instanceof IExtendedRecipeCapabilityHolder outputMachine && outputMachine.isDualMEOutput(recipe)) {
            var contents = new RecipeCapabilityMap<>(recipe.outputs);
            AsyncRecipeOutputTask.addAsyncLogic(this, () -> RecipeRunner.handleRecipe(machine, (Recipe) recipe, IO.OUT, contents, getChanceCaches(), false));
            return true;
        }
        return RecipeRunner.handleRecipeIO(machine, (Recipe) recipe, io, chanceCaches);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public boolean handleTickRecipe(GTRecipe recipe) {
        return RecipeRunner.handleTickRecipe(machine, (Recipe) recipe);
    }

    @Override
    public ParallelCache gtolib$getParallelCache() {
        return this.gtolib$parallelCache;
    }

    @Override
    public RecipeBuilder gtolib$getRecipeBuilder() {
        if (gtolib$recipeBuilder == null) {
            gtolib$recipeBuilder = RecipeBuilder.ofRaw();
        } else {
            gtolib$recipeBuilder.reset();
        }
        return gtolib$recipeBuilder;
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        if (forDrop) return;
        tag.putInt("difficulty", GTOCore.difficulty);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        if (tag.tags.get("difficulty") instanceof IntTag intTag && intTag.getAsInt() != GTOCore.difficulty) {
            throw new IllegalStateException("Difficulty mismatch");
        }
    }
}
