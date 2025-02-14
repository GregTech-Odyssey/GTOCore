package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface ILockableRecipe {

    default RecipeLogic getLogic() {
        return (RecipeLogic) this;
    }

    default boolean canLockRecipe() {
        return getLogic().getClass() == RecipeLogic.class;
    }

    default boolean gTOCore$isLockRecipe() {
        return false;
    }

    default void gTOCore$setLockRecipe(boolean lockRecipe) {}

    static void attachRecipeLockable(ConfiguratorPanel configuratorPanel, RecipeLogic logic) {
        if (logic instanceof ILockableRecipe lockableRecipe && lockableRecipe.canLockRecipe()) {
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GuiTextures.BUTTON_PUBLIC_PRIVATE.getSubTexture(0, 0, 1, 0.5),
                    GuiTextures.BUTTON_PUBLIC_PRIVATE.getSubTexture(0, 0.5, 1, 0.5),
                    lockableRecipe::gTOCore$isLockRecipe, (clickData, pressed) -> lockableRecipe.gTOCore$setLockRecipe(pressed))
                    .setTooltipsSupplier(pressed -> List.of(Component.translatable("config.gtceu.option.recipes").append("[").append(Component.translatable(pressed ? "theoneprobe.ae2.locked" : "theoneprobe.ae2.unlocked").append("]")))));
        }
    }
}
