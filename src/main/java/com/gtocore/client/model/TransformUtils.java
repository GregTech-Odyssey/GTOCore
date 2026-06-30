package com.gtocore.client.model;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.item.ItemDisplayContext;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public final class TransformUtils {

    public static final PerspectiveModelState IDENTITY = PerspectiveModelState.IDENTITY;
    public static final PerspectiveModelState DEFAULT_ITEM;

    private static final Transformation FLIP_X = new Transformation(null, null, new Vector3f(-1.0F, 1.0F, 1.0F), null);

    static {
        Map<ItemDisplayContext, Transformation> transforms = new HashMap<>();
        Transformation thirdPerson = create(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
        Transformation firstPerson = create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
        transforms.put(ItemDisplayContext.GROUND, create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        transforms.put(ItemDisplayContext.HEAD, create(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        transforms.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        transforms.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flipLeft(thirdPerson));
        transforms.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, firstPerson);
        transforms.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, flipLeft(firstPerson));
        DEFAULT_ITEM = new PerspectiveModelState(ImmutableMap.copyOf(transforms));
    }

    private TransformUtils() {}

    public static Transformation create(float tx, float ty, float tz, float rx, float ry, float rz, float scale) {
        return create(
                new Vector3f(tx / 16.0F, ty / 16.0F, tz / 16.0F),
                new Vector3f(rx, ry, rz),
                new Vector3f(scale, scale, scale));
    }

    public static Transformation create(Vector3f translation, Vector3f rotation, Vector3f scale) {
        return new Transformation(
                translation,
                new Quaternionf().rotationXYZ(
                        (float) Math.toRadians(rotation.x()),
                        (float) Math.toRadians(rotation.y()),
                        (float) Math.toRadians(rotation.z())),
                scale,
                null);
    }

    public static Transformation create(ItemTransform transform) {
        if (ItemTransform.NO_TRANSFORM.equals(transform)) {
            return Transformation.identity();
        }
        return create(transform.translation, transform.rotation, transform.scale);
    }

    public static Transformation flipLeft(Transformation transform) {
        return FLIP_X.compose(transform).compose(FLIP_X);
    }

    public static ModelState stateFromItemTransforms(ItemTransforms transforms) {
        if (transforms == ItemTransforms.NO_TRANSFORMS) {
            return IDENTITY;
        }
        ImmutableMap.Builder<ItemDisplayContext, Transformation> builder = ImmutableMap.builder();
        for (ItemDisplayContext context : ItemDisplayContext.values()) {
            builder.put(context, create(transforms.getTransform(context)));
        }
        return new PerspectiveModelState(builder.build());
    }
}
