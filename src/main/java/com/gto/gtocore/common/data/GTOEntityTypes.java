package com.gto.gtocore.common.data;

import com.gto.gtocore.common.entity.NukeBombEntity;

import net.minecraft.world.entity.MobCategory;

import com.tterrag.registrate.util.entry.EntityEntry;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class GTOEntityTypes {

    public static final EntityEntry<NukeBombEntity> NUKE_BOMB = REGISTRATE
            .<NukeBombEntity>entity("nuke_bomb", NukeBombEntity::new, MobCategory.MISC)
            .properties(builder -> builder.sized(0.98F, 0.98F).fireImmune().clientTrackingRange(10).updateInterval(10))
            .register();

    public static void init() {}
}
