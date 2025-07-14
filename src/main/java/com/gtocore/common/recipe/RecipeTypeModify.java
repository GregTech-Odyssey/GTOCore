package com.gtocore.common.recipe;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeCategories;
import com.gtocore.common.recipe.custom.FormingPressLogic;
import com.gtocore.data.recipe.classified.ManaSimulator;
import com.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gtolib.api.capability.recipe.ManaRecipeCapability;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.material.Fluid;

import java.util.Collections;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class RecipeTypeModify {

    private RecipeTypeModify() {}

    private static final CuttingFluid[] FLUID_TIERS = new CuttingFluid[] {
            new CuttingFluid(GTMaterials.Water.getFluid(), 60),
            new CuttingFluid(GTMaterials.Lubricant.getFluid(), 2880),
            new CuttingFluid(GTOMaterials.FilteredSater.getFluid(), 3840),
            new CuttingFluid(GTOMaterials.OzoneWater.getFluid(), 15360),
            new CuttingFluid(GTOMaterials.FlocculentWater.getFluid(), 61440),
            new CuttingFluid(GTOMaterials.PHNeutralWater.getFluid(), 245760),
            new CuttingFluid(GTOMaterials.ExtremeTemperatureWater.getFluid(), 983040),
            new CuttingFluid(GTOMaterials.ElectricEquilibriumWater.getFluid(), 3932160),
            new CuttingFluid(GTOMaterials.DegassedWater.getFluid(), 15728640),
            new CuttingFluid(GTOMaterials.BaryonicPerfectionWater.getFluid(), 62914560)
    };

    public static void init() {
        COMBUSTION_GENERATOR_FUELS.setMaxIOSize(0, 0, 2, 0);
        GAS_TURBINE_FUELS.setMaxIOSize(0, 0, 2, 0);
        DUMMY_RECIPES.setMaxIOSize(1, 1, 1, 1);
        WIREMILL_RECIPES.setMaxIOSize(1, 1, 0, 0);

        SIFTER_RECIPES.setMaxIOSize(1, 6, 1, 0);

        CHEMICAL_RECIPES.onRecipeBuild((r, p) -> {});

        ASSEMBLY_LINE_RECIPES.onBuild(GenerateDisassembly::generateDisassembly);

        ASSEMBLER_RECIPES.setMANAIO(IO.IN);
        ASSEMBLER_RECIPES.onBuild(b -> {
            var mana = b.tickInput.get(ManaRecipeCapability.CAP);
            if (mana != null && !mana.isEmpty()) {
                b.category(GTORecipeCategories.MANA_ASSEMBLER);
            }
            GenerateDisassembly.generateDisassembly(b);
        });

        PLASMA_GENERATOR_FUELS.onRecipeBuild((recipeBuilder, provider) -> {
            long eu = recipeBuilder.duration * GTValues.V[GTValues.EV] * 2;
            FluidIngredient output = FluidRecipeCapability.CAP.of(recipeBuilder.output
                    .get(FluidRecipeCapability.CAP).get(0).getContent()).copy();
            FluidIngredient input = FluidRecipeCapability.CAP.of(recipeBuilder.input
                    .get(FluidRecipeCapability.CAP).get(0).getContent()).copy();
            output.setAmount(9);
            input.setAmount(10);
            HEAT_EXCHANGER_RECIPES.recipeBuilder(recipeBuilder.id)
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
                .onBuild((recipeBuilder) -> {
                    if (recipeBuilder.data.contains("special")) return;
                    var recipe = DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.copyFrom(recipeBuilder)
                            .duration((int) (recipeBuilder.duration * 0.2))
                            .EUt(recipeBuilder.Eut() << 2);
                    double value = Math.log10(recipeBuilder.Eut()) / Math.log10(4);
                    if (value > 10) {
                        recipe.inputFluids(GTOMaterials.EuvPhotoresist.getFluid((int) (value / 2)));
                    } else {
                        recipe.inputFluids(GTOMaterials.Photoresist.getFluid((int) value));
                    }
                    recipe.save();
                });

        CUTTER_RECIPES.onBuild((recipeBuilder) -> {
            if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty() &&
                    recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {

                int originalDuration = recipeBuilder.duration;
                int index = getEUTierIndex(GTUtil.getTierByVoltage(recipeBuilder.Eut()));

                var builder = recipeBuilder.copy(recipeBuilder.id);
                addCuttingFluid(recipeBuilder, index);
                if (index > 1 && index < FLUID_TIERS.length - 1) {
                    int maxUpgradeTiers = FLUID_TIERS.length - index;

                    for (int upgradeTier = 1; upgradeTier < maxUpgradeTiers; upgradeTier++) {
                        double reductionFactor = Math.pow(0.8, upgradeTier);

                        var upgradedRecipe = builder.copy(builder.id.getPath() + "_upgraded_t" + (index + upgradeTier))
                                .duration((int) Math.max(1, originalDuration * reductionFactor));

                        addUpgradedCuttingFluid(upgradedRecipe, index, index + upgradeTier, originalDuration, builder.Eut(), reductionFactor);
                    }
                }
            }
        });

        CIRCUIT_ASSEMBLER_RECIPES.onBuild(recipeBuilder -> {
            if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty() &&
                    recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList())
                            .isEmpty()) {
                if (recipeBuilder.Eut() < GTValues.VA[GTValues.HV]) {
                    recipeBuilder.inputFluids(GTMaterials.Tin.getFluid(Math.max(1, 144 * recipeBuilder.getSolderMultiplier())));
                } else if (recipeBuilder.Eut() < GTValues.VA[GTValues.UV]) {
                    recipeBuilder.inputFluids(GTMaterials.SolderingAlloy.getFluid(Math.max(1, 144 * recipeBuilder.getSolderMultiplier())));
                } else if (recipeBuilder.Eut() < GTValues.VA[GTValues.UIV]) {
                    recipeBuilder.inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(Math.max(1, 144 * (GTUtil.getFloorTierByVoltage(recipeBuilder.Eut()) - 6))));
                } else {
                    recipeBuilder.inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(Math.max(1, 144 * (GTUtil.getFloorTierByVoltage(recipeBuilder.Eut()) - 8))));
                }
            }
        });

        STEAM_BOILER_RECIPES.onRecipeBuild((builder, provider) -> {
            THERMAL_GENERATOR_FUELS.copyFrom(builder)
                    .EUt(-8)
                    .duration((int) Math.sqrt(builder.duration))
                    .save(provider);

            MANA_GARDEN_FUEL.copyFrom(builder)
                    .notConsumable("botania:endoflame")
                    .MANAt(-(int) (1.5 * ManaSimulator.BUFF_FACTOR))
                    .EUt(VA[MV])
                    .duration(builder.duration / 2)
                    .save(provider);
        });

        LARGE_BOILER_RECIPES.addDataInfo(data -> {
            int temperature = data.getInt("temperature");
            if (temperature > 0) {
                return I18n.get("gtceu.multiblock.hpca.temperature", temperature);
            }
            return "";
        }).prioritySearch();

        GTRecipeTypes.FORMING_PRESS_RECIPES.getCustomRecipeLogicRunners().clear();
        GTRecipeTypes.FORMING_PRESS_RECIPES.getCustomRecipeLogicRunners().add(new FormingPressLogic());
    }

    private static int getEUTierIndex(int euTier) {
        return switch (euTier) {
            case 0, 1 -> 0;
            case 2, 3 -> 1;
            case 4 -> 2;
            case 5 -> 3;
            case 6 -> 4;
            case 7 -> 5;
            case 8 -> 6;
            case 9 -> 7;
            case 10 -> 8;
            default -> 9;
        };
    }

    private static void addCuttingFluid(RecipeBuilder recipeBuilder, int index) {
        CuttingFluid selected = FLUID_TIERS[index];
        long fluidAmount = Math.max(1, recipeBuilder.duration * recipeBuilder.Eut() / selected.divisor());
        recipeBuilder.inputFluids(FastFluidIngredient.of(fluidAmount, selected.fluid()));
    }

    private static void addUpgradedCuttingFluid(RecipeBuilder recipeBuilder, int originalIndex, int index, int originalDuration, long originalEUt, double reductionFactor) {
        CuttingFluid selected = FLUID_TIERS[index];

        long fluidAmount = (long) Math.max(1, originalDuration * originalEUt * reductionFactor / FLUID_TIERS[originalIndex].divisor());

        recipeBuilder.inputFluids(FastFluidIngredient.of(fluidAmount, selected.fluid()));
        recipeBuilder.save(a -> {});
    }

    private record CuttingFluid(Fluid fluid, int divisor) {}
}
