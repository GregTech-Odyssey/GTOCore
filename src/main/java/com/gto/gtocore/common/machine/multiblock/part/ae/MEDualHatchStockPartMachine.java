package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.gui.TurnsConfiguratorButton;
import com.gto.gtocore.client.gui.widget.AEDualConfigWidget;
import com.gto.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEStockingFluidList;
import com.gto.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEStockingItemList;
import com.gto.gtocore.common.machine.multiblock.part.ae.slots.NetworkSlotMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.*;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author EasterFG on 2025/3/2
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MEDualHatchStockPartMachine extends MEBusPartMachine implements NetworkSlotMachine, IDataStickInteractable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEDualHatchStockPartMachine.class,
            MEBusPartMachine.MANAGED_FIELD_HOLDER);

    private static final int CONFIG_SIZE = 64;
    private static final int AUTO_PULL_OFF = 0;
    private static final int AUTO_PULL_ALL = 1;
    private static final int AUTO_PULL_ITEM = 2;
    private static final int AUTO_PULL_FLUID = 3;

    private static final IGuiTexture AUTO_PULL_ALL_ICON = new TextTexture("ALL", 0xFFAA00);
    private static final IGuiTexture AUTO_PULL_ITEM_ICON = new ItemStackTexture(Items.IRON_INGOT);
    private static final IGuiTexture AUTO_PULL_FLUID_ICON = new ItemStackTexture(Items.WATER_BUCKET);

    private ExportOnlyAEItemList aeItemHandler;

    private ExportOnlyAEFluidList aeFluidHandler;

    @Persisted
    private final NotifiableFluidTank fluidTank;

    @Setter
    private int page = 1;

    @DescSynced
    @Persisted
    @Getter
    private int autoPullMode;

    public MEDualHatchStockPartMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, IO.IN, args);
        fluidTank = createTank();
    }

    @Override
    protected NotifiableItemStackHandler createInventory(Object... args) {
        this.aeItemHandler = new ExportOnlyAEStockingItemList(this, CONFIG_SIZE) {

            @Override
            public boolean isAutoPull() {
                return autoPullMode > 0;
            }
        };
        return this.aeItemHandler;
    }

    private NotifiableFluidTank createTank() {
        this.aeFluidHandler = new ExportOnlyAEStockingFluidList(this, CONFIG_SIZE) {

            @Override
            public boolean isAutoPull() {
                return autoPullMode > 0;
            }
        };
        return this.aeFluidHandler;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void autoIO() {
        if (!this.isWorkingEnabled()) return;
        if (!shouldSyncME()) return;

        if (updateMEStatus()) {
            syncME();
            updateInventorySubscription();
        }

        if (autoPullMode != AUTO_PULL_OFF && getOffsetTimer() % 50 == 0) {
            refreshList();
            syncME();
        }
    }

    private void refreshList() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            aeItemHandler.clearInventory(0);
            aeFluidHandler.clearInventory(0);
            return;
        }
        IStorageService storageService = grid.getStorageService();
        MEStorage networkStorage = storageService.getInventory();
        var counter = networkStorage.getAvailableStacks();
        int index = 0;
        for (Object2LongMap.Entry<AEKey> entry : counter) {
            if (index >= CONFIG_SIZE) break;
            AEKey what = entry.getKey();
            long amount = entry.getLongValue();
            if (amount <= 0) continue;
            boolean isItem = what instanceof AEItemKey;
            if (autoPullMode != AUTO_PULL_ALL) {
                if (autoPullMode == AUTO_PULL_ITEM && !isItem) {
                    continue;
                } else if (autoPullMode == AUTO_PULL_FLUID && isItem) {
                    continue;
                }
            }
            long request = networkStorage.extract(what, amount, Actionable.SIMULATE, actionSource);
            if (request == 0) continue;
            if (isItem) {
                this.aeFluidHandler.getInventory()[index].setConfig(null);
            } else {
                this.aeItemHandler.getInventory()[index].setConfig(null);
            }
            var itemSlot = this.aeItemHandler.getInventory()[index];
            var fluidSlot = this.aeFluidHandler.getInventory()[index];
            var slot = isItem ? itemSlot : fluidSlot;
            if (isItem) {
                fluidSlot.setConfig(null);
                fluidSlot.setStock(null);
            } else {
                itemSlot.setConfig(null);
                itemSlot.setStock(null);
            }
            slot.setConfig(new GenericStack(what, 1));
            slot.setStock(new GenericStack(what, request));
            index++;
        }
        aeItemHandler.clearInventory(index);
        aeFluidHandler.clearInventory(index);
    }

    private void syncME() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            return;
        }
        MEStorage networkInv = grid.getStorageService().getInventory();
        ExportOnlyAEItemSlot[] aeItem = aeItemHandler.getInventory();
        ExportOnlyAEFluidSlot[] aeFluid = aeFluidHandler.getInventory();
        ExportOnlyAESlot slot;
        for (int i = 0; i < aeItem.length; i++) {
            boolean isFluid = false;
            slot = aeItem[i];
            var config = slot.getConfig();
            if (config == null) {
                slot = aeFluid[i];
                isFluid = true;
                config = slot.getConfig();
            }
            if (config != null) {
                var key = config.what();
                long extracted = networkInv.extract(key, isFluid ? Long.MAX_VALUE : Integer.MAX_VALUE,
                        Actionable.SIMULATE, actionSource);
                if (extracted > 0) {
                    slot.setStock(new GenericStack(key, extracted));
                    continue;
                }
            }
            slot.setStock(null);
        }
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(176, 180, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, 176, 185));
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        // ME Network status
        group.addWidget(new LabelWidget(3, 0, () -> this.isOnline ?
                "gtceu.gui.me_network.online" :
                "gtceu.gui.me_network.offline"));

        // Config slots
        group.addWidget(new AEDualConfigWidget(3, 10, this.aeItemHandler, this.aeFluidHandler, this, page));

        return group;
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new TurnsConfiguratorButton(
                this::getAutoPullMode,
                (clickData, mode) -> setAutoPullMode(mode),
                GuiTextures.BUTTON_AUTO_PULL.getSubTexture(0, 0, 1, 0.5),
                AUTO_PULL_ALL_ICON,
                AUTO_PULL_ITEM_ICON,
                AUTO_PULL_FLUID_ICON).setTooltipsSupplier(mode -> List.of(Component.translatable("gtocore.machine.me_dual_hatch_stock.turns." + mode))));
    }

    private void setAutoPullMode(int autoPullMode) {
        this.autoPullMode = autoPullMode;
        if (!isRemote()) {
            if (this.autoPullMode == 0) {
                this.aeItemHandler.clearInventory(0);
                this.aeFluidHandler.clearInventory(0);
            } else if (updateMEStatus()) {
                this.refreshList();
                updateInventorySubscription();
            }
        }
    }

    private CompoundTag writeConfigToTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("AutoPullMode", autoPullMode);
        CompoundTag configStacks = new CompoundTag();
        if (autoPullMode == 0) {
            tag.put("ConfigStacks", configStacks);
            for (int i = 0; i < CONFIG_SIZE; i++) {
                var slot = this.aeItemHandler.getInventory()[i];
                GenericStack config = slot.getConfig();
                if (config == null) {
                    config = this.aeFluidHandler.getInventory()[i].getConfig();
                    if (config == null) {
                        continue;
                    }
                }
                CompoundTag stackTag = GenericStack.writeTag(config);
                configStacks.put(Integer.toString(i), stackTag);
            }
        }
        tag.putByte("GhostCircuit",
                (byte) IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0)));
        return tag;
    }

    private void readConfigFromTag(CompoundTag tag) {
        if (tag.contains("AutoPullMode")) {
            var autoPullMode = tag.getInt("AutoPullMode");
            this.setAutoPullMode(autoPullMode);
        }

        if (tag.contains("ConfigStacks")) {
            CompoundTag configStacks = tag.getCompound("ConfigStacks");
            for (int i = 0; i < CONFIG_SIZE; i++) {
                String key = Integer.toString(i);
                if (configStacks.contains(key)) {
                    CompoundTag configTag = configStacks.getCompound(key);
                    var stack = GenericStack.readTag(configTag);
                    if (stack != null) {
                        if (stack.what() instanceof AEItemKey) {
                            this.aeItemHandler.getInventory()[i].setConfig(stack);
                        } else {
                            this.aeFluidHandler.getInventory()[i].setConfig(stack);
                        }
                        continue;
                    }
                }
                this.aeItemHandler.getInventory()[i].setConfig(null);
                this.aeFluidHandler.getInventory()[i].setConfig(null);
            }
        }

        if (tag.contains("GhostCircuit")) {
            circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.getByte("GhostCircuit")));
        }
    }

    @Override
    public InteractionResult onDataStickUse(Player player, ItemStack dataStick) {
        CompoundTag tag = dataStick.getTag();
        if (tag == null || !tag.contains("MEDualHatchStock")) {
            return InteractionResult.PASS;
        }

        if (!isRemote()) {
            readConfigFromTag(tag.getCompound("MEDualHatchStock"));
            this.updateInventorySubscription();
            player.sendSystemMessage(Component.translatable("gtceu.machine.me.import_paste_settings"));
        }
        return InteractionResult.sidedSuccess(isRemote());
    }

    @Override
    public InteractionResult onDataStickShiftUse(Player player, ItemStack dataStick) {
        if (!isRemote()) {
            CompoundTag tag = new CompoundTag();
            tag.put("MEDualHatchStock", writeConfigToTag());
            dataStick.setTag(tag);
            dataStick.setHoverName(Component.translatable("gtocore.machine.me_dual_hatch_stock.data_stick.name"));
            player.sendSystemMessage(Component.translatable("gtceu.machine.me.import_copy_settings"));
        }
        return InteractionResult.SUCCESS;
    }
}
