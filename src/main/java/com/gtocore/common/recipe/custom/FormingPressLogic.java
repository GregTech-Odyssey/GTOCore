package com.gtocore.common.recipe.custom;

import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEItems;
import appeng.items.materials.NamePressItem;

import com.gto.datasynclib.util.holder.ObjHolder;
import org.jetbrains.annotations.Nullable;

public final class FormingPressLogic implements GTRecipeType.ICustomRecipeLogic {

    private static final class RecipeData {

        private final RecipeBuilder recipeBuilder = RecipeBuilder.ofRaw();

        private ItemStack mold = ItemStack.EMPTY;
        private ItemStack item = ItemStack.EMPTY;
        private boolean ae;

        private boolean found() {
            return !mold.isEmpty() && !item.isEmpty();
        }

        private GTRecipeDefinition buildRecipe() {
            var output = item.copyWithCount(1);
            String name;
            var c = mold.getTag();
            if (c == null) return null;
            if (ae) {
                if (c.get(NamePressItem.TAG_INSCRIBE_NAME) instanceof StringTag stringTag) {
                    name = stringTag.getAsString();
                } else {
                    return null;
                }
            } else {
                if (c.get("display") instanceof CompoundTag compoundtag && compoundtag.get("Name") instanceof StringTag stringTag) {
                    name = stringTag.getAsString();
                } else {
                    return null;
                }
            }
            output.getOrCreateTagElement("display").putString("Name", name);
            return recipeBuilder.notConsumable(mold.getItem())
                    .inputItems(item.copyWithCount(1))
                    .outputItems(output)
                    .duration(40).EUt(4)
                    .build();
        }
    }

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(IRecipeHandlerHolder h, RecipeHandlerUnit u) {
        RecipeData data = new RecipeData();
        ObjHolder<GTRecipeDefinition> recipeObjectHolder = new ObjHolder<>();
        data.mold = ItemStack.EMPTY;
        data.item = ItemStack.EMPTY;
        u.forEachItems(false, (stack, amount) -> {
            var item = stack.getItem();
            var ae = AEItems.NAME_PRESS.asItem() == item;
            var isMold = ae || GTItems.SHAPE_MOLD_NAME.is(item);
            if (isMold && data.mold.isEmpty() && stack.hasTag()) {
                data.mold = stack;
                data.ae = ae;
            } else if (!isMold && data.item.isEmpty() && !stack.hasCustomHoverName()) {
                data.item = stack;
            }
            if (data.found()) {
                var recipe = data.buildRecipe();
                if (recipe != null) {
                    recipeObjectHolder.value = recipe;
                    return true;
                }
            }
            return false;
        });
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
        GTRecipeDefinition recipe = GTRecipeTypes.FORMING_PRESS_RECIPES.recipeBuilder("name_item")
                .notConsumable(press)
                .inputItems(toName)
                .outputItems(named)
                .duration(40)
                .EUt(4)
                .build();
        GTRecipeTypes.FORMING_PRESS_RECIPES.addToMainCategory(recipe);
    }
}
