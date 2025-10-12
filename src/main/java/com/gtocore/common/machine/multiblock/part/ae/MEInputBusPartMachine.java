package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemSlot;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEItemConfigWidget;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNodeListener;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEInputBusPartMachine extends MEPartMachine implements IDataStickInteractable {

    private TickableSubscription autoIOSubs;

    @Persisted
    final ExportOnlyAEItemList aeItemHandler;

    @Persisted
    protected final NotifiableItemStackHandler circuitInventory;

    public MEInputBusPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.IN);
        aeItemHandler = createInventory();
        circuitInventory = CircuitHandler.create(this);
    }

    /////////////////////////////////
    // ***** Machine LifeCycle ****//
    /////////////////////////////////

    @Override
    public void onMachineRemoved() {
        flushInventory();
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        updateInventorySubscription();
    }

    ExportOnlyAEItemList createInventory() {
        return new ExportOnlyAEItemList(this, CONFIG_SIZE);
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateInventorySubscription();
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    private void autoIO() {
        if (!this.shouldSyncME()) return;

        if (this.updateMEStatus()) {
            this.syncME();
            this.updateInventorySubscription();
        }
    }

    void syncME() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            return;
        }
        MEStorage networkInv = grid.getStorageService().getInventory();
        for (ExportOnlyAEItemSlot aeSlot : this.aeItemHandler.getInventory()) {
            GenericStack exceedItem = aeSlot.exceedStack();
            if (exceedItem != null) {
                long total = exceedItem.amount();
                long inserted = networkInv.insert(exceedItem.what(), exceedItem.amount(), Actionable.MODULATE, this.getActionSourceField());
                if (inserted > 0) {
                    aeSlot.extractItem(inserted, false, true);
                    continue;
                } else {
                    aeSlot.extractItem(total, false, true);
                }
            }
            GenericStack reqItem = aeSlot.requestStack();
            if (reqItem != null) {
                long extracted = networkInv.extract(reqItem.what(), reqItem.amount(), Actionable.MODULATE, this.getActionSourceField());
                if (extracted != 0) {
                    aeSlot.addStack(new GenericStack(reqItem.what(), extracted));
                }
            }
        }
    }

    void updateInventorySubscription() {
        if (isWorkingEnabled() && getOnlineField()) {
            autoIOSubs = subscribeServerTick(autoIOSubs, this::autoIO);
        } else if (autoIOSubs != null) {
            autoIOSubs.unsubscribe();
            autoIOSubs = null;
        }
    }

    void flushInventory() {
        var grid = getMainNode().getGrid();
        if (grid != null) {
            for (var aeSlot : aeItemHandler.getInventory()) {
                GenericStack stock = aeSlot.getStock();
                if (stock != null) {
                    grid.getStorageService().getInventory().insert(stock.what(), stock.amount(), Actionable.MODULATE, getActionSourceField());
                }
            }
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(circuitInventory.storage));
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        // ME Network status
        group.addWidget(new LabelWidget(3, 0, () -> this.getOnlineField() ?
                "gtceu.gui.me_network.online" :
                "gtceu.gui.me_network.offline"));

        // Config slots
        group.addWidget(new AEItemConfigWidget(3, 10, this.aeItemHandler));

        return group;
    }

    ////////////////////////////////
    // ******* Interaction *******//
    ////////////////////////////////

    @Override
    public final InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        if (!isRemote()) {
            CompoundTag tag = new CompoundTag();
            tag.put(this.getConfigKey(), writeConfigToTag());
            dataStick.setTag(tag);
            dataStick.setHoverName(Component.translatable("gtceu.machine.me.import_part.data_stick.name", Component.translatable(this.getDefinition().getDescriptionId())));
            player.sendSystemMessage(Component.translatable("gtceu.machine.me.import_copy_settings"));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public final InteractionResult onDataStickUse(Player player, ItemStack dataStick) {
        CompoundTag tag = dataStick.getTag();
        if (tag == null || !tag.contains(this.getConfigKey())) {
            return InteractionResult.PASS;
        }

        if (!isRemote()) {
            readConfigFromTag(tag.getCompound(this.getConfigKey()));
            this.updateInventorySubscription();
            player.sendSystemMessage(Component.translatable("gtceu.machine.me.import_paste_settings"));
        }
        return InteractionResult.sidedSuccess(isRemote());
    }

    ////////////////////////////////
    // ****** Configuration ******//
    ////////////////////////////////

    CompoundTag writeConfigToTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag configStacks = new CompoundTag();
        tag.putBoolean("DistinctBuses", isDistinct());
        if (!circuitInventory.storage.getStackInSlot(0).isEmpty()) {
            tag.putByte("GhostCircuit", (byte) IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.storage.getStackInSlot(0)));
        }
        tag.put("ConfigStacks", configStacks);
        for (int i = 0; i < CONFIG_SIZE; i++) {
            var slot = this.aeItemHandler.getInventory()[i];
            GenericStack config = slot.getConfig();
            if (config == null) {
                continue;
            }
            CompoundTag stackTag = GenericStack.writeTag(config);
            configStacks.put(Integer.toString(i), stackTag);
        }
        return tag;
    }

    void readConfigFromTag(CompoundTag tag) {
        if (tag.contains("DistinctBuses")) {
            setDistinct(tag.getBoolean("DistinctBuses"));
        }
        if (tag.contains("GhostCircuit")) {
            circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.getByte("GhostCircuit")));
        } else {
            circuitInventory.setStackInSlot(0, ItemStack.EMPTY);
        }
        if (tag.contains("ConfigStacks")) {
            CompoundTag configStacks = tag.getCompound("ConfigStacks");
            for (int i = 0; i < CONFIG_SIZE; i++) {
                String key = Integer.toString(i);
                if (configStacks.contains(key)) {
                    CompoundTag configTag = configStacks.getCompound(key);
                    this.aeItemHandler.getInventory()[i].setConfig(GenericStack.readTag(configTag));
                } else {
                    this.aeItemHandler.getInventory()[i].setConfig(null);
                }
            }
        }
    }

    private String getConfigKey() {
        return this.getDefinition().getId().getPath();
    }
}
