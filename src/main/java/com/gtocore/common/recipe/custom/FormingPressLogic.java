package com.gtocore.common.recipe.custom;

import com.gtolib.api.machine.feature.multiblock.IExtendedRecipeCapabilityHolder;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class FormingPressLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final class RecipeData {

        private final RecipeBuilder recipeBuilder;

        private ItemStack mold = ItemStack.EMPTY;
        private ItemStack item = ItemStack.EMPTY;

        private RecipeData(RecipeBuilder recipeBuilder) {
            this.recipeBuilder = recipeBuilder;
        }

        private boolean found() {
            return !mold.isEmpty() && !item.isEmpty();
        }

        private Recipe buildRecipe() {
            ItemStack output = item.copyWithCount(1);
            CompoundTag compoundtag = mold.getTagElement("display");
            if (compoundtag != null && compoundtag.contains("Name", 8)) {
                try {
                    output.getOrCreateTagElement("display").putString("Name", compoundtag.getString("Name"));
                } catch (Exception var3) {
                    compoundtag.remove("Name");
                    return null;
                }
            }
            return recipeBuilder.notConsumable(mold)
                    .inputItems(item.copyWithCount(1))
                    .outputItems(output)
                    .duration(40).EUt(4)
                    .buildRawRecipe();
        }
    }

    @Override
    public @Nullable GTRecipe createCustomRecipe(IRecipeCapabilityHolder h) {
        if (h instanceof IRecipeLogicMachine recipeLogicMachine) {
            RecipeData data = new RecipeData(IEnhancedRecipeLogic.of(recipeLogicMachine.getRecipeLogic()).gtolib$getRecipeBuilder());
            if (h instanceof IExtendedRecipeCapabilityHolder holder) {
                return collect(data, holder.gtolib$getInput(), holder);
            } else {
                return collect(data, recipeLogicMachine.getCapabilitiesForIO(IO.IN), h);
            }
        }
        return null;
    }

    private static Recipe collect(RecipeData data, List<RecipeHandlerList> rhls, IRecipeCapabilityHolder h) {
        ObjectHolder<Recipe> recipeObjectHolder = new ObjectHolder<>(null);
        l:
        for (var rhl : rhls) {
            data.mold = ItemStack.EMPTY;
            data.item = ItemStack.EMPTY;
            var handlers = rhl.getCapability(ItemRecipeCapability.CAP);
            if (handlers.isEmpty()) continue;
            for (var handler : handlers) {
                if (!handler.shouldSearchContent()) continue;
                if (handler.forEachItems((stack, amount) -> {
                    boolean isMold = GTItems.SHAPE_MOLD_NAME.isIn(stack);
                    if (isMold && data.mold.isEmpty() && stack.hasCustomHoverName()) {
                        data.mold = stack;
                    } else if (!isMold && data.item.isEmpty() && !stack.hasCustomHoverName()) {
                        data.item = stack;
                    }
                    if (data.found()) {
                        var recipe = data.buildRecipe();
                        if (recipe != null) {
                            h.setCurrentHandlerList(rhl, null);
                            recipeObjectHolder.value = recipe;
                            return true;
                        }
                    }
                    return false;
                })) {
                    break l;
                }
            }
        }
        return recipeObjectHolder.value;
    }

    @Override
    public void buildRepresentativeRecipes() {
        ItemStack press = GTItems.SHAPE_MOLD_NAME.asStack();
        press.setHoverName(Component.translatable("gtceu.forming_press.naming.press"));
        ItemStack toName = new ItemStack(Items.NAME_TAG);
        toName.setHoverName(Component.translatable("gtceu.forming_press.naming.to_name"));
        ItemStack named = new ItemStack(Items.NAME_TAG);
        named.setHoverName(Component.translatable("gtceu.forming_press.naming.named"));
        GTRecipe recipe = GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder("name_item")
                .notConsumable(press)
                .inputItems(toName)
                .outputItems(named)
                .duration(40)
                .EUt(4)
                .buildRawRecipe();
        recipe.setId(recipe.getId().withPrefix("/"));
        GTRecipeTypes.FORMING_PRESS_RECIPES.addToMainCategory(recipe);
    }
}
