package com.gtocore.data.lang;

import com.gtocore.client.Tooltips;
import com.gtocore.common.data.GTOBedrockFluids;
import com.gtocore.data.recipe.research.AnalyzeData;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.component_builder.TranslationKeyProvider;
import com.gtolib.api.annotation.dynamic.DynamicInitialData;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.lang.CNEN;
import com.gtolib.api.lang.SimplifiedChineseLanguageProvider;
import com.gtolib.api.lang.TraditionalChineseLanguageProvider;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.registries.GTOMachineBuilder;
import com.gtolib.api.registries.MultiblockBuilder;
import com.gtolib.api.registries.ScanningClass;
import com.gtolib.utils.ChineseConverter;
import com.gtolib.utils.register.BlockRegisterUtils;
import com.gtolib.utils.register.ItemRegisterUtils;
import com.gtolib.utils.register.MaterialsRegisterUtils;
import com.gtolib.utils.register.RecipeTypeRegisterUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import net.minecraftforge.common.data.LanguageProvider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.Map;

public final class LangHandler {

    private static final Map<String, CNEN> LANGS = new Object2ObjectOpenHashMap<>();

    private static void addCNEN(String key, CNEN CNEN) {
        if (LANGS.containsKey(key)) throw new IllegalArgumentException("Duplicate key: " + key);
        LANGS.put(key, CNEN);
    }

    public static void addCNEN(String key, String cn, String en) {
        addCNEN(key, new CNEN(cn, en));
    }

    private static void addCN(String key, String cn) {
        addCNEN(key, cn, null);
    }

    private static void init() {
        MaterialsRegisterUtils.LANG.forEach((k, v) -> addCNEN("material.gtocore." + k, v));
        RecipeTypeRegisterUtils.LANG.forEach((k, v) -> addCNEN("gtceu." + k, v));
        GTOBedrockFluids.LANG.forEach((k, v) -> addCNEN("gtceu.jei.bedrock_fluid." + k, v));
        ItemRegisterUtils.LANG.forEach((k, v) -> addCN("item.gtocore." + k, v));
        BlockRegisterUtils.LANG.forEach((k, v) -> addCN("block.gtocore." + k, v));
        GTOMachineBuilder.LANG.forEach(LangHandler::addCNEN);
        MultiblockBuilder.LANG.forEach(LangHandler::addCNEN);
        Tooltips.LANG.forEach(LangHandler::addCNEN);
        ScanningClass.LANG.forEach(LangHandler::addCNEN);
        DynamicInitialData.LANG.forEach(LangHandler::addCNEN);
        TranslationKeyProvider.LANG.forEach(LangHandler::addCNEN);
        for (var reasons : IdleReason.values()) {
            if (reasons.getEn() == null) continue;
            addCNEN(reasons.getKey(), reasons.getCn(), reasons.getEn());
        }

        Arrays.stream(Dimension.values()).forEach(d -> addCNEN(d.getKey(), d.getCn(), d.getEn()));

        addCN("entity.gtocore.task_entity", "任务执行实体");
        addCN("itemGroup.gtocore.block", "GTO | 方块");
        addCN("itemGroup.gtocore.item", "GTO | 物品");
        addCN("itemGroup.gtocore.machine", "GTO | 机器");
        addCN("itemGroup.gtocore.material_block", "GTO | 材料方块");
        addCN("itemGroup.gtocore.material_fluid", "GTO | 材料流体");
        addCN("itemGroup.gtocore.material_item", "GTO | 材料物品");
        addCN("itemGroup.gtocore.material_pipe", "GTO | 材料管道");

        for (int tier = GTValues.UHV; tier < 17; tier++) {
            int a = (1 << (2 * (tier - 4)));
            addCNEN("gtceu.machine.parallel_hatch_mk" + tier + ".tooltip", "允许同时处理至多" + a + "个配方。", "Allows to run up to " + a + " recipes in parallel.");
        }

        addCNEN("gtceu.machine.available_recipe_map_5.tooltip", "可用配方类型：%s，%s，%s，%s，%s", "Available Recipe Types: %s, %s, %s, %s, %s");
        addCNEN("gtceu.machine.available_recipe_map_6.tooltip", "可用配方类型：%s，%s，%s，%s，%s，%s", "Available Recipe Types: %s, %s, %s, %s, %s, %s");

        addCNEN("key.gtocore.flyingspeed", "飞行速度调节", "Flight Speed Adjustment");
        addCNEN("key.gtocore.nightvision", "夜视开关", "Night Vision Toggle");
        addCNEN("key.gtocore.vajra", "金刚杵按键", "Vajra Key");
        addCNEN("key.gtocore.drift", "飞行惯性", "Flight Inertia");
        addCNEN("key.gtocore.debug_inspect", "调试检查GUI槽位", "Debug Inspect GUI Slot");
        addCNEN("key.keybinding.gtocore", "GTO按键绑定", "GTO Key Bindings");

        addCNEN("structure_writer.export_order", "导出顺序： C:%s  S:%s  A:%s", "Export Order: C:%s  S:%s  A:%s");
        addCNEN("structure_writer.structural_scale", "结构规模： X:%s  Y:%s  Z:%s", "Structural Scale: X:%s  Y:%s  Z:%s");

        addCNEN("gtocore.pattern.blocking_mode", "容器有任何内容时阻止插入", "Block insertion when the container has any content");
        addCNEN("gtocore.pattern.blocking_reverse", "非同一样板时阻止插入", "Prevent insertion when not using the same pattern");
        addCNEN("gtocore.pattern.multiply", "样板配方 x %s", "Pattern Recipe x %s");
        addCNEN("gtocore.pattern.tooltip.multiply", "将样板材料数量 x %s", "Multiply Pattern materials amount by %s");
        addCNEN("gtocore.pattern.divide", "样板配方 ÷ %s", "Pattern Recipe ÷ %s");
        addCNEN("gtocore.pattern.tooltip.divide", "将样板材料数量 ÷ %s", "Divide Pattern materials amount by %s");

        addCNEN("gtocore.gtm", "整合包使用的GregTech-Modern模组为非官方版本，如果您遇到任何问题或有任何建议，请前往%s提供反馈，而不是模组官方渠道", "The GregTech-Modern mod used in the modpack is an unofficial version. If you encounter any issues or have any suggestions, please go to %s to provide feedback instead of the official mod channel.");
        addCNEN("gtocore.dev", "当前版本是开发测试版本，不能保证内容的稳定性和完整性。如果您遇到任何问题或有任何建议，请前往%s提供反馈。", "The current version is a development test version and cannot guarantee the stability and completeness of the content. If you encounter any issues or have any suggestions, please go to %s to provide feedback.");
        addCNEN("gtocore.fly_speed_reset", "飞行速度已重置", "fly Speed Reset");
        addCNEN("gtocore.fly_speed", "飞行速度 x%s", "fly Speed x%s");
        addCNEN("gtocore.reach_limit", "达到极限", "Reach Limit");
        addCNEN("gtocore.me_any", "ME仓允许任意面连接", "ME hatch allows connection from any side.");
        addCNEN("gtocore.me_front", "ME仓只允许正面连接", "ME hatch only allows connection from the front side.");
        addCNEN("gtocore.unlocked", "解锁的", "Unlocked");
        addCNEN("gtocore.ununlocked", "未解锁", "Ununlocked");
        addCNEN("gtocore.build", "构建", "Build");

        addCNEN("item.gtocore.pattern_modifier_pro.name", "模组修改器 Pro", "Pattern Modifier Pro");
        addCNEN("gtocore.patternModifierPro.0", "设置完成后，潜行右击样板供应器以应用", "After setup,shift + right-click template provider to apply");
        addCNEN("gtocore.patternModifierPro.1", "模板乘数：所有物品和流体乘以指定倍数", "Set Item and Fluid Multiplier");
        addCNEN("gtocore.patternModifierPro.2", "模板除数：所有物品和流体除以指定倍数", "Set Item and Fluid Divider");
        addCNEN("gtocore.patternModifierPro.3", "最大物品数：所有物品不会超过此数量", "Set Maximum Item Count");
        addCNEN("gtocore.patternModifierPro.4", "最大流体数：所有流体不会超过此桶数", "Set Maximum Fluid Amount / Bucket");
        addCNEN("gtocore.patternModifierPro.5", "应用次数为：循环上述操作次数，最大为16", "Set Application Cycles , Up to 16");

        addCNEN("gtceu.jei.ore_vein.bauxite_vein", "铝土矿脉", "Bauxite Vein");
        addCNEN("gtceu.jei.ore_vein.chromite_vein", "铬铁矿脉", "Chromite Vein");
        addCNEN("gtceu.jei.ore_vein.pitchblende_vein", "沥青铀矿脉", "Pitchblende Vein");
        addCNEN("gtceu.jei.ore_vein.magnetite_vein", "磁铁矿脉", "Magnetite Vein");
        addCNEN("gtceu.jei.ore_vein.titanium_vein", "钛矿脉", "Titanium Vein");
        addCNEN("gtceu.jei.ore_vein.calorite_vein", "耐热矿脉", "Calorite Vein");
        addCNEN("gtceu.jei.ore_vein.celestine_vein", "天青石矿脉", "Celestine Vein");
        addCNEN("gtceu.jei.ore_vein.desh_vein", "戴斯矿脉", "Desh Vein");
        addCNEN("gtceu.jei.ore_vein.ostrum_vein", "紫金矿脉", "Ostrum Vein");
        addCNEN("gtceu.jei.ore_vein.zircon_vein", "锆石矿脉", "Zircon Vein");
        addCNEN("gtceu.jei.ore_vein.crystal_vein_water_fire", "魔晶矿脉(水-火)", "Crystal Vein(Water-Fire)");
        addCNEN("gtceu.jei.ore_vein.crystal_vein_earth_wind", "魔晶矿脉(地-风)", "Crystal Vein(Earth-Wind)");

        addCNEN("gtocore.recipe.ev_max", "最大中子动能：%s MeV", "Maximum Neutron Energy: %s MeV");
        addCNEN("gtocore.recipe.ev_min", "最小中子动能：%s MeV", "Minimum Neutron Energy: %s MeV");
        addCNEN("gtocore.recipe.evt", "每刻中子动能消耗：%s KeV", "Energy Consumption per Tick: %s KeV");
        addCNEN("gtocore.recipe.frheat", "每秒升温：%s K", "Heating per Second: %s K");
        addCNEN("gtocore.recipe.grindball", "研磨球材质：%s", "macerator Ball Material: %s");
        addCNEN("gtocore.recipe.spool", "线轴类型：%s", "Spool Type: %s");
        addCNEN("gtocore.recipe.law_cleanroom.display_name", "绝对超净间", "Absolute Clean");
        addCNEN("gtocore.recipe.nano_forge_tier", "纳米锻炉等级：%s", "Nano Forge Tier: %s");
        addCNEN("gtocore.recipe.radioactivity", "辐射剂量：%s Sv", "Radiation Dose: %s Sv");
        addCNEN("gtocore.recipe.vacuum.tier", "真空等级：%s", "Vacuum Tier: %s");
        addCNEN("gtocore.recipe.restricted_machine", "只能运行在：%s", "Only runnable on: %s");
        addCNEN("gtocore.recipe.heat.temperature", "需要外部热源：%s K", "External heat source is required: %s K");
        addCNEN("gtocore.recipe.runlimit.count", "运行次数限制：%s", "Run Limit: %s times");
        addCNEN("gtocore.recipe.mana_consumption", "魔力消耗", "Mana Consumption");
        addCNEN("gtocore.recipe.mana_production", "魔力产出", "Mana Production");
        addCNEN("gtocore.condition.gravity", "需要强重力环境", "Requires Strong Gravity Environment");
        addCNEN("gtocore.condition.zero_gravity", "需要无重力环境", "Requires Zero Gravity Environment");

        addCNEN("gtocore.tier.advanced", "高级", "Advanced");
        addCNEN("gtocore.tier.base", "基础", "Basic");
        addCNEN("gtocore.tier.ultimate", "终极", "Ultimate");

        addCNEN("config.jade.plugin_gtocore.accelerate_provider", "[GTOCore] 加速条", "[GTOCore] Accelerated Bar");
        addCNEN("config.jade.plugin_gtocore.wireless_data_hatch_provider", "[GTOCore] 无线数据", "[GTOCore] Wireless Data");
        addCNEN("config.jade.plugin_gtocore.mana_container_provider", "[GTOCore] 魔力容器", "[GTOCore] Mana Container");
        addCNEN("config.jade.plugin_gtocore.vacuum_tier_provider", "[GTOCore] 真空等级", "[GTOCore] Vacuum Tier");
        addCNEN("config.jade.plugin_gtocore.temperature_provider", "[GTOCore] 机器温度", "[GTOCore] Machine Temperature");
        addCNEN("config.jade.plugin_gtocore.tick_time_provider", "[GTOCore] Tick时间", "[GTOCore] Tick Time");
        addCNEN("config.jade.plugin_gtocore.wireless_interactor_provider", "[GTOCore] 无线交互机器信息", "[GTOCore] Wireless Interactive Machine Info");
        addCNEN("config.jade.plugin_gtocore.upgrade_module_provider", "[GTOCore] 升级模块信息", "[GTOCore] Upgrade Module Info");
        addCNEN("config.jade.plugin_gtocore.destroy_time_provider", "[GTOCore] 硬度信息", "[GTOCore] Destroy Time Info");
        addCNEN("config.jade.plugin_gtocore.computation_container_provider", "[GTOCore] 算力容器信息", "[GTOCore] Computation Container Info");
        addCNEN("config.jade.plugin_gtocore.wireless_grid_provider", "[GTOCore] 无线AE网络信息", "[GTOCore] Wireless AE Network Info");

        addCNEN("fluid.gtocore.gelid_cryotheum", "极寒之凛冰", "Gelid Cryotheum");

        addCNEN("biome.gtocore.ancient_world_biome", "远古世界", "Ancient World");
        addCNEN("biome.gtocore.barnarda_c_biome", "巴纳德 C", "Barnarda C");
        addCNEN("biome.gtocore.ceres_biome", "谷神星", "Ceres");
        addCNEN("biome.gtocore.enceladus_biome", "土卫二", "Enceladus");
        addCNEN("biome.gtocore.ganymede_biome", "木卫三", "Ganymede");
        addCNEN("biome.gtocore.io_biome", "木卫二", "Io");
        addCNEN("biome.gtocore.pluto_biome", "冥王星", "Pluto");
        addCNEN("biome.gtocore.titan_biome", "土卫六", "Titan");
        addCNEN("biome.gtocore.create", "创造", "Create");
        addCNEN("biome.gtocore.void", "虚空", "Void");
        addCNEN("biome.gtocore.flat", "超平坦", "Superflat");
        addCNEN("planet.gtocore.barnarda_c", "巴纳德 C", "Barnarda C");
        addCNEN("planet.gtocore.barnarda_c_orbit", "巴纳德 C轨道", "Barnarda C Orbit");
        addCNEN("planet.gtocore.ceres", "谷神星", "Ceres");
        addCNEN("planet.gtocore.ceres_orbit", "谷神星轨道", "Ceres Orbit");
        addCNEN("planet.gtocore.enceladus", "土卫二", "Enceladus");
        addCNEN("planet.gtocore.enceladus_orbit", "土卫二轨道", "Enceladus Orbit");
        addCNEN("planet.gtocore.ganymede", "木卫三", "Ganymede");
        addCNEN("planet.gtocore.ganymede_orbit", "木卫三轨道", "Ganymede Orbit");
        addCNEN("planet.gtocore.io", "木卫一", "Io");
        addCNEN("planet.gtocore.io_orbit", "木卫一轨道", "Io Orbit");
        addCNEN("planet.gtocore.pluto", "冥王星", "Pluto");
        addCNEN("planet.gtocore.pluto_orbit", "冥王星轨道", "Pluto Orbit");
        addCNEN("planet.gtocore.titan", "土卫六", "Titan");
        addCNEN("planet.gtocore.titan_orbit", "土卫六轨道", "Titan Orbit");
        addCNEN("gui.ad_astra.text.barnarda", "巴纳德", "Barnarda");

        addCNEN("key.ae2.me2in1_wireless_locating_service", "打开ME2合1无线终端", "Open ME2in1 Wireless Terminal");

        addCNEN("gtocore.player_exp_status.mysterious_boost_potion.success", "你似乎被赋予了某种神秘能力...", "You seem to be granted with some mysterious ability ......");

        addCNEN("gtocore.gui.encoding_desc", "§o[Shift + 左击] 将样板存入背包/清空所有已编码样板", "§o[Shift + Click] insert encoding pattern into player inventory / clear all encoded patterns");

        addCNEN("gtocore.xaero_waypoint_set", "矿脉", "Ore Vein");

        addCNEN("gtocore.bar.distillation.1", "产出，消耗水", "Output , Consumption water");
        addCNEN("gtocore.bar.exploration", "爆炸", "Explosion");
        addCNEN("gtocore.bar.heat", "温度", "Heat");

        addCNEN("gtocore.player.organ.info_exclamation", "关于：", "About : ");
        addCNEN("gtocore.player.organ.that_is_your", "这是你的", "That is your ");
        addCNEN("gtocore.player.organ.dont_take_it_all_down", "千万不要全部拿下来", "Don't take it all down");
        addCNEN("gtocore.player.organ.precision_very_high", "精度高，可以装载大部分部件", "Precision is high, can load most parts");
        addCNEN("gtocore.player.organ.precision_very_low", "精度低。只能装载小部分部件", "Precision is low,  can load only small parts");
        addCNEN("gtocore.player.organ.even_make_die", "甚至致死", "Even make you die");
        addCNEN("gtocore.player.organ.can_modifier_your_body", "可以修改你的身体部件", "It can Modify your body");
        addCNEN("gtocore.player.organ.name.attribute_tag", "属性标签", "Attribute Tags");
        addCNEN("gtocore.player.organ.name.visceral_editor", "器官改造", "Visceral Editor");
        addCNEN("gtocore.player.organ.trans2open", "设为启用", "set to enable");
        addCNEN("gtocore.player.organ.trans2close", "设为禁用", "set to disable");
        addCNEN("gtocore.player.organ.you_wing_is_broken", "你的翅膀已损坏", "Your wing is broken");
        addCNEN("gtocore.player.organ.time_left", "剩余时长: %s 小时 %s 秒", "%s hours %s second time left");
        addCNEN("gtocore.player.organ.you_wing_need_to_charge", "你的翅膀需要充电", "Your wing need to charge");
        addCNEN("gtocore.player.organ.power", "功率", "power");
        addCNEN("gtocore.player.organ.need_precision_level", "需要精度等级%s级的仪器", "Need Precision Level %s Tool");
        addCNEN("gtocore.player.organ.name.function", "定制功能", "Costume Your Function");
        addCNEN("gtocore.player.organ.name.effect", "效果", "Effect");
        addCNEN("gtocore.player.organ.name.change", "更改", "Change");

        addCNEN("gtocore.player.organ.name.other", "其它", "Other");

        addCNEN("gtocore.satellite_control_center.emi.launch_satellite", "发射卫星", "Launch Satellite");

        addCNEN("effect.gtocore.mysterious_boost", "机械之神附身", "Possession of the Machine God");
        addCNEN("gtocore.death.attack.turbulence_of_another_star", "%s死于异星乱流，%s级别的星球需要%s器官改造", "%s died in the turbulence of another star, and a planet of %s level requires %s organ modification");

        addCNEN("gtocore.not_safe", "现在不安全", "It's not safe now");

        addCNEN("gtceu.recipe.category.mana_assembler", "魔力组装", "Mana Assembler");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.main", "检测到循环依赖，自动合成无法进行", "Cyclic dependency detected, automatic crafting cannot proceed");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.count", "\n发现 %s 个环:", "\nFound %s cycles:");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.more_cycles", "\n    ... 还有 %s 个环未显示", "\n    ... and %s more cycles not shown");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.cycle_number", "\n    环 %s：", "\n    Cycle %s:");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.indent", "\n          ", "\n          ");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.footer", "\n\n请处理所有循环依赖后才可以进行自动合成", "\n\nPlease resolve all cyclic dependencies before automatic crafting");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.click_instruction", "\n点击物品ID可复制到剪贴板", "\nClick on item ID to copy to clipboard");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.item_prefix", "- ", "- ");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.bracket_open", " (", " (");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.bracket_close", ")", ")");
        addCNEN("gtocore.ae.appeng.crafting.cycle_error.click_to_copy", "点击复制: %s", "Click to copy: %s");
        addCNEN("gtocore.ae.appeng.crafting.show_molecular_assembler_only", "只显示合成样板", "Show crafting pattern only");
        addCNEN("gtocore.ae.appeng.crafting.show_molecular_assembler_expect", "不显示合成样板", "Expect crafting pattern");
        addCNEN("gtocore.ae.appeng.crafting.show_molecular_assembler_all", "默认", "Default");
        addCNEN("gtocore.ae.appeng.me2in1.shift_transfer_to", "Shift + 左键将样板转移到", "Shift + Left Click to transfer pattern to");
        addCNEN("gtocore.ae.appeng.me2in1.shift_transfer_to.inventory_or_buffer", "背包或缓冲区", "Inventory or buffer");
        addCNEN("gtocore.ae.appeng.me2in1.shift_transfer_to.accessor", "当前页面中空白的样板管理终端", "Blank slots on the current page of the Pattern Terminal");
        addCNEN("gtocore.ae.appeng.me2in1.encode_to.accessor", "直接编码到当前页面中的空白部分", "Encode directly to the blank slots on the current page of terminal");
        addCNEN("gtocore.ae.appeng.me2in1.draggable_mark.tooltip", "按住并拖动以调整该面板位置", "Hold and drag to adjust the position of this panel");
        addCNEN("gtocore.ae.appeng.me2in1.material_slot", "材料槽", "Material Slot");
        addCNEN("gtocore.ae.appeng.me2in1.material_slot.1", "将带有材料类型的物品（如xx板，xx杆等）放入此槽位", "Place items with materials (such as xx plate, xx rod, etc.) in this slot");
        addCNEN("gtocore.ae.appeng.me2in1.material_slot.2", "编码时将自动应用材料类型到可替换的物品上", "The material type will be automatically applied to replaceable items when encoding");
        addCNEN("gtocore.ae.appeng.me2in1.auto_encode_rename_pattern", "自动编码重命名样板", "Auto Encode Renaming Pattern");
        addCNEN("gtocore.ae.appeng.me2in1.auto_encode_rename_pattern.1", "启用后，所有重命名后的物品将额外编码一份样板，数量与原样板中设置的一致。", "When enabled, all renamed items will encode an additional pattern, with the same quantity as set in the original pattern.");
        addCNEN("gtocore.ae.appeng.me2in1.emi.catalyst", "编码默认不填充催化剂", "Shift + Left Click: Fill catalysts into the pattern");
        addCNEN("gtocore.ae.appeng.me2in1.emi.catalyst.fill", "Shift + 左击：将催化剂填充至样板", "Shift + Left Click: Fill catalysts into the pattern");
        addCNEN("gtocore.ae.appeng.me2in1.emi.catalyst.virtual", "Ctrl + 左击：将催化剂（虚拟物品）填充至样板", "Hold Ctrl to encode catalysts as virtual item catalysts");
        addCNEN("gtocore.ae.appeng.me2in1.emi.gt_batch_encode", "Alt + 左击：批量编码，试图替换的材料用黄色标记", "Batch encoding is available while holding Alt, materials to be replaced are marked in yellow");
        addCNEN("gtocore.ae.appeng.me2in1.emi.gt_batch_encode.1", "替换失败的材料（如该材料不存在这种物品）将在编码时保持原样板的状态", "Materials that fail to replace (e.g., the material does not exist for this item) will retain the original state of the pattern during encoding");
        addCNEN("gtocore.ae.appeng.me2in1.save_default_rename_pattern", "保存默认重命名样板，将可以在自动填充时使用！", "Save the default renaming pattern, which can be used for auto-filling!");
        addCNEN("gtocore.ae.appeng.me2in1.auto_search", "使用EMI填充配方时，", "When using EMI to fill recipes,");
        addCNEN("gtocore.ae.appeng.me2in1.auto_search.on", "自动填充配方的目录名称到样板搜索栏中", "automatically fills the directory name of the recipe into the pattern search bar");
        addCNEN("gtocore.ae.appeng.me2in1.auto_search.off", "不自动在样板终端中搜索", "does not automatically search in the pattern terminal");
        addCNEN("gtocore.ae.appeng.me2in1.vanilla_craft_station", "分子装配", "Molecular Assembl");
        addCNEN("gtocore.ae.appeng.me2in1.wireless", "无线2合1终端", "Wireless 2-in-1 Terminal");
        addCNEN("gtocore.ae.appeng.me2in1", "ME2合1终端", "ME 2-in-1 Terminal");
        addCNEN("gtocore.ae.appeng.me2in1.reset_panel_position", "重置面板位置", "Reset Panel Position");
        addCNEN("gtocore.ae.appeng.me2in1.reset_panel_position.1", "重置所有面板位置到默认位置", "Reset all panel positions to default");
        addCNEN("gtocore.ae.appeng.me2in1.quick_remove_pattern", "点击移除以此为主产物的处理样板至缓冲槽", "Click to remove patterns with this main product to the buffer slot");
        addCNEN("gtocore.ae.appeng.me2in1.quick_remove_pattern.1", "shift + 点击以额外移除其合成树中不参与其他样板的处理样板", "Shift + Click to additionally remove patterns in its synthesis tree that are not involved in other patterns");

        addCNEN("gtocore.adv_terminal.block.confirm", "确认", "Confirm");
        addCNEN("gtocore.adv_terminal.block.select", "选择方块", "Select Block");
        addCNEN("gtocore.adv_terminal.category.select", "选择类别", "Select Category");

        addCNEN("ftbultimine.shape.area", "不定形 (不连续)", "Shapeless (Area)");

        addCNEN("gtocore.source", "结构来源：%s", "Structure From: %s");

        addCNEN(MultiblockState.UNLOAD_ERROR.translateKey, "有区块未加载", "There are chunk not loaded");
        addCNEN(MultiblockState.UNINIT_ERROR.translateKey, "机器未初始化", "Machine not initialized");
        addCNEN(MultiblockState.SHARE_ERROR.translateKey, "该方块不能共享", "This block cannot be shared");
        addCNEN(MultiblockState.BANNED_ERROR.translateKey, "该方块被禁止", "This block is banned");

        addCNEN("gtocore.multiblock.invalid.message", "多方块%s位于(%s)未成型！运行 /" + GTOCore.MOD_ID + "c multiblock on 查看详情。", "Multiblock %s at (%s) is not formed! Run /" + GTOCore.MOD_ID + "c multiblock on for details.");
    }

    public static void enInitialize(LanguageProvider provider) {
        init();
        MachineLang.init();
        BlockLang.init();
        ItemLang.init();
        AnalyzeData.init();
        LANGS.forEach((k, v) -> {
            if (v.en() == null) return;
            provider.add(k, v.en());
        });
    }

    public static void cnInitialize(SimplifiedChineseLanguageProvider provider) {
        LANGS.forEach((k, v) -> {
            if (v.cn() == null) return;
            provider.add(k, v.cn());
        });
    }

    public static void twInitialize(TraditionalChineseLanguageProvider provider) {
        LANGS.forEach((k, v) -> {
            if (v.en() == null) return;
            provider.add(k, ChineseConverter.convert(v.cn()));
        });
    }
}
