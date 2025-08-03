package com.gtocore.data.tag;

import com.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.kyanite.deeperdarker.content.DDBlocks;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import earth.terrarium.adastra.common.registry.ModBlocks;

public final class TagsHandler {

    public static void initBlock(RegistrateTagsProvider<Block> provider) {
        provider.addTag(Tags.ALL_LAYER_STONE).addTag(BlockTags.STONE_ORE_REPLACEABLES).addTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES).addTag(BlockTags.NETHER_CARVER_REPLACEABLES);
        create(provider, Tags.ALL_LAYER_STONE, Blocks.END_STONE, ModBlocks.MOON_STONE.get(), ModBlocks.MARS_STONE.get(), ModBlocks.VENUS_STONE.get(),
                ModBlocks.MERCURY_STONE.get(), ModBlocks.GLACIO_STONE.get(), GTOBlocks.TITAN_STONE.get(), GTOBlocks.PLUTO_STONE.get(),
                GTOBlocks.IO_STONE.get(), GTOBlocks.GANYMEDE_STONE.get(), GTOBlocks.ENCELADUS_STONE.get(), GTOBlocks.CERES_STONE.get(),
                DDBlocks.SCULK_STONE.get(), DDBlocks.GLOOMSLATE.get());
        create(provider, Tags.ARCHWOOD_LOG,
                BlockRegistry.BLAZING_LOG.get(), BlockRegistry.CASCADING_LOG.get(), BlockRegistry.VEXING_LOG.get(), BlockRegistry.FLOURISHING_LOG.get(),
                BlockRegistry.BLAZING_WOOD.get(), BlockRegistry.CASCADING_WOOD.get(), BlockRegistry.FLOURISHING_WOOD.get(), BlockRegistry.VEXING_WOOD.get(),
                BlockRegistry.STRIPPED_AWLOG_BLUE.get(), BlockRegistry.STRIPPED_AWWOOD_BLUE.get(), BlockRegistry.STRIPPED_AWLOG_GREEN.get(), BlockRegistry.STRIPPED_AWWOOD_GREEN.get(),
                BlockRegistry.STRIPPED_AWLOG_RED.get(), BlockRegistry.STRIPPED_AWWOOD_RED.get(), BlockRegistry.STRIPPED_AWLOG_PURPLE.get(), BlockRegistry.STRIPPED_AWWOOD_PURPLE.get());
        create(provider, CustomTags.MINEABLE_WITH_WRENCH,
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
    }

    public static void initItem(RegistrateTagsProvider<Item> provider) {
        create(provider, Tags.HUMAN_EGG, Items.VILLAGER_SPAWN_EGG, Items.WITCH_SPAWN_EGG);
    }

    private static void create(RegistrateTagsProvider<Block> provider, TagKey<Block> tagKey, Block... rls) {
        TagsProvider.TagAppender<Block> builder = provider.addTag(tagKey);
        for (Block block : rls) {
            builder.add(BuiltInRegistries.BLOCK.getResourceKey(block).get());
        }
    }

    private static void create(RegistrateTagsProvider<Item> provider, TagKey<Item> tagKey, Item... rls) {
        var builder = provider.addTag(tagKey);
        for (Item item : rls) {
            builder.add(BuiltInRegistries.ITEM.getResourceKey(item).get());
        }
    }
}
