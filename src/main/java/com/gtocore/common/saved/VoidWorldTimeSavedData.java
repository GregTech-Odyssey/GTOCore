package com.gtocore.common.saved;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;

public class VoidWorldTimeSavedData extends SavedData {

    public static final String DATA_NAME = "void_world_time_data";
    public static VoidWorldTimeSavedData INSTANCE = new VoidWorldTimeSavedData();

    private boolean fixedTime = true;

    public VoidWorldTimeSavedData() {}

    public VoidWorldTimeSavedData(CompoundTag compoundTag) {
        load(compoundTag);
    }

    public static VoidWorldTimeSavedData initialize(CompoundTag compoundTag) {
        return new VoidWorldTimeSavedData(compoundTag);
    }

    public boolean isFixedTime() {
        return fixedTime;
    }

    public boolean toggleFixedTime() {
        fixedTime = !fixedTime;
        setDirty();
        return fixedTime;
    }

    public void setFixedTime(boolean fixedTime) {
        if (this.fixedTime != fixedTime) {
            this.fixedTime = fixedTime;
            setDirty();
        }
    }

    public void load(CompoundTag compoundTag) {
        fixedTime = !compoundTag.contains("fixed_time") || compoundTag.getBoolean("fixed_time");
    }

    public static VoidWorldTimeSavedData get(MinecraftServer server) {
        ServerLevel serverLevel = server.getLevel(Level.OVERWORLD);
        if (serverLevel == null) {
            return INSTANCE;
        }
        INSTANCE = serverLevel.getDataStorage().computeIfAbsent(VoidWorldTimeSavedData::initialize, VoidWorldTimeSavedData::new, DATA_NAME);
        return INSTANCE;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        compoundTag.putBoolean("fixed_time", fixedTime);
        return compoundTag;
    }
}
