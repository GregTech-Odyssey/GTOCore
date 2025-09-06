package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine

import com.gregtechceu.gtceu.api.GTValues
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper

object GTOMachineTooltips {

    fun ComponentListSupplier.section(title: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { gold() }, prefix: ComponentSupplier = ComponentSlang.Bar(1)) {
        add(prefix + title, style)
    }

    fun ComponentListSupplier.content(content: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { white() }, prefix: ComponentSupplier = ComponentSlang.Circle(2).yellow()) {
        add(prefix + content, style)
    }

    // default stylesheets
    fun ComponentListSupplier.info(info: ComponentSupplier) {
        content(info, { gray() }, ComponentSlang.OutTopic(2))
    }

    fun ComponentListSupplier.increase(info: ComponentSupplier) {
        content(info, { green() }, ComponentSlang.Plus(2))
    }

    fun ComponentListSupplier.decrease(info: ComponentSupplier) {
        content(info, { red() }, ComponentSlang.Bar(2))
    }

    // 外置热源锅炉
    val BoilWaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("boil_water_machine")
        section("需要外部热源工作" translatedTo "Requires external heat source to operate")
        content(("当蒸汽溢出后继续工作会" translatedTo "When steam overflows, continuing to work will ") + (ComponentSlang.Explosion))
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
        content(("机器过热会" translatedTo "When machine is too hot, it will ") + ComponentSlang.Explosion)
        content(ComponentSlang.BeAwareOfBurn, { this }, ComponentSlang.Warning(1))
    }

    // 电力加热器
    val ElectricHeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("electric_heater_machine")
        section("使用电力对四周机器进行加热" translatedTo "Use electricity to heat up around machines")
        content(ComponentSlang.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
        content(("此机器不会爆炸" translatedTo "This machine will not explode").gold())
        content(ComponentSlang.BeAwareOfBurn, { this }, ComponentSlang.Warning(1))
    }

    // 裂变反应堆
    val FissionReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("fission_reactor")

        section("反应堆结构组成" translatedTo "Reactor structure components")
        content("通过燃料组件和冷却组件协同工作产生能量" translatedTo "Generates energy through fuel and cooling components working together")
        content(("燃料组件: 提供最大并行数量" translatedTo "Fuel component: Provides maximum parallel number").green())
        info("升温系数 = 燃料组件相邻数 + 1" translatedTo "Heating coefficient = adjacent fuel components + 1")
        content(("冷却组件: 提供最大冷却能力" translatedTo "Cooling component: Provides maximum cooling capability").green())
        content("冷却组件必须与燃料组件相邻才有效" translatedTo "Cooling components must be adjacent to fuel components to be effective")

        section("温度管理系统" translatedTo "Temperature management system")
        content(("初始温度: 298K" translatedTo "Initial temperature: 298K").green())
        content(("温度上限: 1500K" translatedTo "Temperature limit: 1500K").red())
        info("升温速率: 配方产热 × 升温系数/秒" translatedTo "Heating rate: recipe heat × heating coefficient/sec")
        info("自然降温: 停止工作时1K/秒" translatedTo "Natural cooling: 1K/sec when stopped")
        content(("超过温度上限机器开始损坏，完全损坏时" translatedTo "Exceeding temperature limit damages machine, when fully damaged ") + ComponentSlang.Explosion)

        section("冷却系统" translatedTo "Cooling system")
        content(("冷却液类型: 蒸馏水或钠钾合金" translatedTo "Cooling liquid types: Distilled water or sodium-potassium alloy").green())
        info("冷却条件: 供给量 ≥ 需求量" translatedTo "Cooling condition: Supply ≥ demand")
        info("最低需求量 = 配方产热 × 冷却参数 × 实际并行 × 当前温度 / 1500" translatedTo "Min demand = recipe heat × cooling param × actual parallel × current temp / 1500")
        info("最高供给量 = (冷却组件 - 相邻数/3) × 8" translatedTo "Max supply = (cooling components - adjacent/3) × 8")
        info("消耗量 = 需求量 × 冷却液系数" translatedTo "Consumption = demand × cooling liquid coefficient")

        section("超频机制" translatedTo "Overclocking mechanism")
        info("触发条件: 供给量 ≥ n × 需求量 (n>1)" translatedTo "Trigger condition: Supply ≥ n × demand (n>1")
        info("超频效果: 减少n秒配方时间" translatedTo "Overclocking effect: Reduce n seconds recipe time")

        section("冷却液产出" translatedTo "Cooling liquid output")
        content(("蒸馏水冷却: " translatedTo "Distilled water cooling: ").green())
        info("产出蒸汽，产量 = 消耗量 × min(160, 160/(1.4^(373-温度)))" translatedTo "Produces steam, Output = consumption × min(160, 160/(1.4^(373-temperature)))")
        content(("钠钾合金冷却:" translatedTo "Sodium-potassium alloy cooling:").green())
        info("≤825K: 热钠钾合金；>825K: 超临界钠钾合金" translatedTo "≤825K: Hot sodium-potassium alloy; >825K: Supercritical sodium-potassium alloy")
    }

    // 计算中心
    val SupercomputingTooltips = ComponentListSupplier {
        setTranslationPrefix("supercomputing")

        section("计算机超级计算中心" translatedTo "Computer Supercomputing Center", { rainbow() })
        content(
            "将多台计算机集成在一起，提供大规模并行计算能力" translatedTo "Integrates multiple computers together to provide massive parallel computing power",
            { lightPurple() },
            ComponentSlang.Bar(1),
        )

        section("等级系统" translatedTo "Level System")
        content("通过在主机内放置特定物品切换等级" translatedTo "Switch tiers by placing specific items in the mainframe")
        content(("结构方块等级必须与机器等级匹配" translatedTo "Structure block tiers must match machine tier").red())

        section("算力计算系统" translatedTo "Computing Power Calculation System")
        info("最大输出算力 = 计算组件算力和 × 算力修正系数" translatedTo "Max output = sum of component power × correction factor")
        content("等级2/3时修正系数会随时间衰减" translatedTo "At levels 2/3, correction factor decays over time")
        info("衰减公式: ((系数-0.4)²/5000)×(0.8/log(系数+6))，最低0.8" translatedTo "Decay: ((factor-0.4)²/5000)×(0.8/log(factor+6)), at least 0.8")

        section("导热剂冷却增效" translatedTo "Thermal Conductivity Cooling Enhancement")
        content(("通过导热剂仓输入导热剂提升算力修正系数" translatedTo "Input thermal conductivity via hatch to boost correction factor").green())
        info("提升上限: 等级2(4) / 等级3(16)" translatedTo "Enhancement limits: Tier 2(4) / Tier 3(16)")
        content(("导热剂使用后会失效" translatedTo "Thermal conductivity becomes invalid after use").red())
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
        content(("自带跨维度桥接功能" translatedTo "Built-in cross-dimensional bridging capability").gold())
    }

    // 数字矿机
    val DigitalMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("digital_miner")

        section("让机器替代你挖矿" translatedTo "Mine for You")
        content(("固定每两秒采掘一次" translatedTo "Mines once every two seconds").green())
        content("可通过GUI设置采掘范围和目标方块" translatedTo "Mining range and target blocks can be set via GUI")

        section("机器电压等级每高出一级：" translatedTo "For each increase in machine voltage level:", { aqua() })
        increase("可采掘最大范围翻倍（最高256）" translatedTo "Maximum mining range is doubled (up to 256)")
        increase("每次采掘的方块数量翻倍（最高4096）" translatedTo "The number of blocks mined each time is doubled (up to 4096)")
        decrease("耗电量翻4倍" translatedTo "Power consumption is quadrupled")
        content(("通入红石信号以重新计算采掘区域并执行" translatedTo "Input a redstone signal to recalculate the mining area and execute mining").green())
    }

    // 大型内燃机
    val LargeCombustionTooltipsProvider: (Long, Long, Boolean, Long) -> ComponentListSupplier =
        { baseEUt, oxygenBoost, canExtremeBoost, liquidOxygenBoost ->
            ComponentListSupplier {
                setTranslationPrefix("large_combustion")

                section(ComponentSlang.PowerGenerationEfficiency)
                content(ComponentSlang.BaseProductionEut(baseEUt))
                content(ComponentSlang.UsePerHourLubricant(FluidHelper.getBucket()))
                content("提供20mB/s的§a氧气§r，并消耗§4双倍§r燃料以产生§e$oxygenBoost EU/t§r的功率" translatedTo "Provide 20mB/s of §eOxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t")

                if (canExtremeBoost) {
                    content("提供80mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生§e$liquidOxygenBoost EU/t§r的功率" translatedTo "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t")
                }

                section(ComponentSlang.AfterModuleInstallation)
                increase("获得2倍速度" translatedTo "Gains 2x speed")
                decrease("燃料消耗速度变为2倍" translatedTo "Fuel consumption rate becomes 2x")
            }
        }

    // 高速模式
    val TurbineHighSpeedTooltips = ComponentListSupplier {
        setTranslationPrefix("turbine_high_speed")
        section("高速模式" translatedTo "High-Speed Mode")
        content("高速模式可进一步提升运行速度，与模块乘算" translatedTo "High-speed mode can further increase operating speed, multiplied with modules")
    }

    // 大型涡轮
    val LargeTurbineTooltipsProvider: (Long, Int) -> ComponentListSupplier =
        { baseEUt, rotorTier ->
            ComponentListSupplier {
                setTranslationPrefix("large_turbine")

                section(ComponentSlang.PowerGenerationEfficiency)
                content(ComponentSlang.BaseProductionEut(baseEUt))
                content(ComponentSlang.RotorEfficiency(rotorTier))

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
            content(ComponentSlang.BaseProductionEut(baseEUt))
            content(ComponentSlang.RotorEfficiency(rotorTier))
            content(("运行效率相当于16台同类大型涡轮" translatedTo "Operating efficiency is equivalent to 16 large turbines of the same type").green())
            content("可使用更多动力仓" translatedTo "Can use more power hatch")
            content("可安装转子仓，从中自动取出转子安装到空转子支架" translatedTo "Rotors can be installed in the rotor chamber, automatically extracting rotor for installation onto empty rotor brackets")

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
            content(ComponentSlang.BaseProductionEut(GTValues.V[GTValues.ZPM]))
            content(ComponentSlang.UsePerHourLubricant(10 * FluidHelper.getBucket()))
            content(
                ("提供320mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生" translatedTo "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to ") +
                    (GTValues.V[GTValues.UV]).toLiteralSupplier().yellow() +
                    (" EU/t的功率" translatedTo " EU/t"),
            )
            content(
                ("再提供480mB/s的§a四氧化二氮§r，并消耗§4四倍§r燃料以产生" translatedTo "Provide extra 480mB/s of §eNitrous Oxide§r, consuming §afour times§r fuel to produce up to ") +
                    (GTValues.V[GTValues.UHV]).toLiteralSupplier().yellow() +
                    (" EU/t的功率" translatedTo " EU/t"),
            )
        }

    // 化工厂
    val ChemicalFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_factory")

        section(ComponentSlang.CoilEfficiencyBonus)
        content(
            ("线圈等级每高出白铜一级能耗与时间减少5%" translatedTo "Each coil tier above Cupronickel, Reduces energy consumption and duration by 5%"),
            { gold() },
        )
    }
}
