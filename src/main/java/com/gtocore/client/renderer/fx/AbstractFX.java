package com.gtocore.client.renderer.fx;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.joml.Matrix4f;

@Getter
public abstract class AbstractFX {

    private boolean discarded;
    protected int age;

    public AbstractFX() {}

    @MustBeInvokedByOverriders
    public void tick() {
        age++;
    }

    public boolean shouldDiscard() {
        return false;
    }

    protected void onDiscard() {
        this.discarded = true;
    }

    public void render(RenderLevelStageEvent.Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, Frustum frustum) {}
}
