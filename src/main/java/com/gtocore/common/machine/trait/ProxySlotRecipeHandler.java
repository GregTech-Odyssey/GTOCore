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
            proxySlotHandlers.add(PatternBufferProxyRHL.of(machine, patternBuffer.getInternalInventory()[i]));
        }
    }

    public void updateProxy(MEPatternBufferPartMachine patternBuffer) {
        if (patternBuffer == null) {
            for (RecipeHandlerUnit proxySlotHandler : proxySlotHandlers) {
                PatternBufferProxyRHL proxyRHL = (PatternBufferProxyRHL) proxySlotHandler;
                proxyRHL.clearBuffer();
            }
        } else {
            var slotHandlers = patternBuffer.internalRecipeHandler.getSlotHandlers();
            for (int i = 0; i < proxySlotHandlers.size(); ++i) {
                PatternBufferProxyRHL proxyRHL = (PatternBufferProxyRHL) proxySlotHandlers.get(i);
                proxyRHL.setBuffer(patternBuffer, (InternalSlotRecipeHandler.PatternBufferRHL) slotHandlers.get(i));
            }
        }
    }

    private static final class PatternBufferProxyRHL extends InternalSlotRecipeHandler.PatternSlotRHL {

        private final ProxyRecipeHandler slotHandler;
        private final ProxyRecipeHandler circuit;
        private final ProxyRecipeHandler slotCircuit;
        private final ProxyRecipeHandler sharedItem;
        private final ProxyRecipeHandler slotSharedItem;
        private final ProxyRecipeHandler sharedFluid;
        private final ProxyRecipeHandler slotSharedFluid;

        private static PatternBufferProxyRHL of(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine.InternalSlot slot) {
            return new PatternBufferProxyRHL(machine, slot, new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine), new ProxyRecipeHandler(machine));
        }

        private PatternBufferProxyRHL(MEPatternBufferProxyPartMachine machine, MEPatternBufferPartMachine.InternalSlot slot, ProxyRecipeHandler slotHandler, ProxyRecipeHandler circuit, ProxyRecipeHandler slotCircuit, ProxyRecipeHandler sharedItem, ProxyRecipeHandler slotSharedItem, ProxyRecipeHandler sharedFluid, ProxyRecipeHandler slotSharedFluid) {
            super(slot, machine, slotHandler, circuit, slotCircuit, sharedItem, slotSharedItem, sharedFluid, slotSharedFluid);
            this.slotHandler = slotHandler.setCanHandleItem(true).setCanHandleFluid(true);
            this.circuit = circuit.setCanHandleItem(true);
            this.slotCircuit = slotCircuit.setCanHandleItem(true);
            this.sharedItem = sharedItem.setCanHandleItem(true);
            this.slotSharedItem = slotSharedItem.setCanHandleItem(true);
            this.sharedFluid = sharedFluid.setCanHandleFluid(true);
            this.slotSharedFluid = slotSharedFluid.setCanHandleFluid(true);
        }

        private void setBuffer(MEPatternBufferPartMachine buffer, InternalSlotRecipeHandler.PatternBufferRHL slotRHL) {
            slotHandler.setProxy(slotRHL.recipeHandler);
            circuit.setProxy(buffer.circuitInventorySimulated);
            slotCircuit.setProxy(slotRHL.slot.circuitInventory);
            sharedItem.setProxy(buffer.shareInventory);
            slotSharedItem.setProxy(slotRHL.slot.shareInventory);
            sharedFluid.setProxy(buffer.shareTank);
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
