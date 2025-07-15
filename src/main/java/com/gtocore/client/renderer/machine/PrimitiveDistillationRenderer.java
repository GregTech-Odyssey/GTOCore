package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.multiblock.noenergy.PrimitiveDistillationTowerMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import com.lowdragmc.lowdraglib.client.bakedpipeline.Quad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

@SuppressWarnings("unused")
public class PrimitiveDistillationRenderer extends WorkableCasingMachineRenderer implements IHeaterRenderer {

    private static final ResourceLocation CASING = GTCEu.id("block/casings/steam/steel/side");
    private static final ResourceLocation WORKABLE_MODEL = GTOCore.id("block/multiblock/primitive_distillation_tower");

    private static final ResourceLocation TANK_OVERLAY = GTOCore.id("block/multiblock/primitive_distillation_tower/tank");
    private static final ResourceLocation TANK_BG_OVERLAY = GTOCore.id("block/multiblock/primitive_distillation_tower/tank_bg");

    private static final ResourceLocation WATER_STILL = GTOCore.id("block/multiblock/primitive_distillation_tower/water_still");
    private static final ResourceLocation STEAM_STILL = GTOCore.id("block/multiblock/primitive_distillation_tower/steam_still");

    public PrimitiveDistillationRenderer() {
        super(CASING, WORKABLE_MODEL);
    }

    @Override
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        renderHeater(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
    }

    @Override
    public void renderHeater(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        if (!(machine instanceof PrimitiveDistillationTowerMachine distillation) || side == null || !distillation.isFormed()) { //
            return;
        }

        var temp = distillation.getHeat();
        var maxTemp = PrimitiveDistillationTowerMachine.getMaxHeat();
        float tempRatio = Math.min(1.0f, (float) temp / maxTemp);

        var direction = RelativeDirection.FRONT.getRelative(machine.getFrontFacing(), machine.getUpwardsFacing(), false);
        direction = ModelFactory.modelFacing(frontFacing, direction);

        quads.add(
                shiftQuad(IHeaterRenderer.bakeQuad(
                        Wrapper.layer0,
                        1F,
                        direction,
                        Wrapper.BG_OVERLAY,
                        modelState,
                        false,
                        -1,
                        false)));
        quads.add(
                shiftQuad(IHeaterRenderer.bakeQuad(
                        Wrapper.layer1,
                        tempRatio,
                        direction,
                        Wrapper.TEMP_OVERLAY,
                        modelState,
                        true,
                        -1,
                        false)));
        quads.add(
                shiftQuad(IHeaterRenderer.bakeQuad(
                        Wrapper.layer2,
                        1f,
                        direction,
                        Wrapper.METER_OVERLAY,
                        modelState,
                        false,
                        -1,
                        false)));

        var waterState = distillation.getWaterState();
        var waterLevel = Mth.clamp((float) distillation.getWaterLevel() / (float) PrimitiveDistillationTowerMachine.getMaxWaterUsage(), 0f, 1f);
        var waterLayer = waterState.overlay;
        quads.add(
                shiftQuad(IHeaterRenderer.bakeQuad(
                        Wrapper.layer0,
                        1f,
                        direction,
                        TANK_BG_OVERLAY,
                        modelState,
                        false,
                        -1,
                        false)));
        if (waterLayer != null) {
            quads.add(
                    shiftQuad(IHeaterRenderer.bakeQuad(
                            Wrapper.layer1,
                            waterState == PrimitiveDistillationTowerMachine.WaterState.IS_COOLING ? 1f : waterLevel,
                            direction,
                            waterLayer,
                            modelState,
                            true,
                            -1,
                            false)));
        }
        quads.add(
                shiftQuad(IHeaterRenderer.bakeQuad(
                        Wrapper.layer2,
                        1f,
                        direction,
                        TANK_OVERLAY,
                        modelState,
                        false,
                        -1,
                        false)));
    }

    private static BakedQuad shiftQuad(BakedQuad quad) {
        Quad shiftedQuad = Quad.from(quad);
        for (int i = 0; i < 4; i++) {
            var pos = shiftedQuad.getVert(i);
            shiftedQuad = shiftedQuad.withVert(i, new Vector3f(pos.x(), pos.y() + 1, pos.z()));
        }
        return shiftedQuad.rebake();
    }
}
