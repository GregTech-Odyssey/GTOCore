package com.gtocore.common.saved;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.data.Galaxy;

import com.gregtechceu.gtceu.utils.collection.O2IOpenCacheHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static com.gtolib.api.GTOValues.*;

public final class DysonSphereSavaedData extends SavedData {

    private final Object2IntOpenHashMap<Galaxy> dysonLaunchData;
    private final Object2IntOpenHashMap<Galaxy> dysonDamageData;
    private final Object2BooleanOpenHashMap<Galaxy> dysonUse;

    public static boolean getDimensionUse(ResourceKey<Level> dim) {
        Galaxy galaxy = GTODimensions.getGalaxy(dim.location());
        if (galaxy == null) return false;
        return INSTANCE.dysonUse.getOrDefault(galaxy, false);
    }

    public static IntIntImmutablePair getDimensionData(ResourceKey<Level> dim) {
        Galaxy galaxy = GTODimensions.getGalaxy(dim.location());
        if (galaxy == null) return IntIntImmutablePair.of(0, 0);
        return IntIntImmutablePair.of(INSTANCE.dysonLaunchData.getOrDefault(galaxy, 0), INSTANCE.dysonDamageData.getOrDefault(galaxy, 0));
    }

    public static int getDimensionLaunchData(ResourceKey<Level> dim) {
        Galaxy galaxy = GTODimensions.getGalaxy(dim.location());
        if (galaxy == null) return 0;
        return INSTANCE.dysonLaunchData.getOrDefault(galaxy, 0);
    }

    public static void setDysonData(ResourceKey<Level> dim, int a, int b) {
        Galaxy galaxy = GTODimensions.getGalaxy(dim.location());
        if (galaxy == null) return;
        INSTANCE.dysonLaunchData.put(galaxy, a);
        INSTANCE.dysonDamageData.put(galaxy, b);
        INSTANCE.setDirty();
    }

    public static void setDysonUse(ResourceKey<Level> dim, boolean a) {
        Galaxy galaxy = GTODimensions.getGalaxy(dim.location());
        if (galaxy == null) return;
        INSTANCE.dysonUse.put(galaxy, a);
        INSTANCE.setDirty();
    }

    public static DysonSphereSavaedData INSTANCE = new DysonSphereSavaedData();

    public DysonSphereSavaedData() {
        dysonDamageData = new O2IOpenCacheHashMap<>();
        dysonLaunchData = new O2IOpenCacheHashMap<>();
        dysonUse = new Object2BooleanOpenHashMap<>();
    }

    public DysonSphereSavaedData(CompoundTag compoundTag) {
        this();
        ListTag dyson = compoundTag.getList(DYSON_LIST, Tag.TAG_COMPOUND);
        for (int i = 0; i < dyson.size(); i++) {
            CompoundTag tag = dyson.getCompound(i);
            dysonLaunchData.put(Galaxy.get(tag.getString(GALAXY)), tag.getInt(DYSON_COUNT));
            dysonDamageData.put(Galaxy.get(tag.getString(GALAXY)), tag.getInt(DYSON_DAMAGE));
            dysonUse.put(Galaxy.get(tag.getString(GALAXY)), tag.getBoolean(DYSON_USE));
        }
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        dysonLaunchData.forEach((g, i) -> {
            CompoundTag tag = new CompoundTag();
            tag.putString(GALAXY, g.name());
            tag.putInt(DYSON_COUNT, i);
            tag.putInt(DYSON_DAMAGE, dysonDamageData.getOrDefault(g, 0));
            tag.putBoolean(DYSON_USE, dysonUse.getOrDefault(g, false));
            listTag.add(tag);
        });
        compoundTag.put(DYSON_LIST, listTag);
        return compoundTag;
    }

    public Object2IntOpenHashMap<Galaxy> getDysonLaunchData() {
        return this.dysonLaunchData;
    }

    public Object2IntOpenHashMap<Galaxy> getDysonDamageData() {
        return this.dysonDamageData;
    }

    public Object2BooleanOpenHashMap<Galaxy> getDysonUse() {
        return this.dysonUse;
    }
}
