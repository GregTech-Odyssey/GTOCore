package com.gtocore.common.machine.multiblock.part.ae.widget.slot;

import com.gtocore.common.machine.multiblock.part.ae.widget.ConfigWidget;

import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.stacks.GenericStack;
import com.hepdd.ae2emicraftingforge.client.helper.mapper.EmiStackHelper;
import com.lowdragmc.lowdraglib.gui.ingredient.IIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AEConfigSlotWidget extends Widget implements IIngredientSlot {

    final ConfigWidget parentWidget;
    final int index;
    static final int REMOVE_ID = 1000;
    static final int UPDATE_ID = 1001;
    static final int AMOUNT_CHANGE_ID = 1002;
    static final int PICK_UP_ID = 1003;
    boolean select = false;

    AEConfigSlotWidget(Position pos, Size size, ConfigWidget widget, int index) {
        super(pos, size);
        this.parentWidget = widget;
        this.index = index;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawInForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.index);
        if (slot.getConfig() == null) {
            if (mouseOverConfig(mouseX, mouseY)) {
                List<Component> hoverStringList = new ArrayList<>();
                hoverStringList.add(Component.translatable("gtceu.gui.config_slot"));
                if (parentWidget.isAutoPull()) {
                    hoverStringList.add(Component.translatable("gtceu.gui.config_slot.auto_pull_managed"));
                } else {
                    if (!parentWidget.isStocking()) {
                        hoverStringList.add(Component.translatable("gtceu.gui.config_slot.set"));
                        hoverStringList.add(Component.translatable("gtceu.gui.config_slot.scroll"));
                    } else {
                        hoverStringList.add(Component.translatable("gtceu.gui.config_slot.set_only"));
                    }
                    hoverStringList.add(Component.translatable("gtceu.gui.config_slot.remove"));
                }
                setHoverTooltips(hoverStringList);
            }
        } else {
            GenericStack item = null;
            if (mouseOverConfig(mouseX, mouseY)) {
                item = slot.getConfig();
            } else if (mouseOverStock(mouseX, mouseY)) {
                item = slot.getStock();
            }
            if (item != null) {
                setHoverTooltips(Screen.getTooltipFromItem(Minecraft.getInstance(), GenericStack.wrapInItemStack(item)));
            }
        }
    }

    boolean mouseOverConfig(double mouseX, double mouseY) {
        Position position = getPosition();
        return isMouseOver(position.x, position.y, 18, 18, mouseX, mouseY);
    }

    boolean mouseOverStock(double mouseX, double mouseY) {
        Position position = getPosition();
        return isMouseOver(position.x, position.y + 18, 18, 18, mouseX, mouseY);
    }

    boolean isStackValidForSlot(GenericStack stack) {
        if (stack == null || stack.amount() < 0) return true;
        if (!parentWidget.isStocking()) return true;
        return !parentWidget.hasStackInConfig(stack);
    }

    public void setSelect(final boolean select) {
        this.select = select;
    }

    public Object getXEIIngredientOverMouse(double mouseX, double mouseY) {
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.index);
        if (slot == null) {
            return null;
        }
        GenericStack stack = null;
        if (this.mouseOverConfig(mouseX, mouseY)) {
            stack = slot.getConfig();
        } else if (this.mouseOverStock(mouseX, mouseY)) {
            stack = slot.getStock();
        }

        if (stack == null || stack.what() == null) {
            return null;
        }

        EmiStack emiStack = EmiStackHelper.toEmiStack(stack);
        if (emiStack != null) {
            if (emiStack.getAmount() == 0L) {
                emiStack.setAmount(1L);
            }

            return new EmiStackInteraction(emiStack, null, false);
        }
        return null;
    }
}
