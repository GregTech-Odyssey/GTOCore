package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import java.util.Objects;

import static com.gtocore.common.data.GTORecipeTypes.FAST_NEUTRON_BREEDER_REACTOR_RECIPES;

class FastNeutronBreeder {

    public static void init() {
        Material[] mats = new Material[] {
                GTMaterials.Thorium,
                GTMaterials.Protactinium,
                GTMaterials.Uranium235,
                GTMaterials.Uranium238,
                GTMaterials.Neptunium,
                GTMaterials.Plutonium239,
                GTMaterials.Plutonium241,
                GTMaterials.Americium,
                GTMaterials.Berkelium,
                GTMaterials.Curium
        };
        // FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("thorium_breeder_rod")
        // .inputItems(TagPrefix.dust, GTMaterials.Thorium, 12)
        // .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Thorium)
        // .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Thorium)
        // .addData("neutron_flux", 1100)
        // .addData("neutron_flux_change", 11)
        // .addData("heat", 11)
        // .EUt(15)
        // .duration(3200)
        // .save();
        for (int i = 0; i < mats.length - 1; i++) {
            Material mat = mats[i];
            FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder(mat.getName() + "_breeder_reaction")
                    .inputItems(TagPrefix.dust, mat, 12)
                    .inputItems(GTOTagPrefix.BREEDER_ROD, mat)
                    .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, mat)
                    .addData("neutron_flux", 1100 + Objects.hash(mat) % 1100)
                    .addData("neutron_flux_change", 33 - Objects.hash(mat) % 66)
                    .addData("heat", Objects.hash(mat) % 5)
                    .EUt(30)
                    .duration(3200)
                    .save();
        }
    }
}
