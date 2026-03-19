package com.gtocore.data.blockstate;

import com.gtocore.common.data.GTOGlobes;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.AdAstra;

public class AdAstraCompatBlockStateProvider extends BlockStateProvider {

    public AdAstraCompatBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AdAstra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        GTOGlobes.GLOBE_ENTRIES.stream().map(RegistryEntry::get).forEach(this::globe);
        flag(GTOGlobes.Blocks.GTO_FLAG.get());
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }

    public void globe(Block block) {
        itemModels().getBuilder(key(block).getPath()).parent(itemModels().getExistingFile(modLoc("item/rendered_item")));
        getVariantBuilder(block).forAllStates(state -> {
            String name = this.name(block);
            ResourceLocation texture = modLoc("block/globe/%s".formatted(name));

            ConfiguredModel.builder()
                    .modelFile(models().getBuilder(name + "_cube")
                            .texture("0", texture)
                            .parent(models().getExistingFile(modLoc("block/globe_cube"))))
                    .build();

            return ConfiguredModel.builder()
                    .modelFile(models().getBuilder(name)
                            .texture("0", texture)
                            .texture("particle", texture)
                            .parent(models().getExistingFile(modLoc("block/globe"))))
                    .build();
        });
    }

    public void flag(Block block) {
        simpleBlockItem(block, models().getBuilder("block/%s".formatted(name(block))));
        getVariantBuilder(block).forAllStates(state -> {
            String name = this.name(block);
            ResourceLocation texture = modLoc("block/flag/%s".formatted(name));

            return ConfiguredModel.builder()
                    .modelFile(models().getBuilder(name)
                            .texture("0", texture)
                            .texture("particle", texture)
                            .parent(models().getExistingFile(modLoc("block/flag"))))
                    .build();
        });
    }
}
