package com.gtocore.common.pipe.heat;

public final class HeatPipeProperties {

    public static final HeatPipeProperties INSTANCE = new HeatPipeProperties();

    public int getLossPerBlock() {
        return 1;
    }
}
