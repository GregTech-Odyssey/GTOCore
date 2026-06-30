package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.saved.DysonSphereSavaedData;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class DysonSphereLaunchSiloMachine extends ElectricMultiblockMachine {

    private ResourceKey<Level> dimension;

    public DysonSphereLaunchSiloMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private ResourceKey<Level> getDimension() {
        if (dimension == null) {
            var currentDimension = Objects.requireNonNull(getLevel()).dimension();
            dimension = GTODimensions.isOverworld(currentDimension) ? Level.OVERWORLD : currentDimension;
        }
        return dimension;
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        if (!GTODimensions.isPlanet(getDimension())) return null;
        int integer = GTODimensions.getPlanetDistances(getDimension());
        if (integer > 0) recipe.duration = recipe.duration * integer / 4;
        return recipe;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        IntIntImmutablePair pair = DysonSphereSavaedData.getDimensionData(getDimension());
        if (pair.leftInt() < 100000) {
            if (pair.rightInt() > 60) {
                DysonSphereSavaedData.setDysonData(getDimension(), pair.leftInt(), 0);
            } else {
                DysonSphereSavaedData.setDysonData(getDimension(), pair.leftInt() + 1, pair.rightInt());
            }
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (DysonSphereSavaedData.getDimensionUse(getDimension())) textList.add(Component.translatable("gtceu.multiblock.large_miner.working"));
    }
}
