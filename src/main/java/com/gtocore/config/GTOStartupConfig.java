package com.gtocore.config;

import com.gtolib.GTOCore;

import net.minecraftforge.fml.loading.FMLPaths;

import dev.shadowsoffire.placebo.config.Configuration;

import java.io.File;

/**
 * 部分配置需要在游戏极早期加载时获取
 */
public class GTOStartupConfig {

    public static final String difficultyIntroduction = """
            游戏难度：简单/普通/专家 (Simple/Normal/Expert)
            部分特性如下：

            简单模式特性：
            AE2使用原始配方（无魔改）
            ME网络无限频道
            增幅蒸汽/发电产能，提升发电阵列机器容量/涡轮产出
            可配置维护仓上下限提升
            无需超净间！
            部分配方产量提升/简化
            （MV阶段）ME简易样板总成: 含9个样板槽的样板总成
            装配线无需有序输入
            基岩矿脉/基岩流体矿脉储量无限！
            并行仓/加速仓等数值提升
            蒸汽大机器的并行数值提升
            连锁采掘的限制最大数量提升

            普通模式特性：
            AE2全面魔改（MV阶段初步解锁，HV开启自动化）
            ME网络无限频道
            怪物增强！（更高血量，更强攻击力，请小心应对）
            装配线需要有序输入物品才能工作
            方块挖掘速度惩罚（你需要手持GT工具才能高效挖掘方块）

            专家模式特性：
            更难的AE2魔改（MV阶段初步解锁，EV开启自动化）
            ME网络频道有限！请合理规划基地
            怪物增强！（更高血量，更强攻击力，请小心应对）
            方块挖掘速度惩罚（你需要手持GT工具才能高效挖掘方块）
            更少的蒸汽/发电产能
            机器淋雨爆炸！
            配方产出降低/复杂化
            维护仓上下限降低
            机器运行时能量不足将导致配方失败且输入被清空！
            装配线需要有序输入物品与流体才能工作
            并行仓/加速仓等数值降低
            发电设备在输出仓满时将继续发电且销毁多余能量！

            Game Difficulty: Simple/Normal/Expert
            Some features are as follows:

            Simple Mode Features:
            AE2 uses original recipes (no magic modification)
            ME network has unlimited channels
            Increased steam/power generation capacity, improved machine capacity/turbine output of power generation array
            Configurable maintenance bay upper and lower limits
            No clean room required!
            Some recipe output increase/simplification
            (MV stage) ME Simple Pattern Buffer: Pattern buffer with 9 pattern slots
            Assembler does not require ordered input
            Bedrock Ore Vein/Bedrock Fluid Ore Vein has unlimited reserves!
            Parallel Hatch/Overclocker Hatch and other values are increased
            Increased parallel values for large steam machines
            Increased maximum number of vein mining

            Normal Mode Features:
            AE2 fully modified (MV stage preliminary unlock, HV opens automation)
            ME network has unlimited channels
            Monster enhancement! (Higher health, stronger attack power, please deal with it carefully)
            Assembler requires ordered input of items to work
            Block mining speed penalty (you need to hold a GT tool to efficiently mine blocks)

            Expert Mode Features:
            More difficult AE2 magic modification (MV stage preliminary unlock, EV opens automation)
            ME network channels are limited! Please plan your base reasonably
            Monster enhancement! (Higher health, stronger attack power, please deal with it carefully)
            Block mining speed penalty (you need to hold a GT tool to efficiently mine blocks)
            Less steam/power generation capacity
            Machines explode when rained on!
            Recipe output reduction/complexity
            Maintenance bay upper and lower limits reduced
            Machine running out of energy will cause the recipe to fail and the input to be cleared!
            Assembler requires ordered input of items and fluids to work
            Parallel Hatch/Overclocker Hatch and other values are reduced
            Power generation equipment will continue to generate power when the output bay is full and destroy excess energy!
            """;
    public static File configDir;
    public static Configuration config;
    // public static boolean devMode = false;
    // public static boolean selfRestraint = false;
    public static GTOConfig.Difficulty difficulty = GTOConfig.Difficulty.Normal;
    public static String serverLang = "en_us";

    static {
        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), GTOCore.MOD_ID);
        config = new Configuration(new File(configDir, GTOCore.MOD_ID + "_startup.cfg"));
        // devMode = config.getBoolean("Dev mode", "general", false, "启用开发者模式\nEnables developer mode");
        difficulty = GTOConfig.difficultyNameOf(config.getString("Difficulty", "general", GTOConfig.Difficulty.Normal.name(), difficultyIntroduction, new String[] {
                GTOConfig.Difficulty.Simple.name(),
                GTOConfig.Difficulty.Normal.name(),
                GTOConfig.Difficulty.Expert.name()
        }));
        GTOCore.difficulty = difficulty.ordinal() + 1;
        // selfRestraint = config.getBoolean("Self Restraint Mode", "general", false,
        // "启用自我约束模式，在该模式下完全禁止作弊命令的使用！\nEnables Self Restraint Mode, which completely disables the use of
        // cheats/commands!");
        serverLang = config.getString("Server Language", "general", "en_us", "服务器语言（一些机器内容会以服务器语言的翻译呈现）\nServer language (some machine contents will be presented in the server language translation");
        config.setTitle("GTO Startup Config");
        config.setComment("更改这些配置后需要重启游戏\nChanging this config requires a game restart");
        if (config.hasChanged()) config.save();
        config.setCategoryRequiresMcRestart("general", true);
    }
}
