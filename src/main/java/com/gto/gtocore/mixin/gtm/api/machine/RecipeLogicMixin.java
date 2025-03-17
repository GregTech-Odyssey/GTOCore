package com.gto.gtocore.mixin.gtm.api.machine;

import com.gto.gtocore.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(value = RecipeLogic.class, remap = false)
public abstract class RecipeLogicMixin extends MachineTrait implements IEnhancedRecipeLogic {

    @Unique
    private AsyncRecipeSearchTask gtocore$asyncRecipeSearchTask;

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
    public abstract GTRecipe.ActionResult handleTickRecipe(GTRecipe recipe);

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
    private RecipeLogic.Status status;

    @Shadow
    public abstract boolean isSuspend();

    @Shadow
    public abstract boolean isIdle();

    @Shadow
    protected int duration;

    @Shadow
    public abstract void onRecipeFinish();

    @Shadow
    public abstract boolean checkMatchedRecipeAvailable(GTRecipe match);

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
    public abstract void updateTickSubscription();

    @Shadow
    protected abstract void doDamping();

    @Shadow
    public abstract Iterator<GTRecipe> searchRecipe();

    @Shadow
    protected abstract void handleSearchingRecipes(Iterator<GTRecipe> matches);

    @Unique
    private void gTOCore$unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
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
    public void onMachineUnLoad() {
        AsyncRecipeSearchTask.removeAsyncLogic(getLogic());
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
    @Overwrite(remap = false)
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
                if (lastFailedMatches != null) {
                    for (GTRecipe match : lastFailedMatches) {
                        if (checkMatchedRecipeAvailable(match)) {
                            if (gTOCore$lockRecipe) gTOCore$originRecipe = lastOriginRecipe;
                            break;
                        }
                    }
                }
                findAndHandleRecipe();
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
    @Overwrite(remap = false)
    public void findAndHandleRecipe() {
        lastFailedMatches = null;
        if (!recipeDirty && lastRecipe != null && RecipeRunner.matchRecipe(machine, lastRecipe) && RecipeRunner.matchTickRecipe(machine, lastRecipe) && lastRecipe.checkConditions(getLogic()).isSuccess()) {
            GTRecipe recipe = lastRecipe;
            lastRecipe = null;
            lastOriginRecipe = null;
            setupRecipe(recipe);
        } else {
            lastRecipe = null;
            if (gTOCore$lockRecipe && gTOCore$originRecipe != null) {
                lastOriginRecipe = gTOCore$originRecipe;
                GTRecipe modified = machine.fullModifyRecipe(lastOriginRecipe.copy());
                if (modified != null && RecipeRunner.matchRecipe(machine, modified) && RecipeRunner.matchTickRecipe(machine, modified) && modified.checkConditions(getLogic()).isSuccess()) {
                    setupRecipe(modified);
                }
            } else {
                lastOriginRecipe = null;
                if (canLockRecipe()) {
                    if (gtocore$hasAsyncTask()) {
                        if (gtocore$asyncRecipeSearchTask.getResult() != null) {
                            AsyncRecipeSearchTask.IResult result = gtocore$asyncRecipeSearchTask.getResult();
                            if (result.recipe() != null) {
                                setupRecipe(result.modified());
                                if (lastRecipe != null && getStatus() == RecipeLogic.Status.WORKING) {
                                    lastOriginRecipe = result.recipe();
                                    lastFailedMatches = null;
                                    if (gTOCore$lockRecipe) gTOCore$originRecipe = lastOriginRecipe;
                                }
                            }
                            gtocore$asyncRecipeSearchTask.clean();
                        }
                    } else {
                        AsyncRecipeSearchTask.addAsyncLogic(getLogic());
                    }
                } else {
                    handleSearchingRecipes(searchRecipe());
                    if (gTOCore$lockRecipe) gTOCore$originRecipe = lastOriginRecipe;
                }
            }
        }
        recipeDirty = false;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void handleRecipeWorking() {
        RecipeLogic.Status last = status;
        assert lastRecipe != null;
        GTRecipe.ActionResult result = handleTickRecipe(lastRecipe);
        if (result.isSuccess()) {
            if (!machine.onWorking()) {
                interruptRecipe();
                return;
            }
            setStatus(RecipeLogic.Status.WORKING);
            progress++;
            totalContinuousRunningTime++;
        } else {
            setWaiting(result.reason().get());
        }
        if (isWaiting() && machine.dampingWhenWaiting()) {
            if (machine instanceof IEnhancedMultiblockMachine enhancedMultiblockMachine) {
                enhancedMultiblockMachine.doDamping(getLogic());
            } else {
                doDamping();
            }
        }
        if (lastRecipe == null) return;
        if (last == RecipeLogic.Status.WORKING && getStatus() != RecipeLogic.Status.WORKING) {
            lastRecipe.postWorking(machine);
        } else if (last != RecipeLogic.Status.WORKING && getStatus() == RecipeLogic.Status.WORKING) {
            gtocore$interval = 5;
            lastRecipe.preWorking(machine);
        }
    }
}
