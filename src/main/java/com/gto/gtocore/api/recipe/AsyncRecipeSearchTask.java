package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;

import java.util.Iterator;
import java.util.concurrent.*;

public final class AsyncRecipeSearchTask {

    @Getter
    private Result result;

    @Getter
    private boolean hasTask;
    private boolean hasRequest, inQueue;

    private final RecipeLogic logic;

    public AsyncRecipeSearchTask(RecipeLogic logic) {
        this.logic = logic;
    }

    public void clean() {
        result = null;
        hasTask = false;
    }

    private static final CopyOnWriteArraySet<AsyncRecipeSearchTask> tasks = new CopyOnWriteArraySet<>();
    private static ScheduledExecutorService executorService;
    private final static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("Recipe Search Thread-%d")
            .setDaemon(true)
            .build();

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
            for (var task : tasks) {
                if (task.hasRequest) {
                    try {
                        task.hasRequest = false;
                        task.result = searchRecipe(task.logic);
                    } catch (Throwable e) {
                        task.clean();
                        GTOCore.LOGGER.error("Error while searching recipe");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    public static void releaseExecutorService() {
        tasks.clear();
        if (executorService != null) {
            executorService.shutdownNow();
        }
        executorService = null;
    }

    private static Result searchRecipe(RecipeLogic logic) {
        Iterator<GTRecipe> iterator = logic.machine.getRecipeType().searchRecipe(logic.machine, recipe -> RecipeRunnerHelper.checkTier(logic.machine, recipe) && RecipeRunnerHelper.matchRecipe(logic.machine, recipe) && RecipeRunnerHelper.matchTickRecipe(logic.machine, recipe));
        while (iterator.hasNext()) {
            GTRecipe recipe = iterator.next();
            if (recipe == null) continue;
            GTRecipe modified = modifyRecipe(recipe, logic);
            if (modified != null) {
                return new Result(recipe, modified);
            }
        }
        return new Result(null, null);
    }

    private static GTRecipe modifyRecipe(GTRecipe recipe, RecipeLogic logic) {
        GTRecipe modified = logic.machine.fullModifyRecipe(recipe.copy());
        if (RecipeRunnerHelper.check(logic.machine, modified)) {
            return modified;
        }
        return null;
    }

    public record Result(GTRecipe recipe, GTRecipe modified) {}
}
