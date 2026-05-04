package com.gtocore.api.accelerator.particle;

import java.util.Objects;

/**
 * 粒子在emi中显示其基本数据
 * 如：质量，电荷，粒子宽度等
 *
 */
public final class ParticleDefinition {

    private final String name;
    private final double mass;
    private final double charge;
    private final double width;

    /**
     * @param mass   质量，单位eV/c^2
     * @param charge 电荷，单位e
     * @param width  粒子宽度，单位eV
     */
    public ParticleDefinition(String name, double mass, double charge, double width) {
        this.name = name;
        this.mass = mass;
        this.charge = charge;
        this.width = width;
    }

    public String name() {
        return name;
    }

    public double mass() {
        return mass;
    }

    public double charge() {
        return charge;
    }

    public double width() {
        return width;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ParticleDefinition) obj;
        return Objects.equals(this.name, that.name) &&
                Double.doubleToLongBits(this.mass) == Double.doubleToLongBits(that.mass) &&
                Double.doubleToLongBits(this.charge) == Double.doubleToLongBits(that.charge) &&
                Double.doubleToLongBits(this.width) == Double.doubleToLongBits(that.width);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mass, charge, width);
    }

    @Override
    public String toString() {
        return "ParticleDefinition[" +
                "name=" + name + ", " +
                "mass=" + mass + ", " +
                "charge=" + charge + ", " +
                "width=" + width + ']';
    }
}
