package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.toComponentSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.common.data.translation.ComponentSlang.Tab
import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.multiblock.storage.MEStorageMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine

import net.minecraft.network.chat.Component

import com.gregtechceu.gtceu.api.GTValues
import com.gtolib.utils.NumberUtils
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper

fun ComponentListSupplier.section(title: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { gold() }, prefix: ComponentSupplier = ComponentSlang.Bar(1)) {
    add(prefix + title, style)
}

fun ComponentListSupplier.content(content: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { white() }, prefix: ComponentSupplier = ComponentSlang.Circle(2)) {
    add(prefix + content, style)
}

// default stylesheets
fun ComponentListSupplier.main(info: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { lightPurple() }) {
    section(info, style, ComponentSlang.Tab(0))
}

fun ComponentListSupplier.info(info: ComponentSupplier) {
    content(info, { gray() }, ComponentSlang.OutTopic(2))
}

fun ComponentListSupplier.ok(info: ComponentSupplier) {
    content(info, { green() }, ComponentSlang.Right(2))
}

fun ComponentListSupplier.error(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Wrong(2))
}

fun ComponentListSupplier.command(info: ComponentSupplier) {
    content(info, { yellow() })
}

fun ComponentListSupplier.important(info: ComponentSupplier) {
    content(info, { red() })
}

fun ComponentListSupplier.function(info: ComponentSupplier) {
    content(info, { aqua() })
}

fun ComponentListSupplier.increase(info: ComponentSupplier) {
    content(info, { green() }, ComponentSlang.Plus(2))
}

fun ComponentListSupplier.decrease(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Bar(2))
}

fun ComponentListSupplier.danger(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Warning(1))
}

/**
 * 用于收纳机器相关用法
 *
 * 小作文请参看 [GTOMachineStories]
 */
object GTOMachineTooltips {

    // 外置热源锅炉
    val BoilWaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("boil_water_machine")
        section("需要外部热源工作" translatedTo "Requires external heat source to operate")
        error(("当蒸汽溢出后继续工作会" translatedTo "When steam overflows, continuing to work will ") + ComponentSlang.Explosion)
        content(
            ("可能发生爆炸的临界温度为" translatedTo "The critical temperature for explosion is ") +
                BoilWaterMachine.DrawWaterExplosionLine.toLiteralSupplier().red(),
        )
    }

    // 加热器
    val HeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("heater_machine")
        section("通过燃烧对四周机器进行加热" translatedTo "Burning to heat up around machines")
        content("前方被阻挡后停止加热" translatedTo "Stop heating after front side is blocked.")
        content("根据温度发出红石信号" translatedTo "Emits redstone signal according to the temperature.")
        content(ComponentSlang.TemperatureMax(HeaterMachine.MaxTemperature))
        error(("机器过热会" translatedTo "When machine is too hot, it will ") + ComponentSlang.Explosion)
        danger(ComponentSlang.BeAwareOfBurn)
    }

    // 电力加热器
    val ElectricHeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("electric_heater_machine")
        section("使用电力对四周机器进行加热" translatedTo "Use electricity to heat up around machines")
        content(ComponentSlang.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
        ok("此机器不会爆炸" translatedTo "This machine will not explode")
        danger(ComponentSlang.BeAwareOfBurn)
    }

    // 裂变反应堆
    val FissionReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("fission_reactor")

        section("反应堆结构组成" translatedTo "Reactor structure components")
        function("通过燃料组件和冷却组件协同工作产生能量" translatedTo "Generates energy through fuel and cooling components working together")
        command("燃料组件: 提供最大并行数量" translatedTo "Fuel component: Provides maximum parallel number")
        content("升温系数 = 燃料组件相邻数 + 1" translatedTo "Heating coefficient = adjacent fuel components + 1")
        command("冷却组件: 提供最大冷却能力" translatedTo "Cooling component: Provides maximum cooling capability")
        content("冷却组件必须与燃料组件相邻才有效" translatedTo "Cooling components must be adjacent to fuel components to be effective")

        section("温度管理系统" translatedTo "Temperature management system")
        command("初始温度: 298K" translatedTo "Initial temperature: 298K")
        command("温度上限: 1500K" translatedTo "Temperature limit: 1500K")
        info("升温速率: 配方产热 × 升温系数/秒" translatedTo "Heating rate: recipe heat × heating coefficient/sec")
        info("自然降温: 停止工作时1K/秒" translatedTo "Natural cooling: 1K/sec when stopped")
        error(("超过温度上限机器开始损坏，完全损坏时" translatedTo "Exceeding temperature limit damages machine, when fully damaged ") + ComponentSlang.Explosion)

        section("冷却系统" translatedTo "Cooling system")
        content(
            "冷却液类型: 蒸馏水或钠钾合金" translatedTo "Cooling liquid types: Distilled water or sodium-potassium alloy",
            { green() },
        )
        info("冷却条件: 供给量 ≥ 需求量" translatedTo "Cooling condition: Supply ≥ demand")
        info("最低需求量 = 配方产热 × 冷却参数 × 实际并行 × 当前温度 / 1500" translatedTo "Min demand = recipe heat × cooling param × actual parallel × current temp / 1500")
        info("最高供给量 = (冷却组件 - 相邻数/3) × 8" translatedTo "Max supply = (cooling components - adjacent/3) × 8")
        info("消耗量 = 需求量 × 冷却液系数" translatedTo "Consumption = demand × cooling liquid coefficient")

        section("超频机制" translatedTo "Overclocking mechanism")
        info("触发条件: 供给量 ≥ n × 需求量 (n>1)" translatedTo "Trigger condition: Supply ≥ n × demand (n>1")
        info("超频效果: 减少n秒配方时间" translatedTo "Overclocking effect: Reduce n seconds recipe time")

        section("冷却液产出" translatedTo "Cooling liquid output")
        content("蒸馏水冷却: " translatedTo "Distilled water cooling: ", { green() })
        info("产出蒸汽，产量 = 消耗量 × min(160, 160/(1.4^(373-温度)))" translatedTo "Produces steam, Output = consumption × min(160, 160/(1.4^(373-temperature)))")
        content("钠钾合金冷却:" translatedTo "Sodium-potassium alloy cooling:", { green() })
        info("≤825K: 热钠钾合金；>825K: 超临界钠钾合金" translatedTo "≤825K: Hot sodium-potassium alloy; >825K: Supercritical sodium-potassium alloy")
    }

    // 计算中心
    val SupercomputingTooltips = ComponentListSupplier {
        setTranslationPrefix("supercomputing")

        main("计算机超级计算中心" translatedTo "Computer Supercomputing Center") { rainbow() }
        content(
            "将多台计算机集成在一起，提供大规模并行计算能力" translatedTo "Integrates multiple computers together to provide massive parallel computing power",
            { lightPurple() },
            ComponentSlang.Bar(1),
        )

        section("等级系统" translatedTo "Level System")
        content("通过在主机内放置特定物品切换等级" translatedTo "Switch tiers by placing specific items in the mainframe")
        command("结构方块等级必须与机器等级匹配" translatedTo "Structure block tiers must match machine tier")

        section("算力计算系统" translatedTo "Computing Power Calculation System")
        info("最大输出算力 = 计算组件算力和 × 算力修正系数" translatedTo "Max output = sum of component power × correction factor")
        content("等级2/3时修正系数会随时间衰减" translatedTo "At levels 2/3, correction factor decays over time")
        info("衰减公式: ((系数-0.4)²/5000)×(0.8/log(系数+6))，最低0.8" translatedTo "Decay: ((factor-0.4)²/5000)×(0.8/log(factor+6)), at least 0.8")

        section("导热剂冷却增效" translatedTo "Thermal Conductivity Cooling Enhancement")
        content(
            "通过导热剂仓输入导热剂提升算力修正系数" translatedTo "Input thermal conductivity via hatch to boost correction factor",
            { green() },
        )
        info("提升上限: 等级2(4) / 等级3(16)" translatedTo "Enhancement limits: Tier 2(4) / Tier 3(16)")
        important("导热剂使用后会失效" translatedTo "Thermal conductivity becomes invalid after use")
        info("MFPC效率: 块(0.18) 条(0.02) 粒(0.0022)" translatedTo "MFPC efficiency: Block(0.18) Ingot(0.02) Nugget(0.0022)")
        info("Cascade-MFPC效率: 块(0.54) 条(0.06) 粒(0.0066)" translatedTo "Cascade-MFPC efficiency: Block(0.54) Ingot(0.06) Nugget(0.0066)")
        info("寒冰碎片: 0.0001 (极低效率)" translatedTo "Ice Shards: 0.0001 (extremely low efficiency)")

        // Tier 1 组件支持
        section("Tier 1 : 支持HPCA系列组件" translatedTo "Tier 1 : Supports HPCA Series Components", { blue() })
        content("槽位需求: 无" translatedTo "Slot requirement: None")
        content("结构材料需求: 钨强化硼玻璃 + 计算机外壳 + 计算机散热口" translatedTo "Structure material requirements: Tungsten Borosilicate Glass + Computer Casing + Computer Heat Vent")

        // Tier 2 组件支持
        section("Tier 2 : 支持NICH系列组件" translatedTo "Tier 2 : Supports NICH Series Components", { blue() })
        content("槽位需求: 放入§a生物主机" translatedTo "Slot requirement: Place §abiological host")
        content("结构材料需求: 安普洛强化硼玻璃 + 生物计算机外壳 + 相变计算机散热口" translatedTo "Structure material requirements: Neutronium Borosilicate Glass + Biocomputer Casing + Phase Change Biocomputer Cooling Vents")

        // Tier 3 组件支持
        section("Tier 3 : 支持GWCA系列组件" translatedTo "Tier 3 : Supports GWCA Series Components", { blue() })
        content("槽位需求: 放入§5超因果主机" translatedTo "Slot requirement: Place §5Hyper-Causal Host")
        content("结构材料需求: 塔兰强化硼玻璃 + 引力子计算机外壳 + 逆熵计算机冷凝矩阵" translatedTo "Structure material requirements: Taranium Borosilicate Glass + Graviton Computer Casing + Anti Entropy Computer Condensation Matrix")
        ok("自带跨维度桥接功能" translatedTo "Built-in cross-dimensional bridging capability")
    }

    // 数字型采矿机
    val DigitalMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("digital_miner")

        section("让机器替代你挖矿" translatedTo "Mine for You")
        function("固定每两秒采掘一次" translatedTo "Mines once every two seconds")
        content("可通过GUI设置采掘范围和目标方块" translatedTo "Mining range and target blocks can be set via GUI")

        section("机器电压等级每高出一级：" translatedTo "For each increase in machine voltage level:", { aqua() })
        increase("可采掘最大范围翻倍（最高256）" translatedTo "Maximum mining range is doubled (up to 256)")
        increase("每次采掘的方块数量翻倍（最高4096）" translatedTo "The number of blocks mined each time is doubled (up to 4096)")
        decrease("耗电量翻4倍" translatedTo "Power consumption is quadrupled")
        function("通入红石信号以重新计算采掘区域并执行" translatedTo "Input a redstone signal to recalculate the mining area and execute mining")
    }

    // 超级分子装配室
    val SuperMolecularAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("super_molecular_assembler")

        main("分子装配室 Pro Max 版！" translatedTo "Molecular assembler Pro Max Edition!") { rainbow() }
    }

    // ME 超算核心
    val MECPUTooltips = ComponentListSupplier {
        main("ME 网络超级 CPU" translatedTo "Super CPU in ME Network") { rainbow() }

        section("CPU 性能" translatedTo "CPU Performance")
        function("容量：决定CPU可以处理的总任务大小" translatedTo "Capacity: Determines the total task size the CPU can handle")
        content("填满481个T5元件解锁无限存储" translatedTo "Fill 481 T5 storage units to unlock infinity storage", { rainbowSlow() })
        info("公式：Σ合成单元存储量" translatedTo "Formula: ΣCrafting unit storage")
        function("并行数：决定CPU一次可发配的样板数量" translatedTo "Parallel: Determines the number of patterns the CPU can dispatch at once")
        info("公式：合成单元个数 x 并行仓并行数" translatedTo "Formula: Number of crafting units x parallel number of parallel hatch")
        function("线程数：决定CPU能同时进行的任务数量" translatedTo "Threads: Determines the number of tasks the CPU can perform simultaneously")
        info("公式：2^玻璃等级" translatedTo "Formula: 2^Glass Tier")
    }

    // 大型内燃机
    val LargeCombustionTooltipsProvider: (Long, Long, Boolean, Long) -> ComponentListSupplier =
        { baseEUt, oxygenBoost, canExtremeBoost, liquidOxygenBoost ->
            ComponentListSupplier {
                setTranslationPrefix("large_combustion")

                section(ComponentSlang.PowerGenerationEfficiency)
                function(ComponentSlang.BaseProductionEut(baseEUt))
                command(ComponentSlang.UsePerHourLubricant(FluidHelper.getBucket()))
                increase("提供20mB/s的§a氧气§r，并消耗§4双倍§r燃料以产生§e$oxygenBoost EU/t§r的功率" translatedTo "Provide 20mB/s of §eOxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t")

                if (canExtremeBoost) {
                    increase("提供80mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生§e$liquidOxygenBoost EU/t§r的功率" translatedTo "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t")
                }

                section(ComponentSlang.AfterModuleInstallation)
                increase("空气进气速度加倍" translatedTo "Air intake speed is doubled")
                increase("获得2倍速度" translatedTo "Gains 2x speed")
                decrease("燃料消耗速度变为2倍" translatedTo "Fuel consumption rate becomes 2x")
            }
        }

    // 高速模式
    val TurbineHighSpeedTooltips = ComponentListSupplier {
        setTranslationPrefix("turbine_high_speed")
        section("高速模式" translatedTo "High-Speed Mode")
        increase("高速模式可进一步提升运行速度，与模块乘算" translatedTo "High-speed mode can further increase operating speed, multiplied with modules")
    }

    // 大型涡轮
    val LargeTurbineTooltipsProvider: (Long, Int) -> ComponentListSupplier =
        { baseEUt, rotorTier ->
            ComponentListSupplier {
                setTranslationPrefix("large_turbine")

                section(ComponentSlang.PowerGenerationEfficiency)
                function(ComponentSlang.BaseProductionEut(baseEUt))
                increase(ComponentSlang.RotorEfficiency(rotorTier))

                section(ComponentSlang.AfterModuleInstallation)
                increase("获得2倍速度" translatedTo "Gains 2x speed")
                increase("获得额外120%涡轮效率" translatedTo "Gains additional 120% turbine efficiency")
                decrease("转子损耗速度变为2倍" translatedTo "Rotor wear rate becomes 2x")
            }
        }

    // 特大涡轮
    val MegaTurbineGenerateTooltipsProvider: (Long, Int) -> ComponentListSupplier = { baseEUt, rotorTier ->
        ComponentListSupplier {
            setTranslationPrefix("mega_turbine")

            section(ComponentSlang.PowerGenerationEfficiency)
            function(ComponentSlang.BaseProductionEut(baseEUt))
            increase(ComponentSlang.RotorEfficiency(rotorTier))
            function("运行效率相当于16台同类大型涡轮" translatedTo "Operating efficiency is equivalent to 16 large turbines of the same type")
            increase("可使用更多动力仓" translatedTo "Can use more power hatch")
            increase("可安装转子仓，从中自动取出转子安装到空转子支架" translatedTo "Rotors can be installed in the rotor chamber, automatically extracting rotor for installation onto empty rotor brackets")

            section(ComponentSlang.AfterModuleInstallation)
            increase("获得3倍速度" translatedTo "Gains 3x speed")
            increase("获得额外130%涡轮效率" translatedTo "Gains additional 130% turbine efficiency")
            decrease("转子损耗速度变为3倍" translatedTo "Rotor wear rate becomes 3x")
        }
    }

    // 化学能吞噬者
    var ChemicalEnergyDevourerGenerateTooltips =
        ComponentListSupplier {
            setTranslationPrefix("chemical_energy_devourer")

            section(ComponentSlang.PowerGenerationEfficiency)
            function(ComponentSlang.BaseProductionEut(GTValues.V[GTValues.ZPM]))
            command(ComponentSlang.UsePerHourLubricant(10 * FluidHelper.getBucket()))
            increase(
                ("提供320mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生" translatedTo "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to ") +
                    (GTValues.V[GTValues.UV]).toLiteralSupplier().yellow() +
                    (" EU/t的功率" translatedTo " EU/t"),
            )
            increase(
                ("再提供480mB/s的§a四氧化二氮§r，并消耗§4四倍§r燃料以产生" translatedTo "Provide extra 480mB/s of §eNitrous Oxide§r, consuming §afour times§r fuel to produce up to ") +
                    (GTValues.V[GTValues.UHV]).toLiteralSupplier().yellow() +
                    (" EU/t的功率" translatedTo " EU/t"),
            )
        }

    // 化工厂
    val ChemicalFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_factory")

        section(ComponentSlang.CoilEfficiencyBonus)
        increase("线圈等级每高出白铜一级能耗与时间减少5%" translatedTo "Each coil tier above Cupronickel, Reduces energy consumption and duration by 5%")
    }

    // 大型虚空采矿厂
    val LargeVoidMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_void_miner")

        section("精准模式" translatedTo "Precision Mode")
        function("消耗精华采集指定矿脉" translatedTo "Consumes resources to collect specified veins")

        section("随机模式" translatedTo "Random Mode")
        command("消耗10KB的钻井液" translatedTo "Consumes 10KB of Drilling Fluid")
        function("耗时更长随机采集所有矿石" translatedTo "Longer duration to randomly collect all ores")
        important("注意: 确保输出空间足够" translatedTo "Note: Ensure enough output space")
    }

    // 通用工厂
    val ProcessingPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("processing_plant")

        section(ComponentSlang.RunningRequirements)
        command("需要放入对应配方等级的小机器" translatedTo "Requires corresponding tier small machine")
        error("无法通过超净维护仓获得洁净环境" translatedTo "Cannot obtain clean environment through clean maintenance")

        section(ComponentSlang.EfficiencyBonus)
        content("配方等级每高出ULV一级，并行数+2，安装附属模块后+4" translatedTo "For each tier above ULV, parallelism +2, After installing the auxiliary module +4")
        command("最终配方等级受限于整体框架等级" translatedTo "Final recipe tier is constrained by framework tier")
    }

    // 纳米锻炉
    val NanoForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("nano_forge")

        section(ComponentSlang.RunningRequirements)
        command("需要放入对应的纳米蜂群" translatedTo "Requires corresponding nano swarm")
        info("三种等级: 碳, 安普洛, 龙" translatedTo "Three tiers: Carbon, Amprosium, Draconium")
    }

    // 中子活化器
    var NeutronActivatorTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_activator")

        section("超光速运动！" translatedTo "Superluminal Movement!")
        increase("额外高速管道方块提供时间减免" translatedTo "Additional high-speed pipeline blocks provide time reduction")
        decrease("同时降低中子加速器效率" translatedTo "While lowering neutron accelerator efficiency")
        info("效率公式: 0.95^额外方块数量" translatedTo "Efficiency formula: 0.95^Number of Additional Blocks")

        section("中子动能系统" translatedTo "Neutron Kinetic Energy System")
        content("无中子加速器运行时每秒降低§e72KeV§r" translatedTo "When no neutron accelerator is running, decreases §e72KeV§r per second")
        content("输入石墨/铍粉可立即吸收§e10MeV§r" translatedTo "Input graphite/beryllium powder can immediately absorb §e10MeV§r")
    }

    // 热交换机
    val HeatExchangerTooltips = ComponentListSupplier {
        setTranslationPrefix("heat_exchanger")

        section("加热蒸汽" translatedTo "Heats Steam")
        content("每次处理全部输入的热流体" translatedTo "Processes all input hot fluids every time")
        content("需要保证输入的冷却液能将流体全部冷却" translatedTo "Must ensure the cooling liquid input can cool all fluids")
        increase("连续运行4次后将输出高级蒸汽" translatedTo "Outputs high-level steam after running continuously 4 times")
    }

    // 太空电梯
    val SpaceElevatorTooltips = ComponentListSupplier {
        setTranslationPrefix("space_elevator")

        section("模块运行优化系统" translatedTo "Module Operation Optimization System")
        function("可安装最多12个拓展模块" translatedTo "Can install up to 12 expansion modules")
        increase("提升电压等级可为模块提供耗时减免" translatedTo "Increasing voltage tier can provide Duration reductions for modules")
        command("运行前需提供128*(机器等级-7)的算力" translatedTo "Before starting, it is necessary to provide 128 * (tier - 7) computation power")
    }

    // 工业屠宰场
    val SlaughterhouseTooltips = ComponentListSupplier {
        setTranslationPrefix("slaughterhouse")

        section("电动刷怪塔，自动杀怪" translatedTo "Electric Spawner, automatically kills mobs")
        increase("电压等级每高出MV1级，每次处理次数+8" translatedTo "Voltage tier above MV1 increases the number of processes by 8 each time")
        command("运行前需设置电路，1号电路为非敌对生物，2号为敌对生物" translatedTo "Circuit must be set up before running; Circuit 1 is for non-hostile mobs, 2 is for hostile mobs")
        content("如果在机器GUI内放置了电动刷怪笼则只会刷出刷怪笼里的内容" translatedTo "If an electric spawner is placed in the machine GUI, only the contents of the spawner will spawn")
        info("实体生成模式为玩家击杀的实际掉落，性能较差，可获取经验" translatedTo "Entity generation mode is based on actual drops from player kills, performance is lower, can gain experience")
        info("非实体生成模式为虚拟掉落，如果存在刷怪笼则使用并行处理" translatedTo "Non-entity generation mode is virtual drops; if a spawner exists, it uses parallel processing")
    }

    // 溶解罐
    val DissolvingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolving_tank")

        section(ComponentSlang.RunningRequirements)
        command("必须保证输入的流体与配方流体比例相同，否则无产物输出" translatedTo "Must ensure the ratio of input fluid to recipe fluid is the same, otherwise no product output")
    }

    // 基岩钻机
    val BedrockDrillingRigTooltips = ComponentListSupplier {
        setTranslationPrefix("bedrock_drilling_rig")

        section(ComponentSlang.RunningRequirements)
        command("需要基岩在钻头下方" translatedTo "Requires bedrock below the drill head")
        decrease("每次运行有10%概率破坏基岩" translatedTo "Each run has 10% chance to destroy bedrock")
    }

    // 创造之门
    val DoorOfCreateTooltips = ComponentListSupplier {
        setTranslationPrefix("door_of_create")

        section(ComponentSlang.RunningRequirements)
        command("在主世界提供MAX级电压" translatedTo "Provides MAX tier voltage in the main world")
        command("设置电路为1开始运行" translatedTo "Set circuit to 1 to start running")
    }

    // 寒冰冷冻机
    val ColdIceFreezerTooltips = ComponentListSupplier {
        setTranslationPrefix("cold_ice_freezer")

        section(ComponentSlang.RunningRequirements)
        command("需每秒提供10x配方等级^2的§b液态冰§r" translatedTo "Requires to provide 10x(Recipe tier)² of §bLiquid Ice§r per second")
    }

    // 烈焰高炉
    val BlazeBlastFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("blaze_blast_furnace")

        section(ComponentSlang.RunningRequirements)
        command("需每秒提供10x配方等级^2的§6液态烈焰§r" translatedTo "Requires to provide §b10x(Recipe tier)²§r of §6Liquid Blaze§r per second")
    }

    // PCB工厂
    val PCBFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("pcb_factory")

        section(ComponentSlang.RunningRequirements)
        command("使用纳米蜂群引导结构等级" translatedTo "Use nanites to guide structure level")
        info("金：1，山铜：2，末影素：3" translatedTo "Gold: 1, Orichalcum: 2, Enderium: 3")
    }

    // 进阶装配线
    val AdvancedAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_assembly_line")

        ok("可以使用更大的输入总线" translatedTo "Can use larger input buses")
        command("需要保证每片的物品与配方对应" translatedTo "Must ensure each item corresponds to the recipe")
        command("只能使用数据靶仓" translatedTo "Only data target chambers can be used")
    }

    // 方块转换室
    val BlockConversionRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("block_conversion_room")

        main("每秒随机转化机器内部方块" translatedTo "Randomly converts blocks inside the machine every second")

        section("电压等级加成" translatedTo "Voltage Tier Bonus")
        increase("每高出MV1级，转换方块数量+4" translatedTo "For each tier above MV1, block conversion +4")
        important("不会重复转换同一方块" translatedTo "Will not repeatedly convert the same block")
    }

    // 大型方块转换室
    val LargeBlockConversionRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("large_block_conversion_room")

        main("每秒随机转化机器内部一个方块" translatedTo "Randomly converts one block inside the machine every second")

        section("电压等级加成" translatedTo "Voltage Tier Bonus")
        increase("每高出MV1级，转换方块数量+64" translatedTo "For each tier above MV1, block conversion +64")
        important("不会重复转换同一方块" translatedTo "Will not repeatedly convert the same block")
    }

    // 宇宙探测器地面接收单元
    val SpaceProbeSurfaceReceptionTooltips = ComponentListSupplier {
        setTranslationPrefix("space_probe_surface_reception")

        section(ComponentSlang.RunningRequirements)
        important("只能运行在空间站" translatedTo "Can only operate on space station")

        section("戴森球连接" translatedTo "Dyson Sphere Connection")
        content("自动连接星系内未使用的戴森球" translatedTo "Automatically connects to unused Dyson spheres in the galaxy")
        increase("根据戴森球模块数量提升产出" translatedTo "Increases production based on Dyson sphere module count")
        ok("该操作不会损坏戴森球" translatedTo "This operation will not damage the Dyson sphere")
    }

    // 鸿蒙之眼
    val EyeOfHarmonyTooltips = ComponentListSupplier {
        setTranslationPrefix("eye_of_harmony")

        main("创造微缩宇宙并获取资源" translatedTo "Creates a mini-universe and gathers resources inside") { rainbow() }

        section("供电系统" translatedTo "Power System")
        important("需要太多EU，无法用常规手段供能" translatedTo "Requires too much EU — cannot be powered by conventional means")
        important("由无线EU网络直接供给" translatedTo "Directly supplied by wireless EU network")
        info("具体数值可在GUI内查看" translatedTo "Specific values can be viewed in the GUI")

        section("特殊超频" translatedTo "Special Overclocking")
        increase("每提升16倍功率提升2倍速度" translatedTo "Speed increases 2x for every 16x power increase")
        command("超频由编程电路调节" translatedTo "Overclocking must be adjusted via programmed circuits")
        info("电路1: 不执行超频" translatedTo "Circuit 1: No overclocking")
        info("电路2-4: 分别执行1-3次超频" translatedTo "Circuits 2-4: Execute 1-3 stages of overclocking")

        section("启动需求" translatedTo "Startup Requirements")
        command("1024B宇宙素" translatedTo "1024B Cosmic Element")
        command("1024KB氢" translatedTo "1024KB Hydrogen")
        command("1024KB氦" translatedTo "1024KB Helium")
        command("氢氦存储在机器内部并持续消耗" translatedTo "Hydrogen & Helium stored internally and continuously consumed")
    }

    // 温室
    val GreenhouseTooltips = ComponentListSupplier {
        setTranslationPrefix("greenhouse")

        section(ComponentSlang.RunningRequirements)
        command("需要阳光才能运行" translatedTo "Requires sunlight to operate")
        decrease("太阳光照不足时速度减缓" translatedTo "Speed slows down when sunlight is insufficient")
    }

    // 蜂群之心
    val SwarmCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("swarm_core")

        section("蜂群之主" translatedTo "Lord of the Swarm")
        important("能够运行任意等级的纳米锻炉配方" translatedTo "Can run nano forge recipes of any tier")
        increase("处理速度固定为20倍" translatedTo "Processing speed fixed at 20x")
    }

    // 藻类农场
    val AlgaeFarmTooltips = ComponentListSupplier {
        setTranslationPrefix("algae_farm")

        section("基础产出" translatedTo "Base Production")
        function("每10秒随机消耗5-10B水，随机输出1-10个藻类" translatedTo "Every 10 seconds, randomly consumes 5-10B water and outputs 1-10 algae")

        section("增产机制" translatedTo "Yield Boost Mechanisms")
        increase("输入10B发酵生物质 → 产量×10" translatedTo "Input 10B Fermentation Biomass → 10x output")
        increase("输入n个指定藻类 → 锁定产物 & 产量×(n/4)" translatedTo "Input n specific algae → Lock output & ×(n/4) yield")
    }

    // 聚合反应器
    val PolymerizationReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("polymerization_reactor")

        section(ComponentSlang.CoilEfficiencyBonus)
        increase("线圈等级每高出白铜一级能耗与时间减少5%" translatedTo "Each coil tier above Cupronickel, reduces energy consumption and duration by 5%")
    }

    // 卫星控制中心
    val SatelliteControlCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("satellite_control_center")

        main("发射卫星，带回星球数据" translatedTo "Launch a satellite and bring back planet data")
    }

    // 原木拟生场
    val TreeGrowthSimulatorTooltips = ComponentListSupplier {
        setTranslationPrefix("tree_growth_simulator")

        section(ComponentSlang.RunningRequirements)
        command("需要安装伐木工具，仅支持GT工具" translatedTo "Requires GT-compatible tree cutting tool")
        content("根据工具类型和品质决定产出和效率" translatedTo "Output and efficiency determined by tool type and quality")
    }

    // 大型温室
    val LargeGreenhouseTooltips = ComponentListSupplier {
        setTranslationPrefix("large_greenhouse")

        section(ComponentSlang.RunningRequirements)
        ok("可以培育树木和一般作物" translatedTo "Can cultivate trees and general crops")
        ok("无需阳光就能运行" translatedTo "Can operate without sunlight")
    }

    // 雕刻中心
    val CarvingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("carving_center")

        section(ComponentSlang.RunningRequirements)
        command("根据全部电路之和决定输出" translatedTo "Output determined by the sum of all circuits")
        increase("电压等级每高出LV 1级，最大并行数×4" translatedTo "Each tier above LV multiplies max parallel by 4")
    }

    // BOSS召唤器
    val BossSummonerTooltips = ComponentListSupplier {
        setTranslationPrefix("boss_summoner")

        main("电力与反应核的作用" translatedTo "Electricity and Reactor Core Function")
    }

    // 钻井控制中枢
    val DrillingControlCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("drilling_control_center")

        section("范围增产" translatedTo "Area Yield Boost")
        increase("电压等级每高出IV一级，16m内钻机产量×1.5" translatedTo "Each tier above IV → ×1.5 output for fluid drills within 16M")
    }

    // 无线能源塔
    val WirelessEnergySubstationTooltips = ComponentListSupplier {
        setTranslationPrefix("wireless_energy_substation")

        main("为无线电网提供容量支持" translatedTo "Provides capacity support to the wireless grid")

        section("电网容量" translatedTo "Electricity Capacity")
        content("可在内部安装任意无线能量单元来提高容量上限" translatedTo "Install wireless energy units inside to increase capacity limit")
        command("实际起作用的单元受玻璃等级限制" translatedTo "Effective units are limited by glass tier")
        info("总容量 = Σ(单元容量) × 单元数 ÷ 2" translatedTo "Total Capacity = Σ(Unit Capacities) × Unit Count ÷ 2")
        info("总损耗 = 单元损耗平均值" translatedTo "Total Loss = Average of Unit Losses")
    }

    // 无线电网维度中继器
    val WirelessDimensionRepeaterTooltips = ComponentListSupplier {
        setTranslationPrefix("wireless_dimension_repeater")

        section("中继无线能源网络能量" translatedTo "Repeats the wireless energy network energy")
        function("在不同维度间中继能量" translatedTo "Energy is repeated between different dimensions")
        command("能量最大电压取决于使用的外壳等级" translatedTo "Maximum voltage depends on shell tier")
        ok("与电流大小无关" translatedTo "Not related to current size")
        content(
            "没有电流上限简直是原始人的超级科技" translatedTo "No current limit is a super technology of the primitive",
            { rainbowSlow().italic() },
        )
    }

    // 拉丝塔
    val DrawingTowerTooltips = ComponentListSupplier {
        setTranslationPrefix("drawing_tower")

        section(ComponentSlang.EfficiencyBonus)
        info("时间倍率 = 2 / 1.2^[(高度/8)×(温度-5000)/900] ≥ 0.00001" translatedTo "Time Multiplier = 2 / 1.2^[(height/8)×(temp−5000)/900] ≥ 0.00001")
        info("并行数 = log₁.₀₈(温度−9600) − 84 ≥ 1" translatedTo "Parallel = log₁.₀₈(temp−9600) − 84 ≥ 1")
    }

    // ME存储器
    val MEStorageTooltips = ComponentListSupplier {
        setTranslationPrefix("me_storage")

        section("ME存储" translatedTo "ME Storage")
        function("不受存储类型限制" translatedTo "Without storage type restrictions")
        function("你需要在结构中安装存储核心来提升容量" translatedTo "Install Storage Cores in structure to increase capacity.")
        content("使用§eME数据访问仓§r连接ME线缆来访问储存器。" translatedTo "Connect via §eME Data Access Hatch§r to access storage.")
        info("结构可以延长，在EMI看看能有多长吧！" translatedTo "Structure is extendable — check max length in EMI.")

        val bytes = NumberUtils.formatLongToKorM(MEStorageMachine.infinite)

        section(
            "无限容量模式：满足下列条件时自动启用" translatedTo "Infinite Capacity Mode: Automatically enable when the following conditions are met",
            { rainbowSlow() },
        )
        content("至少安装有 $bytes Bytes 容量" translatedTo "At least $bytes Bytes capacity installed")
        content("至少安装有 64 个无限存储组件" translatedTo "At least 64 Infinite Storage Components installed")
    }

    // 原始蒸馏塔
    val PrimitiveDistillationTowerTooltips = ComponentListSupplier {
        setTranslationPrefix("primitive_distillation_tower")

        section("热管理机制" translatedTo "Heat Management")
        info("每20单位时间，若热量>373，消耗最多9000水调节热量" translatedTo "Every 20 time units, if heat > 373, consumes up to 9000 water to regulate — more water cools faster")
        info("机器在温度400以上工作，工作时热量轻微降低" translatedTo "Operates above 400°C; heat slightly decreases during operation")
        info("每20tick消耗一次水：>100降温，≤100升温并加速" translatedTo "Every 20 ticks: water >100 cools, ≤100 heats & speeds up")
        command("添加煤块 +21600时间 | 煤 +1200 | 煤粉 +500（同时升温）" translatedTo "Add Coal Block +21600 | Coal +1200 | Coal Dust +500 (also raises heat)")
        error(("热量超过850会" translatedTo "if heat exceeds 850 it will") + ComponentSlang.Explosion)
        function("传感器定期更新热量状态" translatedTo "Sensors periodically update heat status")

        section(ComponentSlang.RunningRequirements)
        command("配方中每种产物都需要一层蒸馏塔节" translatedTo "Each recipe product requires one distillation tower layer")
        important(ComponentSlang.RecipeLevelBelow(GTValues.MV))
    }

    // 化学气相沉积系统
    val ChemicalVaporDepositionTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_vapor_deposition")

        section(ComponentSlang.EfficiencyBonus)
        content("线圈温度越高，运行速度越快" translatedTo "Higher coil temperature → faster operation")
        info("速度倍率: log(900) / log(温度)" translatedTo "Speed Multiplier: log(900) / log(Temperature)")
        section(ComponentSlang.ParallelBonus)
        content("由电压等级决定" translatedTo "Determined by Voltage Tier")
        info("公式 : 4^(电压等级 - 1)" translatedTo "Formula: 4^(Voltage Tier - 1)")
    }

    // 物理气相沉积系统
    val PhysicalVaporDepositionTooltips = ComponentListSupplier {
        setTranslationPrefix("physical_vapor_deposition")

        section(ComponentSlang.EfficiencyBonus)
        content("玻璃等级越高，运行速度越快" translatedTo "Higher glass tier → faster operation")
        info("速度倍率: √(1 / 玻璃等级)" translatedTo "Speed Multiplier: √(1 / Glass Tier)")
        section(ComponentSlang.ParallelBonus)
        content("由电压等级决定" translatedTo "Determined by Voltage Tier")
        info("公式 : 4^(电压等级 - 1)" translatedTo "Formula: 4^(Voltage Tier - 1)")
    }

    // 集成气相沉积系统
    val integratedVaporDepositionSystemTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("integrated_vapor_deposition_system")
        section(ComponentSlang.ParallelBonus)
        content("由电压等级决定" translatedTo "Determined by Voltage Tier")
        info("公式 : 4^(电压等级 - 1)" translatedTo "Formula: 4^(Voltage Tier - 1)")
    }

    // 等离子冷凝器
    val plasmaCondenserTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("plasma_condenser")
        section(ComponentSlang.EfficiencyBonus)
        content("玻璃等级越高，运行速度越快" translatedTo "Higher glass tier → faster operation")
        info("耗时减免公式 : 1/1.1^玻璃等级" translatedTo "Duration reduction formula : 1/1.1^Glass Tier")
    }

    // 生物提取机
    val BiochemicalExtractionTooltips = ComponentListSupplier {
        setTranslationPrefix("biochemical_extraction")

        section("运行机制" translatedTo "Operation Mechanism")
        command("机器运行时需要输入特定流体，否则中断配方" translatedTo "The machine requires specific fluids as input during operation; otherwise, the recipe is interrupted")
        command("每秒需要输入1B营养精华一次，成功后发出一次红石信号" translatedTo "It needs to input 1B nutrient distillation once per second, and upon success, emits a redstone signal once")
        command("连续运行5秒后需要输入1B浓缩云之精华一次" translatedTo "After continuous operation for 5 seconds, it needs to input 1B cloud seed concentrated once")
        command("连续运行15秒后需要输入1B火焰水一次" translatedTo "After continuous operation for 15 seconds, it needs to input 1B fire water once")
        command("连续运行20秒后需要输入1B轻盈之气一次" translatedTo "After continuous operation for 20 seconds, it needs to input 1B vapor of levity once")
        error("如果连续运行要求输入的流体不符合要求，则中断配方" translatedTo "If the required fluids for continuous operation do not meet the requirements, the recipe is interrupted")
        content("营养精华可与其他流体同时输入" translatedTo "Nutrient distillation can be input simultaneously with other fluids")
        content("20秒后只需完成每秒的营养精华输入要求，配方开始输出" translatedTo "After 20 seconds, only the requirement for inputting nutrient distillation once per second needs to be completed, recipe output begins")
    }

    // 星核钻机
    val PlanetCoreDrillingTooltips = ComponentListSupplier {
        setTranslationPrefix("planet_core_drilling")

        main("每秒产出当前世界的全部矿石65536份" translatedTo "Produces a total of 65536 ores from the current world per second")
    }

    // 进阶无尽钻机
    val AdvancedInfiniteDrillerTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_infinite_driller")

        main("利用维度技术和坚不可摧的钻头无情的抽取星球的每一分血液" translatedTo "Using dimensional technology and indestructible drills, they relentlessly extract every drop of blood from the planet.")
        main("更加高级的无尽流体钻井" translatedTo "More advanced infinite fluid drilling")

        section("启动与温控" translatedTo "Startup & Thermal Control")
        command("需要升温启动，可通入液态烈焰" translatedTo "Requires heating to start, input Liquid Blaze to warm up")
        increase("随着温度提升，效率也会提升" translatedTo "Higher temperature will give higher efficiency")
        error("当温度超过临界值，钻头将会融毁" translatedTo "If the machine overheats, drill head will melt")
        info("产热公式: 温度 / 2000" translatedTo "Heat generation formula: Temperature / 2000")
        info("液态烈焰消耗公式: 温度^1.3" translatedTo "Liquid Blaze consumption formula: Temperature^1.3")

        section("冷却系统" translatedTo "Cooling System")
        command("冷却固定消耗: 200B/5t" translatedTo "Fixed cooling consumption: 200B/5t")
        command("蒸馏水 1K/mB" translatedTo "Distilled Water 1K/mB")
        command("液态氧 2K/mB" translatedTo "Liquid Oxygen 2K/mB")
        command("液态氦 4K/mB" translatedTo "Liquid Helium 4K/mB")
        important("仅可放入中子素钻头（更多钻头待定）" translatedTo "Only Neutron-element drill heads can be placed (more to be determined)")
    }

    // 热力泵
    val ThermalPowerPumpTooltips = ComponentListSupplier {
        setTranslationPrefix("thermal_power_pump")

        section("高效供水" translatedTo "Efficient Water Supply")
        content("输入蒸汽，产生同等数量的水" translatedTo "Input steam → outputs equal amount of water")
    }

    // 虚空流体钻机
    val VoidFluidDrillTooltips = ComponentListSupplier {
        setTranslationPrefix("void_fluid_drilling_rig")

        section(ComponentSlang.RunningRequirements)
        command("需要最低30720EU/t的功率" translatedTo "Requires minimum 30720 EU/t")
    }

    // 虚空采矿机
    val VoidMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("void_miner")

        section(ComponentSlang.RunningRequirements)
        command("需要输入1B钻井液和最低1920EU/t的功率" translatedTo "Requires 1B drilling fluid and minimum 1920 EU/t")

        section("产出机制" translatedTo "Output Mechanism")
        function("按维度随机选取4种矿石输出" translatedTo "Randomly selects 4 ore types based on dimension")
        increase("电压等级决定单次最大输出数量" translatedTo "Voltage tier determines max output quantity per cycle")
        increase("电流决定并行数" translatedTo "Amperage determines parallel count")
    }

    // 精密组装机
    val PrecisionAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembler")

        section(ComponentSlang.RunningRequirements)
        important("外壳等级决定配方等级上限" translatedTo "Machine casing tier determines the upper limit of recipe tier")
    }

    // 熔岩炉
    val LavaFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("lava_furnace")

        section("地热转化" translatedTo "Geothermal Conversion")
        function("每提供一个任意类型的圆石或石头可输出§c1B§7熔岩" translatedTo "Each cobblestone or stone provided outputs §c1B§7 lava")
    }

    // 激光蚀刻工厂
    val EngravingLaserPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("engraving_laser_plant")

        section(ComponentSlang.EfficiencyBonus)
        increase("运行激光焊接配方时速度×5" translatedTo "Running Laser Welder recipes at 5x speed")
        error("精密激光模式不支持并行" translatedTo "Precision Laser mode does not support parallel")
    }

    // 集成矿石处理厂
    val IntegratedOreProcessorTooltips = ComponentListSupplier {
        setTranslationPrefix("integrated_ore_processor")

        main("一步完成矿石处理" translatedTo "Completes ore processing in one step")

        section("电路配置" translatedTo "Circuit Configuration")
        function("1号电路: 破碎-研磨-离心" translatedTo "Circuit 1: Crusher → Macerator → Centrifuging")
        function("2号电路: 破碎-洗矿-热离-研磨" translatedTo "Circuit 2: Crusher → Ore Washer → Thermal Separation → Macerator")
        function("3号电路: 破碎-洗矿-研磨-离心" translatedTo "Circuit 3: Crusher → Ore Washer → Macerator → Centrifuging")
        function("4号电路: 破碎-洗矿-筛选-离心" translatedTo "Circuit 4: Crusher → Ore Washer → Sifter → Centrifuging")
        function("5号电路: 破碎-浸洗-热离-研磨" translatedTo "Circuit 5: Crusher → Chemical Bath → Thermal Centrifuging → Macerator")
        function("6号电路: 破碎-浸洗-研磨-离心" translatedTo "Circuit 6: Crusher → Chemical Bath → Macerator → Centrifuging")
        function("7号电路: 破碎-浸洗-筛选-离心" translatedTo "Circuit 7: Crusher → Chemical Bath → Sifter → Centrifuging")

        section(ComponentSlang.AfterModuleInstallation)
        increase("解锁8线程处理" translatedTo "Unlock 8-thread processing")
    }

    // 大型蒸汽电路组装机
    val LargeSteamCircuitAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_circuit_assembler")

        section("配方等级" translatedTo "Recipe Tier")
        important(ComponentSlang.RecipeLevelBelow(GTValues.MV))
        val name = GTValues.VNF[GTValues.HV] + "§r"
        increase("可安装大型蒸汽输入仓提升至${name}等级" translatedTo "Install Large Steam Input Hatch to upgrade to $name recipe tier")
        increase("同时解锁超频功能" translatedTo "Unlocks overclocking function")

        section("电路倍产" translatedTo "Circuit Multiplication")
        increase("允许通过铭刻电路倍增电路产物" translatedTo "Allows circuit products to be multiplied through engraved circuits")
        important("铭刻后此机器只能加工此种电路" translatedTo "After engraving, this machine can only process this type of circuit")
    }

    var LargeSteamSolarBoilerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_circuit_assembler")

        section("蒸汽产出" translatedTo "Steam Production")
        content("根据集热管数量决定蒸汽产量" translatedTo "Steam production determined by number of collector tubes")
        important("只能在太阳下工作" translatedTo "Can only operate under the sun")

        section("可用大小" translatedTo "Usable Size")
        command("最小：5x5" translatedTo "Minimum: 5x5")
        command("最大：13x13" translatedTo "Maximum: 13x13")
    }

    // 部件组装机
    val ComponentAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("component_assembler")

        section(ComponentSlang.RunningRequirements)
        important(ComponentSlang.RecipeLevelBelow(GTValues.IV))
        increase(
            ("升级结构后支持到" translatedTo "After upgrading the structure, it supports tier ") +
                GTValues.VNF[GTValues.UV].toLiteralSupplier(),
        )
    }

    // 蒸汽搅拌机
    val SteamMixerTooltips = ComponentListSupplier {
        setTranslationPrefix("steam_mixer")

        section(ComponentSlang.RunningRequirements)
        error("无法处理流体配方" translatedTo "Cannot process fluids")
    }

    // 跃进一号高炉
    val LeapForwardOneBlastFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("leap_forward_one_blast_furnace")

        main("我们走在大路上！" translatedTo "We're on the road!")

        section(ComponentSlang.EfficiencyBonus)
        content("随着配方运行，温度升高" translatedTo "As the recipe operating, the temperature increases")
        increase("下次配方获得 400 / 温度 的时间减免" translatedTo "Next recipe gains duration reduction of 400 / temperature")
        danger(ComponentSlang.BeAwareOfBurn)
    }

    // 拆解机
    val DisassemblyTooltips = ComponentListSupplier {
        setTranslationPrefix("disassembly")

        section("回收机器" translatedTo "Disassemble Machines")
        val comma = ", ".toLiteralSupplier()
        function(
            ("可拆解: " translatedTo "Can disassemble: ") +
                Component.translatable("gtceu.assembler").toComponentSupplier() +
                comma +
                Component.translatable("gtceu.precision_assembler").toComponentSupplier() +
                comma +
                Component.translatable("gtceu.assembler_module").toComponentSupplier() +
                comma +
                Component.translatable("gtceu.assembly_line").toComponentSupplier() +
                comma +
                Component.translatable("gtceu.circuit_assembly_line").toComponentSupplier() +
                comma +
                Component.translatable("gtceu.suprachronal_assembly_line").toComponentSupplier(),
        )

        command("同对应配方时间与耗能" translatedTo "Same duration and energy consumption as original recipe")
        error("可由多个配方获取的物品无法拆解" translatedTo "Items obtainable from multiple recipes cannot be disassembled")
    }

    // 工业浮选机
    val IndustrialFlotationCellTooltips = ComponentListSupplier {
        setTranslationPrefix("industrial_flotation_cell")

        main("工业级浮游选矿池" translatedTo "Industrial Flotation Mining Pool")
    }

    // 恒星炎炀锻炉
    val StellarForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("stellar_forge")

        section("连续运行加成" translatedTo "Continuous Operation Bonus")
        function("配方等级不受能源仓限制，连续运行优化" translatedTo "Recipe tier is not limited by energy hatch. Continuous Operation can Optimize")
        increase("首次运行后继续运行，后续配方时间减少50%" translatedTo "After first run, continue → 50% duration reduction on subsequent runs")
        error(("运行中供电不足会产生巨大" translatedTo "Power shortage during operation causes massive ") + ComponentSlang.Explosion)
    }

    // 通天之路
    val RoadOfHeavenTooltips = ComponentListSupplier {
        setTranslationPrefix("road_of_heaven")

        section("模块运行优化系统" translatedTo "Module Operation Optimization System")
        function("可安装最多64个拓展模块" translatedTo "Can install up to 64 expansion modules")
        increase("提升电压等级可为模块提供大幅耗时减免" translatedTo "Increasing voltage tier can provide large Duration reductions for modules")
        increase("额外提升为模块提供的并行数" translatedTo " Additional increase in the parallelism provided by the module")
        command("运行前需提供128*(机器等级-7)的算力" translatedTo "Before starting, it is necessary to provide 128 * (tier - 7) computation power")
        increase(("通天之路额外加成" translatedTo "Road of Heaven Extra Bonus").rainbow() + ("：连接的模块将获得0.707倍耗时的工作速度加成" translatedTo ": Connected modules will receive a 0.707x Duration speed bonus"))
    }

    // 净化处理厂
    val WaterPurificationPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("water_purification_plant")

        section("处理单元链接系统" translatedTo "Processing Unit Link System")
        function("可在§e32§r个方块半径内自由放置处理单元控制器" translatedTo "Processing unit controllers can be placed freely within a §e32§r block radius")
        function("为链接的处理单元控制器提供电力" translatedTo "Provide power to linked processing unit controllers")
        info("默认耗能 = 输出水量 × 2^(净化水等级 - 2)" translatedTo "Default energy = output water × 2^(purification tier - 2)")

        section("处理周期系统" translatedTo "Processing Cycle System")
        content("固定处理周期: 120 秒" translatedTo "Fixed processing cycle: 120 seconds")
        info("所有控制器同步周期，净化水输出 = 输入水量 × 0.9mB" translatedTo "All controllers sync cycle — purified output = input water × 0.9mB")
    }

    // 澄清器净化装置
    val ClarifierPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("clarifier_purification_unit")

        main(ComponentSlang.PurifyLevel(1))

        section(ComponentSlang.RunningRequirements)
        content("处理一定水量后过滤器堵塞" translatedTo "Filter blocks clog after processing certain water volume")
        command("需输入空气(1-8KB) + 水(200-300B)进行反冲洗" translatedTo "Requires air (1-8KB) + water (200-300B) for backflushing")
        content("反冲洗时输出废料" translatedTo "Outputs waste during backflushing")

        section(ComponentSlang.OutputProbability)
        info("基础概率: 70%" translatedTo "Base probability: 70%")
        increase("同等级净化水可提升15%" translatedTo "Same-tier purified water can improve 15%")
        increase("更高等级每级额外增加5%，最高4级达到100%" translatedTo "Higher levels increase by an additional 5% per level, reaching 100% at the maximum of 4 levels")
    }

    // 臭氧净化装置
    val OzonationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ozonation_purification_unit")

        main(ComponentSlang.PurifyLevel(2))

        section(ComponentSlang.RunningRequirements)
        content("臭氧消耗量=输入水量/10000mB" translatedTo "Ozone consumption = input water amount / 10000mB")
        error(("如果输入口含有超过1024B的臭氧气体，将发生" translatedTo "If the inlet contains more than 1024B of Ozone Gas, there will occur an ") + ComponentSlang.Explosion)

        section(ComponentSlang.OutputProbability)
        info("臭氧气体在0-1024B范围内的产出概率为0-80%" translatedTo "The output probability of Ozone Gas in the range of 0-1024B is 0-80%")
        increase("输入少量同等级水可提升15%" translatedTo "Inputting a small amount of the same level water can increase it by 15%")
        increase("更高一级额外提升5%" translatedTo "Higher levels increase by an additional 5%")
    }

    // 絮凝净化装置
    val FlocculationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("flocculation_purification_unit")

        main(ComponentSlang.PurifyLevel(3))

        section(ComponentSlang.RunningRequirements)
        command("提供聚合氯化铝以进行操作" translatedTo "Provide Polymeric Aluminum Chloride for operation.")
        content("输出可循环利用的絮凝废液" translatedTo "Outputs recyclable Flocculent Waste Liquid.")
        content("在操作过程中，将消耗输入仓中的所有聚合氯化铝" translatedTo "During operation, all Polymeric Aluminum Chloride in the input chamber will be consumed.")

        section(ComponentSlang.OutputProbability)
        increase("每消耗100000mB聚合氯化铝，成功率额外增加10.0%" translatedTo "For every 100000mB of Polymeric Aluminum Chloride consumed, success rate increases by 10.0%.")
        decrease("如果提供的液体总量不是100000mB的倍数，则应用成功率惩罚：" translatedTo "If total liquid provided is not a multiple of 100000mB, apply success rate penalty:")
        info("成功率 = 成功率 * 2 ^ (-10 * 溢出比率)" translatedTo "Success Rate = Success Rate * 2 ^ (-10 * Overflow Ratio)")
    }

    // pH中和净化装置
    val PHNeutralizationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ph_neutralization_purification_unit")

        main(ComponentSlang.PurifyLevel(4))

        section(ComponentSlang.RunningRequirements)
        content("每个周期的初始pH值在4.5至9.5之间变化" translatedTo "The initial pH value of each cycle varies between 4.5 to 9.5.")
        content("机器工作时可使用pH传感器读取当前pH值并输出红石信号" translatedTo "During operation, use pH sensor to read current pH and output redstone signal.")
        content("每秒消耗所有输入的§e氢氧化钠§r和§e盐酸§r" translatedTo "Consumes all input Sodium Hydroxide and Hydrochloric Acid every second.")

        section(ComponentSlang.OutputProbability)
        ok("周期结束时pH值在7.0 ±0.05范围内，则配方必定成功" translatedTo "If pH is within 7.0 ±0.05 at cycle end, then recipe always succeeds.")
        error("否则配方必定失败" translatedTo "Otherwise recipe always fails.")

        section("PH调节" translatedTo "PH Adjustment Mechanism")
        command("每消耗1个氢氧化钠粉：pH提高0.01" translatedTo "Each Sodium Hydroxide Powder consumed: pH +0.01")
        command("每消耗10mB盐酸：pH降低0.01" translatedTo "Each 10mB Hydrochloric Acid consumed: pH -0.01")
    }

    // 极端温度波动净化装置
    val ExtremeTemperatureFluctuationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_temperature_fluctuation_purification_unit")

        main(ComponentSlang.PurifyLevel(5))

        section(ComponentSlang.RunningRequirements)
        command("完成加热周期：先加热水至10000K以上，再冷却至10K以下" translatedTo "Complete heating cycle: first heat Water above 10000K, then cool below 10K.")
        content("配方开始时初始温度重置为298K" translatedTo "Initial temperature reset to 298K at recipe start.")
        content("每秒最多消耗10mB氦等离子体和100mB液氦" translatedTo "Consumes up to 10mB Helium Plasma and 100mB Liquid Helium per second.")

        section(ComponentSlang.OutputProbability)
        increase("每完成一个加热周期，成功率增加33%" translatedTo "For each completed heating cycle, success rate increases by 33%.")

        section("温度调节" translatedTo "Temperature Regulation Mechanism")
        command("每消耗1mB氦等离子体：温度升高80-120K" translatedTo "Each 1mB Helium Plasma consumed: temperature +80-120K")
        command("每消耗1mB液氦：温度降低4-6K" translatedTo "Each 1mB Liquid Helium consumed: temperature -4-6K")
        error("温度达到12500K：配方失败并输出超临界蒸汽" translatedTo "If temperature reaches 12500K: recipe fails and outputs Supercritical Steam")
    }

    // 高能激光净化装置
    val HighEnergyLaserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("high_energy_laser_purification_unit")

        main(ComponentSlang.PurifyLevel(6))

        section(ComponentSlang.RunningRequirements)
        command("在操作过程中，需要更换透镜仓内的透镜" translatedTo "During operation, you need replace the Lens in the lens chamber.")
        content("当当前透镜需要更换时，多方块结构将通过透镜指示仓输出信号" translatedTo "When the current Lens needs to be replaced, the multi-block structure will output a signal through the Lens indicator chamber.")
        content("透镜更换请求将在6到12秒的随机间隔内出现" translatedTo "Lens replacement requests will occur at random intervals between 6 and 12 seconds.")
        content("需要在信号输出后的4秒内更换透镜" translatedTo "The lens must be replaced within 4 seconds of the signal output.")
        content("透镜顺序依次为红色、橙色、棕色、黄色、绿色、青色、蓝色、紫色、品红色、粉色" translatedTo "The lens order is Red, Orange, Brown, Yellow, Green, Cyan, Blue, Purple, Magenta, Pink.")
        content("透镜需求可在GUI内查看" translatedTo "The requirements for Lens can be viewed in the GUI.")

        section(ComponentSlang.OutputProbability)
        increase("每次成功更换后运行4秒将成功率提高10%" translatedTo "Each successful replacement followed by 4 seconds of operation increases the success rate by 10%.")
    }

    // 残余污染物脱气净化装置
    val ResidualDecontaminantDegasserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("residual_decontaminant_degasser_purification_unit")

        main(ComponentSlang.PurifyLevel(7))

        section(ComponentSlang.RunningRequirements)
        content("要成功完成配方，需要根据要求输入材料" translatedTo "To successfully complete the recipe, materials must be inputted as required.")
        content("操作开始时，脱气控制仓将输出红石信号，机器每秒将消耗全部输入的材料" translatedTo "At the operation start, the degassing control chamber will output a redstone signal, and the machine will consume all input materials every second.")
        content("红石信号与需求相对应" translatedTo "The redstone signal corresponds to the demand.")

        section("信号需求" translatedTo "Signal Requirement")
        command("1, 3, 5, 7, 9：通过惰性气体进行臭氧曝气" translatedTo "1, 3, 5, 7, 9: Ozone aeration via Inert Gases")
        info("对应 10000mB氦气 / 8000mB氖气 / 6000mB氩气 / 4000mB氪气 / 2000mB氙气" translatedTo "Which is 10000mB Helium / 8000mB Neon / 6000mB Argon / 4000mB Krypton / 2000mB Xenon")
        command("2, 4, 6, 8, 10：超导去离子" translatedTo "2, 4, 6, 8, 10: Superconductive deionization")
        info("需要输入1000mB对应IV，LuV，ZPM，UV，UHV的液态超导" translatedTo "Needs input of 1000mB of liquid superconductors corresponding to IV, LuV, ZPM, UV, UHV.")
        command("11, 13, 15：引力生成差异真空提取" translatedTo "11, 13, 15: Gravitational Differential Vacuum Extraction")
        info("需要输入2000mB液态安普洛" translatedTo "Requires input of 2000mB Liquid Amprosiums.")
        command("12, 14：塞尔多尼安沉淀过程" translatedTo "12, 14: Seldenian precipitation process")
        info("不输入任何东西" translatedTo "Do not input anything.")
        command("0：机器过载" translatedTo "0: Machine overload")
        info("在罕见情况下，机器可能会过载并且不会输出任何控制信号" translatedTo "In rare situations, the machine may overload and not output any control signals.")
        info("为防止机器损坏，输入10000mB液氦" translatedTo "To prevent machine damage, input 10000mB Liquid Helium.")
        error("输入信号未请求的任何流体将始终导致配方失败" translatedTo "Any liquid not requested by the input signal will always cause the recipe to fail.")
    }

    // 绝对重子完美净化装置
    val AbsoluteBaryonicPerfectionPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("absolute_baryonic_perfection_purification_unit")

        main(ComponentSlang.PurifyLevel(8))

        section(ComponentSlang.RunningRequirements)
        command("将§b夸克释放催化剂§r放入输入总线中运行" translatedTo "Put §bQuark Releasing Catalyst§r into the input bus to operate.")
        content("每秒消耗输入槽中的所有催化剂" translatedTo "Consumes all Catalysts in the input slot every second.")
        command("每消耗一个夸克催化剂还需额外消耗144mB§b夸克胶子等离子体§r" translatedTo "For each quark catalyst consumed, an additional 144mB of §bquark gluon plasma§r is required.")
        content("在配方结束时，所有错误插入的催化剂将返回输出槽" translatedTo "At the end of the recipe, all incorrectly inserted Catalysts will return to the output slot.")

        section(ComponentSlang.OutputProbability)
        content("每个配方循环中，不同的两种夸克释放催化剂的组合将正确识别出孤立的夸克并完成配方" translatedTo "Each recipe cycle, different combinations of two Quark Releasing Catalysts will correctly identify the Isolated Quarks and complete the recipe.")
        content("如果最近插入的两种催化剂是正确的组合，则立即输出稳定重子物质" translatedTo "If the last two inserted Catalysts are the correct combination, Stable Baryonic Matter will be output immediately.")
    }

    // 源初重构仪
    val ThePrimordialReconstructorTooltips = ComponentListSupplier {
        setTranslationPrefix("the_primordial_reconstructor")
        add("通过电路选择工作模式" translatedTo "Select the operating mode through the circuit") { gold() }
        add(Tab(1) + ("电路 1：物品解构" translatedTo "Circuit 1: Item Deconstruction")) { aqua() }
        add(Tab(2) + ("需要输入 书 铭刻之布 摘抄符石" translatedTo "Requires input Book  Affix Canvas  Sigil of Withdrawal")) { gray().italic() }
        add(Tab(1) + ("电路 2：物品 + 附魔 解构" translatedTo "Circuit 2: Item + Enchantments Deconstruction")) { aqua() }
        add(Tab(2) + ("需要输入 铭刻之布 摘抄符石" translatedTo "Requires input Affix Canvas  Sigil of Withdrawal")) { gray().italic() }
        add(Tab(1) + ("电路 3：物品 + 刻印 解构" translatedTo "Circuit 3: Item + Affixes Deconstruction")) { aqua() }
        add(Tab(2) + ("需要输入 书 摘抄符石" translatedTo "Requires input Book  Sigil of Withdrawal")) { gray().italic() }
        add(Tab(1) + ("电路 4：物品 + 附魔 + 刻印 解构" translatedTo "Circuit 4: Item + Enchantments + Affixes Deconstruction")) { aqua() }
        add(Tab(2) + ("需要输入 摘抄符石" translatedTo "Requires input Sigil of Withdrawal")) { gray().italic() }
        add(Tab(1) + ("电路 5：附魔精粹合成附魔书" translatedTo "Circuit 5: Essence synthesis Enchanted Book")) { aqua() }
        add(Tab(2) + ("需要输入一本书，消耗附魔精粹和魔力合成" translatedTo "Need to input a book, consume enchantment essence and magic synthesis")) { gray().italic() }
        add(Tab(1) + ("电路 6：附魔书合并" translatedTo "Circuit 6: Enchantment Enchanted Book Merge")) { aqua() }
        add(Tab(2) + ("消耗魔力合成，会输出额外的书" translatedTo "Consume magic power to synthesize, and output additional books")) { gray().italic() }
        add(Tab(1) + ("电路 7：刻印精粹合成铭刻之布" translatedTo "Circuit 7: Affix Enchanted Book Merge")) { aqua() }
        add(Tab(2) + ("消耗魔力合成，需要一个铭刻之布" translatedTo "Consume magic power to synthesize, and requires an Affix Canvas")) { gray().italic() }
        add(Tab(1) + ("电路 8：宝石合并" translatedTo "Circuit 8: Gem Merge")) { aqua() }
        add(Tab(2) + ("使用同级的珍宝材料和宝石粉合并宝石" translatedTo "Use the same level of rarity materials and gem dust to merge gems")) { gray().italic() }
        add(Tab(1) + ("电路 9：强行附魔" translatedTo "Circuit 9: Forced enchantment")) { aqua() }
        add(Tab(2) + ("消耗魔力强行将附魔书上的附魔添加到物品上" translatedTo "Consume magic power to forcibly adds the enchantment from the enchanted book to the item")) { gray().italic() }
        add(Tab(1) + ("电路 10：强行刻印" translatedTo "Circuit 10: Forced add affixes")) { aqua() }
        add(Tab(2) + ("消耗魔力强行将铭刻之布上的刻印添加到物品上" translatedTo "Consume magic power to forcibly adds the affixes from the affix canvas to the item")) { gray().italic() }
        add(Tab(1) + ("电路 11：强行修改物品稀有度" translatedTo "Circuit 11: Forcefully modify item rarity")) { aqua() }
        add(Tab(2) + ("消耗魔力，珍宝材料和新生符文强行改变稀有度" translatedTo "Consume magic power and enter rarity material and sigil of rebirth to forcibly change the rarity")) { gray().italic() }
        add(Tab(1) + ("电路 12：强行添加镶孔" translatedTo "Circuit 12: Forced addition of sockets")) { aqua() }
        add(Tab(2) + ("消耗魔力，镶孔符文强行添加镶孔" translatedTo "Consume magic power and enter sigil of socketing to forcibly addition of sockets")) { gray().italic() }
        add(Tab(1) + ("电路 13：强行镶嵌宝石" translatedTo "Circuit 13: Forced gem inlay")) { aqua() }
        add(Tab(2) + ("消耗魔力，强行将宝石镶嵌到物品上" translatedTo "Consume magic power to forcibly inserting gems into items")) { gray().italic() }
    }
}
