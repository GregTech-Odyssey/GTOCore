package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.machine.mana.ManaHatchPartMachine;
import com.gto.gtocore.common.machine.mana.SimpleManaMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.gregtechceu.gtceu.api.GTValues.TIER_COUNT;
import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gto.gtocore.api.GTOValues.MANA;
import static com.gto.gtocore.api.GTOValues.MANACN;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;
import static com.gto.gtocore.common.data.GTOMachines.workableNoEnergy;

public final class ManaMachine {

    public static void init() {}

    private static MachineBuilder<MachineDefinition> manaMachine(String name, String cn, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        GTOBlocks.addLang(name, cn);
        return REGISTRATE.manaMachine(name, metaMachine);
    }

    public static final MachineDefinition[] MANA_INPUT_HATCH = registerTieredMachines("mana_input_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输入仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.IN, 2),
            (tier, builder) -> builder
                    .langValue(MANA[tier] + " Mana Input Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    public static final MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredMachines("mana_output_hatch", tier -> "%s%s".formatted(MANACN[tier], "魔力输出仓"),
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.OUT, 2),
            (tier, builder) -> builder
                    .langValue(MANA[tier] + " Mana Output Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_64a")))
                    .register(),
            GTMachineUtils.ELECTRIC_TIERS);

    private static MachineDefinition[] registerSimpleManaMachines(String name, String cn, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s".formatted(MANACN[tier], cn), (holder, tier) -> new SimpleManaMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
            builder.noRecipeModifier();
            return builder
                    .langValue("%s %s".formatted(MANA[tier], FormattingUtil.toEnglishName(name)))
                    .editableUI(SimpleManaMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(recipeType)
                    .workableTieredHullRenderer(workableModel)
                    .tooltips(workableNoEnergy(recipeType, tankScalingFunction.apply(tier)))
                    .register();
        },
                tiers);
    }

    private static MachineDefinition[] registerTieredMachines(String name, Function<Integer, String> cn, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, int[] tiers) {
        MachineDefinition[] definitions = new MachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            MachineBuilder<MachineDefinition> register = manaMachine(VN[tier].toLowerCase(Locale.ROOT) + "_" + name, cn.apply(tier), holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }
}
