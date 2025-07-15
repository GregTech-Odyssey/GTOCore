package com.gtocore.common.block;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import org.jetbrains.annotations.NotNull;

public final class CleanroomFilterType implements IFilterType {

    public static final CleanroomFilterType FILTER_CASING_LAW = new CleanroomFilterType("law_filter_casing", GTOCleanroomType.LAW_CLEANROOM);
    private final String name;
    private final CleanroomType cleanroomType;

    private CleanroomFilterType(String name, CleanroomType cleanroomType) {
        this.name = name;
        this.cleanroomType = cleanroomType;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return name;
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public @NotNull CleanroomType getCleanroomType() {
        return this.cleanroomType;
    }
}
