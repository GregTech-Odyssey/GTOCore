package com.gto.gtocore.common.block;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.block.CoilBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum CoilType implements StringRepresentable, ICoilType {

    ABYSSALALLOY("abyssalalloy", "渊狱合金", 12600, 16, 8, GTOMaterials.AbyssalAlloy, GTOCore.id("block/coil/abyssalalloy_coil_block")),
    TITANSTEEL("titansteel", "泰坦钢", 14400, 32, 8, GTOMaterials.TitanSteel, GTOCore.id("block/coil/titansteel_coil_block")),
    ADAMANTINE("adamantine", "精金", 16200, 32, 8, GTOMaterials.Adamantine, GTOCore.id("block/coil/adamantine_coil_block")),
    NAQUADRIATICTARANIUM("naquadriatictaranium", "超能硅岩-塔兰金属合金", 18900, 64, 8, GTOMaterials.NaquadriaticTaranium, GTOCore.id("block/coil/naquadriatictaranium_coil_block")),
    STARMETAL("starmetal", "星辉", 21600, 64, 8, GTOMaterials.Starmetal, GTOCore.id("block/coil/starmetal_coil_block")),
    INFINITY("infinity", "无尽", 36000, 128, 9, GTOMaterials.Infinity, GTOCore.id("block/coil/infinity_coil_block")),
    HYPOGEN("hypogen", "海珀珍", 62000, 256, 9, GTOMaterials.Hypogen, GTOCore.id("block/coil/hypogen_coil_block")),
    ETERNITY("eternity", "永恒", 96000, 512, 9, GTOMaterials.Eternity, GTOCore.id("block/coil/eternity_coil_block")),
    URUIUM("uruium", "超级热熔", 273, 1, 1, GTOMaterials.Uruium, GTOCore.id("block/coil/uruium_coil_block"));

    private final String cnLang;
    private final String name;
    private final int coilTemperature;
    private final int level;
    private final int energyDiscount;
    private final Material material;
    private final ResourceLocation texture;

    CoilType(String name, String cn, int coilTemperature, int level, int energyDiscount, Material material, ResourceLocation texture) {
        this.cnLang = cn;
        this.name = name;
        this.coilTemperature = coilTemperature;
        this.level = level;
        this.energyDiscount = energyDiscount;
        this.material = material;
        this.texture = texture;
    }

    @Override
    public int getTier() {
        return ordinal() + CoilBlock.CoilType.values().length;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }
}
