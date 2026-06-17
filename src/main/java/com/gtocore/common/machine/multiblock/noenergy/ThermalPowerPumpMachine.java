package com.gtocore.common.machine.multiblock.noenergy;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Objects;

public final class ThermalPowerPumpMachine extends NoEnergyMultiblockMachine implements ICustomRecipeLogicHolder {

    private static final Fluid STEAM = GTMaterials.Steam.getFluid();

    private int biomeModifier;

    public ThermalPowerPumpMachine(MetaMachineBlockEntity holder) {
        super(holder);
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

    @Override
    public boolean matchRecipeOutput(GTRecipe recipe) {
        return true;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (biomeModifier == 0) {
            biomeModifier = GTUtil.getPumpBiomeModifier(Objects.requireNonNull(getLevel()).getBiome(getPos()));
        } else if (biomeModifier > 0) {
            int production = (int) Math.min(unit.getFluidAmount(true, STEAM)[0], getFluidProduction());
            if (production > 0) {
                return getRecipeBuilder().duration(20).inputFluids(STEAM, production).outputFluids(Fluids.WATER, production).build();
            }
        }
        return null;
    }
}
