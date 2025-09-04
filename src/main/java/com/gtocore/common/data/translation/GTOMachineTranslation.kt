package com.gtocore.common.data.translation

import com.gtocore.api.lang.*
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.ComponentSlang.Circle
import com.gtocore.common.data.translation.ComponentSlang.OutTopic
import com.gtocore.common.data.translation.ComponentSlang.Star
import com.gtocore.common.data.translation.ComponentSlang.Tab
import com.gtocore.common.data.translation.ComponentSlang.Warning
import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.multiblock.storage.MultiblockCrateMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine

import net.minecraft.network.chat.Component

import com.google.common.collect.ImmutableMap

object GTOMachineTranslation : AutoInitialize<GTOMachineTranslation>() {
    val pattern = ("样板" translatedTo "Patterns").initialize()

    /*************************************************
     *           单方块机器和机器功能仓                *
     **************************************************/

    // 外置热源锅炉
    val BoilWaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("boil_water_machine")
        add("需要外部热源工作" translatedTo "Requires external heat source to operate") { aqua() }
        add(
            ("当蒸汽溢出后继续工作会" translatedTo "When steam overflows, continuing to work will ") + ("爆炸" translatedTo "explode").red()
                .bold(),
        ) { aqua() }
        add(
            ("可能发生爆炸的临界温度为" translatedTo "The critical temperature for explosion is ") + (BoilWaterMachine.DrawWaterExplosionLine.toLiteralSupplier()).red()
                .bold(),
        ) { aqua() }
    }

    // 加热器
    val HeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("heater_machine")
        add("通过燃烧对四周机器进行加热" translatedTo "Burning to heat up around machines") { aqua() }
        add(ComponentSlang.TemperatureMax(HeaterMachine.MaxTemperature))
        add("前方被阻挡后停止加热" translatedTo "Stop heating after front side is blocked.") { aqua() }
        add("根据温度发出红石信号" translatedTo "Emits redstone signal according to the temperature.") { aqua() }
        add(Star(1) + ("机器过热会" translatedTo "When machine is too hot, it will ") + ComponentSlang.Explosion) { aqua() }
        add(ComponentSlang.BewareOfBurns)
    }

    // 电力加热器
    val ElectricHeaterMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("electric_heater_machine")
        add("使用电力对四周机器进行加热" translatedTo "Use electricity to heat up around machines") { aqua() }
        add(ComponentSlang.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
        add(Star(1) + ("此机器不会爆炸" translatedTo "This machine will not explode")) { aqua() }
        add(ComponentSlang.BewareOfBurns)
    }

    // 区域破坏器
    val AreaDestructionToolsTooltips = ComponentListSupplier {
        setTranslationPrefix("area_destruction_tools")

        add("一键清空方块！！！" translatedTo "One click to clear the blocks!!!") { aqua() }
        add("无视一切条件！！！" translatedTo "Ignore all conditions!!!") { aqua() }
        add("注意爆破安全！！！" translatedTo "Pay attention to blasting safety!!!") { aqua() }
        add(Warning(1) + ("爆破时请保持机器加载" translatedTo "Keep the machine loaded while blasting").gold()) { white() }
        add(
            Star(1) + ("专业的世界摧毁者" translatedTo "Professional world destroyer").rainbowSlow().bold().italic(),
        ) { white() }
        add("向库存放入物品以切换模式" translatedTo "Add items to inventory to switch modes") { green() }
        add("- 放入 §e模具 (球)§r 为§6球模式§r" translatedTo "- Put into §eCasting Mold (Ball)§r for §6Ball mode§r") { gray() }
        add("- 放入 §e模具 (圆柱)§r 为§6圆柱模式§r" translatedTo "- Put into §eCasting Mold (Cylinder)§r for §6Casting mode§r") { gray() }
        add("- 放入 §e模具 (块)§r 为§6区块模式§r" translatedTo "- Put into §eCasting Mold (Block)§r for §6Chunk mode§r") { gray() }
        add("- 放入 §e奇点§r 为§6指定区域模式§r" translatedTo "- Put into §eSingularity§r for §6Designated Area mode§r") { gray() }
        add("-- §6指定区域模式§r中" translatedTo "-- In the §6Designated area mode§r") { gray() }
        add("-- 用两个§9坐标信息卡§r来确定区域" translatedTo "-- Use two §9Coordinate Card§r to determine the area") { gray() }
        add(Star(1) + ("向库存添加以下物品以提高爆炸当量：" translatedTo "Add the following items to inventory to increase explosive yield:")) { blue() }
        add(Star(1) + ("§c工业TNT§r/§c核弹§r/§c超能硅岩爆弹§r/§c轻子爆弹§r/§c量子色动力学爆弹§r" translatedTo "§cIndustrial TNT§r/§cNuke Bomb§r/§cNaquadria Charge§r/§cLeptonic Charge§r/§cQuantum Chromodynamic Charge§r")) { blue() }
    }

    // 转子仓
    val RotorHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("rotor_hatch")
        add(Warning(1) + ("转子所在平面3x3内只能存在一个转子仓" translatedTo "There can only be one rotor hatch in the 3x3 plane where the rotor is located").InfoWarning())
        add(Star(1) + ("不同类型的转子的效率，耐久不同" translatedTo "Different types of rotors have different efficiency and durability").InfoFunction())
    }

    // 催化剂仓
    val CatalystHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("catalyst_hatch")
        add(Star(1) + ("使用催化剂仓输入催化剂" translatedTo "Use catalyst hatch to input catalysts")) { aqua() }
        add(Tab(1) + ("可以不消耗催化剂，让催化剂重复使用" translatedTo "Can be used without consuming catalysts, allowing catalysts to be reused")) { gray() }
    }

    // ME存储访问仓
    val MEStorageAccessHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("me_storage_access_hatch")
        add(Star(1) + ("访问ME存储器内的存储" translatedTo "Access storage in ME storage")) { aqua() }
        add(Tab(1) + ("直接让ME线缆连上就好，不推荐无线连接" translatedTo "Directly let ME cable connect, not recommended to use wireless connection")) { gray() }
    }

    // 合成样板仓
    val MeCraftPatternHatchTooltips = ComponentListSupplier {
        setTranslationPrefix("me_craft_pattern_part_machine")
        add(Star(1) + ("合成样板仓用于存储合成样板" translatedTo "Synthesis template hatch is used to store synthesis templates")) { aqua() }
        add(Star(1) + ("配合超级分子装配室使用" translatedTo "use it Combined with Super Molecular Assembler")) { aqua() }
        add(ComponentSlang.Capacity(72.toString())) { aqua() }
    }

    // ME可编程样板总成
    val MeProgrammablePatternAssemblyTooltips = ComponentListSupplier {
        setTranslationPrefix("me_programmable_pattern_assembly")
        add(Star(1) + ("可以使用虚拟物品提供器进行编程" translatedTo "Can be programmed using virtual item provider")) { aqua() }
        add(Tab(1) + ("一台机器，一个总成搞定所有样板" translatedTo "One machine, one pattern hatch ,finished.")) { gray() }
    }

    // ME样板总成
    val MePatternHatchTooltips = { capacity: Int ->
        ComponentListSupplier {
            setTranslationPrefix("me_pattern_assembly")
            add(Star(1) + ("可以放入样板，并进行一键发配" translatedTo "Can put patterns and distribute them one click")) { aqua() }
            add(Tab(1) + ("样板间分别隔离，互不干扰" translatedTo "Patterns are isolated from each other, do not interfere with each other")) { gray() }
            add(Tab(1) + ("对着样板按鼠标中键可单独设置电路或者提供特别输入" translatedTo "Press the middle mouse button on the pattern to set the circuit or provide special input")) { gray() }
            add(OutTopic(1) + ("集成式的ME样板供应器" translatedTo "Integrated ME pattern provider")) { gold() }
            add(ComponentSlang.Capacity(capacity.toString())) { aqua() }
        }
    }.initialize()

    // ME自动连接
    val AutoConnectMETooltips = ComponentListSupplier {
        setTranslationPrefix("auto_connect_me")
        add(Star(1) + ("此机器可以自动连接ME无线网络" translatedTo "This machine can automatically connect to the ME Wireless network")) { aqua() }
        add(Tab(1) + ("按下Shift放置以自动连接收藏的网络" translatedTo "Press Shift to place to automatically connect to the collected network")) { gray() }
        add(Tab(1) + ("小心塞爆矿处！" translatedTo "Be careful to explode the ae storage! ")) { gray() }
    }

    /*************************************************
     *                 实用部件                       *
     **************************************************/

    // 性能监控器
    val PerformanceMonitorMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("performance_monitor_machine")
        add("能监测全部机器2秒内的平均延迟" translatedTo "Can monitor all machines' average delay within 2 seconds and support highlighting") { aqua() }
        add("右键点击机器以打开性能监测界面" translatedTo "Right click on the machine to open performance monitoring interface") { gray() }
    }

    // 监控器系列
    val monitor = { init: ComponentListSupplier.() -> Unit ->
        ComponentListSupplier {
            setTranslationPrefix("monitor")
            init()
            add(Star(1) + ("监控器系列" translatedTo "Monitor series")) { rainbowSlow() }
        }
    }

    // 基础监控器
    val BasicMonitorTooltips = monitor {
        add("是监控器的基础组成部分" translatedTo "Is the basic component of the monitor") { aqua() }
        add("可以与其他§6多个监控器§r系列相连" translatedTo "Can be connected to other §6multiple monitors§r series") { aqua() }
        add("多个监控器可以组成一个大屏" translatedTo "Multiple monitors can form a large screen") { aqua() }
        add("可以使用§d喷漆§r对监控器分组" translatedTo "Can use§d paint spray§r to group monitors") { aqua() }
        add(Star(1) + ("与§6监控器组件§r相连来显示不同信息" translatedTo "Use §6monitor components§r to display different information")) { aqua() }
    }

    // 监控器电网组件
    val MonitorPowerComponentTooltips = monitor {
        add("用于显示§6无线电网§r的数据在监控器上" translatedTo "Used to display§6 wireless energy grid data§r on the monitor") { aqua() }
    }

    // 监控器魔力组件
    val MonitorManaComponentTooltips = monitor {
        add("用于显示§6无线魔力网§r的数据在监控器上" translatedTo "Used to display§6 wireless mana grid data§r on the monitor") { aqua() }
    }

    // 监控器算力组件
    val MonitorComputingComponentTooltips = monitor {
        add("用于显示§6无线算力网§r的数据在监控器上" translatedTo "Used to display§6 wireless computing grid data§r on the monitor") { aqua() }
    }

    // 监控器ME吞吐量监控组件
    val MonitorMEThroughputComponentTooltips = monitor {
        add("用于显示ME中§6指定物品§r的吞吐量数据在监控器上" translatedTo "Used to display the throughput data of §6specified items §rin ME in the monitor") { aqua() }
    }

    // 监控器机器通用组件
    val MonitorMachineComponentTooltips = monitor {
        add("用于显示§6机器§r的通用数据在监控器上" translatedTo "Used to display the general data of §6machines§r on the monitor") { aqua() }
        add("例如耗电，产电，输入，输出等" translatedTo "For example, power consumption, power production, input, output, etc.") { aqua() }
        add("需要放入坐标信息卡" translatedTo "Need to put coordinate card") { aqua() }
    }

    // 监控器合成处理单元组件
    val MonitorCraftingComponentTooltips = monitor {
        add("用于显示§6ME合成处理单元§r的合成数据在监控器上" translatedTo "Used to display the synthesis data of §6crafting unit§r on the monitor") { aqua() }
    }

    // 监控器自定义文本组件
    val MonitorCustomTextComponentTooltips = monitor {
        add("用于显示§6自定义文本§r在监控器上" translatedTo "Used to display§6 custom text§r on the monitor") { aqua() }
        add("同一块监控器大屏的多个文本组件将被顺序显示" translatedTo "Multiple text components of the same block monitor screen will be displayed in sequence") { aqua() }
    }

    // 超立方体
    val HyperCubeMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("hyper_cube_machine")
        add(Star(1) + ("可以代理一个流体或物品存储器" translatedTo "Can proxy a fluid or item or both storage")) { InfoFunctionPrimary() }
        add("绑定某方块后，对此机器进行物品或流体操作视同对被绑定的方块操作" translatedTo "Bind a storage to this machine to operate it as if it were the bound storage") { InfoFunction() }
        add("此方块有升级版本" translatedTo "Has upgrade version") { InfoSupport() }
        add("右键点击以打开界面" translatedTo "Right click to open the interface") { InfoOperationKey() }
    }

    // 进阶超立方体
    val AdvancedHyperCubeMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("hyper_cube_machine")
        add(Star(1) + ("可以代理多个流体或物品存储器" translatedTo "Can proxy (a or multi) (fluid or item or both)storage")) { InfoFunctionPrimary() }
        add("绑定某方块后，对此机器进行物品或流体操作视同对被绑定的方块操作" translatedTo "Bind a storage to this machine to operate it as if it were the bound storage") { InfoFunction() }
        add("若绑定多个方块，则依序对他们操作" translatedTo "Operate them in order if bind multiple storages") { InfoFunction() }
        add("对装配线自动化很好用" translatedTo "Good for assembly line automation") { InfoSupport() }
        add("右键点击以打开界面" translatedTo "Right click to open the interface") { InfoOperationKey() }
    }

    /*************************************************
     *           小型多方块机器                      *
     **************************************************/

    // 不宜过长, 4行以内最佳
    // 尤其注意不可把机器性能描述得过于强大
    // 类GCYM机器(如大热解/裂化/电弧)不要写

    // 多方块板条箱
    val MultiblockCrateMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("multiblock_crate_machine")
        add("多方块箱子" translatedTo "Multiblock Crate") { aqua() }
        add("可以存储大量物品" translatedTo "Can store many many items") { aqua() }
        add("右键点击以打开界面" translatedTo "Right click to open the interface") { gray() }
        add(ComponentSlang.Capacity(MultiblockCrateMachine.Capacity.toString())) { aqua() }
    }

    // 等静压成型
    val IsostaticPressMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("isostatic_press_machine")
        add(("先进的材料学技术一直以来都是格雷科技公司的立身之本" translatedTo "Advanced materials technology has always been the foundation of GregTech Corp")) { aqua() }
        add("被广泛应用的先进工业陶瓷一直是公司的拳头产品" translatedTo "Advanced industrial ceramics widely used in various fields are the company's flagship products") { aqua() }
        add(("型号CML-202等静压成型机外表与百年前无异" translatedTo "Model CML-202 isostatic press looks no different from its predecessors a century ago")) { gray() }
        add("但其先进自动化成型技术和工艺早已不可同日而语" translatedTo "But its advanced automated forming technology and processes are incomparable") { gray() }
    }

    // 烧结炉
    val SinteringFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("sintering_furnace")
        add(
            ("作为陶瓷生产中的" translatedTo "As the ") + ("核心设备" translatedTo "core equipment").gold().bold(),
        ) { aqua() }
        add("格雷科技设计人员为这台烧结炉奋战了无数日夜" translatedTo "GregTech designers fought countless days and nights for this sintering furnace") { aqua() }
        add(("型号HCS-41烧结炉有着完美的成品率" translatedTo "Model HCS-41 sintering furnace has perfect finished product rate")) { gray() }
        add("生产出的优质陶瓷将成为工业帝国的坚固基石" translatedTo "The high-quality ceramics produced will become the solid foundation of industrial empire") { green() }
    }

    // 光伏电站 (PG-11)
    val PhotovoltaicPlant11Tooltips = ComponentListSupplier {
        setTranslationPrefix("photovoltaic_plant_11")
        add("格雷科技致力于能源获取形式的多样化" translatedTo "GregTech is committed to §ediversifying energy acquisition methods") { aqua() }
        add("§6太阳能光伏发电是公司的研究方向之一" translatedTo "§6Solar photovoltaic power generation§r is one of the company's research directions") { aqua() }
        add("§6型号PG-11光伏电站技术起初被员工试用" translatedTo "§6Model PG-11 photovoltaic plant technology§r was initially tested by employees") { gray() }
        add("由于复杂的生产材料要求和较差发电能力常被冷落" translatedTo "Often neglected due to complex material requirements and poor power generation") { gray() }
        add("技术人员偶然发现它能高效采集魔力" translatedTo "Technicians accidentally discovered it can §aefficiently collect mana") { green() }
        add("§6改进后的PMG-11以另一种身份被广泛使用" translatedTo "The §6improved PMG-11§r is widely used in another capacity") { green() }
        add(ComponentSlang.RecommendedUseAs(("生产魔力" translatedTo "mana production"))) { aqua() }
        add(Circle(1) + ("在机器内放置64朵太阳花以使机器不再发电，转而采集魔力" translatedTo "Place 64 dayblooms in the machine to stop power generation and start collecting mana")) { aqua() }
        add(Star(1) + ("§6魔力采集的高效设备" translatedTo "Efficient equipment for mana collection")) { aqua() }
    }

    // 光伏电站 (PG-12)
    val PhotovoltaicPlant12Tooltips = ComponentListSupplier {
        setTranslationPrefix("photovoltaic_plant_12")
        add("格雷科技致力于能源获取形式的多样化" translatedTo "GregTech is committed to §ediversifying energy acquisition methods") { aqua() }
        add("§6太阳能光伏发电是公司的研究方向之一" translatedTo "§6Solar photovoltaic power generation§r is one of the company's research directions") { aqua() }
        add("§6型号PG-12光伏电站技术起初被员工试用" translatedTo "§6Model PG-12 photovoltaic plant technology§r was initially tested by employees") { gray() }
        add("由于复杂的生产材料要求和较差发电能力常被冷落" translatedTo "Often neglected due to complex material requirements and poor power generation") { gray() }
        add("技术人员偶然发现它能高效采集魔力" translatedTo "Technicians accidentally discovered it can §aefficiently collect mana") { green() }
        add("§6改进后的PMG-12以另一种身份被广泛使用" translatedTo "The §6improved PMG-12§r is widely used in another capacity") { green() }
        add(ComponentSlang.RecommendedUseAs(("生产魔力" translatedTo "mana production"))) { aqua() }
        add(Circle(1) + ("在机器内放置64朵太阳花以使机器不再发电，转而采集魔力" translatedTo "Place 64 dayblooms in the machine to stop power generation and start collecting mana")) { aqua() }
        add(Star(1) + ("§6中级魔力采集设备" translatedTo "Intermediate mana collection equipment")) { aqua() }
    }

    // 光伏电站 (PG-13)
    val PhotovoltaicPlant13Tooltips = ComponentListSupplier {
        setTranslationPrefix("photovoltaic_plant_13")
        add("格雷科技致力于能源获取形式的多样化" translatedTo "GregTech is committed to §ediversifying energy acquisition methods") { aqua() }
        add("§6太阳能光伏发电是公司的研究方向之一" translatedTo "§6Solar photovoltaic power generation§r is one of the company's research directions") { aqua() }
        add("§6型号PG-13光伏电站技术起初被员工试用" translatedTo "§6Model PG-13 photovoltaic plant technology§r was initially tested by employees") { gray() }
        add("由于复杂的生产材料要求和较差发电能力常被冷落" translatedTo "Often neglected due to complex material requirements and poor power generation") { gray() }
        add("技术人员偶然发现它能高效采集魔力" translatedTo "Technicians accidentally discovered it can §aefficiently collect mana") { green() }
        add("§6改进后的PMG-13以另一种身份被广泛使用" translatedTo "The §6improved PMG-13§r is widely used in another capacity") { green() }
        add(ComponentSlang.RecommendedUseAs(("生产魔力" translatedTo "mana production"))) { aqua() }
        add(Circle(1) + ("在机器内放置64朵太阳花以使机器不再发电，转而采集魔力" translatedTo "Place 64 dayblooms in the machine to stop power generation and start collecting mana")) { aqua() }
        add(Star(1) + ("§6高级魔力采集设备" translatedTo "Advanced mana collection equipment")) { aqua() }
    }

    // 虚空流体钻机
    val VoidFluidDrillTooltips = ComponentListSupplier {
        setTranslationPrefix("void_fluid_drill")
        add(("§6虚空流体钻机§r§b是格雷科技在虚空领域的又一力作" translatedTo "§6Void Fluid Drill§r is another masterpiece of GregTech in the void field").InfoBrand())
        add(("它可以在虚空中钻取流体" translatedTo "It can drill fluids in the void").InfoBrand())
        add(Star(1) + ("放入维度数据，设置电路后即可获取钻出对应世界流体矿床的流体" translatedTo "Insert dimension data and set the circuit to obtain fluids from corresponding world fluid deposits").InfoFunctionPrimary()) { aqua() }
    }

    // 能量注入仪
    val EnergyInjectorTooltips = ComponentListSupplier {
        setTranslationPrefix("energy_injector")
        add("§e电池箱充电太慢？" translatedTo "§eBattery box charging too slow?") { yellow() }
        add("格雷科技隆重推出§6SCL-1000大型能量注入仪" translatedTo "GregTech proudly introduces the §6SCL-1000 large energy injector") { aqua() }
        add("§a全新设计超级快充系统" translatedTo "§aBrand new super fast charging system") { gray() }
        add("§6可为物品充电，还可消耗电力修复物品耐久" translatedTo "Can to charge items, Can consume electricity to repair item durability") { aqua() }
        add("§6\"充电1秒钟，工作一整年\"" translatedTo "§6\"Charge for 1 second, work for a whole year\"") { white() }
    }

    // 渔场
    val FishingFarmTooltips = ComponentListSupplier {
        setTranslationPrefix("fishing_farm")
        add("§e喜欢吃鱼？" translatedTo "§eLike eating fish?") { yellow() }
        add("§6AFFL-200智能大型渔场是舌尖上的格雷系列常客" translatedTo "§6AFFL-200 intelligent large fishing farm§r is a regular on GregTech cuisine series") { aqua() }
        add("强大的§e智能养殖系统带来强大产能" translatedTo "Powerful §eintelligent breeding system§r brings powerful productivity") { aqua() }
        add("能够满足整个分公司员工的水产食用需求" translatedTo "Can meet the entire branch office employees' §aaquatic food consumption needs") { gray() }
        add(Star(1) + ("§6水产品和工业原料的双重来源" translatedTo "Dual source of aquatic products and industrial materials")) { aqua() }
    }

    // 培养缸
    val CulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("culturing_tank")
        add("格雷科技将目光放到鲸鱼座T星的异星藻类上" translatedTo "GregTech set its sights on §dalien algae from Cetus T star§r") { gray() }
        add("这种藻类有着奇特的群体意识" translatedTo "This algae has §apeculiar collective consciousness") { gray() }
        add("技术人员创造性地使用它们开发出§6全新体制芯片" translatedTo "Technicians creatively used them to develop §6chips of entirely new architecture") { green() }
        add("§6AFMS-05培养缸为培养生物细胞材料量身打造" translatedTo "§6AFMS-05 culturing tank§r is tailor-made for cultivating biological cell materials") { green() }
        add("§e过滤器等级决定配方等级" translatedTo "§eFilter tier§r determines recipe tier") { aqua() }
        add("§e玻璃等级决定可用电压上限" translatedTo "§eGlass tier§r determines  upper limit of voltage usable") { aqua() }
        add(
            Star(1) + ("生物材料培养的基础设施" translatedTo "Basic infrastructure for biological material cultivation").gold()
                .bold().italic(),
        ) { aqua() }
    }

    // 大型培养缸
    val LargeCulturingTankTooltips = ComponentListSupplier {
        setTranslationPrefix("large_culturing_tank")
        add("针对越发庞大的生物材料产能需求" translatedTo "Addressing the increasingly massive §ebiological material production demand§r") { gray() }
        add("§6ABFL-411大型培养缸被开发出来" translatedTo "§6ABFL-411 large culturing tank§r was developed") { gray() }
        add("拥有更大的培养罐，更先进的培养管理系统§r" translatedTo "Features §alarger culture tanks§r and §amore advanced cultivation management system§r") { green() }
        add("在体积没有显著提升的情况下极大提高生产效率§r" translatedTo "Without significant volume increase, §agreatly improves production efficiency§r") { green() }
        add("§e过滤器等级§r决定配方等级" translatedTo "§eFilter tier§r determines recipe tier") { aqua() }
        add("§e玻璃等级§r决定可用电压上限" translatedTo "§eGlass tier§r determines maximum usable voltage") { aqua() }
        add(Star(1) + ("§6大规模生物材料生产设施§r" translatedTo "§6Large-scale biological material production facility§r")) { aqua() }
    }

    // 化工厂
    val ChemicalFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_factory")
        add("§d在化学的世界里" translatedTo "§dIn the world of chemistry§r") { lightPurple() }
        add("§4每一个分子都在诉说着自己的故事" translatedTo "§4Every molecule tells its own story§r") { aqua() }
        add(ComponentSlang.CoilEfficiencyBonus("§6§线圈等级每高出白铜一级能耗与时间减少5%§r" translatedTo "§6Each coil tier above Cupronickel, Reduces energy consumption and duration by 5%§r"))
    }

    // 衰变加速器
    val DecayAcceleratorTooltips = ComponentListSupplier {
        setTranslationPrefix("decay_accelerator")
        add("§7让放射性物质稍微加快一点退休速度§r" translatedTo "§7Gently encouraging radioactive materials to retire faster§r") { gray() }
        add("§8效果有限，但总比干等着强§r" translatedTo "§8Modest effect, but better than just waiting§r") { gray() }
    }

    // 回收机
    val RecyclingMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("recycling_machine")
        add("§2工业界的凤凰，让废弃物重获新生§r" translatedTo "§2Industrial phoenix, giving waste new life§r") { green() }
        add("§6环保主义者的梦想机器，资本家的利润源泉§r" translatedTo "§6Environmentalist's dream, capitalist's profit source§r") { gold() }
        add("§e连最顽固的废料都会在这里找到第二春§r" translatedTo "§eEven the most stubborn waste finds a second chance here§r") { yellow() }
    }

    // 质量发生器
    val MassGeneratorTooltips = ComponentListSupplier {
        setTranslationPrefix("mass_generator")
        add("§7质能转换的入门级实践§r" translatedTo "§7Beginner's practice in mass-energy conversion§r") { gray() }
        add("§8耗电量＞产出量，但科学价值无可替代§r" translatedTo "§8Power consumption > output, but scientifically priceless§r") { gray() }
    }

    // 超临界合成机
    val SpsCraftingTooltips = ComponentListSupplier {
        setTranslationPrefix("sps_crafting")
        add("§7在临界点上跳舞，让物质处于既存在又不存在的量子态" translatedTo "§7Dancing on critical points, maintaining matter in quantum states of existence and non-existence") { gray() }
        add("§b微妙平衡的艺术，温度和压力精确到普朗克尺度" translatedTo "§bArt of delicate balance, temperature and pressure precise to Planck scale") { aqua() }
    }

    // 精密组装机
    val PrecisionAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembler")
        add("§9DSW-17型精密组装机的运作原理是什么？" translatedTo "What is the operating principle of the §9DSW-17 Precision Assembler§r?") { blue() }
        add("§7据说它的§e多轴同步控制系统源自星际传输技术§r" translatedTo "§7Its §emulti-axis synchronous control system§r is said to originate from §dinterstellar transfer technology§r") { gray() }
        add("每一块§6超导组装平台§r都由专属工程师维护" translatedTo "Each §6superconducting assembly platform§r is maintained by dedicated engineers") { gold() }
        add("§a你是否曾想过，每一颗微型芯片的诞生，背后有多少DSW-17型的机械臂在默默运作？" translatedTo "§aHave you ever wondered how many §7DSW-17§r robotic arms work silently behind the birth of every microchip?") { green() }
        add("§e源代码存储于隔离仓，只有持有§d工程师徽章§r的人员可访问" translatedTo "§eSource code is stored in §cisolation vaults§r, accessible only to those with a §dengineer badge§r") { yellow() }
    }

    // 熔岩炉
    val LavaFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("lava_furnace")
        add(("§6地心能量萃取器，将岩石转化为炽热的液态力量§r" translatedTo "§6Planetary core energy extractor, transforming rock into molten power§r")) { gold() }
        add(("§7石头进去，岩浆出来，简单粗暴的地热转换艺术" translatedTo "§7Rocks in, lava out - the brutally simple art of geothermal conversion")) { gray() }
        add(("§e运行时的橙红色光芒能让整个车间充满温暖的地狱氛围" translatedTo "§eThe orange glow during operation creates a cozy infernal ambiance")) { yellow() }
        add(Warning(1) + (("§7每提供一个任意类型的圆石或石头可输出§c1B§7熔岩" translatedTo "§7Each cobblestone or stone provided outputs §c1B§7 lava").green())) { gray() }
    }

    // 大气收集室
    val AtmosphereCollectorRoomTooltips = ComponentListSupplier {
        setTranslationPrefix("atmosphere_collector_room")
        add(("§b大气层的美味榨汁机，专门收集行星的呼吸§r" translatedTo "§bThe atmosphere juicer, specializes in collecting planetary breath§r")) { aqua() }
        add(("§7从稀薄的高空到浓郁的对流层，没有它抽不动的气" translatedTo "§7From thin upper layers to dense troposphere, no gas is too tough to extract")) { gray() }
        add(("§e据说在满月之夜运行，能收集到特别的'月光风味氮气'" translatedTo "§eRumor says it collects special 'moonlight-flavored nitrogen' during full moons")) { yellow() }
        add(Star(1) + ("§7超大容量设计，一瓶更比六瓶强" translatedTo "§7Extra-large capacity design, one bottle equals six regular collectors").italic()) { gray() }
    }

    // 激光蚀刻工厂
    val LaserEtchingFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("laser_etching_factory")
        add(("§6光子雕刻圣殿，用光之刃重塑物质的结构§r" translatedTo "§6Photon engraving sanctuary, reshaping matter with blades of light§r")) { gold() }
        add(("§7当万亿瓦特的激光聚焦于微米之间，连时空都会为之弯曲" translatedTo "§7When terawatt lasers focus at micron scale, even spacetime bends")) { gray() }
        add(("§a精密到可以给病毒刻二维码，虽然没人知道为什么需要这么做" translatedTo "§aPrecise enough to engrave QR codes on viruses, though no one knows why")) { green() }
        add(Star(1) + ("运行激光焊接配方时速度x5" translatedTo "Running laser welding recipes speeds up x5").italic()) { gold() }
        add(Warning(1) + ("精密激光模式不支持并行处理" translatedTo "Precise laser mode does not support parallel processing").italic()) { yellow() }
    }

    // 裂变反应堆
    val FissionReactorTooltips = ComponentListSupplier {
        setTranslationPrefix("fission_reactor")

        // 核心功能描述
        add(("核聚变反应堆，通过燃料组件和冷却组件协同工作产生能量" translatedTo "Nuclear fission reactor that generates energy through fuel and cooling components working together").InfoFunctionPrimary())

        // 反应堆结构
        add(Star(1) + ("反应堆结构组成" translatedTo "Reactor structure components").InfoFunction())
        add(Tab(2) + ("燃料组件: " translatedTo "Fuel component: ").InfoFunction() + ("提供最大并行数量" translatedTo "provides maximum parallel number").InfoData())
        add(Tab(2) + ("升温系数 = 燃料组件相邻数 + 1" translatedTo "Heating coefficient = adjacent fuel components + 1").InfoSupport())
        add(Tab(2) + ("冷却组件: " translatedTo "Cooling component: ").InfoFunction() + ("提供最大冷却能力" translatedTo "provides maximum cooling capability").InfoData())
        add(Tab(2) + ("冷却组件必须与燃料组件相邻才有效" translatedTo "Cooling components must be adjacent to fuel components to be effective").InfoSupport())

        // 温度系统
        add(Star(1) + ("温度管理系统" translatedTo "Temperature management system").InfoFunction())
        add(Tab(2) + ("初始温度: " translatedTo "Initial temperature: ").InfoFunction() + ("298K" translatedTo "298K").InfoData())
        add(Tab(2) + ("温度上限: " translatedTo "Temperature limit: ").InfoWarning() + ("1500K" translatedTo "1500K").InfoCritical())
        add(Tab(2) + ("升温速率: 配方产热 × 升温系数/秒" translatedTo "Heating rate: recipe heat × heating coefficient/sec").InfoSupport())
        add(Tab(2) + ("自然降温: 停止工作时1K/秒" translatedTo "Natural cooling: 1K/sec when stopped").InfoSupport())
        add(Warning(1) + ("超过温度上限机器开始损坏，完全损坏时" translatedTo "Exceeding temperature limit damages machine, when fully damaged ") + ComponentSlang.Explosion.InfoCritical())

        // 冷却系统
        add(Star(1) + ("冷却系统运作" translatedTo "Cooling system operation").InfoFunction())
        add(Tab(2) + ("冷却液类型: " translatedTo "Cooling liquid types: ").InfoFunction() + ("蒸馏水或钠钾合金" translatedTo "Distilled water or sodium-potassium alloy").InfoData())
        add(Tab(2) + ("冷却条件: " translatedTo "Cooling condition: ").InfoSafe() + ("供给量 ≥ 需求量" translatedTo "supply ≥ demand").InfoRecommend())
        add(Tab(2) + ("最低需求量 = 配方产热 × 冷却参数 × 实际并行 × 当前温度 / 1500" translatedTo "Min demand = recipe heat × cooling param × actual parallel × current temp / 1500").InfoSupport())
        add(Tab(2) + ("最高供给量 = (冷却组件 - 相邻数/3) × 8" translatedTo "Max supply = (cooling components - adjacent/3) × 8").InfoSupport())
        add(Tab(2) + ("消耗量 = 需求量 × 冷却液系数" translatedTo "Consumption = demand × cooling liquid coefficient").InfoSupport())

        // 超频机制
        add(Star(1) + ("超频机制" translatedTo "Overclocking mechanism").InfoSafe())
        add(Tab(2) + ("触发条件: " translatedTo "Trigger condition: ").InfoFunction() + ("供给量 ≥ n × 需求量 (n>1)" translatedTo "supply ≥ n × demand (n>1)").InfoData())
        add(Tab(2) + ("超频效果: " translatedTo "Overclocking effect: ").InfoRecommend() + ("减少n秒配方时间" translatedTo "reduce n seconds recipe time").InfoSafe())

        // 产出系统
        add(Star(1) + ("冷却液产出" translatedTo "Cooling liquid output").InfoFunction())
        add(Tab(2) + ("蒸馏水冷却: " translatedTo "Distilled water cooling: ").InfoFunction() + ("产出蒸汽" translatedTo "produces steam").InfoSafe())
        add(Tab(3) + ("产量 = 消耗量 × min(160, 160/(1.4^(373-温度)))" translatedTo "Output = consumption × min(160, 160/(1.4^(373-temperature)))").InfoSupport())
        add(Tab(2) + ("钠钾合金冷却产出:" translatedTo "Sodium-potassium alloy cooling output:").InfoFunction())
        add(Tab(3) + ("≤825K: " translatedTo "≤825K: ").InfoData() + ("热钠钾合金" translatedTo "Hot sodium-potassium alloy").InfoSafe())
        add(Tab(3) + (">825K: " translatedTo ">825K: ").InfoData() + ("超临界钠钾合金" translatedTo "Supercritical sodium-potassium alloy").InfoSafe())

        add(ComponentSlang.BewareOfBurns)
        add(ComponentSlang.GTOSignal_Machine_MilestoneByGTORemix)
    }

    // 稀土离心机
    val RareEarthCentrifugalTooltips = ComponentListSupplier {
        setTranslationPrefix("rare_earth_centrifugal")
        add("§7基础稀土分离专家，用离心力解开大地深处的秘密" translatedTo "§7Basic rare earth separation expert, unlocking earth's deepest secrets with centrifugal force") { gray() }
        add("§b虽然效率有限，但为稀土工业奠定了重要基础" translatedTo "§bThough efficiency is limited, it lays important foundation for rare earth industry") { aqua() }
    }

    // 数字矿机
    val DigitalMinerTooltips = ComponentListSupplier {
        setTranslationPrefix("digital_miner")
        add("固定每两秒采掘一次" translatedTo "Mines once every two seconds") { aqua() }
        add("可通过GUI设置采掘范围和目标方块" translatedTo "Mining range and target blocks can be set via GUI") { aqua() }
        add("机器电压等级每高出一级：" translatedTo "For each increase in machine voltage level:") { aqua() }
        add(" - 可采掘最大范围翻倍（最高256）" translatedTo " - Maximum mining range is doubled (up to 256)") { gray() }
        add(" - 每次采掘的方块数量翻倍（最高4096）" translatedTo " - The number of blocks mined each time is doubled(up to 4096)") { gray() }
        add(" - 耗电量翻4倍" translatedTo " - Power consumption is quadrupled") { gray() }
        add("通入红石信号以重新计算采掘区域并执行" translatedTo "Input a redstone signal to recalculate the mining area and execute mining") { aqua() }
    }

    /*************************************************
     *              巨构(通常跨并/激光)                *
     **************************************************/

    // 如果要用AI, 请至少务必给出如下prompt
    // 6行左右
    // 鼓励科幻元素, 也可以融合一些梗
    // 可以围绕GTO寰宇集团, 但不必强求
    // 切勿风格和字眼过于相似, 必须多样化
    // 文本颜色不可乱用, 一句话最好一个颜色
    // 最后一句加星标语用于描述整个机器作用
    // 你可以参照可用的色彩接口... (copy code)

    // 运算中心
    val ComputerSupercomputingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("computer_supercomputing_center")

        // 核心功能描述
        add(("计算机超级计算中心" translatedTo "Computer Supercomputing Center").InfoFunctionPrimary())
        add(("将多台计算机集成在一起，提供大规模并行计算能力" translatedTo "Integrates multiple computers together to provide massive parallel computing power").InfoFunction())

        // 基本特性
        add(Star(1) + ("先进冷却系统" translatedTo "Advanced cooling system").InfoSafe())
        add(Tab(2) + ("采用先进冷却方案提供更高算力输出" translatedTo "Uses advanced cooling solutions for higher computing power output").InfoSupport())

        add(Star(1) + ("三级架构设计" translatedTo "Three-tier architecture design").InfoFunction())
        add(Tab(2) + ("通过在主机内放置特定物品切换等级" translatedTo "Switch tiers by placing specific items in the mainframe").InfoOperation())
        add(Tab(2) + ("结构方块等级必须与机器等级匹配" translatedTo "Structure block tiers must match machine tier").InfoWarning())

        // 算力系统
        add(Star(1) + ("算力计算系统" translatedTo "Computing power calculation system").InfoFunction())
        add(Tab(2) + ("最大输出算力 = 计算组件算力和 × 算力修正系数" translatedTo "Max output = sum of component power × correction factor").InfoData())
        add(Tab(2) + ("等级2/3时修正系数会随时间衰减" translatedTo "At levels 2/3, correction factor decays over time").InfoWarning())
        add(Tab(3) + ("衰减公式: ((系数-0.4)²/5000)×(0.8/log(系数+6))" translatedTo "Decay: ((factor-0.4)²/5000)×(0.8/log(factor+6))").InfoSupport())
        add(Tab(2) + ("算力修正系数最低值: " translatedTo "Minimum correction factor: ").InfoFunction() + ("0.8" translatedTo "0.8").InfoData())

        // 导热剂系统
        add(Star(1) + ("导热剂冷却增效" translatedTo "Thermal conductivity cooling enhancement").InfoSafe())
        add(Tab(2) + ("通过导热剂仓输入导热剂提升算力修正系数" translatedTo "Input thermal conductivity via hatch to boost correction factor").InfoRecommend())
        add(Tab(2) + ("提升上限: " translatedTo "Enhancement limits: ").InfoFunction() + ("等级2(4) / 等级3(16)" translatedTo "Tier 2(4) / Tier 3(16)").InfoDataImportant())
        add(Warning(1) + ("导热剂使用后会失效" translatedTo "Thermal conductivity becomes invalid after use").InfoWarning())
        add(Tab(2) + ("MFPC效率: " translatedTo "MFPC efficiency: ").InfoFunction() + ("块(0.18) 条(0.02) 粒(0.0022)" translatedTo "Block(0.18) Ingot(0.02) Nugget(0.0022)").InfoData())
        add(Tab(2) + ("Cascade-MFPC效率: " translatedTo "Cascade-MFPC efficiency: ").InfoFunction() + ("块(0.54) 条(0.06) 粒(0.0066)" translatedTo "Block(0.54) Ingot(0.06) Nugget(0.0066)").InfoData())
        add(Tab(2) + ("寒冰碎片: " translatedTo "Ice Shards: ").InfoDanger() + ("0.0001 (极低效率)" translatedTo "0.0001 (extremely low efficiency)").InfoDanger())
        // 槽位
        add(("Tier 1 : 支持HPCA系列组件" translatedTo "Tier 1 : Supports HPCA series components").InfoFunctionPrimary())
        add(Star(1) + ("槽位需求: " translatedTo "Slot requirement: ").InfoFunction() + ("无需安装任何物品" translatedTo "No items required").InfoSafe())
        add(Star(1) + ("结构材料需求:" translatedTo "Structure material requirements:").InfoFunction())
        add(Tab(2) + ("钨强化硼玻璃 + 计算机外壳 + 计算机散热口" translatedTo "Tungsten Borosilicate Glass + Computer Casing + Computer Heat Vent").InfoData())
        add(("Tier 2 : 支持NICH系列组件" translatedTo "Tier 2 : Supports NICH series components").InfoFunctionPrimary())
        add(Star(1) + ("槽位需求: " translatedTo "Slot requirement: ").InfoFunction() + ("放入生物主机" translatedTo "Place biological host").InfoDataImportant())
        add(Star(1) + ("结构材料需求:" translatedTo "Structure material requirements:").InfoFunction())
        add(Tab(2) + ("安普洛强化硼玻璃 + 生物计算机外壳 + 相变计算机散热口" translatedTo "Neutronium Borosilicate Glass + Biocomputer Casing + Phase Change Biocomputer Cooling Vents").InfoData())
        add(("Tier 3 : 支持GWCA系列组件 (自带桥接)" translatedTo "Tier 3 : Supports GWCA series components (with built-in bridge)").InfoFunctionPrimary())
        add(Star(1) + ("槽位需求: " translatedTo "Slot requirement: ").InfoFunction() + ("放入超因果主机" translatedTo "Place Hyper-Causal Host").InfoDataImportant())
        add(Star(1) + ("结构材料需求:" translatedTo "Structure material requirements:").InfoFunction())
        add(Tab(2) + ("塔兰强化硼玻璃 + 引力子计算机外壳 + 逆熵计算机冷凝矩阵" translatedTo "Taranium Borosilicate Glass + Graviton Computer Casing + Anti Entropy Computer Condensation Matrix").InfoData())
        add(Star(1) + ("自带跨维度桥接功能" translatedTo "Built-in cross-dimensional bridging capability").InfoSafe())
        add(ComponentSlang.GTOSignal_Machine_MiracleByGTORemix)
    }

    // 部件装配线
    val ComponentAssemblyLineTooltips = ComponentListSupplier {
        setTranslationPrefix("component_assembly_line")
        add("§6GTO寰宇格雷科技数年心血的结晶，工业自动化里程碑" translatedTo "§6Culmination of GTO Cosmic GregTech's years of effort, milestone in industrial automation") { gold() }
        add("§7将复杂部件装配流程整合为完美协同的流水线艺术" translatedTo "§7Integrating complex component assembly into perfectly synchronized production line artistry") { gray() }
        add("§b铱金外壳下是数千个精密伺服电机的完美共舞" translatedTo "§bBeneath iridium shells, thousands of precision servo motors dance in perfect harmony") { aqua() }
        add("§e虽然建造成本惊人，但带来的效率提升让投资物超所值" translatedTo "§eThough construction costs are staggering, the efficiency gains make it worth every penny") { yellow() }
        add("§a从螺丝到芯片，所有基础部件都能在这里高效产出" translatedTo "§aFrom screws to chips, all basic components can be efficiently produced here") { green() }
        add("§6董事长曾说：这是凡间最接近神之造物的工业奇迹" translatedTo "§6The chairman once said: This is industry's closest approximation to divine creation") { gold() }
        add("§c维护成本同样高昂，但为了未来必须承受的重量" translatedTo "§cMaintenance costs are equally high, but a necessary burden for the future") { red() }
        add(Star(1) + ("§6重新定义量产效率，让基础部件制造进入全新时代" translatedTo "§6Redefining mass production efficiency, ushering in a new era of basic component manufacturing")) { gold() }
    }

    // 进阶集成矿石处理厂
    val AdvancedIntegratedOreProcessorTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_integrated_ore_processor")
        add("§9矿石处理的终极答案，从原矿到成品的完美闭环" translatedTo "§9Ultimate solution for ore processing, perfect closed loop from raw ore to finished product") { blue() }
        add("§7量子级并行处理架构，同时处理9E个配方线程" translatedTo "§7Quantum-level parallel processing architecture, handles 9E recipe threads simultaneously") { gray() }
        add("§e就像拥有无限双手的炼金术士，同时进行所有工序" translatedTo "§eLike an alchemist with infinite hands, performing all processes at once") { yellow() }
        add("§b破碎、洗矿、离心、研磨——一步到位的神奇体验" translatedTo "§bCrushing, washing, centrifuging, maceration - all in one magical experience") { aqua() }
        add("§6可能会让传统的多机器产线感到失业焦虑" translatedTo "§6May cause unemployment anxiety in traditional multi-machine production lines") { gold() }
        add(Star(1) + ("§6重新定义矿石处理效率，让传统产线成为历史" translatedTo "§6Redefining ore processing efficiency, making traditional production lines history")) { gold() }
    }

    // 稀土处理综合设施
    val ComprehensiveTombarthiteProcessingFacilityTooltips = ComponentListSupplier {
        setTranslationPrefix("comprehensive_tombarthite_processing_facility")
        add("§9稀土处理的工业巨构，将离心分离技术推向极致" translatedTo "§9Industrial megastructure for rare earth processing, pushing centrifugal separation to the extreme") { blue() }
        add("§e多重离心阵列协同工作，实现稀土元素的无损精准分离" translatedTo "§eMultiple centrifugal arrays work in harmony, achieving lossless precise separation of rare earth elements") { yellow() }
        add("§b量子级密度识别系统，确保每个元素都被完美分选" translatedTo "§bQuantum-level density recognition system ensures perfect sorting of every element") { aqua() }
        add("§6从钪到镥，17种稀土元素在这里找到各自的归宿" translatedTo "§6From scandium to lutetium, all 17 rare earth elements find their destined paths here") { gold() }
        add("§c运行时的精密振动如同大地的心跳，沉稳而有力" translatedTo "§cPrecision vibrations during operation resemble the earth's heartbeat, steady and powerful") { red() }
        add(Star(1) + ("§6稀土分离技术的终极体现，让稀有元素变得触手可及" translatedTo "§6Ultimate embodiment of rare earth separation technology, making rare elements within reach")) { gold() }
    }

    // 木化工厂
    val WoodDistillationTooltips = ComponentListSupplier {
        setTranslationPrefix("wood_distillation")
        add("§a绿色化学的工业典范，将木质纤维转化为万千化工原料" translatedTo "§aIndustrial典范 of green chemistry, transforming wood fibers into myriad chemical materials") { green() }
        add("§7催化气体精准调控，让木质素和纤维素高效分离重组" translatedTo "§7Precise catalytic gas control enables efficient separation and重组 of lignin and cellulose") { gray() }
        add("§e从木材到甲醇、从木屑到化纤，实现可再生资源的最大化利用" translatedTo "§eFrom wood to methanol, from sawdust to chemical fibers, maximizing utilization of renewable resources") { yellow() }
        add("§b生物催化技术与高温裂解的完美结合，零碳排放的绿色工艺" translatedTo "§bPerfect combination of biocatalysis and pyrolysis, zero-carbon emission green process") { aqua() }
        add("§6运行时散发淡淡的木质香气，仿佛森林在工业化身" translatedTo "§6Emits faint woody aroma during operation, like a forest's industrial incarnation") { gold() }
        add(Star(1) + ("§6木质资源转化的革命性突破，让化工走向可持续发展" translatedTo "§6Revolutionary breakthrough in wood resource conversion, leading chemistry towards sustainability")) { gold() }
    }

    // 石化工厂
    val PetrochemicalPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("petrochemical_plant")
        add("§9工业炼金术的终极殿堂，将黑色黄金转化为万千化工奇迹" translatedTo "§9Ultimate temple of industrial alchemy, transforming black gold into countless chemical marvels") { blue() }
        add("§7裂化线圈与精密分馏塔的完美交响，一步完成全流程" translatedTo "§7Perfect symphony of cracking coils and precision fractionation towers, completing entire process in one step") { gray() }
        add("§e从原油到塑料、从重油到化纤，魔法般的物质蜕变" translatedTo "§eMagical material metamorphosis from crude oil to plastics, from heavy oil to chemical fibers") { yellow() }
        add("§b裂解、重整、分馏——石化工业的三大乐章在此齐奏" translatedTo "§bCracking, reforming, fractionation - three movements of petrochemical industry playing in harmony") { aqua() }
        add("§6运行时会散发特殊气味，建议配备工业级空气净化" translatedTo "§6Emits distinctive odors during operation, industrial air purification recommended") { gold() }
        add("§c庞大的金属丛林，却是有机化工最精密的艺术舞台" translatedTo "§cVast metal jungle, yet the most precise artistic stage for organic chemistry") { red() }
        add(Star(1) + ("§6重新定义石化处理规模，让复杂化工变得简单高效" translatedTo "§6Redefining petrochemical processing scale, making complex chemistry simple and efficient")) { gold() }
    }

    // 纳米集成加工中心
    val NanitesIntegratedProcessingCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("nanites_integrated_processing_center")
        add("§a纳米蜂群技术革命，让传统化工产线成为过去式" translatedTo "§aNanites colony technology revolution, making traditional chemical production lines obsolete") { green() }
        add("§7三模块自由切换：矿物萃取、聚合物扭曲、生物工程" translatedTo "§7Three modular modes: mineral extraction, polymer distortion, bioengineering") { gray() }
        add("§e模块化设计让复杂产线成为历史，一机解决所有难题" translatedTo "§eModular design makes complex production lines history, one machine solves all problems") { gray() }
        add("§e自修复、自适应、自优化，智能蜂群重新定义生产效率" translatedTo "§eSelf-repairing, self-adapting, self-optimizing, intelligent swarms redefine production efficiency") { yellow() }
        add("§e零浪费、零污染、零误差，完美制造的终极答案" translatedTo "§6Zero waste, zero pollution, zero error - the ultimate answer to perfect manufacturing") { yellow() }
        add(Star(1) + ("§6工业生产和纳米蜂群的集成" translatedTo "§6Integration of industrial production and nanobot swarms")) { gold() }
    }
    var MATERIAL_MAP: ImmutableMap<String?, Float?> = ImmutableMap.of<String?, Float?>(
        "gtceu.iron", 1.0f,
        "gtceu.iridium", 1.1f,
        "gtocore.orichalcum", 1.2f,
        "gtocore.infuscolium", 1.3f,
        "gtocore.draconium", 1.4f,
        "gtocore.cosmic_neutronium", 1.5f,
        "gtocore.eternity", 1.6f,
    )
    val NanoswarmDataSheetTooltips = ComponentListSupplier {
        setTranslationPrefix("nanoswarm_data_sheet")
        add(Star(1) + ("安装对应模块解锁对应配方" translatedTo "Install the corresponding module to unlock the corresponding recipe")) { green() }
        add(Circle(1) + ("主机中放入纳米蜂群可减少污染概率" translatedTo "Placing nanites in the host can reduce pollution probability")) { aqua() }
        add(Circle(1) + ("每放入一个纳米蜂群，污染概率减少数如下所示" translatedTo "Each nanite placed reduces pollution probability as follows")) { aqua() }
        MATERIAL_MAP.forEach { (material: String?, reduction: Float?) ->
            add(
                Tab(2) + Component.translatable("material.$material").toComponentSupplier()
                    .gold() + (": -$reduction%").toLiteralSupplier(),
            ) { gray() }
        }
    }

    // 化工复合体
    val ChemicalComplexTooltips = ComponentListSupplier {
        setTranslationPrefix("chemical_complex")
        add("§7量子级反应控制，让每个分子都在精确的时空点相遇" translatedTo "§7Quantum-level reaction control, ensuring every molecule meets at precise spacetime coordinates") { gray() }
        add("§9分子工程的指挥中心，亿万纳米蜂群协同演绎化学交响" translatedTo "§9Command center of molecular engineering, billions of nanobots performing chemical symphonies") { blue() }
        add("§e纳米蜂群如同微观交响乐团，指挥着原子间的舞蹈" translatedTo "§eNanobot swarms conduct atomic dances like microscopic orchestras") { yellow() }
        add("§b从简单化合到复杂聚合，化学反应从未如此优雅精准" translatedTo "§bFrom simple combinations to complex polymerizations, chemical reactions never so elegant and precise") { aqua() }
        add("§6纳米蜂群工作时会发出幽蓝光芒，如同微观星云闪烁" translatedTo "§6Nanobot swarms emit ethereal blue glow during operation, like microscopic nebulae") { gold() }
        add(Star(1) + ("§6分子合成如同艺术创作" translatedTo "§6Molecular synthesis is as elegant as artistic creation")) { gold() }
    }

    // 元素复制机
    val ElementCopyingTooltips = ComponentListSupplier {
        setTranslationPrefix("element_copying")
        add("§7基于UU物质的量子级元素复制，从虚无中创造物质" translatedTo "§7Quantum-level element replication using UU matter, creating substance from void") { gray() }
        add("§e插入预设光盘，选择元素，剩下的交给时空打印机" translatedTo "§eInsert preset discs, select elements, leave the rest to the spacetime printer") { yellow() }
        add("§9GTO寰宇集团物质重构技术突破，打破元素守恒定律" translatedTo "§9GTO Cosmic Group's matter reconstruction breakthrough, defying elemental conservation laws") { blue() }
        add("§b从氢到锎，元素周期表就是你的购物清单" translatedTo "§bFrom hydrogen to californium, the periodic table is your shopping list") { aqua() }
        add("§c炼金术师的终极梦想，现在只需要电力和UU物质" translatedTo "§cAlchemist's ultimate dream, now only needs electricity and UU matter") { red() }
        add(Star(1) + ("§6元素复制成为工业常态" translatedTo "§6Element replication becomes industrial routine")) { gold() }
    }

    // 特大线材轧机
    val MegaWiremillTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_wiremill")
        add("§d当金属遇见绝对的力量" translatedTo "§dWhen metal meets absolute power") { lightPurple() }
        add("§7万吨压力下诞生的完美导体" translatedTo "§7Perfect conductors born under thousand tons of pressure") { gray() }
        add("§6工业血脉的锻造者——将金属转化为能量通道" translatedTo "§6Forger of industrial veins - transforming metal into energy pathways") { gold() }
        add("§e超导级精度确保每个原子完美排列" translatedTo "§eSuperconducting precision ensures perfect atomic alignment") { yellow() }
        add("§a从厚重锭块到纤细如发的完美导线" translatedTo "§aFrom bulky ingots to hair-thin perfect wires") { green() }
        add("§b为整个文明编织能量的神经网络" translatedTo "§bWeaving neural networks of energy for entire civilizations") { aqua() }
        add(Star(1) + ("§6线材制造艺术的终极体现" translatedTo "§6Ultimate embodiment of wire manufacturing art")) { gold() }
    }

    // 超导磁驱冲击装置
    val SuperconductingMagneticPresserTooltips = ComponentListSupplier {
        setTranslationPrefix("superconducting_magnetic_presser")
        add("§9来自御坂美琴的工艺传承" translatedTo "§9Misaka Mikoto's craft legacy") { blue() }
        add("§7以十亿伏特的力量将金属塑造成型" translatedTo "§7Shaping metal with billion-volt power") { gray() }
        add("§b电磁加速让材料获得炮弹般的动能" translatedTo "§bElectromagnetic acceleration gives materials projectile-like kinetic energy") { aqua() }
        add("§e就像超电磁炮一样精准，但用于制造而非破坏" translatedTo "§eAs precise as Railgun, but for creation instead of destruction") { yellow() }
        add("§6「この腕で、未来を打ち抜く」——工业版超电磁炮" translatedTo "§6'With this arm, I'll shoot through the future' — Industrial Railgun edition") { gold() }
        add("§c可以轻松把硬币加速到三马赫" translatedTo "§cCan easily accelerate coins to Mach 3") { red() }
        add(Star(1) + ("§a电磁力量的工业应用" translatedTo "§aIndustrial application of electromagnetic power")) { green() }
    }

    // 重型辊轧机
    val HeavyRollingTooltips = ComponentListSupplier {
        setTranslationPrefix("heavy_rolling")
        add("§9维度压缩的艺术大师，将三维压入二维" translatedTo "§9Master of dimensional compression, squeezing 3D into 2D") { blue() }
        add("§7万吨压力让金属在维度间屈服" translatedTo "§7Ten-thousand-ton pressure makes metal yield between dimensions") { gray() }
        add("§b从厚板到薄箔，每一次碾压都是空间的折叠" translatedTo "§bFrom thick plates to thin foils, each roll is a fold in space") { aqua() }
        add("§e「二向箔制造模式」——开玩笑的，大概" translatedTo "§e'Dimensional Foil Production Mode' — just kidding, probably") { yellow() }
        add("§6过度碾压可能导致材料进入量子级薄度" translatedTo "§6Excessive rolling may lead to quantum thinness") { gold() }
        add(Star(1) + ("§6工业级维度压缩能力" translatedTo "§6Industrial-grade dimensional compression")) { gold() }
    }

    // 相变魔方
    val PhaseChangeCubeTooltips = ComponentListSupplier {
        setTranslationPrefix("phase_change_cube")
        add("§9在立方体间重构分子排列，重塑物质的存在形式" translatedTo "§9Reconstructing molecular arrangements within cubes, reshaping material existence") { blue() }
        add("§7量子级别的相变控制，精确到每一个原子键" translatedTo "§7Quantum-level phase change control, precise to every atomic bond") { gray() }
        add("§b将钢铁化为流水，让熔岩瞬间凝固" translatedTo "§bTurning steel into flowing water, solidifying lava instantly") { aqua() }
        add("§c相变过程中请保持安全距离，物质重组可能产生能量涟漪" translatedTo "§cMaintain safe distance during phase change, matter reorganization may produce energy ripples") { red() }
        add("§e六个面对应六种相变模式，就像真正的魔方" translatedTo "§eSix faces correspond to six phase change modes, just like a real Rubik's Cube") { yellow() }
        add(Star(1) + ("§6突破物质形态的界限" translatedTo "§6Breaking the boundaries of material states")) { gold() }
    }

    // 粒子流矩阵封装机
    val ParticleStreamMatrixFillingMachineTooltips = ComponentListSupplier {
        setTranslationPrefix("particle_stream_matrix_filling_machine")
        add("§9量子级别的封装艺术，让流体在矩阵中完美定格" translatedTo "§9Quantum-level encapsulation art, perfectly freezing fluids within matrices") { blue() }
        add("§7粒子流在磁场引导下精确注入容器，零损耗零污染" translatedTo "§7Particle streams precisely injected into containers under magnetic guidance, zero loss zero contamination") { gray() }
        add("§b从普通铁桶到力场容器，适应各种封装需求" translatedTo "§bFrom ordinary iron drums to forcefield containers, adaptable to various encapsulation needs") { aqua() }
        add("§e矩阵式多通道同时作业，效率提升百倍" translatedTo "§eMatrix-style multi-channel simultaneous operation, hundredfold efficiency improvement") { yellow() }
        add("§6甚至可以封装等离子体和量子流体这类特殊物质" translatedTo "§6Capable of encapsulating special materials like plasma and quantum fluids") { gold() }
        add("§a连光子和声波都能完美封存" translatedTo "§aEven photons and sound waves can be perfectly preserved") { green() }
        add(Star(1) + ("§6安全储存最不稳定的物质" translatedTo "§6Enable safe storage of even the most unstable substances")) { gold() }
    }

    // 熔火之心
    val MoltenCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("molten_core")
        add("§4地心熔炉的化身，将热量提升至行星核心级别" translatedTo "§4Incarnation of the planetary core furnace, raising heat to planetary core levels") { red() }
        add("§7模仿恒星内部环境，让流体达到极限温度" translatedTo "§7Simulating stellar interior environments, bringing fluids to extreme temperatures") { gray() }
        add("§c超导线圈产生足以熔化时空的磁场约束热核反应" translatedTo "§cSuperconducting coils generate magnetic fields strong enough to melt spacetime, containing thermonuclear reactions") { red() }
        add("§6即使是液态钨在这里也会像水一样沸腾" translatedTo "§6Even liquid tungsten boils like water here") { gold() }
        add("§e「小心烫伤」的警告在这里显得过于轻描淡写" translatedTo "§e'Caution: Hot' warnings seem like understatements here") { yellow() }
        add("§b等离子体模式下可产生超过太阳表面的温度" translatedTo "§bPlasma mode can generate temperatures exceeding the solar surface") { aqua() }
        add(Star(1) + ("§6让任何流体都达到理论温度极限" translatedTo "§6Push any fluid to theoretical temperature limits")) { gold() }
    }

    // 复合式蒸馏分馏塔
    val CompoundDistillationTowerTooltips = ComponentListSupplier {
        setTranslationPrefix("compound_distillation_tower")
        add("§d这是现代炼金术的圣殿" translatedTo "§dThis is the temple of modern alchemy") { lightPurple() }
        add("§6原油的暗夜在此裂解出光的碎片" translatedTo "§6The crude oil's darkness cracks into fragments of light here") { gold() }
        add("§d用几何的刚毅线条书写分子世界的缱绻情书" translatedTo "§dWriting tender love letters of the molecular world with geometric rigid lines") { lightPurple() }
        add("§b最炽热的交融终将成就最极致的纯粹" translatedTo "§bThe most intense fusion will ultimately achieve the most extreme purity") { aqua() }
        add("§e格雷科技化工设计部门的最新力作" translatedTo "§eGregTech's chemical design department latest masterpiece") { yellow() }
        add("§6DHG-1020复合式蒸馏分馏塔" translatedTo "§6DHG-1020 compound distillation fractionation tower") { gold() }
        add(Star(1) + ("§6极高效率的多功能化工处理设施" translatedTo "§6Highly efficient multi-functional chemical processing facility")) { gold() }
    }

    // 巨型酿造厂
    val MegaBrewerTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_brewer")
        add("§6工业级发酵艺术，将酿造传统提升到史诗级规模" translatedTo "§6Industrial-scale fermentation art, elevating brewing tradition to epic proportions") { gold() }
        add("§7精密温控系统确保每个发酵罐都处于完美微生物环境" translatedTo "§7Precision temperature control ensures perfect microbial environment in every fermentation tank") { gray() }
        add("§e从麦芽汁到琥珀琼浆，万吨级酿造从未如此精准优雅" translatedTo "§eFrom wort to amber nectar, thousand-ton-scale brewing never so precise and elegant") { yellow() }
        add("§b多阶段并行发酵，让时间与风味在金属巨罐中完美交融" translatedTo "§bMulti-stage parallel fermentation, perfect fusion of time and flavor in metal giants") { aqua() }
        add("§c请勿在设备运行时品尝半成品，强如武松也要体验工业级醉酒" translatedTo "§cDo not taste samples during operation, even Wu Song would experience industrial-grade intoxication") { red() }
        add("§a运行时散发醉人香气，整个车间都弥漫着酿造的艺术" translatedTo "§aEmits intoxicating aromas during operation, the entire workshop filled with brewing artistry") { green() }
        add(Star(1) + ("§6重新定义酿造规模" translatedTo "§6Redefining brewing scale")) { gold() }
    }

    // 引雷针
    val LightningRodTooltips = ComponentListSupplier {
        setTranslationPrefix("lightning_rod")
        add("§6普罗米修斯盗火，我们驾驭雷霆" translatedTo "§6Prometheus stole fire, we harness thunder") { gold() }
        add("§7用亿伏特电弧撕裂虚空，从闪电中锻造物质" translatedTo "§7Tearing the void with billion-volt arcs, forging matter from lightning") { gray() }
        add("§e索尔的铁锤在此显得渺小，宙斯的雷霆成为生产线" translatedTo "§eThor's hammer seems small here, Zeus' thunder becomes production line") { yellow() }
        add("§b每次放电都是创世瞬间，在电光火石间诞生新元素" translatedTo "§bEvery discharge is a creation moment, new elements born in flashes of lightning") { aqua() }
        add("§9玻璃外壳内电蛇狂舞，宛如囚禁着雷神的愤怒" translatedTo "§9Electric serpents dance within glass casing, like imprisoned fury of thunder gods") { blue() }
        add("§c请做好防雷措施" translatedTo "§cTake lightning precautions") { red() }
        add(Star(1) + ("§6x用雷霆书写工业神话" translatedTo "§6Writing industrial myths with thunder")) { gold() }
    }

    // 磁能反应炉
    val MagneticEnergyReactionFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("magnetic_energy_reaction_furnace")
        add("§b电磁创世的熔炉，用电能重塑物质本源" translatedTo "§bElectromagnetic creation furnace, reshaping material essence with electricity") { aqua() }
        add("§7强磁场约束下的等离子电弧，达到恒星核心的温度" translatedTo "§7Plasma arcs constrained by strong magnetic fields, reaching stellar core temperatures") { gray() }
        add("§e既可无中生有创造物质，也能让万物回归元素本源" translatedTo "§eBoth creating matter from nothing, and returning all things to elemental origins") { yellow() }
        add("§6回收模式启动时，连最坚固的合金也会瞬间离子化" translatedTo "§6In recycling mode, even the strongest alloys instantly ionize") { gold() }
        add("§c电弧跃动时会产生臭氧气息，如同雨后雷暴般清新而危险" translatedTo "§cArc discharges produce ozone scent, fresh yet dangerous like post-storm thunder") { blue() }
        add("§9磁场线圈发出低沉嗡鸣，仿佛在吟唱物质的分解与重组" translatedTo "§9Magnetic coils emit low hum, like chanting the decomposition and recombine of matter") { blue() }
        add(Star(1) + ("§6让创造与回收在电弧中完美统一" translatedTo "§6Perfect unity of creation and recycling in electric arcs")) { gold() }
    }

    // 高能激光车床
    val HighEnergyLaserLatheTooltips = ComponentListSupplier {
        setTranslationPrefix("high_energy_laser_lathe")
        add("§b光子雕刻大师，用激光之刃重塑物质的几何形态" translatedTo "§bPhoton engrave master, reshaping material geometry with laser blades") { aqua() }
        add("§7透明车间内光子舞蹈，纳米级精度雕刻出完美构件" translatedTo "§7Photons dance in transparent workshop,纳米-level precision carving perfect components") { gray() }
        add("§e从金属到钻石，激光所至无物不雕，无材不工" translatedTo "§eFrom metal to diamond, nothing uncarvable by laser, no material unworkable") { yellow() }
        add("§6激光路径如光之织锦，在材料表面编织精密纹路" translatedTo "§6Laser paths like light tapestry, weaving precise patterns on material surfaces") { gold() }
        add("§c运行时会投射出绚烂全息图，宛如未来工厂的视觉交响" translatedTo "§cProjects dazzling holograms during operation, like visual symphony of future factories") { red() }
        add("§9透过透明外壳，可以亲眼见证光子雕刻的魔法时刻" translatedTo "§9Through transparent casing, witness the magical moments of photon雕刻 firsthand") { blue() }
        add(Star(1) + ("§6让激光成为最优雅的雕刻工具" translatedTo "§6Make laser the most elegant carving tool")) { gold() }
    }

    // 中子丝线切割
    val NeutroniumWireCuttingTooltips = ComponentListSupplier {
        setTranslationPrefix("neutronium_wire_cutting")
        add("§5星核材料切割专家，用中子丝线分割宇宙最坚硬的物质" translatedTo "§5Stellar core material cutting expert, using neutron threads to divide the universe's hardest substances") { lightPurple() }
        add("§7中子简并态丝线，以强相互作用力实现原子级精准切割" translatedTo "§7Neutron degenerate state threads achieve atomic-level precision cutting through strong interaction force") { gray() }
        add("§e连中子星物质都能轻松分割，地球材料如同黄油般柔软" translatedTo "§eEven neutron star material cuts easily, earthly materials feel like butter") { yellow() }
        add("§b切割时会产生切伦科夫辐射蓝光，如同微型超新星爆发" translatedTo "§bCherenkov radiation blue glow during cutting, like miniature supernova explosions") { aqua() }
        add("§6绝对零度冷却系统确保中子丝线保持量子干涉状态" translatedTo "§6Absolute zero cooling system maintains neutron threads in quantum coherence state") { gold() }
        add(Star(1) + ("§6从此分割变得如同光线般精准" translatedTo "§6From now on division is as precise as light beams")) { gold() }
    }

    // 纳米吞噬厂
    val NanoPhagocytosisPlantTooltips = ComponentListSupplier {
        setTranslationPrefix("nano_phagocytosis_plant")
        add("§8物质终结者，纳米蜂群组成的吞噬风暴" translatedTo "§8Matter terminator, phagocytosis storm composed of nanobot swarms") { gray() }
        add("§7万亿级纳米机器人协同攻击，以分子精度分解一切物质" translatedTo "§7Trillions of nanobots attack in unison, decomposing all matter with molecular precision") { gray() }
        add("§c从钻石到合金，没有任何材料能抵挡纳米蜂群的集体吞噬" translatedTo "§cFrom diamond to alloy, no material can withstand the collective吞噬 of nanobot swarms") { red() }
        add("§e吞噬过程发出密集的嗡鸣，如同微观世界的蝗虫过境" translatedTo "§eDense humming during phagocytosis process, like locust swarms in the microscopic world") { yellow() }
        add("§b分解产物自动分类收集，实现100%物质回收利用率" translatedTo "§bDecomposition products automatically sorted and collected, achieving 100% material recycling") { aqua() }
        add("§6请勿投入贵重物品，纳米蜂群不会区分目标和垃圾" translatedTo "§6Do not input valuables, nanobot swarms don't distinguish between targets and trash") { gold() }
        add(Star(1) + ("§8粉碎技术让物质分解变得如同呼吸般自然" translatedTo "§8Crushing technology makes material decomposition as natural as breathing")) { gold() }
    }

    // 巨型浸洗池
    val MegaBathTankTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_bath_tank")
        add("§9工业级浸泡艺术，让流体与物质在巨池中深度交融" translatedTo "§9Industrial-scale immersion art, deep integration of fluids and materials in giant pools") { blue() }
        add("§7百万升容量设计，同时处理数千种材料的表面处理" translatedTo "§7Million-liter capacity design, simultaneously processing surface treatment of thousands of materials") { gray() }
        add("§e从酸洗到镀膜，从蚀刻到钝化，浸泡改变物质表面命运" translatedTo "§eFrom pickling to coating, etching to passivation, immersion alters material surface destinies") { yellow() }
        add("§b智能流体循环系统，确保每个角落的浓度和温度完全一致" translatedTo "§bIntelligent fluid circulation system ensures uniform concentration and temperature throughout") { aqua() }
        add("§6运行时池面泛起细腻波纹，如同工业版本的温泉疗愈" translatedTo "§6Delicate ripples form on the surface during operation, like industrial version of hot spring therapy") { gold() }
        add("§c高浓度表面活性剂可能导致不可逆的蛋白质变性" translatedTo "§cHigh-concentration surfactants may cause irreversible protein denaturation") { red() }
        add(Star(1) + ("§6材料改性的魔法仪式" translatedTo "§6A magical ritual for material modification")) { gold() }
    }

    // 巨型真空干燥炉
    val MegaVacuumDryingFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("mega_vacuum_drying_furnace")
        add("§8虚空干燥专家，在真空中完美分离" translatedTo "§8Void drying expert, perfectly separating in vacuum") { yellow() }
        add("§7超低气压环境下，水分瞬间升华，只留下纯净的单质粉末" translatedTo "§7In ultra-low pressure environment, moisture instantly sublimates, leaving only pure elemental powder") { gray() }
        add("§e从泡沫到粉末的华丽蜕变，真空环境确保零氧化零污染" translatedTo "§eMagnificent transformation from foam to powder, vacuum environment ensures zero oxidation zero contamination") { yellow() }
        add("§b多层加热线圈精准控温，让每个矿石颗粒都获得完美干燥" translatedTo "§bMulti-layer heating coils provide precise temperature control, ensuring perfect drying for every ore particle") { aqua() }
        add("§6运行时机体发出低沉嗡鸣，如同在真空中演奏工业交响曲" translatedTo "§6Emits low hum during operation, like performing industrial symphony in vacuum") { gold() }
        add(Star(1) + ("§8矿石处理在真空中达到完美纯净" translatedTo "§8Achieve perfect purity in ore processing through vacuum")) { gold() }
    }

    // 分子震荡脱水装置
    val MolecularOscillationDehydratorTooltips = ComponentListSupplier {
        setTranslationPrefix("molecular_oscillation_dehydrator")
        add("§8智子级脱水技术，用分子震荡剥离每一个水分子" translatedTo "§8Sophon-level dehydration tech, stripping every water molecule with molecular oscillation") { yellow() }
        add("§7十一维震荡波穿透物质，让水分无处遁形" translatedTo "§711-dimensional oscillation waves penetrate matter, leaving water molecules nowhere to hide") { gray() }
        add("§e「脱水！脱水！」——三体文明的工业应用" translatedTo "§e'Dehydrate! Dehydrate!' — Trisolaran civilization's industrial application") { yellow() }
        add("§b从有机体到矿物，任何含湿材料都能瞬间变得绝对干燥" translatedTo "§bFrom organisms to minerals, any moist material can become absolutely dry instantly") { aqua() }
        add("§c微弱智子发出闪烁，仿佛在向半人马座发送信号" translatedTo "§cFaint sophon flickering, as if sending signals to Alpha Centauri") { red() }
        add(Star(1) + ("§8脱水技术的维度突破" translatedTo "§8Dimensional breakthrough in dehydration tech")) { gold() }
    }

    // 极限压缩装置
    val HorizontalCompressorTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_compressor")
        add("§8物质密度艺术家，用兆吨压力重塑原子间距" translatedTo "§8Matter density artist, reshaping atomic distances with megaton pressure") { yellow() }
        add("§7行星级液压系统，让最坚硬的金属也变得顺从可塑" translatedTo "§7Planetary-level hydraulic system, making even the hardest metals compliant and malleable") { gray() }
        add("§e从锭到块，从粉到板，压缩改变物质的存在形态" translatedTo "§eFrom ingots to blocks, from powder to plates, compression alters material existence forms") { yellow() }
        add("§b量子隧道效应补偿，确保压缩过程中零材料损耗" translatedTo "§bQuantum tunneling effect compensation ensures zero material loss during compression") { aqua() }
        add("§6运行时大地微微震颤，仿佛在呼应着材料的密度革命" translatedTo "§6Slight ground tremors during operation, as if echoing the material's density revolution") { gold() }
        add(Star(1) + ("§8让物质密度突破理论极限" translatedTo "§8Break theoretical limits of material density")) { gold() }
    }

    // 极限电炉
    val ExtremeElectricFurnaceTooltips = ComponentListSupplier {
        setTranslationPrefix("extreme_electric_furnace")
        add("§4工业级高温大师，用纯粹电能实现完美热控" translatedTo "§4Industrial-grade high-temperature master, achieving perfect thermal control with pure electricity") { red() }
        add("§7智能加热线圈精准调控，让每个配方都获得最佳熔炼效果" translatedTo "§7Smart heating coils provide precise control, ensuring optimal smelting for every recipe") { gray() }
        add("§e从矿石熔炼到合金制备，电能热转化效率突破99.9%" translatedTo "§eFrom ore smelting to alloy preparation, electrical-thermal conversion efficiency突破 99.9%") { yellow() }
        add("§b多温区协同工作，同时处理固体熔炼和简单合金配方" translatedTo "§bMulti-zone coordination, simultaneously processing solid smelting and simple alloy recipes") { aqua() }
        add("§6运行平稳安静，只有材料熔化的细微声响见证变化" translatedTo "§6Operates smoothly and quietly, only subtle sounds of melting materials见证 transformation") { gold() }
        add("§c虽然温度惊人，但相比等离子熔炉显得格外温文尔雅" translatedTo "§cThough temperatures are impressive, compared to plasma furnaces it's remarkably 'gentle'") { red() }
        add(Star(1) + ("§6电能熔炼的完美典范" translatedTo "§6Perfect example of electric smelting")) { gold() }
    }

    // 高温反应枢纽
    val HighTemperatureReactionHubTooltips = ComponentListSupplier {
        setTranslationPrefix("high_temperature_reaction_hub")
        add("§4热力学交响指挥家，用极致温度驱动物质蜕变" translatedTo "§4Thermodynamic symphony conductor, driving material metamorphosis with extreme temperatures") { red() }
        add("§7超导加热矩阵精准控温，让每个反应都在最佳热力学窗口进行" translatedTo "§7Superconducting heating matrix provides precise temperature control, ensuring every reaction occurs in optimal thermodynamic windows") { gray() }
        add("§e从材料液化到高温合成，热激活能在这里被完美利用" translatedTo "§eFrom material liquefaction to high-temperature synthesis, thermal activation energy is perfectly utilized here") { yellow() }
        add("§b多反应室独立控温，可同时进行熔化和化学反应" translatedTo "§bIndependent temperature control for multiple reaction chambers, enabling simultaneous melting and chemical reactions") { aqua() }
        add("§6反应时发出炽热橙光，如同微型太阳在车间中诞生" translatedTo "§6Emits炽热 orange glow during reactions, like miniature suns being born in the workshop") { gold() }
        add("§c热浪辐射范围极大，请确保工作区通风和隔热措施" translatedTo "§cHeat wave radiation range is extensive, ensure workshop ventilation and insulation") { red() }
        add(Star(1) + ("§6让热力学成为工业生产的精确工具" translatedTo "§6Make thermodynamics a precise tool for industrial production")) { gold() }
    }

    // 引力弯折装置
    val GravityBendingDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("gravity_bending_device")
        add("§5时空曲率雕刻者，用人工引力重塑材料形态" translatedTo "§5Spacetime curvature sculptor, reshaping material forms with artificial gravity") { lightPurple() }
        add("§7局部引力场操控，让最坚硬的板材也优雅弯曲" translatedTo "§7Local gravitational field manipulation, making even the hardest plates bend gracefully") { gray() }
        add("§e从平板到曲面，从直杆到圆环，引力是最好的造型师" translatedTo "§eFrom flat plates to curved surfaces, straight rods to perfect rings, gravity is the best stylist") { yellow() }
        add("§b多引力源协同作用，实现复杂三维曲面的精确成型" translatedTo "§bMultiple gravity sources work in harmony, achieving precise forming of complex 3D surfaces") { aqua() }
        add("§6运行时空间微微扭曲，光线在设备周围产生引力透镜效应" translatedTo "§6Space slightly distorts during operation, light producing gravitational lensing effects around the device") { gold() }
        add("§c在强引力场中移动可以体验时空旅行" translatedTo "§cMoving in strong gravitational fields can give you spacetime travel experience") { red() }
        add(Star(1) + ("§6引力成为最优雅的弯折工具" translatedTo "§6Gravity becomes the most elegant bending tool")) { gold() }
    }

    // 阿拉克涅之手
    val HandOfArachneTooltips = ComponentListSupplier {
        setTranslationPrefix("hand_of_arachne")
        add("§5古罗马掌管编织的神" translatedTo "§5The ancient Rome's weaving goddess") { lightPurple() }
        add("§7量子级导线编织，让电流在更粗的通道中奔腾" translatedTo "§7Quantum-level wire weaving, allowing current to surge through thicker channels") { gray() }
        add("§e从单线到16倍粗缆，导电能力呈几何级数增长" translatedTo "§eFrom single wires to 16x thick cables, conductivity grows exponentially") { yellow() }
        add("§b多轴协同编织，如同蜘蛛女神在编织电流的网络" translatedTo "§bMulti-axis coordinated weaving, like the spider goddess weaving networks of current") { aqua() }
        add("§6运行时机臂优雅舞动，宛如阿拉克涅在跳工业之舞" translatedTo "§6Mechanical arms dance gracefully during operation, like Arachne performing an industrial dance") { gold() }
        add(Star(1) + ("§6导线编织的终极艺术" translatedTo "§6Ultimate art of wire weaving")) { gold() }
    }

    // 裂解反应枢纽
    val CrackerHubTooltips = ComponentListSupplier {
        setTranslationPrefix("cracker_hub")
        add("§4分子破碎专家，用高温高压撕裂碳氢化合物的枷锁" translatedTo "§4Molecular fragmentation expert, tearing apart hydrocarbon bonds with high temperature and pressure") { red() }
        add("§7六根反应柱协同工作，实现复杂分子的精确裂解与重组" translatedTo "§7Six reaction columns work in harmony, achieving precise cracking and重组 of complex molecules") { gray() }
        add("§e从重油到轻质燃料，从长链到短链，裂解改变分子命运" translatedTo "§eFrom heavy oil to light fuels, long chains to short chains, cracking alters molecular destinies") { yellow() }
        add("§b管道网络如血管般交织，输送着被高温解放的分子碎片" translatedTo "§bPipeline networks intertwine like blood vessels, transporting molecular fragments liberated by high heat") { aqua() }
        add("§c裂解过程极其剧烈，请确保安全阀正常工作" translatedTo "§cCracking process is extremely violent, ensure safety valves are functional") { red() }
        add(Star(1) + ("§6分子裂解的智慧核心" translatedTo "§6Intelligent core of molecular cracking")) { gold() }
    }

    // 维度聚焦激光蚀刻阵列
    val DimensionalFocusEngravingArrayTooltips = ComponentListSupplier {
        setTranslationPrefix("dimensional_focus_engraving_array")
        add("§5六维聚焦圣殿，让激光在维度间穿梭雕刻" translatedTo "§5Six-dimensional focus sanctuary, allowing lasers to穿梭 and carve between dimensions") { lightPurple() }
        add("§e光束从不同维度汇聚，在奇点处实现量子级精密蚀刻" translatedTo "§eLight beams converge from different dimensions, achieving quantum-level precision etching at singularities") { yellow() }
        add("§b运行时六道光束如神之手指，在中心球体编织光之图案" translatedTo "§bSix light beams during operation like divine fingers, weaving light patterns on the central sphere") { aqua() }
        add("§6中心球体会呈现彩虹色辉光，仿佛孕育着微型宇宙" translatedTo "§6Central sphere displays rainbow luminescence, as if nurturing a miniature universe") { gold() }
        add("§a需要光刻胶作为蚀刻介质，确保激光精准传递能量" translatedTo "§aRequires photoresist as etching medium, ensuring precise laser energy transfer") { green() }
        add(Star(1) + ("§6让雕刻艺术突破时空限制" translatedTo "§6Break through spacetime limits in carving art")) { gold() }
    }

    // 超级冶炼炉
    val SuperBlastSmelterTooltips = ComponentListSupplier {
        setTranslationPrefix("super_blast_smelter")
        add("§4恒星核心模拟器，用人工太阳的温度熔炼万物" translatedTo "§4Stellar core simulator, smelting all things with artificial sun temperatures") { red() }
        add("§7等离子线圈产生超越地核的热量，让最耐熔的材料也化为液态" translatedTo "§7Plasma coils generate heat surpassing Earth's core, liquefying even the most refractory materials") { gray() }
        add("§e从钨钢到中子星物质，没有什么是极限温度无法熔化的" translatedTo "§eFrom tungsten steel to neutron star matter, nothing can withstand extreme temperatures") { yellow() }
        add("§b多相合金同步冶炼，精确控制每种元素的配比与融合" translatedTo "§bMulti-phase alloy simultaneous smelting, precisely controlling each element's ratio and fusion") { aqua() }
        add("§6炉心闪耀着白炽光芒，如同囚禁着一颗微型恒星" translatedTo "§6Furnace core glows with incandescent light, like imprisoning a miniature star") { gold() }
        add(Star(1) + ("§4熔炼技术的终极答案" translatedTo "§4Ultimate answer to smelting technology")) { gold() }
    }

    // 复合式极端冷却装置
    val CompoundExtremeCoolingUnitTooltips = ComponentListSupplier {
        setTranslationPrefix("compound_extreme_cooling_unit")
        add("§b绝对零度艺术家，用极致寒冷重塑炽热物质的形态" translatedTo "§bAbsolute zero artist, reshaping炽热 materials with extreme cold") { aqua() }
        add("§7量子制冷矩阵协同工作，瞬间吸收巨量热能" translatedTo "§7Quantum refrigeration matrix works协同, instantly absorbing massive thermal energy") { gray() }
        add("§e从白热锭块到熔融金属，甚至等离子体都能瞬间凝固成型" translatedTo "§eFrom white-hot ingots to molten metals, even plasma can instantly solidify and form") { yellow() }
        add("§6多级冷却系统，从室温到接近绝对零度的完美温控" translatedTo "§6Multi-stage cooling system, perfect temperature control from room temp to near absolute zero") { gold() }
        add("§c设备表面结满霜晶，如同极地冰川的工业化身" translatedTo "§cDevice surface frosts over, like industrial incarnation of polar glaciers") { red() }
        add("§a激光定位确保冷却均匀，避免材料因温差应力破裂" translatedTo "§aLaser positioning ensures uniform cooling, preventing material fracture from thermal stress") { green() }
        add(Star(1) + ("§6冷却技术的终极体现，让极端低温成为工业生产的精确工具" translatedTo "§6Ultimate embodiment of cooling technology, making extreme low temperatures a precise industrial tool")) { gold() }
    }

    // 超导电磁工厂
    val SuperconductingElectromagnetismTooltips = ComponentListSupplier {
        setTranslationPrefix("superconducting_electromagnetism")
        add("§9麦克斯韦方程组的工业化身，电磁统一的完美体现" translatedTo "§9Industrial incarnation of Maxwell's equations, perfect embodiment of electromagnetic unity") { blue() }
        add("§7超流态氦冷却下的零电阻环境，实现量子磁通钉扎效应" translatedTo "§7Zero-resistance environment under superfluid helium cooling, achieving quantum flux pinning effect") { gray() }
        add("§e洛伦兹力与法拉第定律在这里协同演绎工业交响" translatedTo "§eLorentz force and Faraday's law perform industrial symphony in harmony here") { yellow() }
        add("§b迈斯纳效应确保完美抗磁性，仿佛在嘲笑欧姆定律的局限" translatedTo "§bMeissner effect ensures perfect diamagnetism, as if mocking the limitations of Ohm's law") { aqua() }
        add("§6运行时产生持续的约瑟夫森振荡，如同在向超导先驱们致敬" translatedTo "§6Continuous Josephson oscillation during operation, like paying homage to superconductivity pioneers") { gold() }
        add(Star(1) + ("§6电磁学应用的巅峰之作" translatedTo "§6Pinnacle of electromagnetic applications")) { gold() }
    }

    // 晶体构建者
    val CrystalBuilderTooltips = ComponentListSupplier {
        setTranslationPrefix("crystal_builder")
        add("§9布拉格父子的梦想工厂，在原子层面编织完美晶格" translatedTo "§9Bragg父子's dream factory, weaving perfect crystal lattices at atomic level") { blue() }
        add("§7超高压环境下实现柯塞尔生长模式，培育无缺陷单晶结构" translatedTo "§7Achieving Kossel growth mode under ultra-high pressure, cultivating defect-free monocrystalline structures") { gray() }
        add("§e从硅熔体中拉制完美单晶，晶向精度达到角秒级别" translatedTo "§ePulling perfect monocrystals from silicon melt, crystal orientation precision reaching arc-second level") { yellow() }
        add("§6高压反应室如同钻石砧槽，在极端条件下孕育晶体之美" translatedTo "§6High-pressure chamber like diamond anvil cell, nurturing crystal beauty under extreme conditions") { gold() }
        add(Star(1) + ("§6晶体生长的艺术" translatedTo "§6Art of crystal growth")) { gold() }
    }

    // 神圣分离者
    val HolySeparatorTooltips = ComponentListSupplier {
        setTranslationPrefix("holy_separator")
        add("§d离心圣殿，用向心力演绎物质分离的神圣仪式" translatedTo "§dCentrifugal sanctuary, performing sacred rituals of material separation with centripetal force") { lightPurple() }
        add("§e沉降速度差成为分离之匙，密度梯度揭示组分奥秘" translatedTo "§eSedimentation velocity differences become separation keys, density gradients reveal composition mysteries") { yellow() }
        add("§b祭坛般的旋转平台，让离心过程如同神圣的净化仪式" translatedTo "§bAltar-like rotating platform, making centrifugation like a sacred purification ritual") { aqua() }
        add("§6庄严的低频嗡鸣，仿佛在吟唱分离的圣歌" translatedTo "§6Emits solemn low-frequency hum, like chanting separation hymns") { gold() }
        add(Star(1) + ("§6密度差异成为最优雅的分离艺术" translatedTo "§6Density differences become the most elegant separation art")) { gold() }
    }

    // 力场压模工厂
    val FieldExtruderFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("field_extruder_factory")
        add("§9力场锻造圣殿，用无形之手塑造万般形态" translatedTo "§9Force field forging sanctuary, shaping myriad forms with intangible hands") { blue() }
        add("§7量子力场精准调控，让金属在无形模具中自由流动" translatedTo "§7Quantum force field precise control, allowing metals to flow freely in intangible molds") { gray() }
        add("§e从齿轮到管道，从螺丝到轴承，万般部件一念成形" translatedTo "§eFrom gears to pipes, screws to bearings, all components form in an instant") { yellow() }
        add("§b麦克斯韦妖在此工作，用信息熵减实现完美成型" translatedTo "§bMaxwell's demon works here, achieving perfect forming through information entropy reduction") { aqua() }
        add("§6力场波动如透明丝绸，在空间中编织出精密构件" translatedTo "§6Force field undulations like transparent silk, weaving precision components in space") { gold() }
        add(Star(1) + ("§6部件成型的革命突破" translatedTo "§6Revolutionary breakthrough in component forming")) { gold() }
    }

    // 中子锻砧
    val NeutronForgingAnvilTooltips = ComponentListSupplier {
        setTranslationPrefix("neutron_forging_anvil")
        add("§5强相互作用力锻台，用中子简并压重塑物质本源" translatedTo "§5Strong interaction force forging platform, reshaping material essence with neutron degeneracy pressure") { lightPurple() }
        add("§7模拟中子星表面环境，实现宇宙级压力的等静压成型" translatedTo "§7Simulating neutron star surface conditions, achieving cosmic-level isostatic pressing") { yellow() }
        add("§e从金属锻造到粉末压实，万亿帕斯卡压力下万物归形" translatedTo "§eFrom metal forging to powder compaction, all things take form under trillion pascal pressure") { yellow() }
        add("§6锻击时发出深空般的低沉共鸣，如同中子星在心跳" translatedTo "§6Emits deep space-like low resonance during forging, like neutron star heartbeat") { gold() }
        add("§c超高压操作须严格遵循规程" translatedTo "§cUltra-high pressure operations must strictly follow procedures") { red() }
        add(Star(1) + ("§6宇宙级的锻造力量" translatedTo "§6Cosmic-level forging power")) { gold() }
    }

    // 双子星封装系统
    val GeminiContainmentSystemTooltips = ComponentListSupplier {
        setTranslationPrefix("gemini_containment_system")
        add("§5物质封装双生子，打包与解包的完美对称艺术" translatedTo "§5Material encapsulation twins, perfect symmetrical art of packing and unpacking") { lightPurple() }
        add("§7量子纠缠封装技术，让每个容器都与内容物建立超维连接" translatedTo "§7Quantum entanglement packaging, establishing transdimensional connections between containers and contents") { gray() }
        add("§e一边将万物装入完美包裹，一边让封印之物重见天日" translatedTo "§ePackaging all things into perfect parcels while releasing sealed items back to light") { yellow() }
        add("§b泡利不相容原理的工业应用，确保每个封装都完美无瑕" translatedTo "§bIndustrial application of Pauli exclusion principle, ensuring every package is flawless") { aqua() }
        add("§6双子模块协同工作，如同宇宙正负电荷的完美平衡" translatedTo "§6Twin modules work in harmony, like perfect balance of cosmic positive and negative charges") { gold() }
        add("§c封装态与解封态同时存在，展现量子叠加的工业奇迹" translatedTo "§cPackaged and unpackaged states coexist simultaneously, showcasing quantum superposition industrial marvel") { red() }
        add(Star(1) + ("§6封装技术的镜像" translatedTo "§6Mirror of packaging technology")) { gold() }
    }

    // 克尔-纽曼均质仪
    val KerrNewmanHomogenizerTooltips = ComponentListSupplier {
        setTranslationPrefix("kerr_newman_homogenizer")
        add("§5时空搅拌圣殿，用相对论效应实现完美均质" translatedTo "§5Spacetime mixing sanctuary, achieving perfect homogeneity through relativistic effects") { lightPurple() }
        add("§7参考系拖拽与电磁场协同，让混合物在时空扭曲中达到绝对均匀" translatedTo "§7Frame-dragging and electromagnetic fields collaborate, achieving absolute uniformity in spacetime distortion") { gray() }
        add("§e从胶体到乳液，即使最不相容的物质也能在这里达成和谐" translatedTo "§eFrom colloids to emulsions, even the most incompatible substances achieve harmony here") { yellow() }
        add("§b克尔-纽曼度规在此具现化，旋转电荷产生独特的混合时空" translatedTo "§bKerr-Newman metric materialized here, rotating charges create unique mixing spacetime") { aqua() }
        add("§6搅拌叶片如事件视界般旋转，吞噬所有不均匀性" translatedTo "§6Mixing blades rotate like event horizons, devouring all inhomogeneity") { gold() }
        add("§c警告：强时空扭曲可能导致周围时间流速轻微变化" translatedTo "§cWarning: Strong spacetime distortion may cause slight time flow variations nearby") { red() }
        add(Star(1) + ("§6均质化达到宇宙级的完美标准" translatedTo "§6Relativistic achieves cosmic-level perfect homogeneity standards")) { gold() }
    }

    // 溶解核心
    val DissolutionCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("dissolution_core")
        add("§b溶剂魔法圣殿，用分子间力解开物质的化学枷锁" translatedTo "§bSolvent magic sanctuary, unlocking chemical bonds with intermolecular forces") { aqua() }
        add("§7范德华力与氢键在此成为最优雅的溶解艺术家" translatedTo "§7Van der Waals forces and hydrogen bonds become the most elegant dissolution artists here") { gray() }
        add("§e从矿物酸溶到生物煮解，极性分子与非极性分子的完美共舞" translatedTo "§eFrom mineral acid dissolution to biological digestion, perfect dance of polar and non-polar molecules") { yellow() }
        add("§b溶解度参数精准调控，让每种物质找到最合适的溶剂伴侣" translatedTo "§bPrecise control of solubility parameters, helping each material find its perfect solvent partner") { aqua() }
        add("§6反应釜中溶液如液态彩虹，演绎着溶解与析出的平衡之美" translatedTo "§6Solutions in reactor like liquid rainbow, performing the beauty of dissolution-precipitation balance") { gold() }
        add(Star(1) + ("§6让分子间作用力成为工业分离的魔法钥匙" translatedTo "§6Make intermolecular forces the magic key to industrial separation")) { gold() }
    }

    // 综合气相沉积系统
    val IntegratedVaporDepositionSystemTooltips = ComponentListSupplier {
        setTranslationPrefix("integrated_vapor_deposition_system")
        add("§9原子级镀膜圣殿，让物质以气态演绎重生之舞" translatedTo "§9Atomic-level coating sanctuary, where materials perform rebirth dance in gaseous state") { blue() }
        add("§7PVD与CVD完美融合，物理溅射与化学气相协同作用" translatedTo "§7Perfect fusion of PVD and CVD, physical sputtering and chemical vapor deposition work synergistically") { gray() }
        add("§e从纳米镀层到金刚石薄膜，单原子层精度控制沉积过程" translatedTo "§eFrom nano-coatings to diamond films, single atomic layer precision controls deposition process") { yellow() }
        add("§b朗缪尔-布洛杰特技术在此进化，实现三维复杂曲面均匀沉积" translatedTo "§bLangmuir-Blodgett technique evolved here, achieving uniform deposition on complex 3D surfaces") { aqua() }
        add("§6真空室中原子如星尘飘落，在基材表面编织完美涂层" translatedTo "§6Atoms drift like stardust in vacuum chamber, weaving perfect coatings on substrate surfaces") { gold() }
        add(Star(1) + ("§6表面工程的终极答案" translatedTo "§6Ultimate answer to surface engineering")) { gold() }
    }

    // 微生物之主
    val MicroorganismMasterTooltips = ComponentListSupplier {
        setTranslationPrefix("microorganism_master")
        add("§a生命科学的工业圣殿，在绝对洁净中驾驭微生物的力量" translatedTo "§aIndustrial sanctuary of life science, harnessing microbial power in absolute cleanliness") { green() }
        add("§7自适应紫外辐射系统，精准调控每个微生物的生长环境" translatedTo "§7Adaptive UV radiation system precisely controls every microorganism's growth environment") { gray() }
        add("§e纳米级环境控制，让最脆弱的生物样本也能安全培育" translatedTo "§eNanoscale environmental control enables safe cultivation of even the most fragile biological samples") { yellow() }
        add("§6绝对洁净空间连一个杂菌都无法存活，纯度超越手术室" translatedTo "§6Absolute clean space where not a single contaminant survives, purity surpassing operating rooms") { gold() }
        add("§c警告：辐射期间请勿直视内部！" translatedTo "§cWarning: Avoid looking inside during radiation!") { red() }
        add(Star(1) + ("§6生物技术的巅峰之作" translatedTo "§6Pinnacle of biotechnology")) { gold() }
    }

    // 生命熔炉
    val LifeForgeTooltips = ComponentListSupplier {
        setTranslationPrefix("life_forge")
        add("§a生命炼金圣殿，在工业熔炉中培育生物奇迹" translatedTo "§aLife alchemy sanctuary, cultivating biological miracles in industrial furnaces") { green() }
        add("§7自适应辐射场精准调控，为生化反应提供最佳能量环境" translatedTo "§7Adaptive radiation field precise control, providing optimal energy environment for biochemical reactions") { gray() }
        add("§e从细胞提取到酶催化，生命过程在这里被工业化重现" translatedTo "§eFrom cell extraction to enzyme catalysis, life processes are industrially recreated here") { yellow() }
        add("§b辐射屏蔽穹顶下，生物分子在能量激流中保持活性" translatedTo "§bUnder radiation shielding dome, biomolecules maintain activity in energy torrents") { aqua() }
        add("§6运行生化反应配方时，§6无视环境辐射条件§r" translatedTo "§6When running biochemical reaction recipes, §6ignore radiation condition§r") { gold() }
        add("§c生物活性监测系统确保每个反应都在生命友好条件下进行" translatedTo "§cBio-activity monitoring system ensures every reaction occurs in life-friendly conditions") { red() }
        add(Star(1) + ("§6生物技术的熔炉核心" translatedTo "§6Furnace core of biotechnology")) { gold() }
    }

    // 综合组装车间
    val IntegratedAssemblerTooltips = ComponentListSupplier {
        setTranslationPrefix("integrated_assembler")
        add("§9GTO寰宇集团时空工程技术结晶，突破装配维度限制" translatedTo "§9Crystallization of GTO Cosmic Group's spacetime engineering, breaking dimensional assembly limits") { blue() }
        add("§7超时空装配单元线性排列，如同跨越维度的传送带" translatedTo "§7Hyperspatial assembly units linearly arranged like cross-dimensional conveyor belts") { gray() }
        add("§e从纳米芯片到星舰引擎，万物皆可在此完美组装" translatedTo "§eFrom nano-chips to starship engines, everything can be perfectly assembled here") { yellow() }
        add("§b量子纠缠定位系统确保每个零件在最佳时空点对接" translatedTo "§bQuantum entanglement positioning ensures each component docks at optimal spacetime points") { aqua() }
        add("§6「让复杂成为简单，让不可能成为日常」——GTO设计理念" translatedTo "§6'Make complexity simple, make impossible routine' — GTO design philosophy") { gold() }
        add("§c时空波动期间请勿手动干预" translatedTo "§cDo not manually intervene during spacetime fluctuations") { red() }
        add(Star(1) + ("§6重新定义工业装配的可能性" translatedTo "§6Redefine industrial assembly possibilities")) { gold() }
    }

    // 精密组装中心
    val PrecisionAssemblyCenterTooltips = ComponentListSupplier {
        setTranslationPrefix("precision_assembly_center")
        add("§b微米级装配圣殿，用机械之指谱写精密的工业诗篇" translatedTo "§bMicron-level assembly sanctuary, composing precise industrial poetry with mechanical fingers") { aqua() }
        add("§7量子视觉系统确保每个零件都在纳米精度下完美对接" translatedTo "§7Quantum vision system ensures every component docks perfectly at nanometer precision") { gray() }
        add("§e从微型芯片到精密仪器，组装误差小于原子直径" translatedTo "§eFrom microchips to precision instruments, assembly error smaller than atomic diameter") { yellow() }
        add("§6防震平台与恒温环境，为精密组装提供绝对稳定的舞台" translatedTo "§6Anti-vibration platform and constant temperature environment provide absolutely stable stage for precision assembly") { gold() }
        add("§a机械臂移动如芭蕾舞者，在微观世界中演绎装配的艺术" translatedTo "§aRobotic arms move like ballet dancers, performing the art of assembly in the microscopic world") { green() }
        add("§c呼吸过重都可能影响精度，需要佩戴专业防护装备" translatedTo "§cEven heavy breathing may affect precision, professional protective gear needed") { red() }
        add(Star(1) + ("§6让微观世界的组装变得完美无瑕" translatedTo "§6Makes microscopic assembly flawless")) { gold() }
    }

    // 纳米蜂群电路组装工厂
    val NanoswarmCircuitAssemblyFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("nanoswarm_circuit_assembly_factory")
        add("§d在绝对寂静的空间里" translatedTo "§dIn absolutely silent space") { lightPurple() }
        add("§7一场微观宇宙的创世仪式正在上演" translatedTo "§7A creation ceremony of the microscopic universe is being performed") { gray() }
        add("§6纳米蜂群工厂——这里没有熔炉的咆哮" translatedTo "§6Nanoswarm factory - there is no roar of furnaces here") { gold() }
        add("§b没有机械臂的挥舞" translatedTo "§bNo waving of mechanical arms") { aqua() }
        add("§e只有亿万纳米机器人以光的语言低语" translatedTo "§eOnly billions of nanobots whispering in the language of light") { yellow() }
        add("§a它们如同辛勤的工人，在硅片中建造微观城市" translatedTo "§aThey are like diligent workers, building microscopic cities in silicon") { green() }
        add("§6为工业帝国不断输送神经" translatedTo "§6Continuously supplying nerves for the industrial empire") { gold() }
        add(Star(1) + ("§6最高级电路组装的终极设备" translatedTo "§6Ultimate equipment for highest-grade circuit assembly")) { gold() }
    }

    // 奈亚拉托提普之触
    var NyarlathotepsTentacleTooltips = ComponentListSupplier {
        setTranslationPrefix("nyarlathoteps_tentacle")
        add("§d当空间本身成为装配台" translatedTo "§dWhen space itself becomes the assembly platform") { lightPurple() }
        add("§7现实的结构在触须间重新编织" translatedTo "§7The fabric of reality rewoven between tentacles") { gray() }
        add("§6GTO寰宇集团的终极造物——超越制造的制造" translatedTo "§6GTO Cosmic Group's ultimate creation - manufacturing beyond manufacturing") { gold() }
        add("§b无需机械的运动" translatedTo "§bNo mechanical movement required") { aqua() }
        add("§e维度触须直接在量子层面操作物质" translatedTo "§eDimensional tentacles manipulate matter at the quantum level") { yellow() }
        add("§a每一次触碰都是物理法则的重新定义" translatedTo "§aEvery touch is a redefinition of physical laws") { green() }
        add("§9在时空的织布机上编织未来的蓝图" translatedTo "§9Weaving blueprints of the future on the looms of spacetime") { blue() }
        add(Star(1) + ("§6让不可能成为生产线上的日常" translatedTo "§6Make the impossible routine on the production line")) { gold() }
    }

    // 巨型浮游选矿池
    val GiantFlotationTankTooltips = ComponentListSupplier {
        setTranslationPrefix("giant_flotation_tank")
        add("§9矿物浮选圣殿，用气泡之舞分离大地的馈赠" translatedTo "§9Mineral flotation sanctuary, separating earth's gifts with bubble dances") { blue() }
        add("§7界面化学与流体动力学的完美工业演绎" translatedTo "§7Perfect industrial performance of interface chemistry and fluid dynamics") { gray() }
        add("§e亿万气泡如微型升降机，精准分选不同密度的矿物颗粒" translatedTo "§eBillions of bubbles like miniature elevators, precisely sorting mineral particles by density") { yellow() }
        add("§b接触角与表面张力在此成为分离艺术的关键参数" translatedTo "§bContact angle and surface tension become key parameters of separation art here") { aqua() }
        add("§6泡沫层如液态银河，闪耀着矿物分离的璀璨光芒" translatedTo "§6Foam layer like liquid galaxy, shimmering with the brilliance of mineral separation") { gold() }
        add("§c安全生产 警钟长鸣，须规范操作" translatedTo "§cSafety production, alarm bells toll forever, all must be operated standardly") { red() }
        add(Star(1) + ("§6让浮选成为大地宝藏的精准分拣师" translatedTo "§6Make flotation the precise sorter of earth's treasures")) { gold() }
    }

    // 超限绿洲
    val TransliminalOasisTooltips = ComponentListSupplier {
        setTranslationPrefix("transliminal_oasis")
        add("§a生命绿洲的工业化身，在金属丛林中培育万物生机" translatedTo "§aIndustrial incarnation of life oasis, cultivating all living things in metal jungles") { green() }
        add("§7光合作用与生物催化在此达到工业化完美平衡" translatedTo "§7Photosynthesis and biocatalysis achieve industrial perfect balance here") { gray() }
        add("§e生物活性方块如跳动心脏，为整个系统注入生命能量" translatedTo "§eBio-active blocks pulse like beating hearts, infusing life energy into the entire system") { yellow() }
        add("§6温室穹顶下万物生长，工业与自然达成奇妙和谐" translatedTo "§6All things grow under the greenhouse dome, industry and nature achieve wondrous harmony") { gold() }
        add(Star(1) + ("§6农业科技的绿洲" translatedTo "§6Oasis of agricultural technology")) { gold() }
    }

    // 熵流引擎
    var EntropyFluxEngineTooltips = ComponentListSupplier {
        setTranslationPrefix("entropy_flux_engine")
        add("§a爱因斯坦的梦想，质能方程的现实化身" translatedTo "§8Einstein's dream, physical manifestation of E=mc²") { green() }
        add("§7从虚无中创造质量，物理学家的终极玩具" translatedTo "§7Creating mass from nothing, physicist's ultimate toy") { gray() }
        add("§5可能轻微违反若干守恒定律" translatedTo "§5May slightly violate some conservation laws") { lightPurple() }
        add("§e运行成本：巨额电能；收获：几毫克的质量" translatedTo "§eOperating cost: massive power; Yield: milligrams of mass") { yellow() }
        add("§6但谁说梦想需要讲究性价比呢？" translatedTo "§6But since when do dreams need cost-effectiveness?") { gold() }
        add(Star(1) + ("§6质能转换技术的巅峰体现" translatedTo "§6The pinnacle of mass-energy conversion technology")) { gold() }
    }

    // 蜂群之心
    val SwarmCoreTooltips = ComponentListSupplier {
        setTranslationPrefix("swarm_core")
        add("§4纳米蜂群孕育圣殿，万亿智能单元的诞生摇篮" translatedTo "§3Nanobot swarm growing sanctuary, birth cradle of trillions of intelligent units") { aqua() }
        add("§7量子自组装技术，让纳米机器人在费曼梦中自我复制" translatedTo "§7Quantum self-assembly technology, enabling nanobots to self-replicate in Feynman's dreams") { gray() }
        add("§e从硅基芯片到碳纳米管，每个蜂群单元都承载着工业智慧" translatedTo "§eFrom silicon chips to carbon nanotubes, each swarm unit carries industrial wisdom") { yellow() }
        add("§b8192倍并行培育，让纳米蜂群以指数级速度增长" translatedTo "§b8192x parallel cultivation, enabling nanobot swarms to grow exponentially") { aqua() }
        add("§6培育舱中蓝光闪烁，如同微观星云的诞生与湮灭" translatedTo "§6Blue light flickers in cultivation chambers, like birth and annihilation of microscopic nebulae") { gold() }
        add("§c自进化算法让蜂群不断优化，但请确保控制协议牢固" translatedTo "§cSelf-evolution algorithms constantly optimize swarms, but ensure control protocols are secure") { red() }
        add(Star(1) + ("§6纳米技术的核心引擎" translatedTo "§6Core engine of nanotechnology")) { gold() }
    }

    // 磁约束维度震荡装置
    val MagneticConfinementDimensionalityShockDeviceTooltips = ComponentListSupplier {
        setTranslationPrefix("magnetic_confinement_dimensionality_shock_device")
        add("§5维度工程的奇迹造物，在现实结构中制造可控的时空涟漪" translatedTo "§5Miracle of dimensional engineering, creating controlled spacetime ripples in reality's fabric") { lightPurple() }
        add("§7产生多重磁场，将高能等离子体束缚在维度交界处" translatedTo "§7Generates multi-layered magnetic fields, confining energetic plasma at dimensional boundaries") { gray() }
        add("§d混沌级别的能量搅拌，让等离子体在现实与虚空间震荡跃迁" translatedTo "§dChaos-level energy agitation, making plasma oscillate between reality and void") { lightPurple() }
        add("§b每一次震荡都在改写局部物理常数，产生不可思议的反应产物" translatedTo "§bEach oscillation rewrites local physical constants, producing unimaginable reaction products") { aqua() }
        add("§e运行时会散发彩虹色辉光，仿佛在演奏宇宙的弦理论乐章" translatedTo "§eEmits rainbow-hued luminescence during operation, like playing the string theory symphony of the cosmos") { yellow() }
        add(Star(1) + ("§6在混沌中创造秩序" translatedTo "§6Ultimate solution for energetic plasma processing, creating order from chaos")) { gold() }
    }

    // 进阶质量发生器
    val AdvancedMassFabricatorTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_mass_fabricator")
        add("§4血红外壳下的创世之力，让质能方程成为生产线上的日常" translatedTo "§4Creative power within blood red shell, making mass-energy equations routine on production lines") { red() }
        add("§7线圈编织能量网络，从虚空中编织出实在的质量" translatedTo "§7Coils weave energy networks, spinning substantial mass from the void") { gray() }
        add("§c效率突破临界点，现在产出终于超越了能量消耗" translatedTo "§cEfficiency breakthrough critical point, output now finally exceeds energy consumption") { red() }
        add("§6从微观粒子到宏观物质，创世过程变得可控可量化" translatedTo "§6From microscopic particles to macroscopic matter, the creation process becomes controllable and quantifiable") { gold() }
        add("§e振金外壳发出深红色脉动，如同跳动的心脏" translatedTo "§eVibranium casing pulses with deep crimson glow during operation, like a beating heart") { yellow() }
        add(Star(1) + ("§6质能转换技术的工业级实现" translatedTo "§6Industrial realization in mass-energy conversion")) { gold() }
    }

    // 进阶超临界合成机
    val AdvancedSpsCraftingTooltips = ComponentListSupplier {
        setTranslationPrefix("advanced_sps_crafting")
        add("§f纯白超临界外壳下的超维度合成奇迹" translatedTo "§fTransdimensional synthesis miracle within pure white sps casing") { white() }
        add("§7同时维持多个超临界相，在多重现实中并行合成" translatedTo "§7Maintaining multiple supercritical phases simultaneously, parallel synthesis across multiverse") { gray() }
        add("§b量子纠缠协调系统，确保每个临界点完美同步" translatedTo "§bQuantum entanglement coordination system ensures perfect synchronization of every critical point") { aqua() }
        add("§e运行时外壳会呈现珍珠般的光泽流动，美得令人窒息" translatedTo "§eCasing displays pearl-like luminous flows during operation, breathtakingly beautiful") { yellow() }
        add("§6突破单一时空限制，现在可以在不同维度同时进行合成" translatedTo "§6Breaking single spacetime limits, now capable of simultaneous synthesis across dimensions") { gold() }
        add(Star(1) + ("§f让物质创造突破维度限制" translatedTo "§fUltimate form of supercritical synthesis, breaking dimensional limits in material creation")) { white() }
    }

    // 恒星终极物质锻造工厂
    val StarUltimateMaterialForgeFactoryTooltips = ComponentListSupplier {
        setTranslationPrefix("star_ultimate_material_forge_factory")
        add("§6创世熔炉的终极形态，在工厂中重现恒星的诞生与毁灭" translatedTo "§6Ultimate form of creation furnace, recreating stellar birth and destruction in a factory") { gold() }
        add("§7模拟超新星爆发环境，锻造出只存在于恒星核心的物质" translatedTo "§7Simulating supernova explosion environments, forging materials found only in stellar cores") { gray() }
        add("§e从磁流体到时空晶体，制造宇宙中最神秘的终极材料" translatedTo "§eFrom magnetofluids to spacetime crystals, manufacturing the universe's most mysterious ultimate materials") { yellow() }
        add("§b20亿级别并行处理，同时进行无数个创世级反应" translatedTo "§bTwo billion level parallel processing, simultaneously conducting countless creation-level reactions") { aqua() }
        add("§c运行时整个结构如同活着的恒星，散发着令人敬畏的能量波动" translatedTo "§cEntire structure pulses like a living star during operation, emitting awe-inspiring energy waves") { red() }
        add("§9时空在这里被扭曲，物质在这里被重塑，法则在这里被改写" translatedTo "§9Spacetime twists here, matter reshapes here, physical laws rewritten here") { blue() }
        add(Star(1) + ("§6工业生产达到创世神的领域" translatedTo "§6Industrial production comes to the realm of creation gods")) { gold() }
    }
}
