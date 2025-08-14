package com.gtocore.integration.gtmt;

import com.gtocore.common.block.BlockMap;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class BlockMapSelector extends WidgetGroup {

    static int blockMapSize;
    static int blockMapValuesMaxSize;
    static boolean frozen = false;
    private final DraggableScrollableWidgetGroup categoryScrollArea;
    private final DraggableScrollableWidgetGroup blockScrollArea;
    private final WidgetGroup categoryPlaceholder;
    private final WidgetGroup blockPlaceholder;
    private final WidgetGroup[] categoryWidgets;
    private final BlockSelectorButton[] blockMapValuesWidgets;

    private String text = "";
    private int tier = 0;
    private final BiConsumer<String, Integer> onConfirm;

    private boolean isCategoryScrollAreaOpen = false;
    private boolean isBlockScrollAreaOpen = false;

    public BlockMapSelector(int x, int y, int width, int height, BiConsumer<String, Integer> onConfirm) {
        super(x, y, width, height);
        if (!frozen) {
            blockMapSize = BlockMap.MAP.size();
            blockMapValuesMaxSize = BlockMap.MAP.values().stream()
                    .mapToInt(map -> map.length)
                    .max()
                    .orElse(0);
            frozen = true;
        }
        this.categoryWidgets = new WidgetGroup[blockMapSize];
        this.blockMapValuesWidgets = new BlockSelectorButton[blockMapValuesMaxSize];

        this.onConfirm = onConfirm;

        this.categoryScrollArea = (DraggableScrollableWidgetGroup) new DraggableScrollableWidgetGroup(0, 0, 100, 100).setActive(false).setVisible(false).setBackground(GuiTextures.BACKGROUND_INVERSE);
        this.blockScrollArea = (DraggableScrollableWidgetGroup) new DraggableScrollableWidgetGroup(0, 0, 100, 100).setActive(false).setVisible(false).setBackground(GuiTextures.BACKGROUND_INVERSE);
        this.addWidget(categoryScrollArea);
        this.addWidget(blockScrollArea);
        this.addWidget(new ToggleButtonWidget(getSizeWidth() - getSizeHeight(), 0, getSizeHeight(), getSizeHeight(), this::isCategoryScrollAreaOpen, pressed -> {
            isCategoryScrollAreaOpen = pressed;
            if (pressed) {
                categoryScrollArea.setActive(true);
                categoryScrollArea.setVisible(true);
                this.onCategoryAreaActivate();
            } else {
                collapseAll();
            }
        }).setTexture(new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, new TextTexture("✎")), new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, new TextTexture("✘"))).setHoverTooltips(Component.literal("ID")));

        this.categoryPlaceholder = new WidgetGroup();
        this.blockPlaceholder = new WidgetGroup();

        this.categoryScrollArea.setSelfPosition(this.getSizeWidth() + 19, 0);
        this.categoryScrollArea.setSize(100, 160 + 20);
        this.categoryScrollArea.addWidget(categoryPlaceholder);// 专治各种不能滚动
        this.blockScrollArea.setSelfPosition(this.getSizeWidth() + 23 + this.categoryScrollArea.getSizeWidth(), 0);
        this.blockScrollArea.setSize(100, 180);
        this.blockScrollArea.addWidget(blockPlaceholder);
    }

    public boolean isCategoryScrollAreaOpen() {
        return isCategoryScrollAreaOpen;
    }

    public void collapseAll() {
        isCategoryScrollAreaOpen = false;
        isBlockScrollAreaOpen = false;
        categoryScrollArea.setActive(false);
        categoryScrollArea.setVisible(false);
        blockScrollArea.setActive(false);
        blockScrollArea.setVisible(false);
    }

    public void onCategoryAreaActivate() {
        AtomicInteger rowIndex = new AtomicInteger(0);
        for (var entry : BlockMap.MAP.entrySet()) {
            String category = entry.getKey();
            WidgetGroup widgetGroup;
            if (categoryWidgets[rowIndex.get()] != null) {
                widgetGroup = categoryWidgets[rowIndex.getAndIncrement()];
            } else {
                widgetGroup = new WidgetGroup(0, 0, getSizeWidth() - 8, 16);
                categoryWidgets[rowIndex.get()] = widgetGroup;

                widgetGroup.setVisible(true);
                widgetGroup.setActive(true);

                widgetGroup.addWidget(new LabelWidget(4, 6 + 16 * rowIndex.get(), () -> Component.translatable(BlockMap.namePrefix + "." + category).getString()));
                widgetGroup.addWidget(new ToggleButtonWidget(80, 6 + 16 * rowIndex.getAndIncrement(), 12, 12, () -> isBlockScrollAreaOpen, pressed -> {
                    isBlockScrollAreaOpen = pressed;
                    if (pressed) {
                        blockScrollArea.setActive(true);
                        blockScrollArea.setVisible(true);
                        onBlockAreaActivate(category);
                    } else {
                        blockScrollArea.setActive(false);
                        blockScrollArea.setVisible(false);
                    }
                })
                        .setTexture(new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, new TextTexture("▶")), new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, new TextTexture("◀")))
                        .setHoverTooltips(Component.translatable("gtocore.adv_terminal.category.select")));
                categoryPlaceholder.addWidget(widgetGroup);
            }
        }
        categoryPlaceholder.setSize(getSizeWidth() - 8, rowIndex.get() * 16 + 20);
    }

    public void onBlockAreaActivate(String category) {
        int rowIndex = 0;
        for (BlockSelectorButton blockMapValuesWidget : blockMapValuesWidgets) {
            if (blockMapValuesWidget != null) {
                blockPlaceholder.removeWidget(blockMapValuesWidget);
            }
        }
        for (var block : BlockMap.MAP.getOrDefault(category, new Block[0])) {
            Component blockName = block.getName();
            BlockSelectorButton widgetGroup;
            if (blockMapValuesWidgets[rowIndex] != null) {
                widgetGroup = blockMapValuesWidgets[rowIndex];
            } else {
                widgetGroup = new BlockSelectorButton();
                blockMapValuesWidgets[rowIndex] = widgetGroup;
            }
            blockPlaceholder.addWidget(widgetGroup);
            widgetGroup.tier = rowIndex + 1;

            widgetGroup.label.setComponent(blockName);
            widgetGroup.label.setTextSupplier(blockName::getString);
            widgetGroup.label.setSelfPosition(5, 6 + 16 * rowIndex);

            widgetGroup.button.setSelfPosition(100 - 20, 6 + 16 * rowIndex++);
            widgetGroup.button.setOnPressCallback((cd) -> {
                text = blockName.getString();
                tier = widgetGroup.tier;
                onConfirm.accept(category, tier);
            }).setHoverTooltips(Component.translatable("gtocore.adv_terminal.block.confirm"));

        }
        blockPlaceholder.setSize(getSizeWidth() - 8, rowIndex * 16 + 20);
    }

    private static class BlockSelectorButton extends WidgetGroup {

        private final LabelWidget label;
        private final ButtonWidget button;
        private int tier;

        public BlockSelectorButton() {
            super(0, 0, 100, 20);
            this.label = new LabelWidget(5, 5, Component.literal("Select Block"));
            this.button = (ButtonWidget) new ButtonWidget(80, 5, 12, 12, (cd) -> {
                // Logic to select block
            }).setHoverTooltips(Component.translatable("gtocore.adv_terminal.block.select"))
                    .setBackground(new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, new TextTexture("✔")));
            this.addWidget(label);
            this.addWidget(button);
        }
    }
}
