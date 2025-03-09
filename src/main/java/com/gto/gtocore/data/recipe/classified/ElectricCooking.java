package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.utils.RLUtils;
import com.gto.gtocore.utils.StringUtils;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.ELECTRIC_COOKING_RECIPES;

interface ElectricCooking {

    static void init() {
        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("baked_cod_stew"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_fishes/cod")))
                .inputItems(new ItemStack(Items.POTATO.asItem()))
                .inputItems(new ItemStack(Items.EGG.asItem()))
                .inputItems("farmersdelight:tomato")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:baked_cod_stew")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("squid_ink_pasta"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_fishes")))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato")
                .inputItems(new ItemStack(Items.INK_SAC.asItem()))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:squid_ink_pasta")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("blazing_chili"))
                .inputItems(new ItemStack(Items.BLAZE_POWDER.asItem(), 2))
                .inputItems(new ItemStack(Items.NETHER_WART.asItem(), 2))
                .inputItems("farmersrespite:coffee_beans")
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_beef")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersrespite:blazing_chili")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("rabbit_stew"))
                .inputItems(new ItemStack(Items.BAKED_POTATO.asItem()))
                .inputItems(new ItemStack(Items.RABBIT.asItem()))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("mushrooms")))
                .outputItems(new ItemStack(Items.RABBIT_STEW.asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("bone_broth"))
                .inputItems(new ItemStack(Items.BONE.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("mushrooms")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:bone_broth")
                .inputFluids(GTMaterials.Water.getFluid(1000))

                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("tea_curry"))
                .inputItems("farmersrespite:yellow_tea_leaves", 2)
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_chicken")))
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems("farmersdelight:onion")
                .inputItems("farmersdelight:rice")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersrespite:tea_curry")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("cabbage_rolls"))
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems(TagUtils.createTag(RLUtils.fd("cabbage_roll_ingredients")))
                .outputItems("farmersdelight:cabbage_rolls")
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("beef_stew"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_beef")))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(new ItemStack(Items.POTATO.asItem()))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:beef_stew")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("cooked_rice"))
                .inputItems("farmersdelight:rice")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:cooked_rice")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("mushroom_rice"))
                .inputItems(new ItemStack(Items.BROWN_MUSHROOM.asItem()))
                .inputItems(new ItemStack(Items.RED_MUSHROOM.asItem()))
                .inputItems("farmersdelight:rice")
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(new ItemStack(Items.POTATO.asItem()))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:mushroom_rice")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("vegetable_noodles"))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(new ItemStack(Items.BROWN_MUSHROOM.asItem()))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems(TagUtils.createTag(RLUtils.forge("vegetables")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:vegetable_noodles")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("dumplings"))
                .inputItems(GTItems.DOUGH.asStack())
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems("farmersdelight:onion")
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_pork")))
                .outputItems("farmersdelight:dumplings", 2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("fried_rice"))
                .inputItems("farmersdelight:rice")
                .inputItems(new ItemStack(Items.EGG.asItem()))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems("farmersdelight:onion")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:fried_rice")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("vegetable_soup"))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(new ItemStack(Items.POTATO.asItem()))
                .inputItems(new ItemStack(Items.BEETROOT.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:vegetable_soup")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("mushroom_stew"))
                .inputItems(new ItemStack(Items.BROWN_MUSHROOM.asItem()))
                .inputItems(new ItemStack(Items.RED_MUSHROOM.asItem()))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems(new ItemStack(Items.MUSHROOM_STEW.asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("stuffed_pumpkin_block"))
                .inputItems("farmersdelight:rice")
                .inputItems("farmersdelight:onion")
                .inputItems(new ItemStack(Items.BROWN_MUSHROOM.asItem()))
                .inputItems(new ItemStack(Items.POTATO.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.mc("fox_food")))
                .inputItems(TagUtils.createTag(RLUtils.forge("vegetables")))
                .inputItems(new ItemStack(Items.PUMPKIN.asItem()))
                .outputItems("farmersdelight:stuffed_pumpkin_block")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("chicken_soup"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_chicken")))
                .inputItems(new ItemStack(Items.CARROT.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("vegetables")))
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:chicken_soup")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("tomato_sauce"))
                .inputItems("farmersdelight:tomato", 2)
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:tomato_sauce")
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("pasta_with_meatballs"))
                .inputItems("farmersdelight:minced_beef")
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:pasta_with_meatballs")
                .outputItems(new ItemStack(Items.BOWL.asItem()))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("glow_berry_custard"))
                .inputItems(new ItemStack(Items.GLOW_BERRIES.asItem()))
                .inputItems(new ItemStack(Items.EGG.asItem()))
                .inputItems(new ItemStack(Items.SUGAR.asItem()))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems("farmersdelight:glow_berry_custard")
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("dog_food"))
                .inputItems(new ItemStack(Items.ROTTEN_FLESH.asItem()))
                .inputItems(new ItemStack(Items.BONE.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.fd("wolf_prey")))
                .inputItems("farmersdelight:rice")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:dog_food")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("beetroot_soup"))
                .inputItems(new ItemStack(Items.BEETROOT.asItem(), 3))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems(new ItemStack(Items.BEETROOT_SOUP.asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(2)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("ratatouille"))
                .inputItems("farmersdelight:tomato")
                .inputItems("farmersdelight:onion")
                .inputItems(new ItemStack(Items.BEETROOT.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("vegetables")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:ratatouille")
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("pasta_with_mutton_chop"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_mutton")))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:pasta_with_mutton_chop")
                .outputItems(new ItemStack(Items.BOWL.asItem()))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("pumpkin_soup"))
                .inputItems("farmersdelight:pumpkin_slice")
                .inputItems(TagUtils.createTag(RLUtils.forge("crops/cabbage")))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_pork")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:pumpkin_soup")
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("noodle_soup"))
                .inputItems("farmersdelight:raw_pasta")
                .inputItems("farmersdelight:fried_egg")
                .inputItems(new ItemStack(Items.DRIED_KELP.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_pork")))
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:noodle_soup")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(1)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("fish_stew"))
                .inputItems(TagUtils.createTag(RLUtils.forge("raw_fishes")))
                .inputItems("farmersdelight:tomato_sauce")
                .inputItems("farmersdelight:onion")
                .inputItems(new ItemStack(Items.BOWL.asItem()))
                .outputItems("farmersdelight:fish_stew")
                .outputItems(new ItemStack(Items.BOWL.asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
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
        addSmoking("farmersdelight:bacon", "farmersdelight:cooked_bacon");
        addSmoking("farmersdelight:minced_beef", "farmersdelight:beef_patty");
        addSmoking("gtceu:dough", "minecraft:bread");
        addSmoking("minecraft:mutton", "minecraft:cooked_mutton");
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

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("dandelion_tea"))
                .inputItems(new ItemStack(Items.DANDELION.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.fr("tea_leaves")))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems("farmersrespite:dandelion_tea")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(4)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("long_dandelion_tea"))
                .inputItems(new ItemStack(Items.DANDELION.asItem()))
                .inputItems(TagUtils.createTag(RLUtils.fr("tea_leaves")))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems("farmersrespite:long_dandelion_tea")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(5)
                .EUt(120)
                .duration(300)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("hot_cocoa"))
                .inputItems(new ItemStack(Items.COCOA_BEANS.asItem()))
                .inputItems(TagPrefix.dust, GTMaterials.Sugar)
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems("farmersdelight:hot_cocoa")
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(4)
                .EUt(120)
                .duration(200)
                .save();

        ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id("strong_hot_cocoa"))
                .inputItems(new ItemStack(Items.COCOA_BEANS.asItem()))
                .inputItems(TagPrefix.dust, GTMaterials.Sugar)
                .inputItems(new ItemStack(Items.HONEY_BOTTLE.asItem()))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems("farmersrespite:strong_hot_cocoa")
                .outputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(6)
                .EUt(120)
                .duration(300)
                .save();
    }

    private static void addSmoking(String input, String output) {
        GTORecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id(StringUtils.decompose(output)[1]))
                .inputItems(input)
                .outputItems(output)
                .circuitMeta(3)
                .EUt(120)
                .duration(200);
        builder.save();
    }

    private static void addDrinking(String input1, String input2, String output) {
        GTORecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id(StringUtils.decompose(output)[1]))
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems(output)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(4)
                .EUt(120)
                .duration(200);
        builder.save();
    }

    private static void addDrinkingLong(String input1, String input2, String output) {
        GTORecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id(StringUtils.decompose(output)[1]))
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems(output)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTMaterials.Milk.getFluid(1000))
                .circuitMeta(5)
                .EUt(120)
                .duration(300);
        builder.save();
    }

    private static void addDrinkingStrong(String input1, String input2, String output) {
        GTORecipeBuilder builder = ELECTRIC_COOKING_RECIPES.recipeBuilder(GTOCore.id(StringUtils.decompose(output)[1]))
                .inputItems(input1)
                .inputItems(input2)
                .inputItems(new ItemStack(Items.HONEY_BOTTLE.asItem()))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputItems(output)
                .outputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .circuitMeta(6)
                .EUt(120)
                .duration(300);
        builder.save();
    }
};
