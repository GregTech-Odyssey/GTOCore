package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.common.item.DataCrystalItem;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.BlockableSlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;

import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ScanningHolderMachine extends MultiblockPartMachine implements IMachineLife {

    public static final int CATALYST_SLOT = 0;
    public static final int DATA_SLOT = 1;
    public static final int SCAN_SLOT = 2;
    public static final int FLUID_TANK_CAPACITY = 64 * FluidType.BUCKET_VOLUME;

    @Persisted
    private final ScanningHolder heldItems;
    @Setter
    @Getter
    @Persisted
    @DescSynced
    private boolean isLocked;
    @Getter
    @Persisted
    private final NotifiableFluidTank catalystFluidTank;

    public ScanningHolderMachine(MetaMachineBlockEntity holder) {
        super(holder);
        heldItems = new ScanningHolder(this);
        catalystFluidTank = new NotifiableFluidTank(this, 1, FLUID_TANK_CAPACITY, IO.IN, IO.BOTH);
    }

    public @NotNull ItemStack getHeldItem(boolean remove) {
        return getHeldItem(SCAN_SLOT, remove);
    }

    public void setHeldItem(@NotNull ItemStack heldItem) {
        heldItems.setStackInSlot(SCAN_SLOT, heldItem);
    }

    public @NotNull ItemStack getDataItem(boolean remove) {
        return getHeldItem(DATA_SLOT, remove);
    }

    public void setDataItem(@NotNull ItemStack dataItem) {
        heldItems.setStackInSlot(DATA_SLOT, dataItem);
    }

    public @NotNull ItemStack getCatalystItem(boolean remove) {
        return getHeldItem(CATALYST_SLOT, remove);
    }

    public void setCatalystItem(@NotNull ItemStack catalystItem) {
        heldItems.setStackInSlot(CATALYST_SLOT, catalystItem);
    }

    public @NotNull NotifiableItemStackHandler getAsHandler() {
        return heldItems;
    }

    @NotNull
    private ItemStack getHeldItem(int slot, boolean remove) {
        ItemStack stackInSlot = heldItems.getStackInSlot(slot);
        if (remove && !stackInSlot.isEmpty()) heldItems.setStackInSlot(slot, ItemStack.EMPTY);
        return stackInSlot;
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(this.heldItems.storage);
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        group.addWidget(new ImageWidget(46, 15, 84, 60, GuiTextures.PROGRESS_BAR_RESEARCH_STATION_BASE))
                .addWidget(new BlockableSlotWidget(heldItems, SCAN_SLOT, 79, 36)
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.RESEARCH_STATION_OVERLAY))
                .addWidget(new ScanningWidget(catalystFluidTank, 79, 36, 18, 18, true, true)
                        .setBackground(GuiTextures.BLANK_TRANSPARENT)
                        .setDrawHoverOverlay(false))
                .addWidget(new BlockableSlotWidget(heldItems, CATALYST_SLOT, 15, 15)
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.MOLECULAR_OVERLAY_1))
                .addWidget(new BlockableSlotWidget(heldItems, DATA_SLOT, 15, 57)
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GTOGuiTextures.DATA_CRYSTAL_OVERLAY));
        return group;
    }

    @Override
    public void setFrontFacing(Direction frontFacing) {
        super.setFrontFacing(frontFacing);
        var controllers = getControllers();
        for (var controller : controllers) {
            if (controller != null && controller.isFormed()) controller.checkPatternWithLock();
        }
    }

    private static final class ScanningHolder extends NotifiableItemStackHandler {

        private final ScanningHolderMachine machine;

        private ScanningHolder(ScanningHolderMachine machine) {
            super(machine, 3, IO.IN, IO.BOTH, ScanningHolderStackHandler::new);
            this.machine = machine;
        }

        // 各槽位容量限制
        @Override
        public int getSlotLimit(int slot) {
            return switch (slot) {
                case SCAN_SLOT -> 64;
                case CATALYST_SLOT, DATA_SLOT -> 1;
                default -> super.getSlotLimit(slot);
            };
        }

        // 防止在锁定状态下提取物品
        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!machine.isLocked()) return super.extractItem(slot, amount, simulate);
            return ItemStack.EMPTY;
        }

        // 槽位物品验证
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (stack.isEmpty()) return true;

            boolean isDataItem = stack.getItem() instanceof DataCrystalItem;

            return switch (slot) {
                case SCAN_SLOT -> !isDataItem && !hasFluidInContainer(stack) && machine.getCatalystFluidTank().isEmpty();
                case CATALYST_SLOT -> !isDataItem;
                case DATA_SLOT -> isDataItem;
                default -> super.isItemValid(slot, stack);
            };
        }

        private boolean hasFluidInContainer(ItemStack stack) {
            return FluidUtil.getFluidContained(stack)
                    .map(fluidStack -> !fluidStack.isEmpty())
                    .orElse(false);
        }

        private static final class ScanningHolderStackHandler extends CustomItemStackHandler {

            private ScanningHolderStackHandler(int size) {
                super(size);
            }

            @Override
            public int getSlotLimit(int slot) {
                return switch (slot) {
                    case SCAN_SLOT -> 64;
                    case CATALYST_SLOT, DATA_SLOT -> 1;
                    default -> super.getSlotLimit(slot);
                };
            }
        }
    }

    private static final class ScanningWidget extends TankWidget {

        ScanningWidget(NotifiableFluidTank fluidTank, int x, int y, int width, int height, boolean allowFill, boolean allowDrain) {
            super(fluidTank, x, y, width, height, allowFill, allowDrain);
        }

        @Override
        public List<Component> getFullTooltipTexts() {
            FluidStack fluid = getFluid();
            if (fluid.isEmpty()) return Collections.emptyList();
            return super.getFullTooltipTexts();
        }

        @Override
        public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
            this.drawHoverOverlay = !getFluid().isEmpty();
        }
    }
}
