package com.gto.gtocore.api.gui;

import com.gto.gtocore.GTOCore;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public interface GTOGuiTextures {

    ResourceTexture PLANET_TELEPORT = createTexture("planet_teleport");

    ResourceTexture HIGH_SPEED_MODE = createTexture("high_speed_mode");

    ResourceTexture PROGRESS_BAR_MINING_MODULE = createTexture("progress_bar_mining_module");
    ResourceTexture PROGRESS_BAR_DRILLING_MODULE = createTexture("progress_bar_drilling_module");

    private static ResourceTexture createTexture(String location) {
        return new ResourceTexture(GTOCore.id("textures/gui/%s.png".formatted(location)));
    }
}
