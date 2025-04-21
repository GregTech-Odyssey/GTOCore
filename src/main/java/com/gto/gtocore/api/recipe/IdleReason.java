package com.gto.gtocore.api.recipe;

import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import lombok.Getter;

import java.util.List;

public enum IdleReason {

    INVALID_INPUT(null, "Invalid Input", "无效输入"),
    NO_MATCH(null, "No recipe found", "没有找到配方"),
    NO_CWU("gtceu.multiblock.computation.not_enough_computation", null, null),
    NO_EU("behavior.prospector.not_enough_energy", null, null),
    OUTPUT_FULL("gui.enderio.output_full", null, null),
    MAINTENANCE_BROKEN("gtceu.top.maintenance_broken", null, null),
    MUFFLER_OBSTRUCTED("gtceu.multiblock.universal.muffler_obstructed", null, null),
    TEMPERATURE_NOT_ENOUGH(null, "Temperature not enough", "温度不足"),
    BLOCK_TIER_NOT_SATISFIES(null, "Block tier not satisfies", "方块等级未达到要求");

    private MutableComponent reason;
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

    public MutableComponent reason() {
        if (reason == null) reason = Component.translatable(key);
        return reason;
    }

    public static void addMachineText(List<Component> textList, IRecipeLogicMachine machine) {
        if (machine.getRecipeLogic().isIdle() && machine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic && enhancedRecipeLogic.gTOCore$getIdleReason() != null) {
            textList.add(enhancedRecipeLogic.gTOCore$getIdleReason().withStyle(ChatFormatting.GRAY));
        }
    }
}
