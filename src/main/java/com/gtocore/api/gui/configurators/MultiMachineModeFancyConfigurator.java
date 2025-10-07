package com.gtocore.api.gui.configurators;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gtolib.api.recipe.CombinedRecipeType;

import java.util.*;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;

public class MultiMachineModeFancyConfigurator extends CustomModeFancyConfigurator {
    static ArrayList<GTRecipeType> EMPTY_LIST=new ArrayList<>(List.of(DUMMY_RECIPES));
    private final Consumer<GTRecipeType> onChange;
    ArrayList<GTRecipeType> recipeTypes;
    int mode;
    public MultiMachineModeFancyConfigurator(ArrayList<GTRecipeType> recipeTypes, GTRecipeType selected, Consumer<GTRecipeType> onChange) {
        super((recipeTypes.isEmpty()?EMPTY_LIST:recipeTypes).size());
        this.recipeTypes= (recipeTypes.isEmpty()?EMPTY_LIST:recipeTypes);
        this.onChange=onChange;
        setRecipeType(selected);
    }

    public static ArrayList<GTRecipeType> extractRecipeTypes(SortedSet<IMultiController> machines) {
        var set = new HashSet<GTRecipeType>();
        for (var machine : machines) {
            if(machine instanceof IRecipeLogicMachine rMachine){
                var gtRecipeTypes=rMachine.getRecipeTypes();
                set.addAll(Arrays.asList(gtRecipeTypes));
                for(var i:gtRecipeTypes){
                    if(i instanceof CombinedRecipeType crt){
                        set.addAll(List.of(crt.getTypes()));
                    }
                }
            }

        }
        return new ArrayList<>(set.stream().toList());
    }
    public static ArrayList<GTRecipeType> extractRecipeTypesCombined(SortedSet<IMultiController> machines){
        var list= new ArrayList<>(List.of(DUMMY_RECIPES));
        list.addAll(extractRecipeTypes(machines));
        return list;
    }
    public GTRecipeType getCurrent(){
        return recipeTypes.get(getCurrentMode());
    }

    public void setRecipeType(GTRecipeType recipe){
        if(recipeTypes.contains(recipe))setMode(recipeTypes.indexOf(recipe));
        else setMode(0);
    }
    @Override
    public void setMode(int index) {
        this.mode=index;
        onChange.accept(getCurrent());
    }

    @Override
    public int getCurrentMode() {
        return mode>modeSize?0:mode;
    }

    @Override
    public String getLanguageKey(int index) {
        return recipeTypes.get(index).registryName.toLanguageKey();
    }
}
