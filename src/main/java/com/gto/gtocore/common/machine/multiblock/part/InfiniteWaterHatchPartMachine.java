package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.mixin.gtm.api.recipe.FluidIngredientFluidValueAccessor;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class InfiniteWaterHatchPartMachine extends TieredIOPartMachine {

    public InfiniteWaterHatchPartMachine(IMachineBlockEntity holder) {
        super(holder, GTValues.IV, IO.IN);
        new FluidTank(this);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    private static class FluidTank extends NotifiableRecipeHandlerTrait<FluidIngredient> {

        private static final List<FluidStack> WATER = List.of(new FluidStack(Fluids.WATER, Integer.MAX_VALUE));

        private FluidTank(MetaMachine machine) {
            super(machine);
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            return left;
        }

        @Override
        public @NotNull List<Object> getContents() {
            return new ObjectArrayList<>(WATER);
        }

        @Override
        public double getTotalContentAmount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
        }

        @Override
        public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List<?> list, boolean simulate) {
            var left = (List<FluidIngredient>) list;
            if (io == IO.IN) {
                for (var it = left.iterator(); it.hasNext();) {
                    for (var value : it.next().values) {
                        if (value instanceof FluidIngredient.FluidValue && ((FluidIngredientFluidValueAccessor) value).getFluid() == Fluids.WATER) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
            return left.isEmpty() ? null : left;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }
    }
}
