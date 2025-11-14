package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.dynamic.DynamicInitialData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.AbstractBooleanTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;

public class DifficultyTask extends AbstractBooleanTask {

    static TaskType GTODIFFICULTY;
    private int d;

    public DifficultyTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return GTOCore.difficulty == d || d == 0;
    }

    @Override
    public TaskType getType() {
        return GTODIFFICULTY;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putInt("d", d);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        d = nbt.getInt("d");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeInt(d);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        d = buffer.readInt();
    }

    public MutableComponent getAltTitle() {
        return Component.translatable("selectWorld.gto_difficulty", DynamicInitialData.getDifficultyComponent(d))
                .append(GTOCore.difficulty == d || d == 0 ? "[✔]" : "[✘]");
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addInt("difficulty", d, v -> d = v, 0, 0, 3);
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }
}
