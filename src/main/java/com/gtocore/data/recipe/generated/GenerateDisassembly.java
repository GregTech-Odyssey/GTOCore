package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.*;

public final class GenerateDisassembly {

    public static final Set<ResourceLocation> DISASSEMBLY_RECORD = new ObjectOpenHashSet<>();

    public static final Set<ResourceLocation> DISASSEMBLY_BLACKLIST = new ObjectOpenHashSet<>();

    private static final String[] outputItem = { "_frame", "_fence", "_electric_motor",
            "_electric_pump", "_conveyor_module", "_electric_piston", "_robot_arm", "_field_generator",
            "_emitter", "_sensor", "smd_", "_lamp", "ae2:blank_pattern", "gtocore:carbon_nanites", "cell_component" };

    private static boolean isExcludeItems(String id) {
        for (String pattern : outputItem) {
            if (id.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    public static void generateDisassembly(GTRecipeBuilder recipeBuilder) {
        long eut = recipeBuilder.EUt();
        if (eut < 1) return;
        List<Content> c = recipeBuilder.output.getOrDefault(ItemRecipeCapability.CAP, null);
        if (c == null) {
            GTOCore.LOGGER.error("配方{}没有输出", recipeBuilder.id);
            return;
        }
        Ingredient output = ItemRecipeCapability.CAP.of(c.get(0).getContent());
        if (output.isEmpty()) return;
        var item = ItemUtils.getFirstSized(output).getItem();
        if (recipeBuilder.recipeType == LASER_WELDER_RECIPES && !(item instanceof MetaMachineItem)) {
            return;
        }
        ResourceLocation id = ItemUtils.getIdLocation(item);
        if (DISASSEMBLY_BLACKLIST.contains(id)) return;
        boolean cal = recipeBuilder.recipeType == CIRCUIT_ASSEMBLY_LINE_RECIPES;
        ResourceLocation typeid = RecipeBuilder.getTypeID(id, DISASSEMBLY_RECIPES);
        if (cal && RecipeBuilder.RECIPE_MAP.containsKey(typeid)) return;
        if ((!cal && DISASSEMBLY_RECORD.remove(id)) || isExcludeItems(id.toString())) {
            DISASSEMBLY_BLACKLIST.add(id);
            RecipeBuilder.RECIPE_MAP.remove(typeid);
            return;
        }
        RecipeBuilder builder = DISASSEMBLY_RECIPES.recipeBuilder(id)
                .inputItems(FastSizedIngredient.copy(output))
                .duration(recipeBuilder.duration)
                .EUt(eut);
        boolean hasOutput = false;
        List<Content> itemList = recipeBuilder.input.getOrDefault(ItemRecipeCapability.CAP, null);
        List<Content> fluidList = recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, null);
        if (itemList != null) {
            for (Content content : itemList) {
                Ingredient input = ItemRecipeCapability.CAP.of(content.getContent());
                if (input instanceof FastSizedIngredient sizedIngredient) {
                    Ingredient inner = sizedIngredient.getInner();
                    a:
                    for (Ingredient.Value value : inner.values) {
                        if (value instanceof Ingredient.ItemValue itemValue) {
                            Collection<ItemStack> stacks = itemValue.getItems();
                            if (stacks.size() == 1) {
                                for (ItemStack stack : stacks) {
                                    if (!stack.isEmpty() && content.chance == ChanceLogic.getMaxChancedValue() && !stack.hasTag()) {
                                        builder.output(ItemRecipeCapability.CAP, FastSizedIngredient.copy(input));
                                        hasOutput = true;
                                        break a;
                                    }
                                }
                            }
                        } else if (value instanceof Ingredient.TagValue tagValue) {
                            Integer i = Tags.CIRCUITS_ARRAY.get(tagValue.tag);
                            if (i != null) {
                                builder.outputItems(GTOItems.UNIVERSAL_CIRCUIT[i].get(), sizedIngredient.getSaturatedAmount());
                                break;
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
