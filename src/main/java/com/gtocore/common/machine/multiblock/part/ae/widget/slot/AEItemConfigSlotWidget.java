package com.gtocore.common.machine.multiblock.part.ae.widget.slot;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAESlot;
import com.gtocore.common.machine.multiblock.part.ae.widget.ConfigWidget;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.misc.IGhostItemTarget;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.lowdragmc.lowdraglib.gui.util.TextFormattingUtil;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget.drawSelectionOverlay;
import static com.lowdragmc.lowdraglib.gui.util.DrawerHelper.drawItemStack;
import static com.lowdragmc.lowdraglib.gui.util.DrawerHelper.drawStringFixedCorner;

public class AEItemConfigSlotWidget extends AEConfigSlotWidget implements IGhostItemTarget {

    public AEItemConfigSlotWidget(int x, int y, ConfigWidget widget, int index) {
        super(new Position(x, y), new Size(18, 36), widget, index);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position position = getPosition();
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.index);
        GenericStack config = slot.getConfig();
        GenericStack stock = slot.getStock();
        drawSlots(graphics, mouseX, mouseY, position.x, position.y, parentWidget.isAutoPull());
        if (this.select) {
            GuiTextures.SELECT_BOX.draw(graphics, mouseX, mouseY, position.x, position.y, 18, 18);
        }
        int stackX = position.x + 1;
        int stackY = position.y + 1;
        if (config != null) {
            ItemStack stack = config.what() instanceof AEItemKey key ? new ItemStack(key.getItem()) : ItemStack.EMPTY;
            drawItemStack(graphics, stack, stackX, stackY, 0xFFFFFFFF, null);

            if (!parentWidget.isStocking()) {
                String amountStr = TextFormattingUtil.formatLongToCompactString(config.amount(), 4);
                drawStringFixedCorner(graphics, amountStr, stackX + 17, stackY + 17, 16777215, true, 0.5f);
            }
        }
        if (stock != null) {
            ItemStack stack = stock.what() instanceof AEItemKey key ? new ItemStack(key.getItem()) : ItemStack.EMPTY;
            drawItemStack(graphics, stack, stackX, stackY + 18, 0xFFFFFFFF, null);
            String amountStr = TextFormattingUtil.formatLongToCompactString(stock.amount(), 4);
            drawStringFixedCorner(graphics, amountStr, stackX + 17, stackY + 18 + 17, 16777215, true, 0.5f);
        }
        if (mouseOverConfig(mouseX, mouseY)) {
            drawSelectionOverlay(graphics, stackX, stackY, 16, 16);
        } else if (mouseOverStock(mouseX, mouseY)) {
            drawSelectionOverlay(graphics, stackX, stackY + 18, 16, 16);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawSlots(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, boolean autoPull) {
        if (autoPull) {
            GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
            GuiTextures.CONFIG_ARROW.draw(graphics, mouseX, mouseY, x, y, 18, 18);
        } else {
            GuiTextures.SLOT.draw(graphics, mouseX, mouseY, x, y, 18, 18);
            GuiTextures.CONFIG_ARROW_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
        }
        GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y + 18, 18, 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseOverConfig(mouseX, mouseY)) {
            if (parentWidget.isAutoPull()) {
                return false;
            }

            if (button == 1) {
                writeClientAction(REMOVE_ID, buf -> {});

                if (!parentWidget.isStocking()) {
                    this.parentWidget.disableAmountClient();
                }
            } else if (button == 0) {
                // Left click to set/select
                ItemStack item = this.gui.getModularUIContainer().getCarried();

                if (!item.isEmpty()) {
                    writeClientAction(UPDATE_ID, buf -> buf.writeItem(item));
                }

                if (!parentWidget.isStocking()) {
                    this.parentWidget.enableAmountClient(this.index);
                    this.select = true;
                }
            }
            return true;
        } else if (mouseOverStock(mouseX, mouseY)) {
            if (parentWidget.isStocking()) {
                return false;
            }
            var stack = this.parentWidget.getDisplay(this.index).getStock();
            if (stack != null) {
                writeClientAction(SLOT_CLICK_ID, buf -> {
                    buf.writeInt(button);
                    buf.writeBoolean(isShiftDown());
                });
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.parentWidget.getDisplay(this.index).getStock() == null) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        var minecraft = Minecraft.getInstance();
        if (minecraft.options.keyDrop.matches(keyCode, scanCode)) {
            if (parentWidget.isStocking()) {
                return false;
            }
            var mouseX = minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
            var mouseY = minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();

            if (isMouseOverElement(mouseX, mouseY) && mouseOverStock(mouseX, mouseY)) {
                writeClientAction(SLOT_DROP_ID, buf -> buf.writeBoolean(isCtrlDown()));
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        var slot = this.parentWidget.getConfig(this.index);
        switch (id) {
            case REMOVE_ID -> {
                slot.setConfig(null);
                this.parentWidget.disableAmount();
                writeUpdateInfo(REMOVE_ID, buf -> {});
            }
            case UPDATE_ID -> {
                var itemStack = buffer.readItem();
                var stack = GenericStack.fromItemStack(itemStack);
                if (!isStackValidForSlot(stack)) return;
                slot.setConfig(stack);
                this.parentWidget.enableAmount(this.index);
                if (!itemStack.isEmpty()) {
                    writeUpdateInfo(UPDATE_ID, buf -> buf.writeItem(itemStack));
                }
            }
            case AMOUNT_CHANGE_ID -> {
                if (slot.getConfig() == null) return;
                long amt = buffer.readVarLong();
                slot.setConfig(new GenericStack(slot.getConfig().what(), amt));
                writeUpdateInfo(AMOUNT_CHANGE_ID, buf -> buf.writeVarLong(amt));
            }
            case SLOT_CLICK_ID -> {
                var mouseButton = buffer.readInt();
                var isShiftDown = buffer.readBoolean();
                if (slot.getStock() == null || !(slot.getStock().what() instanceof AEItemKey key)) {
                    return;
                }
                var amount = slot.getStock().amount();
                var maxStackSize = key.getMaxStackSize();
                int clickResult;
                if (mouseButton == 0 && isShiftDown) {
                    var player = this.gui.entityPlayer;
                    if (player == null) return;
                    var moveCount = (int) Math.min(amount, maxStackSize);
                    var moveStack = key.toStack(moveCount);
                    transferToPlayerInventory(player, moveStack);
                    var newStock = ExportOnlyAESlot.copy(slot.getStock(), amount - (moveCount - moveStack.getCount()));
                    slot.setStock(newStock.amount() == 0 ? null : newStock);
                    clickResult = gui.getModularUIContainer().getCarried().getCount();
                } else {
                    var container = this.gui.getModularUIContainer();
                    var carried = container.getCarried();
                    var pickUpCount = (int) Math.min(amount, maxStackSize);
                    if (mouseButton == 1) {
                        pickUpCount = (pickUpCount + 1) / 2;
                    }
                    var pickUpStack = key.toStack(pickUpCount);
                    if (carried.isEmpty()) {
                        container.setCarried(pickUpStack);
                        var newStock = ExportOnlyAESlot.copy(slot.getStock(), amount - pickUpCount);
                        slot.setStock(newStock.amount() == 0 ? null : newStock);
                        clickResult = pickUpStack.getCount();
                    } else if (ItemStack.isSameItemSameTags(carried, pickUpStack)) {
                        var canAdd = Math.min(pickUpCount, carried.getMaxStackSize() - carried.getCount());
                        if (canAdd <= 0) return;
                        carried.grow(canAdd);
                        var newStock = ExportOnlyAESlot.copy(slot.getStock(), amount - canAdd);
                        slot.setStock(newStock.amount() == 0 ? null : newStock);
                        clickResult = carried.getCount();
                    } else {
                        clickResult = -1;
                    }
                }
                if (clickResult >= 0) {
                    writeUpdateInfo(SLOT_CLICK_ID, buf -> buf.writeVarInt(clickResult));
                }
            }
            case SLOT_DROP_ID -> {
                var isCtrlDown = buffer.readBoolean();
                if (slot.getStock() == null || !(slot.getStock().what() instanceof AEItemKey key)) {
                    return;
                }
                var player = this.gui.entityPlayer;
                if (player == null) return;
                var amount = slot.getStock().amount();
                var maxStackSize = key.getMaxStackSize();
                var dropCount = isCtrlDown ? (int) Math.min(amount, maxStackSize) : 1;
                var dropStack = key.toStack(dropCount);
                player.drop(dropStack, true);
                var newStock = ExportOnlyAESlot.copy(slot.getStock(), amount - dropCount);
                slot.setStock(newStock.amount() == 0 ? null : newStock);
                writeUpdateInfo(SLOT_DROP_ID, buf -> buf.writeVarLong(newStock.amount() == 0 ? 0 : newStock.amount()));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.index);
        switch (id) {
            case REMOVE_ID -> {
                slot.setConfig(null);
            }
            case UPDATE_ID -> {
                ItemStack item = buffer.readItem();
                slot.setConfig(new GenericStack(AEItemKey.of(item), item.getCount()));
            }
            case AMOUNT_CHANGE_ID -> {
                if (slot.getConfig() != null) {
                    long amt = buffer.readVarLong();
                    slot.setConfig(new GenericStack(slot.getConfig().what(), amt));
                }
            }
            case SLOT_CLICK_ID -> {
                if (slot.getStock() != null && slot.getStock().what() instanceof AEItemKey) {
                    var currentStack = gui.getModularUIContainer().getCarried();
                    int newStackSize = buffer.readVarInt();
                    currentStack.setCount(newStackSize);
                    gui.getModularUIContainer().setCarried(currentStack);

                    long amount = buffer.readVarLong();
                    slot.setStock(ExportOnlyAESlot.copy(slot.getStock(), amount));
                }
            }
            case SLOT_DROP_ID -> {
                if (slot.getStock() != null && slot.getStock().what() instanceof AEItemKey) {
                    long amt = buffer.readVarLong();
                    slot.setStock(ExportOnlyAESlot.copy(slot.getStock(), amt));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Rect2i getRectangleBox() {
        Rect2i rectangle = toRectangleBox();
        rectangle.setHeight(rectangle.getHeight() / 2);
        return rectangle;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void acceptItem(ItemStack itemStack) {
        writeClientAction(UPDATE_ID, buf -> buf.writeItem(itemStack));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        // Only allow the amount scrolling if not stocking, as amount is useless for stocking
        if (parentWidget.isStocking()) return false;
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.index);
        Rect2i rectangle = toRectangleBox();
        rectangle.setHeight(rectangle.getHeight() / 2);
        if (slot.getConfig() == null || wheelDelta == 0 || !rectangle.contains((int) mouseX, (int) mouseY)) {
            return false;
        }
        GenericStack stack = slot.getConfig();
        long amt;
        if (isCtrlDown()) {
            amt = wheelDelta > 0 ? stack.amount() << 1 : stack.amount() / 2L;
        } else {
            amt = wheelDelta > 0 ? stack.amount() + 1L : stack.amount() - 1L;
        }
        if (amt > 0 && amt < Integer.MAX_VALUE + 1L) {
            writeClientAction(AMOUNT_CHANGE_ID, buf -> buf.writeVarLong(amt));
            return true;
        }
        return false;
    }

    private void transferToPlayerInventory(Player player, ItemStack stack) {
        var playerInv = player.getInventory();
        while (!stack.isEmpty()) {
            int slotIndex = playerInv.getSlotWithRemainingSpace(stack);
            if (slotIndex != -1) {
                var itemInSlot = playerInv.getItem(slotIndex);
                int spaceAvailable = itemInSlot.getMaxStackSize() - itemInSlot.getCount();
                int moveCount = Math.min(stack.getCount(), spaceAvailable);
                itemInSlot.grow(moveCount);
                stack.shrink(moveCount);
                continue;
            }
            slotIndex = playerInv.getFreeSlot();
            if (slotIndex != -1) {
                int moveCount = Math.min(stack.getCount(), stack.getMaxStackSize());
                var moveStack = stack.copy();
                moveStack.setCount(moveCount);
                playerInv.setItem(slotIndex, moveStack);
                stack.shrink(moveCount);
                continue;
            }
            break;
        }
    }
}
