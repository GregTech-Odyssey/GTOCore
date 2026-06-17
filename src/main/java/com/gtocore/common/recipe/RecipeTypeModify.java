package com.gtocore.common.recipe;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeCategories;
import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.recipe.custom.FormingPressLogic;
import com.gtocore.data.recipe.classified.ManaSimulator;
import com.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gtolib.api.recipe.extension.MANARecipeExtension;
import com.gtolib.api.recipe.extension.MANATRecipeExtension;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.GTRecipeBuilder;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.steam.SteamLiquidBoilerMachine;
import com.gregtechceu.gtceu.common.machine.steam.SteamSolidBoilerMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.material.Fluid;

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

        CHEMICAL_RECIPES.onRecipeBuild((r) -> {});

        LARGE_CHEMICAL_RECIPES.getProxyRecipes().add(CHEMICAL_RECIPES);

        ASSEMBLY_LINE_RECIPES.onRecipeBuild(GenerateDisassembly::generateDisassembly);

        ASSEMBLER_RECIPES.onRecipeBuild((b) -> {
            var mana = b.getData().getLong(MANATRecipeExtension.INSTANCE) + b.getData().getLong(MANARecipeExtension.INSTANCE);
            if (mana > 0) {
                b.category(GTORecipeCategories.MANA_ASSEMBLER);
                MANA_FLOW_ASSEMBLER_RECIPES.copyFrom(b).save();
            }
            GenerateDisassembly.generateDisassembly(b);
        });

        PLASMA_GENERATOR_FUELS.onRecipeBuild((recipeBuilder) -> {
            long eu = recipeBuilder.getDuration() * GTValues.V[GTValues.EV] * 2;
            int water = (int) (eu / 80);
            FluidIngredient input = recipeBuilder.getFluidInputs().getFirst().inner.copy(10);
            FluidIngredient output = recipeBuilder.getFluidOutputs().getFirst().inner.copy(9);
            HEAT_EXCHANGER_RECIPES.recipeBuilder(recipeBuilder.getId())
                    .inputFluids(input)
                    .inputFluids(GTMaterials.DistilledWater.getFluid(water))
                    .outputFluids(output)
                    .outputFluids(GTOMaterials.HighPressureSteam.getFluid(water * 40))
                    .outputFluids(GTOMaterials.SupercriticalSteam.getFluid(water * 10))
                    .addData(GTORecipeDataKeys.EU, eu)
                    .duration(200)
                    .save();
        });

        LASER_ENGRAVER_RECIPES.setMaxIOSize(2, 1, 2, 1)
                .onRecipeBuild((recipeBuilder) -> {
                    if (recipeBuilder.getData().contains(GTORecipeDataKeys.SPECIAL)) return;
                    var recipe = DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.copyFrom(recipeBuilder)
                            .duration((int) (recipeBuilder.getDuration() * 0.2))
                            .EUt(recipeBuilder.EUt() << 2);
                    double value = Math.log10(recipeBuilder.EUt()) / Math.log10(4);
                    if (value > 10) {
                        recipe.inputFluids(GTOMaterials.EuvPhotoresist.getFluid((int) (value / 2)));
                    } else {
                        recipe.inputFluids(GTOMaterials.Photoresist.getFluid((int) value));
                    }
                    recipe.save();
                });

        CUTTER_RECIPES.onRecipeBuild((recipeBuilder) -> {
            if (recipeBuilder.getFluidInputs().isEmpty()) {

                int originalDuration = recipeBuilder.getDuration();
                int index = getEUTierIndex(GTUtil.getTierByVoltage(recipeBuilder.EUt()));

                var builder = recipeBuilder.copy(recipeBuilder.getId());
                addCuttingFluid(recipeBuilder, index);
                if (index > 1 && index < FLUID_TIERS.length - 1) {
                    int maxUpgradeTiers = FLUID_TIERS.length - index;

                    for (int upgradeTier = 1; upgradeTier < maxUpgradeTiers; upgradeTier++) {
                        double reductionFactor = Math.pow(0.8, upgradeTier);

                        var upgradedRecipe = builder.copy(builder.getId().getPath() + "_upgraded_t" + (index + upgradeTier))
                                .duration((int) Math.max(1, originalDuration * reductionFactor));

                        addUpgradedCuttingFluid(upgradedRecipe, index, index + upgradeTier, originalDuration, builder.EUt(), reductionFactor);
                    }
                }
            }
        });

        CIRCUIT_ASSEMBLER_RECIPES.onRecipeBuild((recipeBuilder) -> {
            if (recipeBuilder.getFluidInputs().isEmpty()) {
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

        STEAM_BOILER_RECIPES.onRecipeBuild((builder) -> {
            THERMAL_GENERATOR_FUELS.copyFrom(builder)
                    .EUt(-8)
                    .duration((int) Math.sqrt(builder.getDuration()))
                    .save();

            MANA_GARDEN_FUEL.copyFrom(builder)
                    .notConsumable("botania:endoflame")
                    .MANAt(-(int) (1.5 * ManaSimulator.BUFF_FACTOR))
                    .EUt(VA[MV])
                    .duration(builder.getDuration() / 2)
                    .save();

            var fluids = builder.getFluidInputs();
            if (!fluids.isEmpty()) {
                var fluid = fluids.getFirst().inner.getFluid();
                if (fluid != null) SteamLiquidBoilerMachine.FUEL_CACHE.add(fluid);
            }
            var items = builder.getItemInputs();
            if (!items.isEmpty()) {
                var item = items.getFirst().inner.getItem();
                if (!item.isEmpty()) SteamSolidBoilerMachine.FUEL_CACHE.add(item.getItem());
            }
        });

        LARGE_BOILER_RECIPES.addDataInfo(data -> {
            int temperature = data.data.getInt(GTORecipeDataKeys.TEMPERATURE);
            if (temperature > 0) {
                return I18n.get("gtceu.multiblock.hpca.temperature", temperature);
            }
            return "";
        });

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

    private static void addCuttingFluid(GTRecipeBuilder recipeBuilder, int index) {
        CuttingFluid selected = FLUID_TIERS[index];
        long fluidAmount = Math.max(1, recipeBuilder.getDuration() * recipeBuilder.EUt() / selected.divisor());
        recipeBuilder.inputFluids(FluidIngredient.of(selected.fluid(), fluidAmount));
    }

    private static void addUpgradedCuttingFluid(GTRecipeBuilder recipeBuilder, int originalIndex, int index, int originalDuration, long originalEUt, double reductionFactor) {
        CuttingFluid selected = FLUID_TIERS[index];

        long fluidAmount = (long) Math.max(1, originalDuration * originalEUt * reductionFactor / FLUID_TIERS[originalIndex].divisor());

        recipeBuilder.inputFluids(FluidIngredient.of(selected.fluid(), fluidAmount));
        recipeBuilder.save();
    }

    private record CuttingFluid(Fluid fluid, int divisor) {}
}
