package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidSlot;
import com.gtocore.common.machine.multiblock.part.ae.slots.MECircuitHandler;
import com.gtocore.common.machine.multiblock.part.ae.widget.AEFluidConfigWidget;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
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
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEInputHatchPartMachine extends MEPartMachine implements IDataStickInteractable {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEInputHatchPartMachine.class, MEPartMachine.Companion.getMANAGED_FIELD_HOLDER());

    @Nullable
    private TickableSubscription autoIOSubs;

    @Persisted
    final ExportOnlyAEFluidList aeFluidHandler;

    @Persisted
    protected final NotifiableItemStackHandler circuitInventory;

    public MEInputHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.IN);
        aeFluidHandler = createTank();
        circuitInventory = new MECircuitHandler(this);
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
        updateTankSubscription();
    }

    ExportOnlyAEFluidList createTank() {
        return new ExportOnlyAEFluidList(this, CONFIG_SIZE);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateTankSubscription();
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    private void autoIO() {
        if (!this.shouldSyncME()) return;

        if (this.updateMEStatus()) {
            this.syncME();
            this.updateTankSubscription();
        }
    }

    void syncME() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            return;
        }
        MEStorage networkInv = grid.getStorageService().getInventory();
        for (ExportOnlyAEFluidSlot aeTank : this.aeFluidHandler.getInventory()) {
            GenericStack exceedFluid = aeTank.exceedStack();
            if (exceedFluid != null) {
                long total = exceedFluid.amount();
                long inserted = networkInv.insert(exceedFluid.what(), exceedFluid.amount(), Actionable.MODULATE, this.getActionSourceField());
                if (inserted > 0) {
                    aeTank.drain(inserted, false, true);
                    continue;
                } else {
                    aeTank.drain(total, false, true);
                }
            }
            GenericStack reqFluid = aeTank.requestStack();
            if (reqFluid != null) {
                long extracted = networkInv.extract(reqFluid.what(), reqFluid.amount(), Actionable.MODULATE, this.getActionSourceField());
                if (extracted > 0) {
                    aeTank.addStack(new GenericStack(reqFluid.what(), extracted));
                }
            }
        }
    }

    void updateTankSubscription() {
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
            for (var aeSlot : aeFluidHandler.getInventory()) {
                GenericStack stock = aeSlot.getStock();
                if (stock != null) {
                    grid.getStorageService().getInventory().insert(stock.what(), stock.amount(), Actionable.MODULATE,
                            getActionSourceField());
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
        group.addWidget(new AEFluidConfigWidget(3, 10, this.aeFluidHandler));

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
            this.updateTankSubscription();
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
            var slot = this.aeFluidHandler.getInventory()[i];
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
                    this.aeFluidHandler.getInventory()[i].setConfig(GenericStack.readTag(configTag));
                } else {
                    this.aeFluidHandler.getInventory()[i].setConfig(null);
                }
            }
        }
    }

    private String getConfigKey() {
        return this.getDefinition().getId().getPath();
    }
}
