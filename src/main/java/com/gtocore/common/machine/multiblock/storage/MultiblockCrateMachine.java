package com.gtocore.common.machine.multiblock.storage;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiblockCrateMachine extends MultiblockControllerMachine implements IUIMachine, IDropSaveMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MultiblockCrateMachine.class, MultiblockControllerMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    public final NotifiableItemStackHandler inventory;

    public MultiblockCrateMachine(IMachineBlockEntity holder) {
        super(holder);
        this.inventory = new NotifiableItemStackHandler(this, 576, IO.BOTH);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return isFormed && IUIMachine.super.shouldOpenUI(player, hand, hit);
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return isFormed ? super.getItemHandlerCap(side, useCoverCapability) : (IItemHandlerModifiable) EmptyHandler.INSTANCE;
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int xOffset = 162;
        int yOverflow = 9;
        // int yOffset = (576 - 3 * yOverflow) / yOverflow * 18;
        var modularUI = new ModularUI(xOffset + 19, 244, this, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7, 162, true));

        var innerContainer = new DraggableScrollableWidgetGroup(4, 4, xOffset + 6, 130)
                .setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON).setYScrollBarWidth(4);
        int x = 0;
        int y = 0;
        for (int slot = 0; slot < 576; slot++) {
            innerContainer.addWidget(new SlotWidget(inventory, slot, x * 18, y * 18).setBackgroundTexture(GuiTextures.SLOT));
            x++;
            if (x == yOverflow) {
                x = 0;
                y++;
            }
        }
        var container = new WidgetGroup(
                3, 17, xOffset + 20, 140).addWidget(innerContainer);
        return modularUI.widget(container);
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        inventory.storage.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        tag.put("inventory", inventory.storage.serializeNBT());
    }
}
