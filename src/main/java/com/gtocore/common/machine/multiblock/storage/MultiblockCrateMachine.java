package com.gtocore.common.machine.multiblock.storage;

import com.gtocore.common.network.ClientMessage;

import com.gtolib.GTOCore;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.api.transfer.item.LockableItemStackHandler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
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
    public static final int Capacity = 576;

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private final NotifiableItemStackHandler inventory;
    private final LockableItemStackHandler itemStackHandler;

    public MultiblockCrateMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.inventory = new NotifiableItemStackHandler(this, Capacity, IO.BOTH);
        itemStackHandler = new LockableItemStackHandler(inventory).setLock(true);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return isFormed && IUIMachine.super.shouldOpenUI(player, hand, hit);
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return itemStackHandler;
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        itemStackHandler.setLock(false);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        itemStackHandler.setLock(true);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        int xOffset = 162;
        int yOverflow = 9;
        // int yOffset = (Capacity - 3 * yOverflow) / yOverflow * 18;
        var modularUI = new ModularUI(xOffset + 19, 244, this, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7, 162, true));

        var innerContainer = new DraggableScrollableWidgetGroup(4, 4, xOffset + 6, 130)
                .setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON).setYScrollBarWidth(4);

        modularUI.widget(new ButtonWidget(176 - 15, 3, 14, 14,
                new ResourceTexture(GTOCore.id("textures/gui/sort.png")),
                (press) -> ClientMessage.send("sortInventory", GTOUtils.noopConsumer())));
        int x = 0;
        int y = 0;
        for (int slot = 0; slot < Capacity; slot++) {
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
