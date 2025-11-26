package com.gtocore.mixin.ae2.storage;

import com.gtolib.api.ae2.stacks.IKeyCounter;
import com.gtolib.api.ae2.storage.CellDataStorage;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.config.IncludeExclude;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.IBasicCellItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.me.cells.BasicCellInventory;
import appeng.util.prioritylist.IPartitionList;
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
    private AEKeyMap<AEKey> gtocore$aeKeyMap;

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

    @Shadow(remap = false)
    @Final
    private boolean hasVoidUpgrade;

    @Shadow(remap = false)
    public abstract boolean isPreformatted();

    @Shadow(remap = false)
    @Final
    private IPartitionList partitionList;

    @Shadow(remap = false)
    @Final
    private IncludeExclude partitionListMode;

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
    private AEKeyMap<AEKey> gtolib$getCellStoredMap() {
        if (gtocore$aeKeyMap == null) {
            CellDataStorage storage = gtolib$getCellStorage();
            if (storage == CellDataStorage.EMPTY) return CellDataStorage.EMPTY.getStoredMap();
            gtocore$aeKeyMap = storage.getStoredMap();
            if (gtocore$aeKeyMap == null) {
                gtocore$aeKeyMap = new AEKeyMap<>();
                storage.setStoredMap(gtocore$aeKeyMap);
            } else {
                double totalAmount = 0;
                for (long amount : gtocore$aeKeyMap.values()) {
                    totalAmount += (double) amount / keyType.getAmountPerByte();
                }
                storage.setBytes(totalAmount);
                var tag = i.getTag();
                if (tag != null) {
                    tag.putLong(USED_BYTE, (long) totalAmount);
                    tag.putInt(USED_TYPE, gtocore$aeKeyMap.size());
                }
            }
        }
        return gtocore$aeKeyMap;
    }

    @Unique
    @NotNull
    private CellDataStorage gtolib$getCellStorage() {
        if (gtolib$cache != null) return gtolib$cache;
        if (GTCEu.isClientThread()) return CellDataStorage.EMPTY;
        UUID uuid = gtolib$getUUID();
        if (uuid == null) return gtolib$cache = CellDataStorage.EMPTY;
        return gtolib$cache = CellDataStorage.get(uuid);
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
    public void persist() {
        var map = gtolib$getCellStoredMap();
        double totalAmount = 0;
        for (long amount : map.values()) {
            totalAmount += (double) amount / keyType.getAmountPerByte();
        }
        CellDataStorage storage = gtolib$getCellStorage();
        storage.setBytes(totalAmount);
        var tag = i.getTag();
        if (tag != null) {
            tag.putLong(USED_BYTE, (long) totalAmount);
            tag.putInt(USED_TYPE, map.size());
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void saveChanges() {
        if (container != null) {
            double totalAmount = 0;
            for (long amount : gtolib$getCellStoredMap().values()) {
                totalAmount += (double) amount / keyType.getAmountPerByte();
            }
            gtolib$getCellStorage().setBytes(totalAmount);
            container.saveChanges();
        } else {
            persist();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void getAvailableStacks(KeyCounter out) {
        var map = gtolib$getCellStoredMap();
        IKeyCounter.addAll(out, map.size(), m -> map.reference2LongEntrySet().fastForEach(e -> m.addTo(e.getKey(), e.getLongValue())));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount == 0 || !keyType.contains(what)) return 0;
        if (!this.partitionList.matchesFilter(what, this.partitionListMode)) return 0;
        if (this.cellType.isBlackListed(this.i, what)) return 0;
        long inserted = innerInsert(what, amount, mode);
        if (!isPreformatted() && hasVoidUpgrade && !canHoldNewItem()) {
            return gtolib$getCellStoredMap().containsKey(what) ? amount : inserted;
        }
        return hasVoidUpgrade ? amount : inserted;
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
            gtolib$cache = CellDataStorage.get(uuid);
        }
        var data = gtolib$getCellStorage();
        if (data == CellDataStorage.EMPTY) return 0;
        amount = Math.min(gtolib$totalAmount - (long) (data.getBytes() * keyType.getAmountPerByte()), amount);
        if (amount < 1) return 0;
        if (mode == Actionable.MODULATE) {
            gtolib$getCellStoredMap().addTo(what, amount);
            data.setDirty();
            saveChanges();
        }

        return amount;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        var map = gtolib$getCellStoredMap();
        var currentAmount = map.getLong(what);
        if (currentAmount > 0) {
            if (amount >= currentAmount) {
                if (mode == Actionable.MODULATE) {
                    map.remove(what, currentAmount);
                    gtolib$getCellStorage().setDirty();
                    this.saveChanges();
                }
                return currentAmount;
            } else {
                if (mode == Actionable.MODULATE) {
                    map.put(what, currentAmount - amount);
                    gtolib$getCellStorage().setDirty();
                    this.saveChanges();
                }
                return amount;
            }
        }
        return 0;
    }
}
