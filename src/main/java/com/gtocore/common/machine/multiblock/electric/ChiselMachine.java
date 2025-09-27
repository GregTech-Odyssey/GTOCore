package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.world.item.Item;

import com.periut.chisel.block.ChiselGroupLookup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ChiselMachine extends CustomParallelMultiblockMachine {

    public ChiselMachine(MetaMachineBlockEntity holder) {
        super(holder, false, m -> 1L << (2 * (m.getTier() - 1)));
    }

    @Nullable
    private Recipe getRecipe() {
        AtomicInteger c = new AtomicInteger();
        AtomicReference<Item> item = new AtomicReference<>();
        forEachInputItems((stack, amount) -> {
            if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                c.addAndGet(IntCircuitBehaviour.getCircuitConfiguration(stack));
            } else {
                item.set(stack.getItem());
            }
            return false;
        });
        if (c.get() > 0 && item.get() != null) {
            List<Item> list = ChiselGroupLookup.getBlocksInGroup(item.get());
            if (list.isEmpty()) return null;
            Item output = list.get(Math.min(list.size(), c.get()) - 1);
            if (output == null) return null;
            RecipeBuilder builder = getRecipeBuilder().duration(20).EUt(30);
            builder.inputItems(item.get());
            builder.outputItems(output);
            Recipe recipe = builder.buildRawRecipe();
            recipe = ParallelLogic.accurateParallel(this, recipe, getParallel());
            if (recipe != null && RecipeRunner.matchRecipe(this, recipe) && RecipeRunner.matchTickRecipe(this, recipe)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
