package com.gtocore.data.tag;

import com.gtocore.common.forge.GTOVillagerRegistry;

import com.gtolib.GTOCore;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class GtoPoiTypeTagsProvider extends PoiTypeTagsProvider {

    public GtoPoiTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(packOutput, registries, GTOCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(GTOVillagerRegistry.EXOTIC_POI.getKey());
    }
}
