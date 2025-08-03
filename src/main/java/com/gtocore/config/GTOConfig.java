package com.gtocore.config;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.config.ConfigHolder;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

@Scanned
@Config(id = GTOCore.MOD_ID)
public final class GTOConfig {

    @RegisterLanguage(en = "GTO Core Config", cn = "GTO Core 配置")
    private static final String SCREEN = "config.screen.gtocore";
    public static GTOConfig INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(GTOConfig.class, ConfigFormats.yaml()).getConfigInstance();
            if (INSTANCE.startSpark == SparkRange.ALL || INSTANCE.startSpark == SparkRange.MAIN_MENU) {
                SparkLaunchProfiler.start("all");
            }
            GTOCore.difficulty = INSTANCE.gameDifficulty.ordinal() + 1;
        }
        int difficulty = GTOCore.difficulty;
        ConfigHolder.init();
        ConfigHolder.INSTANCE.recipes.generateLowQualityGems = false;
        ConfigHolder.INSTANCE.recipes.disableManualCompression = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.harderRods = difficulty == 3;
        ConfigHolder.INSTANCE.recipes.harderBrickRecipes = difficulty == 3;
        ConfigHolder.INSTANCE.recipes.nerfWoodCrafting = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardWoodRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardIronRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardRedstoneRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardToolArmorRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardMiscRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardGlassRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.nerfPaperCrafting = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardAdvancedIronRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardDyeRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.harderCharcoalRecipe = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.flintAndSteelRequireSteel = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.removeVanillaBlockRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.removeVanillaTNTRecipe = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.casingsPerCraft = Math.max(1, 3 - difficulty);
        ConfigHolder.INSTANCE.recipes.harderCircuitRecipes = difficulty > 1;
        ConfigHolder.INSTANCE.recipes.hardMultiRecipes = difficulty == 3;
        ConfigHolder.INSTANCE.recipes.enchantedTools = difficulty == 1;
        ConfigHolder.INSTANCE.compat.energy.nativeEUToFE = true;
        ConfigHolder.INSTANCE.compat.energy.enableFEConverters = true;
        ConfigHolder.INSTANCE.compat.energy.feToEuRatio = 20;
        ConfigHolder.INSTANCE.compat.energy.euToFeRatio = 16;
        ConfigHolder.INSTANCE.compat.ae2.meHatchEnergyUsage = 480 * difficulty;
        ConfigHolder.INSTANCE.compat.minimap.toggle.ftbChunksIntegration = false;
        ConfigHolder.INSTANCE.compat.minimap.toggle.journeyMapIntegration = false;
        ConfigHolder.INSTANCE.compat.showDimensionTier = true;
        ConfigHolder.INSTANCE.worldgen.rubberTreeSpawnChance = (float) (2 - 0.5 * difficulty);
        ConfigHolder.INSTANCE.worldgen.allUniqueStoneTypes = true;
        ConfigHolder.INSTANCE.worldgen.sandOresFall = false;
        ConfigHolder.INSTANCE.worldgen.increaseDungeonLoot = true;
        ConfigHolder.INSTANCE.worldgen.addLoot = true;
        ConfigHolder.INSTANCE.worldgen.oreVeins.oreVeinGridSize = 3;
        ConfigHolder.INSTANCE.worldgen.oreVeins.oreVeinRandomOffset = 12;
        ConfigHolder.INSTANCE.worldgen.oreVeins.removeVanillaOreGen = false;
        ConfigHolder.INSTANCE.worldgen.oreVeins.removeVanillaLargeOreVeins = true;
        ConfigHolder.INSTANCE.worldgen.oreVeins.bedrockOreDistance = difficulty;
        ConfigHolder.INSTANCE.worldgen.oreVeins.infiniteBedrockOresFluids = difficulty == 1;
        ConfigHolder.INSTANCE.worldgen.oreVeins.oreIndicators = true;
        ConfigHolder.INSTANCE.worldgen.oreVeins.oreGenerationChunkCacheSize = 512;
        ConfigHolder.INSTANCE.worldgen.oreVeins.oreIndicatorChunkCacheSize = 2048;
        ConfigHolder.INSTANCE.machines.recipeProgressLowEnergy = difficulty == 3;
        ConfigHolder.INSTANCE.machines.requireGTToolsForBlocks = difficulty > 1;
        ConfigHolder.INSTANCE.machines.shouldWeatherOrTerrainExplosion = difficulty == 3;
        ConfigHolder.INSTANCE.machines.energyUsageMultiplier = 100 * difficulty;
        ConfigHolder.INSTANCE.machines.prospectorEnergyUseMultiplier = 100 * difficulty;
        ConfigHolder.INSTANCE.machines.doesExplosionDamagesTerrain = difficulty > 1;
        ConfigHolder.INSTANCE.machines.harmlessActiveTransformers = difficulty == 1;
        ConfigHolder.INSTANCE.machines.steelSteamMultiblocks = false;
        ConfigHolder.INSTANCE.machines.enableCleanroom = difficulty > 1;
        ConfigHolder.INSTANCE.machines.cleanMultiblocks = difficulty == 1;
        ConfigHolder.INSTANCE.machines.replaceMinedBlocksWith = "minecraft:cobblestone";
        ConfigHolder.INSTANCE.machines.enableResearch = true;
        ConfigHolder.INSTANCE.machines.enableMaintenance = difficulty > 1;
        ConfigHolder.INSTANCE.machines.enableWorldAccelerators = true;
        ConfigHolder.INSTANCE.machines.gt6StylePipesCables = true;
        ConfigHolder.INSTANCE.machines.doBedrockOres = true;
        ConfigHolder.INSTANCE.machines.bedrockOreDropTagPrefix = "raw";
        ConfigHolder.INSTANCE.machines.minerSpeed = 80;
        ConfigHolder.INSTANCE.machines.enableTieredCasings = difficulty > 1;
        ConfigHolder.INSTANCE.machines.ldItemPipeMinDistance = 50;
        ConfigHolder.INSTANCE.machines.ldFluidPipeMinDistance = 50;
        ConfigHolder.INSTANCE.machines.onlyOwnerGUI = false;
        ConfigHolder.INSTANCE.machines.onlyOwnerBreak = false;
        ConfigHolder.INSTANCE.machines.ownerOPBypass = 2;
        ConfigHolder.INSTANCE.machines.highTierContent = true;
        ConfigHolder.INSTANCE.machines.orderedAssemblyLineItems = difficulty > 1;
        ConfigHolder.INSTANCE.machines.orderedAssemblyLineFluids = difficulty == 3;
        ConfigHolder.INSTANCE.machines.steamMultiParallelAmount = 8;
        int boilerFactor = 8 >> difficulty;
        ConfigHolder.INSTANCE.machines.smallBoilers.solidBoilerBaseOutput = 120 * boilerFactor;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpSolidBoilerBaseOutput = 300 * boilerFactor;
        ConfigHolder.INSTANCE.machines.smallBoilers.liquidBoilerBaseOutput = 240 * boilerFactor;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpLiquidBoilerBaseOutput = 600 * boilerFactor;
        ConfigHolder.INSTANCE.machines.smallBoilers.solarBoilerBaseOutput = 80 * boilerFactor;
        ConfigHolder.INSTANCE.machines.smallBoilers.hpSolarBoilerBaseOutput = 240 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.steamPerWater = 160 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.bronzeBoilerMaxTemperature = 800 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.bronzeBoilerHeatSpeed = boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.steelBoilerMaxTemperature = 1800 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.steelBoilerHeatSpeed = boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.titaniumBoilerMaxTemperature = 3200 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.titaniumBoilerHeatSpeed = boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.tungstensteelBoilerMaxTemperature = 6400 * boilerFactor;
        ConfigHolder.INSTANCE.machines.largeBoilers.tungstensteelBoilerHeatSpeed = boilerFactor;
        ConfigHolder.INSTANCE.tools.rngDamageElectricTools = 5 << difficulty;
        ConfigHolder.INSTANCE.tools.sprayCanChainLength = 16;
        ConfigHolder.INSTANCE.tools.treeFellingDelay = 2;
        ConfigHolder.INSTANCE.tools.voltageTierNightVision = 1;
        ConfigHolder.INSTANCE.tools.voltageTierNanoSuit = 3;
        ConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit = 3;
        ConfigHolder.INSTANCE.tools.voltageTierQuarkTech = 5;
        ConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech = 6;
        ConfigHolder.INSTANCE.tools.voltageTierImpeller = 2;
        ConfigHolder.INSTANCE.tools.voltageTierAdvImpeller = 3;
        ConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberDamageBoost = 256 >> difficulty;
        ConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberBaseDamage = 1;
        ConfigHolder.INSTANCE.tools.nanoSaber.zombieSpawnWithSabers = true;
        ConfigHolder.INSTANCE.tools.nanoSaber.energyConsumption = 64;
        ConfigHolder.INSTANCE.gameplay.environmentalHazards = false;
        ConfigHolder.INSTANCE.gameplay.environmentalHazardDecayRate = 0.001F;
        if (GTOCore.isSimple()) {
            ConfigHolder.INSTANCE.gameplay.hazardsEnabled = false;
            ConfigHolder.INSTANCE.gameplay.universalHazards = false;
        }
        ConfigHolder.INSTANCE.dev.debug = GTCEu.isDev();
    }

    @Configurable
    @Configurable.Comment("Optional: Simple, Normal, Expert")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Game Difficulty", cn = "游戏难度")
    public Difficulty gameDifficulty = Difficulty.Normal;
    @Configurable
    @Configurable.Comment("Prevent cheating")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Self Restraint Mode", cn = "自我约束模式")
    public boolean selfRestraint;
    @Configurable
    @Configurable.Comment("Remove unnecessary loading")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Fast MultiBlock Page", cn = "快速多方块页面")
    public boolean fastMultiBlockPage = true;
    @Configurable
    @Configurable.Comment("The interval increases gradually when the machine cannot find a recipe; this is the maximum interval.")
    @Configurable.Range(min = 5, max = 200)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Recipe Search Max Interval", cn = "配方搜索最大间隔")
    public int recipeSearchMaxInterval = 20;
    @Configurable
    @Configurable.Range(min = 600, max = 144000)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Batch Processing Mode Max Duration", cn = "批处理模式最大时间")
    public int batchProcessingMaxDuration = 72000;
    @Configurable
    @Configurable.Range(min = 1, max = 20)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Chain Mining Adjacent Block Check Range", cn = "连锁挖掘相邻方块检查范围")
    public int ftbUltimineRange = 3;
    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Black List Of Chain Blocks", cn = "连锁挖掘黑名单")
    public String[] breakBlocksBlackList = { "ae2:cable_bus" };
    @Configurable
    @Configurable.Comment("Check for conflicts between recipes")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug]Recipe Abnormal Check", cn = "[调试]配方异常检查")
    public boolean recipeCheck;
    @Configurable
    @Configurable.Comment("Dev Mode")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Dev mode", cn = "开发模式")
    public boolean dev;
    @Configurable
    @Configurable.Comment("Gto Ae Log")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug]AE2 Log", cn = "[调试]AE2日志")
    public boolean aeLog = false;
    @Configurable
    @Configurable.Comment("When disabled, the emi favorites in different saves will be independent from each other")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "EMI Global Favorites", cn = "全局 EMI 书签")
    public boolean emiGlobalFavorites = true;
    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Disable Charge Bomb", cn = "禁用爆弹")
    public boolean disableChargeBomb = true;
    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Spark Start Phase", cn = "火花启动阶段")
    public SparkRange startSpark = SparkRange.NONE;

    private enum Difficulty {
        Simple,
        Normal,
        Expert
    }
}
