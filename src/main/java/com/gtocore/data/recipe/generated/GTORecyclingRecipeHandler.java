package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.L;
import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

final class GTORecyclingRecipeHandler {

    private static final Set<TagPrefix> IGNORE_ARC_SMELTING = Set.of(ingot, gem, nugget);

    static void run(@NotNull Material material) {
        for (TagPrefix prefix : TagPrefix.values()) {
            if (prefix.generateRecycling()) {
                processCrushing(prefix, material);
            }
        }
    }

    private static void processCrushing(@NotNull TagPrefix prefix, @NotNull Material material) {
        ItemStack stack = ChemicalHelper.get(prefix, material);
        if (stack.isEmpty()) return;
        ArrayList<MaterialStack> materialStacks = new ArrayList<>();
        long amount = prefix.getMaterialAmount(material);
        materialStacks.add(new MaterialStack(material, amount));
        materialStacks.addAll(prefix.secondaryMaterials());
        // only ignore arc smelting for blacklisted prefixes if yielded material is the same as input material
        // if arc smelting gives different material, allow it
        boolean ignoreArcSmelting = IGNORE_ARC_SMELTING.contains(prefix) &&
                !(material.hasProperty(PropertyKey.INGOT) &&
                        material.getProperty(PropertyKey.INGOT).getArcSmeltingInto() != material);
        RecyclingRecipes.registerRecyclingRecipes(stack, materialStacks,
                ignoreArcSmelting, prefix);

        if (!material.hasProperty(PropertyKey.FLUID) || material.getFluid() == null || (prefix == TagPrefix.dust && material.hasProperty(PropertyKey.BLAST))) return;
        GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("extract_" + ItemUtils.getIdLocation(stack.getItem()).getPath())
                .outputFluids(material.getFluid((int) (amount * L / M)))
                .duration((int) Math.max(1, amount * material.getMass() / M))
                .blastFurnaceTemp(Math.max(800, (int) (material.getBlastTemperature() * 0.6)))
                .EUt(GTOUtils.getVoltageMultiplier(material))
                .inputItems(stack)
                .save();
    }
}
