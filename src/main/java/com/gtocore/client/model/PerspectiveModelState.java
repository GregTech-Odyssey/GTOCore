package com.gtocore.client.model;

import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.item.ItemDisplayContext;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;

import java.util.Map;

public class PerspectiveModelState implements ModelState {

    public static final PerspectiveModelState IDENTITY = new PerspectiveModelState(ImmutableMap.of());

    private final Map<ItemDisplayContext, Transformation> transforms;
    private final boolean uvLocked;

    public PerspectiveModelState(Map<ItemDisplayContext, Transformation> transforms) {
        this(transforms, false);
    }

    public PerspectiveModelState(Map<ItemDisplayContext, Transformation> transforms, boolean uvLocked) {
        this.transforms = ImmutableMap.copyOf(transforms);
        this.uvLocked = uvLocked;
    }

    public Transformation getTransform(ItemDisplayContext transformType) {
        return transforms.getOrDefault(transformType, Transformation.identity());
    }

    @Override
    public Transformation getRotation() {
        return Transformation.identity();
    }

    @Override
    public boolean isUvLocked() {
        return uvLocked;
    }
}
