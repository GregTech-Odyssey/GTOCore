package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.ComponentSlang.AfterModuleInstallation
import com.gtocore.common.data.translation.ComponentSlang.RunningRequirements

import net.minecraft.network.chat.Component

object GTOMachineTooltipsA : AutoInitialize<GTOMachineTooltipsA>() {

    // 溶解罐
    val DissolvingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolving_tank")

        section(RunningRequirements)
        command("必须保证输入的流体与配方流体比例相同，否则无产物输出" translatedTo "Must ensure the ratio of input fluid to recipe fluid is the same, otherwise no product output")
        increase("当安装附属模块时，模块将帮助机器自动进行原料配比，无上述条件限制" translatedTo "When the auxiliary module is installed, the module will help the machine automatically match the raw materials, without the above conditions")
    }

    // 框镖巨型核聚变反应堆
    val kuangbiaoGiantNuclearFusionReactorTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("kuangbiao_giant_nuclear_fusion_reactor")

        section(AfterModuleInstallation)
        info("模块分为两种：高能模块与超频模块" translatedTo "There are two types of modules: high-energy modules and overclock modules")
        increase("每多安装一个高能模块，反应堆热容量提升一倍" translatedTo "For each additional high-energy module installed, the reactor's heat capacity is doubled")
        command("高能模块必须按顺序安装，且不可重复安装相同模块" translatedTo "High-energy modules must be installed in order and the same module cannot be installed repeatedly")
        command("高能模块总计可提升三次热容量" translatedTo "High-energy modules can increase heat capacity a total of three times")
        increase("超频模块允许安装超频仓/线程仓" translatedTo "Overclock modules allow the installation of overclocking chambers/thread chambers")
        command("超频模块仅允许安装一个" translatedTo "Only one overclock module is allowed to be installed")
        info("多方块预览中的前三个预览位分别对应前三级高能模块安装后的状态" translatedTo "The first three preview slots in the multiblock preview correspond to the states after installing the first three high-energy modules")
        info("最后一个预览位对应安装超频模块后的状态" translatedTo "The last preview slot corresponds to the state after installing the overclock module")

        command("若高能模块与超频模块存在冲突，请先安装高能模块，再安装超频模块" translatedTo "If there is a conflict between the high-energy module and the overclock module, please install the high-energy module first, then install the overclock module")
    }
    val KuangbiaoGiantNuclearFusionReactorEnergyStorageTooltip = { eut: Long ->
        ComponentListSupplier {
            setTranslationPrefix("kuangbiao_giant_nuclear_fusion_reactor_energy_storage")

            command(
                ComponentSupplier(Component.translatable("gtceu.machine.fusion_reactor.capacity", eut)) +
                    (" [可安装模块扩容]" translatedTo " [can be expanded by installing modules]").rainbowFast(),
            )

            command(ComponentSupplier(Component.translatable("gtceu.machine.fusion_reactor.overclocking")))
        }
    }
}
