package com.gtocore.common.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.common.blocks.FlagBlock;
import earth.terrarium.adastra.common.blocks.GlobeBlock;
import earth.terrarium.adastra.common.items.rendered.RenderedBlockItem;
import earth.terrarium.adastra.common.registry.ModBlocks;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static earth.terrarium.adastra.common.registry.ModItems.FLAGS;
import static earth.terrarium.adastra.common.registry.ModItems.GLOBES;
import static net.minecraft.world.level.block.Blocks.IRON_BLOCK;
import static net.minecraft.world.level.block.Blocks.OAK_PLANKS;

public final class GTOGlobes {

    public static final ReferenceSet<RegistryEntry<GlobeBlock>> GLOBE_ENTRIES = new ReferenceOpenHashSet<>();

    public static final class Items {

        public static final RegistryEntry<Item> TITAN_GLOBE = GLOBES.register("titan_globe", () -> new RenderedBlockItem(Blocks.TITAN_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> PLUTO_GLOBE = GLOBES.register("pluto_globe", () -> new RenderedBlockItem(Blocks.PLUTO_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> IO_GLOBE = GLOBES.register("io_globe", () -> new RenderedBlockItem(Blocks.IO_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> GANYMEDE_GLOBE = GLOBES.register("ganymede_globe", () -> new RenderedBlockItem(Blocks.GANYMEDE_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> ENCELADUS_GLOBE = GLOBES.register("enceladus_globe", () -> new RenderedBlockItem(Blocks.ENCELADUS_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> CERES_GLOBE = GLOBES.register("ceres_globe", () -> new RenderedBlockItem(Blocks.CERES_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        public static final RegistryEntry<Item> BARNARDA_C_GLOBE = GLOBES.register("barnarda_c_globe", () -> new RenderedBlockItem(Blocks.BARNARDA_C_GLOBE.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static final RegistryEntry<Item> GTO_FLAG = FLAGS.register("gto_flag", () -> new RenderedBlockItem(Blocks.GTO_FLAG.get(), new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));

        public static void init() {}
    }

    public static final class Blocks {

        public static final RegistryEntry<FlagBlock> GTO_FLAG = ModBlocks.FLAGS.register("gto_flag", () -> new FlagBlock(BlockBehaviour.Properties.copy(OAK_PLANKS).strength(1.0F)) {

            @Override
            public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
                return InteractionResult.PASS;
            }

            @Override
            public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, @NotNull ItemStack stack) {}

            @Override
            public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {}
        });

        // titan
        public static final RegistryEntry<GlobeBlock> TITAN_GLOBE = registerGlobe("titan_globe");
        // pluto
        public static final RegistryEntry<GlobeBlock> PLUTO_GLOBE = registerGlobe("pluto_globe");
        // io
        public static final RegistryEntry<GlobeBlock> IO_GLOBE = registerGlobe("io_globe");
        // ganymede
        public static final RegistryEntry<GlobeBlock> GANYMEDE_GLOBE = registerGlobe("ganymede_globe");
        // enceladus
        public static final RegistryEntry<GlobeBlock> ENCELADUS_GLOBE = registerGlobe("enceladus_globe");
        // ceres
        public static final RegistryEntry<GlobeBlock> CERES_GLOBE = registerGlobe("ceres_globe");
        // barnarda_c
        public static final RegistryEntry<GlobeBlock> BARNARDA_C_GLOBE = registerGlobe("barnarda_c_globe");

        public static void init() {}

        private static RegistryEntry<GlobeBlock> registerGlobe(String name) {
            var entry = ModBlocks.GLOBES.register(name, () -> new GlobeBlock(BlockBehaviour.Properties.copy(IRON_BLOCK).noOcclusion()));
            GLOBE_ENTRIES.add(entry);
            return entry;
        }
    }
}
