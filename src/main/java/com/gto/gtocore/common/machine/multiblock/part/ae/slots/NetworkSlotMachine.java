package com.gto.gtocore.common.machine.multiblock.part.ae.slots;

import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;

/**
 * @author EasterFG on 2025/3/28
 */
public interface NetworkSlotMachine {

    boolean isOnline();

    IManagedGridNode getMainNode();

    IActionSource getActionSource();
}
