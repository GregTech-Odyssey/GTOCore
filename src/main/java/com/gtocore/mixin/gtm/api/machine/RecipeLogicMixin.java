package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.misc.AsyncTask;
import com.gtolib.api.recipe.*;
import com.gtolib.api.recipe.modifier.ParallelCache;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RecipeLogic.class, remap = false)
public abstract class RecipeLogicMixin extends MachineTrait implements IEnhancedRecipeLogic {

    @Unique
    private ParallelCache gtolib$parallelCache;
    @Unique
    private RecipeBuilder gtolib$recipeBuilder;
    @Unique
    private AsyncTask gtolib$asyncRecipeOutputTask;
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
    protected int status;

    @Shadow
    public abstract boolean checkMatchedRecipeAvailable(GTRecipe match);

    @Override
    public void setAsyncTask(AsyncTask task) {
        gtolib$asyncRecipeOutputTask = task;
    }

    @Override
    public AsyncTask getAsyncTask() {
        return gtolib$asyncRecipeOutputTask;
    }

    @Override
    public void onMachineUnLoad() {
        AsyncTask.removeAsyncTask(this);
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
        gtolib$parallelCache = new ParallelCache();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void findAndHandleRecipe() {
        lastRecipe = null;
        lastOriginRecipe = null;
        machine.getRecipeType().findRecipe(machine, match -> {
            var r = (Recipe) match;
            return RecipeRunner.checkTier(machine, r) && RecipeRunner.checkConditions(machine, r) && checkMatchedRecipeAvailable(r);
        });
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
            AsyncTask.addAsyncTask(this, () -> RecipeRunner.handleRecipe(machine, (Recipe) recipe, IO.OUT, contents, getChanceCaches(), false));
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
}
