package com.gtocore.common.data.translation

import com.gtocore.api.data.Algae
import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.ComponentSlang.AfterModuleInstallation
import com.gtocore.common.data.translation.ComponentSlang.MainFunction
import com.gtocore.common.data.translation.ComponentSlang.RunningRequirements

import net.minecraft.network.chat.Component

import appeng.api.config.PowerUnits
import com.gregtechceu.gtceu.config.ConfigHolder

object GTOMachineTooltipsA : AutoInitialize<GTOMachineTooltipsA>() {

    val meEnergySubstationTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("me_energy_substation")

        section(MainFunction)
        ok("为ME网络提供额外的能量供应" translatedTo "Provides additional energy supply for the ME network")
        command(
            ("每一点EU可以转换成 " translatedTo "Each point of EU can be converted into ") +
                PowerUnits.FE.convertTo(PowerUnits.AE, ConfigHolder.INSTANCE.compat.energy.euToFeRatio.toDouble()).toLiteralSupplier() +
                (" 点AE能量" translatedTo " points of AE energy"),
        )
        info("使用ME能量访问仓导出能量到ME网络" translatedTo "Use the ME Energy Access Hatch to export energy to the ME network")
        increase("玻璃等级每级可将转换效率提升30%" translatedTo "Each glass level can increase the conversion efficiency by 30%")
        section(AfterModuleInstallation)
        increase("安装模块可使转换效率额外x2" translatedTo "Installing modules can further double the conversion efficiency")
    }

    val spaceBioResearchModuleTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("space_bio_research_module")

        section(MainFunction)
        command("用于在空间站内进行生物研究" translatedTo "Used for biological research in the space station")
        command("超净间环境等级由环境维护舱决定" translatedTo "The cleanroom environment level is determined by the Environmental Maintenance Module")
        info("当运行培养缸或生化反应室配方时，提供可调节的0~40Sv背景辐射环境" translatedTo "Provides an adjustable 0~40Sv background radiation environment when running bioreactor or biochemical reaction chamber recipes")
    }
    val spaceElevatorConnectorModuleTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("space_elevator_connector_module")

        command("与当前星球的太空电梯连接" translatedTo "Connects to the space elevator of the current planet")
        increase(
            "连接后，空间站各运行模块（如轨道冶炼舱等）可获得(0.8^n)×的耗时减免，n为太空电梯的动力模块等级" translatedTo
                "After connecting, each operating module of the space station (such as orbital smelting chamber, etc.) can get a time reduction of (0.8^n)×, where n is the power module level of the space elevator",
        )
        increase(
            "太空电梯安装的模块也将获得额外(0.8^(n/2))×的耗时减免" translatedTo
                "Modules installed on the space elevator will also receive a time reduction of (0.8^(n/2))×",
        )
        decrease("会增加太空电梯50%的算力消耗" translatedTo "Increases the space elevator's Computational Workload consumption by 50%")

        command("该模块仅能连接在其他模块的下方" translatedTo "This module can only connect below other modules")
    }

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
        command("高能模块总计可提升四次热容量" translatedTo "High-energy modules can increase heat capacity a total of four times")
        increase("超频模块允许安装超频仓/线程仓" translatedTo "Overclock modules allow the installation of overclocking chambers/thread chambers")
        command("超频模块仅允许安装一个" translatedTo "Only one overclock module is allowed to be installed")
        info("多方块预览中的前四个预览位分别对应前四级高能模块安装后的状态" translatedTo "The first four preview slots in the multiblock preview correspond to the states after installing the first three high-energy modules")
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
    val SpaceStationDockingModule = ComponentListSupplier {
        setTranslationPrefix("space_station_docking_module")
        important("使用高级终端的模块搭建功能来选择该舱的不同形态" translatedTo "Use the module building function of the advanced terminal to select different forms of this chamber")
//        important("因该机器为多形态机器，故不支持镜像搭建" translatedTo "As this machine is a multi-form machine, mirror building is not supported")
//        important("请通过旋转主机来调整对接口方向" translatedTo "Please adjust the docking port direction by rotating the main machine")
        error("无法同时成型多个形态" translatedTo "Cannot form multiple shapes at the same time")
    }

    // 大型藻类养殖中心
    val LargeAlgaeFarmTooltips = ComponentListSupplier {
        setTranslationPrefix("large_algae_farm")

        section(RunningRequirements)
        command("耗能：(电压等级对应电压/2) EU/t" translatedTo "Energy consumption: (voltage level corresponding voltage / 2) EU/t")
        important(
            "每种藻类每次繁殖需要消耗1mb/个体/秒的生物质，请确保输入总线提供足够的生物质，否则藻类可能会死亡" translatedTo
                "Each type of algae requires 1mb/individual/second of biomass for each reproduction. Please ensure that the input bus provides enough biomass, otherwise the algae may die",
        )
        section("藻类生长机制" translatedTo "Algae Growth Mechanism")

        command("每秒更新一次藻类生长状态" translatedTo "Updates algae growth status once per second")
        important("每次更新，藻类种群会根据其环境最大容量与种群权重呈S型增长" translatedTo "With each update, the algae population grows in an S-curve based on its environmental maximum capacity and population weight")
        command("注意：每种藻类仅对其互补颜色的光源有最大提升效果" translatedTo "Note: Each type of algae only has the maximum enhancement effect on its complementary color light source")

        info("公式：增长量 = x(cap-x)(1-f)/(x+f(cap-x))" translatedTo "Formula: Growth amount = x(cap-x)(1-f)/(x+f(cap-x))")
        info(
            "其中x为当前种群数量" translatedTo
                "where x is the current population",
        )
        info(
            "cap决定环境最大容量,其值为(4^玻璃等级)*藻类权重" translatedTo
                "cap determines the environmental maximum capacity, its value is (4^[glass level])*[algae weight]",
        )
        info(
            "f为藻类的增长因子（越接近0越快），其值为0.1+0.9*e^(-(电压等级 + 1.0) * 藻类吸光/2)" translatedTo
                "f is the growth factor of algae(the closer to 0, the faster), where its value is 0.1+0.9*e^(-([voltage level] + 1.0) * [algae light absorption]/2)",
        )

        section("光吸收与权重机制" translatedTo "Light absorption & weight mechanics")
        info("藻类生长速度受环境光照强度影响" translatedTo "Algae growth rate is affected by environmental light intensity")
        info("每种颜色的卤素灯可以为对应波长范围的藻类提供额外光照，提升其种群权重" translatedTo "Each color of halogen lamp can provide additional illumination for algae in the corresponding wavelength range, increasing its population weight")
        info("向输入总线提供红/绿/蓝三种卤素灯以提升光照强度" translatedTo "Provide red/green/blue halogen lights to the input bus to enhance light intensity")
        command("每种颜色的卤素灯最多安装16个" translatedTo "A maximum of 16 halogen lights of each color can be installed")
        command(
            "光照强度 = min( min( 红色卤素灯数量,16 ) + min( 绿色卤素灯数量,16 ) + min( 蓝色卤素灯数量,16 ),16)" translatedTo
                "Light intensity = min( min( redHalogenLampCount,16 ) + min( greenHalogenLampCount,16 ) + min( blueHalogenLampCount,16 ),16 )",
        )

        info(
            "每次更新先按红/绿/蓝三色累计吸收：藻类的单色光吸收率 = (单色吸收数据(列于下表) / 255) * 单色光占比" translatedTo
                "In each update, first accumulate absorption by red/green/blue: Algae's monochromatic light absorption rate = (monochromatic absorption data (listed in the table below) / 255) * colorWeight",
        )
        info(
            "单色光占比为当前卤素灯数量占所有输入的卤素灯数量的比例" translatedTo
                "colorWeight is the proportion of the current halogen lamp count to the total input halogen lamp count",
        )

        info(
            "每种藻类的权重 = max( 红色光吸收率, 绿色光吸收率, 蓝色光吸收率 )，用于决定环境容量的占比：cap = 4^玻璃等级 * 权重" translatedTo
                "Algae weight = max( redRatio, greenRatio, blueRatio ), used to determine the proportion of environmental capacity: cap = 4^[glass level] * weight",
        )

        info(
            "当前光吸收值 = (r/255*红色光吸收率 + g/255*绿色光吸收率 + b/255*蓝色光吸收率) * (光照强度 / 16)" translatedTo
                "Current light absorption = (r/255*redRatio + g/255*greenRatio + b/255*blueRatio) * (lightIntensity / 16)",
        )
        info(
            "该吸收值用于决定藻类的增长因子f" translatedTo
                "This absorption value is used to determine the growth factor f of algae",
        )

        section("藻类卤素灯光波段吸收数据" translatedTo "Algae Halogen Lamp Light Wavelength Absorption Data")
        info("可养殖的藻类为：红藻、褐藻、金藻、绿藻、蓝藻" translatedTo "The cultivable algae are: red algae, brown algae, golden algae, green algae, blue algae")
        info("使用专用的藻类访问仓来收集或投放藻类" translatedTo "Use a dedicated algae access hatch to collect or release algae")
        Algae.entries.forEach { algae ->
            val colorName = when (algae) {
                Algae.RedAlge -> "红藻" translatedTo "Red"
                Algae.BrownAlge -> "褐藻" translatedTo "Brown"
                Algae.GoldAlge -> "金藻" translatedTo "Golden"
                Algae.GreenAlge -> "绿藻" translatedTo "Green"
                Algae.BlueAlge -> "蓝藻" translatedTo "Blue"
            }.color(algae.color)
            info(
                colorName +
                    ("r:" + algae.redAbsorption + " g:" + algae.greenAbsorption + " b:" + algae.blueAbsorption).toLiteralSupplier(),
            )
        }
    }

    val LargeSteamCrackerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_cracker")
        info("原料效率仅正常裂化机的80%" translatedTo "The raw material efficiency is only 80% of that of a normal cracker")
        increase("每使用高一等级的蒸汽输入仓，原料效率提升20%" translatedTo "For each higher level of steam input hatch used, the raw material efficiency increases by 20%")
    }
}
