package com.gtocore.common.data;

import com.gtocore.common.entity.TaskEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

import com.gto.registrate.util.entry.EntityEntry;

import static com.gtolib.api.registries.GTORegistration.GTO;

public final class GTOEntityTypes {

    public static final EntityEntry<Entity> TASK_ENTITY = GTO
            .entity("task_entity", TaskEntity::new, MobCategory.MISC)
            .properties(builder -> builder.sized(0, 0).noSummon().fireImmune().clientTrackingRange(0).updateInterval(1))
            .register();

    public static void init() {}
}
