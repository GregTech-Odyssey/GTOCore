package com.gtocore.data.recipe.gtm.misc;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.recipe.FacadeCoverRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOItems.*;

public final class CraftingRecipeLoader {

    public static void init() {
        VanillaRecipeHelper.addShapedRecipe("small_wooden_pipe", ChemicalHelper.get(pipeSmallFluid, Wood),
                "sWr", 'W', ItemTags.PLANKS);
        VanillaRecipeHelper.addShapedRecipe("normal_wooden_pipe", ChemicalHelper.get(pipeNormalFluid, Wood),
                "WWW", "s r", 'W', ItemTags.PLANKS);
        VanillaRecipeHelper.addShapedRecipe("large_wooden_pipe", ChemicalHelper.get(pipeLargeFluid, Wood),
                "WWW", "s r", "WWW", 'W', ItemTags.PLANKS);

        VanillaRecipeHelper.addShapedRecipe("small_treated_wooden_pipe",
                ChemicalHelper.get(pipeSmallFluid, TreatedWood), "sWr", 'W', GTBlocks.TREATED_WOOD_PLANK.asItem());
        VanillaRecipeHelper.addShapedRecipe("normal_treated_wooden_pipe",
                ChemicalHelper.get(pipeNormalFluid, TreatedWood), "WWW", "s r", 'W',
                GTBlocks.TREATED_WOOD_PLANK.asItem());
        VanillaRecipeHelper.addShapedRecipe("large_treated_wooden_pipe",
                ChemicalHelper.get(pipeLargeFluid, TreatedWood), "WWW", "s r", "WWW", 'W',
                GTBlocks.TREATED_WOOD_PLANK.asItem());

        VanillaRecipeHelper.addShapelessRecipe("programmed_circuit", PROGRAMMED_CIRCUIT.asStack(),
                CustomTags.LV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe("item_filter", ITEM_FILTER.asItem(), "XXX", "XYX", "XXX", 'X',
                new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Steel));
        VanillaRecipeHelper.addShapedRecipe("fluid_filter_lapis", FLUID_FILTER.asItem(), "XXX", "XYX", "XXX",
                'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Lapis));
        VanillaRecipeHelper.addShapedRecipe("fluid_filter_lazurite", FLUID_FILTER.asItem(), "XXX", "XYX",
                "XXX", 'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Lazurite));
        VanillaRecipeHelper.addShapedRecipe("fluid_filter_sodalite", FLUID_FILTER.asItem(), "XXX", "XYX",
                "XXX", 'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Sodalite));

        VanillaRecipeHelper.addShapedRecipe("tag_filter_olivine", TAG_FILTER.asItem(),
                "XXX", "XYX", "XXX", 'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Olivine));
        VanillaRecipeHelper.addShapedRecipe("tag_filter_emerald", TAG_FILTER.asItem(),
                "XXX", "XYX", "XXX", 'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Emerald));
        VanillaRecipeHelper.addShapedRecipe("fluid_tag_filter", TAG_FLUID_FILTER.asItem(),
                "XXX", "XYX", "XXX", 'X', new MaterialEntry(foil, Zinc), 'Y', new MaterialEntry(plate, Amethyst));

        VanillaRecipeHelper.addShapedRecipe("item_smart_filter_olivine", SMART_ITEM_FILTER.asItem(), "XEX",
                "XCX", "XEX", 'X', new MaterialEntry(foil, Zinc), 'C', CustomTags.LV_CIRCUITS, 'E',
                new MaterialEntry(plate, Ruby));

        VanillaRecipeHelper.addShapedRecipe("plank_to_wooden_shape", WOODEN_FORM_EMPTY.asItem(), "   ",
                " X ", "s  ", 'X', ItemTags.PLANKS);
        VanillaRecipeHelper.addShapedRecipe("wooden_shape_brick", WOODEN_FORM_BRICK.asItem(), "k ", " X",
                'X', WOODEN_FORM_EMPTY.asItem());

        VanillaRecipeHelper.addShapedRecipe("compressed_coke_clay", COMPRESSED_COKE_CLAY.asItem(), 3, "XXX",
                "SYS", "SSS", 'Y', WOODEN_FORM_BRICK.asItem(), 'X', new ItemStack(Items.CLAY_BALL), 'S',
                ItemTags.SAND);
        VanillaRecipeHelper.addShapelessRecipe("fireclay_dust", ChemicalHelper.get(dust, Fireclay, 2),
                new MaterialEntry(dust, Brick), new MaterialEntry(dust, Clay));
        VanillaRecipeHelper.addSmeltingRecipe("coke_oven_brick", COMPRESSED_COKE_CLAY.asItem(),
                COKE_OVEN_BRICK.asItem(), 0.3f);
        VanillaRecipeHelper.addSmeltingRecipe("fireclay_brick", COMPRESSED_FIRECLAY.asItem(),
                FIRECLAY_BRICK.asItem(), 0.3f);

        VanillaRecipeHelper.addSmeltingRecipe("wrought_iron_nugget", ChemicalHelper.getTag(nugget, Iron),
                ChemicalHelper.get(nugget, WroughtIron));
        VanillaRecipeHelper.addShapelessRecipe("nugget_disassembling_iron",
                new ItemStack(Items.IRON_NUGGET, 9), new ItemStack(Items.IRON_INGOT), 's');

        VanillaRecipeHelper.addShapedFluidContainerRecipe("treated_wood_planks",
                GTBlocks.TREATED_WOOD_PLANK.asStack(8),
                "PPP", "PBP", "PPP", 'P', ItemTags.PLANKS, 'B',
                new FluidContainerIngredient(Creosote.getFluid(1000)));

        VanillaRecipeHelper.addShapedRecipe("rubber_ring", ChemicalHelper.get(ring, Rubber), "k", "X", 'X',
                new MaterialEntry(plate, Rubber));
        VanillaRecipeHelper.addShapedRecipe("silicone_rubber_ring", ChemicalHelper.get(ring, SiliconeRubber),
                "k", "P", 'P', ChemicalHelper.get(plate, SiliconeRubber));
        VanillaRecipeHelper.addShapedRecipe("styrene_rubber_ring",
                ChemicalHelper.get(ring, StyreneButadieneRubber), "k", "P", 'P',
                ChemicalHelper.get(plate, StyreneButadieneRubber));

        VanillaRecipeHelper.addShapelessRecipe("iron_magnetic_stick", ChemicalHelper.get(rod, IronMagnetic),
                new MaterialEntry(rod, Iron), new MaterialEntry(dust, Redstone),
                new MaterialEntry(dust, Redstone), new MaterialEntry(dust, Redstone),
                new MaterialEntry(dust, Redstone));

        VanillaRecipeHelper.addShapedRecipe("component_grinder_diamond", COMPONENT_GRINDER_DIAMOND.asItem(),
                "XSX", "SDS", "XSX", 'X', new MaterialEntry(dust, Diamond), 'S',
                new MaterialEntry(plateDouble, Steel), 'D', new MaterialEntry(gem, Diamond));
        VanillaRecipeHelper.addShapedRecipe("component_grinder_tungsten",
                COMPONENT_GRINDER_TUNGSTEN.asItem(), "WSW", "SDS", "WSW", 'W', new MaterialEntry(plate, Tungsten),
                'S', new MaterialEntry(plateDouble, VanadiumSteel), 'D', new MaterialEntry(gem, Diamond));

        VanillaRecipeHelper.addShapedRecipe("minecart_wheels_iron", IRON_MINECART_WHEELS.asItem(), " h ",
                "RSR", " w ", 'R', new MaterialEntry(ring, Iron), 'S', new MaterialEntry(rod, Iron));
        VanillaRecipeHelper.addShapedRecipe("minecart_wheels_steel", STEEL_MINECART_WHEELS.asItem(), " h ",
                "RSR", " w ", 'R', new MaterialEntry(ring, Steel), 'S', new MaterialEntry(rod, Steel));

        VanillaRecipeHelper.addShapedRecipe("nano_saber", NANO_SABER.asItem(), "PIC", "PIC",
                "XEX", 'P', new MaterialEntry(plate, Platinum), 'I', new MaterialEntry(plate, Ruridit), 'C',
                CARBON_FIBER_PLATE.asItem(), 'X', CustomTags.EV_CIRCUITS, 'E', ENERGIUM_CRYSTAL.asItem());

        VanillaRecipeHelper.addShapedRecipe("solar_panel_basic", COVER_SOLAR_PANEL.asItem(), "WGW", "CPC",
                'W', SILICON_WAFER.asItem(), 'G', new ItemStack(Blocks.GLASS_PANE), 'C', CustomTags.LV_CIRCUITS, 'P',
                CARBON_FIBER_PLATE.asItem());
        VanillaRecipeHelper.addShapedRecipe("solar_panel_ulv", COVER_SOLAR_PANEL_ULV.asItem(), "WGW", "CAC",
                "P P", 'W', PHOSPHORUS_WAFER.asItem(), 'G', Tags.Items.GLASS_PANES, 'C', CustomTags.HV_CIRCUITS, 'P',
                ChemicalHelper.get(plate, GalliumArsenide), 'A', ChemicalHelper.get(wireGtQuadruple, Graphene));
        VanillaRecipeHelper.addShapedRecipe("solar_panel_lv", COVER_SOLAR_PANEL_LV.asItem(), "WGW", "CAC",
                "P P", 'W', NAQUADAH_WAFER.asItem(), 'G', GTBlocks.CASING_TEMPERED_GLASS.asItem(), 'C',
                CustomTags.LuV_CIRCUITS, 'P', ChemicalHelper.get(plate, IndiumGalliumPhosphide), 'A',
                ChemicalHelper.get(wireGtHex, Graphene));

        VanillaRecipeHelper.addShapedRecipe("universal_fluid_cell", FLUID_CELL_UNIVERSAL.asItem(), "C ",
                "  ", 'C', FLUID_CELL);
        VanillaRecipeHelper.addShapedRecipe("universal_fluid_cell_revert", FLUID_CELL.asItem(), "C ", "  ",
                'C', FLUID_CELL_UNIVERSAL);

        VanillaRecipeHelper.addShapedRecipe("blacklight", BLACKLIGHT.asItem(), "SPS", "GRG", "CPK", 'S',
                new MaterialEntry(screw, TungstenCarbide), 'P', new MaterialEntry(plate, TungstenCarbide), 'G',
                GTBlocks.CASING_LAMINATED_GLASS.asItem(), 'R', new MaterialEntry(spring, Europium), 'C',
                CustomTags.IV_CIRCUITS, 'K', new MaterialEntry(cableGtSingle, Platinum));

        VanillaRecipeHelper.addShapedRecipe(true, "filter_casing", GTBlocks.FILTER_CASING.asItem(), "BBB",
                "III", "MFR", 'B', new ItemStack(Blocks.IRON_BARS), 'I', ITEM_FILTER.asItem(), 'M',
                ELECTRIC_MOTOR_MV.asItem(), 'F', new MaterialEntry(frameGt, Steel), 'R',
                new MaterialEntry(rotor, Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "filter_casing_sterile",
                GTBlocks.FILTER_CASING_STERILE.asItem(), "BEB", "ISI", "MFR", 'B',
                new MaterialEntry(pipeLargeFluid, Polybenzimidazole), 'E', EMITTER_ZPM.asItem(), 'I',
                ITEM_FILTER.asItem(), 'S', BLACKLIGHT.asItem(), 'M', ELECTRIC_MOTOR_ZPM.asItem(), 'F',
                new MaterialEntry(frameGt, Tritanium), 'R', new MaterialEntry(rotor, NaquadahAlloy));

        VanillaRecipeHelper.addShapedRecipe("mana_ulv_prospector", PROSPECTOR_MANA_ULV.asItem(),
                "ASB", "EDE", "SCS",
                'A', BotaniaFlowerBlocks.solegnolia, 'B', BotaniaFlowerBlocks.hopperhock,
                'C', BotaniaItems.manaTablet, 'D', BotaniaBlocks.manaGlass,
                'E', new MaterialEntry(gem, GTOMaterials.ManaDiamond), 'S', new MaterialEntry(plate, GTOMaterials.Manasteel));

        VanillaRecipeHelper.addShapedRecipe("mana_lv_prospector", PROSPECTOR_MANA_LV.asItem(),
                "ASB", "EDE", "SCS",
                'A', EMITTER_LV, 'B', SENSOR_LV,
                'C', BotaniaItems.manaTablet, 'D', BotaniaItems.lensEfficiency,
                'E', CustomTags.LV_CIRCUITS, 'S', new MaterialEntry(plate, GTOMaterials.Terrasteel));

        VanillaRecipeHelper.addShapedRecipe("mana_hv_prospector", PROSPECTOR_MANA_HV.asItem(),
                "ASB", "EDE", "SCS",
                'A', EMITTER_HV, 'B', SENSOR_HV,
                'C', BotaniaItems.manaTablet, 'D', BotaniaBlocks.bifrostPerm,
                'E', CustomTags.HV_CIRCUITS, 'S', new MaterialEntry(plate, GTOMaterials.Alfsteel));

        VanillaRecipeHelper.addShapedRecipe("machine_coordinate_card", COORDINATE_CARD.asItem(),
                " A ",
                "ABA",
                " A ",
                'A', new MaterialEntry(plate, Steel), 'B', new MaterialEntry(plate, Glass));

        ///////////////////////////////////////////////////
        // Shapes and Molds //
        ///////////////////////////////////////////////////
        VanillaRecipeHelper.addShapedRecipe("shape_empty", SHAPE_EMPTY.asItem(), "hf", "PP", "PP", 'P',
                new MaterialEntry(plate, Steel));

        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_bottle", SHAPE_EXTRUDER_BOTTLE.asStack(),
                "  x", " S ", "   ", 'S', SHAPE_EXTRUDER_RING.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_gear", SHAPE_EXTRUDER_GEAR.asStack(), "x  ",
                " S ", "   ", 'S', SHAPE_EXTRUDER_RING.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_block", SHAPE_EXTRUDER_BLOCK.asStack(),
                "x  ", " S ", "   ", 'S', SHAPE_EXTRUDER_INGOT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_pipe_huge",
                SHAPE_EXTRUDER_PIPE_HUGE.asStack(), "   ", " S ", "  x", 'S', SHAPE_EXTRUDER_BOLT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_pipe_large",
                SHAPE_EXTRUDER_PIPE_LARGE.asStack(), "   ", " Sx", "   ", 'S', SHAPE_EXTRUDER_BOLT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_pipe_normal",
                SHAPE_EXTRUDER_PIPE_NORMAL.asStack(), "  x", " S ", "   ", 'S', SHAPE_EXTRUDER_BOLT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_pipe_small",
                SHAPE_EXTRUDER_PIPE_SMALL.asStack(), " x ", " S ", "   ", 'S', SHAPE_EXTRUDER_BOLT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_pipe_tiny",
                SHAPE_EXTRUDER_PIPE_TINY.asStack(), "x  ", " S ", "   ", 'S', SHAPE_EXTRUDER_BOLT.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_wire", SHAPE_EXTRUDER_WIRE.asStack(), " x ",
                " S ", "   ", 'S', SHAPE_EXTRUDER_ROD.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_ingot", SHAPE_EXTRUDER_INGOT.asStack(),
                "x  ", " S ", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_cell", SHAPE_EXTRUDER_CELL.asStack(), "   ",
                " Sx", "   ", 'S', SHAPE_EXTRUDER_RING.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_ring", SHAPE_EXTRUDER_RING.asStack(), "   ",
                " S ", " x ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_bolt", SHAPE_EXTRUDER_BOLT.asStack(), "x  ",
                " S ", "   ", 'S', SHAPE_EXTRUDER_ROD.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_rod", SHAPE_EXTRUDER_ROD.asStack(), "   ",
                " Sx", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_plate", SHAPE_EXTRUDER_PLATE.asStack(),
                "x  ", " S ", "   ", 'S', SHAPE_EXTRUDER_FOIL.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_rod_long",
                SHAPE_EXTRUDER_ROD_LONG.asStack(), "  x", " S ", "   ", 'S', SHAPE_EXTRUDER_ROD.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_gear_small",
                SHAPE_EXTRUDER_GEAR_SMALL.asStack(), " x ", " S ", "   ", 'S', SHAPE_EXTRUDER_RING.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_foil", SHAPE_EXTRUDER_FOIL.asStack(), "   ",
                " S ", "  x", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_extruder_rotor", SHAPE_EXTRUDER_ROTOR.asStack(),
                "   ", " S ", "x  ", 'S', SHAPE_EMPTY.asItem());

        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_pill", SHAPE_MOLD_PILL.asStack(), "  h",
                "  S", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_rotor", SHAPE_MOLD_ROTOR.asStack(), "  h",
                " S ", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_gear_small", SHAPE_MOLD_GEAR_SMALL.asStack(),
                "   ", "   ", "h S", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_name", SHAPE_MOLD_NAME.asStack(), "  S", "   ",
                "h  ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_anvil", SHAPE_MOLD_ANVIL.asStack(), "  S",
                "   ", " h ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_cylinder", SHAPE_MOLD_CYLINDER.asStack(), "  S",
                "   ", "  h", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_nugget", SHAPE_MOLD_NUGGET.asStack(), "   ",
                "S h", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_block", SHAPE_MOLD_BLOCK.asStack(), "   ",
                "hS ", "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_ball", SHAPE_MOLD_BALL.asStack(), "   ", " S ",
                "h  ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_ingot", SHAPE_MOLD_INGOT.asStack(), "   ",
                " S ", " h ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_bottle", SHAPE_MOLD_BOTTLE.asStack(), "   ",
                " S ", "  h", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_gear", SHAPE_MOLD_GEAR.asStack(), "   ", " Sh",
                "   ", 'S', SHAPE_EMPTY.asItem());
        VanillaRecipeHelper.addStrictShapedRecipe("shape_mold_plate", SHAPE_MOLD_PLATE.asStack(), " h ",
                " S ", "   ", 'S', SHAPE_EMPTY.asItem());
        ///////////////////////////////////////////////////
        // Armors //
        ///////////////////////////////////////////////////
        VanillaRecipeHelper.addShapedRecipe("nightvision_goggles", GTItems.NIGHTVISION_GOGGLES.asItem(),
                "CSC", "RBR", "LdL", 'C', CustomTags.ULV_CIRCUITS, 'S', new MaterialEntry(screw, Steel), 'R',
                new MaterialEntry(ring, Rubber), 'B', GTItems.BATTERY_LV_SODIUM, 'L',
                new MaterialEntry(lens, Glass));
        VanillaRecipeHelper.addShapedRecipe("fluid_jetpack", GTItems.LIQUID_FUEL_JETPACK.asItem(), "xCw",
                "SUS", "RIR", 'C', CustomTags.LV_CIRCUITS, 'S', GTItems.FLUID_CELL_LARGE_STEEL.asItem(), 'U',
                GTItems.ELECTRIC_PUMP_LV.asItem(), 'R', new MaterialEntry(rotor, Lead), 'I',
                new MaterialEntry(pipeSmallFluid, Potin));
        VanillaRecipeHelper.addShapedRecipe("electric_jetpack", GTItems.ELECTRIC_JETPACK.asItem(), "xCd",
                "TBT", "I I", 'C', CustomTags.MV_CIRCUITS, 'T', GTItems.POWER_THRUSTER.asItem(), 'B',
                GTItems.BATTERY_MV_LITHIUM.asItem(), 'I', new MaterialEntry(wireGtDouble, AnnealedCopper));
        VanillaRecipeHelper.addShapedRecipe("electric_jetpack_advanced",
                GTItems.ELECTRIC_JETPACK_ADVANCED.asItem(), "xJd", "TBT", "WCW", 'J',
                GTItems.ELECTRIC_JETPACK.asItem(), 'T', GTItems.POWER_THRUSTER_ADVANCED.asItem(), 'B',
                ENERGIUM_CRYSTAL.asItem(), 'W', new MaterialEntry(wireGtQuadruple, Gold), 'C',
                CustomTags.HV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe("nano_helmet", GTItems.NANO_HELMET.asItem(), "PPP", "PNP", "xEd",
                'P', GTItems.CARBON_FIBER_PLATE.asItem(), 'N', GTItems.NIGHTVISION_GOGGLES.asItem(), 'E',
                GTItems.ENERGIUM_CRYSTAL.asItem());
        VanillaRecipeHelper.addShapedRecipe("nano_chestplate", GTItems.NANO_CHESTPLATE.asItem(), "PEP",
                "PPP", "PPP", 'P', GTItems.CARBON_FIBER_PLATE.asItem(), 'E', GTItems.ENERGIUM_CRYSTAL.asItem());
        VanillaRecipeHelper.addShapedRecipe("nano_leggings", GTItems.NANO_LEGGINGS.asItem(), "PPP", "PEP",
                "PxP", 'P', GTItems.CARBON_FIBER_PLATE.asItem(), 'E', GTItems.ENERGIUM_CRYSTAL.asItem());
        VanillaRecipeHelper.addShapedRecipe("nano_boots", GTItems.NANO_BOOTS.asItem(), "PxP", "PEP", 'P',
                GTItems.CARBON_FIBER_PLATE.asItem(), 'E', GTItems.ENERGIUM_CRYSTAL.asItem());
        VanillaRecipeHelper.addShapedRecipe("nano_chestplate_advanced",
                GTItems.NANO_CHESTPLATE_ADVANCED.asItem(), "xJd", "PNP", "WCW", 'J',
                GTItems.ELECTRIC_JETPACK_ADVANCED.asItem(), 'P', GTItems.LOW_POWER_INTEGRATED_CIRCUIT.asItem(), 'N',
                GTItems.NANO_CHESTPLATE.asItem(), 'W', new MaterialEntry(wireGtQuadruple, Platinum), 'C',
                CustomTags.IV_CIRCUITS);
        VanillaRecipeHelper.addShapedRecipe("gravitation_engine", GTItems.GRAVITATION_ENGINE.asItem(), "ESE",
                "POP", "ESE", 'E', GTItems.EMITTER_LuV.asItem(), 'S', new MaterialEntry(wireGtQuadruple, Osmium),
                'P', new MaterialEntry(plateDouble, Iridium), 'O', GTItems.ENERGY_LAPOTRONIC_ORB.asItem());

        VanillaRecipeHelper.addShapedRecipe("face_mask", FACE_MASK.asItem(), "S S", "PPP", 'S', Items.STRING,
                'P', Items.PAPER);
        VanillaRecipeHelper.addShapedRecipe("rubber_gloves", RUBBER_GLOVES.asItem(), "P P", 'P',
                new MaterialEntry(plate, Rubber));

        VanillaRecipeHelper.addShapedRecipe("powderbarrel", new ItemStack(GTBlocks.POWDERBARREL), "PSP",
                "GGG", "PGP",
                'P', new MaterialEntry(plate, Wood),
                'S', new ItemStack(Items.STRING),
                'G', new MaterialEntry(dust, Gunpowder));

        ///////////////////////////////////////////////////
        // Special //
        ///////////////////////////////////////////////////
        SpecialRecipeBuilder.special(FacadeCoverRecipe.SERIALIZER).save(GTDynamicDataPack.CONSUMER, "gtceu:crafting/facade_cover");
    }
}
