package com.gtocore.api.gui.configurators;

import com.gtolib.api.recipe.CombinedRecipeType;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import java.util.*;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COMBINED_RECIPES;

public class MultiMachineModeFancyConfigurator extends CustomModeFancyConfigurator {

    static ArrayList<GTRecipeType> EMPTY_LIST = new ArrayList<>(List.of(COMBINED_RECIPES));
    private final Consumer<GTRecipeType> onChange;
    ArrayList<GTRecipeType> recipeTypes;
    int mode;

    public MultiMachineModeFancyConfigurator(ArrayList<GTRecipeType> recipeTypes, GTRecipeType selected, Consumer<GTRecipeType> onChange) {
        super((recipeTypes.isEmpty() ? EMPTY_LIST : recipeTypes).size() + (recipeTypes.contains(selected) ? 0 : 1));
        this.recipeTypes = (recipeTypes.isEmpty() ? EMPTY_LIST : (ArrayList<GTRecipeType>) recipeTypes.clone());
        this.onChange = onChange;
        if (!this.recipeTypes.contains(selected)) {
            this.recipeTypes.add(selected);
        }
        setRecipeType(selected);
    }

    public static ArrayList<GTRecipeType> extractRecipeTypes(SortedSet<IMultiController> machines) {
        var set = new HashSet<GTRecipeType>();
        for (var machine : machines) {
            if (machine instanceof IRecipeLogicMachine rMachine) {
                var gtRecipeTypes = rMachine.getRecipeTypes();
                for (var i : gtRecipeTypes) {
                    if (i instanceof CombinedRecipeType crt) {
                        set.addAll(List.of(crt.getTypes()));
                    }
                    if (i != COMBINED_RECIPES)
                        set.add(i);
                }
            }

        }
        return new ArrayList<>(set.stream().toList());
    }

    public static ArrayList<GTRecipeType> extractRecipeTypesCombined(SortedSet<IMultiController> machines) {
        var list = (ArrayList<GTRecipeType>) EMPTY_LIST.clone();
        list.addAll(extractRecipeTypes(machines));
        return list;
    }

    public GTRecipeType getCurrent() {
        return recipeTypes.get(getCurrentMode());
    }

    public void setRecipeType(GTRecipeType recipe) {
        if (recipeTypes.contains(recipe)) setMode(recipeTypes.indexOf(recipe));
    }

    @Override
    public void setMode(int index) {
        this.mode = index;
        onChange.accept(getCurrent());
    }

    @Override
    public int getCurrentMode() {
        return mode > modeSize ? 0 : mode;
    }

    @Override
    public String getLanguageKey(int index) {
        return recipeTypes.get(index).registryName.toLanguageKey();
    }
}
