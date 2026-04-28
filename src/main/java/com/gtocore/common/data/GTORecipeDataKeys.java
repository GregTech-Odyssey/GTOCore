package com.gtocore.common.data;

import com.gtocore.common.block.BlockMap;
import com.gtocore.utils.DataCodecs;

import com.gtolib.api.GTOValues;
import com.gtolib.api.recipe.TierDataKey;

import com.gregtechceu.gtceu.common.data.GTRecipeDataKeys;

import net.minecraft.nbt.CompoundTag;

import com.gto.datasynclib.datasream.DataComponentKey;
import com.gto.datasynclib.datasream.codec.DataCodec;

public final class GTORecipeDataKeys {

    public static final DataComponentKey<Boolean> IS_CUSTOM = register("isCustom", DataCodec.BOOLEAN_CODEC);
    public static final DataComponentKey<Boolean> SPECIAL = register("special", DataCodec.BOOLEAN_CODEC);
    public static final DataComponentKey<Integer> TIER = register("tier", DataCodec.INT_CODEC);
    public static final DataComponentKey<Long> EU = register("eu", DataCodec.LONG_CODEC);
    public static final DataComponentKey<Integer> TEMPERATURE = register("temperature", DataCodec.INT_CODEC);

    public static final DataComponentKey<Long> CONVERTED_ENERGY = register("convertedEnergy", DataCodec.LONG_CODEC);
    public static final DataComponentKey<Float> EFFICIENCY = register("efficiency", DataCodec.FLOAT_CODEC);

    public static final DataComponentKey<Integer> RADIOACTIVITY = register("radioactivity", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> FILTER_CASING = register("filter_casing", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> EV_MIN = register("ev_min", DataCodec.INT_CODEC);
    public static final DataComponentKey<Integer> EV_MAX = register("ev_max", DataCodec.INT_CODEC);
    public static final DataComponentKey<Integer> EVT = register("evt", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> FR_HEAT = register("FRheat", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> MODULE = register("module", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> NANO_FORGE_TIER = register("nano_forge_tier", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> GRINDBALL = register("grindball", DataCodec.INT_CODEC);

    public static final DataComponentKey<Integer> SPOOL = register("spool", DataCodec.INT_CODEC);

    public static final DataComponentKey<Float> NEUTRON_FLUX = register("neutron_flux", DataCodec.FLOAT_CODEC);
    public static final DataComponentKey<Float> NEUTRON_FLUX_CHANGE = register("neutron_flux_change", DataCodec.FLOAT_CODEC);
    public static final DataComponentKey<Float> HEAT = register("heat", DataCodec.FLOAT_CODEC);

    public static final DataComponentKey<Integer> PARAM1 = register("param1", DataCodec.INT_CODEC);
    public static final DataComponentKey<Integer> PARAM2 = register("param2", DataCodec.INT_CODEC);
    public static final DataComponentKey<Integer> PARAM3 = register("param3", DataCodec.INT_CODEC);
    public static final DataComponentKey<Integer>[] PARAM = new DataComponentKey[] { PARAM1, PARAM2, PARAM3 };

    public static final DataComponentKey<CompoundTag> RESONANCE = register("resonance", DataCodecs.COMPOUND_TAG);

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

    public static <T> DataComponentKey<T> register(String name, DataCodec<T> codec) {
        return GTRecipeDataKeys.REGISTRY.register(name, codec);
    }

    public static void init() {}
}
