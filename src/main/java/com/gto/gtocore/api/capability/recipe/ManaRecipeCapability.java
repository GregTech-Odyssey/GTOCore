package com.gto.gtocore.api.capability.recipe;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.SerializerLong;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import com.google.common.primitives.Ints;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ManaRecipeCapability extends RecipeCapability<Long> {

    public final static ManaRecipeCapability CAP = new ManaRecipeCapability();

    protected ManaRecipeCapability() {
        super("mana", 0xFFEEEE00, false, 3, SerializerLong.INSTANCE);
    }

    @Override
    public Long copyInner(Long content) {
        return content;
    }

    @Override
    public Long copyWithModifier(Long content, ContentModifier modifier) {
        return modifier.apply(content).longValue();
    }

    @Override
    public void addXEIInfo(WidgetGroup group, int xOffset, GTRecipe recipe, List<Content> contents, boolean perTick,
                           boolean isInput, MutableInt yOffset) {
        if (perTick) {
            long mana = contents.stream().map(Content::getContent).mapToLong(ManaRecipeCapability.CAP::of).sum();
            group.addWidget(new LabelWidget(3 - xOffset, yOffset.addAndGet(10),
                    LocalizationUtils.format("gtocore.recipe.nama_per_tick", isInput ? ("- " + mana) : ("+ " + mana))));
        }
    }

    @Override
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        long maxMana = 0;
        List<IRecipeHandler<?>> recipeHandlerList = Objects
                .requireNonNullElseGet(holder.getCapabilitiesProxy().get(IO.IN, ManaRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)
                .stream()
                .filter(handler -> !handler.isProxy()).toList();
        for (IRecipeHandler<?> container : recipeHandlerList) {
            if (container.getContents() instanceof IManaContainer manaContainer) {
                maxMana += manaContainer.getMaxConsumption();
            }
        }
        long recipeMana = ManaRecipeCapability.CAP.of(recipe.tickInputs.get(ManaRecipeCapability.CAP).get(0).getContent());
        if (recipeMana == 0) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(Ints.saturatedCast(maxMana / recipeMana));
    }
}
