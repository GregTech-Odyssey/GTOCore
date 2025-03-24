package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.utils.RLUtils;

import com.kyanite.deeperdarker.DeeperDarker;

import static com.gregtechceu.gtceu.common.data.GTDimensionMarkers.createAndRegister;

public interface GTODimensionMarkers {

    static void init() {
        createAndRegister(GTODimensions.MOON,
                1, RLUtils.ad("moon_stone"), "dimension.ad_astra.moon");
        createAndRegister(GTODimensions.MARS,
                2, RLUtils.ad("mars_stone"), "dimension.ad_astra.mars");
        createAndRegister(GTODimensions.VENUS,
                3, RLUtils.ad("venus_stone"), "dimension.ad_astra.venus");
        createAndRegister(GTODimensions.MERCURY,
                3, RLUtils.ad("mercury_stone"), "dimension.ad_astra.mercury");
        createAndRegister(GTODimensions.GLACIO,
                7, RLUtils.ad("glacio_stone"), "dimension.ad_astra.glacio");
        createAndRegister(GTODimensions.ANCIENT_WORLD,
                0, GTOCore.id("reactor_core"), "biome.gtocore.ancient_world_biome");
        createAndRegister(GTODimensions.TITAN,
                6, GTOCore.id("titan_stone"), "biome.gtocore.titan_biome");
        createAndRegister(GTODimensions.PLUTO,
                6, GTOCore.id("pluto_stone"), "biome.gtocore.pluto_biome");
        createAndRegister(GTODimensions.IO,
                5, GTOCore.id("io_stone"), "biome.gtocore.io_biome");
        createAndRegister(GTODimensions.GANYMEDE,
                5, GTOCore.id("ganymede_stone"), "biome.gtocore.ganymede_biome");
        createAndRegister(GTODimensions.ENCELADUS,
                6, GTOCore.id("enceladus_stone"), "biome.gtocore.enceladus_biome");
        createAndRegister(GTODimensions.CERES,
                4, GTOCore.id("ceres_stone"), "biome.gtocore.ceres_biome");
        createAndRegister(GTODimensions.BARNARDA_C,
                8, GTOCore.id("barnarda_c_log"), "biome.gtocore.barnarda_c_biome");
        createAndRegister(GTODimensions.OTHERSIDE,
                9, DeeperDarker.rl("sculk_stone"), "Otherside");
        createAndRegister(GTODimensions.CREATE,
                10, GTOCore.id("dimension_creation_casing"), "biome.gtocore.create");
        createAndRegister(GTODimensions.VOID,
                0, RLUtils.mc("obsidian"), "biome.gtocore.void");
        createAndRegister(GTODimensions.FLAT,
                0, RLUtils.mc("crying_obsidian"), "biome.gtocore.flat");
    }
}
