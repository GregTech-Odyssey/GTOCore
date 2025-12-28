package com.gtocore.data.recipe.mod;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Pipez {

    public static void init() {
        if (GTOCore.isEasy()) return;
        if (!Mods.PIPEZ.isLoaded()) return;
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("item_pipe"), RegistriesUtils.getItemStack("pipez:item_pipe", 8),
                "AAA",
                "BBB",
                "AAA",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.pipeSmallItem, GTMaterials.Cobalt));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_pipe"), RegistriesUtils.getItemStack("pipez:fluid_pipe", 8),
                "AAA",
                "BBB",
                "AAA",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("universal_pipe"), RegistriesUtils.getItemStack("pipez:universal_pipe", 8),
                "AAA",
                "BCD",
                "EEE",
                'A', RegistriesUtils.getItemStack("pipez:fluid_pipe"), 'B', new MaterialEntry(TagPrefix.pipeHugeRestrictive, GTMaterials.Cupronickel), 'C', new MaterialEntry(TagPrefix.frameGt, GTOMaterials.RedstoneAlloy), 'D', new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.TinAlloy), 'E', RegistriesUtils.getItemStack("pipez:item_pipe"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_upgrade"), RegistriesUtils.getItemStack("pipez:basic_upgrade"),
                "AAA",
                " B ",
                " C ",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Iron), 'B', Items.PISTON, 'C', Items.REDSTONE_TORCH);
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_upgrade"), RegistriesUtils.getItemStack("pipez:basic_upgrade", 2),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.RedstoneAlloy), 'C', new ItemStack(Items.PISTON), 'D', new ItemStack(Items.REDSTONE_TORCH));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("improved_upgrade"), RegistriesUtils.getItemStack("pipez:improved_upgrade", 2),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.ConductiveAlloy), 'C', new ItemStack(Items.PISTON), 'D', new ItemStack(Items.REDSTONE_TORCH));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_upgrade"), RegistriesUtils.getItemStack("pipez:advanced_upgrade", 2),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.DarkSteel), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.Soularium), 'C', GTOItems.ULV_ELECTRIC_PISTON.asItem(), 'D', GTItems.VACUUM_TUBE.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ultimate_upgrade"), RegistriesUtils.getItemStack("pipez:ultimate_upgrade", 2),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.DarkSteel), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.EnergeticAlloy), 'C', GTOItems.ULV_ELECTRIC_PISTON.asItem(), 'D', GTItems.VACUUM_TUBE.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("filter_destination_tool"), RegistriesUtils.getItemStack("pipez:filter_destination_tool"),
                "A A",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.RedAlloy), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'C', Items.REDSTONE_TORCH, 'D', GTItems.VACUUM_TUBE.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wrench"), RegistriesUtils.getItemStack("pipez:wrench"),
                " A ",
                " BA",
                "B  ",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Flint), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
    }
}
