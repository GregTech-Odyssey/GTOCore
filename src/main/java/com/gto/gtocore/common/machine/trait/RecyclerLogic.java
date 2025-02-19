package com.gto.gtocore.common.machine.trait;

import com.gto.gtocore.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.item.ItemMap;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class RecyclerLogic implements GTRecipeType.ICustomRecipeLogic {

    @Override
    public @Nullable GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
        if (!holder.hasProxies()) return null;
        if (holder instanceof CustomParallelMultiblockMachine parallelMultiblockMachine && MachineUtils.notConsumableItem(parallelMultiblockMachine, GTOItems.SCRAP_BOX.asStack())) {
            GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().EUt(480);
            int parallel = MachineUtils.getItemAmount(parallelMultiblockMachine, GTOItems.SCRAP_BOX.get())[0];
            builder.duration(20 * parallel).inputItems(GTOItems.SCRAP_BOX.asStack(parallel));
            Map<Item, Integer> map = new Object2IntOpenHashMap<>();
            for (int i = 0; i < parallel; i++) {
                ItemStack stack = ItemMap.getScrapItem();
                if (map.containsKey(stack.getItem())) {
                    map.put(stack.getItem(), map.get(stack.getItem()) + 1);
                } else {
                    map.put(stack.getItem(), 1);
                }
            }
            map.forEach(builder::outputItems);
            GTRecipe recipe = builder.buildRawRecipe();
            recipe.data.putBoolean("isCustom", true);
            return recipe;
        }
        return null;
    }

    @Override
    public void buildRepresentativeRecipes() {
        ItemStack stack = GTOItems.SCRAP.asStack();
        stack.setHoverName(Component.literal("Random item"));
        GTORecipeTypes.RECYCLER_RECIPES.addToMainCategory(GTORecipeTypes.RECYCLER_RECIPES
                .recipeBuilder("random")
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .outputItems(stack)
                .EUt(120)
                .duration(20)
                .buildRawRecipe());
    }
}
