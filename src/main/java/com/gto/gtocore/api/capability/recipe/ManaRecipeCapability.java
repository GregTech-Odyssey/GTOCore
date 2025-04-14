package com.gto.gtocore.api.capability.recipe;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.SerializerLong;

import com.google.common.primitives.Ints;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public final class ManaRecipeCapability extends RecipeCapability<Long> {

    public final static ManaRecipeCapability CAP = new ManaRecipeCapability();

    private ManaRecipeCapability() {
        super("mana", 0xFF00B1FF, false, 3, SerializerLong.INSTANCE);
    }

    @Override
    public Long copyInner(Long content) {
        return content;
    }

    @Override
    public Long copyWithModifier(Long content, ContentModifier modifier) {
        return modifier.apply(content);
    }

    @Override
    public void addXEIInfo(WidgetGroup group, int xOffset, GTRecipe recipe, List<Content> contents, boolean perTick, boolean isInput, MutableInt yOffset) {
        long mana = contents.stream().map(Content::getContent).mapToLong(CAP::of).sum();
        group.addWidget(new LabelWidget(3 - xOffset, yOffset.addAndGet(10),
                LocalizationUtils.format(isInput ? "gtocore.machine.mana_input" : "gtocore.machine.mana_output", mana) + (perTick ? " /t" : "")));
        if (perTick) {
            group.addWidget(new LabelWidget(3 - xOffset, yOffset.addAndGet(10),
                    LocalizationUtils.format("gtocore.machine.mana_stored", mana * recipe.duration)));
        }
    }

    @Override
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        long maxManaConsumptionRate = 0;
        for (IRecipeHandler<?> container : holder.getCapabilitiesFlat(IO.IN, CAP)) {
            if (container instanceof IManaContainer manaContainer) {
                maxManaConsumptionRate += manaContainer.getMaxConsumptionRate();
            }
        }
        long recipeMana = CAP.of(recipe.tickInputs.get(CAP).get(0).getContent());
        if (recipeMana == 0) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(Ints.saturatedCast(maxManaConsumptionRate / recipeMana));
    }
}
