package com.gto.gtocore.api.data.chemical.material;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;

import net.minecraft.world.item.Rarity;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GTOMaterialBuilder extends Material.Builder {

    private Rarity rarity;

    private boolean glow;

    private int temp;

    public GTOMaterialBuilder(String name) {
        super(GTOCore.id(name));
    }

    @Override
    public GTOMaterialBuilder wood() {
        return (GTOMaterialBuilder) super.wood();
    }

    @Override
    public GTOMaterialBuilder fluid() {
        return (GTOMaterialBuilder) super.fluid();
    }

    @Override
    public GTOMaterialBuilder liquid() {
        return (GTOMaterialBuilder) super.liquid();
    }

    @Override
    public GTOMaterialBuilder plasma() {
        return (GTOMaterialBuilder) super.plasma();
    }

    @Override
    public GTOMaterialBuilder gas() {
        return (GTOMaterialBuilder) super.gas();
    }

    @Override
    public GTOMaterialBuilder dust() {
        return (GTOMaterialBuilder) super.dust();
    }

    @Override
    public GTOMaterialBuilder gem() {
        return (GTOMaterialBuilder) super.gem();
    }

    @Override
    public GTOMaterialBuilder ingot() {
        return (GTOMaterialBuilder) super.ingot();
    }

    @Override
    public GTOMaterialBuilder polymer() {
        return (GTOMaterialBuilder) super.polymer();
    }

    @Override
    public GTOMaterialBuilder ore() {
        return (GTOMaterialBuilder) super.ore();
    }

    @Override
    public GTOMaterialBuilder addOreByproducts(Material... byproducts) {
        return (GTOMaterialBuilder) super.addOreByproducts(byproducts);
    }

    @Override
    public GTOMaterialBuilder color(int color) {
        return (GTOMaterialBuilder) super.color(color);
    }

    @Override
    public GTOMaterialBuilder secondaryColor(int color) {
        return (GTOMaterialBuilder) super.secondaryColor(color);
    }

    @Override
    public GTOMaterialBuilder iconSet(MaterialIconSet iconSet) {
        return (GTOMaterialBuilder) super.iconSet(iconSet);
    }

    @Override
    public GTOMaterialBuilder components(Object... components) {
        return (GTOMaterialBuilder) super.components(components);
    }

    @Override
    public GTOMaterialBuilder flags(MaterialFlag... flags) {
        return (GTOMaterialBuilder) super.flags(flags);
    }

    @Override
    public GTOMaterialBuilder element(Element element) {
        return (GTOMaterialBuilder) super.element(element);
    }

    @Override
    public GTOMaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTie) {
        return (GTOMaterialBuilder) super.blastTemp(temp, gasTie);
    }

    @Override
    public GTOMaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTie, int eutOverride) {
        return (GTOMaterialBuilder) super.blastTemp(temp, gasTie, eutOverride);
    }

    @Override
    public GTOMaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTie, int eutOverride, int durationOverride) {
        return (GTOMaterialBuilder) super.blastTemp(temp, gasTie, eutOverride, durationOverride);
    }

    @Override
    public GTOMaterialBuilder blastTemp(int temp) {
        return (GTOMaterialBuilder) super.blast(temp);
    }

    @Override
    public GTOMaterialBuilder cableProperties(long voltage, int amperage, int loss, boolean isSuperCon) {
        return (GTOMaterialBuilder) super.cableProperties(voltage, amperage, loss, isSuperCon);
    }

    @Override
    public Material buildAndRegister() {
        Material mat = super.buildAndRegister();
        if (mat instanceof GTOMaterial material) {
            if (rarity != null) material.gtocore$setRarity(rarity);
            if (glow) material.gtocore$setGlow();
            if (temp > 0) material.gtocore$setTemp(temp);
        }
        return mat;
    }
}
