package com.gtocore.data.lang;

import com.gtocore.common.machine.monitor.DisplayRegistry;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;

import static com.gtocore.data.lang.LangHandler.addCNEN;

final class MachineLang {

    public static void init() {
        addCNEN("gtocore.machine.programmablec_hatch.tooltip", "通过虚拟物品提供器来设置电路槽物品", "Set the circuit slot items through a virtual item provider");
        addCNEN("gtocore.machine.energy_loss", "能量损失：%s", "Energy loss: %s");
        addCNEN("gtocore.machine.highlight_module", "高亮显示模块位置", "Highlight module position.");
        addCNEN("gtocore.machine.structure_check", "更新结构检查(shift点击将重新检查结构)", "Update structure check (Clicking on Shift will recheck the structure).");
        addCNEN("gtocore.machine.overclock_configurator", "调整机器超频的最小时间", "Adjust the minimum time for machine overclocking.");
        addCNEN("gtocore.machine.thread", "同时处理至多 %s 种不同配方，每种配方至多 %s 个", "Processing up to %s different recipes simultaneously, with a maximum of %s for each recipe.");
        addCNEN("gtocore.machine.thread.0", "同时处理至多 %s 种不同配方", "Processing up to %s different recipes simultaneously");
        addCNEN("gtocore.machine.thread.1", "每种配方至多 %s 个", "With a maximum of %s for each recipe.");
        addCNEN("gtocore.machine.thread_hatch.tooltip.0", "可为机器提供 %s 线程的并行", "Can provide %s thread parallel processing for the machine.");
        addCNEN("gtocore.machine.rest_burn_time", "剩余燃烧时间: %s Tick", "Rest Burn Time %s Tick");
        addCNEN("gtocore.machine.total_time.duration", "额外耗时减免: %s", "Additional Duration Reduction: %s");
        addCNEN("gtocore.machine.block_conversion_room.am", "每次转化数量: %s", "Amount converted each time: %s");
        addCNEN("gtocore.machine.dimensionally_transcendent_plasma_forge.coil", "当前配方模式无法使用该线圈", "Current recipe mode cannot use this coil");
        addCNEN("gtocore.machine.duration_multiplier.tooltip", "耗时倍数: %s", "Duration Multiplication: %s");
        addCNEN("gtocore.machine.dyson_sphere.number", "该星系发射次数: %s / 10000", "Launch Times: %s / 10000");
        addCNEN("gtocore.machine.dyson_sphere.voltage", "最大能量输出: %s EU/t", "Maximum Energy Output: %s EU/t");
        addCNEN("gtocore.machine.efficiency.tooltip", "§7效率: §r%s", "§7Efficiency: §r%s");
        addCNEN("gtocore.machine.eut_multiplier.tooltip", "耗能倍数: %s", "Energy Consumption Multiplier: %s");
        addCNEN("gtocore.machine.eye_of_harmony.eu", "启动耗能: %s EU", "Startup Energy Consumption: %s EU");
        addCNEN("gtocore.machine.eye_of_harmony.helium", "氦储量: %smB", "Helium Storage: %smB");
        addCNEN("gtocore.machine.eye_of_harmony.hydrogen", "氢储量: %smB", "Hydrogen Storage: %smB");
        addCNEN("gtocore.machine.fission_reactor.cooler", "冷却组件数量: %s，相邻数: %s", "Number of Cooling Components: %s, Adjacent Count: %s");
        addCNEN("gtocore.machine.fission_reactor.damaged", "损坏: %s", "Damage: %s");
        addCNEN("gtocore.machine.fission_reactor.fuel", "燃料组件数量: %s，相邻数: %s", "Number of Fuel Components: %s, Adjacent Count: %s");
        addCNEN("gtocore.machine.fission_reactor.heat", "堆温: %s K", "Reactor Temperature: %s K");
        addCNEN("gtocore.machine.generator_array.wireless", "无线电网模式:", "Wireless Network Mode:");
        addCNEN("gtocore.machine.greenhouse.SkyLight", "当前光照: %s", "Current Illumination: %s");
        addCNEN("gtocore.machine.large_combustion_engine.Joint_boosted", "§b联合促燃中", "§bJoint Combustion Boosted");
        addCNEN("gtocore.machine.large_combustion_engine.supply_dinitrogen_tetroxide_to_boost", "提供四氧化二氮来联合促燃", "Provide Dinitrogen Tetroxide to joint combustion");
        addCNEN("gtocore.machine.large_steam_circuit_assembler.circuit", "已铭刻电路: %s", "Inscribed Circuit: %s");
        addCNEN("gtocore.machine.large_steam_circuit_assembler.engrave_circuit", "铭刻电路", "Engrave Circuit");
        addCNEN("gtocore.machine.laser.tooltip", "允许使用超高安能源仓", "Allows the use of ultra high amperage energy hatch");
        addCNEN("gtocore.machine.lightning_rod.tooltip.0", "上方避雷针被雷击后产生大量能量", "Large amounts of energy are generated after the lightning rod above is struck");
        addCNEN("gtocore.machine.lightning_rod.tooltip.1", "每0.5秒只能产生一次能量，且有一定几率破坏上方避雷针", "Can only generate energy once every 0.5 seconds, with a chance to damage the lightning rod above");
        addCNEN("gtocore.machine.lightning_rod.tooltip.2", "如果存储能量已满，机器将会爆炸", "If the stored energy is full, the machine will explode");
        addCNEN("gtocore.machine.mega_turbine.high_speed_mode", "高速模式:", "High Speed Mode:");
        addCNEN("gtocore.machine.module", "已安装的模块数量: %s", "Number of Installed Modules: %s");
        addCNEN("gtocore.machine.module.am", "已安装的模块数: %s", "Number of Installed Modules: %s");
        addCNEN("gtocore.machine.module.have", "该模块已成功安装", "This module has been successfully installed");
        addCNEN("gtocore.machine.module.null", "该模块未成功安装", "This module has not been successfully installed");
        addCNEN("gtocore.machine.multiple_recipes.tooltip", "支持跨配方并行", "Supports cross recipe parallel processing");
        addCNEN("gtocore.machine.neutron_accelerator.tooltip.0", "§6最大EU消耗: §r%s", "§6Max EU Consumption: §r%s");
        addCNEN("gtocore.machine.neutron_accelerator.tooltip.1", "§b每点EU都会转化为§e10~20-eV§b中子动能", "§bEach point of EU converts to §e10~20-eV§b neutron kinetic energy");
        addCNEN("gtocore.machine.neutron_activator.efficiency", "动能消耗倍速: %s", "Kinetic Energy Consumption Multiplier: %s");
        addCNEN("gtocore.machine.neutron_activator.ev", "当前中子动能: %seV", "Current Neutron Kinetic Energy: %seV");
        addCNEN("gtocore.machine.height", "高度: %s", "Height: %s");
        addCNEN("gtocore.machine.sensor.tooltip", "未反转时，数值低于最低值或高于最高值时输出0，介于两者之间时输出递增的1-15的红石信号", "When not inverted, outputs 0 when the value is below the minimum or above the maximum, or an increasing redstone signal between 1-14 when in between.");
        addCNEN("gtocore.machine.sensor.tooltip.0", "反转时，数值低于最低值或高于最高值时输出15，介于两者之间时输出递减的1-15的红石信号", "When inverted, outputs 15 when the value is above the maximum or below the minimum, or a decreasing redstone signal between 1-14 when in between.");
        addCNEN("gtocore.machine.sensor.invert.disabled.0", "红石输出: 普通", "Redstone Output: Normal");
        addCNEN("gtocore.machine.sensor.invert.disabled.1", "点击为以反转红石逻辑", "Click to toggle inverted redstone logic");
        addCNEN("gtocore.machine.sensor.invert.disabled.2", "介于所设定的最小值和最大值之间时传感器将发出红石信号", "When neutron kinetic energy is between the set minimum and maximum values, the sensor will emit a redstone signal");
        addCNEN("gtocore.machine.sensor.invert.enabled.0", "红石输出: 反转", "Redstone Output: Inverted");
        addCNEN("gtocore.machine.sensor.invert.enabled.1", "点击切换为普通红石逻辑", "Click to switch to normal redstone logic");
        addCNEN("gtocore.machine.sensor.invert.enabled.2", "介于所设定的最小值和最大值之外时传感器将发出红石信号", "When neutron kinetic energy is outside the set minimum and maximum values, the sensor will emit a redstone signal");
        addCNEN("gtocore.machine.neutron_sensor.tooltip.0", "基于§6中子动能§7输出红石信号，右键以打开GUI进行设置", "Outputs redstone signals based on §6Neutron Kinetic Energy§7, right-click to open GUI for settings");
        addCNEN("gtocore.machine.oc_amount", "超频次数: %s", "Overclocking Times: %s");
        addCNEN("gtocore.machine.off", "关闭", "Off");
        addCNEN("gtocore.machine.on", "打开", "On");
        addCNEN("gtocore.machine.pattern.error.tier", "§c必须使用同种等级方块§r", "§cMust use blocks of the same tier§r");
        addCNEN("gtocore.machine.primitive_magic_energy.tooltip.0", "无尽地吸收机器上方末地水晶的能量，如果能量已满，机器将会爆炸", "Endlessly absorbs the energy from ender crystals above the machine, if the energy capacity is full, the machine will explode.");
        addCNEN("gtocore.machine.primitive_magic_energy.tooltip.1", "每秒需输入同电压x电流的魔力，否则爆炸", "Requires a constant mana input of [X] EU/s (Voltage × Current). Failure cause an explosion.");
        addCNEN("gtocore.machine.processing_plant.mismatched", "配方等级与小机器等级不匹配", "Recipe tier does not match small machine tier");
        addCNEN("gtocore.machine.radiation_hatch.inhibition_dose", "抑制量: %s Sv", "Inhibition Amount: %s Sv");
        addCNEN("gtocore.machine.radiation_hatch.time", "时间: %s / %s Tick", "Time: %s / %s Tick");
        addCNEN("gtocore.machine.slaughterhouse.is_spawn", "实体生成模式: ", "Entity Generation: ");
        addCNEN("gtocore.machine.space_elevator.set_out", "启程", "Set Off");
        addCNEN("gtocore.machine.space_elevator.connected", "已连接正在运行的太空电梯", "Connected To A Running SpaceElevator");
        addCNEN("gtocore.machine.space_elevator.not_connected", "未连接正在运行的太空电梯", "Not Connected To A Running SpaceElevator");
        addCNEN("gtocore.machine.steam_parallel_machine.modification_oc", "修改超频次数: ", "Modify Overclocking Times: ");
        addCNEN("gtocore.machine.steam_parallel_machine.oc", "每次超频将减少2倍耗时和增加3倍蒸汽消耗", "Each time of overclocking will reduce time by 2 times and increase steam consumption by 3 times");
        addCNEN("gtocore.machine.total_time", "连续运行时间: %s Tick", "Continuous Running Time: %s Tick");
        addCNEN("gtocore.machine.uev_fusion_reactor.description", "核聚变反应堆MK-V是台大型多方块结构，用于融合元素形成更重的元素。它仅可使用UEV等级的能源仓。每个能源仓可增加160MEU的能量缓存，最大能量缓存为2560MEU。", "The Fusion Reactor MK V is a large multiblock structure used for fusing elements into heavier ones. It can only use UEV Energy Hatches. For every Hatch it has, its buffer increases by 160M EU, and has a maximum of 2560M.");
        addCNEN("gtocore.machine.uhv_fusion_reactor.description", "核聚变反应堆MK-IV是台大型多方块结构，用于融合元素形成更重的元素。它仅可使用UHV等级的能源仓。每个能源仓可增加80MEU的能量缓存，最大能量缓存为1280MEU。", "The Fusion Reactor MK-IV is a large multiblock structure used for fusing elements into heavier ones. It can only use UHV Energy Hatches. For every Hatch it has, its buffer increases by 80M EU, and has a maximum of 1280M.");
        addCNEN("gtocore.machine.wind_mill_turbine.actualPower", "能量输出: %s EU/t", "Energy Output: %s EU/t");
        addCNEN("gtocore.machine.wind_mill_turbine.tooltip.0", "风速低于转子最小值无法工作，高于最大值转子将快速损坏", "The rotor cannot operate below the minimum wind speed, and it will be damaged quickly above the maximum wind speed");
        addCNEN("gtocore.machine.wind_mill_turbine.tooltip.1", "风速加成: 雨天x1.5，雷雨天x2，风速决定最高转速", "Wind Speed Bonus: x1.5 for rainy days, x2 for thunderstorms, the wind speed determines the maximum rotation speed");
        addCNEN("gtocore.machine.wind_mill_turbine.wind", "当前风力: %s", "Current Wind Speed: %s");
        addCNEN("gtocore.machine.water_purification_plant.bind", "已绑定机器:", "Bound Machine:");
        addCNEN("gtocore.machine.absolute_baryonic_perfection_purification_unit.items", "本次循环组合:\n%s, %s", "Current combination for this cycle:\n%s, %s");
        addCNEN("gtocore.machine.residual_decontaminant_degasser_purification_unit.fluids", "本次循环需求:\n%s", "Current cycle requirements:\n%s");
        addCNEN("gtocore.machine.wireless_data_hatch.bind", "无线数据仓绑定完成", "Wireless data hatch binding completed.");
        addCNEN("gtocore.machine.wireless_data_transmitter_hatch.tooltip.0", "需要使用闪存右键无线光学数据靶仓和无线数据源仓进行绑定", "Use the flash drive to right-click and bind the wireless optical data target hatch and the wireless data source hatch.");
        addCNEN("gtocore.machine.wireless_data_transmitter_hatch.to_bind", "源仓数据读取完成，请右键靶仓进行绑定", "Source hatch data reading completed, please right-click the target hatch to bind.");
        addCNEN("gtocore.machine.wireless_data_transmitter_hatch.bind", "已绑定无线光学数据靶仓(%s)", "Bound wireless optical data target hatch (%s).");
        addCNEN("gtocore.machine.wireless_data_transmitter_hatch.unbind", "未绑定无线光学数据靶仓", "Wireless optical data target hatch not bound.");
        addCNEN("gtocore.machine.wireless_data_receiver_hatch.tooltip.0", "需要使用闪存右键无线光学数据靶仓和无线光学数据源仓进行绑定", "Use the flash drive to right-click and bind the wireless optical data target hatch and the wireless optical data source hatch.");
        addCNEN("gtocore.machine.wireless_data_receiver_hatch.to_bind", "靶仓数据读取完成，请右键源仓进行绑定", "Target hatch data reading completed, please right-click the source hatch to bind.");
        addCNEN("gtocore.machine.wireless_data_receiver_hatch.bind", "已绑定无线光学数据源仓(%s)", "Bound wireless optical data source hatch (%s).");
        addCNEN("gtocore.machine.wireless_data_receiver_hatch.unbind", "未绑定无线光学数据源仓", "Wireless optical data source hatch not bound.");
        addCNEN("gtocore.machine.need", "需要: %s", "Need: %s");
        addCNEN("gtocore.machine.advanced_infinite_driller.not_fluid_head", "无钻头", "No drill head");
        addCNEN("gtocore.machine.advanced_infinite_driller.heat", "最大温度: %sK / 工作温度: %sK", "Max Temperature: %sK / Operating Temperature: %sK");
        addCNEN("gtocore.machine.current_temperature", "当前温度: %sK", "Current Temperature: %sK");
        addCNEN("gtocore.machine.advanced_infinite_driller.drilled_fluid", "流体: %s 产量: %s", "Fluid: %s Output: %s");
        addCNEN("gtocore.machine.kuangbiao_one_giant_nuclear_fusion_reactor.tooltip.0", "它所模拟的，是亘古不变的光芒", "What it simulates is an eternal light");
        addCNEN("gtocore.machine.steam.tooltip.1", "默认支持%s等级及以下的配方处理", "default can process &s-Tier recipes and below, processing time is 1.5 times");
        addCNEN("gtocore.machine.steam.tooltip.2", "安装大型蒸汽输入仓后提升一个配方等级，并解锁超频功能", "After installing a large steam input hatch, upgrade one recipe tier and unlock the overclocking function");
        addCNEN("gtocore.machine.mana_stored", "魔力总量：%s", "Total Mana: %s");
        addCNEN("gtocore.machine.mana_consumption", "最大魔力消耗：%s", "Max Mana Consumption Rate: %s");
        addCNEN("gtocore.machine.mana_production", "最大魔力产出：%s", "Max Mana Production Rate: %s");
        addCNEN("gtocore.machine.mana_input", "魔力输入：%s", "Mana input: %s");
        addCNEN("gtocore.machine.mana_output", "魔力输出：%s", "Mana output: %s");
        addCNEN("gtocore.machine.mana_eu", "支持电力配方，转换比1:1", "Supports EU recipes, conversion ratio 1:1");
        addCNEN("gtocore.machine.processing_array.tooltip.0", "玻璃等级限制了内部机器等级", "Tier is limited by glass grade");
        addCNEN("gtocore.machine.processing_array.tooltip.1", "并行数由内部机器数量决定", "Parallel are determined by the number of internal machines");
        addCNEN("gtocore.machine.maximum_amount", "最大数量：%s", "Maximum amount: %s");
        addCNEN("gtocore.machine.binding_amount", "绑定数量：%s", "Binding amount: %s");
        addCNEN("gtocore.machine.accelerate_hatch.tooltip.0", "加速机器运行", "Accelerates machine operation");
        addCNEN("gtocore.machine.accelerate_hatch.tooltip.1", "加速仓的等级低于机器配方等级时加速效果减弱", "Acceleration effect is reduced when the accelerator hatch tier is lower than the machine recipe tier");
        addCNEN("gtocore.machine.recipe.run", "运行%s配方时：", "When running the % recipe: ");
        addCNEN("gtocore.machine.parallel", "最大并行数：%s", "Maximum number of parallel: %s");
        addCNEN("gtocore.machine.repeated_recipes", "并行重复配方[%s]", "Parallel repeated recipes [%s]");
        addCNEN("gtocore.machine.cwut_modification", "算力修正系数：%s", "Hashrate correction factor: %s");
        addCNEN("gtocore.machine.tag_filter.tag_config_title", "标签过滤配置", "Tag Filtering Configuration");
        addCNEN("gtocore.machine.tag_filter.tooltip.0", "* 表示通配符 () 表示优先", "* Indicates a wildcard () Indicates Priority");
        addCNEN("gtocore.machine.tag_filter.tooltip.1", "& = 逻辑与 | = 逻辑或 ^ = 逻辑异或", "& = Logic with | = Logic or ^ = Logical XOR");
        addCNEN("gtocore.machine.me_dual_hatch_stock.turns.0", "自动拉取关", "Auto-Pull Disable");
        addCNEN("gtocore.machine.me_dual_hatch_stock.turns.1", "自动拉取流体/物品", "Auto-Pull Fluid or Item");
        addCNEN("gtocore.machine.me_dual_hatch_stock.turns.2", "仅拉取物品", "Auto-Pull Item");
        addCNEN("gtocore.machine.me_dual_hatch_stock.turns.3", "仅拉取流体", "Auto-Pull Fluid");
        addCNEN("gtocore.machine.me_dual_hatch_stock.tooltip.0", "可标记64种流体或物品", "Keeps 64 fluid or item types in stock");
        addCNEN("gtocore.machine.me_dual_hatch_stock.tooltip.1", "直接从ME网络抽取流体或物品", "Retrieves fluids or item directly from the ME network");
        addCNEN("gtocore.machine.me_dual_hatch_stock.data_stick.name", "§oME Stock Input Dual Hatch Config Data", "§oME库存输入总成配置数据");
        addCNEN("gtocore.machine.mega_steam_turbine.tooltip.0", "运行效率相当于16台同类大型涡轮，可使用更多动力仓", "Operating efficiency is equivalent to 16 large turbines of the same type, can use more power hatch");
        addCNEN("gtocore.machine.mega_steam_turbine.tooltip.1", "可安装转子仓，从中自动取出转子安装到空转子支架", "Rotors can be installed in the rotor chamber, automatically extracting rotor for installation onto empty rotor brackets");
        addCNEN("gtocore.machine.mega_steam_turbine.tooltip.2", "可启用高速模式，进一步提升运行速度，高速模式下，运行速度提升为3倍，代价是维护问题出现几率与涡轮耐久损耗均变为12倍", "High speed mode can be enabled to further increase operating speed; in high speed mode, operating speed x3, at the cost of increasing maintenance issue probability and turbine durability loss being 12 times");
        addCNEN("gtocore.machine.scanning", "扫描中...", "Scanning...");
        addCNEN("gtocore.machine.analysis", "分析中...", "Analysing...");
        addCNEN("gtocore.machine.assembling", "装配中...", "Assembling...");
        addCNEN("gtocore.machine.wireless_mode", "无线模式", "Wireless Mode");
        addCNEN("gtocore.machine.alchemical_device.1", "嬗变中", "Transmutation");
        addCNEN("gtocore.machine.alchemical_device.2", "完美嬗变中", "Perfect Transmutation");
        addCNEN("gtocore.machine.monitor.no_information", "没有可显示的信息", "No information to display");
        addCNEN("gtocore.machine.monitor.mana.current", "当前网络魔力: ", "Current Network Mana: ");
        addCNEN("gtocore.machine.monitor.mana.pool.0", "(约", "(Approx. ");
        addCNEN("gtocore.machine.monitor.mana.pool.1", "池)", "Pools)");
        addCNEN("gtocore.machine.monitor.mana.pool.2", "池/s)", "Pools per Second)");
        addCNEN("gtocore.machine.monitor.mana.increase", "每秒魔力增长: ", "Mana Growth Per Second: ");
        addCNEN("gtocore.machine.monitor.mana.decrease", "每秒魔力消耗: ", "Mana Consumption Per Second: ");
        addCNEN("gtocore.machine.monitor.cwu.capacity", "当前网络算力: %s/%s CWU", "Current Network Hashrate: %s/%s CWU");
        addCNEN("gtocore.machine.monitor.cwu.used", "（已使用: %s CWU）", "(Used: %s CWU)");
        addCNEN("gtocore.machine.monitor.cwu.no_container", "警告：算力信息缺失！", "Warning: Hashrate information is missing!");
        addCNEN("gtocore.machine.monitor.eu.no_container", "警告：无限能源塔绑定信息缺失！", "Warning: Infinite Energy Tower binding information is missing!");
        addCNEN("gtocore.machine.monitor.eu.fullness", "能量塔能量存量: %s%%", "Energy Tower Energy Storage: %s%%");
        addCNEN("gtocore.machine.monitor.priority", "调整显示的优先级", "Adjust the display priority");
        addCNEN("gtocore.machine.monitor.adjust_component.move_up", "上移", "Move Up");
        addCNEN("gtocore.machine.monitor.adjust_component.move_down", "下移", "Move Down");
        addCNEN("gtocore.machine.monitor.adjust_component.switch", "切换显示", "Toggle Display");
        addCNEN("gtocore.machine.monitor.adjust_component.reset", "清除显示顺序配置", "Clear Display Order Configuration");
        addCNEN("gtocore.machine.monitor.custom_info.tooltip", "自定义信息", "Custom Information");
        addCNEN("gtocore.machine.monitor.custom_info.code_input_tooltip.1", "复制§a格式化代码§r到剪贴板", "Copy §aformatting code§r to clipboard");
        addCNEN("gtocore.machine.monitor.custom_info.code_input_tooltip.2", "§l格式化代码（Formatting code）§r，又称§l颜色代码（Color code）§r，能使在游戏中加入含颜色和格式信息。",
                "§lFormatting code§r, also known as §lcolor code§r, allows adding color and formatting information in the game.");
        addCNEN("gtocore.machine.monitor.custom_info.code_input_tooltip.3", "请前往§dMinecraft Wiki§r了解更多关于格式化代码的信息。",
                "Please visit the §dMinecraft Wiki§r for more information about formatting codes.");
        addCNEN("gtocore.machine.monitor.ae.status.no_grid", "警告：无法连接到AE网络！", "Warning: Unable to connect to AE network!");
        addCNEN("gtocore.machine.monitor.ae.status.no_config", "无配置信息", "No configuration information");
        addCNEN("gtocore.machine.monitor.ae.status.0", "正在监视的物品：%s", "Monitored Items: %s");
        addCNEN("gtocore.machine.monitor.ae.status.1", "正在监视的流体：%s", "Monitored Fluids: %s");
        addCNEN("gtocore.machine.monitor.ae.amount", "总量统计：%s", "Total Amount: %s");
        addCNEN("gtocore.machine.monitor.ae.stat.title", "平均吞吐量统计", "Average Throughput Statistics");
        addCNEN("gtocore.machine.monitor.ae.stat.minute", "近一分钟：%s", "Last Minute: %s");
        addCNEN("gtocore.machine.monitor.ae.stat.hour", "近一小时：%s", "Last Hour: %s");
        addCNEN("gtocore.machine.monitor.ae.stat.day", "近一天：%s", "Last Day: %s");
        addCNEN("gtocore.machine.monitor.ae.stat.remaining_time", "预计耗尽时间：%s", "Estimated Depletion Time: %s");
        addCNEN("gtocore.machine.monitor.ae.set_filter", "设置过滤器", "Set Filter");
        addCNEN("gtocore.machine.basic_monitor.tooltip.1", "将多个监控器连接在一起，形成一个多功能监控器", "Connect multiple monitors together to form a multifunctional monitor");
        addCNEN("gtocore.machine.basic_monitor.tooltip.2", "在连接的监控器中添加§b组件§r来显示不同的信息", "Add §bcomponents§r in connected monitors to display different information");
        addCNEN("gtocore.machine.boiler.tooltip.warning", "§c警告§r：请勿在锅炉内积攒过多的蒸汽！", "§cWarning: §rDo not accumulate too much steam in the boiler!");
        addCNEN("gtocore.machine.monitor.ae.cpu.usage", "正在工作的CPU: %s 个（总数： %s 个）", "Working CPUs: %s (Total: %s)");
        addCNEN("gtocore.machine.monitor.ae.cpu.monitored", "正在监视的CPU: #%s - %s", "Monitored CPU: #%s - %s");
        addCNEN("gtocore.machine.monitor.ae.cpu.list", "CPU列表", "CPU List");
        addCNEN("gtocore.machine.monitor.ae.cpu.set.1", "设置监视的CPU", "Set Monitored CPU");
        addCNEN("gtocore.machine.monitor.ae.cpu.set.2", "§c警告§r：网络中CPU变动时，需要重新选择！", "Warning: When the CPU changes in the network, you need to reselect!");
        addCNEN("gtocore.machine.machine_monitor.slot", "请放入机器坐标信息卡", "Insert Machine Coordinate Card");
        addCNEN("gtocore.part.extendae.tag_filter.tooltip", "左键将标签添加到过滤器中，右键复制标签到剪贴板", "Left-click to add tags to the filter, right-click to copy tags to the clipboard");
        addCNEN("gtocore.part.extendae.tag_filter.whitelist.tooltip", "可放入目标，选择白名单的标签过滤器", "Can place target, select whitelist tag filter");
        addCNEN("gtocore.part.extendae.tag_filter.blacklist.tooltip", "可放入目标，选择黑名单的标签过滤器", "Can place target, select blacklist tag filter");
        addCNEN("gtocore.machine.area_destruction_tools.detonate_instruction", "起爆", "Detonate instruction");
        addCNEN("gtocore.machine.area_destruction_tools.missing_items", "缺失物品", "Missing items");
        addCNEN("gtocore.machine.area_destruction_tools.model.0", "模式：空", "Model: Empty");
        addCNEN("gtocore.machine.area_destruction_tools.model.1", "模式：球", "Model: Sphere");
        addCNEN("gtocore.machine.area_destruction_tools.model.2", "模式：圆柱", "Model: Cylinder");
        addCNEN("gtocore.machine.area_destruction_tools.model.3", "模式：区块", "Model: Chunk");
        addCNEN("gtocore.machine.area_destruction_tools.model.4", "模式：指定区域", "Model: Designated Area");
        addCNEN("gtocore.machine.area_destruction_tools.explosive_yield", "爆炸当量：%s", "Explosive Yield: %s");
        addCNEN("gtocore.machine.large_steam_solar_boiler.size", "尺寸：%s × %s", "Size: %s × %s");
        addCNEN("gtocore.machine.large_steam_solar_boiler.heat_collector_pipe", "有效集热管数量：%s", "Number of effective Heat collector pipe: %s");
        addCNEN("gtocore.machine.large_steam_solar_boiler.steam_production", "蒸汽产量：%s / s", "Steam production: %s / s");

        addSingleMachineTooltips();

        DisplayRegistry.registerLanguage();
    }

    private static void addSingleMachineTooltips() {
        // machines defined in GTM
        SingleMachineTooltipBuilder.createGTM("electric_furnace")
                .epic("§7工业级烤面包机", "§7Industrial-grade Toaster")
                .legendary("§7太阳核心模拟器", "§7Solar Core Simulator");
        SingleMachineTooltipBuilder.createGTM("alloy_smelter")
                .epic("§7合金搅拌大师", "§7Alloy Mixing Master")
                .legendary("§7纳米级材料融合装置", "§7Nanoscale Material Fusion Device");
        SingleMachineTooltipBuilder.createGTM("arc_furnace")
                .epic("§7雷电法王", "§7Lightning Arc Master")
                .legendary("§7宙斯之怒", "§7Wrath of Zeus");
        SingleMachineTooltipBuilder.createGTM("assembler")
                .primitive("§7拼凑小物件", "§7Cobbling small items")
                .epic("§7自动化工厂核心", "§7Automated Factory Core")
                .legendary("§7自我复制的纳米机器人集群", "§7Self-replicating Nanobot Swarm");
        SingleMachineTooltipBuilder.createGTM("autoclave")
                .epic("§7高压灭菌Pro Max", "§7High-pressure Sterilization Pro Max")
                .legendary("§7超立方体相变舱", "§7Hypercube Phase Change Chamber");
        SingleMachineTooltipBuilder.createGTM("bender")
                .epic("§7金属芭蕾舞者", "§7Metal Ballerina")
                .legendary("§7黑洞级别的弯折力量", "§7Blackhole-level Bending Force");
        SingleMachineTooltipBuilder.createGTM("brewery")
                .epic("§7量子酿酒师", "§7Quantum Brewer")
                .legendary("§7醉倒泰坦的佳酿", "§7Titan-intoxicating Nectar");
        SingleMachineTooltipBuilder.createGTM("canner")
                .epic("§7罐头喷射器", "§7Can Launcher")
                .legendary("§7时空封装装置", "§7Spacetime Sealing Device");
        SingleMachineTooltipBuilder.createGTM("centrifuge")
                .epic("§7超速旋转咖啡杯", "§7Hyperspin Coffee Cup")
                .legendary("§7人工中子星核心", "§7Artificial Neutron Star Core");
        SingleMachineTooltipBuilder.createGTM("chemical_bath")
                .epic("§7元素温泉", "§7Elemental Hot Spring")
                .legendary("§7地球原始汤", "§7Primordial Soup");
        SingleMachineTooltipBuilder.createGTM("chemical_reactor")
                .primitive("§7简单物质化合", "§7Simple substance combination")
                .epic("§7分子舞池", "§7Molecular Dance Floor")
                .legendary("§7炼金术师的终极梦想", "§7Ultimate Dream of Alchemists");
        SingleMachineTooltipBuilder.createGTM("compressor")
                .epic("§7超级压力机", "§7Super Compressor")
                .legendary("§7人造白矮星核心", "§7Artificial White Dwarf Core");
        SingleMachineTooltipBuilder.createGTM("cutter")
                .epic("§7光子切割大师", "§7Photon Cutting Master")
                .legendary("§7因果律切割刀", "§7Causality Cutting Blade");
        SingleMachineTooltipBuilder.createGTM("distillery")
                .epic("§7精密提纯装置", "§7Precision Distillation Unit")
                .legendary("§7绝对纯净领域", "§7Absolute Pure Realm");
        SingleMachineTooltipBuilder.createGTM("electrolyzer")
                .epic("§7雷霆分解者", "§7Thunder Splitter")
                .legendary("§7强相互作用力破解器", "§7Strong Force Decryptor");
        SingleMachineTooltipBuilder.createGTM("electromagnetic_separator")
                .epic("§7磁暴分离器", "§7Magnetic Storm Separator")
                .legendary("§7希格斯场操纵仪", "§7Higgs Field Manipulator");
        SingleMachineTooltipBuilder.createGTM("extractor")
                .epic("§7终极榨汁机", "§7Ultimate Juicer")
                .legendary("§7真空零点能提取器", "§7Zero-point Energy Extractor");
        SingleMachineTooltipBuilder.createGTM("extruder")
                .epic("§7无限面条机", "§7Infinite Noodle Maker")
                .legendary("§7时空挤压装置", "§7Spacetime Extrusion Device");
        SingleMachineTooltipBuilder.createGTM("fermenter")
                .epic("§7微生物狂欢派对", "§7Microorganism Rave Party")
                .legendary("§7有机体超进化池", "§7Organism Hyper-evolution Pool");
        SingleMachineTooltipBuilder.createGTM("fluid_heater")
                .epic("§7等离子加热器", "§7Plasma Heater")
                .legendary("§7恒星内核模拟装置", "§7Stellar Core Simulator");
        SingleMachineTooltipBuilder.createGTM("fluid_solidifier")
                .primitive("§7静置液体形成固体", "§7Liquid to Solid by Settling")
                .epic("§7瞬间冷冻大师", "§7Instant Freeze Master")
                .legendary("§7绝对零度生成器", "§7Absolute Zero Generator");
        SingleMachineTooltipBuilder.createGTM("forge_hammer")
                .epic("§7雷神之锤", "§7Thor's Hammer")
                .legendary("§7行星级锻压装置", "§7Planetary Forging Device");
        SingleMachineTooltipBuilder.createGTM("forming_press")
                .epic("§7上帝之手", "§7God's Hand")
                .legendary("§7现实重塑器", "§7Reality Reshaper");
        SingleMachineTooltipBuilder.createGTM("lathe")
                .primitive("§7以稍高的效率生产杆", "§7Produces Rods a little more efficiently")
                .epic("§7比金刚石更加坚硬", "§7Harder than diamond")
                .legendary("§7特种钢超级飞轮", "§7Special Steel Super Flywheel");
        SingleMachineTooltipBuilder.createGTM("scanner")
                .epic("§7量子扫描仪", "§7Quantum Scanner")
                .legendary("§7超维信息读取器", "§7Hyperdimensional Information Reader");
        SingleMachineTooltipBuilder.createGTM("mixer")
                .epic("§7混沌搅拌机", "§7Chaos Mixer")
                .legendary("§7大爆炸模拟器", "§7Big Bang Simulator");
        SingleMachineTooltipBuilder.createGTM("ore_washer")
                .epic("§7矿物SPA中心", "§7Ore SPA Center")
                .legendary("§7量子级纯净清洗", "§7Quantum-level Purification");
        SingleMachineTooltipBuilder.createGTM("packer")
                .primitive("§7物品封装", "§7Item Packing")
                .epic("§7空间压缩包装机", "§7Space Compression Packer")
                .legendary("§7二向箔包装技术", "§7Dimensional Foil Packing");
        SingleMachineTooltipBuilder.createGTM("polarizer")
                .epic("§7磁极大师", "§7Polarity Master")
                .legendary("§7量子自旋操纵仪", "§7Quantum Spin Manipulator");
        SingleMachineTooltipBuilder.createGTM("laser_engraver")
                .epic("§7光子雕刻师", "§7Photon Engraver")
                .legendary("§7时空蚀刻机", "§7Spacetime Etcher");
        SingleMachineTooltipBuilder.createGTM("sifter")
                .epic("§7量子筛分仪", "§7Quantum Sifter")
                .legendary("§7平行宇宙过滤器", "§7Parallel Universe Filter");
        SingleMachineTooltipBuilder.createGTM("thermal_centrifuge")
                .epic("§7等离子离心机", "§7Plasma Centrifuge")
                .legendary("§7超高温物质分离器", "§7Ultra-high Temperature Separator");
        SingleMachineTooltipBuilder.createGTM("wiremill")
                .primitive("§7简单地生产导线", "§7Simple Wire Production")
                .epic("§7拉力约一百万牛", "§7~1MN Tension")
                .legendary("§7彻底摧毁范德华力", "§7Van der Waals Force Destroyer");
        SingleMachineTooltipBuilder.createGTM("circuit_assembler")
                .epic("§7纳米级电路工坊", "§7Nanoscale Circuit Workshop")
                .legendary("§7量子计算机组装线", "§7Quantum Computer Assembly Line");
        SingleMachineTooltipBuilder.createGTM("macerator")
                .epic("§7分子破碎机", "§7Molecular Grinder")
                .legendary("§7物质解构器", "§7Matter Deconstructor");
        // tooltips for higher tier of gas collector and rock crusher has written by GTM

        // machines defined in GTO
        SingleMachineTooltipBuilder.createGTO("arc_generator")
                .basic("§7摩擦生电", "§7Friction Electricity")
                .elite("§7特斯拉线圈", "§7Tesla Coil")
                .ultimate("§7人工闪电风暴", "§7Artificial Lightning Storm")
                .epic("§7等离子电弧矩阵", "§7Plasma Arc Matrix")
                .legendary("§7恒星能量提取器", "§7Stellar Energy Extractor");
        SingleMachineTooltipBuilder.createGTO("dehydrator")
                .basic("§7脱水，脱水！", "§7Dehydrate, Dehydrate!")
                .elite("§7沙漠风暴模拟器", "§7Desert Storm Simulator")
                .ultimate("§7绝对干燥领域", "§7Absolute Dry Zone")
                .epic("§7真空干燥奇点", "§7Vacuum Drying Singularity")
                .legendary("§7水分子消除装置", "§7Water Molecule Eliminator");
        SingleMachineTooltipBuilder.createGTO("unpacker")
                .primitive("§7粗糙地拆开", "§7Crude Unpacking")
                .basic("§7标准拆包操作", "§7Standard Unpacking")
                .elite("§7精准解包大师", "§7Precision Unpacking Master")
                .ultimate("§7分子级拆解", "§7Molecular Disassembly")
                .epic("§7空间解压缩装置", "§7Spatial Decompression Unit")
                .legendary("§7因果律包装破解器", "§7Causality Package Cracker");
        SingleMachineTooltipBuilder.createGTO("cluster")
                .basic("§7生产薄片", "§7Produces Foils")
                .elite("§7精密镀膜机", "§7Precision Coating Machine")
                .ultimate("§7原子级沉积", "§7Atomic Deposition")
                .epic("§7量子层积装置", "§7Quantum Lamination Device")
                .legendary("§7现实编织者", "§7Reality Weaver");
        SingleMachineTooltipBuilder.createGTO("rolling")
                .basic("§7压扁成型", "§7Flat Rolling")
                .elite("§7液压精密轧制", "§7Hydraulic Precision Rolling")
                .ultimate("§7分子级压延", "§7Molecular Calendering")
                .epic("§7时空碾压机", "§7Spacetime Roller")
                .legendary("§7维度碾压装置", "§7Dimensional Compactor");
        SingleMachineTooltipBuilder.createGTO("laminator")
                .basic("§7绝缘套皮，安全用电！", "§7Insulation Coating, Safe Electricity!")
                .elite("§7纳米涂层机", "§7Nano Coating Machine")
                .ultimate("§7量子层压技术", "§7Quantum Lamination")
                .epic("§7超导覆膜装置", "§7Superconducting Coating Unit")
                .legendary("§7绝对绝缘屏障生成器", "§7Absolute Insulation Barrier Generator");
        SingleMachineTooltipBuilder.createGTO("loom")
                .primitive("§7只是把导线捆在一起", "§7Just Bundling Wires Together")
                .basic("§7自动化编织", "§7Automated Weaving")
                .elite("§7精密纺织大师", "§7Precision Textile Master")
                .ultimate("§7分子级编织", "§7Molecular Weaving")
                .epic("§7量子织布机", "§7Quantum Loom")
                .legendary("§7命运纺纱机", "§7Fate Spinning Wheel");
        SingleMachineTooltipBuilder.createGTO("laser_welder")
                .basic("§7古法电焊技术", "§7Ancient Welding Technique")
                .elite("§7光子焊接大师", "§7Photon Welding Master")
                .ultimate("§7量子纠缠焊接", "§7Quantum Entanglement Welding")
                .epic("§7时空缝合装置", "§7Spacetime Stitcher")
                .legendary("§7因果律焊接器", "§7Causality Welder");
        SingleMachineTooltipBuilder.createGTO("world_data_scanner")
                .basic("§7区块信息读取器", "§7Chunk Data Reader")
                .elite("§7维度特征分析仪", "§7Dimensional Feature Analyzer")
                .ultimate("§7现实数据采集装置", "§7Reality Data Collection Unit")
                .epic("§7量子态世界扫描仪", "§7Quantum State World Scanner")
                .legendary("§7宇宙信息库终端", "§7Cosmic Database Terminal");
        SingleMachineTooltipBuilder.createGTO("vacuum_pump")
                .steam("§7简易抽气装置", "§7Simple Air Pump", "§7过热蒸汽真空泵", "§7Superheated Steam Vacuum Pump")
                .add("§7基础真空发生器", "§7Basic Vacuum Generator", GTValues.LV)
                .add("§7工业级真空系统", "§7Industrial Vacuum System", GTValues.MV)
                .add("§7高效真空抽取装置", "§7High-efficiency Vacuum Extraction Unit", GTValues.HV);
    }

    private record SingleMachineTooltipBuilder(String namespace, String type) {

        private static SingleMachineTooltipBuilder createGTM(String machineType) {
            return new SingleMachineTooltipBuilder(GTCEu.MOD_ID, machineType);
        }

        private static SingleMachineTooltipBuilder createGTO(String machineType) {
            return new SingleMachineTooltipBuilder(GTOCore.MOD_ID, machineType);
        }

        private SingleMachineTooltipBuilder add(String cn, String en, int tier) {
            addCNEN(namespace + ".machine." + GTValues.VN[tier].toLowerCase() + "_" + type + ".tooltip", cn, en);
            return this;
        }

        private SingleMachineTooltipBuilder add(String cn, String en, int tierMin, int tierMax) {
            for (int tier = tierMin; tier <= tierMax; tier++) {
                add(cn, en, tier);
            }
            return this;
        }

        private SingleMachineTooltipBuilder steam(String lpCn, String lpEn, String hpCn, String hpEn) {
            addCNEN(namespace + ".machine.lp_steam_" + type + ".tooltip", lpCn, lpEn);
            addCNEN(namespace + ".machine.hp_steam_" + type + ".tooltip", hpCn, hpEn);
            return this;
        }

        private SingleMachineTooltipBuilder primitive(String cn, String en) {
            // primitive machines are all from GTO
            addCNEN(GTOCore.MOD_ID + ".machine." + GTValues.VN[GTValues.ULV].toLowerCase() + "_" + type + ".tooltip", cn, en);
            return this;
        }

        private SingleMachineTooltipBuilder basic(String cn, String en) {
            return add(cn, en, GTValues.LV, GTValues.EV);
        }

        private SingleMachineTooltipBuilder elite(String cn, String en) {
            return add(cn, en, GTValues.IV, GTValues.ZPM);
        }

        private SingleMachineTooltipBuilder ultimate(String cn, String en) {
            return add(cn, en, GTValues.UV);
        }

        private SingleMachineTooltipBuilder epic(String cn, String en) {
            return add(cn, en, GTValues.UHV, GTValues.UXV);
        }

        private SingleMachineTooltipBuilder legendary(String cn, String en) {
            return add(cn, en, GTValues.OpV);
        }
    }
}
