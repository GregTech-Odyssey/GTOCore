package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;

import static com.gto.gtocore.common.data.GTORecipeTypes.FERMENTING_RECIPES;

interface Fermenting {

    static void init() {
        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("taranium_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium)
                .EUt(2)
                .duration(200000)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("poisonous_potato"))
                .inputItems(new ItemStack(Blocks.POTATOES.asItem()))
                .outputItems(new ItemStack(Items.POISONOUS_POTATO.asItem()))
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("rotten_flesh"))
                .inputItems(TagPrefix.dust, GTMaterials.Meat)
                .outputItems(new ItemStack(Items.ROTTEN_FLESH.asItem()))
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("nutrient_distillation"))
                .inputItems(GTItems.DOUGH.get())
                .inputFluids(GTMaterials.FermentedBiomass.getFluid(1000))
                .outputFluids(new FluidStack(EIOFluids.NUTRIENT_DISTILLATION.getSource(), 1000))
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("cloud_seed_concentrated"))
                .inputItems(GTOItems.ESSENCE.asItem())
                .inputFluids(new FluidStack(EIOFluids.CLOUD_SEED.getSource(), 1000))
                .outputFluids(new FluidStack(EIOFluids.CLOUD_SEED_CONCENTRATED.getSource(), 1000))
                .EUt(480)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("hootch"))
                .inputItems(GTOItems.RED_ALGAE.asStack(4))
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputFluids(new FluidStack(EIOFluids.HOOTCH.getSource(), 1000))
                .EUt(120)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder(GTOCore.id("vapor_of_levity"))
                .inputItems(GTOItems.BLUE_ALGAE.asStack(4))
                .inputFluids(new FluidStack(EIOFluids.DEW_OF_THE_VOID.getSource(), 1000))
                .outputFluids(new FluidStack(EIOFluids.VAPOR_OF_LEVITY.getSource(), 1000))
                .EUt(120)
                .duration(40)
                .save();
    }
}
