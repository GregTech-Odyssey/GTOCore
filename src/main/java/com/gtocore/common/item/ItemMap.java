package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.data.chemical.GTOChemicalHelper;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEItems;
import com.google.common.collect.ImmutableSet;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.Arrays;
import java.util.Objects;

public final class ItemMap {

    public static final ImmutableSet<Item> UNIVERSAL_CIRCUITS = ImmutableSet.copyOf(Arrays.stream(GTOItems.UNIVERSAL_CIRCUIT).filter(Objects::nonNull).map(RegistryEntry::get).toList());

    private static final Object2IntMap<Item> SCRAP_MAP = new Object2IntOpenHashMap<>();
    private static final int TOTAL_PROBABILITY;

    static {
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Endstone), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Magnalium), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Titanium), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Chromium), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Tungsten), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Platinum), 9);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.Diamond), 15);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.Emerald), 15);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.Sapphire), 15);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.GreenSapphire), 15);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.Ruby), 15);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.Olivine), 15);
        SCRAP_MAP.put(Items.CAKE, 31);
        SCRAP_MAP.put(GTItems.ELECTRONIC_CIRCUIT_MV.asItem(), 62);
        SCRAP_MAP.put(AEItems.SILICON.asItem(), 62);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.wireGtSingle, GTMaterials.Gold), 124);
        SCRAP_MAP.put(GTItems.ELECTRONIC_CIRCUIT_LV.asItem(), 124);
        SCRAP_MAP.put(Items.COOKED_CHICKEN, 124);
        SCRAP_MAP.put(Items.COOKED_BEEF, 124);
        SCRAP_MAP.put(Items.COOKED_PORKCHOP, 124);
        SCRAP_MAP.put(Items.CLAY, 422);
        SCRAP_MAP.put(Items.STICK, 1124);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.GarnetYellow), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.GarnetRed), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.gem, GTMaterials.GarnetSand), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Steel), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Copper), 374);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Brass), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Tin), 374);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Iron), 311);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Lead), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Aluminium), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Zinc), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Nickel), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Bauxite), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Electrum), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Silver), 155);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Redstone), 280);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Carbon), 249);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Glowstone), 249);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Lapis), 623);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Pyrite), 623);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Calcite), 623);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Obsidian), 467);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Sulfur), 467);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Sodalite), 623);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Charcoal), 779);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Wood), 1184);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Flint), 1246);
        SCRAP_MAP.put(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Paper), 1184);
        SCRAP_MAP.put(GTOItems.SCRAP.asItem(), 2961);
    }

    private static final Item[] SCRAP_ITEMS = new Item[SCRAP_MAP.size()];
    private static final int[] CUMULATIVE_PROBABILITIES = new int[SCRAP_MAP.size()];

    static {
        int index = 0;
        int cumulativeProbability = 0;
        for (Object2IntMap.Entry<Item> entry : SCRAP_MAP.object2IntEntrySet()) {
            SCRAP_ITEMS[index] = entry.getKey();
            cumulativeProbability += entry.getIntValue();
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
        return SCRAP_ITEMS[searchIndex].getDefaultInstance();
    }
}
