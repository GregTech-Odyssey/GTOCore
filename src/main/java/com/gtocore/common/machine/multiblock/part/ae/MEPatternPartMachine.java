package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.data.machines.GTAEMachines;

import com.gtolib.ae2.MyPatternDetailsHelper;
import com.gtolib.ae2.pattern.IParallelPatternDetails;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEPatternViewSlotWidget;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
abstract class MEPatternPartMachine<T extends MEPatternPartMachine.AbstractInternalSlot> extends MEBusPartMachine implements ICraftingProvider, PatternContainer {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEPatternPartMachine.class, MEBusPartMachine.MANAGED_FIELD_HOLDER);
    public final int maxPatternCount;
    final InternalInventory internalPatternInventory = new InternalInventory() {

        @Override
        public int size() {
            return maxPatternCount;
        }

        @Override
        public ItemStack getStackInSlot(int slotIndex) {
            return patternInventory.getStackInSlot(slotIndex);
        }

        @Override
        public void setItemDirect(int slotIndex, ItemStack stack) {
            patternInventory.setStackInSlot(slotIndex, stack);
            patternInventory.onContentsChanged(slotIndex);
            onPatternChange(slotIndex);
        }
    };
    @Persisted
    @DescSynced
    private final CustomItemStackHandler patternInventory;
    @Persisted
    private final AbstractInternalSlot[] internalInventory;
    final BiMap<IPatternDetails, T> detailsSlotMap;
    private List<IPatternDetails> patterns = Collections.emptyList();
    @DescSynced
    @Persisted
    protected String customName = "";
    private boolean needPatternSync;
    @Nullable
    private TickableSubscription updateSubs;

    MEPatternPartMachine(IMachineBlockEntity holder, int maxPatternCount) {
        super(holder, IO.IN);
        this.maxPatternCount = maxPatternCount;
        this.patternInventory = new CustomItemStackHandler(maxPatternCount);
        this.patternInventory.setFilter(this::patternFilter);
        this.internalInventory = createInternalSlotArray();
        this.detailsSlotMap = HashBiMap.create(maxPatternCount);
        for (int i = 0; i < this.internalInventory.length; i++) {
            this.internalInventory[i] = createInternalSlot(i);
        }
        getMainNode().addService(ICraftingProvider.class, this);
    }

    @SuppressWarnings("unchecked")
    public T[] getInternalInventory() {
        return (T[]) internalInventory;
    }

    abstract T[] createInternalSlotArray();

    abstract boolean patternFilter(ItemStack stack);

    abstract T createInternalSlot(int i);

    private void updatePatterns() {
        patterns = detailsSlotMap.keySet().stream().filter(Objects::nonNull).toList();
        needPatternSync = true;
    }

    @Nullable
    private IPatternDetails decodePattern(ItemStack stack) {
        return IParallelPatternDetails.of(MyPatternDetailsHelper.decodePattern(stack, holder.getSelf(), getGrid()), getLevel(), 1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(1, () -> {
                var slots = patternInventory.getSlots();
                for (int i = 0; i < slots; i++) {
                    var pattern = patternInventory.getStackInSlot(i);
                    var patternDetails = decodePattern(pattern);
                    if (patternDetails != null) {
                        detailsSlotMap.put(patternDetails, getInternalInventory()[i]);
                    }
                }
                updatePatterns();
            }));
        }
    }

    @Override
    public List<RecipeHandlerList> getRecipeHandlers() {
        return Collections.emptyList();
    }

    @Override
    protected RecipeHandlerList getHandlerList() {
        return RecipeHandlerList.NO_DATA;
    }

    @Override
    public boolean isWorkingEnabled() {
        return true;
    }

    @Override
    public void setWorkingEnabled(boolean ignored) {}

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void setDistinct(boolean ignored) {}

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateSubscription();
    }

    private void updateSubscription() {
        if (getMainNode().isOnline()) {
            updateSubs = subscribeServerTick(updateSubs, this::update);
        } else if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }

    private void update() {
        if (needPatternSync) {
            ICraftingProvider.requestUpdate(getMainNode());
            this.needPatternSync = false;
        }
    }

    void onPatternChange(int index) {
        if (isRemote()) return;
        T internalInv = getInternalInventory()[index];
        var newPattern = patternInventory.getStackInSlot(index);
        var newPatternDetails = decodePattern(newPattern);
        var oldPatternDetails = detailsSlotMap.inverse().get(internalInv);
        detailsSlotMap.forcePut(newPatternDetails, internalInv);
        if (oldPatternDetails != null && !oldPatternDetails.equals(newPatternDetails)) {
            internalInv.onPatternChange();
        }
        updatePatterns();
    }

    @Nullable
    Component appendHoverTooltips(int index) {
        return null;
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {}

    @Override
    public Widget createUIWidget() {
        int rowSize = 9;
        int colSize = maxPatternCount / 9;
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
        int index = 0;
        for (int y = 0; y < colSize; ++y) {
            for (int x = 0; x < rowSize; ++x) {
                int finalI = index;
                var slot = new AEPatternViewSlotWidget(patternInventory, index++, 8 + x * 18, 14 + y * 18).setOccupiedTexture(GuiTextures.SLOT).setItemHook(stack -> {
                    if (stack.getItem() instanceof EncodedPatternItem iep) {
                        final ItemStack out = iep.getOutput(stack);
                        if (!out.isEmpty()) {
                            return out;
                        }
                    }
                    return stack;
                }).setChangeListener(() -> onPatternChange(finalI)).setOnAddedTooltips((s, l) -> {
                    var c = appendHoverTooltips(finalI);
                    if (c != null) l.add(c);
                }).setBackground(GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY);
                group.addWidget(slot);
            }
        }
        group.addWidget(new LabelWidget(8, 2, () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        group.addWidget(new AETextInputButtonWidget(18 * rowSize + 8 - 70, 2, 70, 10).setText(customName).setOnConfirm(this::setCustomName).setButtonTooltips(Component.translatable("gui.expatternprovider.renamer")));
        return group;
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return patterns;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (!getMainNode().isActive()) return false;
        var slot = detailsSlotMap.get(patternDetails);
        if (slot != null) {
            return slot.pushPattern(patternDetails, inputHolder);
        }
        return false;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    boolean shouldSubscribe() {
        return false;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(patternInventory);
    }

    @Override
    @Nullable
    public IGrid getGrid() {
        return getMainNode().getGrid();
    }

    @Override
    public InternalInventory getTerminalPatternInventory() {
        return internalPatternInventory;
    }

    @Override
    public PatternContainerGroup getTerminalGroup() {
        if (isFormed()) {
            IMultiController controller = getControllers().first();
            MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.literal(customName), Collections.emptyList());
            } else {
                return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.translatable(controllerDefinition.getDescriptionId()), Collections.emptyList());
            }
        } else {
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()), Component.literal(customName), Collections.emptyList());
            } else {
                return new PatternContainerGroup(AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()), GTAEMachines.ME_PATTERN_BUFFER.get().getDefinition().getItem().getDescription(), Collections.emptyList());
            }
        }
    }

    static abstract class AbstractInternalSlot implements ITagSerializable<CompoundTag>, IContentChangeAware {

        abstract boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder);

        abstract void onPatternChange();

        @Override
        public CompoundTag serializeNBT() {
            return new CompoundTag();
        }
    }

    private void setCustomName(final String customName) {
        this.customName = customName;
    }
}
