package com.gtocore.integration.emi.stack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import dev.emi.emi.api.stack.EmiStack;

import java.util.List;

public class EmiSearchTextStack extends EmiStack {

    int hash;

    @Override
    public EmiStack copy() {
        return null;
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta, int flags) {}

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public CompoundTag getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public List<Component> getTooltipText() {
        return List.of();
    }

    @Override
    public Component getName() {
        return null;
    }
}
