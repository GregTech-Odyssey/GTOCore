package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class PrimitiveDistillationRenderer extends WorkableCasingMachineRenderer {

    private static final ResourceLocation CASING = GTCEu.id("block/casings/steam/steel/side");
    private static final ResourceLocation WORKABLE_MODEL = GTOCore.id("block/multiblock/primitive_distillation_tower");

    private static final ResourceLocation TANK_OVERLAY = GTOCore.id("block/multiblock/primitive_distillation_tower/tank");
    private static final ResourceLocation TANK_BG_OVERLAY = GTOCore.id("block/multiblock/primitive_distillation_tower/tank_bg");

    private static final ResourceLocation WATER_STILL = GTOCore.id("block/multiblock/primitive_distillation_tower/water_still");
    private static final ResourceLocation STEAM_STILL = GTOCore.id("block/multiblock/primitive_distillation_tower/steam_still");

    public PrimitiveDistillationRenderer() {
        super(CASING, WORKABLE_MODEL);
    }
}
