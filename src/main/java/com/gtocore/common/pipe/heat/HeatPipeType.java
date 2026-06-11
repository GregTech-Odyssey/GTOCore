package com.gtocore.common.pipe.heat;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.pipenet.IPipeType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum HeatPipeType implements IPipeType<HeatPipeProperties>, StringRepresentable {

    NORMAL("普通", GTMaterials.Copper);

    public static final ResourceLocation TYPE = GTCEu.id("heat");

    public final String cnName;
    public final Material material;

    HeatPipeType(String cnName, Material material) {
        this.cnName = cnName;
        this.material = material;
    }

    @Override
    public float getThickness() {
        return 0.375F;
    }

    @Override
    public HeatPipeProperties modifyProperties(HeatPipeProperties baseProperties) {
        return baseProperties;
    }

    @Override
    public ResourceLocation type() {
        return TYPE;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
