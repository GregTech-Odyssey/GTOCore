package com.gto.gtocore.config;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.config.ConfigHolder;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTOCore.MOD_ID)
public final class GTOConfig {

    public static GTOConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(GTOConfig.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
        ConfigHolder.init();

        ConfigHolder.INSTANCE.recipes.nerfWoodCrafting = true;
        ConfigHolder.INSTANCE.recipes.hardWoodRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardRedstoneRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardToolArmorRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardMiscRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardAdvancedIronRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardDyeRecipes = true;
        ConfigHolder.INSTANCE.recipes.casingsPerCraft = 1;
        ConfigHolder.INSTANCE.recipes.harderCircuitRecipes = true;
        ConfigHolder.INSTANCE.recipes.hardMultiRecipes = true;

        ConfigHolder.INSTANCE.worldgen.allUniqueStoneTypes = true;
        ConfigHolder.INSTANCE.worldgen.oreVeins.removeVanillaOreGen = false;
        ConfigHolder.INSTANCE.worldgen.oreVeins.bedrockOreDistance = 1;
        ConfigHolder.INSTANCE.worldgen.oreVeins.infiniteBedrockOresFluids = false;

        ConfigHolder.INSTANCE.machines.requireGTToolsForBlocks = true;
        ConfigHolder.INSTANCE.machines.enableCleanroom = true;
        ConfigHolder.INSTANCE.machines.cleanMultiblocks = false;
        ConfigHolder.INSTANCE.machines.enableResearch = true;
        ConfigHolder.INSTANCE.machines.enableMaintenance = true;
        ConfigHolder.INSTANCE.machines.doBedrockOres = true;
        ConfigHolder.INSTANCE.machines.highTierContent = true;
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

        ConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberDamageBoost = 50;
        ConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberBaseDamage = 1;

        ConfigHolder.INSTANCE.gameplay.hazardsEnabled = false;
        ConfigHolder.INSTANCE.gameplay.universalHazards = false;
        ConfigHolder.INSTANCE.gameplay.environmentalHazards = false;

        ConfigHolder.INSTANCE.compat.energy.enableFEConverters = true;
        ConfigHolder.INSTANCE.compat.energy.euToFeRatio = 16;
        ConfigHolder.INSTANCE.compat.energy.feToEuRatio = 20;
        ConfigHolder.INSTANCE.compat.ae2.meHatchEnergyUsage = 1920;
        ConfigHolder.INSTANCE.compat.showDimensionTier = true;
        ConfigHolder.INSTANCE.compat.minimap.toggle.journeyMapIntegration = false;
    }

    @Configurable
    @Configurable.Comment("Prevent cheating")
    public boolean selfRestraint;
    @Configurable
    @Configurable.Comment("Enabled, no mining required")
    public boolean enablePrimitiveVoidOre;
    @Configurable
    @Configurable.Comment("Reduce resource usage for the MultiBlock page (Reduce memory usage, improve startup speed, only on the client side)")
    public boolean disableMultiBlockPage;
    @Configurable
    @Configurable.Comment("Remove unnecessary loading")
    public boolean fastMultiBlockPage = true;
    @Configurable
    @Configurable.Comment("The interval increases gradually when the machine cannot find a recipe; this is the maximum interval.")
    @Configurable.Range(min = 1, max = 200)
    public int recipeMaxCheckInterval = 80;
    @Configurable
    @Configurable.Comment("Ore product multiplier")
    @Configurable.Range(min = 1, max = 64)
    public int oreMultiplier = 4;
    @Configurable
    @Configurable.Range(min = 1)
    public int spacetimePip = Integer.MAX_VALUE;
    @Configurable
    @Configurable.Range(min = 1, max = 1024)
    public int eioFluidRate = 16;
    @Configurable
    public String[] breakBlocksBlackList = { "ae2:cable_bus" };
    @Configurable
    public boolean enableAnimalsAreAfraidToEatTheirMeat = true;
    @Configurable
    @Configurable.Range(min = 1, max = 64)
    public int enableAnimalsAreAfraidToEatTheirMeatRange = 12;

    @Configurable
    @Configurable.Comment("Check for conflicts between recipes")
    public boolean recipeCheck;
    @Configurable
    @Configurable.Comment("Dev Mode")
    public boolean dev;
}
