package com.gtocore.data.recipe.classified;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.StringUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gtocore.common.data.GTORecipeTypes.ELECTRIC_COOKING_RECIPES;

final class ElectricCooking {

    public static void init() {
        ELECTRIC_COOKING_RECIPES.recipeBuilder("baked_cod_stew")
                .inputItems(TagUtils.createForgeItemTag("raw_fishes/cod"))
                .inputItems(Items.POTATO.asItem())
                .inputItems(Items.EGG.asItem())
                .inputItems("farmersdelight:tomato")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:baked_cod_stew")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("squid_ink_pasta")
                .inputItems(TagUtils.createForgeItemTag("raw_fishes"))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato")
                .inputItems(Items.INK_SAC.asItem())
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:squid_ink_pasta")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("blazing_chili")
                .inputItems(Items.BLAZE_POWDER.asItem(), 2)
                .inputItems(Items.NETHER_WART.asItem(), 2)
                .inputItems("farmersrespite:coffee_beans")
                .inputItems(TagUtils.createForgeItemTag("raw_beef"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersrespite:blazing_chili")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("rabbit_stew")
                .inputItems(Items.BAKED_POTATO.asItem())
                .inputItems(Items.RABBIT.asItem())
                .inputItems(Items.CARROT.asItem())
                .inputItems(Items.BOWL.asItem())
                .inputItems(TagUtils.createForgeItemTag("mushrooms"))
                .outputItems(Items.RABBIT_STEW.asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("bone_broth")
                .inputItems(Items.BONE.asItem())
                .inputItems(TagUtils.createForgeItemTag("mushrooms"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:bone_broth")
                .inputFluids(GTMaterials.Water, 1000)

                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("tea_curry")
                .inputItems("farmersrespite:yellow_tea_leaves", 2)
                .inputItems(TagUtils.createForgeItemTag("raw_chicken"))
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems("farmersdelight:onion")
                .inputItems("farmersdelight:rice")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersrespite:tea_curry")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("cabbage_rolls")
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems(TagUtils.createItemTag(RLUtils.fd("cabbage_roll_ingredients")))
                .outputItems("farmersdelight:cabbage_rolls")
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("beef_stew")
                .inputItems(TagUtils.createForgeItemTag("raw_beef"))
                .inputItems(Items.CARROT.asItem())
                .inputItems(Items.POTATO.asItem())
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:beef_stew")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("cooked_rice")
                .inputItems("farmersdelight:rice")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:cooked_rice")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("mushroom_rice")
                .inputItems(Items.BROWN_MUSHROOM.asItem())
                .inputItems(Items.RED_MUSHROOM.asItem())
                .inputItems("farmersdelight:rice")
                .inputItems(Items.CARROT.asItem())
                .inputItems(Items.POTATO.asItem())
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:mushroom_rice")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("vegetable_noodles")
                .inputItems(Items.CARROT.asItem())
                .inputItems(Items.BROWN_MUSHROOM.asItem())
                .inputItems("farmersdelight:raw_pasta")
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems(TagUtils.createForgeItemTag("vegetables"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:vegetable_noodles")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("dumplings")
                .inputItems(GTItems.DOUGH)
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems("farmersdelight:onion")
                .inputItems(TagUtils.createForgeItemTag("raw_pork"))
                .outputItems("farmersdelight:dumplings", 2)
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("fried_rice")
                .inputItems("farmersdelight:rice")
                .inputItems(Items.EGG.asItem())
                .inputItems(Items.CARROT.asItem())
                .inputItems("farmersdelight:onion")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:fried_rice")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("vegetable_soup")
                .inputItems(Items.CARROT.asItem())
                .inputItems(Items.POTATO.asItem())
                .inputItems(Items.BEETROOT.asItem())
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:vegetable_soup")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("mushroom_stew")
                .inputItems(Items.BROWN_MUSHROOM.asItem())
                .inputItems(Items.RED_MUSHROOM.asItem())
                .inputItems(Items.BOWL.asItem())
                .outputItems(Items.MUSHROOM_STEW.asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("stuffed_pumpkin_block")
                .inputItems("farmersdelight:rice")
                .inputItems("farmersdelight:onion")
                .inputItems(Items.BROWN_MUSHROOM.asItem())
                .inputItems(Items.POTATO.asItem())
                .inputItems(TagUtils.createItemTag(RLUtils.mc("fox_food")))
                .inputItems(TagUtils.createForgeItemTag("vegetables"))
                .inputItems(Items.PUMPKIN.asItem())
                .outputItems("farmersdelight:stuffed_pumpkin_block")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("chicken_soup")
                .inputItems(TagUtils.createForgeItemTag("raw_chicken"))
                .inputItems(Items.CARROT.asItem())
                .inputItems(TagUtils.createForgeItemTag("vegetables"))
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:chicken_soup")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("tomato_sauce")
                .inputItems("farmersdelight:tomato", 2)
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:tomato_sauce")
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("pasta_with_meatballs")
                .inputItems("farmersdelight:minced_beef")
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:pasta_with_meatballs")
                .outputItems(Items.BOWL.asItem())
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("glow_berry_custard")
                .inputItems(Items.GLOW_BERRIES.asItem())
                .inputItems(Items.EGG.asItem())
                .inputItems(Items.SUGAR.asItem())
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems("farmersdelight:glow_berry_custard")
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("dog_food")
                .inputItems(Items.ROTTEN_FLESH.asItem())
                .inputItems(Items.BONE.asItem())
                .inputItems(TagUtils.createItemTag(RLUtils.fd("wolf_prey")))
                .inputItems("farmersdelight:rice")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:dog_food")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("beetroot_soup")
                .inputItems(Items.BEETROOT.asItem(), 3)
                .inputItems(Items.BOWL.asItem())
                .outputItems(Items.BEETROOT_SOUP.asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("ratatouille")
                .inputItems("farmersdelight:tomato")
                .inputItems("farmersdelight:onion")
                .inputItems(Items.BEETROOT.asItem())
                .inputItems(TagUtils.createForgeItemTag("vegetables"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:ratatouille")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("pasta_with_mutton_chop")
                .inputItems(TagUtils.createForgeItemTag("raw_mutton"))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:pasta_with_mutton_chop")
                .outputItems(Items.BOWL.asItem())
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("pumpkin_soup")
                .inputItems("farmersdelight:pumpkin_slice")
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems(TagUtils.createForgeItemTag("raw_pork"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:pumpkin_soup")
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("noodle_soup")
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:fried_egg")
                .inputItems(Items.DRIED_KELP.asItem())
                .inputItems(TagUtils.createForgeItemTag("raw_pork"))
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:noodle_soup")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("fish_stew")
                .inputItems(TagUtils.createForgeItemTag("raw_fishes"))
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems("farmersdelight:onion")
                .inputItems(Items.BOWL.asItem())
                .outputItems("farmersdelight:fish_stew")
                .outputItems(Items.BOWL.asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        addSmoking("farmersdelight:chicken_cuts", "farmersdelight:cooked_chicken_cuts");
        addSmoking("minecraft:beef", "minecraft:cooked_beef");
        addSmoking("minecraft:egg", "farmersdelight:fried_egg");
        addSmoking("farmersdelight:bacon", "farmersdelight:cooked_bacon");
        addSmoking("farmersdelight:salmon_slice", "farmersdelight:cooked_salmon_slice");
        addSmoking("minecraft:cod", "minecraft:cooked_cod");
        addSmoking("farmersdelight:minced_beef", "farmersdelight:beef_patty");
        addSmoking("gtceu:dough", "minecraft:bread");
        addSmoking("minecraft:chicken", "minecraft:cooked_chicken");
        addSmoking("minecraft:salmon", "minecraft:cooked_salmon");
        addSmoking("farmersdelight:mutton_chops", "farmersdelight:cooked_mutton_chops");
        addSmoking("minecraft:mutton", "minecraft:cooked_mutton");
        addSmoking("minecraft:kelp", "minecraft:dried_kelp");
        addSmoking("farmersdelight:ham", "farmersdelight:smoked_ham");
        addSmoking("minecraft:potato", "minecraft:baked_potato");
        addSmoking("minecraft:rabbit", "minecraft:cooked_rabbit");
        addSmoking("farmersdelight:cod_slice", "farmersdelight:cooked_cod_slice");
        addSmoking("minecraft:porkchop", "minecraft:cooked_porkchop");

        addDrinking("farmersrespite:green_tea_leaves", "farmersrespite:green_tea_leaves", "farmersrespite:green_tea");
        addDrinking("farmersrespite:yellow_tea_leaves", "farmersrespite:yellow_tea_leaves", "farmersrespite:yellow_tea");
        addDrinking("farmersrespite:black_tea_leaves", "farmersrespite:black_tea_leaves", "farmersrespite:black_tea");
        addDrinking("farmersrespite:rose_hips", "farmersrespite:rose_hips", "farmersrespite:rose_hip_tea");
        addDrinking("minecraft:nether_wart", "minecraft:fermented_spider_eye", "farmersrespite:purulent_tea");
        addDrinking("farmersrespite:coffee_berries", "minecraft:glow_berries", "farmersrespite:gamblers_tea");
        addDrinking("farmersrespite:coffee_beans", "farmersrespite:coffee_beans", "farmersrespite:coffee");
        addDrinking("minecraft:apple", "minecraft:sugar", "farmersdelight:apple_cider");
        addDrinking("minecraft:melon_slice", "minecraft:sugar", "farmersdelight:melon_juice");

        addDrinkingLong("farmersrespite:green_tea_leaves", "farmersrespite:green_tea_leaves", "farmersrespite:long_green_tea");
        addDrinkingLong("farmersrespite:yellow_tea_leaves", "farmersrespite:yellow_tea_leaves", "farmersrespite:long_yellow_tea");
        addDrinkingLong("farmersrespite:black_tea_leaves", "farmersrespite:black_tea_leaves", "farmersrespite:long_black_tea");
        addDrinkingLong("farmersrespite:rose_hips", "farmersrespite:rose_hips", "farmersrespite:long_rose_hip_tea");
        addDrinkingLong("minecraft:nether_wart", "minecraft:fermented_spider_eye", "farmersrespite:long_purulent_tea");
        addDrinkingLong("farmersrespite:coffee_berries", "minecraft:glow_berries", "farmersrespite:long_gamblers_tea");
        addDrinkingLong("farmersrespite:coffee_beans", "farmersrespite:coffee_beans", "farmersrespite:long_coffee");
        addDrinkingLong("minecraft:apple", "minecraft:sugar", "farmersrespite:long_apple_cider");

        addDrinkingStrong("farmersrespite:green_tea_leaves", "farmersrespite:green_tea_leaves", "farmersrespite:strong_green_tea");
        addDrinkingStrong("farmersrespite:yellow_tea_leaves", "farmersrespite:yellow_tea_leaves", "farmersrespite:strong_yellow_tea");
        addDrinkingStrong("farmersrespite:black_tea_leaves", "farmersrespite:black_tea_leaves", "farmersrespite:strong_black_tea");
        addDrinkingStrong("farmersrespite:rose_hips", "farmersrespite:rose_hips", "farmersrespite:strong_rose_hip_tea");
        addDrinkingStrong("minecraft:nether_wart", "minecraft:fermented_spider_eye", "farmersrespite:strong_purulent_tea");
        addDrinkingStrong("farmersrespite:coffee_berries", "minecraft:glow_berries", "farmersrespite:strong_gamblers_tea");
        addDrinkingStrong("farmersrespite:coffee_beans", "farmersrespite:coffee_beans", "farmersrespite:strong_coffee");
        addDrinkingStrong("minecraft:apple", "minecraft:sugar", "farmersrespite:strong_apple_cider");
        addDrinkingStrong("minecraft:melon_slice", "minecraft:sugar", "farmersrespite:strong_melon_juice");

        ELECTRIC_COOKING_RECIPES.recipeBuilder("dandelion_tea")
                .inputItems(Items.DANDELION.asItem())
                .inputItems(TagUtils.createItemTag(RLUtils.fr("tea_leaves")))
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems("farmersrespite:dandelion_tea")
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(4)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("long_dandelion_tea")
                .inputItems(Items.DANDELION.asItem())
                .inputItems(TagUtils.createItemTag(RLUtils.fr("tea_leaves")))
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems("farmersrespite:long_dandelion_tea")
                .inputFluids(GTMaterials.Water, 1000)
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(5)
                .EUt(120)
                .duration(300)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("hot_cocoa")
                .inputItems(Items.COCOA_BEANS.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Sugar)
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems("farmersdelight:hot_cocoa")
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(4)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder("strong_hot_cocoa")
                .inputItems(Items.COCOA_BEANS.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Sugar)
                .inputItems(Items.HONEY_BOTTLE.asItem())
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems("farmersrespite:strong_hot_cocoa")
                .outputItems(Items.GLASS_BOTTLE.asItem())
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(6)
                .EUt(120)
                .duration(300)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("cake")
                .inputItems(GTItems.DOUGH)
                .inputItems(TagPrefix.dust, GTMaterials.Sugar, 2)
                .inputItems(Items.EGG)
                .outputItems(Items.CAKE)
                .inputFluids(GTMaterials.Milk, 750)
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("steak_and_potatoes")
                .inputItems(Items.BAKED_POTATO)
                .inputItems(Items.COOKED_BEEF)
                .inputItems(Items.BOWL)
                .inputItems("farmersdelight:onion")
                .inputItems("farmersdelight:cooked_rice")
                .outputItems("farmersdelight:steak_and_potatoes")
                .outputItems(Items.BOWL)
                .EUt(120)
                .duration(200)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("roasted_mutton_chops")
                .inputItems("farmersdelight:cooked_mutton_chops")
                .inputItems(Items.BEETROOT)
                .inputItems(Items.BOWL)
                .inputItems("farmersdelight:cooked_rice")
                .inputItems("farmersdelight:tomato")
                .outputItems("farmersdelight:roasted_mutton_chops")
                .outputItems(Items.BOWL)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("salmon_roll")
                .inputItems("farmersdelight:salmon_slice", 2)
                .inputItems("farmersdelight:cooked_rice")
                .outputItems("farmersdelight:salmon_roll", 2)
                .outputItems(Items.BOWL)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("cod_roll")
                .inputItems("farmersdelight:cod_slice", 2)
                .inputItems("farmersdelight:cooked_rice")
                .outputItems("farmersdelight:cod_roll", 2)
                .outputItems(Items.BOWL)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("black_cod")
                .inputItems(TagUtils.createForgeItemTag("cooked_fishes/cod"))
                .inputItems("farmersrespite:black_tea_leaves")
                .inputItems(Items.BOWL)
                .inputItems(TagUtils.createForgeItemTag("crops/cabbage"))
                .inputItems("farmersdelight:cooked_rice")
                .outputItems("farmersrespite:black_cod")
                .outputItems(Items.BOWL)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("honey_glazed_ham_block")
                .inputItems(Items.SWEET_BERRIES, 4)
                .inputItems(Items.HONEY_BOTTLE)
                .inputItems("farmersdelight:smoked_ham")
                .inputItems("farmersdelight:cooked_rice", 2)
                .inputItems(Items.BOWL)
                .outputItems("farmersdelight:honey_glazed_ham_block")
                .outputItems(Items.BOWL, 2)
                .outputItems(Items.GLASS_BOTTLE)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("kelp_roll")
                .inputItems("farmersdelight:cooked_rice", 2)
                .inputItems(Items.CARROT)
                .inputItems(Items.DRIED_KELP, 3)
                .outputItems("farmersdelight:kelp_roll")
                .outputItems(Items.BOWL, 2)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("stuffed_potato")
                .inputItems(Items.BAKED_POTATO)
                .inputItems(Items.COOKED_BEEF)
                .outputItems("farmersdelight:stuffed_potato")
                .inputFluids(GTMaterials.Milk, 1000)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("shepherds_pie_block")
                .inputItems(Items.BAKED_POTATO, 2)
                .inputItems(TagUtils.createForgeItemTag("cooked_mutton"), 3)
                .inputItems("farmersdelight:onion", 2)
                .inputItems(Items.BOWL)
                .outputItems("farmersdelight:shepherds_pie_block")
                .inputFluids(GTMaterials.Milk, 1000)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("honey_cookie")
                .inputItems(Items.HONEY_BOTTLE)
                .inputItems(Items.WHEAT, 2)
                .outputItems("farmersdelight:honey_cookie")
                .outputItems(Items.GLASS_BOTTLE)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("chocolate_pie")
                .inputItems(Items.COCOA_BEANS, 3)
                .inputItems(TagPrefix.dust, GTMaterials.Sugar, 2)
                .inputItems("farmersdelight:pie_crust")
                .outputItems("farmersdelight:chocolate_pie")
                .inputFluids(GTMaterials.Milk, 3000)
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("pie_crust")
                .circuitMeta(3)
                .inputItems(Items.WHEAT, 3)
                .inputFluids(GTMaterials.Milk, 1000)
                .outputItems("farmersdelight:pie_crust")
                .EUt(120)
                .duration(120)
                .save();
        ELECTRIC_COOKING_RECIPES.builder("coffee_cake")
                .inputItems(TagPrefix.dust, GTMaterials.Sugar)
                .inputItems("farmersrespite:coffee_beans", 2)
                .inputItems(Items.EGG)
                .inputItems(Items.WHEAT, 3)
                .outputItems("farmersrespite:coffee_cake")
                .inputFluids(GTMaterials.Milk, 2000)
                .EUt(120)
                .duration(120)
                .save();
    }

    private static void addSmoking(String input, String output) {
        RecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(StringUtils.decompose(output)[1])
                .inputItems(input)
                .outputItems(output)
                .circuitMeta(3)
                .EUt(120)
                .duration(200);
        builder.save();
    }

    private static void addDrinking(String input1, String input2, String output) {
        RecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(StringUtils.decompose(output)[1])
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems(output)
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(4)
                .EUt(120)
                .duration(200);
        builder.save();
    }

    private static void addDrinkingLong(String input1, String input2, String output) {
        RecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(StringUtils.decompose(output)[1])
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems(output)
                .inputFluids(GTMaterials.Water, 1000)
                .inputFluids(GTMaterials.Milk, 1000)
                .circuitMeta(5)
                .EUt(120)
                .duration(300);
        builder.save();
    }

    private static void addDrinkingStrong(String input1, String input2, String output) {
        RecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(StringUtils.decompose(output)[1])
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(Items.HONEY_BOTTLE.asItem())
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .outputItems(output)
                .outputItems(Items.GLASS_BOTTLE.asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .circuitMeta(6)
                .EUt(120)
                .duration(300);
        builder.save();
    }
}
