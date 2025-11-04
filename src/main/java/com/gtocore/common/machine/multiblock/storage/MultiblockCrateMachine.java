package com.gtocore.common.machine.multiblock.storage;

import appeng.api.storage.MEStorage;
import appeng.me.cells.BasicCellInventory;
import appeng.menu.slot.AppEngSlot;
import appeng.menu.slot.CellPartitionSlot;
import appeng.util.inv.AppEngInternalInventory;
import com.google.common.base.Preconditions;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.api.transfer.item.LockableItemStackHandler;
import com.gtolib.GTOCore;
import com.gtolib.utils.SortUtils;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiblockCrateMachine extends MultiblockControllerMachine implements IUIMachine, IDropSaveMachine {

    public static final int Capacity = 243;

    public final AppEngInternalInventory inv;
    private final LockableItemStackHandler itemStackHandler;

    public MultiblockCrateMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.inv = new AppEngInternalInventory(null, Capacity, 1024) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                Preconditions.checkArgument(slot >= 0 && slot < this.size(), "slot out of range");
                if (!stack.isEmpty() && this.isItemValid(slot, stack)) {
                    ItemStack inSlot = this.getStackInSlot(slot);
                    int maxSpace = this.getSlotLimit(slot);
                    int freeSpace = maxSpace - inSlot.getCount();
                    if (freeSpace <= 0) {
                        return stack;
                    } else if (!inSlot.isEmpty() && !ItemStack.isSameItemSameTags(inSlot, stack)) {
                        return stack;
                    } else {
                        int insertAmount = Math.min(stack.getCount(), freeSpace);
                        if (!simulate) {
                            ItemStack newItem = inSlot.isEmpty() ? stack.copy() : inSlot.copy();
                            newItem.setCount(inSlot.getCount() + insertAmount);
                            this.setItemDirect(slot, newItem);
                        }

                        if (freeSpace >= stack.getCount()) {
                            return ItemStack.EMPTY;
                        } else {
                            ItemStack r = stack.copy();
                            r.shrink(insertAmount);
                            return r;
                        }
                    }
                } else {
                    return stack;
                }
            }
        };
        this.itemStackHandler = new LockableItemStackHandler((IItemHandlerModifiable) inv.toItemHandler());
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return isFormed && IUIMachine.super.shouldOpenUI(player, hand, hit);
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return (IItemHandlerModifiable) inv.toItemHandler();
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        itemStackHandler.setLock(false);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        itemStackHandler.setLock(true);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int xOffset = 162;
        int yOverflow = 9;
        // int yOffset = (Capacity - 3 * yOverflow) / yOverflow * 18;
        var modularUI = new ModularUI(xOffset + 19, 244, this, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(5, 5, () -> Component.translatable(getBlockState().getBlock().getDescriptionId()).getString()))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7, 162, true));

        var innerContainer = new DraggableScrollableWidgetGroup(4, 4, xOffset + 6, 130)
                .setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON).setYScrollBarWidth(4);

        modularUI.widget(new ButtonWidget(176 - 15, 3, 14, 14,
                new ResourceTexture(GTOCore.id("textures/gui/sort.png")),
                (press) -> SortUtils.sort()));
        int x = 0;
        int y = 0;
        for (int slot = 0; slot < Capacity; slot++) {
            var widget = new SlotWidget((IItemHandlerModifiable) inv.toItemHandler(), slot, x * 18, y * 18) {
                @Override
                protected Slot createSlot(IItemHandlerModifiable itemHandler, int index) {
                    return new AppEngSlot(inv, index);
                }
            };
            innerContainer.addWidget(widget);
            x++;
            if (x == yOverflow) {
                x = 0;
                y++;
            }
        }
        var container = new WidgetGroup(
                3, 17, xOffset + 20, 140).addWidget(innerContainer);

        return modularUI.widget(container);
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        inv.writeToNBT(tag, "inv");
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        inv.readFromNBT(tag, "inv");
    }
}
