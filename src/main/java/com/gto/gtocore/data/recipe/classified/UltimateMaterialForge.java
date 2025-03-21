package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES;

interface UltimateMaterialForge {

    static void init() {
        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("void_matter"))
                .inputItems(GTOItems.OMNI_MATTER.asItem())
                .inputItems(GTOItems.PELLET_ANTIMATTER.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(2000))
                .inputFluids(GTOMaterials.Gluons.getFluid(1000))
                .outputItems(GTOItems.VOID_MATTER.asItem())
                .chancedOutput(GTOItems.CORPOREAL_MATTER.asStack(), 2000, 0)
                .EUt(2013265920)
                .duration(400)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("temporal_matter"))
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asItem())
                .inputItems(GTOItems.KINETIC_MATTER.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.TEMPORAL_MATTER.asItem())
                .chancedOutput(GTOItems.OMNI_MATTER.asStack(), 500, 0)
                .EUt(2013265920)
                .duration(600)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("corporeal_matter"))
                .inputItems(GTOItems.PROTO_MATTER.asItem())
                .inputItems(TagPrefix.block, GTMaterials.Iron)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTMaterials.Nihonium.getFluid(100))
                .outputItems(GTOItems.CORPOREAL_MATTER.asItem())
                .chancedOutput(TagPrefix.nugget, GTOMaterials.HeavyQuarkDegenerateMatter, 500, 0)
                .EUt(503316480)
                .duration(800)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("proto_matter"))
                .inputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .outputItems(GTOItems.PROTO_MATTER.asItem())
                .chancedOutput(TagPrefix.ingot, GTOMaterials.Neutron, 6000, 0)
                .EUt(503316480)
                .duration(1600)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("dark_matter"))
                .inputItems(GTOItems.TEMPORAL_MATTER.asItem())
                .inputItems(GTOItems.VOID_MATTER.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(3000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000))
                .outputItems(GTOItems.DARK_MATTER.asItem())
                .chancedOutput(GTOItems.KINETIC_MATTER.asStack(), 1000, 0)
                .EUt(2013265920)
                .duration(1200)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("kinetic_matter"))
                .inputItems(GTOItems.CORPOREAL_MATTER.asItem())
                .inputItems(TagPrefix.block, GTMaterials.Tritanium)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTMaterials.Naquadria.getFluid(1000))
                .outputItems(GTOItems.KINETIC_MATTER.asItem())
                .chancedOutput(GTOItems.AMORPHOUS_MATTER.asStack(), 200, 0)
                .EUt(503316480)
                .duration(600)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("amorphous_matter"))
                .inputItems(GTOItems.CORPOREAL_MATTER.asItem())
                .inputItems(TagPrefix.block, GTOMaterials.CarbonNanotubes)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.Legendarium.getFluid(1000))
                .outputItems(GTOItems.AMORPHOUS_MATTER.asItem())
                .chancedOutput(GTOItems.ESSENTIA_MATTER.asStack(), 100, 0)
                .EUt(503316480)
                .duration(800)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("pellet_antimatter"))
                .inputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 64))
                .inputItems(TagPrefix.nugget, GTOMaterials.Neutron)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.Antihydrogen.getFluid(10))
                .outputItems(GTOItems.PELLET_ANTIMATTER.asItem())
                .chancedOutput(GTOItems.VOID_MATTER.asStack(), 100, 0)
                .EUt(125829120)
                .duration(2000)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("essentia_matter"))
                .inputItems(GTOItems.AMORPHOUS_MATTER.asItem())
                .inputItems(TagPrefix.block, GTOMaterials.HeavyQuarkDegenerateMatter)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(1000))
                .outputItems(GTOItems.ESSENTIA_MATTER.asItem())
                .chancedOutput(GTOItems.DARK_MATTER.asStack(), 100, 0)
                .EUt(503316480)
                .duration(1200)
                .save();

        ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("omni_matter"))
                .inputItems(GTOItems.ESSENTIA_MATTER.asItem())
                .inputItems(GTOItems.KINETIC_MATTER.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.OMNI_MATTER.asItem())
                .chancedOutput(TagPrefix.dustTiny, GTOMaterials.CosmicNeutronium, 1000, 0)
                .EUt(2013265920)
                .duration(800)
                .save();
    }
}
