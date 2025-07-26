package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;

import java.util.List;

public class MonitorCustomInfo extends AbstractInfoProviderMonitor {

    private static final char FORMATTING_CODE = 167;
    private static final int MAX_LENGTH = 100;
    @DescSynced
    @Persisted
    private String content = "";

    public MonitorCustomInfo(IMachineBlockEntity holder) {
        super(holder);
    }

    public MonitorCustomInfo(Object o) {
        this((IMachineBlockEntity) o);
    }

    @Override
    public void syncInfoFromServer() {}

    @Override
    public Widget createUIWidget() {
        var textField = new TextFieldWidget(20, 10, 120, 30, this::getContent, this::setContent);
        textField.setBackground(GuiTextures.NUMBER_BACKGROUND);
        textField.setHoverTooltips(Component.translatable("gtocore.machine.monitor.custom_info.tooltip"));
        textField.setMaxStringLength(MAX_LENGTH);

        var formattingCodeInputButton = new ButtonWidget(160, 10, 30, 30, (click) -> {
            if (isRemote()) {
                // client side
                Minecraft.getInstance().keyboardHandler.setClipboard(String.valueOf(FORMATTING_CODE));
            }
        });
        formattingCodeInputButton.setButtonTexture(GuiTextures.BUTTON)
                .setHoverTooltips(
                        Component.translatable("gtocore.machine.monitor.custom_info.code_input_tooltip.1"),
                        Component.translatable("gtocore.machine.monitor.custom_info.code_input_tooltip.2"),
                        Component.translatable("gtocore.machine.monitor.custom_info.code_input_tooltip.3"));

        LongInputWidget input = new LongInputWidget(Position.of(50, 70),
                this::getPriority, this::setPriority);
        input.setMax((long) Integer.MAX_VALUE).setMin((long) Integer.MIN_VALUE);
        input.setHoverTooltips(Component.translatable("gtocore.machine.monitor.priority"));

        var panel = new ComponentPanelWidget(
                input.getPositionX() + input.getSizeWidth() / 2,
                input.getPositionY() - 18,
                List.of(Component.translatable("gtocore.machine.monitor.priority").withStyle(ChatFormatting.BLACK)))
                .setCenter(true)
                .setClientSideWidget();
        return ((new WidgetGroup(0, 0, 200, 90))).addWidget(input).addWidget(textField).addWidget(formattingCodeInputButton).addWidget(panel);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public DisplayComponentList provideInformation() {
        var infoList = super.provideInformation();
        infoList.addIfAbsent(
                DisplayRegistry.CUSTOM_DISPLAY.id(),
                Component.literal(getContent())
                        .getVisualOrderText());
        return infoList;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.add(DisplayRegistry.CUSTOM_DISPLAY.id());
        return rls;
    }
}
