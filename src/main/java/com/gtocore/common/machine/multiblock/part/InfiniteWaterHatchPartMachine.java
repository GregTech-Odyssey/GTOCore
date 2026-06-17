package com.gtocore.common.machine.multiblock.part;

import com.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableTieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

import com.fast.recipesearch.IntLongMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.ObjLongConsumer;

public final class InfiniteWaterHatchPartMachine extends WorkableTieredIOPartMachine {

    public InfiniteWaterHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.IV, IO.IN);
        new FluidTank(this);
    }

    @Override
    public void onPaintingColorChanged(int color) {
        getHandlerUnit().setColor(color, true);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    private static final class FluidTank extends NotifiableRecipeHandlerTrait {

        private static final FluidStack WATER = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);

        private FluidTank(MetaMachine machine) {
            super(machine);
        }

        @Override
        public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
            if (io == IO.IN) {
                for (var it = fluids.iterator(); it.hasNext();) {
                    if (it.next().inner.getFluid() == Fluids.WATER) {
                        it.remove();
                        break;
                    }
                }
            }
            return fluids.isEmpty();
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        private static final IntLongMap MAP = new IntLongMap();

        static {
            GTORecipeTypes.DUMMY_RECIPES.convertFluid(WATER, Long.MAX_VALUE, MAP);
        }

        @Override
        public boolean forEachFluids(ObjLongPredicate<FluidStack> function) {
            return function.test(WATER, Long.MAX_VALUE);
        }

        @Override
        public void fastForEachFluids(ObjLongConsumer<FluidStack> function) {
            function.accept(WATER, Long.MAX_VALUE);
        }

        @Override
        public IntLongMap getSearchMap(@NotNull GTRecipeType type) {
            return MAP;
        }

        @Override
        public boolean canHandleFluid() {
            return true;
        }
    }
}
