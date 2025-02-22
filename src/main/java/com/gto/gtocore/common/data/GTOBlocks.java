package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.registries.GTORegistration;
import com.gto.gtocore.common.block.*;
import com.gto.gtocore.utils.RLUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import appeng.block.crafting.CraftingUnitBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.gto.gtocore.common.block.BlockMap.*;
import static com.gto.gtocore.utils.register.BlockRegisterUtils.*;

public interface GTOBlocks {

    static void init() {
        GTORegistration.REGISTRATE.blockEntity("crafting_storage", CraftingBlockEntity::new)
                .validBlocks(
                        CRAFTING_STORAGE_1M,
                        CRAFTING_STORAGE_4M,
                        CRAFTING_STORAGE_16M,
                        CRAFTING_STORAGE_64M,
                        CRAFTING_STORAGE_256M,
                        CRAFTING_STORAGE_MAX)
                .onRegister(type -> {
                    for (CraftingUnitType craftingUnitType : CraftingUnitType.values()) {
                        AEBaseBlockEntity.registerBlockEntityItem(type, craftingUnitType.getItemFromType());
                        craftingUnitType.getDefinition().get().setBlockEntity(CraftingBlockEntity.class, type, null, null);
                    }
                })
                .register();
    }

    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_1M = registerCraftingUnitBlock(1,
            CraftingUnitType.STORAGE_1M);
    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_4M = registerCraftingUnitBlock(4,
            CraftingUnitType.STORAGE_4M);
    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_16M = registerCraftingUnitBlock(16,
            CraftingUnitType.STORAGE_16M);
    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_64M = registerCraftingUnitBlock(64,
            CraftingUnitType.STORAGE_64M);
    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_256M = registerCraftingUnitBlock(256,
            CraftingUnitType.STORAGE_256M);
    BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_MAX = registerCraftingUnitBlock(-1,
            CraftingUnitType.STORAGE_MAX);

    BlockEntry<GelidCryotheumBlock> GELID_CRYOTHEUM = block("gelid_cryotheum", "极寒之凛冰", GelidCryotheumBlock::new)
            .blockstate(NonNullBiConsumer.noop())
            .register();

    BlockEntry<NukeBombBlock> NUKE_BOMB = block("nuke_bomb", "核弹", NukeBombBlock::new)
            .properties(p -> p.mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                    GTOCore.id("block/nuke_bomb"),
                    RLUtils.mc("block/tnt_bottom"),
                    RLUtils.mc("block/tnt_top"))))
            .simpleItem()
            .register();

    BlockEntry<RotatedPillarBlock> VARIATION_WOOD = block("variation_wood", "变异原木", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS).strength(1000).sound(SoundType.WOOD))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    BlockEntry<RotatedPillarBlock> BARNARDA_C_LOG = block("barnarda_c_log", "巴纳德C原木", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS).strength(100).sound(SoundType.WOOD))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    BlockEntry<Block> BARNARDA_C_LEAVES = block("barnarda_c_leaves", "巴纳德C树叶", Block::new)
            .properties(p -> p.mapColor(MapColor.PLANT).strength(50).sound(SoundType.GRASS).noOcclusion())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(NonNullBiConsumer.noop())
            .item(BlockItem::new)
            .build()
            .register();

    BlockEntry<CreateAggregationeCore> CREATE_AGGREGATIONE_CORE = block("create_aggregatione_core", "创造稳定核心", CreateAggregationeCore::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll("create_aggregatione_core", GTOCore.id("block/create_aggregatione_core"))))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item(BlockItem::new)
            .build()
            .register();

    BlockEntry<Block> LAW_FILTER_CASING = createCleanroomFilter();

    BlockEntry<Block> TITAN_STONE = createStoneBlock("titan_stone", "土卫六岩石", GTOCore.id("block/stone/titan_stone"));
    BlockEntry<Block> PLUTO_STONE = createStoneBlock("pluto_stone", "冥王星岩石", GTOCore.id("block/stone/pluto_stone"));
    BlockEntry<Block> IO_STONE = createStoneBlock("io_stone", "木卫一岩石", GTOCore.id("block/stone/io_stone"));
    BlockEntry<Block> GANYMEDE_STONE = createStoneBlock("ganymede_stone", "木卫三岩石", GTOCore.id("block/stone/ganymede_stone"));
    BlockEntry<Block> ENCELADUS_STONE = createStoneBlock("enceladus_stone", "土卫二岩石", GTOCore.id("block/stone/enceladus_stone"));
    BlockEntry<Block> CERES_STONE = createStoneBlock("ceres_stone", "谷神星岩石", GTOCore.id("block/stone/ceres_stone"));
    BlockEntry<Block> ESSENCE_BLOCK = createStoneBlock("essence_block", "精华方块", GTOCore.id("block/essence_block"));
    BlockEntry<Block> REACTOR_CORE = createStoneBlock("reactor_core", "远古反应核", GTOCore.id("block/reactor_core"));
    BlockEntry<Block> COMMAND_BLOCK_BROKEN = createStoneBlock("command_block_broken", "即将打破的命令方块", GTOCore.id("block/command_block_broken"));
    BlockEntry<Block> CHAIN_COMMAND_BLOCK_BROKEN = createStoneBlock("chain_command_block_broken", "即将打破的连锁命令方块", GTOCore.id("block/chain_command_block_broken"));
    BlockEntry<Block> INFUSED_OBSIDIAN = createStoneBlock("infused_obsidian", "注入龙力的黑曜石", GTOCore.id("block/infused_obsidian"));
    BlockEntry<Block> DRACONIUM_BLOCK_CHARGED = createStoneBlock("draconium_block_charged", "充能龙块", GTOCore.id("block/draconium_block_charged"));
    BlockEntry<Block> SHINING_OBSIDIAN = createStoneBlock("shining_obsidian", "发光的黑曜石", GTOCore.id("block/shining_obsidian"));
    BlockEntry<Block> ENDER_OBSIDIAN = createStoneBlock("ender_obsidian", "末影黑曜石", GTOCore.id("block/ender_obsidian"));

    BlockEntry<Block> TITAN_GRUNT = createSandBlock("titan_grunt", "土卫六表皮", GTOCore.id("block/sand/titan_grunt"));
    BlockEntry<Block> PLUTO_GRUNT = createSandBlock("pluto_grunt", "冥王星表皮", GTOCore.id("block/sand/pluto_grunt"));
    BlockEntry<Block> IO_ASH = createSandBlock("io_ash", "木卫一灰烬", GTOCore.id("block/sand/io_ash"));
    BlockEntry<Block> GANYMEDE_GRUNT = createSandBlock("ganymede_grunt", "木卫三表皮", GTOCore.id("block/sand/titan_grunt"));
    BlockEntry<Block> CERES_GRUNT = createSandBlock("ceres_grunt", "谷神星表皮", GTOCore.id("block/sand/ceres_grunt"));

    BlockEntry<Block> SPEEDING_PIPE = createSidedCasingBlock("speeding_pipe", "高速管道", GTOCore.id("block/casings/speeding_pipe"));
    BlockEntry<Block> MOLECULAR_COIL = createSidedCasingBlock("molecular_coil", "分子线圈", GTOCore.id("block/casings/molecular_coil"));
    BlockEntry<Block> SPACE_ELEVATOR_INTERNAL_SUPPORT = createSidedCasingBlock("space_elevator_internal_support", "太空电梯内部支撑结构", GTOCore.id("block/casings/space_elevator_internal_support"));
    BlockEntry<Block> SPACE_ELEVATOR_MODULE_BASE = createSidedCasingBlock("space_elevator_module_base", "太空电梯模块基座", GTOCore.id("block/casings/space_elevator_module_base"));
    BlockEntry<Block> DYSON_RECEIVER_CASING = createSidedCasingBlock("dyson_receiver_casing", "戴森球能量接收基座机械方块", GTOCore.id("block/casings/dyson_receiver_casing"));
    BlockEntry<Block> DYSON_DEPLOYMENT_MAGNET = createSidedCasingBlock("dyson_deployment_magnet", "戴森球模块部署单元超导磁体", GTOCore.id("block/casings/dyson_deployment_magnet"));

    BlockEntry<Block> HERMETIC_CASING_UEV = createHermeticCasing(GTValues.UEV);
    BlockEntry<Block> HERMETIC_CASING_UIV = createHermeticCasing(GTValues.UIV);
    BlockEntry<Block> HERMETIC_CASING_UXV = createHermeticCasing(GTValues.UXV);
    BlockEntry<Block> HERMETIC_CASING_OpV = createHermeticCasing(GTValues.OpV);

    BlockEntry<CoilBlock> URUIUM_COIL_BLOCK = createCoilBlock(CoilType.URUIUM);
    BlockEntry<CoilBlock> ABYSSALALLOY_COIL_BLOCK = createCoilBlock(CoilType.ABYSSALALLOY);
    BlockEntry<CoilBlock> TITANSTEEL_COIL_BLOCK = createCoilBlock(CoilType.TITANSTEEL);
    BlockEntry<CoilBlock> ADAMANTINE_COIL_BLOCK = createCoilBlock(CoilType.ADAMANTINE);
    BlockEntry<CoilBlock> NAQUADRIATICTARANIUM_COIL_BLOCK = createCoilBlock(CoilType.NAQUADRIATICTARANIUM);
    BlockEntry<CoilBlock> STARMETAL_COIL_BLOCK = createCoilBlock(CoilType.STARMETAL);
    BlockEntry<CoilBlock> INFINITY_COIL_BLOCK = createCoilBlock(CoilType.INFINITY);
    BlockEntry<CoilBlock> HYPOGEN_COIL_BLOCK = createCoilBlock(CoilType.HYPOGEN);
    BlockEntry<CoilBlock> ETERNITY_COIL_BLOCK = createCoilBlock(CoilType.ETERNITY);

    BlockEntry<WirelessEnergyUnitBlock> LV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.LV);
    BlockEntry<WirelessEnergyUnitBlock> MV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.MV);
    BlockEntry<WirelessEnergyUnitBlock> HV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.HV);
    BlockEntry<WirelessEnergyUnitBlock> EV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.EV);
    BlockEntry<WirelessEnergyUnitBlock> IV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.IV);
    BlockEntry<WirelessEnergyUnitBlock> LUV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.LuV);
    BlockEntry<WirelessEnergyUnitBlock> ZPM_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.ZPM);
    BlockEntry<WirelessEnergyUnitBlock> UV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UV);
    BlockEntry<WirelessEnergyUnitBlock> UHV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UHV);
    BlockEntry<WirelessEnergyUnitBlock> UEV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UEV);
    BlockEntry<WirelessEnergyUnitBlock> UIV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UIV);
    BlockEntry<WirelessEnergyUnitBlock> UXV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.UXV);
    BlockEntry<WirelessEnergyUnitBlock> OPV_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.OpV);
    BlockEntry<WirelessEnergyUnitBlock> MAX_WIRELESS_ENERGY_UNIT = createWirelessEnergyUnit(GTValues.MAX);

    BlockEntry<FusionCasingBlock> FUSION_CASING_MK4 = createFusionCasing(FusionCasings.CasingType.FUSION_CASING_MK4, "聚变机械方块 MK-IV");
    BlockEntry<FusionCasingBlock> FUSION_CASING_MK5 = createFusionCasing(FusionCasings.CasingType.FUSION_CASING_MK5, "聚变机械方块 MK-V");

    BlockEntry<ActiveBlock> ADVANCED_FUSION_COIL = createActiveCasing("advanced_fusion_coil", "进阶聚变线圈方块", "block/variant/advanced_fusion_coil");
    BlockEntry<ActiveBlock> FUSION_COIL_MK2 = createActiveCasing("fusion_coil_mk2", "聚变线圈方块MK-II", "block/variant/fusion_coil_mk2");
    BlockEntry<ActiveBlock> IMPROVED_SUPERCONDUCTOR_COIL = createActiveCasing("improved_superconductor_coil", "改良型超导线圈方块", "block/variant/improved_superconductor_coil");
    BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL = createActiveCasing("compressed_fusion_coil", "压缩聚变线圈方块", "block/variant/compressed_fusion_coil");
    BlockEntry<ActiveBlock> ADVANCED_COMPRESSED_FUSION_COIL = createActiveCasing("advanced_compressed_fusion_coil", "进阶压缩聚变线圈方块", "block/variant/advanced_compressed_fusion_coil");
    BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2_PROTOTYPE = createActiveCasing("compressed_fusion_coil_mk2_prototype", "压缩聚变线圈方块MK-II原型", "block/variant/compressed_fusion_coil_mk2_prototype");
    BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2 = createActiveCasing("compressed_fusion_coil_mk2", "压缩聚变线圈方块MK-II", "block/variant/compressed_fusion_coil_mk2");

    BlockEntry<ActiveBlock> SPACE_ELEVATOR_SUPPORT = createActiveCasing("space_elevator_support", "太空电梯支撑结构", "block/variant/space_elevator_support");
    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_CORE = createActiveCasing("space_elevator_power_core", "太空电梯动力核心", "block/variant/space_elevator_power_core");
    BlockEntry<ActiveBlock> HYPER_CORE = createActiveCasing("hyper_core", "超能核心", "block/variant/hyper_core");
    BlockEntry<ActiveBlock> SUPER_COMPUTATION_COMPONENT = createActiveCasing("super_computation_component", "超级计算机组件", "block/variant/super_computation_component");
    BlockEntry<ActiveBlock> SUPER_COOLER_COMPONENT = createActiveCasing("super_cooler_component", "超级冷却组件", "block/variant/super_cooler_component");
    BlockEntry<ActiveBlock> SPACETIMECONTINUUMRIPPER = createActiveCasing("spacetimecontinuumripper", "时空连续体撕裂方块", "block/variant/spacetimecontinuumripper");
    BlockEntry<ActiveBlock> SPACETIMEBENDINGCORE = createActiveCasing("spacetimebendingcore", "时空扭曲核心方块", "block/variant/spacetimebendingcore");
    BlockEntry<ActiveBlock> QUANTUM_FORCE_TRANSFORMER_COIL = createActiveCasing("quantum_force_transformer_coil", "量子操纵者线圈方块", "block/variant/quantum_force_transformer_coil");
    BlockEntry<ActiveBlock> FISSION_FUEL_COMPONENT = createActiveCasing("fission_fuel_component", "裂变燃料组件", "block/variant/fission_fuel_component");
    BlockEntry<ActiveBlock> FISSION_COOLER_COMPONENT = createActiveCasing("fission_cooler_component", "裂变冷却组件", "block/variant/fission_cooler_component");
    BlockEntry<ActiveBlock> ADVANCED_ASSEMBLY_LINE_UNIT = createActiveCasing("advanced_assembly_line_unit", "进阶装配线控制外壳", "block/variant/advanced_assembly_line_unit");
    BlockEntry<ActiveBlock> MAGIC_CORE = createActiveCasing("magic_core", "魔法核心", "block/variant/magic_core");

    BlockEntry<Block> ENERGETIC_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("energetic_photovoltaic_block", "充能光伏方块");
    BlockEntry<Block> PULSATING_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("pulsating_photovoltaic_block", "脉冲光伏方块");
    BlockEntry<Block> VIBRANT_PHOTOVOLTAIC_BLOCK = createCustomModelCasingBlock("vibrant_photovoltaic_block", "振动光伏方块");

    BlockEntry<Block> STELLAR_CONTAINMENT_CASING = createTierCasings("stellar_containment_casing", "基础恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 1);
    BlockEntry<Block> ADVANCED_STELLAR_CONTAINMENT_CASING = createTierCasings("advanced_stellar_containment_casing", "高级恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 2);
    BlockEntry<Block> ULTIMATE_STELLAR_CONTAINMENT_CASING = createTierCasings("ultimate_stellar_containment_casing", "终极恒星热力容器", GTOCore.id("block/stellar_containment_casing"), SCMAP, 3);

    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LV = createTierCasings("component_assembly_line_casing_lv", "部件装配线外壳(LV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_lv"), CALMAP, 1);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MV = createTierCasings("component_assembly_line_casing_mv", "部件装配线外壳(MV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_mv"), CALMAP, 2);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_HV = createTierCasings("component_assembly_line_casing_hv", "部件装配线外壳(HV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_hv"), CALMAP, 3);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_EV = createTierCasings("component_assembly_line_casing_ev", "部件装配线外壳(EV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_ev"), CALMAP, 4);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_IV = createTierCasings("component_assembly_line_casing_iv", "部件装配线外壳(IV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_iv"), CALMAP, 5);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_LUV = createTierCasings("component_assembly_line_casing_luv", "部件装配线外壳(LuV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_luv"), CALMAP, 6);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_ZPM = createTierCasings("component_assembly_line_casing_zpm", "部件装配线外壳(ZPM)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_zpm"), CALMAP, 7);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UV = createTierCasings("component_assembly_line_casing_uv", "部件装配线外壳(UV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uv"), CALMAP, 8);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UHV = createTierCasings("component_assembly_line_casing_uhv", "部件装配线外壳(UHV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uhv"), CALMAP, 9);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UEV = createTierCasings("component_assembly_line_casing_uev", "部件装配线外壳(UEV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uev"), CALMAP, 10);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UIV = createTierCasings("component_assembly_line_casing_uiv", "部件装配线外壳(UIV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uiv"), CALMAP, 11);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_UXV = createTierCasings("component_assembly_line_casing_uxv", "部件装配线外壳(UXV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_uxv"), CALMAP, 12);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_OPV = createTierCasings("component_assembly_line_casing_opv", "部件装配线外壳(OPV)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_opv"), CALMAP, 13);
    BlockEntry<Block> COMPONENT_ASSEMBLY_LINE_CASING_MAX = createTierCasings("component_assembly_line_casing_max", "部件装配线外壳(MAX)", GTOCore.id("block/casings/component_assembly_line/component_assembly_line_casing_max"), CALMAP, 14);

    BlockEntry<Block> REMOTE_GRAVITON_FLOW_MODULATOR = createTierCasings("remote_graviton_flow_modulator", "远程引力流调节器", GTOCore.id("block/remote_graviton_flow_modulator"), GRAVITONFLOWMAP, 1);
    BlockEntry<Block> MEDIAL_GRAVITON_FLOW_MODULATOR = createTierCasings("medial_graviton_flow_modulator", "中介引力流调节器", GTOCore.id("block/medial_graviton_flow_modulator"), GRAVITONFLOWMAP, 2);
    BlockEntry<Block> CENTRAL_GRAVITON_FLOW_MODULATOR = createTierCasings("central_graviton_flow_modulator", "中心引力流调节器", GTOCore.id("block/central_graviton_flow_modulator"), GRAVITONFLOWMAP, 3);

    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_MODULE_1 = createActiveTierCasing("space_elevator_power_module_1", "太空电梯动力模块MK1", "block/variant/space_elevator_power_module_1", SEPMMAP, 1);
    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_MODULE_2 = createActiveTierCasing("space_elevator_power_module_2", "太空电梯动力模块MK2", "block/variant/space_elevator_power_module_2", SEPMMAP, 2);
    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_MODULE_3 = createActiveTierCasing("space_elevator_power_module_3", "太空电梯动力模块MK3", "block/variant/space_elevator_power_module_3", SEPMMAP, 3);
    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_MODULE_4 = createActiveTierCasing("space_elevator_power_module_4", "太空电梯动力模块MK4", "block/variant/space_elevator_power_module_4", SEPMMAP, 4);
    BlockEntry<ActiveBlock> SPACE_ELEVATOR_POWER_MODULE_5 = createActiveTierCasing("space_elevator_power_module_5", "太空电梯动力模块MK5", "block/variant/space_elevator_power_module_5", SEPMMAP, 5);

    BlockEntry<Block> BOROSILICATE_GLASS = createTierGlassCasings("borosilicate_glass", "硼硅玻璃", GTOCore.id("block/casings/borosilicate_glass"), GTValues.HV);
    BlockEntry<Block> TITANIUM_BOROSILICATE_GLASS = createTierGlassCasings("titanium_borosilicate_glass", "钛强化硼玻璃", GTOCore.id("block/casings/titanium_borosilicate_glass"), GTValues.EV);
    BlockEntry<Block> TUNGSTEN_BOROSILICATE_GLASS = createTierGlassCasings("tungsten_borosilicate_glass", "钨强化硼玻璃", GTOCore.id("block/casings/tungsten_borosilicate_glass"), GTValues.IV);
    BlockEntry<Block> HSSS_BOROSILICATE_GLASS = createTierGlassCasings("hsss_borosilicate_glass", "高速钢-S强化硼玻璃", GTOCore.id("block/casings/hsss_borosilicate_glass"), GTValues.LuV);
    BlockEntry<Block> NAQUADAH_BOROSILICATE_GLASS = createTierGlassCasings("naquadah_borosilicate_glass", "硅岩强化硼玻璃", GTOCore.id("block/casings/naquadah_borosilicate_glass"), GTValues.ZPM);
    BlockEntry<Block> TRITANIUM_BOROSILICATE_GLASS = createTierGlassCasings("tritanium_borosilicate_glass", "三钛强化硼玻璃", GTOCore.id("block/casings/tritanium_borosilicate_glass"), GTValues.UV);
    BlockEntry<Block> AMPROSIUM_BOROSILICATE_GLASS = createTierGlassCasings("amprosium_borosilicate_glass", "安普洛强化硼玻璃", GTOCore.id("block/casings/neutronium_borosilicate_glass"), GTValues.UHV);
    BlockEntry<Block> ENDERIUM_BOROSILICATE_GLASS = createTierGlassCasings("enderium_borosilicate_glass", "末影强化硼玻璃", GTOCore.id("block/casings/enderium_borosilicate_glass"), GTValues.UEV);
    BlockEntry<Block> TARANIUM_BOROSILICATE_GLASS = createTierGlassCasings("taranium_borosilicate_glass", "塔兰强化硼玻璃", GTOCore.id("block/casings/taranium_borosilicate_glass"), GTValues.UIV);
    BlockEntry<Block> QUARKS_BOROSILICATE_GLASS = createTierGlassCasings("quarks_borosilicate_glass", "夸克强化硼玻璃", GTOCore.id("block/casings/quarks_borosilicate_glass"), GTValues.UXV);
    BlockEntry<Block> DRACONIUM_BOROSILICATE_GLASS = createTierGlassCasings("draconium_borosilicate_glass", "龙强化硼玻璃", GTOCore.id("block/casings/draconium_borosilicate_glass"), GTValues.OpV);
    BlockEntry<Block> COSMIC_NEUTRONIUM_BOROSILICATE_GLASS = createTierGlassCasings("cosmic_neutronium_borosilicate_glass", "宇宙中子强化硼玻璃", GTOCore.id("block/casings/cosmic_neutronium_borosilicate_glass"), GTValues.MAX);

    BlockEntry<Block> INFINITY_GLASS = createGlassCasingBlock("infinity_glass", "无尽强化玻璃", GTOCore.id("block/casings/infinity_glass"));
    BlockEntry<Block> RHENIUM_REINFORCED_ENERGY_GLASS = createGlassCasingBlock("rhenium_reinforced_energy_glass", "铼强化聚能玻璃", GTOCore.id("block/casings/rhenium_reinforced_energy_glass"));
    BlockEntry<Block> ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS = createGlassCasingBlock("electron_permeable_amprosium_coated_glass", "电子渗透安普洛涂层玻璃", GTOCore.id("block/casings/electron_permeable_neutronium_coated_glass"));
    BlockEntry<Block> NON_PHOTONIC_MATTER_EXCLUSION_GLASS = createGlassCasingBlock("non_photonic_matter_exclusion_glass", "非光子物质排除玻璃", GTOCore.id("block/casings/non_photonic_matter_exclusion_glass"));
    BlockEntry<Block> OMNI_PURPOSE_INFINITY_FUSED_GLASS = createGlassCasingBlock("omni_purpose_infinity_fused_glass", "全能无限融合玻璃", GTOCore.id("block/casings/omni_purpose_infinity_fused_glass"));
    BlockEntry<Block> HAWKING_RADIATION_REALIGNMENT_FOCUS = createGlassCasingBlock("hawking_radiation_realignment_focus", "霍金辐射重新调整焦点", GTOCore.id("block/casings/hawking_radiation_realignment_focus"));
    BlockEntry<Block> CHEMICAL_GRADE_GLASS = createGlassCasingBlock("chemical_grade_glass", "化学级玻璃", GTOCore.id("block/casings/chemical_grade_glass"));
    BlockEntry<Block> ANTIMATTER_CONTAINMENT_CASING = createGlassCasingBlock("antimatter_containment_casing", "反物质隔离机械方块", GTOCore.id("block/casings/antimatter_containment_casing"));
    BlockEntry<Block> QUANTUM_GLASS = createGlassCasingBlock("quantum_glass", "量子玻璃", GTOCore.id("block/casings/quantum_glass"));

    BlockEntry<Block> FORCE_FIELD_GLASS = createGlassCasingBlock("force_field_glass", "力场玻璃", GTOCore.id("block/force_field_glass"));
    BlockEntry<Block> SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK = createGlassCasingBlock("spatially_transcendent_gravitational_lens_block", "超空间引力透镜块", GTOCore.id("block/spatially_transcendent_gravitational_lens_block"));

    BlockEntry<Block> NAQUADRIA_CHARGE = createCasingBlock("naquadria_charge", "超能硅岩爆弹", GTOCore.id("block/naquadria_charge"));
    BlockEntry<Block> LEPTONIC_CHARGE = createCasingBlock("leptonic_charge", "轻子爆弹", GTOCore.id("block/leptonic_charge"));
    BlockEntry<Block> QUANTUM_CHROMODYNAMIC_CHARGE = createCasingBlock("quantum_chromodynamic_charge", "量子色动力学爆弹", GTOCore.id("block/quantum_chromodynamic_charge"));
    BlockEntry<Block> ANNIHILATE_CORE = createCasingBlock("annihilate_core", "湮灭核心", GTOCore.id("block/annihilate_core"));
    BlockEntry<Block> AMPROSIUM_PIPE_CASING = createCasingBlock("amprosium_pipe_casing", "安普洛管道方块", GTOCore.id("block/neutronium_pipe_casing"));
    BlockEntry<Block> INCONEL_625_PIPE = createCasingBlock("inconel_625_pipe", "镍铬基合金-625温和分散管道", GTOCore.id("block/inconel_625_pipe"));
    BlockEntry<Block> HASTELLOY_N_75_PIPE = createCasingBlock("hastelloy_n_75_pipe", "哈斯特洛依合金-N75油膜管道", GTOCore.id("block/hastelloy_n_75_pipe"));
    BlockEntry<Block> AMPROSIUM_GEARBOX = createCasingBlock("amprosium_gearbox", "安普洛齿轮箱机械方块", GTOCore.id("block/neutronium_gearbox"));
    BlockEntry<Block> INCONEL_625_GEARBOX = createCasingBlock("inconel_625_gearbox", "镍铬基合金-625球磨齿轮箱", GTOCore.id("block/inconel_625_gearbox"));
    BlockEntry<Block> HASTELLOY_N_75_GEARBOX = createCasingBlock("hastelloy_n_75_gearbox", "哈斯特洛依合金-N75齿轮箱", GTOCore.id("block/hastelloy_n_75_gearbox"));
    BlockEntry<Block> LASER_COOLING_CASING = createCasingBlock("laser_cooling_casing", "激光冷却方块", GTOCore.id("block/laser_cooling_casing"));
    BlockEntry<Block> SPACETIME_COMPRESSION_FIELD_GENERATOR = createCasingBlock("spacetime_compression_field_generator", "压缩时空力场发生器", GTOCore.id("block/spacetime_compression_field_generator"));
    BlockEntry<Block> DIMENSIONAL_BRIDGE_CASING = createCasingBlock("dimensional_bridge_casing", "维度桥接方块", GTOCore.id("block/dimensional_bridge_casing"));
    BlockEntry<Block> DIMENSIONAL_STABILITY_CASING = createCasingBlock("dimensional_stability_casing", "维度稳定方块", GTOCore.id("block/dimensional_stability_casing"));
    BlockEntry<Block> MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE = createCasingBlock("machine_casing_circuit_assembly_line", "电路装配线控制外壳", GTOCore.id("block/machine_casing_circuit_assembly_line"));
    BlockEntry<Block> HIGH_STRENGTH_CONCRETE = createCasingBlock("high_strength_concrete", "高强度混凝土", GTOCore.id("block/casings/space_elevator_module_base/side"));
    BlockEntry<Block> AGGREGATIONE_CORE = createCasingBlock("aggregatione_core", "聚合核心", GTOCore.id("block/aggregatione_core"));
    BlockEntry<Block> ACCELERATED_PIPELINE = createCasingBlock("accelerated_pipeline", "加速管道", GTOCore.id("block/accelerated_pipeline"));
    BlockEntry<Block> DIMENSION_CREATION_CASING = createCasingBlock("dimension_creation_casing", "维度创造机械方块", GTOCore.id("block/dimension_creation_casing"));
    BlockEntry<Block> MACHINE_CASING_GRINDING_HEAD = createCasingBlock("machine_casing_grinding_head", "坚固钻头", GTOCore.id("block/machine_casing_grinding_head"));
    BlockEntry<Block> CREATE_HPCA_COMPONENT = createCasingBlock("create_hpca_component", "创造计算组件", GTOCore.id("block/create_hpca_component"));
    BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_UNIT = createCasingBlock("spacetime_assembly_line_unit", "超时空装配单元", GTOCore.id("block/spacetime_assembly_line_unit"));
    BlockEntry<Block> SPACETIME_ASSEMBLY_LINE_CASING = createCasingBlock("spacetime_assembly_line_casing", "超时空装配外壳", GTOCore.id("block/spacetime_assembly_line_casing"));
    BlockEntry<Block> HOLLOW_CASING = createCasingBlock("hollow_casing", "中空机械方块", GTOCore.id("block/hollow_casing"));
    BlockEntry<Block> CONTAINMENT_FIELD_GENERATOR = createCasingBlock("containment_field_generator", "遏制场发生器", GTOCore.id("block/containment_field_generator"));
    BlockEntry<Block> STEAM_ASSEMBLY_BLOCK = createCasingBlock("steam_assembly_block", "蒸汽装配方块", GTOCore.id("block/steam_assembly_block"));
    BlockEntry<Block> RESTRAINT_DEVICE = createCasingBlock("restraint_device", "力场约束装置", GTOCore.id("block/restraint_device"));
    BlockEntry<Block> FLOTATION_CELL = createCasingBlock("flotation_cell", "浮选矿池单元", GTOCore.id("block/flotation_cell"));
    BlockEntry<Block> MANIPULATOR = createCasingBlock("manipulator", "量子操纵者机械方块", GTOCore.id("block/manipulator"));
    BlockEntry<Block> MODULE_CONNECTOR = createCasingBlock("module_connector", "太空电梯模块连接器", GTOCore.id("block/module_connector"));

    BlockEntry<Block> SPACE_ELEVATOR_MECHANICAL_CASING = createCasingBlock("space_elevator_mechanical_casing", "太空电梯机械方块", GTOCore.id("block/casings/space_elevator_mechanical_casing"));
    BlockEntry<Block> DYSON_CONTROL_CASING = createCasingBlock("dyson_control_casing", "戴森球控制中心基座机械方块", GTOCore.id("block/casings/dyson_control_casing"));
    BlockEntry<Block> DYSON_CONTROL_TOROID = createCasingBlock("dyson_control_toroid", "戴森球控制中心环形机械方块", GTOCore.id("block/casings/dyson_control_toroid"));
    BlockEntry<Block> DYSON_DEPLOYMENT_CORE = createCasingBlock("dyson_deployment_core", "戴森球模块部署单元核心", GTOCore.id("block/casings/dyson_deployment_core"));
    BlockEntry<Block> DYSON_DEPLOYMENT_CASING = createCasingBlock("dyson_deployment_casing", "戴森球模块部署单元基座机械方块", GTOCore.id("block/casings/dyson_deployment_casing"));
    BlockEntry<Block> CREATE_CASING = createCasingBlock("create_casing", "创造机械方块", GTOCore.id("block/casings/create_casing"));
    BlockEntry<Block> SUPERCRITICAL_TURBINE_CASING = createCasingBlock("supercritical_turbine_casing", "超临界涡轮机械方块", GTOCore.id("block/casings/supercritical_turbine_casing"));
    BlockEntry<Block> BLAZE_CASING = createCasingBlock("blaze_casing", "烈焰机械方块", GTOCore.id("block/casings/blaze_casing"));
    BlockEntry<Block> COLD_ICE_CASING = createCasingBlock("cold_ice_casing", "寒冰机械方块", GTOCore.id("block/casings/cold_ice_casing"));
    BlockEntry<Block> RED_STEEL_CASING = createCasingBlock("red_steel_casing", "高气密红钢机器外壳", GTOCore.id("block/casings/red_steel_casing"));
    BlockEntry<Block> MOLECULAR_CASING = createCasingBlock("molecular_casing", "分子机械方块", GTOCore.id("block/casings/molecular_casing"));
    BlockEntry<Block> MULTI_FUNCTIONAL_CASING = createCasingBlock("multi_functional_casing", "多功能机械方块", GTOCore.id("block/casings/multi_functional_casing"));
    BlockEntry<Block> INCONEL_625_CASING = createCasingBlock("inconel_625_casing", "防震镍铬基合金-625机器外壳", GTOCore.id("block/casings/inconel_625_casing"));
    BlockEntry<Block> HASTELLOY_N_75_CASING = createCasingBlock("hastelloy_n_75_casing", "哈斯特洛依合金-N75防水机器外壳", GTOCore.id("block/casings/hastelloy_n_75_casing"));
    BlockEntry<Block> DIMENSION_CONNECTION_CASING = createCasingBlock("dimension_connection_casing", "维度连接机械方块", GTOCore.id("block/casings/dimension_connection_casing"));
    BlockEntry<Block> DIMENSION_INJECTION_CASING = createCasingBlock("dimension_injection_casing", "维度注入机械方块", GTOCore.id("block/casings/dimension_injection_casing"));
    BlockEntry<Block> DIMENSIONALLY_TRANSCENDENT_CASING = createCasingBlock("dimensionally_transcendent_casing", "超维度机械方块", GTOCore.id("block/casings/dimensionally_transcendent_casing"));
    BlockEntry<Block> ECHO_CASING = createCasingBlock("echo_casing", "回响强化机械方块", GTOCore.id("block/casings/echo_casing"));
    BlockEntry<Block> DRAGON_STRENGTH_TRITANIUM_CASING = createCasingBlock("dragon_strength_tritanium_casing", "龙之力量三钛合金机械方块", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    BlockEntry<Block> ALUMINIUM_BRONZE_CASING = createCasingBlock("aluminium_bronze_casing", "铝青铜机械方块", GTOCore.id("block/casings/aluminium_bronze_casing"));
    BlockEntry<Block> ANTIFREEZE_HEATPROOF_MACHINE_CASING = createCasingBlock("antifreeze_heatproof_machine_casing", "防冻隔热机械方块", GTOCore.id("block/casings/antifreeze_heatproof_machine_casing"));
    BlockEntry<Block> ENHANCE_HYPER_MECHANICAL_CASING = createCasingBlock("enhance_hyper_mechanical_casing", "强化超能机械方块", GTOCore.id("block/casings/enhance_hyper_mechanical_casing"));
    BlockEntry<Block> EXTREME_STRENGTH_TRITANIUM_CASING = createCasingBlock("extreme_strength_tritanium_casing", "极限强度三钛合金机械方块", GTOCore.id("block/casings/extreme_strength_tritanium_casing"));
    BlockEntry<Block> GRAVITON_FIELD_CONSTRAINT_CASING = createCasingBlock("graviton_field_constraint_casing", "引力场约束方块", GTOCore.id("block/casings/graviton_field_constraint_casing"));
    BlockEntry<Block> HYPER_MECHANICAL_CASING = createCasingBlock("hyper_mechanical_casing", "超能机械方块", GTOCore.id("block/casings/hyper_mechanical_casing"));
    BlockEntry<Block> IRIDIUM_CASING = createCasingBlock("iridium_casing", "铱强化机械方块", GTOCore.id("block/casings/iridium_casing"));
    BlockEntry<Block> LAFIUM_MECHANICAL_CASING = createCasingBlock("lafium_mechanical_casing", "路菲恩机械方块", GTOCore.id("block/casings/lafium_mechanical_casing"));
    BlockEntry<Block> OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING = createCasingBlock("oxidation_resistant_hastelloy_n_mechanical_casing", "抗氧化哈斯特洛伊合金-N机械方块", GTOCore.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing"));
    BlockEntry<Block> PIKYONIUM_MACHINE_CASING = createCasingBlock("pikyonium_machine_casing", "皮卡优机械方块", GTOCore.id("block/casings/pikyonium_machine_casing"));
    BlockEntry<Block> SPS_CASING = createCasingBlock("sps_casing", "超临界外壳", GTOCore.id("block/casings/sps_casing"));
    BlockEntry<Block> NAQUADAH_ALLOY_CASING = createCasingBlock("naquadah_alloy_casing", "硅岩合金机械外壳", GTOCore.id("block/casings/hyper_mechanical_casing"));
    BlockEntry<Block> PROCESS_MACHINE_CASING = createCasingBlock("process_machine_casing", "处理机械方块", GTOCore.id("block/casings/process_machine_casing"));
    BlockEntry<Block> FISSION_REACTOR_CASING = createCasingBlock("fission_reactor_casing", "裂变反应堆外壳", GTOCore.id("block/casings/fission_reactor_casing"));
    BlockEntry<Block> DEGENERATE_RHENIUM_CONSTRAINED_CASING = createCasingBlock("degenerate_rhenium_constrained_casing", "简并态铼约束外壳", GTOCore.id("block/casings/degenerate_rhenium_constrained_casing"));
    BlockEntry<Block> PRESSURE_CONTAINMENT_CASING = createCasingBlock("pressure_containment_casing", "压力容器机械方块", GTOCore.id("block/casings/pressure_containment_casing"));
    BlockEntry<Block> AWAKENED_DRACONIUM_CASING = createCasingBlock("awakened_draconium_casing", "觉醒龙外壳", GTOCore.id("block/casings/awakened_draconium_casing"));
    BlockEntry<Block> MAGTECH_CASING = createCasingBlock("magtech_casing", "磁力机械方块", GTOCore.id("block/casings/magtech_casing"));
    BlockEntry<Block> BRASS_REINFORCED_WOODEN_CASING = createCasingBlock("brass_reinforced_wooden_casing", "黄铜加固木制机械方块", GTOCore.id("block/casings/brass_reinforced_wooden_casing"));
    BlockEntry<Block> COMPRESSOR_CONTROLLER_CASING = createCasingBlock("compressor_controller_casing", "压缩控制机械方块", GTOCore.id("block/casings/compressor_controller_casing"));
    BlockEntry<Block> QUARK_EXCLUSION_CASING = createCasingBlock("quark_exclusion_casing", "夸克排斥机械方块", GTOCore.id("block/casings/quark_exclusion_casing"));
    BlockEntry<Block> NAQUADAH_REINFORCED_PLANT_CASING = createCasingBlock("naquadah_reinforced_plant_casing", "硅岩增强处理机械方块", GTOCore.id("block/casings/naquadah_reinforced_plant_casing"));
    BlockEntry<Block> BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING = createCasingBlock("boundless_gravitationally_severed_structure_casing", "无边重力切割结构机械方块", GTOCore.id("block/casings/boundless_gravitationally_severed_structure_casing"));
    BlockEntry<Block> CELESTIAL_MATTER_GUIDANCE_CASING = createCasingBlock("celestial_matter_guidance_casing", "天体物质引导机械方块", GTOCore.id("block/casings/celestial_matter_guidance_casing"));
    BlockEntry<Block> SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING = createCasingBlock("singularity_reinforced_stellar_shielding_casing", "奇点增强恒星屏蔽机械方块", GTOCore.id("block/casings/singularity_reinforced_stellar_shielding_casing"));

    BlockEntry<Block> STELLAR_ENERGY_SIPHON_CASING = createCasingBlock("stellar_energy_siphon_casing", "恒星能量汲取机械方块", GTOCore.id("block/stellar_energy_siphon_casing"));
    BlockEntry<Block> TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING = createCasingBlock("transcendentally_amplified_magnetic_confinement_casing", "超强放大磁约束机械方块", GTOCore.id("block/transcendentally_amplified_magnetic_confinement_casing"));
    BlockEntry<Block> COMPRESSOR_PIPE_CASING = createCasingBlock("compressor_pipe_casing", "压缩管道机械方块", GTOCore.id("block/compressor_pipe_casing"));
    BlockEntry<Block> EXTREME_DENSITY_CASING = createCasingBlock("extreme_density_casing", "极密机械方块", GTOCore.id("block/extreme_density_casing"));
    BlockEntry<Block> FLOCCULATION_CASING = createCasingBlock("flocculation_casing", "光滑无菌絮凝机械方块", GTOCore.id("block/flocculation_casing"));
    BlockEntry<Block> GRAVITY_STABILIZATION_CASING = createCasingBlock("gravity_stabilization_casing", "重力稳定机械方块", GTOCore.id("block/gravity_stabilization_casing"));
    BlockEntry<Block> HIGH_PRESSURE_RESISTANT_CASING = createCasingBlock("high_pressure_resistant_casing", "高能耐耐机械方块", GTOCore.id("block/high_pressure_resistant_casing"));
    BlockEntry<Block> LASER_CASING = createCasingBlock("laser_casing", "激光机械方块", GTOCore.id("block/laser_casing"));
    BlockEntry<Block> AMPROSIUM_CASING = createCasingBlock("amprosium_casing", "安普洛机械方块", GTOCore.id("block/neutronium_casing"));
    BlockEntry<Block> OZONE_CASING = createCasingBlock("ozone_casing", "臭氧机械方块", GTOCore.id("block/ozone_casing"));
    BlockEntry<Block> PLASMA_HEATER_CASING = createCasingBlock("plasma_heater_casing", "等离子加热机械方块", GTOCore.id("block/plasma_heater_casing"));
    BlockEntry<Block> RADIATION_ABSORBENT_CASING = createCasingBlock("radiation_absorbent_casing", "辐射吸收机械方块", GTOCore.id("block/radiation_absorbent_casing"));
    BlockEntry<Block> REINFORCED_WOOD_CASING = createSidedCasingBlock("reinforced_wood_casing", "增强木制机械方块", GTOCore.id("block/casings/reinforced_wood_casing"));
    BlockEntry<Block> SHIELDED_ACCELERATOR = createCasingBlock("shielded_accelerator", "屏蔽加速器机械方块", GTOCore.id("block/shielded_accelerator"));
    BlockEntry<Block> AMPROSIUM_ACTIVE_CASING = createCasingBlock("amprosium_active_casing", "安普洛活性机械方块", GTOCore.id("block/neutronium_active_casing"));
    BlockEntry<Block> QUARK_PIPE = createCasingBlock("quark_pipe", "夸克管道", GTOCore.id("block/quark_pipe"));
    BlockEntry<Block> INERT_NEUTRALIZATION_WATER_PLANT_CASING = createCasingBlock("inert_neutralization_water_plant_casing", "惰性中和水处理机械方块", GTOCore.id("block/inert_neutralization_water_plant_casing"));
    BlockEntry<Block> HIGH_ENERGY_ULTRAVIOLET_EMITTER_CASING = createCasingBlock("high_energy_ultraviolet_emitter_casing", "高能紫外线发射器机械方块", GTOCore.id("block/high_energy_ultraviolet_emitter_casing"));
    BlockEntry<Block> REINFORCED_STERILE_WATER_PLANT_CASING = createCasingBlock("reinforced_sterile_water_plant_casing", "加固无菌水处理机械方块", GTOCore.id("block/reinforced_sterile_water_plant_casing"));
    BlockEntry<Block> NEUTRONIUM_STABLE_CASING = createCasingBlock("neutronium_stable_casing", "中子稳定机械方块", GTOCore.id("block/neutronium_stable_casing"));
    BlockEntry<Block> STERILE_WATER_PLANT_CASING = createCasingBlock("sterile_water_plant_casing", "无菌水处理机械方块", GTOCore.id("block/sterile_water_plant_casing"));
    BlockEntry<Block> STABILIZED_NAQUADAH_WATER_PLANT_CASING = createCasingBlock("stabilized_naquadah_water_plant_casing", "稳定硅岩水处理机械方块", GTOCore.id("block/stabilized_naquadah_water_plant_casing"));
    BlockEntry<Block> STRENGTHEN_THE_BASE_BLOCK = createCasingBlock("strengthen_the_base_block", "强化基座方块", GTOCore.id("block/casings/strengthen_the_base_block"));

    BlockEntry<Block> PVC_PLASTIC_MECHANICAL_HOUSING = createCasingBlock("pvc_plastic_mechanical_housing", "PVC塑料机械外壳", GTOCore.id("block/pvc_plastic_mechanical_housing"));
    BlockEntry<Block> PI_HIGH_TEMPERATURE_INSULATION_MECHANICAL_HOUSING = createCasingBlock("pi_high_temperature_insulation_mechanical_housing", "PI高温绝缘机械外壳", GTOCore.id("block/pi_high_temperature_insulation_mechanical_housing"));
    BlockEntry<Block> MC_NYLON_TENSILE_MECHANICAL_SHELL = createCasingBlock("mc_nylon_tensile_mechanical_shell", "MC尼龙抗拉机械外壳", GTOCore.id("block/mc_nylon_tensile_mechanical_shell"));
    BlockEntry<Block> PEEK_WEAR_RESISTANT_MECHANICAL_HOUSING = createCasingBlock("peek_wear_resistant_mechanical_housing", "PEEK耐磨机械外壳", GTOCore.id("block/peek_wear_resistant_mechanical_housing"));
    BlockEntry<Block> REINFORCED_EPOXY_RESIN_MECHANICAL_HOUSING = createCasingBlock("reinforced_epoxy_resin_mechanical_housing", "强化环氧树脂机械外壳", GTOCore.id("block/reinforced_epoxy_resin_mechanical_housing"));
    BlockEntry<Block> PPS_CORROSION_RESISTANT_MECHANICAL_HOUSING = createCasingBlock("pps_corrosion_resistant_mechanical_housing", "PPS耐腐蚀机械外壳", GTOCore.id("block/pps_corrosion_resistant_mechanical_housing"));
    BlockEntry<Block> PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE = createCasingBlock("pbi_radiation_resistant_mechanical_enclosure", "PBI抗辐射机械外壳", GTOCore.id("block/pbi_radiation_resistant_mechanical_enclosure"));

    BlockEntry<Block> CALCIUM_OXIDE_CERAMIC_ANTI_METAL_CORROSION_MECHANICAL_BLOCK = createCasingBlock("calcium_oxide_ceramic_anti_metal_corrosion_mechanical_block", "氧化钙陶瓷抗金属侵蚀机械方块", GTOCore.id("block/calcium_oxide_ceramic_anti_metal_corrosion_mechanical_block"));
    BlockEntry<Block> ZIRCONIA_CERAMIC_HIGH_STRENGTH_BENDING_RESISTANCE_MECHANICAL_BLOCK = createCasingBlock("zirconia_ceramic_high_strength_bending_resistance_mechanical_block", "氧化锆陶瓷高强度耐弯折机械方块", GTOCore.id("block/zirconia_ceramic_high_strength_bending_resistance_mechanical_block"));
    BlockEntry<Block> LITHIUM_OXIDE_CERAMIC_HEAT_RESISTANT_SHOCK_RESISTANT_MECHANICAL_CUBE = createCasingBlock("lithium_oxide_ceramic_heat_resistant_shock_resistant_mechanical_cube", "氧化锂陶瓷防热抗震机械方块", GTOCore.id("block/lithium_oxide_ceramic_heat_resistant_shock_resistant_mechanical_cube"));
    BlockEntry<Block> TITANIUM_NITRIDE_CERAMIC_IMPACT_RESISTANT_MECHANICAL_BLOCK = createCasingBlock("titanium_nitride_ceramic_impact_resistant_mechanical_block", "氮化钛陶瓷抗冲击机械方块", GTOCore.id("block/titanium_nitride_ceramic_impact_resistant_mechanical_block"));
    BlockEntry<Block> STRONTIUM_CARBONATE_CERAMIC_RAY_ABSORBING_MECHANICAL_CUBE = createCasingBlock("strontium_carbonate_ceramic_ray_absorbing_mechanical_cube", "碳酸锶陶瓷射线吸收机械方块", GTOCore.id("block/strontium_carbonate_ceramic_ray_absorbing_mechanical_cube"));
    BlockEntry<Block> MAGNESIUM_OXIDE_CERAMIC_HIGH_TEMPERATURE_INSULATION_MECHANICAL_BLOCK = createCasingBlock("magnesium_oxide_ceramic_high_temperature_insulation_mechanical_block", "氧化镁陶瓷高温绝缘机械方块", GTOCore.id("block/magnesium_oxide_ceramic_high_temperature_insulation_mechanical_block"));
    BlockEntry<Block> BORON_CARBIDE_CERAMIC_RADIATION_RESISTANT_MECHANICAL_CUBE = createCasingBlock("boron_carbide_ceramic_radiation_resistant_mechanical_cube", "碳化硼陶瓷耐辐射机械方块", GTOCore.id("block/boron_carbide_ceramic_radiation_resistant_mechanical_cube"));
    BlockEntry<Block> COBALT_OXIDE_CERAMIC_STRONG_THERMALLY_CONDUCTIVE_MECHANICAL_BLOCK = createCasingBlock("cobalt_oxide_ceramic_strong_thermally_conductive_mechanical_block", "氧化钴陶瓷坚固导热机械方块", GTOCore.id("block/cobalt_oxide_ceramic_strong_thermally_conductive_mechanical_block"));

    BlockEntry<Block> ABS_BLACK_CASING = createCasingBlock("abs_black_casing", "黑色ABS塑料机械外壳", GTOCore.id("block/casings/abs_black_casing"));
    BlockEntry<Block> ABS_BLUE_CASING = createCasingBlock("abs_blue_casing", "蓝色ABS塑料机械外壳", GTOCore.id("block/casings/abs_blue_casing"));
    BlockEntry<Block> ABS_BROWN_CASING = createCasingBlock("abs_brown_casing", "棕色ABS塑料机械外壳", GTOCore.id("block/casings/abs_brown_casing"));
    BlockEntry<Block> ABS_GREEN_CASING = createCasingBlock("abs_green_casing", "绿色ABS塑料机械外壳", GTOCore.id("block/casings/abs_green_casing"));
    BlockEntry<Block> ABS_GREY_CASING = createCasingBlock("abs_grey_casing", "灰色ABS塑料机械外壳", GTOCore.id("block/casings/abs_grey_casing"));
    BlockEntry<Block> ABS_LIME_CASING = createCasingBlock("abs_lime_casing", "黄绿色ABS塑料机械外壳", GTOCore.id("block/casings/abs_lime_casing"));
    BlockEntry<Block> ABS_ORANGE_CASING = createCasingBlock("abs_orange_casing", "橙色ABS塑料机械外壳", GTOCore.id("block/casings/abs_orange_casing"));
    BlockEntry<Block> ABS_RAD_CASING = createCasingBlock("abs_rad_casing", "红色ABS塑料机械外壳", GTOCore.id("block/casings/abs_rad_casing"));
    BlockEntry<Block> ABS_WHITE_CASING = createCasingBlock("abs_white_casing", "白色ABS塑料机械外壳", GTOCore.id("block/casings/abs_white_casing"));
    BlockEntry<Block> ABS_YELLOW_CASING = createCasingBlock("abs_yellow_casing", "黄色ABS塑料机械外壳", GTOCore.id("block/casings/abs_yellow_casing"));
    BlockEntry<Block> ABS_CYAN_CASING = createCasingBlock("abs_cyan_casing", "青色ABS塑料机械外壳", GTOCore.id("block/casings/abs_cyan_casing"));
    BlockEntry<Block> ABS_MAGENTA_CASING = createCasingBlock("abs_magenta_casing", "品红色ABS塑料机械外壳", GTOCore.id("block/casings/abs_magenta_casing"));
    BlockEntry<Block> ABS_PINK_CASING = createCasingBlock("abs_pink_casing", "粉色ABS塑料机械外壳", GTOCore.id("block/casings/abs_pink_casing"));
    BlockEntry<Block> ABS_PURPLE_CASING = createCasingBlock("abs_purple_casing", "紫色ABS塑料机械外壳", GTOCore.id("block/casings/abs_purple_casing"));
    BlockEntry<Block> ABS_LIGHT_BULL_CASING = createCasingBlock("abs_light_bull_casing", "浅蓝色ABS塑料机械外壳", GTOCore.id("block/casings/abs_light_bull_casing"));
    BlockEntry<Block> ABS_LIGHT_GREY_CASING = createCasingBlock("abs_light_grey_casing", "浅灰色ABS塑料机械外壳", GTOCore.id("block/casings/abs_light_grey_casing"));
}
