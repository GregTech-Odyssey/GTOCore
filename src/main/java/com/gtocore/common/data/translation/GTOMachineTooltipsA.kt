package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.ComponentSlang.RunningRequirements

object GTOMachineTooltipsA : AutoInitialize<GTOMachineTooltipsA>() {

    // 溶解罐
    val DissolvingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolving_tank")

        section(RunningRequirements)
        command("必须保证输入的流体与配方流体比例相同，否则无产物输出" translatedTo "Must ensure the ratio of input fluid to recipe fluid is the same, otherwise no product output")
        increase("当安装附属模块时，模块将帮助机器自动进行原料配比，无上述条件限制" translatedTo "When the auxiliary module is installed, the module will help the machine automatically match the raw materials, without the above conditions")
    }
}
