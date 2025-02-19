package com.gto.gtocore.data;

import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.kyanite.deeperdarker.content.DDBlocks;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import earth.terrarium.adastra.common.registry.ModBlocks;

public final class TagsHandler {

    public static final TagKey<Block> ALL_LAYER_STONE = TagUtil.createBlockTag("all_layer_stone");

    static void initBlock(RegistrateTagsProvider<Block> provider) {
        provider.addTag(ALL_LAYER_STONE).addTag(BlockTags.STONE_ORE_REPLACEABLES).addTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES).addTag(BlockTags.NETHER_CARVER_REPLACEABLES);
        create(provider, ALL_LAYER_STONE, Blocks.END_STONE, ModBlocks.MOON_STONE.get(), ModBlocks.MARS_STONE.get(), ModBlocks.VENUS_STONE.get(),
                ModBlocks.MERCURY_STONE.get(), ModBlocks.GLACIO_STONE.get(), GTOBlocks.TITAN_STONE.get(), GTOBlocks.PLUTO_STONE.get(),
                GTOBlocks.IO_STONE.get(), GTOBlocks.GANYMEDE_STONE.get(), GTOBlocks.ENCELADUS_STONE.get(), GTOBlocks.CERES_STONE.get(),
                DDBlocks.SCULK_STONE.get(), DDBlocks.GLOOMSLATE.get());
    }

    static void initItem(RegistrateTagsProvider<Item> provider) {}

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
