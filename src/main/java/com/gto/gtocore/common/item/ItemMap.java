package com.gto.gtocore.common.item;

import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEItems;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.Arrays;
import java.util.Map;

public final class ItemMap {

    private static final Map<ItemStack, Integer> SCRAP_MAP = new Object2IntOpenHashMap<>();
    private static final int TOTAL_PROBABILITY;

    static {
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Endstone), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Magnalium), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Titanium), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Chromium), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Tungsten), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Platinum), 9);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Diamond), 15);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Emerald), 15);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Sapphire), 15);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.GreenSapphire), 15);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Ruby), 15);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Olivine), 15);
        SCRAP_MAP.put(Items.CAKE.getDefaultInstance(), 31);
        SCRAP_MAP.put(GTItems.ELECTRONIC_CIRCUIT_MV.asStack(), 62);
        SCRAP_MAP.put(AEItems.SILICON.stack(), 62);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.wireGtSingle, GTMaterials.Gold), 124);
        SCRAP_MAP.put(GTItems.ELECTRONIC_CIRCUIT_LV.asStack(), 124);
        SCRAP_MAP.put(Items.COOKED_CHICKEN.getDefaultInstance(), 124);
        SCRAP_MAP.put(Items.COOKED_BEEF.getDefaultInstance(), 124);
        SCRAP_MAP.put(Items.COOKED_PORKCHOP.getDefaultInstance(), 124);
        SCRAP_MAP.put(Items.CLAY.getDefaultInstance(), 422);
        SCRAP_MAP.put(Items.STICK.getDefaultInstance(), 1124);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.GarnetYellow), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.GarnetRed), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.gem, GTMaterials.GarnetSand), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Steel), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Copper), 374);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Brass), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Tin), 374);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Iron), 311);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Lead), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Aluminium), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Zinc), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Nickel), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Bauxite), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Electrum), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Silver), 155);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Redstone), 280);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Carbon), 249);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Glowstone), 249);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Lapis), 623);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Pyrite), 623);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Calcite), 623);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Obsidian), 467);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sulfur), 467);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Sodalite), 623);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Charcoal), 779);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Wood), 1184);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Flint), 1246);
        SCRAP_MAP.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Paper), 1184);
        SCRAP_MAP.put(GTOItems.SCRAP.asStack(), 2961);
    }

    private static final ItemStack[] SCRAP_ITEMS = new ItemStack[SCRAP_MAP.size()];
    private static final int[] CUMULATIVE_PROBABILITIES = new int[SCRAP_MAP.size()];

    static {
        int index = 0;
        int cumulativeProbability = 0;
        for (Map.Entry<ItemStack, Integer> entry : SCRAP_MAP.entrySet()) {
            SCRAP_ITEMS[index] = entry.getKey();
            cumulativeProbability += entry.getValue();
            CUMULATIVE_PROBABILITIES[index] = cumulativeProbability;
            index++;
        }
        TOTAL_PROBABILITY = cumulativeProbability;
        SCRAP_MAP.clear();
    }

    public static ItemStack getScrapItem() {
        int randomValue = GTValues.RNG.nextInt(TOTAL_PROBABILITY);
        int searchIndex = Arrays.binarySearch(CUMULATIVE_PROBABILITIES, randomValue);
        if (searchIndex < 0) {
            searchIndex = -(searchIndex + 1);
        }
        return SCRAP_ITEMS[searchIndex];
    }
}
