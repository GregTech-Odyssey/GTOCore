package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.ae2.stacks.IKeyCounter;
import com.gtolib.ae2.storage.CellDataStorage;
import com.gtolib.mixin.NetworkStorageAccessor;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.me.storage.NetworkStorage;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MEStorageAccessPartMachine extends MultiblockPartMachine implements IMachineLife, MEStorage, IGridConnectedMachine, IDataStickInteractable, IStorageAccess {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEStorageAccessPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
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
    UUID uuid;
    private final Set<MEStorageAccessPartMachine> proxyMachines = new ReferenceOpenHashSet<>();
    private final ConditionalSubscriptionHandler tickSubs;

    public MEStorageAccessPartMachine(IMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
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
        for (var machine : proxyMachines) {
            machine.setCapacity(capacity);
        }
    }

    @Override
    public void setInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
        for (var machine : proxyMachines) {
            machine.setInfinite(isInfinite);
        }
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
        for (var machine : proxyMachines) {
            machine.setUUID(uuid);
        }
        unmount();
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

    void addProxy(MEStorageAccessProxyartMachine proxy) {
        proxyMachines.add(proxy);
    }

    void removeProxy(MEStorageAccessProxyartMachine proxy) {
        proxyMachines.remove(proxy);
    }

    boolean contains(List<MEStorage> list) {
        for (var machine : proxyMachines) {
            if (list.contains(machine)) return true;
        }
        return list.contains(this);
    }

    private void unmount() {
        dataStorage = null;
        IGrid grid = getMainNode().getGrid();
        if (grid == null) return;
        ((NetworkStorage) grid.getStorageService().getInventory()).unmount(this);
        for (var machine : proxyMachines) {
            machine.unmount();
        }
    }

    private void tickUpdate() {
        if (uuid == null || capacity == 0 || !isOnline) return;
        if (!check) {
            counter++;
            if (counter > 600) {
                counter = 0;
                setCapacity(0);
                setOwnerUUID(null);
                setInfinite(false);
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
        if (getOffsetTimer() % 20 != 0) return;
        IGrid grid = getMainNode().getGrid();
        if (grid == null) return;
        var inv = ((NetworkStorage) grid.getStorageService().getInventory());
        var inventory = ((NetworkStorageAccessor) inv).getPriorityInventory().get(0);
        if (inventory == null || !contains(inventory)) {
            inv.mount(0, this);
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
    public InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        dataStick.getOrCreateTag().putIntArray("pos", new int[] { getPos().getX(), getPos().getY(), getPos().getZ() });
        return InteractionResult.SUCCESS;
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
        unmount();
    }

    @Override
    public void onMachineRemoved() {
        unmount();
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
        if (amount > 0) {
            if (mode == Actionable.MODULATE) {
                getCellStoredMap().addTo(what, amount);
                data.setPersisted(false);
            }
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
                    getCellStorage().setPersisted(false);
                }
                return currentAmount;
            } else {
                if (mode == Actionable.MODULATE) {
                    map.put(what, currentAmount - amount);
                    getCellStorage().setPersisted(false);
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
