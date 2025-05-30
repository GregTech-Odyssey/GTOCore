package com.gto.gtocore.api.capability.recipe;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.SerializerInteger;

import com.google.common.primitives.Ints;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ManaRecipeCapability extends RecipeCapability<Integer> {

    public final static ManaRecipeCapability CAP = new ManaRecipeCapability();

    private ManaRecipeCapability() {
        super("mana", 0xFF00B1FF, false, 3, SerializerInteger.INSTANCE);
    }

    @Override
    public Integer copyInner(Integer content) {
        return content;
    }

    @Override
    public Integer copyWithModifier(Integer content, ContentModifier modifier) {
        return modifier.apply(content);
    }

    @Override
    public void addXEIInfo(WidgetGroup group, int xOffset, GTRecipe recipe, List<Content> contents, boolean perTick, boolean isInput, MutableInt yOffset) {
        if (perTick) {
            int mana = contents.stream().map(Content::getContent).mapToInt(CAP::of).sum();
            group.addWidget(new LabelWidget(3 - xOffset, yOffset.addAndGet(10),
                    LocalizationUtils.format(isInput ? "gtocore.recipe.nama_in" : "gtocore.recipe.nama_out", mana)));
        }
    }

    @Override
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        int maxMana = 0;
        List<IRecipeHandler<?>> recipeHandlerList = Objects
                .requireNonNullElseGet(holder.getCapabilitiesProxy().get(IO.IN, CAP), Collections::<IRecipeHandler<?>>emptyList)
                .stream()
                .filter(handler -> !handler.isProxy()).toList();
        for (IRecipeHandler<?> container : recipeHandlerList) {
            if (container.getContents() instanceof IManaContainer manaContainer) {
                maxMana += manaContainer.getMaxConsumption();
            }
        }
        int recipeMana = CAP.of(recipe.tickInputs.get(CAP).get(0).getContent());
        if (recipeMana == 0) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(Ints.saturatedCast(maxMana / recipeMana));
    }
}
