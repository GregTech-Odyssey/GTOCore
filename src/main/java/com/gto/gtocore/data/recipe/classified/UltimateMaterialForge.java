package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEItems;

import java.util.function.Consumer;

interface UltimateMaterialForge {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("void_matter"))
                .inputItems(GTOItems.OMNI_MATTER.asStack())
                .inputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(2000))
                .inputFluids(GTOMaterials.Gluons.getFluid(1000))
                .outputItems(GTOItems.VOID_MATTER.asStack())
                .chancedOutput(GTOItems.CORPOREAL_MATTER.asStack(), 2000, 0)
                .EUt(2013265920)
                .duration(400)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("temporal_matter"))
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asStack())
                .inputItems(GTOItems.KINETIC_MATTER.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.TEMPORAL_MATTER.asStack())
                .chancedOutput(GTOItems.OMNI_MATTER.asStack(), 500, 0)
                .EUt(2013265920)
                .duration(600)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("corporeal_matter"))
                .inputItems(GTOItems.PROTO_MATTER.asStack())
                .inputItems(TagPrefix.block, GTMaterials.Iron)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTMaterials.Nihonium.getFluid(100))
                .outputItems(GTOItems.CORPOREAL_MATTER.asStack())
                .chancedOutput(TagPrefix.nugget, GTOMaterials.HeavyQuarkDegenerateMatter, 500, 0)
                .EUt(503316480)
                .duration(800)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("proto_matter"))
                .inputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .outputItems(GTOItems.PROTO_MATTER.asStack())
                .chancedOutput(TagPrefix.ingot, GTMaterials.Neutronium, 6000, 0)
                .EUt(503316480)
                .duration(1600)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("dark_matter"))
                .inputItems(GTOItems.TEMPORAL_MATTER.asStack())
                .inputItems(GTOItems.VOID_MATTER.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(3000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000))
                .outputItems(GTOItems.DARK_MATTER.asStack())
                .chancedOutput(GTOItems.KINETIC_MATTER.asStack(), 1000, 0)
                .EUt(2013265920)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("kinetic_matter"))
                .inputItems(GTOItems.CORPOREAL_MATTER.asStack())
                .inputItems(TagPrefix.block, GTMaterials.Tritanium)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTMaterials.Naquadria.getFluid(1000))
                .outputItems(GTOItems.KINETIC_MATTER.asStack())
                .chancedOutput(GTOItems.AMORPHOUS_MATTER.asStack(), 200, 0)
                .EUt(503316480)
                .duration(600)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("amorphous_matter"))
                .inputItems(GTOItems.CORPOREAL_MATTER.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.CarbonNanotubes)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.Legendarium.getFluid(1000))
                .outputItems(GTOItems.AMORPHOUS_MATTER.asStack())
                .chancedOutput(GTOItems.ESSENTIA_MATTER.asStack(), 100, 0)
                .EUt(503316480)
                .duration(800)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("pellet_antimatter"))
                .inputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 64))
                .inputItems(TagPrefix.nugget, GTMaterials.Neutronium)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.Antihydrogen.getFluid(10))
                .outputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .chancedOutput(GTOItems.VOID_MATTER.asStack(), 100, 0)
                .EUt(125829120)
                .duration(2000)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("essentia_matter"))
                .inputItems(GTOItems.AMORPHOUS_MATTER.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.HeavyQuarkDegenerateMatter)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(1000))
                .outputItems(GTOItems.ESSENTIA_MATTER.asStack())
                .chancedOutput(GTOItems.DARK_MATTER.asStack(), 100, 0)
                .EUt(503316480)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES.recipeBuilder(GTOCore.id("omni_matter"))
                .inputItems(GTOItems.ESSENTIA_MATTER.asStack())
                .inputItems(GTOItems.KINETIC_MATTER.asStack())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.OMNI_MATTER.asStack())
                .chancedOutput(TagPrefix.dustTiny, GTOMaterials.CosmicNeutronium, 1000, 0)
                .EUt(2013265920)
                .duration(800)
                .save(provider);
    }
}
