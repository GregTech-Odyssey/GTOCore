package com.gto.gtocore.data.lang;

import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.api.registries.MultiblockBuilder;
import com.gto.gtocore.common.data.*;
import com.gto.gtocore.data.lang.provider.SimplifiedChineseLanguageProvider;
import com.gto.gtocore.data.lang.provider.TraditionalChineseLanguageProvider;
import com.gto.gtocore.utils.ChineseConverter;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraftforge.common.data.LanguageProvider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.Map;

import static com.gto.gtocore.api.GTOValues.*;

public final class LangHandler {

    private static final Map<String, ENCN> LANGS = new Object2ObjectOpenHashMap<>();

    private static void addENCN(String key, ENCN encn) {
        LANGS.put(key, encn);
    }

    static void addENCN(String key, String en, String cn) {
        addENCN(key, new ENCN(en, cn));
    }

    private static void addCN(String key, String cn) {
        addENCN(key, null, cn);
    }

    private static void init() {
        GTOMaterials.LANG.forEach((k, v) -> addENCN("material.gtocore." + k, v));
        GTORecipeTypes.LANG.forEach((k, v) -> addENCN("gtocore." + k, v));
        GTOBedrockFluids.LANG.forEach((k, v) -> addENCN("gtceu.jei.bedrock_fluid." + k, v));
        GTOItems.LANG.forEach((k, v) -> addCN("item.gtocore." + k, v));
        GTOBlocks.LANG.forEach((k, v) -> addCN("block.gtocore." + k, v));
        MultiblockBuilder.TOOLTIPS_MAP.forEach(LangHandler::addENCN);

        addCN("entity.gtocore.task_entity", "任务执行实体");
        addCN("itemGroup.gtocore.block", "GTO | 方块");
        addCN("itemGroup.gtocore.item", "GTO | 物品");
        addCN("itemGroup.gtocore.machine", "GTO | 机器");
        addCN("itemGroup.gtocore.material_block", "GTO | 材料方块");
        addCN("itemGroup.gtocore.material_fluid", "GTO | 材料流体");
        addCN("itemGroup.gtocore.material_item", "GTO | 材料物品");
        addCN("itemGroup.gtocore.material_pipe", "GTO | 材料管道");

        for (int tier = GTValues.UHV; tier < 16; tier++) {
            int a = (1 << (2 * (tier - 4)));
            addENCN("gtceu.machine.parallel_hatch_mk" + tier + ".tooltip", "Allows to run up to " + a + " recipes in parallel.", "允许同时处理至多" + a + "个配方。");
        }

        addENCN("gtceu.machine.available_recipe_map_5.tooltip", "Available Recipe Types: %s, %s, %s, %s, %s", "可用配方类型：%s，%s，%s，%s，%s");
        addENCN("gtceu.machine.available_recipe_map_6.tooltip", "Available Recipe Types: %s, %s, %s, %s, %s, %s", "可用配方类型：%s，%s，%s，%s，%s，%s");

        addENCN("key.gtocore.flyingspeed", "Flight Speed Adjustment", "飞行速度调节");
        addENCN("key.gtocore.nightvision", "Night Vision Toggle", "夜视开关");
        addENCN("key.gtocore.vajra", "Vajra Key", "金刚杵按键");
        addENCN("key.gtocore.drift", "Flight Inertia", "飞行惯性");
        addENCN("key.keybinding.gtocore", "GTO Key Bindings", "GTO按键绑定");

        addENCN("structure_writer.export_order", "Export Order: C:%s  S:%s  A:%s", "导出顺序： C:%s  S:%s  A:%s");
        addENCN("structure_writer.structural_scale", "Structural Scale: X:%s  Y:%s  Z:%s", "结构规模： X:%s  Y:%s  Z:%s");

        addENCN("gtocore.pattern.blocking_mode", "Block insertion when the container has any content (except for programming circuits)", "容器有任何内容时阻止插入（编程电路除外）");
        addENCN("gtocore.pattern.multiply", "Pattern Recipe x %s", "样板配方 x %s");
        addENCN("gtocore.pattern.tooltip.multiply", "Multiply Pattern materials amount by %s", "将样板材料数量 x %s");
        addENCN("gtocore.pattern.divide", "Pattern Recipe ÷ %s", "样板配方 ÷ %s");
        addENCN("gtocore.pattern.tooltip.divide", "Divide Pattern materials amount by %s", "将样板材料数量 ÷ %s");

        addENCN("gtocore.dev", "The current version is a development test version and cannot guarantee the stability and completeness of the content. If you encounter any issues or have any suggestions, please provide feedback on GitHub.", "当前版本是开发测试版本，不能保证内容的稳定性和完整性。如果您遇到任何问题或有任何建议，请访问GitHub提供反馈。");
        addENCN("gtocore.handheld_data_required", "Handheld Data Required", "需要手持数据");
        addENCN("gtocore.structural_error", "Structural Error", "结构错误");
        addENCN("gtocore.fly_speed_reset", "fly Speed Reset", "飞行速度已重置");
        addENCN("gtocore.fly_speed", "fly Speed x%s", "飞行速度 x%s");
        addENCN("gtocore.reach_limit", "Reach Limit", "达到极限");
        addENCN("gtocore.same", "Same", "相同");
        addENCN("gtocore.me_any", "ME hatch allows connection from any side.", "ME仓允许任意面连接");
        addENCN("gtocore.me_front", "ME hatch only allows connection from the front side.", "ME仓只允许正面连接");

        addENCN("config.gtocore.option.enableAnimalsAreAfraidToEatTheirMeat", "Enable Animals Are Afraid To Eat Their Meat", "启用动物害怕他们的肉被吃");
        addENCN("config.gtocore.option.enableAnimalsAreAfraidToEatTheirMeatRange", "Enable Animals Are Afraid To Eat Their Meat's Range", "启用动物害怕他们的肉被吃的范围");
        addENCN("gtocore.patternModifierPro.0", "After setup,shift + right-click template provider to apply", "设置完成后，潜行右击样板供应器以应用");
        addENCN("gtocore.patternModifierPro.1", "Set Item and Fluid Multiplier", "模板乘数：所有物品和流体乘以指定倍数");
        addENCN("gtocore.patternModifierPro.2", "Set Item and Fluid Divider", "模板乘数：所有物品和流体除以指定倍数");
        addENCN("gtocore.patternModifierPro.3", "Set Maximum Item Count", "最大物品数：所有物品不会超过此数量");
        addENCN("gtocore.patternModifierPro.4", "Set Maximum Fluid Amount / Bucket", "最大流体数：所有流体不会超过此桶数");
        addENCN("gtocore.patternModifierPro.5", "Set Application Cycles , Up to 16", "应用次数为：循环上述操作次数，最大为16");

        addENCN("config.gtocore.option.selfRestraint", "Self Restraint Mode", "自我约束模式");
        addENCN("config.gtocore.option.recipeCheck", "[Debug]Recipe Abnormal Check", "[调试]配方异常检查");
        addENCN("config.gtocore.option.eioFluidRate", "EIO Fluid Pipe Rate Multiplier", "EIO流体管道速率倍数");
        addENCN("config.gtocore.option.enablePrimitiveVoidOre", "Enable Primitive Void Ore Machine", "启用原始虚空矿机");
        addENCN("config.gtocore.option.oreMultiplier", "Ore Yield Multiplier", "矿石产量乘数");
        addENCN("config.gtocore.option.spacetimePip", "Spacetime Pipe Flow", "时空管道流量");
        addENCN("config.gtocore.option.disableMultiBlockPage", "Disable MultiBlock Page", "关闭多方块页面");
        addENCN("config.gtocore.option.fastMultiBlockPage", "Fast MultiBlock Page", "优化多方块页面");
        addENCN("config.gtocore.option.recipeMaxCheckInterval", "Recipe Max Check Interval", "配方最大检查间隔");
        addENCN("config.gtocore.option.breakBlocksBlackList", "Black List Of Chain Blocks", "连锁挖掘黑名单");
        addENCN("config.screen.gtocore", "GTO Core Config", "GTO Core 配置");

        addENCN("gtceu.jei.ore_vein.bauxite_vein", "Bauxite Vein", "铝土矿脉");
        addENCN("gtceu.jei.ore_vein.chromite_vein", "Chromite Vein", "铬铁矿脉");
        addENCN("gtceu.jei.ore_vein.pitchblende_vein", "Pitchblende Vein", "沥青铀矿脉");
        addENCN("gtceu.jei.ore_vein.magnetite_vein", "Magnetite Vein", "磁铁矿脉");
        addENCN("gtceu.jei.ore_vein.titanium_vein", "Titanium Vein", "钛矿脉");
        addENCN("gtceu.jei.ore_vein.calorite_vein", "Calorite Vein", "耐热矿脉");
        addENCN("gtceu.jei.ore_vein.celestine_vein", "Celestine Vein", "天青石矿脉");
        addENCN("gtceu.jei.ore_vein.desh_vein", "Desh Vein", "戴斯矿脉");
        addENCN("gtceu.jei.ore_vein.ostrum_vein", "Ostrum Vein", "紫金矿脉");
        addENCN("gtceu.jei.ore_vein.zircon_vein", "Zircon Vein", "锆石矿脉");

        addENCN(TierCasingTrait.getTierTranslationKey(GRAVITON_FLOW_TIER), "Graviton Flow Tier: %s", "引力流等级：%s");
        addENCN(TierCasingTrait.getTierTranslationKey(GLASS_TIER), "Glass Tier: %s", "玻璃等级：%s");
        addENCN(TierCasingTrait.getTierTranslationKey(MACHINE_CASING_TIER), "Machine Casing Tier: %s", "机器外壳等级：%s");
        addENCN(TierCasingTrait.getTierTranslationKey(COMPONENT_ASSEMBLY_CASING_TIER), "Casing Tier: %s", "外壳等级：%s");
        addENCN(TierCasingTrait.getTierTranslationKey(POWER_MODULE_TIER), "Power Module Tier: %s", "动力模块等级：%s");
        addENCN(TierCasingTrait.getTierTranslationKey(STELLAR_CONTAINMENT_TIER), "Stellar Container Tier: %s", "恒星热力容器等级：%s");
        addENCN("gtocore.recipe.ev_max", "Maximum Neutron Energy: %s MeV", "最大中子动能：%s MeV");
        addENCN("gtocore.recipe.ev_min", "Minimum Neutron Energy: %s MeV", "最小中子动能：%s MeV");
        addENCN("gtocore.recipe.evt", "Energy Consumption per Tick: %s KeV", "每刻中子动能消耗：%s KeV");
        addENCN("gtocore.recipe.frheat", "Heating per Second: %s K", "每秒升温：%s K");
        addENCN("gtocore.recipe.grindball", "macerator Ball Material: %s", "研磨球材质：%s");
        addENCN("gtocore.recipe.law_cleanroom.display_name", "Absolute Clean", "绝对超净间");
        addENCN("gtocore.recipe.nano_forge_tier", "Nano Forge Tier: %s", "纳米锻炉等级：%s");
        addENCN("gtocore.recipe.radioactivity", "Radiation Dose: %s Sv", "辐射剂量：%s Sv");
        addENCN("gtocore.recipe.vacuum.tier", "Vacuum Tier: %s", "真空等级：%s");
        addENCN("gtocore.registry.modify", "Modified by GregTech Odyssey", "由GregTech Odyssey修改");
        addENCN("gtocore.tier.advanced", "Advanced", "高级");
        addENCN("gtocore.tier.base", "Basic", "基础");
        addENCN("gtocore.tier.ultimate", "Ultimate", "终极");
        addENCN("gtocore.universal.tooltip.ampere_out", "§bOutput Current: §r%sA", "§b输出电流：§r%sA");
        addENCN("gtocore.condition.gravity", "Requires Strong Gravity Environment", "需要强重力环境");
        addENCN("gtocore.condition.zero_gravity", "Requires Zero Gravity Environment", "需要无重力环境");
        addENCN("gtocore.recipe.nama_in", "Mana Input : %s / Tick", "魔力输入：%s/t");
        addENCN("gtocore.recipe.nama_out", "Mana Output : %s / Tick", "魔力输出：%s/t");

        addENCN("config.jade.plugin_gtocore.accelerate_provider", "[GTOCore] Accelerated Bar", "[GTOCore] 加速条");
        addENCN("config.jade.plugin_gtocore.wireless_data_hatch_provider", "[GTOCore] Wireless Data", "[GTOCore] 无线数据");
        addENCN("config.jade.plugin_gtocore.mana_container_provider", "[GTOCore] Mana Container", "[GTOCore] 魔力容器");

        addENCN("fluid.gtocore.gelid_cryotheum", "Gelid Cryotheum", "极寒之凛冰");

        addENCN("biome.gtocore.ancient_world_biome", "Ancient World", "远古世界");
        addENCN("biome.gtocore.barnarda_c_biome", "Barnarda C", "巴纳德 C");
        addENCN("biome.gtocore.ceres_biome", "Ceres", "谷神星");
        addENCN("biome.gtocore.enceladus_biome", "Enceladus", "土卫二");
        addENCN("biome.gtocore.ganymede_biome", "Ganymede", "木卫三");
        addENCN("biome.gtocore.io_biome", "Io", "木卫二");
        addENCN("biome.gtocore.pluto_biome", "Pluto", "冥王星");
        addENCN("biome.gtocore.titan_biome", "Titan", "土卫六");
        addENCN("biome.gtocore.create", "Create", "创造");
        addENCN("biome.gtocore.void", "Void", "虚空");
        addENCN("biome.gtocore.flat", "Superflat", "超平坦");
        addENCN("planet.gtocore.barnarda_c", "Barnarda C", "巴纳德 C");
        addENCN("planet.gtocore.barnarda_c_orbit", "Barnarda C Orbit", "巴纳德 C轨道");
        addENCN("planet.gtocore.ceres", "Ceres", "谷神星");
        addENCN("planet.gtocore.ceres_orbit", "Ceres Orbit", "谷神星轨道");
        addENCN("planet.gtocore.enceladus", "Enceladus", "土卫二");
        addENCN("planet.gtocore.enceladus_orbit", "Enceladus Orbit", "土卫二轨道");
        addENCN("planet.gtocore.ganymede", "Ganymede", "木卫三");
        addENCN("planet.gtocore.ganymede_orbit", "Ganymede Orbit", "木卫三轨道");
        addENCN("planet.gtocore.io", "Io", "木卫一");
        addENCN("planet.gtocore.io_orbit", "Io Orbit", "木卫一轨道");
        addENCN("planet.gtocore.pluto", "Pluto", "冥王星");
        addENCN("planet.gtocore.pluto_orbit", "Pluto Orbit", "冥王星轨道");
        addENCN("planet.gtocore.titan", "Titan", "土卫六");
        addENCN("planet.gtocore.titan_orbit", "Titan Orbit", "土卫六轨道");
        addENCN("gui.ad_astra.text.barnarda", "Barnarda", "巴纳德");
    }

    public static void enInitialize(LanguageProvider provider) {
        init();
        MachineLang.init();
        BlockLang.init();
        ItemLang.init();
        LANGS.forEach((k, v) -> {
            if (v.en == null) return;
            provider.add(k, v.en);
        });
    }

    public static void cnInitialize(SimplifiedChineseLanguageProvider provider) {
        LANGS.forEach((k, v) -> {
            if (v.cn == null) return;
            provider.add(k, v.cn);
        });
    }

    public static void twInitialize(TraditionalChineseLanguageProvider provider) {
        LANGS.forEach((k, v) -> {
            if (v.cn == null) return;
            provider.add(k, ChineseConverter.convert(v.cn));
        });
    }

    public record ENCN(String en, String cn) {

        @Override
        public boolean equals(Object o) {
            if (o instanceof ENCN encn) {
                return encn.en.equals(en) && encn.cn.equals(cn);
            }
            return false;
        }
    }

    public record ENCNS(String[] ens, String[] cns) {

        @Override
        public boolean equals(Object o) {
            if (o instanceof ENCNS encn) {
                return Arrays.equals(encn.ens, ens) && Arrays.equals(encn.cns, cns);
            }
            return false;
        }

        public int length() {
            return ens.length;
        }
    }
}
