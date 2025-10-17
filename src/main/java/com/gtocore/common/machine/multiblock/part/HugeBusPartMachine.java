package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.function.ObjectLongConsumer;
import com.gregtechceu.gtceu.utils.function.ObjectLongPredicate;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import com.hepdd.gtmthings.api.machine.fancyconfigurator.ButtonConfigurator;
import com.hepdd.gtmthings.api.transfer.UnlimitItemTransferHelper;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HugeBusPartMachine extends TieredIOPartMachine implements IMachineLife {

    @Persisted
    private final HugeNotifiableItemStackHandler inventory;
    @Nullable
    private TickableSubscription autoIOSubs;
    @Nullable
    private ISubscription inventorySubs;

    public HugeBusPartMachine(MetaMachineBlockEntity holder) {
        super(holder, GTValues.IV, IO.IN);
        this.inventory = new HugeNotifiableItemStackHandler(this);
        workingEnabled = false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateInventorySubscription));
        }
        inventorySubs = inventory.addChangedListener(this::updateInventorySubscription);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (inventorySubs != null) {
            inventorySubs.unsubscribe();
            inventorySubs = null;
        }
    }

    private void refundAll(ClickData clickData) {
        if (clickData.isRemote) return;
        setWorkingEnabled(false);
        exportToNearby(inventory, getFrontFacing());
    }

    @Override
    public void onPaintingColorChanged(int color) {
        getHandlerList().setColor(color, true);
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateInventorySubscription();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        updateInventorySubscription();
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    private void updateInventorySubscription() {
        if (isWorkingEnabled() && ItemTransferHelper.getItemTransfer(getLevel(), getPos().relative(getFrontFacing()), getFrontFacing().getOpposite()) != null) {
            autoIOSubs = subscribeServerTick(autoIOSubs, this::autoIO);
        } else if (autoIOSubs != null) {
            autoIOSubs.unsubscribe();
            autoIOSubs = null;
        }
    }

    private void autoIO() {
        if (getOffsetTimer() % 40 == 0) {
            if (isWorkingEnabled()) {
                inventory.importFromNearby(getFrontFacing());
            }
            updateInventorySubscription();
        }
    }

    private void exportToNearby(HugeNotifiableItemStackHandler handler, @NotNull Direction facing) {
        if (handler.getCount() < 1) return;
        var level = getLevel();
        var pos = getPos();
        UnlimitItemTransferHelper.exportToTarget(handler.storage, Integer.MAX_VALUE, f -> true, level, pos.relative(facing), facing.getOpposite());
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        updateInventorySubscription();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ButtonConfigurator(new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture("\ud83d\udd19")), this::refundAll).setTooltips(List.of(Component.translatable("gtmthings.machine.huge_item_bus.tooltip.1"))));
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 109, 63);
        var importItems = createImportItems();
        group.addWidget(new ImageWidget(4, 4, 82, 55, GuiTextures.DISPLAY))
                .addWidget(new LabelWidget(8, 8, "gtceu.machine.quantum_chest.items_stored"))
                .addWidget(new LabelWidget(8, 18, () -> FormattingUtil.formatNumbers(inventory.getCount())))
                .addWidget(new com.gregtechceu.gtceu.api.gui.widget.SlotWidget(importItems, 0, 87, 4, false, true).setBackgroundTexture(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.IN_SLOT_OVERLAY)))
                .addWidget(new com.gregtechceu.gtceu.api.gui.widget.SlotWidget(inventory, 0, 87, 22, false, false).setItemHook(s -> s.copyWithCount((int) Math.min(inventory.getCount(), s.getMaxStackSize()))).setBackgroundTexture(GuiTextures.SLOT))
                .addWidget(new ButtonWidget(87, 41, 18, 18, new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, Icons.DOWN.scale(0.7F)), cd -> {
                    if (!cd.isRemote) {
                        if (!inventory.isEmpty()) {
                            var extracted = inventory.extractItemInternal(0, (int) Math.min(inventory.getCount(), inventory.getStackInSlot(0).getMaxStackSize()), false);
                            if (!group.getGui().entityPlayer.addItem(extracted)) {
                                Block.popResource(group.getGui().entityPlayer.level(), group.getGui().entityPlayer.getOnPos(), extracted);
                            }
                        }
                    }
                }));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private CustomItemStackHandler createImportItems() {
        var importItems = new CustomItemStackHandler();
        importItems.setFilter(itemStack -> inventory.canCapInput() && (inventory.insertItem(0, itemStack, true).getCount() != itemStack.getCount()));
        importItems.setOnContentsChanged(() -> {
            var item = importItems.getStackInSlot(0).copy();
            if (!item.isEmpty()) {
                importItems.setStackInSlot(0, ItemStack.EMPTY);
                importItems.onContentsChanged(0);
                inventory.insertItem(0, item.copy(), false);
            }
        });
        return importItems;
    }

    private static final class HugeNotifiableItemStackHandler extends NotifiableItemStackHandler {

        private HugeNotifiableItemStackHandler(MetaMachine machine) {
            super(machine, 1, IO.IN, IO.BOTH, i -> new HugeCustomItemStackHandler());
        }

        private long getCount() {
            return ((HugeCustomItemStackHandler) storage).count;
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            return ((HugeCustomItemStackHandler) storage).stack;
        }

        @Override
        public boolean forEachItems(ObjectLongPredicate<ItemStack> function) {
            var amount = ((HugeCustomItemStackHandler) storage).count;
            if (amount > 0) {
                return function.test(getStackInSlot(0), amount);
            }
            return false;
        }

        @Override
        public void fastForEachItems(ObjectLongConsumer<ItemStack> function) {
            var amount = ((HugeCustomItemStackHandler) storage).count;
            if (amount > 0) {
                function.accept(getStackInSlot(0), amount);
            }
        }

        @Override
        public boolean isEmpty() {
            if (this.isEmpty == null) {
                this.isEmpty = ((HugeCustomItemStackHandler) storage).stack.isEmpty();
            }

            return this.isEmpty;
        }

        @Override
        public IntIngredientMap getIngredientMap() {
            if (changed) {
                changed = false;
                intIngredientMap.clear();
                var amount = ((HugeCustomItemStackHandler) storage).count;
                if (amount > 0) {
                    IntIngredientMap.ITEM_CONVERSION.convert(getStackInSlot(0), amount, intIngredientMap);
                }
            }
            return intIngredientMap;
        }

        @Override
        public boolean canCapOutput() {
            return true;
        }

        @Override
        @Nullable
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            if (io != IO.IN && ((HugeCustomItemStackHandler) storage).count > 0) return left.isEmpty() ? null : left;
            for (var it = left.listIterator(0); it.hasNext();) {
                var ingredient = it.next();
                if (ingredient.isEmpty()) {
                    it.remove();
                    continue;
                }
                long amount;
                if (ingredient instanceof FastSizedIngredient si) amount = si.getAmount();
                else amount = 1;
                if (amount < 1) {
                    it.remove();
                    continue;
                }
                long count = Math.min(amount, getCount());
                if (count == 0) continue;
                if (ingredient.test(getStackInSlot(0))) {
                    if (!simulate) {
                        ((HugeCustomItemStackHandler) storage).count -= count;
                        getStackInSlot(0).setCount(MathUtil.saturatedCast(((HugeCustomItemStackHandler) storage).count));
                        storage.onContentsChanged(0);
                    }
                    amount -= count;
                }
                if (amount <= 0) {
                    it.remove();
                } else {
                    if (ingredient instanceof FastSizedIngredient si) {
                        si.setAmount(amount);
                    }
                }
            }
            return left.isEmpty() ? null : left;
        }
    }

    private static final class HugeCustomItemStackHandler extends CustomItemStackHandler {

        @NotNull
        private ItemStack stack = ItemStack.EMPTY;
        private long count;

        private HugeCustomItemStackHandler() {
            super(1);
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public void setStackInSlot(int index, @NotNull ItemStack stack) {
            this.stack = stack;
            count = stack.getCount();
            onContentsChanged(index);
        }

        @Override
        @NotNull
        public ItemStack getStackInSlot(int slot) {
            return stack;
        }

        @Override
        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            if (count < 1 || this.stack.isEmpty()) {
                if (!simulate) {
                    this.stack = stack.copy();
                    count = stack.getCount();
                    onContentsChanged(0);
                }
                return ItemStack.EMPTY;
            } else if (this.stack.getItem() == stack.getItem()) {
                var tag = this.stack.getShareTag();
                if (tag == null) {
                    if (stack.getShareTag() == null) {
                        if (!simulate) {
                            count += stack.getCount();
                            this.stack.setCount(MathUtil.saturatedCast(count));
                            onContentsChanged(0);
                        }
                        return ItemStack.EMPTY;
                    }
                } else if (tag.equals(stack.getShareTag())) {
                    if (!simulate) {
                        count += stack.getCount();
                        this.stack.setCount(MathUtil.saturatedCast(count));
                        onContentsChanged(0);
                    }
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        }

        @Override
        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0 || count < 1 || this.stack.isEmpty()) return ItemStack.EMPTY;
            if (amount >= count) {
                if (simulate) {
                    return stack;
                } else {
                    count = 0;
                    var stack = this.stack;
                    this.stack = ItemStack.EMPTY;
                    onContentsChanged(0);
                    return stack;
                }
            } else {
                if (!simulate) {
                    count -= amount;
                    stack.setCount(MathUtil.saturatedCast(count));
                    onContentsChanged(0);
                }
                return this.stack.copyWithCount(amount);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.put("stack", stack.serializeNBT());
            nbt.putLong("count", count);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            var stack = nbt.get("stack");
            if (stack instanceof CompoundTag tag) {
                this.stack = ItemStack.of(tag);
            }
            count = nbt.getLong("count");
            this.stack.setCount(MathUtil.saturatedCast(count));
        }
    }

    public NotifiableItemStackHandler getInventory() {
        return this.inventory;
    }
}
