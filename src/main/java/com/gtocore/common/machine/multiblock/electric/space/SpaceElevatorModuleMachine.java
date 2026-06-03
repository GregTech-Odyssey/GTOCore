package com.gtocore.common.machine.multiblock.electric.space;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToLongFunction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceElevatorModuleMachine extends CustomParallelMultiblockMachine {

    @Getter
    SpaceElevatorMachine spaceElevatorMachine;

    private final boolean powerModuleTier;

    public SpaceElevatorModuleMachine(MetaMachineBlockEntity holder, boolean powerModuleTier) {
        this(holder, powerModuleTier, m -> ((SpaceElevatorModuleMachine) m).getSpaceElevatorTier() > 7 ? (int) Math.pow(((SpaceElevatorModuleMachine) m).isSuper() ? 8 : 4, ((SpaceElevatorModuleMachine) m).spaceElevatorMachine.getCasingTier(GTORecipeDataKeys.POWER_MODULE_TIER) - 1) : 0);
    }

    SpaceElevatorModuleMachine(MetaMachineBlockEntity holder, boolean powerModuleTier, ToLongFunction<CustomParallelMultiblockMachine> getParallel) {
        super(holder, false, getParallel);
        this.powerModuleTier = powerModuleTier;
    }

    public int getSpaceElevatorTier() {
        if (spaceElevatorMachine != null && spaceElevatorMachine.getRecipeLogic().isWorking()) {
            return spaceElevatorMachine.getTier();
        }
        return 0;
    }

    private boolean isSuper() {
        return spaceElevatorMachine instanceof SuperSpaceElevatorMachine;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        spaceElevatorMachine = null;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        spaceElevatorMachine = null;
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        if (getSpaceElevatorTier() < 8) return null;
        if (powerModuleTier && recipe.data.getInt(GTORecipeDataKeys.POWER_MODULE_TIER) > spaceElevatorMachine.getCasingTier(GTORecipeDataKeys.POWER_MODULE_TIER)) return null;
        recipe = ParallelLogic.accurateParallel(this, unit, recipe, getParallel());
        if (recipe == null) return null;
        return RecipeModifier.overclocking(this, unit, recipe, false, 1, getDurationMultiplier(), 0.5);
    }

    @Override
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (!super.handleTickRecipe(recipe)) return false;
        if (getOffsetTimer() % 10 == 0) {
            return getSpaceElevatorTier() >= 8;
        }
        return true;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.space_elevator." + (getSpaceElevatorTier() < 8 ? "not_" : "") + "connected"));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getDurationMultiplier())));
    }

    private double getDurationMultiplier() {
        double mul = 1;
        if (spaceElevatorMachine != null) {
            mul = spaceElevatorMachine.netMachineCache == null ? 1.0d : spaceElevatorMachine.netMachineCache.getDurationMultiplier();
        }
        return Math.sqrt(mul / ((getSpaceElevatorTier() - GTValues.ZPM) * (isSuper() ? 2 : 1)));
    }
}
