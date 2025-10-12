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

public class AnalyzeHolderMachine extends MultiblockPartMachine implements IMachineLife, IControllable {

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public static final int CATALYST_SLOT = 0;
    public static final int EMPTY_SLOT = 1;
    public static final int DATA_SLOT = 2;

    protected final IO io;

    @Persisted
    private final AnalyzeHolder heldItems;
    @Persisted
    @DescSynced
    private boolean isLocked;

    public AnalyzeHolderMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.io = IO.IN;
        heldItems = new AnalyzeHolder(this);
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
        group.addWidget(new ImageWidget(0, 15, 84, 60, GuiTextures.PROGRESS_BAR_RESEARCH_STATION_BASE))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT, 33, 36, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY))
                .addWidget(new BlockableSlotWidget(heldItems, CATALYST_SLOT, 99, 15, true, io.support(IO.IN))
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.MOLECULAR_OVERLAY_1))
                .addWidget(new BlockableSlotWidget(heldItems, EMPTY_SLOT, 99, 57, true, io.support(IO.IN))
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

    private static class AnalyzeHolder extends NotifiableItemStackHandler {

        private final AnalyzeHolderMachine machine;

        private AnalyzeHolder(AnalyzeHolderMachine machine) {
            super(machine, 3, IO.IN, IO.BOTH, MyCustomItemStackHandler::new);
            this.machine = machine;
        }

        // 各槽位容量限制
        @Override
        public int getSlotLimit(int slot) {
            return switch (slot) {
                case DATA_SLOT, CATALYST_SLOT -> 1;
                case EMPTY_SLOT -> 64;
                default -> super.getSlotLimit(slot);
            };
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

            return switch (slot) {
                case DATA_SLOT -> hasNBT && !emptyNBT;
                case EMPTY_SLOT -> emptyNBT;
                case CATALYST_SLOT -> !isDataItem;
                default -> super.isItemValid(slot, stack);
            };
        }

        private static final class MyCustomItemStackHandler extends CustomItemStackHandler {

            private MyCustomItemStackHandler(int size) {
                super(size);
            }

            @Override
            public int getSlotLimit(int slot) {
                return switch (slot) {
                    case DATA_SLOT, CATALYST_SLOT -> 1;
                    case EMPTY_SLOT -> 64;
                    default -> super.getSlotLimit(slot);
                };
            }
        }
    }
}
