package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.recipe.AsyncRecipeOutputTask;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IEnhancedRecipeLogic {

    default RecipeLogic getLogic() {
        return (RecipeLogic) this;
    }

    default boolean gtocore$hasAsyncTask() {
        return false;
    }

    default AsyncRecipeSearchTask gtocore$createAsyncRecipeSearchTask() {
        return new AsyncRecipeSearchTask(getLogic());
    }

    default AsyncRecipeSearchTask gtocore$getAsyncRecipeSearchTask() {
        return null;
    }

    default AsyncRecipeOutputTask gtocore$getAsyncRecipeOutputTask() {
        return null;
    }

    default void gtocore$setAsyncRecipeSearchTask(AsyncRecipeSearchTask task) {}

    default void gtocore$setAsyncRecipeOutputTask(AsyncRecipeOutputTask task) {}

    default boolean canLockRecipe() {
        return getLogic().getClass() == RecipeLogic.class;
    }

    default boolean gTOCore$isLockRecipe() {
        return false;
    }

    default void gTOCore$setLockRecipe(boolean lockRecipe) {}

    static void attachRecipeLockable(ConfiguratorPanel configuratorPanel, RecipeLogic logic) {
        if (logic instanceof IEnhancedRecipeLogic lockableRecipe && lockableRecipe.canLockRecipe()) {
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GuiTextures.BUTTON_PUBLIC_PRIVATE.getSubTexture(0, 0, 1, 0.5),
                    GuiTextures.BUTTON_PUBLIC_PRIVATE.getSubTexture(0, 0.5, 1, 0.5),
                    lockableRecipe::gTOCore$isLockRecipe, (clickData, pressed) -> lockableRecipe.gTOCore$setLockRecipe(pressed))
                    .setTooltipsSupplier(pressed -> List.of(Component.translatable("config.gtceu.option.recipes").append("[").append(Component.translatable(pressed ? "theoneprobe.ae2.locked" : "theoneprobe.ae2.unlocked").append("]")))));
        }
    }
}
