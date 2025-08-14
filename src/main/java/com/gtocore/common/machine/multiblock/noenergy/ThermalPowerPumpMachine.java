package com.gtocore.common.machine.multiblock.noenergy;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ThermalPowerPumpMachine extends NoEnergyMultiblockMachine {

    private static final Fluid STEAM = GTMaterials.Steam.getFluid();

    private int biomeModifier;

    public ThermalPowerPumpMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    private Recipe getRecipe() {
        if (biomeModifier == 0) {
            biomeModifier = GTUtil.getPumpBiomeModifier(Objects.requireNonNull(getLevel()).getBiome(getPos()));
        } else if (biomeModifier > 0) {
            int production = (int) Math.min(getFluidAmount(STEAM)[0], getFluidProduction());
            if (production > 0) {
                Recipe recipe = getRecipeBuilder().duration(20).inputFluids(new FluidStack(STEAM, production)).outputFluids(new FluidStack(Fluids.WATER, production)).buildRawRecipe();
                if (RecipeRunner.matchRecipe(this, recipe)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    private boolean isRainingInBiome() {
        if (!Objects.requireNonNull(getLevel()).isRaining()) return false;
        return Objects.requireNonNull(getLevel()).getBiome(getPos()).value().getPrecipitationAt(getPos()) != Biome.Precipitation.NONE;
    }

    private int getFluidProduction() {
        int value = biomeModifier << 8;
        if (isRainingInBiome()) {
            value = value * 3 / 2;
        }
        return value;
    }
}
