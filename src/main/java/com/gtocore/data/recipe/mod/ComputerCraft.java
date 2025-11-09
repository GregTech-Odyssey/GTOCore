package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

public class ComputerCraft {

    public static void init() {
        if (GTOCore.isEasy()) return;
        if (!Mods.COMPUTERCRAFT.isLoaded()) return;

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("computer_normal"), RegistriesUtils.getItemStack("computercraft:computer_normal"),
                "ABA",
                "ACA",
                "ADA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', GTOMachines.BASIC_MONITOR.asStack(), 'C', CustomTags.EV_CIRCUITS, 'D', RegistriesUtils.getItemStack("expatternprovider:ex_pattern_provider"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("computer_advanced"), RegistriesUtils.getItemStack("computercraft:computer_advanced"),
                "ABA",
                "ACA",
                "ADA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.Electrum), 'B', GTOMachines.BASIC_MONITOR.asStack(), 'C', CustomTags.IV_CIRCUITS, 'D', RegistriesUtils.getItemStack("expatternprovider:ex_pattern_provider"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("pocket_computer_normal"), RegistriesUtils.getItemStack("computercraft:pocket_computer_normal"),
                "AB ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("computercraft:computer_normal"), 'B', RegistriesUtils.getItemStack("gtmthings:advanced_terminal"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("pocket_computer_advanced"), RegistriesUtils.getItemStack("computercraft:pocket_computer_advanced"),
                "AB ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("computercraft:computer_advanced"), 'B', RegistriesUtils.getItemStack("gtmthings:advanced_terminal"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("speaker"), RegistriesUtils.getItemStack("computercraft:speaker"),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', new ItemStack(Items.NOTE_BLOCK), 'C', CustomTags.HV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("disk_drive"), RegistriesUtils.getItemStack("computercraft:disk_drive"),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', RegistriesUtils.getItemStack("gtceu:data_access_hatch"), 'C', CustomTags.HV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("printer"), RegistriesUtils.getItemStack("computercraft:printer"),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', GTMachines.FORMING_PRESS[GTValues.HV].asStack(), 'C', CustomTags.HV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("monitor_normal"), RegistriesUtils.getItemStack("computercraft:monitor_normal"),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', GTOMachines.BASIC_MONITOR.asStack(), 'C', CustomTags.HV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("monitor_advanced"), RegistriesUtils.getItemStack("computercraft:monitor_advanced"),
                "AAA",
                "ABA",
                "ACA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.Electrum), 'B', GTOMachines.BASIC_MONITOR.asStack(), 'C', CustomTags.EV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wireless_modem_normal"), RegistriesUtils.getItemStack("computercraft:wireless_modem_normal"),
                "ABA",
                "ACA",
                "ADA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', new ItemStack(AEItems.WIRELESS_RECEIVER.asItem()), 'C', new MaterialEntry(GTOTagPrefix.gem, GTMaterials.EnderEye), 'D', CustomTags.EV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wireless_modem_advanced"), RegistriesUtils.getItemStack("computercraft:wireless_modem_advanced"),
                "ABA",
                "ACA",
                "ADA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.Electrum), 'B', new ItemStack(AEItems.WIRELESS_RECEIVER.asItem()), 'C', GTOMachines.TESSERACT_GENERATOR.asStack(), 'D', CustomTags.IV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wired_modem_full"), RegistriesUtils.getItemStack("computercraft:wired_modem_full"),
                "ABA",
                "ACA",
                "ADA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', RegistriesUtils.getItemStack("ae2:fluix_smart_dense_cable"), 'C', new ItemStack(AEBlocks.INTERFACE.asItem()), 'D', CustomTags.EV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("cct_cable"), RegistriesUtils.getItemStack("computercraft:cable", 3),
                "AAA",
                "BBB",
                "AAA",
                'A', new MaterialEntry(GTOTagPrefix.plate, GTMaterials.StainlessSteel), 'B', RegistriesUtils.getItemStack("ae2:fluix_smart_cable"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wired_modem"), RegistriesUtils.getItemStack("computercraft:wired_modem"),
                "A  ",
                "   ",
                "   ",
                'A', RegistriesUtils.getItemStack("computercraft:wired_modem_full"));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("turtle_normal"), RegistriesUtils.getItemStack("computercraft:turtle_normal"),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.SENSOR_HV.asStack(), 'B', GTItems.EMITTER_HV.asStack(), 'C', GTItems.ROBOT_ARM_HV.asStack(), 'D', RegistriesUtils.getItemStack("computercraft:computer_normal"), 'E', GTItems.FIELD_GENERATOR_HV.asStack(), 'F', GTItems.POWER_THRUSTER.asStack());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("turtle_advanced"), RegistriesUtils.getItemStack("computercraft:turtle_advanced"),
                "ABA",
                "CDC",
                "EFE",
                'A', GTItems.SENSOR_EV.asStack(), 'B', GTItems.EMITTER_EV.asStack(), 'C', GTItems.ROBOT_ARM_EV.asStack(), 'D', RegistriesUtils.getItemStack("computercraft:computer_advanced"), 'E', GTItems.FIELD_GENERATOR_EV.asStack(), 'F', GTItems.POWER_THRUSTER_ADVANCED.asStack());
    }
}
