package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;

public class AsyncRecipeSearchTask {

    @Getter
    private IResult result;

    @Getter
    boolean hasTask;
    private boolean hasRequest, inQueue;

    final RecipeLogic logic;

    public AsyncRecipeSearchTask(RecipeLogic logic) {
        this.logic = logic;
    }

    public void clean() {
        result = null;
        hasTask = false;
    }

    IResult searchRecipe() {
        return searchRecipe(logic);
    }

    private static final CopyOnWriteArraySet<AsyncRecipeSearchTask> tasks = new CopyOnWriteArraySet<>();
    private static ScheduledExecutorService executorService;
    private final static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("Async Recipe Search Thread-%d")
            .setDaemon(true)
            .build();

    private static int tick = 0;

    private static void createExecutorService() {
        if (executorService != null && !executorService.isShutdown()) return;
        executorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
        executorService.scheduleAtFixedRate(AsyncRecipeSearchTask::searchingTask, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static void addAsyncLogic(RecipeLogic logic) {
        if (logic instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            AsyncRecipeSearchTask task = enhancedRecipeLogic.gtocore$getAsyncRecipeSearchTask();
            if (task == null) {
                task = enhancedRecipeLogic.gtocore$createAsyncRecipeSearchTask();
                enhancedRecipeLogic.gtocore$setAsyncRecipeSearchTask(task);
            }
            task.hasTask = true;
            task.hasRequest = true;
            task.result = null;
            if (task.inQueue) return;
            tasks.add(task);
            task.inQueue = true;
            createExecutorService();
        }
    }

    public static void removeAsyncLogic(RecipeLogic logic) {
        if (logic instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            AsyncRecipeSearchTask task = enhancedRecipeLogic.gtocore$getAsyncRecipeSearchTask();
            if (task == null) return;
            enhancedRecipeLogic.gtocore$setAsyncRecipeSearchTask(null);
            if (task.inQueue && tasks.remove(task)) {
                task.hasTask = false;
                task.hasRequest = false;
                task.inQueue = false;
                task.result = null;
                if (tasks.isEmpty()) {
                    releaseExecutorService();
                }
            }
        }
    }

    private static void searchingTask() {
        try {
            if (!GTCEu.canGetServerLevel()) return;
            if (tick > 100) tick = 0;
            tick++;
            for (var task : tasks) {
                if (task.hasRequest && (task.logic.getMachine().holder.getOffset() + tick) % 5 == 0) {
                    try {
                        task.result = task.searchRecipe();
                        task.hasRequest = false;
                    } catch (Throwable e) {
                        GTOCore.LOGGER.error("Error while searching recipe: {}", e.getMessage());
                    }
                }
            }
        } catch (Throwable e) {
            GTOCore.LOGGER.error("Error while searching task: {}", e.getMessage());
        }
    }

    public static void releaseExecutorService() {
        tasks.clear();
        if (executorService != null) {
            executorService.shutdownNow();
        }
        executorService = null;
    }

    private static IResult searchRecipe(RecipeLogic logic) {
        if (logic.machine.hasProxies()) {
            Iterator<GTRecipe> iterator = logic.machine.getRecipeType().getLookup().getRecipeIterator(logic.machine, recipe -> !recipe.isFuel && recipe.matchRecipe(logic.machine).isSuccess() && recipe.matchTickRecipe(logic.machine).isSuccess());
            while (iterator.hasNext()) {
                GTRecipe recipe = iterator.next();
                if (recipe == null) continue;
                GTRecipe modified = modifyRecipe(recipe, logic);
                if (modified != null) {
                    return new Result(recipe, modified);
                }
                if (logic.lastFailedMatches == null) {
                    logic.lastFailedMatches = new ArrayList<>();
                }
                logic.lastFailedMatches.add(recipe);
            }
            for (GTRecipeType.ICustomRecipeLogic customRecipeLogic : logic.machine.getRecipeType().getCustomRecipeLogicRunners()) {
                GTRecipe recipe = customRecipeLogic.createCustomRecipe(logic.machine);
                if (recipe != null) {
                    GTRecipe modified = modifyRecipe(recipe, logic);
                    if (modified != null) {
                        return new Result(recipe, modified);
                    }
                    if (logic.lastFailedMatches == null) {
                        logic.lastFailedMatches = new ArrayList<>();
                    }
                    logic.lastFailedMatches.add(recipe);
                }
            }
        }
        return new Result(null, null);
    }

    private static GTRecipe modifyRecipe(GTRecipe recipe, RecipeLogic logic) {
        GTRecipe modified = logic.machine.fullModifyRecipe(recipe.copy());
        if (modified != null) {
            if (modified.checkConditions(logic).isSuccess() && modified.matchRecipe(logic.machine).isSuccess() && modified.matchTickRecipe(logic.machine).isSuccess()) {
                return modified;
            }
        }
        return null;
    }

    private record Result(GTRecipe recipe, GTRecipe modified) implements IResult {}

    public interface IResult {

        GTRecipe recipe();

        GTRecipe modified();
    }
}
