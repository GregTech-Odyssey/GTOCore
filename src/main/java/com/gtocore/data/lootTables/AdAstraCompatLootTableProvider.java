package com.gtocore.data.lootTables;

import com.gtocore.common.data.GTOGlobes;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.common.blocks.FlagBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdAstraCompatLootTableProvider extends LootTableProvider {

    public AdAstraCompatLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {}

    public static class BlockLootTables extends BlockLootSubProvider {

        public BlockLootTables() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            GTOGlobes.GLOBE_ENTRIES.stream()
                    .map(RegistryEntry::get)
                    .forEach(this::dropSelf);
            add(GTOGlobes.Blocks.GTO_FLAG.get(), b2 -> createSinglePropConditionTable(b2, FlagBlock.HALF, DoubleBlockHalf.LOWER));
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return Stream.concat(GTOGlobes.GLOBE_ENTRIES.stream(), Stream.of(GTOGlobes.Blocks.GTO_FLAG))
                    .map(RegistryEntry::get)
                    .collect(Collectors.toSet());
        }
    }
}
