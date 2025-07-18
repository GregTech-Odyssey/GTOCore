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

    public static final boolean SCANNING_DEBUG = false;

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
        ErrorDataMap.put(1, 0x01181C20);
        ErrorDataMap.put(2, 0x021820D9);
        ErrorDataMap.put(3, 0x03181F46);
        ErrorDataMap.put(4, 0x041823FF);
        ErrorDataMap.put(5, 0x0518226C);
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
    public static final Int2ObjectMap<String> ScanningMap = new Int2ObjectOpenHashMap<>();
    static {
        ScanningMap.defaultReturnValue(null);
    }

    public static final String ANALYZE_NBT_TAG = "analyze_research";
    public static final String ANALYZE_ID_NBT_TAG = "analyze_id";
    public static final String ANALYZE_SERIAL_NBT_TAG = "analyze_serial";
    public static final Int2ObjectMap<String> AnalyzeMap = new Int2ObjectOpenHashMap<>();
    static {
        AnalyzeMap.defaultReturnValue(null);
        AnalyzeMap.put(0, "empty");
    }

    public static final String EMPTY_NBT_TAG = "empty_crystal";
    public static final String EMPTY_ID_NBT_TAG = "empty_id";

    public static void writeScanningResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull ItemStack Scanned, int dataTier, int dataCrystal) {
        CompoundTag compound = new CompoundTag();
        String ScanningId = itemStackToString(Scanned);
        int Serial = generateSerialId(ScanningId, dataTier, dataCrystal);
        compound.putString(SCANNING_ID_NBT_TAG, ScanningId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, Serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        ScanningMap.put(Serial, ScanningId);
    }

    public static void writeScanningResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull FluidStack Scanned, int dataTier, int dataCrystal) {
        CompoundTag compound = new CompoundTag();
        String ScanningId = fluidStackToString(Scanned);
        int Serial = generateSerialId(ScanningId, dataTier, dataCrystal);
        compound.putString(SCANNING_ID_NBT_TAG, ScanningId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, Serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        ScanningMap.put(Serial, ScanningId);
    }

    public static ItemStack getScanningData(int Serial) {
        var data = DataCrystalMap.get(ExtractDataCrystal(Serial)).getDefaultInstance();
        CompoundTag stackCompound = data.getOrCreateTag();
        String AnalyzeId = ScanningMap.get(Serial);
        CompoundTag compound = new CompoundTag();
        compound.putString(SCANNING_ID_NBT_TAG, AnalyzeId);
        compound.putInt(SCANNING_SERIAL_NBT_TAG, Serial);
        stackCompound.put(SCANNING_NBT_TAG, compound);
        return data;
    }

    public static void writeAnalyzeResearchToMap(String AnalyzeId, int dataTier, int dataCrystal) {
        int Serial = generateSerialId(AnalyzeId, dataTier, dataCrystal);
        AnalyzeMap.put(Serial, AnalyzeId);
    }

    public static ItemStack getAnalyzeData(int Serial) {
        var data = DataCrystalMap.get(ExtractDataCrystal(Serial)).getDefaultInstance();
        CompoundTag stackCompound = data.getOrCreateTag();
        String AnalyzeId = AnalyzeMap.get(Serial);
        CompoundTag compound = new CompoundTag();
        compound.putString(ANALYZE_ID_NBT_TAG, AnalyzeId);
        compound.putInt(ANALYZE_SERIAL_NBT_TAG, Serial);
        stackCompound.put(ANALYZE_NBT_TAG, compound);
        return data;
    }

    public static ItemStack getEmptyCrystal(int tire) {
        var data = DataCrystalMap.get(tire).getDefaultInstance();
        CompoundTag stackCompound = data.getOrCreateTag();
        CompoundTag compound = new CompoundTag();
        compound.putInt(EMPTY_ID_NBT_TAG, 0);
        stackCompound.put(EMPTY_NBT_TAG, compound);
        return data;
    }

    public static String itemStackToString(@NotNull ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return stack.getCount() + "i-" + itemId.getNamespace() + "-" + itemId.getPath();
    }

    public static String fluidStackToString(@NotNull FluidStack stack) {
        ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
        return stack.getAmount() + "f-" + fluidId.getNamespace() + "-" + fluidId.getPath();
    }

    /**
     * 使用FNV-1a算法生成压缩序列号（6位哈希 + 2位前缀）
     *
     * @param scanningId  输入字符串
     * @param dataTier    层级标识 (0-15)
     * @param dataCrystal 数据标识 (0-15)
     * @return 32位整数，高8位为(tier+dataCrystal)，低24位为压缩哈希
     */
    private static int generateSerialId(String scanningId, int dataTier, int dataCrystal) {
        int hash = FNV_OFFSET_BASIS;
        byte[] bytes = scanningId.getBytes(StandardCharsets.UTF_8);

        for (byte b : bytes) {
            hash ^= (b & 0xFF);
            hash *= FNV_PRIME;
        }

        int prefix = ((dataTier & 0x0F) << 4) | (dataCrystal & 0x0F);

        return (prefix << 24) | (hash & 0xFFFFFF);
    }

    public static int ExtractDataTier(int serialId) {
        return (serialId >>> 28) & 0x0F;
    }

    public static int ExtractDataCrystal(int serialId) {
        return (serialId >>> 24) & 0x0F;
    }
}
