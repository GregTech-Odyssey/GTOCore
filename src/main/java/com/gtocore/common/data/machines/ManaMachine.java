package com.gtocore.common.data.machines;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.client.renderer.machine.CelestialCondenserRenderer;
import com.gtocore.client.renderer.machine.ManaHeaterRenderer;
import com.gtocore.client.renderer.machine.OverlayManaTieredMachineRenderer;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.common.machine.generator.MagicEnergyMachine;
import com.gtocore.common.machine.mana.AlchemyCauldron;
import com.gtocore.common.machine.mana.AreaDestructionToolsMachine;
import com.gtocore.common.machine.mana.CelestialCondenser;
import com.gtocore.common.machine.mana.ManaHeaterMachine;
import com.gtocore.common.machine.mana.part.ManaAmplifierPartMachine;
import com.gtocore.common.machine.mana.part.ManaExtractHatchPartMachine;
import com.gtocore.common.machine.mana.part.ManaHatchPartMachine;
import com.gtocore.common.machine.mana.part.WirelessManaHatchPartMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.electric.HullMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtocore.utils.register.MachineRegisterUtils.*;
import static com.gtolib.api.GTOValues.MANACN;
import static com.gtolib.api.GTOValues.MANAN;

public final class ManaMachine {

    public static void init() {
        ManaMultiBlock.init();
    }

    public static final MachineDefinition[] MANA_HULL = registerTieredManaMachines("mana_machine_hull", tier -> "%s%s".formatted(MANACN[tier], "魔法机器外壳"),
            HullMachine::new,
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Machine Hull")
                    .allRotation()
                    .abilities(GTOPartAbility.PASSTHROUGH_HATCH_MANA)
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTCEu.id("block/machine/part/hull")))
                    .tooltips(Component.translatable("gtceu.machine.hull.tooltip"))
                    .register(),
            GTValues.tiersBetween(0, 13));

    public static final MachineDefinition[] MANA_ASSEMBLER = registerSimpleManaMachines("mana_assembler", "魔力组装机", GTRecipeTypes.ASSEMBLER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/assembler"), MANA_TIERS);

    public static final MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = registerTieredManaMachines(
            "primitive_magic_energy", tier -> "%s原始魔法能源吸收器 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            MagicEnergyMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Primitive Magic Energy %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier,
                            GTOCore.id("block/generators/primitive_magic_energy")))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.1"))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.amperage_out", 16 >> GTOCore.difficulty))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(V[tier] << 8)))
                    .register(),
            LV, MV);

    public static final MachineDefinition[] MANA_EXTRACT_HATCH = registerTieredManaMachines("mana_extract_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力抽取仓"),
            ManaExtractHatchPartMachine::new,
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Extract Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.EXTRACT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal((GTOValues.MANA[tier] << 2) + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition[] MANA_INPUT_HATCH = registerTieredManaMachines("mana_input_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输入仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.IN, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Input Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal(4 * GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredManaMachines("mana_output_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输出仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.OUT, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Mana Output Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_output", Component.literal(4 * GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition[] WIRELESS_MANA_INPUT_HATCH = registerTieredManaMachines("wireless_mana_input_hatch", tier -> "%s%s".formatted(MANACN[tier], "无线魔力输入仓"),
            (holder, tier) -> new WirelessManaHatchPartMachine(holder, tier, IO.IN, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Wireless Mana Input Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal(4 * GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTOCore.id("block/machine/part/" + "wireless_mana_hatch")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition[] WIRELESS_MANA_OUTPUT_HATCH = registerTieredManaMachines("wireless_mana_output_hatch", tier -> "%s%s".formatted(MANACN[tier], "无线魔力输出仓"),
            (holder, tier) -> new WirelessManaHatchPartMachine(holder, tier, IO.OUT, 1),
            (tier, builder) -> builder
                    .langValue(MANAN[tier] + " Wireless Mana Output Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .tooltips(Component.translatable("gtocore.machine.mana_output", Component.literal(4 * GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .renderer(() -> new OverlayManaTieredMachineRenderer(tier, GTOCore.id("block/machine/part/" + "wireless_mana_hatch")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition MANA_AMPLIFIER_HATCH = manaMachine("mana_amplifier_hatch", "魔力增幅仓", ManaAmplifierPartMachine::new)
            .tier(MV)
            .allRotation()
            .tooltips(GTOMachineTooltips.INSTANCE.getManaAmplifierHatchTooltips().getSupplier())
            .workableManaTieredHullRenderer(6, GTOCore.id("block/multiblock/mana"))
            .register();

    public static final MachineDefinition ALCHEMY_CAULDRON = manaMachine("alchemy_cauldron", "炼金锅", AlchemyCauldron::new)
            .tier(LV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("alchemy_cauldron"), GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES))
            .recipeType(GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES)
            .tooltips(GTOMachineTooltips.INSTANCE.getAlchemicalDeviceTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getAlchemyCauldronTooltips().getSupplier())
            .nonYAxisRotation()
            .modelRenderer(() -> GTOCore.id("block/machine/alchemy_cauldron"))
            .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
            .tooltips(workableNoEnergy(GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES, 1600))
            .register();

    public static final MachineDefinition CELESTIAL_CONDENSER = manaMachine("celestial_condenser", "苍穹凝聚器", CelestialCondenser::new)
            .tier(HV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("celestial_condenser"), GTORecipeTypes.CELESTIAL_CONDENSER_RECIPES))
            .recipeType(GTORecipeTypes.CELESTIAL_CONDENSER_RECIPES)
            .nonYAxisRotation()
            .renderer(CelestialCondenserRenderer::new)
            .hasTESR(true)
            .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
            .register();

    public static final MachineDefinition MANA_HEATER = manaMachine("mana_heater", "魔力加热器", ManaHeaterMachine::new)
            .tier(MV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("mana_heater"), GTORecipeTypes.MANA_HEATER_RECIPES))
            .recipeType(GTORecipeTypes.MANA_HEATER_RECIPES)
            .noRecipeModifier()
            .nonYAxisRotation()
            .tooltips(GTOMachineTooltips.INSTANCE.getManaHeaterTooltips().getSupplier())
            .renderer(() -> new ManaHeaterRenderer(MV))
            .register();

    public static final MachineDefinition AREA_DESTRUCTION_TOOLS = manaMachine("area_destruction_tools", "区域破坏器", AreaDestructionToolsMachine::new)
            .tier(LuV)
            .tooltips(GTOMachineTooltips.INSTANCE.getAreaDestructionToolsTooltips().getSupplier())
            .nonYAxisRotation()
            .workableManaTieredHullRenderer(6, GTOCore.id("block/multiblock/area_destruction_tools"))
            .register();
}
