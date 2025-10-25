package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.cover.filter.TagFilter;
import com.gregtechceu.gtceu.api.cover.filter.TagFluidFilter;
import com.gregtechceu.gtceu.api.cover.filter.TagItemFilter;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

interface ITagFilterPartMachine extends IDropSaveMachine {

    String getTagWhite();

    String getTagBlack();

    void setTagWhite(String tagWhite);

    void setTagBlack(String tagBlack);

    @Override
    default void saveToItem(CompoundTag tag) {
        IDropSaveMachine.super.saveToItem(tag);
        tag.putString("TagWhite", getTagWhite());
        tag.putString("TagBlack", getTagBlack());
    }

    @Override
    default void loadFromItem(CompoundTag tag) {
        IDropSaveMachine.super.loadFromItem(tag);
        if (tag.contains("TagWhite")) {
            setTagWhite(tag.getString("TagWhite"));
        }
        if (tag.contains("TagBlack")) {
            setTagBlack(tag.getString("TagBlack"));
        }
    }

    class FilterIFancyConfigurator implements IFancyConfigurator {

        private final ITagFilterPartMachine machine;
        private TagFilter.StackHandlerWidget<?, ?> handlerWidget;

        FilterIFancyConfigurator(ITagFilterPartMachine machine) {
            this.machine = machine;
        }

        @Override
        public Component getTitle() {
            return Component.translatable("gtocore.machine.tag_filter.tag_config_title");
        }

        @Override
        public IGuiTexture getIcon() {
            return GuiTextures.BUTTON_BLACKLIST.getSubTexture(0, 0, 20, 20);
        }

        @Override
        public Widget createConfigurator() {
            TextField t1, t2;
            TagFilter.StackHandlerWidget<?, ?> h1, h2;
            boolean isItem = machine instanceof METagFilterStockBusPartMachine;
            WidgetGroup wc = new WidgetGroup(-25, 0, 150, 100)
                    .addWidget(new LabelWidget(9, 4,
                            () -> "gui.enderio.filter.whitelist"))
                    .addWidget(t1 = new TextField(9, 16, 114, 16,
                            machine::getTagWhite,
                            machine::setTagWhite))
                    .addWidget(((Widget) (h1 = isItem ?
                            new TagItemFilter.PhantomSlot(new CustomItemStackHandler(1)) :
                            new TagFluidFilter.TankSlot(new CustomFluidTank(1)))))
                    .addWidget(new LabelWidget(9, 36,
                            () -> "gui.enderio.filter.blacklist"))
                    .addWidget(t2 = new TextField(9, 48, 114, 16,
                            machine::getTagBlack,
                            machine::setTagBlack))
                    .addWidget((Widget) (h2 = isItem ?
                            new TagItemFilter.PhantomSlot(new CustomItemStackHandler(1)) :
                            new TagFluidFilter.TankSlot(new CustomFluidTank(1))))
                    .addWidget(new LabelWidget(0, 68,
                            () -> "gtocore.machine.tag_filter.tooltip.0"))
                    .addWidget(new LabelWidget(0, 84,
                            () -> "gtocore.machine.tag_filter.tooltip.1"));
            DraggableScrollableWidgetGroup container = new DraggableScrollableWidgetGroup(0, 100, 140, 100);
            container.setClientSideWidget().setActive(false).setVisible(false).setBackground(GuiTextures.BACKGROUND_INVERSE);
            BiFunction<TagFilter.StackHandlerWidget<?, ?>, TextField, Runnable> callbackFactory = (TagFilter.StackHandlerWidget<?, ?> hw, TextField tf) -> () -> {
                var tags = hw.getTags()
                        .map(tag -> tag.location().toString())
                        .toList();
                Widget[] newWidgets = createTagLabelContainer(tf, tags);
                container.clearAllWidgets();
                container.addWidgets(newWidgets);
                if (!hw.isEmpty()) {
                    container.setVisible(true).setActive(true);
                } else {
                    container.setVisible(false).setActive(false);
                }
            };
            h1.setOnContentsChanged(callbackFactory.apply(h1, t1));
            ((Widget) h1).setSelfPosition(t1.getSelfPositionX() + t1.getSizeWidth() + 4, t1.getSelfPositionY());
            h2.setOnContentsChanged(callbackFactory.apply(h2, t2));
            ((Widget) h2).setSelfPosition(t2.getSelfPositionX() + t2.getSizeWidth() + 4, t2.getSelfPositionY());
            return wc.addWidget(container);
        }

        /**
         * 创建标签展示和交互容器，供子类 openConfigurator 使用。
         */
        protected Widget[] createTagLabelContainer(TextField textFieldWidget, List<String> tags) {
            var atomicI = new AtomicInteger(0);
            var container = new Widget[tags.size()];
            for (String tag : tags) {
                container[atomicI.get()] = (new LabelWidget(4, atomicI.getAndIncrement() * 12 + 4, tag) {

                    @Override
                    public boolean mouseReleased(double mouseX, double mouseY, int button) {
                        if (isMouseOverElement(mouseX, mouseY)) {
                            if (button == 0) {
                                textFieldWidget.setDirectly(tag);
                            } else if (button == 1) {
                                Minecraft.getInstance().keyboardHandler.setClipboard(tag);
                            }
                            playButtonClickSound();
                            return true;
                        }
                        return super.mouseReleased(mouseX, mouseY, button);
                    }
                }.setTextColor(0x39c5bb).setHoverTooltips(Component.translatable("gtocore.part.extendae.tag_filter.tooltip")).setClientSideWidget());
            }
            return container;
        }
    }

    class TextField extends TextFieldWidget {

        public TextField(int x, int y, int width, int height, Supplier<String> textSupplier, Consumer<String> textConsumer) {
            super(x, y, width, height, textSupplier, textConsumer);
        }

        public void setDirectly(String newTextString) {
            this.setCurrentString(newTextString);
            this.writeClientAction(1, buf -> buf.writeUtf(newTextString));
        }
    }
}
