package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.gto.datasynclib.util.holder.ObjHolder;

import java.util.List;

public final class AlgaeFarmMachine extends NoEnergyMultiblockMachine implements ICustomRecipeLogicHolder {

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

    private GTRecipeDefinition getRecipe(boolean raise, ItemStack stack) {
        RecipeBuilder builder = getRecipeBuilder().inputFluids(new FluidStack(Fluids.WATER, 100 * GTValues.RNG.nextInt(50) + 5000)).duration(200);
        builder.outputItems(stack);
        if (raise) builder.inputFluids(FERMENTEDBIOMASS, 10000);
        return builder.build();
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        boolean raise = unit.matchFluid(FERMENTEDBIOMASS, 10000);
        int amount = raise ? 10 : 1;
        amount = amount + GTValues.RNG.nextInt(9 * amount);
        ObjHolder<GTRecipeDefinition> recipe = new ObjHolder<>();
        int finalAmount = amount;
        unit.forEachItems(true, (stack, a) -> {
            if (ALGAES.contains(stack.getItem())) {
                recipe.set(getRecipe(raise, stack.copyWithCount((int) (finalAmount * Math.max(1, a / 4)))));
                return true;
            }
            return false;
        });
        if (recipe.get() == null) {
            recipe.set(getRecipe(raise, new ItemStack(ALGAES.get(GTValues.RNG.nextInt(5)), amount)));
        }
        return recipe.get();
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }
}
