package com.gtocore.mixin.gtm.recipe;

import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.chemical.material.ItemMaterialData.ITEM_MATERIAL_INFO;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.IS_MAGNETIC;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ARC_FURNACE_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.MACERATOR_RECIPES;

@Mixin(RecyclingRecipes.class)
public abstract class RecyclingRecipesMixin {

    @Shadow(remap = false)
    private static int calculateVoltageMultiplier(List<MaterialStack> materials) {
        return 0;
    }

    @Shadow(remap = false)
    private static List<ItemStack> finalizeOutputs(List<MaterialStack> materials, int maxOutputs, Function<MaterialStack, ItemStack> toItemStackMapper) {
        return null;
    }

    @Shadow(remap = false)
    private static int calculateDuration(List<ItemStack> materials) {
        return 0;
    }

    @Shadow(remap = false)
    private static List<MaterialStack> combineStacks(List<MaterialStack> rawList) {
        return null;
    }

    @Shadow(remap = false)
    private static boolean needsRecyclingCategory(@Nullable TagPrefix prefix, @Nullable MaterialStack inputStack, @NotNull List<ItemStack> outputs) {
        return false;
    }

    @Shadow(remap = false)
    private static MaterialStack getArcSmeltingResult(MaterialStack materialStack) {
        return null;
    }

    @Shadow(remap = false)
    private static ItemStack getArcIngotOrDust(@NotNull MaterialStack stack) {
        return null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {
        if (GTCEu.isDev()) return;
        for (var entry : ITEM_MATERIAL_INFO.entrySet()) {
            var item = entry.getKey();
            if (item instanceof IGTTool) continue;
            registerRecyclingRecipes(entry.getKey().getDefaultInstance(), entry.getValue().getMaterials(), false, null);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void registerRecyclingRecipes(ItemStack input, List<MaterialStack> components, boolean ignoreArcSmelting, @Nullable TagPrefix prefix) {
        if (GTCEu.isDev()) return;
        List<MaterialStack> materials = components.stream()
                .filter(stack -> stack.material().hasProperty(PropertyKey.DUST))
                .filter(stack -> stack.amount() >= M / 9)
                .sorted(Comparator.comparingLong(ms -> -ms.amount()))
                .toList();
        if (materials.isEmpty()) return;

        int voltageMultiplier = calculateVoltageMultiplier(materials);

        if (prefix != TagPrefix.dust) {
            registerMaceratorRecycling(input, materials, voltageMultiplier);
        }
        if (ignoreArcSmelting) return;

        if (materials.size() == 1) {
            Material m = materials.get(0).material();

            // skip non-ingot materials
            if (!m.hasProperty(PropertyKey.INGOT)) {
                return;
            }

            // Skip Ingot -> Ingot Arc Recipes
            if (ChemicalHelper.getPrefix(input.getItem()) == TagPrefix.ingot &&
                    m.getProperty(PropertyKey.INGOT).getArcSmeltingInto() == m) {
                return;
            }

            // Prevent Magnetic dust -> Regular Ingot Arc Furnacing, avoiding the EBF recipe
            // "I will rework magnetic materials soon" - DStrand1
            if (prefix == TagPrefix.dust && m.hasFlag(IS_MAGNETIC)) {
                return;
            }
        }
        registerArcRecycling(input, materials, prefix);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerMaceratorRecycling(ItemStack input, List<MaterialStack> materials, int multiplier) {
        List<ItemStack> outputs = finalizeOutputs(materials, MACERATOR_RECIPES.getMaxOutputs(ItemRecipeCapability.CAP), ChemicalHelper::getDust);
        if (outputs != null && !outputs.isEmpty()) {
            ResourceLocation itemPath = ItemUtils.getIdLocation(input.getItem());
            var builder = MACERATOR_RECIPES.recipeBuilder("macerate_" + itemPath.getPath()).outputItems(outputs.toArray(ItemStack[]::new)).duration(calculateDuration(outputs)).EUt(2L * (long) multiplier);
            builder.inputItems(input);
            builder.category(GTRecipeCategories.MACERATOR_RECYCLING);
            builder.save();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerArcRecycling(ItemStack input, List<MaterialStack> materials, @Nullable TagPrefix prefix) {
        MaterialStack ms = ChemicalHelper.getMaterialStack(input);
        if (prefix != TagPrefix.dust || ms.isEmpty() || !ms.material().hasProperty(PropertyKey.BLAST)) {
            if (prefix != TagPrefix.block) {
                materials = combineStacks(materials.stream().map(RecyclingRecipesMixin::getArcSmeltingResult).filter(Objects::nonNull).collect(Collectors.toList()));
                List<ItemStack> outputs = finalizeOutputs(materials, ARC_FURNACE_RECIPES.getMaxOutputs(ItemRecipeCapability.CAP), RecyclingRecipesMixin::getArcIngotOrDust);
                if (outputs != null && !outputs.isEmpty()) {
                    ResourceLocation itemPath = ItemUtils.getIdLocation(input.getItem());
                    var builder = ARC_FURNACE_RECIPES.recipeBuilder("arc_" + itemPath.getPath()).outputItems(outputs.toArray(ItemStack[]::new)).duration(calculateDuration(outputs)).EUt(GTValues.VA[1]);
                    builder.inputItems(input.copy());

                    if (needsRecyclingCategory(prefix, ms, outputs)) {
                        builder.category(GTRecipeCategories.ARC_FURNACE_RECYCLING);
                    }

                    builder.save();
                }
            } else {
                if (!ms.isEmpty() && !ms.material().hasProperty(PropertyKey.GEM)) {
                    ItemStack output = ChemicalHelper.get(TagPrefix.ingot, ms.material().getProperty(PropertyKey.INGOT).getArcSmeltingInto(), (int) (TagPrefix.block.getMaterialAmount(ms.material()) / 3628800L));
                    ResourceLocation itemPath = ItemUtils.getIdLocation(input.getItem());
                    var builder = ARC_FURNACE_RECIPES.recipeBuilder("arc_" + itemPath.getPath()).outputItems(output).duration(calculateDuration(Collections.singletonList(output))).EUt(GTValues.VA[1]);
                    builder.inputItems(input.copy());

                    if (ms.material().hasFlag(MaterialFlags.IS_MAGNETIC) || ms.material() == ms.material().getProperty(PropertyKey.INGOT).getArcSmeltingInto()) {
                        builder.category(GTRecipeCategories.ARC_FURNACE_RECYCLING);
                    }

                    builder.save();
                }

            }
        }
    }
}
