package com.gto.gtocore.api.item;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public interface IItem {

    @NotNull
    ResourceLocation gtocore$getIdLocation();

    default String gtocore$getId() {
        return gtocore$getIdLocation().toString();
    }
}
