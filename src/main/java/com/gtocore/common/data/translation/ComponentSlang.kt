package com.gtocore.common.data.translation

import com.gtocore.api.misc.AutoInitialize
import com.gtocore.utils.ComponentSupplier
import com.gtocore.utils.initialize
import com.gtocore.utils.toLiteralSupplier
import com.gtocore.utils.translatedTo

object ComponentSlang : AutoInitialize<ComponentSlang>() {
    // ****** 量词 ****** //
    val Infinite = ("无限" translatedTo "Infinite").rainbow().initialize()

    // ****** 格式 ****** //
    val Bar = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}- ".toLiteralSupplier().gold() }.initialize()
    val Tab = { tab: Int -> "  ".repeat(tab).toLiteralSupplier() }.initialize()
    val Star = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⭐ ".toLiteralSupplier().gold() }.initialize()
    val OutTopic = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}# ".toLiteralSupplier().gray() }.initialize()

    // ****** 单位 ****** //
    val TemperatureMax = { temp: Int -> (("最高温度: " translatedTo "Max Temperature: ") + (temp.toLiteralSupplier().red().bold())).gold() }.initialize()
    val TemperatureMin = { temp: Int -> ("最低温度: " translatedTo "Min Temperature: ") + (temp.toLiteralSupplier()).blue().bold() }.initialize()
    val Temperature = { temp: Int -> ("温度: " translatedTo "Temperature: ") + (temp.toLiteralSupplier()).gold().bold() }.initialize()
    val Capacity = { capacity: String -> ("容量: " translatedTo "Capacity: ") + (capacity.toLiteralSupplier()).white().bold() }.initialize()

    // ****** 特别动词 ****** //
    val Explosion = ("爆炸" translatedTo "Explosion").red().bold().initialize()

    // ****** 常用话术 ****** //
    val RecommendedToUse = { other: ComponentSupplier -> (("推荐使用" translatedTo "Recommended to use ") + other.gold()).aqua() }.initialize()

    // ****** 常用，带一级缩进，应放最底下 ****** //
    val BewareOfBurns = Star(1) + ("小心烫伤" translatedTo "Beware of burns").red().bold().initialize()

    // ****** 其他 ****** //
    val GTOCoreNormal = ("GTOCore" translatedTo "GTOCore").gold().initialize()
    val GTOCoreRemix = ("GTOCore" translatedTo "GTOCore").scrollFullColor().initialize()
    val EditionByGTONormal = OutTopic(1) + (("经 " translatedTo "By ") + GTOCoreNormal + (" 修改" translatedTo " Edition")).gray().italic().initialize()
    val EditionByGTORemix = OutTopic(1) + (("经 " translatedTo "By ") + GTOCoreRemix + (" 修改" translatedTo " Edition")).gray().italic().initialize()
}
