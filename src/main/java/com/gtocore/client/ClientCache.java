package com.gtocore.client;

import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

public final class ClientCache {

    public static boolean disableDrift;

    public static final Set<ResourceLocation> UNLOCKED_PLANET = new OpenCacheHashSet<>();

    public static boolean initializedBook;

    @Nullable
    public static UUID SERVER_IDENTIFIER;

    public static int highlightTime;

    public static String autoRenameName = GTOConfig.INSTANCE.renamePatternDefaultString;

    public static boolean machineNotFormedHighlight = false;
}
