package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;

import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;

public class GTOQuestTypes {

    public static void init() {
        ModTask.MOD = TaskTypes.register(GTOCore.id("mod"), ModTask::new,
                () -> Icons.ACCEPT_GRAY);
        DifficultyTask.GTODIFFICULTY = TaskTypes.register(GTOCore.id("gtodifficulty"), DifficultyTask::new,
                () -> Icons.ACCEPT_GRAY);
        ScheduledTask.SCHEDULED = TaskTypes.register(GTOCore.id("scheduled"), ScheduledTask::new,
                () -> Icons.ACCEPT_GRAY);
    }
}
