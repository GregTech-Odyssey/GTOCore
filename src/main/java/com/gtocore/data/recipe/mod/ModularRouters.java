package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public class ModularRouters {

    public static void init() {
        if (GTOCore.isEasy()) return;
        if (Mods.MODULARROUTERS.isLoaded()) {
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("modular_router"), RegistriesUtils.getItemStack("modularrouters:modular_router"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(GTOTagPrefix.ingot, GTMaterials.BlackSteel), 'B', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.Kanthal), 'C', RegistriesUtils.getItemStack("modularrouters:blank_module"));

            ASSEMBLER_RECIPES.builder("blank_module")
                    .inputItems(GTOTagPrefix.plate, GTMaterials.Aluminium, 4)
                    .inputItems("modularrouters:bulk_item_filter")
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(GTItems.SENSOR_MV)
                    .inputItems(GTItems.ROBOT_ARM_MV)
                    .inputItems(GTItems.ELECTRIC_PUMP_MV)
                    .outputItems("modularrouters:blank_module")
                    .duration(200)
                    .EUt(GTValues.VA[GTValues.MV])
                    .save();

            ASSEMBLER_RECIPES.builder("blank_upgrade")
                    .inputItems(GTOTagPrefix.plate, GTMaterials.StainlessSteel, 3)
                    .inputItems(GTOTagPrefix.wireFine, GTMaterials.Gold, 16)
                    .inputItems(GTItems.FIELD_GENERATOR_MV)
                    .inputItems(GTItems.INTEGRATED_CIRCUIT_MV)
                    .outputItems("modularrouters:blank_upgrade")
                    .duration(200)
                    .EUt(GTValues.VA[GTValues.HV])
                    .save();

            ASSEMBLER_RECIPES.builder("augment_core")
                    .inputItems(GTOTagPrefix.plate, GTMaterials.Aluminium, 4)
                    .inputItems(GTOTagPrefix.plate, GTMaterials.EnderPearl, 4)
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .outputItems("modularrouters:augment_core")
                    .duration(200)
                    .EUt(GTValues.VA[GTValues.MV])
                    .save();

            ASSEMBLER_RECIPES.builder("bulk_item_filter")
                    .inputItems(GTOTagPrefix.plate, GTMaterials.Diamond, 4)
                    .inputItems(GTItems.ITEM_FILTER, 4)
                    .inputItems(CustomTags.LV_CIRCUITS)
                    .outputItems("modularrouters:bulk_item_filter")
                    .duration(200)
                    .save();
        }
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        if (GTOCore.isEasy()) return;
        if (Mods.MODULARROUTERS.isLoaded()) {
            filters.add(RLUtils.fromNamespaceAndPath("modularrouters", "modular_router"));
            filters.add(RLUtils.fromNamespaceAndPath("modularrouters", "blank_module"));
            filters.add(RLUtils.fromNamespaceAndPath("modularrouters", "blank_upgrade"));
            filters.add(RLUtils.fromNamespaceAndPath("modularrouters", "augment_core"));
            filters.add(RLUtils.fromNamespaceAndPath("modularrouters", "bulk_item_filter"));
        }
    }
}
