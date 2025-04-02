package com.gto.gtocore.client.gui.widget;

import com.gto.gtocore.common.machine.multiblock.part.ae.MEDualHatchStockPartMachine;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEConfigSlotWidget;
import com.gregtechceu.gtceu.integration.ae2.slot.*;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.stacks.GenericStack;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author EasterFG on 2025/3/7
 */
public final class AEDualConfigWidget extends WidgetGroup {

    private final IConfigurableSlot[] config;
    private final Int2ObjectMap<IConfigurableSlot> changeMap = new Int2ObjectOpenHashMap<>();

    private IConfigurableSlot[] cached;
    private IConfigurableSlot[] displayList;
    private final ExportOnlyAEItemList aeItem;
    private final ExportOnlyAEFluidList aeFluid;
    private final MEDualHatchStockPartMachine machine;

    public static final int CONFIG_SIZE = 16;
    int page;
    private final int MAX_PAGE;
    private final int offset;

    private static final int UPDATE_ID = 1001;

    public AEDualConfigWidget(int x, int y, ExportOnlyAEItemList aeItem, ExportOnlyAEFluidList aeFluid, MEDualHatchStockPartMachine machine, int page) {
        super(new Position(x, y), new Size(CONFIG_SIZE / 2 * 18, (18 << 2) + 2 + 19));
        this.aeItem = aeItem;
        this.aeFluid = aeFluid;
        this.machine = machine;
        this.offset = aeItem.getSize();
        this.page = page;
        this.MAX_PAGE = Math.max(1, offset / CONFIG_SIZE);
        this.config = marge();
        init();
        ButtonWidget pageUpWidget = new ButtonWidget(3, 77, 16, 16,
                GuiTextures.BUTTON_LEFT, this::pageUp);
        ButtonWidget pageDownWidget = new ButtonWidget(125, 77, 16, 16,
                GuiTextures.BUTTON_RIGHT, this::pageDown);
        this.addWidget(new LabelWidget(65, 82, () -> this.page + " / " + MAX_PAGE));
        this.addWidget(pageUpWidget);
        this.addWidget(pageDownWidget);
    }

    private void pageUp(ClickData data) {
        if (page < 2) return;
        page--;
        machine.setPage(page);
    }

    private void pageDown(ClickData data) {
        if (page >= MAX_PAGE) return;
        page++;
        machine.setPage(page);
    }

    boolean isAutoPull() {
        return aeFluid.isAutoPull() || aeItem.isAutoPull();
    }

    private IConfigurableSlot[] marge() {
        ExportOnlyAEItemSlot[] itemSlots = aeItem.getInventory();
        ExportOnlyAEFluidSlot[] fluidSlots = aeFluid.getInventory();
        IConfigurableSlot[] result = new IConfigurableSlot[itemSlots.length + fluidSlots.length];
        System.arraycopy(itemSlots, 0, result, 0, itemSlots.length);
        System.arraycopy(fluidSlots, 0, result, itemSlots.length, fluidSlots.length);
        return result;
    }

    private void init() {
        int line;
        this.displayList = new IConfigurableSlot[this.config.length];
        this.cached = new IConfigurableSlot[this.config.length];
        int half = this.config.length / 2;
        for (int index = 0; index < this.config.length; index++) {
            this.displayList[index] = index < half ? new ExportOnlyAEItemSlot() : new ExportOnlyAEFluidSlot();
            this.cached[index] = index < half ? new ExportOnlyAEItemSlot() : new ExportOnlyAEFluidSlot();
            line = index / 8;
            if (line >= 2) continue;
            this.addWidget(new AEDualConfigSlotWidget((index - (line << 3)) * 18, line * ((18 << 1) + 2), this, index));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Widget w : this.widgets) {
            if (w instanceof AEConfigSlotWidget slot) {
                slot.setSelect(false);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.changeMap.clear();
        for (int index = 0; index < this.config.length; index++) {
            IConfigurableSlot newSlot = this.config[index];
            IConfigurableSlot oldSlot = this.cached[index];
            GenericStack nConfig = newSlot.getConfig();
            GenericStack nStock = newSlot.getStock();
            GenericStack oConfig = oldSlot.getConfig();
            GenericStack oStock = oldSlot.getStock();
            if (areAEStackCountsEqual(nConfig, oConfig) || areAEStackCountsEqual(nStock, oStock)) {
                this.changeMap.put(index, newSlot.copy());
                this.cached[index] = this.config[index].copy();
                this.gui.holder.markAsDirty();
            }
        }
        if (!this.changeMap.isEmpty()) {
            this.writeUpdateInfo(UPDATE_ID, buf -> {
                buf.writeVarInt(this.changeMap.size());
                for (int index : this.changeMap.keySet()) {
                    GenericStack sConfig = this.changeMap.get(index).getConfig();
                    GenericStack sStock = this.changeMap.get(index).getStock();
                    buf.writeVarInt(index);
                    if (sConfig != null) {
                        buf.writeBoolean(true);
                        GenericStack.writeBuffer(sConfig, buf);
                    } else {
                        buf.writeBoolean(false);
                    }
                    if (sStock != null) {
                        buf.writeBoolean(true);
                        GenericStack.writeBuffer(sStock, buf);
                    } else {
                        buf.writeBoolean(false);
                    }
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == UPDATE_ID) {
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                int index = buffer.readVarInt();
                IConfigurableSlot slot = this.displayList[index];
                if (buffer.readBoolean()) {
                    slot.setConfig(GenericStack.readBuffer(buffer));
                } else {
                    slot.setConfig(null);
                }
                if (buffer.readBoolean()) {
                    slot.setStock(GenericStack.readBuffer(buffer));
                } else {
                    slot.setStock(null);
                }
            }
        }
    }

    public boolean hasStackInConfig(GenericStack stack) {
        return aeItem.hasStackInConfig(stack, true) ||
                aeFluid.hasStackInConfig(stack, true);
    }

    public void setItemConfig(int index, GenericStack stack) {
        var fluidConfig = getFluidConfig(index);
        if (fluidConfig != null) {
            fluidConfig.setConfig(null);
        }
        getItemConfig(index).setConfig(stack);
    }

    public void setFluidConfig(int index, GenericStack stack) {
        var itemConfig = getItemConfig(index);
        if (itemConfig != null) {
            itemConfig.setConfig(null);
        }
        getFluidConfig(index).setConfig(stack);
    }

    private IConfigurableSlot getItemConfig(int index) {
        return config[index];
    }

    private IConfigurableSlot getFluidConfig(int index) {
        return config[index + offset];
    }

    public IConfigurableSlot getConfig(int index) {
        var c = getItemConfig(index);
        if (c.getConfig() == null) {
            return getFluidConfig(index);
        }
        return c;
    }

    public IConfigurableSlot getDisplay(int index) {
        var c = displayList[index];
        if (c.getConfig() == null) {
            return displayList[index + offset];
        }
        return c;
    }

    private static boolean areAEStackCountsEqual(GenericStack s1, GenericStack s2) {
        if (s2 == s1) {
            return false;
        }
        if (s1 != null && s2 != null) {
            return s1.amount() != s2.amount() || !s1.what().matches(s2);
        }
        return true;
    }
}
