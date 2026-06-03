package com.gtocore.common.machine.multiblock.part;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.recipe.RecipeHelper;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import lombok.Getter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RadiationHatchPartMachine extends MultiblockPartMachine implements IMachineLife {

    @SaveToDisk
    private final NotifiableItemStackHandler inventory;
    @Getter
    @SaveToDisk
    private int radioactivity;
    @SaveToDisk
    private int initialRadioactivity;
    @SaveToDisk
    private int count;
    @SaveToDisk
    private int time;
    @SaveToDisk
    private int inhibitionDose;
    @SaveToDisk
    private int initialTime;

    private TickableSubscription radiationSubs;
    private final RecipeHandlerUnit handlerList;

    public RadiationHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH);
        handlerList = RecipeHandlerUnit.of(IO.IN, inventory);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        radiationSubs = subscribeServerTick(radiationSubs, this::checkRadiation);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (radiationSubs != null) {
            radiationSubs.unsubscribe();
            radiationSubs = null;
        }
    }

    private void checkRadiation() {
        if (time > 0) {
            if (count < 1) {
                radioactivity = initialRadioactivity * (initialTime + time) / (initialTime << 1);
            }
            time--;
        } else if (getOffsetTimer() % 20 == 0) {
            radioactivity = 0;
            GTRecipeType[] recipeTypes = getDefinition().getRecipeTypes();
            if (recipeTypes != null) {
                GTRecipeType recipeType = recipeTypes[0];
                handlerList.findRecipe(recipeType, (u, r) -> {
                    if (handlerList.handleRecipeItem(IO.IN, r.toRuntime(), RecipeHelper.copyContents(r.itemInputs, 1), false)) {
                        count = inventory.storage.getStackInSlot(0).getCount();
                        initialRadioactivity = (int) ((r.data.getInt(GTORecipeDataKeys.RADIOACTIVITY) - inhibitionDose) * (1 + ((double) count / 64)));
                        initialTime = r.duration * (inhibitionDose + 200) / 200;
                        time = initialTime;
                        radioactivity = initialRadioactivity;
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(GuiTextures.DISPLAY).addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId())).addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));
        var size = group.getSize();
        group.addWidget(new SlotWidget(inventory.storage, 0, size.width - 30, size.height - 30, true, true).setBackground(GuiTextures.SLOT));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.radiation_hatch.inhibition_dose", inhibitionDose).append(ComponentPanelWidget.withButton(Component.literal(" [-]"), "Sub")).append(ComponentPanelWidget.withButton(Component.literal(" [+]"), "Add")));
        textList.add(Component.translatable("gtocore.recipe.radioactivity", radioactivity));
        textList.add(Component.translatable("gtocore.machine.radiation_hatch.time", time, initialTime));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            var amount = clickData.isCtrlClick ? 40 : (clickData.isShiftClick ? 8 : 1);
            inhibitionDose = Mth.clamp(inhibitionDose + ("Add".equals(componentData) ? amount : -amount), 0, 40);
        }
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    @Override
    public boolean canShared() {
        return false;
    }
}
