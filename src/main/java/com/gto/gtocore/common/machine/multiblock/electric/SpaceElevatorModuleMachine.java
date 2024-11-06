package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.IParallelMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceElevatorModuleMachine extends WorkableElectricMultiblockMachine implements IParallelMachine {

    SpaceElevatorMachine spaceElevatorMachine = null;

    private final boolean power_module_tier;

    public SpaceElevatorModuleMachine(IMachineBlockEntity holder, boolean power_module_tier, Object... args) {
        super(holder, args);
        this.power_module_tier = power_module_tier;
    }

    private int getSpaceElevatorTier() {
        if (spaceElevatorMachine != null && spaceElevatorMachine.getRecipeLogic().isWorking()) {
            return spaceElevatorMachine.getTier();
        }
        return 0;
    }

    private boolean isSuper() {
        return spaceElevatorMachine instanceof SuperSpaceElevatorMachine;
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof SpaceElevatorModuleMachine spaceElevatorModuleMachine) {
            if (spaceElevatorModuleMachine.getSpaceElevatorTier() < 8) {
                return null;
            }
            if (spaceElevatorModuleMachine.power_module_tier && recipe.data.getInt("power_module_tier") > spaceElevatorModuleMachine.spaceElevatorMachine.getCasingTiers().get("power_module_tier")) {
                return null;
            }
            GTRecipe recipe1 = GTORecipeModifiers.reduction(machine, recipe, 1, spaceElevatorModuleMachine.getDurationMultiplier());
            if (recipe1 != null) {
                recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe1, spaceElevatorModuleMachine.getParallel(), false).getFirst();
                if (recipe1 != null) return RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1, spaceElevatorModuleMachine.getOverclockVoltage(), params, result);
            }
        }
        return null;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getOffsetTimer() % 10 == 0) {
            if (getSpaceElevatorTier() < 8) {
                return false;
            }
        }
        return value;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        textList.add(Component.translatable("gtceu.multiblock.parallel", Component.literal(FormattingUtil.formatNumbers(getParallel())).withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtocore.machine.space_elevator." + (getSpaceElevatorTier() < 8 ? "not_" : "") + "connected"));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getDurationMultiplier())));
    }

    private double getDurationMultiplier() {
        return Math.sqrt(1D / ((getSpaceElevatorTier() - 1) * (isSuper() ? 2 : 1)));
    }

    @Override
    public int getParallel() {
        return getSpaceElevatorTier() > 7 ? (int) Math.pow(isSuper() ? 8 : 4, spaceElevatorMachine.getCasingTiers().get("power_module_tier") - 1) : 0;
    }
}
