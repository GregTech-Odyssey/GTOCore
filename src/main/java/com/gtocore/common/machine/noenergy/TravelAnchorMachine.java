package com.gtocore.common.machine.noenergy;

import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.eio_travel.ITravelTarget;
import com.gtocore.eio_travel.api.TravelSavedData;
import com.gtocore.eio_travel.implementations.AnchorTravelTarget;

import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TravelAnchorMachine extends MetaMachine implements IFancyUIMachine, IMachineLife {

    @Persisted
    private final ItemStackTransfer itemTransfer;

    public TravelAnchorMachine(MetaMachineBlockEntity holder) {
        super(holder);
        itemTransfer = new ItemStackTransfer();
        itemTransfer.setOnContentsChanged(this::onInventoryContentsChanged);
    }

    @Override
    public void onMachineRemoved() {
        IMachineLife.super.onMachineRemoved();
        if (getLevel() != null) {
            TravelSavedData.getTravelData(getLevel()).removeTravelTargetAt(getLevel(), getPos());
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 140, 100);
        var setNameLabel = new LabelWidget(14, 8, "ftblibrary.select_item.display_name");
        group.addWidget(setNameLabel);
        var textInputWidget = new TextFieldWidget()
                .setTextSupplier(this::getName)
                .setTextResponder(this::setName);
        textInputWidget.setSelfPosition(14, 20);
        group.addWidget(textInputWidget);
        var toggleButton = createToggleButton();
        toggleButton.setSelfPosition(110, 20);
        group.addWidget(toggleButton);
        var size = group.getSize();
        var setIconLabel = new LabelWidget(14, size.height - 30, "ftbquests.icon");
        group.addWidget(setIconLabel);
        var slot = new PhantomSlotWidget(itemTransfer, 0, size.width - 30, size.height - 30);
        slot.setMaxStackSize(1);
        slot.setBackground(GuiTextures.SLOT);
        group.addWidget(slot);
        return group;
    }

    private void onInventoryContentsChanged() {
        ItemStack stack = itemTransfer.getStackInSlot(0);
        setIcon(stack.getItem());
    }

    @Nullable
    public String getName() {
        return getOrCreateTravelTarget().getName();
    }

    public void setName(String name) {
        getOrCreateTravelTarget().setName(name);
    }

    public Item getIcon() {
        return getOrCreateTravelTarget().getIcon();
    }

    public void setIcon(Item icon) {
        getOrCreateTravelTarget().setIcon(icon);
    }

    public boolean getVisibility() {
        return getOrCreateTravelTarget().getVisibility();
    }

    public void setVisibility(boolean visible) {
        getOrCreateTravelTarget().setVisibility(visible);
    }

    private AnchorTravelTarget getOrCreateTravelTarget() {
        var worldPosition = getPos();
        Optional<ITravelTarget> travelTarget = getTravelData().getTravelTarget(worldPosition);
        if (travelTarget.isPresent() && travelTarget.get() instanceof AnchorTravelTarget anchorTravelTarget) {
            return anchorTravelTarget;
        }
        AnchorTravelTarget anchorTravelTarget = new AnchorTravelTarget(worldPosition, "", Items.AIR, false);
        getTravelData().addTravelTarget(getLevel(), anchorTravelTarget);
        return anchorTravelTarget;
    }

    public TravelSavedData getTravelData() {
        return TravelSavedData.getTravelData(getLevel());
    }

    private ToggleButtonWidget createToggleButton() {
        ObjectHolder<ToggleButtonWidget> toggleHolder = new ObjectHolder<>(null);
        ToggleButtonWidget toggleButton = new ToggleButtonWidget(
                0, 40, 16, 16,
                this::getVisibility,
                pressed -> {
                    setVisibility(pressed);
                    if (LDLib.isRemote()) {
                        toggleHolder.value.setHoverTooltips(getConfigureLevelsOrPointsTooltip(pressed));
                    }
                });
        toggleHolder.value = toggleButton;
        toggleButton.setPressed(getVisibility());
        toggleButton.setTexture(new GuiTextureGroup(GuiTextures.BUTTON, GTOGuiTextures.SMALL_XP_ORB),
                new GuiTextureGroup(GuiTextures.BUTTON, GTOGuiTextures.LARGE_XP_ORB.scale(0.8f)));
        toggleButton.setHoverTooltips(getConfigureLevelsOrPointsTooltip(getVisibility()));
        return toggleButton;
    }

    private Component getConfigureLevelsOrPointsTooltip(boolean pressed) {
        return Component.translatable("emi.header." + (pressed ? "visible" : "invisible"));
    }
}
