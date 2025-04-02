package com.gto.gtocore.client.gui.widget;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.misc.IGhostFluidTarget;
import com.gregtechceu.gtceu.api.gui.misc.IGhostItemTarget;
import com.gregtechceu.gtceu.api.gui.widget.PhantomFluidWidget;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import com.google.common.collect.Lists;
import com.lowdragmc.lowdraglib.gui.ingredient.Target;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.side.fluid.forge.FluidHelperImpl;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.lowdragmc.lowdraglib.gui.util.DrawerHelper.*;
import static com.lowdragmc.lowdraglib.gui.widget.PhantomFluidWidget.drainFrom;

/**
 * @author EasterFG on 2025/3/7
 */
public final class AEDualConfigSlotWidget extends Widget implements IGhostItemTarget, IGhostFluidTarget {

    private final AEDualConfigWidget parentWidget;
    private final int index;
    private final static int REMOVE_ID = 1000;
    private final static int ITEM_UPDATE_ID = 1001;
    private final static int FLUID_UPDATE_ID = 1002;

    @Setter
    private boolean select = false;

    public AEDualConfigSlotWidget(int x, int y, AEDualConfigWidget widget, int index) {
        super(new Position(x, y), new Size(18, 18 << 1));
        this.parentWidget = widget;
        this.index = index;
    }

    private int getIndex() {
        return (this.parentWidget.page - 1) * AEDualConfigWidget.CONFIG_SIZE + index;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawInForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.getIndex());
        if (slot.getConfig() == null) {
            if (mouseOverConfig(mouseX, mouseY)) {
                List<Component> hoverStringList = new ArrayList<>();
                hoverStringList.add(Component.translatable("gtceu.gui.config_slot"));
                if (this.parentWidget.isAutoPull()) {
                    hoverStringList.add(Component.translatable("gtceu.gui.config_slot.auto_pull_managed"));
                } else {
                    hoverStringList.add(Component.translatable("gtceu.gui.config_slot.set_only"));
                    hoverStringList.add(Component.translatable("gtceu.gui.config_slot.remove"));
                }
                graphics.renderTooltip(Minecraft.getInstance().font, hoverStringList, Optional.empty(), mouseX, mouseY);
            }
        } else {
            GenericStack item = null;
            if (mouseOverConfig(mouseX, mouseY)) {
                item = slot.getConfig();
            } else if (mouseOverStock(mouseX, mouseY)) {
                item = slot.getStock();
            }
            if (item != null) {
                graphics.renderTooltip(Minecraft.getInstance().font, GenericStack.wrapInItemStack(item), mouseX,
                        mouseY);
            }
        }
    }

    private boolean mouseOverConfig(double mouseX, double mouseY) {
        Position position = getPosition();
        return isMouseOver(position.x, position.y, 18, 18, mouseX, mouseY);
    }

    private boolean mouseOverStock(double mouseX, double mouseY) {
        Position position = getPosition();
        return isMouseOver(position.x, position.y + 18, 18, 18, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawSelectionOverlay(GuiGraphics graphics, int x, int y) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        drawGradientRect(graphics, x, y, 16, 16, -2130706433, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    private boolean isStackValidForSlot(GenericStack stack) {
        if (stack == null || stack.amount() < 0) return false;
        return parentWidget.hasStackInConfig(stack);
    }

    // custom draw

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position position = getPosition();
        IConfigurableSlot slot = this.parentWidget.getDisplay(this.getIndex());
        GenericStack config = slot.getConfig();
        GenericStack stock = slot.getStock();
        drawSlots(graphics, mouseX, mouseY, position.x, position.y);
        if (this.select) {
            GuiTextures.SELECT_BOX.draw(graphics, mouseX, mouseY, position.x, position.y, 18, 18);
        }
        int stackX = position.x + 1;
        int stackY = position.y + 1;
        if (config != null) {
            if (config.what() instanceof AEItemKey itemKey) {
                ItemStack stack = new ItemStack(itemKey.getItem(), (int) config.amount());
                stack.setCount(1);
                drawItemStack(graphics, stack, stackX, stackY, 0xFFFFFFFF, null);
            } else if (config.what() instanceof AEFluidKey) {
                var stack = AEUtil.toFluidStack(config);
                if (!stack.isEmpty()) {
                    DrawerHelper.drawFluidForGui(graphics, FluidHelperImpl.toFluidStack(stack), config.amount(), stackX, stackY, 16, 16);
                }
            }
        }

        if (stock != null) {
            if (stock.what() instanceof AEItemKey itemKey) {
                ItemStack stack = new ItemStack(itemKey.getItem(), (int) stock.amount());
                stack.setCount(1);
                drawItemStack(graphics, stack, stackX, stackY + 18, 0xFFFFFFFF, null);
            } else if (stock.what() instanceof AEFluidKey) {
                var stack = AEUtil.toFluidStack(stock);
                if (!stack.isEmpty()) {
                    DrawerHelper.drawFluidForGui(graphics, FluidHelperImpl.toFluidStack(stack), stock.amount(), stackX, stackY + 18, 16, 16);
                }
            }
            if (stock.amount() > 0) {
                drawStringFixedCorner(graphics, stock.what().formatAmount(stock.amount(), AmountFormat.SLOT),
                        stackX + 17, stackY + 18 + 17, 16777215, true, 0.5f);
            }
            if (mouseOverConfig(mouseX, mouseY)) {
                drawSelectionOverlay(graphics, stackX, stackY);
            } else if (mouseOverStock(mouseX, mouseY)) {
                drawSelectionOverlay(graphics, stackX, stackY + 18);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void drawSlots(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {
        if (parentWidget.isAutoPull()) {
            GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
            GuiTextures.CONFIG_ARROW_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
        } else {
            GuiTextures.FLUID_SLOT.draw(graphics, mouseX, mouseY, x, y, 18, 18);
            GuiTextures.CONFIG_ARROW.draw(graphics, mouseX, mouseY, x, y, 18, 18);
        }
        GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y + 18, 18, 18);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseOverConfig(mouseX, mouseY)) {
            if (parentWidget.isAutoPull()) {
                return false;
            }

            if (button == 1) {
                writeClientAction(REMOVE_ID, buf -> {});
            } else if (button == 0) {
                ItemStack hold = this.gui.getModularUIContainer().getCarried();
                FluidStack fluidStack = FluidUtil.getFluidContained(hold).orElse(FluidStack.EMPTY);
                if (fluidStack != FluidStack.EMPTY) {
                    writeClientAction(FLUID_UPDATE_ID, fluidStack::writeToPacket);
                } else if (!hold.isEmpty()) {
                    writeClientAction(ITEM_UPDATE_ID, buf -> buf.writeItem(hold));
                }
            }
        }
        return false;
    }

    // handler network

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        int index = this.getIndex();
        IConfigurableSlot slot = this.parentWidget.getConfig(index);
        if (id == REMOVE_ID) {
            slot.setConfig(null);
            writeUpdateInfo(REMOVE_ID, buf -> {});
        }

        if (id == ITEM_UPDATE_ID) {
            ItemStack item = buffer.readItem();
            var stack = GenericStack.fromItemStack(item);
            if (isStackValidForSlot(stack)) return;
            // item set
            this.parentWidget.setItemConfig(index, stack);
            if (!item.isEmpty()) {
                writeUpdateInfo(ITEM_UPDATE_ID, buf -> buf.writeItem(item));
            }
        }

        if (id == FLUID_UPDATE_ID) {
            var fluid = net.minecraftforge.fluids.FluidStack.readFromPacket(buffer);
            var stack = AEUtil.fromFluidStack(fluid);
            if (isStackValidForSlot(stack)) return;
            this.parentWidget.setFluidConfig(index, stack);
            if (fluid != net.minecraftforge.fluids.FluidStack.EMPTY) {
                writeUpdateInfo(FLUID_UPDATE_ID, fluid::writeToPacket);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        int index = this.getIndex();
        IConfigurableSlot slot = this.parentWidget.getDisplay(index);
        if (id == REMOVE_ID) {
            slot.setConfig(null);
        }
        if (id == ITEM_UPDATE_ID) {
            ItemStack item = buffer.readItem();
            this.parentWidget.setItemConfig(index, new GenericStack(AEItemKey.of(item.getItem(), item.getTag()), item.getCount()));
        }
        if (id == FLUID_UPDATE_ID) {
            FluidStack fluid = new FluidStack(BuiltInRegistries.FLUID.get(buffer.readResourceLocation()), buffer.readVarInt());
            this.parentWidget.setFluidConfig(index, new GenericStack(AEFluidKey.of(fluid.getFluid()), fluid.getAmount()));
        }
    }

    // Interface IGhostItemTarget and IGhostFluidTarget

    @OnlyIn(Dist.CLIENT)
    @Override
    public Rect2i getRectangleBox() {
        Rect2i rectangle = toRectangleBox();
        rectangle.setHeight(rectangle.getHeight() / 2);
        return rectangle;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Target> getPhantomTargets(Object ingredient) {
        Object result = convertIngredient(ingredient);
        final Rect2i rectangle = getRectangleBox();
        if (result instanceof ItemStack) {
            return Lists.newArrayList(new Target() {

                @NotNull
                @Override
                public Rect2i getArea() {
                    return rectangle;
                }

                @Override
                public void accept(Object ingredient) {
                    ingredient = convertIngredientItem(ingredient);

                    if (ingredient instanceof ItemStack stack) {
                        acceptItem(stack);
                    }
                }
            });
        } else if (result instanceof FluidStack && drainFrom(ingredient) != null) {
            return Lists.newArrayList(new Target[] { new Target() {

                @NotNull
                public Rect2i getArea() {
                    return rectangle;
                }

                public void accept(@NotNull Object ingredient) {
                    ingredient = convertIngredient(ingredient);
                    FluidStack ingredientStack;
                    if (ingredient instanceof FluidStack fluidStack) {
                        ingredientStack = fluidStack;
                    } else {
                        ingredientStack = PhantomFluidWidget.drainFrom(ingredient);
                    }

                    if (ingredientStack != null) {
                        acceptFluid(ingredientStack);
                    }
                }
            } });
        }
        return Collections.emptyList();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object convertIngredient(Object ingredient) {
        Object result = IGhostItemTarget.super.convertIngredient(ingredient);
        if (result instanceof ItemStack stack && !stack.isEmpty()) return result;
        result = this.convertIngredientFluid(ingredient);
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    private Object convertIngredientItem(Object ingredient) {
        return IGhostItemTarget.super.convertIngredient(ingredient);
    }

    @OnlyIn(Dist.CLIENT)
    private Object convertIngredientFluid(Object ingredient) {
        return IGhostFluidTarget.super.convertIngredient(ingredient);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void acceptItem(ItemStack itemStack) {
        writeClientAction(ITEM_UPDATE_ID, buf -> buf.writeItem(itemStack));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void acceptFluid(FluidStack fluidStack) {
        if (fluidStack.getRawFluid() != Fluids.EMPTY && fluidStack.getAmount() <= 0) {
            fluidStack.setAmount(1000);
        }

        if (!fluidStack.isEmpty()) {
            Objects.requireNonNull(fluidStack);
            this.writeClientAction(FLUID_UPDATE_ID, fluidStack::writeToPacket);
        }
    }
}
