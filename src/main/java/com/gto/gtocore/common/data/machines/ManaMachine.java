package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.machine.generator.MagicEnergyMachine;
import com.gto.gtocore.common.machine.mana.AlchemyCauldron;
import com.gto.gtocore.common.machine.mana.ManaHeaterMachine;
import com.gto.gtocore.common.machine.mana.part.ManaAmplifierPartMachine;
import com.gto.gtocore.common.machine.mana.part.ManaExtractHatchPartMachine;
import com.gto.gtocore.common.machine.mana.part.ManaHatchPartMachine;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gto.gtocore.api.GTOValues.MANACN;
import static com.gto.gtocore.api.GTOValues.MANAN;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.*;

public interface ManaMachine {

    static void init() {
        ManaMultiBlock.init();
    }

    MachineDefinition[] MANA_ASSEMBLER = registerSimpleManaMachines("mana_assembler", "魔力组装机", GTRecipeTypes.ASSEMBLER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/assembler"), MANA_TIERS);

    MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = registerTieredManaMachines(
            "primitive_magic_energy", tier -> "%s原始魔法能源吸收器 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            MagicEnergyMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Primitive Magic Energy %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier,
                            GTOCore.id("block/generators/primitive_magic_energy")))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.1"))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.amperage_out", 16 >> GTOConfig.getDifficulty()))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(V[tier] << 8)))
                    .register(),
            LV, MV);

    MachineDefinition[] MANA_EXTRACT_HATCH = registerTieredManaMachines("mana_extract_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力抽取仓"),
            ManaExtractHatchPartMachine::new,
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Extract Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.EXTRACT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal((GTOValues.MANA[tier] << 2) + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition[] MANA_INPUT_HATCH = registerTieredManaMachines("mana_input_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输入仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.IN, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Input Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal(GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredManaMachines("mana_output_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输出仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.OUT, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Output Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_output", Component.literal(GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition MANA_AMPLIFIER_HATCH = manaMachine("mana_amplifier_hatch", "魔力增幅仓", ManaAmplifierPartMachine::new)
            .tier(MV)
            .allRotation()
            .tooltipsText("If mana equivalent to the machine's maximum power is input prior to operation, the current recipe will switch to perfect overclocking.", "如果运行前输入了等同机器最大功率的魔力，则将本次配方改为无损超频")
            .tooltipsText("Otherwise, the machine will not execute the recipe.", "否则，机器不执行配方")
            .workableTieredHullRenderer(GTOCore.id("block/multiblock/mana"))
            .register();

    MachineDefinition ALCHEMY_CAULDRON = manaMachine("alchemy_cauldron", "炼金锅", AlchemyCauldron::new)
            .tier(LV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("alchemy_cauldron"), GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES))
            .recipeType(GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES)
            .alwaysTryModifyRecipe(true)
            .tooltipsText("§7Do not use it for cooking food", "§7不要用它来做饭")
            .nonYAxisRotation()
            .modelRenderer(() -> GTOCore.id("block/machine/alchemy_cauldron"))
            .tooltips(workableNoEnergy(GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES, 1600))
            .register();

    MachineDefinition MANA_HEATER = manaMachine("mana_heater", "魔力加热器", ManaHeaterMachine::new)
            .tier(MV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("mana_heater"), GTORecipeTypes.MANA_HEATER_RECIPES))
            .recipeType(GTORecipeTypes.MANA_HEATER_RECIPES)
            .noRecipeModifier()
            .nonYAxisRotation()
            .tooltipsText("Input mana to heat, if fire element is input, the heating speed will be 5 times faster.", "输入魔力加热，如果输入火元素，则加热速度翻5倍")
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/coal"))
            .register();
}
