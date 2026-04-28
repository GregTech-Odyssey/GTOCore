package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import java.util.Locale;

import static com.gtocore.common.data.GTORecipeTypes.RADIATION_HATCH_RECIPES;

public final class RadiationHatchRecipes {

    public static void init() {
        registerRadioactiveMaterial(GTMaterials.Uranium238, 8000, 50);
        registerRadioactiveMaterial(GTMaterials.Uranium235, 6500, 100);
        registerRadioactiveMaterial(GTMaterials.Plutonium241, 6000, 110);
        registerRadioactiveMaterial(GTMaterials.Naquadah, 15000, 120);
        registerRadioactiveMaterial(GTMaterials.NaquadahEnriched, 10000, 140);
        registerRadioactiveMaterial(GTMaterials.Naquadria, 7500, 160);
    }

    private static void registerRadioactiveMaterial(Material material, int duration, int radioactivity) {
        if (material.hasFlag(MaterialFlags.GENERATE_ROD))
            RADIATION_HATCH_RECIPES.recipeBuilder("radioactive_material_rod" + material.getName().toLowerCase(Locale.ROOT))
                    .inputItems(ChemicalHelper.get(TagPrefix.rod, material, 1))
                    .duration(duration)
                    .addData(GTORecipeDataKeys.RADIOACTIVITY, radioactivity)
                    .save();

        if (material.hasFlag(MaterialFlags.GENERATE_LONG_ROD))
            RADIATION_HATCH_RECIPES.recipeBuilder("radioactive_material_long_rod" + material.getName().toLowerCase(Locale.ROOT))
                    .inputItems(ChemicalHelper.get(TagPrefix.rodLong, material, 1))
                    .duration((int) (duration * 1.5))
                    .addData(GTORecipeDataKeys.RADIOACTIVITY, (int) (radioactivity * 1.2))
                    .save();
    }
}
