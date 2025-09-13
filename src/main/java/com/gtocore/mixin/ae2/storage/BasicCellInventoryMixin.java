package com.gtocore.mixin.ae2.storage;

import com.gtolib.api.ae2.stacks.IKeyCounter;
import com.gtolib.api.ae2.storage.CellDataStorage;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.IBasicCellItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.items.tools.powered.PortableCellItem;
import appeng.me.cells.BasicCellInventory;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static com.gtolib.api.GTOValues.CELL_UUID;

@Mixin(BasicCellInventory.class)
public abstract class BasicCellInventoryMixin implements StorageCell {

    @Unique
    private static final String USED_BYTE = "byte";

    @Unique
    private static final String USED_TYPE = "type";

    @Unique
    private CellDataStorage gtolib$cache;

    @Unique
    private UUID gtolib$uuid;

    @Unique
    private int gtolib$totalbytes;

    @Unique
    private long gtolib$totalAmount;

    @Shadow(remap = false)
    @Final
    private ItemStack i;

    @Shadow(remap = false)
    @Final
    private ISaveProvider container;

    @Shadow(remap = false)
    @Final
    private IBasicCellItem cellType;

    @Shadow(remap = false)
    @Final
    private AEKeyType keyType;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(IBasicCellItem cellType, ItemStack o, ISaveProvider container, CallbackInfo ci) {
        gtolib$totalbytes = Math.min(262144, cellType.getBytes(o));
        gtolib$totalAmount = (long) gtolib$totalbytes * keyType.getAmountPerByte();
    }

    @Unique
    @Nullable
    private UUID gtolib$getUUID() {
        if (this.gtolib$uuid == null) {
            var tag = i.getTag();
            if (tag != null) {
                var uuid = tag.get(CELL_UUID);
                if (uuid != null && uuid.getType() == IntArrayTag.TYPE && ((IntArrayTag) uuid).getAsIntArray().length == 4) {
                    this.gtolib$uuid = NbtUtils.loadUUID(uuid);
                }
            }
        }
        return this.gtolib$uuid;
    }

    @Unique
    @NotNull
    private Object2LongOpenHashMap<AEKey> gtolib$getCellStoredMap() {
        CellDataStorage storage = gtolib$getCellStorage();
        var map = storage.getStoredMap();
        if (map == null) {
            map = new O2LOpenCacheHashMap<>();
            storage.setStoredMap(map);
            long[] amounts = storage.getAmounts();
            double totalAmount = 0;
            for (int i = 0; i < amounts.length; i++) {
                long amount = amounts[i];
                AEKey key = AEKey.fromTagGeneric(storage.getKeys().getCompound(i));
                if (amount <= 0 || key == null) continue;
                totalAmount += (double) amount / keyType.getAmountPerByte();
                map.put(key, amount);
            }
            storage.setBytes(totalAmount);
            var tag = i.getTag();
            if (tag != null) {
                tag.putLong(USED_BYTE, (long) totalAmount);
                tag.putInt(USED_TYPE, amounts.length);
            }
        }
        return map;
    }

    @Unique
    @NotNull
    private CellDataStorage gtolib$getCellStorage() {
        if (gtolib$cache != null) return gtolib$cache;
        if (GTCEu.isClientThread()) return CellDataStorage.EMPTY;
        UUID uuid = gtolib$getUUID();
        if (uuid == null) return CellDataStorage.EMPTY;
        gtolib$cache = CellDataStorage.get(uuid);
        return gtolib$cache;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long getTotalBytes() {
        return gtolib$totalbytes;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean canHoldNewItem() {
        var data = gtolib$getCellStorage();
        return data.getBytes() <= gtolib$totalbytes;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long getUsedBytes() {
        if (GTCEu.isClientThread()) {
            var tag = i.getTag();
            if (tag == null) return 0;
            return tag.getLong(USED_BYTE);
        }
        return (long) gtolib$getCellStorage().getBytes();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long getStoredItemCount() {
        return (long) gtolib$getCellStorage().getBytes();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long getStoredItemTypes() {
        if (GTCEu.isClientThread()) {
            var tag = i.getTag();
            if (tag == null) return 0;
            return tag.getLong(USED_TYPE);
        }
        return gtolib$getCellStoredMap().size();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected Object2LongMap<AEKey> getCellItems() {
        return gtolib$getCellStoredMap();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void persist() {
        CellDataStorage storage = gtolib$getCellStorage();
        if (storage.isPersisted()) return;
        if (gtolib$getCellStoredMap().isEmpty()) {
            UUID uuid = gtolib$getUUID();
            if (uuid != null) {
                CellDataStorage.remove(uuid);
                if (i.getTag() != null) {
                    i.getTag().remove(CELL_UUID);
                    i.getTag().remove(USED_BYTE);
                    i.getTag().remove(USED_TYPE);
                }
                gtolib$uuid = null;
                gtolib$cache = null;
            }
            return;
        }
        double totalAmount = 0;
        LongArrayList amounts = new LongArrayList(gtolib$getCellStoredMap().size());
        ListTag keys = new ListTag();
        for (ObjectIterator<Object2LongMap.Entry<AEKey>> it = gtolib$getCellStoredMap().object2LongEntrySet().fastIterator(); it.hasNext();) {
            Object2LongOpenHashMap.Entry<AEKey> entry = it.next();
            long amount = entry.getLongValue();
            if (amount > 0) {
                var key = entry.getKey();
                totalAmount += (double) amount / keyType.getAmountPerByte();
                keys.add(key.toTagGeneric());
                amounts.add(amount);
            }
        }
        storage.setPersisted(true);
        storage.setAmounts(amounts.toArray(new long[0]));
        storage.setKeys(keys);
        storage.setBytes(totalAmount);
        var tag = i.getTag();
        if (tag != null) {
            tag.putLong(USED_BYTE, (long) totalAmount);
            tag.putInt(USED_TYPE, amounts.size());
        }
        CellDataStorage.setDirty();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void saveChanges() {
        CellDataStorage storage = gtolib$getCellStorage();
        double totalAmount = 0;
        for (ObjectIterator<Object2LongMap.Entry<AEKey>> it = gtolib$getCellStoredMap().object2LongEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            totalAmount += (double) entry.getLongValue() / keyType.getAmountPerByte();
        }
        storage.setBytes(totalAmount);
        gtolib$getCellStorage().setPersisted(false);
        if (container != null) {
            container.saveChanges();
        } else {
            persist();
        }
    }

    @Override
    public KeyCounter getAvailableStacks() {
        KeyCounter keyCounter;
        if (cellType instanceof PortableCellItem) {
            keyCounter = new KeyCounter();
        } else {
            var data = gtolib$getCellStorage();
            keyCounter = data.getKeyCounter();
            if (keyCounter == null) {
                keyCounter = new KeyCounter();
                data.setKeyCounter(keyCounter);
            } else {
                keyCounter.clear();
            }
        }
        getAvailableStacks(keyCounter);
        keyCounter.removeEmptySubmaps();
        return keyCounter;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void getAvailableStacks(KeyCounter out) {
        IKeyCounter.addAll(out, gtolib$getCellStoredMap());
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private long innerInsert(AEKey what, long amount, Actionable mode) {
        if (gtolib$getUUID() == null) {
            UUID uuid = UUID.randomUUID();
            i.getOrCreateTag().putUUID(CELL_UUID, uuid);
            CellDataStorage.get(uuid);
        }
        var data = gtolib$getCellStorage();
        if (data == CellDataStorage.EMPTY) return 0;
        amount = Math.min(gtolib$totalAmount - (long) (data.getBytes() * keyType.getAmountPerByte()), amount);
        if (amount < 1) return 0;
        if (mode == Actionable.MODULATE) {
            gtolib$getCellStoredMap().addTo(what, amount);
            saveChanges();
        }

        return amount;
    }
}
