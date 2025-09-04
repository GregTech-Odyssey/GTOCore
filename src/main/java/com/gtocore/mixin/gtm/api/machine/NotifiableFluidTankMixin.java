package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.SimpleFluidIngredient;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(NotifiableFluidTank.class)
public abstract class NotifiableFluidTankMixin {

    @Shadow(remap = false)
    @Final
    public IO handlerIO;

    @Shadow(remap = false)
    @Final
    protected CustomFluidTank[] storages;

    @Shadow(remap = false)
    @Final
    protected CustomFluidTank lockedFluid;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
        if (io != handlerIO) return left;
        if (io != IO.IN && io != IO.OUT) return left.isEmpty() ? null : left;
        Runnable[] listeners = null;
        if (!simulate) {
            listeners = new Runnable[storages.length];
            for (int i = 0; i < storages.length; i++) {
                listeners[i] = storages[i].getOnContentsChanged();
                storages[i].setOnContentsChangedAndfreeze(GTUtil.NOOP);
            }
        }
        boolean changed = false;
        SimpleFluidIngredient[] visited = new SimpleFluidIngredient[storages.length];
        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
        for (var it = left.listIterator(0); it.hasNext();) {
            var ingredient = it.next();
            var fluids = FastFluidIngredient.getFluidStack(ingredient);
            if (fluids.length == 0 || fluids[0].isEmpty()) {
                it.remove();
                continue;
            }
            for (int tank = 0; tank < storages.length; ++tank) {
                var storage = storages[tank];
                FluidStack stored = storage.getFluid();
                int amount = (visited[tank] == null ? stored.getAmount() : visited[tank].amount());
                if (io == IO.IN) {
                    if (amount == 0) continue;
                    if ((visited[tank] == null && ingredient.test(stored)) || (visited[tank] != null && ingredient.test(visited[tank].stack()))) {
                        var drained = storage.drain(ingredient.getAmount(), action);
                        if (drained.getAmount() > 0) {
                            if (simulate) {
                                visited[tank] = new SimpleFluidIngredient(drained, amount - drained.getAmount());
                            }
                            changed = true;
                            ingredient.shrink(drained.getAmount());
                            if (ingredient.getAmount() <= 0) {
                                it.remove();
                                break;
                            }
                        }
                    }
                } else {
                    var fluid = fluids[0];
                    if (amount < storage.getCapacity() && (lockedFluid.isEmpty() || lockedFluid.getFluid().getFluid() == fluid.getFluid()) && (stored.isEmpty() || stored.isFluidEqual(fluid)) && (visited[tank] == null || visited[tank].stack().isFluidEqual(fluid))) {
                        FluidStack output = fluid.copy();
                        output.setAmount(ingredient.getAmount());
                        int filled = storage.fill(output, action);
                        if (filled > 0) {
                            if (simulate) {
                                visited[tank] = new SimpleFluidIngredient(output, filled);
                            }
                            changed = true;
                            ingredient.shrink(filled);
                            if (ingredient.getAmount() <= 0) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (listeners != null) {
            for (int i = 0; i < storages.length; i++) {
                storages[i].setOnContentsChangedAndfreeze(listeners[i]);
                if (changed) listeners[i].run();
            }
        }
        return left.isEmpty() ? null : left;
    }
}
