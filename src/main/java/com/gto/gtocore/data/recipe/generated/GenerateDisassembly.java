package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.gto.gtocore.common.data.GTORecipeTypes.CIRCUIT_ASSEMBLY_LINE_RECIPES;
import static com.gto.gtocore.common.data.GTORecipeTypes.DISASSEMBLY_RECIPES;

public interface GenerateDisassembly {

    Set<ResourceLocation> DISASSEMBLY_RECORD = new ObjectOpenHashSet<>();

    Set<ResourceLocation> DISASSEMBLY_BLACKLIST = new ObjectOpenHashSet<>();

    String[] outputItem = { "_frame", "_fence", "_electric_motor",
            "_electric_pump", "_conveyor_module", "_electric_piston", "_robot_arm", "_field_generator",
            "_emitter", "_sensor", "smd_", "_lamp", "ae2:blank_pattern", "gtceu:carbon_nanites" };

    private static boolean isExcludeItems(String id) {
        for (String pattern : outputItem) {
            if (id.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    static void generateDisassembly(GTRecipeBuilder recipeBuilder, Consumer<FinishedRecipe> consumer) {
        List<Content> c = recipeBuilder.output.getOrDefault(ItemRecipeCapability.CAP, null);
        if (c == null) {
            GTOCore.LOGGER.atError().log("配方{}没有输出", recipeBuilder.id);
            return;
        }
        Ingredient output = ItemRecipeCapability.CAP.of(c.get(0).getContent());
        if (output.isEmpty()) return;
        ResourceLocation id = ItemUtils.getIdLocation(ItemUtils.getFirstSized(output).getItem());
        if (DISASSEMBLY_BLACKLIST.contains(id)) return;
        boolean cal = recipeBuilder.recipeType == CIRCUIT_ASSEMBLY_LINE_RECIPES;
        ResourceLocation typeid = GTORecipeBuilder.getTypeID(id, DISASSEMBLY_RECIPES);
        if (cal && GTORecipes.GT_RECIPE_MAP.containsKey(typeid)) return;
        if ((!cal && DISASSEMBLY_RECORD.remove(id)) || isExcludeItems(id.toString())) {
            DISASSEMBLY_BLACKLIST.add(id);
            GTORecipes.GT_RECIPE_MAP.remove(typeid);
            return;
        }
        GTORecipeBuilder builder = DISASSEMBLY_RECIPES.recipeBuilder(id)
                .inputItems(SizedIngredient.copy(output))
                .duration(recipeBuilder.duration)
                .EUt(recipeBuilder.EUt());
        boolean hasOutput = false;
        List<Content> itemList = recipeBuilder.input.getOrDefault(ItemRecipeCapability.CAP, null);
        List<Content> fluidList = recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, null);
        if (itemList != null) {
            for (Content content : itemList) {
                Ingredient input = ItemRecipeCapability.CAP.of(content.getContent());
                if (input instanceof SizedIngredient sizedIngredient) {
                    Ingredient inner = sizedIngredient.getInner();
                    a:
                    for (Ingredient.Value value : ((IngredientAccessor) inner).getValues()) {
                        if (value instanceof Ingredient.ItemValue itemValue) {
                            Collection<ItemStack> stacks = itemValue.getItems();
                            if (stacks.size() == 1) {
                                for (ItemStack item : stacks) {
                                    if (!item.isEmpty() && content.chance == ChanceLogic.getMaxChancedValue() && !item.hasTag()) {
                                        builder.output(ItemRecipeCapability.CAP, SizedIngredient.copy(input));
                                        hasOutput = true;
                                        break a;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (fluidList != null) {
            for (Content content : fluidList) {
                FluidIngredient fluid = FluidRecipeCapability.CAP.of(content.getContent());
                if (content.chance == ChanceLogic.getMaxChancedValue() && !fluid.isEmpty()) {
                    builder.outputFluids(fluid.copy());
                    hasOutput = true;
                }
            }
        }
        if (hasOutput) builder.save();
        DISASSEMBLY_RECORD.add(id);
    }
}
