package com.gtocore.api.gui.configurators;

import com.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class MultiMachineModeFancyConfigurator extends CustomModeFancyConfigurator {

    private static final List<GTRecipeType> EMPTY_LIST = Collections.singletonList(GTORecipeTypes.HATCH_COMBINED);

    private final Consumer<GTRecipeType> onChange;
    private final List<GTRecipeType> recipeTypes;
    private int currentMode;

    public MultiMachineModeFancyConfigurator(List<GTRecipeType> recipeTypes, GTRecipeType selected, Consumer<GTRecipeType> onChange) {
        super(calculateModeSize(recipeTypes, selected));
        this.recipeTypes = createRecipeTypeList(recipeTypes, selected);
        this.onChange = Objects.requireNonNull(onChange, "onChange consumer cannot be null");
        setRecipeType(selected == null ? GTORecipeTypes.HATCH_COMBINED : selected);
    }

    public static List<GTRecipeType> extractRecipeTypes(@Nullable IMultiController controller) {
        if (!(controller instanceof IRecipeLogicMachine machine)) {
            return Collections.emptyList();
        }
        return Arrays.asList(machine.getAvailableRecipeTypes());
    }

    public static void verify(Collection<GTRecipeType> recipeTypes, GTRecipeType currentRecipeType, Runnable clearOperation) {
        if (currentRecipeType == null || recipeTypes.contains(currentRecipeType)) return;
        clearOperation.run();
    }

    private static int calculateModeSize(List<GTRecipeType> recipeTypes, GTRecipeType selected) {
        if (recipeTypes.isEmpty()) return 1;
        return recipeTypes.size() + (selected == null || recipeTypes.contains(selected) ? 1 : 2);
    }

    private static List<GTRecipeType> createRecipeTypeList(List<GTRecipeType> original, GTRecipeType selected) {
        if (original.isEmpty()) return EMPTY_LIST;
        List<GTRecipeType> result = new ArrayList<>(original);
        result.add(GTORecipeTypes.HATCH_COMBINED);
        if (selected != null && !result.contains(selected)) {
            result.add(selected);
        }
        return result;
    }

    private GTRecipeType getCurrentRecipeType() {
        return recipeTypes.get(currentMode);
    }

    public void setRecipeType(GTRecipeType recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe type cannot be null");
        }

        int index = recipeTypes.indexOf(recipe);
        if (index >= 0) {
            setMode(index);
        }
    }

    @Override
    public void setMode(int index) {
        if (index < 0 || index >= recipeTypes.size()) {
            throw new IllegalArgumentException("Mode index out of bounds: " + index);
        }

        this.currentMode = index;
        onChange.accept(getCurrentRecipeType() == GTORecipeTypes.HATCH_COMBINED ? null : getCurrentRecipeType());
    }

    // ============ 私有辅助方法 ============

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public String getLanguageKey(int index) {
        if (index < 0 || index >= recipeTypes.size()) {
            return getLanguageKey(0);
        }

        GTRecipeType recipeType = recipeTypes.get(index);
        if (recipeType == null) {
            // 如果遇到意外的null，重置到安全模式
            return getLanguageKey(0);
        }

        return recipeType.registryName.toLanguageKey();
    }
}
