package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

import static com.gto.gtocore.common.data.GTORecipeTypes.DISASSEMBLY_RECIPES;
import static com.gto.gtocore.common.data.GTORecipes.DISASSEMBLY_RECORD;

public class GenerateDisassembly {

    private static final String[] inputItem = { "suprachronal_", "_circuit", "_processor", "_assembly",
            "_computer", "_mainframe", "circuit_resonatic_" };

    private static final String[] outputItem = { "_frame", "_fence", "_electric_motor",
            "_electric_pump", "_conveyor_module", "_electric_piston", "_robot_arm", "_field_generator",
            "_emitter", "_sensor", "smd_", "_lamp", "_crate", "_drum", "_machine_casing",
            "ae2:blank_pattern", "gtceu:carbon_nanoswarm" };

    private static boolean isExcludeItems(String id, String[] excludeItems) {
        for (String pattern : excludeItems) {
            if (id.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    public static void generateDisassembly(GTRecipeBuilder r, Consumer<FinishedRecipe> p) {
        List<Content> c = r.output.getOrDefault(ItemRecipeCapability.CAP, null);
        if (c == null) {
            GTOCore.LOGGER.atError().log("配方{}没有输出", r.id);
            return;
        }
        ItemStack[] outputs = ItemRecipeCapability.CAP
                .of(c.get(0).getContent()).getItems();
        if (outputs.length == 0) return;
        ItemStack output = outputs[0];
        String id = output.kjs$getId();
        boolean cal = r.recipeType == GTRecipeTypes.get("circuit_assembly_line");
        if (isExcludeItems(id, outputItem)) return;
        GTRecipeBuilder builder = DISASSEMBLY_RECIPES.recipeBuilder(new ResourceLocation(id))
                .inputItems(output)
                .duration(1)
                .EUt(1);
        boolean hasOutput = false;
        if (!DISASSEMBLY_RECORD.remove(id) || cal) {
            List<Content> itemList = r.input.getOrDefault(ItemRecipeCapability.CAP, null);
            List<Content> fluidList = r.input.getOrDefault(FluidRecipeCapability.CAP, null);
            if (itemList != null) {
                for (Content content : itemList) {
                    ItemStack[] items = ItemRecipeCapability.CAP
                            .of(content.getContent()).getItems();
                    if (items.length == 0) return;
                    ItemStack item = items[0];
                    if (content.chance == ChanceLogic.getMaxChancedValue() && !item.isEmpty() && !item.hasTag()) {
                        if (cal || !isExcludeItems(item.kjs$getId(), inputItem)) {
                            builder.outputItems(item);
                            hasOutput = true;
                        }
                    }
                }
            }
            if (fluidList != null) {
                for (Content content : fluidList) {
                    FluidIngredient fluid = FluidRecipeCapability.CAP
                            .of(content.getContent());
                    if (content.chance == ChanceLogic.getMaxChancedValue() && !fluid.isEmpty()) {
                        builder.outputFluids(fluid);
                        hasOutput = true;
                    }
                }
            }
        }
        DISASSEMBLY_RECORD.add(id);
        if (hasOutput) builder.save(p);
    }
}
