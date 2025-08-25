package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.glodblock.github.extendedae.ExtendedAE;

import java.util.Set;
import java.util.function.Consumer;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public final class AE2 {

    public static void init(Consumer<FinishedRecipe> provider) {
        if (GTOCore.isSimple()) return;
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("import_bus"), RegistriesUtils.getItemStack("ae2:import_bus"),
                "ABC",
                'A', new ItemStack(AEItems.ANNIHILATION_CORE.asItem()), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', RegistriesUtils.getItemStack("ae2:fluix_glass_cable"));
        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("export_bus"), RegistriesUtils.getItemStack("ae2:export_bus"),
                "ABC",
                'A', new ItemStack(AEItems.FORMATION_CORE.asItem()), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', RegistriesUtils.getItemStack("ae2:fluix_glass_cable"));

        ASSEMBLER_RECIPES.builder("annihilation_core")
                .inputItems(GTOTagPrefix.FIELD_GENERATOR_CASING, GTMaterials.Steel)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(CustomTags.ULV_CIRCUITS)
                .inputItems(TagPrefix.dust, GTMaterials.NetherQuartz, 2)
                .outputItems(AEItems.ANNIHILATION_CORE.asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("formation_core")
                .inputItems(GTOTagPrefix.FIELD_GENERATOR_CASING, GTMaterials.Steel)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(CustomTags.ULV_CIRCUITS)
                .inputItems(TagPrefix.dust, GTMaterials.CertusQuartz, 2)
                .outputItems(AEItems.FORMATION_CORE.asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("controller")
                .inputItems(AEBlocks.ENERGY_ACCEPTOR.block().asItem())
                .inputItems(AEBlocks.SMOOTH_SKY_STONE_BLOCK.block().asItem(), 4)
                .inputItems(TagPrefix.plate, GTOMaterials.Fluix, 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem())
                .outputItems(AEBlocks.CONTROLLER.block().asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(120)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("drive")
                .inputItems(TagPrefix.frameGt, GTMaterials.Aluminium)
                .inputItems(TagPrefix.plate, GTMaterials.VanadiumSteel, 6)
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem(), 4)
                .inputItems("ae2:fluix_glass_cable", 4)
                .inputItems(CustomTags.MV_CIRCUITS)
                .inputItems(TagPrefix.rotor, GTMaterials.Aluminium)
                .inputItems(GTItems.ELECTRIC_MOTOR_MV.asStack())
                .inputItems(TagPrefix.gem, GTMaterials.CertusQuartz, 4)
                .outputItems(AEBlocks.DRIVE.block().asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(120)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("molecular_assembler")
                .inputItems(TagPrefix.frameGt, GTMaterials.Steel)
                .inputItems(AEItems.FORMATION_CORE.asItem(), 2)
                .inputItems(AEItems.ANNIHILATION_CORE.asItem(), 2)
                .inputItems(GTItems.ROBOT_ARM_LV.asStack())
                .inputItems(GTItems.CONVEYOR_MODULE_LV.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.Invar, 6)
                .outputItems(AEBlocks.MOLECULAR_ASSEMBLER.block().asItem())
                .inputFluids(GTMaterials.Glass, 288)
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("energy_acceptor")
                .inputItems(GTMachines.DIODE[GTValues.LV].asStack())
                .inputItems(GTMachines.ENERGY_CONVERTER_16A[GTValues.LV].asStack())
                .inputItems(AEBlocks.QUARTZ_GLASS.block().asItem(), 4)
                .inputItems(CustomTags.LV_CIRCUITS, 4)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.Nickel, 4)
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 8)
                .outputItems(AEBlocks.ENERGY_ACCEPTOR.block().asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(30)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.builder("interface_lv")
                .inputItems(TagPrefix.frameGt, GTOMaterials.Livingsteel)
                .inputItems(AEItems.FORMATION_CORE.asItem())
                .inputItems(AEItems.ANNIHILATION_CORE.asItem())
                .inputItems(GTItems.CONVEYOR_MODULE_LV.asStack())
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Steel, 2)
                .inputItems(GTBlocks.CASING_TEMPERED_GLASS.asStack(2))
                .outputItems(AEBlocks.INTERFACE.block().asItem())
                .inputFluids(GTMaterials.Polyethylene, 144)
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("interface_mv")
                .inputItems(TagPrefix.frameGt, GTMaterials.Aluminium)
                .inputItems(AEItems.FORMATION_CORE.asItem())
                .inputItems(AEItems.ANNIHILATION_CORE.asItem())
                .inputItems(GTItems.CONVEYOR_MODULE_MV.asStack())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Aluminium, 4)
                .inputItems(GTOBlocks.BOROSILICATE_GLASS.asStack(4))
                .outputItems(AEBlocks.INTERFACE.block().asItem(), 4)
                .inputFluids(GTMaterials.Polyethylene, 576)
                .EUt(120)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("assembler_matrix_frame")
                .inputItems(GTBlocks.PLASTCRETE.asStack())
                .inputItems(AEBlocks.QUARTZ_GLASS.block().asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Lapis, 8)
                .inputItems(TagPrefix.frameGt, GTMaterials.StainlessSteel)
                .outputItems("expatternprovider:assembler_matrix_frame")
                .inputFluids(GTMaterials.Polytetrafluoroethylene, 288)
                .EUt(120)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("assembler_matrix_wall")
                .inputItems("expatternprovider:assembler_matrix_frame")
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem(), 2)
                .inputItems("ae2:fluix_smart_cable", 4)
                .inputItems(TagPrefix.gemFlawless, GTMaterials.NetherQuartz, 4)
                .inputItems(CustomTags.HV_CIRCUITS)
                .outputItems("expatternprovider:assembler_matrix_wall", 2)
                .inputFluids(GTMaterials.PolyvinylChloride, 288)
                .EUt(120)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("assembler_matrix_pattern")
                .inputItems("expatternprovider:assembler_matrix_wall")
                .inputItems("expatternprovider:ex_pattern_provider", 2)
                .inputItems("ae2:blue_lumen_paint_ball", 6)
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems("ae2:fluix_glass_cable", 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem())
                .outputItems("expatternprovider:assembler_matrix_pattern")
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(120)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("assembler_matrix_crafter")
                .inputItems("expatternprovider:assembler_matrix_wall")
                .inputItems("expatternprovider:ex_molecular_assembler", 2)
                .inputItems("ae2:purple_lumen_paint_ball", 6)
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems("ae2:fluix_glass_cable", 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem())
                .outputItems("expatternprovider:assembler_matrix_crafter")
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .EUt(120)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("assembler_matrix_speed")
                .inputItems("expatternprovider:assembler_matrix_wall")
                .inputItems(AEItems.SPEED_CARD.asItem(), 2)
                .inputItems("ae2:red_lumen_paint_ball", 6)
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems("ae2:fluix_glass_cable", 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem())
                .outputItems("expatternprovider:assembler_matrix_speed")
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(480)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("crafting_unit")
                .inputItems(TagPrefix.frameGt, GTMaterials.BlueSteel)
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems("ae2:fluix_glass_cable", 4)
                .outputItems(AEBlocks.CRAFTING_UNIT.block().asItem())
                .inputFluids(GTMaterials.Polytetrafluoroethylene, 288)
                .EUt(480)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("pattern_provider")
                .inputItems(AEItems.FORMATION_CORE.asItem())
                .inputItems(AEItems.ANNIHILATION_CORE.asItem())
                .inputItems(CustomTags.HV_CIRCUITS)
                .inputItems(GTItems.ROBOT_ARM_MV.asStack(2))
                .inputItems(TagPrefix.frameGt, GTMaterials.Aluminium)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .inputItems(AEItems.LOGIC_PROCESSOR.asItem())
                .inputItems(AEItems.CALCULATION_PROCESSOR.asItem())
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem())
                .outputItems(AEBlocks.PATTERN_PROVIDER.block().asItem())
                .inputFluids(GTMaterials.PolyvinylChloride, 576)
                .EUt(480)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("requester")
                .inputItems(GTItems.TOOL_DATA_STICK.asStack(8))
                .inputItems(TagPrefix.frameGt, GTMaterials.Titanium)
                .inputItems(AEItems.ENGINEERING_PROCESSOR.asItem(), 4)
                .inputItems("ae2:cable_interface")
                .inputItems(AEBlocks.CRAFTING_ACCELERATOR.block().asItem())
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Amethyst, 2)
                .inputItems("ae2:fluix_glass_cable", 4)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 6)
                .inputItems(AEBlocks.INTERFACE.block().asItem())
                .outputItems("merequester:requester")
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .EUt(7680)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cell_component_1k")
                .inputItems(CustomTags.ULV_CIRCUITS, 2)
                .inputItems(TagPrefix.plate, GTMaterials.NetherQuartz, 2)
                .inputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone, 2)
                .inputItems(new ItemStack(AEItems.SKY_DUST.asItem(), 2))
                .inputItems(TagPrefix.plate, GTMaterials.CertusQuartz, 2)
                .inputFluids(GTMaterials.Tin.getFluid(72))
                .outputItems(new ItemStack(AEItems.CELL_COMPONENT_1K.asItem()))
                .EUt(7)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cell_component_4k")
                .inputItems(new ItemStack(AEItems.CELL_COMPONENT_1K.asItem()))
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .inputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Quartzite)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(new ItemStack(AEItems.CELL_COMPONENT_4K.asItem()))
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cell_component_16k")
                .inputItems(new ItemStack(AEItems.CELL_COMPONENT_4K.asItem()))
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .inputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .inputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Quartzite)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(new ItemStack(AEItems.CELL_COMPONENT_16K.asItem()))
                .EUt(30)
                .duration(300)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cell_component_64k")
                .inputItems(new ItemStack(AEItems.CELL_COMPONENT_16K.asItem()))
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .inputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Quartzite)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(new ItemStack(AEItems.CELL_COMPONENT_64K.asItem()))
                .EUt(120)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("cell_component_256k")
                .inputItems(new ItemStack(AEItems.CELL_COMPONENT_64K.asItem()))
                .inputItems(CustomTags.EV_CIRCUITS, 2)
                .inputItems(new ItemStack(AEItems.LOGIC_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Redstone)
                .inputItems(new ItemStack(AEItems.CALCULATION_PROCESSOR.asItem()))
                .inputItems(TagPrefix.plate, GTMaterials.Quartzite)
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(72))
                .outputItems(new ItemStack(AEItems.CELL_COMPONENT_256K.asItem()))
                .EUt(120)
                .duration(300)
                .save();

        if (GTOCore.isNormal()) {
            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("blank_pattern"), new ItemStack(AEItems.BLANK_PATTERN.asItem()),
                    "ABA",
                    "CDC",
                    "EEE",
                    'A', CustomTags.LV_CIRCUITS, 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.RedAlloy), 'C', new MaterialEntry(TagPrefix.plate, GTMaterials.Glass), 'D', GTItems.SHAPE_EMPTY.asStack(), 'E', new MaterialEntry(TagPrefix.plate, GTMaterials.PolyvinylChloride));
        } else {
            ASSEMBLER_RECIPES.recipeBuilder("blank_pattern")
                    .inputItems(TagPrefix.plate, GTMaterials.Steel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 2)
                    .inputItems(TagPrefix.plate, GTMaterials.RedAlloy, 4)
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .inputFluids(GTMaterials.Glass.getFluid(288))
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem()))
                    .EUt(120)
                    .duration(200)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("blank_pattern_better")
                    .inputItems(TagPrefix.plate, GTMaterials.Steel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene, 2)
                    .inputItems(TagPrefix.plate, GTMaterials.RedAlloy, 4)
                    .inputItems(CustomTags.EV_CIRCUITS)
                    .inputFluids(GTMaterials.Glass.getFluid(1152))
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 4))
                    .EUt(480)
                    .duration(200)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("blank_pattern_best")
                    .inputItems(TagPrefix.plate, GTMaterials.Steel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole, 2)
                    .inputItems(TagPrefix.plate, GTMaterials.RedAlloy, 4)
                    .inputItems(CustomTags.IV_CIRCUITS)
                    .inputFluids(GTMaterials.Glass.getFluid(2304))
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 16))
                    .EUt(1920)
                    .duration(200)
                    .save();
        }
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        if (GTOCore.isSimple()) return;
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_1k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_4k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_16k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_64k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_256k_part"));
        filters.add(AppEng.makeId("decorative/quartz_block"));
        filters.add(AppEng.makeId("decorative/fluix_block"));
        filters.add(AppEng.makeId("misc/deconstruction_certus_quartz_block"));
        filters.add(AppEng.makeId("misc/deconstruction_fluix_block"));
        filters.add(AppEng.makeId("misc/fluixpearl"));
        filters.add(AppEng.makeId("network/cables/glass_fluix"));
        filters.add(AppEng.makeId("network/crafting/patterns_blank"));
        filters.add(AppEng.makeId("network/parts/export_bus"));
        filters.add(AppEng.makeId("network/parts/import_bus"));
        filters.add(AppEng.makeId("network/wireless_part"));
        filters.add(AppEng.makeId("network/crafting/cpu_crafting_unit"));
        filters.add(AppEng.makeId("materials/annihilationcore"));
        filters.add(AppEng.makeId("materials/formationcore"));
        filters.add(AppEng.makeId("materials/advancedcard"));
        filters.add(AppEng.makeId("materials/basiccard"));
        filters.add(AppEng.makeId("network/cables/dense_covered_fluix"));
        filters.add(AppEng.makeId("network/cables/dense_smart_from_smart"));
        filters.add(AppEng.makeId("network/cables/dense_smart_fluix"));
        filters.add(AppEng.makeId("network/blocks/controller"));
        filters.add(AppEng.makeId("network/blocks/storage_drive"));
        filters.add(AppEng.makeId("network/crafting/molecular_assembler"));
        filters.add(AppEng.makeId("network/blocks/energy_energy_acceptor"));
        filters.add(AppEng.makeId("network/blocks/interfaces_interface"));
        filters.add(AppEng.makeId("network/blocks/pattern_providers_interface"));

        filters.add(RLUtils.fromNamespaceAndPath("merequester", "requester"));

        filters.add(ExtendedAE.id("cobblestone_cell"));
        filters.add(ExtendedAE.id("water_cell"));
        filters.add(ExtendedAE.id("tape"));
        filters.add(ExtendedAE.id("assembler_matrix_wall"));
        filters.add(ExtendedAE.id("assembler_matrix_frame"));
        filters.add(ExtendedAE.id("assembler_matrix_crafter"));
        filters.add(ExtendedAE.id("assembler_matrix_pattern"));
        filters.add(ExtendedAE.id("assembler_matrix_speed"));
    }
}
