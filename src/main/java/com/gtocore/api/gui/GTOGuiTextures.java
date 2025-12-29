package com.gtocore.api.gui;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public final class GTOGuiTextures {

    public static final ResourceTexture PARALLEL_CONFIG = new ResourceTexture(GTCEu.id("textures/gui/icon/io_config/cover_settings.png"));
    public static final ResourceTexture BOXED_BACKGROUND = new ResourceTexture(GTCEu.id("textures/gui/base/boxed_background.png"));

    public static final ResourceTexture REFRESH = getTexture("base/refresh");
    public static final ResourceTexture DELETE = getTexture("base/delete");
    public static final ResourceTexture ENERGY = getTexture("base/energy");
    public static final ResourceTexture[] VILLAGER_RECIPE_SLOTS = {
            getTexture("base/villager_recipe_slot_0"),
            getTexture("base/villager_recipe_slot_1"),
            getTexture("base/villager_recipe_slot_2") };

    public static final ResourceTexture PROGRESS_BAR_DATA_GENERATE_BASE = getTexture("progress_bar/progress_bar_data_generate_base");
    public static final ResourceTexture PROGRESS_BAR_RESEARCH_BASE = getTexture("progress_bar/progress_bar_research_base");
    public static final ResourceTexture CONDENSE_FROM_FLUID = getTexture("progress_bar/condense_from_fluid");
    public static final ResourceTexture CONDENSE_FROM_PLASMA = getTexture("progress_bar/condense_from_plasma");
    public static final ResourceTexture CONDENSE_FROM_MOLTEN = getTexture("progress_bar/condense_from_molten");
    public static final ResourceTexture PROGRESS_BAR_MINING_MODULE = getTexture("progress_bar/progress_bar_mining_module");
    public static final ResourceTexture PROGRESS_BAR_DRILLING_MODULE = getTexture("progress_bar/progress_bar_drilling_module");

    public static final ResourceTexture DATA_CRYSTAL_OVERLAY = getTexture("overlay/data_crystal_overlay");
    public static final ResourceTexture PLANET_TELEPORT = getTexture("overlay/planet_teleport");
    public static final ResourceTexture HIGH_SPEED_MODE = getTexture("overlay/high_speed_mode");
    public static final ResourceTexture OVERCLOCK_CONFIG = getTexture("overlay/overclock_config");
    public static final ResourceTexture STRUCTURE_CHECK = getTexture("overlay/structure_check");
    public static final ResourceTexture XP_ORBS = new ResourceTexture(RLUtils.mc("textures/entity/experience_orb.png"));
    public static final ResourceTexture SMALL_XP_ORB = XP_ORBS.getSubTexture(0.25, 0, 0.25, 0.25);
    public static final ResourceTexture LARGE_XP_ORB = XP_ORBS.getSubTexture(0, 0.5, 0.25, 0.25);

    private static ResourceTexture getTexture(String string) {
        return new ResourceTexture(GTOCore.id("textures/gui/" + string + ".png"));
    }
}
