package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.trait.IExtendRecipeHandler;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
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

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class InfiniteWaterHatchPartMachine extends TieredIOPartMachine {

    public InfiniteWaterHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.IV, IO.IN);
        new FluidTank(this);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    private static final class FluidTank extends NotifiableRecipeHandlerTrait<FluidIngredient> implements IExtendRecipeHandler {

        private static final List<FluidStack> WATER = List.of(new FluidStack(Fluids.WATER, Integer.MAX_VALUE));

        private FluidTank(MetaMachine machine) {
            super(machine);
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            return handleRecipe(io, recipe, left, simulate);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return new ArrayList<>(WATER);
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
                    var f = FastFluidIngredient.getFluid(it.next());
                    if (f == Fluids.WATER) {
                        it.remove();
                        break;
                    }
                }
            }
            return left.isEmpty() ? null : left;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        private static final Object2LongOpenHashMap<FluidStack> MAP = new Object2LongOpenHashMap<>(2, 0.99F);

        static {
            MAP.put(WATER.get(0), Long.MAX_VALUE);
        }

        @Override
        public Object2LongOpenHashMap<FluidStack> getFluidMap() {
            return MAP;
        }
    }
}
