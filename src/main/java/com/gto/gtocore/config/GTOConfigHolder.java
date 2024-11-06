package com.gto.gtocore.config;

import com.gto.gtocore.GTOCore;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTOCore.MOD_ID)
public class GTOConfigHolder {

    public static GTOConfigHolder INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(GTOConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
    }

    @Configurable
    public boolean disableDrift = true;
    @Configurable
    public boolean enablePrimitiveVoidOre = false;
    @Configurable
    @Configurable.Range(min = 1, max = 64)
    public int oreMultiplier = 4;
    @Configurable
    @Configurable.Range(min = 1)
    public int spacetimePip = Integer.MAX_VALUE;
    @Configurable
    @Configurable.Range(min = 1, max = 1024)
    public int eioFluidRate = 16;
}
