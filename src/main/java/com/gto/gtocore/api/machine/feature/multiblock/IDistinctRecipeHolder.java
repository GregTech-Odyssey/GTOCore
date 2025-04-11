package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public interface IDistinctRecipeHolder extends IRecipeLogicMachine {

    default boolean isDistinctState() {
        return false;
    }

    default ResourceLocation getRecipeId() {
        return null;
    }

    default RecipeHandlerList getCurrentDistinct() {
        return null;
    }

    default List<RecipeHandlerList> getDistinct() {
        return List.of();
    }

    default List<RecipeHandlerList> getIndistinct() {
        return List.of();
    }

    default void setRecipeId(ResourceLocation id) {}

    default void setCurrentDistinct(RecipeHandlerList list) {}

    default void setDistinct(List<RecipeHandlerList> list) {}

    default void setIndistinct(List<RecipeHandlerList> list) {}

    default void setDistinctState(boolean distinctState) {}

    default void arrangeDistinct() {
        setCurrentDistinct(null);
        setRecipeId(null);
        var handlers = getCapabilitiesProxy().get(IO.IN);
        List<RecipeHandlerList> distinct = new ObjectArrayList<>();
        List<RecipeHandlerList> indistinct = new ObjectArrayList<>();
        for (var handler : handlers) {
            if (handler.isDistinct()) distinct.add(handler);
            else indistinct.add(handler);
        }
        setDistinct(distinct);
        setIndistinct(indistinct);
    }
}
