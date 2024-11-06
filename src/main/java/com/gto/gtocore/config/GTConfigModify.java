package com.gto.gtocore.config;

import com.gregtechceu.gtceu.config.ConfigHolder;

public class GTConfigModify {

    public static void init() {
        ConfigHolder.INSTANCE.machines.smallBoilers.solidBoilerBaseOutput = 240;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpSolidBoilerBaseOutput = 600;
        ConfigHolder.INSTANCE.machines.smallBoilers.liquidBoilerBaseOutput = 480;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpLiquidBoilerBaseOutput = 1200;
        ConfigHolder.INSTANCE.machines.smallBoilers.solarBoilerBaseOutput = 240;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpSolarBoilerBaseOutput = 720;

        ConfigHolder.INSTANCE.machines.largeBoilers.bronzeBoilerMaxTemperature = 1600;
        ConfigHolder.INSTANCE.machines.largeBoilers.steelBoilerMaxTemperature = 3600;
        ConfigHolder.INSTANCE.machines.largeBoilers.titaniumBoilerMaxTemperature = 6400;
        ConfigHolder.INSTANCE.machines.largeBoilers.tungstensteelBoilerMaxTemperature = 12800;

        ConfigHolder.INSTANCE.compat.energy.platformToEuRatio = 64;
        ConfigHolder.INSTANCE.compat.energy.euToPlatformRatio = 48;
        ConfigHolder.INSTANCE.compat.ae2.meHatchEnergyUsage = 1920;
    }
}
