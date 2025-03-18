package com.gto.gtocore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(NotifiableFluidTank.class)
public class NotifiableFluidTankMixin {

    @Shadow(remap = false)
    @Final
    public IO handlerIO;

    @Shadow(remap = false)
    @Final
    protected CustomFluidTank[] storages;

    @Shadow(remap = false)
    protected boolean allowSameFluids;

    @Inject(method = "handleRecipeInner", at = @At("HEAD"), remap = false, cancellable = true)
    private void handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, @Nullable String slotName, boolean simulate, CallbackInfoReturnable<List<FluidIngredient>> cir) {
        if (simulate) return;
        cir.setReturnValue(gtocore$handle(io, left, handlerIO, storages, allowSameFluids));
    }

    @Unique
    private synchronized static List<FluidIngredient> gtocore$handle(IO io, List<FluidIngredient> left, IO handlerIO, CustomFluidTank[] storages, boolean allowSameFluids) {
        if (io != handlerIO) return left;
        if (io != IO.IN && io != IO.OUT) return left.isEmpty() ? null : left;
        IFluidHandler.FluidAction action = IFluidHandler.FluidAction.EXECUTE;
        FluidStack[] visited = new FluidStack[storages.length];
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
            if (io == IO.OUT && !allowSameFluids) {
                CustomFluidTank existing = null;
                for (var storage : storages) {
                    if (!storage.getFluid().isEmpty() && storage.getFluid().isFluidEqual(fluids[0])) {
                        existing = storage;
                        break;
                    }
                }
                if (existing != null) {
                    FluidStack output = fluids[0];
                    int filled = existing.fill(output, action);
                    ingredient.shrink(filled);
                    if (ingredient.getAmount() <= 0) {
                        it.remove();
                    }
                    // Continue to next ingredient regardless of if we filled this ingredient completely
                    continue;
                }
            }
            for (int tank = 0; tank < storages.length; ++tank) {
                FluidStack stored = storages[tank].getFluid();
                int amount = (visited[tank] == null ? stored.getAmount() : visited[tank].getAmount());
                if (io == IO.IN) {
                    if (amount == 0) continue;
                    if ((visited[tank] == null && ingredient.test(stored)) || ingredient.test(visited[tank])) {
                        var drained = storages[tank].drain(ingredient.getAmount(), action);
                        if (drained.getAmount() > 0) {
                            visited[tank] = drained.copy();
                            visited[tank].setAmount(amount - drained.getAmount());
                            ingredient.shrink(drained.getAmount());
                        }
                    }
                } else {
                    // IO.OUT && No tank already has this output
                    FluidStack output = fluids[0].copy();
                    output.setAmount(ingredient.getAmount());
                    if (visited[tank] == null || visited[tank].isFluidEqual(output)) {
                        int filled = storages[tank].fill(output, action);
                        if (filled > 0) {
                            visited[tank] = output.copy();
                            visited[tank].setAmount(filled);
                            ingredient.shrink(filled);
                            if (!allowSameFluids) {
                                if (ingredient.getAmount() <= 0) it.remove();
                                break;
                            }
                        }
                    }
                }
                if (ingredient.getAmount() <= 0) {
                    it.remove();
                    break;
                }
            }
        }
        return left.isEmpty() ? null : left;
    }
}
