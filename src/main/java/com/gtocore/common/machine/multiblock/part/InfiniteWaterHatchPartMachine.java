package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.function.FluidConsumer;
import com.gregtechceu.gtceu.api.capability.recipe.function.FluidPredicate;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class InfiniteWaterHatchPartMachine extends TieredIOPartMachine {

    public InfiniteWaterHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.IV, IO.IN);
        new FluidTank(this);
    }

    @Override
    public void onPaintingColorChanged(int color) {
        getHandlerList().setColor(color, true);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    private static final class FluidTank extends NotifiableRecipeHandlerTrait<FluidIngredient> {

        private static final FluidStack WATER = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);

        private FluidTank(MetaMachine machine) {
            super(machine);
        }

        @Override
        public boolean hasCapability(@Nullable Direction side) {
            return false;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            if (io == IO.IN) {
                for (var it = left.listIterator(0); it.hasNext();) {
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
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
        }

        @Override
        public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List<?> list, boolean simulate) {
            return handleRecipeInner(io, recipe, new ObjectArrayList(list), simulate);
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        private static final IntIngredientMap MAP = new IntIngredientMap();

        static {
            IntIngredientMap.FLUID_CONVERSION.convert(WATER, Long.MAX_VALUE, MAP);
        }

        @Override
        public boolean forEachFluids(FluidPredicate function) {
            return function.test(WATER, Long.MAX_VALUE);
        }

        @Override
        public void fastForEachFluids(FluidConsumer function) {
            function.accept(WATER, Long.MAX_VALUE);
        }

        @Override
        public IntIngredientMap getIngredientMap() {
            return MAP;
        }
    }
}
