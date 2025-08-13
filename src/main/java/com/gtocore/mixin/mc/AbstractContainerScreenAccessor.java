package com.gtocore.mixin.mc;

import net.minecraft.world.inventory.Slot;

import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {

    @Invoker
    Slot callFindSlot(double mouseX, double mouseY);
}
