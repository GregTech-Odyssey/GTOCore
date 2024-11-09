package com.gto.gtocore.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class GTOBedrockFluids {

    public static final Map<ResourceLocation, BedrockFluidDefinition> toReRegister = new HashMap<>();

    public static BedrockFluidDefinition VOID_HEAVY_OIL = create(GTCEu.id("void_heavy_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.OilHeavy::getFluid)
                    .weight(15)
                    .yield(100, 200)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition VOID_LIGHT_OIL = create(GTCEu.id("void_light_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.OilLight::getFluid)
                    .weight(25)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition VOID_NATURAL_GAS = create(GTCEu.id("void_natural_gas_deposit"),
            builder -> builder
                    .fluid(GTMaterials.NaturalGas::getFluid)
                    .weight(15)
                    .yield(100, 175)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition VOID_OIL = create(GTCEu.id("void_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Oil::getFluid)
                    .weight(20)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition VOID_RAW_OIL = create(GTCEu.id("void_raw_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.RawOil::getFluid)
                    .weight(20)
                    .yield(200, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition VOID_ALT_WATER = create(GTCEu.id("void_salt_water_deposit"),
            builder -> builder
                    .fluid(GTMaterials.SaltWater::getFluid)
                    .weight(10)
                    .yield(50, 100)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(15)
                    .dimensions(getDim("gtocore", "void")));

    public static BedrockFluidDefinition FLAT_HEAVY_OIL = create(GTCEu.id("flat_heavy_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.OilHeavy::getFluid)
                    .weight(15)
                    .yield(100, 200)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition FLAT_LIGHT_OIL = create(GTCEu.id("flat_light_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.OilLight::getFluid)
                    .weight(25)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition FLAT_NATURAL_GAS = create(GTCEu.id("flat_natural_gas_deposit"),
            builder -> builder
                    .fluid(GTMaterials.NaturalGas::getFluid)
                    .weight(15)
                    .yield(100, 175)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition FLAT_OIL = create(GTCEu.id("flat_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Oil::getFluid)
                    .weight(20)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition FLAT_RAW_OIL = create(GTCEu.id("flat_raw_oil_deposit"),
            builder -> builder
                    .fluid(GTMaterials.RawOil::getFluid)
                    .weight(20)
                    .yield(200, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition FLAT_ALT_WATER = create(GTCEu.id("flat_salt_water_deposit"),
            builder -> builder
                    .fluid(GTMaterials.SaltWater::getFluid)
                    .weight(10)
                    .yield(50, 100)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(15)
                    .dimensions(getDim("gtocore", "flat")));

    public static BedrockFluidDefinition HELIUM_3 = create(GTCEu.id("helium3_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Helium3::getFluid)
                    .weight(10)
                    .yield(50, 180)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("ad_astra", "moon")));

    public static BedrockFluidDefinition HELIUM = create(GTCEu.id("helium_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Helium::getFluid)
                    .weight(20)
                    .yield(50, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("ad_astra", "moon")));

    public static BedrockFluidDefinition SULFURIC_ACID = create(GTCEu.id("sulfuric_acid_deposit"),
            builder -> builder
                    .fluid(GTMaterials.SulfuricAcid::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("ad_astra", "venus")));

    public static BedrockFluidDefinition DEUTERIUM = create(GTCEu.id("deuterium_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Deuterium::getFluid)
                    .weight(15)
                    .yield(80, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("ad_astra", "mercury")));

    public static BedrockFluidDefinition RADON = create(GTCEu.id("radon_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Radon::getFluid)
                    .weight(20)
                    .yield(50, 80)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("ad_astra", "mars")));

    public static BedrockFluidDefinition CERES_RADON = create(GTCEu.id("ceres_radon_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Radon::getFluid)
                    .weight(15)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "ceres")));

    public static BedrockFluidDefinition METHANE = create(GTCEu.id("methane_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Methane::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "titan")));

    public static BedrockFluidDefinition BENZENE = create(GTCEu.id("benzene_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Benzene::getFluid)
                    .weight(15)
                    .yield(60, 160)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "titan")));

    public static BedrockFluidDefinition CHARCOAL_BYPRODUCTS = create(GTCEu.id("charcoal_byproducts"),
            builder -> builder
                    .fluid(GTMaterials.CharcoalByproducts::getFluid)
                    .weight(10)
                    .yield(80, 260)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "titan")));

    public static BedrockFluidDefinition COAL_GAS = create(GTCEu.id("coal_gas_deposit"),
            builder -> builder
                    .fluid(GTMaterials.CoalGas::getFluid)
                    .weight(20)
                    .yield(100, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "io")));

    public static BedrockFluidDefinition NITRIC_ACID = create(GTCEu.id("nitric_acid_deposit"),
            builder -> builder
                    .fluid(GTMaterials.NitricAcid::getFluid)
                    .weight(20)
                    .yield(80, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "pluto")));

    public static BedrockFluidDefinition HYDROCHLORIC_ACID = create(GTCEu.id("hydrochloric_acid_deposit"),
            builder -> builder
                    .fluid(GTMaterials.HydrochloricAcid::getFluid)
                    .weight(20)
                    .yield(100, 350)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "ganymede")));

    public static BedrockFluidDefinition CERES_XENON = create(GTCEu.id("ceres_xenon_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Xenon::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "ceres")));

    public static BedrockFluidDefinition CERES_KRYPTON = create(GTCEu.id("ceres_krypton_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Krypton::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "ceres")));

    public static BedrockFluidDefinition CERES_NEON = create(GTCEu.id("ceres_neon_deposit"),
            builder -> builder
                    .fluid(GTMaterials.Neon::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "ceres")));

    public static BedrockFluidDefinition FLUORINE = create(GTCEu.id("fluorine"),
            builder -> builder
                    .fluid(GTMaterials.Fluorine::getFluid)
                    .weight(10)
                    .yield(180, 320)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "enceladus")));

    public static BedrockFluidDefinition CHLORINE = create(GTCEu.id("chlorine"),
            builder -> builder
                    .fluid(GTMaterials.Chlorine::getFluid)
                    .weight(20)
                    .yield(180, 420)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "enceladus")));

    public static BedrockFluidDefinition UNKNOWWATER = create(GTCEu.id("unknowwater"),
            builder -> builder
                    .fluid(GTOMaterials.UnknowWater::getFluid)
                    .weight(20)
                    .yield(40, 60)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDim("gtocore", "barnarda")));

    public static void init() {
        toReRegister.forEach(GTRegistries.BEDROCK_FLUID_DEFINITIONS::registerOrOverride);
    }

    public static BedrockFluidDefinition create(ResourceLocation id,
                                                Consumer<BedrockFluidDefinition.Builder> consumer) {
        BedrockFluidDefinition.Builder builder = BedrockFluidDefinition.builder(id);
        consumer.accept(builder);

        BedrockFluidDefinition definition = builder.build();
        toReRegister.put(id, definition);
        return definition;
    }

    public static Set<ResourceKey<Level>> getDim(String namespace, String path) {
        return Set.of(ResourceKey.create(Registries.DIMENSION,
                new ResourceLocation(namespace, path)));
    }
}
