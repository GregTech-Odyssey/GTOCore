package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.saved.DysonSphereSavaedData;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class DysonSphereLaunchSiloMachine extends ElectricMultiblockMachine {

    private ResourceKey<Level> dimension;

    public DysonSphereLaunchSiloMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private ResourceKey<Level> getDimension() {
        if (dimension == null) dimension = Objects.requireNonNull(getLevel()).dimension();
        return dimension;
    }

    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        int integer = GTODimensions.getPlanetDistances(getDimension().location());
        if (integer > 0) recipe.duration = recipe.duration * integer / 4;
        return recipe;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        return GTODimensions.isPlanet(getDimension().location()) && super.beforeWorking(recipe);
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        IntIntImmutablePair pair = DysonSphereSavaedData.getDimensionData(getDimension());
        if (pair.leftInt() < 10000) {
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
