package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.ae2.IKeyCounter;
import com.gtolib.ae2.storage.CellDataStorage;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MEStorageAccessPartMachine extends MultiblockPartMachine implements IStorageAccess {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEStorageAccessPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    private boolean observe;
    private CellDataStorage dataStorage;
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

    public MEStorageAccessPartMachine(MetaMachineBlockEntity holder) {
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
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public void setInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
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
            onChanged();
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
            CellDataStorage storage = getCellStorage();
            double totalAmount = 0;
            for (ObjectIterator<Object2LongMap.Entry<AEKey>> it = getCellStoredMap().object2LongEntrySet().fastIterator(); it.hasNext();) {
                var entry = it.next();
                totalAmount += (double) entry.getLongValue() / entry.getKey().getType().getAmountPerByte();
            }
            storage.setBytes(totalAmount);
        } else if (!isInfinite && getOffsetTimer() % 20 == 7) {
            observe = true;
        }
    }

    private CellDataStorage getCellStorage() {
        if (dataStorage != null) return dataStorage;
        if (uuid == null || isRemote()) return CellDataStorage.EMPTY;
        dataStorage = CellDataStorage.get(uuid);
        return dataStorage;
    }

    private Object2LongOpenHashMap<AEKey> getCellStoredMap() {
        var data = getCellStorage();
        var map = data.getStoredMap();
        if (map == null) {
            map = new Object2LongOpenHashMap<>();
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
        CellDataStorage storage = getCellStorage();
        if (storage.isPersisted()) return;
        if (getCellStoredMap().isEmpty()) {
            if (uuid != null) {
                CellDataStorage.remove(uuid);
                dataStorage = null;
            }
            return;
        }
        double totalAmount = 0;
        LongArrayList amounts = new LongArrayList(getCellStoredMap().size());
        ListTag keys = new ListTag();
        for (ObjectIterator<Object2LongMap.Entry<AEKey>> it = getCellStoredMap().object2LongEntrySet().fastIterator(); it.hasNext();) {
            Object2LongOpenHashMap.Entry<AEKey> entry = it.next();
            long amount = entry.getLongValue();
            if (amount > 0) {
                var key = entry.getKey();
                totalAmount += (double) amount / key.getType().getAmountPerByte();
                keys.add(key.toTagGeneric());
                amounts.add(amount);
            }
        }
        storage.setPersisted(true);
        storage.setAmounts(amounts.toArray(new long[0]));
        storage.setKeys(keys);
        storage.setBytes(totalAmount);
        CellDataStorage.setDirty();
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        if (uuid == null) return;
        CellDataStorage storage = getCellStorage();
        Object2LongOpenHashMap<AEKey> map = getCellStoredMap();
        long[] amounts = storage.getAmounts();
        double totalAmount = 0;
        for (int i = 0; i < amounts.length; i++) {
            long amount = amounts[i];
            AEKey key = AEKey.fromTagGeneric(storage.getKeys().getCompound(i));
            if (amount <= 0 || key == null) continue;
            totalAmount += (double) amount / key.getType().getAmountPerByte();
            map.put(key, amount);
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
        if (data == CellDataStorage.EMPTY) return 0;
        if (!isInfinite) {
            amount = (long) Math.min(capacity - data.getBytes(), amount);
        }
        if (amount < 1) return 0;
        if (mode == Actionable.MODULATE) {
            getCellStoredMap().addTo(what, amount);
            dirty = true;
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        var map = getCellStoredMap();
        long currentAmount = map.getLong(what);
        if (currentAmount > 0) {
            if (amount >= currentAmount) {
                if (mode == Actionable.MODULATE) {
                    map.remove(what, currentAmount);
                    dirty = true;
                }
                return currentAmount;
            } else {
                if (mode == Actionable.MODULATE) {
                    map.put(what, currentAmount - amount);
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
        if (data == CellDataStorage.EMPTY) return;
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
    public double getCapacity() {
        return this.capacity;
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
