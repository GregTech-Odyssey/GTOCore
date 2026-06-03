package com.gtocore.common.data;

import com.gtocore.common.block.BlockMap;
import com.gtocore.common.machine.mana.CelestialHandler;

import com.gtolib.api.GTOValues;
import com.gtolib.api.recipe.TierDataKey;

import com.gregtechceu.gtceu.common.data.GTRecipeDataKeys;

import net.minecraft.nbt.CompoundTag;

import com.gto.datasynclib.DataSyncCodec;
import com.gto.datasynclib.datasream.DataComponentKey;

public final class GTORecipeDataKeys {

    public static final DataComponentKey<Boolean> IS_CUSTOM = register("isCustom", DataSyncCodec.BOOLEAN_CODEC);
    public static final DataComponentKey<Boolean> SPECIAL = register("special", DataSyncCodec.BOOLEAN_CODEC);
    public static final DataComponentKey<Integer> TIER = register("tier", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Long> EU = register("eu", DataSyncCodec.LONG_CODEC);
    public static final DataComponentKey<Integer> TEMPERATURE = register("temperature", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Long> CONVERTED_ENERGY = register("convertedEnergy", DataSyncCodec.LONG_CODEC);
    public static final DataComponentKey<Float> EFFICIENCY = register("efficiency", DataSyncCodec.FLOAT_CODEC);

    public static final DataComponentKey<Integer> RADIOACTIVITY = register("radioactivity", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> FILTER_CASING = register("filter_casing", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> EV_MIN = register("ev_min", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Integer> EV_MAX = register("ev_max", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Integer> EVT = register("evt", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> FR_HEAT = register("FRheat", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> MODULE = register("module", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> NANO_FORGE_TIER = register("nano_forge_tier", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> GRINDBALL = register("grindball", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Integer> SPOOL = register("spool", DataSyncCodec.INT_CODEC);

    public static final DataComponentKey<Float> NEUTRON_FLUX = register("neutron_flux", DataSyncCodec.FLOAT_CODEC);
    public static final DataComponentKey<Float> NEUTRON_FLUX_CHANGE = register("neutron_flux_change", DataSyncCodec.FLOAT_CODEC);
    public static final DataComponentKey<Float> HEAT = register("heat", DataSyncCodec.FLOAT_CODEC);

    public static final DataComponentKey<Integer> PARAM1 = register("param1", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Integer> PARAM2 = register("param2", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Integer> PARAM3 = register("param3", DataSyncCodec.INT_CODEC);
    public static final DataComponentKey<Integer>[] PARAM = new DataComponentKey[] { PARAM1, PARAM2, PARAM3 };

    public static final DataComponentKey<CompoundTag> RESONANCE = register("resonance", DataSyncCodec.COMPOUND_TAG_CODEC);

    public static final TierDataKey HERMETIC_CASING_TIER = registerTier(BlockMap.hermetic_casing);

    public static final TierDataKey STELLAR_CONTAINMENT_TIER = registerTier(GTOValues.STELLAR_CONTAINMENT_TIER);
    public static final TierDataKey POWER_MODULE_TIER = registerTier(GTOValues.POWER_MODULE_TIER);
    public static final TierDataKey COMPONENT_ASSEMBLY_CASING_TIER = registerTier(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER);
    public static final TierDataKey GLASS_TIER = registerTier(GTOValues.GLASS_TIER);
    public static final TierDataKey MACHINE_CASING_TIER = registerTier(GTOValues.MACHINE_CASING_TIER);
    public static final TierDataKey GRAVITON_FLOW_TIER = registerTier(GTOValues.GRAVITON_FLOW_TIER);
    public static final TierDataKey INTEGRAL_FRAMEWORK_TIER = registerTier(GTOValues.INTEGRAL_FRAMEWORK_TIER);
    public static final TierDataKey COMPUTER_CASING_TIER = registerTier(GTOValues.COMPUTER_CASING_TIER);
    public static final TierDataKey COMPUTER_HEAT_TIER = registerTier(GTOValues.COMPUTER_HEAT_TIER);

    public static TierDataKey registerTier(String name) {
        return (TierDataKey) GTRecipeDataKeys.REGISTRY.register(new TierDataKey(name));
    }

    public static <T> DataComponentKey<T> register(String name, DataSyncCodec<T> codec) {
        return GTRecipeDataKeys.REGISTRY.register(name, codec);
    }

    public static void init() {
        CelestialHandler.init();
    }
}
