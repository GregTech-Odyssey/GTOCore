package com.gto.gtocore.api.gui;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.Widget;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;

/**
 * @author EasterFG on 2025/3/22
 */
public class TurnsConfiguratorButton implements IFancyConfiguratorButton {

    protected IGuiTexture[] group;
    protected BiConsumer<ClickData, Integer> onClick;
    protected IntSupplier intSupplier;
    int mode;
    Function<Integer, List<Component>> tooltipsSupplier = isPressed -> Collections.emptyList();

    public TurnsConfiguratorButton(IntSupplier intSupplier, BiConsumer<ClickData, Integer> onClick, IGuiTexture... turns) {
        this.intSupplier = intSupplier;
        this.onClick = onClick;
        this.group = turns;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public Widget createConfigurator() {
        return null;
    }

    @Override
    public IGuiTexture getIcon() {
        return mode < group.length ? group[mode] : IGuiTexture.MISSING_TEXTURE;
    }

    public TurnsConfiguratorButton setTooltipsSupplier(Function<Integer, List<Component>> tooltipsSupplier) {
        this.tooltipsSupplier = tooltipsSupplier;
        return this;
    }

    @Override
    public List<Component> getTooltips() {
        return tooltipsSupplier.apply(mode);
    }

    @Override
    public void detectAndSendChange(BiConsumer<Integer, Consumer<FriendlyByteBuf>> sender) {
        var newValue = intSupplier.getAsInt();
        if (newValue != mode) {
            mode = newValue;
            sender.accept(0, buf -> buf.writeInt(mode));
        }
    }

    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buf) {
        if (id == 0) {
            mode = buf.readInt();
        }
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        this.mode = intSupplier.getAsInt();
        buffer.writeInt(this.mode);
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        this.mode = buffer.readInt();
    }

    @Override
    public void onClick(ClickData clickData) {
        mode++;
        if (mode == group.length) {
            mode = 0;
        }
        onClick.accept(clickData, mode);
    }
}
