package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.storage.cells.StorageCell;
import com.gtolib.api.ae2.IKeyCounter;
import com.gtolib.api.ae2.storage.BigCellDataStorage;
import com.gtolib.api.ae2.storage.CellDataStorage;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.*;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.UUID;

@DataGeneratorScanned
public abstract class StorageAccessPartMachine extends AmountConfigurationHatchPartMachine implements IMachineLife, MEStorage, IGridConnectedMachine, IStorageProvider {

    public static StorageAccessPartMachine create(MetaMachineBlockEntity holder) {
        return new StorageAccessPartMachine.LONG(holder);
    }

    public static StorageAccessPartMachine createBig(MetaMachineBlockEntity holder) {
        return new StorageAccessPartMachine.Big(holder);
    }

    public static StorageAccessPartMachine createIO(MetaMachineBlockEntity holder) {
        return new StorageAccessPartMachine.IO(holder);
    }

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            StorageAccessPartMachine.class, AmountConfigurationHatchPartMachine.MANAGED_FIELD_HOLDER);
    private static final ManagedFieldHolder IO_FIELD_HOLDER = new ManagedFieldHolder(
            IO.class, MANAGED_FIELD_HOLDER);

    boolean observe;

    int counter;
    boolean check;
    boolean dirty = false;

    @Persisted
    double capacity;
    @Persisted
    boolean isInfinite;
    @DescSynced
    boolean isOnline;
    @Persisted
    UUID uuid;

    @Persisted
    private final GridNodeHolder nodeHolder;
    private final ConditionalSubscriptionHandler tickSubs;

    StorageAccessPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.EV, -1000000, 1000000);
        this.nodeHolder = new GridNodeHolder(this);
        getMainNode().addService(IStorageProvider.class, this);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, () -> true);
        current = 0;
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        return ((WidgetGroup) super.createUIWidget()).addWidget(new LabelWidget(24, -16, () -> "gui.ae2.Priority"));
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
    protected long getCurrent() {
        return current;
    }

    @Override
    protected void onAmountChange(long amount) {
        IStorageProvider.requestUpdate(getMainNode());
    }

    abstract void tickUpdate();

    public abstract void setUUID(UUID uuid);

    public abstract int getTypes();

    public abstract double getBytes();

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
    }

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        if (uuid == null) return;
        storageMounts.mount(this, (int) current);
    }

    @Override
    public Component getDescription() {
        return getDefinition().getItem().getDescription();
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    public void setObserve(final boolean observe) {
        this.observe = observe;
    }

    public void setCheck(final boolean check) {
        this.check = check;
    }

    public double getCapacity() {
        return this.capacity;
    }

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

    private static class LONG extends StorageAccessPartMachine {

        private CellDataStorage dataStorage;

        private LONG(MetaMachineBlockEntity holder) {
            super(holder);
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

        void tickUpdate() {
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
                    setUUID(null);
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

        protected CellDataStorage getCellStorage() {
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
    }

    private static class IO extends LONG implements IControllable {

        @Persisted
        private boolean isWorkingEnabled;
        @Persisted
        private boolean export;
        @Persisted
        private long rate = 33554432L;

        private final IActionSource mySrc;
        IFancyConfiguratorButton.Toggle toggle;

        private IO(MetaMachineBlockEntity holder) {
            super(holder);
            mySrc = IActionSource.ofMachine(this);
        }

        @Override
        public @NotNull ManagedFieldHolder getFieldHolder() {
            return IO_FIELD_HOLDER;
        }

        @Override
        public Widget createUIWidget() {
            var longInput = new LongInputWidget(() -> rate, (v) -> rate = v);
            longInput.setMax(Long.MAX_VALUE);
            longInput.setMin(0L);
            return new WidgetGroup(0, 0, 100, 20).addWidget(longInput).addWidget(new LabelWidget(24, -16, () -> LANG_RATE_SETTING));
        }

        @Override
        public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
            super.attachConfigurators(configuratorPanel);
            configuratorPanel.attachConfigurators(toggle = new IFancyConfiguratorButton.Toggle(
                    new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                            .getSubTexture(0, 0, 1, 0.5)),

                    new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy()
                            .getSubTexture(0, 0.5, 1, 0.5)),
                    () -> export,
                    (cd, b) -> export = b

            ));
            if (isRemote()) {
                toggle.setTooltipsSupplier((export) -> Collections.singletonList(export ?
                        Component.translatable(LANG_EXPORT) :
                        Component.translatable(LANG_IMPORT)));
            }
        }

        @Override
        public boolean isWorkingEnabled() {
            return isWorkingEnabled;
        }

        @Override
        public void setWorkingEnabled(boolean isWorkingAllowed) {
            isWorkingEnabled = isWorkingAllowed;
        }

        /// do not allow insertion when exporting and working enabled
        @Override
        public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
            return (export && isWorkingEnabled()) ? 0 : super.insert(what, amount, mode, source);
        }

        /// do not allow extraction when importing and working enabled
        @Override
        public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
            return (!export && isWorkingEnabled()) ? 0 : super.extract(what, amount, mode, source);
        }

        @Override
        void tickUpdate() {
            super.tickUpdate();
            if (!this.getMainNode().isActive() || !isWorkingEnabled()) {
                return;
            }

            // check if the controller has any other storage parts than this one
            if (this.controllers.isEmpty() || this.controllers.first().getParts().stream().anyMatch(
                    p -> p instanceof StorageAccessPartMachine && p != this)) {
                setWorkingEnabled(false);
                return;
            }

            var grid = getMainNode().getGrid();
            if (grid == null) {
                setWorkingEnabled(false);
                return;
            }

            transferContents(grid);
        }

        /// logic from {@link appeng.blockentity.storage.IOPortBlockEntity#transferContents(IGrid, StorageCell, long)}
        private void transferContents(IGrid grid) {
            var networkInv = grid.getStorageService().getInventory();
            long itemsToMove = rate;

            KeyCounter srcList;
            MEStorage src, destination;
            if (export) {
                src = this;
                srcList = this.getAvailableStacks();
                destination = networkInv;
            } else {
                src = networkInv;
                srcList = grid.getStorageService().getCachedInventory();
                destination = this;
            }

            var energy = grid.getEnergyService();
            boolean didStuff;

            do {
                didStuff = false;

                for (var srcEntry : srcList) {
                    var totalStackSize = srcEntry.getLongValue();
                    if (totalStackSize > 0) {
                        var what = srcEntry.getKey();
                        var possible = destination.insert(what, totalStackSize, Actionable.SIMULATE, this.mySrc);

                        if (possible > 0) {
                            possible = Math.min(possible, itemsToMove * what.getAmountPerOperation());

                            possible = src.extract(what, possible, Actionable.MODULATE, this.mySrc);
                            if (possible > 0) {
                                var inserted = StorageHelper.poweredInsert(energy, destination, what, possible, this.mySrc);

                                if (inserted < possible) {
                                    src.insert(what, possible - inserted, Actionable.MODULATE, this.mySrc);
                                }

                                if (inserted > 0) {
                                    itemsToMove -= Math.max(1, inserted / what.getAmountPerOperation());
                                    didStuff = true;
                                }

                                break;
                            }
                        }
                    }
                }
            } while (itemsToMove > 0 && didStuff);
            setWorkingEnabled(didStuff);
        }
    }

    public static final class Big extends StorageAccessPartMachine {

        private BigCellDataStorage dataStorage;

        private Big(MetaMachineBlockEntity holder) {
            super(holder);
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

        void tickUpdate() {
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
                    setUUID(null);
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
    }

    @RegisterLanguage(cn = "从ME存储器导出", en = "Export from ME Storage")
    public static final String LANG_EXPORT = "gtocore.machine.part.ae.storage_access.export";
    @RegisterLanguage(cn = "导入到ME存储器", en = "Import to ME Storage")
    public static final String LANG_IMPORT = "gtocore.machine.part.ae.storage_access.import";
    @RegisterLanguage(cn = "导入/导出速率设置", en = "Import/Export Rate Setting")
    public static final String LANG_RATE_SETTING = "gtocore.machine.part.ae.storage_access.rate_setting";
}
