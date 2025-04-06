package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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

    public ChiselMachine(IMachineBlockEntity holder) {
        super(holder, false, m -> 1 << (2 * (m.getTier() - 1)));
    }

    @Nullable
    private GTRecipe getRecipe() {
        AtomicInteger c = new AtomicInteger();
        AtomicReference<Item> item = new AtomicReference<>();
        MachineUtils.forEachInputItems(this, itemStack -> {
            if (itemStack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                c.addAndGet(IntCircuitBehaviour.getCircuitConfiguration(itemStack));
            } else {
                item.set(itemStack.getItem());
            }
            return false;
        });
        if (c.get() > 0 && item.get() != null) {
            List<Item> list = ChiselGroupLookup.getBlocksInGroup(item.get());
            if (list.isEmpty()) return null;
            Item output = list.get(c.get() - 1);
            if (output == null) return null;
            GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().duration(20).EUt(30);
            builder.inputItems(item.get());
            builder.outputItems(output);
            GTRecipe recipe = builder.buildRawRecipe();
            recipe = GTORecipeModifiers.accurateParallel(this, recipe, getParallel());
            if (recipe != null && RecipeRunnerHelper.matchRecipe(this, recipe) && RecipeRunnerHelper.matchTickRecipe(this, recipe)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
