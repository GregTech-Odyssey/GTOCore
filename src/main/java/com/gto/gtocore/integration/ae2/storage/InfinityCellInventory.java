package com.gto.gtocore.integration.ae2.storage;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.integration.ae2.InfinityCell;
import com.gto.gtocore.utils.NumberUtils;
import com.gto.gtocore.utils.StorageManager;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.core.AELog;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class InfinityCellInventory implements StorageCell {

    private final ISaveProvider container;
    private final AEKeyType keyType;
    private Object2LongMap<AEKey> storedMap;
    private final ItemStack stack;
    private boolean isPersisted = true;

    public InfinityCellInventory(AEKeyType keyType, ItemStack stack, ISaveProvider saveProvider) {
        this.stack = stack;
        this.container = saveProvider;
        this.keyType = keyType;
        this.storedMap = null;
        initData();
    }

    private InfinityCellDataStorage getDiskStorage() {
        if (getDiskUUID() != null)
            return getStorageInstance().getOrCreateDisk(getDiskUUID());
        else
            return InfinityCellDataStorage.EMPTY;
    }

    private void initData() {
        if (!hasDiskUUID()) {
            getCellStoredMap();
        }
    }

    @Override
    public CellState getStatus() {
        if (getCellStoredMap().isEmpty()) {
            return CellState.EMPTY;
        }
        return CellState.NOT_EMPTY;
    }

    @Override
    public double getIdleDrain() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void persist() {
        if (this.isPersisted) {
            return;
        }

        if (getCellStoredMap().isEmpty()) {
            if (hasDiskUUID()) {
                getStorageInstance().removeDisk(getDiskUUID());
                if (stack.getTag() != null) {
                    stack.getTag().remove("diskuuid");
                }
                initData();
            }
            return;
        }

        LongArrayList amounts = new LongArrayList(getCellStoredMap().size());
        ListTag keys = new ListTag();

        for (Object2LongMap.Entry<AEKey> entry : getCellStoredMap().object2LongEntrySet()) {
            long amount = entry.getLongValue();

            if (amount > 0) {
                keys.add(entry.getKey().toTagGeneric());
                amounts.add(amount);
            }
        }

        if (keys.isEmpty()) {
            getStorageInstance().updateDisk(getDiskUUID(), new InfinityCellDataStorage());
        } else {
            getStorageInstance().modifyDisk(getDiskUUID(), keys, amounts.toArray(new long[0]));
        }

        this.isPersisted = true;
    }

    @Override
    public Component getDescription() {
        return null;
    }

    public static InfinityCellInventory createInventory(ItemStack stack, ISaveProvider saveProvider) {
        Objects.requireNonNull(stack, "Cannot create cell inventory for null itemstack");

        if (!(stack.getItem() instanceof InfinityCell cellType)) {
            return null;
        }

        return new InfinityCellInventory(cellType.getKeyType(), stack, saveProvider);
    }

    public boolean hasDiskUUID() {
        return stack.hasTag() && stack.getOrCreateTag().contains("diskuuid");
    }

    public static boolean hasDiskUUID(ItemStack disk) {
        if (disk.getItem() instanceof InfinityCell) {
            return disk.hasTag() && disk.getOrCreateTag().contains("diskuuid");
        }
        return false;
    }

    public UUID getDiskUUID() {
        if (hasDiskUUID())
            return stack.getOrCreateTag().getUUID("diskuuid");
        else
            return null;
    }

    public String getTotalStorage() {
        BigDecimal itemCount = BigDecimal.ZERO;
        int unitIndex = 0;
        for (long storedAmount : getCellStoredMap().values()) {
            itemCount = itemCount.add(BigDecimal.valueOf(storedAmount));
        }
        while (itemCount.compareTo(BigDecimal.TEN.pow(3)) >= 0) {
            itemCount = itemCount.divide(BigDecimal.TEN.pow(3), 3, RoundingMode.HALF_DOWN);
            unitIndex++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedNumber = df.format(itemCount.doubleValue());
        return formattedNumber + NumberUtils.UNITS[unitIndex];
    }

    private boolean isStorageCell(AEItemKey key) {
        InfinityCell type = getStorageCell(key);
        return type != null;
    }

    private static InfinityCell getStorageCell(AEItemKey itemKey) {
        if (itemKey.getItem() instanceof InfinityCell infinityCell) {
            return infinityCell;
        }

        return null;
    }

    private static boolean isCellEmpty(InfinityCellInventory inv) {
        if (inv != null) {
            return inv.getAvailableStacks().isEmpty();
        }
        return true;
    }

    protected Object2LongMap<AEKey> getCellStoredMap() {
        if (this.storedMap == null) {
            this.storedMap = new Object2LongOpenHashMap<>();
            this.loadCellStoredMap();
        }

        return this.storedMap;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (Object2LongMap.Entry<AEKey> entry : getCellStoredMap().object2LongEntrySet()) {
            out.add(entry.getKey(), entry.getLongValue());
        }
    }

    private void loadCellStoredMap() {
        boolean corruptedTag = false;

        if (!stack.hasTag()) {
            return;
        }

        long[] amounts = getDiskStorage().stackAmounts;
        ListTag tags = getDiskStorage().stackKeys;
        if (amounts.length != tags.size()) {
            AELog.warn("Loading storage cell with mismatched amounts/tags: %d != %d",
                    amounts.length, tags.size());
        }

        for (int i = 0; i < amounts.length; i++) {
            long amount = amounts[i];
            AEKey key = AEKey.fromTagGeneric(tags.getCompound(i));

            if (amount <= 0 || key == null) {
                corruptedTag = true;
            } else {
                getCellStoredMap().put(key, amount);
            }
        }

        if (corruptedTag) {
            this.saveChanges();
        }
    }

    private StorageManager getStorageInstance() {
        return GTOCore.STORAGE_INSTANCE;
    }

    protected void saveChanges() {
        this.isPersisted = false;
        if (this.container != null) {
            this.container.saveChanges();
        } else {
            this.persist();
        }
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount == 0 || !keyType.contains(what)) {
            return 0;
        }

        if (what instanceof AEItemKey itemKey && this.isStorageCell(itemKey)) {
            InfinityCellInventory meInventory = createInventory(itemKey.toStack(), null);
            if (!isCellEmpty(meInventory)) {
                return 0;
            }
        }

        if (!hasDiskUUID()) {
            stack.getOrCreateTag().putUUID("diskuuid", UUID.randomUUID());
            getStorageInstance().getOrCreateDisk(getDiskUUID());
            loadCellStoredMap();
        }

        long currentAmount = this.getCellStoredMap().getLong(what);

        if (mode == Actionable.MODULATE) {
            getCellStoredMap().put(what, currentAmount + amount);
            this.saveChanges();
        }

        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        long currentAmount = getCellStoredMap().getLong(what);
        if (currentAmount > 0) {
            if (amount >= currentAmount) {
                if (mode == Actionable.MODULATE) {
                    getCellStoredMap().remove(what, currentAmount);
                    this.saveChanges();
                }

                return currentAmount;
            } else {
                if (mode == Actionable.MODULATE) {
                    getCellStoredMap().put(what, currentAmount - amount);
                    this.saveChanges();
                }

                return amount;
            }
        }
        return 0;
    }
}
