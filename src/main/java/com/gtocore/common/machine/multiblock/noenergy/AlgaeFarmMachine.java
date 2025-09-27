package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class AlgaeFarmMachine extends NoEnergyMultiblockMachine {

    private static final Fluid FERMENTEDBIOMASS = GTMaterials.FermentedBiomass.getFluid();

    private static final List<Item> ALGAES = List.of(
            GTOItems.BLUE_ALGAE.get(),
            GTOItems.BROWN_ALGAE.get(),
            GTOItems.GOLD_ALGAE.get(),
            GTOItems.GREEN_ALGAE.get(),
            GTOItems.RED_ALGAE.get());

    public AlgaeFarmMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private Recipe getRecipe(ItemStack stack) {
        RecipeBuilder builder = getRecipeBuilder().inputFluids(new FluidStack(Fluids.WATER, 100 * GTValues.RNG.nextInt(50) + 5000)).duration(200);
        builder.outputItems(stack);
        Recipe recipe = builder.buildRawRecipe();
        if (RecipeRunner.matchRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Nullable
    private Recipe getRecipe() {
        boolean raise = inputFluid(FERMENTEDBIOMASS, 10000);
        int amount = raise ? 10 : 1;
        amount = amount + GTValues.RNG.nextInt(9 * amount);
        AtomicReference<Recipe> recipe = new AtomicReference<>();
        int finalAmount = amount;
        forEachInputItems((stack, a) -> {
            if (ALGAES.contains(stack.getItem())) {
                recipe.set(getRecipe(stack.copyWithCount((int) (finalAmount * Math.max(1, a / 4)))));
                return true;
            }
            return false;
        });
        if (recipe.get() == null) {
            recipe.set(getRecipe(new ItemStack(ALGAES.get(GTValues.RNG.nextInt(5)), amount)));
        }
        return recipe.get();
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
