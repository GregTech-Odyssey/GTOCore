package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.fast.recipesearch.IntLongMap;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.utils.collection.FastObjectArrayList;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtolib.api.ae2.MyPatternDetailsHelper;
import com.gtolib.api.ae2.pattern.IParallelPatternDetails;
import com.gtolib.api.ae2.stacks.IIngredientConvertible;
import com.gtolib.api.ae2.stacks.TagPrefixKey;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class MEWildcardPatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    private List<IPatternDetails> cachedPatterns;

    public MEWildcardPatternBufferPartMachine(@NotNull MetaMachineBlockEntity holder) {
        super(holder, 1);
    }

    @Override
    public boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem;
    }

    @Override
    public @Nullable IPatternDetails decodePattern(ItemStack stack, int index) {
        var pattern = MyPatternDetailsHelper.decodePattern(stack, holder, getGrid());
        if (pattern == null) return null;
        return IParallelPatternDetails.of(pattern, getLevel(), 1);
    }

    @Override
    public @NotNull List<@NotNull IPatternDetails> getAvailablePatterns() {
        var patterns = super.getAvailablePatterns();
        if (patterns.isEmpty()) {
            return patterns;
        }
        if (!getDetailsInit() || cachedPatterns == null) {
            var newPatterns = new FastObjectArrayList<IPatternDetails>();
            GTCEuAPI.materialManager.getRegisteredMaterials().forEach(
                    material -> patterns.forEach(cp -> {
//                        cp = convertPattern(p, 0);
                        if (cp instanceof AEProcessingPattern processingPattern) {
                            var sparseInput = processingPattern.getSparseInputs();
                            var input = new ObjectArrayList<GenericStack>(sparseInput.length);
                            for (var stack : sparseInput) {
                                if (stack.what() instanceof TagPrefixKey tagPrefixKey) {
                                    var what = tagPrefixKey.getFromMaterial(material);
                                    if (what == null) return;
                                    input.add(new GenericStack(what, stack.amount()));
                                }else{
                                    input.add(stack);
                                }
                            }
                            var sparseOutput = processingPattern.getSparseOutputs();
                            var output = new ObjectArrayList<GenericStack>(sparseOutput.length);
                            for (var stack : sparseOutput) {
                                if (stack.what() instanceof TagPrefixKey tagPrefixKey) {
                                    var what = tagPrefixKey.getFromMaterial(material);
                                    if (what == null) return;
                                    output.add(new GenericStack(what, stack.amount()));
                                } else {
                                    output.add(stack);
                                }
                            }
                            var stack = PatternDetailsHelper.encodeProcessingPattern(input.toArray(new GenericStack[0]), output.toArray(new GenericStack[0]));
                            var detail = MyPatternDetailsHelper.CACHE.get(AEItemKey.of(stack));
                            if (validatePattern(detail)) {
                                newPatterns.add(convertPattern(detail, 0));
                            }
                        }
                    })
            );
            cachedPatterns = newPatterns;
        }
        return cachedPatterns;
    }

    private boolean validatePattern(AEProcessingPattern pattern) {
        if (recipeType == GTORecipeTypes.HATCH_COMBINED) {
            return !getRecipeTypes().isEmpty() &&
                    getRecipeTypes().stream().anyMatch(rt -> {
                        AtomicBoolean valid = new AtomicBoolean(false);
                        rt.findRecipe(virtual(pattern), r -> {
                            valid.set(checkProb(r));
                            return valid.get();
                        });
                        return valid.get();
                    });
        } else {
            AtomicBoolean valid = new AtomicBoolean(false);
            recipeType.findRecipe(virtual(pattern), r -> {
                valid.set(checkProb(r));
                return valid.get();
            });
            return valid.get();
        }
    }
    private IRecipeCapabilityHolder virtual(AEProcessingPattern pattern) {
        return new IRecipeCapabilityHolder() {
            @Override
            public @NotNull Map<IO, List<RecipeHandlerList>> getCapabilitiesProxy() {
                return Map.of(IO.IN, List.of(new VirtualList(pattern)));
            }

            @Override
            public @NotNull Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> getCapabilitiesFlat() {
                return Map.of();
            }
        };
    }
    private class VirtualList extends RecipeHandlerList {
        final AEProcessingPattern pattern;

        public VirtualList(AEProcessingPattern pattern) {
            super(IO.IN, null);
            this.pattern = pattern;
            var slot = getInternalInventory()[0];
            var buffer = MEWildcardPatternBufferPartMachine.this;
            addHandlers(slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
        }

        @Override
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
            var ings = super.getIngredientMap(type);
            var sparseInput = pattern.getSparseInputs();
            Arrays.stream(sparseInput).map(GenericStack::what)
                    .forEach(key -> {
                        if (key instanceof AEItemKey what && what.getItem() == CustomItems.VIRTUAL_ITEM_PROVIDER.get() && what.getTag() != null && what.getTag().tags.containsKey("n")){
                            ItemStack virtualItem = VirtualItemProviderBehavior.getVirtualItem(what.getReadOnlyStack());
                            if (virtualItem.isEmpty()) return;
                            key = AEItemKey.of(virtualItem);
                        }
                        ((IIngredientConvertible) key).gtolib$convert(Long.MAX_VALUE, ings);
                    });
            return ings;
        }

        @Override
        public boolean findRecipe(IRecipeCapabilityHolder holder, GTRecipeType recipeType, Predicate<GTRecipe> canHandle) {
            var ings = this.getIngredientMap(recipeType);
            if (ings.isEmpty()) return false;
            holder.setCurrentHandlerList(this, null);
//            var map = new IntLongMap();
//            Arrays.stream(((IItem) Items.BONE_MEAL).gtolib$getMapItem()).forEach(i -> map.add(i, Long.MAX_VALUE));
//            GTORecipeTypes.EXTRACTOR_RECIPES.db.find(map, r ->{
//                GTOCore.LOGGER.warn("Found extractor recipe during wildcard pattern validation: " + r);
//                return true;
//            });
            return recipeType.db.find(ings, canHandle);
        }
    }
    private boolean checkProb(GTRecipe recipe) {
        for (var ingredient : recipe.getInputContents(ItemRecipeCapability.CAP)) {
            if (ingredient.chance != 10000 && ingredient.chance != 0) return false;
        }
        for (var ingredient : recipe.getInputContents(FluidRecipeCapability.CAP)) {
            if (ingredient.chance != 10000 && ingredient.chance != 0) return false;
        }
        return true;
    }

}
