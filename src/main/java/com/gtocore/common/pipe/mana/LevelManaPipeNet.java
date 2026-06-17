package com.gtocore.common.pipe.mana;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public final class LevelManaPipeNet extends LevelPipeNet<ManaPipeProperties, ManaPipeNet> {

    private static final String DATA_ID = "gtocore_mana_pipe_net";

    public static LevelManaPipeNet getOrCreate(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(tag -> new LevelManaPipeNet(serverLevel, tag),
                () -> new LevelManaPipeNet(serverLevel), DATA_ID);
    }

    public LevelManaPipeNet(ServerLevel level) {
        super(level);
    }

    public LevelManaPipeNet(ServerLevel serverLevel, CompoundTag tag) {
        super(serverLevel, tag);
    }

    @Override
    protected ManaPipeNet createNetInstance() {
        return new ManaPipeNet(this);
    }
}
