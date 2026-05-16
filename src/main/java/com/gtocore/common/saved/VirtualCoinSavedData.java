package com.gtocore.common.saved;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import com.fast.fastcollection.O2LOpenCacheHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualCoinSavedData extends SavedData {

    public static VirtualCoinSavedData INSTANCE = new VirtualCoinSavedData();
    private final Object2LongOpenHashMap<UUID> teamCurrentCoinWork = new O2LOpenCacheHashMap<>();
    private final Object2LongOpenHashMap<UUID> teamTimesHasRun = new O2LOpenCacheHashMap<>();
    private static final Lock LOCK = new ReentrantLock();

    private static long getNextCoinConsumption(long timesHasRun) {
        return (GTOCore.isExpert() ? 1000L : 250L) * (timesHasRun + 1) * (timesHasRun);
    }

    /**
     * Accumulate the work for the team, and check if it has reached the next coin consumption threshold.
     * 
     * @return the number of coins gained, and update the current work and times has run accordingly.
     */
    public static int accumulateCoinWork(UUID team, long work) {
        LOCK.lock();
        try {
            long currentWork = INSTANCE.teamCurrentCoinWork.getLong(team);
            long timesHasRun = INSTANCE.teamTimesHasRun.getLong(team);
            long nextCoinConsumption = getNextCoinConsumption(timesHasRun);
            if (currentWork + work >= nextCoinConsumption) {
                var unused = currentWork + work;
                int gainedCoins = 0;
                while (unused >= getNextCoinConsumption(timesHasRun)) {
                    unused -= getNextCoinConsumption(timesHasRun);
                    timesHasRun++;
                    gainedCoins++;
                }
                INSTANCE.teamCurrentCoinWork.put(team, unused);
                INSTANCE.teamTimesHasRun.put(team, timesHasRun);
                INSTANCE.setDirty();
                return gainedCoins;
            } else {
                INSTANCE.teamCurrentCoinWork.put(team, currentWork + work);
                INSTANCE.setDirty();
                return 0;
            }
        } finally {
            LOCK.unlock();
        }
    }

    public static long getCurrentCoinWork(UUID team) {
        return INSTANCE.teamCurrentCoinWork.getLong(team);
    }

    public static long getNextCoinNeeded(UUID team) {
        long timesHasRun = INSTANCE.teamTimesHasRun.getLong(team);
        return getNextCoinConsumption(timesHasRun) - INSTANCE.teamCurrentCoinWork.getLong(team);
    }

    public static long getTimesHasRun(UUID team) {
        return Math.max(INSTANCE.teamTimesHasRun.getLong(team) - 1, 0);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        for (Map.Entry<UUID, Long> entry : teamCurrentCoinWork.object2LongEntrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("u", entry.getKey());
            tag.putLong("n", entry.getValue());
            tag.putLong("h", teamTimesHasRun.getLong(entry.getKey()));
            compoundTag.put(entry.getKey().toString(), tag);
        }
        return compoundTag;
    }

    public VirtualCoinSavedData(CompoundTag compoundTag) {
        for (String key : compoundTag.getAllKeys()) {
            CompoundTag tag = compoundTag.getCompound(key);
            UUID uuid = tag.getUUID("u");
            teamCurrentCoinWork.put(uuid, tag.getLong("n"));
            teamTimesHasRun.put(uuid, tag.getLong("h"));
        }
    }

    public VirtualCoinSavedData() {}
}
