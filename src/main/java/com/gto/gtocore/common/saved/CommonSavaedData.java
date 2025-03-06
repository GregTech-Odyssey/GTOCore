package com.gto.gtocore.common.saved;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.gto.gtocore.api.GTOValues.*;

public final class CommonSavaedData extends SavedData {

    public static CommonSavaedData INSTANCE = new CommonSavaedData();

    public static void setData(CompoundTag data) {
        INSTANCE.data = data;
        INSTANCE.setDirty();
    }

    public static CompoundTag getData() {
        return INSTANCE.data;
    }

    @Getter
    private final Map<UUID, Set<ResourceLocation>> planetUnlocked;

    private CompoundTag data;

    public CommonSavaedData() {
        planetUnlocked = new Object2ObjectOpenHashMap<>();
        data = new CompoundTag();
        setDirty();
    }

    public CommonSavaedData(CompoundTag compoundTag) {
        this();
        data = (CompoundTag) compoundTag.get("data");
        ListTag listTag = compoundTag.getList(PLAYER_LIST, 10);
        for (Tag tag : listTag) {
            CompoundTag playerTag = (CompoundTag) tag;
            Set<ResourceLocation> set = new ObjectOpenHashSet<>();
            ListTag planetsList = playerTag.getList(PLANET_LIST, 10);
            for (Tag planetTag : planetsList) {
                set.add(new ResourceLocation(((CompoundTag) planetTag).getString(PLANET_NAME)));
            }
            planetUnlocked.put(playerTag.getUUID(PLAYER_UUID), set);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        planetUnlocked.forEach((k, v) -> {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID(PLAYER_UUID, k);
            ListTag planetsList = new ListTag();
            v.forEach(planet -> {
                CompoundTag planetTag = new CompoundTag();
                planetTag.putString(PLANET_NAME, planet.toString());
                planetsList.add(planetTag);
            });
            playerTag.put(PLANET_LIST, planetsList);
            listTag.add(playerTag);
        });
        compoundTag.put("data", data);
        compoundTag.put(PLAYER_LIST, listTag);
        return compoundTag;
    }
}
