package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Biomass;
import static com.gregtechceu.gtceu.common.data.GTMaterials.FermentedBiomass;
import static com.gtocore.common.data.GTORecipeTypes.FERMENTING_RECIPES;

final class Fermenting {

    public static void init() {
        FERMENTING_RECIPES.recipeBuilder("taranium_dust")
                .inputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium)
                .EUt(2)
                .duration(200000)
                .save();

        FERMENTING_RECIPES.recipeBuilder("poisonous_potato")
                .inputItems(Blocks.POTATOES.asItem())
                .outputItems(Items.POISONOUS_POTATO.asItem())
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder("rotten_flesh")
                .inputItems(TagPrefix.dust, GTMaterials.Meat)
                .outputItems(Items.ROTTEN_FLESH.asItem())
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder("nutrient_distillation")
                .inputItems(GTItems.DOUGH.get())
                .inputFluids(GTMaterials.FermentedBiomass.getFluid(1000))
                .outputFluids(new FluidStack(EIOFluids.NUTRIENT_DISTILLATION.getSource(), 1000))
                .EUt(30)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder("cloud_seed_concentrated")
                .inputItems(GTOItems.ESSENCE.asItem())
                .inputFluids(new FluidStack(EIOFluids.CLOUD_SEED.getSource(), 1000))
                .outputFluids(new FluidStack(EIOFluids.CLOUD_SEED_CONCENTRATED.getSource(), 1000))
                .EUt(480)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder("hootch")
                .inputItems(GTOItems.RED_ALGAE.asItem(), 4)
                .inputFluids(GTMaterials.Biomass.getFluid(1000))
                .outputFluids(new FluidStack(EIOFluids.HOOTCH.getSource(), 1000))
                .EUt(120)
                .duration(400)
                .save();

        FERMENTING_RECIPES.recipeBuilder("vapor_of_levity")
                .inputItems(GTOItems.BLUE_ALGAE.asItem(), 4)
                .inputFluids(new FluidStack(EIOFluids.DEW_OF_THE_VOID.getSource(), 1000))
                .outputFluids(new FluidStack(EIOFluids.VAPOR_OF_LEVITY.getSource(), 1000))
                .EUt(120)
                .duration(40)
                .save();

        FERMENTING_RECIPES.recipeBuilder("fermented_biomass")
                .circuitMeta(1)
                .inputFluids(Biomass.getFluid(100))
                .outputFluids(FermentedBiomass.getFluid(100))
                .duration(150).EUt(2).save();
    }
}
