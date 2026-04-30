package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidSlot;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemList;
import com.gtocore.common.machine.multiblock.part.ae.widget.MEInputBufferPartMachineUIKt;

import com.gtolib.api.capability.ISync;
import com.gtolib.api.gui.ktflexible.VBoxBuilder;
import com.gtolib.api.machine.trait.*;
import com.gtolib.api.network.SyncManagedFieldHolder;
import com.gtolib.api.recipe.modifier.ParallelCache;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.transfer.item.LockableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.nbt.*;
import net.minecraft.server.TickTask;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IStackWatcher;
import appeng.api.networking.crafting.*;
import appeng.api.stacks.*;
import appeng.api.storage.MEStorage;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.MultiCraftingTracker;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class MEInputBufferPartMachine extends MEPatternPartMachineKt<MEInputBufferPartMachine.InternalSlot> {

    private IStackWatcher storageWatcher;
    private IStackWatcher craftingWatcher;

    private final List<RecipeHandlerList> recipeHandlers;

    private static final SyncManagedFieldHolder SYNC_MANAGED_FIELD_HOLDER = new SyncManagedFieldHolder(MEInputBufferPartMachine.class, MEPatternPartMachineKt.getSYNC_MANAGED_FIELD_HOLDER());
    @Getter
    public IntSyncedField configuratorField = ISync.createIntField(this)
            .set(-1)
            .setReceiverListener((side, o, n) -> {
                if (side.isServer()) Objects.requireNonNull(Objects.requireNonNull(getLevel()).getServer()).tell(new TickTask(10, () -> freshWidgetGroup.serverFresh()));
            });

    @Override
    public void onMouseClicked(int index) {
        if (!isRemote()) return;
        if (configuratorField.get() == index) {
            configuratorField.setAndSyncToServer(-1);
        } else {
            configuratorField.setAndSyncToServer(index);
        }
    }

    @Override
    public @NotNull SyncManagedFieldHolder getSyncHolder() {
        return SYNC_MANAGED_FIELD_HOLDER;
    }

    private final Multimap<AEKey, InternalSlot> watcher2SlotMap = Multimaps.newSetMultimap(new Reference2ObjectOpenHashMap<>(), ReferenceOpenHashSet::new);
    private final Reference2ReferenceMap<InternalSlot, AEKey> slot2WatcherMap = new Reference2ReferenceOpenHashMap<>();

    @SuppressWarnings("FieldCanBeLocal")
    private final ICraftingWatcherNode craftingWatcherNode = new ICraftingWatcherNode() {

        @Override
        public void updateWatcher(IStackWatcher newWatcher) {
            craftingWatcher = newWatcher;
            configureWatchers();
        }

        @Override
        public void onRequestChange(AEKey what) {
            updateState();
        }

        @Override
        public void onCraftableChange(AEKey what) {}
    };

    @Nullable
    private TickableSubscription autoIOSubs;

    public MEInputBufferPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 9);
        getMainNode().addService(ICraftingWatcherNode.class, craftingWatcherNode);
        this.recipeHandlers = Arrays.stream(getInternalInventory())
                .map(s -> (RecipeHandlerList) new SlotRHL(s, this)).toList();
    }

    void autoIO() {
        if (this.updateMEStatus()) {
            IGrid grid = getMainNode().getGrid();
            if (grid == null) {
                return;
            }
            for (InternalSlot slot : getInternalInventory()) {
                slot.syncME(grid);
            }
            this.updateSubscription();
            configureWatchers();
        }
    }

    private void updateSubscription() {
        if (isWorkingEnabled() && getOnlineField()) {
            autoIOSubs = subscribeServerTick(autoIOSubs, this::autoIO, 40);
        } else if (autoIOSubs != null) {
            autoIOSubs.unsubscribe();
            autoIOSubs = null;
        }
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.@NotNull State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateSubscription();
    }

    @Override
    public void onDetailsPostInit() {
        super.onDetailsPostInit();
        for (InternalSlot slot : getInternalInventory()) {
            slot.reloadConfig();
        }
        configureWatchers();
    }

    @Override
    public @NotNull InternalSlot createInternalSlot(int i) {
        return new InternalSlot(this, i);
    }

    @Override
    public InternalSlot @NotNull [] createInternalSlotArray() {
        return new InternalSlot[getMaxPatternCount()];
    }

    @Override
    public @NotNull List<RecipeHandlerList> getRecipeHandlers() {
        return recipeHandlers;
    }

    @Override
    public @NotNull List<IPatternDetails> getAvailablePatterns() {
        return List.of();
    }

    @Override
    public boolean pushPattern(@NotNull IPatternDetails patternDetails, KeyCounter @NotNull [] inputHolder) {
        return false;
    }

    @Override
    public @NotNull Widget createUIWidget() {
        return MEInputBufferPartMachineUIKt.createUIWidgetFor(this);
    }

    @Override
    public void buildToolBoxContent(@NotNull VBoxBuilder $this$buildToolBoxContent) {
        MEInputBufferPartMachineUIKt.buildToolBoxContentFor($this$buildToolBoxContent, this);
    }

    @Override
    public boolean isBusy() {
        return true;
    }

    @Override
    public Set<AEKey> getEmitableItems() {
        return slot2WatcherMap.entrySet().stream()
                .filter(e -> e.getKey().isEmitterMode)
                .filter(e -> e.getValue() != null)
                .map(Map.Entry::getValue)
                .collect(ObjectOpenHashSet::new, Set::add, Set::addAll);
    }

    @Override
    public void onPatternChange(int index) {
        super.onPatternChange(index);
    }

    private void configureWatchers() {
        if (this.storageWatcher != null) {
            this.storageWatcher.reset();
        }

        if (this.craftingWatcher != null) {
            this.craftingWatcher.reset();
        }

        ICraftingProvider.requestUpdate(getMainNode());

        collectWatcherValues();

        updateState();
        onChanged();
    }

    private void updateState() {
        if (getController() instanceof WorkableMultiblockMachine w) {
            w.recipeLogic.updateTickSubscription();
        }
    }

    private void collectWatcherValues() {
        var slots = getInternalInventory();
        slot2WatcherMap.clear();
        watcher2SlotMap.clear();
        for (InternalSlot slot : slots) {
            if (slot == null || slot.reportingKey == null) continue;
            if (slot.isEmitterMode && craftingWatcher != null) {
                craftingWatcher.add(slot.reportingKey);
            }
            if (slot.minThreshold >= 0 && storageWatcher != null) {
                storageWatcher.add(slot.reportingKey);
            }
            slot2WatcherMap.put(slot, slot.reportingKey);
            watcher2SlotMap.put(slot.reportingKey, slot);
        }
    }

    @Override
    public boolean patternFilter(@NotNull ItemStack stack) {
        return stack.getItem() instanceof ProcessingPatternItem &&
                MEPatternPartMachineKtKt.checkDuplicatedPattern(this, stack);
    }

    public static final class InternalSlot extends AbstractInternalSlot implements ICraftingRequester {

        public final MEInputBufferPartMachine machine;
        public final int index;
        private Runnable onContentsChanged = () -> {};
        public boolean itemChanged = true;
        public boolean fluidChanged = true;

        @Persisted
        public final NotifiableNotConsumableItemHandler notConsumableItem;
        @Persisted
        public final NotifiableNotConsumableFluidHandler notConsumableFluid;
        @Persisted
        public final ExportOnlyAEItemList exportOnlyItemList;
        @Persisted
        public final ExportOnlyAEFluidList exportOnlyFluidList;
        public final NotifiableItemStackHandler circuitInventory;

        @Getter
        public final LockableItemStackHandler lockableInventory;

        public AEKey reportingKey = null;
        @Getter
        @Setter
        @Persisted
        public long minThreshold = -1;
        @Persisted
        private boolean isEmitterMode = false;
        @Persisted
        public boolean useRequest = false;

        MultiCraftingTracker craftingTracker = new MultiCraftingTracker(this, 32);

        private InternalSlot(MEInputBufferPartMachine machine, int index) {
            this.machine = machine;
            this.index = index;
            this.notConsumableItem = createShareInventory();
            this.notConsumableFluid = new NotifiableNotConsumableFluidHandler(machine, 9, 64000);

            this.exportOnlyItemList = new ExportOnlyAEItemList(machine, 16) {

                @Override
                public boolean isStocking() {
                    return true;
                }

                @Override
                public boolean isAutoPull() {
                    return true;
                }
            };
            this.exportOnlyFluidList = new ExportOnlyAEFluidList(machine, 16) {

                @Override
                public boolean isStocking() {
                    return true;
                }

                @Override
                public boolean isAutoPull() {
                    return true;
                }
            };

            this.circuitInventory = CircuitHandler.create(machine);
            this.lockableInventory = new LockableItemStackHandler(notConsumableItem.storage);
        }

        private NotifiableNotConsumableItemHandler createShareInventory() {
            var h = new NotifiableNotConsumableItemHandler(machine, 9, IO.NONE);
            h.setFilter(stack -> !(stack.getItem() instanceof EncodedPatternItem));
            return h;
        }

        public boolean isEmpty() {
            return exportOnlyItemList.isEmpty() && exportOnlyItemList.isEmpty();
        }

        private void refund() {
            var network = machine.getMainNode().getGrid();
            if (network != null) {
                MEStorage networkInv = network.getStorageService().getInventory();
                for (var aeSlot : exportOnlyItemList.getInventory()) {
                    GenericStack stock = aeSlot.getStock();
                    if (stock != null) {
                        networkInv.insert(stock.what(), stock.amount(), Actionable.MODULATE,
                                machine.getActionSourceField());
                    }
                }
                for (var aeTank : exportOnlyFluidList.getInventory()) {
                    GenericStack stock = aeTank.getStock();
                    if (stock != null) {
                        networkInv.insert(stock.what(), stock.amount(), Actionable.MODULATE,
                                machine.getActionSourceField());
                    }
                }
                itemChanged = true;
                fluidChanged = true;
                onContentsChanged.run();
            }
        }

        @Override
        public void onPatternChange() {
            refund();
            reloadConfig();
        }

        public boolean isEmitterMode() {
            if (reportingKey == null) return false;
            return isEmitterMode;
        }

        public void setEmitterMode(boolean emitterMode) {
            isEmitterMode = emitterMode;
            ICraftingProvider.requestUpdate(machine.getMainNode());
        }

        private void reloadConfig() {
            final var oldWatcher = reportingKey;
            if (oldWatcher != null) {
                machine.watcher2SlotMap.remove(oldWatcher, this);
                machine.slot2WatcherMap.remove(this);
            }
            var newPattern = machine.getInternalPatternInventory().getStackInSlot(index);
            var details = machine.decodePattern(newPattern, index);
            if (details instanceof AEProcessingPattern aeProcessingPattern) {
                reportingKey = aeProcessingPattern.getPrimaryOutput().what();
                machine.watcher2SlotMap.put(reportingKey, this);
                machine.slot2WatcherMap.put(this, reportingKey);
                int itemIdx = 0, fluidIdx = 0;
                for (var ingredient : aeProcessingPattern.getSparseInputs()) {
                    var key = ingredient.what();
                    if (key instanceof AEItemKey) {
                        if (itemIdx >= exportOnlyItemList.getInventory().length) continue;
                        exportOnlyItemList.getInventory()[itemIdx++].setConfig(ingredient);
                    } else if (key instanceof AEFluidKey) {
                        if (fluidIdx >= exportOnlyFluidList.getInventory().length) continue;
                        exportOnlyFluidList.getInventory()[fluidIdx++].setConfig(ingredient);
                    }
                }
            }
        }

        private void clearConfig() {
            for (var slot : exportOnlyItemList.getInventory()) {
                slot.setConfig(null);
            }
            for (var slot : exportOnlyFluidList.getInventory()) {
                slot.setConfig(null);
            }
        }

        private boolean shouldSync(IGrid grid) {
            if (reportingKey == null) {
                return false;
            }
            if (isEmitterMode) {
                return grid.getCraftingService().isRequesting(reportingKey);
            }
            if (minThreshold < 0) {
                return true;
            }
            var last = grid.getStorageService().getCachedInventory().get(reportingKey);
            return last < minThreshold;
        }

        private boolean disconnected = false;

        private void syncME(@NotNull IGrid grid) {
            if (!shouldSync(grid)) {
                if (disconnected) {
                    return;
                }
                disconnected = true;
                clearConfig();
            } else {
                if (disconnected) {
                    reloadConfig();
                }
                disconnected = false;
            }
            var cg = grid.getCraftingService();
            MEStorage networkInv = grid.getStorageService().getInventory();
            for (int i = 0; i < exportOnlyItemList.getSlots(); i++) {
                var aeSlot = exportOnlyItemList.getInventory()[i];
                GenericStack exceedItem = aeSlot.exceedStack();
                if (exceedItem != null) {
                    long total = exceedItem.amount();
                    long inserted = networkInv.insert(exceedItem.what(), exceedItem.amount(), Actionable.MODULATE, machine.getActionSourceField());
                    if (inserted > 0) {
                        aeSlot.extractItem(inserted, false, true);
                        continue;
                    } else {
                        aeSlot.extractItem(total, false, true);
                    }
                }
                GenericStack reqItem = aeSlot.requestStack();
                if (reqItem != null) {
                    long extracted = networkInv.extract(reqItem.what(), reqItem.amount(), Actionable.MODULATE, machine.getActionSourceField());
                    if (useRequest && extracted < reqItem.amount()) {
                        craftingTracker.handleCrafting(i, reqItem.what(), reqItem.amount() - extracted,
                                machine.getLevel(), cg, machine.getActionSourceField());
                    }
                    if (extracted != 0) {
                        aeSlot.addStack(new GenericStack(reqItem.what(), extracted));
                    }
                }
            }
            for (int i = 0; i < exportOnlyFluidList.getTanks(); i++) {
                ExportOnlyAEFluidSlot aeTank = exportOnlyFluidList.getInventory()[i];
                GenericStack exceedFluid = aeTank.exceedStack();
                if (exceedFluid != null) {
                    long total = exceedFluid.amount();
                    long inserted = networkInv.insert(exceedFluid.what(), exceedFluid.amount(), Actionable.MODULATE, machine.getActionSourceField());
                    if (inserted > 0) {
                        aeTank.drain(inserted, false, true);
                        continue;
                    } else {
                        aeTank.drain(total, false, true);
                    }
                }
                GenericStack reqFluid = aeTank.requestStack();
                if (reqFluid != null) {
                    long extracted = networkInv.extract(reqFluid.what(), reqFluid.amount(), Actionable.MODULATE, machine.getActionSourceField());
                    if (useRequest && extracted < reqFluid.amount()) {
                        craftingTracker.handleCrafting(i + exportOnlyItemList.getSlots(), reqFluid.what(), reqFluid.amount() - extracted,
                                machine.getLevel(), cg, machine.getActionSourceField());
                    }
                    if (extracted > 0) {
                        aeTank.addStack(new GenericStack(reqFluid.what(), extracted));
                    }
                }
            }
        }

        @Override
        public @NotNull CompoundTag serializeNBT() {
            CompoundTag tag = super.serializeNBT();
            if (!notConsumableItem.isEmpty()) tag.put("inv", notConsumableItem.storage.serializeNBT());
            if (!notConsumableFluid.isEmpty()) {
                ListTag tanks = new ListTag();
                for (var tank : notConsumableFluid.getStorages()) {
                    if (tank.isEmpty()) {
                        tanks.add(new CompoundTag());
                    } else tanks.add(tank.serializeNBT());
                }
                tag.put("tank", tanks);
            }
            ListTag exportItems = new ListTag();
            for (var slot : exportOnlyItemList.getInventory()) {
                exportItems.add(slot.serializeNBT());
            }
            tag.put("exI", exportItems);
            ListTag exportFluids = new ListTag();
            for (var slot : exportOnlyFluidList.getInventory()) {
                exportFluids.add(slot.serializeNBT());
            }
            tag.put("exF", exportFluids);
            tag.putBoolean("emitterMode", isEmitterMode);
            tag.putBoolean("useRequest", useRequest);
            tag.putLong("minThreshold", minThreshold);
            var c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.storage.getStackInSlot(0));
            if (c > 0) tag.putInt("c", c);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            if (tag.tags.get("inv") instanceof CompoundTag inv) {
                notConsumableItem.storage.deserializeNBT(inv);
            }
            if (tag.tags.get("tank") instanceof ListTag tanks) {
                for (int i = 0; i < tanks.size(); i++) {
                    var t = tanks.getCompound(i);
                    if (t.isEmpty()) continue;
                    var tank = notConsumableFluid.getStorages()[i];
                    tank.deserializeNBT(t);
                }
            }
            if (tag.tags.get("exI") instanceof ListTag exportItems) {
                var slots = exportOnlyItemList.getInventory();
                for (int i = 0; i < exportItems.size() && i < slots.length; i++) {
                    var t = exportItems.getCompound(i);
                    slots[i].deserializeNBT(t);
                }
            }
            if (tag.tags.get("exF") instanceof ListTag exportFluids) {
                var slots = exportOnlyFluidList.getInventory();
                for (int i = 0; i < exportFluids.size() && i < slots.length; i++) {
                    var t = exportFluids.getCompound(i);
                    slots[i].deserializeNBT(t);
                }
            }
            if (tag.tags.get("emitterMode") instanceof ByteTag emitterMode) {
                isEmitterMode = emitterMode.getAsByte() != 0;
            }
            if (tag.tags.get("useRequest") instanceof ByteTag useReq) {
                this.useRequest = useReq.getAsByte() != 0;
            }
            if (tag.tags.get("minThreshold") instanceof LongTag minThres) {
                this.minThreshold = minThres.getAsLong();
            }
            var c = tag.getInt("c");
            if (c > 0) circuitInventory.storage.setStackInSlot(0, IntCircuitBehaviour.stack(c));
        }

        @Override
        public void setOnContentsChanged(final Runnable onContentsChanged) {
            this.onContentsChanged = onContentsChanged;
        }

        @Override
        public Runnable getOnContentsChanged() {
            return this.onContentsChanged;
        }

        @Override
        public boolean pushPattern(@NotNull IPatternDetails patternDetails, @NotNull KeyCounter @NotNull [] inputHolder) {
            return false;
        }

        @Override
        public ImmutableSet<ICraftingLink> getRequestedJobs() {
            return craftingTracker.getRequestedJobs();
        }

        @Override
        public long insertCraftedItems(ICraftingLink link, AEKey what, long amount, Actionable mode) {
            return 0;
        }

        @Override
        public void jobStateChange(ICraftingLink link) {
            craftingTracker.jobStateChange(link);
            machine.updateSubscription();
        }

        @Override
        public @Nullable IGridNode getActionableNode() {
            return machine.getActionableNode();
        }
    }

    private static final class SlotRHL extends ExtendedRecipeHandlerList {

        final InternalSlot slot;

        SlotRHL(InternalSlot slot, MEInputBufferPartMachine part) {
            super(IO.IN, part);
            addHandlers(slot.notConsumableItem, slot.notConsumableFluid, slot.circuitInventory, slot.exportOnlyItemList, slot.exportOnlyFluidList);
            this.slot = slot;
        }

        @Override
        public boolean findRecipe(IRecipeCapabilityHolder holder, GTRecipeType recipeType, Predicate<GTRecipeDefinition> canHandle) {
            if (slot.isEmpty() || !(holder instanceof IRecipeLogicMachine)) return false;
            var map = this.getIngredientMap(recipeType);
            if (map.isEmpty()) return false;
            holder.setCurrentHandlerList(this);
            return recipeType.search(map, canHandle);
        }

        @Override
        public ExtendedRecipeHandlerList wrapper() {
            return new SlotRHL(slot, (MEInputBufferPartMachine) part);
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        private Reference2LongOpenHashMap<Item> getItemMap(ParallelCache parallelCache) {
            var ingredientStacks = parallelCache.getItemIngredientMap();
            for (var container : getCapability(ItemRecipeCapability.CAP)) {
                if (container.isNotConsumable() || (container instanceof NonStandardHandler handler && handler.isNonStandardHandler())) continue;
                container.fastForEachItems((a, b) -> ingredientStacks.addTo(a.getItem(), b));
            }
            return ingredientStacks;
        }

        @Override
        public long getInputItemParallel(IRecipeLogicMachine holder, List<Content> contents, long parallelAmount) {
            ParallelCache parallelCache = IEnhancedRecipeLogic.of(holder.getRecipeLogic()).gtolib$getParallelCache();
            Reference2LongOpenHashMap<Item> ingredientStacks = null;
            for (var content : contents) {
                if (content.chance > 0 && content.inner instanceof ItemIngredient ingredient) {
                    long needed = ingredient.getAmount();
                    if (needed < 1) continue;
                    long available = 0;
                    for (var it : slot.exportOnlyItemList.getInventory()) {
                        if (ingredient.testItem(it.getReadOnlyStack().getItem())) {
                            if (it.getStock() != null) {
                                available += it.getStock().amount();
                            }
                            if (available >= needed) break;
                        }
                    }
                    if (available < needed) {
                        if (ingredientStacks == null) ingredientStacks = getItemMap(parallelCache);
                        for (var iter = ingredientStacks.reference2LongEntrySet().fastIterator(); iter.hasNext();) {
                            var inventoryEntry = iter.next();
                            if (ingredient.testItem(inventoryEntry.getKey())) {
                                available += inventoryEntry.getLongValue();
                                if (available >= needed) break;
                            }
                        }
                    }
                    if (available >= needed) {
                        parallelAmount = Math.min(parallelAmount, available / needed);
                    } else {
                        parallelAmount = 0;
                        break;
                    }
                }
            }
            parallelCache.cleanItemMap();
            return parallelAmount;
        }

        @Override
        public long getInputFluidParallel(IRecipeLogicMachine holder, List<Content> contents, long parallelAmount) {
            for (var content : contents) {
                if (content.chance > 0 && content.inner instanceof FluidIngredient ingredient) {
                    long needed = ingredient.amount;
                    if (needed < 1) continue;
                    long available = 0;
                    for (var it : slot.exportOnlyFluidList.getInventory()) {
                        if (ingredient.testFluid(it.getReadOnlyStack().getFluid())) {
                            if (it.getStock() != null) {
                                available += it.getStock().amount();
                            }
                            if (available >= needed) break;
                        }
                    }
                    if (available >= needed) {
                        parallelAmount = Math.min(parallelAmount, available / needed);
                    } else {
                        parallelAmount = 0;
                        break;
                    }
                }
            }
            return parallelAmount;
        }

        @Override
        public boolean handleRecipeContent(IO io, GTRecipe recipe, RecipeCapabilityMap<List<Object>> contents, boolean simulate, boolean distinct) {
            if (slot.isEmpty()) return false;
            boolean item = contents.item == null;
            if (!item) {
                List left = contents.item;
                for (var handler : getCapability(ItemRecipeCapability.CAP)) {
                    left = handler.handleRecipe(IO.IN, recipe, left, simulate);
                    if (left == null) {
                        item = true;
                        break;
                    }
                }
            }
            if (item) {
                if (contents.fluid == null) {
                    return true;
                } else {
                    List left = contents.fluid;
                    for (var handler : getCapability(FluidRecipeCapability.CAP)) {
                        left = handler.handleRecipe(IO.IN, recipe, left, simulate);
                        if (left == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
