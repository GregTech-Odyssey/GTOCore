package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagLoader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gto.fastcollection.O2OOpenCacheHashMap;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import lombok.experimental.UtilityClass;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class GTOTags {

    private final ResourceLocation RED_ALLOY_INGOT = RLUtils.parse("morered:red_alloy_ingot");

    private boolean cache;

    private TagEntry convert(ResourceLocation id, TagEntry tagEntry) {
        if (cache) return tagEntry;
        if (tagEntry.getId().equals(RED_ALLOY_INGOT)) {
            cache = true;
            return null;
        }
        return tagEntry;
    }

    public O2OOpenCacheHashMap<ResourceLocation, List<TagLoader.EntryWithSource>> load(ResourceManager resourceManager, String directory) {
        GTOTags.cache = false;
        var map = new O2OOpenCacheHashMap<ResourceLocation, List<TagLoader.EntryWithSource>>();
        FileToIdConverter filetoidconverter = FileToIdConverter.json(directory);
        filetoidconverter.listMatchingResourceStacks(resourceManager).forEach((resourceLocation, resources) -> {
            ResourceLocation resourcelocation1 = filetoidconverter.fileToId(resourceLocation);
            resources.forEach(resource -> {
                try (Reader reader = resource.openAsReader()) {
                    JsonElement jsonelement = JsonParser.parseReader(reader);
                    var list = map.computeIfAbsent(resourcelocation1, (p_215974_) -> new ArrayList<>());
                    TagFile tagfile = (TagFile) TagFile.CODEC.parse(new Dynamic(JsonOps.INSTANCE, jsonelement)).getOrThrow(false, s -> GTOCore.LOGGER.error(s.toString()));
                    if (tagfile.replace()) {
                        list.clear();
                    }

                    String s = resource.sourcePackId();
                    for (var e : tagfile.entries()) {
                        e = GTOTags.convert(resourcelocation1, e);
                        if (e == null) continue;
                        list.add(new TagLoader.EntryWithSource(e, s));
                    }
                    for (var e : tagfile.remove()) {
                        list.add(new TagLoader.EntryWithSource(e, s, true));
                    }
                } catch (Exception exception) {
                    GTOCore.LOGGER.error("Couldn't read tag list {} from {} in data pack {}", resourcelocation1, resourceLocation, resource.sourcePackId(), exception);
                }
            });
        });
        return map;
    }
}
