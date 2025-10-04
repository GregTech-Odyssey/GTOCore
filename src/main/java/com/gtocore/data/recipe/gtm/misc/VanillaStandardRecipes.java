package com.gtocore.data.recipe.gtm.misc;

import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class VanillaStandardRecipes {

    public static void init() {
        compressingRecipes();
        glassRecipes();
        smashingRecipes();
        woodRecipes();
        cuttingRecipes();
        dyingCleaningRecipes();
        redstoneRecipes();
        metalRecipes();
        miscRecipes();
        mixingRecipes();
        dyeRecipes();
    }

    /**
     * + Adds compression recipes for vanilla items
     */
    private static void compressingRecipes() {
        COMPRESSOR_RECIPES.recipeBuilder("stone_from_dust").duration(300).EUt(2)
                .inputItems(plate, Stone, 9)
                .outputItems(Blocks.STONE)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("sandstone").duration(300).EUt(2)
                .inputItems(Blocks.SAND, 4)
                .outputItems(Blocks.SANDSTONE)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("red_sandstone").duration(300).EUt(2)
                .inputItems(Blocks.RED_SAND, 4)
                .outputItems(Blocks.RED_SANDSTONE)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("bricks").duration(300).EUt(2)
                .inputItems(Items.BRICK, 4)
                .outputItems(Blocks.BRICKS)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("nether_bricks").duration(300).EUt(2)
                .inputItems(Items.NETHER_BRICK, 4)
                .outputItems(Blocks.NETHER_BRICKS)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("ice_from_snow").duration(300).EUt(2)
                .inputItems(Blocks.SNOW)
                .outputItems(Blocks.ICE)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("clay").duration(300).EUt(2)
                .inputItems(Items.CLAY_BALL, 4)
                .outputItems(Blocks.CLAY)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("snowballs_to_snow").duration(200).EUt(2)
                .inputItems(Items.SNOWBALL, 4)
                .outputItems(Items.SNOW_BLOCK)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("glowstone").duration(300).EUt(2)
                .inputItems(Items.GLOWSTONE_DUST, 4)
                .outputItems(Blocks.GLOWSTONE)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("packed_ice")
                .inputItems(Blocks.ICE, 9)
                .outputItems(Blocks.PACKED_ICE)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("blue_ice")
                .inputItems(Blocks.PACKED_ICE, 9)
                .outputItems(Blocks.BLUE_ICE)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("ice_from_dust")
                .inputItems(dust, Ice)
                .outputItems(Blocks.ICE)
                .duration(300).EUt(2)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("dripstone_block_from_pointed_dripstone")
                .inputItems(Items.POINTED_DRIPSTONE, 4)
                .outputItems(Blocks.DRIPSTONE_BLOCK)
                .duration(300).EUt(2)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("pointed_dripstone_from_dripstone_block")
                .inputItems(Blocks.DRIPSTONE_BLOCK)
                .outputItems(Items.POINTED_DRIPSTONE, 4)
                .duration(300).EUt(2)
                .save();

        PACKER_RECIPES.recipeBuilder("hay_block")
                .inputItems(Items.WHEAT, 9)
                .circuitMeta(8)
                .outputItems(Blocks.HAY_BLOCK)
                .duration(200).EUt(2)
                .save();

        PACKER_RECIPES.recipeBuilder("wheat")
                .inputItems(Blocks.HAY_BLOCK)
                .outputItems(Items.WHEAT, 9)
                .circuitMeta(9)
                .duration(200).EUt(2)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("wheat_from_hay_block")
                .inputItems(Blocks.HAY_BLOCK)
                .outputItems(Items.WHEAT, 9)
                .duration(200).EUt(2)
                .save();

        PACKER_RECIPES.recipeBuilder("melon")
                .inputItems(Items.MELON_SLICE, 9)
                .circuitMeta(9)
                .outputItems(Blocks.MELON)
                .duration(200).EUt(2)
                .save();
    }

    /**
     * + Adds new glass related recipes
     * + Adds steam age manual glass recipes
     * - Removes some glass related recipes based on configs
     */
    private static void glassRecipes() {
        VanillaRecipeHelper.addShapedRecipe("glass_dust_hammer", ChemicalHelper.get(dust, Glass), "hG", 'G',
                new ItemStack(Blocks.GLASS));

        VanillaRecipeHelper.addShapedRecipe("quartz_sand", ChemicalHelper.get(dust, QuartzSand), "S", "m",
                'S', new ItemStack(Blocks.SAND));

        MACERATOR_RECIPES.recipeBuilder("quartz_sand_from_sand")
                .inputItems(Blocks.SAND)
                .outputItems(dust, QuartzSand)
                .duration(30).EUt(2).save();

        VanillaRecipeHelper.addShapelessRecipe("glass_dust_flint", ChemicalHelper.get(dust, Glass),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dustTiny, Flint));

        VanillaRecipeHelper.addShapelessRecipe("glass_full_dust_flint", ChemicalHelper.get(dust, Glass, 8),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, QuartzSand),
                new MaterialEntry(dust, Flint));

        MIXER_RECIPES.recipeBuilder("glass_from_quartzite").duration(160).EUt(VA[ULV])
                .inputItems(dustSmall, Flint)
                .inputItems(dust, Quartzite, 4)
                .outputItems(dust, Glass, 5)
                .save();

        MIXER_RECIPES.recipeBuilder("full_dust_glass_from_quartzite").duration(640).EUt(VA[ULV])
                .inputItems(dust, Flint)
                .inputItems(dust, Quartzite, 16)
                .outputItems(dust, Glass, 20)
                .save();

        MIXER_RECIPES.recipeBuilder("glass_from_quartz_sand").duration(200).EUt(VA[ULV])
                .inputItems(dustSmall, Flint)
                .inputItems(dust, QuartzSand, 4)
                .outputItems(dust, Glass, 4)
                .save();

        MIXER_RECIPES.recipeBuilder("full_dust_glass_from_sand").duration(800).EUt(VA[ULV])
                .inputItems(dust, Flint)
                .inputItems(dust, QuartzSand, 16)
                .outputItems(dust, Glass, 16)
                .save();

        ARC_FURNACE_RECIPES.recipeBuilder("glass_from_sand").duration(20).EUt(VA[LV])
                .inputItems(ItemTags.SMELTS_TO_GLASS)
                .outputItems(Blocks.GLASS, 2)
                .save();

        FORMING_PRESS_RECIPES.recipeBuilder("form_glass").duration(80).EUt(VA[LV])
                .notConsumable(SHAPE_MOLD_BLOCK)
                .inputItems(dust, Glass)
                .outputItems(Blocks.GLASS).save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("glass_bottle").duration(64).EUt(4)
                .inputItems(dust, Glass)
                .notConsumable(SHAPE_MOLD_BOTTLE)
                .outputItems(Items.GLASS_BOTTLE).save();

        EXTRUDER_RECIPES.recipeBuilder("glass_bottle").duration(32).EUt(16)
                .inputItems(dust, Glass)
                .notConsumable(SHAPE_EXTRUDER_BOTTLE)
                .outputItems(Items.GLASS_BOTTLE)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("glass_bottle").duration(12).EUt(4)
                .inputFluids(Glass.getFluid(L))
                .notConsumable(SHAPE_MOLD_BOTTLE)
                .outputItems(Items.GLASS_BOTTLE)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("glass").duration(120).EUt(16)
                .inputItems(dust, Glass)
                .notConsumable(SHAPE_MOLD_BLOCK)
                .outputItems(Blocks.GLASS)
                .save();

        CUTTER_RECIPES.recipeBuilder("glass_block_to_plate").duration(50).EUt(VA[ULV])
                .inputItems(Blocks.GLASS, 3)
                .outputItems(Blocks.GLASS_PANE, 8).save();
    }

    /**
     * Adds smashing related recipes for vanilla blocks and items
     */
    private static void smashingRecipes() {
        FORGE_HAMMER_RECIPES.recipeBuilder("cobblestone_to_gravel")
                .inputItems(ItemTags.STONE_CRAFTING_MATERIALS)
                .outputItems(Blocks.GRAVEL)
                .EUt(16).duration(10)
                .save();

        FORGE_HAMMER_RECIPES.recipeBuilder("gravel_to_sand")
                .inputItems(Blocks.GRAVEL)
                .outputItems(Blocks.SAND)
                .EUt(16).duration(10)
                .save();

        MACERATOR_RECIPES.recipeBuilder("gravel_to_flint")
                .inputItems(Blocks.GRAVEL)
                .outputItems(dust, Stone)
                .chancedOutput(new ItemStack(Items.FLINT), 1000, 1000)
                .duration(400).EUt(2)
                .save();

        // todo other sandstone types?
        FORGE_HAMMER_RECIPES.recipeBuilder("sandstone_to_sand")
                .inputItems(Blocks.SANDSTONE)
                .outputItems(Blocks.SAND)
                .EUt(2).duration(400).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("smooth_sandstone_to_sand")
                .inputItems(Blocks.SMOOTH_SANDSTONE)
                .outputItems(Blocks.SAND)
                .EUt(2).duration(400).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("chiseled_sandstone_to_sand")
                .inputItems(Blocks.CHISELED_SANDSTONE)
                .outputItems(Blocks.SAND)
                .EUt(2).duration(400).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("red_sandstone_to_red_sand")
                .inputItems(Blocks.RED_SANDSTONE)
                .outputItems(Blocks.RED_SAND)
                .EUt(2).duration(400).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("smooth_red_sandstone_to_red_sand")
                .inputItems(Blocks.SMOOTH_RED_SANDSTONE)
                .outputItems(Blocks.RED_SAND)
                .EUt(2).duration(400).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("chiseled_red_sandstone_to_red_sand")
                .inputItems(Blocks.CHISELED_RED_SANDSTONE)
                .outputItems(Blocks.RED_SAND)
                .EUt(2).duration(400).save();

        VanillaRecipeHelper.addShapelessRecipe("clay_block_to_dust", ChemicalHelper.get(dust, Clay), 'm',
                Blocks.CLAY);
        VanillaRecipeHelper.addShapelessRecipe("clay_ball_to_dust", ChemicalHelper.get(dustSmall, Clay), 'm',
                Items.CLAY_BALL);
        VanillaRecipeHelper.addShapelessRecipe("brick_block_to_dust", ChemicalHelper.get(dust, Brick), 'm',
                Blocks.BRICKS);
        VanillaRecipeHelper.addShapelessRecipe("brick_to_dust", ChemicalHelper.get(dustSmall, Brick), 'm',
                Items.BRICK);
        VanillaRecipeHelper.addShapelessRecipe("wheat_to_dust", ChemicalHelper.get(dust, Wheat), 'm',
                Items.WHEAT);
        VanillaRecipeHelper.addShapelessRecipe("gravel_to_flint", new ItemStack(Items.FLINT), 'm',
                Blocks.GRAVEL);
        VanillaRecipeHelper.addShapelessRecipe("bone_to_bone_meal", new ItemStack(Items.BONE_MEAL, 4), 'm',
                Items.BONE);
        VanillaRecipeHelper.addShapelessRecipe("blaze_rod_to_powder", new ItemStack(Items.BLAZE_POWDER, 3),
                'm', Items.BLAZE_ROD);

        MACERATOR_RECIPES.recipeBuilder("macerate_cocoa")
                .inputItems(Items.COCOA_BEANS)
                .outputItems(dust, Cocoa)
                .duration(400).EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("macerate_sugar_cane")
                .inputItems(Items.SUGAR_CANE)
                .outputItems(Items.SUGAR)
                .duration(400).EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("macerate_melon_block")
                .inputItems(Blocks.MELON)
                .outputItems(Items.MELON_SLICE, 8)
                .chancedOutput(new ItemStack(Items.MELON_SEEDS), 8000, 500)
                .duration(400).EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("macerate_pumpkin")
                .inputItems(Blocks.PUMPKIN)
                .outputItems(Items.PUMPKIN_SEEDS, 4)
                .duration(400).EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("macerate_melon_slice")
                .inputItems(Items.MELON_SLICE)
                .outputItems(Items.MELON_SEEDS)
                .duration(400).EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("macerate_wool")
                .inputItems(ItemTags.WOOL)
                .outputItems(Items.STRING)
                .chancedOutput(new ItemStack(Items.STRING), 9000, 0)
                .chancedOutput(new ItemStack(Items.STRING), 5000, 0)
                .chancedOutput(new ItemStack(Items.STRING), 2000, 0)
                .duration(200).EUt(2)
                .save();
    }

    /**
     * + Adds new recipes for wood related items and blocks
     */
    private static void woodRecipes() {
        MACERATOR_RECIPES.recipeBuilder("macerate_logs")
                .inputItems(ItemTags.LOGS)
                .outputItems(dust, Wood, 6)
                .chancedOutput(dust, Wood, 8000, 680)
                .duration(150).EUt(2)
                .save();

        LATHE_RECIPES.recipeBuilder("lathe_planks")
                .inputItems(ItemTags.PLANKS)
                .outputItems(Items.STICK, 2)
                .duration(10).EUt(VA[ULV])
                .save();

        LATHE_RECIPES.recipeBuilder("lathe_saplings")
                .inputItems(ItemTags.SAPLINGS)
                .outputItems(Items.STICK)
                .outputItems(dustTiny, Wood)
                .duration(16).EUt(VA[ULV])
                .save();

        LATHE_RECIPES.recipeBuilder("lathe_wood_slabs")
                .inputItems(ItemTags.WOODEN_SLABS)
                .outputItems(Items.BOWL)
                .outputItems(dustSmall, Wood)
                .duration(50).EUt(VA[ULV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("bookshelf")
                .inputItems(ItemTags.PLANKS, 6)
                .inputItems(Items.BOOK, 3)
                .outputItems(Blocks.BOOKSHELF)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("chest")
                .inputItems(ItemTags.PLANKS, 8)
                .outputItems(Blocks.CHEST)
                .duration(100).EUt(4).circuitMeta(8).save();

        ASSEMBLER_RECIPES.recipeBuilder("torch_coal")
                .inputItems(ItemTags.COALS)
                .inputItems(Items.STICK)
                .outputItems(Blocks.TORCH, 4)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("torch_coal_dust")
                .inputItems(dust, Coal)
                .inputItems(Items.STICK)
                .outputItems(Blocks.TORCH, 4)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("torch_charcoal_dust")
                .inputItems(dust, Charcoal)
                .inputItems(Items.STICK)
                .outputItems(Blocks.TORCH, 4)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("torch_coke_gem")
                .inputItems(gem, Coke)
                .inputItems(Items.STICK)
                .outputItems(Blocks.TORCH, 8)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("torch_coke_dust")
                .inputItems(dust, Coke)
                .inputItems(Items.STICK)
                .outputItems(Blocks.TORCH, 8)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("soul_torch")
                .inputItems(Blocks.TORCH)
                .inputItems(ItemTags.SOUL_FIRE_BASE_BLOCKS)
                .outputItems(Blocks.SOUL_TORCH)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("soul_lantern_from_lantern")
                .inputItems(Blocks.LANTERN)
                .inputItems(ItemTags.SOUL_FIRE_BASE_BLOCKS)
                .outputItems(Blocks.SOUL_LANTERN)
                .duration(100).EUt(1).save();

        VanillaRecipeHelper.addShapedRecipe("sticky_resin_torch", new ItemStack(Blocks.TORCH), 3, "X", "Y",
                'X', STICKY_RESIN, 'Y', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_sulfur", new ItemStack(Blocks.TORCH), 2, "C", "S", 'C',
                new MaterialEntry(dust, Sulfur), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_phosphorus", new ItemStack(Blocks.TORCH), 6, "C", "S", 'C',
                new MaterialEntry(dust, Phosphorus), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_coal_dust", new ItemStack(Blocks.TORCH), 4, "C", "S", 'C',
                new MaterialEntry(dust, Coal), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_charcoal_dust", new ItemStack(Blocks.TORCH), 4, "C", "S",
                'C', new MaterialEntry(dust, Charcoal), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_coke", new ItemStack(Blocks.TORCH), 8, "C", "S", 'C',
                new MaterialEntry(gem, Coke), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_coke_dust", new ItemStack(Blocks.TORCH), 8, "C", "S", 'C',
                new MaterialEntry(dust, Coke), 'S', new ItemStack(Items.STICK));
        VanillaRecipeHelper.addShapedRecipe("torch_creosote", new ItemStack(Blocks.TORCH), 16, "WB", "S ",
                'W', ItemTags.WOOL, 'S', new ItemStack(Items.STICK), 'B',
                new FluidContainerIngredient(Creosote.getFluid(1000)));
        VanillaRecipeHelper.addShapedRecipe("soul_torch", new ItemStack(Blocks.SOUL_TORCH), 1, "WB",
                'W', ItemTags.SOUL_FIRE_BASE_BLOCKS, 'B', new ItemStack(Blocks.TORCH));
        if (!ConfigHolder.INSTANCE.recipes.hardMiscRecipes) {
            VanillaRecipeHelper.addShapedRecipe("soul_lantern_from_lantern",
                    new ItemStack(Blocks.SOUL_LANTERN), 1, "WB",
                    'W', ItemTags.SOUL_FIRE_BASE_BLOCKS, 'B', new ItemStack(Blocks.LANTERN));
        }

        ASSEMBLER_RECIPES.recipeBuilder("redstone_torch").EUt(4).inputItems(dust, Redstone)
                .inputItems(Items.STICK).outputItems(Blocks.REDSTONE_TORCH)
                .circuitMeta(3).duration(100).save();
        ASSEMBLER_RECIPES.recipeBuilder("torch_sulfur").EUt(4).inputItems(Items.STICK)
                .inputItems(dust, Sulfur).outputItems(Blocks.TORCH, 2).duration(100).save();
        ASSEMBLER_RECIPES.recipeBuilder("torch_phosphorus").EUt(4).inputItems(Items.STICK)
                .inputItems(dust, Phosphorus).outputItems(Blocks.TORCH, 6).duration(100).save();

        ASSEMBLER_RECIPES.recipeBuilder("ladder").EUt(4).duration(40).circuitMeta(7)
                .inputItems(Items.STICK, 7).outputItems(Blocks.LADDER, 2).save();

        ASSEMBLER_RECIPES.recipeBuilder("barrel")
                .inputItems(ItemTags.PLANKS, 7)
                .outputItems(Blocks.BARREL)
                .circuitMeta(24)
                .duration(100).EUt(4)
                .save();
    }

    /**
     * + Adds cutting recipes for vanilla blocks
     */
    private static void cuttingRecipes() {
        CUTTER_RECIPES.recipeBuilder("snow_layer")
                .inputItems(Blocks.SNOW_BLOCK)
                .outputItems(Blocks.SNOW, 12)
                .duration(25).EUt(VA[ULV]).save();
    }

    /**
     * + Adds dying and cleaning recipes for vanilla blocks
     */
    private static void dyingCleaningRecipes() {
        for (DyeColor color : DyeColor.values()) {
            String dyeName = color.getName();
            MIXER_RECIPES.recipeBuilder(dyeName + "_concrete_powder").duration(200).EUt(VA[ULV])
                    .inputItems(Tags.Items.SAND, 4)
                    .inputItems(Tags.Items.GRAVEL, 4)
                    .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L))
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_concrete_powder")), 8)
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder(dyeName + "_concrete").duration(20).EUt(VA[ULV])
                    .inputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_concrete_powder")))
                    .inputFluids(Water.getFluid(1000))
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_concrete")))
                    .category(GTRecipeCategories.CHEM_DYES)
                    .save();

            if (color != DyeColor.WHITE) {
                CHEMICAL_BATH_RECIPES.recipeBuilder("dye_concrete_to_" + dyeName).duration(20).EUt(VA[ULV])
                        .inputItems(CustomTags.CONCRETE_ITEM)
                        .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L / 8))
                        .outputItems(new ItemStack(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_concrete"))))
                        .category(GTRecipeCategories.CHEM_DYES)
                        .save();
            }

            CHEMICAL_BATH_RECIPES.recipeBuilder("dye_terracotta_to_" + dyeName).duration(20).EUt(VA[ULV])
                    .inputItems(Blocks.TERRACOTTA)
                    .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L / 8))
                    .outputItems(new ItemStack(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_terracotta"))))
                    .category(GTRecipeCategories.CHEM_DYES)
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("dye_glass_to_" + dyeName).duration(20).EUt(VA[ULV])
                    .inputItems(Blocks.GLASS)
                    .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L / 8))
                    .outputItems(new ItemStack(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_stained_glass"))))
                    .category(GTRecipeCategories.CHEM_DYES)
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("dye_glass_pane_to_" + dyeName).duration(20).EUt(VA[ULV])
                    .inputItems(Blocks.GLASS_PANE)
                    .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L / 8))
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_stained_glass_pane")))
                    .category(GTRecipeCategories.CHEM_DYES)
                    .save();

            CUTTER_RECIPES.recipeBuilder("cut_" + dyeName + "_glass_to_pane").duration(20).EUt(VA[ULV])
                    .inputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_stained_glass")), 3)
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_stained_glass_pane")), 8)
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("dye_candle_to_" + dyeName).duration(20).EUt(VA[ULV])
                    .inputItems(Items.CANDLE)
                    .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L / 8))
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_candle")))
                    .category(GTRecipeCategories.CHEM_DYES)
                    .save();

            if (color != DyeColor.WHITE) {
                CHEMICAL_BATH_RECIPES.recipeBuilder("dye_wool_to_" + dyeName).duration(20).EUt(VA[ULV])
                        .inputItems(Blocks.WHITE_WOOL)
                        .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L))
                        .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_wool")))
                        .category(GTRecipeCategories.CHEM_DYES)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("dye_bed_to_" + dyeName).duration(20).EUt(VA[ULV])
                        .inputItems(Blocks.WHITE_BED)
                        .inputFluids(CHEMICAL_DYES[color.ordinal()].getFluid(L))
                        .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_bed")))
                        .category(GTRecipeCategories.CHEM_DYES)
                        .save();
            }

            CUTTER_RECIPES.recipeBuilder("cut_" + dyeName + "_wool_to_carpet").duration(20).EUt(VA[ULV])
                    .inputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_wool")))
                    .outputItems(new ItemStack(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_carpet")), 2))
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder(dyeName + "_banner").duration(20).EUt(VA[ULV])
                    .circuitMeta(6)
                    .inputItems(Items.STICK)
                    .inputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_wool")), 6)
                    .outputItems(RegistriesUtils.getItem(RLUtils.mc(dyeName + "_banner")))
                    .save();
        }

        // todo new tags to avoid white -> white recipe?
        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_wool")
                .inputItems(ItemTags.WOOL)
                .inputFluids(Chlorine.getFluid(50))
                .outputItems(Blocks.WHITE_WOOL)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_carpet")
                .inputItems(ItemTags.WOOL_CARPETS)
                .inputFluids(Chlorine.getFluid(25))
                .outputItems(Blocks.WHITE_CARPET)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_terracotta")
                .inputItems(ItemTags.TERRACOTTA)
                .inputFluids(Chlorine.getFluid(50))
                .outputItems(Items.TERRACOTTA)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_stained_glass")
                .inputItems(Tags.Items.STAINED_GLASS)
                .inputFluids(Chlorine.getFluid(50))
                .outputItems(Items.GLASS)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_stained_glass_pane")
                .inputItems(Tags.Items.STAINED_GLASS_PANES)
                .inputFluids(Chlorine.getFluid(20))
                .outputItems(Items.GLASS_PANE)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_concrete")
                .inputItems(CustomTags.CONCRETE_ITEM)
                .inputFluids(Chlorine.getFluid(20))
                .outputItems(Items.WHITE_CONCRETE)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("sticky_piston_to_piston")
                .inputItems(Blocks.STICKY_PISTON)
                .inputFluids(Chlorine.getFluid(10))
                .outputItems(Blocks.PISTON)
                .duration(30).EUt(VA[LV]).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_candle")
                .inputItems(ItemTags.CANDLES)
                .inputFluids(Chlorine.getFluid(20))
                .outputItems(Items.CANDLE)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("decolor_bed")
                .inputItems(ItemTags.BEDS)
                .inputFluids(Chlorine.getFluid(20))
                .outputItems(Items.WHITE_BED)
                .category(GTRecipeCategories.CHEM_DYES)
                .duration(400).EUt(2).save();
    }

    /**
     * + Adds more redstone related recipes
     */
    private static void redstoneRecipes() {
        ASSEMBLER_RECIPES.recipeBuilder("sticky_piston_resin")
                .inputItems(STICKY_RESIN)
                .inputItems(Blocks.PISTON)
                .outputItems(Blocks.STICKY_PISTON)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("sticky_piston_slime")
                .inputItems(Items.SLIME_BALL)
                .inputItems(Blocks.PISTON)
                .outputItems(Blocks.STICKY_PISTON)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("sticky_piston_glue")
                .inputItems(Blocks.PISTON)
                .inputFluids(Glue.getFluid(100))
                .outputItems(Blocks.STICKY_PISTON)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("tripwire_hook_iron")
                .inputItems(Items.STICK, 2)
                .inputItems(ring, Iron, 2)
                .outputItems(Blocks.TRIPWIRE_HOOK)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("tripwire_hook_wrought_iron")
                .inputItems(Items.STICK, 2)
                .inputItems(ring, WroughtIron, 2)
                .outputItems(Blocks.TRIPWIRE_HOOK)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("redstone_lamp")
                .inputItems(dust, Redstone, 4)
                .inputItems(dust, Glowstone, 4)
                .outputItems(Blocks.REDSTONE_LAMP)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("repeater")
                .inputItems(Blocks.REDSTONE_TORCH, 2)
                .inputItems(dust, Redstone)
                .inputFluids(Concrete.getFluid(L))
                .outputItems(Items.REPEATER)
                .duration(100).EUt(10).save();

        ASSEMBLER_RECIPES.recipeBuilder("comparator_nether_quartz")
                .inputItems(Blocks.REDSTONE_TORCH, 3)
                .inputItems(gem, NetherQuartz)
                .inputFluids(Concrete.getFluid(L))
                .outputItems(Items.COMPARATOR)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("comparator_certus_quartz")
                .inputItems(Blocks.REDSTONE_TORCH, 3)
                .inputItems(gem, CertusQuartz)
                .inputFluids(Concrete.getFluid(L))
                .outputItems(Items.COMPARATOR)
                .duration(100).EUt(1).save();

        ASSEMBLER_RECIPES.recipeBuilder("comparator_quartzite")
                .inputItems(Blocks.REDSTONE_TORCH, 3)
                .inputItems(gem, Quartzite)
                .inputFluids(Concrete.getFluid(L))
                .outputItems(Items.COMPARATOR)
                .duration(100).EUt(1).save();

        if (!ConfigHolder.INSTANCE.recipes.hardRedstoneRecipes) {
            CUTTER_RECIPES.recipeBuilder("stone_pressure_plate")
                    .inputItems(Blocks.STONE_SLAB)
                    .circuitMeta(26)
                    .outputItems(Blocks.STONE_PRESSURE_PLATE, 8)
                    .duration(250).EUt(VA[ULV]).save();

            CUTTER_RECIPES.recipeBuilder("polished_blackstone_pressure_plate")
                    .inputItems(Blocks.POLISHED_BLACKSTONE_SLAB)
                    .outputItems(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE, 8)
                    .duration(250).EUt(VA[ULV]).save();
        }
    }

    /**
     * + Adds metal related recipes
     * + Adds horse armor and chainmail recipes
     */
    private static void metalRecipes() {
        VanillaRecipeHelper.addShapedRecipe("leather_horse_armor", new ItemStack(Items.LEATHER_HORSE_ARMOR),
                "hdH",
                "PCP", "LSL",
                'H', new ItemStack(Items.LEATHER_HELMET),
                'P', new ItemStack(Items.LEATHER),
                'C', new ItemStack(Items.LEATHER_CHESTPLATE),
                'L', new ItemStack(Items.LEATHER_LEGGINGS),
                'S', new MaterialEntry(screw, Iron));

        VanillaRecipeHelper.addShapedRecipe(true, "iron_horse_armor", new ItemStack(Items.IRON_HORSE_ARMOR),
                "hdH",
                "PCP", "LSL",
                'H', new ItemStack(Items.IRON_HELMET),
                'P', new MaterialEntry(plate, Iron),
                'C', new ItemStack(Items.IRON_CHESTPLATE),
                'L', new ItemStack(Items.IRON_LEGGINGS),
                'S', new MaterialEntry(screw, Iron));

        VanillaRecipeHelper.addShapedRecipe(true, "golden_horse_armor",
                new ItemStack(Items.GOLDEN_HORSE_ARMOR),
                "hdH", "PCP", "LSL",
                'H', new ItemStack(Items.GOLDEN_HELMET),
                'P', new MaterialEntry(plate, Gold),
                'C', new ItemStack(Items.GOLDEN_CHESTPLATE),
                'L', new ItemStack(Items.GOLDEN_LEGGINGS),
                'S', new MaterialEntry(screw, Gold));

        VanillaRecipeHelper.addShapedRecipe(true, "diamond_horse_armor",
                new ItemStack(Items.DIAMOND_HORSE_ARMOR),
                "hdH", "PCP", "LSL",
                'H', new ItemStack(Items.DIAMOND_HELMET),
                'P', new MaterialEntry(plate, Diamond),
                'C', new ItemStack(Items.DIAMOND_CHESTPLATE),
                'L', new ItemStack(Items.DIAMOND_LEGGINGS),
                'S', new MaterialEntry(bolt, Diamond));

        VanillaRecipeHelper.addShapedRecipe(true, "chainmail_helmet", new ItemStack(Items.CHAINMAIL_HELMET),
                "PPP",
                "PhP",
                'P', Items.CHAIN);

        VanillaRecipeHelper.addShapedRecipe(true, "chainmail_chestplate",
                new ItemStack(Items.CHAINMAIL_CHESTPLATE),
                "PhP", "PPP", "PPP",
                'P', Items.CHAIN);

        VanillaRecipeHelper.addShapedRecipe(true, "chainmail_leggings",
                new ItemStack(Items.CHAINMAIL_LEGGINGS),
                "PPP", "PhP", "P P",
                'P', Items.CHAIN);

        VanillaRecipeHelper.addShapedRecipe(true, "chainmail_boots", new ItemStack(Items.CHAINMAIL_BOOTS),
                "P P",
                "PhP",
                'P', Items.CHAIN);

        ASSEMBLER_RECIPES.recipeBuilder("cauldron")
                .inputItems(plate, Iron, 7)
                .outputItems(Items.CAULDRON)
                .circuitMeta(7)
                .duration(700).EUt(4)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("iron_bars")
                .inputItems(rod, Iron, 3)
                .outputItems(Blocks.IRON_BARS, 4)
                .circuitMeta(3)
                .duration(300).EUt(4)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("iron_trapdoor")
                .inputItems(plate, Iron, 4)
                .circuitMeta(16)
                .outputItems(Blocks.IRON_TRAPDOOR)
                .duration(100).EUt(16)
                .save();

        if (!ConfigHolder.INSTANCE.recipes.hardAdvancedIronRecipes) {
            ASSEMBLER_RECIPES.recipeBuilder("iron_door")
                    .inputItems(TagPrefix.plate, GTMaterials.Iron, 6)
                    .circuitMeta(6)
                    .outputItems(Items.IRON_DOOR, 3)
                    .duration(100).EUt(16)
                    .save();
        }
    }

    /**
     * Adds miscellaneous vanilla recipes
     * Adds vanilla fluid solidification recipes
     * Adds anvil recipes
     * Adds Slime to rubber
     * Adds alternative gunpowder recipes
     * Adds polished stone variant autoclave recipes
     */
    private static void miscRecipes() {
        if (ConfigHolder.INSTANCE.recipes.hardToolArmorRecipes) {
            ASSEMBLER_RECIPES.recipeBuilder("fishing_rod")
                    .inputItems(Items.STRING)
                    .inputItems(rodLong, Wood, 2)
                    .inputItems(ring, Iron)
                    .outputItems(Items.FISHING_ROD)
                    .circuitMeta(16)
                    .duration(100).EUt(4).save();
        } else {
            ASSEMBLER_RECIPES.recipeBuilder("fishing_rod")
                    .inputItems(Items.STRING, 2)
                    .inputItems(rod, Wood, 3)
                    .outputItems(Items.FISHING_ROD)
                    .circuitMeta(16)
                    .duration(100).EUt(4).save();
        }

        ASSEMBLER_RECIPES.recipeBuilder("book_from_leather")
                .inputItems(Items.PAPER, 3)
                .inputItems(Items.LEATHER)
                .inputFluids(Glue.getFluid(20))
                .outputItems(Items.BOOK)
                .duration(32).EUt(VA[ULV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("book_from_pvc")
                .inputItems(Items.PAPER, 3)
                .inputItems(foil, PolyvinylChloride)
                .inputFluids(Glue.getFluid(20))
                .outputItems(Items.BOOK)
                .duration(20).EUt(16).save();

        ASSEMBLER_RECIPES.recipeBuilder("map")
                .inputItems(Items.PAPER, 8)
                .inputItems(Items.COMPASS)
                .outputItems(Items.MAP)
                .duration(100).EUt(VA[ULV]).save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("form_nether_brick")
                .inputItems(dust, Netherrack)
                .notConsumable(SHAPE_MOLD_INGOT)
                .outputItems(Items.NETHER_BRICK)
                .duration(200).EUt(2).save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("form_brick")
                .inputItems(Items.CLAY_BALL)
                .notConsumable(SHAPE_MOLD_INGOT)
                .outputItems(Items.BRICK)
                .duration(200).EUt(2).save();

        ASSEMBLER_RECIPES.recipeBuilder("lead")
                .inputItems(Items.STRING)
                .inputItems(Items.SLIME_BALL)
                .outputItems(Items.LEAD, 2)
                .duration(100).EUt(2).save();

        ASSEMBLER_RECIPES.recipeBuilder("name_tag")
                .inputItems(Items.LEATHER)
                .inputItems(Items.LEAD)
                .inputFluids(Glue.getFluid(100))
                .outputItems(Items.NAME_TAG)
                .duration(100).EUt(VA[ULV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("bow")
                .inputItems(Items.STRING, 3)
                .inputItems(Items.STICK, 3)
                .outputItems(Items.BOW)
                .circuitMeta(10)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("crossbow")
                .inputItems(Items.STRING, 2)
                .inputItems(Items.STICK, 3)
                .inputItems(Items.TRIPWIRE_HOOK)
                .outputItems(Items.CROSSBOW)
                .circuitMeta(11)
                .duration(100).EUt(4).save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("snowball").duration(128).EUt(4).notConsumable(SHAPE_MOLD_BALL)
                .inputFluids(Water.getFluid(250)).outputItems(Items.SNOWBALL).save();
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("snowball_distilled").duration(128).EUt(4)
                .notConsumable(SHAPE_MOLD_BALL).inputFluids(DistilledWater.getFluid(250))
                .outputItems(Items.SNOWBALL).save();
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("snow_block").duration(512).EUt(4).notConsumable(SHAPE_MOLD_BLOCK)
                .inputFluids(Water.getFluid(1000)).outputItems(Blocks.SNOW_BLOCK).save();
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("snow_block_distilled").duration(512).EUt(4)
                .notConsumable(SHAPE_MOLD_BLOCK).inputFluids(DistilledWater.getFluid(1000))
                .outputItems(Blocks.SNOW_BLOCK).save();
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("obsidian").duration(1024).EUt(16).notConsumable(SHAPE_MOLD_BLOCK)
                .inputFluids(Lava.getFluid(1000)).outputItems(Blocks.OBSIDIAN).save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_anvil").duration(1680).EUt(16)
                .notConsumable(SHAPE_MOLD_ANVIL).inputFluids(Iron.getFluid(L * 31))
                .outputItems(Blocks.ANVIL).save();
        ALLOY_SMELTER_RECIPES.recipeBuilder("anvil").inputItems(ingot, Iron, 31).notConsumable(SHAPE_MOLD_ANVIL)
                .outputItems(Blocks.ANVIL).duration(1680).EUt(16).save();

        VanillaRecipeHelper.addSmeltingRecipe("sticky_resin_from_slime", new ItemStack(Items.SLIME_BALL),
                STICKY_RESIN.asStack(), 0.3f);

        MIXER_RECIPES.recipeBuilder("mossy_cobblestone_from_vine")
                .inputItems(Blocks.COBBLESTONE)
                .inputItems(Blocks.VINE)
                .inputFluids(Water.getFluid(250))
                .outputItems(Blocks.MOSSY_COBBLESTONE)
                .duration(40).EUt(1).save();

        MIXER_RECIPES.recipeBuilder("mossy_cobblestone_from_moss_block")
                .inputItems(Blocks.COBBLESTONE)
                .inputItems(Blocks.MOSS_BLOCK)
                .inputFluids(Water.getFluid(250))
                .outputItems(Blocks.MOSSY_COBBLESTONE)
                .duration(40).EUt(1).save();

        MIXER_RECIPES.recipeBuilder("mossy_stone_bricks_from_vine")
                .inputItems(Blocks.STONE_BRICKS)
                .inputItems(Blocks.VINE)
                .inputFluids(Water.getFluid(250))
                .outputItems(Blocks.MOSSY_STONE_BRICKS)
                .duration(40).EUt(1).save();

        MIXER_RECIPES.recipeBuilder("mossy_stone_bricks_from_moss_block")
                .inputItems(Blocks.STONE_BRICKS)
                .inputItems(Blocks.MOSS_BLOCK)
                .inputFluids(Water.getFluid(250))
                .outputItems(Blocks.MOSSY_STONE_BRICKS)
                .duration(40).EUt(1).save();

        CANNER_RECIPES.recipeBuilder("jack_o_lantern").EUt(4).duration(100).inputItems(Blocks.PUMPKIN)
                .inputItems(Blocks.TORCH).outputItems(Blocks.JACK_O_LANTERN)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("sea_lantern").EUt(4).duration(40)
                .inputItems(Items.PRISMARINE_CRYSTALS, 5)
                .inputItems(Items.PRISMARINE_SHARD, 4).outputItems(Blocks.SEA_LANTERN)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("red_nether_bricks").EUt(4).duration(40)
                .inputItems(Items.NETHER_BRICK, 2).inputItems(Items.NETHER_WART, 2)
                .outputItems(Blocks.RED_NETHER_BRICKS).save();

        ASSEMBLER_RECIPES.recipeBuilder("nether_brick_fence").duration(100).EUt(4).circuitMeta(3)
                .inputItems(Blocks.NETHER_BRICKS).outputItems(Blocks.NETHER_BRICK_FENCE)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("end_rod").duration(100).EUt(4)
                .inputItems(Items.POPPED_CHORUS_FRUIT).inputItems(Items.BLAZE_ROD)
                .outputItems(Blocks.END_ROD, 4).save();

        ASSEMBLER_RECIPES.recipeBuilder("shulker_box").duration(100).EUt(VA[ULV])
                .inputItems(Tags.Items.CHESTS_WOODEN).inputItems(Items.SHULKER_SHELL, 2)
                .outputItems(Blocks.SHULKER_BOX).save();

        ASSEMBLER_RECIPES.recipeBuilder("painting").duration(100).EUt(4).circuitMeta(1).inputItems(ItemTags.WOOL)
                .inputItems(Items.STICK, 8).outputItems(Items.PAINTING)
                .save();
        ASSEMBLER_RECIPES.recipeBuilder("item_frame").duration(100).EUt(4).inputItems(Items.LEATHER)
                .inputItems(Items.STICK, 8).outputItems(Items.ITEM_FRAME)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("flower_pot").duration(10).EUt(2).inputItems(Items.BRICK, 3)
                .outputItems(Items.FLOWER_POT)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("end_crystal").duration(30).EUt(16).inputItems(Items.GHAST_TEAR)
                .inputItems(Items.ENDER_EYE)
                .inputFluids(Glass.getFluid(L * 7))
                .outputItems(Items.END_CRYSTAL)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("rail")
                .inputItems(rod, Iron, 12)
                .inputItems(Items.STICK)
                .circuitMeta(1)
                .outputItems(Blocks.RAIL, 32)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("powered_rail")
                .inputItems(rod, Gold, 12)
                .inputItems(Items.STICK)
                .inputItems(dust, Redstone)
                .circuitMeta(1)
                .outputItems(Blocks.POWERED_RAIL, 12)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("detector_rail")
                .inputItems(rod, Iron, 12)
                .inputItems(Items.STICK)
                .inputItems(dust, Redstone)
                .circuitMeta(5)
                .outputItems(Blocks.DETECTOR_RAIL, 12)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("activator_rail")
                .inputItems(rod, Iron, 12)
                .inputItems(Items.STICK, 2)
                .inputItems(Blocks.REDSTONE_TORCH)
                .circuitMeta(5)
                .outputItems(Blocks.ACTIVATOR_RAIL, 12)
                .duration(100).EUt(VA[LV]).save();

        VanillaRecipeHelper.addShapedRecipe("saddle", new ItemStack(Items.SADDLE), "LLL", "LCL", "RSR",
                'L', new ItemStack(Items.LEATHER),
                'C', ItemTags.WOOL_CARPETS,
                'R', new MaterialEntry(ring, Iron),
                'S', new ItemStack(Items.STRING));

        AUTOCLAVE_RECIPES.recipeBuilder("clay_from_dust")
                .inputItems(dust, Clay)
                .inputFluids(Water.getFluid(250))
                .outputItems(Items.CLAY_BALL)
                .duration(600).EUt(24).save();

        AUTOCLAVE_RECIPES.recipeBuilder("clay_from_dust_distilled")
                .inputItems(dust, Clay)
                .inputFluids(DistilledWater.getFluid(250))
                .outputItems(Items.CLAY_BALL)
                .duration(300).EUt(24).save();

        COMPRESSOR_RECIPES.recipeBuilder("redstone_block")
                .inputItems(dust, Redstone, 9)
                .outputItems(Blocks.REDSTONE_BLOCK)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("bone_block")
                .inputItems(dust, Bone, 9)
                .outputItems(Blocks.BONE_BLOCK)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("purpur_block")
                .inputItems(Items.POPPED_CHORUS_FRUIT, 4)
                .outputItems(Blocks.PURPUR_BLOCK, 4)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("magma_block")
                .inputItems(Items.MAGMA_CREAM, 4)
                .outputItems(Blocks.MAGMA_BLOCK)
                .duration(300).EUt(2).save();

        COMPRESSOR_RECIPES.recipeBuilder("slime_block")
                .inputItems(Items.SLIME_BALL, 9)
                .outputItems(Blocks.SLIME_BLOCK)
                .duration(300).EUt(2).save();

        PACKER_RECIPES.recipeBuilder("nether_wart_block")
                .inputItems(Items.NETHER_WART, 9)
                .circuitMeta(9)
                .outputItems(Blocks.NETHER_WART_BLOCK)
                .duration(200).EUt(2).save();

        PACKER_RECIPES.recipeBuilder("prismarine")
                .inputItems(Items.PRISMARINE_SHARD, 4)
                .circuitMeta(4)
                .outputItems(Blocks.PRISMARINE)
                .duration(100).EUt(2).save();

        PACKER_RECIPES.recipeBuilder("prismarine_bricks")
                .inputItems(Items.PRISMARINE_SHARD, 9)
                .circuitMeta(9)
                .outputItems(Blocks.PRISMARINE_BRICKS)
                .duration(200).EUt(2).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("eye_of_ender")
                .inputFluids(Blaze.getFluid(L))
                .inputItems(gem, EnderPearl)
                .outputItems(Items.ENDER_EYE)
                .duration(50).EUt(VA[HV]).save();

        COMPRESSOR_RECIPES.recipeBuilder("blaze_rod")
                .inputItems(dust, Blaze, 4)
                .outputItems(Items.BLAZE_ROD)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("mud_to_clay")
                .inputItems(Items.MUD)
                .outputItems(Items.CLAY)
                .duration(40).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("ender_chest").duration(100).EUt(4)
                .inputItems(Blocks.OBSIDIAN, 8).inputItems(Items.ENDER_EYE)
                .outputItems(Blocks.ENDER_CHEST).save();
        ASSEMBLER_RECIPES.recipeBuilder("armor_stand").duration(30).EUt(VA[ULV])
                .inputItems(Blocks.SMOOTH_STONE_SLAB).inputItems(Items.STICK, 6)
                .outputItems(Items.ARMOR_STAND).save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("candle")
                .inputFluids(Wax.getFluid(L))
                .inputItems(Items.STRING)
                .outputItems(Blocks.CANDLE, 2)
                .duration(20).EUt(1).save();

        FORGE_HAMMER_RECIPES.recipeBuilder("disc_fragment_5")
                .inputItems(Items.MUSIC_DISC_5)
                .outputItems(Items.DISC_FRAGMENT_5, 9)
                .duration(100).EUt(6).save();

        ASSEMBLER_RECIPES.recipeBuilder("chest_minecart").EUt(4).duration(100).inputItems(Items.MINECART)
                .inputItems(Tags.Items.CHESTS_WOODEN).outputItems(Items.CHEST_MINECART).save();
        ASSEMBLER_RECIPES.recipeBuilder("furnace_minecart").EUt(4).duration(100)
                .inputItems(Items.MINECART).inputItems(Blocks.FURNACE)
                .outputItems(Items.FURNACE_MINECART).save();
        ASSEMBLER_RECIPES.recipeBuilder("tnt_minecart").EUt(4).duration(100).inputItems(Items.MINECART)
                .inputItems(Blocks.TNT).outputItems(Items.TNT_MINECART).save();
        ASSEMBLER_RECIPES.recipeBuilder("hopper_minecart").EUt(4).duration(100)
                .inputItems(Items.MINECART).inputItems(Blocks.HOPPER)
                .outputItems(Items.HOPPER_MINECART).save();

        VanillaRecipeHelper.addShapelessRecipe("hay_block_to_hay", new ItemStack(Items.WHEAT, 9),
                Items.HAY_BLOCK, 'k');

        ASSEMBLER_RECIPES.recipeBuilder("conduit")
                .inputItems(Items.HEART_OF_THE_SEA)
                .inputItems(Items.NAUTILUS_SHELL, 8)
                .outputItems(Blocks.CONDUIT)
                .duration(200).EUt(16).save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("granite")
                .inputItems(Items.DIORITE)
                .inputItems(Items.QUARTZ)
                .outputItems(Items.GRANITE)
                .duration(80).EUt(4).save();
        ALLOY_SMELTER_RECIPES.recipeBuilder("diorite")
                .inputItems(Items.COBBLESTONE, 2)
                .inputItems(Items.QUARTZ, 2)
                .outputItems(Items.DIORITE, 2)
                .duration(80).EUt(4).save();
        ALLOY_SMELTER_RECIPES.recipeBuilder("andesite")
                .inputItems(Items.DIORITE)
                .inputItems(Items.COBBLESTONE)
                .outputItems(Items.ANDESITE, 2)
                .duration(80).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("assemble_block_of_quartz_into_quartz_pillar")
                .inputItems(Items.QUARTZ_BLOCK)
                .circuitMeta(5)
                .outputItems(Items.QUARTZ_PILLAR)
                .duration(80).EUt(1).save();

        MIXER_RECIPES.recipeBuilder("packed_mud")
                .inputItems(Items.MUD)
                .inputItems(Items.WHEAT)
                .outputItems(Items.PACKED_MUD)
                .duration(80).EUt(4).save();
    }

    /**
     * Adds various mixer recipes for vanilla items and blocks
     */
    private static void mixingRecipes() {
        MIXER_RECIPES.recipeBuilder("fire_charge")
                .inputItems(dust, Coal)
                .inputItems(dust, Gunpowder)
                .inputItems(dust, Blaze)
                .outputItems(Items.FIRE_CHARGE, 3)
                .duration(100).EUt(VA[LV]).save();

        MIXER_RECIPES.recipeBuilder("coarse_dirt")
                .inputItems(Blocks.GRAVEL)
                .inputItems(Blocks.DIRT)
                .outputItems(Blocks.COARSE_DIRT, 2)
                .duration(100).EUt(4).save();

        MIXER_RECIPES.recipeBuilder("mud")
                .inputItems(Blocks.DIRT)
                .circuitMeta(1)
                .inputFluids(Water.getFluid(L))
                .outputItems(Blocks.MUD)
                .duration(100).EUt(4).save();
    }

    private static void dyeRecipes() {
        EXTRACTOR_RECIPES.recipeBuilder("poppy_dye")
                .inputItems(Blocks.POPPY)
                .outputItems(Items.RED_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("blue_orchid_dye")
                .inputItems(Blocks.BLUE_ORCHID)
                .outputItems(Items.LIGHT_BLUE_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("allium_dye")
                .inputItems(Blocks.ALLIUM)
                .outputItems(Items.MAGENTA_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("azure_bluet_dye")
                .inputItems(Blocks.AZURE_BLUET)
                .outputItems(Items.LIGHT_GRAY_DYE)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("red_tulip_dye")
                .inputItems(Blocks.RED_TULIP)
                .outputItems(Items.RED_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("orange_tulip_dye")
                .inputItems(Blocks.ORANGE_TULIP)
                .outputItems(Items.ORANGE_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("white_tulip_dye")
                .inputItems(Blocks.WHITE_TULIP)
                .outputItems(Items.LIGHT_GRAY_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("pink_tulip_dye")
                .inputItems(Blocks.PINK_TULIP)
                .outputItems(Items.PINK_DYE)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("oxeye_daisy_dye")
                .inputItems(Blocks.OXEYE_DAISY)
                .outputItems(Items.LIGHT_GRAY_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("dandelion_dye")
                .inputItems(Blocks.DANDELION)
                .outputItems(Items.YELLOW_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("cornflower_dye")
                .inputItems(Blocks.CORNFLOWER)
                .outputItems(Items.BLUE_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("lily_of_the_valley_dye")
                .inputItems(Blocks.LILY_OF_THE_VALLEY)
                .outputItems(Items.WHITE_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("wither_rose_dye")
                .inputItems(Blocks.WITHER_ROSE)
                .outputItems(Items.BLACK_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("sunflower_dye")
                .inputItems(Blocks.SUNFLOWER)
                .outputItems(Items.YELLOW_DYE, 3)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("lilac_dye")
                .inputItems(Blocks.LILAC)
                .outputItems(Items.MAGENTA_DYE, 3)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("rose_bush_dye")
                .inputItems(Blocks.ROSE_BUSH)
                .outputItems(Items.RED_DYE, 3)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("peony_dye")
                .inputItems(Blocks.PEONY)
                .outputItems(Items.PINK_DYE, 3)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("beetroot_dye")
                .inputItems(Items.BEETROOT)
                .outputItems(Items.RED_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("torchflower_dye")
                .inputItems(Items.TORCHFLOWER)
                .outputItems(Items.ORANGE_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("pitcher_dye")
                .inputItems(Items.PITCHER_PLANT)
                .outputItems(Items.CYAN_DYE, 3)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("pink_petals_dye")
                .inputItems(Items.PINK_PETALS)
                .outputItems(Items.PINK_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("sea_pickle_dye")
                .inputItems(Items.SEA_PICKLE)
                .outputItems(Items.LIME_DYE, 2)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("white_dye")
                .inputItems(Items.BONE_MEAL)
                .outputItems(Items.WHITE_DYE)
                .save();
        EXTRACTOR_RECIPES.recipeBuilder("lapis_dye")
                .inputItems(Items.LAPIS_LAZULI)
                .outputItems(Items.BLUE_DYE)
                .save();
        EXTRACTOR_RECIPES.recipeBuilder("ink_dye")
                .inputItems(Items.INK_SAC)
                .outputItems(Items.BLACK_DYE)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("dark_prismarine")
                .inputItems(Items.PRISMARINE_SHARD, 4)
                .inputFluids(DyeBlack.getFluid(L))
                .outputItems(Blocks.DARK_PRISMARINE)
                .duration(20).EUt(VA[ULV]).save();
    }
}
