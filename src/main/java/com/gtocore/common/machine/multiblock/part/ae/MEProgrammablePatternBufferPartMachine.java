package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.trait.NotifiableNotConsumableItemHandler;
import com.gtolib.api.machine.trait.NotifiableProgrammableCircuitHandler;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.KeyCounter;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class MEProgrammablePatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    public MEProgrammablePatternBufferPartMachine(IMachineBlockEntity holder) {
        super(holder, 27);
        Predicate<ItemStack> itemCallback = stack -> {
            if (stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
                circuitInventorySimulated.setStackInSlot(0, VirtualItemProviderBehavior.getVirtualItem(stack));
                return true;
            }
            return false;
        };
        for (InternalSlot internalSlot : getInternalInventory()) {
            internalSlot.inputSink.setItemCallback(itemCallback);
        }
    }

    @Override
    @NotNull
    NotifiableNotConsumableItemHandler createCircuitInventory() {
        return new NotifiableProgrammableCircuitHandler(this);
    }

    @Override
    public boolean pushPattern(@NotNull IPatternDetails patternDetails, KeyCounter @NotNull [] inputHolder) {
        if (!getMainNode().isActive()) return false;
        var slot = getDetailsSlotMap().get(patternDetails);
        if (slot != null) {
            for (var s : getDetailsSlotMap().values()) {
                if (s.equals(slot)) continue;
                if (!s.isEmpty()) return false;
            }
            slot.pushPattern(patternDetails, inputHolder);
            return true;
        }
        return false;
    }
}
