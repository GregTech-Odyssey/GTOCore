package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.recipe.AsyncRecipeOutputTask;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.List;

public interface IEnhancedRecipeLogic {

    default RecipeLogic getLogic() {
        return (RecipeLogic) this;
    }

    default Object2LongOpenCustomHashMap<ItemStack> gtocore$getParallelItemMap() {
        return new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
    }

    default Object2LongOpenCustomHashMap<ItemStack> gtocore$getItemIngredientStacks() {
        return new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
    }

    default Object2LongOpenCustomHashMap<ItemStack> gtocore$getItemMap() {
        return new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
    }

    default Object2IntOpenHashMap<Ingredient> gtocore$getItemNotConsumableMap() {
        return new Object2IntOpenHashMap<>();
    }

    default Object2IntOpenHashMap<Ingredient> gtocore$getItemConsumableMap() {
        return new Object2IntOpenHashMap<>();
    }

    default Object2LongOpenHashMap<FluidStack> gtocore$getParallelFluidMap() {
        return new Object2LongOpenHashMap<>();
    }

    default Object2LongOpenHashMap<FluidStack> gtocore$getFluidIngredientStacks() {
        return new Object2LongOpenHashMap<>();
    }

    default Object2LongOpenHashMap<FluidStack> gtocore$getFluidMap() {
        return new Object2LongOpenHashMap<>();
    }

    default Object2IntOpenHashMap<FluidIngredient> gtocore$getFluidNotConsumableMap() {
        return new Object2IntOpenHashMap<>();
    }

    default Object2IntOpenHashMap<FluidIngredient> gtocore$getFluidConsumableMap() {
        return new Object2IntOpenHashMap<>();
    }

    default void gtocore$cleanParallelMap() {}

    default int gtocore$getlastParallel() {
        return 1;
    }

    default void gtocore$setModifyRecipe() {}

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

    default void gTOCore$setIdleReason(Component reason) {}

    default Component gTOCore$getIdleReason() {
        return null;
    }

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
