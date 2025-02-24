package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.machine.mana.AlchemyPot;
import com.gto.gtocore.common.machine.mana.ManaHeaterMachine;
import com.gto.gtocore.common.machine.mana.part.ManaExtractHatchPartMachine;
import com.gto.gtocore.common.machine.mana.part.ManaHatchPartMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.GTValues.LV;
import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gto.gtocore.api.GTOValues.MANACN;
import static com.gto.gtocore.api.GTOValues.MANAN;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.*;

public interface ManaMachine {

    static void init() {
        ManaMultiBlock.init();
    }

    MachineDefinition[] MANA_ASSEMBLER = registerSimpleManaMachines("mana_assembler", "魔力组装机", GTRecipeTypes.ASSEMBLER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/assembler"), GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition[] MANA_EXTRACT_HATCH = registerTieredManaMachines("mana_extract_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力抽取仓"),
            ManaExtractHatchPartMachine::new,
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Extract Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.EXTRACT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", GTOValues.MANA[tier] << 2).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition[] MANA_INPUT_HATCH = registerTieredManaMachines("mana_input_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输入仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.IN, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Input Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", GTOValues.MANA[tier]).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredManaMachines("mana_output_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输出仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.OUT, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Output Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_output", GTOValues.MANA[tier]).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    MachineDefinition ALCHEMY_POT = manaMachine("alchemy_pot", "炼金锅", AlchemyPot::new)
            .tier(LV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("alchemy_pot"), GTORecipeTypes.ALCHEMY_POT_RECIPES))
            .recipeType(GTORecipeTypes.ALCHEMY_POT_RECIPES)
            .alwaysTryModifyRecipe(true)
            .rotationState(RotationState.NON_Y_AXIS)
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/lava"))
            .tooltips(workableNoEnergy(GTORecipeTypes.ALCHEMY_POT_RECIPES, 1600))
            .register();

    MachineDefinition MANA_HEATER = manaMachine("mana_heater", "魔力加热器", ManaHeaterMachine::new)
            .tier(MV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("mana_heater"), GTORecipeTypes.MANA_HEATER_RECIPES))
            .recipeType(GTORecipeTypes.MANA_HEATER_RECIPES)
            .noRecipeModifier()
            .rotationState(RotationState.NON_Y_AXIS)
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/coal"))
            .register();
}
