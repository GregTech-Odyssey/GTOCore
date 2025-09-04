package com.gtocore.mixin.gtm.machine;

import com.gtocore.common.network.ClientMessage;

import com.gtolib.GTOCore;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.storage.CrateMachine;

import net.minecraft.world.entity.player.Player;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrateMachine.class)
public abstract class CrateMachineMixin extends MetaMachine implements IUIMachine, IMachineLife, IDropSaveMachine, IInteractedMachine {

    @Shadow(remap = false)
    @Final
    public NotifiableItemStackHandler inventory;

    @Shadow(remap = false)
    @Final
    private int inventorySize;

    protected CrateMachineMixin(MetaMachineBlockEntity holder) {
        super(holder);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ModularUI createUI(Player entityPlayer) {
        int xOffset = inventorySize >= 90 ? 162 : 0;
        int yOverflow = xOffset > 0 ? 18 : 9;
        int yOffset = inventorySize > 3 * yOverflow ? (inventorySize - 3 * yOverflow - (inventorySize - 3 * yOverflow) % yOverflow) / yOverflow * 18 : 0;
        var modularUI = new ModularUI(176 + xOffset, 166 + yOffset, this, entityPlayer).background(GuiTextures.BACKGROUND).widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId())).widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7 + xOffset / 2, 82 + yOffset, true));
        int x = 0;
        int y = 0;
        for (int slot = 0; slot < inventorySize; slot++) {
            modularUI.widget(new SlotWidget(inventory, slot, x * 18 + 7, y * 18 + 17).setBackgroundTexture(GuiTextures.SLOT));
            x++;
            if (x == yOverflow) {
                x = 0;
                y++;
            }
        }
        modularUI.widget(new ButtonWidget(176 + xOffset - 17, 3, 14, 14,
                new ResourceTexture(GTOCore.id("textures/gui/sort.png")),
                (press) -> ClientMessage.send("sortInventory", GTOUtils.noopConsumer())));
        return modularUI;
    }
}
