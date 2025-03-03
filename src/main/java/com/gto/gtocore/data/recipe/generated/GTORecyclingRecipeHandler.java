package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

interface GTORecyclingRecipeHandler {

    List<Object> PREFIXES = Arrays.asList(
            ingot, gem, rod, plate, ring, rodLong, foil, bolt, screw,
            nugget, gearSmall, gear, frameGt, plateDense, spring, springSmall,
            block, wireFine, rotor, lens, turbineBlade, round, plateDouble, dust,
            GTOTagPrefix.curvedPlate, GTOTagPrefix.motorEnclosure, GTOTagPrefix.pumpBarrel,
            GTOTagPrefix.pistonHousing, GTOTagPrefix.emitterBases, GTOTagPrefix.sensorCasing,
            GTOTagPrefix.fieldGeneratorCasing,
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("gem"),
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("wireGt"),
            (Predicate<TagPrefix>) orePrefix -> orePrefix.name().startsWith("pipe"));

    Set<TagPrefix> IGNORE_ARC_SMELTING = Set.of(ingot, gem, nugget);

    static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        for (TagPrefix prefix : TagPrefix.values()) {
            if (PREFIXES.stream().anyMatch(object -> {
                if (object instanceof TagPrefix) {
                    return object == prefix;
                }
                if (object instanceof Predicate) {
                    return ((Predicate<TagPrefix>) object).test(prefix);
                }
                return false;
            })) {
                processCrushing(provider, prefix, material);
            }
        }
    }

    private static void processCrushing(@NotNull Consumer<FinishedRecipe> provider, @NotNull TagPrefix prefix, @NotNull Material material) {
        ItemStack stack = ChemicalHelper.get(prefix, material);
        if (stack.isEmpty()) return;
        ArrayList<MaterialStack> materialStacks = new ArrayList<>();
        materialStacks.add(new MaterialStack(material, prefix.getMaterialAmount(material)));
        materialStacks.addAll(prefix.secondaryMaterials());
        // only ignore arc smelting for blacklisted prefixes if yielded material is the same as input material
        // if arc smelting gives different material, allow it
        boolean ignoreArcSmelting = IGNORE_ARC_SMELTING.contains(prefix) &&
                !(material.hasProperty(PropertyKey.INGOT) &&
                        material.getProperty(PropertyKey.INGOT).getArcSmeltingInto() != material);
        RecyclingRecipes.registerRecyclingRecipes(provider, stack, materialStacks,
                ignoreArcSmelting, prefix);
    }
}
