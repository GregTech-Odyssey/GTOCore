package com.gto.gtocore.common.recipe;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;

import java.util.Collections;

public final class RecipeTypeModify {

    public static void init() {
        GTRecipeTypes.COMBUSTION_GENERATOR_FUELS.setMaxIOSize(0, 0, 2, 0);
        GTRecipeTypes.GAS_TURBINE_FUELS.setMaxIOSize(0, 0, 2, 0);
        GTRecipeTypes.DUMMY_RECIPES.setMaxIOSize(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        GTRecipeTypes.WIREMILL_RECIPES.setMaxIOSize(1, 1, 0, 0);

        GTRecipeTypes.SIFTER_RECIPES.setMaxIOSize(1, 6, 1, 1);

        GTRecipeTypes.CHEMICAL_RECIPES.onRecipeBuild((r, p) -> {});

        GTRecipeTypes.ASSEMBLY_LINE_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
            ResearchManager.createDefaultResearchRecipe(recipeBuilder, provider);
            GenerateDisassembly.generateDisassembly(recipeBuilder, provider);
        });

        GTRecipeTypes.ASSEMBLER_RECIPES.onRecipeBuild(GenerateDisassembly::generateDisassembly);

        GTRecipeTypes.PLASMA_GENERATOR_FUELS.onRecipeBuild((recipeBuilder, provider) -> {
            long eu = recipeBuilder.duration * GTValues.V[GTValues.EV];
            GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(recipeBuilder.id)
                    .inputFluids(FluidRecipeCapability.CAP.of(recipeBuilder.input
                            .get(FluidRecipeCapability.CAP).get(0).getContent()))
                    .inputFluids(GTMaterials.DistilledWater.getFluid((int) (eu / 160)))
                    .outputFluids(FluidRecipeCapability.CAP.of(recipeBuilder.output
                            .get(FluidRecipeCapability.CAP).get(0).getContent()))
                    .outputFluids(GTOMaterials.SupercriticalSteam.getFluid((int) (eu / 16)))
                    .addData("eu", eu)
                    .duration(200)
                    .save(provider);
        });

        GTRecipeTypes.LASER_ENGRAVER_RECIPES.setMaxIOSize(2, 1, 2, 1)
                .onRecipeBuild((recipeBuilder, provider) -> {
                    if (recipeBuilder.data.contains("special")) return;
                    GTRecipeBuilder recipe = GTORecipeTypes.DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.copyFrom(recipeBuilder)
                            .duration((int) (recipeBuilder.duration * 0.2))
                            .EUt(recipeBuilder.EUt() << 2);
                    double value = Math.log10(recipeBuilder.EUt()) / Math.log10(4);
                    if (value > 10) {
                        recipe.inputFluids(GTOMaterials.EuvPhotoresist.getFluid((int) (value / 2)));
                    } else {
                        recipe.inputFluids(GTOMaterials.Photoresist.getFluid((int) value));
                    }
                    recipe.save(provider);
                });

        GTRecipeTypes.CUTTER_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
            if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty() &&
                    recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {
                if (recipeBuilder.EUt() < GTValues.VA[GTValues.MV]) {
                    recipeBuilder.inputFluids(GTMaterials.Water.getFluid((int) Math.max(4,
                            recipeBuilder.duration * recipeBuilder.EUt() / 60)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.EV]) {
                    recipeBuilder.inputFluids(GTMaterials.Lubricant.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 2880)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.IV]) {
                    recipeBuilder.inputFluids(GTOMaterials.FilteredSater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 3840)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.LuV]) {
                    recipeBuilder.inputFluids(GTOMaterials.OzoneWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 15360)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.ZPM]) {
                    recipeBuilder.inputFluids(GTOMaterials.FlocculentWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 61440)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UV]) {
                    recipeBuilder.inputFluids(GTOMaterials.PHNeutralWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 245760)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UHV]) {
                    recipeBuilder.inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 983040)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UEV]) {
                    recipeBuilder.inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 3932160)));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UIV]) {
                    recipeBuilder.inputFluids(GTOMaterials.DegassedWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 15728640)));
                } else {
                    recipeBuilder.inputFluids(GTOMaterials.BaryonicPerfectionWater.getFluid((int) Math.max(1,
                            recipeBuilder.duration * recipeBuilder.EUt() / 62914560)));
                }
            }
        });

        GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
            if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty() &&
                    recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList())
                            .isEmpty()) {
                if (recipeBuilder.EUt() < GTValues.VA[GTValues.HV]) {
                    recipeBuilder.inputFluids(GTMaterials.Tin.getFluid(Math.max(1, 144 * recipeBuilder.getSolderMultiplier())));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UV]) {
                    recipeBuilder.inputFluids(GTMaterials.SolderingAlloy.getFluid(Math.max(1, 144 * recipeBuilder.getSolderMultiplier())));
                } else if (recipeBuilder.EUt() < GTValues.VA[GTValues.UIV]) {
                    recipeBuilder.inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(Math.max(1, 144 * (GTUtil.getFloorTierByVoltage(recipeBuilder.EUt()) - 6))));
                } else {
                    recipeBuilder.inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(Math.max(1, 144 * (GTUtil.getFloorTierByVoltage(recipeBuilder.EUt()) - 8))));
                }
            }
        });

        GTRecipeTypes.STEAM_BOILER_RECIPES.onRecipeBuild((builder, provider) -> GTORecipeTypes.THERMAL_GENERATOR_FUELS.copyFrom(builder)
                .EUt(-8)
                .duration((int) Math.sqrt(builder.duration))
                .save(provider));
    }
}
