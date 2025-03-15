package com.gto.gtocore.api.data.chemical.material;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;

import net.minecraft.world.item.Rarity;

public interface GTOMaterial {

    MaterialProperties gtocore$getProperties();

    Rarity gtocore$rarity();

    void gtocore$setRarity(Rarity rarity);

    boolean gtocore$glow();

    void gtocore$setGlow();

    int gtocore$temp();

    void gtocore$setTemp(int temp);
}
