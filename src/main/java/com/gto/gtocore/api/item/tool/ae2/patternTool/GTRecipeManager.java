package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class GTRecipeManager {

    private List<GTRecipe> recipes = new ArrayList<>();

    public GTRecipeManager() {
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        RecipeManager recipeManager = currentServer.getRecipeManager();
        recipeManager.getRecipes().forEach(recipe -> {
            if (recipe instanceof GTRecipe gtRecipe) {
                recipes.add(gtRecipe);
            }
        });
    }

    public void filterRecipesByType(GTRecipeType recipeType) {
        recipes = recipes.stream().filter(gtRecipe -> gtRecipe.getType().equals(recipeType)).toList();
    }

    public boolean isItemStackMatchByStringArray(String[] matchList, ItemStack itemStack) {
        boolean isMatch = false;
        for (String match : matchList) {
            if ((itemStack.kjs$getId()).contains(match)) {
                isMatch = true;
            }
        }
        return isMatch;
    }

    public boolean isFluidStackMatchByStringArray(String[] matchList, FluidStack fluidStack) {
        boolean isMatch = false;
        for (String match : matchList) {
            if (match.isEmpty()) continue;
            if ((Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid()))).toString().contains(match)) {
                isMatch = true;
            }
        }
        return isMatch;
    }

    public void filterRecipesByInputsIdArray(String[] matchList, boolean white) {
        this.recipes = this.recipes.stream().filter(gtRecipe -> {
            boolean isAnyoneMatch = false;
            for (ItemStack inputItem : RecipeHelper.getInputItems(gtRecipe)) {
                if (isItemStackMatchByStringArray(matchList, inputItem)) isAnyoneMatch = true;
            }
            for (FluidStack fluidStack : RecipeHelper.getInputFluids(gtRecipe)) {
                if (isFluidStackMatchByStringArray(matchList, fluidStack)) isAnyoneMatch = true;
            }
            if (white) {
                return isAnyoneMatch;
            } else {
                return !isAnyoneMatch;
            }
        }).toList();
    }

    public void filterRecipesByOutputsIdArray(String[] matchList, boolean white) {
        this.recipes = this.recipes.stream().filter(gtRecipe -> {
            boolean isAnyoneMatch = false;
            for (ItemStack inputItem : RecipeHelper.getOutputItems(gtRecipe)) {
                if (isItemStackMatchByStringArray(matchList, inputItem)) isAnyoneMatch = true;
            }
            for (FluidStack fluidStack : RecipeHelper.getOutputFluids(gtRecipe)) {
                if (isFluidStackMatchByStringArray(matchList, fluidStack)) isAnyoneMatch = true;
            }
            if (white) {
                return isAnyoneMatch;
            } else {
                return !isAnyoneMatch;
            }
        }).toList();
    }

    public void filterRecipesByCircuit(int circuit) {
        List<GTRecipe> filedRecipes = new ArrayList<>();
        for (GTRecipe recipe : recipes) {
            int circuitConfiguration = 0;
            for (Content inputContent : recipe.getInputContents(ItemRecipeCapability.CAP)) {
                ItemStack itemStack = ItemRecipeCapability.CAP.of(inputContent.getContent()).kjs$getFirst();
                if (itemStack.getItem().equals(GTItems.INTEGRATED_CIRCUIT.asItem())) {
                    circuitConfiguration = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
                }
            }
            if (circuitConfiguration == circuit) {
                filedRecipes.add(recipe);
            }
        }
        recipes = filedRecipes;
    }
}
