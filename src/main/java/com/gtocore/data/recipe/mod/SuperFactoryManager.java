package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEBlocks;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public class SuperFactoryManager {

    public static void init() {
        if (GTOCore.isEasy()) return;
        if (!Mods.SFM.isLoaded()) return;
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("manager"), RegistriesUtils.getItemStack("sfm:manager"),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(GTOTagPrefix.frameGt, GTMaterials.Ultimet), 'B', RegistriesUtils.getItemStack("expatternprovider:oversize_interface"), 'C', CustomTags.IV_CIRCUITS, 'D', new ItemStack(AEBlocks.CRAFTING_STORAGE_4K.asItem()), 'E', RegistriesUtils.getItemStack("expatternprovider:ex_pattern_provider"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("cable"), RegistriesUtils.getItemStack("sfm:cable"),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(GTOTagPrefix.plateDouble, GTMaterials.Titanium), 'B', GTItems.ROBOT_ARM_HV.asStack(), 'C', RegistriesUtils.getItemStack("expatternprovider:oversize_interface"), 'D', GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.asStack(), 'E', GTItems.FLUID_REGULATOR_HV.asStack());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("labelgun"), RegistriesUtils.getItemStack("sfm:labelgun"),
                " AB",
                " CD",
                "C  ",
                'A', GTItems.EMITTER_EV.asStack(), 'B', RegistriesUtils.getItemStack("sfm:disk"), 'C', new MaterialEntry(GTOTagPrefix.rodLong, GTMaterials.Titanium), 'D', GTItems.SENSOR_EV.asStack());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("water_tank"), RegistriesUtils.getItemStack("sfm:water_tank"),
                "ABC",
                "   ",
                "   ",
                'A', GTMachines.HULL[GTValues.MV].asStack(), 'B', GTItems.COVER_INFINITE_WATER.asStack(), 'C', GTItems.ELECTRIC_PUMP_HV.asStack());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("network_tool_conv"), RegistriesUtils.getItemStack("sfm:network_tool"),
                "A  ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("sfm:labelgun"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("labelgun_conv"), RegistriesUtils.getItemStack("sfm:labelgun"),
                "A  ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("sfm:network_tool"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("printing_press"), RegistriesUtils.getItemStack("sfm:printing_press"),
                "ABA",
                "CDC",
                "AEA",
                'A', new MaterialEntry(GTOTagPrefix.ingot, GTMaterials.Ultimet), 'B', new ItemStack(Items.ANVIL), 'C', RegistriesUtils.getItemStack("botania:mana_pylon"), 'D', GTMachines.HULL[GTValues.HV].asStack(), 'E', new ItemStack(Items.ENCHANTING_TABLE));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fancy_cable"), RegistriesUtils.getItemStack("sfm:fancy_cable"),
                "A  ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("sfm:cable"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("cable"), RegistriesUtils.getItemStack("sfm:cable"),
                "A  ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("sfm:fancy_cable"));

        ASSEMBLER_RECIPES.builder("disk")
                .inputItems(GTItems.TOOL_DATA_STICK, 8)
                .inputItems(GTOTagPrefix.plate, GTMaterials.TitaniumCarbide, 4)
                .inputItems(GTOTagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems("gtmthings:advanced_terminal")
                .outputItems("sfm:disk")
                .duration(200)
                .save();
    }
}
