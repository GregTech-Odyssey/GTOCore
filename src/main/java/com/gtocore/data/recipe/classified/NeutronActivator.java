package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES;

final class NeutronActivator {

    public static void init() {
        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("oganesson")
                .inputFluids(GTOMaterials.MetastableOganesson.getFluid(1000))
                .outputFluids(GTMaterials.Oganesson.getFluid(1000))
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData("ev_min", 720)
                .addData("ev_max", 800)
                .addData("evt", 1200)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("quantanium")
                .inputItems(GTItems.QUANTUM_STAR.asItem(), 4)
                .inputItems(GTItems.QUANTUM_EYE.asItem(), 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Mithril, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Gadolinium, 16)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.Fluix, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.EnergeticNetherite, 64)
                .inputFluids(GTOMaterials.Lemurite.getFluid(10000))
                .outputFluids(GTOMaterials.Quantanium.getFluid(10000))
                .duration(1200)
                .addData("ev_min", 1020)
                .addData("ev_max", 1200)
                .addData("evt", 3840)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("draconium_dust")
                .notConsumable(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .inputItems(Blocks.DRAGON_EGG.asItem())
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.EnderEye, 8)
                .outputItems(TagPrefix.dust, GTMaterials.EnderPearl, 4)
                .chancedOutput(GTOItems.DRACONIUM_DIRT.asItem(), 4000, 0)
                .duration(800)
                .addData("ev_min", 800)
                .addData("ev_max", 900)
                .addData("evt", 5760)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("hassium")
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputFluids(GTMaterials.Hassium.getFluid(1000))
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData("ev_min", 340)
                .addData("ev_max", 380)
                .addData("evt", 480)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("netherite")
                .inputItems(Items.NETHERITE_SCRAP.asItem(), 4)
                .outputItems(TagPrefix.dust, GTMaterials.Netherite)
                .inputFluids(GTMaterials.Gold.getFluid(576))
                .duration(600)
                .addData("ev_min", 100)
                .addData("ev_max", 1200)
                .addData("evt", 300)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("netherite_a")
                .inputItems(Items.ANCIENT_DEBRIS.asItem())
                .outputItems(TagPrefix.dust, GTMaterials.Netherite)
                .duration(400)
                .addData("ev_min", 1100)
                .addData("ev_max", 1200)
                .addData("evt", GTValues.VH[GTValues.OpV])
                .save();

        NEUTRON_ACTIVATOR_RECIPES.builder("haderoth_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Copper76)
                .inputItems(TagPrefix.dust, GTOMaterials.CopperAlloy)
                .outputItems(TagPrefix.dust, GTOMaterials.Haderoth, 2)
                .inputFluids(GTOMaterials.TranscendingMatter, 100)
                .duration(400)
                .addData("ev_min", 900)
                .addData("ev_max", 1100)
                .addData("evt", 1200)
                .save();

        NEUTRON_ACTIVATOR_RECIPES.builder("alduorite_dust")
                .inputItems(GTOItems.DUST_CRYOTHEUM.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Ceruclase)
                .outputItems(TagPrefix.dust, GTOMaterials.Alduorite)
                .inputFluids(GTOMaterials.TranscendingMatter, 80)
                .duration(200)
                .addData("ev_min", 1000)
                .addData("ev_max", 1100)
                .addData("evt", 1600)
                .save();
    }
}
