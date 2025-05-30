package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;
import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.function.Consumer;

final class Autoclave {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("sterilized_petri_dish"))
                .inputItems(GTItems.PETRI_DISH)
                .inputFluids(GTOMaterials.AbsoluteEthanol.getFluid(100))
                .outputItems(GTOItems.STERILIZED_PETRI_DISH)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .duration(25).EUt(7680).save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("spacetime_catalyst"))
                .inputItems(ModItems.infinity_catalyst.get())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .outputItems(GTOItems.SPACETIME_CATALYST.asStack())
                .EUt(8053063680L)
                .duration(1200)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("dry_graphene_gel_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GrapheneGelSuspension)
                .inputFluids(GTMaterials.Acetone.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.DryGrapheneGel)
                .EUt(480)
                .duration(260)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("periodically_poled_lithium_niobate_boule"))
                .notConsumable(GTOItems.ELECTRON_SOURCE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumNiobateNanoparticles, 2)
                .inputFluids(GTMaterials.Xenon.getFluid(1000))
                .outputItems(GTOItems.PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE.asStack())
                .EUt(1966080)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("soul_soil"))
                .inputItems(EIOItems.FILLED_SOUL_VIAL.asStack())
                .inputItems(new ItemStack(Blocks.ROOTED_DIRT.asItem()))
                .inputFluids(GTMaterials.LiquidNetherAir.getFluid(100))
                .outputItems(EIOItems.EMPTY_SOUL_VIAL.asStack())
                .outputItems(new ItemStack(Blocks.SOUL_SOIL.asItem()))
                .EUt(480)
                .duration(240)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("ender_crystal"))
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asStack())
                .inputFluids(GTOMaterials.Enderium.getFluid(8))
                .outputItems(EIOItems.ENDER_CRYSTAL.asStack())
                .EUt(30)
                .duration(200)
                .addCondition(new VacuumCondition(4))
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("hassium_seed_crystal"))
                .inputItems(TagPrefix.dustTiny, GTMaterials.Hassium)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(GTOItems.HASSIUM_SEED_CRYSTAL.asStack())
                .EUt(31457280)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("unstable_star"))
                .inputItems(GTOTagPrefix.nanites, GTOMaterials.Orichalcum)
                .inputItems(GTItems.GRAVI_STAR.asStack())
                .inputFluids(GTOMaterials.Adamantine.getFluid(288))
                .outputItems(GTOTagPrefix.contaminableNanites, GTOMaterials.Orichalcum)
                .outputItems(GTOItems.UNSTABLE_STAR.asStack())
                .EUt(491520)
                .duration(480)
                .addCondition(new GravityCondition(true))
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("nuclear_star"))
                .inputItems(GTOTagPrefix.nanites, GTOMaterials.CosmicNeutronium)
                .inputItems(GTOItems.UNSTABLE_STAR.asStack())
                .inputFluids(GTOMaterials.Infinity.getFluid(288))
                .outputItems(GTOTagPrefix.contaminableNanites, GTOMaterials.CosmicNeutronium)
                .outputItems(GTOItems.NUCLEAR_STAR.asStack())
                .EUt(31457280)
                .duration(480)
                .addCondition(new GravityCondition(true))
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("super_mutated_living_solder"))
                .inputItems(GTOItems.SPACE_ESSENCE.asStack(64))
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack(64))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(10000))
                .outputItems(GTOBlocks.ESSENCE_BLOCK.asStack())
                .outputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(10000))
                .EUt(7864320)
                .duration(2400)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("contained_kerr_singularity"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Vibranium)
                .inputItems(GTOItems.CONTAINED_KERR_NEWMANN_SINGULARITY.asStack())
                .inputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .outputItems(GTOItems.CONTAINED_KERR_SINGULARITY.asStack())
                .EUt(1966080)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("draconium_dust"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Enderium, 64)
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack())
                .inputFluids(GTOMaterials.DragonElement.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("prescient_crystal"))
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asStack())
                .inputFluids(GTOMaterials.Mithril.getFluid(8))
                .outputItems(EIOItems.PRESCIENT_CRYSTAL.asStack())
                .EUt(30)
                .duration(200)
                .addCondition(new VacuumCondition(4))
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("vibrant_crystal"))
                .inputItems(TagPrefix.gem, GTMaterials.Emerald)
                .inputFluids(GTOMaterials.PulsatingAlloy.getFluid(72))
                .outputItems(EIOItems.VIBRANT_CRYSTAL.asStack())
                .EUt(30)
                .duration(160)
                .addCondition(new VacuumCondition(2))
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("imprinted_resonatic_circuit_board"))
                .inputItems(GTOItems.RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(432))
                .outputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .EUt(1920)
                .duration(300)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("pulsating_crystal"))
                .inputItems(TagPrefix.gem, GTMaterials.Diamond)
                .inputFluids(GTOMaterials.PulsatingAlloy.getFluid(72))
                .outputItems(EIOItems.PULSATING_CRYSTAL.asStack())
                .EUt(30)
                .duration(100)
                .addCondition(new VacuumCondition(2))
                .save(provider);
    }
}
