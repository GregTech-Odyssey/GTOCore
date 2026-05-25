package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine;

import com.gtolib.api.machine.trait.ProxyRecipeHandler;

import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class ProxySlotRecipeHandler {

    public static final ProxySlotRecipeHandler DEFAULT = new ProxySlotRecipeHandler(null, null);
    private final List<RecipeHandlerUnit> proxySlotHandlers;

    public ProxySlotRecipeHandler(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine patternBuffer) {
        int slots = patternBuffer == null ? 0 : patternBuffer.getMaxPatternCount();
        proxySlotHandlers = new ArrayList<>(slots);
        for (int i = 0; i < slots; ++i) {
            proxySlotHandlers.add(ProxyRHL.of(machine, patternBuffer.getInternalInventory()[i]));
        }
    }

    public void updateProxy(MEPatternBufferPartMachine patternBuffer) {
        var slotHandlers = patternBuffer.internalRecipeHandler.getSlotHandlers();
        for (int i = 0; i < proxySlotHandlers.size(); ++i) {
            ProxyRHL proxyRHL = (ProxyRHL) proxySlotHandlers.get(i);
            proxyRHL.setBuffer(patternBuffer, (InternalSlotRecipeHandler.SlotRHL) slotHandlers.get(i));
        }
    }

    private static final class ProxyRHL extends InternalSlotRecipeHandler.AbstractRHL<MEPatternBufferPartMachine.InternalSlot> {

        private static ProxyRHL of(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine.InternalSlot slot) {
            return new ProxyRHL(machine, slot, new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine));
        }

        private final ProxyRecipeHandler slotHandler;
        private final ProxyRecipeHandler circuit;
        private final ProxyRecipeHandler slotCircuit;
        private final ProxyRecipeHandler sharedItem;
        private final ProxyRecipeHandler slotSharedItem;
        private final ProxyRecipeHandler sharedFluid;
        private final ProxyRecipeHandler slotSharedFluid;

        private ProxyRHL(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine.InternalSlot slot, ProxyRecipeHandler slotHandler, ProxyRecipeHandler circuit, ProxyRecipeHandler slotCircuit, ProxyRecipeHandler sharedItem, ProxyRecipeHandler slotSharedItem, ProxyRecipeHandler sharedFluid, ProxyRecipeHandler slotSharedFluid) {
            super(slot, machine, slotHandler, circuit, slotCircuit, sharedItem, slotSharedItem, sharedFluid, slotSharedFluid);
            this.slotHandler = slotHandler;
            this.circuit = circuit;
            this.slotCircuit = slotCircuit;
            this.sharedItem = sharedItem;
            this.slotSharedItem = slotSharedItem;
            this.sharedFluid = sharedFluid;
            this.slotSharedFluid = slotSharedFluid;
        }

        private void setBuffer(MEPatternBufferPartMachine buffer, InternalSlotRecipeHandler.SlotRHL slotRHL) {
            circuit.setProxy(buffer.circuitInventorySimulated);
            sharedItem.setProxy(buffer.shareInventory);
            sharedFluid.setProxy(buffer.shareTank);
            slotHandler.setProxy(slotRHL.recipeHandler);
            slotCircuit.setProxy(slotRHL.slot.circuitInventory);
            slotSharedItem.setProxy(slotRHL.slot.shareInventory);
            slotSharedFluid.setProxy(slotRHL.slot.shareTank);
        }

        private void clearBuffer() {
            circuit.setProxy(null);
            sharedItem.setProxy(null);
            sharedFluid.setProxy(null);
            slotHandler.setProxy(null);
            slotCircuit.setProxy(null);
            slotSharedItem.setProxy(null);
            slotSharedFluid.setProxy(null);
        }
    }
}
