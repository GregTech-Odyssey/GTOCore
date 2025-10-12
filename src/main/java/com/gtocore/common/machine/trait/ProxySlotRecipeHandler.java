package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine;

import com.gtolib.api.machine.trait.ProxyFluidRecipeHandler;
import com.gtolib.api.machine.trait.ProxyItemRecipeHandler;

import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import java.util.ArrayList;
import java.util.List;

public final class ProxySlotRecipeHandler {

    public static final ProxySlotRecipeHandler DEFAULT = new ProxySlotRecipeHandler(null, null);
    private final List<RecipeHandlerList> proxySlotHandlers;

    public ProxySlotRecipeHandler(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine patternBuffer) {
        int slots = patternBuffer == null ? 0 : patternBuffer.getMaxPatternCount();
        proxySlotHandlers = new ArrayList<>(slots);
        for (int i = 0; i < slots; ++i) {
            proxySlotHandlers.add(new ProxyRHL(machine, patternBuffer.getInternalInventory()[i]));
        }
    }

    public void updateProxy(MEPatternBufferPartMachine patternBuffer) {
        var slotHandlers = patternBuffer.internalRecipeHandler.getSlotHandlers();
        for (int i = 0; i < proxySlotHandlers.size(); ++i) {
            ProxyRHL proxyRHL = (ProxyRHL) proxySlotHandlers.get(i);
            proxyRHL.setBuffer(patternBuffer, (InternalSlotRecipeHandler.SlotRHL) slotHandlers.get(i));
        }
    }

    private static final class ProxyRHL extends InternalSlotRecipeHandler.AbstractRHL {

        private final ProxyItemRecipeHandler circuit;
        private final ProxyItemRecipeHandler slotCircuit;
        private final ProxyItemRecipeHandler sharedItem;
        private final ProxyItemRecipeHandler slotItem;
        private final ProxyItemRecipeHandler slotSharedItem;
        private final ProxyFluidRecipeHandler sharedFluid;
        private final ProxyFluidRecipeHandler slotFluid;
        private final ProxyFluidRecipeHandler slotSharedFluid;

        private ProxyRHL(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine.InternalSlot slot) {
            super(slot, machine);
            circuit = new ProxyItemRecipeHandler(machine);
            slotCircuit = new ProxyItemRecipeHandler(machine);
            sharedItem = new ProxyItemRecipeHandler(machine);
            slotItem = new ProxyItemRecipeHandler(machine);
            slotSharedItem = new ProxyItemRecipeHandler(machine);
            sharedFluid = new ProxyFluidRecipeHandler(machine);
            slotFluid = new ProxyFluidRecipeHandler(machine);
            slotSharedFluid = new ProxyFluidRecipeHandler(machine);
            addHandlers(slotItem, slotFluid, slotCircuit, slotSharedItem, slotSharedFluid, circuit, sharedItem, sharedFluid);
        }

        private void setBuffer(MEPatternBufferPartMachine buffer, InternalSlotRecipeHandler.SlotRHL slotRHL) {
            circuit.setProxy(buffer.circuitInventorySimulated);
            sharedItem.setProxy(buffer.shareInventory);
            sharedFluid.setProxy(buffer.shareTank);
            slotItem.setProxy(slotRHL.itemRecipeHandler);
            slotFluid.setProxy(slotRHL.fluidRecipeHandler);
            slotCircuit.setProxy(slotRHL.slot.circuitInventory);
            slotSharedItem.setProxy(slotRHL.slot.shareInventory);
            slotSharedFluid.setProxy(slotRHL.slot.shareTank);
        }

        private void clearBuffer() {
            circuit.setProxy(null);
            sharedItem.setProxy(null);
            sharedFluid.setProxy(null);
            slotItem.setProxy(null);
            slotFluid.setProxy(null);
            slotCircuit.setProxy(null);
            slotSharedItem.setProxy(null);
            slotSharedFluid.setProxy(null);
        }
    }

    public List<RecipeHandlerList> getProxySlotHandlers() {
        return this.proxySlotHandlers;
    }
}
