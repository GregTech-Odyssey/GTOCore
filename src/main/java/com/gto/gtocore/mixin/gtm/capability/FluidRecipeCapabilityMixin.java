package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.machine.trait.IPatternBufferRecipeHandler;
import com.gto.gtocore.api.recipe.MapFluid;
import com.gto.gtocore.mixin.gtm.api.recipe.FluidIngredientFluidValueAccessor;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.MapFluidTagIngredient;

import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(FluidRecipeCapability.class)
public class FluidRecipeCapabilityMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<AbstractMapIngredient> convertToMapIngredient(Object obj) {
        List<AbstractMapIngredient> ingredients = new ObjectArrayList<>(1);
        if (obj instanceof FluidIngredient ingredient) {
            for (FluidIngredient.Value value : ingredient.values) {
                if (value instanceof FluidIngredient.TagValue tagValue) {
                    ingredients.add(new MapFluidTagIngredient(tagValue.getTag()));
                } else {
                    ingredients.add(new MapFluid(((FluidIngredientFluidValueAccessor) value).getFluid(), ingredient.getNbt()));
                }
            }
        } else if (obj instanceof FluidStack stack) {
            ingredients.add(new MapFluid(stack.getFluid(), stack.getTag()));
        }
        return ingredients;
    }

    /**
     * @author .
     * @reason 优化性能，支持隔离
     */
    @Overwrite(remap = false)
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        if (holder instanceof IRecipeLogicMachine machine && machine.getRecipeLogic() instanceof IEnhancedRecipeLogic recipeLogic) {

            Object2LongOpenHashMap<FluidStack> ingredientStacks = recipeLogic.gtocore$getFluidIngredientStacks();
            if (machine instanceof IDistinctRecipeHolder distinctRecipeHolder && distinctRecipeHolder.getCurrentDistinct() != null) {
                var recipeHandlerList = distinctRecipeHolder.getCurrentDistinct().getCapability(FluidRecipeCapability.CAP);
                for (IRecipeHandler<?> container : recipeHandlerList) {
                    if (container instanceof IPatternBufferRecipeHandler handler) {
                        for (var entry : handler.getFluidMap().object2LongEntrySet()) {
                            ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : v + entry.getLongValue());
                        }
                    } else {
                        for (Object object : container.getContents()) {
                            if (object instanceof FluidStack fluidStack) {
                                ingredientStacks.computeLong(fluidStack, (k, v) -> v == null ? fluidStack.getAmount() : v + fluidStack.getAmount());
                            }
                        }
                    }
                }
            } else {
                Object2LongOpenHashMap<FluidStack> map = recipeLogic.gtocore$getParallelFluidMap();
                var recipeHandlerList = holder.getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP);

                for (IRecipeHandler<?> container : recipeHandlerList) {
                    boolean patternBuffer = container instanceof IPatternBufferRecipeHandler;
                    Object2LongOpenHashMap<FluidStack> fluidMap;
                    if (patternBuffer) {
                        fluidMap = ((IPatternBufferRecipeHandler) container).getFluidMap();
                    } else {
                        fluidMap = recipeLogic.gtocore$getFluidMap();
                        for (Object object : container.getContents()) {
                            if (object instanceof FluidStack fluidStack) {
                                fluidMap.computeLong(fluidStack, (k, v) -> v == null ? fluidStack.getAmount() : v + fluidStack.getAmount());
                            }
                        }
                    }

                    if (container.isDistinct()) {
                        for (var entry : fluidMap.object2LongEntrySet()) {
                            ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : Math.max(v, entry.getLongValue()));
                        }
                    } else {
                        for (Object2LongMap.Entry<FluidStack> obj : fluidMap.object2LongEntrySet()) {
                            map.computeLong(obj.getKey(), (k, v) -> v == null ? obj.getLongValue() : v + obj.getLongValue());
                        }
                    }
                    if (!patternBuffer) fluidMap.clear();
                }
                for (var entry : map.object2LongEntrySet()) {
                    ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : Math.max(v, entry.getLongValue()));
                }
            }

            long minMultiplier = Integer.MAX_VALUE - 1;
            Object2IntOpenHashMap<FluidIngredient> notConsumableMap = recipeLogic.gtocore$getFluidNotConsumableMap();
            Object2IntOpenHashMap<FluidIngredient> fluidCountMap = recipeLogic.gtocore$getFluidConsumableMap();
            for (Content content : recipe.getInputContents(FluidRecipeCapability.CAP)) {
                FluidIngredient fluidInput = FluidRecipeCapability.CAP.of(content.content);
                int fluidAmount = fluidInput.getAmount();
                if (content.chance == 0) {
                    notConsumableMap.computeIfPresent(fluidInput, (k, v) -> v + fluidAmount);
                    notConsumableMap.putIfAbsent(fluidInput, fluidAmount);
                } else {
                    fluidCountMap.computeIfPresent(fluidInput, (k, v) -> v + fluidAmount);
                    fluidCountMap.putIfAbsent(fluidInput, fluidAmount);
                }
            }

            for (Object2IntMap.Entry<FluidIngredient> notConsumableFluid : notConsumableMap.object2IntEntrySet()) {
                long needed = notConsumableFluid.getIntValue();
                long available = 0;
                for (Object2LongMap.Entry<FluidStack> inputFluid : ingredientStacks.object2LongEntrySet()) {
                    if (notConsumableFluid.getKey().test(inputFluid.getKey())) {
                        available = inputFluid.getLongValue();
                        if (available > needed) {
                            inputFluid.setValue(available - needed);
                            needed -= available;
                            break;
                        } else {
                            inputFluid.setValue(0L);
                            notConsumableFluid.setValue((int) (needed - available));
                            needed -= available;
                        }
                    }
                }
                if (needed >= available) {
                    return 0;
                }
            }

            if (fluidCountMap.isEmpty() && !notConsumableMap.isEmpty()) {
                return parallelAmount;
            }

            for (Object2IntMap.Entry<FluidIngredient> fs : fluidCountMap.object2IntEntrySet()) {
                long needed = fs.getIntValue();
                long available = 0;
                for (Object2LongMap.Entry<FluidStack> inputFluid : ingredientStacks.object2LongEntrySet()) {
                    if (fs.getKey().test(inputFluid.getKey())) {
                        available += inputFluid.getLongValue();
                    }
                }
                if (available >= needed) {
                    int ratio = (int) Math.min(parallelAmount, (float) available / needed);
                    if (ratio < minMultiplier) {
                        minMultiplier = ratio;
                    }
                } else {
                    return 0;
                }
            }
            return (int) minMultiplier;
        }
        return 1;
    }
}
