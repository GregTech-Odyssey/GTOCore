package com.gtocore.common.recipe.custom;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.item.ItemMap;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

public final class RecyclerLogic implements GTRecipeType.ICustomRecipeLogic {

    @Override
    public @Nullable GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
        if (holder instanceof CustomParallelMultiblockMachine parallelMultiblockMachine && MachineUtils.notConsumableItem(parallelMultiblockMachine, GTOItems.SCRAP_BOX.asStack())) {
            RecipeBuilder builder = parallelMultiblockMachine.getRecipeBuilder().EUt(480);
            int parallel = MathUtil.saturatedCast(MachineUtils.getItemAmount(parallelMultiblockMachine, GTOItems.SCRAP_BOX.get())[0]);
            builder.duration(20 * parallel).inputItems(GTOItems.SCRAP_BOX.asStack(parallel));
            Reference2IntOpenHashMap<Item> map = new Reference2IntOpenHashMap<>();
            int cycle = Math.min(64, parallel);
            int multiplier = Math.max(1, parallel / cycle);
            for (int i = 0; i < cycle; i++) {
                map.addTo(ItemMap.getScrapItem(), multiplier);
            }
            if (multiplier > 1) {
                for (int i = 0, remainder = parallel % cycle; i < remainder; i++) {
                    map.addTo(ItemMap.getScrapItem(), 1);
                }
            }
            map.forEach(builder::outputItems);
            Recipe recipe = builder.buildRawRecipe();
            recipe.data.putBoolean("isCustom", true);
            return recipe;
        }
        return null;
    }

    @Override
    public void buildRepresentativeRecipes() {
        ItemStack stack = GTOItems.SCRAP.asStack();
        stack.setHoverName(Component.translatable("gtocore.recipe.recycler.random_output"));
        GTORecipeTypes.RECYCLER_RECIPES.addToMainCategory(GTORecipeTypes.RECYCLER_RECIPES
                .recipeBuilder("random")
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .outputItems(stack)
                .EUt(120)
                .duration(20)
                .buildRawRecipe());
    }
}
