package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

@Scanned
public class MESimplePatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    @DynamicInitialValue(
                         key = "gtceu.machine.part.ae.simple_pattern_buffer.slot_count",
                         en = "Slot Count",
                         enComment = "The number of AE patterns that can be stored inside the machine",
                         cn = "插槽数量",
                         cnComment = "内部可以放入的AE样板个数",
                         simpleValue = "9",
                         normalValue = "0",
                         expertValue = "0")
    private static int slotCount = 9;

    public MESimplePatternBufferPartMachine(MetaMachineBlockEntity holder) {
        super(holder, slotCount);
    }

    @Override
    public boolean allowThisMachineConnectToWirelessGrid() {
        return false;
    }
}
