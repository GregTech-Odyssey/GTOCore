package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class AsyncRecipeOutputTask {

    private Runnable runnable;

    private boolean hasRequest, inQueue;

    private static final CopyOnWriteArraySet<AsyncRecipeOutputTask> tasks = new CopyOnWriteArraySet<>();
    private static ScheduledExecutorService executorService;
    private final static ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("Async Recipe Output Thread-%d")
            .setDaemon(true)
            .build();

    private static void createExecutorService() {
        if (executorService != null && !executorService.isShutdown()) return;
        executorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
        executorService.scheduleAtFixedRate(AsyncRecipeOutputTask::outputTask, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static void addAsyncLogic(RecipeLogic logic, Runnable runnable) {
        if (logic instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            AsyncRecipeOutputTask task = enhancedRecipeLogic.gtocore$getAsyncRecipeOutputTask();
            if (task == null) {
                task = new AsyncRecipeOutputTask();
                enhancedRecipeLogic.gtocore$setAsyncRecipeOutputTask(task);
            }
            task.runnable = runnable;
            task.hasRequest = true;
            if (task.inQueue) return;
            tasks.add(task);
            task.inQueue = true;
            createExecutorService();
        }
    }

    public static void removeAsyncLogic(RecipeLogic logic) {
        if (logic instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            AsyncRecipeOutputTask task = enhancedRecipeLogic.gtocore$getAsyncRecipeOutputTask();
            if (task == null) return;
            enhancedRecipeLogic.gtocore$setAsyncRecipeOutputTask(null);
            if (task.inQueue && tasks.remove(task)) {
                task.hasRequest = false;
                task.inQueue = false;
                task.runnable = null;
                if (tasks.isEmpty()) {
                    releaseExecutorService();
                }
            }
        }
    }

    private static void outputTask() {
        try {
            if (!GTCEu.canGetServerLevel()) return;
            for (var task : tasks) {
                if (task.hasRequest) {
                    try {
                        task.hasRequest = false;
                        if (task.runnable != null) task.runnable.run();
                        task.runnable = null;
                    } catch (Throwable e) {
                        GTOCore.LOGGER.error("Error while output recipe: {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            GTOCore.LOGGER.error("Error while output task: {}", e.getMessage());
        }
    }

    public static void releaseExecutorService() {
        tasks.clear();
        if (executorService != null) {
            executorService.shutdownNow();
        }
        executorService = null;
    }
}
