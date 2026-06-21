package com.gtocore.integration.teammap.bridge;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface GuiMapBridge {

    double gtoTeamMap$getCameraX();

    double gtoTeamMap$getCameraZ();

    double gtoTeamMap$getScale();

    ResourceKey<Level> gtoTeamMap$getViewedDimension();
}
