package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

interface Fermenting {

    static void init() {
        GTORecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("taranium_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium)
                .EUt(2)
                .duration(200000)
                .save();

        GTORecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("poisonous_potato"))
                .inputItems(new ItemStack(Blocks.POTATOES.asItem()))
                .outputItems(new ItemStack(Items.POISONOUS_POTATO.asItem()))
                .EUt(30)
                .duration(400)
                .save();

        GTORecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("rotten_flesh"))
                .inputItems(TagPrefix.dust, GTMaterials.Meat)
                .outputItems(new ItemStack(Items.ROTTEN_FLESH.asItem()))
                .EUt(30)
                .duration(400)
                .save();
    }
}
