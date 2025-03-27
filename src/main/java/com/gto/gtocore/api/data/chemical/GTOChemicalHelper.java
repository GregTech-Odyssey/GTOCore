package com.gto.gtocore.api.data.chemical;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper.getItems;

public interface GTOChemicalHelper {

    static Item getItem(MaterialEntry MaterialEntry) {
        var list = getItems(MaterialEntry);
        if (list.isEmpty()) return Items.AIR;
        return list.get(0).asItem();
    }

    static Item getItem(TagPrefix tagPrefix, @NotNull Material material) {
        return getItem(new MaterialEntry(tagPrefix, material));
    }
}
