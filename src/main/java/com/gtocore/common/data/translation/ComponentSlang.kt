package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.initialize
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.lang.translatedTo
import com.gtocore.api.misc.AutoInitialize

import com.gregtechceu.gtceu.api.GTValues

object ComponentSlang : AutoInitialize<ComponentSlang>() {
    // ****** 量词 ****** //
    val Infinite = ("无限" translatedTo "Infinite").rainbow().initialize()

    // ****** 符号 ****** //
    val right = "✔".toLiteralSupplier()
    val wrong = "✘".toLiteralSupplier()

    // ****** 格式 ****** //
    val Bar = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}- ".toLiteralSupplier().gold() }.initialize()
    val Plus = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}+ ".toLiteralSupplier().gold() }.initialize()
    val Asterisk = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}* ".toLiteralSupplier().gold() }.initialize()
    val Tab = { tab: Int -> "  ".repeat(tab).toLiteralSupplier() }.initialize()
    val Star = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⭐ ".toLiteralSupplier().gold() }.initialize()
    val Circle = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}৹ ".toLiteralSupplier().gold() }.initialize()
    val Warning =
        { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⚠ ".toLiteralSupplier().red().bold() }.initialize()
    val OutTopic =
        { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}# ".toLiteralSupplier().gray() }.initialize()
    val Right = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}✔ ".toLiteralSupplier().gold() }.initialize()
    val Wrong = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}✘ ".toLiteralSupplier().red() }.initialize()
    val LegendSignalWrapper = { other: ComponentSupplier ->
        "111".toLiteralSupplier().obfuscated().scrollFullColor()
            .underline() + " ".toLiteralSupplier() + other + " ".toLiteralSupplier() + "111".toLiteralSupplier()
            .obfuscated().scrollFullColor().underline()
    }

    // ****** 单位 ****** //
    val Temperature =
        { temp: Int -> ("温度: " translatedTo "Temperature: ") + (temp.toLiteralSupplier()).gold().bold() }.initialize()
    val Capacity = { capacity: String ->
        ("容量: " translatedTo "Capacity: ") + (capacity.toLiteralSupplier()).white().bold()
    }.initialize()

    // ****** 常用话术 ****** //
    val RecommendedToUse =
        { other: ComponentSupplier -> (("推荐使用" translatedTo "Recommended to use ") + other.gold()).aqua() }.initialize()
    val RecommendedUseAs =
        { other: ComponentSupplier -> (("推荐用于" translatedTo "Recommended use it to ") + other.gold()).aqua() }.initialize()

    // ****** 机器专用术语 ******//
    val Explosion = ("爆炸" translatedTo "explode").red().bold().initialize()
    val TemperatureMax = { temp: Int ->
        ("最高温度: " translatedTo "Max Temperature: ") + (temp.toLiteralSupplier()).gold()
    }.initialize()
    val RunningRequirements = ("运行要求" translatedTo "Running Requirements").initialize()
    val MainFunction = ("主要功能" translatedTo "Main Function").initialize()
    val EfficiencyBonus = ("效率加成" translatedTo "Efficiency Bonus").initialize()
    val ParallelBonus = ("并行加成" translatedTo "Parallel Bonus").initialize()
    val PowerGenerationEfficiency = ("发电效率" translatedTo "Power Generation Efficiency").initialize()
    val AfterModuleInstallation = ("安装模块后" translatedTo "After Module Installation").initialize()
    val BaseProductionEut =
        { eut: Long -> ("基础产能功率: " translatedTo "Base Production EUt: ") + (eut.toLiteralSupplier()).yellow() + "EU/t".toLiteralSupplier() }.initialize()
    val RotorEfficiency = { tier: Int ->
        var name = GTValues.VNF[tier] + "§r"
        "转子支架每超过${name}一级，每级增加10%效率，并翻倍输出功率" translatedTo
            "Each Rotor Holder above $name adds 10% efficiency and multiplies EU/t by 2"
    }.initialize()
    val RecipeLevelBelow = { tier: Int ->
        var name = GTValues.VNF[tier] + "§r"
        "只能运行${name}级及以下配方" translatedTo "Can only run $name-tier and below recipes"
    }.initialize()
    val UsePerHourLubricant =
        { cnt: Long -> "每小时消耗${cnt}mB润滑油" translatedTo "Use ${cnt}mB Lubricant Per Hour" }.initialize()
    val CoilEfficiencyBonus = ("线圈效率加成" translatedTo "Coil Efficiency Bonus").initialize()
    val BeAwareOfBurn = ("小心烫伤！" translatedTo "BE AWARE OF BURNS!").red().bold().initialize()
    val PurifyLevel = { tier: Int ->
        ("净化等级：" translatedTo "Purify Level: ") + tier.toLiteralSupplier().green().bold()
    }.initialize()
    val OutputProbability = ("产出概率" translatedTo "Output Probability").initialize()

    // ****** 其他 ****** //
    val GTOSignal_LOGO_GTOCoreNormal = ("GTOCore" translatedTo "GTOCore").gold().initialize()
    val GTOSignal_LOGO_GTOCoreRemix = ("GTOCore" translatedTo "GTOCore").scrollFullColor().initialize()
    val GTOSignal_Edition_ByGTONormal =
        OutTopic(1) + (("经 " translatedTo "By ") + GTOSignal_LOGO_GTOCoreNormal + (" 修改" translatedTo " Edition")).gray()
            .italic().initialize()
    val GTOSignal_Edition_ByGTORemix =
        OutTopic(1) + (("经 " translatedTo "By ") + GTOSignal_LOGO_GTOCoreRemix + (" 修改" translatedTo " Edition")).gray()
            .italic().initialize()
    val GTOSignal_Machine_MilestoneByGTORemix =
        OutTopic(1) + LegendSignalWrapper(GTOSignal_LOGO_GTOCoreRemix) + ("里程碑建筑" translatedTo "Milestone Building")
    val GTOSignal_Machine_MiracleByGTORemix =
        OutTopic(1) + LegendSignalWrapper(GTOSignal_LOGO_GTOCoreRemix) + ("奇迹工程" translatedTo "Miracle Project")
}
