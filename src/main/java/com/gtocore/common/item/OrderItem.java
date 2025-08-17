package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public final class OrderItem implements IItemUIFactory, IFancyUIProvider, ICustomDescriptionId {

    public static final OrderItem INSTANCE = new OrderItem();

    private InteractionHand hand;
    private Player player;

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        this.hand = usedHand;
        this.player = player;
        return IItemUIFactory.super.use(item, level, player, usedHand);
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder heldItemHolder, Player player) {
        return new ModularUI(176, 166, heldItemHolder, player).widget(new FancyMachineUIWidget(this, 176, 166));
    }

    public static ItemStack setTarget(ItemStack stack, ItemStack target) {
        var tag = stack.getOrCreateTag();
        var id = BuiltInRegistries.ITEM.getKey(target.getItem());
        tag.putString("marker_id", id.toString());
        return stack;
    }

    public static ItemStack getTarget(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        var id = tag.getString("marker_id");
        if (id.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(RegistriesUtils.getItem(RLUtils.parse(id)));
    }

    public static ItemStack clearTarget(ItemStack stack) {
        if (!stack.hasTag()) return stack;
        var tag = stack.getOrCreateTag();
        tag.remove("marker_id");
        return stack;
    }

    @Override
    public Component getItemName(ItemStack stack) {
        Component name = Component.empty();
        if (stack.hasTag()) {
            name = getTarget(stack).getHoverName();
        }
        return Component.translatable(stack.getDescriptionId(), name);
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        WidgetGroup group = new WidgetGroup(0, 0, 34, 34);
        WidgetGroup container = new WidgetGroup(4, 4, 26, 26);
        container.addWidget(new PhantomSlotWidget(new ItemHandler(hand, player), 0, 4, 4));
        group.addWidget(container);
        return group;
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTOItems.ORDER.asItem());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.gtocore.order.config");
    }

    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
    }

    private static class ItemHandler implements IItemTransfer {

        @NotNull
        private ItemStack stack = ItemStack.EMPTY;
        private final InteractionHand hand;
        private final Player player;

        public ItemHandler(InteractionHand hand, @NotNull Player player) {
            this.hand = hand;
            this.player = player;
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return stack;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChanges) {
            this.stack = stack;
            player.setItemInHand(hand, setTarget(player.getItemInHand(hand), stack));
            return null;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
            this.stack = ItemStack.EMPTY;
            player.setItemInHand(hand, clearTarget(player.getItemInHand(hand)));
            return stack;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() == this.stack.getItem();
        }

        @NotNull
        @Override
        public Object createSnapshot() {
            return stack;
        }

        @Override
        public void restoreFromSnapshot(Object snapshot) {
            if (snapshot instanceof ItemStack stack) {
                this.stack = stack;
            }
        }
    }
}
