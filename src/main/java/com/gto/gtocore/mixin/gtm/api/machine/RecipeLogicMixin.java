package com.gto.gtocore.mixin.gtm.api.machine;

import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;
import com.gto.gtocore.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.recipe.AsyncRecipeOutputTask;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(value = RecipeLogic.class, remap = false)
public abstract class RecipeLogicMixin extends MachineTrait implements IEnhancedRecipeLogic {

    @Unique
    private Object2LongOpenCustomHashMap<ItemStack> gtocore$parallelItemMap;

    @Unique
    private Object2LongOpenCustomHashMap<ItemStack> gtocore$itemMap;

    @Unique
    private Object2LongOpenCustomHashMap<ItemStack> gtocore$itemIngredientStacks;

    @Unique
    private Object2IntOpenHashMap<Ingredient> gtocore$itemNotConsumableMap;

    @Unique
    private Object2IntOpenHashMap<Ingredient> gtocore$itemConsumableMap;

    @Unique
    private Object2LongOpenHashMap<FluidStack> gtocore$parallelFluidMap;

    @Unique
    private Object2LongOpenHashMap<FluidStack> gtocore$fluidIngredientStacks;

    @Unique
    private Object2LongOpenHashMap<FluidStack> gtocore$fluidMap;

    @Unique
    private Object2IntOpenHashMap<FluidIngredient> gtocore$fluidNotConsumableMap;

    @Unique
    private Object2IntOpenHashMap<FluidIngredient> gtocore$fluidConsumableMap;

    @Unique
    private int gtocore$lastParallel;

    @Unique
    private boolean gtocore$modifyRecipe;

    @Unique
    private AsyncRecipeSearchTask gtocore$asyncRecipeSearchTask;

    @Unique
    private AsyncRecipeOutputTask gtocore$asyncRecipeOutputTask;

    @Unique
    @Persisted(key = "lockRecipe")
    private boolean gTOCore$lockRecipe;

    @Unique
    @Persisted(key = "originRecipe")
    private GTRecipe gTOCore$originRecipe;

    @Unique
    private int gtocore$interval = 5;

    @Shadow
    @Nullable
    protected GTRecipe lastRecipe;

    protected RecipeLogicMixin(MetaMachine machine) {
        super(machine);
    }

    @Shadow
    public abstract void setStatus(RecipeLogic.Status status);

    @Shadow
    @Final
    public IRecipeLogicMachine machine;

    @Shadow
    public abstract void interruptRecipe();

    @Shadow
    protected int progress;

    @Shadow
    protected long totalContinuousRunningTime;

    @Shadow
    public abstract void setWaiting(@Nullable Component reason);

    @Shadow
    public abstract boolean isWaiting();

    @Shadow
    public abstract RecipeLogic.Status getStatus();

    @Shadow
    public abstract boolean isSuspend();

    @Shadow
    public abstract boolean isIdle();

    @Shadow
    protected int duration;
    @Shadow
    public List<GTRecipe> lastFailedMatches;

    @Shadow
    protected TickableSubscription subscription;

    @Shadow
    @Nullable
    protected GTRecipe lastOriginRecipe;

    @Shadow
    public abstract void setupRecipe(GTRecipe recipe);

    @Shadow
    protected boolean recipeDirty;

    @Shadow
    protected abstract void regressRecipe();

    @Shadow
    public abstract Iterator<GTRecipe> searchRecipe();

    @Shadow
    protected abstract void handleSearchingRecipes(Iterator<GTRecipe> matches);

    @Shadow
    protected int consecutiveRecipes;

    @Shadow
    @Final
    protected Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches;

    @Shadow
    public abstract void markLastRecipeDirty();

    @Shadow
    protected boolean suspendAfterFinish;

    @Shadow
    protected boolean isActive;

    @Shadow
    public abstract boolean isActive();

    @Shadow
    public abstract void updateTickSubscription();

    @Unique
    private void gTOCore$unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Unique
    private boolean gTOCore$successfullyRecipe(GTRecipe originrecipe) {
        if (lastRecipe != null && getStatus() == RecipeLogic.Status.WORKING) {
            lastOriginRecipe = originrecipe;
            lastFailedMatches = null;
            gtocore$interval = 5;
            if (gTOCore$lockRecipe) gTOCore$originRecipe = originrecipe;
            return true;
        }
        return false;
    }

    @Override
    public Object2LongOpenCustomHashMap<ItemStack> gtocore$getParallelItemMap() {
        if (gtocore$parallelItemMap == null) {
            gtocore$parallelItemMap = IEnhancedRecipeLogic.super.gtocore$getParallelItemMap();
        } else {
            gtocore$parallelItemMap.clear();
        }
        return gtocore$parallelItemMap;
    }

    @Override
    public Object2LongOpenCustomHashMap<ItemStack> gtocore$getItemMap() {
        if (gtocore$itemMap == null) {
            gtocore$itemMap = IEnhancedRecipeLogic.super.gtocore$getItemMap();
        } else {
            gtocore$itemMap.clear();
        }
        return gtocore$itemMap;
    }

    @Override
    public Object2LongOpenCustomHashMap<ItemStack> gtocore$getItemIngredientStacks() {
        if (gtocore$itemIngredientStacks == null) {
            gtocore$itemIngredientStacks = IEnhancedRecipeLogic.super.gtocore$getItemIngredientStacks();
        } else {
            gtocore$itemIngredientStacks.clear();
        }
        return gtocore$itemIngredientStacks;
    }

    @Override
    public Object2IntOpenHashMap<Ingredient> gtocore$getItemNotConsumableMap() {
        if (gtocore$itemNotConsumableMap == null) {
            gtocore$itemNotConsumableMap = IEnhancedRecipeLogic.super.gtocore$getItemNotConsumableMap();
        } else {
            gtocore$itemNotConsumableMap.clear();
        }
        return gtocore$itemNotConsumableMap;
    }

    @Override
    public Object2IntOpenHashMap<Ingredient> gtocore$getItemConsumableMap() {
        if (gtocore$itemConsumableMap == null) {
            gtocore$itemConsumableMap = IEnhancedRecipeLogic.super.gtocore$getItemConsumableMap();
        } else {
            gtocore$itemConsumableMap.clear();
        }
        return gtocore$itemConsumableMap;
    }

    @Override
    public Object2LongOpenHashMap<FluidStack> gtocore$getParallelFluidMap() {
        if (gtocore$parallelFluidMap == null) {
            gtocore$parallelFluidMap = IEnhancedRecipeLogic.super.gtocore$getParallelFluidMap();
        } else {
            gtocore$parallelFluidMap.clear();
        }
        return gtocore$parallelFluidMap;
    }

    @Override
    public Object2LongOpenHashMap<FluidStack> gtocore$getFluidMap() {
        if (gtocore$fluidMap == null) {
            gtocore$fluidMap = IEnhancedRecipeLogic.super.gtocore$getFluidMap();
        } else {
            gtocore$fluidMap.clear();
        }
        return gtocore$fluidMap;
    }

    @Override
    public Object2LongOpenHashMap<FluidStack> gtocore$getFluidIngredientStacks() {
        if (gtocore$fluidIngredientStacks == null) {
            gtocore$fluidIngredientStacks = IEnhancedRecipeLogic.super.gtocore$getFluidIngredientStacks();
        } else {
            gtocore$fluidIngredientStacks.clear();
        }
        return gtocore$fluidIngredientStacks;
    }

    @Override
    public Object2IntOpenHashMap<FluidIngredient> gtocore$getFluidNotConsumableMap() {
        if (gtocore$fluidNotConsumableMap == null) {
            gtocore$fluidNotConsumableMap = IEnhancedRecipeLogic.super.gtocore$getFluidNotConsumableMap();
        } else {
            gtocore$fluidNotConsumableMap.clear();
        }
        return gtocore$fluidNotConsumableMap;
    }

    @Override
    public Object2IntOpenHashMap<FluidIngredient> gtocore$getFluidConsumableMap() {
        if (gtocore$fluidConsumableMap == null) {
            gtocore$fluidConsumableMap = IEnhancedRecipeLogic.super.gtocore$getFluidConsumableMap();
        } else {
            gtocore$fluidConsumableMap.clear();
        }
        return gtocore$fluidConsumableMap;
    }

    @Override
    public void gtocore$cleanParallelMap() {
        if (gtocore$parallelItemMap != null) gtocore$parallelItemMap.clear();
        if (gtocore$itemIngredientStacks != null) gtocore$itemIngredientStacks.clear();
        if (gtocore$itemNotConsumableMap != null) gtocore$itemNotConsumableMap.clear();
        if (gtocore$itemConsumableMap != null) gtocore$itemConsumableMap.clear();
        if (gtocore$parallelFluidMap != null) gtocore$parallelFluidMap.clear();
        if (gtocore$fluidIngredientStacks != null) gtocore$fluidIngredientStacks.clear();
        if (gtocore$fluidNotConsumableMap != null) gtocore$fluidNotConsumableMap.clear();
        if (gtocore$fluidConsumableMap != null) gtocore$fluidConsumableMap.clear();
    }

    @Override
    public int gtocore$getlastParallel() {
        return gtocore$lastParallel;
    }

    @Override
    public void gtocore$setModifyRecipe() {
        gtocore$modifyRecipe = true;
    }

    @Override
    public boolean gtocore$hasAsyncTask() {
        return gtocore$asyncRecipeSearchTask != null && gtocore$asyncRecipeSearchTask.isHasTask();
    }

    @Override
    public void gtocore$setAsyncRecipeSearchTask(AsyncRecipeSearchTask task) {
        gtocore$asyncRecipeSearchTask = task;
    }

    @Override
    public AsyncRecipeSearchTask gtocore$getAsyncRecipeSearchTask() {
        return gtocore$asyncRecipeSearchTask;
    }

    @Override
    public void gtocore$setAsyncRecipeOutputTask(AsyncRecipeOutputTask task) {
        gtocore$asyncRecipeOutputTask = task;
    }

    @Override
    public AsyncRecipeOutputTask gtocore$getAsyncRecipeOutputTask() {
        return gtocore$asyncRecipeOutputTask;
    }

    @Override
    public void onMachineUnLoad() {
        AsyncRecipeSearchTask.removeAsyncLogic(getLogic());
        AsyncRecipeOutputTask.removeAsyncLogic(getLogic());
    }

    @Override
    public void gTOCore$setLockRecipe(boolean look) {
        gTOCore$lockRecipe = look;
        gTOCore$originRecipe = null;
        updateTickSubscription();
    }

    @Override
    public boolean gTOCore$isLockRecipe() {
        return gTOCore$lockRecipe;
    }

    @OnlyIn(Dist.CLIENT)
    @Redirect(method = "updateSound", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;getSound()Lcom/gregtechceu/gtceu/api/sound/SoundEntry;"), remap = false)
    private SoundEntry updateSound(GTRecipeType instance) {
        SoundEntry sound = null;
        if (machine instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
            sound = enhancedRecipeLogicMachine.getSound();
        }
        if (sound == null) sound = instance.getSound();
        return sound;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void serverTick() {
        if (isSuspend()) {
            gTOCore$unsubscribe();
        } else {
            if (!isIdle() && lastRecipe != null) {
                if (progress < duration) {
                    handleRecipeWorking();
                } else {
                    if (machine instanceof IEnhancedMultiblockMachine enhancedRecipeLogicMachine) {
                        enhancedRecipeLogicMachine.onRecipeFinish();
                    }
                    onRecipeFinish();
                }
            } else if (lastRecipe != null) {
                findAndHandleRecipe();
            } else if (gtocore$hasAsyncTask() || getMachine().getOffsetTimer() % gtocore$interval == 0) {
                boolean hasResult = false;
                if (gtocore$hasAsyncTask() && gtocore$asyncRecipeSearchTask.getResult() != null) {
                    AsyncRecipeSearchTask.Result result = gtocore$asyncRecipeSearchTask.getResult();
                    if (result.recipe() != null) {
                        setupRecipe(result.modified());
                        if (gTOCore$successfullyRecipe(result.recipe())) {
                            recipeDirty = false;
                            gtocore$asyncRecipeSearchTask.clean();
                            return;
                        }
                    }
                    hasResult = true;
                    gtocore$asyncRecipeSearchTask.clean();
                }
                if (lastFailedMatches != null) {
                    for (GTRecipe match : lastFailedMatches) {
                        if (checkMatchedRecipeAvailable(match)) {
                            recipeDirty = false;
                            return;
                        }
                    }
                }
                if (!hasResult) findAndHandleRecipe();
                if (!gtocore$hasAsyncTask() && lastRecipe == null && isIdle() && !machine.keepSubscribing() && !recipeDirty && lastFailedMatches == null) {
                    if (gtocore$interval < GTOConfig.INSTANCE.recipeMaxCheckInterval) {
                        gtocore$interval <<= 1;
                    }
                    gTOCore$unsubscribe();
                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public boolean checkMatchedRecipeAvailable(GTRecipe match) {
        GTRecipe modified = machine.fullModifyRecipe(match.copy());
        if (modified != null) {
            if (RecipeRunnerHelper.check(machine, modified)) {
                setupRecipe(modified);
            }
            return gTOCore$successfullyRecipe(match);
        }
        return false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void findAndHandleRecipe() {
        lastFailedMatches = null;
        if (!recipeDirty && RecipeRunnerHelper.check(machine, lastRecipe)) {
            GTRecipe recipe = lastRecipe;
            lastRecipe = null;
            lastOriginRecipe = null;
            setupRecipe(recipe);
        } else {
            lastRecipe = null;
            if (gTOCore$lockRecipe && gTOCore$originRecipe != null) {
                lastOriginRecipe = gTOCore$originRecipe;
                GTRecipe modified = machine.fullModifyRecipe(lastOriginRecipe.copy());
                if (RecipeRunnerHelper.check(machine, modified)) {
                    setupRecipe(modified);
                }
            } else {
                lastOriginRecipe = null;
                if (GTOConfig.INSTANCE.asyncRecipeSearch && getLogic().getClass() == RecipeLogic.class) {
                    if (!gtocore$hasAsyncTask()) {
                        AsyncRecipeSearchTask.addAsyncLogic(getLogic());
                    }
                } else {
                    handleSearchingRecipes(searchRecipe());
                }
            }
        }
        recipeDirty = false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void onRecipeFinish() {
        machine.afterWorking();
        gtocore$lastParallel = 1;
        if (lastRecipe != null) {
            consecutiveRecipes++;
            handleRecipeIO(lastRecipe, IO.OUT);
            if (machine instanceof IDistinctRecipeHolder recipeHolder) recipeHolder.setDistinctState(true);
            if (machine.alwaysTryModifyRecipe() || gtocore$modifyRecipe) {
                if (lastOriginRecipe != null) {
                    if (gtocore$modifyRecipe || !(GTORecipeModifiers.TRY_AGAIN.contains(getMachine().getDefinition().getRecipeModifier()) && lastRecipe.parallels == MachineUtils.getHatchParallel(getMachine()) && RecipeRunnerHelper.checkConditions(machine, lastRecipe) && RecipeRunnerHelper.matchRecipe(machine, lastRecipe) && RecipeRunnerHelper.matchTickRecipe(machine, lastRecipe))) {
                        gtocore$lastParallel = lastRecipe.parallels;
                        var modified = machine.fullModifyRecipe(lastOriginRecipe.copy());
                        if (modified == null) {
                            markLastRecipeDirty();
                        } else {
                            lastRecipe = modified;
                        }
                    }
                } else {
                    markLastRecipeDirty();
                }
                gtocore$modifyRecipe = false;
            }
            if (!recipeDirty && !suspendAfterFinish && RecipeRunnerHelper.check(machine, lastRecipe)) {
                setupRecipe(lastRecipe);
            } else {
                if (suspendAfterFinish) {
                    setStatus(RecipeLogic.Status.SUSPEND);
                    suspendAfterFinish = false;
                } else {
                    setStatus(RecipeLogic.Status.IDLE);
                }
                consecutiveRecipes = 0;
                progress = 0;
                duration = 0;
                isActive = false;
            }
            if (machine instanceof IDistinctRecipeHolder recipeHolder) recipeHolder.setDistinctState(false);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
        if (io == IO.OUT && GTOConfig.INSTANCE.asyncRecipeOutput && machine instanceof IMEOutputMachine outputMachine && outputMachine.gTOCore$DualMEOutput(recipe)) {
            AsyncRecipeOutputTask.addAsyncLogic(getLogic(), () -> RecipeRunnerHelper.handleRecipeOutput(machine, recipe));
            return ActionResult.SUCCESS;
        }
        if (RecipeRunnerHelper.handleRecipeIO(machine, recipe, io, chanceCaches)) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS_NO_CONTENTS;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public void handleRecipeWorking() {
        assert lastRecipe != null;
        if (gtocore$handleTickRecipe(lastRecipe)) {
            if (!machine.onWorking()) {
                interruptRecipe();
                return;
            }
            setStatus(RecipeLogic.Status.WORKING);
            progress++;
            totalContinuousRunningTime++;
        } else {
            setWaiting(null);
        }
        if (isWaiting()) {
            if (machine instanceof IEnhancedMultiblockMachine enhancedMultiblockMachine) {
                enhancedMultiblockMachine.doDamping(getLogic());
            } else {
                regressRecipe();
            }
        }
    }

    @Unique
    private boolean gtocore$handleTickRecipe(GTRecipe recipe) {
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : recipe.tickInputs.entrySet()) {
            if (RecipeRunnerHelper.handleTickRecipe(machine, IO.IN, recipe, entry.getValue(), entry.getKey())) {
                return false;
            }
        }
        for (Map.Entry<RecipeCapability<?>, List<Content>> entry : recipe.tickOutputs.entrySet()) {
            if (RecipeRunnerHelper.handleTickRecipe(machine, IO.OUT, recipe, entry.getValue(), entry.getKey())) {
                return false;
            }
        }
        return true;
    }
}
