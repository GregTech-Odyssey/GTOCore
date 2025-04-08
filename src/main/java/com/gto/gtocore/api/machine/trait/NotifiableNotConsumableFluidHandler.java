package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public final class NotifiableNotConsumableFluidHandler extends NotifiableFluidTank {

    public NotifiableNotConsumableFluidHandler(MetaMachine machine, int slots, int capacity) {
        super(machine, slots, capacity, IO.IN, IO.NONE);
    }

    @Override
    public boolean shouldSearchContent() {
        return false;
    }

    @Override
    public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, boolean simulate) {
        return handleRecipeInner(io, recipe, (List<FluidIngredient>) left, true);
    }

    @Override
    public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
        if (simulate && io == IO.IN) {
            left = new ObjectArrayList<>(left);
            for (var it = left.iterator(); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                var fluids = ingredient.getStacks();
                if (fluids.length == 0 || fluids[0].isEmpty()) {
                    it.remove();
                    continue;
                }
                int amount = ingredient.getAmount();
                for (int tank = 0; tank < storages.length; ++tank) {
                    FluidStack stored = getFluidInTank(tank);
                    int count = stored.getAmount();
                    if (count == 0) continue;
                    if (ingredient.test(stored)) {
                        amount -= count;
                    }
                    if (amount <= 0) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}
