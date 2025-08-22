package com.gtocore.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public final class TurbineModel extends Model {

    private static final Set<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);

    private final ModelPart base;

    public TurbineModel() {
        super(RenderType::entityCutoutNoCull);

        ModelPart.Cube[] baseCuboids = {
                new ModelPart.Cube(0, 0, -2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, 64.0F, 64.0F, ALL_DIRECTIONS),
                new ModelPart.Cube(0, 6, -1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, 64.0F, 64.0F, ALL_DIRECTIONS)
        };

        ImmutableMap.Builder<String, ModelPart> map = ImmutableMap.builder();
        ModelPart.Cube[] blade1Cuboids = { new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, 64.0F, 64.0F, ALL_DIRECTIONS) };
        ModelPart blade1 = new ModelPart(Arrays.asList(blade1Cuboids), Collections.emptyMap());
        blade1.setPos(0.0F, 0.0F, 0.0F);
        setRotation(blade1, 0.0F);
        map.put("blade1", blade1);
        ModelPart.Cube[] blade2Cuboids = { new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, 64.0F, 64.0F, ALL_DIRECTIONS) };
        ModelPart blade2 = new ModelPart(Arrays.asList(blade2Cuboids), Collections.emptyMap());
        blade2.setPos(0.0F, 0.0F, 0.0F);
        setRotation(blade2, 2.0944F);
        map.put("blade2", blade2);

        ModelPart.Cube[] blade3Cuboids = { new ModelPart.Cube(0, 9, -24.0F, -2.0F, -1.075F, 24.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, 64.0F, 64.0F, ALL_DIRECTIONS) };
        ModelPart blade3 = new ModelPart(Arrays.asList(blade3Cuboids), Collections.emptyMap());
        blade3.setPos(0.0F, 0.0F, 0.0F);
        setRotation(blade3, -2.0944F);
        map.put("blade3", blade3);

        base = new ModelPart(Arrays.asList(baseCuboids), map.build());
        base.setPos(0.0F, 24.0F, 0.0F);
    }

    private static void setRotation(ModelPart model, float z) {
        model.xRot = -0.5236f;
        model.yRot = 0.0f;
        model.zRot = z;
    }

    public void setSpin(float z) {
        base.zRot = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrixStack, vertexConsumer, light, overlay);
    }
}
