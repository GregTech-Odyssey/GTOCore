package com.gtocore.integration.ftbquests;

import com.gregtechceu.gtceu.GTCEu;

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

public class ModTask extends AbstractBooleanTask {

    static TaskType MOD;
    private String modid = "";

    public ModTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return GTCEu.isModLoaded(modid);
    }

    @Override
    public TaskType getType() {
        return MOD;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("modid", modid);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        modid = nbt.getString("modid");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(modid, Short.MAX_VALUE);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        modid = buffer.readUtf(Short.MAX_VALUE);
    }

    public MutableComponent getAltTitle() {
        return Component.translatable("gui.tooltips.ae2.Mod")
                .append("[" + modid + "] ")
                .append(Component.translatable("gui.loaded"))
                .append(Component.literal(": "))
                .append(GTCEu.isModLoaded(modid) ? "✔" : "✘");
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("modid", modid, v -> modid = v, "");
    }
}
