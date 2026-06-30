package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.world.item.Item;

import com.gto.datasynclib.util.holder.ObjHolder;
import com.periut.chisel.block.ChiselGroupLookup;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class ChiselMachine extends CustomParallelMultiblockMachine implements ICustomRecipeLogicHolder {

    public ChiselMachine(MetaMachineBlockEntity holder) {
        super(holder, m -> 1L << (2 * (m.getTier() - 1)));
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        AtomicInteger c = new AtomicInteger();
        ObjHolder<Item> item = new ObjHolder<>();
        unit.fastForEachItems(false, (stack, amount) -> {
            if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                c.addAndGet(IntCircuitBehaviour.getCircuitConfiguration(stack));
            } else {
                item.set(stack.getItem());
            }
        });
        if (c.get() > 0 && item.get() != null) {
            List<Item> list = ChiselGroupLookup.getBlocksInGroup(item.get());
            if (list.isEmpty()) return null;
            Item output = list.get(Math.min(list.size(), c.get()) - 1);
            if (output == null) return null;
            RecipeBuilder builder = getRecipeBuilder().duration(20).EUt(30);
            builder.inputItems(item.get());
            builder.outputItems(output);
            return builder.build();
        }
        return null;
    }
}
