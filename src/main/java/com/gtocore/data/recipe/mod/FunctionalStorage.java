package com.gtocore.data.recipe.mod;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class FunctionalStorage {

    public static void init() {
        if (GTCEu.isModLoaded("functionalstorage")) {
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_upgrade"), RegistriesUtils.getItemStack("functionalstorage:copper_upgrade"),
                    "AAA",
                    "BCD",
                    "AAA",
                    'A', new MaterialEntry(TagPrefix.block, GTMaterials.Copper), 'B', RegistriesUtils.getItemStack("gtceu:bronze_crate"), 'C', CustomTags.ULV_CIRCUITS, 'D', RegistriesUtils.getItemStack("gtceu:bronze_drum"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gold_upgrade"), RegistriesUtils.getItemStack("functionalstorage:gold_upgrade"),
                    "AAA",
                    "CBC",
                    "AAA",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("functionalstorage:copper_upgrade"), 'C', CustomTags.LV_CIRCUITS);

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("diamond_upgrade"), RegistriesUtils.getItemStack("functionalstorage:diamond_upgrade"),
                    "AAA",
                    "CBC",
                    "AAA",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.ManaDiamond), 'B', RegistriesUtils.getItemStack("functionalstorage:gold_upgrade"), 'C', CustomTags.MV_CIRCUITS);

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("netherite_upgrade"), RegistriesUtils.getItemStack("functionalstorage:netherite_upgrade"),
                    "AAA",
                    "CBC",
                    "AAA",
                    'A', Items.NETHERITE_BLOCK.getDefaultInstance(), 'B', RegistriesUtils.getItemStack("functionalstorage:diamond_upgrade"), 'C', CustomTags.HV_CIRCUITS);

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_1"), RegistriesUtils.getItemStack("functionalstorage:fluid_1", 1),
                    "AAA",
                    "BCB",
                    "AAA",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Steel), 'C', RegistriesUtils.getItemStack("enderio:pressurized_fluid_tank"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_2"), RegistriesUtils.getItemStack("functionalstorage:fluid_2", 2),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', RegistriesUtils.getItemStack("enderio:pressurized_fluid_tank"), 'C', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Steel), 'D', RegistriesUtils.getItemStack("enderio:fluid_tank"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_4"), RegistriesUtils.getItemStack("functionalstorage:fluid_4", 4),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', RegistriesUtils.getItemStack("enderio:pressurized_fluid_tank"), 'B', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Steel), 'C', RegistriesUtils.getItemStack("enderio:fluid_tank"));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("storage_controller"), RegistriesUtils.getItemStack("functionalstorage:storage_controller"),
                    "ABA",
                    "CDC",
                    "CBC",
                    'A', GTItems.FLUID_REGULATOR_LV.asStack(), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', CustomTags.LV_CIRCUITS, 'D', TagUtils.createTag("functionalstorage:drawer"));
        }
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        if (GTCEu.isModLoaded("functionalstorage")) {
            filters.add(RLUtils.functionalstorage("copper_upgrade"));
            filters.add(RLUtils.functionalstorage("fluid_1"));
            filters.add(RLUtils.functionalstorage("fluid_2"));
            filters.add(RLUtils.functionalstorage("fluid_4"));
            filters.add(RLUtils.functionalstorage("storage_controller"));
            filters.add(RLUtils.functionalstorage("gold_upgrade"));
            filters.add(RLUtils.functionalstorage("diamond_upgrade"));
            filters.add(RLUtils.functionalstorage("netherite_upgrade"));
            filters.add(RLUtils.functionalstorage("oak_drawer_alternate_x1"));
            filters.add(RLUtils.functionalstorage("oak_drawer_alternate_x2"));
            filters.add(RLUtils.functionalstorage("oak_drawer_alternate_x4"));
        }
    }
}
