package com.gto.gtocore.common.data;

import com.gto.gtocore.common.entity.TaskEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

import com.tterrag.registrate.util.entry.EntityEntry;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public final class GTOEntityTypes {

    public static final EntityEntry<Entity> TASK_ENTITY = REGISTRATE
            .entity("task_entity", TaskEntity::new, MobCategory.MISC)
            .properties(builder -> builder.sized(0, 0).noSummon().fireImmune().clientTrackingRange(0).updateInterval(1))
            .register();

    public static void init() {}
}
