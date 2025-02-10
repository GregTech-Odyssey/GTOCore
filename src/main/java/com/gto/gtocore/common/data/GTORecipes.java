package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.IMultiblockMachineDefinition;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.data.recipe.*;
import com.gto.gtocore.data.recipe.generated.*;
import com.gto.gtocore.data.recipe.generated.ComponentRecipes;
import com.gto.gtocore.data.recipe.pack.PackRecipe;
import com.gto.gtocore.data.recipe.processing.*;
import com.gto.gtocore.integration.kjs.GTKubeJSPlugin;
import com.gto.gtocore.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.armor.PowerlessJetpack;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.MaterialInfoLoader;
import com.gregtechceu.gtceu.data.recipe.configurable.RecipeAddition;
import com.gregtechceu.gtceu.data.recipe.generated.DecompositionRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.PolarizingRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.ToolRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.misc.*;
import com.gregtechceu.gtceu.data.recipe.serialized.chemistry.ChemistryRecipes;
import com.gregtechceu.gtceu.integration.emi.recipe.GTEmiRecipe;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;

import appeng.core.AppEng;
import com.glodblock.github.extendedae.ExtendedAE;
import com.google.common.collect.ImmutableSet;
import com.kyanite.deeperdarker.DeeperDarker;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.recipe.special.EmiRepairItemRecipe;
import dev.shadowsoffire.placebo.loot.LootSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VN;

public final class GTORecipes implements Runnable {

    public static ImmutableSet<GTEmiRecipe> EMI_RECIPES;

    public static Map<String, GTRecipe> GT_RECIPE_MAP = new Object2ObjectOpenHashMap<>(5000);

    public static Set<ResourceLocation> GT_FILTER_RECIPES;

    public static Set<ResourceLocation> SHAPED_FILTER_RECIPES;

    public static void init() {
        long time = System.currentTimeMillis();
        SHAPED_FILTER_RECIPES = new ObjectOpenHashSet<>();
        GT_FILTER_RECIPES = new ObjectOpenHashSet<>(200);
        SHAPED_FILTER_RECIPES.add(GTCEu.id("large_plasma_turbine"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("maintenance_hatch_cleaning"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("vacuum_tube"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("iron_bucket"));
        SHAPED_FILTER_RECIPES.add(GTCEu.id("steam_alloy_smelter_bronze"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_lv_cadmium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_lv_lithium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_lv_sodium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_mv_cadmium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_mv_lithium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_mv_sodium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_hv_cadmium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_hv_lithium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_hv_sodium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_ev_vanadium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_iv_vanadium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_luv_vanadium_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_zpm_naquadria_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_uv_naquadria_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_ev_lapotronic_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_iv_lapotronic_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_luv_lapotronic_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_zpm_lapotronic_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_uv_lapotronic_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("packer/unpackage_uhv_ultimate_battery"));
        GT_FILTER_RECIPES.add(GTCEu.id("macerator/macerate_wheat"));
        GT_FILTER_RECIPES.add(GTCEu.id("autoclave/agar"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_ulv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_lv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_mv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_hv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_ev"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_iv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_luv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_zpm"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_uv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/casing_uhv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/hull_hv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/hull_ev"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/hull_uhv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/wool_from_string"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/diode_glass"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembler/diode_glass_annealed"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembly_line/dynamo_hatch_uhv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembly_line/energy_hatch_uhv"));
        GT_FILTER_RECIPES.add(GTCEu.id("assembly_line/energy_cluster"));
        GT_FILTER_RECIPES.add(GTCEu.id("bender/bucket"));
        GT_FILTER_RECIPES.add(GTCEu.id("research_station/1_x_gtceu_uv_energy_input_hatch"));
        GT_FILTER_RECIPES.add(GTCEu.id("research_station/1_x_gtceu_uv_energy_output_hatch"));
        GT_FILTER_RECIPES.add(GTCEu.id("mixer/rhodium_plated_palladium"));
        GT_FILTER_RECIPES.add(GTCEu.id("mixer/ender_pearl_dust"));
        GT_FILTER_RECIPES.add(GTCEu.id("mixer/rocket_fuel_from_dinitrogen_tetroxide"));
        GT_FILTER_RECIPES.add(GTCEu.id("chemical_reactor/stem_cells"));
        GT_FILTER_RECIPES.add(GTCEu.id("chemical_reactor/formaldehyde"));
        GT_FILTER_RECIPES.add(GTCEu.id("forming_press/credit_cupronickel"));
        GT_FILTER_RECIPES.add(GTCEu.id("extruder/nan_certificate"));
        GT_FILTER_RECIPES.add(GTCEu.id("centrifuge/rare_earth_separation"));
        GT_FILTER_RECIPES.add(GTCEu.id("electrolyzer/bone_meal_electrolysis"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/pyrite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_ruby_dust"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_ruby_gem"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_sapphire_gem"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/stibnite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/soda_ash_from_calcite"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/pentlandite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_green_sapphire_gem"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/rutile_from_ilmenite"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/chalcopyrite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/cobaltite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/tetrahedrite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/galena_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/sphalerite_metallurgy"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_green_sapphire_dust"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/blast_silicon_dioxide"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/aluminium_from_sapphire_dust"));
        GT_FILTER_RECIPES.add(GTCEu.id("electric_blast_furnace/titanium_from_tetrachloride"));

        Material[] fluidMap = { GTMaterials.Glue, GTMaterials.Polyethylene,
                GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole };

        for (var machine : GTMachines.DUAL_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                GT_FILTER_RECIPES.add(GTCEu.id("assembler/" + "dual_import_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }

        for (var machine : GTMachines.DUAL_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                GT_FILTER_RECIPES.add(GTCEu.id("assembler/" + "dual_export_bus_" + VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
        }
        ChemicalHelper.reinitializeUnification();
        MaterialInfoLoader.init();
        GTOMaterialInfoLoader.init();

        Consumer<FinishedRecipe> consumer = GTDynamicDataPack::addRecipe;

        DecompositionRecipeHandler.init(consumer);
        PolarizingRecipeHandler.init(consumer);
        ToolRecipeHandler.init(consumer);

        ChemistryRecipes.init(consumer);
        MetaTileEntityMachineRecipeLoader.init(consumer);
        MiscRecipeLoader.init(consumer);
        VanillaStandardRecipes.init(consumer);
        WoodMachineRecipes.init(consumer);
        StoneMachineRecipes.init(consumer);
        CraftingRecipeLoader.init(consumer);
        FusionLoader.init(consumer);
        MachineRecipeLoader.init(consumer);
        AssemblerRecipeLoader.init(consumer);
        AssemblyLineLoader.init(consumer);
        BatteryRecipes.init(consumer);
        DecorationRecipes.init(consumer);

        CircuitRecipes.init(consumer);
        MetaTileEntityLoader.init(consumer);

        GCYMRecipes.init(consumer);
        RecipeAddition.init(consumer);
        GT_FILTER_RECIPES = null;
        SHAPED_FILTER_RECIPES = null;
        // GTO
        GTOMaterialRecipeHandler.init(consumer);
        GTOOreRecipeHandler.init(consumer);
        GTOPartsRecipeHandler.init(consumer);
        GTOPipeRecipeHandler.init(consumer);
        GTORecyclingRecipeHandler.init(consumer);
        GTOWireCombiningHandler.init(consumer);
        GTOWireRecipeHandler.init(consumer);
        GTMTRecipe.init(consumer);
        FuelRecipe.init(consumer);
        NaquadahProcess.init(consumer);
        PlatGroupMetals.init(consumer);
        GCYRecipes.init(consumer);
        MachineRecipe.init(consumer);
        ComponentRecipes.init(consumer);
        MiscRecipe.init(consumer);
        ElementCopying.init(consumer);
        StoneDustProcess.init(consumer);
        Lanthanidetreatment.init(consumer);
        NewResearchSystem.init(consumer);
        RadiationHatchRecipes.init(consumer);
        RecipeOverwrite.init(consumer);
        PetrochemRecipes.init(consumer);
        GlassRecipe.init(consumer);
        DyeRecipes.init(consumer);
        PackRecipe.init(consumer);

        GenerateDisassembly.DISASSEMBLY_RECORD.clear();
        GenerateDisassembly.DISASSEMBLY_BLACKLIST.clear();
        RecyclingRecipes.init(consumer);
        ChemicalHelper.ITEM_MATERIAL_INFO.clear();
        GTOCore.LOGGER.info("Data loading took {}ms", System.currentTimeMillis() - time);
    }

    @Override
    public void run() {
        init();
        if (!GTOConfig.INSTANCE.disableMultiBlockPage) {
            IMultiblockMachineDefinition.init();
        }
        if (GTCEu.Mods.isEMILoaded()) {
            long time = System.currentTimeMillis();
            EmiRepairItemRecipe.TOOLS.clear();
            initGTCategoryMap();
            Set<GTEmiRecipe> recipes = new ObjectOpenHashSet<>(5000);
            for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
                if (!category.shouldRegisterDisplays()) continue;
                var type = category.getRecipeType();
                if (category == type.getCategory()) type.buildRepresentativeRecipes();
                EmiRecipeCategory emiCategory = GTRecipeEMICategory.CATEGORIES.apply(category);
                type.getRecipesInCategory(category).stream().map(recipe -> new GTEmiRecipe(recipe, emiCategory)).forEach(recipes::add);
            }
            EMI_RECIPES = ImmutableSet.copyOf(recipes);
            clearCategoryMap(false);
            GTOCore.LOGGER.info("Pre initialization EMI GTRecipe took {}ms", System.currentTimeMillis() - time);
        }
        GTOLoots.BLOCKS.forEach(b -> LootSystem.defaultBlockTable((Block) b));
        GTOLoots.BLOCKS = null;
    }

    public static void clearCategoryMap(boolean thoroughly) {
        if (GTOConfig.INSTANCE.recipeCheck) return;
        for (GTRecipeType type : GTRegistries.RECIPE_TYPES) {
            if (thoroughly) {
                type.getCategoryMap().clear();
            } else {
                type.getCategoryMap().values().forEach(Set::clear);
            }
        }
    }

    private static void initGTCategoryMap() {
        GT_RECIPE_MAP.values().forEach(recipe -> recipe.recipeCategory.addRecipe(recipe));
    }

    public static void initKJSCategoryMap() {
        GTKubeJSPlugin.KJS_GT_RECIPE.forEach(recipe -> recipe.recipeCategory.addRecipe(recipe));
    }

    public static void initCategoryMap() {
        initGTCategoryMap();
        initKJSCategoryMap();
    }

    public static boolean cache;

    public static void initLookup(Map<ResourceLocation, Recipe<?>> recipes) {
        cache = true;
        Thread thread = new Thread(new Lookup(recipes));
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private static class Lookup implements Runnable {

        private final Map<ResourceLocation, Recipe<?>> recipes;

        private Lookup(Map<ResourceLocation, Recipe<?>> map) {
            recipes = map;
        }

        @Override
        public void run() {
            long time = System.currentTimeMillis();
            PowerlessJetpack.FUELS.clear();
            GTRegistries.RECIPE_TYPES.forEach(t -> t.getLookup().removeAllRecipes());
            GT_RECIPE_MAP.values().forEach(r -> r.recipeType.getLookup().addRecipe(r));
            GTKubeJSPlugin.KJS_GT_RECIPE.forEach(r -> r.recipeType.getLookup().addRecipe(r));
            recipes.forEach((k, v) -> GTRecipeTypes.FURNACE_RECIPES.getLookup().addRecipe(GTRecipeTypes.FURNACE_RECIPES.toGTrecipe(k, v)));
            if (GTCEu.Mods.isEMILoaded() && !GTKubeJSPlugin.hasKJSGTRecipe) GT_RECIPE_MAP = null;
            GTOCore.LOGGER.info("InitLookup took {}ms", System.currentTimeMillis() - time);
        }
    }

    public static void removal(Set<ResourceLocation> filters) {
        String[] ore1 = new String[] { "coal", "redstone", "emerald", "diamond" };
        String[] ore2 = new String[] { "iron", "copper", "gold" };
        String[] ore3 = new String[] { "desh", "ostrum", "calorite" };

        for (String o : ore1) {
            filters.add(RLUtils.mc(o + "_from_smelting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_blasting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_from_blasting_deepslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_smelting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_blasting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_smelting_gloomslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_from_blasting_gloomslate_" + o + "_ore"));
        }
        for (String o : ore2) {
            filters.add(RLUtils.mc(o + "_ingot_from_smelting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_blasting_" + o + "_ore"));
            filters.add(RLUtils.mc(o + "_ingot_from_blasting_deepslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_smelting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_blasting_sculk_stone_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_smelting_gloomslate_" + o + "_ore"));
            filters.add(DeeperDarker.rl(o + "_ingot_from_blasting_gloomslate_" + o + "_ore"));
        }
        for (String o : ore3) {
            filters.add(RLUtils.ad("smelting/" + o + "_ingot_from_smelting_deepslate_" + o + "_ore"));
            filters.add(RLUtils.ad("blasting/" + o + "_ingot_from_blasting_deepslate_" + o + "_ore"));
        }
        filters.add(RLUtils.mc("gold_ingot_from_blasting_nether_gold_ore"));
        filters.add(RLUtils.mc("gold_ingot_from_smelting_nether_gold_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_smelting_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_smelting_deepslate_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_blasting_lapis_ore"));
        filters.add(RLUtils.mc("lapis_lazuli_from_blasting_deepslate_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_smelting_sculk_stone_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_blasting_sculk_stone_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_smelting_gloomslate_lapis_ore"));
        filters.add(DeeperDarker.rl("lapis_lazuli_from_blasting_gloomslate_lapis_ore"));
        filters.add(RLUtils.ad("smelting/desh_ingot_from_smelting_moon_desh_ore"));
        filters.add(RLUtils.ad("blasting/desh_ingot_from_blasting_moon_desh_ore"));
        filters.add(RLUtils.ad("smelting/ostrum_ingot_from_smelting_mars_ostrum_ore"));
        filters.add(RLUtils.ad("blasting/ostrum_ingot_from_blasting_mars_ostrum_ore"));
        filters.add(RLUtils.ad("smelting/calorite_ingot_from_smelting_venus_calorite_ore"));
        filters.add(RLUtils.ad("blasting/calorite_ingot_from_blasting_venus_calorite_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_moon_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_moon_iron_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_moon_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_moon_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_deepslate_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_deepslate_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_mars_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_mars_iron_ore"));
        filters.add(RLUtils.ad("smelting/diamond_from_smelting_mars_diamond_ore"));
        filters.add(RLUtils.ad("blasting/diamond_from_blasting_mars_diamond_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_mars_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_mars_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/coal_from_smelting_venus_coal_ore"));
        filters.add(RLUtils.ad("blasting/coal_from_blasting_venus_coal_ore"));
        filters.add(RLUtils.ad("smelting/gold_ingot_from_smelting_venus_gold_ore"));
        filters.add(RLUtils.ad("blasting/gold_ingot_from_blasting_venus_gold_ore"));
        filters.add(RLUtils.ad("smelting/diamond_from_smelting_venus_diamond_ore"));
        filters.add(RLUtils.ad("blasting/diamond_from_blasting_venus_diamond_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_mercury_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_mercury_iron_ore"));
        filters.add(RLUtils.ad("smelting/ice_shard_from_smelting_glacio_ice_shard_ore"));
        filters.add(RLUtils.ad("blasting/ice_shard_from_blasting_glacio_ice_shard_ore"));
        filters.add(RLUtils.ad("smelting/coal_from_smelting_glacio_coal_ore"));
        filters.add(RLUtils.ad("blasting/coal_from_blasting_glacio_coal_ore"));
        filters.add(RLUtils.ad("smelting/copper_ingot_from_smelting_glacio_copper_ore"));
        filters.add(RLUtils.ad("blasting/copper_ingot_from_blasting_glacio_copper_ore"));
        filters.add(RLUtils.ad("smelting/iron_ingot_from_smelting_glacio_iron_ore"));
        filters.add(RLUtils.ad("blasting/iron_ingot_from_blasting_glacio_iron_ore"));
        filters.add(RLUtils.ad("smelting/lapis_lazuli_from_smelting_glacio_lapis_ore"));
        filters.add(RLUtils.ad("blasting/lapis_lazuli_from_blasting_glacio_lapis_ore"));

        filters.add(new ResourceLocation("torchmaster", "frozen_pearl"));

        filters.add(RLUtils.bot("mana_infusion/mana_diamond_block"));
        filters.add(RLUtils.bot("mana_infusion/manasteel_block"));
        filters.add(RLUtils.bot("conversions/manasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/manasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/manasteel_to_nuggets"));
        filters.add(RLUtils.bot("manasteel_block"));
        filters.add(RLUtils.bot("conversions/terrasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/terrasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/terrasteel_to_nuggets"));
        filters.add(RLUtils.bot("terrasteel_block"));
        filters.add(RLUtils.bot("conversions/elementium_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/elementium_from_nuggets"));
        filters.add(RLUtils.bot("conversions/elementium_to_nuggets"));
        filters.add(RLUtils.bot("elementium_block"));

        filters.add(DeeperDarker.rl("reinforced_echo_shard"));
        filters.add(DeeperDarker.rl("resonarium_shovel_smithing"));
        filters.add(DeeperDarker.rl("resonarium_pickaxe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_axe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_hoe_smithing"));
        filters.add(DeeperDarker.rl("resonarium_sword_smithing"));
        filters.add(DeeperDarker.rl("resonarium_helmet_smithing"));
        filters.add(DeeperDarker.rl("resonarium_chestplate_smithing"));
        filters.add(DeeperDarker.rl("resonarium_leggings_smithing"));
        filters.add(DeeperDarker.rl("resonarium_boots_smithing"));

        filters.add(RLUtils.mc("wooden_shovel"));
        filters.add(RLUtils.mc("wooden_pickaxe"));
        filters.add(RLUtils.mc("wooden_axe"));
        filters.add(RLUtils.mc("wooden_hoe"));
        filters.add(RLUtils.mc("wooden_sword"));
        filters.add(RLUtils.mc("stone_shovel"));
        filters.add(RLUtils.mc("stone_pickaxe"));
        filters.add(RLUtils.mc("stone_axe"));
        filters.add(RLUtils.mc("stone_hoe"));
        filters.add(RLUtils.mc("stone_sword"));
        filters.add(RLUtils.mc("quartz"));
        filters.add(RLUtils.mc("quartz_from_blasting"));
        filters.add(RLUtils.mc("hay_block"));
        filters.add(RLUtils.mc("netherite_ingot"));
        filters.add(RLUtils.mc("netherite_scrap"));
        filters.add(RLUtils.mc("netherite_scrap_from_blasting"));
        filters.add(RLUtils.mc("infinity_nugget"));
        filters.add(RLUtils.mc("infinity_ingot"));
        filters.add(RLUtils.mc("infinity_ingot_from_infinity_nugget"));
        filters.add(RLUtils.mc("infinity_block_from_infinity_ingot"));
        filters.add(RLUtils.mc("crystal_matrix_ingot"));
        filters.add(RLUtils.mc("double_compressed_crafting_table"));
        filters.add(RLUtils.mc("compressed_crafting_table"));
        filters.add(RLUtils.mc("crystal_matrix"));
        filters.add(RLUtils.mc("neutron_pile"));
        filters.add(RLUtils.mc("neutron_pile_from_ingots"));
        filters.add(RLUtils.mc("neutron_ingot_from_nuggets"));
        filters.add(RLUtils.mc("neutron_ingot_from_neutron_block"));
        filters.add(RLUtils.mc("neutron_nugget"));
        filters.add(RLUtils.mc("neutron"));

        filters.add(RLUtils.avaritia("infinity_catalyst"));
        filters.add(RLUtils.avaritia("crystal_matrix_ingot"));
        filters.add(RLUtils.avaritia("diamond_lattice"));

        filters.add(ExtendedAE.id("fishbig"));

        filters.add(RLUtils.ad("refining/fuel_from_refining_oil"));
        filters.add(RLUtils.ad("oxygen_loading/oxygen_from_oxygen_loading_oxygen"));
        filters.add(RLUtils.ad("oxygen_loading/oxygen_from_oxygen_loading_water"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_blue_ice"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_ice_shard"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_ice"));
        filters.add(RLUtils.ad("cryo_freezing/cryo_fuel_from_cryo_freezing_packed_ice"));
        filters.add(RLUtils.ad("compressing/calorite_plate_from_compressing_calorite_blocks"));
        filters.add(RLUtils.ad("compressing/calorite_plate_from_compressing_calorite_ingots"));
        filters.add(RLUtils.ad("compressing/desh_plate_from_compressing_desh_blocks"));
        filters.add(RLUtils.ad("compressing/desh_plate_from_compressing_desh_ingots"));
        filters.add(RLUtils.ad("compressing/iron_plate_from_compressing_iron_block"));
        filters.add(RLUtils.ad("compressing/iron_plate_from_compressing_iron_ingot"));
        filters.add(RLUtils.ad("compressing/ostrum_plate_from_compressing_ostrum_blocks"));
        filters.add(RLUtils.ad("compressing/ostrum_plate_from_compressing_ostrum_ingots"));
        filters.add(RLUtils.ad("compressing/steel_plate_from_compressing_steel_blocks"));
        filters.add(RLUtils.ad("compressing/steel_plate_from_compressing_steel_ingots"));
        filters.add(RLUtils.ad("alloying/steel_ingot_from_alloying_iron_ingot_and_coals"));
        filters.add(RLUtils.ad("nasa_workbench/tier_1_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_2_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_3_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("nasa_workbench/tier_4_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_5_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_6_rocket_from_nasa_workbench"));
        filters.add(new ResourceLocation("ad_astra_rocketed", "nasa_workbench/default/tier_7_rocket_from_nasa_workbench"));
        filters.add(RLUtils.ad("compressor"));
        filters.add(RLUtils.ad("steel_block"));
        filters.add(RLUtils.ad("steel_ingot_from_steel_block"));
        filters.add(RLUtils.ad("steel_nugget"));
        filters.add(RLUtils.ad("energizer"));
        filters.add(RLUtils.ad("steel_cable"));
        filters.add(RLUtils.ad("steel_rod"));
        filters.add(RLUtils.ad("iron_rod"));
        filters.add(RLUtils.ad("desh_block"));
        filters.add(RLUtils.ad("desh_ingot"));
        filters.add(RLUtils.ad("desh_nugget"));
        filters.add(RLUtils.ad("desh_ingot_from_desh_block"));
        filters.add(RLUtils.ad("desh_cable"));
        filters.add(RLUtils.ad("ostrum_block"));
        filters.add(RLUtils.ad("ostrum_ingot"));
        filters.add(RLUtils.ad("ostrum_nugget"));
        filters.add(RLUtils.ad("ostrum_ingot_from_ostrum_block"));
        filters.add(RLUtils.ad("calorite_block"));
        filters.add(RLUtils.ad("calorite_ingot"));
        filters.add(RLUtils.ad("calorite_nugget"));
        filters.add(RLUtils.ad("calorite_ingot_from_calorite_block"));
        filters.add(RLUtils.ad("cable_duct"));
        filters.add(RLUtils.ad("steel_ingot"));
        filters.add(RLUtils.ad("nasa_workbench"));
        filters.add(RLUtils.ad("compressor"));
        filters.add(RLUtils.ad("coal_generator"));
        filters.add(RLUtils.ad("etrionic_blast_furnace"));
        filters.add(RLUtils.ad("fuel_refinery"));
        filters.add(RLUtils.ad("solar_panel"));
        filters.add(RLUtils.ad("water_pump"));
        filters.add(RLUtils.ad("cryo_freezer"));
        filters.add(RLUtils.ad("fan"));
        filters.add(RLUtils.ad("engine_frame"));
        filters.add(RLUtils.ad("steel_engine"));
        filters.add(RLUtils.ad("desh_engine"));
        filters.add(RLUtils.ad("ostrum_engine"));
        filters.add(RLUtils.ad("calorite_engine"));
        filters.add(RLUtils.ad("calorite_tank"));
        filters.add(RLUtils.ad("ostrum_tank"));
        filters.add(RLUtils.ad("desh_tank"));
        filters.add(RLUtils.ad("steel_tank"));
        filters.add(RLUtils.ad("rocket_fin"));
        filters.add(RLUtils.ad("rocket_nose_cone"));

        filters.add(RLUtils.eio("darksteel_upgrade"));
        filters.add(RLUtils.eio("iron_ingot_from_blasting"));
        filters.add(RLUtils.eio("iron_ingot_from_smelting"));
        filters.add(RLUtils.eio("gold_ingot_from_blasting"));
        filters.add(RLUtils.eio("gold_ingot_from_smelting"));
        filters.add(RLUtils.eio("copper_ingot_from_blasting"));
        filters.add(RLUtils.eio("copper_ingot_from_smelting"));
        filters.add(RLUtils.eio("copper_alloy_block"));
        filters.add(RLUtils.eio("copper_alloy_ingot"));
        filters.add(RLUtils.eio("copper_alloy_nugget"));
        filters.add(RLUtils.eio("copper_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("energetic_alloy_block"));
        filters.add(RLUtils.eio("energetic_alloy_ingot"));
        filters.add(RLUtils.eio("energetic_alloy_nugget"));
        filters.add(RLUtils.eio("energetic_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("vibrant_alloy_block"));
        filters.add(RLUtils.eio("vibrant_alloy_ingot"));
        filters.add(RLUtils.eio("vibrant_alloy_nugget"));
        filters.add(RLUtils.eio("vibrant_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("redstone_alloy_block"));
        filters.add(RLUtils.eio("redstone_alloy_ingot"));
        filters.add(RLUtils.eio("redstone_alloy_nugget"));
        filters.add(RLUtils.eio("redstone_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("conductive_alloy_block"));
        filters.add(RLUtils.eio("conductive_alloy_ingot"));
        filters.add(RLUtils.eio("conductive_alloy_nugget"));
        filters.add(RLUtils.eio("conductive_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("pulsating_alloy_block"));
        filters.add(RLUtils.eio("pulsating_alloy_ingot"));
        filters.add(RLUtils.eio("pulsating_alloy_nugget"));
        filters.add(RLUtils.eio("pulsating_alloy_nugget_to_ingot"));
        filters.add(RLUtils.eio("dark_steel_block"));
        filters.add(RLUtils.eio("dark_steel_ingot"));
        filters.add(RLUtils.eio("dark_steel_nugget"));
        filters.add(RLUtils.eio("dark_steel_nugget_to_ingot"));
        filters.add(RLUtils.eio("soularium_block"));
        filters.add(RLUtils.eio("soularium_ingot"));
        filters.add(RLUtils.eio("soularium_nugget"));
        filters.add(RLUtils.eio("soularium_nugget_to_ingot"));
        filters.add(RLUtils.eio("end_steel_block"));
        filters.add(RLUtils.eio("end_steel_ingot"));
        filters.add(RLUtils.eio("end_steel_nugget"));
        filters.add(RLUtils.eio("end_steel_nugget_to_ingot"));
        filters.add(RLUtils.eio("wood_gear_corner"));
        filters.add(RLUtils.eio("wood_gear"));
        filters.add(RLUtils.eio("iron_gear"));
        filters.add(RLUtils.eio("energized_gear"));
        filters.add(RLUtils.eio("vibrant_gear"));
        filters.add(RLUtils.eio("dark_bimetal_gear"));
        filters.add(RLUtils.eio("pulsating_crystal"));
        filters.add(RLUtils.eio("vibrant_crystal"));
        filters.add(RLUtils.eio("stick"));
        filters.add(RLUtils.eio("sag_milling/ender_crystal"));
        filters.add(RLUtils.eio("sag_milling/precient_crystal"));
        filters.add(RLUtils.eio("sag_milling/pulsating_crystal"));
        filters.add(RLUtils.eio("sag_milling/vibrant_crystal"));
        filters.add(RLUtils.eio("sag_milling/soularium"));
        filters.add(RLUtils.eio("alloy_smelting/energetic_alloy_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/vibrant_alloy_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/dark_steel_ingot"));
        filters.add(RLUtils.eio("alloy_smelting/end_steel_ingot"));

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

        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_1k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_4k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_16k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_64k_part"));
        filters.add(AppEng.makeId("network/cells/item_storage_components_cell_256k_part"));
        filters.add(AppEng.makeId("decorative/quartz_block"));
        filters.add(AppEng.makeId("decorative/fluix_block"));
        filters.add(AppEng.makeId("misc/deconstruction_certus_quartz_block"));
        filters.add(AppEng.makeId("misc/deconstruction_fluix_block"));
        filters.add(AppEng.makeId("misc/fluixpearl"));
        filters.add(AppEng.makeId("network/cables/glass_fluix"));
        filters.add(AppEng.makeId("network/blocks/controller"));
        filters.add(AppEng.makeId("network/crafting/patterns_blank"));
        filters.add(AppEng.makeId("network/parts/export_bus"));
        filters.add(AppEng.makeId("network/parts/import_bus"));
        filters.add(AppEng.makeId("network/wireless_part"));
        filters.add(AppEng.makeId("network/crafting/cpu_crafting_unit"));
        filters.add(AppEng.makeId("materials/annihilationcore"));
        filters.add(AppEng.makeId("materials/formationcore"));
        filters.add(AppEng.makeId("materials/advancedcard"));
        filters.add(AppEng.makeId("materials/basiccard"));
    }
}
