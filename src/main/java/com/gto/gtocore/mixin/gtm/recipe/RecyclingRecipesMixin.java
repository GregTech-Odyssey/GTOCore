package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.data.GTORecipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.L;
import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.IS_MAGNETIC;

@Mixin(RecyclingRecipes.class)
public class RecyclingRecipesMixin {

    @Shadow(remap = false)
    private static int calculateVoltageMultiplier(List<MaterialStack> materials) {
        return 0;
    }

    @Shadow(remap = false)
    private static void registerArcRecycling(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> materials, @Nullable TagPrefix prefix) {}

    @Shadow(remap = false)
    private static void registerMaceratorRecycling(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> materials, int multiplier) {}

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        GTORecipes.recipeAddition(provider);
        for (Map.Entry<ItemStack, ItemMaterialInfo> entry : ChemicalHelper.getAllItemInfos()) {
            ItemStack itemStack = entry.getKey();
            if (itemStack.getItem() instanceof IGTTool) continue;
            gTLCore$registerRecyclingRecipes(provider, itemStack, new ArrayList<>(entry.getValue().getMaterials()), false, null);
        }
    }

    @Unique
    private static void gTLCore$registerRecyclingRecipes(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> components, boolean ignoreArcSmelting, @Nullable TagPrefix prefix) {
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
            gTLCore$registerExtractorRecycling(provider, input, components, voltageMultiplier, prefix);
        }
        if (ignoreArcSmelting) return;

        if (components.size() == 1) {
            Material m = components.get(0).material();

            if (!m.hasProperty(PropertyKey.INGOT)) {
                return;
            }

            if (ChemicalHelper.getPrefix(input.getItem()) == TagPrefix.ingot &&
                    m.getProperty(PropertyKey.INGOT).getArcSmeltingInto() == m) {
                return;
            }

            if (prefix == TagPrefix.dust && m.hasFlag(IS_MAGNETIC)) {
                return;
            }

            if (prefix == TagPrefix.block) return;
        }
        registerArcRecycling(provider, input, components, prefix);
    }

    @Unique
    private static void gTLCore$registerExtractorRecycling(Consumer<FinishedRecipe> provider, ItemStack input,
                                                           List<MaterialStack> materials, int multiplier,
                                                           @Nullable TagPrefix prefix) {
        UnificationEntry entry = ChemicalHelper.getUnificationEntry(input.getItem());
        TagKey<Item> inputTag = null;
        if (entry != null && entry.material != null) {
            inputTag = ChemicalHelper.getTag(entry.tagPrefix, entry.material);
        }

        // Handle simple materials separately
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
            GTRecipeBuilder builder = GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("extract_" + itemPath.getPath())
                    .outputFluids(m.getFluid((int) (ms.amount() * L / M)))
                    .duration((int) Math.max(1, ms.amount() * ms.material().getMass() / M))
                    .blastFurnaceTemp(Math.max(800, (int) (ms.material().getBlastTemperature() * 0.6)))
                    .EUt((long) GTValues.VA[GTValues.LV] * multiplier);

            if (inputTag == null) {
                builder.inputItems(input.copy());
            } else {
                builder.inputItems(inputTag);
            }

            builder.save(provider);
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

        if (inputTag == null) {
            extractorBuilder.inputItems(input.copy());
        } else {
            extractorBuilder.inputItems(inputTag);
        }

        // Null check the Item before adding it to the Builder.
        if (itemMs != null) {
            ItemStack outputStack = ChemicalHelper.getIngotOrDust(itemMs);
            if (!outputStack.isEmpty()) {
                extractorBuilder.outputItems(outputStack);
            }
        }

        extractorBuilder.save(provider);
    }
}
