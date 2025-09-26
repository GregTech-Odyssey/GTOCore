package com.gtocore.api.gui;

import com.gtocore.common.machine.monitor.DisplayRegistry;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DisplayComponentGroup extends WidgetGroup {

    private static final int PACKET_ID = 1;
    @Nullable
    private final Consumer<List<ResourceLocation>> orderedCallback;
    @NotNull
    private final List<ResourceLocation> originList;
    /// Uses both sides
    private final List<ObjectBooleanPair<ResourceLocation>> current = new ObjectArrayList<>();
    private final Map<ResourceLocation, DisplayComponentWidget> displayWidgets = new O2OOpenCacheHashMap<>();
    private DraggableScrollableWidgetGroup scrollArea;

    public DisplayComponentGroup(@NotNull List<ResourceLocation> originList,
                                 @NotNull List<ResourceLocation> current,
                                 @Nullable Consumer<List<ResourceLocation>> orderedCallback, Position position, Size size) {
        super(position, size);
        this.orderedCallback = orderedCallback;
        this.originList = originList;
        this.current.addAll(current.stream()
                .map(rl -> ObjectBooleanPair.of(rl, true))
                .toList());
        this.current.addAll(originList.stream()
                .filter(rl -> current.stream().noneMatch(rl::equals))
                .map(rl -> ObjectBooleanPair.of(rl, false))
                .toList());
        this.setBackground(GuiTextures.BACKGROUND_INVERSE);

        this.addWidget(new ButtonWidget(this.getSizeWidth() - 16, this.getSizeHeight() - 12, 10, 10, (click) -> {
            if (!isRemote()) {
                reset();
            }
        }).setButtonTexture(GuiTextures.BUTTON, GuiTextures.BUTTON_VOID.copy().scale(0.8f))
                .setHoverTooltips(Component.translatable("gtocore.machine.monitor.adjust_component.reset")));

        init();
    }

    public void init() {
        boolean firstInit = this.scrollArea == null;
        if (firstInit) {
            scrollArea = new DraggableScrollableWidgetGroup(4, 4, this.getSizeWidth() - 8, this.getSizeHeight() - 8);
            this.addWidget(scrollArea);
            scrollArea.setScrollable(true);
            scrollArea.setYBarStyle(GuiTextures.BACKGROUND, GuiTextures.BOX_OVERLAY);
        } else {
            scrollArea.clearAllWidgets();
        }
        int y = 2;
        for (var rl : current) {
            var displayWidget = firstInit ? new DisplayComponentWidget(rl.left(), rl.rightBoolean()) :
                    displayWidgets.get(rl.left()).setEnabled(rl.rightBoolean());
            displayWidget.setSelfPosition(5, y);
            displayWidget.setSize(125, 20);
            scrollArea.acceptWidget(displayWidget);
            displayWidgets.putIfAbsent(rl.left(), displayWidget);
            y += 10;
        }
    }

    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == PACKET_ID) {
            this.current.clear();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ResourceLocation rl = buffer.readResourceLocation();
                boolean enabled = buffer.readBoolean();
                this.current.add(ObjectBooleanPair.of(rl, enabled));
            }
            // Update the corresponding widget
            init();
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }

    /// 仅在服务端调用
    private void reset() {
        this.current.clear();
        this.current.addAll(originList.stream()
                .map(rl -> ObjectBooleanPair.of(rl, true))
                .toList());
        init();
        writeUpdateInfo(PACKET_ID, buf -> {
            buf.writeVarInt(current.size());
            for (var rl : current) {
                buf.writeResourceLocation(rl.left());
                buf.writeBoolean(rl.rightBoolean());
            }
        });
    }

    /// 仅在服务端调用
    private void updateOrdered() {
        if (orderedCallback != null) {
            var orderedList = scrollArea.widgets.stream()
                    .filter(widget -> widget instanceof DisplayComponentWidget)
                    .map(widget -> (DisplayComponentWidget) widget)
                    .filter(DisplayComponentWidget::isEnabled)
                    .map(DisplayComponentWidget::getRL)
                    .toList();
            orderedCallback.accept(orderedList);
            current.clear();
            current.addAll(orderedList.stream()
                    .map(rl -> ObjectBooleanPair.of(rl, true))
                    .toList());
            current.addAll(originList.stream()
                    .filter(rl -> orderedList.stream().noneMatch(rl::equals))
                    .map(rl -> ObjectBooleanPair.of(rl, false))
                    .toList());
            init();
            writeUpdateInfo(PACKET_ID, buf -> {
                buf.writeVarInt(current.size());
                for (var rl : current) {
                    buf.writeResourceLocation(rl.left());
                    buf.writeBoolean(rl.rightBoolean());
                }
            });
        }
    }

    private void moveWidgetUp(DisplayComponentWidget widget) {
        int index = scrollArea.widgets.indexOf(widget);
        if (index > 0) {
            scrollArea.removeWidget(widget);
            scrollArea.widgets.add(index - 1, widget);
        }
        updateOrdered();
    }

    private void moveWidgetDown(DisplayComponentWidget widget) {
        int index = scrollArea.widgets.indexOf(widget);
        if (index < scrollArea.widgets.size() - 1) {
            scrollArea.removeWidget(widget);
            scrollArea.widgets.add(index + 1, widget);
        }
        updateOrdered();
    }

    private class DisplayComponentWidget extends WidgetGroup {

        private final ResourceLocation id;
        private final SwitchWidget switchWidget;
        private final LabelWidget labelWidget;
        private final ButtonWidget upButton;
        private final ButtonWidget downButton;

        @SuppressWarnings("ConstantConditions")
        public DisplayComponentWidget(ResourceLocation id, boolean enabledInitially) {
            this.id = id;

            var color = enabledInitially ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
            this.addWidget(labelWidget = new LabelWidget(0, 0, Component.translatable(DisplayRegistry.langKey(id))
                    .withStyle(color)));
            labelWidget.setClientSideWidget();

            this.addWidget(switchWidget = new SwitchWidget(110, 0, 10, 10, (click, result) -> {
                if (!isRemote()) {
                    updateOrdered();
                } else this.labelWidget.setColor(result ? ChatFormatting.GREEN.getColor() : ChatFormatting.YELLOW.getColor());
            })
                    .setPressed(enabledInitially)
                    .setBaseTexture(GuiTextures.BUTTON,
                            GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                    .copy()
                                    .getSubTexture(0.0F, 0.0F, 1.0F, (double) 0.5F).scale(0.8F))
                    .setPressedTexture(GuiTextures.BUTTON,
                            GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                    .copy()
                                    .getSubTexture(0.0F, 0.5F, 1.0F, (double) 0.5F).scale(0.8F)));
            switchWidget.setHoverTooltips(Component.translatable("gtocore.machine.monitor.adjust_component.switch"));

            // Add up and down buttons for moving the widget
            this.addWidget(upButton = new ButtonWidget(125, 0, 10, 10, (click) -> {
                if (!isRemote()) moveWidgetUp(this);
            }).setButtonTexture(GuiTextures.BUTTON, GuiTextures.BUTTON_RIGHT.copy().rotate(-45).scale(0.8f)));
            upButton.setHoverTooltips(Component.translatable("gtocore.machine.monitor.adjust_component.move_up"));
            upButton.setVisible(enabledInitially);

            this.addWidget(downButton = new ButtonWidget(140, 0, 10, 10, (click) -> {
                if (!isRemote()) moveWidgetDown(this);
            }).setButtonTexture(GuiTextures.BUTTON, GuiTextures.BUTTON_LEFT.copy().rotate(-45).scale(0.8f)));
            downButton.setHoverTooltips(Component.translatable("gtocore.machine.monitor.adjust_component.move_down"));
            downButton.setVisible(enabledInitially);
        }

        public ResourceLocation getRL() {
            return id;
        }

        public boolean isEnabled() {
            return switchWidget.isPressed();
        }

        @SuppressWarnings("ConstantConditions")
        public DisplayComponentWidget setEnabled(boolean enabled) {
            switchWidget.setPressed(enabled);
            upButton.setVisible(enabled);
            downButton.setVisible(enabled);
            if (isRemote()) labelWidget.setColor(enabled ? ChatFormatting.GREEN.getColor() : ChatFormatting.YELLOW.getColor());
            return this;
        }
    }
}
