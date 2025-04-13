package com.gto.gtocore.common.recipe;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.client.resources.language.I18n;

import java.util.Collections;

import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface RecipeTypeModify {

    static void init() {
        COMBUSTION_GENERATOR_FUELS.setMaxIOSize(0, 0, 2, 0);
        GAS_TURBINE_FUELS.setMaxIOSize(0, 0, 2, 0);
        DUMMY_RECIPES.setMaxIOSize(1, 1, 1, 1);
        WIREMILL_RECIPES.setMaxIOSize(1, 1, 0, 0);

        SIFTER_RECIPES.setMaxIOSize(1, 6, 1, 0);

        CHEMICAL_RECIPES.onRecipeBuild((r, p) -> {});

        ASSEMBLY_LINE_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
            ResearchManager.createDefaultResearchRecipe(recipeBuilder, provider);
            GenerateDisassembly.generateDisassembly(recipeBuilder, provider);
        });

        ASSEMBLER_RECIPES.setMANAIO(IO.IN);
        ASSEMBLER_RECIPES.onRecipeBuild(GenerateDisassembly::generateDisassembly);

        PLASMA_GENERATOR_FUELS.onRecipeBuild((recipeBuilder, provider) -> {
            long eu = recipeBuilder.duration * GTValues.V[GTValues.EV] * 5;
            FluidIngredient output = FluidRecipeCapability.CAP.of(recipeBuilder.output
                    .get(FluidRecipeCapability.CAP).get(0).getContent()).copy();
            FluidIngredient input = FluidRecipeCapability.CAP.of(recipeBuilder.input
                    .get(FluidRecipeCapability.CAP).get(0).getContent());
            output.setAmount(output.getAmount() << 2);
            input.setAmount(input.getAmount() * 5);
            GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(recipeBuilder.id)
                    .inputFluids(input)
                    .inputFluids(GTMaterials.DistilledWater.getFluid((int) (eu / 160)))
                    .outputFluids(output)
                    .outputFluids(GTOMaterials.HighPressureSteam.getFluid((int) (eu / 4)))
                    .outputFluids(GTOMaterials.SupercriticalSteam.getFluid((int) (eu / 16)))
                    .addData("eu", eu)
                    .duration(200)
                    .save(provider);
        });

        LASER_ENGRAVER_RECIPES.setMaxIOSize(2, 1, 2, 1)
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

        CUTTER_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
            if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty() &&
                    recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {
                addCuttingFluid(recipeBuilder);
            }
        });

        CIRCUIT_ASSEMBLER_RECIPES.onRecipeBuild((recipeBuilder, provider) -> {
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

        STEAM_BOILER_RECIPES.onRecipeBuild((builder, provider) -> GTORecipeTypes.THERMAL_GENERATOR_FUELS.copyFrom(builder)
                .EUt(-8)
                .duration((int) Math.sqrt(builder.duration))
                .save(provider));

        LARGE_BOILER_RECIPES.addDataInfo(data -> {
            int temperature = data.getInt("temperature");
            if (temperature > 0) {
                return I18n.get("gtceu.multiblock.hpca.temperature", temperature);
            }
            return "";
        });
    }

    static void addCuttingFluid(GTRecipeBuilder recipeBuilder) {
        int euTier = GTUtil.getFloorTierByVoltage(recipeBuilder.EUt());
        int duration = recipeBuilder.duration;
        long euPerTick = recipeBuilder.EUt();

        record CuttingFluid(FluidIngredient fluid, int divisor) {}

        CuttingFluid[] fluidTiers = new CuttingFluid[] {
                new CuttingFluid(GTMaterials.Water.getFluid(1), 60),
                new CuttingFluid(GTMaterials.Lubricant.getFluid(1), 2880),
                new CuttingFluid(GTOMaterials.FilteredSater.getFluid(1), 3840),
                new CuttingFluid(GTOMaterials.OzoneWater.getFluid(1), 15360),
                new CuttingFluid(GTOMaterials.FlocculentWater.getFluid(1), 61440),
                new CuttingFluid(GTOMaterials.PHNeutralWater.getFluid(1), 245760),
                new CuttingFluid(GTOMaterials.ExtremeTemperatureWater.getFluid(1), 983040),
                new CuttingFluid(GTOMaterials.ElectricEquilibriumWater.getFluid(1), 3932160),
                new CuttingFluid(GTOMaterials.DegassedWater.getFluid(1), 15728640),
                new CuttingFluid(GTOMaterials.BaryonicPerfectionWater.getFluid(1), 62914560)
        };

        int index = 0;
        if (euTier >= GTValues.MV && euTier < GTValues.EV) index = 1;
        else if (euTier >= GTValues.EV && euTier < GTValues.IV) index = 2;
        else if (euTier >= GTValues.IV && euTier < GTValues.LuV) index = 3;
        else if (euTier >= GTValues.LuV && euTier < GTValues.ZPM) index = 4;
        else if (euTier >= GTValues.ZPM && euTier < GTValues.UV) index = 5;
        else if (euTier >= GTValues.UV && euTier < GTValues.UHV) index = 6;
        else if (euTier >= GTValues.UHV && euTier < GTValues.UEV) index = 7;
        else if (euTier >= GTValues.UEV && euTier < GTValues.UIV) index = 8;
        else if (euTier >= GTValues.UIV) index = 9;

        CuttingFluid selected = fluidTiers[index];
        int fluidAmount = (int) Math.max(1, duration * euPerTick / selected.divisor());

        FluidIngredient fluid = selected.fluid();
        fluid.setAmount(fluidAmount);
        recipeBuilder.inputFluids(fluid);
    }
}
