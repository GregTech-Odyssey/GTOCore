package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.ae2.stacks.IKeyCounter;
import com.gtolib.ae2.storage.BigCellDataStorage;
import com.gtolib.ae2.storage.CellDataStorage;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.config.Actionable;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public final class MEBigStorageAccessPartMachine extends MultiblockPartMachine implements IStorageAccess {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEBigStorageAccessPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    private boolean observe;
    private BigCellDataStorage dataStorage;
    @Persisted
    private final GridNodeHolder nodeHolder;
    private int counter;
    private boolean check;
    @Persisted
    private double capacity;
    @Persisted
    private boolean isInfinite;
    @DescSynced
    private boolean isOnline;
    @Persisted
    private UUID uuid;
    private final ConditionalSubscriptionHandler tickSubs;

    private boolean dirty = false;

    public MEBigStorageAccessPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
        getMainNode().addService(IStorageProvider.class, this);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, () -> true);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        if (uuid == null) return;
        storageMounts.mount(this, 0);
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
        dataStorage = null;
        IStorageProvider.requestUpdate(getMainNode());
    }

    @Override
    public int getTypes() {
        if (dataStorage == null) return 0;
        return dataStorage.getKeys().size();
    }

    @Override
    public double getBytes() {
        if (dataStorage == null) return 0;
        return dataStorage.getBytes();
    }

    private void tickUpdate() {
        if (dirty) {
            dirty = false;
            getCellStorage().setPersisted(false);
            markDirty();
        }
        if (uuid == null || capacity == 0 || !isOnline) return;
        if (!check) {
            counter++;
            if (counter > 600) {
                counter = 0;
                capacity = 0;
                setOwnerUUID(null);
                isInfinite = false;
            }
        }
        if (observe) {
            observe = false;
            var data = getCellStorage();
            if (data == BigCellDataStorage.EMPTY) return;
            var map = data.getStoredMap();
            if (map == null) return;
            double totalAmount = 0;
            for (ObjectIterator<Object2ObjectMap.Entry<AEKey, BigInteger>> it = map.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                totalAmount += entry.getValue().doubleValue() / entry.getKey().getType().getAmountPerByte();
            }
            data.setBytes(totalAmount);
        } else if (!isInfinite && getOffsetTimer() % 20 == 7) {
            observe = true;
        }
    }

    public BigCellDataStorage getCellStorage() {
        if (dataStorage != null) return dataStorage;
        if (uuid == null || isRemote()) return BigCellDataStorage.EMPTY;
        dataStorage = BigCellDataStorage.get(uuid);
        return dataStorage;
    }

    private Object2ObjectOpenHashMap<AEKey, BigInteger> getCellStoredMap() {
        var data = getCellStorage();
        var map = data.getStoredMap();
        if (map == null) {
            map = new Object2ObjectOpenHashMap<>();
            data.setStoredMap(map);
        }
        return map;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickSubs.initialize(getLevel());
    }

    @Override
    public void onUnload() {
        super.onUnload();
        tickSubs.unsubscribe();
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        var storage = getCellStorage();
        if (storage.isPersisted()) return;
        if (storage == BigCellDataStorage.EMPTY) return;
        var map = storage.getStoredMap();
        if (map == null) return;
        if (map.isEmpty()) {
            if (uuid != null) {
                BigCellDataStorage.remove(uuid);
                dataStorage = null;
            }
            return;
        }
        double totalAmount = 0;
        ListTag amounts = new ListTag();
        ListTag keys = new ListTag();
        for (ObjectIterator<Object2ObjectMap.Entry<AEKey, BigInteger>> it = map.object2ObjectEntrySet().fastIterator(); it.hasNext();) {
            Object2ObjectMap.Entry<AEKey, BigInteger> entry = it.next();
            var amount = entry.getValue();
            if (amount.signum() > 0) {
                var key = entry.getKey();
                totalAmount += amount.doubleValue() / key.getType().getAmountPerByte();
                keys.add(key.toTagGeneric());
                amounts.add(StringTag.valueOf(amount.toString()));
            }
        }
        storage.setPersisted(true);
        storage.setAmounts(amounts);
        storage.setKeys(keys);
        storage.setBytes(totalAmount);
        CellDataStorage.setDirty();
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        if (uuid == null) return;
        var storage = getCellStorage();
        var map = getCellStoredMap();
        ListTag amounts = storage.getAmounts();
        double totalAmount = 0;
        var size = amounts.size();
        for (int i = 0; i < size; i++) {
            String amount = amounts.getString(i);
            AEKey key = AEKey.fromTagGeneric(storage.getKeys().getCompound(i));
            if (key == null || amount.isEmpty()) continue;
            var a = new BigInteger(amount);
            totalAmount += a.doubleValue() / key.getType().getAmountPerByte();
            map.put(key, a);
        }
        storage.setBytes(totalAmount);
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return isInfinite || capacity > getCellStorage().getBytes();
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount == 0 || uuid == null) return 0;
        var data = getCellStorage();
        if (data == BigCellDataStorage.EMPTY) return 0;
        if (!isInfinite) {
            amount = (long) Math.min(capacity - data.getBytes(), amount);
        }
        if (amount < 1) return 0;
        if (mode == Actionable.MODULATE) {
            long finalAmount = amount;
            getCellStoredMap().compute(what, (k, v) -> {
                if (v == null) return BigInteger.valueOf(finalAmount);
                return v.add(BigInteger.valueOf(finalAmount));
            });
            dirty = true;
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        var data = getCellStorage();
        if (data == BigCellDataStorage.EMPTY) return 0;
        var map = data.getStoredMap();
        if (map == null) return 0;
        var o = map.get(what);
        if (o == null) return 0;
        if (o.signum() > 0) {
            var extractAmount = BigInteger.valueOf(amount);
            if (o.compareTo(extractAmount) < 1) {
                if (mode == Actionable.MODULATE) {
                    map.remove(what);
                    dirty = true;
                }
                return o.longValue();
            } else {
                if (mode == Actionable.MODULATE) {
                    map.put(what, o.subtract(extractAmount));
                    dirty = true;
                }
                return amount;
            }
        }
        return 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        var data = getCellStorage();
        if (data == BigCellDataStorage.EMPTY) return;
        var map = data.getStoredMap();
        if (map == null) return;
        IKeyCounter.addAll(out, map);
    }

    @Override
    public KeyCounter getAvailableStacks() {
        var data = getCellStorage();
        var keyCounter = data.getKeyCounter();
        if (keyCounter == null) {
            keyCounter = new KeyCounter();
            data.setKeyCounter(keyCounter);
        } else {
            keyCounter.clear();
        }
        getAvailableStacks(keyCounter);
        keyCounter.removeEmptySubmaps();
        return keyCounter;
    }

    @Override
    public Component getDescription() {
        return getDefinition().getItem().getDescription();
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public void setObserve(final boolean observe) {
        this.observe = observe;
    }

    @Override
    public void setCheck(final boolean check) {
        this.check = check;
    }

    @Override
    public void setCapacity(final double capacity) {
        this.capacity = capacity;
    }

    @Override
    public double getCapacity() {
        return this.capacity;
    }

    @Override
    public void setInfinite(final boolean isInfinite) {
        this.isInfinite = isInfinite;
    }

    @Override
    public boolean isInfinite() {
        return this.isInfinite;
    }

    @Override
    public void setOnline(final boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public boolean isOnline() {
        return this.isOnline;
    }
}
