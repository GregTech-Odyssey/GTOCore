package com.gtocore.config;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
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
            RecipeLogic.SEARCH_MAX_INTERVAL = GTOConfig.INSTANCE.recipeSearchMaxInterval;
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

    // 游戏核心设置
    @Configurable
    @Configurable.Comment("游戏难度等级：简单、普通、专家")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Game Difficulty", cn = "游戏难度")
    public Difficulty gameDifficulty = Difficulty.Normal;

    @Configurable
    @Configurable.Comment("启用自我约束模式以防止作弊行为")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Self Restraint Mode", cn = "自我约束模式")
    public boolean selfRestraint = false;

    // 性能优化设置
    @Configurable
    @Configurable.Comment("快速加载多方块结构页面，减少不必要的加载时间")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Fast Multiblock Page Loading", cn = "快速多方块页面加载")
    public boolean fastMultiBlockPage = true;

    @Configurable
    @Configurable.Comment("机器查找配方最大间隔（tick）")
    @Configurable.Range(min = 5, max = 200)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Recipe Search Max Interval", cn = "配方搜索最大间隔")
    public int recipeSearchMaxInterval = 20;

    @Configurable
    @Configurable.Comment("批处理模式的最大持续时间（tick）")
    @Configurable.Range(min = 600, max = 144000)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Batch Processing Max Duration", cn = "批处理模式最大持续时间")
    public int batchProcessingMaxDuration = 72000;

    // 挖掘系统设置
    @Configurable
    @Configurable.Comment("连锁挖掘时检查相邻方块的范围")
    @Configurable.Range(min = 1, max = 20)
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Chain Mining Range", cn = "连锁挖掘检查范围")
    public int ftbUltimineRange = 3;

    @Configurable
    @Configurable.Comment("连锁挖掘功能的方块黑名单")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Chain Mining Blacklist", cn = "连锁挖掘黑名单")
    public String[] breakBlocksBlackList = { "ae2:cable_bus" };

    // 界面和显示设置
    @Configurable
    @Configurable.Comment("禁用后，不同存档的 EMI 收藏夹将相互独立")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "EMI Global Favorites", cn = "EMI 全局收藏夹")
    public boolean emiGlobalFavorites = true;

    @Configurable
    @Configurable.Comment("禁用爆弹物品的使用")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Disable Charge Bomb", cn = "禁用爆弹")
    public boolean disableChargeBomb = true;

    @Configurable
    @Configurable.Comment("在物品下方显示英文名称")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Show Item English Name", cn = "显示物品英文名称")
    public boolean showEnglishName = true;

    // 开发和调试设置
    @Configurable
    @Configurable.Comment("开启开发者模式")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Developer Mode", cn = "开发者模式")
    public boolean dev = false;

    @Configurable
    @Configurable.Comment("检查配方之间的冲突问题")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] Recipe Conflict Check", cn = "[调试] 配方冲突检查")
    public boolean recipeCheck = false;

    @Configurable
    @Configurable.Comment("启用 AE2 和同步组件的详细日志")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] AE2 & Sync Logging", cn = "[调试] AE2 和同步日志")
    public boolean aeLog = false;

    @Configurable
    @Configurable.Comment("Spark 性能分析器的启动阶段")
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Spark Profiler Start Phase", cn = "Spark 分析器启动阶段")
    public SparkRange startSpark = SparkRange.NONE;

    private enum Difficulty {
        Simple,
        Normal,
        Expert
    }
}
