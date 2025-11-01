package com.gtocore.data;

import com.gtolib.api.annotation.Scanned;

@Scanned
public final class IdleReason extends com.gtolib.api.recipe.IdleReason {

    public static final IdleReason ORDERED = new IdleReason("gtocore.idle_reason.ordered", "未满足有序要求", "Ordered Not Satisfies");
    public static final IdleReason SET_CIRCUIT = new IdleReason("gtocore.idle_reason.set_circuit", "需要设置电路", "Need to set circuit");

    public static final IdleReason GRIND_BALL = new IdleReason("gtocore.idle_reason.grindball", "需要研磨球", "Need to grind ball");

    public static final IdleReason CHARGE = new IdleReason("gtocore.idle_reason.charge", "工具剩余电量不足以支持此次运行", "Tool charge is not enough to support this run");

    public static final IdleReason FELLING_TOOL = new IdleReason("gtocore.idle_reason.felling_tool", "需要伐木工具", "Need to felling tool");

    public static final IdleReason RADIATION = new IdleReason("gtocore.idle_reason.radiation", "未处在要求辐射范围内", "Not in required radiation range");

    public static final IdleReason NO_ORES = new IdleReason("gtocore.idle_reason.no_ores", "该维度中没有可用的矿石", "No ores available in this dimension");

    public static final IdleReason INCORRECT_DIRECTION_VOLTA = new IdleReason("gtocore.idle_reason.incorrect_direction_volta", "这个方向摆放的机器晒不到太阳", "The machine placed in this direction can't get sunlight");
    public static final IdleReason OBSTRUCTED_VOLTA = new IdleReason("gtocore.idle_reason.obstructed_volta", "太阳能板被遮挡了", "The solar panel is obstructed");

    public static final IdleReason MUFFLER_NOT_SUPPORTED = new IdleReason("gtocore.idle_reason.muffler_not_supported", "机器电压等级不支持高级消声仓", "The machine voltage tier does not support advanced muffler");

    public IdleReason(String key, String cn, String en) {
        super(key, en, cn);
    }
}
