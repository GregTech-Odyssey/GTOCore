package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.saved.DysonSphereSavaedData;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class DysonSphereReceivingStationMcahine extends ElectricMultiblockMachine implements ICustomRecipeLogicHolder {

    private ResourceKey<Level> dimension;

    public DysonSphereReceivingStationMcahine(MetaMachineBlockEntity holder) {
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
    public void beforeWorking(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        DysonSphereSavaedData.setDysonUse(getDimension(), true);
        super.beforeWorking(unit, recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        DysonSphereSavaedData.setDysonUse(getDimension(), false);
        applyDamage();
        updateSignal();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        DysonSphereSavaedData.setDysonUse(getDimension(), false);
        updateSignal();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        IntIntImmutablePair pair = DysonSphereSavaedData.getDimensionData(getDimension());
        textList.add(Component.translatable("gtocore.machine.dyson_sphere.amount", pair.leftInt()));
        textList.add(Component.translatable("gtocore.machine.dyson_sphere.voltage", (pair.leftInt() > 0 ? getOverclockVoltage() : 0)));
        textList.add(Component.translatable("gtocore.machine.fission_reactor.damaged", pair.rightInt()).append("%"));
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (DysonSphereSavaedData.getDimensionUse(getDimension())) return null;
        IntIntImmutablePair pair = DysonSphereSavaedData.getDimensionData(getDimension());
        if (pair.leftInt() < 1) return null;
        int integer = GTODimensions.getPlanetDistances(getDimension());
        if (integer == 0) return null;
        return getRecipeBuilder().duration(20)
                .CWUt(Math.max(1, pair.leftInt() * integer / 10))
                .EUt(-GTValues.V[GTValues.MAX] * pair.leftInt() * (50 - Math.max(0, pair.rightInt() - 60)) / 50)
                .inputFluids(GTOFluids.GELID_CRYOTHEUM.get(), Math.max(1, (int) Math.sqrt(pair.leftInt())))
                .build();
    }

    @Override
    public long requestCWU(long cwut, boolean simulate) {
        var recipeLogic = getRecipeLogic();
        if (simulate && recipeLogic.isWorking() && recipeLogic.getProgress() >= recipeLogic.getMaxProgress()) {
            return cwut;
        }
        return getComputationProvider().requestCWU(cwut, simulate);
    }

    private void applyDamage() {
        IntIntImmutablePair pair = DysonSphereSavaedData.getDimensionData(getDimension());
        if (GTValues.RNG.nextFloat() >= 0.01F * (1 + (double) pair.leftInt() / 128)) {
            return;
        }

        if (pair.rightInt() > 99) {
            int count = pair.leftInt() - 1;
            if (count < 1) {
                DysonSphereSavaedData.setDysonData(getDimension(), 0, 0);
            } else {
                DysonSphereSavaedData.setDysonData(getDimension(), count, 0);
            }
        } else {
            DysonSphereSavaedData.setDysonData(getDimension(), pair.leftInt(), pair.rightInt() + 1);
        }
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (!isFormed() || side != getFrontFacing().getOpposite()) {
            return 0;
        }
        int damage = DysonSphereSavaedData.getDimensionData(getDimension()).rightInt();
        if (damage <= 60) {
            return 0;
        }
        return Math.min(15, (15 * (damage - 60) + 39) / 40);
    }

    @Override
    public boolean canConnectRedstone(@NotNull Direction side) {
        return side == getFrontFacing();
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }
}
