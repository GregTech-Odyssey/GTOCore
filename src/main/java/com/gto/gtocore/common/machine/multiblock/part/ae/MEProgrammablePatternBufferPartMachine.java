package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.machine.trait.NotifiableNotConsumableItemHandler;
import com.gto.gtocore.api.machine.trait.NotifiableProgrammableCircuitHandler;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.KeyCounter;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;

public final class MEProgrammablePatternBufferPartMachine extends MEPatternBufferPartMachine {

    public MEProgrammablePatternBufferPartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    @NotNull
    NotifiableNotConsumableItemHandler createCircuitInventory() {
        return new NotifiableProgrammableCircuitHandler(this);
    }

    @Override
    public boolean pushPattern(@NotNull IPatternDetails patternDetails, KeyCounter @NotNull [] inputHolder) {
        if (!isFormed() || !getMainNode().isActive()) return false;
        var slot = detailsSlotMap.get(patternDetails);
        if (slot != null) {
            for (var s : detailsSlotMap.values()) {
                if (s.equals(slot)) continue;
                if (!s.isEmpty()) return false;
            }
            slot.pushPattern(patternDetails, inputHolder, this::addItem);
            return true;
        }
        return false;
    }

    private boolean addItem(@NotNull ItemStack stack) {
        if (stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
            getCircuitInventory().setStackInSlot(0, VirtualItemProviderBehavior.getVirtualItem(stack));
            return true;
        }
        return false;
    }
}
