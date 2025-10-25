package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOFluidStorageKey;
import com.gtocore.common.machine.multiblock.generator.FullCellGenerator;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.item.armor.PowerlessJetpack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import com.google.common.collect.ImmutableMap;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Oxygen;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.common.machine.multiblock.generator.FullCellGenerator.Wrapper.MEMBRANE_MATS;

public class FuelCell {

    public static void init() {
        int i = 0;
        for (var materialSet : FullCellGenerator.Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET.entrySet()) {
            var material = materialSet.getKey();
            var euPerMb = materialSet.getValue();
            var membrane = ChemicalHelper.get(GTOTagPrefix.MEMBRANE_ELECTRODE, MEMBRANE_MATS.get(i++));
            FUEL_CELL_ENERGY_RELEASE_RECIPES.recipeBuilder(material.getName() + "_release")
                    .notConsumable(membrane.copy())
                    .inputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE), 20)
                    .inputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE), 20)
                    .outputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE), 19)
                    .outputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE), 19)
                    .EUt(-euPerMb * 5)
                    .duration(20)
                    .save();
            for (var materialSet2 : ImmutableMap.copyOf(FullCellGenerator.Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET).entrySet()) {
                var material2 = materialSet2.getKey();
                var euPerMb2 = materialSet2.getValue();
                long partialEuPerMb1 = euPerMb >> 8;
                long partialEuPerMb2 = euPerMb2 >> 8;
                if (material == material2) continue;
                FUEL_CELL_ENERGY_TRANSFER_RECIPES.recipeBuilder(material.getName() + "_to_" + material2.getName() + "_absorption")
                        .notConsumable(membrane.copyWithCount(2))
                        .inputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE), 20 * partialEuPerMb2)
                        .inputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE), 20 * partialEuPerMb2)
                        .inputFluids(material2.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE), 19 * partialEuPerMb1)
                        .inputFluids(material2.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE), 19 * partialEuPerMb1)
                        .outputFluids(material2.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE), 19 * partialEuPerMb1)
                        .outputFluids(material2.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE), 19 * partialEuPerMb1)
                        .outputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE), 19 * partialEuPerMb2)
                        .outputFluids(material.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE), 19 * partialEuPerMb2)
                        .EUt(1)
                        .duration(20)
                        .addData("efficiency", (float) partialEuPerMb1 / partialEuPerMb2 * euPerMb2 / euPerMb * 0.95f)
                        .save();
            }
        }

        PowerlessJetpack.FUELS.forEach((fluidStack, duration) -> {
            Fluid fluid = fluidStack.getStacks()[0].getFluid();
            long totalEu = (long) duration * 4;
            FUEL_CELL_ENERGY_ABSORPTION_RECIPES.recipeBuilder(BuiltInRegistries.FLUID.getResourceKey(fluid).map(k -> k.location().getPath()).orElseThrow() + "_absorption")
                    .notConsumable(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("gt", "membrane_electrodes")))
                    .inputFluids(Oxygen.getFluid(FluidStorageKeys.LIQUID), fluidStack.getAmount())
                    .inputFluids(fluid, fluidStack.getAmount())
                    .EUt(1)
                    .duration(20)
                    .addData("convertedEnergy", totalEu)
                    .save();
        });
    }
}
