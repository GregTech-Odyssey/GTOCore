package com.gtocore.mixin.eae;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.emi.EmiStackHelper;
import com.glodblock.github.extendedae.client.gui.pattern.GuiPattern;
import com.glodblock.github.extendedae.container.pattern.ContainerPattern;
import dev.emi.emi.api.EmiApi;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiPattern.class)
public abstract class GuiPatternMixin<T extends ContainerPattern> extends AbstractContainerScreen<T> {

    protected GuiPatternMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (slot instanceof ContainerPattern.DisplayOnlySlot dSlot && !slot.getItem().isEmpty()) {
            var emiStack = EmiStackHelper.toEmiStack(GenericStack.fromItemStack(dSlot.getItem()));
            if (emiStack == null) {
                return;
            }
            if (mouseButton == 0) {
                // Prevent crafting empty patterns
                EmiApi.displayRecipes(emiStack);
            }
            if (mouseButton == 1) {
                // Prevent crafting empty patterns
                EmiApi.displayUses(emiStack);
            }
        }
    }
}
