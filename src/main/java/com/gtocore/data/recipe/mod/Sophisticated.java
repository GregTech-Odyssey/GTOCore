package com.gtocore.data.recipe.mod;

import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class Sophisticated {

    public static void init() {
        if (!Mods.sophisticatedbackpacks() || GTOCore.isEasy()) return;
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), GTMachines.SUPER_CHEST[GTValues.MV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), GTItems.ELECTRIC_PISTON_MV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.COVER_ITEM_VOIDING.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_MAGNET_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), GTMachines.SUPER_CHEST[GTValues.HV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), GTItems.ITEM_FILTER.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), GTItems.ROBOT_ARM_MV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), RegistriesUtils.getItemStack("gtceu:bronze_drum"));
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_FILTER.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), GTItems.ITEM_MAGNET_HV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ROBOT_ARM_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), GTItems.TAG_FILTER.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.SUPER_CHEST[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), GTItems.COVER_ITEM_VOIDING_ADVANCED.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:blasting_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), GTMachines.QUANTUM_CHEST[GTValues.UHV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), GTMachines.QUANTUM_CHEST[GTValues.IV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), GTMachines.SUPER_CHEST[GTValues.EV].asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PUMP_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:smoking_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PISTON_LV.asItem());
        VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.ITEM_COLLECTOR[GTValues.LV].asItem());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("backpack"), RegistriesUtils.getItemStack("sophisticatedbackpacks:backpack"),
                "ABA",
                "BCB",
                "DBD",
                'A', new MaterialEntry(TagPrefix.screw, GTMaterials.WroughtIron), 'B', new ItemStack(Items.LEATHER.asItem()), 'C', RegistriesUtils.getItemStack("gtceu:wood_crate"), 'D', new ItemStack(Items.STRING.asItem()));
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        if (!Mods.sophisticatedbackpacks() || GTOCore.isEasy()) return;
        filters.add(RLUtils.sp("backpack"));
        filters.add(RLUtils.sp("pickup_upgrade"));
        filters.add(RLUtils.sp("filter_upgrade"));
        filters.add(RLUtils.sp("advanced_pickup_upgrade"));
        filters.add(RLUtils.sp("advanced_filter_upgrade"));
        filters.add(RLUtils.sp("magnet_upgrade"));
        filters.add(RLUtils.sp("advanced_magnet_upgrade"));
        filters.add(RLUtils.sp("advanced_magnet_upgrade_from_basic"));
        filters.add(RLUtils.sp("compacting_upgrade"));
        filters.add(RLUtils.sp("advanced_compacting_upgrade"));
        filters.add(RLUtils.sp("void_upgrade"));
        filters.add(RLUtils.sp("advanced_void_upgrade"));
        filters.add(RLUtils.sp("pump_upgrade"));
        filters.add(RLUtils.sp("advanced_pump_upgrade"));
        filters.add(RLUtils.sp("battery_upgrade"));
        filters.add(RLUtils.sp("tank_upgrade"));
        filters.add(RLUtils.sp("refill_upgrade"));
        filters.add(RLUtils.sp("advanced_refill_upgrade"));
        filters.add(RLUtils.sp("inception_upgrade"));
        filters.add(RLUtils.sp("auto_smelting_upgrade"));
        filters.add(RLUtils.sp("auto_smoking_upgrade"));
        filters.add(RLUtils.sp("auto_smoking_upgrade_from_auto_smelting_upgrade"));
        filters.add(RLUtils.sp("auto_blasting_upgrade"));
        filters.add(RLUtils.sp("auto_blasting_upgrade_from_auto_smelting_upgrade"));
        filters.add(RLUtils.sp("stack_upgrade_starter_tier"));
        filters.add(RLUtils.sp("stack_upgrade_tier_1"));
        filters.add(RLUtils.sp("stack_upgrade_tier_1_from_starter"));
        filters.add(RLUtils.sp("stack_upgrade_tier_2"));
        filters.add(RLUtils.sp("stack_upgrade_tier_3"));
        filters.add(RLUtils.sp("stack_upgrade_tier_4"));
        filters.add(RLUtils.sp("stack_upgrade_omega_tier"));
    }
}
