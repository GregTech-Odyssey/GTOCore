package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipeBuilder;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import com.fast.fastcollection.OpenCacheHashSet;

import java.util.Collection;
import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.*;

public final class GenerateDisassembly {

    public static final Set<ResourceLocation> DISASSEMBLY_RECORD = new OpenCacheHashSet<>();

    public static final Set<ResourceLocation> DISASSEMBLY_BLACKLIST = new OpenCacheHashSet<>();

    private static final String[] outputItem = { "_frame", "_fence", "_electric_motor",
            "_electric_pump", "_conveyor_module", "_electric_piston", "_robot_arm", "_field_generator",
            "_emitter", "_sensor", "smd_", "_lamp", "_integrated_control_core", "ae2:blank_pattern",
            "gtocore:carbon_nanites", "gtmthings:virtual_item_provider", "gtocore:me_wildcard_pattern_buffer" };

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
        var c = recipeBuilder.getItemOutputs();
        if (c.isEmpty()) {
            GTOCore.LOGGER.error("配方{}没有输出", recipeBuilder.getId());
            return;
        }
        var outIng = c.getFirst().inner;
        var output = outIng.getItem();
        if (output.isEmpty()) return;
        var item = output.getItem();
        var amount = outIng.getAmount();
        if (recipeBuilder.getRecipeType() == LASER_WELDER_RECIPES && !(item instanceof MetaMachineItem)) {
            return;
        }
        ResourceLocation id = ItemUtils.getIdLocation(item);
        if (DISASSEMBLY_BLACKLIST.contains(id)) return;
        boolean cal = recipeBuilder.getRecipeType() == CIRCUIT_ASSEMBLY_LINE_RECIPES;
        ResourceLocation typeid = RecipeBuilder.getTypeID(id, DISASSEMBLY_RECIPES);
        if (cal && RecipeBuilder.get(typeid) != null) return;
        if ((!cal && DISASSEMBLY_RECORD.remove(id)) || isExcludeItems(id.toString())) {
            DISASSEMBLY_BLACKLIST.add(id);
            RecipeBuilder.remove(typeid);
            return;
        }
        RecipeBuilder builder = DISASSEMBLY_RECIPES.recipeBuilder(id)
                .inputItems(item, amount)
                .duration(recipeBuilder.getDuration())
                .EUt(eut);
        boolean hasOutput = false;
        var itemList = recipeBuilder.getItemInputs();
        var fluidList = recipeBuilder.getFluidInputs();
        for (var content : itemList) {
            if (content.chance == Content.MAX_CHANCE) {
                var input = content.inner;
                Ingredient inner = input.inner;
                a:
                for (Ingredient.Value value : inner.values) {
                    if (value instanceof Ingredient.ItemValue itemValue) {
                        Collection<ItemStack> stacks = itemValue.getItems();
                        if (stacks.size() == 1) {
                            for (ItemStack stack : stacks) {
                                if (!stack.isEmpty() && !stack.hasTag()) {
                                    builder.outputItems(input);
                                    hasOutput = true;
                                    break a;
                                }
                            }
                        }
                    } else if (value instanceof Ingredient.TagValue tagValue) {
                        Integer i = Tags.CIRCUITS_ARRAY.get(tagValue.tag);
                        if (i != null) {
                            builder.outputItems(GTOItems.UNIVERSAL_CIRCUIT[i].get(), input.getAmount());
                            break;
                        }
                    }
                }
            }
        }
        for (var content : fluidList) {
            FluidIngredient fluid = content.inner;
            if (content.chance == Content.MAX_CHANCE && !fluid.isEmpty()) {
                builder.outputFluids(fluid);
                hasOutput = true;
            }
        }
        if (hasOutput) builder.save();
        DISASSEMBLY_RECORD.add(id);
    }
}
