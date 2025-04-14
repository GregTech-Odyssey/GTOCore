package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.utils.FluidUtils;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.recipe.EmiSyntheticIngredientRecipe;
import dev.emi.emi.recipe.EmiTagRecipe;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.runtime.EmiFavorite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author EasterFG on 2025/3/2
 */
@Mixin(EmiApi.class)
public abstract class EmiApiMixin {

    @Shadow(remap = false)
    private static Map<EmiRecipeCategory, List<EmiRecipe>> mapRecipes(List<EmiRecipe> list) {
        return null;
    }

    @Shadow(remap = false)
    private static List<EmiRecipe> pruneUses(List<EmiRecipe> list, EmiIngredient context) {
        return null;
    }

    @Shadow(remap = false)
    private static void setPages(Map<EmiRecipeCategory, List<EmiRecipe>> recipes, EmiIngredient stack) {}

    @Shadow(remap = false)
    private static List<EmiRecipe> pruneSources(List<EmiRecipe> list, EmiStack context) {
        return null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void displayUses(EmiIngredient stack) {
        if (!stack.isEmpty()) {
            EmiStack zero = stack.getEmiStacks().get(0);
            List<EmiRecipe> combinedRecipes = new ArrayList<>();
            List<EmiRecipe> recipesByInput = pruneUses(EmiApi.getRecipeManager().getRecipesByInput(zero), stack);
            if (recipesByInput == null) {
                recipesByInput = new ArrayList<>();
            } else {
                recipesByInput = new ArrayList<>(recipesByInput);
            }
            var fluid = gtocore$getBucketFluid(stack);
            if (fluid != null) {
                var list = pruneUses(EmiApi.getRecipeManager().getRecipesByInput(fluid.getEmiStacks().get(0)), fluid);
                if (list != null) recipesByInput.addAll(list);
            }
            List<EmiRecipe> workstationRecipes = EmiRecipes.byWorkstation.getOrDefault(zero, new ArrayList<>());

            for (EmiRecipe recipe : recipesByInput) {
                if (!combinedRecipes.contains(recipe)) {
                    combinedRecipes.add(recipe);
                }
            }

            for (EmiRecipe recipe : workstationRecipes) {
                if (!combinedRecipes.contains(recipe)) {
                    combinedRecipes.add(recipe);
                }
            }

            Map<EmiRecipeCategory, List<EmiRecipe>> map = mapRecipes(combinedRecipes);
            setPages(map, stack);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void displayRecipes(EmiIngredient stack) {
        if (stack instanceof EmiFavorite fav) {
            stack = fav.getStack();
        }
        if (stack instanceof TagEmiIngredient tag) {
            for (EmiRecipe recipe : EmiApi.getRecipeManager().getRecipes(VanillaPlugin.TAG)) {
                if (recipe instanceof EmiTagRecipe tr && tr.key.equals(tag.key)) {
                    setPages(Map.of(VanillaPlugin.TAG, List.of(recipe)), stack);
                    break;
                }
            }
        } else if (stack instanceof ListEmiIngredient) {
            setPages(Map.of(VanillaPlugin.INGREDIENT, List.of(new EmiSyntheticIngredientRecipe(stack))), stack);
        } else {
            var recipesByOutput = new ArrayList<EmiRecipe>();
            var fluid = gtocore$getBucketFluid(stack);
            EmiStack es = stack.getEmiStacks().get(0);
            if (fluid != null) {
                EmiStack fes = fluid.getEmiStacks().get(0);
                recipesByOutput.addAll(pruneSources(EmiApi.getRecipeManager().getRecipesByOutput(fes), fes));
            }
            if (stack.getEmiStacks().size() == 1) {
                recipesByOutput.addAll(pruneSources(EmiApi.getRecipeManager().getRecipesByOutput(es), es));
            } else if (!recipesByOutput.isEmpty()) {
                es = fluid.getEmiStacks().get(0);
            }
            if (recipesByOutput.isEmpty()) return;
            setPages(mapRecipes(recipesByOutput), stack);
            EmiApi.focusRecipe(BoM.getRecipe(es));
        }
    }

    @Unique
    private static EmiIngredient gtocore$getBucketFluid(EmiIngredient stack) {
        if (stack instanceof EmiStack emiStack) {
            Fluid fluid = Fluids.EMPTY;
            if (emiStack.getKey() instanceof BucketItem bucketItem) {
                fluid = bucketItem.getFluid();
            } else if (emiStack.getKey() instanceof ComponentItem && emiStack.hasNbt()) {
                CompoundTag nbt = emiStack.getNbt();
                if (nbt.contains("Fluid", Tag.TAG_COMPOUND)) {
                    var fluidTag = nbt.getCompound("Fluid");
                    var fluidName = fluidTag.getString("FluidName");
                    fluid = FluidUtils.getFluid(fluidName);
                }
            }
            return fluid == Fluids.EMPTY ? null : EmiStack.of(fluid);
        }
        return null;
    }
}
