package com.gtocore.integration.ftbquests;

import com.gtolib.utils.ServerUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import com.fast.fastcollection.O2LOpenCacheHashMap;
import dev.architectury.event.EventResult;
import dev.ftb.mods.ftbquests.events.ObjectCompletedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class AdditionalTeamData extends SavedData {

    public static AdditionalTeamData instance = new AdditionalTeamData();
    private final O2LOpenCacheHashMap<IdsData> uuids2lastCompleteMillis = new O2LOpenCacheHashMap<>();
    private final O2LOpenCacheHashMap<IdsData> uuids2lastCompleteServerTick = new O2LOpenCacheHashMap<>();

    static {
        ObjectCompletedEvent.TASK.register(AdditionalTeamData::onCompleted);
    }

    public AdditionalTeamData() {}

    public AdditionalTeamData(CompoundTag nbt) {
        ListTag list = nbt.getList("dataList", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag dataTag = list.getCompound(i);
            IdsData data = IdsData.readFromNBT(dataTag.getCompound("idsData"));
            long lastCompleteStamp = dataTag.getLong("lastCompleteStamp");
            long lastCompleteServerTick = dataTag.getLong("lastCompleteServerTick");
            uuids2lastCompleteMillis.put(data, lastCompleteStamp);
            uuids2lastCompleteServerTick.put(data, lastCompleteServerTick);
        }
    }

    public static long getLastCompleteMillis(long questID, UUID teamUUID) {
        IdsData data = new IdsData(questID, teamUUID);
        return instance.uuids2lastCompleteMillis.getLong(data);
    }

    public static long getLastCompleteServerTick(long questID, UUID teamUUID) {
        IdsData data = new IdsData(questID, teamUUID);
        return instance.uuids2lastCompleteServerTick.getLong(data);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag list = new ListTag();
        for (IdsData data : uuids2lastCompleteMillis.keySet()) {
            CompoundTag dataTag = new CompoundTag();
            dataTag.put("idsData", data.writeToNBT());
            dataTag.putLong("lastCompleteStamp", uuids2lastCompleteMillis.getLong(data));
            dataTag.putLong("lastCompleteServerTick", uuids2lastCompleteServerTick.getLong(data));
            list.add(dataTag);
        }
        compoundTag.put("dataList", list);
        return compoundTag;
    }

    public static EventResult onCompleted(ObjectCompletedEvent.TaskEvent event) {
        instance.uuids2lastCompleteMillis.put(
                new IdsData(event.getTask().getQuest().id, event.getData().getTeamId()),
                event.getTime().getTime());
        instance.uuids2lastCompleteServerTick.put(
                new IdsData(event.getTask().getQuest().id, event.getData().getTeamId()),
                ServerUtils.getServer().getTickCount());
        instance.setDirty();
        return EventResult.pass();
    }

    public record IdsData(long questID, UUID teamUUID) implements Comparable<IdsData> {

        @Override
        public int compareTo(IdsData o) {
            return this.questID != o.questID ? Long.compare(this.questID, o.questID) : this.teamUUID.compareTo(o.teamUUID);
        }

        public static IdsData readFromNBT(CompoundTag nbt) {
            return new IdsData(nbt.getLong("questID"), nbt.getUUID("teamUUID"));
        }

        public CompoundTag writeToNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putLong("questID", questID);
            nbt.putUUID("teamUUID", teamUUID);
            return nbt;
        }
    }
}
