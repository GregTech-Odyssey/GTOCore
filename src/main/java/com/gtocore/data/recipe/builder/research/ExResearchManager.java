package com.gtocore.data.recipe.builder.research;

import com.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class ExResearchManager {

    private static final int FNV_OFFSET_BASIS = 0x811C9DC5;
    private static final int FNV_PRIME = 0x01000193;

    public static final Int2ObjectMap<Item> DataCrystalMap = new Int2ObjectOpenHashMap<>();
    static {
        DataCrystalMap.put(1, GTOItems.DATA_CRYSTAL_MK1.asItem());
        DataCrystalMap.put(2, GTOItems.DATA_CRYSTAL_MK2.asItem());
        DataCrystalMap.put(3, GTOItems.DATA_CRYSTAL_MK3.asItem());
        DataCrystalMap.put(4, GTOItems.DATA_CRYSTAL_MK4.asItem());
        DataCrystalMap.put(5, GTOItems.DATA_CRYSTAL_MK5.asItem());
    }

    public static final Int2IntMap ErrorDataMap = new Int2IntOpenHashMap();
    static {
        ErrorDataMap.put(1, 0x38181C20);
        ErrorDataMap.put(2, 0x3B1820D9);
        ErrorDataMap.put(3, 0x3A181F46);
        ErrorDataMap.put(4, 0x3D1823FF);
        ErrorDataMap.put(5, 0x3C18226C);
    }

    public static final Int2ObjectMap<Item> DataItemMap = new Int2ObjectOpenHashMap<>();
    static {
        DataItemMap.put(1, GTItems.TOOL_DATA_STICK.get());
        DataItemMap.put(2, GTItems.TOOL_DATA_ORB.asItem());
        DataItemMap.put(3, GTItems.TOOL_DATA_MODULE.asItem());
        DataItemMap.put(4, GTOItems.NEURAL_MATRIX.asItem());
        DataItemMap.put(5, GTOItems.ATOMIC_ARCHIVES.asItem());
        DataItemMap.put(6, GTOItems.OBSIDIAN_MATRIX.asItem());
        DataItemMap.put(7, GTOItems.MICROCOSM.asItem());
    }

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

    public record DataCrystal(
                              String data,
                              int serial,
                              int tier,
                              int crystal) {}

    /**
     * 向物品NBT写入扫描数据（物品）
     */
    public static void writeScanningResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull ItemStack scanned, int dataTier, int dataCrystal) {
        CompoundTag compound = new CompoundTag();
        String scanningId = itemStackToString(scanned);
        int serial = generateSerialId(scanningId);
        compound.putString(SCANNING_ID_NBT_TAG, scanningId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        scanningMap.put(serial, new DataCrystal(scanningId, serial, dataTier, dataCrystal));
    }

    /**
     * 向物品NBT写入扫描数据（流体）
     */
    public static void writeScanningResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull FluidStack scanned, int dataTier, int dataCrystal) {
        CompoundTag compound = new CompoundTag();
        String scanningId = fluidStackToString(scanned);
        int serial = generateSerialId(scanningId);
        compound.putString(SCANNING_ID_NBT_TAG, scanningId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        scanningMap.put(serial, new DataCrystal(scanningId, serial, dataTier, dataCrystal));
    }

    /**
     * 写入分析数据到Map
     */
    public static void writeAnalyzeResearchToMap(String analyzeId, int dataTier, int dataCrystal) {
        int serial = generateSerialId(analyzeId);
        analyzeMap.put(serial, new DataCrystal(analyzeId, serial, dataTier, dataCrystal));
    }

    /**
     * 根据序列号生成带数据的晶体物品
     */
    public static ItemStack getDataCrystal(int serial) {
        DataCrystal analyzeData = analyzeMap.get(serial);
        if (analyzeData != null) {
            return createCrystalWithData(analyzeData, ANALYZE_NBT_TAG, ANALYZE_ID_NBT_TAG, ANALYZE_SERIAL_NBT_TAG);
        }
        DataCrystal scanningData = scanningMap.get(serial);
        if (scanningData != null) {
            return createCrystalWithData(scanningData, SCANNING_NBT_TAG, SCANNING_ID_NBT_TAG, SCANNING_SERIAL_NBT_TAG);
        }
        Item defaultCrystal = DataCrystalMap.getOrDefault(1, GTOItems.DATA_CRYSTAL_MK1.asItem());
        ItemStack unknownStack = defaultCrystal.getDefaultInstance();
        CompoundTag unknownTag = unknownStack.getOrCreateTag();
        CompoundTag dataTag = new CompoundTag();
        dataTag.putString(SCANNING_ID_NBT_TAG, "§m unknown §r");
        dataTag.putInt(SCANNING_SERIAL_NBT_TAG, serial);
        unknownTag.put(SCANNING_NBT_TAG, dataTag);
        return unknownStack;
    }

    /**
     * 生成指定等级的空晶体
     */
    public static ItemStack getEmptyCrystal(int tier) {
        Item crystalItem = DataCrystalMap.getOrDefault(tier, GTOItems.DATA_CRYSTAL_MK1.asItem());
        ItemStack emptyStack = crystalItem.getDefaultInstance();
        CompoundTag stackTag = emptyStack.getOrCreateTag();
        CompoundTag emptyTag = new CompoundTag();
        emptyTag.putInt(EMPTY_ID_NBT_TAG, 0);
        stackTag.put(EMPTY_NBT_TAG, emptyTag);
        return emptyStack;
    }

    /**
     * 将物品栈转换为唯一字符串ID
     */
    public static String itemStackToString(@NotNull ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return stack.getCount() + "i-" + itemId.getNamespace() + "-" + itemId.getPath();
    }

    /**
     * 将流体栈转换为唯一字符串ID
     */
    public static String fluidStackToString(@NotNull FluidStack stack) {
        ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
        return stack.getAmount() + "f-" + fluidId.getNamespace() + "-" + fluidId.getPath();
    }

    /**
     * 生成序列号：仅基于dataId的FNV哈希（保证唯一性）
     */
    private static int generateSerialId(String dataId) {
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
    private static ItemStack createCrystalWithData(DataCrystal data, String nbtTag, String idTag, String serialTag) {
        Item crystalItem = DataCrystalMap.getOrDefault(data.crystal(), GTOItems.DATA_CRYSTAL_MK1.asItem());
        ItemStack crystalStack = crystalItem.getDefaultInstance();
        CompoundTag stackTag = crystalStack.getOrCreateTag();
        CompoundTag dataTag = new CompoundTag();
        dataTag.putString(idTag, data.data());
        dataTag.putInt(serialTag, data.serial());
        stackTag.put(nbtTag, dataTag);
        return crystalStack;
    }
}
