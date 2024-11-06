package com.gto.gtocore.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TurbineModel extends Model {

    private static final Set<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);

    private final ModelPart base;

    public TurbineModel() {
        super(RenderType::entityCutoutNoCull);

        ModelPart.Cube[] baseCuboids = {
                new ModelPart.Cube(0, 0, -2.0F, -2.0F, -1.0F, 4F, 4F, 2F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS),
                new ModelPart.Cube(0, 6, -1.0F, -1.0F, -2.0F, 2F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
        };

        base = new ModelPart(Arrays.asList(baseCuboids), new HashMap<>() {

            {
                ModelPart.Cube[] blade1Cuboids = {
                        new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
                };
                ModelPart blade1 = new ModelPart(Arrays.asList(blade1Cuboids), Collections.emptyMap());
                blade1.setPos(0.0F, 0.0F, 0.0F);
                setRotation(blade1, 0.0F);
                put("blade1", blade1);

                ModelPart.Cube[] blade2Cuboids = {
                        new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
                };
                ModelPart blade2 = new ModelPart(Arrays.asList(blade2Cuboids), Collections.emptyMap());
                blade2.setPos(0.0F, 0.0F, 0.0F);
                setRotation(blade2, 2.0944F);
                put("blade2", blade2);

                ModelPart.Cube[] blade3Cuboids = {
                        new ModelPart.Cube(0, 9, -24.0F, -2.0F, -1.075F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
                };
                ModelPart blade3 = new ModelPart(Arrays.asList(blade3Cuboids), Collections.emptyMap());
                blade3.setPos(0.0F, 0.0F, 0.0F);
                setRotation(blade3, -2.0944F);
                put("blade3", blade3);
            }
        });
        base.setPos(0.0F, 24.0F, 0.0F);
    }

    private void setRotation(ModelPart model, float z) {
        model.xRot = (float) -0.5236;
        model.yRot = (float) 0.0;
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
