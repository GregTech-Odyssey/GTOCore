package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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

    public AlgaeFarmMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private GTRecipe getRecipe(ItemStack stack) {
        GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().inputFluids(new FluidStack(Fluids.WATER, 100 * GTValues.RNG.nextInt(50) + 5000)).duration(200);
        builder.outputItems(stack);
        GTRecipe recipe = builder.buildRawRecipe();
        if (RecipeRunner.matchRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (!hasCapabilityProxies()) return null;
        boolean raise = MachineUtils.inputFluid(this, FERMENTEDBIOMASS, 10000);
        int amount = raise ? 10 : 1;
        amount = amount + GTValues.RNG.nextInt(9 * amount);
        AtomicReference<GTRecipe> recipe = new AtomicReference<>();
        int finalAmount = amount;
        MachineUtils.forEachInputItems(this, stack -> {
            if (ALGAES.contains(stack.getItem())) {
                recipe.set(getRecipe(stack.copyWithCount(finalAmount * Math.max(1, stack.getCount() / 4))));
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
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
