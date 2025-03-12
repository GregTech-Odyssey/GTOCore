package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.integration.emi.GTEMIPlugin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.CraftConfirmScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.crafting.CraftConfirmMenu;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftConfirmScreen.class)
public abstract class CraftConfirmScreenMixin extends AEBaseScreen<CraftConfirmMenu> {

    protected CraftConfirmScreenMixin(CraftConfirmMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var stackWithBounds = getStackUnderMouse(mouseX, mouseY);
        if (stackWithBounds != null) {
            EmiIngredient ingredient = GTEMIPlugin.genericStackToEmiIngredient(stackWithBounds.stack());
            if (button == 0) {
                EmiApi.displayRecipes(ingredient);
            } else if (button == 1) {
                EmiApi.displayUses(ingredient);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
