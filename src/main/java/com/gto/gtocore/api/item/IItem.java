package com.gto.gtocore.api.item;

import net.minecraft.resources.ResourceLocation;

public interface IItem {

    ResourceLocation gtocore$getIdLocation();

    default String gtocore$getId() {
        return gtocore$getIdLocation().toString();
    }
}
