package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.common.data.GTBedrockFluids;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.gto.gtocore.api.data.GTOWorldGenLayers.*;

@SuppressWarnings("unused")
public interface GTOBedrockFluids {

    Map<String, LangHandler.ENCN> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    Map<ResourceKey<Level>, List<FluidStack>> ALL_BEDROCK_FLUID = new Object2ObjectOpenHashMap<>();

    BedrockFluidDefinition VOID_HEAVY_OIL = create(GTCEu.id("void_heavy_oil_deposit"),
            "虚空重油矿藏",
            builder -> builder
                    .fluid(GTMaterials.OilHeavy::getFluid)
                    .weight(15)
                    .yield(100, 200)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition VOID_LIGHT_OIL = create(GTCEu.id("void_light_oil_deposit"),
            "虚空轻油矿藏",
            builder -> builder
                    .fluid(GTMaterials.OilLight::getFluid)
                    .weight(25)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition VOID_NATURAL_GAS = create(GTCEu.id("void_natural_gas_deposit"),
            "虚空天然气矿藏",
            builder -> builder
                    .fluid(GTMaterials.NaturalGas::getFluid)
                    .weight(15)
                    .yield(100, 175)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition VOID_OIL = create(GTCEu.id("void_oil_deposit"),
            "虚空石油矿藏",
            builder -> builder
                    .fluid(GTMaterials.Oil::getFluid)
                    .weight(20)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition VOID_RAW_OIL = create(GTCEu.id("void_raw_oil_deposit"),
            "虚空原油矿藏",
            builder -> builder
                    .fluid(GTMaterials.RawOil::getFluid)
                    .weight(20)
                    .yield(200, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition VOID_ALT_WATER = create(GTCEu.id("void_salt_water_deposit"),
            "虚空盐水矿藏",
            builder -> builder
                    .fluid(GTMaterials.SaltWater::getFluid)
                    .weight(10)
                    .yield(50, 100)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(15)
                    .dimensions(getDimensions(VOID)));

    BedrockFluidDefinition FLAT_HEAVY_OIL = create(GTCEu.id("flat_heavy_oil_deposit"),
            "超平坦重油矿藏",
            builder -> builder
                    .fluid(GTMaterials.OilHeavy::getFluid)
                    .weight(15)
                    .yield(100, 200)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition FLAT_LIGHT_OIL = create(GTCEu.id("flat_light_oil_deposit"),
            "超平坦轻油矿藏",
            builder -> builder
                    .fluid(GTMaterials.OilLight::getFluid)
                    .weight(25)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition FLAT_NATURAL_GAS = create(GTCEu.id("flat_natural_gas_deposit"),
            "超平坦天然气矿藏",
            builder -> builder
                    .fluid(GTMaterials.NaturalGas::getFluid)
                    .weight(15)
                    .yield(100, 175)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(20)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition FLAT_OIL = create(GTCEu.id("flat_oil_deposit"),
            "超平坦石油矿藏",
            builder -> builder
                    .fluid(GTMaterials.Oil::getFluid)
                    .weight(20)
                    .yield(175, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition FLAT_RAW_OIL = create(GTCEu.id("flat_raw_oil_deposit"),
            "超平坦原油矿藏",
            builder -> builder
                    .fluid(GTMaterials.RawOil::getFluid)
                    .weight(20)
                    .yield(200, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(25)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition FLAT_ALT_WATER = create(GTCEu.id("flat_salt_water_deposit"),
            "超平坦盐水矿藏",
            builder -> builder
                    .fluid(GTMaterials.SaltWater::getFluid)
                    .weight(10)
                    .yield(50, 100)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(15)
                    .dimensions(getDimensions(FLAT)));

    BedrockFluidDefinition HELIUM_3 = create(GTCEu.id("helium3_deposit"),
            "氦-3矿藏",
            builder -> builder
                    .fluid(GTMaterials.Helium3::getFluid)
                    .weight(10)
                    .yield(50, 180)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(MOON)));

    BedrockFluidDefinition HELIUM = create(GTCEu.id("helium_deposit"),
            "氦矿藏",
            builder -> builder
                    .fluid(GTMaterials.Helium::getFluid)
                    .weight(20)
                    .yield(50, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(MOON)));

    BedrockFluidDefinition SULFURIC_ACID = create(GTCEu.id("sulfuric_acid_deposit"),
            "硫酸矿藏",
            builder -> builder
                    .fluid(GTMaterials.SulfuricAcid::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(VENUS)));

    BedrockFluidDefinition DEUTERIUM = create(GTCEu.id("deuterium_deposit"),
            "氘矿藏",
            builder -> builder
                    .fluid(GTMaterials.Deuterium::getFluid)
                    .weight(15)
                    .yield(80, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(MERCURY)));

    BedrockFluidDefinition RADON = create(GTCEu.id("radon_deposit"),
            "氡矿藏",
            builder -> builder
                    .fluid(GTMaterials.Radon::getFluid)
                    .weight(20)
                    .yield(50, 80)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(MARS)));

    BedrockFluidDefinition CERES_RADON = create(GTCEu.id("ceres_radon_deposit"),
            "谷神星氡矿藏",
            builder -> builder
                    .fluid(GTMaterials.Radon::getFluid)
                    .weight(15)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(CERES)));

    BedrockFluidDefinition METHANE = create(GTCEu.id("methane_deposit"),
            "甲烷矿藏",
            builder -> builder
                    .fluid(GTMaterials.Methane::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(TITAN)));

    BedrockFluidDefinition BENZENE = create(GTCEu.id("benzene_deposit"),
            "苯矿藏",
            builder -> builder
                    .fluid(GTMaterials.Benzene::getFluid)
                    .weight(15)
                    .yield(60, 160)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(TITAN)));

    BedrockFluidDefinition CHARCOAL_BYPRODUCTS = create(GTCEu.id("charcoal_byproducts"),
            "木炭副产矿藏",
            builder -> builder
                    .fluid(GTMaterials.CharcoalByproducts::getFluid)
                    .weight(10)
                    .yield(80, 260)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(TITAN)));

    BedrockFluidDefinition COAL_GAS = create(GTCEu.id("coal_gas_deposit"),
            "煤气矿藏",
            builder -> builder
                    .fluid(GTMaterials.CoalGas::getFluid)
                    .weight(20)
                    .yield(100, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(IO)));

    BedrockFluidDefinition NITRIC_ACID = create(GTCEu.id("nitric_acid_deposit"),
            "硝酸矿藏",
            builder -> builder
                    .fluid(GTMaterials.NitricAcid::getFluid)
                    .weight(20)
                    .yield(80, 300)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(PLUTO)));

    BedrockFluidDefinition HYDROCHLORIC_ACID = create(GTCEu.id("hydrochloric_acid_deposit"),
            "盐酸矿藏",
            builder -> builder
                    .fluid(GTMaterials.HydrochloricAcid::getFluid)
                    .weight(20)
                    .yield(100, 350)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(GANYMEDE)));

    BedrockFluidDefinition CERES_XENON = create(GTCEu.id("ceres_xenon_deposit"),
            "氙矿藏",
            builder -> builder
                    .fluid(GTMaterials.Xenon::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(CERES)));

    BedrockFluidDefinition CERES_KRYPTON = create(GTCEu.id("ceres_krypton_deposit"),
            "氪矿藏",
            builder -> builder
                    .fluid(GTMaterials.Krypton::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(CERES)));

    BedrockFluidDefinition CERES_NEON = create(GTCEu.id("ceres_neon_deposit"),
            "氖矿藏",
            builder -> builder
                    .fluid(GTMaterials.Neon::getFluid)
                    .weight(20)
                    .yield(100, 250)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(CERES)));

    BedrockFluidDefinition FLUORINE = create(GTCEu.id("fluorine_deposit"),
            "氟矿藏",
            builder -> builder
                    .fluid(GTMaterials.Fluorine::getFluid)
                    .weight(10)
                    .yield(180, 320)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(ENCELADUS)));

    BedrockFluidDefinition CHLORINE = create(GTCEu.id("chlorine_deposit"),
            "氯矿藏",
            builder -> builder
                    .fluid(GTMaterials.Chlorine::getFluid)
                    .weight(20)
                    .yield(180, 420)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(ENCELADUS)));

    BedrockFluidDefinition UNKNOWWATER = create(GTCEu.id("unknowwater_deposit"),
            "不明液体矿藏",
            builder -> builder
                    .fluid(GTOMaterials.UnknowWater::getFluid)
                    .weight(20)
                    .yield(40, 60)
                    .depletionAmount(1)
                    .depletionChance(100)
                    .depletedYield(40)
                    .dimensions(getDimensions(BARNARDA_C)));

    static void init() {}

    private static BedrockFluidDefinition create(ResourceLocation id, String cn, Consumer<BedrockFluidDefinition.Builder> consumer) {
        if (LANG != null) {
            String name = id.getPath();
            LangHandler.ENCN lang = new LangHandler.ENCN(FormattingUtil.toEnglishName(name), cn);
            if (LANG.containsKey(name)) {
                GTOCore.LOGGER.error("Repetitive Key: {}", id);
            }
            if (LANG.containsValue(lang)) {
                GTOCore.LOGGER.error("Repetitive Value: {}", lang);
            }
            LANG.put(name, lang);
        }
        BedrockFluidDefinition definition = GTBedrockFluids.create(id, consumer);
        addVoid(definition);
        return definition;
    }

    static void addVoid(BedrockFluidDefinition definition) {
        ResourceKey<Level> dimension = definition.dimensionFilter.iterator().next();
        if (dimension != getDimension(VOID) || dimension != getDimension(FLAT) || dimension != getDimension(CREATE)) {
            List<FluidStack> fluidStacks = ALL_BEDROCK_FLUID.computeIfAbsent(dimension, k -> new ArrayList<>());
            fluidStacks.add(new FluidStack(definition.getStoredFluid().get(), definition.getMaximumYield() * definition.getWeight()));
            ALL_BEDROCK_FLUID.put(dimension, fluidStacks);
        }
    }
}
