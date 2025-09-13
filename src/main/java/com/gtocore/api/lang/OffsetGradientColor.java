package com.gtocore.api.lang;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import dev.shadowsoffire.placebo.PlaceboClient;
import dev.shadowsoffire.placebo.color.GradientColor;

public class OffsetGradientColor extends GradientColor {

    protected final int offset;
    protected final boolean reverse;

    public OffsetGradientColor(int[] gradient, String id, float speed, int offset, boolean reverse) {
        super(gradient, id, speed);
        this.offset = offset;
        this.reverse = reverse;
    }

    public OffsetGradientColor(int[] gradient, String id, int offset) {
        this(gradient, id, 1, offset, false);
    }

    @Override
    public int getValue() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            int index = (int) ((PlaceboClient.getColorTicks() * speed + offset) % this.gradient.length);
            if (reverse) {
                return this.gradient[this.gradient.length - 1 - index];
            } else {
                return this.gradient[index];
            }
        }
        return super.getValue();
    }
}
