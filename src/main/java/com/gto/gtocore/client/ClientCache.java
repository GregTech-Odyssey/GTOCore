package com.gto.gtocore.client;

import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

public final class ClientCache {

    public static boolean disableDrift;

    public static final Set<ResourceLocation> UNLOCKED_PLANET = new ObjectOpenHashSet<>();

    public static boolean initializedBook;
}
