package com.gto.gtocore.common.saved;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class RecipeRunLimitSavaedData extends SavedData {

    public static RecipeRunLimitSavaedData INSTANCE = new RecipeRunLimitSavaedData();

    private final Map<UUID, Map<ResourceLocation, Integer>> recipeRunLimit = new Object2ObjectOpenHashMap<>();

    public static void set(UUID uuid, ResourceLocation recipe, int count) {
        INSTANCE.recipeRunLimit.computeIfAbsent(uuid, k -> new Object2IntOpenHashMap<>()).put(recipe, count);
        INSTANCE.setDirty();
    }

    public static int get(UUID uuid, ResourceLocation recipe) {
        return INSTANCE.recipeRunLimit.getOrDefault(uuid, Map.of()).getOrDefault(recipe, 0);
    }

    public RecipeRunLimitSavaedData(CompoundTag compoundTag) {
        ListTag uuid = compoundTag.getList("l", 10);
        for (int i = 0; i < uuid.size(); i++) {
            CompoundTag uuidTag = uuid.getCompound(i);
            UUID uuid1 = uuidTag.getUUID("u");
            ListTag list = uuidTag.getList("r", 10);
            Map<ResourceLocation, Integer> map = new Object2IntOpenHashMap<>();
            for (int j = 0; j < list.size(); j++) {
                CompoundTag id = list.getCompound(j);
                map.put(new ResourceLocation(id.getString("i")), id.getInt("c"));
            }
            recipeRunLimit.put(uuid1, map);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag uuid = new ListTag();
        for (Map.Entry<UUID, Map<ResourceLocation, Integer>> uuidEntry : recipeRunLimit.entrySet()) {
            ListTag list = new ListTag();
            for (Map.Entry<ResourceLocation, Integer> recipeEntry : uuidEntry.getValue().entrySet()) {
                CompoundTag id = new CompoundTag();
                id.putString("i", recipeEntry.getKey().toString());
                id.putInt("c", recipeEntry.getValue());
                list.add(id);
            }
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("u", uuidEntry.getKey());
            uuidTag.put("r", list);
        }
        compoundTag.put("l", uuid);
        return compoundTag;
    }
}
