package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;

import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class MEPatternVirtualInputHelper {

    private MEPatternVirtualInputHelper() {}

    static void readRecipeTag(ItemStack stack, Consumer<GTRecipeDefinition> recipeSetter) {
        if (stack.getOrCreateTag().tags.get("recipe") instanceof StringTag stringTag) {
            recipeSetter.accept(RecipeBuilder.get(RLUtils.parse(stringTag.getAsString())));
        }
    }

    static @NotNull IPatternDetails convertPattern(
                                                   @NotNull IPatternDetails pattern,
                                                   Supplier<IGrid> gridGetter,
                                                   Supplier<IActionSource> actionSourceGetter,
                                                   NotifiableItemStackHandler circuitInventory,
                                                   CustomItemStackHandler itemStorage,
                                                   BooleanSupplier lockOnce) {
        if (!(pattern instanceof AEProcessingPattern processingPattern)) {
            return pattern;
        }

        var sparseInput = processingPattern.getSparseInputs();
        var input = new ArrayList<GenericStack>(sparseInput.length);
        var in = 0;
        var locked = false;
        for (var stack : sparseInput) {
            if (!isVirtualItemProvider(stack)) {
                input.add(stack);
                continue;
            }

            var what = (AEItemKey) stack.what();
            ItemStack virtualItem = VirtualItemProviderBehavior.getVirtualItem(what.getReadOnlyStack());
            if (virtualItem.isEmpty()) continue;
            if (!locked) {
                locked = lockOnce.getAsBoolean();
            }
            if (GTItems.PROGRAMMED_CIRCUIT.isIn(virtualItem)) {
                circuitInventory.storage.setStackInSlot(0, virtualItem);
                continue;
            }

            virtualItem.setCount(Math.clamp(stack.amount(), 1, virtualItem.getMaxStackSize()));
            var grid = gridGetter.get();
            if (grid != null && grid.getStorageService().getInventory().extract(what, 1, Actionable.SIMULATE, actionSourceGetter.get()) == 1) {
                var inSlot = itemStorage.getStackInSlot(in);
                if (!inSlot.isEmpty()) {
                    itemStorage.setStackInSlot(in, ItemStack.EMPTY);
                    grid.getStorageService().getInventory().insert(AEItemKey.of(inSlot), inSlot.getCount(), Actionable.MODULATE, actionSourceGetter.get());
                }
                itemStorage.setStackInSlot(in, virtualItem);
                in++;
                if (in > itemStorage.getSlots()) break;
            }
        }

        if (input.size() == sparseInput.length || input.isEmpty()) {
            return pattern;
        }
        var stack = PatternDetailsHelper.encodeProcessingPattern(input.toArray(new GenericStack[0]), processingPattern.getSparseOutputs());
        return MyPatternDetailsHelper.decode(AEItemKey.of(stack));
    }

    private static boolean isVirtualItemProvider(GenericStack stack) {
        return stack != null &&
                stack.what() instanceof AEItemKey what &&
                what.getItem() == CustomItems.VIRTUAL_ITEM_PROVIDER.get() &&
                what.getTag() != null &&
                what.getTag().tags.containsKey("n");
    }
}
