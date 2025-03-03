package com.gto.gtocore.integration.emi;

import com.gregtechceu.gtceu.utils.SupplierMemoizer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.tags.TagKey;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;

import java.util.List;
import java.util.function.Supplier;

public final class TagEmiIngredientSupplier extends TagEmiIngredient {

    private final Supplier<TagEmiIngredient> supplier;

    public TagEmiIngredientSupplier(TagKey<?> key, Supplier<TagEmiIngredient> supplier) {
        super(key, List.of(), 0);
        this.supplier = SupplierMemoizer.memoize(supplier);
    }

    @Override
    public List<EmiStack> getEmiStacks() {
        return supplier.get().getEmiStacks();
    }

    @Override
    public boolean isEmpty() {
        return supplier.get().isEmpty();
    }

    @Override
    public EmiIngredient copy() {
        return supplier.get().copy();
    }

    @Override
    public long getAmount() {
        return supplier.get().getAmount();
    }

    @Override
    public EmiIngredient setAmount(long amount) {
        return supplier.get().setAmount(amount);
    }

    @Override
    public float getChance() {
        return supplier.get().getChance();
    }

    @Override
    public EmiIngredient setChance(float chance) {
        return supplier.get().setChance(chance);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        supplier.get().render(draw, x, y, delta);
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
        supplier.get().render(draw, x, y, delta, flags);
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        return supplier.get().getTooltip();
    }
}
