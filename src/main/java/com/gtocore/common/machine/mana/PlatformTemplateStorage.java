package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.machine.mana.PlatformBlockType.*;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PlatformTemplateStorage {

    private static final List<PlatformPreset> presets = new ArrayList<>();

    static {
        PlatformBlockStructure core = PlatformBlockStructure.structure("core")
                .type("方块")
                .displayName("一个方块")
                .description("这是一个方块")
                .source("maple")
                .preview(true)
                .resource(GTOCore.id("platforms/road_x"))
                .where('C', Blocks.COBBLESTONE)
                .materials(0, 256)
                .materials(1, 256)
                .materials(2, 256)
                .build();

        PlatformBlockStructure roadX = PlatformBlockStructure.structure("roadX")
                .displayName("一个lhc")
                .description("这是一个lhc")
                .source("疏影酱")
                .preview(true)
                .resource(GTOCore.id("platforms/lhc"))
                .where('A', GTOBlocks.IRIDIUM_CASING.get())
                .where('B', GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get())
                .where('C', Objects.requireNonNull(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)))
                .where('D', GTOBlocks.NAQUADAH_ALLOY_CASING.get())
                .where('E', GTOBlocks.ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS.get())
                .where('F', GTBlocks.HIGH_POWER_CASING.get())
                .where('G', GTOBlocks.HIGH_PRESSURE_PIPE_CASING.get())
                .where('H', GTBlocks.HERMETIC_CASING_UHV.get())
                .where('I', GTBlocks.FUSION_CASING.get())
                .where('J', GTOBlocks.MOLECULAR_COIL.get())
                .where('K', GTOBlocks.SHIELDED_ACCELERATOR.get())
                .where('L', RegistriesUtils.getBlock("gtceu:magnetic_neodymium_block"))
                .where('M', RegistriesUtils.getBlock("gtceu:magnetic_samarium_block"))
                .where('N', RegistriesUtils.getBlock("gtocore:energetic_netherite_block"))
                .where('O', GTOBlocks.MAGTECH_CASING.get())
                .where('P', GTOBlocks.RADIATION_ABSORBENT_CASING.get())
                .where('Q', GTOBlocks.AMPROSIUM_PIPE_CASING.get())
                .where('R', Objects.requireNonNull(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.HastelloyN)))
                .where('S', GTOBlocks.BORON_CARBIDE_CERAMIC_RADIATION_RESISTANT_MECHANICAL_CUBE.get())
                .where('T', GTOBlocks.HOLLOW_CASING.get())
                .where('U', GCYMBlocks.CASING_ATOMIC.get())
                .where('V', GTOBlocks.OIL_GAS_TRANSPORTATION_PIPE_CASING.get())
                .where('W', Blocks.SNOW_BLOCK)
                .where('X', Blocks.LIME_CONCRETE)
                .where('Y', GTOBlocks.IRIDIUM_CASING.get())
                .where(' ', Blocks.AIR)
                .materials(0, 256)
                .extraMaterials("avaritia:enhancement_core", 1)
                .xSize(16)
                .ySize(1)
                .zSize(16)
                .build();

        PlatformBlockStructure roadZ = PlatformBlockStructure.structure("roadZ")
                .displayName("半个房子")
                .description("这是半个房子")
                .source("某村民")
                .preview(true)
                .resource(GTOCore.id("platforms/20251005-002738-451-ad553080.txt"))
                .symbolMap(GTOCore.id("platforms/20251005-002738-451-ad553080.json"))
                .materials(0, 256)
                .xSize(16)
                .ySize(1)
                .zSize(16)
                .build();

        PlatformBlockStructure cross = PlatformBlockStructure.structure("cross")
                .preview(false)
                .resource(GTOCore.id("platforms/road_x"))
                .where('A', Blocks.COBBLESTONE)
                .where('~', Blocks.AIR)
                .materials(0, 256)
                .build();

        // 注册预设组
        presets.add(
                PlatformPreset.preset("cobblestone_single")
                        .displayName("一些方块")
                        .description("这是一些方块")
                        .source("maple")
                        .addStructure(core)
                        .build());

        presets.add(
                PlatformPreset.preset("cobblestone_roads")
                        .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.name")
                        .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_roads.desc")
                        .source("vanilla")
                        .addStructure(roadX)
                        .addStructure(roadZ)
                        .addStructure(cross)
                        .build());

        presets.add(
                PlatformPreset.preset("cobblestone_full")
                        .displayName("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.name")
                        .description("gtocore.machine.industrial_platform_deployment_tools.preset.cobblestone_full.desc")
                        .source("vanilla")
                        .addStructure(core)
                        .addStructure(roadX)
                        .addStructure(roadZ)
                        .addStructure(cross)
                        .build());
    }

    public static List<PlatformPreset> initializePresets() {
        return List.copyOf(presets);
    }
}
