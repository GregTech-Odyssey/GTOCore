package com.gtocore.api.gui.configurators;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture.TextType;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public abstract class CustomModeFancyConfigurator implements IFancyUIProvider {

    private final int modeSize;

    CustomModeFancyConfigurator(int modeSize) {
        this.modeSize = modeSize;
    }

    public abstract void setMode(int index);

    public abstract int getCurrentMode();

    public abstract String getLanguageKey(int index);

    public Component getTitle() {
        return Component.translatable("gtceu.gui.machinemode.title");
    }

    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTItems.ROBOT_ARM_LV.get());
    }

    public Widget createMainPage(FancyMachineUIWidget widget) {
        MachineModeConfigurator group = new MachineModeConfigurator(0, 0, 140, 20 * modeSize + 4);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        for (int i = 0; i < modeSize; ++i) {
            int finalI = i;
            group.addWidget(new ButtonWidget(2, 2 + i * 20, 136, 20, IGuiTexture.EMPTY, (cd) -> setMode(finalI)));
            group.addWidget(new ImageWidget(2, 2 + i * 20, 136, 20, () -> new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy().setColor(getCurrentMode() == finalI ? ColorPattern.CYAN.color : -1), (new TextTexture(getLanguageKey(finalI))).setWidth(136).setType(TextType.ROLL))));
        }
        return group;
    }

    public List<Component> getTabTooltips() {
        List<Component> tooltip = new ObjectArrayList<>();
        tooltip.add(Component.literal("Change active Machine Mode"));
        return tooltip;
    }

    private class MachineModeConfigurator extends WidgetGroup {

        private MachineModeConfigurator(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public void writeInitialData(FriendlyByteBuf buffer) {
            buffer.writeVarInt(CustomModeFancyConfigurator.this.getCurrentMode());
        }

        public void readInitialData(FriendlyByteBuf buffer) {
            CustomModeFancyConfigurator.this.setMode(buffer.readVarInt());
        }

        public void detectAndSendChanges() {
            this.writeUpdateInfo(0, (buf) -> buf.writeVarInt(CustomModeFancyConfigurator.this.getCurrentMode()));
        }

        public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
            if (id == 0) {
                CustomModeFancyConfigurator.this.setMode(buffer.readVarInt());
            }
        }
    }
}
