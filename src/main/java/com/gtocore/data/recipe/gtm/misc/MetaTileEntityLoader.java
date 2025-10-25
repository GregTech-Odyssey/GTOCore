package com.gtocore.data.recipe.gtm.misc;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public final class MetaTileEntityLoader {

    public static void init() {
        VanillaRecipeHelper.addShapedRecipe(true, "casing_ulv", GTBlocks.MACHINE_CASING_ULV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_lv", GTBlocks.MACHINE_CASING_LV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_mv", GTBlocks.MACHINE_CASING_MV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_hv", GTBlocks.MACHINE_CASING_HV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_ev", GTBlocks.MACHINE_CASING_EV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_iv", GTBlocks.MACHINE_CASING_IV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_luv", GTBlocks.MACHINE_CASING_LuV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_zpm", GTBlocks.MACHINE_CASING_ZPM.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.NaquadahAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_uv", GTBlocks.MACHINE_CASING_UV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Darmstadtium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_uhv", GTBlocks.MACHINE_CASING_UHV.asItem(), "PPP",
                "PwP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium));

        registerMachineRecipe(false, GTMachines.HULL, "PLP", "CHC", 'P', HULL_PLATE, 'L', PLATE, 'C', CABLE,
                'H', CASING);

        VanillaRecipeHelper.addShapedRecipe(true, "casing_coke_bricks", GTBlocks.CASING_COKE_BRICKS.asItem(),
                "XX", "XX", 'X', GTItems.COKE_OVEN_BRICK);
        VanillaRecipeHelper.addShapedRecipe(true, "casing_bronze_bricks",
                GTBlocks.CASING_BRONZE_BRICKS.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PBP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B',
                new ItemStack(Blocks.BRICKS));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_steel_solid",
                GTBlocks.CASING_STEEL_SOLID.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP", "PwP",
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_titanium_stable",
                GTBlocks.CASING_TITANIUM_STABLE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_invar_heatproof",
                GTBlocks.CASING_INVAR_HEATPROOF.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Invar), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Invar));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_aluminium_frostproof",
                GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "PFP", "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Aluminium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_stainless_clean",
                GTBlocks.CASING_STAINLESS_CLEAN.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_tungstensteel_robust",
                GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "PFP", "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_hsse_sturdy",
                GTBlocks.CASING_HSSE_STURDY.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP", "PwP",
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.HSSE), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Europium));

        VanillaRecipeHelper.addShapedRecipe(true, "casing_steel_turbine_casing",
                GTBlocks.CASING_STEEL_TURBINE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Magnalium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.BlueSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_stainless_turbine_casing",
                GTBlocks.CASING_STAINLESS_TURBINE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                GTBlocks.CASING_STEEL_TURBINE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "casing_titanium_turbine_casing",
                GTBlocks.CASING_TITANIUM_TURBINE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "PFP",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                GTBlocks.CASING_STEEL_TURBINE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "casing_tungstensteel_turbine_casing",
                GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "PFP", "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                GTBlocks.CASING_STEEL_TURBINE.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "casing_bronze_pipe",
                GTBlocks.CASING_BRONZE_PIPE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PIP", "IFI", "PIP",
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze), 'I',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_steel_pipe",
                GTBlocks.CASING_STEEL_PIPE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PIP", "IFI", "PIP",
                'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Steel), 'I',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_titanium_pipe",
                GTBlocks.CASING_TITANIUM_PIPE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PIP", "IFI",
                "PIP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Titanium), 'I',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_tungstensteel_pipe",
                GTBlocks.CASING_TUNGSTENSTEEL_PIPE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PIP", "IFI",
                "PIP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.TungstenSteel), 'I',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_ptfe_pipe",
                GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft),
                "PIP", "IFI", "PIP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene),
                'F', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Polytetrafluoroethylene), 'I',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Polytetrafluoroethylene));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_bronze_firebox", GTBlocks.FIREBOX_BRONZE.asStack(2),
                "PSP", "SFS", "PSP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze), 'S',
                new MaterialEntry(TagPrefix.rod, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_steel_firebox", GTBlocks.FIREBOX_STEEL.asStack(2),
                "PSP", "SFS", "PSP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Steel), 'S',
                new MaterialEntry(TagPrefix.rod, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_titanium_firebox",
                GTBlocks.FIREBOX_TITANIUM.asStack(2), "PSP", "SFS", "PSP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Titanium), 'S',
                new MaterialEntry(TagPrefix.rod, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_tungstensteel_firebox",
                GTBlocks.FIREBOX_TUNGSTENSTEEL.asStack(2), "PSP", "SFS", "PSP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.TungstenSteel), 'S',
                new MaterialEntry(TagPrefix.rod, GTMaterials.TungstenSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "casing_bronze_gearbox",
                GTBlocks.CASING_BRONZE_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "GFG",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Bronze), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_steel_gearbox",
                GTBlocks.CASING_STEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "GFG",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Steel), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_stainless_steel_gearbox",
                GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "GFG", "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.StainlessSteel), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_titanium_gearbox",
                GTBlocks.CASING_TITANIUM_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "GFG",
                "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.Titanium), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_tungstensteel_gearbox",
                GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "GFG", "PwP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                new MaterialEntry(TagPrefix.frameGt, GTMaterials.TungstenSteel), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.TungstenSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "casing_grate_casing",
                GTBlocks.CASING_GRATE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PVP", "PFP", "PMP", 'P',
                new ItemStack(Blocks.IRON_BARS, 1), 'F', new MaterialEntry(TagPrefix.frameGt, GTMaterials.Steel),
                'M', GTItems.ELECTRIC_MOTOR_MV, 'V', new MaterialEntry(TagPrefix.rotor, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "casing_assembly_control",
                GTBlocks.CASING_ASSEMBLY_CONTROL.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "CPC", "SFE",
                "CMC", 'C', GTOCore.isExpert() ? CustomTags.LuV_CIRCUITS : CustomTags.EV_CIRCUITS, 'P', GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 'S',
                GTItems.SENSOR_IV.asItem(), 'F', new MaterialEntry(TagPrefix.frameGt, GTMaterials.TungstenSteel),
                'E', GTItems.EMITTER_IV.asItem(), 'M', GTItems.ELECTRIC_MOTOR_IV);
        VanillaRecipeHelper.addShapedRecipe(true, "casing_assembly_line",
                GTBlocks.CASING_ASSEMBLY_LINE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PGP", "AFA",
                "PGP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Ruridit), 'A', GTItems.ROBOT_ARM_IV.asItem(), 'F',
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.TungstenSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_yellow_stripes",
                GTBlocks.YELLOW_STRIPES_BLOCK_A.asItem(), "Y  ", " M ", "  B", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_small_yellow_stripes",
                GTBlocks.YELLOW_STRIPES_BLOCK_B.asItem(), "  Y", " M ", "B  ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_radioactive_hazard",
                GTBlocks.RADIOACTIVE_HAZARD_SIGN_BLOCK.asItem(), " YB", " M ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_bio_hazard",
                GTBlocks.BIO_HAZARD_SIGN_BLOCK.asItem(), " Y ", " MB", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_explosion_hazard",
                GTBlocks.EXPLOSION_HAZARD_SIGN_BLOCK.asItem(), " Y ", " M ", "  B", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_fire_hazard",
                GTBlocks.FIRE_HAZARD_SIGN_BLOCK.asItem(), " Y ", " M ", " B ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_acid_hazard",
                GTBlocks.ACID_HAZARD_SIGN_BLOCK.asItem(), " Y ", " M ", "B  ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_magic_hazard",
                GTBlocks.MAGIC_HAZARD_SIGN_BLOCK.asItem(), " Y ", "BM ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_frost_hazard",
                GTBlocks.FROST_HAZARD_SIGN_BLOCK.asItem(), "BY ", " M ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_noise_hazard",
                GTBlocks.NOISE_HAZARD_SIGN_BLOCK.asItem(), "   ", " M ", "BY ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_generic_hazard",
                GTBlocks.GENERIC_HAZARD_SIGN_BLOCK.asItem(), "   ", "BM ", " Y ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_high_voltage_hazard",
                GTBlocks.HIGH_VOLTAGE_HAZARD_SIGN_BLOCK.asItem(), "B  ", " M ", " Y ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_magnetic_hazard",
                GTBlocks.MAGNETIC_HAZARD_SIGN_BLOCK.asItem(), " B ", " M ", " Y ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_antimatter_hazard",
                GTBlocks.ANTIMATTER_HAZARD_SIGN_BLOCK.asItem(), "  B", " M ", " Y ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_high_temperature_hazard",
                GTBlocks.HIGH_TEMPERATURE_HAZARD_SIGN_BLOCK.asItem(), "   ", " MB", " Y ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_sign_void_hazard",
                GTBlocks.VOID_HAZARD_SIGN_BLOCK.asItem(), "   ", " M ", " YB", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_mob_spawner_hazard",
                GTBlocks.MOB_SPAWNER_HAZARD_SIGN_BLOCK.asItem(), "B  ", "YM ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_spatial_storage_hazard",
                GTBlocks.SPATIAL_STORAGE_HAZARD_SIGN_BLOCK.asItem(), " B ", "YM ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_laser_hazard",
                GTBlocks.LASER_HAZARD_SIGN_BLOCK.asItem(), "  B", "YM ", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_mob_hazard",
                GTBlocks.MOB_INFESTATION_HAZARD_SIGN_BLOCK.asItem(), "   ", "YMB", "   ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_boss_hazard",
                GTBlocks.BOSS_HAZARD_SIGN_BLOCK.asItem(), "   ", "YM ", "  B", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_gregification_hazard",
                GTBlocks.GREGIFICATION_HAZARD_SIGN_BLOCK.asItem(), "   ", "YM ", " B ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_causality_hazard",
                GTBlocks.CAUSALITY_HAZARD_SIGN_BLOCK.asItem(), "   ", "YM ", "B  ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_turret_hazard",
                GTBlocks.TURRET_HAZARD_SIGN_BLOCK.asItem(), "   ", " MY", "  B", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);
        VanillaRecipeHelper.addShapedRecipe(true, "warning_high_pressure_hazard",
                GTBlocks.HIGH_PRESSURE_HAZARD_SIGN_BLOCK.asItem(), "   ", " MY", " B ", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'Y', Tags.Items.DYES_YELLOW, 'B', Tags.Items.DYES_BLACK);

        VanillaRecipeHelper.addShapelessRecipe("yellow_stripes_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.YELLOW_STRIPES_BLOCK_A.asItem());
        VanillaRecipeHelper.addShapelessRecipe("small_yellow_stripes_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.YELLOW_STRIPES_BLOCK_B.asItem());
        VanillaRecipeHelper.addShapelessRecipe("radioactive_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.RADIOACTIVE_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("bio_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.BIO_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("explosion_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.EXPLOSION_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("fire_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.FIRE_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("acid_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.ACID_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("magic_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.MAGIC_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("frost_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.FROST_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("noise_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.NOISE_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("generic_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.GENERIC_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("high_voltage_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.HIGH_VOLTAGE_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("magnetic_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.MAGNETIC_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("antimatter_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.ANTIMATTER_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("high_temperature_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.HIGH_TEMPERATURE_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("void_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.VOID_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("mob_spawner_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.MOB_SPAWNER_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("laser_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.LASER_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("mob_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.MOB_INFESTATION_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("boss_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.BOSS_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("gregification_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.GREGIFICATION_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("causality_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.CAUSALITY_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("turret_hazard_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.TURRET_HAZARD_SIGN_BLOCK.asItem());
        VanillaRecipeHelper.addShapelessRecipe("high_pressure_to_steel_solid_casing",
                GTBlocks.CASING_STEEL_SOLID.asStack(), GTBlocks.HIGH_PRESSURE_HAZARD_SIGN_BLOCK.asItem());

        var multiHatchMaterials = new Material[] {
                GTMaterials.Titanium, GTMaterials.TungstenSteel, GTMaterials.NiobiumTitanium,
                GTMaterials.Iridium, GTMaterials.Naquadah, GTMaterials.Neutronium
        };
        for (int i = 0; i < multiHatchMaterials.length; i++) {
            var tier = GTMachineUtils.MULTI_HATCH_TIERS[i];
            var tierName = VN[tier].toLowerCase(Locale.ROOT);

            var material = multiHatchMaterials[i];

            var importHatch = GTMachines.FLUID_IMPORT_HATCH[tier];
            var exportHatch = GTMachines.FLUID_EXPORT_HATCH[tier];

            var importHatch4x = GTMachines.FLUID_IMPORT_HATCH_4X[tier];
            var exportHatch4x = GTMachines.FLUID_EXPORT_HATCH_4X[tier];
            var importHatch9x = GTMachines.FLUID_IMPORT_HATCH_9X[tier];
            var exportHatch9x = GTMachines.FLUID_EXPORT_HATCH_9X[tier];

            VanillaRecipeHelper.addShapedRecipe(true, "fluid_import_hatch_4x_" + tierName,
                    importHatch4x.asItem(), "P", "M",
                    'M', importHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, "fluid_export_hatch_4x_" + tierName,
                    exportHatch4x.asItem(), "M", "P",
                    'M', exportHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, "fluid_import_hatch_9x_" + tierName,
                    importHatch9x.asItem(), "P", "M",
                    'M', importHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeNonupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, "fluid_export_hatch_9x_" + tierName,
                    exportHatch9x.asItem(), "M", "P",
                    'M', exportHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeNonupleFluid, material));
        }

        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_hv", GTMachines.ROTOR_HOLDER[HV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[HV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.BlackSteel), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_ev", GTMachines.ROTOR_HOLDER[EV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.EV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Ultimet), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_iv", GTMachines.ROTOR_HOLDER[IV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.IV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.HSSG), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_luv", GTMachines.ROTOR_HOLDER[LuV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.LuV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Ruthenium), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.RhodiumPlatedPalladium));
        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_zpm", GTMachines.ROTOR_HOLDER[ZPM].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.ZPM].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Trinium), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.NaquadahAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "rotor_holder_uv", GTMachines.ROTOR_HOLDER[UV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[GTValues.UV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Tritanium), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Darmstadtium));

        VanillaRecipeHelper.addShapedRecipe(true, "maintenance_hatch", GTMachines.MAINTENANCE_HATCH.asItem(),
                "dwx", "hHc", "fsr", 'H', GTMachines.HULL[GTValues.LV].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "maintenance_hatch_configurable",
                GTMachines.CONFIGURABLE_MAINTENANCE_HATCH.asItem(), "   ", "CMC", "VHV", 'C',
                CIRCUIT.get(HV), 'M', GTMachines.MAINTENANCE_HATCH.asItem(), 'V', CONVEYOR.get(HV),
                'H', GTMachines.HULL[HV].asItem());
        if (GTOCore.isExpert()) {
            VanillaRecipeHelper.addShapedRecipe(true, "maintenance_hatch_automatic",
                    GTMachines.AUTO_MAINTENANCE_HATCH.asItem(), "CMC", "RHR", "CMC", 'C', CIRCUIT.get(IV), 'M',
                    GTMachines.MAINTENANCE_HATCH.asItem(), 'R', ROBOT_ARM.get(EV), 'H',
                    GTMachines.HULL[HV].asItem());
        } else if (GTOCore.isNormal()) {
            VanillaRecipeHelper.addShapedRecipe(true, "maintenance_hatch_automatic",
                    GTMachines.AUTO_MAINTENANCE_HATCH.asItem(), "CMC", "RHR", "CMC", 'C', CIRCUIT.get(EV), 'M',
                    GTMachines.MAINTENANCE_HATCH.asItem(), 'R', ROBOT_ARM.get(HV), 'H',
                    GTMachines.HULL[HV].asItem());
        } else {
            VanillaRecipeHelper.addShapedRecipe(true, "maintenance_hatch_automatic",
                    GTMachines.AUTO_MAINTENANCE_HATCH.asItem(), "CMC", "RHR", "CMC", 'C', CIRCUIT.get(HV), 'M',
                    GTMachines.MAINTENANCE_HATCH.asItem(), 'R', ROBOT_ARM.get(HV), 'H',
                    GTMachines.HULL[HV].asItem());
        }

        // STEAM MACHINES
        VanillaRecipeHelper.addShapedRecipe(true, "bronze_hull", GTBlocks.BRONZE_HULL.asItem(), "PPP", "PhP",
                "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "bronze_bricks_hull", GTBlocks.BRONZE_BRICKS_HULL.asItem(),
                "PPP", "PhP", "BBB", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B',
                new ItemStack(Blocks.BRICKS));
        VanillaRecipeHelper.addShapedRecipe(true, "steel_hull", GTBlocks.STEEL_HULL.asItem(), "PPP", "PhP",
                "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "steel_bricks_hull", GTBlocks.STEEL_BRICKS_HULL.asItem(),
                "PPP", "PhP", "BBB", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'B',
                new ItemStack(Blocks.BRICKS));

        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_coal_bronze",
                GTMachines.STEAM_SOLID_BOILER.left().asItem(), "PPP", "PwP", "BFB", 'F', Blocks.FURNACE, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'B', new ItemStack(Blocks.BRICKS));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_coal_steel",
                GTMachines.STEAM_SOLID_BOILER.right().asItem(), "PPP", "PwP", "BFB", 'F', Blocks.FURNACE, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'B', new ItemStack(Blocks.BRICKS));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_lava_bronze",
                GTMachines.STEAM_LIQUID_BOILER.left().asItem(), "PPP", "PGP", "PMP", 'M',
                GTBlocks.BRONZE_BRICKS_HULL.asItem(), 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze),
                'G', new ItemStack(Blocks.GLASS, 1));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_lava_steel",
                GTMachines.STEAM_LIQUID_BOILER.right().asItem(), "PPP", "PGP", "PMP", 'M',
                GTBlocks.STEEL_BRICKS_HULL.asItem(), 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel),
                'G', new ItemStack(Blocks.GLASS, 1));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_solar_bronze",
                GTMachines.STEAM_SOLAR_BOILER.left().asItem(), "GGG", "SSS", "PMP", 'M',
                GTBlocks.BRONZE_BRICKS_HULL.asItem(), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Silver), 'G', new ItemStack(Blocks.GLASS));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_boiler_solar_steel",
                GTMachines.STEAM_SOLAR_BOILER.right().asItem(), "GGG", "SSS", "PMP", 'M',
                GTBlocks.STEEL_BRICKS_HULL.asItem(), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Steel), 'S',
                new MaterialEntry(TagPrefix.plateDouble, GTMaterials.Silver), 'G', new ItemStack(Blocks.GLASS));

        VanillaRecipeHelper.addShapedRecipe(true, "steam_furnace_bronze",
                GTMachines.STEAM_FURNACE.left().asItem(), "XXX", "XMX", "XFX", 'M',
                GTBlocks.BRONZE_BRICKS_HULL.asItem(), 'X',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'F', Blocks.FURNACE);
        VanillaRecipeHelper.addShapedRecipe(true, "steam_furnace_steel",
                GTMachines.STEAM_FURNACE.right().asItem(), "XSX", "PMP", "XXX", 'M',
                GTMachines.STEAM_FURNACE.left().asItem(), 'X',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_macerator_bronze",
                GTMachines.STEAM_MACERATOR.left().asItem(), "DXD", "XMX", "PXP", 'M', GTBlocks.BRONZE_HULL.asItem(),
                'X', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'P', CustomTags.PISTONS,
                'D', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_macerator_steel",
                GTMachines.STEAM_MACERATOR.right().asItem(), "WSW", "PMP", "WWW", 'M',
                GTMachines.STEAM_MACERATOR.left().asItem(), 'W',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_extractor_bronze",
                GTMachines.STEAM_EXTRACTOR.left().asItem(), "XXX", "PMG", "XXX", 'M', GTBlocks.BRONZE_HULL.asItem(),
                'X', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'P', CustomTags.PISTONS,
                'G', new ItemStack(Blocks.GLASS));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_extractor_steel",
                GTMachines.STEAM_EXTRACTOR.right().asItem(), "PSP", "WMW", "PPP", 'M',
                GTMachines.STEAM_EXTRACTOR.left().asItem(), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'W',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_hammer_bronze",
                GTMachines.STEAM_HAMMER.left().asItem(), "XPX", "XMX", "XAX", 'M', GTBlocks.BRONZE_HULL.asItem(), 'X',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'P', CustomTags.PISTONS, 'A',
                Blocks.ANVIL);
        VanillaRecipeHelper.addShapedRecipe(true, "steam_hammer_steel",
                GTMachines.STEAM_HAMMER.right().asItem(), "WSW", "PMP", "WWW", 'M',
                GTMachines.STEAM_HAMMER.left().asItem(), 'S', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel),
                'W', new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_compressor_bronze",
                GTMachines.STEAM_COMPRESSOR.left().asItem(), "XXX", "PMP", "XXX", 'M', GTBlocks.BRONZE_HULL.asItem(),
                'X', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze), 'P', CustomTags.PISTONS);
        VanillaRecipeHelper.addShapedRecipe(true, "steam_compressor_steel",
                GTMachines.STEAM_COMPRESSOR.right().asItem(), "PSP", "WMW", "PPP", 'M',
                GTMachines.STEAM_COMPRESSOR.left().asItem(), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'W',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy));

        VanillaRecipeHelper.addShapedRecipe(true, "steam_alloy_smelter_steel",
                GTMachines.STEAM_ALLOY_SMELTER.right().asItem(), "WSW", "WMW", "WPW", 'M',
                GTMachines.STEAM_ALLOY_SMELTER.left().asItem(), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'W',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_rock_breaker_bronze",
                GTMachines.STEAM_ROCK_CRUSHER.left().asItem(), "PXP", "XMX", "DXD", 'M',
                GTBlocks.BRONZE_HULL.asItem(), 'X', new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.Bronze),
                'P', CustomTags.PISTONS, 'D', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_rock_breaker_steel",
                GTMachines.STEAM_ROCK_CRUSHER.right().asItem(), "WSW", "PMP", "WWW", 'M',
                GTMachines.STEAM_ROCK_CRUSHER.left().asItem(), 'W',
                new MaterialEntry(TagPrefix.plate, GTMaterials.WroughtIron), 'S',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'P',
                new MaterialEntry(TagPrefix.pipeSmallFluid, GTMaterials.TinAlloy));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_miner_bronze",
                GTMachines.STEAM_MINER.first().asItem(),
                "DSD", "SMS", "GSG",
                'M', GTBlocks.BRONZE_BRICKS_HULL.asItem(),
                'S', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze),
                'D', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond),
                'G', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_miner_steel",
                GTMachines.STEAM_MINER.second().asItem(), "DSD", "SMS", "GSG",
                'M', GTMachines.STEAM_MINER.first().asItem(),
                'S', new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.TinAlloy),
                'D', new MaterialEntry(TagPrefix.gem, GTMaterials.Diamond),
                'G', new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Steel));
        // MULTI BLOCK CONTROLLERS
        VanillaRecipeHelper.addShapedRecipe(true, "bronze_primitive_blast_furnace",
                GTMultiMachines.PRIMITIVE_BLAST_FURNACE.asItem(), "hRS", "PBR", "dRS", 'R',
                new MaterialEntry(TagPrefix.rod, GTMaterials.Iron), 'S',
                new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'B',
                GTBlocks.CASING_PRIMITIVE_BRICKS.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "coke_oven", GTMultiMachines.COKE_OVEN.asItem(), "PIP",
                "IwI",
                "PIP", 'P', GTBlocks.CASING_COKE_BRICKS.asItem(), 'I',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(true, "coke_oven_hatch", GTMachines.COKE_OVEN_HATCH.asItem(),
                "CBD", 'C', Tags.Items.CHESTS_WOODEN, 'B', GTBlocks.CASING_COKE_BRICKS.asItem(), 'D',
                GTMachines.WOODEN_DRUM.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "charcoal_pile_igniter",
                GTMultiMachines.CHARCOAL_PILE_IGNITER.asItem(),
                "ERE", "EHE", "FFF",
                'E', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze),
                'R', new MaterialEntry(TagPrefix.rotor, GTMaterials.Iron),
                'H', GTBlocks.BRONZE_BRICKS_HULL,
                'F', Items.FLINT_AND_STEEL);
        if (!ConfigHolder.INSTANCE.recipes.hardMultiRecipes) {
            VanillaRecipeHelper.addShapedRecipe(true, "electric_blast_furnace",
                    GTMultiMachines.ELECTRIC_BLAST_FURNACE.asItem(), "FFF", "CMC", "WCW", 'M',
                    GTBlocks.CASING_INVAR_HEATPROOF.asItem(), 'F', Blocks.FURNACE.asItem(), 'C',
                    CustomTags.LV_CIRCUITS,
                    'W', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin));
        } else {
            VanillaRecipeHelper.addShapedRecipe(true, "electric_blast_furnace",
                    GTMultiMachines.ELECTRIC_BLAST_FURNACE.asItem(), "FFF", "CMC", "WCW", 'M',
                    GTBlocks.CASING_INVAR_HEATPROOF.asItem(), 'F', GTMachines.ELECTRIC_FURNACE[LV].asItem(), 'C',
                    CustomTags.LV_CIRCUITS,
                    'W', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin));
        }
        VanillaRecipeHelper.addShapedRecipe(true, "vacuum_freezer", GTMultiMachines.VACUUM_FREEZER.asItem(),
                "PPP", "CMC", "WCW", 'M', GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asItem(), 'P', GTItems.ELECTRIC_PUMP_HV,
                'C', CustomTags.EV_CIRCUITS, 'W', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold));
        VanillaRecipeHelper.addShapedRecipe(true, "implosion_compressor",
                GTMultiMachines.IMPLOSION_COMPRESSOR.asItem(), "OOO", "CMC", "WCW", 'M',
                GTBlocks.CASING_STEEL_SOLID.asItem(), 'O', new MaterialEntry(TagPrefix.rock, GTMaterials.Obsidian),
                'C', CustomTags.HV_CIRCUITS, 'W', new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold));
        VanillaRecipeHelper.addShapedRecipe(true, "distillation_tower",
                GTMultiMachines.DISTILLATION_TOWER.asItem(), "CBC", "FMF", "CBC", 'M', GTMachines.HULL[HV].asItem(),
                'B',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.StainlessSteel), 'C', CustomTags.EV_CIRCUITS,
                'F', GTItems.ELECTRIC_PUMP_HV);
        VanillaRecipeHelper.addShapedRecipe(true, "cracking_unit", GTMultiMachines.CRACKER.asItem(), "CEC",
                "PHP",
                "CEC", 'C', GTBlocks.COIL_CUPRONICKEL.asItem(), 'E', GTItems.ELECTRIC_PUMP_HV.asItem(), 'P',
                CustomTags.HV_CIRCUITS, 'H', GTMachines.HULL[HV].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "pyrolyse_oven", GTMultiMachines.PYROLYSE_OVEN.asItem(),
                "WEP",
                "EME", "WCP", 'M', GTMachines.HULL[GTValues.MV].asItem(), 'W', GTItems.ELECTRIC_PISTON_MV.asItem(),
                'P', new MaterialEntry(TagPrefix.wireGtQuadruple, GTMaterials.Cupronickel), 'E',
                CustomTags.MV_CIRCUITS, 'C', GTItems.ELECTRIC_PUMP_MV);
        VanillaRecipeHelper.addShapedRecipe(true, "large_combustion_engine",
                GTMultiMachines.LARGE_COMBUSTION_ENGINE.asItem(), "PCP", "EME", "GWG", 'M',
                GTMachines.HULL[GTValues.EV].asItem(), 'P', GTItems.ELECTRIC_PISTON_EV.asItem(), 'E',
                GTItems.ELECTRIC_MOTOR_EV.asItem(), 'C', CustomTags.IV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Aluminium), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "extreme_combustion_engine",
                GTMultiMachines.EXTREME_COMBUSTION_ENGINE.asItem(), "PCP", "EME", "GWG", 'M',
                GTMachines.HULL[GTValues.IV].asItem(), 'P', GTItems.ELECTRIC_PISTON_IV.asItem(), 'E',
                GTItems.ELECTRIC_MOTOR_IV.asItem(), 'C', CustomTags.LuV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.HSSG), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "engine_intake_casing",
                GTBlocks.CASING_ENGINE_INTAKE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP", "RFR",
                "PwP", 'R', new MaterialEntry(TagPrefix.rotor, GTMaterials.Titanium), 'F',
                GTBlocks.CASING_TITANIUM_STABLE.asItem(), 'P',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "extreme_engine_intake_casing",
                GTBlocks.CASING_EXTREME_ENGINE_INTAKE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "PhP",
                "RFR", "PwP", 'R', new MaterialEntry(TagPrefix.rotor, GTMaterials.TungstenSteel), 'F',
                GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.asItem(), 'P',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "multi_furnace", GTMultiMachines.MULTI_SMELTER.asItem(),
                "PPP",
                "ASA", "CAC", 'P', Blocks.FURNACE, 'A', CustomTags.HV_CIRCUITS, 'S',
                GTBlocks.CASING_INVAR_HEATPROOF.asItem(), 'C',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Copper));

        VanillaRecipeHelper.addShapedRecipe(true, "large_steam_turbine",
                GTMultiMachines.LARGE_STEAM_TURBINE.asItem(), "PSP", "SAS", "CSC", 'S',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Steel), 'P', CustomTags.HV_CIRCUITS, 'A',
                GTMachines.HULL[HV].asItem(), 'C', new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "large_gas_turbine",
                GTMultiMachines.LARGE_GAS_TURBINE.asItem(),
                "PSP", "SAS", "CSC", 'S', new MaterialEntry(TagPrefix.gear, GTMaterials.StainlessSteel), 'P',
                CustomTags.EV_CIRCUITS, 'A', GTMachines.HULL[GTValues.EV].asItem(), 'C',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.StainlessSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "large_bronze_boiler",
                GTMultiMachines.LARGE_BOILER_BRONZE.asItem(), "PSP", "SAS", "PSP", 'P',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin), 'S', CustomTags.LV_CIRCUITS, 'A',
                GTBlocks.FIREBOX_BRONZE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "large_steel_boiler",
                GTMultiMachines.LARGE_BOILER_STEEL.asItem(), "PSP", "SAS", "PSP", 'P',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Copper), 'S', CustomTags.HV_CIRCUITS, 'A',
                GTBlocks.FIREBOX_STEEL.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "large_titanium_boiler",
                GTMultiMachines.LARGE_BOILER_TITANIUM.asItem(), "PSP", "SAS", "PSP", 'P',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold), 'S', CustomTags.EV_CIRCUITS, 'A',
                GTBlocks.FIREBOX_TITANIUM.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "large_tungstensteel_boiler",
                GTMultiMachines.LARGE_BOILER_TUNGSTENSTEEL.asItem(), "PSP", "SAS", "PSP", 'P',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Aluminium), 'S', CustomTags.IV_CIRCUITS, 'A',
                GTBlocks.FIREBOX_TUNGSTENSTEEL.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "assembly_line", GTMultiMachines.ASSEMBLY_LINE.asItem(),
                "CRC",
                "SAS", "CRC", 'A', GTMachines.HULL[GTValues.IV].asItem(), 'R', GTItems.ROBOT_ARM_IV, 'C',
                GTBlocks.CASING_ASSEMBLY_CONTROL.asItem(), 'S', CustomTags.IV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(true, "large_chemical_reactor",
                GTMultiMachines.LARGE_CHEMICAL_REACTOR.asItem(), "CRC", "PMP", "CHC", 'C', CustomTags.HV_CIRCUITS, 'R',
                ChemicalHelper.get(TagPrefix.rotor, GTMaterials.StainlessSteel), 'P',
                ChemicalHelper.get(TagPrefix.pipeLargeFluid, GTMaterials.Polytetrafluoroethylene), 'M',
                GTItems.ELECTRIC_MOTOR_HV.asItem(), 'H', GTMachines.HULL[HV].asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "power_substation",
                GTMultiMachines.POWER_SUBSTATION.asItem(),
                "LPL", "CBC", "LPL", 'L', GTItems.LAPOTRON_CRYSTAL, 'P', GTItems.POWER_INTEGRATED_CIRCUIT, 'C',
                CustomTags.LuV_CIRCUITS, 'B', GTBlocks.CASING_PALLADIUM_SUBSTATION.asItem());

        // GENERATORS
        VanillaRecipeHelper.addShapedRecipe(true, "diesel_generator_lv", GTMachines.COMBUSTION[LV].asItem(),
                "PCP", "EME", "GWG", 'M', GTMachines.HULL[GTValues.LV].asItem(), 'P', GTItems.ELECTRIC_PISTON_LV, 'E',
                GTItems.ELECTRIC_MOTOR_LV, 'C', CustomTags.LV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "diesel_generator_mv", GTMachines.COMBUSTION[MV].asItem(),
                "PCP", "EME", "GWG", 'M', GTMachines.HULL[GTValues.MV].asItem(), 'P', GTItems.ELECTRIC_PISTON_MV, 'E',
                GTItems.ELECTRIC_MOTOR_MV, 'C', CustomTags.MV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Copper), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.Aluminium));
        VanillaRecipeHelper.addShapedRecipe(true, "diesel_generator_hv", GTMachines.COMBUSTION[HV].asItem(),
                "PCP", "EME", "GWG", 'M', GTMachines.HULL[HV].asItem(), 'P', GTItems.ELECTRIC_PISTON_HV, 'E',
                GTItems.ELECTRIC_MOTOR_HV, 'C', CustomTags.HV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold), 'G',
                new MaterialEntry(TagPrefix.gear, GTMaterials.StainlessSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "gas_turbine_lv", GTMachines.GAS_TURBINE[LV].asItem(),
                "CRC", "RMR", "EWE", 'M', GTMachines.HULL[GTValues.LV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_LV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Tin), 'C', CustomTags.LV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin));
        VanillaRecipeHelper.addShapedRecipe(true, "gas_turbine_mv", GTMachines.GAS_TURBINE[MV].asItem(),
                "CRC", "RMR", "EWE", 'M', GTMachines.HULL[GTValues.MV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_MV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Bronze), 'C', CustomTags.MV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Copper));
        VanillaRecipeHelper.addShapedRecipe(true, "gas_turbine_hv", GTMachines.GAS_TURBINE[HV].asItem(),
                "CRC", "RMR", "EWE", 'M', GTMachines.HULL[HV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_HV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Steel), 'C', CustomTags.HV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold));

        VanillaRecipeHelper.addShapedRecipe(true, "steam_turbine_lv", GTMachines.STEAM_TURBINE[LV].asItem(),
                "PCP", "RMR", "EWE", 'M', GTMachines.HULL[GTValues.LV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_LV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Tin), 'C', CustomTags.LV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Tin), 'P',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_turbine_mv", GTMachines.STEAM_TURBINE[MV].asItem(),
                "PCP", "RMR", "EWE", 'M', GTMachines.HULL[GTValues.MV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_MV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Bronze), 'C', CustomTags.MV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Copper), 'P',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "steam_turbine_hv", GTMachines.STEAM_TURBINE[HV].asItem(),
                "PCP", "RMR", "EWE", 'M', GTMachines.HULL[HV].asItem(), 'E', GTItems.ELECTRIC_MOTOR_HV, 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Steel), 'C', CustomTags.HV_CIRCUITS, 'W',
                new MaterialEntry(TagPrefix.cableGtSingle, GTMaterials.Gold), 'P',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.StainlessSteel));

        // TODO Crafting station
        // VanillaRecipeHelper.addShapedRecipe( true, "workbench_bronze", GTMachines.WORKBENCH.getStackForm(),
        // "CSC", "PWP", "PsP", 'C', OreDictNames.chestWood, 'W', new ItemStack(Blocks.CRAFTING_TABLE), 'S',
        // OreDictUnifier.get("slabWood"), 'P', new UnificationEntry(TagPrefix.plank, GTMaterials.Wood));

        VanillaRecipeHelper.addShapedRecipe(true, "primitive_pump", GTMultiMachines.PRIMITIVE_PUMP.asItem(),
                "RGS", "OWd", "CLC", 'R', new MaterialEntry(TagPrefix.ring, GTMaterials.Iron), 'G',
                new MaterialEntry(TagPrefix.pipeNormalFluid, GTMaterials.Wood), 'S',
                new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'O',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.Iron), 'W', GTBlocks.TREATED_WOOD_PLANK.asItem(),
                'C', new ItemStack(Items.COBBLESTONE_SLAB), 'L',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Wood));
        VanillaRecipeHelper.addShapedRecipe(true, "pump_deck",
                GTBlocks.CASING_PUMP_DECK.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft), "SWS", "dCh", 'S',
                new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'W', GTBlocks.TREATED_WOOD_PLANK.asItem(),
                'C', new ItemStack(Items.COBBLESTONE_SLAB));
        VanillaRecipeHelper.addShapedRecipe(true, "pump_hatch", GTMachines.PUMP_HATCH.asItem(), "SRd", "PLP",
                "CRC", 'S', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'R',
                new MaterialEntry(TagPrefix.ring, GTMaterials.Iron), 'P', GTBlocks.TREATED_WOOD_PLANK.asItem(), 'L',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Wood), 'C',
                new ItemStack(Items.COBBLESTONE_SLAB));

        VanillaRecipeHelper.addShapedRecipe(true, "wood_multiblock_tank",
                GTMultiMachines.WOODEN_MULTIBLOCK_TANK.asItem(), " R ", "rCs", " R ", 'R',
                new MaterialEntry(TagPrefix.ring, GTMaterials.Copper), 'C', GTBlocks.CASING_WOOD_WALL.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "steel_multiblock_tank",
                GTMultiMachines.STEEL_MULTIBLOCK_TANK.asItem(), " R ", "hCw", " R ", 'R',
                new MaterialEntry(TagPrefix.ring, GTMaterials.Steel), 'C', GTBlocks.CASING_STEEL_SOLID.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "wood_tank_valve",
                GTMultiMachines.WOODEN_TANK_VALVE.asItem(),
                " R ", "rCs", " O ", 'O', new MaterialEntry(TagPrefix.rotor, GTMaterials.Copper), 'R',
                new MaterialEntry(TagPrefix.ring, GTMaterials.Copper), 'C', GTBlocks.CASING_WOOD_WALL.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "steel_tank_valve",
                GTMultiMachines.STEEL_TANK_VALVE.asItem(),
                " R ", "hCw", " O ", 'O', new MaterialEntry(TagPrefix.rotor, GTMaterials.Steel), 'R',
                new MaterialEntry(TagPrefix.ring, GTMaterials.Steel), 'C', GTBlocks.CASING_STEEL_SOLID.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "wood_wall", GTBlocks.CASING_WOOD_WALL.asItem(), "W W",
                "sPh", "W W", 'W', GTBlocks.TREATED_WOOD_PLANK.asItem(), 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Copper));

        // MACHINES
        registerMachineRecipe(GTMachines.ALLOY_SMELTER, "ECE", "CMC", "WCW", 'M', HULL, 'E', CIRCUIT, 'W',
                CABLE, 'C', COIL_HEATING_DOUBLE);
        registerMachineRecipe(GTMachines.ASSEMBLER, "ACA", "VMV", "WCW", 'M', HULL, 'V', CONVEYOR, 'A',
                ROBOT_ARM, 'C', CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.BENDER, "PBP", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE, 'B', PLATE);
        registerMachineRecipe(GTMachines.CANNER, "WPW", "CMC", "GGG", 'M', HULL, 'P', PUMP, 'C', CIRCUIT, 'W',
                CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.COMPRESSOR, " C ", "PMP", "WCW", 'M', HULL, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.CUTTER, "WCG", "VMB", "CWE", 'M', HULL, 'E', MOTOR, 'V', CONVEYOR,
                'C', CIRCUIT, 'W', CABLE, 'G', GLASS, 'B', SAWBLADE);
        registerMachineRecipe(GTMachines.ELECTRIC_FURNACE, "ECE", "CMC", "WCW", 'M', HULL, 'E', CIRCUIT, 'W',
                CABLE, 'C', COIL_HEATING);
        registerMachineRecipe(GTMachines.EXTRACTOR, "GCG", "EMP", "WCW", 'M', HULL, 'E', PISTON, 'P', PUMP,
                'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.EXTRUDER, "CCE", "XMP", "CCE", 'M', HULL, 'X', PISTON, 'E', CIRCUIT,
                'P', PIPE_NORMAL, 'C', COIL_HEATING_DOUBLE);
        registerMachineRecipe(GTMachines.LATHE, "WCW", "EMD", "CWP", 'M', HULL, 'E', MOTOR, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE, 'D', GRINDER);
        registerMachineRecipe(GTMachines.MACERATOR, "PEG", "WWM", "CCW", 'M', HULL, 'E', MOTOR, 'P', PISTON,
                'C', CIRCUIT, 'W', CABLE, 'G', GRINDER);
        registerMachineRecipe(GTMachines.WIREMILL, "EWE", "CMC", "EWE", 'M', HULL, 'E', MOTOR, 'C', CIRCUIT,
                'W', CABLE);
        registerMachineRecipe(GTMachines.CENTRIFUGE, "CEC", "WMW", "CEC", 'M', HULL, 'E', MOTOR, 'C', CIRCUIT,
                'W', CABLE);
        registerMachineRecipe(GTMachines.ELECTROLYZER, "IGI", "IMI", "CWC", 'M', HULL, 'C', CIRCUIT, 'W',
                CABLE, 'I', WIRE_ELECTRIC, 'G', GLASS);
        registerMachineRecipe(GTMachines.THERMAL_CENTRIFUGE, "CEC", "OMO", "WEW", 'M', HULL, 'E', MOTOR, 'C',
                CIRCUIT, 'W', CABLE, 'O', COIL_HEATING_DOUBLE);
        registerMachineRecipe(GTMachines.ORE_WASHER, "RGR", "CEC", "WMW", 'M', HULL, 'R', ROTOR, 'E', MOTOR,
                'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.CHEMICAL_REACTOR, "GRG", "WEW", "CMC", 'M', HULL, 'R', ROTOR, 'E',
                MOTOR, 'C', CIRCUIT, 'W', CABLE, 'G', PIPE_REACTOR);
        registerMachineRecipe(GTMachines.PACKER, "BCB", "RMV", "WCW", 'M', HULL, 'R', ROBOT_ARM, 'V',
                CONVEYOR, 'C', CIRCUIT, 'W', CABLE, 'B', Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(GTMachines.BREWERY, "GPG", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'B',
                ROD_DISTILLATION, 'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.FERMENTER, "WPW", "GMG", "WCW", 'M', HULL, 'P', PUMP, 'C', CIRCUIT,
                'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.DISTILLERY, "GBG", "CMC", "WPW", 'M', HULL, 'P', PUMP, 'B',
                ROD_DISTILLATION, 'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.FLUID_SOLIDIFIER, "PGP", "WMW", "CBC", 'M', HULL, 'P', PUMP, 'C',
                CIRCUIT, 'W', CABLE, 'G', GLASS, 'B', Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(GTMachines.CHEMICAL_BATH, "VGW", "PGV", "CMC", 'M', HULL, 'P', PUMP, 'V',
                CONVEYOR, 'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.POLARIZER, "ZSZ", "WMW", "ZSZ", 'M', HULL, 'S',
                ROD_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'W', CABLE);
        registerMachineRecipe(GTMachines.ELECTROMAGNETIC_SEPARATOR, "VWZ", "WMS", "CWZ", 'M', HULL, 'S',
                ROD_ELECTROMAGNETIC, 'Z', COIL_ELECTRIC, 'V', CONVEYOR, 'C', CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.AUTOCLAVE, "IGI", "IMI", "CPC", 'M', HULL, 'P', PUMP, 'C', CIRCUIT,
                'I', PLATE, 'G', GLASS);
        registerMachineRecipe(GTMachines.MIXER, "GRG", "GEG", "CMC", 'M', HULL, 'E', MOTOR, 'R', ROTOR, 'C',
                CIRCUIT, 'G', GLASS);
        registerMachineRecipe(GTMachines.LASER_ENGRAVER, "PEP", "CMC", "WCW", 'M', HULL, 'E', EMITTER, 'P',
                PISTON, 'C', CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.FORMING_PRESS, "WPW", "CMC", "WPW", 'M', HULL, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.FORGE_HAMMER, "WPW", "CMC", "WAW", 'M', HULL, 'P', PISTON, 'C',
                CIRCUIT, 'W', CABLE, 'A', Blocks.ANVIL);
        registerMachineRecipe(GTMachines.FLUID_HEATER, "OGO", "PMP", "WCW", 'M', HULL, 'P', PUMP, 'O',
                COIL_HEATING_DOUBLE, 'C', CIRCUIT, 'W', CABLE, 'G', GLASS);
        registerMachineRecipe(GTMachines.SIFTER, "WFW", "PMP", "CFC", 'M', HULL, 'P', PISTON, 'F',
                GTItems.ITEM_FILTER, 'C', CIRCUIT, 'W', CABLE);
        registerMachineRecipe(GTMachines.ARC_FURNACE, "WGW", "CMC", "PPP", 'M', HULL, 'P', PLATE, 'C',
                CIRCUIT, 'W', CABLE_QUAD, 'G', new MaterialEntry(TagPrefix.dust, GTMaterials.Graphite));
        registerMachineRecipe(GTMachines.CIRCUIT_ASSEMBLER, "RIE", "CHC", "WIW", 'R', ROBOT_ARM, 'I',
                BETTER_CIRCUIT, 'E', EMITTER, 'C', CONVEYOR, 'H', HULL, 'W', CABLE);

        // TODO Replication system
        // registerMachineRecipe(GTMachines.MASS_FABRICATOR, "CFC", "QMQ", "CFC", 'M', HULL, 'Q', CABLE_QUAD, 'C',
        // BETTER_CIRCUIT, 'F', FIELD_GENERATOR);
        // registerMachineRecipe(GTMachines.REPLICATOR, "EFE", "CMC", "EQE", 'M', HULL, 'Q', CABLE_QUAD, 'C',
        // BETTER_CIRCUIT, 'F', FIELD_GENERATOR, 'E', EMITTER);
        registerMachineRecipe(GTMachines.SCANNER, "CEC", "WHW", "CSC", 'C', BETTER_CIRCUIT, 'E', EMITTER, 'W',
                CABLE, 'H', HULL, 'S', SENSOR);
        registerMachineRecipe(GTMachines.GAS_COLLECTOR, "WFW", "PHP", "WCW", 'W', Blocks.IRON_BARS, 'F',
                GTItems.FLUID_FILTER, 'P', PUMP, 'H', HULL, 'C', CIRCUIT);
        registerMachineRecipe(GTMachines.AIR_SCRUBBER, "PFP", "FHF", "CFC", 'F', GTItems.FLUID_FILTER,
                'P', PUMP, 'H', HULL, 'C', CIRCUIT);
        registerMachineRecipe(GTMachines.ROCK_CRUSHER, "PMW", "CHC", "GGG", 'P', PISTON, 'M', MOTOR, 'W',
                GRINDER, 'C', CABLE, 'H', HULL, 'G', GLASS);
        registerMachineRecipe(GTMachines.PUMP, "WGW", "GMG", "TGT", 'M', HULL, 'W', CIRCUIT, 'G', PUMP, 'T',
                PIPE_LARGE);

        registerMachineRecipe(GTMachines.FISHER, "WTW", "PMP", "TGT", 'M', HULL, 'W', CIRCUIT, 'G', PUMP, 'T',
                MOTOR, 'P', PISTON);
        registerMachineRecipe(GTMachines.ITEM_COLLECTOR, "MRM", "RHR", "CWC", 'M', MOTOR, 'R', ROTOR, 'H',
                HULL, 'C', CIRCUIT, 'W', CABLE);
        if (ConfigHolder.INSTANCE.machines.enableWorldAccelerators)
            registerMachineRecipe(GTMachines.WORLD_ACCELERATOR, "FSF", "EHE", "FSF", 'F', FIELD_GENERATOR,
                    'S', SENSOR, 'E', EMITTER, 'H', HULL);
        registerMachineRecipe(GTMachines.BLOCK_BREAKER, "MGM", "CHC", "WSW", 'M', MOTOR, 'H', HULL, 'C',
                CIRCUIT, 'W', CABLE, 'S', Tags.Items.CHESTS_WOODEN, 'G', GRINDER);
        registerMachineRecipe(GTMachines.MINER, "MMM", "WHW", "CSC", 'M', MOTOR, 'W', CABLE, 'H', HULL, 'C',
                CIRCUIT, 'S', SENSOR);

        registerMachineRecipe(GTMachines.MUFFLER_HATCH, "HM", "PR", 'H', HULL, 'M', MOTOR, 'P', PIPE_NORMAL,
                'R', ROTOR);

        registerMachineRecipe(ArrayUtils.subarray(GTMachines.DIODE, GTValues.ULV, HV), "CDC", "DHD", "PDP",
                'H', HULL, 'D', CustomTags.DIODES, 'P', PLATE, 'C', CABLE_QUAD);
        registerMachineRecipe(ArrayUtils.subarray(GTMachines.DIODE, HV, GTValues.LuV), "CDC", "DHD", "PDP",
                'H', HULL, 'D', GTItems.SMD_DIODE, 'P', PLATE, 'C', CABLE_QUAD);
        registerMachineRecipe(ArrayUtils.subarray(GTMachines.DIODE, GTValues.LuV, GTMachines.DIODE.length),
                "CDC", "DHD", "PDP", 'H', HULL, 'D', GTItems.ADVANCED_SMD_DIODE, 'P', PLATE, 'C', CABLE_QUAD);

        registerMachineRecipe(ArrayUtils.subarray(GTMachines.TRANSFORMER, GTValues.ULV, GTValues.MV), " CC",
                "TH ", " CC", 'C', CABLE, 'T', CABLE_TIER_UP, 'H', HULL);
        registerMachineRecipe(ArrayUtils.subarray(GTMachines.TRANSFORMER, GTValues.MV, GTValues.UHV), "WCC",
                "TH ", "WCC", 'W', POWER_COMPONENT, 'C', CABLE, 'T', CABLE_TIER_UP, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_2A, GTValues.ULV, GTValues.MV), " CC", "TH ", " CC",
                'C', CABLE_DOUBLE, 'T', CABLE_TIER_UP_DOUBLE, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_2A, GTValues.MV, GTValues.UHV), "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_DOUBLE, 'T', CABLE_TIER_UP_DOUBLE, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_4A, GTValues.ULV, GTValues.MV), " CC", "TH ", " CC",
                'C', CABLE_QUAD, 'T', CABLE_TIER_UP_QUAD, 'H', HULL);
        registerMachineRecipe(
                ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_4A, GTValues.MV, GTValues.UHV), "WCC", "TH ", "WCC",
                'W', POWER_COMPONENT, 'C', CABLE_QUAD, 'T', CABLE_TIER_UP_QUAD, 'H', HULL);
        registerMachineRecipe(ArrayUtils.subarray(GTMachines.POWER_TRANSFORMER, GTValues.ULV, GTValues.MV),
                " CC", "TH ", " CC", 'C', CABLE_HEX, 'T', CABLE_TIER_UP_HEX, 'H', HULL);
        registerMachineRecipe(ArrayUtils.subarray(GTMachines.POWER_TRANSFORMER, GTValues.MV, GTValues.UHV),
                "WCC", "TH ", "WCC", 'W', POWER_COMPONENT, 'C', CABLE_HEX, 'T', CABLE_TIER_UP_HEX, 'H', HULL);

        registerMachineRecipe(GTMachines.BATTERY_BUFFER_4, "WTW", "WMW", 'M', HULL, 'W', WIRE_QUAD, 'T',
                Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(GTMachines.BATTERY_BUFFER_8, "WTW", "WMW", 'M', HULL, 'W', WIRE_OCT, 'T',
                Tags.Items.CHESTS_WOODEN);
        registerMachineRecipe(GTMachines.BATTERY_BUFFER_16, "WTW", "WMW", 'M', HULL, 'W', WIRE_HEX, 'T',
                Tags.Items.CHESTS_WOODEN);

        registerMachineRecipe(GTMachines.CHARGER_4, "WTW", "WMW", "BCB", 'M', HULL, 'W', WIRE_QUAD, 'T',
                Tags.Items.CHESTS_WOODEN, 'B', CABLE, 'C', CIRCUIT);

        Material[] fluidMap = new Material[] { GTMaterials.Glue, GTMaterials.Polyethylene,
                GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole };

        for (var machine : GTMachines.FLUID_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                ASSEMBLER_RECIPES.recipeBuilder("fluid_hatch_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(DRUM.get(tier))
                        .circuitMeta(1)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save();
            }
        }

        for (var machine : GTMachines.FLUID_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                ASSEMBLER_RECIPES.recipeBuilder("fluid_export_hatch_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(DRUM.get(tier))
                        .circuitMeta(2)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save();
            }
        }

        for (var machine : GTMachines.ITEM_IMPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                ASSEMBLER_RECIPES.recipeBuilder("item_import_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(CRATE.get(tier))
                        .circuitMeta(1)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save();
            }
        }

        for (var machine : GTMachines.ITEM_EXPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                ASSEMBLER_RECIPES.recipeBuilder("item_export_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(CRATE.get(tier))
                        .circuitMeta(2)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save();
            }
        }

        VanillaRecipeHelper.addShapedRecipe(true, "wooden_crate", GTMachines.WOODEN_CRATE.asItem(), "RPR",
                "PsP", "RPR", 'P', ItemTags.PLANKS, 'R', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(true, "bronze_crate", GTMachines.BRONZE_CRATE.asItem(), "RPR",
                "PhP", "RPR", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "steel_crate", GTMachines.STEEL_CRATE.asItem(), "RPR",
                "PhP", "RPR", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "aluminium_crate", GTMachines.ALUMINIUM_CRATE.asItem(),
                "RPR", "PhP", "RPR", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Aluminium));
        VanillaRecipeHelper.addShapedRecipe(true, "stainless_steel_crate",
                GTMachines.STAINLESS_STEEL_CRATE.asItem(), "RPR", "PhP", "RPR", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "titanium_crate", GTMachines.TITANIUM_CRATE.asItem(),
                "RPR", "PhP", "RPR", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "tungstensteel_crate",
                GTMachines.TUNGSTENSTEEL_CRATE.asItem(), "RPR", "PhP", "RPR", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.TungstenSteel));

        VanillaRecipeHelper.addShapedRecipe(true, "wooden_barrel", GTMachines.WOODEN_DRUM.asItem(), "rSs",
                "PRP", "PRP", 'S', GTItems.STICKY_RESIN.asItem(), 'P', ItemTags.PLANKS, 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Iron));
        VanillaRecipeHelper.addShapedRecipe(true, "bronze_drum", GTMachines.BRONZE_DRUM.asItem(), " h ",
                "PRP", "PRP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Bronze), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Bronze));
        VanillaRecipeHelper.addShapedRecipe(true, "steel_drum", GTMachines.STEEL_DRUM.asItem(), " h ", "PRP",
                "PRP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Steel));
        VanillaRecipeHelper.addShapedRecipe(true, "aluminium_drum", GTMachines.ALUMINIUM_DRUM.asItem(),
                " h ", "PRP", "PRP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Aluminium));
        VanillaRecipeHelper.addShapedRecipe(true, "stainless_steel_drum",
                GTMachines.STAINLESS_STEEL_DRUM.asItem(), " h ", "PRP", "PRP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "gold_drum", GTMachines.GOLD_DRUM.asItem(), " h ", "PRP",
                "PRP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'R',
                new MaterialEntry(TagPrefix.rodLong, GTMaterials.Gold));

        // Hermetic Casings
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_lv", GTBlocks.HERMETIC_CASING_LV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polyethylene));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_mv", GTBlocks.HERMETIC_CASING_MV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'F',
                new MaterialEntry(TagPrefix.pipeLargeItem, GTMaterials.PolyvinylChloride));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_hv", GTBlocks.HERMETIC_CASING_HV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polytetrafluoroethylene));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_ev", GTBlocks.HERMETIC_CASING_EV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.StainlessSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_iv", GTBlocks.HERMETIC_CASING_IV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.TungstenSteel), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Titanium));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_luv",
                GTBlocks.HERMETIC_CASING_LuV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.TungstenSteel));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_zpm",
                GTBlocks.HERMETIC_CASING_ZPM.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.NaquadahAlloy), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.NiobiumTitanium));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_uv", GTBlocks.HERMETIC_CASING_UV.asItem(),
                "PPP", "PFP", "PPP", 'P', new MaterialEntry(TagPrefix.plate, GTMaterials.Darmstadtium), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Naquadah));
        VanillaRecipeHelper.addShapedRecipe(true, "hermetic_casing_max",
                GTBlocks.HERMETIC_CASING_UHV.asItem(), "PPP", "PFP", "PPP", 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium), 'F',
                new MaterialEntry(TagPrefix.pipeLargeFluid, GTMaterials.Duranium));

        // Super / Quantum Chests
        VanillaRecipeHelper.addShapedRecipe(true, "super_chest_lv", GTMachines.SUPER_CHEST[LV].asItem(),
                "CPC", "PFP", "CPC", 'C', CustomTags.LV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F', GTMachines.STEEL_CRATE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_chest_mv", GTMachines.SUPER_CHEST[MV].asItem(),
                "CPC", "PFP", "CPC", 'C', CustomTags.MV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'F',
                GTMachines.ALUMINIUM_CRATE.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_chest_hv", GTMachines.SUPER_CHEST[HV].asItem(),
                "CPC", "PFP", "CGC", 'C', CustomTags.HV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                GTMachines.STAINLESS_STEEL_CRATE.asItem(), 'G', GTItems.FIELD_GENERATOR_LV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_chest_ev", GTMachines.SUPER_CHEST[EV].asItem(),
                "CPC", "PFP", "CGC", 'C', CustomTags.EV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F', GTMachines.TITANIUM_CRATE.asItem(),
                'G', GTItems.FIELD_GENERATOR_MV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "quantum_chest_iv", GTMachines.QUANTUM_CHEST[IV].asItem(),
                "CPC", "PHP", "CFC", 'C', CustomTags.IV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.TungstenSteel), 'F',
                GTItems.FIELD_GENERATOR_HV.asItem(), 'H', GTMachines.HULL[5].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_chest_luv",
                GTMachines.QUANTUM_CHEST[LuV].asItem(), "CPC", "PHP", "CFC", 'C', CustomTags.LuV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.RhodiumPlatedPalladium), 'F',
                GTItems.FIELD_GENERATOR_EV.asItem(), 'H', GTMachines.HULL[6].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_chest_zpm",
                GTMachines.QUANTUM_CHEST[ZPM].asItem(), "CPC", "PHP", "CFC", 'C', CustomTags.ZPM_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.NaquadahAlloy), 'F',
                GTItems.FIELD_GENERATOR_IV.asItem(), 'H', GTMachines.HULL[7].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_chest_uv", GTMachines.QUANTUM_CHEST[UV].asItem(),
                "CPC", "PHP", "CFC", 'C', CustomTags.UV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.Darmstadtium), 'F',
                GTItems.FIELD_GENERATOR_LuV.asItem(), 'H', GTMachines.HULL[8].asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_chest_uhv",
                GTMachines.QUANTUM_CHEST[UHV].asItem(), "CPC", "PHP", "CFC", 'C', CustomTags.UHV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium), 'F',
                GTItems.FIELD_GENERATOR_ZPM.asItem(), 'H', GTMachines.HULL[9].asItem());

        // Super / Quantum Tanks
        VanillaRecipeHelper.addShapedRecipe(true, "super_tank_lv", GTMachines.SUPER_TANK[LV].asItem(), "CPC",
                "PHP", "CFC", 'C', CustomTags.LV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Steel), 'F', GTItems.ELECTRIC_PUMP_LV.asItem(), 'H',
                GTBlocks.HERMETIC_CASING_LV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_tank_mv", GTMachines.SUPER_TANK[MV].asItem(), "CPC",
                "PHP", "CFC", 'C', CustomTags.MV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'F', GTItems.ELECTRIC_PUMP_MV.asItem(),
                'H', GTBlocks.HERMETIC_CASING_MV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_tank_hv", GTMachines.SUPER_TANK[HV].asItem(), "CGC",
                "PHP", "CFC", 'C', CustomTags.HV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.StainlessSteel), 'F',
                GTItems.ELECTRIC_PUMP_HV.asItem(), 'H', GTBlocks.HERMETIC_CASING_HV.asItem(), 'G',
                GTItems.FIELD_GENERATOR_LV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "super_tank_ev", GTMachines.SUPER_TANK[EV].asItem(), "CGC",
                "PHP", "CFC", 'C', CustomTags.EV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Titanium), 'F', GTItems.ELECTRIC_PUMP_EV.asItem(),
                'H', GTBlocks.HERMETIC_CASING_EV.asItem(), 'G', GTItems.FIELD_GENERATOR_MV.asItem());

        VanillaRecipeHelper.addShapedRecipe(true, "quantum_tank_iv", GTMachines.QUANTUM_TANK[IV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.IV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.TungstenSteel), 'U',
                GTItems.ELECTRIC_PUMP_IV.asItem(), 'G', GTItems.FIELD_GENERATOR_HV.asItem(), 'H',
                GTBlocks.HERMETIC_CASING_IV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_tank_luv", GTMachines.QUANTUM_TANK[LuV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.LuV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.RhodiumPlatedPalladium), 'U',
                GTItems.ELECTRIC_PUMP_LuV.asItem(), 'G', GTItems.FIELD_GENERATOR_EV.asItem(), 'H',
                GTBlocks.HERMETIC_CASING_LuV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_tank_zpm", GTMachines.QUANTUM_TANK[ZPM].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.ZPM_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.NaquadahAlloy), 'U',
                GTItems.ELECTRIC_PUMP_ZPM.asItem(), 'G', GTItems.FIELD_GENERATOR_IV.asItem(), 'H',
                GTBlocks.HERMETIC_CASING_ZPM.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_tank_uv", GTMachines.QUANTUM_TANK[UV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plateDense, GTMaterials.Darmstadtium), 'U',
                GTItems.ELECTRIC_PUMP_UV.asItem(), 'G', GTItems.FIELD_GENERATOR_LuV.asItem(), 'H',
                GTBlocks.HERMETIC_CASING_UV.asItem());
        VanillaRecipeHelper.addShapedRecipe(true, "quantum_tank_uhv", GTMachines.QUANTUM_TANK[UHV].asItem(),
                "CGC", "PHP", "CUC", 'C', CustomTags.UHV_CIRCUITS, 'P',
                new MaterialEntry(TagPrefix.plate, GTMaterials.Neutronium), 'U', GTItems.ELECTRIC_PUMP_UV.asItem(),
                'G', GTItems.FIELD_GENERATOR_ZPM.asItem(), 'H', GTBlocks.HERMETIC_CASING_UHV.asItem());

        registerMachineRecipe(true, GTMachines.BUFFER, "HP", "CV",
                'H', HULL, 'P', PUMP, 'V', CONVEYOR, 'C', CustomTags.LV_CIRCUITS);

        VanillaRecipeHelper.addShapedRecipe(true, "cleanroom", GTMultiMachines.CLEANROOM.asItem(), "FFF",
                "RHR",
                "MCM", 'F', GTItems.ITEM_FILTER.asItem(), 'R',
                new MaterialEntry(TagPrefix.rotor, GTMaterials.StainlessSteel), 'H', HULL.get(HV), 'M',
                GTItems.ELECTRIC_MOTOR_HV.asItem(), 'C', CustomTags.HV_CIRCUITS);
    }

    // Can only accept a subset of "Item" types:
    // - ItemStack
    // - Item
    // - Block
    // - ItemEntry<?> (like GTItems)
    // - CraftingComponent.Component
    // - MaterialEntry
    // - TagKey<?>
    private static void registerMachineRecipe(boolean setMaterialInfoData,
                                              MachineDefinition[] machines, Object... recipe) {
        for (MachineDefinition machine : machines) {

            // Needed to skip certain tiers if not enabled.
            // Leaves UHV+ machine recipes to be implemented by addons.
            if (machine != null) {
                Object[] prepRecipe = prepareRecipe(machine.getTier(), Arrays.copyOf(recipe, recipe.length));
                VanillaRecipeHelper.addShapedRecipe(setMaterialInfoData, machine.getName(), machine.asItem(),
                        prepRecipe);
            }
        }
    }

    private static void registerMachineRecipe(MachineDefinition[] machines,
                                              Object... recipe) {
        registerMachineRecipe(true, machines, recipe);
    }

    private static Object[] prepareRecipe(int tier, Object @NotNull... recipe) {
        for (int i = 3; i < recipe.length; i++) {
            if (recipe[i] instanceof CraftingComponent) {
                Object component = ((CraftingComponent) recipe[i]).get(tier);
                recipe[i] = component;
            } else if (recipe[i] instanceof Item item) {
                recipe[i] = item;
            } else if (recipe[i] instanceof Block block) {
                recipe[i] = block.asItem();
            } else if (recipe[i] instanceof ItemProviderEntry<?> itemEntry) {
                recipe[i] = itemEntry.asItem();
            }
        }
        return recipe;
    }
}
