package com.gtocore.data.tag;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.*;

import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTTags;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.kyanite.deeperdarker.content.DDBlocks;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.common.registry.ModBlocks;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Comparator;

public final class TagsHandler {

    public static void initBlock() {
        create(Tags.ALL_LAYER_STONE, Blocks.END_STONE, ModBlocks.MOON_STONE.get(), ModBlocks.MARS_STONE.get(), ModBlocks.VENUS_STONE.get(),
                ModBlocks.MERCURY_STONE.get(), ModBlocks.GLACIO_STONE.get(), GTOBlocks.TITAN_STONE.get(), GTOBlocks.PLUTO_STONE.get(),
                GTOBlocks.IO_STONE.get(), GTOBlocks.GANYMEDE_STONE.get(), GTOBlocks.ENCELADUS_STONE.get(), GTOBlocks.CERES_STONE.get(),
                DDBlocks.SCULK_STONE.get(), DDBlocks.GLOOMSLATE.get(), BotaniaBlocks.livingrock);
        create(Tags.ARCHWOOD_LOG,
                BlockRegistry.BLAZING_LOG.get(), BlockRegistry.CASCADING_LOG.get(), BlockRegistry.VEXING_LOG.get(), BlockRegistry.FLOURISHING_LOG.get(),
                BlockRegistry.BLAZING_WOOD.get(), BlockRegistry.CASCADING_WOOD.get(), BlockRegistry.FLOURISHING_WOOD.get(), BlockRegistry.VEXING_WOOD.get(),
                BlockRegistry.STRIPPED_AWLOG_BLUE.get(), BlockRegistry.STRIPPED_AWWOOD_BLUE.get(), BlockRegistry.STRIPPED_AWLOG_GREEN.get(), BlockRegistry.STRIPPED_AWWOOD_GREEN.get(),
                BlockRegistry.STRIPPED_AWLOG_RED.get(), BlockRegistry.STRIPPED_AWWOOD_RED.get(), BlockRegistry.STRIPPED_AWLOG_PURPLE.get(), BlockRegistry.STRIPPED_AWWOOD_PURPLE.get());
        create(CustomTags.MINEABLE_WITH_WRENCH,
                BlockRegistry.SCRIBES_BLOCK.get(),
                BlockRegistry.RELAY.get(),
                BlockRegistry.ARCANE_CORE_BLOCK.get(),
                BlockRegistry.ENCHANTING_APP_BLOCK.get(),
                BlockRegistry.ARCANE_PEDESTAL.get(),
                BlockRegistry.ARCANE_PLATFORM.get(),
                BlockRegistry.MAGELIGHT_TORCH.get(),
                BlockRegistry.CREATIVE_SOURCE_JAR.get(),
                BlockRegistry.RUNE_BLOCK.get(),
                BlockRegistry.AGRONOMIC_SOURCELINK.get(),
                BlockRegistry.IMBUEMENT_BLOCK.get(),
                BlockRegistry.SOURCE_JAR.get(),
                BlockRegistry.RELAY_SPLITTER.get(),
                BlockRegistry.ENCHANTED_SPELL_TURRET.get(),
                BlockRegistry.VOLCANIC_BLOCK.get(),
                BlockRegistry.WIXIE_CAULDRON.get(),
                BlockRegistry.SOURCE_GEM_BLOCK.get(),
                BlockRegistry.RITUAL_BLOCK.get(),
                BlockRegistry.POTION_JAR.get(),
                BlockRegistry.POTION_MELDER.get(),
                BlockRegistry.GOLD_SCONCE_BLOCK.get(),
                BlockRegistry.SOURCESTONE_SCONCE_BLOCK.get(),
                BlockRegistry.POLISHED_SCONCE_BLOCK.get(),
                BlockRegistry.ARCHWOOD_SCONCE_BLOCK.get(),
                BlockRegistry.DRYGMY_BLOCK.get(),
                BlockRegistry.ALCHEMICAL_BLOCK.get(),
                BlockRegistry.VITALIC_BLOCK.get(),
                BlockRegistry.MYCELIAL_BLOCK.get(),
                BlockRegistry.RELAY_DEPOSIT.get(),
                BlockRegistry.RELAY_WARP.get(),
                BlockRegistry.BASIC_SPELL_TURRET.get(),
                BlockRegistry.TIMER_SPELL_TURRET.get(),
                BlockRegistry.SPELL_PRISM.get(),
                BlockRegistry.SCRYERS_CRYSTAL.get(),
                BlockRegistry.SCRYERS_OCULUS.get(),
                BlockRegistry.POTION_DIFFUSER.get(),
                BlockRegistry.MOB_JAR.get(),
                BlockRegistry.VOID_PRISM.get(),
                BlockRegistry.BRAZIER_RELAY.get(),
                BlockRegistry.REDSTONE_RELAY.get(),
                BlockRegistry.CRAFTING_LECTERN.get(),
                BlockRegistry.ARCHWOOD_CHEST.get(),
                BlockRegistry.ALTERATION_TABLE.get(),
                BlockRegistry.ITEM_DETECTOR.get(),
                BlockRegistry.REPOSITORY.get());

        create(CustomTags.MINEABLE_WITH_WRENCH, GTOGlobes.GLOBE_ENTRIES.stream()
                .sorted(Comparator.comparing(s -> s.get().getDescriptionId()))
                .map(RegistryEntry::get).toArray(Block[]::new));
        create(BlockTags.MINEABLE_WITH_PICKAXE, GTOGlobes.GLOBE_ENTRIES.stream()
                .sorted(Comparator.comparing(s -> s.get().getDescriptionId()))
                .map(RegistryEntry::get).toArray(Block[]::new));
        create(CustomTags.MINEABLE_WITH_WRENCH, GTOGlobes.Blocks.GTO_FLAG.get());
        create(BlockTags.MINEABLE_WITH_AXE, GTOGlobes.Blocks.GTO_FLAG.get());

        create(BotaniaTags.Blocks.UNETHICAL_TNT_CHECK,
                GTBlocks.INDUSTRIAL_TNT.get(),
                GTBlocks.POWDERBARREL.get());
        create(CustomTags.CLEANROOM_FLOORS,
                GTOMachines.TRAVEL_ANCHOR.get());
    }

    public static void initItem() {
        create(Tags.HUMAN_EGG, Items.VILLAGER_SPAWN_EGG, Items.WITCH_SPAWN_EGG);
        create(ResourceLocation.fromNamespaceAndPath("curios", "curio"),
                GTOItems.WIRELESS_ME2IN1.asItem());
        create(ResourceLocation.parse("forge:ingots/redstone_alloy"),
                ChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.RedAlloy));
        Item[] isaProcessableItems = GTCEuAPI.materialManager.getRegistries().stream()
                .flatMap(registry -> registry.getAllMaterials().stream())
                .filter(material -> material.hasFlag(GTOMaterialFlags.GENERATE_MILLED))
                .flatMap(material -> TagPrefix.ORES.keySet().stream().map(tagPrefix -> ChemicalHelper.getItem(tagPrefix, material)).filter(item -> item != Items.AIR))
                .sorted(Comparator.comparing(Item::toString))
                .toArray(Item[]::new);
        create(TagUtils.createTGItemTag("isa_processable"), isaProcessableItems);
    }

    public static void initFluid() {
        create(Tags.PURIFY_WATER, GTOMaterials.FilteredSater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.OzoneWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.FlocculentWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.PHNeutralWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.ExtremeTemperatureWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.ElectricEquilibriumWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.DegassedWater.getFluid());
        create(Tags.PURIFY_WATER, GTOMaterials.BaryonicPerfectionWater.getFluid());
    }

    private static void create(ResourceLocation location, Block... rls) {
        for (var block : rls) {
            GTTags.addBlockEntry(block, location);
        }
    }

    private static void create(ResourceLocation location, Item... rls) {
        for (var item : rls) {
            GTTags.addItemEntry(item, location);
        }
    }

    private static void create(ResourceLocation location, Fluid... rls) {
        for (var fluid : rls) {
            GTTags.addFluidEntry(fluid, location);
        }
    }

    private static void create(TagKey<Block> tagKey, Block... rls) {
        for (var block : rls) {
            GTTags.addBlockEntry(block, tagKey.location());
        }
    }

    private static void create(TagKey<Item> tagKey, Item... rls) {
        for (var item : rls) {
            GTTags.addItemEntry(item, tagKey.location());
        }
    }

    private static void create(TagKey<Fluid> tagKey, Fluid... rls) {
        for (var fluid : rls) {
            GTTags.addFluidEntry(fluid, tagKey.location());
        }
    }
}
