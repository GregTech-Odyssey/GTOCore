package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public final class GTOWireRecipeHandler {

    private static final Map<TagPrefix, Integer> INSULATION_AMOUNT = ImmutableMap.of(
            cableGtSingle, 1,
            cableGtDouble, 1,
            cableGtQuadruple, 2,
            cableGtOctal, 3,
            cableGtHex, 5);

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        WireProperties property = material.getProperty(PropertyKey.WIRE);
        if (property == null) {
            return;
        }
        processWires(material, provider);

        generateCableCovering(wireGtSingle, material, property, provider);
        generateCableCovering(wireGtDouble, material, property, provider);
        generateCableCovering(wireGtQuadruple, material, property, provider);
        generateCableCovering(wireGtOctal, material, property, provider);
        generateCableCovering(wireGtHex, material, property, provider);
    }

    private static void processWires(Material material, Consumer<FinishedRecipe> provider) {
        if (!wireGtSingle.shouldGenerateRecipes(material)) {
            return;
        }
        TagPrefix prefix = material.hasProperty(PropertyKey.INGOT) ? ingot :
                material.hasProperty(PropertyKey.GEM) ? gem : dust;
        int mass = (int) material.getMass();
        GTRecipeTypes.WIREMILL_RECIPES.recipeBuilder(GTOCore.id("mill_" + material.getName() + "_wire"))
                .inputItems(prefix, material)
                .outputItems(wireGtSingle, material, 2)
                .duration(mass)
                .EUt(GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (!material.hasFlag(MaterialFlags.NO_WORKING) && material.hasFlag(MaterialFlags.GENERATE_PLATE) && mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("%s_wire_single", material.getName()),
                    ChemicalHelper.get(wireGtSingle, material), "Xx",
                    'X', new UnificationEntry(plate, material));
        }
    }

    private static void generateCableCovering(TagPrefix wirePrefix, Material material, WireProperties property, Consumer<FinishedRecipe> provider) {
        if (!wirePrefix.shouldGenerateRecipes(material)) {
            return;
        }
        if (property.isSuperconductor()) return;

        TagPrefix cablePrefix = TagPrefix.get("cable" + wirePrefix.name().substring(4));
        int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);

        if (voltageTier <= LV) {
            generateManualRecipe(wirePrefix, material, cablePrefix, provider, voltageTier == ULV);
        }

        if (voltageTier < IV) {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_rubber"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material)
                    .inputFluids(Rubber.getFluid(L * insulationAmount));

            if (voltageTier == EV) {
                builder.inputItems(foil, PolyvinylChloride, insulationAmount);
            }
            builder.save(provider);
        } else if (voltageTier < UHV) {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_silicone"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            if (voltageTier >= LuV) {
                builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);
            }

            builder.inputItems(foil, PolyvinylChloride, insulationAmount);

            builder.inputFluids(SiliconeRubber.getFluid(L * insulationAmount))
                    .save(provider);
        } else {
            GTRecipeBuilder builder = GTORecipeTypes.LAMINATOR_RECIPES
                    .recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_styrene_butadiene"))
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            if (voltageTier > UEV) {
                builder.inputItems(GTOItems.HIGHLY_INSULATING_FOIL.asStack(insulationAmount));
            }

            builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);

            builder.inputFluids(StyreneButadieneRubber.getFluid(L * insulationAmount))
                    .save(provider);
        }
    }

    private static void generateManualRecipe(TagPrefix wirePrefix, Material material, TagPrefix cablePrefix, Consumer<FinishedRecipe> provider, boolean manual) {
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);
        if (manual) {
            Object[] ingredients = new Object[insulationAmount + 1];
            ingredients[0] = new UnificationEntry(wirePrefix, material);
            for (int i = 1; i <= insulationAmount; i++) {
                ingredients[i] = ChemicalHelper.get(plate, Rubber);
            }
            VanillaRecipeHelper.addShapelessRecipe(provider, String.format("%s_cable_%d", material.getName(), (int) ((wirePrefix.getMaterialAmount(material) << 1) / M)),
                    ChemicalHelper.get(cablePrefix, material),
                    ingredients);
        }
        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase()))
                .inputItems(wirePrefix, material)
                .inputItems(plate, Rubber, insulationAmount)
                .outputItems(cablePrefix, material)
                .duration(100).EUt(VA[ULV])
                .save(provider);
    }
}
