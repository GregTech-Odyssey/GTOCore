package com.gtocore.data.recipe.builder.research;

import com.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ExResearchManager {

    private static final int FNV_OFFSET_BASIS = 0x811C9DC5;
    private static final int FNV_PRIME = 0x01000193;

    public static final String SCANNING_NBT_TAG = "scanning_research";
    public static final String SCANNING_ID_NBT_TAG = "scanning_id";
    public static final String SCANNING_SERIAL_NBT_TAG = "scanning_serial";
    public static final Int2ObjectMap<DataCrystal> scanningMap = new Int2ObjectOpenHashMap<>();

    public static final String ANALYZE_NBT_TAG = "analyze_research";
    public static final String ANALYZE_ID_NBT_TAG = "analyze_id";
    public static final String ANALYZE_SERIAL_NBT_TAG = "analyze_serial";
    public static final Int2ObjectMap<DataCrystal> analyzeMap = new Int2ObjectOpenHashMap<>();
    static {
        analyzeMap.put(0, new DataCrystal("empty", 0, 0, 0));
    }

    public static final String EMPTY_NBT_TAG = "empty_crystal";
    public static final String EMPTY_ID_NBT_TAG = "empty_id";

    public static final List<ItemStack> DataCrystalList = new ArrayList<>();
    static {
        DataCrystalList.addFirst(Items.BARRIER.getDefaultInstance());
        DataCrystalList.add(1, GTOItems.DATA_CRYSTAL_MK1.asStack());
        DataCrystalList.add(2, GTOItems.DATA_CRYSTAL_MK2.asStack());
        DataCrystalList.add(3, GTOItems.DATA_CRYSTAL_MK3.asStack());
        DataCrystalList.add(4, GTOItems.DATA_CRYSTAL_MK4.asStack());
        DataCrystalList.add(5, GTOItems.DATA_CRYSTAL_MK5.asStack());
    }

    public static ItemStack getDataCrystalItem(int tier) {
        if (tier < 1 || tier > 5) throw new IllegalArgumentException("Invalid crystal tier: " + tier + " (must be 1-5)");
        return DataCrystalList.get(tier).copy();
    }

    public static final List<ItemStack> ErrorDataCrystalList = new ArrayList<>();
    static {
        ErrorDataCrystalList.addFirst(Items.BARRIER.getDefaultInstance());
        int[] errorDataCrystal = { 0x38181C20, 0x3B1820D9, 0x3A181F46, 0x3D1823FF, 0x3C18226C };
        for (int tier = 1; tier <= 5; tier++) {
            ItemStack emptyStack = getDataCrystalItem(tier);
            CompoundTag stackTag = emptyStack.getOrCreateTag();
            CompoundTag dataTag = new CompoundTag();
            dataTag.putString(ANALYZE_ID_NBT_TAG, "error" + tier);
            dataTag.putInt(ANALYZE_SERIAL_NBT_TAG, errorDataCrystal[tier - 1]);
            stackTag.put(ANALYZE_NBT_TAG, dataTag);
            ErrorDataCrystalList.add(tier, emptyStack);
        }
    }

    public static final List<ItemStack> EmptyDataCrystalList = new ArrayList<>();
    static {
        EmptyDataCrystalList.addFirst(Items.BARRIER.getDefaultInstance());
        for (int tier = 1; tier <= 5; tier++) {
            ItemStack emptyStack = getDataCrystalItem(tier);
            CompoundTag stackTag = emptyStack.getOrCreateTag();
            CompoundTag emptyTag = new CompoundTag();
            emptyTag.putInt(EMPTY_ID_NBT_TAG, 0);
            stackTag.put(EMPTY_NBT_TAG, emptyTag);
            EmptyDataCrystalList.add(tier, emptyStack);
        }
    }

    public static final List<Item> DataItemList = new ArrayList<>();
    static {
        DataItemList.addFirst(Items.BARRIER);
        DataItemList.add(1, GTItems.TOOL_DATA_STICK.asItem());
        DataItemList.add(2, GTItems.TOOL_DATA_ORB.asItem());
        DataItemList.add(3, GTItems.TOOL_DATA_MODULE.asItem());
        DataItemList.add(4, GTOItems.NEURAL_MATRIX.asItem());
        DataItemList.add(5, GTOItems.ATOMIC_ARCHIVES.asItem());
        DataItemList.add(6, GTOItems.OBSIDIAN_MATRIX.asItem());
        DataItemList.add(7, GTOItems.MICROCOSM.asItem());
    }

    public record DataCrystal(
                              String data,
                              int serial,
                              int tier,
                              int crystal) {}

    /**
     * 向物品NBT写入扫描数据
     */
    public static void writeScanningResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull Object scanned, int dataTier, int dataCrystal) {
        if (dataCrystal < 1 || dataCrystal > 5) throw new IllegalArgumentException("dataCrystal must be 1-5, got " + dataCrystal);
        if (!(scanned instanceof ItemStack) && !(scanned instanceof FluidStack)) throw new IllegalArgumentException("scanned must be ItemStack or FluidStack");
        String scanningId = (scanned instanceof ItemStack itemStack) ? itemStackToString(itemStack) : fluidStackToString((FluidStack) scanned);
        int serial = generateSerialId(scanningId);
        CompoundTag compound = new CompoundTag();
        compound.putString(SCANNING_ID_NBT_TAG, scanningId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        scanningMap.put(serial, new DataCrystal(scanningId, serial, dataTier, dataCrystal));
    }

    /**
     * 写入分析数据到Map
     */
    public static void writeAnalyzeResearchToMap(@NotNull String analyzeId, int dataTier, int dataCrystal) {
        if (dataCrystal < 1 || dataCrystal > 5) throw new IllegalArgumentException("dataCrystal must be 1-5, got " + dataCrystal);
        int serial = generateSerialId(analyzeId);
        analyzeMap.put(serial, new DataCrystal(analyzeId, serial, dataTier, dataCrystal));
    }

    /**
     * 根据序列号生成带数据的晶体物品
     */
    public static ItemStack getDataCrystal(int serial) {
        DataCrystal data;
        if ((data = analyzeMap.get(serial)) != null) return createCrystalWithData(data, ANALYZE_NBT_TAG, ANALYZE_ID_NBT_TAG, ANALYZE_SERIAL_NBT_TAG);
        if ((data = scanningMap.get(serial)) != null) return createCrystalWithData(data, SCANNING_NBT_TAG, SCANNING_ID_NBT_TAG, SCANNING_SERIAL_NBT_TAG);
        return Items.BARRIER.getDefaultInstance();
    }

    /**
     * 将物品栈转换为唯一字符串ID
     */
    public static String itemStackToString(@NotNull ItemStack stack) {
        return stack.getCount() + "i-" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString().replace(":", "-");
    }

    /**
     * 将流体栈转换为唯一字符串ID
     */
    public static String fluidStackToString(@NotNull FluidStack stack) {
        return stack.getAmount() + "f-" + Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(stack.getFluid())).toString().replace(":", "-");
    }

    /**
     * 生成序列号：仅基于dataId的FNV哈希（保证唯一性）
     */
    private static int generateSerialId(@NotNull String dataId) {
        int hash = FNV_OFFSET_BASIS;
        byte[] bytes = dataId.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            hash ^= (b & 0xFF);
            hash *= FNV_PRIME;
        }
        return hash;
    }

    /**
     * 辅助方法：根据DataCrystal生成带NBT数据的晶体物品
     */
    private static ItemStack createCrystalWithData(@NotNull DataCrystal data, String nbtTag, String idTag, String serialTag) {
        ItemStack crystalStack = getDataCrystalItem(data.crystal());
        CompoundTag stackTag = crystalStack.getOrCreateTag();
        CompoundTag dataTag = new CompoundTag();
        dataTag.putString(idTag, data.data());
        dataTag.putInt(serialTag, data.serial());
        stackTag.put(nbtTag, dataTag);
        return crystalStack;
    }
}
