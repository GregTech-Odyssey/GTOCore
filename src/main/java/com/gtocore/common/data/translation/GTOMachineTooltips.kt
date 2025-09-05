package com.gtocore.common.data.translation

import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine

import net.minecraft.network.chat.Component

import com.gregtechceu.gtceu.api.GTValues
import com.gtolib.api.annotation.NewDataAttributes
import com.gtolib.api.annotation.component_builder.ComponentBuilder
import com.gtolib.api.annotation.component_builder.ComponentTemplate
import com.gtolib.api.annotation.component_builder.StyleBuilder
import com.gtolib.api.lang.CNEN
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper

import java.util.function.Supplier

object GTOMachineTooltips {

    class TitleContentTooltipBuilder(val title: CNEN, val titleAttr: ComponentTemplate = NewDataAttributes.EMPTY_WITH_BAR, val titleStyle: StyleBuilder.() -> StyleBuilder = StyleBuilder::setGold) {
        private val contents: MutableList<ComponentBuilder> = mutableListOf()

        fun content(content: CNEN, attr: ComponentTemplate = NewDataAttributes.EMPTY_WITH_POINT, style: StyleBuilder.() -> StyleBuilder = StyleBuilder::setWhite, leadingStyle: StyleBuilder.() -> StyleBuilder = StyleBuilder::setOneTab): TitleContentTooltipBuilder {
            val line = attr.createBuilder(
                { it.addLines(content.cn(), content.en(), style) },
                { it },
                leadingStyle,
            )
            contents.add(line)
            return this
        }

        fun getSupplier(): Supplier<List<Component>> = titleAttr.create(
            { it.addLines(title.cn(), title.en(), titleStyle) },
            { it.addLines(*contents.toTypedArray()) },
        )
    }

    infix fun String.cnen(en: String): CNEN = CNEN(this, en)

    fun CNEN.andThen(other: Any): CNEN = CNEN(this.cn() + other, this.en() + other)

    fun CNEN.andThen(other: CNEN): CNEN = CNEN(this.cn() + other.cn(), this.en() + other.en())

    object Keywords {
        val Explosion = CNEN("§4§l爆炸", "§4§lexplode")
        val TemperatureMax = { temp: Int -> CNEN("最高温度: $temp", "Max Temperature: $temp") }
        val BaseProductionEut = { eut: Long -> CNEN("基础产能功率: §e$eut EU/t", "Base Production EUt: §e$eut EU/t") }
        val RotorEfficiency = { tier: Int ->
            var name = GTValues.VNF[tier] + "§r"
            CNEN(
                "转子支架每超过${name}一级，每级增加10%效率，并翻倍输出功率",
                "Each Rotor Holder above $name adds 10% efficiency and multiplies EU/t by 2",
            )
        }
        val UsePerHourLubricant = { cnt: Long -> CNEN("每小时消耗${cnt}mB润滑油", "Use ${cnt}mB Lubricant Per Hour") }
    }

    // 外置热源锅炉
    val BoilWaterMachineTooltips =
        TitleContentTooltipBuilder("需要外部热源工作" cnen "Requires external heat source to operate")
            .content(("当蒸汽溢出后继续工作会" cnen "When steam overflows, continuing to work will ").andThen(Keywords.Explosion))
            .content(
                ("可能发生爆炸的临界温度为§6" cnen "The critical temperature for explosion is §6").andThen(
                    BoilWaterMachine.DrawWaterExplosionLine,
                ),
            )

    val BeAwareOfBurnTooltips = TitleContentTooltipBuilder(
        "§4§l小 心 烫 伤 ！" cnen "§4§lBE AWARE OF BURNS!",
        NewDataAttributes.EMPTY_WITH_NON,
    )

    // 加热器
    val HeaterMachineTooltips =
        TitleContentTooltipBuilder("通过燃烧对四周机器进行加热" cnen "Burning to heat up around machines")
            .content("前方被阻挡后停止加热" cnen "Stop heating after front side is blocked.")
            .content("根据温度发出红石信号" cnen "Emits redstone signal according to the temperature.")
            .content(Keywords.TemperatureMax(HeaterMachine.MaxTemperature))
            .content(("机器过热会" cnen "When machine is too hot, it will ").andThen(Keywords.Explosion))

    // 电力加热器
    val ElectricHeaterMachineTooltips =
        TitleContentTooltipBuilder("使用电力对四周机器进行加热" cnen "Use electricity to heat up around machines")
            .content(Keywords.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
            .content(Keywords.TemperatureMax(HeaterMachine.MaxTemperature))
            .content("§6此机器不会爆炸" cnen "§6This machine will not explode")

    // 裂变反应堆

    // 反应堆结构
    val FissionReactorStructureTooltips =
        TitleContentTooltipBuilder(
            "反应堆结构组成" cnen "Reactor structure components",
        )
            .content(
                "通过燃料组件和冷却组件协同工作产生能量" cnen "Generates energy through fuel and cooling components working together",
            )
            .content(
                "燃料组件: 提供最大并行数量" cnen "Fuel component: Provides maximum parallel number",
                style = StyleBuilder::setGreen,
            )
            .content(
                "升温系数 = 燃料组件相邻数 + 1" cnen "Heating coefficient = adjacent fuel components + 1",
                style = StyleBuilder::setGray,
            )
            .content(
                "冷却组件: 提供最大冷却能力" cnen "Cooling component: Provides maximum cooling capability",
                style = StyleBuilder::setGreen,
            )
            .content(
                "冷却组件必须与燃料组件相邻才有效" cnen "Cooling components must be adjacent to fuel components to be effective",
                style = StyleBuilder::setGray,
            )

    // 温度管理系统
    val FissionReactorTemperatureTooltips = TitleContentTooltipBuilder(
        "温度管理系统" cnen "Temperature management system",
    )
        .content(
            "初始温度: 298K" cnen "Initial temperature: 298K",
            style = StyleBuilder::setGreen,
        )
        .content(
            "温度上限: 1500K" cnen "Temperature limit: 1500K",
            style = StyleBuilder::setRed,
        )
        .content(
            "升温速率: 配方产热 × 升温系数/秒" cnen "Heating rate: recipe heat × heating coefficient/sec",
            style = StyleBuilder::setGray,
        )
        .content(
            "自然降温: 停止工作时1K/秒" cnen "Natural cooling: 1K/sec when stopped",
            style = StyleBuilder::setGray,
        )
        .content(
            (
                "超过温度上限机器开始损坏，完全损坏时" cnen "Exceeding temperature limit damages machine, when fully damaged "
                ).andThen(Keywords.Explosion),
        )

    // 冷却系统
    val FissionReactorCoolingTooltips = TitleContentTooltipBuilder(
        "冷却系统" cnen "Cooling system",
    )
        .content(
            "冷却液类型: 蒸馏水或钠钾合金" cnen "Cooling liquid types: Distilled water or sodium-potassium alloy",
            style = StyleBuilder::setGreen,
        )
        .content(
            "冷却条件: 供给量 ≥ 需求量" cnen "Cooling condition: Supply ≥ demand",
        )
        .content(
            "最低需求量 = 配方产热 × 冷却参数 × 实际并行 × 当前温度 / 1500" cnen "Min demand = recipe heat × cooling param × actual parallel × current temp / 1500",
            style = StyleBuilder::setGray,
        )
        .content(
            "最高供给量 = (冷却组件 - 相邻数/3) × 8" cnen "Max supply = (cooling components - adjacent/3) × 8",
            style = StyleBuilder::setGray,
        )
        .content(
            "消耗量 = 需求量 × 冷却液系数" cnen "Consumption = demand × cooling liquid coefficient",
            style = StyleBuilder::setGray,
        )

    // 超频机制
    val FissionReactorOverclockingTooltips = TitleContentTooltipBuilder(
        "超频机制" cnen "Overclocking mechanism",
    )
        .content(
            "触发条件: 供给量 ≥ n × 需求量 (n>1)" cnen "Trigger condition: Supply ≥ n × demand (n>1",
            style = StyleBuilder::setGray,
        )
        .content(
            "超频效果: 减少n秒配方时间" cnen "Overclocking effect: Reduce n seconds recipe time",
            style = StyleBuilder::setGray,
        )

    // 冷却液产出
    val FissionReactorCoolingLiquidsTooltips = TitleContentTooltipBuilder(
        "冷却液产出" cnen "Cooling liquid output",
    )
        .content(
            "蒸馏水冷却: " cnen "Distilled water cooling: ",
            style = StyleBuilder::setGreen,
        )
        .content(
            "产出蒸汽，产量 = 消耗量 × min(160, 160/(1.4^(373-温度)))" cnen "Produces steam, Output = consumption × min(160, 160/(1.4^(373-temperature)))",
            style = StyleBuilder::setGray,
        )
        .content(
            "钠钾合金冷却:" cnen "Sodium-potassium alloy cooling:",
            style = StyleBuilder::setGreen,
        )
        .content(
            "≤825K: 热钠钾合金；>825K: 超临界钠钾合金" cnen "≤825K: Hot sodium-potassium alloy; >825K: Supercritical sodium-potassium alloy",
        )

    // 主信息：超级计算中心介绍
    val SupercomputingMainTooltips = TitleContentTooltipBuilder(
        title = "计算机超级计算中心" cnen "Computer Supercomputing Center",
        titleStyle = StyleBuilder::setRainbow,
    )
        .content(
            "将多台计算机集成在一起，提供大规模并行计算能力" cnen "Integrates multiple computers together to provide massive parallel computing power",
            attr = NewDataAttributes.EMPTY_WITH_TAB,
            style = StyleBuilder::setLightPurple,
        )

    // 先进冷却系统
    val SupercomputingLevelTooltips = TitleContentTooltipBuilder(
        title = "等级系统" cnen "Level System",
    )
        .content(
            "通过在主机内放置特定物品切换等级" cnen "Switch tiers by placing specific items in the mainframe",
        )
        .content(
            "结构方块等级必须与机器等级匹配" cnen "Structure block tiers must match machine tier",
            style = StyleBuilder::setBlinkingRed,
        )

    // 算力计算系统
    val SupercomputingPowerTooltips = TitleContentTooltipBuilder(
        title = "算力计算系统" cnen "Computing Power Calculation System",
    )
        .content(
            "最大输出算力 = 计算组件算力和 × 算力修正系数" cnen "Max output = sum of component power × correction factor",
        )
        .content(
            "等级2/3时修正系数会随时间衰减" cnen "At levels 2/3, correction factor decays over time",
            style = StyleBuilder::setGray,
        )
        .content(
            "衰减公式: ((系数-0.4)²/5000)×(0.8/log(系数+6))，最低0.8" cnen "Decay: ((factor-0.4)²/5000)×(0.8/log(factor+6)), at least 0.8",
            style = StyleBuilder::setGray,
        )

    // 导热剂冷却增效
    val SupercomputingThermalConductivityTooltips = TitleContentTooltipBuilder(
        title = "导热剂冷却增效" cnen "Thermal Conductivity Cooling Enhancement",
    )
        .content(
            "通过导热剂仓输入导热剂提升算力修正系数" cnen "Input thermal conductivity via hatch to boost correction factor",
            style = StyleBuilder::setGreen,
        )
        .content(
            "提升上限: 等级2(4) / 等级3(16)" cnen "Enhancement limits: Tier 2(4) / Tier 3(16)",
        )
        .content(
            "导热剂使用后会失效" cnen "Thermal conductivity becomes invalid after use",
            style = StyleBuilder::setRed,
        )
        .content(
            "MFPC效率: 块(0.18) 条(0.02) 粒(0.0022)" cnen "MFPC efficiency: Block(0.18) Ingot(0.02) Nugget(0.0022)",
            style = StyleBuilder::setGray,
        )
        .content(
            "Cascade-MFPC效率: 块(0.54) 条(0.06) 粒(0.0066)" cnen "Cascade-MFPC efficiency: Block(0.54) Ingot(0.06) Nugget(0.0066)",
            style = StyleBuilder::setGray,
        )
        .content(
            "寒冰碎片: 0.0001 (极低效率)" cnen "Ice Shards: 0.0001 (extremely low efficiency)",
            style = StyleBuilder::setGray,
        )

    // Tier 1 组件支持
    val SupercomputingTier1Tooltips = TitleContentTooltipBuilder(
        title = "Tier 1 : 支持HPCA系列组件" cnen "Tier 1 : Supports HPCA Series Components",
        titleStyle = StyleBuilder::setBlue,
    )
        .content(
            "槽位需求: 无" cnen "Slot requirement: None",
        )
        .content(
            "结构材料需求: 钨强化硼玻璃 + 计算机外壳 + 计算机散热口" cnen "Structure material requirements:",
        )

    // Tier 2 组件支持
    val SupercomputingTier2Tooltips = TitleContentTooltipBuilder(
        title = "Tier 2 : 支持NICH系列组件" cnen "Tier 2 : Supports NICH Series Components",
        titleStyle = StyleBuilder::setBlue,
    )
        .content(
            "槽位需求: 放入§a生物主机" cnen "Slot requirement: Place §abiological host",
        )
        .content(
            "结构材料需求: 安普洛强化硼玻璃 + 生物计算机外壳 + 相变计算机散热口" cnen "Structure material requirements:",
        )

    // Tier 3 组件支持
    val SupercomputingTier3Tooltips = TitleContentTooltipBuilder(
        title = "Tier 3 : 支持GWCA系列组件" cnen "Tier 3 : Supports GWCA Series Components",
        titleStyle = StyleBuilder::setBlue,
    )
        .content(
            "槽位需求: 放入§5超因果主机" cnen "Slot requirement: Place §5Hyper-Causal Host",
        )
        .content(
            "结构材料需求: 塔兰强化硼玻璃 + 引力子计算机外壳 + 逆熵计算机冷凝矩阵" cnen "Structure material requirements:",
        )
        .content(
            "自带跨维度桥接功能" cnen "Built-in cross-dimensional bridging capability",
            style = StyleBuilder::setOrange,
        )

    // 数字矿机
    val DigitalMinerTooltips = TitleContentTooltipBuilder(
        "让机器替代你挖矿" cnen "Mine for You",
    )
        .content(
            "固定每两秒采掘一次" cnen "Mines once every two seconds",
            style = StyleBuilder::setGreen,
        )
        .content(
            "可通过GUI设置采掘范围和目标方块" cnen "Mining range and target blocks can be set via GUI",
        )
        .content(
            "机器电压等级每高出一级：" cnen "For each increase in machine voltage level:",
            style = StyleBuilder::setAqua,
        )
        .content(
            "可采掘最大范围翻倍（最高256）" cnen "Maximum mining range is doubled (up to 256)",
            style = StyleBuilder::setGray,
        )
        .content(
            "每次采掘的方块数量翻倍（最高4096）" cnen "The number of blocks mined each time is doubled (up to 4096)",
            style = StyleBuilder::setGray,
        )
        .content(
            "耗电量翻4倍" cnen "Power consumption is quadrupled",
            style = StyleBuilder::setGray,
        )
        .content(
            "通入红石信号以重新计算采掘区域并执行" cnen "Input a redstone signal to recalculate the mining area and execute mining",
            style = StyleBuilder::setGreen,
        )

    // 大型内燃机

    val LargeCombustionGenerateTooltipsProvider: (Long, Long, Boolean, Long) -> TitleContentTooltipBuilder =
        { baseEUt, oxygenBoost, canExtremeBoost, liquidOxygenBoost ->
            var builder = TitleContentTooltipBuilder("发电效率" cnen "Power Generation Efficiency")
                .content(Keywords.BaseProductionEut(baseEUt))
                .content(Keywords.UsePerHourLubricant(FluidHelper.getBucket()))
                .content(
                    "提供20mB/s的§a氧气§r，并消耗§4双倍§r燃料以产生§e$oxygenBoost EU/t§r的功率" cnen "Provide 20mB/s of §eOxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t",
                )
            if (canExtremeBoost) {
                builder.content(
                    "提供80mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生§e$liquidOxygenBoost EU/t§r的功率" cnen "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to §e$oxygenBoost §rEU/t",
                )
            }
            builder
        }

    val LargeCombustionModuleTooltips = TitleContentTooltipBuilder("安装模块增益" cnen "Module Gain")
        .content("获得2倍速度" cnen "Gains 2x speed", style = StyleBuilder::setGreen)
        .content("燃料消耗速度变为2倍" cnen "Fuel consumption rate becomes 2x", style = StyleBuilder::setRed)

    // 大型涡轮

    // 高速模式
    val TurbineHighSpeedTooltips = TitleContentTooltipBuilder("高速模式" cnen "High-Speed Mode")
        .content("高速模式可进一步提升运行速度，与模块乘算" cnen "High-speed mode can further increase operating speed, multiplied with modules")

    // 发电效率
    val LargeTurbineGenerateTooltipsProvider: (Long, Int) -> TitleContentTooltipBuilder = { baseEUt, rotorTier ->
        TitleContentTooltipBuilder("发电效率" cnen "Power Generation Efficiency")
            .content(Keywords.BaseProductionEut(baseEUt))
            .content(Keywords.RotorEfficiency(rotorTier))
    }

    // 模块增益
    val LargeTurbineModuleTooltips = TitleContentTooltipBuilder("安装模块增益" cnen "Module Gain")
        .content("获得2倍速度" cnen "Gains 2x speed", style = StyleBuilder::setGreen)
        .content("获得额外110%涡轮效率" cnen "Gains additional 110% turbine efficiency", style = StyleBuilder::setGreen)
        .content("转子损耗速度变为2倍" cnen "Rotor wear rate becomes 2x", style = StyleBuilder::setRed)

    // 特大涡轮

    // 发电效率
    val MegaTurbineGenerateTooltipsProvider: (Long, Int) -> TitleContentTooltipBuilder = { baseEUt, rotorTier ->
        TitleContentTooltipBuilder("发电效率" cnen "Power Generation Efficiency")
            .content(Keywords.BaseProductionEut(baseEUt))
            .content(Keywords.RotorEfficiency(rotorTier))
            .content(
                "运行效率相当于16台同类大型涡轮" cnen "Operating efficiency is equivalent to 16 large turbines of the same type",
                style = StyleBuilder::setGreen,
            )
            .content("可使用更多动力仓" cnen "Can use more power hatch")
            .content("可安装转子仓，从中自动取出转子安装到空转子支架" cnen "Rotors can be installed in the rotor chamber, automatically extracting rotor for installation onto empty rotor brackets")
    }

    // 模块增益
    val MegaTurbineModuleTooltips = TitleContentTooltipBuilder("安装模块增益" cnen "Module Gain")
        .content("获得3倍速度" cnen "Gains 3x speed", style = StyleBuilder::setGreen)
        .content("获得额外130%涡轮效率" cnen "Gains additional 130% turbine efficiency", style = StyleBuilder::setGreen)
        .content("转子损耗速度变为3倍" cnen "Rotor wear rate becomes 3x", style = StyleBuilder::setRed)

    // 化学能吞噬者
    var ChemicalEnergyDevourerGenerateTooltips =
        TitleContentTooltipBuilder("发电效率" cnen "Power Generation Efficiency")
            .content(Keywords.BaseProductionEut(GTValues.V[GTValues.ZPM]))
            .content(Keywords.UsePerHourLubricant(10 * FluidHelper.getBucket()))
            .content(
                ("提供320mB/s的§a液态氧§r，并消耗§4双倍§r燃料以产生§e" cnen "Provide 80mB/s of §eLiquid Oxygen§r, consuming §adouble§r fuel to produce up to §e")
                    .andThen(GTValues.V[GTValues.UV])
                    .andThen(" EU/t§r的功率" cnen " §rEU/t"),
            )
            .content(
                ("再提供480mB/s的§a四氧化二氮§r，并消耗§4四倍§r燃料以产生§e" cnen "Provide extra 480mB/s of §eNitrous Oxide§r, consuming §afour times§r fuel to produce up to §e")
                    .andThen(GTValues.V[GTValues.UHV])
                    .andThen(" EU/t§r的功率" cnen " §rEU/t"),
            )
}
