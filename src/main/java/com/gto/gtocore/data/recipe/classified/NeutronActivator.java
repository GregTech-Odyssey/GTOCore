package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

interface NeutronActivator {

    static void init() {
        GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES.recipeBuilder(GTOCore.id("oganesson"))
                .inputFluids(GTOMaterials.MetastableOganesson.getFluid(1000))
                .outputFluids(GTMaterials.Oganesson.getFluid(1000))
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData("ev_min", 720)
                .addData("ev_max", 800)
                .addData("evt", 1200)
                .save();

        GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES.recipeBuilder(GTOCore.id("quantanium"))
                .inputItems(GTItems.QUANTUM_STAR.asStack(4))
                .inputItems(GTItems.QUANTUM_EYE.asStack(8))
                .inputItems(TagPrefix.dust, GTOMaterials.Mithril, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Gadolinium, 16)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.Fluix, 16)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem(), 64))
                .inputFluids(GTMaterials.Neon.getFluid(10000))
                .outputFluids(GTOMaterials.Quantanium.getFluid(10000))
                .duration(1200)
                .addData("ev_min", 1020)
                .addData("ev_max", 1200)
                .addData("evt", 3840)
                .save();

        GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES.recipeBuilder(GTOCore.id("draconium_dust"))
                .notConsumable(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .inputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.EnderEye, 8)
                .outputItems(TagPrefix.dust, GTMaterials.EnderPearl, 4)
                .chancedOutput(GTOItems.DRACONIUM_DIRT.asStack(), 4000, 0)
                .duration(800)
                .addData("ev_min", 800)
                .addData("ev_max", 900)
                .addData("evt", 5760)
                .save();

        GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES.recipeBuilder(GTOCore.id("hassium"))
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputFluids(GTMaterials.Hassium.getFluid(1000))
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .addData("ev_min", 340)
                .addData("ev_max", 380)
                .addData("evt", 480)
                .save();
    }
}
