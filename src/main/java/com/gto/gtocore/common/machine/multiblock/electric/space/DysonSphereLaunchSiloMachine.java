package com.gto.gtocore.common.machine.multiblock.electric.space;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class DysonSphereLaunchSiloMachine extends ElectricMultiblockMachine {

    private ResourceKey<Level> dimension;

    public DysonSphereLaunchSiloMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private ResourceKey<Level> getDimension() {
        if (dimension == null) dimension = Objects.requireNonNull(getLevel()).dimension();
        return dimension;
    }

    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        Integer integer = GTODimensions.ALL_GALAXY_DIM.get(getDimension().location());
        if (integer != null) recipe.duration = recipe.duration * integer / 4;
        return recipe;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        return GTODimensions.ALL_PLANET.containsKey(getDimension().location()) && super.beforeWorking(recipe);
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        Pair<Integer, Integer> pair = DysonSphereSavaedData.getDimensionData(getDimension());
        if (pair.getFirst() < 10000) {
            if (pair.getSecond() > 60) {
                DysonSphereSavaedData.setDysonData(getDimension(), pair.getFirst(), 0);
            } else {
                DysonSphereSavaedData.setDysonData(getDimension(), pair.getFirst() + 1, pair.getSecond());
            }
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (DysonSphereSavaedData.getDimensionUse(getDimension())) textList.add(Component.translatable("gtceu.multiblock.large_miner.working"));
    }
}
