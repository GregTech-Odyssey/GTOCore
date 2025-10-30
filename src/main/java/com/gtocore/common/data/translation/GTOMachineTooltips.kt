package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.toComponentSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.common.data.translation.ComponentSlang.MainFunction
import com.gtocore.common.data.translation.ComponentSlang.RunningRequirements
import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.multiblock.generator.TurbineMachine
import com.gtocore.common.machine.multiblock.storage.MEStorageMachine
import com.gtocore.common.machine.multiblock.storage.MultiblockCrateMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine

import net.minecraft.network.chat.Component

import com.google.common.collect.ImmutableMap
import com.gregtechceu.gtceu.api.GTValues
import com.gtolib.GTOCore
import com.gtolib.utils.NumberUtils
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper

/**
 * 用于收纳机器相关用法
 *
 * 小作文请参看 [GTOMachineStories]
 */
object GTOMachineTooltips {

    // 区域破坏器
    val AreaDestructionToolsTooltips = ComponentListSupplier {
        setTranslationPrefix("area_destruction_tools")

        miraculousTools("区域破坏器" translatedTo "Area Destruction Tools")

        highlight("专业的世界摧毁者" translatedTo "Professional world destroyer") { rainbowSlow() }
        danger("一键清空方块！！！" translatedTo "One click to clear the blocks!!!")
        danger("无视一切条件！！！" translatedTo "Ignore all conditions!!!")
        danger("注意爆破安全！！！" translatedTo "Pay attention to blasting safety!!!")
        highlight("多人游戏请务必提醒其他成员！" translatedTo "Be sure to remind other players in the multiplayer game! ")

        section("可用模式" translatedTo "Available Modes")
        function("向库存放入物品以切换模式" translatedTo "Add items to inventory to switch modes")
        command("放入§b模具(球)§r为§6球模式§r" translatedTo "Put §bCasting Mold (Ball)§r for §6Ball mode§r")
        command("放入§b模具(圆柱)§r为§6圆柱模式§r" translatedTo "Put §bCasting Mold (Cylinder)§r for §6Casting mode§r")
        command("放入§b模具(块)§r为§6区块模式§r" translatedTo "Put §bCasting Mold (Block)§r for §6Chunk mode§r")
        command("放入§b奇点§r为§6指定区域模式§r" translatedTo "Put §bSingularity§r for §6Designated Area mode§r")
        info("此时用两个§9坐标信息卡§r来确定区域" translatedTo "Now use two §9Coordinate Cards§r to determine the area")

        section("爆炸当量" translatedTo "Explosive Yield")
        increase("向库存添加以下物品以提高爆炸当量：" translatedTo "Add the following items to inventory to increase explosive yield:")
        info("§c工业TNT§r/§c核弹§r/§c超能硅岩爆弹§r/§c轻子爆弹§r/§c量子色动力学爆弹§r" translatedTo "§cIndustrial TNT§r/§cNuke Bomb§r/§cNaquadria Charge§r/§cLeptonic Charge§r/§cQuantum Chromodynamic Charge§r")
    }

    // 工业平台展开工具 - 物品描述
    val IndustrialPlatformDeploymentToolsTooltips = ComponentListSupplier {
        setTranslationPrefix("industrial_platform_deployment_tools")

        story(
            "在过去，工业平台展开工具曾是工程师建造巨型工厂的神器" translatedTo
                "In the past, the Industrial Platform Deployment Tool was a revered instrument for engineers constructing massive factories.",
        )
        story(
            "只需几条指令就能生成复杂的多层工业设施" translatedTo
                "With just a few commands, it could generate complex multi-layered industrial facilities.",
        )
        story(
            "然而，受能源危机与安全管制影响，新版工具的功能被大幅削弱" translatedTo
                "However, due to the energy crisis and safety regulations, the capabilities of the new version have been significantly reduced.",
        )
        story(
            "如今它只能根据预设蓝图放置一些简单的平台结构" translatedTo
                "Now it can only place simple platform structures based on preset blueprints.",
        )
        story(
            "尽管如此，对于快速搭建基础工业基地，它依然是不可或缺的好帮手。" translatedTo
                "Nevertheless, it remains an indispensable assistant for quickly setting up basic industrial bases.",
        )

        miraculousTools("工业平台展开工具" translatedTo "Industrial Platform Deployment Tools")

        section("使用预设蓝图部署基地" translatedTo "Deploy the base using the preset blueprint.")
        section(
            "需要使用指定的工业组件作为材料" translatedTo
                "Specific industrial components are required as materials.",
        )
        section(
            "有时需要根据需求提供额外的物品作为材料" translatedTo
                "Sometimes it is necessary to provide additional items as materials based on demand.",
        )

        highlight("专业的基地平台展开工具" translatedTo "Professional base platform deployment tools") { rainbowSlow() }
        highlight("来自泛银河系格雷科技销售部的拳头产品" translatedTo "Flagship product from the sales department of Pan-Galaxy Gray Technology") { rainbowSlow() }

        story(
            "可以添加扩展平台预设以获得更多预设蓝图。" translatedTo
                "You can add extended platform presets to get more preset blueprints.",
        )

        guide("详细操作请查看机器内简介" translatedTo "For detailed instructions, see the in-machine introduction")
    }

    // 工业平台展开工具 — 详细介绍
    val IndustrialPlatformDeploymentToolsIntroduction = ComponentListSupplier {
        setTranslationPrefix("industrial_platform_deployment_tools.introduction")

        highlight("快速部署基础工业平台" translatedTo "Quickly deploy a basic industrial platform") { rainbowSlow() }

        section("基本功能" translatedTo "Basic Functions")
        content(
            "本工具可根据预设蓝图快速放置各种工业平台结构" translatedTo
                "This tool can quickly place various industrial platform structures based on preset blueprints.",
        )
        content(
            "适用于快速搭建基础生产基地和复杂工业设施" translatedTo
                "making it ideal for rapidly establishing both basic production bases and complex industrial facilities.",
        )

        section("界面简介" translatedTo "Interface Introduction")
        content("一：主界面 - 用于选择预设，调整位置，浏览材料需求，修改展开设置。" translatedTo "1. Main Interface - Used to select presets, adjust positions, browse material requirements, and modify expansion settings.")
        content("二：控制区 - 右上角一块，用于控制翻页，启动和进度监控" translatedTo "2. Control Area - The upper right corner, used to control page turning, startup, and progress monitoring.")
        content("三：物品槽区 - 拥有27格存储，前9格可以通过GUI访问，用来输入材料需求" translatedTo "3. Item Slots - Has 27 storage slots, the first 9 of which can be accessed through the GUI and used to input material requirements.")

        section("使用流程" translatedTo "Usage Process")
        content("第一步：选择预设蓝图" translatedTo "1. Select a preset blueprint")
        content("第二步：确认所需材料，可从物品栏加载或卸载材料" translatedTo "2. Confirm required materials; you can load or unload materials from the inventory")
        content("第三步：调整放置参数（位置偏移、旋转、镜像等）" translatedTo "3. Adjust placement parameters (position offset, rotation, mirroring, etc.)")
        content("第四步：确认无误后开始放置任务" translatedTo "4. Start the placement task after confirming everything is correct")
        content("任务完成后可重新开始新一轮部署" translatedTo "5. Can start a new deployment after task completion")

        section("高级功能" translatedTo "Advanced Features")
        content("支持使用坐标卡导出已有区域为新的平台蓝图" translatedTo "Supports using coordinate cards to export existing areas as new platform blueprints")
        content("可调整放置速度、光照更新、空气方块处理等高级选项" translatedTo "Allows adjustment of advanced options such as placement speed, light updates, and air block handling")

        section("材料需求" translatedTo "Material Requirements")
        content(
            "根据选择的预设蓝图，系统会自动计算所需的材料" translatedTo
                "The system will automatically calculate the required materials based on the selected blueprint.",
        )
        content(
            "支持多种材料类型，包括基础、扩展和特种系列工业组件" translatedTo
                "Supports multiple material types, including basic, extended, and special-series industrial component.",
        )
        content(
            "部分预设需要额外的材料" translatedTo
                "Some presets require additional materials.",
        )

        section("注意事项" translatedTo "Notes")
        content(
            "放置任务开始后无法取消，请确认位置和材料无误" translatedTo
                "Once started, the placement task cannot be canceled. Please confirm that the position and materials are correct.",
        )
        content(
            "大型结构可能需要较长放置时间，请耐心等待" translatedTo
                "Large structures may take longer to place. Please be patient.",
        )

        guide(
            "请按照步骤操作，完成后点击确认按钮开始放置" translatedTo
                "Please follow the steps and click the confirm button to start placement.",
        )

        section("导出功能" translatedTo "Export function")
        content("放入两张坐标卡开启导出模式，可导出平台与GT多方块结构" translatedTo "Insert two coordinate cards and enable export mode to export the platform and GT multi-block structure.")
    }

    // 魔力增幅仓
    val ManaAmplifierHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("mana_amplifier_hatch")

        section(MainFunction)
        content("如果运行前输入了等同机器最大功率的魔力" translatedTo "If mana equivalent to the machine's maximum power is input prior to operation")
        increase("则将本次配方改为无损超频" translatedTo "The current recipe will switch to perfect overclocking.")
        decrease("否则，机器不执行配方" translatedTo "Otherwise, the machine will not execute the recipe.")
    }

    // 魔力加热器
    val ManaHeaterTooltips = ComponentListSupplier {
        setTranslationPrefix("mana_heater")

        section(RunningRequirements)
        command("输入魔力加热" translatedTo "Input mana to heat")
        increase("如果输入§c火元素蒸汽§r，则加热速度翻5倍" translatedTo "If §cfire element gas§r is input, the heating speed will be 5 times faster")
        command(ComponentSlang.TemperatureMax(2400))
    }

    // 苍穹凝聚器
    val CelestialCondenserTooltips = ComponentListSupplier {
        setTranslationPrefix("celestial_condenser")

        content("凝聚苍穹之上的能量" translatedTo "Condenses the energy from beyond the firmament")
        section(RunningRequirements)
        command("暴露于天空之下，不可有遮挡" translatedTo "Must be exposed directly to the sky with no obstructions")
        increase("在主世界白天可以凝聚 - 曦煌" translatedTo "Can be condensed in the Overworld during daytime - Solaris")
        increase("在主世界夜晚可以凝聚 - 胧华" translatedTo "Can be condensed in the Overworld during nighttime - Lunara")
        increase("在末地可以凝聚 - 虚湮" translatedTo "Can be condensed in the End - Voidflux")
        command("运行配方时需要消耗这些能量" translatedTo "This energy is consumed when running recipes")
    }

    var spaceShieldHatchTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("space_shield_hatch")

        content("在GTO寰宇重工的空间站技术还没完全完善的时候科研人员紧急开发出的小型防护罩" translatedTo "A small protective shield urgently developed by researchers when GTO Universal Heavy Industries' space station technology was not fully developed")
        content("经过技术改良，现在可以保护机器免受太空环境干扰" translatedTo "After technical improvements, it can now protect machines from being unable to operate due to space environment interference")
        decrease("需要机器内置的激光仓提供激光" translatedTo "Requires lasers provided by built-in laser hatches in the machine")
        section(MainFunction)
        function("保护机器免受太空环境干扰而无法运行" translatedTo "Protects machines from being unable to operate due to space environment interference")
        decrease("需要机器同时配备激光能源仓" translatedTo "Requires the machine to be equipped with laser hatches at the same time")
    }

    // 转子仓
    val RotorHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("rotor_hatch")

        section(RunningRequirements)
        info("不同类型的转子的效率，耐久不同" translatedTo "Different types of rotors have different efficiency and durability")
    }

    // 放射仓
    val RadiationHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("radiation_hatch")

        section(RunningRequirements)
        content("仓中的辐射遵循以下规则：" translatedTo "The radiation in the hatch follows the following rules:")
        info("初始辐射=(配方辐射-抑制量)x(1+放射材料数量/64)" translatedTo "Initial radiation = (Recipe radiation - inhibition) * (1 + count of radiation materials / 64)")
        decrease("当仓中没有放射性材料时辐射随时间逐渐衰减" translatedTo "When there are no radiation materials in the barn, the radioactivity gradually decreases over time")
    }

    // 模块化可配置维护仓
    val ModularConfigurationMaintenanceHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("modular_configuration_maintenance_hatch")

        section(MainFunction)
        function("插入不同的自动维护仓以启用不同的功能" translatedTo "Insert different auto-maintenance hatches to enable different functions.")
    }

    // 温度/真空接口
    val TempVacuumInterfaceTooltips = ComponentListSupplier {
        setTranslationPrefix("temp_vacuum_interface")

        section(MainFunction)
        content("与此部件连接可以为多方块机器传导热量并提供真空" translatedTo "Conduct heat and provide vacuum for multiblock machines through connections with this part")
    }

    // 中子加速器
    val NeutronAcceleratorTooltips = { voltage: Long, voltageName: String, euConsume: Long, euCapacity: Long ->
        ComponentListSupplier {
            setTranslationPrefix("neutron_accelerator")

            section(RunningRequirements)
            command("最大输入电压：$voltage ($voltageName§r)" translatedTo "Max Voltage Input: $voltage ($voltageName§r)")
            command("最大EU消耗: $euConsume" translatedTo "Max EU Consumption: $euConsume")
            function("每点EU都会转化为§e10~20-eV§b中子动能" translatedTo "Each point of EU converts to §e10~20-eV§b neutron kinetic energy")
            content(ComponentSlang.Capacity(euCapacity.toString()))
        }
    }

    // 传感器
    val SensorTooltips = ComponentListSupplier {
        setTranslationPrefix("sensor")

        section("红石信号" translatedTo "Redstone Signal")
        content("未反转时，数值低于最低值或高于最高值时输出0，介于两者之间时输出递增的1-15的红石信号" translatedTo "When not inverted, outputs 0 when the value is below the minimum or above the maximum, or an increasing redstone signal between 1-14 when in between")
        content("反转时，数值低于最低值或高于最高值时输出15，介于两者之间时输出递减的1-15的红石信号" translatedTo "When inverted, outputs 15 when the value is above the maximum or below the minimum, or a decreasing redstone signal between 1-14 when in between")
    }

    // 催化剂仓
    val CatalystHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("catalyst_hatch")

        section(MainFunction)
        function("使用催化剂仓输入催化剂" translatedTo "Use catalyst hatch to input catalysts")
        ok("每次运行只消耗一点催化剂耐久，可以让催化剂重复使用" translatedTo "Only consumes ONE catalyst durability each time, allowing the catalyst to be reused")
    }

    // ME存储访问仓
    val MEStorageAccessHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("me_storage_access_hatch")

        section(MainFunction)
        function("访问ME存储器内的存储" translatedTo "Access storage in ME storage")
        content("直接让ME线缆连上就好，不推荐无线连接" translatedTo "Directly let ME cable connect, not recommended to use wireless connection")
    }

    // 合成样板仓
    val MeCraftPatternHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("me_craft_pattern_part_machine")

        section(MainFunction)
        function("合成样板仓用于存储合成样板" translatedTo "Craft Pattern Hatch is used to store crafting patterns")
        function("配合超级分子装配室使用" translatedTo "Use it with Super Molecular Assembler")
        info(ComponentSlang.Capacity(72.toString()))
    }

    // ME样板总成
    val MePatternHatchTooltips = { capacity: Int ->
        ComponentListSupplier {
            setTranslationPrefix("me_pattern_hatch")

            section("提供样板" translatedTo "Provides Patterns")
            function("可以放入样板，并进行一键发配" translatedTo "Can put patterns and distribute them one click")
            function("样板间分别隔离，互不干扰" translatedTo "Patterns are isolated from each other, do not interfere with each other")
            important("对着样板按鼠标中键可单独设置电路或者提供特别输入" translatedTo "Press the middle mouse button on the pattern to set the circuit or provide special input")
            info(ComponentSlang.Capacity(capacity.toString()))
        }
    }

    // ME催化剂样板总成
    val MeCatalystPatternBufferTooltips = ComponentListSupplier {
        setTranslationPrefix("me_catalyst_pattern_buffer")

        section(MainFunction)
        function("使用催化剂仓输入催化剂" translatedTo "Use catalyst hatch to input catalysts")
        ok("不消耗催化剂耐久，可以让催化剂重复使用" translatedTo "Not consume catalyst durability each time, allowing the catalyst to be reused")
    }

    // ME自动连接
    val AutoConnectMETooltips = ComponentListSupplier {
        setTranslationPrefix("auto_connect_me")

        section("允许自动连接ME无线网络" translatedTo "Allow automatically connecting to the ME Wireless network")
        guide("按下Shift放置以自动连接收藏的网络" translatedTo "Press Shift to place to automatically connect to the collected network")
        danger("小心塞爆矿处！" translatedTo "Be careful to explode the ae storage! ")
    }

    // 多方块板条箱
    val MultiblockCrateMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("multiblock_crate_machine")

        section(MainFunction)
        function("可以存储大量物品" translatedTo "Can store many many items")
        guide("右键点击以打开界面" translatedTo "Right click to open the interface")
        info(ComponentSlang.Capacity(MultiblockCrateMachine.Capacity.toString()))
    }

    val fishingFarmTooltips: ComponentListSupplier = ComponentListSupplier {
        setTranslationPrefix("fishing_farm")

        section(("设置电路以启用" translatedTo "Set the circuit to enable").gold() + ("自动钓鱼" translatedTo "automatic fishing").scrollOptical())
        content(("1号电路：" translatedTo "Circuit 1:").gold() + ("随机垂钓" translatedTo "random fishing").aqua())
        content(("2号电路：" translatedTo "Circuit 2:").gold() + ("捕捉鱼类" translatedTo "catch fish").aqua())
        content(("3号电路：" translatedTo "Circuit 3:").gold() + ("捕捉垃圾" translatedTo "catch junk").aqua())
        content(("4号电路：" translatedTo "Circuit 4:").gold() + ("捕捉宝藏" translatedTo "catch treasure").aqua())
    }

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

    // 性能监控器
    val PerformanceMonitorMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("performance_monitor_machine")

        section(MainFunction)
        function("能监测全部机器或AE网络3.2秒内的平均延迟" translatedTo "Can monitor all machines or AE grids' average delay within 3.2 seconds and support highlighting")
        guide("右键点击机器以打开性能监测界面" translatedTo "Right click on the machine to open performance monitoring interface")
    }

    // 监控器系列
    val monitor = { tooltip: ComponentListSupplier.() -> Unit ->
        ComponentListSupplier {
            setTranslationPrefix("monitor")
            highlight("监控器系列" translatedTo "Monitor series") { rainbowSlow() }
            tooltip()
        }
    }

    // 基础监控器
    val BasicMonitorTooltips = monitor {
        section(MainFunction)
        function("是监控器的基础组成部分" translatedTo "Is the basic component of the monitor")
        function("可以与其他§6多个监控器§r系列相连" translatedTo "Can be connected to other §6multiple monitors§r series")
        function("多个监控器可以组成一个大屏" translatedTo "Multiple monitors can form a large screen")
        function("可以使用§d喷漆§r对监控器分组" translatedTo "Can use§d paint spray§r to group monitors")
        function("与§6监控器组件§r相连来显示不同信息" translatedTo "Use §6monitor components§r to display different information")
    }

    // 监控器电网组件
    val MonitorPowerComponentTooltips = monitor {
        section(MainFunction)
        function("显示§6无线电网§r的数据" translatedTo "Display§6 wireless energy grid§r data")
    }

    // 监控器魔力组件
    val MonitorManaComponentTooltips = monitor {
        section(MainFunction)
        function("显示§6无线魔力网§r的数据" translatedTo "Display§6 wireless mana grid§r data")
    }

    // 监控器算力组件
    val MonitorComputingComponentTooltips = monitor {
        section(MainFunction)
        function("接入光缆网络以显示§6算力使用情况§r" translatedTo "Connect to the optical cable network to display §6computing power usage§r")
    }

    // 监控器ME吞吐量监控组件
    val MonitorMEThroughputComponentTooltips = monitor {
        section(MainFunction)
        function("显示ME中§6指定物品§r的吞吐量数据" translatedTo "Display the throughput data of §6specified items §rin ME")
    }

    // 监控器机器通用组件
    val MonitorMachineComponentTooltips = monitor {
        section(MainFunction)
        function("显示§6机器§r的通用数据" translatedTo "Display the general data of §6machines§r")
        function("例如耗电，产电，输入，输出等" translatedTo "For example, power consumption, power production, input, output, etc.")
        command("需要放入坐标信息卡" translatedTo "Need to put coordinate card")
    }

    // 监控器合成处理单元组件
    val MonitorCraftingComponentTooltips = monitor {
        section(MainFunction)
        function("显示§6ME合成处理单元§r的合成数据" translatedTo "Display the crafting data of §6crafting unit§r")
    }

    // 监控器自定义文本组件
    val MonitorCustomTextComponentTooltips = monitor {
        section(MainFunction)
        function("用于显示§6自定义文本§r在监控器上" translatedTo "Display§6 custom text§r")
        function("同一块监控器大屏的多个文本组件将被顺序显示" translatedTo "Multiple text components of the same block monitor screen will be displayed in sequence")
    }

    // 超立方体
    val HyperCubeMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("hyper_cube_machine")

        highlight("代理一个流体或物品存储器" translatedTo "Proxy a fluid or item or both storage")
        command("使用§b坐标信息卡§r绑定方块" translatedTo "Use the §bCordinate Card§r to bind a storage block")
        function("绑定某方块后，对此机器进行物品或流体操作视同对被绑定的方块操作" translatedTo "Bind a storage to this machine to operate it as if it were the bound storage")
        guide("右键点击以打开界面" translatedTo "Right click to open the interface")
        increase("此方块有升级版本" translatedTo "Has upgrade version")
    }

    // 进阶超立方体
    val AdvancedHyperCubeMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_hyper_cube_machine")

        highlight("代理多个流体或物品存储器" translatedTo "Proxy (a or multi) (fluid or item or both)storage")
        command("使用§b坐标信息卡§r绑定方块" translatedTo "Use the §bCordinate Card§r to bind a storage block")
        function("绑定某方块后，对此机器进行物品或流体操作视同对被绑定的方块操作" translatedTo "Bind a storage to this machine to operate it as if it were the bound storage")
        function("若绑定多个方块，则依序对他们操作" translatedTo "Operate them in order if bind multiple storages")
        guide("右键点击以打开界面" translatedTo "Right click to open the interface")
        guide("对装配线自动化很好用" translatedTo "Good for assembly line automation")
    }

    // 光伏电站
    val PhotovoltaicPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("photovoltaic_plant")

        section(RunningRequirements)
        function("维度和天气决定输出功率" translatedTo "Calculate power output based on dimension and weather")
        command("在空间站运行时可保持最大功率，但需提供每秒功率/4mB的蒸馏水保持运行" translatedTo "The space station can maintain full power operation, requires a distilled water supply of Power/4 mB per second")

        section(ComponentSlang.RecommendedUseAs("生产魔力" translatedTo "mana production"))
        function("在机器内放置64朵太阳花以使机器不再发电，转而采集魔力" translatedTo "Place 64 dayblooms in the machine to stop power generation and start collecting mana")
    }

    // 加热器
    val HeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("heater_machine")

        section("通过燃烧对四周机器进行加热" translatedTo "Burning to heat up around machines")
        content("前方被阻挡后停止加热" translatedTo "Stop heating after front side is blocked.")
        content("根据温度发出红石信号" translatedTo "Emits redstone signal according to the temperature.")
        command(ComponentSlang.TemperatureMax(HeaterMachine.MaxTemperature))
        error(("机器过热会" translatedTo "When machine is too hot, it will ") + ComponentSlang.Explosion)
        danger(ComponentSlang.BeAwareOfBurn)
    }

    // 电力加热器
    val ElectricHeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("electric_heater_machine")

        section("使用电力对四周机器进行加热" translatedTo "Use electricity to heat up around machines")
        command(ComponentSlang.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
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

        highlight("计算机超级计算中心" translatedTo "Computer Supercomputing Center") { rainbow() }
        content(
            "将多台计算机集成在一起，提供大规模并行计算能力" translatedTo "Integrates multiple computers together to provide massive parallel computing power",
            { lightPurple() },
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
        content("使用冷却剂: 多氯联苯冷却剂" translatedTo "Coolant used: PCB coolant")

        // Tier 2 组件支持
        section("Tier 2 : 支持NICH系列组件" translatedTo "Tier 2 : Supports NICH Series Components", { blue() })
        content(("槽位需求: 放入" translatedTo "Slot requirement: Place ") + ("生物活性主机" translatedTo "Biological Mainframe").scrollBioware())
        content("结构材料需求: 安普洛强化硼玻璃 + 生物计算机外壳 + 相变计算机散热口" translatedTo "Structure material requirements: Neutronium Borosilicate Glass + Biocomputer Casing + Phase Change Biocomputer Cooling Vents")
        content("使用冷却剂: 液态氦(会输出气态氦)" translatedTo "Coolant: Liquid helium (will output gaseous helium)")

        // Tier 3 组件支持
        section("Tier 3 : 支持GWCA系列组件" translatedTo "Tier 3 : Supports GWCA Series Components", { blue() })
        content(("槽位需求: 放入" translatedTo "Slot requirement: Place ") + ("超因果主机" translatedTo "Supracausal Mainframe").rainbowGradient())
        content("结构材料需求: 塔兰强化硼玻璃 + 引力子计算机外壳 + 逆熵计算机冷凝矩阵" translatedTo "Structure material requirements: Taranium Borosilicate Glass + Graviton Computer Casing + Anti Entropy Computer Condensation Matrix")
        content("使用冷却剂: 液态氦(会输出气态氦)" translatedTo "Coolant: Liquid helium (will output gaseous helium)")
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

        highlight("分子装配室 Pro Max 版！" translatedTo "Molecular assembler Pro Max Edition!") { rainbowSlow() }

        section(RunningRequirements)
        command("通过合成样板仓放入合成样板" translatedTo "Insert crafting patterns via craft pattern hatches")
        info("每个物品合成消耗1EU" translatedTo "Each item crafted consumes 1 EU")
    }

    // ME 超算核心
    val MECPUTooltips = ComponentListSupplier {
        highlight("ME 网络超级 CPU" translatedTo "Super CPU in ME Network") { rainbowSlow() }

        section("CPU 性能" translatedTo "CPU Performance")
        function("容量：决定CPU可以处理的总任务大小" translatedTo "Capacity: Determines the total task size the CPU can handle")
        content(
            "填满481个T5元件解锁无限存储" translatedTo "Fill 481 T5 storage units to unlock infinity storage",
            { rainbowSlow() },
        )
        info("公式：Σ合成单元存储量" translatedTo "Formula: ΣCrafting unit storage")
        function("并行数：决定CPU一次可发配的样板数量" translatedTo "Parallel: Determines the number of patterns the CPU can dispatch at once")
        info("公式：合成单元个数 x 并行仓并行数" translatedTo "Formula: Number of crafting units x parallel number of parallel hatch")
        function("线程数：决定CPU能同时进行的任务数量" translatedTo "Threads: Determines the number of tasks the CPU can perform simultaneously")
        info("公式：2^玻璃等级" translatedTo "Formula: 2^Glass Tier")
    }

    // 大型内燃机
    val LargeCombustionTooltips =
        { baseEUt: Long, oxygenBoost: Long, canExtremeBoost: Boolean, liquidOxygenBoost: Long ->
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
    val LargeTurbineTooltips = { baseEUt: Long, rotorTier: Int ->
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
    val MegaTurbineGenerateTooltips = { baseEUt: Long, rotorTier: Int ->
        ComponentListSupplier {
            setTranslationPrefix("mega_turbine")

            section(ComponentSlang.PowerGenerationEfficiency)
            function(ComponentSlang.BaseProductionEut(baseEUt))
            increase(ComponentSlang.RotorEfficiency(rotorTier))
            function("运行效率相当于16台同类大型涡轮" translatedTo "Operating efficiency is equivalent to 16 large turbines of the same type")
            function("启动速度为同类大型涡轮的4倍" translatedTo "Startup speed is 4 times that of similar large turbines")
            increase("可使用更多动力仓" translatedTo "Can use more power hatch")
            increase("可安装转子仓，从中自动取出转子安装到空转子支架" translatedTo "Rotors can be installed in the rotor chamber, automatically extracting rotor for installation onto empty rotor brackets")

            section(ComponentSlang.AfterModuleInstallation)
            increase("获得3倍速度" translatedTo "Gains 3x speed")
            increase("获得额外130%涡轮效率" translatedTo "Gains additional 130% turbine efficiency")
            decrease("转子损耗速度变为3倍" translatedTo "Rotor wear rate becomes 3x")

            section(ComponentSlang.CoilEfficiencyBonus)
            increase("线圈等级每高出白铜一级，转子启动速度增加20%" translatedTo "Each coil tier above Cupronickel increases rotor startup speed by 20%")

            val cs1 = ("高速模式调节器（专家模式专属）" translatedTo "High-Speed Mode Regulator (Expert Mode Exclusive)")
            val cf1 = ("允许调节涡轮的高速倍率以换取转子寿命" translatedTo "Allows adjustment of turbine high-speed multiplier in exchange for rotor lifespan")
            val ci1 = ("高速倍率范围为0.1x到5.0x，并将乘数自动与原涡轮乘数相乘" translatedTo "The high-speed multiplier ranges from 0.1x to 5.0x, and the multiplier is automatically multiplied by the original turbine multiplier")

            val cs2 = ("玻璃等级加成（专家模式专属）" translatedTo "Glass Tier Bonus (Expert Mode Exclusive)")
            val ci2 = ("专家模式下，玻璃等级每一级，高速调节器的损坏基数-0.08" translatedTo "In expert mode, each glass tier reduces the damage base of the high-speed regulator by 0.08")
            val cf2 = ("最低的损坏基数为1.2" translatedTo "The minimum damage base is 1.2")

            if (GTOCore.isExpert()) {
                section(cs1)
                function(cf1)
                info(ci1)
                info(ComponentSupplier(Component.translatable(TurbineMachine.DESC4)))
                info(ComponentSupplier(Component.translatable(TurbineMachine.DESC5)))

                section(cs2)
                increase(ci2)
                info(cf2)
            }
        }
    }

    // 化学能吞噬者
    val ChemicalEnergyDevourerGenerateTooltips = ComponentListSupplier {
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

        section(RunningRequirements)
        command("需要放入对应配方等级的小机器" translatedTo "Requires corresponding tier small machine")
        error("无法通过超净维护仓获得洁净环境" translatedTo "Cannot obtain clean environment through clean maintenance")

        section(ComponentSlang.EfficiencyBonus)
        content("配方等级每高出ULV一级，并行数+2，安装附属模块后+4" translatedTo "For each tier above ULV, parallelism +2, After installing the auxiliary module +4")
        command("最终配方等级受限于整体框架等级" translatedTo "Final recipe tier is constrained by framework tier")
    }

    // 培养缸
    val CulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("culturing_tank")

        section(RunningRequirements)
        command("过滤器等级决定配方等级" translatedTo "Filter tier§r determines recipe tier")
        command("玻璃等级决定可用电压上限" translatedTo "Glass tier§r determines upper limit of voltage usable")
    }

    // 大型培养缸
    val LargeCulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("large_culturing_tank")

        section(RunningRequirements)
        command("过滤器等级决定配方等级" translatedTo "Filter tier§r determines recipe tier")
        command("玻璃等级决定可用电压上限" translatedTo "Glass tier§r determines upper limit of voltage usable")
    }

    // 纳米锻炉
    val NanoForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("nano_forge")

        section(RunningRequirements)
        command("需要放入对应的纳米蜂群" translatedTo "Requires corresponding nano swarm")
        info("三种等级: 碳, 安普洛, 龙" translatedTo "Three tiers: Carbon, Amprosium, Draconium")
    }

    // 中子活化器
    val NeutronActivatorTooltips = ComponentListSupplier {
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

        section(MainFunction)
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
        increase("电压等级每高出LV1级，每次处理次数x3" translatedTo "Voltage tier above LV1 increases the number of processes x3")
        info("玻璃等级限制了电压等级" translatedTo "Glass tier limits voltage tier")
        command("运行前需设置电路，1号电路为非敌对生物，2号为敌对生物" translatedTo "Circuit must be set up before running; Circuit 1 is for non-hostile mobs, 2 is for hostile mobs")
        content("如果在机器GUI内放置了电动刷怪笼则只会刷出刷怪笼里的内容" translatedTo "If an electric spawner is placed in the machine GUI, only the contents of the spawner will spawn")
        content("只会使用检测到的第一把武器去尝试击杀其中的生物" translatedTo "Will only use the first weapon detected to try to kill the creature inside")
        info("安装输出仓后输出经验" translatedTo "Outputs XP after installing output hatch")
    }

    // 基岩钻机
    val BedrockDrillingRigTooltips = ComponentListSupplier {
        setTranslationPrefix("bedrock_drilling_rig")

        section(RunningRequirements)
        command("需要基岩在钻头下方" translatedTo "Requires bedrock below the drill head")
        decrease("每次运行有10%概率破坏基岩" translatedTo "Each run has 10% chance to destroy bedrock")
    }

    // 创造之门
    val DoorOfCreateTooltips = ComponentListSupplier {
        setTranslationPrefix("door_of_create")

        section(RunningRequirements)
        command("在主世界提供MAX级电压" translatedTo "Provides MAX tier voltage in the main world")
        command("设置电路为1开始运行" translatedTo "Set circuit to 1 to start running")
    }

    // 寒冰冷冻机
    val ColdIceFreezerTooltips = ComponentListSupplier {
        setTranslationPrefix("cold_ice_freezer")

        section(RunningRequirements)
        command("需每秒提供10x配方等级^2的§b液态冰§r" translatedTo "Requires to provide 10x(Recipe tier)² of §bLiquid Ice§r per second")
        command("雾化冷凝配方需要机器安装模块" translatedTo "Atomization condensation recipes require the machine to be equipped with modules")
    }

    // 烈焰高炉
    val BlazeBlastFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("blaze_blast_furnace")

        section(RunningRequirements)
        command("需每秒提供10x配方等级^2的§6液态烈焰§r" translatedTo "Requires to provide §b10x(Recipe tier)²§r of §6Liquid Blaze§r per second")
    }

    // PCB工厂
    val PCBFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("pcb_factory")

        section(RunningRequirements)
        command("使用纳米蜂群引导结构等级" translatedTo "Use nanites to guide structure level")
        info("金：1，山铜：2，末影素：3" translatedTo "Gold: 1, Orichalcum: 2, Enderium: 3")
    }

    // 进阶装配线
    val AdvancedAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_assembly_line")

        section(RunningRequirements)
        ok("可以使用更大的输入总线" translatedTo "Can use larger input buses")
        command("需要保证每片的物品与配方对应" translatedTo "Must ensure each item corresponds to the recipe")
        command("只能使用数据靶仓" translatedTo "Only data target chambers can be used")
    }

    // 方块转换室
    val BlockConversionRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("block_conversion_room")

        highlight("每秒随机转化机器内部方块" translatedTo "Randomly converts blocks inside the machine every second")

        section("电压等级加成" translatedTo "Voltage Tier Bonus")
        increase("每高出MV1级，转换方块数量+4" translatedTo "For each tier above MV1, block conversion +4")
        important("不会重复转换同一方块" translatedTo "Will not repeatedly convert the same block")
    }

    // 大型方块转换室
    val LargeBlockConversionRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("large_block_conversion_room")

        highlight("每秒随机转化机器内部一个方块" translatedTo "Randomly converts one block inside the machine every second")

        section("电压等级加成" translatedTo "Voltage Tier Bonus")
        increase("每高出MV1级，转换方块数量+64" translatedTo "For each tier above MV1, block conversion +64")
        important("不会重复转换同一方块" translatedTo "Will not repeatedly convert the same block")
    }

    // 宇宙探测器地面接收单元
    val SpaceProbeSurfaceReceptionTooltips = ComponentListSupplier {
        setTranslationPrefix("space_probe_surface_reception")

        section(RunningRequirements)
        important("只能运行在空间站" translatedTo "Can only operate on space station")

        section("戴森球连接" translatedTo "Dyson Sphere Connection")
        content("自动连接星系内未使用的戴森球" translatedTo "Automatically connects to unused Dyson spheres in the galaxy")
        increase("根据戴森球模块数量提升产出" translatedTo "Increases production based on Dyson sphere module count")
        ok("该操作不会损坏戴森球" translatedTo "This operation will not damage the Dyson sphere")
    }

    // 鸿蒙之眼
    val EyeOfHarmonyTooltips = ComponentListSupplier {
        setTranslationPrefix("eye_of_harmony")

        highlight("创造微缩宇宙并获取资源" translatedTo "Creates a mini-universe and gathers resources inside") { rainbowSlow() }

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

        section(RunningRequirements)
        command("需要阳光才能运行" translatedTo "Requires sunlight to operate")
        decrease("太阳光照不足时速度减缓" translatedTo "Speed slows down when sunlight is insufficient")
    }

    // 蜂群之心
    val SwarmCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("swarm_core")

        section(MainFunction)
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

        highlight("发射卫星，带回星球数据" translatedTo "Launch a satellite and bring back planet data")
    }

    // 原木拟生场
    val TreeGrowthSimulatorTooltips = ComponentListSupplier {
        setTranslationPrefix("tree_growth_simulator")

        section(RunningRequirements)
        command("需要安装伐木工具，仅支持GT工具" translatedTo "Requires GT-compatible tree cutting tool")
        content("根据工具类型和品质决定产出和效率" translatedTo "Output and efficiency determined by tool type and quality")
    }

    // 大型温室
    val LargeGreenhouseTooltips = ComponentListSupplier {
        setTranslationPrefix("large_greenhouse")

        section(RunningRequirements)
        ok("可以培育树木和一般作物" translatedTo "Can cultivate trees and general crops")
        ok("无需阳光就能运行" translatedTo "Can operate without sunlight")
    }

    // 雕刻中心
    val CarvingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("carving_center")

        section(RunningRequirements)
        command("根据全部电路之和决定输出" translatedTo "Output determined by the sum of all circuits")
        increase("电压等级每高出LV 1级，最大并行数×4" translatedTo "Each tier above LV multiplies max parallel by 4")
    }

    // BOSS召唤器
    val BossSummonerTooltips = ComponentListSupplier {
        setTranslationPrefix("boss_summoner")

        highlight("电力与反应核的作用" translatedTo "Electricity and Reactor Core Function")
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

        highlight("为无线电网提供容量支持" translatedTo "Provides capacity support to the wireless grid")

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

        section(RunningRequirements)
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

        highlight("每秒产出当前世界的全部矿石65536份" translatedTo "Produces a total of 65536 ores from the current world per second")
    }

    // 进阶无尽钻机
    val AdvancedInfiniteDrillerTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_infinite_driller")

        highlight("利用维度技术和坚不可摧的钻头无情的抽取星球的每一分血液" translatedTo "Using dimensional technology and indestructible drills, they relentlessly extract every drop of blood from the planet.")

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

        section(RunningRequirements)
        command("需要最低30720EU/t的功率" translatedTo "Requires minimum 30720 EU/t")
    }

    // 纳米集成加工中心
    val MATERIAL_MAP: ImmutableMap<String?, Float?> = ImmutableMap.of<String?, Float?>(
        "gtceu.iron", 1.0f,
        "gtceu.iridium", 1.1f,
        "gtocore.orichalcum", 1.2f,
        "gtocore.infuscolium", 1.3f,
        "gtocore.draconium", 1.4f,
        "gtocore.cosmic_neutronium", 1.5f,
        "gtocore.eternity", 1.6f,
    )
    val NanitesIntegratedProcessingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("nanites_integrated_processing_center")

        section(RunningRequirements)
        command("安装对应模块解锁对应配方" translatedTo "Install the corresponding module to unlock the corresponding recipe")
        increase("主机中放入纳米蜂群可减少污染概率" translatedTo "Placing nanites in the host can reduce pollution probability")
        content("每放入一个纳米蜂群，污染概率减少数如下所示" translatedTo "Each nanite placed reduces pollution probability as follows")
        MATERIAL_MAP.forEach { (material: String?, reduction: Float?) ->
            info(
                Component.translatable("material.$material").toComponentSupplier() +
                    (": -$reduction%").toLiteralSupplier(),
            )
        }
    }

    // 虚空采矿机
    val VoidMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("void_miner")

        section(RunningRequirements)
        command("需要输入1B钻井液和最低1920EU/t的功率" translatedTo "Requires 1B drilling fluid and minimum 1920 EU/t")

        section("产出机制" translatedTo "Output Mechanism")
        function("按维度随机选取4种矿石输出" translatedTo "Randomly selects 4 ore types based on dimension")
        increase("电压等级决定单次最大输出数量" translatedTo "Voltage tier determines max output quantity per cycle")
        increase("电流决定并行数" translatedTo "Amperage determines parallel count")
    }

    // 精密组装机
    val PrecisionAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembler")

        section(RunningRequirements)
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

        highlight("一步完成矿石处理" translatedTo "Completes ore processing in one step")

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

    val LargeSteamSolarBoilerTooltips = ComponentListSupplier {
        setTranslationPrefix("large_steam_circuit_assembler")

        section("蒸汽产出" translatedTo "Steam Production")
        content("根据集热管数量决定蒸汽产量" translatedTo "Steam production determined by number of collector tubes")
        important("只能在太阳下工作" translatedTo "Can only operate under the sun")

        section("可用大小" translatedTo "Usable Size")
        command("最小：5x5" translatedTo "Minimum: 5x5")
        command("最大：127x127" translatedTo "Maximum: 127x127")
    }

    // 部件组装机
    val ComponentAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("component_assembler")

        section(RunningRequirements)
        important(ComponentSlang.RecipeLevelBelow(GTValues.IV))
        increase(
            ("升级结构后支持到" translatedTo "After upgrading the structure, it supports tier ") +
                GTValues.VNF[GTValues.UV].toLiteralSupplier(),
        )
    }

    // 蒸汽搅拌机
    val SteamMixerTooltips = ComponentListSupplier {
        setTranslationPrefix("steam_mixer")

        section(RunningRequirements)
        error("无法处理流体配方" translatedTo "Cannot process fluids")
    }

    // 跃进一号高炉
    val LeapForwardOneBlastFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("leap_forward_one_blast_furnace")

        highlight("我们走在大路上！" translatedTo "We're on the road!")

        section(ComponentSlang.EfficiencyBonus)
        content("随着配方运行，温度升高" translatedTo "As the recipe operating, the temperature increases")
        increase("下次配方获得 400 / 温度 的时间减免" translatedTo "Next recipe gains duration reduction of 400 / temperature")
        danger(ComponentSlang.BeAwareOfBurn)
    }

    // 中子旋涡
    val NeutronVortexTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_vortex")

        section(RunningRequirements)
        ok("运行时不消耗中子动能" translatedTo "Does not consume neutron kinetic energy while running.")
        increase("安装附属结构后可开启能源转换模式，消耗电力自动适应配方的中子动能" translatedTo "After installing auxiliary structures, you can enable energy conversion mode, which automatically adapts the neutron kinetic energy consumption based on the recipe.")
    }

    // 微生物之主
    val MicroorganismMasterTooltips = ComponentListSupplier {
        setTranslationPrefix("microorganism_master")

        section(RunningRequirements)
        ok("无视辐射与洁净要求" translatedTo "Ignores radiation and cleanliness requirements")
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

        highlight("工业级浮游选矿池" translatedTo "Industrial Flotation Mining Pool")
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
        increase("连接的模块将获得0.707倍耗时的速度加成" translatedTo "Connected modules will receive a 0.707x Duration speed bonus")
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

        highlight(ComponentSlang.PurifyLevel(1))

        section(RunningRequirements)
        content("处理一定水量后过滤器堵塞" translatedTo "Filter blocks clog after processing certain water volume")
        command("需输入空气(1-8KB) + 水(200-300B)进行反冲洗" translatedTo "Requires air (1-8KB) + water (200-300B) for backflushing")
        content("反冲洗时输出废料" translatedTo "Outputs waste during backflushing")

        section(ComponentSlang.OutputProbability)
        info("基础概率: 70%" translatedTo "Base probability: 70%")
        increase("输入少量同等级净化水可提升15%" translatedTo "Adding a small amount of purified water of the same grade increases probability by 15%")
        increase("输入更高等级净化水每级额外增加5%，最高4级达到100%" translatedTo "Each higher grade of purified water adds 5% per level, up to 4 levels maximum for 100% probability")
    }

    // 臭氧净化装置
    val OzonationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ozonation_purification_unit")

        highlight(ComponentSlang.PurifyLevel(2))

        section(RunningRequirements)
        content("臭氧消耗量=输入水量/10,000mB" translatedTo "Ozone consumption = input water amount / 10,000mB")
        error(("如果输入口含有超过1,024B的臭氧气体，将发生" translatedTo "If the input contains more than 1,024B of ozone gas, it will cause an ") + ComponentSlang.Explosion)

        section(ComponentSlang.OutputProbability)
        info("臭氧气体在0-1,024B范围内的产出概率为0-80%" translatedTo "Output probability ranges from 0-80% based on ozone gas amount (0-1,024B)")
        increase("输入少量同等级净化水可提升15%" translatedTo "Adding a small amount of purified water of the same grade increases probability by 15%")
        increase("输入更高一级净化水可额外提升5%" translatedTo "Adding a higher grade of purified water provides an additional 5% increase")
    }

    // 絮凝净化装置
    val FlocculationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("flocculation_purification_unit")

        highlight(ComponentSlang.PurifyLevel(3))

        section(RunningRequirements)
        command("提供聚合氯化铝以进行操作" translatedTo "Provide Polymeric Aluminum Chloride for operation.")
        content("输出可循环利用的絮凝废液" translatedTo "Outputs recyclable Flocculent Waste Liquid.")
        content("在操作过程中，将消耗输入仓中的所有聚合氯化铝" translatedTo "During operation, all Polymeric Aluminum Chloride in the input chamber will be consumed.")

        section(ComponentSlang.OutputProbability)
        increase("每消耗100,000mB聚合氯化铝，成功率额外增加10.0%" translatedTo "For every 100,000mB of Polymeric Aluminum Chloride consumed, success rate increases by 10.0%.")
        decrease("如果提供的液体总量不是100,000mB的倍数，则应用成功率惩罚：" translatedTo "If total liquid provided is not a multiple of 100,000mB, apply success rate penalty:")
        info("成功率 = 成功率 * 2 ^ (-10 * 溢出比率)" translatedTo "Success Rate = Success Rate * 2 ^ (-10 * Overflow Ratio)")
    }

    // pH中和净化装置
    val PHNeutralizationPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("ph_neutralization_purification_unit")

        highlight(ComponentSlang.PurifyLevel(4))

        section(RunningRequirements)
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

        highlight(ComponentSlang.PurifyLevel(5))

        section(RunningRequirements)
        command("完成加热周期：先加热水至10,000K以上，再冷却至10K以下" translatedTo "Complete heating cycle: first heat Water above 10,000K, then cool below 10K.")
        content("配方开始时初始温度重置为298K" translatedTo "Initial temperature reset to 298K at recipe start.")
        content("每秒最多消耗10mB氦等离子体和100mB液氦" translatedTo "Consumes up to 10mB Helium Plasma and 100mB Liquid Helium per second.")

        section(ComponentSlang.OutputProbability)
        increase("每完成一个加热周期，成功率增加33%" translatedTo "For each completed heating cycle, success rate increases by 33%.")

        section("温度调节" translatedTo "Temperature Regulation Mechanism")
        command("每消耗1mB氦等离子体：温度升高80-120K" translatedTo "Each 1mB Helium Plasma consumed: temperature +80-120K")
        command("每消耗1mB液氦：温度降低4-6K" translatedTo "Each 1mB Liquid Helium consumed: temperature -4-6K")
        error("温度达到12,500K：配方失败并输出超临界蒸汽" translatedTo "If temperature reaches 12,500K: recipe fails and outputs Supercritical Steam")
    }

    // 高能激光净化装置
    val HighEnergyLaserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("high_energy_laser_purification_unit")

        highlight(ComponentSlang.PurifyLevel(6))

        section(RunningRequirements)
        command("在操作过程中，需要更换透镜仓内的透镜" translatedTo "During operation, you need replace the Lens in the lens chamber.")
        content("当当前透镜需要更换时，多方块结构将通过透镜指示仓输出信号" translatedTo "When the current Lens needs to be replaced, the multi-block structure will output a signal through the Lens indicator chamber.")
        content("透镜更换请求将在6到12秒的随机间隔内出现" translatedTo "Lens replacement requests will occur at random intervals between 6 and 12 seconds.")
        content("需要在信号输出后的4秒内更换透镜" translatedTo "The lens must be replaced within 4 seconds of the signal output.")
        content("透镜顺序依次为红/橙/棕/黄/绿/青/蓝/紫/品红/粉" translatedTo "The lens order is Red/Orange/Brown/Yellow/Green/Cyan/Blue/Purple/Magenta/Pink.")
        content("透镜需求可在GUI内查看" translatedTo "The requirements for Lens can be viewed in the GUI.")

        section(ComponentSlang.OutputProbability)
        increase("每次成功更换后运行4秒将成功率提高10%" translatedTo "Each successful replacement followed by 4 seconds of operation increases the success rate by 10%.")
    }

    // 残余污染物脱气净化装置
    val ResidualDecontaminantDegasserPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("residual_decontaminant_degasser_purification_unit")

        highlight(ComponentSlang.PurifyLevel(7))

        section(RunningRequirements)
        content("要成功完成配方，需要根据要求输入材料" translatedTo "To successfully complete the recipe, materials must be inputted as required.")
        content("操作开始时，脱气控制仓将输出红石信号，机器每秒将消耗全部输入的材料" translatedTo "At the operation start, the degassing control chamber will output a redstone signal, and the machine will consume all input materials every second.")
        content("红石信号与需求相对应" translatedTo "The redstone signal corresponds to the demand.")

        section("信号需求" translatedTo "Signal Requirement")
        command("1, 3, 5, 7, 9：通过惰性气体进行臭氧曝气" translatedTo "1, 3, 5, 7, 9: Ozone aeration via Inert Gases")
        info("对应 10,000mB氦气 / 8,000mB氖气 / 6,000mB氩气 / 4,000mB氪气 / 2,000mB氙气" translatedTo "Which is 10,000mB Helium / 8,000mB Neon / 6,000mB Argon / 4,000mB Krypton / 2,000mB Xenon")
        command("2, 4, 6, 8, 10：超导去离子" translatedTo "2, 4, 6, 8, 10: Superconductive deionization")
        info("需要输入1,000mB对应IV，LuV，ZPM，UV，UHV的液态超导" translatedTo "Needs input of 1,000mB of liquid superconductors corresponding to IV, LuV, ZPM, UV, UHV.")
        command("11, 13, 15：引力生成差异真空提取" translatedTo "11, 13, 15: Gravitational Differential Vacuum Extraction")
        info("需要输入2,000mB液态安普洛" translatedTo "Requires input of 2,000mB Liquid Amprosiums.")
        command("12, 14：塞尔多尼安沉淀过程" translatedTo "12, 14: Seldenian precipitation process")
        info("不输入任何东西" translatedTo "Do not input anything.")
        command("0：机器过载" translatedTo "0: Machine overload")
        info("在罕见情况下，机器可能会过载并且不会输出任何控制信号" translatedTo "In rare situations, the machine may overload and not output any control signals.")
        info("为防止机器损坏，输入10,000mB液氦" translatedTo "To prevent machine damage, input 10,000mB Liquid Helium.")
        error("输入信号未请求的任何流体将始终导致配方失败" translatedTo "Any liquid not requested by the input signal will always cause the recipe to fail.")
    }

    // 绝对重子完美净化装置
    val AbsoluteBaryonicPerfectionPurificationUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("absolute_baryonic_perfection_purification_unit")

        highlight(ComponentSlang.PurifyLevel(8))

        section(RunningRequirements)
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

        section("通过电路选择工作模式" translatedTo "Select the operating mode through the circuit")
        command("电路 1：物品解构" translatedTo "Circuit 1: Item Deconstruction")
        info("需要输入 书 铭刻之布 摘除符石" translatedTo "Requires input Book  Affix Canvas  Sigil of Withdrawal")
        command("电路 2：物品 + 附魔 解构" translatedTo "Circuit 2: Item + Enchantments Deconstruction")
        info("需要输入 铭刻之布 摘除符石" translatedTo "Requires input Affix Canvas  Sigil of Withdrawal")
        command("电路 3：物品 + 刻印 解构" translatedTo "Circuit 3: Item + Affixes Deconstruction")
        info("需要输入 书 摘除符石" translatedTo "Requires input Book  Sigil of Withdrawal")
        command("电路 4：物品 + 附魔 + 刻印 解构" translatedTo "Circuit 4: Item + Enchantments + Affixes Deconstruction")
        info("需要输入 摘除符石" translatedTo "Requires input Sigil of Withdrawal")
        command("电路 5：附魔精粹合成附魔书" translatedTo "Circuit 5: Essence crafting Enchanted Book")
        info("需要输入一本书，消耗附魔精粹和魔力合成" translatedTo "Need to input a book, consume enchantment essence and magic to craft")
        command("电路 6：附魔书合并" translatedTo "Circuit 6: Enchantment Enchanted Book Merge")
        info("消耗魔力合成，会输出额外的书" translatedTo "Consume magic power to synthesize, and output additional books")
        command("电路 7：刻印精粹合成铭刻之布" translatedTo "Circuit 7: Affix Enchanted Book Merge")
        info("消耗魔力合成，需要一个铭刻之布" translatedTo "Consume magic power to synthesize, and requires an Affix Canvas")
        command("电路 8：宝石合并" translatedTo "Circuit 8: Gem Merge")
        info("使用同级的珍宝材料和宝石粉合并宝石" translatedTo "Use the same level of rarity materials and gem dust to merge gems")
        command("电路 9：强行附魔" translatedTo "Circuit 9: Forced enchantment")
        info("消耗魔力强行将附魔书上的附魔添加到物品上" translatedTo "Consume magic power to forcibly adds the enchantment from the enchanted book to the item")
        command("电路 10：强行刻印" translatedTo "Circuit 10: Forced add affixes")
        info("消耗魔力强行将铭刻之布上的刻印添加到物品上" translatedTo "Consume magic power to forcibly adds the affixes from the affix canvas to the item")
        command("电路 11：强行修改物品稀有度" translatedTo "Circuit 11: Forcefully modify item rarity")
        info("消耗魔力，珍宝材料和新生符文强行改变稀有度" translatedTo "Consume magic power and enter rarity material and sigil of rebirth to forcibly change the rarity")
        command("电路 12：强行添加镶孔" translatedTo "Circuit 12: Forced addition of sockets")
        info("消耗魔力，镶孔符文强行添加镶孔" translatedTo "Consume magic power and enter sigil of socketing to forcibly addition of sockets")
        command("电路 13：强行镶嵌宝石" translatedTo "Circuit 13: Forced gem inlay")
        info("消耗魔力，强行将宝石镶嵌到物品上" translatedTo "Consume magic power to forcibly inserting gems into items")
    }

    // 炼金装置
    val AlchemicalDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("alchemical_device")
        add("炼金是一个神秘的过程" translatedTo "Alchemy is a mysterious process") { gold() }
        add("物质在连续不断的转换中获得升华" translatedTo "Matter attains sublimation through continuous transformation") { gold() }
        add("部分配方产出概率随运行次数增长" translatedTo "§7The probability of partial recipe output increases with the number of runs") { gray().italic() }
    }

    // 炼金锅
    val AlchemyCauldronTooltips = ComponentListSupplier {
        setTranslationPrefix("alchemy_cauldron")

        command(ComponentSlang.TemperatureMax(1600))
        important("不要用它来做饭" translatedTo "Do not use it for cooking food")
    }

    // 大型炼金装置补充
    val LargeAlchemicalDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("large_alchemical_device")

        section(ComponentSlang.AfterModuleInstallation)
        increase("激活完美嬗变模式" translatedTo "Activate perfect transmutation mode")
        increase("获得0.01x耗时减免" translatedTo "Gain 0.01x Duration reduction")
    }

    // 精灵交易所
    val ElfExchangeMachine = ComponentListSupplier {
        setTranslationPrefix("elf_exchange_machine")

        section("下界模式" translatedTo "Nether Mode")
        info("在下界中工作，且设置电路时，交易对象不再是亚尔夫海姆世界的居民" translatedTo "When working in the Nether, and setting the circuit meta, the trading partner is no longer a resident of Alfheim.")
        info("而是猪灵堡垒中的§6猪灵§r" translatedTo "Instead, it is the §6Piglins§r in the Bastion Remnants.")
        info("每一笔交易将花费§e金锭§r作为交易货币" translatedTo "Each trade will cost §eGold Ingots§r as the trading currency.")
        command("设置§b2号电路§r时，将过滤交易物品，不允许§b装备/药水/附魔书§r" translatedTo "When setting §bCircuit 2§r, it will filter traded items, disallowing §bEquipment/Potions/Enchanted Books§r.")
        important("此法过滤的物品仍然需要支付§e金锭§r" translatedTo "Items filtered by this method still require payment in §eGold Ingots§r.")
    }

    // 快中子增殖堆
    val FastNeutronBreederTooltips = ComponentListSupplier {
        setTranslationPrefix("fast_neutron_breeder")

        section("运行机制" translatedTo "Operating Mechanism")
        command("最高支持2048并行，无法通过其他方式加速" translatedTo "Supports up to 2048 parallel, cannot be accelerated by other means")
        command("配方需满足最低中子通量" translatedTo "Recipe requires minimum neutron flux")
        content("中子通量可通过输入中子源或配方运行时增加" translatedTo "Neutron flux can be increased by inputting neutron sources or during recipe operation")
        command("中子通量越高，堆升温速率越大" translatedTo "The higher the neutron flux, the faster the pile heats up")
        info("输入石墨粉可吸收中子通量" translatedTo "Inputting Graphite Dust can absorb neutron flux")
        info("输入冷却剂可降低温度" translatedTo "Inputting coolant can lower the temperature")
        error("温度超过2098K时配方失败，输出(一点可怜的)核废料，燃料组件方块全部融毁" translatedTo "If the temperature exceeds 2098K, the recipe fails, outputs waste, and all fuel component blocks melt.")

        section("配方相关" translatedTo "Recipe Related")
        command("输入：增殖棒与对应元素粉，不同配方需不同中子通量" translatedTo "Input: Breeding Rods and corresponding Element Dust, different recipes require different neutron flux")
        content("输出：枯竭增殖棒" translatedTo "Output: Depleted Breeding Rods")
        info("实际并行越大，运行时间越短，公式：T = t * (0.9 - (当前中子通量 - 需要的中子通量) / 10MeV)^0.5" translatedTo "The larger the actual parallelism, the shorter the running time, formula: T = t * (0.9 - (current neutron flux - required neutron flux) / 10MeV)^0.5")

        section("数值机制" translatedTo "Numerical Mechanism")
        function("消耗中子源提供初始通量：锑-铍10keV，钚-铍100keV，锎-252 1MeV" translatedTo "Consume neutron sources to provide initial flux: Sb-Be 10keV, Pu-Be 100keV, Cf-252 1MeV")
        function("中子通量每秒减少10keV" translatedTo "Neutron flux decreases by 10keV per second")
        function("小撮/小堆/石墨粉分别降低0.1/0.25/1MeV" translatedTo "Small Pile/Big Pile/Graphite Dust reduce by 0.1/0.25/1MeV respectively")
        function("中子通量为E（keV）时，在主机内放入N个铱中子反射板后，中子通量每秒增加 (EN)^0.5 keV" translatedTo "When neutron flux is E (keV), after placing N Iridium Neutron Reflectors in the mainframe, neutron flux increases by (EN)^0.5 keV per second")
        info("初始温度298K，临界点2098K" translatedTo "Initial temperature 298K, critical point 2098K")
        error("经过计算，当中子动能在7MeV以上时，堆温每秒将上升超过1800K，足以在一秒内达到临界点" translatedTo "According to calculations, when neutron kinetic energy is above 4.5keV, the pile temperature will rise by 1800K per second, enough to reach the critical point in one second")
        function("每秒产热公式：H=K×1.27×(E×10)^1.88，结果向上取整" translatedTo "Heat generation formula per second: H=K×1.27×(E×10)^1.88, result rounded up")
        function("冷却液系数(K/mB/s)：蒸馏水1，液氮4，液氦80" translatedTo "Coolant coefficients(K/mB/s): Distilled Water 1, Liquid Nitrogen 4, Liquid Helium 80")
        content("冷却后分别输出蒸汽、气态氮、气态氦" translatedTo "Outputs Steam, Gaseous Nitrogen, Gaseous Helium respectively after cooling")
        command("每秒将消耗全部输入的中子调节剂/冷却剂" translatedTo "Will consume all neutron moderators/coolants input per second")
        function("每秒温度变化：ΔT=H-C×M，结果向下取整" translatedTo "Temperature change per second: ΔT=H-C×M, result rounded down")
    }

    // 燃料电池发电机
    val FuelCellGeneratorTooltips = ComponentListSupplier {
        setTranslationPrefix("fuel_cell_generator")

        info("将燃料在专用电解液中‘燃烧’以转移获取400%燃料效率级别的能量" translatedTo "‘Burn’ fuel in a special electrolyte to transfer and obtain energy at 400% fuel efficiency level")
        info("或者将储存在电解液中的能量释放出来" translatedTo "Or release the energy stored in the electrolyte")

        section("工作模式" translatedTo "Operating Modes")
        function("模式一：输入燃料，消耗少量电能将释能电解液转化为同数量的储能电解液" translatedTo "Mode 1: Input fuel, consume a small amount of EU to convert Discharged Electrolyte into the same amount of Charged Electrolyte")
        command("需要等量的阴极液与阳极液来吸收燃料中的能量" translatedTo "Requires equal amounts of Catholyte and Anolyte to absorb the energy from the fuel")
        ok("使用的电极膜每高一级，燃料效率额外×1.25" translatedTo "Each higher tier of Electrode Membrane used multiplies fuel efficiency by an additional ×1.25")
        info("并行数 = min(输入的电解液数量, 当前燃料可转化的电解液数量)" translatedTo "Parallel = min(input electrolyte amount, amount of electrolyte convertible by current fuel)")
        info("耗能 = 1EU × 并行数" translatedTo "EU cost = 1EU × parallel")
        function("模式二：能量交换，将A类储能电解液的能量转移给B类释能电解液" translatedTo "Mode 2: Energy Exchange, transfers the energy from Type A Charged Electrolyte to Type B Discharged Electrolyte")
        command("对于输入的两种电解液，均需要等量的阴极液与阳极液" translatedTo "Both input electrolytes require equal amounts of Catholyte and Anolyte")
        info("产物为两种电解液交换能量状态后的结果" translatedTo "The output is the result of the two electrolytes swapping their energy states")
        info("并行数仅受限于输入流体的最低储量" translatedTo "Parallelism is only limited by the lowest amount of input fluid")
        info("此过程有能量损耗，具体效率由配方决定" translatedTo "This process involves energy loss; the specific efficiency is determined by the recipe")
        function("模式三：发电，将储能电解液转化为95%总量的释能电解液，并释放100%总量的能量" translatedTo "Mode 3: Power generation, convert Charged Electrolyte into 95% Discharged Electrolyte and release 100% of the energy")
        info("最大并行数 = 50, 即每秒最多消耗1B储能电解液" translatedTo "Max parallel = 50, i.e. up to 1B Charged Electrolyte can be consumed per second")
        info("每次发电输出的能量等于储能电解液储存的能量" translatedTo "Each operation outputs energy equal to the energy stored in the Charged Electrolyte")
        error("在该模式下， 不能安装能源仓" translatedTo "In this mode, Energy Input Hatches cannot be installed")
        command("所有工作模式均需要输入电极膜才可工作" translatedTo "All operating modes require input of Electrode Membranes to operate")
        command("且发电模式仅可使用特定电极膜" translatedTo "And the power generation mode can only use specific Electrode Membranes")
    }

    val SpaceStationTooltips = ComponentListSupplier {
        setTranslationPrefix("space_station")

        section(MainFunction)
        info("提供一个保护机器正常工作，免受太空辐射等复杂环境影响的空间" translatedTo "Provides a space that protects machines from complex environments such as space radiation")
        info("空间站内部自带供玩家呼吸的空气" translatedTo "The interior of the space station comes with air for players to breathe")
        info("且会根据内部安装的过滤器方块种类提供超净环境" translatedTo "And provides a super clean environment based on the types of filter blocks installed inside")
        info("可连接§b光伏阵列§r以获取太阳能发电" translatedTo "Can connect to §bPhotovoltaic Arrays§r to obtain solar power")

        section("建造要求" translatedTo "Construction Requirements")
        command("必须在太空中建造" translatedTo "Must be constructed in space")

        section(RunningRequirements)
        command("运行需要消耗1920EU/t的基础能量" translatedTo "Operation requires a base energy consumption of 1920EU/t")
        info("每10秒固定消耗：" translatedTo "Fixed consumption every 10 seconds:")
        important("15mB 蒸馏水" translatedTo "15mB Distilled Water")
        important("10mB 火箭燃料" translatedTo "15mB Rocket Fuel")
        important("100mB 空气" translatedTo "100mB Air")
        info("当拥有更多蒸馏水供给时，空间站会尝试每秒向连接的光伏阵列各输送§b8mB 蒸馏水§r以冷却其太阳能板" translatedTo "When more distilled water supply is available, the space station will attempt to supply each connected photovoltaic array with §b8mB Distilled Water§r per second to cool its solar panels")
        info("运行时每10秒将排出30mB废水" translatedTo "When operating, it will discharge 30mB of Waste Water every 10 seconds")
    }
    val LargeSpaceStationTooltips = ComponentListSupplier {
        setTranslationPrefix("large_space_station")

        section(MainFunction)
        highlight("提供一个保护机器正常工作，免受太空辐射等复杂环境影响的空间" translatedTo "Provides a space that protects machines from complex environments such as space radiation")
        highlight("空间站内部自带供玩家呼吸的空气" translatedTo "The interior of the space station comes with air for players to breathe")
        highlight("且自带无线电网中继器的功能，提供的电压根据安装的整体框架的等级而定" translatedTo "And comes with the function of a wireless radio network repeater, providing voltage based on the level of the installed mainframe")
        highlight("可以向外侧安装拓展舱体以增加内部空间/更多功能" translatedTo "Can install expansion modules on the outside to increase internal space/more functions")

        section("建造要求" translatedTo "Construction Requirements")
        command("必须在太空中建造" translatedTo "Must be constructed in space")
        command("必须安装§b核心舱§r，且只能安装一个" translatedTo "Must install §bCore Module§r, and only one can be installed")
        command("必须在固定位置安装§b拓展舱体§r" translatedTo "Must install §bExpansion Modules§r in fixed positions")
        important("安装的具体位置可在机器GUI内点击左下高亮按钮以在世界中高亮显示" translatedTo "The specific installation position can be highlighted in the world by clicking the highlight button at the bottom left in the GUI")

        section(RunningRequirements)
        command("运行需要消耗7680EU/t的基础能量" translatedTo "Operation requires a base energy consumption of 1920EU/t")
        command("会根据安装的拓展舱体数量增加额外能耗以及资源消耗" translatedTo "Will increase additional energy consumption and resource consumption based on the number of expansion modules installed")
        info("每秒固定消耗：" translatedTo "Fixed consumption every second:")
        important("15mB * (1 + 拓展舱体数量) 蒸馏水" translatedTo "15mB * (1 + number of expansion modules) Distilled Water")
        important("10mB * (1 + 拓展舱体数量) 火箭燃料" translatedTo "15mB * (1 + number of expansion modules) Rocket Fuel")
        important("100mB * (1 + 拓展舱体数量) 空气" translatedTo "100mB * (1 + number of expansion modules) Air")
        info("能耗增加与拓展舱体的具体种类与数量有关" translatedTo "Energy consumption increase is related to the specific types and quantities of expansion modules")

        section("拓展舱体" translatedTo "Expansion Modules")
        info("§b核心舱§r：大型空间站的核心部分，必须安装且只能安装一个" translatedTo "§bCore Module§r: The core part of the large space station, must be installed and only one can be installed")
        info("§b衔接舱§r：用于连接核心舱/模块仓与其他拓展舱体" translatedTo "§bConnection Module§r: Used to connect the core module with other expansion modules")
        info("§b模块舱§r：提供额外的空间与功能" translatedTo "§bFunctional Module§r: Provides additional space and functions")
    }
    val SpaceStationWorkspaceExtensionTooltips = ComponentListSupplier {
        setTranslationPrefix("space_station_workspace_extension")

        section(MainFunction)
        info("提供额外的空间以容纳更多机器" translatedTo "Provides additional space to accommodate more machines")
        info("且可安装在§b大型空间站§r的任意一侧" translatedTo "And can be installed on any side of the §bLarge Space Station§r")
        important(("该模块为变长模块" translatedTo "This module is a variable-length module").scrollExotic())
        important("在搭建模块前请在机器GUI内点击左下调整器调整模块的长度" translatedTo "Before building the module, please click the adjuster in the bottom left of the machine GUI to adjust the length of the module")
    }
    val SpaceStationEnvironmentalMaintenanceModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("space_station_environmental_maintenance_module")

        section(MainFunction)
        highlight("给当前空间站里的所有机器提供超净环境" translatedTo "Provides a super clean environment for all machines in the current space station")
        info("提供的环境等级取决于安装的过滤器方块种类" translatedTo "The level of environment provided depends on the types of filter blocks installed")
        highlight(
            ("且不同于常规超净间，高级超净环境" translatedTo "And unlike conventional super clean rooms, advanced super clean environments") +
                ("向下兼容低级超净环境" translatedTo " are backward compatible with lower-level super clean environments").scrollOptical(),
        )
        highlight("提供无人机仓，可供无人机清理空间站内机器的垃圾，或自动维护机器" translatedTo "Provides a drone bay for drones to clean up machine waste or automatically maintain machines in the space station")
    }
    val NoExtensionAvailableTooltips = ComponentListSupplier {
        setTranslationPrefix("no_extension_available")
        error("该种类空间站舱段无法向外侧安装拓展舱体" translatedTo "This type of space station module cannot install expansion modules on the outside")
    }
    val RecipeExtensionTooltips = ComponentListSupplier {
        setTranslationPrefix("recipe_extension")
        section("配方拓展舱室" translatedTo "Recipe Extension Module")
        info("§6被动耗能§r由§b核心舱§r提供，而§6配方耗能§r需由安装在§b此舱§r的§d能源仓§r提供" translatedTo "§6Passive energy consumption§r is provided by the §bCore Module§r, while §6recipe energy consumption§r needs to be provided by the §dEnergy Input Hatch§r installed in the §bthis module§r")
        error("无核心舱连接时，无法运行配方" translatedTo "Cannot run recipes without a linked core module")
        highlight(
            ("当前空间站内如果安装有" translatedTo "If the current space station has installed") +
                ("空间站高能转换调配舱" translatedTo "Space Station High-Energy Conversion and Dispensing Module").scrollExotic(),
        )
        highlight("则解锁§d激光仓§r/§d超频仓§r/§d线程仓§r等高级舱体的使用权限" translatedTo "The use of advanced modules such as §dLaser Chamber§r/§dOverclocking Chamber§r/§dThread Chamber§r will be unlocked")
    }
    val CoreSpaceStationModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("core_space_station_module")
        section("工业空间站太空舱种类" translatedTo "Types of Industrial Space Station Modules")
        info(ComponentSlang.IsWhatTypeSpaceModule(ComponentSlang.CoreModuleSpaceModule))
        info(ComponentSlang.CanConnectToWhatTypeSpaceModule(listOf(ComponentSlang.ConjunctionModuleSpaceModule)))
    }
    val ConjunctionSpaceStationModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("conjunction_space_station_module")
        section("工业空间站太空舱种类" translatedTo "Types of Industrial Space Station Modules")
        info(ComponentSlang.IsWhatTypeSpaceModule(ComponentSlang.ConjunctionModuleSpaceModule))
        info(ComponentSlang.CanConnectToWhatTypeSpaceModule(listOf(ComponentSlang.ConjunctionModuleSpaceModule, ComponentSlang.FunctionModuleSpaceModule)))
    }
    val FunctionSpaceStationModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("function_space_station_module")
        section("工业空间站太空舱种类" translatedTo "Types of Industrial Space Station Modules")
        info(ComponentSlang.IsWhatTypeSpaceModule(ComponentSlang.FunctionModuleSpaceModule))
        info(ComponentSlang.CanConnectToWhatTypeSpaceModule(listOf(ComponentSlang.ConjunctionModuleSpaceModule)))
    }
    val SpaceDroneDockTooltips = ComponentListSupplier {
        setTranslationPrefix("space_drone_dock")
        section(MainFunction)
        increase("向太空发送无人机以收集宇宙尘埃" translatedTo "Send drones into space to collect cosmic dust")
        info("在不同的星系能够收集到不同种类的宇宙尘埃" translatedTo "Different types of cosmic dust can be collected in different galaxies")
        section(RunningRequirements)
        command("每次运行时，需要配方提供的无人机/电池有能量时才能运行" translatedTo "Each operation requires the drones/batteries provided by the recipe to have energy to operate")
        info("每次消耗其内部存储的全部电量" translatedTo "Consumes all the internal stored energy each time")
        increase("每消耗600,000EU，配方最大并行数+1" translatedTo "For every 600,000EU consumed, the maximum parallelism of the recipe +1")
        decrease("无人机/电池缺电时，配方无法运行" translatedTo "The recipe cannot run when the drone has no power")
    }
    val SpaceStationEnergyConversionModuleTooltips = ComponentListSupplier {
        setTranslationPrefix("space_station_energy_conversion_module")
        section(MainFunction)
        highlight("安装后，空间站内的其他拓展舱体将能够使用§d激光仓§r/§d超频仓§r/§d线程仓§r等高级舱体" translatedTo "When installed, other expansion modules in the space station will be able to use advanced modules such as §dLaser Chamber§r/§dOverclocking Chamber§r/§dThread Chamber§r")
    }
}
