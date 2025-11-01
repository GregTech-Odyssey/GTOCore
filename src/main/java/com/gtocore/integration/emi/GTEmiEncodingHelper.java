package com.gtocore.integration.emi;

import com.gtocore.common.item.ItemMap;
import com.gtocore.data.tag.Tags;
import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.hepdd.gtmthings.api.misc.Hatch;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class GTEmiEncodingHelper { // also accessed by gtolib

    @Nullable
    private static GenericStack ofVirtual(EmiStack stack) {
        if (stack.getKey() instanceof Item) {
            var item = CustomItems.VIRTUAL_ITEM_PROVIDER.asStack();
            item.getOrCreateTag().putBoolean("marked", true);
            return new GenericStack(AEItemKey.of(VirtualItemProviderBehavior.setVirtualItem(item, stack.getItemStack())), 1);
        }
        return null;
    }

    private static List<GenericStack> intoGenericStack(EmiIngredient ingredient, boolean virtual) {
        if (ingredient.isEmpty()) {
            return Collections.emptyList();
        }
        if (ingredient instanceof TagEmiIngredient tag && Tags.CIRCUITS_ARRAY.containsKey(tag.key)) {
            return ingredient.getEmiStacks().stream().filter(stack -> stack instanceof ItemEmiStack itemEmiStack && ItemMap.UNIVERSAL_CIRCUITS.contains(itemEmiStack.getKey())).map(stack -> fromEmiStackVirtualConvertible(stack, ingredient.getAmount(), virtual)).toList();
        }
        return ingredient.getEmiStacks().stream().map(stack -> fromEmiStackVirtualConvertible(stack, ingredient.getAmount(), virtual)).toList();
    }

    private static GenericStack fromEmiStackVirtualConvertible(EmiStack stack, long amount, boolean virtual) {
        if (stack.getKey() instanceof Item) {
            if (virtual) {
                return ofVirtual(stack);
            }
            return new GenericStack(AEItemKey.of(stack.getItemStack()), amount);
        } else if (stack.getKey() instanceof Fluid fluid) {
            return new GenericStack(AEFluidKey.of(fluid), amount);
        }
        return new GenericStack(AEItemKey.of(Items.STICK), 0);
    }

    public static List<GenericStack> intoGenericStack(EmiIngredient ingredient) {
        return intoGenericStack(ingredient, false);
    }

    public static List<List<GenericStack>> ofInputs(EmiRecipe emiRecipe) {
        if (emiRecipe instanceof MultiblockInfoEmiRecipe recipe) {
            var stream = recipe.getInputs(recipe.i)
                    .stream()
                    .filter(GTEmiEncodingHelper::isNotHatch)
                    .map(GTEmiEncodingHelper::intoGenericStack);
            if (recipe.i > 0 && recipe.definition.getSubPatternFactory() != null && GTUtil.isCtrlDown()) {
                stream = Stream.concat(recipe.getInputs(0)
                        .stream()
                        .filter(GTEmiEncodingHelper::isNotHatch)
                        .map(GTEmiEncodingHelper::intoGenericStack), stream);
            }
            return stream.toList();
        }
        var list = new ArrayList<List<GenericStack>>();
        if (GTUtil.isShiftDown() || GTUtil.isCtrlDown()) {
            emiRecipe.getCatalysts()
                    .stream()
                    .map(s -> intoGenericStack(s, GTUtil.isCtrlDown()))
                    .forEach(list::add);
            if (list.isEmpty() && GTUtil.isCtrlDown()) {
                var itemKey = AEItemKey.of(VirtualItemProviderBehavior.setVirtualItem(CustomItems.VIRTUAL_ITEM_PROVIDER.asStack(), ItemStack.EMPTY));
                itemKey.getTag().putBoolean("marked", true);
                list.add(List.of(new GenericStack(itemKey, 1)));
            }
        }
        emiRecipe.getInputs()
                .stream()
                .map(GTEmiEncodingHelper::intoGenericStack)
                .forEach(list::add);
        return list;
    }

    private static boolean isNotHatch(EmiIngredient ingredient) {
        if (ingredient instanceof EmiStack stack) {
            return GTUtil.isShiftDown() || !(stack.getKey() instanceof MetaMachineItem meta) || !Hatch.Set.contains(meta.getBlock());
        }
        return false;
    }
}
