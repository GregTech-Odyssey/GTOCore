package com.gtocore.common.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.common.blocks.GlobeBlock;
import earth.terrarium.adastra.common.items.rendered.RenderedBlockItem;
import earth.terrarium.adastra.common.registry.ModBlocks;

import static earth.terrarium.adastra.common.registry.ModItems.GLOBES;
import static net.minecraft.world.level.block.Blocks.IRON_BLOCK;

public class GTOGlobes {

    public static class Items {

        public static final RegistryEntry<Item> TITAN_GLOBE = GLOBES.register("titan_globe", () -> new RenderedBlockItem(Blocks.TITAN_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> PLUTO_GLOBE = GLOBES.register("pluto_globe", () -> new RenderedBlockItem(Blocks.PLUTO_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> IO_GLOBE = GLOBES.register("io_globe", () -> new RenderedBlockItem(Blocks.IO_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> GANYMEDE_GLOBE = GLOBES.register("ganymede_globe", () -> new RenderedBlockItem(Blocks.GANYMEDE_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> ENCELADUS_GLOBE = GLOBES.register("enceladus_globe", () -> new RenderedBlockItem(Blocks.ENCELADUS_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> CERES_GLOBE = GLOBES.register("ceres_globe", () -> new RenderedBlockItem(Blocks.CERES_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> BARNARDA_C_GLOBE = GLOBES.register("barnarda_c_globe", () -> new RenderedBlockItem(Blocks.BARNARDA_C_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static void init() {}
    }

    public static class Blocks {

        // titan
        public static final RegistryEntry<GlobeBlock> TITAN_GLOBE = ModBlocks.GLOBES.register("titan_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // pluto
        public static final RegistryEntry<GlobeBlock> PLUTO_GLOBE = ModBlocks.GLOBES.register("pluto_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // io
        public static final RegistryEntry<GlobeBlock> IO_GLOBE = ModBlocks.GLOBES.register("io_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // ganymede
        public static final RegistryEntry<GlobeBlock> GANYMEDE_GLOBE = ModBlocks.GLOBES.register("ganymede_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // enceladus
        public static final RegistryEntry<GlobeBlock> ENCELADUS_GLOBE = ModBlocks.GLOBES.register("enceladus_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // ceres
        public static final RegistryEntry<GlobeBlock> CERES_GLOBE = ModBlocks.GLOBES.register("ceres_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
        // barnarda_c
        public static final RegistryEntry<GlobeBlock> BARNARDA_C_GLOBE = ModBlocks.GLOBES.register("barnarda_c_globe", () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));

        public static void init() {}
    }
}
