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

import net.minecraft.world.item.Items;

public class Pipez {

    public static void init() {
        if (GTOCore.isEasy()) return;
        if (!Mods.PIPEZ.isLoaded()) return;
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("item_pipe"), RegistriesUtils.getItemStack("pipez:item_pipe", 6),
                "AAA",
                "BBB",
                "AAA",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.pipeSmallItem, GTMaterials.Cobalt));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("fluid_pipe"), RegistriesUtils.getItemStack("pipez:fluid_pipe", 6),
                "AAA",
                "BBB",
                "AAA",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Bronze), 'B', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Potin));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("universal_pipe"), RegistriesUtils.getItemStack("pipez:universal_pipe", 6),
                "AAA",
                "BCD",
                "EEE",
                'A', RegistriesUtils.getItem("pipez:fluid_pipe"), 'B', new MaterialEntry(TagPrefix.pipeHugeRestrictive, GTMaterials.Cupronickel), 'C', new MaterialEntry(TagPrefix.frameGt, GTOMaterials.RedstoneAlloy), 'D', new MaterialEntry(TagPrefix.pipeNonupleFluid, GTMaterials.TinAlloy), 'E', RegistriesUtils.getItem("pipez:item_pipe"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_upgrade"), RegistriesUtils.getItem("pipez:basic_upgrade"),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.WroughtIron), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.RedstoneAlloy), 'C', Items.PISTON, 'D', Items.REDSTONE_TORCH);
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("improved_upgrade"), RegistriesUtils.getItem("pipez:improved_upgrade"),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.Steel), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.ConductiveAlloy), 'C', GTOItems.ULV_ELECTRIC_PISTON.asItem(), 'D', RegistriesUtils.getItem("pipez:basic_upgrade"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_upgrade"), RegistriesUtils.getItem("pipez:advanced_upgrade"),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.DarkSteel), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.Soularium), 'C', GTItems.ELECTRIC_PISTON_LV.asItem(), 'D', RegistriesUtils.getItem("pipez:improved_upgrade"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ultimate_upgrade"), RegistriesUtils.getItem("pipez:ultimate_upgrade"),
                "AAA",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.ingot, GTMaterials.VanadiumSteel), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.EnergeticAlloy), 'C', GTItems.ELECTRIC_PISTON_MV.asItem(), 'D', RegistriesUtils.getItem("pipez:advanced_upgrade"));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("filter_destination_tool"), RegistriesUtils.getItem("pipez:filter_destination_tool"),
                "A A",
                "BCB",
                "BDB",
                'A', new MaterialEntry(TagPrefix.wireGtSingle, GTMaterials.RedAlloy), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'C', Items.REDSTONE_TORCH, 'D', GTItems.VACUUM_TUBE.asItem());
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wrench"), RegistriesUtils.getItem("pipez:wrench"),
                " A ",
                " BA",
                "B  ",
                'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Flint), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
    }
}
