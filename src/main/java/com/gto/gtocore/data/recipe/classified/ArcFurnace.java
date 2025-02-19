package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.function.Consumer;

interface ArcFurnace {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("warped_ender_pearl"))
                .inputItems(RegistriesUtils.getItemStack("torchmaster:frozen_pearl"))
                .inputFluids(GTMaterials.Blaze.getFluid(576))
                .outputItems(GTOItems.WARPED_ENDER_PEARL.asStack())
                .outputFluids(new FluidStack(Fluids.WATER, 500))
                .EUt(480)
                .duration(200)
                .save(provider);

        GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("anthracene"))
                .inputItems(TagPrefix.gem, GTMaterials.Coke)
                .inputFluids(GTMaterials.Oxygen.getFluid(400))
                .outputFluids(GTOMaterials.Anthracene.getFluid(100))
                .EUt(120)
                .duration(400)
                .save(provider);

        GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("germanium_ash_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumContainingPrecipitate, 2)
                .inputFluids(GTMaterials.Oxygen.getFluid(120))
                .outputItems(TagPrefix.dust, GTOMaterials.GermaniumAsh)
                .EUt(30)
                .duration(120)
                .save(provider);

        GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cosmic_singularity"))
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(9216))
                .outputItems(GTOItems.COSMIC_SINGULARITY.asStack())
                .outputItems(TagPrefix.dust, GTOMaterials.Shirabon, 64)
                .outputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(2013265920)
                .duration(200)
                .save(provider);
    }
}
