package com.gtocore.integration.ftbquests;

import com.gtolib.utils.ServerUtils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.AbstractBooleanTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;

public class ScheduledTask extends AbstractBooleanTask {

    static TaskType SCHEDULED;
    private boolean isInGame = false;
    private boolean refreshInFixedTime = false;
    private long intervalInSeconds = 0;

    public ScheduledTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        if (intervalInSeconds == 0) {
            return false;
        }
        long lastCompleteTimeSeconds = isInGame ?
                AdditionalTeamData.getLastCompleteServerTick(getQuest().id, teamData.getTeamId()) / 20 :
                AdditionalTeamData.getLastCompleteMillis(getQuest().id, teamData.getTeamId()) / 1000;
        long currentTimeSeconds = isInGame ?
                ServerUtils.getServer().getTickCount() / 20 :
                System.currentTimeMillis() / 1000;
        if (refreshInFixedTime) {
            return (currentTimeSeconds / intervalInSeconds) > (lastCompleteTimeSeconds / intervalInSeconds);
        } else {
            return (currentTimeSeconds - lastCompleteTimeSeconds) >= intervalInSeconds;
        }
    }

    @Override
    public TaskType getType() {
        return SCHEDULED;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putBoolean("isInGame", isInGame);
        nbt.putBoolean("refreshInFixedTime", refreshInFixedTime);
        nbt.putLong("intervalInSeconds", intervalInSeconds);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        isInGame = nbt.getBoolean("isInGame");
        refreshInFixedTime = nbt.getBoolean("refreshInFixedTime");
        intervalInSeconds = nbt.getLong("intervalInSeconds");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeBoolean(isInGame);
        buffer.writeBoolean(refreshInFixedTime);
        buffer.writeLong(intervalInSeconds);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        isInGame = buffer.readBoolean();
        refreshInFixedTime = buffer.readBoolean();
        intervalInSeconds = buffer.readLong();
    }

    private long getRemainingSeconds(TeamData teamData) {
        long lastCompleteTimeSeconds = isInGame ?
                AdditionalTeamData.getLastCompleteServerTick(getQuest().id, teamData.getTeamId()) / 20 :
                AdditionalTeamData.getLastCompleteMillis(getQuest().id, teamData.getTeamId()) / 1000;
        long currentTimeSeconds = isInGame ?
                ServerUtils.getServer().getTickCount() / 20 :
                System.currentTimeMillis() / 1000;
        if (refreshInFixedTime) {
            long nextTime = ((currentTimeSeconds / intervalInSeconds) + 1) * intervalInSeconds;
            return nextTime - currentTimeSeconds;
        } else {
            long elapsed = currentTimeSeconds - lastCompleteTimeSeconds;
            return Math.max(0, intervalInSeconds - elapsed);
        }
    }

    @Override
    public String formatProgress(TeamData teamData, long progress) {
        return I18n.get("tooltip.ad_astra.eta", getRemainingSeconds(teamData));
    }

    @Override
    public String formatMaxProgress() {
        return I18n.get("emi.cooking.time", intervalInSeconds);
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addBool("isInGame", isInGame, v -> isInGame = v, false);
        config.addBool("refreshInFixedTime", refreshInFixedTime, v -> refreshInFixedTime = v, false);
        config.addLong("intervalInSeconds", intervalInSeconds, v -> intervalInSeconds = v, 0, 0, Long.MAX_VALUE);
    }
}
