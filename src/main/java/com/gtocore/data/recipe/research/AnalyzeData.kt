package com.gtocore.data.recipe.research

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.info
import com.gtocore.common.data.translation.section
import com.gtocore.data.recipe.builder.research.ExResearchManager.writeAnalyzeResearchToMap

import com.fast.fastcollection.O2OOpenCacheHashMap
import com.gregtechceu.gtceu.GTCEu
import com.gtolib.api.lang.CNEN

object AnalyzeData : AutoInitialize<AnalyzeData>() {

    val langMap: Map<String, CNEN> = if (GTCEu.isDataGen()) O2OOpenCacheHashMap() else emptyMap()

    private val researchTooltips = mutableMapOf<Int, ComponentListSupplier>()

    fun getTooltip(researchKey: Int): ComponentListSupplier? = researchTooltips[researchKey]

    private var itemRegister = false

    override fun init() {
        initErrorResearchData()
        initSampleResearchData()
        initBasicResearchData()
        itemRegister = true
    }

    private fun initErrorResearchData() {
        addResearch(
            "error1",
            "§k1§r错误§k1§r",
            "§k1§rError§k1§r",
            0,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.error1")
                info("只是一个意外罢了" translatedTo "It was just an accident")
            },
        )
        addResearch(
            "error2",
            "§k22§r错误§k22§r",
            "§k22§rError§k22§r",
            0,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.double_error")
                info("只是两个意外罢了" translatedTo "It was just two accidents")
            },
        )
        addResearch(
            "error3",
            "§k333§r错误§k333§r",
            "§k333§rError§k333§r",
            0,
            3,
        )
        addResearch(
            "error4",
            "§k4444§r错误§k4444§r",
            "§k4444§rError§k4444§r",
            0,
            4,
        )
        addResearch(
            "error5",
            "§k55555§r错误§k55555§r",
            "§k55555§rError§k55555§r",
            0,
            4,
        )
    }

    private fun initSampleResearchData() {
        addResearch(
            "基础材料研究",
            "Basic Material Study",
            1,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.basic_material_study")
                section("研究金属与矿物特性" translatedTo "Study metal and mineral properties")
            },
        )
        addResearch(
            "能量传输研究",
            "Energy Transmission Research",
            1,
            2,
        )
    }

    private fun initBasicResearchData() {
        // ==================== dataTier 1: 行星系探索基础（15项） ====================
        // 行星观测与定位
        addResearch(
            "planetary_telescope_calibration",
            "行星望远镜校准",
            "Planetary Telescope Calibration",
            1,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.planetary_telescope_calibration")
                info("优化光学望远镜参数，提高行星观测精度" translatedTo "Optimize optical telescope parameters to improve planetary observation accuracy")
            },
        )
        addResearch("solar_system_orbit_modeling", "太阳系轨道建模", "Solar System Orbit Modeling", 1, 1)
        addResearch("planetary_surface_feature_mapping", "行星表面特征测绘", "Planetary Surface Feature Mapping", 1, 1)
        addResearch("stellar_reference_frame", "恒星参考系建立", "Stellar Reference Frame Establishment", 1, 1)
        // 基础航天材料
        addResearch("aluminum_magnesium_alloy_forging", "铝镁合金锻造", "Aluminum-Magnesium Alloy Forging", 1, 2)
        addResearch(
            "vacuum_resistant_material_testing",
            "耐真空材料测试",
            "Vacuum-Resistant Material Testing",
            1,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.vacuum_resistant_material_testing")
                info("检测材料在真空环境下的物理性能稳定性" translatedTo "Detect the physical performance stability of materials in vacuum environment")
            },
        )
        addResearch("low_temp_resistant_polymer", "耐低温聚合物研发", "Low-Temperature Resistant Polymer R&D", 1, 2)
        // 行星探测准备
        addResearch("unmanned_probe_battery_tech", "无人探测器电池技术", "Unmanned Probe Battery Technology", 1, 2)
        addResearch("simple_thermal_control_system", "简易热控制系统", "Simple Thermal Control System", 1, 2)
        // 星际通信入门
        addResearch("radio_wave_propagation_in_space", "空间无线电波传播", "Radio Wave Propagation in Space", 1, 3)
        addResearch(
            "probe_data_transmission_protocol",
            "探测器数据传输协议",
            "Probe Data Transmission Protocol",
            1,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.probe_data_transmission_protocol")
                info("制定探测器与地面站的通信数据格式标准" translatedTo "Formulate communication data format standards between probes and ground stations")
            },
        )
        addResearch("signal_noise_reduction_tech", "信号降噪技术", "Signal Noise Reduction Technology", 1, 3)
        // 行星资源初探
        addResearch("lunar_soil_composition_analysis", "月球土壤成分分析", "Lunar Soil Composition Analysis", 1, 3)
        addResearch("planetary_resource_signature_detection", "行星资源特征探测", "Planetary Resource Signature Detection", 1, 3)
        addResearch("water_ice_signature_recognition", "水冰特征识别", "Water Ice Signature Recognition", 1, 3)

        // ==================== dataTier 2: 行星探测深化（25项） ====================
        // 观测技术升级
        addResearch(
            "infrared_imaging_sensor_development",
            "红外成像传感器开发",
            "Infrared Imaging Sensor Development",
            2,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.infrared_imaging_sensor_development")
                info("研发适用于行星观测的高灵敏度红外传感器" translatedTo "Develop high-sensitivity infrared sensors suitable for planetary observation")
            },
        )
        addResearch("multispectral_planet_scanning", "多光谱行星扫描", "Multispectral Planetary Scanning", 2, 1)
        addResearch("planetary_atmosphere_composition_detection", "行星大气成分检测", "Planetary Atmosphere Composition Detection", 2, 1)
        addResearch("subsurface_structure_radar_probe", "地下结构雷达探测", "Subsurface Structure Radar Probe", 2, 1)
        // 推进系统优化
        addResearch("liquid_fuel_rocket_engine_testing", "液体燃料火箭发动机测试", "Liquid Fuel Rocket Engine Testing", 2, 2)
        addResearch(
            "rocket_thrust_enhancement",
            "火箭推力增强",
            "Rocket Thrust Enhancement",
            2,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.rocket_thrust_enhancement")
                info("改进燃料喷射系统提升火箭发动机推力" translatedTo "Improve fuel injection system to enhance rocket engine thrust")
            },
        )
        addResearch("propellant_efficiency_optimization", "推进剂效率优化", "Propellant Efficiency Optimization", 2, 2)
        addResearch("rocket_nozzle_design_improvement", "火箭喷管设计改进", "Rocket Nozzle Design Improvement", 2, 2)
        // 着陆与漫游技术
        addResearch("parachute_descent_control_system", "降落伞下降控制系统", "Parachute Descent Control System", 2, 3)
        addResearch(
            "buffer_landing_gear_design",
            "缓冲着陆架设计",
            "Buffer Landing Gear Design",
            2,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.buffer_landing_gear_design")
                info("设计可吸收冲击能量的探测器着陆架" translatedTo "Design a probe landing gear that can absorb impact energy")
            },
        )
        addResearch("rover_wheel_traction_optimization", "漫游车轮牵引力优化", "Rover Wheel Traction Optimization", 2, 3)
        addResearch("autonomous_obstacle_avoidance_basic", "自主避障基础", "Autonomous Obstacle Avoidance Basic", 2, 3)
        addResearch("sample_collection_mechanism", "样本采集机构", "Sample Collection Mechanism", 2, 3)
        // 空间基地基础
        addResearch("space_module_connection_tech", "空间模块连接技术", "Space Module Connection Technology", 2, 4)
        addResearch("basic_life_support_system", "基础生命维持系统", "Basic Life Support System", 2, 4)
        addResearch("microgravity_water_recycling", "微重力水循环", "Microgravity Water Recycling", 2, 4)
        addResearch("space_food_preservation_tech", "空间食品保存技术", "Space Food Preservation Technology", 2, 4)
        // 导航与通信进阶
        addResearch("planetary_relative_navigation", "行星相对导航", "Planetary Relative Navigation", 2, 4)
        addResearch(
            "interplanetary_communication_relay",
            "行星际通信中继",
            "Interplanetary Communication Relay",
            2,
            4,
            ComponentListSupplier {
                setTranslationPrefix("research.interplanetary_communication_relay")
                info("利用卫星实现行星间通信信号中继转发" translatedTo "Use satellites to realize interplanetary communication signal relay转发")
            },
        )
        addResearch("navigation_data_error_correction", "导航数据误差修正", "Navigation Data Error Correction", 2, 4)
        // 资源利用基础
        addResearch("lunar_regolith_glass_synthesis", "月球表土玻璃合成", "Lunar Regolith Glass Synthesis", 2, 5)
        addResearch("in_situ_oxygen_extraction_test", "原位氧气提取测试", "In-Situ Oxygen Extraction Test", 2, 5)
        addResearch("space_resource_processing_equipment", "空间资源加工设备", "Space Resource Processing Equipment", 2, 5)
        addResearch("regolith_based_building_material", "表土基建筑材料", "Regolith-Based Building Material", 2, 5)
        addResearch("resource_mapping_algorithm_optimization", "资源测绘算法优化", "Resource Mapping Algorithm Optimization", 2, 5)

        // ==================== dataTier 3: 星际航行与自动化（35项） ====================
        // 高效推进技术
        addResearch(
            "nuclear_thermal_propulsion_test",
            "核热推进测试",
            "Nuclear Thermal Propulsion Test",
            3,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.nuclear_thermal_propulsion_test")
                info("验证核反应堆加热工质的推进可行性" translatedTo "Verify the feasibility of nuclear reactor heating working fluid for propulsion")
            },
        )
        addResearch("ion_engine_thrust_enhancement", "离子发动机推力增强", "Ion Engine Thrust Enhancement", 3, 2)
        addResearch("plasma_propulsion_system_integration", "等离子推进系统集成", "Plasma Propulsion System Integration", 3, 2)
        addResearch("hybrid_propulsion_technology", "混合推进技术", "Hybrid Propulsion Technology", 3, 2)
        // 深空探测系统
        addResearch("outer_planet_probe_power_system", "外行星探测器动力系统", "Outer Planet Probe Power System", 3, 3)
        addResearch(
            "gas_giant_radiation_shielding",
            "气态巨行星辐射屏蔽",
            "Gas Giant Radiation Shielding",
            3,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.gas_giant_radiation_shielding")
                info("研发抵御气态巨行星强辐射的屏蔽材料" translatedTo "Develop shielding materials to resist strong radiation from gas giants")
            },
        )
        addResearch("long_duration_space_probe", "长续航空间探测器", "Long-Duration Space Probe", 3, 3)
        addResearch("comet_sample_return_mission", "彗星样本返回任务", "Comet Sample Return Mission", 3, 3)
        // 自动化机器人技术
        addResearch("space_robot_vision_recognition", "空间机器人视觉识别", "Space Robot Vision Recognition", 3, 3)
        addResearch("precision_robotic_arm_operation", "精密机械臂操作", "Precision Robotic Arm Operation", 3, 3)
        addResearch("multi_robot_coordination_system", "多机器人协调系统", "Multi-Robot Coordination System", 3, 3)
        addResearch("robot_fault_self_diagnosis", "机器人故障自诊断", "Robot Fault Self-Diagnosis", 3, 3)
        addResearch("remote_robotic_surgery_basic", "远程机器人手术基础", "Remote Robotic Surgery Basic", 3, 3)
        // 空间能源系统
        addResearch(
            "multi_junction_solar_cell",
            "多结太阳能电池",
            "Multi-Junction Solar Cell",
            3,
            4,
            ComponentListSupplier {
                setTranslationPrefix("research.multi_junction_solar_cell")
                info("研发高效吸收不同波段光的太阳能电池" translatedTo "Develop solar cells that efficiently absorb light of different wavelengths")
            },
        )
        addResearch("radioisotope_thermoelectric_generator", "放射性同位素热电发生器", "Radioisotope Thermoelectric Generator", 3, 4)
        addResearch("lithium_sulfur_battery_space_app", "锂硫电池空间应用", "Lithium-Sulfur Battery Space Application", 3, 4)
        addResearch("energy_management_system", "能源管理系统", "Energy Management System", 3, 4)
        addResearch("power_grid_stabilization_tech", "电网稳定技术", "Power Grid Stabilization Technology", 3, 4)
        // 通信技术升级
        addResearch("laser_communication_transceiver", "激光通信收发器", "Laser Communication Transceiver", 3, 4)
        addResearch(
            "deep_space_network_node",
            "深空网络节点",
            "Deep Space Network Node",
            3,
            4,
            ComponentListSupplier {
                setTranslationPrefix("research.deep_space_network_node")
                info("构建覆盖太阳系的深空通信网络节点" translatedTo "Build deep space communication network nodes covering the solar system")
            },
        )
        addResearch("communication_signal_encryption", "通信信号加密", "Communication Signal Encryption", 3, 4)
        addResearch("high_bandwidth_data_transmission", "高带宽数据传输", "High-Bandwidth Data Transmission", 3, 4)
        // 行星基地建设
        addResearch("lunar_base_module_design", "月球基地模块设计", "Lunar Base Module Design", 3, 5)
        addResearch("closed_loop_ecosystem_basic", "闭环生态系统基础", "Closed-Loop Ecosystem Basic", 3, 5)
        addResearch("base_thermal_insulation_material", "基地隔热材料", "Base Thermal Insulation Material", 3, 5)
        addResearch("space_agriculture_experiment", "空间农业实验", "Space Agriculture Experiment", 3, 5)
        addResearch("meteorite_impact_protection_system", "陨石撞击防护系统", "Meteorite Impact Protection System", 3, 5)
        // 材料与导航
        addResearch("carbon_nanotube_composite", "碳纳米管复合材料", "Carbon Nanotube Composite", 3, 5)
        addResearch("shape_memory_alloy_actuator", "形状记忆合金执行器", "Shape Memory Alloy Actuator", 3, 5)
        addResearch("autonomous_trajectory_planning", "自主轨迹规划", "Autonomous Trajectory Planning", 3, 5)
        addResearch("space_debris_monitoring_system", "空间碎片监测系统", "Space Debris Monitoring System", 3, 5)
        addResearch("attitude_control_system_precision", "姿态控制系统精度", "Attitude Control System Precision", 3, 5)
        addResearch("hypersonic_entry_heat_shield", "高超声速进入热屏蔽", "Hypersonic Entry Heat Shield", 3, 5)

        // ==================== dataTier 4: 先进材料与能源（45项） ====================
        // 前沿材料研发
        addResearch(
            "graphene_sensor_technology",
            "石墨烯传感器技术",
            "Graphene Sensor Technology",
            4,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.graphene_sensor_technology")
                info("开发基于石墨烯的高灵敏度空间传感器" translatedTo "Develop high-sensitivity space sensors based on graphene")
            },
        )
        addResearch("metamaterial_radiation_shield", "超材料辐射屏蔽", "Metamaterial Radiation Shield", 4, 2)
        addResearch("high_entropy_alloy_properties", "高熵合金特性研究", "High Entropy Alloy Properties Study", 4, 2)
        addResearch("self_healing_polymer_materials", "自修复聚合物材料", "Self-Healing Polymer Materials", 4, 2)
        addResearch("transparent_ceramic_optics", "透明陶瓷光学器件", "Transparent Ceramic Optical Devices", 4, 2)
        // 能源技术革新
        addResearch(
            "tokamak_fusion_experiment",
            "托卡马克核聚变实验",
            "Tokamak Fusion Experiment",
            4,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.tokamak_fusion_experiment")
                info("进行可控核聚变反应的初步实验研究" translatedTo "Conduct preliminary experimental research on controlled nuclear fusion reactions")
            },
        )
        addResearch("magnetohydrodynamic_power_gen", "磁流体发电", "Magnetohydrodynamic Power Generation", 4, 3)
        addResearch("thermoelectric_conversion_efficiency", "热电转换效率提升", "Thermoelectric Conversion Efficiency Improvement", 4, 3)
        addResearch("superconducting_magnetic_energy", "超导磁储能", "Superconducting Magnetic Energy Storage", 4, 3)
        addResearch("renewable_energy_hybrid_system", "可再生能源混合系统", "Renewable Energy Hybrid System", 4, 3)
        // 航天器技术
        addResearch("reusable_rocket_stage", "可重复使用火箭级", "Reusable Rocket Stage", 4, 4)
        addResearch(
            "space_plane_airframe_design",
            "空天飞机机身设计",
            "Space Plane Airframe Design",
            4,
            4,
            ComponentListSupplier {
                setTranslationPrefix("research.space_plane_airframe_design")
                info("设计适合天地往返的空天飞机轻量化机身" translatedTo "Design a lightweight airframe for space planes suitable for space-ground transportation")
            },
        )
        addResearch("hypersonic_reentry_thermal_protection", "高超声速再入热防护", "Hypersonic Reentry Thermal Protection", 4, 4)
        addResearch("lightweight_pressure_vessel_tech", "轻量化压力容器技术", "Lightweight Pressure Vessel Technology", 4, 4)
        addResearch("航天器_propulsion_control", "航天器推进控制", "Spacecraft Propulsion Control", 4, 4)
        // 人工智能应用
        addResearch("ai_flight_control_algorithm", "AI飞行控制算法", "AI Flight Control Algorithm", 4, 4)
        addResearch("machine_learning_data_analysis", "机器学习数据分析", "Machine Learning Data Analysis", 4, 4)
        addResearch("neural_network_navigation_system", "神经网络导航系统", "Neural Network Navigation System", 4, 4)
        addResearch("autonomous_fault_recovery", "自主故障恢复", "Autonomous Fault Recovery", 4, 4)
        addResearch("ai_assisted_spacecraft_design", "AI辅助航天器设计", "AI-Assisted Spacecraft Design", 4, 4)
        // 行星科学研究
        addResearch("planetary_internal_structure", "行星内部结构研究", "Planetary Internal Structure Study", 4, 5)
        addResearch(
            "exoplanet_detection_method",
            "系外行星探测方法",
            "Exoplanet Detection Method",
            4,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.exoplanet_detection_method")
                info("开发基于凌日法的系外行星探测技术" translatedTo "Develop exoplanet detection technology based on transit method")
            },
        )
        addResearch("planetary_climate_simulation", "行星气候模拟", "Planetary Climate Simulation", 4, 5)
        addResearch("astrobiology_habitable_zone", "天体生物学宜居带", "Astrobiology Habitable Zone", 4, 5)
        addResearch("planetary_magnetosphere_modeling", "行星磁层建模", "Planetary Magnetosphere Modeling", 4, 5)
        // 空间制造技术
        addResearch("space_metal_3d_printing", "空间金属3D打印", "Space Metal 3D Printing", 4, 5)
        addResearch(
            "microgravity_material_synthesis",
            "微重力材料合成",
            "Microgravity Material Synthesis",
            4,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.microgravity_material_synthesis")
                info("在微重力环境下合成特殊性能材料" translatedTo "Synthesize materials with special properties in microgravity environment")
            },
        )
        addResearch("in_space_precision_machining", "在轨精密加工", "In-Space Precision Machining", 4, 5)
        addResearch("modular_manufacturing_unit", "模块化制造单元", "Modular Manufacturing Unit", 4, 5)
        addResearch("space_resource_smelting", "空间资源冶炼", "Space Resource Smelting", 4, 5)
        // 通信与安全
        addResearch("space_quantum_key_distribution", "空间量子密钥分发", "Space Quantum Key Distribution", 4, 5)
        addResearch("massive_mimo_satellite_comm", "大规模MIMO卫星通信", "Massive MIMO Satellite Communication", 4, 5)
        addResearch("software_defined_radio_system", "软件定义无线电系统", "Software-Defined Radio System", 4, 5)
        addResearch("space_cybersecurity_protocol", "空间网络安全协议", "Space Cybersecurity Protocol", 4, 5)
        addResearch("emergency_escape_pod_design", "应急逃逸舱设计", "Emergency Escape Pod Design", 4, 5)
        // 医疗与防护
        addResearch("space_radiation_health_effects", "空间辐射健康影响", "Space Radiation Health Effects", 4, 5)
        addResearch("meteoroid_impact_shield_material", "流星体撞击屏蔽材料", "Meteoroid Impact Shield Material", 4, 5)
        addResearch("space_biomedical_monitoring", "空间生物医学监测", "Space Biomedical Monitoring", 4, 5)
        addResearch("zero_gravity_medical_treatment", "零重力医疗处理", "Zero-Gravity Medical Treatment", 4, 5)

        // ==================== dataTier 5: 光学计算机与前沿科技（55项） ====================
        // 光学计算机基础
        addResearch(
            "photonic_computer_architecture",
            "光子计算机架构",
            "Photonic Computer Architecture",
            5,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.photonic_computer_architecture")
                info("设计基于光子传输的计算机硬件架构" translatedTo "Design computer hardware architecture based on photon transmission")
            },
        )
        addResearch("integrated_photonic_circuits", "集成光子电路", "Integrated Photonic Circuits", 5, 3)
        addResearch("optical_logic_gate_design", "光学逻辑门设计", "Optical Logic Gate Design", 5, 3)
        addResearch("laser_light_source_stabilization", "激光光源稳定", "Laser Light Source Stabilization", 5, 3)
        addResearch("optical_signal_modulation_tech", "光信号调制技术", "Optical Signal Modulation Technology", 5, 3)
        // 光学材料器件
        addResearch("high_refractive_index_glass", "高折射率玻璃", "High Refractive Index Glass", 5, 3)
        addResearch(
            "photonic_crystal_waveguide",
            "光子晶体波导",
            "Photonic Crystal Waveguide",
            5,
            3,
            ComponentListSupplier {
                setTranslationPrefix("research.photonic_crystal_waveguide")
                info("研发用于光信号传输的光子晶体波导器件" translatedTo "Develop photonic crystal waveguide devices for optical signal transmission")
            },
        )
        addResearch("ultra_low_loss_optical_fiber", "超低损耗光纤", "Ultra-Low Loss Optical Fiber", 5, 3)
        addResearch("high_power_semiconductor_laser", "高功率半导体激光器", "High-Power Semiconductor Laser", 5, 3)
        addResearch("fast_response_optical_detector", "快速响应光探测器", "Fast-Response Optical Detector", 5, 3)
        // 光学计算算法
        addResearch("optical_parallel_computing", "光学并行计算", "Optical Parallel Computing", 5, 4)
        addResearch(
            "photonic_neural_network",
            "光子神经网络",
            "Photonic Neural Network",
            5,
            4,
            ComponentListSupplier {
                setTranslationPrefix("research.photonic_neural_network")
                info("构建基于光信号的神经网络计算模型" translatedTo "Construct a neural network computing model based on optical signals")
            },
        )
        addResearch("optical_data_encoding_method", "光数据编码方法", "Optical Data Encoding Method", 5, 4)
        addResearch("holographic_memory_storage", "全息存储技术", "Holographic Memory Storage", 5, 4)
        addResearch("optical_algorithm_optimization", "光学算法优化", "Optical Algorithm Optimization", 5, 4)
        // 量子光学应用
        addResearch("single_photon_emitter", "单光子发射器", "Single Photon Emitter", 5, 4)
        addResearch("quantum_optical_logic_gate", "量子光学逻辑门", "Quantum Optical Logic Gate", 5, 4)
        addResearch("optical_quantum_teleportation", "光学量子隐形传态", "Optical Quantum Teleportation", 5, 4)
        addResearch("quantum_optical_sensor", "量子光学传感器", "Quantum Optical Sensor", 5, 4)
        addResearch("quantum_light_matter_interaction", "量子光物质相互作用", "Quantum Light-Matter Interaction", 5, 4)
        // 先进航天器技术
        addResearch(" Alcubierre_warp_theory", "阿尔库比尔曲速理论", "Alcubierre Warp Theory", 5, 5)
        addResearch(
            "antimatter_confinement_tech",
            "反物质约束技术",
            "Antimatter Confinement Technology",
            5,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.antimatter_confinement_tech")
                info("研发磁场约束反物质的存储装置" translatedTo "Develop storage devices for confining antimatter with magnetic fields")
            },
        )
        addResearch("interstellar_probe_propulsion", "星际探测器推进", "Interstellar Probe Propulsion", 5, 5)
        addResearch("wormhole_signature_detection", "虫洞特征探测", "Wormhole Signature Detection", 5, 5)
        addResearch("advanced_space_navigation", "先进空间导航", "Advanced Space Navigation", 5, 5)
        // 行星改造技术
        addResearch("mars_atmosphere_thickening", "火星大气增厚", "Mars Atmosphere Thickening", 5, 5)
        addResearch(
            "planetary_magnetic_field_generator",
            "行星磁场发生器",
            "Planetary Magnetic Field Generator",
            5,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.planetary_magnetic_field_generator")
                info("设计用于创建行星人工磁场的装置" translatedTo "Design a device for creating artificial planetary magnetic fields")
            },
        )
        addResearch("biological_terraforming_agents", "生物改造剂", "Biological Terraforming Agents", 5, 5)
        addResearch("martian_climate_control", "火星气候控制", "Martian Climate Control", 5, 5)
        addResearch("terraforming_ecosystem_model", "改造生态系统模型", "Terraforming Ecosystem Model", 5, 5)
        // 生命科学前沿
        addResearch("space_genetic_engineering", "空间基因工程", "Space Genetic Engineering", 5, 5)
        addResearch(
            "cryogenic_sleep_system",
            "低温休眠系统",
            "Cryogenic Sleep System",
            5,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.cryogenic_sleep_system")
                info("研发用于长期星际航行的人体低温休眠系统" translatedTo "Develop a human cryogenic sleep system for long-term interstellar travel")
            },
        )
        addResearch("artificial_brain_interface", "人工脑接口", "Artificial Brain Interface", 5, 5)
        addResearch("space_synthetic_biology", "空间合成生物学", "Space Synthetic Biology", 5, 5)
        addResearch("neural_prosthetic_device", "神经假体装置", "Neural Prosthetic Device", 5, 5)
        // 信息与能源巅峰
        addResearch("exaflop_photonic_computer", "百亿亿次光子计算机", "Exaflop Photonic Computer", 5, 5)
        addResearch("interstellar_communication_network", "星际通信网络", "Interstellar Communication Network", 5, 5)
        addResearch("holographic_data_visualization", "全息数据可视化", "Holographic Data Visualization", 5, 5)
        addResearch("antimatter_power_conversion", "反物质能量转换", "Antimatter Power Conversion", 5, 5)
        addResearch("zero_point_energy_harvesting", "零点能量收集", "Zero-Point Energy Harvesting", 5, 5)
        // 宇宙科学探索
        addResearch("dark_matter_detector_space", "空间暗物质探测器", "Space Dark Matter Detector", 5, 5)
        addResearch(
            "dark_energy_survey",
            "暗能量调查",
            "Dark Energy Survey",
            5,
            5,
            ComponentListSupplier {
                setTranslationPrefix("research.dark_energy_survey")
                info("通过观测宇宙膨胀研究暗能量特性" translatedTo "Study dark energy properties by observing cosmic expansion")
            },
        )
        addResearch("cosmic_microwave_background_mapping", "宇宙微波背景测绘", "Cosmic Microwave Background Mapping", 5, 5)
        addResearch("galaxy_evolution_simulation", "星系演化模拟", "Galaxy Evolution Simulation", 5, 5)
        addResearch("universal_constants_measurement", "宇宙常数测量", "Universal Constants Measurement", 5, 5)
        addResearch("black_hole_event_horizon", "黑洞事件视界研究", "Black Hole Event Horizon Study", 5, 5)
    }

    private fun addResearch(cnName: String, enName: String, dataTier: Int, dataCrystal: Int, tooltip: ComponentListSupplier? = null) {
        addResearch(enName.replace(" ", "_").lowercase(), cnName, enName, dataTier, dataCrystal, tooltip)
    }

    private fun addResearch(key: String, cnName: String, enName: String, dataTier: Int, dataCrystal: Int, tooltip: ComponentListSupplier? = null) {
        if (!itemRegister) {
            tooltip?.let { researchTooltips[generateSerialId(key)] = it }
            if (GTCEu.isDataGen()) (langMap as O2OOpenCacheHashMap)[key] = CNEN(cnName, enName)
        } else {
            writeAnalyzeResearchToMap(key, dataTier, dataCrystal)
        }
    }

    private const val FNV_OFFSET_BASIS = 0x811C9DC5.toInt()
    private const val FNV_PRIME = 0x01000193
    private fun generateSerialId(dataId: String): Int {
        var hash = FNV_OFFSET_BASIS
        val bytes = dataId.toByteArray(Charsets.UTF_8)
        for (b in bytes) {
            hash = hash xor (b.toInt() and 0xFF)
            hash *= FNV_PRIME
        }
        return hash
    }
}
