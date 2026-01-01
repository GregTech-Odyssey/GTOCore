package com.gtocore.config;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import dev.toma.configuration.config.io.ConfigIO;
import dev.toma.configuration.config.value.ConfigValue;
import dev.toma.configuration.config.value.ObjectValue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

import static dev.toma.configuration.config.ConfigHolder.getConfig;

@DataGeneratorScanned
@Config(id = GTOCore.MOD_ID, group = GTOCore.MOD_ID)
@SuppressWarnings("unused")
public final class GTOConfig {

    @RegisterLanguage(en = "GTO Core Config", cn = "GTO Core 配置")
    private static final String SCREEN = "config.screen.gtocore";
    public final static GTOConfig INSTANCE;

    static {
        ConfigHolder.init();
        INSTANCE = Configuration.registerConfig(GTOConfig.class, ConfigFormats.YAML).getConfigInstance();
        if (INSTANCE.startSpark == SparkRange.ALL || INSTANCE.startSpark == SparkRange.MAIN_MENU) {
            SparkLaunchProfiler.start("all");
        }
        if (INSTANCE.dev) Configurator.setRootLevel(Level.INFO);
        if (INSTANCE.detailedLogging) Configurator.setRootLevel(Level.DEBUG);
        int difficulty = INSTANCE.difficulty.ordinal() + 1;
        GTOCore.difficulty = difficulty;
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
        ConfigHolder.INSTANCE.compat.energy.enableFEConverters = false;
        ConfigHolder.INSTANCE.compat.energy.feToEuRatio = 20;
        ConfigHolder.INSTANCE.compat.energy.euToFeRatio = 16;
        ConfigHolder.INSTANCE.compat.ae2.meHatchEnergyUsage = 32 * difficulty;
        ConfigHolder.INSTANCE.compat.showDimensionTier = true;
        ConfigHolder.INSTANCE.worldgen.rubberTreeSpawnChance = (float) (2 - 0.5 * difficulty);
        ConfigHolder.INSTANCE.worldgen.allUniqueStoneTypes = true;
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
        ConfigHolder.INSTANCE.machines.dualChamberPressurizationMode = difficulty == 3 ? 3 : 1;
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
        if (GTOCore.isEasy()) {
            ConfigHolder.INSTANCE.gameplay.hazardsEnabled = false;
        }
        ConfigHolder.INSTANCE.dev.debug = INSTANCE.dev;

        MultiblockControllerMachine.sendMessage = INSTANCE.sendMultiblockErrorMessages;
    }

    // 游戏核心设置
    @Configurable
    @Configurable.Comment({ "游戏难度等级", "Game difficulty level" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Game Difficulty", cn = "游戏难度")
    public Difficulty difficulty = Difficulty.Normal;

    @Configurable
    @Configurable.Comment({ "启用自我约束模式以限制任何形式的作弊指令使用（警告：一旦开启，游玩的存档将永久锁定自我约束模式！）", "Enable Self Restraint Mode to restrict the use of any form of cheat commands (Warning: Once enabled, the played save will be permanently locked in Self Restraint Mode!)" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Self Restraint Mode", cn = "自我约束模式")
    public boolean selfRestraint = false;

    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "disable Muffler Part", cn = "禁用消声仓")
    @Configurable.Comment({ "禁用后失去掏灰玩法(在非专家模式生效)", "Removing this disables Ash-Scooping gameplay (only applies in non-Expert mode)" })
    public boolean disableMufflerPart = false;

    @Configurable
    @Configurable.Range(min = 36, max = 216)
    @Configurable.Comment({ "扩展样板供应器容量", "专家模式下，此选项无效",
            "Extended Pattern Provider Size", "In Expert mode, this option is invalid" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Extended Pattern Provider Size", cn = "扩展样板供应器容量")
    @Configurable.Gui.Slider
    public int exPatternSize = 36;

    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Default Value for Rename Pattern", cn = "重命名样板的默认值")
    @Configurable.Comment({ "在装配线模式编码带有重命名物品的样板时使用的默认名字", "The default name used when encoding patterns with renamed items in assembly line mode" })
    public String renamePatternDefaultString = "";

    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Allow Missing Crafting Jobs", cn = "允许下单缺少材料的合成任务")
    @Configurable.Comment({ "允许在 AE2 中下单缺少原料的任务", "缺少的原料将以“正在合成”的状态被等待接收", "Allow placing orders for tasks that are missing ingredients in AE2", "Missing ingredients will be in a 'crafting' state waiting to be received" })
    public boolean allowMissingCraftingJobs = true;

    @Configurable
    @Configurable.Comment({ "启用后，样板供应器/样板总成会显示在旅行权杖的节点列表中，以便捷传送", "When enabled, Pattern Providers/Pattern Assemblers will appear in the node list of the Staff Of Travelling for easy teleportation" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Staff Of Travelling Pattern Nodes", cn = "旅行权杖样板节点")
    public boolean staffOfTravellingPatternNodes = true;

    @Configurable
    @Configurable.Comment({ "启用后，且未开启 EMI 作弊时，EMI 的作弊交互功能将转为试图从现有的ME终端/无线终端中提取物品", "When enabled, and EMI cheats are not enabled, EMI's cheat interaction feature will attempt to extract items from existing ME Terminals/Wireless Terminals" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Non-Cheat EMI Interaction", cn = "非作弊时EMI交互")
    public boolean nonCheatEmiInteraction = true;

    @Configurable
    @Configurable.Comment({ "启用后，且未开启 EMI 作弊时，在 EMI 界面中悬停物品时，将显示 AE 系统中该物品的数量信息", "When enabled, and EMI cheats are not enabled, hovering over an item in the EMI interface will show the quantity information of that item in the AE system" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Show AE Amount Tooltip Everywhere in EMI", cn = "在EMI显示 AE 数量提示")
    public boolean showAEAmountTooltipEverywhereEmi = true;

    @Configurable
    @Configurable.Comment({ "启用后，选取方块时，若AE终端没有相关物品，但相关物品可合成，则自动触发合成请求", "When enabled, when picking a block, if the AE terminal does not have the relevant item but it can be crafted, an automatic crafting request is triggered" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Auto Craft on Pick Block", cn = "选取方块自动合成")
    public boolean pickCraft = true;

    @Configurable
    @Configurable.Comment({ "选取方块时，自动触发的最大合成任务数", "The maximum number of crafting tasks automatically triggered when picking a block" })
    @Configurable.Range(min = 1, max = 100)
    @Configurable.Gui.Slider
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Pick Block Craft Max Tasks", cn = "选取方块合成最大任务数")
    public int pickCraftMaxTasks = 3;

    // 性能优化设置
    @Configurable
    @Configurable.Comment({ "快速加载多方块结构页面，减少不必要的加载时间", "Fast loading of multiblock structure pages to reduce unnecessary loading time" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Fast Multiblock Page Loading", cn = "快速多方块页面加载")
    public boolean fastMultiBlockPage = true;

    @Configurable
    @Configurable.Comment({ "批处理模式的最大持续时间（tick）", "Maximum duration of batch processing mode (ticks)" })
    @Configurable.Range(min = 600, max = 144000)
    @Configurable.Gui.Slider
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Batch Processing Max Duration", cn = "批处理模式最大持续时间")
    public int batchProcessingMaxDuration = 1200;

    // 挖掘系统设置
    @Configurable
    @Configurable.Comment({ "连锁挖掘（不连续模式）时，检查相邻方块的范围", "The range to check adjacent blocks during chain mining (non-continuous mode)" })
    @Configurable.Range(min = 1, max = 20)
    @Configurable.Gui.Slider
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Chain Mining Range", cn = "连锁挖掘检查范围")
    public int ftbUltimineRange = 3;

    @Configurable
    @Configurable.Comment({ "连锁挖掘功能的方块黑名单", "Block blacklist for chain mining feature" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Chain Mining Blacklist", cn = "连锁挖掘黑名单")
    public String[] breakBlocksBlackList = { "ae2:cable_bus" };

    // 界面和显示设置
    @Configurable
    @Configurable.Comment({ "禁用后，不同存档的 EMI 收藏夹将相互独立", "After disabling, EMI favorites from different saves will be independent of each other" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "EMI Global Favorites", cn = "EMI 全局收藏夹")
    public boolean emiGlobalFavorites = true;

    @Configurable
    @Configurable.Comment({ "禁用爆弹物品的使用",
            "警告：爆弹会造成极大范围的破坏！如果你不想爆弹破坏重要的东西，请确保提前备份存档。",
            "Disable the use of Charge Bomb items",
            "Warning: Charge Bombs can cause massive destruction! If you don't want Charge Bombs to destroy important things, make sure to back up your save in advance." })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Disable Charge Bomb", cn = "禁用爆弹")
    public boolean disableChargeBomb = false;

    @Configurable
    @Configurable.Comment({ "在物品下方显示英文名称", "Show the English name below the item" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Show Item English Name", cn = "显示物品英文名称")
    public boolean showEnglishName = false;

    @Configurable
    @Configurable.Comment({ "调整监控器的最大成型尺寸", "Adjust the maximum formed size of the monitor" })
    @Configurable.Range(min = 4, max = 64)
    @Configurable.Gui.Slider
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Maximum Monitor Size", cn = "监控器最大尺寸")
    public int maxMonitorSize = 16;

    @Configurable
    @Configurable.Comment({ "引雷针在工作时是否生成闪电特效", "Whether the lightning rod generates lightning effects when working" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Lightning Rod Effect", cn = "引雷针特效")
    public boolean lightningRodEffect = true;

    @Configurable
    @Configurable.Comment({ "启用内置夜视。该效果也可在游戏内按绑定热键切换", "Enable built-in night vision. This effect can also be toggled in-game by pressing the bound hotkey"
    })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Built-in Night Vision", cn = "内置夜视")
    public boolean nightVision = false;

    @Configurable
    @Configurable.Comment({ "禁用后将渲染视角外，且渲染器被标记为Global的机器，一些高级特效机器需要开启此选项才能正常渲染", "When turned disable, machines that are outside the field of view and whose renderer is marked as Global will be rendered. Some advanced effect machines need to turn on this option to render properly" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Disable Embeddium Global BE Culling", cn = "禁用Embbedium Global方块实体剔除")
    public boolean disableEmbeddiumBECulling = true;

    @Configurable
    @Configurable.Comment({ "启用后，进入游戏时，若多方块结构未能成型，则将错误信息将发送给机器的所有者", "When enabled, if the multiblock structure fails to form when entering the game, the error message will be sent to the owner of the machine" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Send Multiblock Error Messages", cn = "发送多方块错误信息")
    public boolean sendMultiblockErrorMessages = true;

    @Configurable
    @Configurable.Comment({ "一些机器内容会以服务器语言的翻译呈现", "Some machine contents will be presented in the server language translation" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Server language", cn = "服务器语言")
    public String serverLang = "en_us";

    // 开发和调试设置
    @Configurable
    @Configurable.Comment({ "开启开发者模式", "Enable Developer Mode" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Developer Mode", cn = "开发者模式")
    public boolean dev = false;

    @Configurable
    @Configurable.Comment({ "启用后将显示详细的启动日志输出，包含所有 DEBUG 级别的日志（增加日志文件大小但便于调试）", "When enabled, shows detailed startup log output including all DEBUG level logs (increases log file size but useful for debugging)" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] Detailed Logging", cn = "[调试] 详细日志输出")
    public boolean detailedLogging = false;

    @Configurable
    @Configurable.Comment({ "检查配方之间的冲突问题", "Check for conflicts between recipes" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] Recipe Conflict Check", cn = "[调试] 配方冲突检查")
    public boolean recipeCheck = false;

    @Configurable
    @Configurable.Comment({ "启用 AE2 和同步组件的详细日志", "Enable detailed logging for AE2 and sync components" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] AE2 & Sync Logging", cn = "[调试] AE2 和同步日志")
    public boolean aeLog = false;

    @Configurable
    @Configurable.Comment({ "启用 AE2 无线网络调试日志", "Enable AE2 wireless network debug logging" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "[Debug] AE2 & Sync Logging", cn = "[调试] AE2 无线网络调试日志")
    public boolean aeWirelessLog = false;

    @Configurable
    @Configurable.Comment({ "AE2 无线网络使用的存储键，切换后将使用新的存储键重新生成网络（警告：切换后所有AE无线设备的设置将重置！）", "The storage key used by the AE2 wireless network. After switching, a new storage key will be used to regenerate the network (Warning: After switching, all AE wireless device settings will be reset!)" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", cn = "AE2 网格存储键", en = "AE2 Grid Storage Key")
    public String aeGridKey = "four";

    @Configurable
    @Configurable.Comment({ "Spark 性能分析器的启动阶段", "The startup phase of the Spark profiler" })
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Spark Profiler Start Phase", cn = "Spark 分析器启动阶段")
    public SparkRange startSpark = SparkRange.NONE;

    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Mob Settings", cn = "生物设置")
    public MobConfig mobConfig = new MobConfig();

    @DataGeneratorScanned
    public static class MobConfig {

        @Configurable
        @Configurable.Comment({ "当玩家在某动物附近食用其来源食物时，影响的半径（格）", "The radius (blocks) affected when a player consumes food derived from an animal near that animal" })
        @Configurable.Range(min = 1, max = 64)
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Carnivory Punish Radius", cn = "食肉惩罚半径")
        @Configurable.Gui.Slider
        public int cannibalismRadius = 32;

        @Configurable
        @Configurable.Comment({ "当玩家在某动物附近食用其来源食物时，对该动物造成的伤害值（半颗心=1.0）", "The amount of damage dealt to the animal when a player consumes food derived from that animal nearby (Half Heart = 1.0)" })
        @Configurable.Range(min = 0, max = 100)
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Carnivory Punish Damage", cn = "食肉惩罚伤害")
        public float cannibalismDamage = 1.0F;

        @Configurable
        @Configurable.Comment({ "启用后，所有生物将能够自然回血", "When enabled, all mobs will naturally regenerate health" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Mob Natural Regeneration", cn = "生物自然回血")
        public boolean naturalRegeneration = true;
    }

    @Configurable
    @RegisterLanguage(namePrefix = "config.gtocore.option", en = "HUD Settings", cn = "HUD 设置")
    public HUDConfig hud = new HUDConfig();

    @DataGeneratorScanned
    public static class HUDConfig {

        @Configurable
        @Configurable.Comment({ "启用无线能量 HUD 显示", "Enable Wireless Energy HUD display" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Wireless Energy HUD Enabled", cn = "无线能量 HUD 启用")
        public boolean wirelessEnergyHUDEnabled = false;

        @Configurable
        @Configurable.Comment({ "无线能量 HUD 的默认 X 相对位置", "0意味着屏幕左侧，100意味着屏幕右侧", "The default X relative position of the Wireless Energy HUD", "0.0 means the left side of the screen, 1.0 means the right side of the screen" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Wireless Energy HUD Default X", cn = "无线能量 HUD 默认 X 位置")
        @Configurable.Range(min = 0, max = 100)
        @Configurable.Gui.Slider
        public int wirelessEnergyHUDDefaultX = 5;

        @Configurable
        @Configurable.Comment({ "无线能量 HUD 的默认 Y 相对位置", "0意味着屏幕顶部，100意味着屏幕底部", "The default Y relative position of the Wireless Energy HUD", "0.0 means the top of the screen, 1.0 means the bottom of the screen" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Wireless Energy HUD Default Y", cn = "无线能量 HUD 默认 Y 位置")
        @Configurable.Range(min = 0, max = 100)
        @Configurable.Gui.Slider
        public int wirelessEnergyHUDDefaultY = 75;

        @Configurable
        @Configurable.Comment({ "无线能量 HUD 显示的历史秒数", "例如：设为30则显示过去30秒的能量变化情况", "The number of historical seconds displayed by the Wireless Energy HUD", "For example: setting it to 30 will show the energy changes over the past 30 seconds" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Wireless Energy HUD History Seconds", cn = "无线能量 HUD 历史秒数")
        @Configurable.Range(min = 5, max = 300)
        @Configurable.Gui.Slider
        public int wirelessEnergyHUDHistorySeconds = 30;

        @Configurable
        @Configurable.Comment({ "无线能量 HUD 折线颜色", "Wireless Energy HUD line color" })
        @RegisterLanguage(namePrefix = "config.gtocore.option", en = "Wireless Energy HUD Line Color", cn = "无线能量 HUD 折线颜色")
        @Configurable.StringPattern("#[0-9a-fA-F]{1,6}")
        @Configurable.Gui.ColorValue
        public String wirelessEnergyHUDLineColor = "#ECEC71";
    }

    public static <T> void set(String fieldName, T value) {
        getConfig(GTOCore.MOD_ID).ifPresent(config -> {
            ((ConfigValue<T>) (config.getValueMap().get(fieldName))).setValue(value);
            ConfigIO.saveClientValues(config);
            ConfigIO.reloadClientValues(config);
        });
    }

    public static <T> void set(String fieldName, T value, String... objectPath) {
        getConfig(GTOCore.MOD_ID).ifPresent(config -> {
            if (objectPath.length == 0) {
                set(fieldName, value);
                return;
            }
            ObjectValue valueMap0 = (ObjectValue) config.getValueMap().get(objectPath[0]);
            if (objectPath.length == 1) {
                ((ConfigValue<T>) (valueMap0.getChildById(fieldName))).setValue(value);
            } else {
                for (int i = 1; i < objectPath.length; i++) {
                    valueMap0 = (ObjectValue) valueMap0.getChildById(objectPath[i]);
                }
                ((ConfigValue<T>) (valueMap0.getChildById(fieldName))).setValue(value);
            }
            ConfigIO.saveClientValues(config);
            ConfigIO.reloadClientValues(config);
        });
    }
}
