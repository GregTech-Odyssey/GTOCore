package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.item.tool.IExDataItem;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.BlockableSlotWidget;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import org.jetbrains.annotations.NotNull;

public class ResearchHolderMachine extends MultiblockPartMachine implements IMachineLife, IControllable {

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public static final int CATALYST_SLOT_1 = 0;
    public static final int CATALYST_SLOT_2 = 1;
    public static final int EMPTY_SLOT = 2;
    public static final int[] DATA_SLOT = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

    protected final IO io;

    @Persisted
    private final ResearchHolder heldItems;
    @Persisted
    @DescSynced
    private boolean isLocked;

    public ResearchHolderMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.io = IO.IN;
        heldItems = new ResearchHolder(this);
    }

    public boolean isWorkingEnabled() {
        return false;
    }

    public void setWorkingEnabled(boolean workingEnabled) {}

    @Override
    public void onMachineRemoved() {
        clearInventory(this.heldItems.storage);
    }

    public @NotNull NotifiableItemStackHandler getAsHandler() {
        return heldItems;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        int centerX = 60;
        int centerY = 55;
        group.addWidget(new ImageWidget(centerX - 33, centerY - 21, 84, 60, GuiTextures.PROGRESS_BAR_RESEARCH_STATION_BASE))

                .addWidget(new BlockableSlotWidget(heldItems, CATALYST_SLOT_1, centerX - 60, centerY, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.MOLECULAR_OVERLAY_1))
                .addWidget(new BlockableSlotWidget(heldItems, CATALYST_SLOT_2, centerX + 60, centerY, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.MOLECULAR_OVERLAY_1))
                .addWidget(new BlockableSlotWidget(heldItems, EMPTY_SLOT, centerX, centerY, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))

                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[0], centerX - 38, centerY - 30, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[1], centerX - 19, centerY - 40, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[2], centerX - 0, centerY - 46, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[3], centerX + 19, centerY - 40, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[4], centerX + 38, centerY - 30, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[5], centerX + 38, centerY + 30, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[6], centerX + 19, centerY + 40, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[7], centerX + 0, centerY + 46, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[8], centerX - 19, centerY + 40, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT[9], centerX - 38, centerY + 30, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY));

        return group;
    }

    @Override
    public void setFrontFacing(@NotNull Direction frontFacing) {
        super.setFrontFacing(frontFacing);
        var controllers = getControllers();
        for (var controller : controllers) {
            if (controller != null && controller.isFormed()) {
                controller.checkPatternWithLock();
            }
        }
    }

    private static class ResearchHolder extends NotifiableItemStackHandler {

        private final ResearchHolderMachine machine;

        private ResearchHolder(ResearchHolderMachine machine) {
            super(machine, 13, IO.IN, IO.BOTH, MyCustomItemStackHandler::new);
            this.machine = machine;
        }

        // 各槽位容量限制
        @Override
        public int getSlotLimit(int slot) {
            if (slot == EMPTY_SLOT) {
                return 64;
            } else if (slot == CATALYST_SLOT_1 || slot == CATALYST_SLOT_2 || (slot >= 3 && slot <= 12)) {
                return 1;
            } else {
                return super.getSlotLimit(slot);
            }
        }

        // 防止在锁定状态下提取物品
        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!machine.isLocked()) {
                return super.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        // 槽位物品验证
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (stack.isEmpty()) {
                return true;
            }

            // 检查是否为数据物品
            boolean isDataItem = false;
            boolean hasNBT = false;
            boolean emptyNBT = false;
            if (stack.getItem() instanceof IComponentItem metaItem) {
                for (IItemComponent behaviour : metaItem.getComponents()) {
                    if (behaviour instanceof IExDataItem) {
                        isDataItem = true;
                        hasNBT = stack.hasTag();
                        if (stack.getTag() != null && stack.hasTag() && stack.getTag().contains("empty_crystal", CompoundTag.TAG_COMPOUND))
                            emptyNBT = true;
                        break;
                    }
                }
            }

            if (slot == EMPTY_SLOT) {
                return emptyNBT;
            } else if (slot >= 3 && slot <= 12) {
                return hasNBT && !emptyNBT;
            } else if (slot == CATALYST_SLOT_1 || slot == CATALYST_SLOT_2) {
                return !isDataItem;
            } else {
                return super.isItemValid(slot, stack);
            }
        }

        private static final class MyCustomItemStackHandler extends CustomItemStackHandler {

            private MyCustomItemStackHandler(int size) {
                super(size);
            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot == EMPTY_SLOT) {
                    return 64;
                } else if (slot == CATALYST_SLOT_1 || slot == CATALYST_SLOT_2 || (slot >= 3 && slot <= 12)) {
                    return 1;
                } else {
                    return super.getSlotLimit(slot);
                }
            }
        }
    }
}
