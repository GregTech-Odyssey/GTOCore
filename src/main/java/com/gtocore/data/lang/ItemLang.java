package com.gtocore.data.lang;

import com.gtocore.client.Tooltips;

import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gtocore.data.lang.LangHandler.addCNEN;
import static net.minecraft.ChatFormatting.GOLD;
import static net.minecraft.ChatFormatting.RESET;

final class ItemLang {

    public static void init() {
        addCNEN("item.gtceu.circuits", "电路", "Circuits");
        for (int i = 0; i < 15; i++) {
            addCNEN("tag.item.gtceu.circuits." + GTValues.VN[i].toLowerCase(), GTValues.VN[i] + "电路", GTValues.VN[i] + " Circuit");
        }

        Tooltips.TOOL_TIPS_MAP.forEach((i, s) -> {
            for (int j = 0; j < s.length(); j++) {
                addCNEN("gtocore.tooltip.item." + ItemUtils.getIdLocation(i).getPath() + "." + j, s.cns()[j], s.ens()[j]);
            }
        });

        addCNEN("item.gtceu.long_rod_extruder_mold", "模头（长杆）", "Extruder Mold (Long Rod)");

        addCNEN("tagprefix.living_rock", "活%s矿石", "Living %s Ore");
        addCNEN("tagprefix.gloomslate", "幽暗%s矿石", "Gloomslate %s Ore");
        addCNEN("tagprefix.sculk_stone", "幽匿%s矿石", "Sculk %s Ore");
        addCNEN("tagprefix.ceres_stone", "谷神星%s矿石", "Ceres %s Ore");
        addCNEN("tagprefix.contaminable_nanites", "污染的%s纳米蜂群", "Contaminable %s Nano Swarm");
        addCNEN("tagprefix.enceladus_stone", "土卫二%s矿石", "Enceladus %s Ore");
        addCNEN("tagprefix.ganymede_stone", "木卫三%s矿石", "Ganymede %s Ore");
        addCNEN("tagprefix.glacio_stone", "霜原石%s矿石", "Glacio %s Ore");
        addCNEN("tagprefix.io_stone", "木卫一%s矿石", "Io %s Ore");
        addCNEN("tagprefix.mars_stone", "火星%s矿石", "Mars %s Ore");
        addCNEN("tagprefix.mercury_stone", "水星%s矿石", "Mercury %s Ore");
        addCNEN("tagprefix.milled", "碾磨%s", "Milled %s");
        addCNEN("tagprefix.moon_stone", "月石%s矿石", "Moon %s Ore");
        addCNEN("tagprefix.nanites", "%s纳米蜂群", "%s Nano Swarm");
        addCNEN("tagprefix.pluto_stone", "冥王星%s矿石", "Pluto %s Ore");
        addCNEN("tagprefix.titan_stone", "土卫六%s矿石", "Titan %s Ore");
        addCNEN("tagprefix.venus_stone", "金星%s矿石", "Venus %s Ore");
        addCNEN("tagprefix.curved_plate", "弯曲%s板", "Curved %s Plate");
        addCNEN("tagprefix.motor_enclosure", "%s马达外壳", "%s Motor Enclosure");
        addCNEN("tagprefix.pump_barrel", "%s泵筒", "%s Pump Barrel");
        addCNEN("tagprefix.piston_housing", "%s活塞壳体", "%s Piston Housing");
        addCNEN("tagprefix.emitter_base", "%s发射器基底", "%s Emitter Base");
        addCNEN("tagprefix.sensor_casing", "%s传感器外壳", "%s Sensor Casing");
        addCNEN("tagprefix.field_generator_casing", "%s力场发生器外壳", "%s Field Generator Casing");
        addCNEN("tagprefix.rough_blank", "%s粗坯", "%s Rough Blank");
        addCNEN("tagprefix.brick", "%s砖", "%s Brick");
        addCNEN("tagprefix.flake", "%s薄片", "%s Flake");
        addCNEN("tagprefix.catalyst", "%s催化剂", "%s Catalyst");
        addCNEN("tagprefix.artificial_gem", "人造%s", "Artificial %s");
        addCNEN("tagprefix.crystal_seed", "%s晶种", "%s Crystal Seed");
        addCNEN("tagprefix.superconductor_base", "%s超导粗胚", "%s Superconductor Base");
        addCNEN("tagprefix.coin", "%s币", "%s Coin");
        addCNEN("tagprefix.particle_source", "%s粒子源", "%s Particle Source");
        addCNEN("tagprefix.target_base", "%s靶基底", "%s Target Base");
        addCNEN("tagprefix.beryllium_target", "%s-铍靶", "%s Beryllium Target");
        addCNEN("tagprefix.stainless_steel_target", "%s-不锈钢靶", "%s Stainless Steel Target");
        addCNEN("tagprefix.zirconium_carbide_target", "%s-碳化锆靶", "%s Zirconium Carbide Target");
        addCNEN("tagprefix.breeder_rod", "%s增殖棒", "%s Breeder Rod");
        addCNEN("tagprefix.excited_beryllium_target", "活化%s-铍靶", "Excited %s Beryllium Target");
        addCNEN("tagprefix.excited_stainless_steel_target", "活化%s-不锈钢靶", "Excited %s Stainless Steel Target");
        addCNEN("tagprefix.excited_zirconium_carbide_target", "活化%s-碳化锆靶", "Excited %s Zirconium Carbide Target");
        addCNEN("tagprefix.depleted_breeder_rod", "枯竭%s增殖棒", "%s Depleted Breeder Rod");
        addCNEN("tagprefix.membrane_electrode", "%s膜电极", "%s Membrane Electrode");
        addCNEN("tagprefix.mxene", "%s二维纳米层", "%s MXene");
        addCNEN("tagprefix.mborene", "%s二维纳米层", "%s MBorene");
        addCNEN("tagprefix.aluminium_contained_mxene_precursor", "MAXene%s二维纳米层前驱体", "A-Contained %s MXene Precursor");
        addCNEN("tagprefix.carbon_fiber", "%s纤维", "%s Fiber");
        addCNEN("tagprefix.carbon_fibres", "%s纤维原丝", "%s Fiber Tow");
        addCNEN("tagprefix.carbon_fiber_mesh", "%s纤维网", "%s Fiber Mesh");

        addCNEN("metaitem.tool.tooltip.rotor.coating_durability", "镀层耐久：%s / %s", "Coating Durability: %s / %s");
        addCNEN("metaitem.tool.tooltip.rotor.coating_efficiency", "镀层效率：%s%%", "Coating Efficiency: %s%%");
        addCNEN("metaitem.tool.tooltip.rotor.coating_power", "镀层产能：%s%%", "Coating Power: %s%%");
        addCNEN("metaitem.tool.tooltip.rotor.coating", "镀层材料：%s", "Coating Material: %s");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.0", "§6镀层机制：", "§6Coating Mechanism:");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.1", "§b镀层会提供一道吸收转子损耗的屏障，当转子A有镀层B，镀层B的耐久为§dmax(B/10, min(B-A, B/2))",
                "§bThe coating provides a barrier that absorbs rotor wear. When rotor A has coating B, the durability of coating B is §dmax(B/10, min(B-A, B/2))");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.2", "§b每次转子损坏时95%概率优先消耗镀层耐久，镀层耐久耗尽后再消耗本体耐久",
                "§bThere is a 95% chance of consuming the coating durability first when the rotor is damaged. After the coating durability is exhausted, the body durability is consumed");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.0.magic", "§d魔法§6镀层机制：", "§dMagical§6 Coating Mechanism:");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.1.magic", "§d魔法§b镀层将不再提供耐久度屏障，而是会提供魔法材料属性×30%的转子效率与涡轮产能",
                "§dMagical§b Coating will no longer provide a durability barrier, but will provide rotor efficiency and turbine power generation of magical material properties ×30%");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.2.magic", "§b本体消耗耐久时，§d魔法§b镀层将同时损耗耐久，镀层耐久耗尽后转子效率与涡轮产能将回落至原始值。",
                "When the body consumes durability, the §dmagical§b coating will also consume durability. After the coating durability is exhausted, the rotor efficiency and turbine power generation will fall back to the original value. ");
        addCNEN("metaitem.tool.tooltip.rotor.coating.tooltip.3.magic", "当转子A有镀层B，镀层B的耐久为§dmax(B/10, min(B-A, B/2))",
                "When rotor A has coating B, the durability of coating B is §dmax(B/10, min(B-A, B/2))");
        addCNEN("gtocore.turbine_rotor.coated", "%s镀层", "%s Coated");
        addCNEN("gtocore.tooltip.artificial_gem", "比自然的更好", "Better than nature");
        addCNEN("gtocore.tooltip.unknown", "未知", "Unknown");

        addCNEN("item.gtceu.tool.hv_vajra", "%s 基础金刚杵", "%s Basic Vajra");
        addCNEN("item.gtceu.tool.ev_vajra", "%s 高级金刚杵", "%s Advanced Vajra");
        addCNEN("item.gtceu.tool.iv_vajra", "%s 终极金刚杵", "%s Ultimate Vajra");
        addCNEN("item.gtceu.tool.vajra.tooltip", "手持工具时按[%s]调整挖掘速度", "Hold [%s] to adjust mining speed when holding the tool");
        addCNEN("item.gtceu.tool.vajra.tooltip.shift", "手持工具时按[Shift + %s]启用/禁用矿工热忱", "Hold [Shift + %s] to enable/disable Miner Fervor when holding the tool");
        addCNEN("item.gtceu.tool.vajra.tooltip.max_speed", "当前挖掘速度: §a%s§r (最大§a%s%%§r)", "Current mining speed: §a%s§r (Max §a%s%%§r)");
        addCNEN("gtocore.tooltip.item.kinetic_rotor.max", "最大风力: %s", "Max Wind Speed: %s");
        addCNEN("gtocore.tooltip.item.kinetic_rotor.min", "最小风力: %s", "Min Wind Speed: %s");
        addCNEN("gtocore.tooltip.item.virtual_item_provider", "为机器的虚拟电路槽提供物品", "Provide items for the machine's virtual circuit slot.");

        addCNEN("gtocore.tooltip.item.really_max_battery", "填满它就能通关GregTechCEu Modern", "Filling it up can allow you to complete GregTechCEu Modern");
        addCNEN("gtocore.tooltip.item.transcendent_max_battery", "填满它就能通关GregTech Odyssey", "Filling it up can allow you to complete GregTech Odyssey");
        addCNEN("gtocore.tooltip.item.extremely_max_battery", "有生之年将它填满", "Fill it up within your lifetime");
        addCNEN("gtocore.tooltip.item.insanely_max_battery", "填满也就图一乐", "Filling it up is just for fun");
        addCNEN("gtocore.tooltip.item.mega_max_battery", "填满电池 机械飞升", "Fill up the battery for mechanical ascension");
        addCNEN("gtocore.tooltip.item.tier_circuit", "%s级电路", "%s Tier Circuit");
        addCNEN("gtocore.tooltip.item.suprachronal_circuit", "运行在已知时空之外", "Running outside of known spacetime");
        addCNEN("gtocore.tooltip.item.magneto_resonatic_circuit", "利用磁共振仪器产生的强大磁场来运行", "Utilize the powerful magnetic field generated by magnetic resonance instruments to operate");
        addCNEN("gtocore.tooltip.item.universal_circuit", "一个通用的电路", "A universal circuit");
        addCNEN("gtocore.tooltip.item.craft_step", "合成步骤：%s", "Craft Step: %s");

        addCNEN("item.gtocore.structure_detect.error.1", "在 [%s, %s, %s]:", "At [%s, %s, %s]:");
        addCNEN("item.gtocore.structure_detect.tooltip.0", "潜行右键主方块以检测多方块结构", "Sneak right-click the main block to detect multiblock structure.");
        addCNEN("item.gtocore.structure_detect.tooltip.1", "检测大型结构时可能会有延迟", "There may be lag when detecting large structures.");
        addCNEN("item.gtocore.disc.data", "光盘数据：%s", "Disc data: %s");

        addCNEN("item.gtocore.order.config", "订单配置", "Order Config");

        addCNEN("item.gtocore.data_item.type.title", "§n%s配方数据：", "§n%s Recipes Data:");
        addCNEN("gtocore.tooltip.item.empty_data", "§n空数据：", "§nEmpty Data:");
        addCNEN("gtocore.tooltip.item.empty_serial", "- §a编号：%s", "- §aNumber: %s");
        addCNEN("gtocore.tooltip.item.scanning_data", "§n扫描数据：", "§nScan Data:");
        addCNEN("gtocore.tooltip.item.scanned_things", "- %d× %s", "- %d× %s");
        addCNEN("gtocore.tooltip.item.scanning_serial", "- §a扫描编号：%s", "- §aScan number: %s");
        addCNEN("gtocore.tooltip.item.analyze_data", "§n研究数据：", "§nResearch data:");
        addCNEN("gtocore.tooltip.item.analyze_things", "- %d", "- %d");
        addCNEN("gtocore.tooltip.item.analyze_serial", "- §a研究编号：%s", "- §aResearch number: %s");

        addCNEN("gtocore.behaviour.grass_harvest.description", GOLD + "极大" + RESET + "地提升小麦种子掉落概率", GOLD + "Greatly" + RESET + " increase the probability of wheat seed dropping");
        addCNEN("gtocore.behaviour.grass_harvest.description2", "右键以收割", "Right click to harvest");

        addCNEN("gtocore.tooltip.item.prospector.mana.1", "魔力: §b%s§r / §a%s§r ",
                "Mana: §b%s§r / §a%s§r ");
        addCNEN("gtocore.tooltip.item.prospector.mana.2", "（§b%s§r / §a%s§r 个魔力池）",
                "（§b%s§r / §a%s§r Mana Pools）");

        addCNEN("gtocore.tooltip.item.machine_coordinate_card.tooltip.1", "右键机器记录其坐标，右键空气清空记录。",
                "Right-click a machine to record its coordinates, right-click air to clear the record");
        addCNEN("gtocore.tooltip.item.machine_coordinate_card.tooltip.2", "§6记录信息：§r%s§b(%s§b, %s§b, %s§b)",
                "§6Coordinate Info: §r%s§b(%s§b, %s§b, %s§b)");

        addCNEN("tooltip.ad_astra.unknown_tag", "§c未知标签§r: %s", "§cUnknown tag§r: %s");
        addCNEN("tooltip.ad_astra.unknown_ingredient", "§c未知物品§r", "§cUnknown item§r");
        addCNEN("tag.item.forge.ingots.steel", "钢锭", "Steel Ingot");
        addCNEN("item.gtocore.globe.earth", "地球仪的球体", "Earth Globe Sphere");

        addCNEN("tooltip.item.pattern.uuid", "编码者：%s", "Encoded by: %s");
    }
}
