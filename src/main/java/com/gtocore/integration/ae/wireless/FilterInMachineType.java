package com.gtocore.integration.ae.wireless;

import com.gtocore.api.lang.ComponentSupplier;
import com.gtocore.common.data.translation.EnumTranslation;

public enum FilterInMachineType {

    BOTH(EnumTranslation.INSTANCE.getBoth()),
    PATTERN_HATCH(EnumTranslation.INSTANCE.getPatternHatch()),
    WIRELESS_CONNECT_MACHINE(EnumTranslation.INSTANCE.getWirelessConnectionMachine()),
    OTHER(EnumTranslation.INSTANCE.getOther()),;

    public final ComponentSupplier label;

    FilterInMachineType(ComponentSupplier label) {
        this.label = label;
    }
}
