package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeDataKeys;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES;

final class NeutronActivator {

    public static void init() {
        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("oganesson")
                .inputFluids(GTOMaterials.MetastableOganesson, 1000)
                .outputFluids(GTMaterials.Oganesson, 1000)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData(GTORecipeDataKeys.EV_MIN, 720)
                .addData(GTORecipeDataKeys.EV_MAX, 800)
                .addData(GTORecipeDataKeys.EVT, 1200)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("quantanium")
                .inputItems(GTItems.QUANTUM_STAR, 4)
                .inputItems(GTItems.QUANTUM_EYE, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Mithril, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Gadolinium, 16)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.Fluix, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.EnergeticNetherite, 64)
                .inputFluids(GTOMaterials.Lemurite, 10000)
                .outputFluids(GTOMaterials.Quantanium, 10000)
                .duration(1200)
                .addData(GTORecipeDataKeys.EV_MIN, 1020)
                .addData(GTORecipeDataKeys.EV_MAX, 1200)
                .addData(GTORecipeDataKeys.EVT, 3840)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("draconium_dust")
                .notConsumable(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .inputItems(Blocks.DRAGON_EGG.asItem())
                .inputFluids(GTOMaterials.UuAmplifier, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.EnderEye, 8)
                .outputItems(TagPrefix.dust, GTMaterials.EnderPearl, 4)
                .chancedOutput(GTOItems.DRACONIUM_DIRT.asItem(), 4000, 0)
                .duration(800)
                .addData(GTORecipeDataKeys.EV_MIN, 800)
                .addData(GTORecipeDataKeys.EV_MAX, 900)
                .addData(GTORecipeDataKeys.EVT, 5760)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("hassium")
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputFluids(GTMaterials.Hassium, 1000)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData(GTORecipeDataKeys.EV_MIN, 340)
                .addData(GTORecipeDataKeys.EV_MAX, 380)
                .addData(GTORecipeDataKeys.EVT, 480)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("netherite")
                .inputItems(Items.NETHERITE_SCRAP.asItem(), 4)
                .outputItems(TagPrefix.dust, GTMaterials.Netherite)
                .inputFluids(GTMaterials.Gold, 576)
                .duration(600)
                .addData(GTORecipeDataKeys.EV_MIN, 100)
                .addData(GTORecipeDataKeys.EV_MAX, 1200)
                .addData(GTORecipeDataKeys.EVT, 300)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("netherite_a")
                .inputItems(Items.ANCIENT_DEBRIS.asItem())
                .outputItems(TagPrefix.dust, GTMaterials.Netherite)
                .duration(400)
                .addData(GTORecipeDataKeys.EV_MIN, 1100)
                .addData(GTORecipeDataKeys.EV_MAX, 1200)
                .addData(GTORecipeDataKeys.EVT, GTValues.VH[GTValues.OpV])
                .save();

        NEUTRON_ACTIVATOR_RECIPES.builder("haderoth_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Copper76)
                .inputItems(TagPrefix.dust, GTOMaterials.CopperAlloy)
                .outputItems(TagPrefix.dust, GTOMaterials.Haderoth, 2)
                .inputFluids(GTOMaterials.TranscendingMatter, 100)
                .duration(400)
                .addData(GTORecipeDataKeys.EV_MIN, 900)
                .addData(GTORecipeDataKeys.EV_MAX, 1100)
                .addData(GTORecipeDataKeys.EVT, 1200)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.builder("alduorite_dust")
                .inputItems(GTOItems.DUST_CRYOTHEUM)
                .inputItems(TagPrefix.dust, GTOMaterials.Ceruclase)
                .outputItems(TagPrefix.dust, GTOMaterials.Alduorite)
                .inputFluids(GTOMaterials.TranscendingMatter, 80)
                .duration(200)
                .addData(GTORecipeDataKeys.EV_MIN, 1000)
                .addData(GTORecipeDataKeys.EV_MAX, 1100)
                .addData(GTORecipeDataKeys.EVT, 1600)
                .save();
        NEUTRON_ACTIVATOR_RECIPES.builder("small_photonic_kristallite_dust")
                .inputItems(AEItems.MATTER_BALL.asItem(), 64)
                .inputItems(TagPrefix.dust, GTOMaterials.Photonium, 16)
                .notConsumable(GTOItems.HIGH_FREQUENCY_LASER.asStack())
                .chancedInput(GTOBlocks.ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS.asStack(), 75, 0)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.PhotonicKristallite, 16)
                .inputFluids(GTOMaterials.Quantanium, 1000)
                .outputFluids(GTOMaterials.PhotonicKristallite, 4000)
                .duration(1200)
                .addData(GTORecipeDataKeys.EV_MIN, 960)
                .addData(GTORecipeDataKeys.EV_MAX, 1080)
                .addData(GTORecipeDataKeys.EVT, 1920)
                .save();
        NEUTRON_ACTIVATOR_RECIPES.builder("rearranged_cosmic_dust_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ExcitedScorchingCosmicDust, 40)
                .outputItems(TagPrefix.dust, GTOMaterials.RearrangedCosmicDust, 30)
                .outputItems(TagPrefix.dust, GTOMaterials.ScorchingCosmicDust, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.ExcitedScorchingCosmicDust, 8)
                .inputFluids(GTOMaterials.CosmicDustDispersant, 1000)
                .chancedOutput(GTOMaterials.Sanguinite.getFluid(500), 5000, 0)
                .duration(100)
                .addData(GTORecipeDataKeys.EV_MIN, 420)
                .addData(GTORecipeDataKeys.EV_MAX, 1280)
                .addData(GTORecipeDataKeys.EVT, 7800)
                .save();
    }
}
