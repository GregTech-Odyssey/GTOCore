package com.gtocore.common.pipe.heat;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public final class LevelHeatPipeNet extends LevelPipeNet<HeatPipeProperties, HeatPipeNet> {

    private static final String DATA_ID = "gtocore_heat_pipe_net";

    public static LevelHeatPipeNet getOrCreate(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(tag -> new LevelHeatPipeNet(serverLevel, tag),
                () -> new LevelHeatPipeNet(serverLevel), DATA_ID);
    }

    public LevelHeatPipeNet(ServerLevel level) {
        super(level);
    }

    public LevelHeatPipeNet(ServerLevel serverLevel, CompoundTag tag) {
        super(serverLevel, tag);
    }

    @Override
    protected HeatPipeNet createNetInstance() {
        return new HeatPipeNet(this);
    }
}
