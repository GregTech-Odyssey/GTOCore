package com.gto.gtocore.api.data.chemical.material;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;

import net.minecraft.world.item.Rarity;

public interface GTOMaterial {

    Rarity gtocore$rarity();

    void gtocore$setRarity(Rarity rarity);

    MaterialProperties gtocore$getProperties();

    int gtocore$temp();

    void gtocore$setTemp(int temp);
}
