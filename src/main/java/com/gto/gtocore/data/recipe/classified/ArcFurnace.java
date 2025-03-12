package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.recipe.condition.RestrictedMachineCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import committee.nova.mods.avaritia.init.registry.ModItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.ARC_FURNACE_RECIPES;

interface ArcFurnace {

    static void init() {
        ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("warped_ender_pearl"))
                .inputItems("torchmaster:frozen_pearl")
                .inputFluids(GTMaterials.Blaze.getFluid(576))
                .outputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .outputFluids(new FluidStack(Fluids.WATER, 500))
                .EUt(480)
                .duration(200)
                .save();

        ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("anthracene"))
                .inputItems(TagPrefix.gem, GTMaterials.Coke)
                .inputFluids(GTMaterials.Oxygen.getFluid(400))
                .outputFluids(GTOMaterials.Anthracene.getFluid(100))
                .EUt(120)
                .duration(400)
                .save();

        ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("germanium_ash_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumContainingPrecipitate, 2)
                .inputFluids(GTMaterials.Oxygen.getFluid(120))
                .outputItems(TagPrefix.dust, GTOMaterials.GermaniumAsh)
                .EUt(30)
                .duration(120)
                .save();

        ARC_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cosmic_singularity"))
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(9216))
                .outputItems(GTOItems.COSMIC_SINGULARITY.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.Shirabon, 64)
                .outputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(2013265920)
                .duration(800)
                .addCondition(new RestrictedMachineCondition(GTOCore.id("magnetic_energy_reaction_furnace")))
                .save();
    }
}
