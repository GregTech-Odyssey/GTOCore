package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.api.GTValues.L;
import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.IS_MAGNETIC;

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
    public static void init(Consumer<FinishedRecipe> provider) {
        for (Map.Entry<ItemStack, ItemMaterialInfo> entry : ChemicalHelper.getAllItemInfos()) {
            ItemStack itemStack = entry.getKey();
            if (itemStack.getItem() instanceof IGTTool) continue;
            RecyclingRecipes.registerRecyclingRecipes(provider, itemStack, new ArrayList<>(entry.getValue().getMaterials()), false, null);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void registerRecyclingRecipes(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> components, boolean ignoreArcSmelting, @Nullable TagPrefix prefix) {
        List<MaterialStack> materials = components.stream()
                .filter(stack -> stack.material().hasProperty(PropertyKey.DUST))
                .filter(stack -> stack.amount() >= M / 9)
                .sorted(Comparator.comparingLong(ms -> -ms.amount()))
                .toList();
        if (materials.isEmpty()) return;

        int voltageMultiplier = calculateVoltageMultiplier(components);

        if (prefix != null && prefix != TagPrefix.dust) {
            registerMaceratorRecycling(provider, input, components, voltageMultiplier);
        }
        if (prefix == TagPrefix.ingot || prefix == TagPrefix.dust) {
            registerExtractorRecycling(provider, input, components, voltageMultiplier, prefix);
        }
        if (ignoreArcSmelting) return;

        if (components.size() == 1) {
            Material m = components.get(0).material();

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
        registerArcRecycling(provider, input, components, prefix);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerMaceratorRecycling(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> materials, int multiplier) {
        List<ItemStack> outputs = finalizeOutputs(materials, GTRecipeTypes.MACERATOR_RECIPES.getMaxOutputs(ItemRecipeCapability.CAP), ChemicalHelper::getDust);
        if (outputs != null && !outputs.isEmpty()) {
            ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(input.getItem());
            GTRecipeBuilder builder = GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder("macerate_" + itemPath.getPath()).outputItems(outputs.toArray(ItemStack[]::new)).duration(calculateDuration(outputs)).EUt(2L * (long) multiplier);
            builder.inputItems(input);
            builder.category(GTRecipeCategories.MACERATOR_RECYCLING);
            builder.save(provider);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerExtractorRecycling(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> materials, int multiplier, @Nullable TagPrefix prefix) {
        if (prefix != null && prefix.secondaryMaterials().isEmpty()) {
            MaterialStack ms = ChemicalHelper.getMaterial(input);
            if (ms == null || ms.material() == null) {
                return;
            }
            Material m = ms.material();
            if (m.hasProperty(PropertyKey.INGOT) && m.getProperty(PropertyKey.INGOT).getMacerateInto() != m) {
                m = m.getProperty(PropertyKey.INGOT).getMacerateInto();
            }
            if (!m.hasProperty(PropertyKey.FLUID) || m.getFluid() == null ||
                    (prefix == TagPrefix.dust && m.hasProperty(PropertyKey.BLAST))) {
                return;
            }

            ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(input.getItem());
            GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("extract_" + itemPath.getPath())
                    .outputFluids(m.getFluid((int) (ms.amount() * L / M)))
                    .duration((int) Math.max(1, ms.amount() * ms.material().getMass() / M))
                    .blastFurnaceTemp(Math.max(800, (int) (ms.material().getBlastTemperature() * 0.6)))
                    .EUt((long) GTValues.VA[GTValues.LV] * multiplier)
                    .inputItems(input)
                    .save(provider);
            return;
        }

        // Find MaterialStacks for fluid and item outputs simultaneously
        MaterialStack fluidMs = null;
        MaterialStack itemMs = null;
        for (MaterialStack ms : materials) {
            if (fluidMs == null && ms.material().hasProperty(PropertyKey.FLUID) && ms.material().getFluid() != null) {
                fluidMs = ms;
            } else if (fluidMs != null && !ms.material().equals(fluidMs.material())) {
                itemMs = ms;
            }
            if (itemMs != null) {
                break;
            }
        }

        if (fluidMs == null) return;

        // Calculate the duration based off of those two possible outputs.
        long duration = fluidMs.amount() * fluidMs.material().getMass();
        if (itemMs != null) duration += (itemMs.amount() * itemMs.material().getMass());
        duration = Math.max(1L, duration / M);

        // Build the final Recipe.
        ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(input.getItem());
        GTRecipeBuilder extractorBuilder = GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES
                .recipeBuilder("extract_" + itemPath.getPath())
                .outputFluids(fluidMs.material().getFluid((int) (fluidMs.amount() * L / M)))
                .duration((int) duration)
                .blastFurnaceTemp(Math.max(800, (int) (fluidMs.material().getBlastTemperature() * 0.6)))
                .EUt((long) GTValues.VA[GTValues.LV] * multiplier);

        extractorBuilder.inputItems(input);

        // Null check the Item before adding it to the Builder.
        if (itemMs != null) {
            ItemStack outputStack = ChemicalHelper.getIngotOrDust(itemMs);
            if (!outputStack.isEmpty()) {
                extractorBuilder.outputItems(outputStack);
            }
        }

        extractorBuilder.save(provider);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static void registerArcRecycling(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> materials, @Nullable TagPrefix prefix) {
        MaterialStack ms = ChemicalHelper.getMaterial(input);
        if (prefix != TagPrefix.dust || ms == null || !ms.material().hasProperty(PropertyKey.BLAST)) {
            if (prefix != TagPrefix.block) {
                materials = combineStacks(materials.stream().map(RecyclingRecipesMixin::getArcSmeltingResult).filter(Objects::nonNull).collect(Collectors.toList()));
                List<ItemStack> outputs = finalizeOutputs(materials, GTRecipeTypes.ARC_FURNACE_RECIPES.getMaxOutputs(ItemRecipeCapability.CAP), stack -> getArcIngotOrDust(stack));
                if (outputs != null && !outputs.isEmpty()) {
                    ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(input.getItem());
                    GTRecipeBuilder builder = GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder("arc_" + itemPath.getPath()).outputItems(outputs.toArray(ItemStack[]::new)).duration(calculateDuration(outputs)).EUt(GTValues.VA[1]);
                    builder.inputItems(input.copy());

                    if (needsRecyclingCategory(prefix, ms, outputs)) {
                        builder.category(GTRecipeCategories.ARC_FURNACE_RECYCLING);
                    }

                    builder.save(provider);
                }
            } else {
                if (ms != null && !ms.material().hasProperty(PropertyKey.GEM)) {
                    ItemStack output = ChemicalHelper.get(TagPrefix.ingot, ms.material().getProperty(PropertyKey.INGOT).getArcSmeltingInto(), (int) (TagPrefix.block.getMaterialAmount(ms.material()) / 3628800L));
                    ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(input.getItem());
                    GTRecipeBuilder builder = GTRecipeTypes.ARC_FURNACE_RECIPES.recipeBuilder("arc_" + itemPath.getPath()).outputItems(output).duration(calculateDuration(Collections.singletonList(output))).EUt(GTValues.VA[1]);
                    builder.inputItems(input.copy());

                    if (ms.material().hasFlag(MaterialFlags.IS_MAGNETIC) || ms.material() == ms.material().getProperty(PropertyKey.INGOT).getArcSmeltingInto()) {
                        builder.category(GTRecipeCategories.ARC_FURNACE_RECYCLING);
                    }

                    builder.save(provider);
                }

            }
        }
    }
}
