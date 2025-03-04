package com.gto.gtocore.common.machine.multiblock.electric.space;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.data.GTOFluids;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class DysonSphereReceivingStationMcahine extends ElectricMultiblockMachine {

    private ResourceKey<Level> dimension;

    public DysonSphereReceivingStationMcahine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private ResourceKey<Level> getDimension() {
        if (dimension == null) dimension = Objects.requireNonNull(getLevel()).dimension();
        return dimension;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        DysonSphereSavaedData.setDysonUse(getDimension(), true);
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        DysonSphereSavaedData.setDysonUse(getDimension(), false);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        DysonSphereSavaedData.setDysonUse(getDimension(), false);
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            if (DysonSphereSavaedData.getDimensionUse(getDimension())) return null;
            Pair<Integer, Integer> pair = DysonSphereSavaedData.getDimensionData(getDimension());
            if (pair.getFirst() < 1) return null;
            if (GTValues.RNG.nextFloat() < 0.01F * (1 + (double) pair.getFirst() / 128)) {
                if (pair.getSecond() > 99) {
                    int count = pair.getFirst() - 1;
                    if (count < 1) {
                        DysonSphereSavaedData.setDysonData(getDimension(), 0, 0);
                        return null;
                    }
                    DysonSphereSavaedData.setDysonData(getDimension(), count, 0);
                } else {
                    DysonSphereSavaedData.setDysonData(getDimension(), pair.getFirst(), pair.getSecond() + 1);
                }
            }
            Integer integer = GTODimensions.ALL_GALAXY_DIM.get(getDimension().location());
            if (integer == null) return null;
            GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(20)
                    .CWUt(Math.max(1, pair.getFirst() * integer / 2))
                    .EUt(-GTValues.V[GTValues.MAX] * pair.getFirst() * (50 - Math.max(0, pair.getSecond() - 60)) / 50)
                    .inputFluids(new FluidStack(GTOFluids.GELID_CRYOTHEUM.get(), Math.max(1, (int) Math.sqrt(pair.getFirst()))))
                    .buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe) && RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
            return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        Pair<Integer, Integer> pair = DysonSphereSavaedData.getDimensionData(getDimension());
        textList.add(Component.translatable("gtocore.machine.dyson_sphere.number", pair.getFirst()));
        textList.add(Component.translatable("gtocore.machine.dyson_sphere.voltage", (pair.getFirst() > 0 ? getOverclockVoltage() : 0)));
        textList.add(Component.translatable("gtocore.machine.fission_reactor.damaged", pair.getSecond()).append("%"));
    }
}
