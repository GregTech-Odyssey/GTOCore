package com.gtocore.client.renderer.machine;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.feature.ITemperatureMachine;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.util.StaticFaceBakery;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public interface IHeaterRenderer {

    @Contract("_, _, _, _, _, _, _, _ -> new")
    static @NotNull BakedQuad bakeQuad(
                                       AABB layer,
                                       float renderHeight,
                                       final Direction face,
                                       ResourceLocation texture,
                                       ModelState modelState,
                                       boolean isEmissive,
                                       int tint,
                                       boolean mirror

    ) {
        return StaticFaceBakery.bakeQuad(
                new Vector3f((float) layer.minX * 16.0F, (float) layer.minY * 16.0F, (float) layer.minZ * 16.0F),
                new Vector3f((float) layer.maxX * 16.0F, (float) layer.maxY * 16.0F * renderHeight, (float) layer.maxZ * 16.0F),
                new BlockElementFace(null, tint, "", new BlockFaceUV(getTempUV(renderHeight, mirror), 0)),
                ModelFactory.getBlockSprite(texture),
                face,
                modelState,
                null,
                true, isEmissive ? 15 : 0);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static float @NotNull [] getTempUV(float tempRatio, boolean mirror) {
        if (mirror) {
            return new float[] { 16.0F, 15f - tempRatio * 14f, 0.0F, 16.0F };
        }
        return new float[] { 0.0F, 15f - tempRatio * 14f, 16.0F, 16.0F };
    }

    default void renderHeater(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, Direction modelFacing, ModelState modelState) {
        if (!(machine instanceof ITemperatureMachine heater) || side == null) {
            return;
        }
        int heatLevel = heater.getSignal(side);
        var temp = heater.getTemperature();
        var maxTemp = heater.getMaxTemperature();
        float tempRatio = Math.min(1.0f, (float) temp / maxTemp);

        var leftFacing = RelativeDirection.LEFT.getRelative(machine.getFrontFacing(), machine.getUpwardsFacing(), false);
        var rightFacing = RelativeDirection.RIGHT.getRelative(machine.getFrontFacing(), machine.getUpwardsFacing(), false);
        leftFacing = ModelFactory.modelFacing(frontFacing, leftFacing);
        rightFacing = ModelFactory.modelFacing(frontFacing, rightFacing);

        for (Direction direction : new Direction[] { leftFacing, rightFacing }) {
            boolean mirror = direction == rightFacing;
            quads.add(
                    bakeQuad(
                            Wrapper.layer0,
                            1F,
                            direction,
                            Wrapper.BG_OVERLAY,
                            modelState,
                            false,
                            HeaterRenderer.TemperatureLevel.getTintByTemperature(heatLevel),
                            mirror));
            quads.add(
                    bakeQuad(
                            Wrapper.layer1,
                            tempRatio,
                            direction,
                            Wrapper.TEMP_OVERLAY,
                            modelState,
                            true,
                            -1,
                            mirror));
            quads.add(
                    bakeQuad(
                            Wrapper.layer2,
                            1F,
                            direction,
                            Wrapper.METER_OVERLAY,
                            modelState,
                            false,
                            -1,
                            mirror));
        }
    }

    enum TemperatureLevel {

        COLD(0, 5, 0xffffff),
        WARM(6, 9, 0xf0df9b),
        HOT(10, 12, 0xffaf59),
        VERY_HOT(13, 15, 0xff9359);

        private final int minLevel;
        private final int maxLevel;
        private final int tint;

        TemperatureLevel(int minLevel, int maxLevel, int tint) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.tint = tint;
        }

        static int getTintByTemperature(int temperature) {
            for (TemperatureLevel level : values()) {
                if (temperature >= level.minLevel && temperature <= level.maxLevel) {
                    return level.tint;
                }
            }
            return COLD.tint; // Default to COLD if no match found
        }
    }

    final class Wrapper {

        private static final float DEFLATE_Y = 1 / 16f;

        static final ResourceLocation TEMP_OVERLAY = GTOCore.id("block/machines/heater/temp");
        static final ResourceLocation METER_OVERLAY = GTOCore.id("block/machines/heater/meter");
        static final ResourceLocation BG_OVERLAY = GTOCore.id("block/machines/heater/bg");
        static final AABB layer0 = StaticFaceBakery.SLIGHTLY_OVER_BLOCK.deflate(0, DEFLATE_Y, 0).inflate(1e-5f);
        static final AABB layer1 = layer0.inflate(0.0005f);
        static final AABB layer2 = layer0.inflate(0.001f);
    }
}
