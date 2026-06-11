package com.gtocore.common.pipe.mana;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.pipenet.IPipeType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ManaPipeType implements IPipeType<ManaPipeProperties>, StringRepresentable {

    NORMAL("普通", GTOMaterials.Manasteel);

    public static final ResourceLocation TYPE = GTCEu.id("mana");

    public final String cnName;
    public final Material material;

    ManaPipeType(String cnName, Material material) {
        this.cnName = cnName;
        this.material = material;
    }

    @Override
    public float getThickness() {
        return 0.375F;
    }

    @Override
    public ManaPipeProperties modifyProperties(ManaPipeProperties baseProperties) {
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
