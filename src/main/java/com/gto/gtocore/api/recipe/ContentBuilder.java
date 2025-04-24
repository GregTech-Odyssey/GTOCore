package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class ContentBuilder {

    private final boolean list;

    private ContentBuilder(boolean list) {
        this.list = list;
        if (list) {
            content = new ArrayList<>();
        }
    }

    public static Content builderLong(long number) {
        return new Content(number, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0);
    }

    public static ContentBuilder create() {
        return new ContentBuilder(false);
    }

    public static ContentBuilder createList() {
        return new ContentBuilder(true);
    }

    private Object content;
    @Setter
    @Accessors(chain = true, fluent = true)
    private int chance = ChanceLogic.getMaxChancedValue();
    @Setter
    @Accessors(chain = true, fluent = true)
    private int maxChance = ChanceLogic.getMaxChancedValue();
    @Setter
    @Accessors(chain = true, fluent = true)
    private int tierChanceBoost = 0;

    public ContentBuilder EU(long eu) {
        if (list) {
            ((List<Object>) content).add(eu);
        }
        content = eu;
        return this;
    }

    public ContentBuilder items(ItemStack... item) {
        for (ItemStack i : item) {
            ((List<Object>) content).add(FastSizedIngredient.of(i));
        }
        return this;
    }

    public ContentBuilder item(ItemStack item) {
        Object object = FastSizedIngredient.of(item);
        if (list) {
            ((List<Object>) content).add(object);
        } else {
            content = object;
        }
        return this;
    }

    public ContentBuilder fluids(FluidStack... fluid) {
        for (FluidStack f : fluid) {
            ((List<Object>) content).add(FluidIngredient.of(f));
        }
        return this;
    }

    public ContentBuilder fluid(FluidStack fluid) {
        Object object = FluidIngredient.of(fluid);
        if (list) {
            ((List<Object>) content).add(object);
        } else {
            content = object;
        }
        return this;
    }

    public Content builder() {
        return new Content(content, chance, maxChance, tierChanceBoost);
    }

    public List<Content> buildList() {
        return ((List<Object>) content).stream().map(c -> new Content(c, chance, maxChance, tierChanceBoost)).toList();
    }
}
