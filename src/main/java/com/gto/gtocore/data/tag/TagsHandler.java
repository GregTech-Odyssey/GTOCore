package com.gto.gtocore.data.tag;

import com.gto.gtocore.common.data.GTOBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.kyanite.deeperdarker.content.DDBlocks;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import earth.terrarium.adastra.common.registry.ModBlocks;

public interface TagsHandler {

    static void initBlock(RegistrateTagsProvider<Block> provider) {
        provider.addTag(Tags.ALL_LAYER_STONE).addTag(BlockTags.STONE_ORE_REPLACEABLES).addTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES).addTag(BlockTags.NETHER_CARVER_REPLACEABLES);
        create(provider, Tags.ALL_LAYER_STONE, Blocks.END_STONE, ModBlocks.MOON_STONE.get(), ModBlocks.MARS_STONE.get(), ModBlocks.VENUS_STONE.get(),
                ModBlocks.MERCURY_STONE.get(), ModBlocks.GLACIO_STONE.get(), GTOBlocks.TITAN_STONE.get(), GTOBlocks.PLUTO_STONE.get(),
                GTOBlocks.IO_STONE.get(), GTOBlocks.GANYMEDE_STONE.get(), GTOBlocks.ENCELADUS_STONE.get(), GTOBlocks.CERES_STONE.get(),
                DDBlocks.SCULK_STONE.get(), DDBlocks.GLOOMSLATE.get());
    }

    static void initItem(RegistrateTagsProvider<Item> provider) {
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
