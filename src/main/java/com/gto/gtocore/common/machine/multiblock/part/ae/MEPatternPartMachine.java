package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.machine.trait.NotifiableNotConsumableItemHandler;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEPatternViewSlotWidget;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
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
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
abstract class MEPatternPartMachine<T extends MEPatternPartMachine.AbstractInternalSlot> extends MEBusPartMachine implements ICraftingProvider, PatternContainer {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEPatternPartMachine.class, MEBusPartMachine.MANAGED_FIELD_HOLDER);

    final int maxPatternCount;

    private final InternalInventory internalPatternInventory = new InternalInventory() {

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
    private final NotifiableNotConsumableItemHandler circuitInventorySimulated;

    @Persisted
    private final AbstractInternalSlot[] internalInventory;

    final BiMap<IPatternDetails, T> detailsSlotMap;

    List<IPatternDetails> patterns = List.of();

    @DescSynced
    @Persisted
    @Setter
    private String customName = "";

    private boolean needPatternSync;

    @Nullable
    private TickableSubscription updateSubs;

    MEPatternPartMachine(IMachineBlockEntity holder, int maxPatternCount) {
        super(holder, IO.IN);
        this.patternInventory = new CustomItemStackHandler(maxPatternCount);
        this.patternInventory.setFilter(this::patternFilter);
        this.circuitInventorySimulated = createCircuitInventory();
        this.internalInventory = createInternalSlotArray();
        this.detailsSlotMap = HashBiMap.create(maxPatternCount);
        this.maxPatternCount = maxPatternCount;
        for (int i = 0; i < this.internalInventory.length; i++) {
            this.internalInventory[i] = createInternalSlot();
        }
        getMainNode().addService(ICraftingProvider.class, this);
        circuitSlotEnabled = false;
    }

    @SuppressWarnings("unchecked")
    public T[] getInternalInventory() {
        return (T[]) internalInventory;
    }

    abstract T[] createInternalSlotArray();

    abstract boolean patternFilter(ItemStack stack);

    abstract T createInternalSlot();

    NotifiableNotConsumableItemHandler createCircuitInventory() {
        NotifiableNotConsumableItemHandler handle = new NotifiableNotConsumableItemHandler(this, 1, IO.NONE);
        handle.setFilter(IntCircuitBehaviour::isIntegratedCircuit);
        return handle;
    }

    private void updatePatterns() {
        patterns = detailsSlotMap.keySet().stream().filter(Objects::nonNull).toList();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(1, () -> {
                for (int i = 0; i < patternInventory.getSlots(); i++) {
                    var pattern = patternInventory.getStackInSlot(i);
                    var patternDetails = PatternDetailsHelper.decodePattern(pattern, getLevel());
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
        return List.of();
    }

    @Override
    protected RecipeHandlerList getHandlerList() {
        return RecipeHandlerList.NO_DATA;
    }

    @Override
    public NotifiableItemStackHandler getCircuitInventory() {
        return circuitInventorySimulated;
    }

    @Override
    public boolean canShared() {
        return false;
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

    private void onPatternChange(int index) {
        if (isRemote()) return;

        T internalInv = getInternalInventory()[index];
        var newPattern = patternInventory.getStackInSlot(index);
        var newPatternDetails = PatternDetailsHelper.decodePattern(newPattern, getLevel());
        var oldPatternDetails = detailsSlotMap.inverse().get(internalInv);
        detailsSlotMap.forcePut(newPatternDetails, internalInv);
        if (oldPatternDetails != null && !oldPatternDetails.equals(newPatternDetails)) {
            internalInv.refund();
        }
        updatePatterns();
        needPatternSync = true;
    }

    //////////////////////////////////////
    // ********** GUI ***********//
    //////////////////////////////////////
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
                var slot = new AEPatternViewSlotWidget(patternInventory, index++, 8 + x * 18, 14 + y * 18)
                        .setOccupiedTexture(GuiTextures.SLOT)
                        .setItemHook(stack -> {
                            if (!stack.isEmpty() && stack.getItem() instanceof EncodedPatternItem iep) {
                                final ItemStack out = iep.getOutput(stack);
                                if (!out.isEmpty()) {
                                    return out;
                                }
                            }
                            return stack;
                        })
                        .setChangeListener(() -> onPatternChange(finalI))
                        .setBackground(GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY);
                group.addWidget(slot);
            }
        }
        group.addWidget(new LabelWidget(
                8,
                2,
                () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));

        group.addWidget(new AETextInputButtonWidget(18 * rowSize + 8 - 70, 2, 70, 10)
                .setText(customName)
                .setOnConfirm(this::setCustomName)
                .setButtonTooltips(Component.translatable("gui.gtceu.rename.desc")));

        return group;
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return patterns;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (!isFormed() || !getMainNode().isActive()) return false;
        var slot = detailsSlotMap.get(patternDetails);
        if (slot != null) {
            return slot.pushPattern(patternDetails, inputHolder, i -> false);
        }
        return false;
    }

    @Override
    public boolean isBusy() {
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
    public @Nullable IGrid getGrid() {
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
                return new PatternContainerGroup(
                        AEItemKey.of(controllerDefinition.asStack()),
                        Component.literal(customName),
                        Collections.emptyList());
            } else {
                ItemStack circuitStack = circuitInventorySimulated.storage.getStackInSlot(0);
                int circuitConfiguration = circuitStack.isEmpty() ? -1 :
                        IntCircuitBehaviour.getCircuitConfiguration(circuitStack);

                Component groupName = circuitConfiguration != -1 ?
                        Component.translatable(controllerDefinition.getDescriptionId())
                                .append(" - " + circuitConfiguration) :
                        Component.translatable(controllerDefinition.getDescriptionId());

                return new PatternContainerGroup(
                        AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
            }
        } else {
            if (!customName.isEmpty()) {
                return new PatternContainerGroup(
                        AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()),
                        Component.literal(customName),
                        Collections.emptyList());
            } else {
                return new PatternContainerGroup(
                        AEItemKey.of(GTAEMachines.ME_PATTERN_BUFFER.getItem()),
                        GTAEMachines.ME_PATTERN_BUFFER.get().getDefinition().getItem().getDescription(),
                        Collections.emptyList());
            }
        }
    }

    abstract static class AbstractInternalSlot implements ITagSerializable<CompoundTag>, IContentChangeAware {

        abstract boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder, Predicate<ItemStack> itemCallback);

        abstract void refund();
    }
}
