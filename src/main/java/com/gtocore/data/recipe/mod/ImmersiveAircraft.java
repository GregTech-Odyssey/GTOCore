package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEBlocks;

import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public final class ImmersiveAircraft {

    public static void init() {
        if (GTCEu.isModLoaded("immersive_aircraft")) {
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_boiler"), RegistriesUtils.getItemStack("immersive_aircraft:boiler"),
                    "AAA",
                    "A A",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Copper), 'B', new ItemStack(Items.FURNACE.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_steel_boiler"), RegistriesUtils.getItemStack("immersive_aircraft:steel_boiler"),
                    "ABA",
                    "ACA",
                    "ADA",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Steel), 'B', GTOItems.AIR_VENT.asItem(), 'C', RegistriesUtils.getItemStack("immersive_aircraft:boiler"), 'D', new ItemStack(Items.BLAST_FURNACE.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_engine"), RegistriesUtils.getItemStack("immersive_aircraft:engine"),
                    "AAA",
                    "BCB",
                    "DED",
                    'A', new MaterialEntry(TagPrefix.plateDouble, GTMaterials.WroughtIron), 'B', new ItemStack(Items.PISTON.asItem()), 'C', new ItemStack(Items.BLAST_FURNACE.asItem()), 'D', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze), 'E', RegistriesUtils.getItemStack("immersive_aircraft:boiler"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_propeller"), RegistriesUtils.getItemStack("immersive_aircraft:propeller"),
                    "AAB",
                    "rDd",
                    "BAA",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'D', new MaterialEntry(TagPrefix.ring, GTMaterials.Iron));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_hull"), RegistriesUtils.getItemStack("immersive_aircraft:hull"),
                    "AAA",
                    "BCB",
                    "AAA",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.TreatedWood), 'B', new MaterialEntry(TagPrefix.screw, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_enhanced_propeller"), RegistriesUtils.getItemStack("immersive_aircraft:enhanced_propeller"),
                    "hBf",
                    "BDB",
                    "EBE",
                    'B', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Bronze), 'D', RegistriesUtils.getItemStack("immersive_aircraft:propeller"), 'E', new MaterialEntry(TagPrefix.screw, GTMaterials.Bronze));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_industrial_gears"), RegistriesUtils.getItemStack("immersive_aircraft:industrial_gears"),
                    "hBB",
                    "CDB",
                    "CCd",
                    'B', new MaterialEntry(TagPrefix.gear, GTMaterials.Bronze), 'C', new MaterialEntry(TagPrefix.gear, GTMaterials.Steel), 'D', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Copper));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_sturdy_pipes"), RegistriesUtils.getItemStack("immersive_aircraft:sturdy_pipes"),
                    "hBC",
                    "CCC",
                    "CBf",
                    'B', new MaterialEntry(TagPrefix.ring, GTMaterials.Steel), 'C', new MaterialEntry(TagPrefix.pipeNormalRestrictive, GTMaterials.PolyvinylChloride));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_improved_landing_gear"), RegistriesUtils.getItemStack("immersive_aircraft:improved_landing_gear"),
                    "AhA",
                    "A A",
                    "CwC",
                    'A', new MaterialEntry(TagPrefix.rodLong, GTMaterials.Invar), 'C', GTItems.STEEL_MINECART_WHEELS.asStack());

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_rotary_cannon"), RegistriesUtils.getItemStack("immersive_aircraft:rotary_cannon"),
                    "ABA",
                    "fDd",
                    "hGw",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Lead), 'B', new ItemStack(Items.DISPENSER.asItem()), 'D', RegistriesUtils.getItemStack("immersive_aircraft:industrial_gears"), 'G', new MaterialEntry(TagPrefix.block, GTMaterials.Copper));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("aircraft_bomb_bay"), RegistriesUtils.getItemStack("immersive_aircraft:bomb_bay"),
                    "ABA",
                    "ACA",
                    "DhD",
                    'A', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, GTMaterials.Steel), 'B', new ItemStack(AEBlocks.TINY_TNT.block().asItem()), 'C', new ItemStack(Items.DROPPER.asItem()), 'D', new MaterialEntry(TagPrefix.screw, GTMaterials.Steel));

            ASSEMBLER_RECIPES.builder("aircraft_nether_engine")
                    .inputItems("immersive_aircraft:engine", 4)
                    .inputItems(CustomTags.MV_CIRCUITS, 2)
                    .inputItems(TagPrefix.plateDouble, GTMaterials.Molybdenum, 4)
                    .inputItems(TagPrefix.rod, GTMaterials.Blaze, 4)
                    .outputItems("immersive_aircraft:nether_engine")
                    .inputFluids(GTMaterials.Lava, 1000)
                    .EUt(120)
                    .duration(200)
                    .save();

            ASSEMBLER_RECIPES.builder("aircraft_eco_engine")
                    .inputItems("immersive_aircraft:engine", 4)
                    .inputItems(CustomTags.LV_CIRCUITS, 2)
                    .inputItems(TagPrefix.plateDouble, GTOMaterials.Manasteel, 4)
                    .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond, 4)
                    .outputItems("immersive_aircraft:eco_engine")
                    .inputFluids(GTOMaterials.Mana, 1000)
                    .duration(200)
                    .MANAt(4)
                    .save();
        }
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        if (GTCEu.isModLoaded("immersive_aircraft")) {
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "boiler"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "steel_boiler"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "engine"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "propeller"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "hull"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "enhanced_propeller"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "industrial_gears"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "sturdy_pipes"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "improved_landing_gear"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "rotary_cannon"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "bomb_bay"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "nether_engine"));
            filters.add(RLUtils.fromNamespaceAndPath("immersive_aircraft", "eco_engine"));
        }
    }
}
