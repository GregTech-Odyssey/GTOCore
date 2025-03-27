package com.gto.gtocore.api.recipe;

import com.gto.gtocore.utils.FluidUtils;

import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

public final class MapFluid extends AbstractMapIngredient {

    private final Fluid f;
    private final CompoundTag t;

    public MapFluid(Fluid fluid, CompoundTag tag) {
        this.f = fluid;
        this.t = tag;
    }

    @Override
    protected int hash() {
        int hash = 31 + FluidUtils.getIdLocation(f).hashCode();
        if (t != null) {
            return 31 * hash + t.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            MapFluid other = (MapFluid) o;
            if (this.f.isSame(other.f)) {
                return Objects.equals(t, other.t);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MapFluid{" + "{f=" + FluidUtils.getIdLocation(f) + "} {t=" + t + "}";
    }
}
