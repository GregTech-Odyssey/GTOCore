package com.gto.gtocore.api.gui;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public interface GTOGuiTextures {

    ResourceTexture PLANET_TELEPORT = createTexture("planet_teleport");

    ResourceTexture HIGH_SPEED_MODE = createTexture("high_speed_mode");

    ResourceTexture PARALLEL_CONFIG = new ResourceTexture(GTCEu.id("textures/gui/icon/io_config/cover_settings.png"));

    ResourceTexture OVERCLOCK_CONFIG = createTexture("overclock_config");

    ResourceTexture PROGRESS_BAR_MINING_MODULE = createTexture("progress_bar_mining_module");
    ResourceTexture PROGRESS_BAR_DRILLING_MODULE = createTexture("progress_bar_drilling_module");

    ResourceTexture STRUCTURE_CHECK = createTexture("structure_check");
    ResourceTexture FOLDING_OUTPUT = createTexture("folding_output");

    private static ResourceTexture createTexture(String location) {
        return new ResourceTexture(GTOCore.id("textures/gui/%s.png".formatted(location)));
    }
}
