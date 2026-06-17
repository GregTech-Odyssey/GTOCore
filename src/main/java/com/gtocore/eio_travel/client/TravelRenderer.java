package com.gtocore.eio_travel.client;

import com.gtocore.eio_travel.api.ITravelTarget;

import net.minecraft.client.renderer.LevelRenderer;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TravelRenderer<T extends ITravelTarget> {

    void render(T travelData, LevelRenderer levelRenderer, PoseStack poseStack, double distanceSquared, boolean active);
}
