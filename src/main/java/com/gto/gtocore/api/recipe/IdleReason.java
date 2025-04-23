package com.gto.gtocore.api.recipe;

import net.minecraft.network.chat.Component;

import lombok.Getter;

public enum IdleReason {

    INVALID_INPUT(null, "Invalid Input", "无效输入"),
    NO_MATCH(null, "No Recipe Found", "没有找到配方"),
    NO_CWU("gtceu.multiblock.computation.not_enough_computation", null, null),
    NO_EU("behavior.prospector.not_enough_energy", null, null),
    OUTPUT_FULL("gui.enderio.output_full", null, null),
    MAINTENANCE_BROKEN("gtceu.top.maintenance_broken", null, null),
    MUFFLER_OBSTRUCTED("gtceu.multiblock.universal.muffler_obstructed", null, null),
    INSUFFICIENT_TEMPERATURE(null, "Insufficient Temperature", "温度不足"),
    BLOCK_TIER_NOT_SATISFIES(null, "Block Tier Not Satisfies", "方块等级未达到要求"),
    VOLTAGE_TIER_NOT_SATISFIES(null, "Voltage Tier Not Satisfies", "电压等级未达到要求");

    private Component reason;
    @Getter
    private final String key;
    @Getter
    private final String en;
    @Getter
    private final String cn;

    IdleReason(String key, String en, String cn) {
        this.key = key == null ? "gtocore.idle_reason" + this.name().toLowerCase() : key;
        this.en = en;
        this.cn = cn;
    }

    public Component reason() {
        if (reason == null) reason = Component.translatable(key);
        return reason;
    }
}
