package com.gtocore.common.data;

import com.gtocore.common.cover.*;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.client.renderer.cover.*;
import com.gregtechceu.gtceu.common.cover.ConveyorCover;
import com.gregtechceu.gtceu.common.cover.FluidRegulatorCover;
import com.gregtechceu.gtceu.common.cover.PumpCover;
import com.gregtechceu.gtceu.common.cover.RobotArmCover;
import com.gregtechceu.gtceu.common.data.GTCovers;

import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.common.cover.WirelessEnergyReceiveCover;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

public final class GTOCovers {

    static final CoverDefinition WIRELESS_CHARGER_COVER = GTCovers.register(GTOCore.id("wireless_charger_cover"), WirelessChargerCover::new, () -> () -> new SimpleCoverRenderer(GTOCore.id("item/wireless_charger_cover")));

    private static final Supplier<ICoverRenderer> POWER_AMPLIFIER = () -> new SimpleCoverRenderer(GTOCore.id("gui/overclock_config"));

    static final CoverDefinition[] POWER_AMPLIFIERS = registerTiered("power_amplifier", PowerAmplifierCover::new, () -> POWER_AMPLIFIER, GTValues.tiersBetween(GTValues.LV, GTValues.LuV));

    static final CoverDefinition AIR_VENT = GTCovers.register(GTOCore.id("air_vent"), AirVentCover::new, () -> () -> new SimpleCoverRenderer(GTOCore.id("block/machines/vacuum_pump/overlay_top")));

    static final CoverDefinition STEAM_PUMP = GTCovers.register(GTOCore.id("steam_pump"), SteamPumpCover::new, () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    static final CoverDefinition ELECTRIC_PUMP_ULV = GTCovers.register(
            GTOCore.id("pump.ulv"),
            (def, coverable, side) -> new PumpCover(def, coverable, side, GTValues.ULV),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    static final CoverDefinition FLUID_REGULATOR_ULV = GTCovers.register(
            GTOCore.id("fluid_regulator.ulv"),
            (def, coverable, side) -> new FluidRegulatorCover(def, coverable, side, GTValues.ULV),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    static final CoverDefinition CONVEYOR_MODULE_ULV = GTCovers.register(
            GTOCore.id("conveyor.ulv"),
            (def, coverable, side) -> new ConveyorCover(def, coverable, side, GTValues.ULV),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/conveyor"),
                    null,
                    GTCEu.id("block/cover/conveyor_emissive"),
                    GTCEu.id("block/cover/conveyor_inverted_emissive")));

    static final CoverDefinition ROBOT_ARM_ULV = GTCovers.register(
            GTOCore.id("robot_arm.ulv"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.ULV),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));

    static final CoverDefinition ELECTRIC_PUMP_MAX = GTCovers.register(
            GTOCore.id("pump.max"),
            (def, coverable, side) -> new PumpCover(def, coverable, side, GTValues.MAX),
            () -> () -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER);

    static final CoverDefinition CONVEYOR_MODULE_MAX = GTCovers.register(
            GTOCore.id("conveyor.max"),
            (def, coverable, side) -> new ConveyorCover(def, coverable, side, GTValues.MAX),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/conveyor"),
                    null,
                    GTCEu.id("block/cover/conveyor_emissive"),
                    GTCEu.id("block/cover/conveyor_inverted_emissive")));

    static final CoverDefinition ROBOT_ARM_MAX = GTCovers.register(
            GTOCore.id("robot_arm.max"),
            (def, coverable, side) -> new RobotArmCover(def, coverable, side, GTValues.MAX),
            () -> () -> new IOCoverRenderer(
                    GTCEu.id("block/cover/arm"),
                    null,
                    GTCEu.id("block/cover/arm_emissive"),
                    GTCEu.id("block/cover/arm_inverted_emissive")));

    public static final CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE = registerTieredWirelessCover(
            "wireless_energy_receive", 1);

    public static final CoverDefinition MAX_WIRELESS_ENERGY_RECEIVE_4A = registerTieredWirelessCover(
            "4a_wireless_energy_receive", 4);

    private static CoverDefinition registerTieredWirelessCover(String id, int amperage) {
        String name = id + "." + GTValues.VN[GTValues.MAX].toLowerCase(Locale.ROOT);
        return GTCovers.register(GTOCore.id(name), (holder, coverable, side) -> new WirelessEnergyReceiveCover(holder, coverable, side, GTValues.MAX, amperage),
                () -> () -> new SimpleCoverRenderer(GTMThings.id("block/cover/overlay_" + (amperage == 1 ? "" : "4a_") + "wireless_energy_receive")));
    }

    private static CoverDefinition[] registerTiered(String id,
                                                    CoverDefinition.TieredCoverBehaviourProvider behaviorCreator, Supplier<Supplier<ICoverRenderer>> coverRenderer,
                                                    int... tiers) {
        return Arrays.stream(tiers).mapToObj(tier -> {
            var name = id + "." + GTValues.VN[tier].toLowerCase(Locale.ROOT);
            return GTCovers.register(GTOCore.id(name), (def, coverable, side) -> behaviorCreator.create(def, coverable, side, tier), coverRenderer);
        }).toArray(CoverDefinition[]::new);
    }

    public static void init() {}
}
