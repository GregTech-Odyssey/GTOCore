package com.gtocore.data.recipe;

import com.gtocore.data.recipe.ae2.AE2;
import com.gtocore.data.recipe.generated.DyeRecipes;
import com.gtocore.data.recipe.mod.FunctionalStorage;
import com.gtocore.data.recipe.mod.ImmersiveAircraft;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.data.recipe.configurable.RecipeRemoval;
import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.resources.ResourceLocation;

import com.kyanite.deeperdarker.DeeperDarker;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.gtocore.common.data.GTORecipeTypes.*;

public final class RecipeFilter {

    public static Predicate<ResourceLocation> JsonFilter;

    public static Predicate<ResourceLocation> getFilter() {
        return JsonFilter;
    }

    public static void init() {
        MACERATOR_RECIPES.addFilter("macerate_wheat");
        MACERATOR_RECIPES.addFilter("macerate_red_granite");
        MACERATOR_RECIPES.addFilter("macerate_andesite");
        MACERATOR_RECIPES.addFilter("macerate_diorite");
        MACERATOR_RECIPES.addFilter("macerate_end_stone");
        MACERATOR_RECIPES.addFilter("macerate_granite");
        MACERATOR_RECIPES.addFilter("macerate_basalt");
        CUTTER_RECIPES.addFilter("cut_glass_block_to_plate");
        ARC_FURNACE_RECIPES.addFilter("arc_carbon_dust");
        ASSEMBLER_RECIPES.addFilter("assemble_wood_frame"); // 与告示牌重复

        List<Predicate<ResourceLocation>> filters = new ObjectArrayList<>();
        addFilter(filters);
        Predicate<ResourceLocation> filter = filters.get(0);
        for (int i = 1; i < filters.size(); i++) {
            filter = filter.or(filters.get(i));
        }
        JsonFilter = filter;
    }

    private static void addFilter(List<Predicate<ResourceLocation>> filters) {
        ObjectOpenHashSet<ResourceLocation> ids = new OpenCacheHashSet<>(2048);
        initIdFilter(ids);
        RecipeRemoval.init(ids::add);
        filters.add(ids::contains);
        ObjectOpenHashSet<String> mods = new OpenCacheHashSet<>();
        initModFilter(mods);
        filters.add(rl -> mods.contains(rl.getNamespace()));
    }

    private static void initModFilter(Set<String> filters) {
        filters.add("itemfilters");
    }

    private static void initIdFilter(Set<ResourceLocation> filters) {
        ImmersiveAircraft.initJsonFilter(filters);
        FunctionalStorage.initJsonFilter(filters);
        AE2.initJsonFilter(filters);

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
            filters.add(RLUtils.ad("smelting/" + o + "_ingot_from_smelting_raw_" + o));
            filters.add(RLUtils.ad("blasting/" + o + "_ingot_from_blasting_raw_" + o));
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

        filters.add(RLUtils.fromNamespaceAndPath("torchmaster", "frozen_pearl"));

        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "alfsteel_block_decompress"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "alfsteel_nugget_compress"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "alfsteel_ingot_compress"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "alfsteel_ingot_decompress"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "smelting/elementium_ingot"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "blasting/elementium_ingot"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "smelting/dragonstone"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "blasting/dragonstone"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "smelting/elementium_ingot"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "blasting/elementium_ingot"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "alfsteel_pylon"));
        filters.add(RLUtils.fromNamespaceAndPath("mythicbotany", "gaia_pylon"));
        filters.add(RLUtils.bot("pure_daisy/livingwood"));
        filters.add(RLUtils.bot("mana_infusion/manasteel"));
        filters.add(RLUtils.bot("mana_infusion/manasteel_block"));
        filters.add(RLUtils.bot("mana_infusion/mana_pearl"));
        filters.add(RLUtils.bot("mana_infusion/mana_string"));
        filters.add(RLUtils.bot("conversions/manasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/manasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/manasteel_to_nuggets"));
        filters.add(RLUtils.bot("conversions/terrasteel_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/terrasteel_from_nuggets"));
        filters.add(RLUtils.bot("conversions/terrasteel_to_nuggets"));
        filters.add(RLUtils.bot("conversions/elementium_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/elementium_from_nuggets"));
        filters.add(RLUtils.bot("conversions/elementium_to_nuggets"));
        filters.add(RLUtils.bot("conversions/manadiamond_block_deconstruct"));
        filters.add(RLUtils.bot("conversions/dragonstone_block_deconstruct"));
        filters.add(RLUtils.bot("orechid/redstone_ore"));
        filters.add(RLUtils.bot("orechid/emerald_ore"));
        filters.add(RLUtils.bot("orechid/diamond_ore"));
        filters.add(RLUtils.bot("orechid/copper_ore"));
        filters.add(RLUtils.bot("orechid/coal_ore"));
        filters.add(RLUtils.bot("orechid/lapis_ore"));
        filters.add(RLUtils.bot("orechid/gold_ore"));
        filters.add(RLUtils.bot("orechid/iron_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_redstone_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_emerald_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_diamond_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_copper_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_coal_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_lapis_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_gold_ore"));
        filters.add(RLUtils.bot("orechid/deepslate_iron_ore"));
        filters.add(RLUtils.bot("orechid_ignem/nether_gold_ore"));
        filters.add(RLUtils.bot("orechid_ignem/ancient_debris"));
        filters.add(RLUtils.bot("orechid_ignem/nether_quartz_ore"));
        filters.add(RLUtils.bot("elven_trade/dragonstone"));
        filters.add(RLUtils.bot("elven_trade/dragonstone_block"));
        filters.add(RLUtils.bot("elven_trade/diamond_block_return"));
        filters.add(RLUtils.bot("elven_trade/diamond_return"));
        filters.add(RLUtils.bot("elven_trade/iron_block_return"));
        filters.add(RLUtils.bot("elven_trade/iron_return"));
        filters.add(RLUtils.bot("elven_trade/ender_pearl_return"));
        filters.add(RLUtils.bot("runic_altar"));
        filters.add(RLUtils.bot("runic_altar_alt"));
        filters.add(RLUtils.bot("terra_plate"));
        filters.add(RLUtils.bot("manasteel_block"));
        filters.add(RLUtils.bot("terrasteel_block"));
        filters.add(RLUtils.bot("elementium_block"));
        filters.add(RLUtils.bot("mana_diamond_block"));
        filters.add(RLUtils.bot("dragonstone_block"));
        filters.add(RLUtils.bot("dye_white"));
        filters.add(RLUtils.bot("dye_light_gray"));
        filters.add(RLUtils.bot("dye_gray"));
        filters.add(RLUtils.bot("dye_black"));
        filters.add(RLUtils.bot("dye_brown"));
        filters.add(RLUtils.bot("dye_red"));
        filters.add(RLUtils.bot("dye_orange"));
        filters.add(RLUtils.bot("dye_yellow"));
        filters.add(RLUtils.bot("dye_lime"));
        filters.add(RLUtils.bot("dye_green"));
        filters.add(RLUtils.bot("dye_cyan"));
        filters.add(RLUtils.bot("dye_light_blue"));
        filters.add(RLUtils.bot("dye_blue"));
        filters.add(RLUtils.bot("dye_purple"));
        filters.add(RLUtils.bot("dye_magenta"));
        filters.add(RLUtils.bot("dye_pink"));
        filters.add(RLUtils.bot("alfheim_portal"));
        filters.add(RLUtils.bot("mana_pylon"));
        filters.add(RLUtils.bot("natura_pylon"));
        filters.add(RLUtils.bot("gaia_pylon"));
        filters.add(RLUtils.bot("gaia_ingot"));
        filters.add(RLUtils.exbot("conversions/orichalcos_block_deconstruct"));
        filters.add(RLUtils.exbot("orichalcos_block"));
        filters.add(RLUtils.exbot("conversions/orichalcos_to_nuggets"));
        filters.add(RLUtils.exbot("conversions/orichalcos_from_nuggets"));
        filters.add(RLUtils.exbot("conversions/photonium_block_deconstruct"));
        filters.add(RLUtils.exbot("photonium_block"));
        filters.add(RLUtils.exbot("conversions/photonium_to_nuggets"));
        filters.add(RLUtils.exbot("conversions/photonium_from_nuggets"));
        filters.add(RLUtils.exbot("conversions/shadowium_block_deconstruct"));
        filters.add(RLUtils.exbot("shadowium_block"));
        filters.add(RLUtils.exbot("conversions/shadowium_to_nuggets"));
        filters.add(RLUtils.exbot("conversions/shadowium_from_nuggets"));
        filters.add(RLUtils.exbot("conversions/aerialite_block_deconstruct"));
        filters.add(RLUtils.exbot("aerialite_block"));
        filters.add(RLUtils.exbot("conversions/aerialite_to_nuggets"));
        filters.add(RLUtils.exbot("conversions/aerialite_from_nuggets"));

        filters.add(RLUtils.ars("imbuement_amethyst"));
        filters.add(RLUtils.ars("imbuement_lapis"));
        filters.add(RLUtils.ars("imbuement_amethyst_block"));
        filters.add(RLUtils.ars("imbuement_earth_essence"));
        filters.add(RLUtils.ars("imbuement_air_essence"));
        filters.add(RLUtils.ars("imbuement_water_essence"));
        filters.add(RLUtils.ars("imbuement_fire_essence"));
        filters.add(RLUtils.ars("imbuement_manipulation_essence"));
        filters.add(RLUtils.ars("imbuement_abjuration_essence"));
        filters.add(RLUtils.ars("imbuement_conjuration_essence"));

        filters.add(RLUtils.ars("source_gem_block"));
        filters.add(RLUtils.ars("imbuement_chamber"));
        filters.add(RLUtils.ars("enchanting_apparatus"));
        filters.add(RLUtils.ars("arcane_core"));
        filters.add(RLUtils.ars("blank_thread"));
        filters.add(RLUtils.ars("apprentice_spell_book_upgrade"));
        filters.add(RLUtils.ars("archmage_spell_book_upgrade"));
        filters.add(RLUtils.ars("worn_notebook"));
        filters.add(RLUtils.ars("dowsing_rod"));
        filters.add(RLUtils.ars("wixie_hat"));
        filters.add(RLUtils.ars("agronomic_sourcelink"));
        filters.add(RLUtils.ars("source_jar"));
        filters.add(RLUtils.ars("relay"));
        filters.add(RLUtils.ars("scribes_table"));
        filters.add(RLUtils.ars("volcanic_sourcelink"));
        filters.add(RLUtils.ars("volcanic_sourcelink"));
        filters.add(RLUtils.ars("vitalic_sourcelink"));
        filters.add(RLUtils.ars("mycelial_sourcelink"));
        filters.add(RLUtils.ars("basic_spell_turret"));
        filters.add(RLUtils.ars("archwood_chest"));
        filters.add(RLUtils.ars("spell_prism"));
        filters.add(RLUtils.ars("mob_jar"));
        filters.add(RLUtils.ars("repository"));
        filters.add(RLUtils.ars("magelight_torch"));
        filters.add(RLUtils.ars("arcane_pedestal"));
        filters.add(RLUtils.ars("ritual_brazier"));
        filters.add(RLUtils.ars("redstone_relay"));
        filters.add(RLUtils.ars("alchemical_sourcelink"));

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
        filters.add(RLUtils.mc("diamond_lattice_block"));
        filters.add(RLUtils.mc("diamond_lattice"));

        filters.add(RLUtils.avaritia("enhancement_core"));
        filters.add(RLUtils.avaritia("infinity_catalyst"));
        filters.add(RLUtils.avaritia("crystal_matrix_ingot"));
        filters.add(RLUtils.avaritia("diamond_lattice"));
        filters.add(RLUtils.avaritia("botania_mana_tablet"));
        filters.add(RLUtils.avaritia("eio_creative_power"));
        filters.add(RLUtils.avaritia("botania_creative_pool"));
        filters.add(RLUtils.avaritia("compressed_chest"));
        filters.add(RLUtils.avaritia("infinity_catalyst_eternal"));

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
        filters.add(RLUtils.fromNamespaceAndPath("ad_astra_rocketed", "nasa_workbench/default/tier_5_rocket_from_nasa_workbench"));
        filters.add(RLUtils.fromNamespaceAndPath("ad_astra_rocketed", "nasa_workbench/default/tier_6_rocket_from_nasa_workbench"));
        filters.add(RLUtils.fromNamespaceAndPath("ad_astra_rocketed", "nasa_workbench/default/tier_7_rocket_from_nasa_workbench"));
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
        filters.add(RLUtils.ad("gas_tank"));

        filters.add(RLUtils.eio("darksteel_upgrade"));
        filters.add(RLUtils.eio("iron_ingot_from_blasting"));
        filters.add(RLUtils.eio("iron_ingot_from_smelting"));
        filters.add(RLUtils.eio("gold_ingot_from_blasting"));
        filters.add(RLUtils.eio("gold_ingot_from_smelting"));
        filters.add(RLUtils.eio("copper_ingot_from_blasting"));
        filters.add(RLUtils.eio("copper_ingot_from_smelting"));
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
        filters.add(RLUtils.eio("sag_milling/ender_pearl"));

        if (!GTOCore.isSimple()) {

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

        filters.add(RLUtils.fd("wheat_dough_from_water"));
        filters.add(RLUtils.fd("wheat_dough_from_eggs"));
        filters.add(RLUtils.fd("bread_from_smelting"));
        filters.add(RLUtils.fd("bread_from_smoking"));
        filters.add(RLUtils.fd("carrot_crate"));
        filters.add(RLUtils.fd("potato_crate"));
        filters.add(RLUtils.fd("beetroot_crate"));
        filters.add(RLUtils.fd("cabbage_crate"));
        filters.add(RLUtils.fd("tomato_crate"));
        filters.add(RLUtils.fd("onion_crate"));
        filters.add(RLUtils.fd("rice_bale"));
        filters.add(RLUtils.fd("rice_bag"));
        filters.add(RLUtils.fd("straw_bale"));
        filters.add(RLUtils.fd("carrot_from_crate"));
        filters.add(RLUtils.fd("potato_from_crate"));
        filters.add(RLUtils.fd("beetroot_from_crate"));
        filters.add(RLUtils.fd("cabbage"));
        filters.add(RLUtils.fd("tomato"));
        filters.add(RLUtils.fd("onion"));
        filters.add(RLUtils.fd("rice_panicle"));
        filters.add(RLUtils.fd("rice_from_bag"));
        filters.add(RLUtils.fd("straw"));
        filters.add(RLUtils.fd("paper_from_tree_bark"));
        filters.add(RLUtils.fd("cooking_pot"));
        filters.add(RLUtils.mc("red_dye"));
        filters.add(RLUtils.mc("book_from_canvas"));
        filters.add(RLUtils.mc("painting_from_canvas"));
        filters.add(RLUtils.mc("scaffolding_from_canvas"));

        filters.add(RLUtils.fromNamespaceAndPath("farmersrespite", "green_tea_leaves_sack"));
        filters.add(RLUtils.fromNamespaceAndPath("farmersrespite", "yellow_tea_leaves_sack"));
        filters.add(RLUtils.fromNamespaceAndPath("farmersrespite", "black_tea_leaves_sack"));
        filters.add(RLUtils.fromNamespaceAndPath("farmersrespite", "coffee_beans_sack"));

        if (Mods.biomeswevegone()) {
            DyeRecipes.BWG.forEach((k, v) -> {
                filters.add(RLUtils.fromNamespaceAndPath("minecraft", k + "_dye_from_bwg_dye_tag"));
                if (v) filters.add(RLUtils.fromNamespaceAndPath("minecraft", k + "_dye_from_bwg_2_dye_tag"));
            });
            DyeRecipes.BWG.clear();
        }
    }
}
