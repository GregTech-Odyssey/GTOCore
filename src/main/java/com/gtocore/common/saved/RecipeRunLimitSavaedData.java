package com.gtocore.common.saved;

import com.gtolib.utils.RLUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class RecipeRunLimitSavaedData extends SavedData {

    public static RecipeRunLimitSavaedData INSTANCE = new RecipeRunLimitSavaedData();
    private final Map<UUID, Object2IntOpenHashMap<ResourceLocation>> recipeRunLimit = new Object2ObjectOpenHashMap<>();

    public static void set(UUID uuid, ResourceLocation recipe, int count) {
        INSTANCE.recipeRunLimit.computeIfAbsent(uuid, k -> new Object2IntOpenHashMap<>()).put(recipe, count);
        INSTANCE.setDirty();
    }

    public static int get(UUID uuid, ResourceLocation recipe) {
        var map = INSTANCE.recipeRunLimit.get(uuid);
        if (map == null) return 0;
        return map.getOrDefault(recipe, 0);
    }

    public RecipeRunLimitSavaedData(CompoundTag compoundTag) {
        ListTag uuid = compoundTag.getList("l", 10);
        for (int i = 0; i < uuid.size(); i++) {
            CompoundTag uuidTag = uuid.getCompound(i);
            UUID uuid1 = uuidTag.getUUID("u");
            ListTag list = uuidTag.getList("r", 10);
            Object2IntOpenHashMap<ResourceLocation> map = new Object2IntOpenHashMap<>();
            for (int j = 0; j < list.size(); j++) {
                CompoundTag id = list.getCompound(j);
                map.put(RLUtils.parse(id.getString("i")), id.getInt("c"));
            }
            recipeRunLimit.put(uuid1, map);
        }
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag uuid = new ListTag();
        for (Map.Entry<UUID, Object2IntOpenHashMap<ResourceLocation>> uuidEntry : recipeRunLimit.entrySet()) {
            ListTag list = new ListTag();
            for (Object2IntMap.Entry<ResourceLocation> recipeEntry : uuidEntry.getValue().object2IntEntrySet()) {
                CompoundTag id = new CompoundTag();
                id.putString("i", recipeEntry.getKey().toString());
                id.putInt("c", recipeEntry.getIntValue());
                list.add(id);
            }
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("u", uuidEntry.getKey());
            uuidTag.put("r", list);
        }
        compoundTag.put("l", uuid);
        return compoundTag;
    }

    public RecipeRunLimitSavaedData() {}
}
