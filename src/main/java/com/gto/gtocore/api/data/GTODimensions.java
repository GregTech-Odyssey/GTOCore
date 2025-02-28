package com.gto.gtocore.api.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.utils.RLUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import com.google.common.collect.ImmutableMap;
import com.kyanite.deeperdarker.DeeperDarker;

import java.util.LinkedHashSet;
import java.util.Set;

public final class GTODimensions {

    public static final ResourceLocation OVERWORLD = RLUtils.mc("overworld");
    public static final ResourceLocation THE_NETHER = RLUtils.mc("the_nether");
    public static final ResourceLocation THE_END = RLUtils.mc("the_end");
    public static final ResourceLocation MOON = RLUtils.ad("moon");
    public static final ResourceLocation MARS = RLUtils.ad("mars");
    public static final ResourceLocation VENUS = RLUtils.ad("venus");
    public static final ResourceLocation MERCURY = RLUtils.ad("mercury");
    public static final ResourceLocation GLACIO = RLUtils.ad("glacio");
    public static final ResourceLocation ANCIENT_WORLD = GTOCore.id("ancient_world");
    public static final ResourceLocation TITAN = GTOCore.id("titan");
    public static final ResourceLocation PLUTO = GTOCore.id("pluto");
    public static final ResourceLocation IO = GTOCore.id("io");
    public static final ResourceLocation GANYMEDE = GTOCore.id("ganymede");
    public static final ResourceLocation ENCELADUS = GTOCore.id("enceladus");
    public static final ResourceLocation CERES = GTOCore.id("ceres");
    public static final ResourceLocation BARNARDA_C = GTOCore.id("barnarda_c");
    public static final ResourceLocation OTHERSIDE = DeeperDarker.rl("otherside");

    public static final ResourceLocation FLAT = GTOCore.id("flat");
    public static final ResourceLocation VOID = GTOCore.id("void");
    public static final ResourceLocation CREATE = GTOCore.id("create");

    public static final ImmutableMap<ResourceLocation, Integer> ALL_GALAXY_DIM;
    private static final ImmutableMap<ResourceLocation, Integer> SOLAR;
    private static final ImmutableMap<ResourceLocation, Integer> PROXIMA_CENTAURI;
    private static final ImmutableMap<ResourceLocation, Integer> BARNARDA;

    static {
        ImmutableMap.Builder<ResourceLocation, Integer> SOLARBuilder = ImmutableMap.builder();
        SOLARBuilder.put(OVERWORLD, 3);
        SOLARBuilder.put(RLUtils.ad("earth_orbit"), 3);
        SOLARBuilder.put(MOON, 3);
        SOLARBuilder.put(RLUtils.ad("moon_orbit"), 3);
        SOLARBuilder.put(MARS, 4);
        SOLARBuilder.put(RLUtils.ad("mars_orbit"), 4);
        SOLARBuilder.put(VENUS, 2);
        SOLARBuilder.put(RLUtils.ad("venus_orbit"), 2);
        SOLARBuilder.put(MERCURY, 1);
        SOLARBuilder.put(RLUtils.ad("mercury_orbit"), 1);
        SOLARBuilder.put(TITAN, 5);
        SOLARBuilder.put(GTOCore.id("titan_orbit"), 5);
        SOLARBuilder.put(PLUTO, 7);
        SOLARBuilder.put(GTOCore.id("pluto_orbit"), 7);
        SOLARBuilder.put(IO, 6);
        SOLARBuilder.put(GTOCore.id("io_orbit"), 6);
        SOLARBuilder.put(GANYMEDE, 6);
        SOLARBuilder.put(GTOCore.id("ganymede_orbit"), 6);
        SOLARBuilder.put(ENCELADUS, 5);
        SOLARBuilder.put(GTOCore.id("enceladus_orbit"), 5);
        SOLARBuilder.put(CERES, 7);
        SOLARBuilder.put(GTOCore.id("ceres_orbit"), 7);
        SOLAR = SOLARBuilder.build();

        ImmutableMap.Builder<ResourceLocation, Integer> PROXIMA_CENTAURIBuilder = ImmutableMap.builder();
        PROXIMA_CENTAURIBuilder.put(GLACIO, 1);
        PROXIMA_CENTAURIBuilder.put(RLUtils.ad("glacio_orbit"), 1);
        PROXIMA_CENTAURI = PROXIMA_CENTAURIBuilder.build();

        ImmutableMap.Builder<ResourceLocation, Integer> BARNARDABuilder = ImmutableMap.builder();
        BARNARDABuilder.put(BARNARDA_C, 1);
        BARNARDABuilder.put(GTOCore.id("barnarda_c_orbit"), 1);
        BARNARDA = BARNARDABuilder.build();

        ImmutableMap.Builder<ResourceLocation, Integer> ALL_GALAXY_DIMBuilder = ImmutableMap.builder();
        ALL_GALAXY_DIMBuilder.putAll(SOLAR);
        ALL_GALAXY_DIMBuilder.putAll(PROXIMA_CENTAURI);
        ALL_GALAXY_DIMBuilder.putAll(BARNARDA);
        ALL_GALAXY_DIM = ALL_GALAXY_DIMBuilder.build();
    }

    public static final Set<ResourceLocation> SOLAR_PLANET = new LinkedHashSet<>();

    private static final Set<ResourceLocation> PROXIMA_CENTAURI_PLANET = new LinkedHashSet<>();

    private static final Set<ResourceLocation> BARNARDA_PLANET = new LinkedHashSet<>();

    public static final Set<ResourceLocation> ALL_PLANET = new LinkedHashSet<>();

    public static final Set<ResourceLocation> ALL_LAYER_DIMENSION = new LinkedHashSet<>();

    static {
        SOLAR_PLANET.add(OVERWORLD);
        SOLAR_PLANET.add(MOON);
        SOLAR_PLANET.add(MARS);
        SOLAR_PLANET.add(VENUS);
        SOLAR_PLANET.add(MERCURY);
        SOLAR_PLANET.add(TITAN);
        SOLAR_PLANET.add(PLUTO);
        SOLAR_PLANET.add(IO);
        SOLAR_PLANET.add(GANYMEDE);
        SOLAR_PLANET.add(ENCELADUS);
        SOLAR_PLANET.add(CERES);
        PROXIMA_CENTAURI_PLANET.add(GLACIO);
        BARNARDA_PLANET.add(BARNARDA_C);
        ALL_PLANET.addAll(SOLAR_PLANET);
        ALL_PLANET.addAll(PROXIMA_CENTAURI_PLANET);
        ALL_PLANET.addAll(BARNARDA_PLANET);
        ALL_LAYER_DIMENSION.addAll(ALL_PLANET);
        ALL_LAYER_DIMENSION.add(ANCIENT_WORLD);
        ALL_LAYER_DIMENSION.add(THE_NETHER);
        ALL_LAYER_DIMENSION.add(THE_END);
        ALL_LAYER_DIMENSION.add(OTHERSIDE);
    }

    public static Set<ResourceKey<Level>> getDimensionKeys(ResourceLocation resourceLocation) {
        return Set.of(getDimensionKey(resourceLocation));
    }

    public static ResourceKey<Level> getDimensionKey(ResourceLocation resourceLocation) {
        return ResourceKey.create(Registries.DIMENSION, resourceLocation);
    }

    public static String getGalaxy(ResourceLocation d) {
        if (SOLAR.containsKey(d)) return "proxima_centauri";
        if (PROXIMA_CENTAURI.containsKey(d)) return "barnarda";
        if (BARNARDA.containsKey(d)) return "solar";
        return null;
    }

    public static final ImmutableMap<ResourceLocation, Integer> PLANET_DISTANCES;

    static {
        ImmutableMap.Builder<ResourceLocation, Integer> PLANET_DISTANCESBuilder = ImmutableMap.builder();
        PLANET_DISTANCESBuilder.put(MERCURY, 1);
        PLANET_DISTANCESBuilder.put(VENUS, 2);
        PLANET_DISTANCESBuilder.put(OVERWORLD, 3);
        PLANET_DISTANCESBuilder.put(MOON, 3);
        PLANET_DISTANCESBuilder.put(MARS, 4);
        PLANET_DISTANCESBuilder.put(IO, 5);
        PLANET_DISTANCESBuilder.put(GANYMEDE, 5);
        PLANET_DISTANCESBuilder.put(TITAN, 6);
        PLANET_DISTANCESBuilder.put(ENCELADUS, 6);
        PLANET_DISTANCESBuilder.put(CERES, 7);
        PLANET_DISTANCESBuilder.put(PLUTO, 8);
        PLANET_DISTANCES = PLANET_DISTANCESBuilder.build();
    }

    public static int calculateDistance(ResourceLocation planet1, ResourceLocation planet2) {
        if (planet1 == null || planet2 == null) return 7;
        Integer distanceFromEarth1 = PLANET_DISTANCES.get(planet1);
        Integer distanceFromEarth2 = PLANET_DISTANCES.get(planet2);
        if (distanceFromEarth1 == null || distanceFromEarth2 == null) return 7;
        return Math.max(1, Math.min(6, Math.abs(distanceFromEarth1 - distanceFromEarth2)));
    }
}
