package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.initialize
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.lang.translatedTo
import com.gtocore.api.misc.AutoInitialize

object ComponentSlang : AutoInitialize<ComponentSlang>() {
    // ****** 量词 ****** //
    val Infinite = ("无限" translatedTo "Infinite").rainbow().initialize()

    // ****** 符号 ****** //
    val right = "✔".toLiteralSupplier()
    val wrong = "✘".toLiteralSupplier()

    // ****** 格式 ****** //
    val Bar = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}- ".toLiteralSupplier().gold() }.initialize()
    val Tab = { tab: Int -> "  ".repeat(tab).toLiteralSupplier() }.initialize()
    val Star = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⭐ ".toLiteralSupplier().gold() }.initialize()
    val Circle = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}○ ".toLiteralSupplier().gold() }.initialize()
    val Warning = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⚠ ".toLiteralSupplier().red().bold() }.initialize()
    val OutTopic = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}# ".toLiteralSupplier().gray() }.initialize()
    val LegendSignalWrapper = { other: ComponentSupplier -> "111".toLiteralSupplier().obfuscated().scrollFullColor().underline() + " ".toLiteralSupplier() + other + " ".toLiteralSupplier() + "111".toLiteralSupplier().obfuscated().scrollFullColor().underline() }

    // ****** 单位 ****** //
    val TemperatureMax = { temp: Int -> (("最高温度: " translatedTo "Max Temperature: ") + (temp.toLiteralSupplier().red().bold())).gold() }.initialize()
    val TemperatureMin = { temp: Int -> ("最低温度: " translatedTo "Min Temperature: ") + (temp.toLiteralSupplier()).blue().bold() }.initialize()
    val Temperature = { temp: Int -> ("温度: " translatedTo "Temperature: ") + (temp.toLiteralSupplier()).gold().bold() }.initialize()
    val Capacity = { capacity: String -> ("容量: " translatedTo "Capacity: ") + (capacity.toLiteralSupplier()).white().bold() }.initialize()

    // ****** 特别动词 ****** //
    val Explosion = ("爆炸" translatedTo "Explosion").red().bold().initialize()

    // ****** 常用话术 ****** //
    val RecommendedToUse = { other: ComponentSupplier -> (("推荐使用" translatedTo "Recommended to use ") + other.gold()).aqua() }.initialize()
    val RecommendedUseAs = { other: ComponentSupplier -> (("推荐用于" translatedTo "Recommended use it to ") + other.gold()).aqua() }.initialize()

    // ****** 常用，带一级缩进，应放最底下 ****** //
    val BewareOfBurns = Star(1) + ("小心烫伤" translatedTo "Beware of burns").red().bold().initialize()
    val CoilEfficiencyBonus = { other: ComponentSupplier -> Star(1) + ("线圈效率加成: " translatedTo "Coil Efficiency Bonus: ").green().initialize() + other.gold() }.initialize()

    // ****** 其他 ****** //
    val GTOSignal_LOGO_GTOCoreNormal = ("GTOCore" translatedTo "GTOCore").gold().initialize()
    val GTOSignal_LOGO_GTOCoreRemix = ("GTOCore" translatedTo "GTOCore").scrollFullColor().initialize()
    val GTOSignal_Edition_ByGTONormal = OutTopic(1) + (("经 " translatedTo "By ") + GTOSignal_LOGO_GTOCoreNormal + (" 修改" translatedTo " Edition")).gray().italic().initialize()
    val GTOSignal_Edition_ByGTORemix = OutTopic(1) + (("经 " translatedTo "By ") + GTOSignal_LOGO_GTOCoreRemix + (" 修改" translatedTo " Edition")).gray().italic().initialize()
    val GTOSignal_Machine_MilestoneByGTORemix = OutTopic(1) + LegendSignalWrapper(GTOSignal_LOGO_GTOCoreRemix) + ("里程碑建筑" translatedTo "Milestone Building")
    val GTOSignal_Machine_MiracleByGTORemix = OutTopic(1) + LegendSignalWrapper(GTOSignal_LOGO_GTOCoreRemix) + ("奇迹工程" translatedTo "Miracle Project")
}
