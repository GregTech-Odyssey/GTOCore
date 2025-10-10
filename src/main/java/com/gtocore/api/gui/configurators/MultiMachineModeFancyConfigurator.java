package com.gtocore.api.gui.configurators;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COMBINED_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.NO_FILTER;

public class MultiMachineModeFancyConfigurator extends CustomModeFancyConfigurator {

    private static final List<GTRecipeType> EMPTY_LIST = Collections.singletonList(NO_FILTER);

    private final Consumer<GTRecipeType> onChange;
    private final List<GTRecipeType> recipeTypes;
    private int currentMode;

    public MultiMachineModeFancyConfigurator(List<GTRecipeType> recipeTypes, GTRecipeType selected, Consumer<GTRecipeType> onChange) {
        super(calculateModeSize(recipeTypes, selected==COMBINED_RECIPES?NO_FILTER:selected));
        selected=selected==COMBINED_RECIPES?NO_FILTER:selected;
        this.recipeTypes = createRecipeTypeList(recipeTypes, selected);
        this.onChange = Objects.requireNonNull(onChange, "onChange consumer cannot be null");
        setRecipeType(selected);
    }

    public static List<GTRecipeType> extractRecipeTypes(SortedSet<IMultiController> machines) {
        if (machines == null || machines.isEmpty()) {
            return Collections.emptyList();
        }

        return machines.stream()
                .filter(IRecipeLogicMachine.class::isInstance)
                .map(IRecipeLogicMachine.class::cast)
                .map(IRecipeLogicMachine::getRecipeTypes)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .flatMap(recipeType -> recipeType == COMBINED_RECIPES ? Stream.empty() : Stream.of(recipeType))
                .collect(Collectors.toCollection(LinkedHashSet::new)) // 保持顺序并去重
                .stream()
                .toList();
    }

    public static List<GTRecipeType> extractRecipeTypesCombined(SortedSet<IMultiController> machines) {
        List<GTRecipeType> result = new ArrayList<>(EMPTY_LIST);
        result.addAll(extractRecipeTypes(machines));
        return Collections.unmodifiableList(result);
    }

    private static int calculateModeSize(List<GTRecipeType> recipeTypes, GTRecipeType selected) {
        int baseSize = recipeTypes.isEmpty() ? EMPTY_LIST.size() : recipeTypes.size();
        boolean needsToAddSelected = selected != null && !recipeTypes.contains(selected);
        return baseSize + (needsToAddSelected ? 1 : 0);
    }

    private static List<GTRecipeType> createRecipeTypeList(List<GTRecipeType> original, GTRecipeType selected) {
        List<GTRecipeType> result = new ArrayList<>(
                original.isEmpty() ? EMPTY_LIST : original);

        if (selected != null && !result.contains(selected)) {
            result.add(selected);
        }

        return Collections.unmodifiableList(result);
    }

    public GTRecipeType getCurrentRecipeType() {
        return recipeTypes.get(getCurrentMode());
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
        onChange.accept(getCurrentRecipeType()==NO_FILTER?COMBINED_RECIPES:getCurrentRecipeType());
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
