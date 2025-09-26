package com.gtocore.common.machine.noenergy;

import com.gtocore.common.network.ClientMessage;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.stacks.IKeyCounter;
import com.gtolib.api.ae2.storage.CellDataStorage;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;
import com.gregtechceu.gtceu.utils.collection.O2LOpenCacheHashMap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import appeng.api.config.Actionable;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public final class VirtualItemProviderMachine extends MetaMachine implements IUIMachine, IDropSaveMachine, MEStorage, IGridConnectedMachine, IStorageProvider {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            VirtualItemProviderMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    private static final Item VIRTUAL_ITEM_PROVIDER = CustomItems.VIRTUAL_ITEM_PROVIDER.asItem();
    static private final AEKey EMPTY_STACK;

    static {
        var es = VirtualItemProviderBehavior.setVirtualItem(new ItemStack(VIRTUAL_ITEM_PROVIDER.asItem()), ItemStack.EMPTY);
        es = es.copyWithCount(1);
        es.getOrCreateTag().putBoolean("marked", true);
        EMPTY_STACK = AEItemKey.of(es);
    }

    private final CellDataStorage storage = new CellDataStorage();
    @Persisted
    private final NotifiableItemStackHandler inventory;
    @Persisted
    private final GridNodeHolder nodeHolder;
    @DescSynced
    private boolean isOnline;

    public VirtualItemProviderMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.inventory = new NotifiableItemStackHandler(this, 288, IO.NONE, IO.BOTH);
        this.nodeHolder = new GridNodeHolder(this);
        getMainNode().addService(IStorageProvider.class, this);
        storage.setStoredMap(new O2LOpenCacheHashMap<>());
        inventory.addChangedListener(() -> {
            storage.getStoredMap().clear();
            storage.getStoredMap().addTo(EMPTY_STACK, IParallelMachine.MAX_PARALLEL << 6);
            for (var i = 0; i < inventory.storage.size; i++) {
                var stack = inventory.storage.stacks[i];
                if (stack.isEmpty()) continue;
                if (stack.getItem() == VIRTUAL_ITEM_PROVIDER.asItem() && stack.hasTag()) {
                    stack = stack.copyWithCount(1);
                    stack.getOrCreateTag().putBoolean("marked", true);
                    storage.getStoredMap().addTo(AEItemKey.of(stack), IParallelMachine.MAX_PARALLEL);
                } else {
                    int count = stack.getCount();
                    stack = VirtualItemProviderBehavior.setVirtualItem(new ItemStack(VIRTUAL_ITEM_PROVIDER.asItem()), stack);
                    stack = stack.copyWithCount(1);
                    stack.getOrCreateTag().putBoolean("marked", true);
                    storage.getStoredMap().addTo(AEItemKey.of(stack), IParallelMachine.MAX_PARALLEL * count);
                }

            }
        });
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inventory.notifyListeners();
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int xOffset = 162;
        int yOverflow = 9;
        var modularUI = new ModularUI(xOffset + 19, 244, this, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(5, 5, () -> Component.translatable(getBlockState().getBlock().getDescriptionId()).getString() +
                        "(" + Stream.of(inventory.storage.stacks).filter(i -> !i.isEmpty()).count() + "/" + 288 + ")"))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7, 162, true));

        var innerContainer = new DraggableScrollableWidgetGroup(4, 4, xOffset + 6, 130)
                .setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON).setYScrollBarWidth(4);

        modularUI.widget(new ButtonWidget(176 - 15, 3, 14, 14,
                new ResourceTexture(GTOCore.id("textures/gui/sort.png")),
                (press) -> ClientMessage.send("sortInventory", GTOUtils.noopConsumer())));
        int x = 0;
        int y = 0;
        for (int slot = 0; slot < 288; slot++) {
            innerContainer.addWidget(new SlotWidget(inventory.storage, slot, x * 18, y * 18) {

                @Override
                public boolean isEnabled() {
                    return true;
                }
            }.setBackgroundTexture(GuiTextures.SLOT));
            x++;
            if (x == yOverflow) {
                x = 0;
                y++;
            }
        }
        var container = new WidgetGroup(3, 17, xOffset + 20, 140).addWidget(innerContainer);
        return modularUI.widget(container);
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        inventory.storage.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        tag.put("inventory", inventory.storage.serializeNBT());
    }

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        storageMounts.mount(this, Integer.MAX_VALUE - 1);
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
    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return what instanceof AEItemKey itemKey && itemKey.getItem() == VIRTUAL_ITEM_PROVIDER && itemKey.hasTag();
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount > 0 && what instanceof AEItemKey itemKey && itemKey.getItem() == VIRTUAL_ITEM_PROVIDER) {
            var stack = itemKey.getReadOnlyStack();
            var tag = stack.getTag();
            if (tag != null && tag.tags.containsKey("n")) {
                if (tag.getBoolean("marked")) return amount;
                if (ItemHandlerHelper.insertItem(inventory.storage, stack, mode.isSimulate()).getCount() < amount) {
                    return amount;
                }
            }
        }
        return 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount > 0 && what instanceof AEItemKey itemKey && itemKey.getItem() == VIRTUAL_ITEM_PROVIDER && storage.getStoredMap().containsKey(itemKey)) {
            return amount;
        }
        return 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        IKeyCounter.addAll(out, storage.getStoredMap());
    }

    @Override
    public KeyCounter getAvailableStacks() {
        var keyCounter = storage.getKeyCounter();
        if (keyCounter == null) {
            keyCounter = new KeyCounter();
            storage.setKeyCounter(keyCounter);
        } else {
            keyCounter.clear();
        }
        getAvailableStacks(keyCounter);
        keyCounter.removeEmptySubmaps();
        return keyCounter;
    }
}
