package com.gtocore.api.gui;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public final class GTOGuiTextures {

    public static final ResourceTexture PARALLEL_CONFIG = new ResourceTexture(GTCEu.id("textures/gui/icon/io_config/cover_settings.png"));

    public static final ResourceTexture REFRESH = new ResourceTexture(GTOCore.id("textures/gui/base/refresh.png"));
    public static final ResourceTexture DELETE = new ResourceTexture(GTOCore.id("textures/gui/base/delete"));
    public static final ResourceTexture[] VILLAGER_RECIPE_SLOTS = {
            new ResourceTexture(GTOCore.id("textures/gui/base/villager_recipe_slot_1.png")),
            new ResourceTexture(GTOCore.id("textures/gui/base/villager_recipe_slot_2.png")),
            new ResourceTexture(GTOCore.id("textures/gui/base/villager_recipe_slot_3.png")) };

    public static final ResourceTexture PROGRESS_BAR_DATA_GENERATE_BASE = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/progress_bar_data_generate_base.png"));
    public static final ResourceTexture PROGRESS_BAR_RESEARCH_BASE = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/progress_bar_research_base.png"));
    public static final ResourceTexture CONDENSE_FROM_FLUID = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/condense_from_fluid.png"));
    public static final ResourceTexture CONDENSE_FROM_PLASMA = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/condense_from_plasma.png"));
    public static final ResourceTexture CONDENSE_FROM_MOLTEN = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/condense_from_molten.png"));
    public static final ResourceTexture PROGRESS_BAR_MINING_MODULE = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/progress_bar_mining_module"));
    public static final ResourceTexture PROGRESS_BAR_DRILLING_MODULE = new ResourceTexture(GTOCore.id("textures/gui/progress_bar/progress_bar_drilling_module"));

    public static final ResourceTexture DATA_CRYSTAL_OVERLAY = new ResourceTexture(GTOCore.id("textures/gui/overlay/data_crystal_overlay"));
    public static final ResourceTexture PLANET_TELEPORT = new ResourceTexture(GTOCore.id("textures/gui/overlay/planet_teleport"));
    public static final ResourceTexture HIGH_SPEED_MODE = new ResourceTexture(GTOCore.id("textures/gui/overlay/high_speed_mode"));
    public static final ResourceTexture OVERCLOCK_CONFIG = new ResourceTexture(GTOCore.id("textures/gui/overlay/overclock_config"));
    public static final ResourceTexture STRUCTURE_CHECK = new ResourceTexture(GTOCore.id("textures/gui/overlay/structure_check"));
}
