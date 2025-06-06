package com.gto.gtocore.common.data;

import com.gto.gtocore.api.machine.feature.ICoilMachine;
import com.gto.gtocore.api.machine.feature.IOverclockConfigMachine;
import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public final class GTORecipeModifiers {

    public static final RecipeModifier GCYM_OVERCLOCKING = (machine, r) -> recipe -> overclocking(machine, hatchParallel(machine, recipe), false, false, 0.8, 0.6);

    public static final RecipeModifier GENERATOR_OVERCLOCKING = (machine, r) -> recipe -> generatorOverclocking(machine, recipe);
    public static final RecipeModifier PERFECT_OVERCLOCKING = (machine, r) -> recipe -> perfectOverclocking(machine, recipe);
    public static final RecipeModifier OVERCLOCKING = (machine, r) -> recipe -> overclocking(machine, recipe);

    public static final RecipeModifier HATCH_PARALLEL = (machine, r) -> recipe -> hatchParallel(machine, recipe);

    public static RecipeModifier overclocking(boolean perfect, double reductionEUt, double reductionDuration) {
        return (machine, r) -> recipe -> overclocking(machine, recipe, perfect, false, reductionEUt, reductionDuration);
    }

    public static ModifierFunction simpleGeneratorMachineModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof SimpleGeneratorMachine generator) {
            var EUt = RecipeHelper.getOutputEUt(recipe);
            if (EUt > 0) {
                int maxParallel = (int) ((GTValues.V[generator.getTier()] * GeneratorArrayMachine.getAmperage(generator.getTier())) / EUt);
                int parallels = ParallelLogic.getParallelAmountFast(generator, recipe, maxParallel);
                return ModifierFunction.builder().durationMultiplier((double) GeneratorArrayMachine.getEfficiency(generator.getRecipeType(), generator.getTier()) / 100).inputModifier(ContentModifier.multiplier(parallels)).outputModifier(ContentModifier.multiplier(parallels)).eutMultiplier(parallels).parallels(parallels).build();
            }
        }
        return ModifierFunction.NULL;
    }

    public static ModifierFunction largeBoilerModifier(MetaMachine machine, GTRecipe r) {
        return recipe -> {
            if (machine instanceof LargeBoilerMachine largeBoilerMachine) {
                double duration = recipe.duration * 1600.0D / largeBoilerMachine.maxTemperature;
                if (duration < 1) {
                    recipe = accurateParallel(machine, recipe, (int) (1 / duration));
                }
                if (largeBoilerMachine.getThrottle() < 100) {
                    duration = duration * 100 / largeBoilerMachine.getThrottle();
                }
                recipe.duration = (int) (recipe.duration * 100 * duration / largeBoilerMachine.getThrottle());
            }
            return recipe;
        };
    }

    public static ModifierFunction chemicalPlantOverclock(MetaMachine machine, GTRecipe r) {
        return recipe -> {
            if (machine instanceof ICoilMachine coilMachine) {
                return overclocking(machine, hatchParallel(machine, recipe), true, false, (1.0 - coilMachine.getCoilTier() * 0.05), (1.0 - coilMachine.getCoilTier() * 0.05));
            }
            return null;
        };
    }

    public static ModifierFunction crackerOverclock(MetaMachine machine, GTRecipe r) {
        return recipe -> {
            if (machine instanceof ICoilMachine coilMachine) {
                return overclocking(machine, recipe, false, false, (1.0 - coilMachine.getCoilTier() * 0.1), 1);
            }
            return null;
        };
    }

    public static ModifierFunction pyrolyseOvenOverclock(MetaMachine machine, GTRecipe r) {
        return recipe -> {
            if (machine instanceof ICoilMachine coilMachine) {
                if (coilMachine.getCoilTier() == 0) {
                    return overclocking(machine, recipe, false, false, 1, 1.33);
                } else {
                    return overclocking(machine, recipe, false, false, 1, 2.0 / (coilMachine.getCoilTier() + 1));
                }
            }
            return null;
        };
    }

    public static ModifierFunction ebfOverclock(MetaMachine machine, GTRecipe r) {
        return recipe -> {
            if (machine instanceof ICoilMachine coilMachine && machine instanceof IOverclockMachine overclockMachine) {
                int temperature = coilMachine.getCoilType().getCoilTemperature() + (100 * Math.max(0, ((WorkableElectricMultiblockMachine) coilMachine).getTier() - GTValues.MV));
                int recipeTemp = recipe.data.getInt("ebf_temp");
                if (recipeTemp > temperature) return null;
                long recipeVoltage = (long) (RecipeHelper.getInputEUt(recipe) * OverclockingLogic.getCoilEUtDiscount(recipeTemp, temperature));
                int duration = recipe.duration;
                long maxVoltage = overclockMachine.getOverclockVoltage();
                int amountPerfectOC = Math.max(0, (temperature - recipeTemp) / 900);
                long overclockVoltage;
                int limit;
                if (machine instanceof IOverclockConfigMachine configMachine) {
                    limit = configMachine.gTOCore$getOCLimit();
                } else {
                    limit = 20;
                }
                int ocLevel = 0;
                while (duration > limit) {
                    int factor = amountPerfectOC > 0 ? 1 : 2;
                    overclockVoltage = recipeVoltage << factor;
                    if (overclockVoltage > maxVoltage) break;
                    amountPerfectOC--;
                    recipeVoltage = overclockVoltage;
                    duration >>= 1;
                    ocLevel += factor;
                }
                recipe.ocLevel = ocLevel / 2;
                recipe.duration = duration;
                recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(recipeVoltage, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            }
            return recipe;
        };
    }

    public static GTRecipe hatchParallel(MetaMachine machine, GTRecipe recipe) {
        return accurateParallel(machine, recipe, MachineUtils.getHatchParallel(machine));
    }

    public static GTRecipe accurateParallel(MetaMachine machine, GTRecipe recipe, int maxParallel) {
        int parallel = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);
        if (parallel > 1) {
            recipe = recipe.copy(ContentModifier.multiplier(parallel), false);
            recipe.parallels *= parallel;
        }
        return recipe;
    }

    public static GTRecipe recipeReduction(MetaMachine machine, GTRecipe recipe, double reductionEUt, double reductionDuration) {
        if (reductionEUt != 1) {
            recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content((long) Math.max(1, RecipeHelper.getInputEUt(recipe) * reductionEUt), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
        }
        if (reductionDuration != 1) {
            recipe.duration = (int) Math.max(1, recipe.duration * reductionDuration);
        }
        return recipe;
    }

    public static GTRecipe laserLossOverclocking(MetaMachine machine, GTRecipe recipe) {
        return overclocking(machine, recipe, false, true, false, 1, 1);
    }

    public static GTRecipe generatorOverclocking(MetaMachine machine, GTRecipe recipe) {
        return overclocking(machine, recipe, true, true, 1, 1);
    }

    public static GTRecipe perfectOverclocking(MetaMachine machine, GTRecipe recipe) {
        return overclocking(machine, recipe, true, false, 1, 1);
    }

    public static GTRecipe overclocking(MetaMachine machine, GTRecipe recipe) {
        return overclocking(machine, recipe, false, false, 1, 1);
    }

    public static GTRecipe overclocking(MetaMachine machine, GTRecipe recipe, boolean perfect, boolean generator, double reductionEUt, double reductionDuration) {
        return overclocking(machine, recipe, perfect, false, generator, reductionEUt, reductionDuration);
    }

    public static GTRecipe overclocking(MetaMachine machine, GTRecipe recipe, boolean perfect, boolean laserLoss, boolean generator, double reductionEUt, double reductionDuration) {
        if (recipe != null && machine instanceof IOverclockMachine overclockMachine) {
            long recipeVoltage = (long) ((generator ? RecipeHelper.getOutputEUt(recipe) : RecipeHelper.getInputEUt(recipe)) * reductionEUt);
            int duration = (int) (recipe.duration * reductionDuration);
            int factor = perfect ? 1 : 2;
            long maxVoltage = overclockMachine.getOverclockVoltage();
            long overclockVoltage;
            int limit;
            if (machine instanceof IOverclockConfigMachine configMachine) {
                limit = configMachine.gTOCore$getOCLimit();
            } else {
                limit = 20;
            }
            int ocLevel = 0;
            while (duration > limit) {
                overclockVoltage = recipeVoltage << factor;
                if (overclockVoltage > maxVoltage) break;
                if (laserLoss) duration = duration * 5 / 4;
                recipeVoltage = overclockVoltage;
                duration >>= 1;
                ocLevel += factor;
            }
            recipe.ocLevel = ocLevel / 2;
            recipe.duration = duration;
            List<Content> content = List.of(new Content(recipeVoltage, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null));
            if (generator) {
                recipe.tickOutputs.put(EURecipeCapability.CAP, content);
            } else {
                recipe.tickInputs.put(EURecipeCapability.CAP, content);
            }
        }
        return recipe;
    }
}
