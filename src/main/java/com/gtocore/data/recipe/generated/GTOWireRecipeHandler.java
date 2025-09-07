package com.gtocore.data.recipe.generated;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

final class GTOWireRecipeHandler {

    private static final Map<TagPrefix, Integer> INSULATION_AMOUNT = ImmutableMap.of(
            cableGtSingle, 1,
            cableGtDouble, 1,
            cableGtQuadruple, 2,
            cableGtOctal, 3,
            cableGtHex, 5);

    static void run(@NotNull Material material) {
        WireProperties property = material.getProperty(PropertyKey.WIRE);
        if (property == null) {
            return;
        }
        processWires(material, property);
        if (property.isSuperconductor()) return;
        generateCableCovering(wireGtSingle, material, property);
        generateCableCovering(wireGtDouble, material, property);
        generateCableCovering(wireGtQuadruple, material, property);
        generateCableCovering(wireGtOctal, material, property);
        generateCableCovering(wireGtHex, material, property);
    }

    private static void processWires(Material material, WireProperties property) {
        ItemStack wireSingle = ChemicalHelper.get((property.isSuperconductor() && property.getVoltage() < VA[MAX]) ? GTOTagPrefix.SUPERCONDUCTOR_BASE : TagPrefix.wireGtSingle, material, 2);
        if (wireSingle.isEmpty()) return;
        TagPrefix prefix = material.hasProperty(PropertyKey.INGOT) ? ingot :
                material.hasProperty(PropertyKey.GEM) ? gem : dust;
        int mass = (int) material.getMass();
        long voltageMultiplier = GTOUtils.getVoltageMultiplier(material);
        Integer voltage = GTOMaterials.MATERIAL_VOLTAGE.get(material);
        if (voltage != null) {
            voltageMultiplier = voltage;
        }
        WIREMILL_RECIPES.recipeBuilder("mill_" + material.getName() + "_wire")
                .inputItems(prefix, material)
                .outputItems(wireSingle)
                .duration(mass)
                .EUt(voltageMultiplier)
                .save();

        if (!material.hasFlag(MaterialFlags.NO_WORKING) && material.hasFlag(MaterialFlags.GENERATE_PLATE) && mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(String.format("%s_wire_single", material.getName()),
                    wireSingle.copyWithCount(1), "Xx",
                    'X', new MaterialEntry(plate, material));
        }
    }

    private static void generateCableCovering(TagPrefix wirePrefix, Material material, WireProperties property) {
        ItemStack wire = ChemicalHelper.get(wirePrefix, material);
        if (wire.isEmpty()) return;

        TagPrefix cablePrefix = TagPrefix.get("cable" + wirePrefix.name().substring(4));
        int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);

        if (voltageTier <= LV) {
            generateManualRecipe(wirePrefix, material, cablePrefix, voltageTier == ULV);
        }

        if (voltageTier < IV) {
            RecipeBuilder builder = LAMINATOR_RECIPES
                    .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_rubber")
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wire)
                    .outputItems(cablePrefix, material)
                    .inputFluids(Rubber.getFluid(L * insulationAmount));

            if (voltageTier == EV) {
                builder.inputItems(foil, PolyvinylChloride, insulationAmount);
            }
            builder.save();
        }
        if (voltageTier < UHV) {
            RecipeBuilder builder = LAMINATOR_RECIPES
                    .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_silicone")
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wire)
                    .outputItems(cablePrefix, material);

            if (voltageTier >= LuV) {
                builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);
            }

            builder.inputItems(foil, PolyvinylChloride, insulationAmount);

            builder.inputFluids(SiliconeRubber.getFluid(L * insulationAmount / (voltageTier >= LuV ? 1 : 2)))
                    .save();
        }
        RecipeBuilder builder = LAMINATOR_RECIPES
                .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase() + "_styrene_butadiene")
                .EUt(VA[ULV]).duration(100)
                .inputItems(wire)
                .outputItems(cablePrefix, material);

        if (voltageTier > UEV) {
            builder.inputItems(GTOItems.HIGHLY_INSULATING_FOIL.asStack(insulationAmount));
        }

        builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);

        builder.inputFluids(StyreneButadieneRubber.getFluid(L * insulationAmount / (voltageTier > UEV ? 1 : (voltageTier >= LuV ? 2 : 4))))
                .save();
    }

    private static void generateManualRecipe(TagPrefix wirePrefix, Material material, TagPrefix cablePrefix, boolean manual) {
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);
        if (manual) {
            Object[] ingredients = new Object[insulationAmount + 1];
            ingredients[0] = new MaterialEntry(wirePrefix, material);
            for (int i = 1; i <= insulationAmount; i++) {
                ingredients[i] = ChemicalHelper.get(plate, Rubber);
            }
            VanillaRecipeHelper.addShapelessRecipe(String.format("%s_cable_%d", material.getName(), (int) ((wirePrefix.getMaterialAmount(material) << 1) / M)),
                    ChemicalHelper.get(cablePrefix, material),
                    ingredients);
        }
        PACKER_RECIPES.recipeBuilder("cover_" + material.getName() + "_" + wirePrefix.name().toLowerCase())
                .inputItems(wirePrefix, material)
                .inputItems(plate, Rubber, insulationAmount)
                .outputItems(cablePrefix, material)
                .duration(100).EUt(VA[ULV])
                .save();
    }
}
