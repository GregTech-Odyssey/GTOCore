package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;
import com.gtolib.utils.StringIndex;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.WeightedMaterial;
import com.gregtechceu.gtceu.api.data.worldgen.generator.VeinGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.indicators.SurfaceIndicatorGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.DikeVeinGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.VeinedVeinGenerator;
import com.gregtechceu.gtceu.common.data.GTBedrockFluids;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTOres;
import com.gregtechceu.gtceu.utils.collection.O2IOpenCacheHashMap;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;
import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.*;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtolib.api.data.GTODimensions.*;
import static com.gtolib.api.data.GTOWorldGenLayers.ALL_LAYER;

@SuppressWarnings("unused")
public final class GTOOres {

    private static final Map<ResourceLocation, MaterialSelector> RANDOM_ORES = new O2OOpenCacheHashMap<>();
    public static final Map<ResourceLocation, Object2IntOpenHashMap<Material>> ALL_ORES = new O2OOpenCacheHashMap<>();

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        GTBedrockFluids.init();
        if (false) {
            Map<ResourceLocation, Set<String>> ORE_MAP = new O2OOpenCacheHashMap<>();
            ORE_MAP.put(THE_NETHER, Set.of("TagPrefix.oreNetherrack"));
            ORE_MAP.put(THE_END, Set.of("TagPrefix.oreEndstone"));
            ORE_MAP.put(MOON, Set.of("GTOTagPrefix.MOON_STONE"));
            ORE_MAP.put(MARS, Set.of("GTOTagPrefix.MARS_STONE"));
            ORE_MAP.put(VENUS, Set.of("GTOTagPrefix.VENUS_STONE"));
            ORE_MAP.put(MERCURY, Set.of("GTOTagPrefix.MERCURY_STONE"));
            ORE_MAP.put(GLACIO, Set.of("GTOTagPrefix.GLACIO_STONE"));
            ORE_MAP.put(TITAN, Set.of("GTOTagPrefix.TITAN_STONE"));
            ORE_MAP.put(PLUTO, Set.of("GTOTagPrefix.PLUTO_STONE"));
            ORE_MAP.put(IO, Set.of("GTOTagPrefix.IO_STONE"));
            ORE_MAP.put(GANYMEDE, Set.of("GTOTagPrefix.GANYMEDE_STONE"));
            ORE_MAP.put(ENCELADUS, Set.of("GTOTagPrefix.ENCELADUS_STONE"));
            ORE_MAP.put(CERES, Set.of("GTOTagPrefix.CERES_STONE"));
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, Set<String>> a = new O2OOpenCacheHashMap<>();
            GTOOres.ALL_ORES.forEach((k, v) -> v.keySet().forEach(m -> {
                Set<String> b = a.computeIfAbsent(StringIndex.MATERIAL_MAP.get(m), m1 -> new OpenCacheHashSet<>());
                if (ORE_MAP.containsKey(k)) b.addAll(ORE_MAP.get(k));
                a.put(StringIndex.MATERIAL_MAP.get(m), b);
            }));
            a.forEach((m, s) -> {
                stringBuilder.append("\nOREBuilder.put(").append(m).append(", ");
                StringBuilder sb = new StringBuilder();
                sb.append("Set.of(");
                s.forEach(s1 -> sb.append(s1).append(","));
                sb.deleteCharAt(sb.length() - 1).append(")");
                stringBuilder.append(sb).append(");");
            });
            GTOCore.LOGGER.error(stringBuilder.toString());
        }
    }

    private static final GTOreDefinition BAUXITE_VEIN = create("bauxite_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.3f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(MOON, GANYMEDE, OTHERSIDE)
            .heightRangeUniform(10, 80)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(2).mat(Bauxite).size(1, 4))
                            .layer(l -> l.weight(1).mat(Ilmenite).size(1, 2))
                            .layer(l -> l.weight(1).mat(Aluminium).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Bauxite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CHROMITE_VEIN = create("chromite_vein", vein -> vein
            .clusterSize(UniformInt.of(38, 44)).density(0.15f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(VENUS, TITAN, OTHERSIDE)
            .heightRangeUniform(20, 80)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Magnetite).size(1, 3))
                            .layer(l -> l.weight(1).mat(VanadiumMagnetite).size(1, 1))
                            .layer(l -> l.weight(4).mat(Chromite).size(1, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Magnetite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition NAQUADAH_VEIN = create("naquadah_vein", vein -> vein
            .clusterSize(UniformInt.of(48, 80)).density(0.25f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(IO, PLUTO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(10, 90)
            .cuboidVeinGenerator(generator -> generator
                    .top(b -> b.mat(Naquadah).size(2))
                    .middle(b -> b.mat(Naquadah).size(3))
                    .bottom(b -> b.mat(Naquadah).size(2))
                    .spread(b -> b.mat(Plutonium239)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Naquadah)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition PITCHBLENDE_VEIN = create("pitchblende_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 64)).density(0.25f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(MOON, TITAN, PLUTO, OTHERSIDE)
            .heightRangeUniform(30, 60)
            .cuboidVeinGenerator(generator -> generator
                    .top(b -> b.mat(Pitchblende).size(2))
                    .middle(b -> b.mat(Pitchblende).size(3))
                    .bottom(b -> b.mat(Pitchblende).size(2))
                    .spread(b -> b.mat(Uraninite)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Pitchblende)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition BORAX_VEIN = create("borax_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 64)).density(0.25f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(VENUS, MARS, CERES, OTHERSIDE)
            .heightRangeUniform(30, 60)
            .cuboidVeinGenerator(generator -> generator
                    .top(b -> b.mat(RockSalt).size(2))
                    .middle(b -> b.mat(Borax).size(3))
                    .bottom(b -> b.mat(Salt).size(2))
                    .spread(b -> b.mat(Lepidolite)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Borax)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition SCHEELITE_VEIN = create("scheelite_vein", vein -> vein
            .clusterSize(UniformInt.of(50, 64)).density(0.7f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(MARS, GLACIO, CERES, OTHERSIDE)
            .heightRangeUniform(20, 60)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Scheelite, 3, 20, 60))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Tungstate, 2, 35, 55))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Lithium, 1, 20, 40)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Scheelite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition SHELDONITE_VEIN = create("sheldonite_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.2f).weight(10)
            .layer(ALL_LAYER)
            .dimensions(MARS, MERCURY, ENCELADUS, GLACIO, OTHERSIDE)
            .heightRangeUniform(5, 50)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Bornite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Cooperite).size(1, 1))
                            .layer(l -> l.weight(2).mat(Platinum).size(1, 1))
                            .layer(l -> l.weight(1).mat(Palladium).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Platinum)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition BANDED_IRON_VEIN = create("banded_iron_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(VENUS, THE_NETHER, OTHERSIDE)
            .heightRangeUniform(20, 40)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Goethite, 3))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(YellowLimonite, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Hematite, 2))
                    .rareBlock(new VeinedVeinGenerator.VeinBlockDefinition(Gold, 1))
                    .rareBlockChance(0.075f)
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Goethite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition BERYLLIUM_VEIN = create("beryllium_vein", vein -> vein
            .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, THE_NETHER, GANYMEDE, OTHERSIDE)
            .heightRangeUniform(5, 30)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Beryllium, 3, 5, 30))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Emerald, 2, 5, 19))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Emerald, 2, 16, 30)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Beryllium)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CERTUS_QUARTZ_VEIN = create("certus_quartz", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, PLUTO, THE_NETHER, OTHERSIDE)
            .heightRangeUniform(20, 120)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Quartzite).size(2, 4))
                            .layer(l -> l.weight(2).mat(CertusQuartz).size(1, 1))
                            .layer(l -> l.weight(1).mat(Barite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(CertusQuartz)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));

    private static final GTOreDefinition MANGANESE_VEIN = create("manganese_vein", vein -> vein
            .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, ANCIENT_WORLD, MERCURY, THE_NETHER, CERES, OTHERSIDE)
            .heightRangeUniform(-30, 0)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Grossular, 3, -50, -5))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Spessartine, 2, -40, -15))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Pyrolusite, 2, -40, -15))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Tantalite, 1, -30, -5)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Grossular)
                    .density(0.15f)
                    .radius(3)));

    private static final GTOreDefinition MOLYBDENUM_VEIN = create("molybdenum_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(5)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, VENUS, THE_NETHER, IO, ENCELADUS, OTHERSIDE)
            .heightRangeUniform(20, 50)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Wulfenite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Molybdenite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Molybdenum).size(1, 1))
                            .layer(l -> l.weight(1).mat(Powellite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Molybdenum)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition MONAZITE_VEIN = create("monazite_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(30)
            .layer(ALL_LAYER)
            .dimensions(MOON, CERES, GLACIO, OTHERSIDE)
            .heightRangeUniform(20, 40)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Bastnasite).size(2, 4))
                            .layer(l -> l.weight(1).mat(Monazite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Neodymium).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Bastnasite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition NETHER_QUARTZ_VEIN = create("nether_quartz_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, THE_NETHER, OTHERSIDE)
            .heightRangeUniform(0, 40)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(NetherQuartz).size(2, 4))
                            .layer(l -> l.weight(1).mat(Quartzite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(NetherQuartz)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition REDSTONE_VEIN = create("redstone_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(60)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, THE_NETHER, ENCELADUS, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(5, 40)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Redstone).size(2, 4))
                            .layer(l -> l.weight(2).mat(Ruby).size(1, 1))
                            .layer(l -> l.weight(1).mat(Cinnabar).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Redstone)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition SALTPETER_VEIN = create("saltpeter_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, MARS, MERCURY, THE_NETHER, OTHERSIDE)
            .heightRangeUniform(5, 45)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Saltpeter).size(2, 4))
                            .layer(l -> l.weight(2).mat(Diatomite).size(1, 1))
                            .layer(l -> l.weight(2).mat(Electrotine).size(1, 1))
                            .layer(l -> l.weight(1).mat(Alunite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Saltpeter)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition SULFUR_VEIN = create("sulfur_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(100)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, VENUS, THE_NETHER, IO, OTHERSIDE)
            .heightRangeUniform(10, 30)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Sulfur).size(2, 4))
                            .layer(l -> l.weight(2).mat(Pyrite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Sphalerite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Sulfur)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition TETRAHEDRITE_VEIN = create("tetrahedrite_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(70)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, MARS, THE_NETHER, TITAN, OTHERSIDE)
            .heightRangeUniform(20, 100)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Tetrahedrite, 4))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Copper, 2))
                    .rareBlock(new VeinedVeinGenerator.VeinBlockDefinition(Stibnite, 1))
                    .rareBlockChance(0.15f)
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Tetrahedrite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));

    private static final GTOreDefinition TOPAZ_VEIN = create("topaz_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(70)
            .layer(ALL_LAYER)
            .dimensions(ANCIENT_WORLD, MERCURY, THE_NETHER, ENCELADUS, OTHERSIDE)
            .heightRangeUniform(20, 70)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(BlueTopaz).size(2, 4))
                            .layer(l -> l.weight(2).mat(Topaz).size(1, 1))
                            .layer(l -> l.weight(2).mat(Chalcocite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Bornite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Topaz)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));

    private static final GTOreDefinition APATITE_VEIN = create("apatite_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MARS, TITAN, PLUTO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(10, 80)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Apatite).size(2, 4))
                            .layer(l -> l.weight(2).mat(TricalciumPhosphate).size(1, 1))
                            .layer(l -> l.weight(1).mat(Pyrochlore).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Apatite)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CASSITERITE_VEIN = create("cassiterite_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MOON, GANYMEDE, GLACIO, OTHERSIDE)
            .heightRangeUniform(10, 80)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Tin, 4))
                    .rareBlock(new VeinedVeinGenerator.VeinBlockDefinition(Cassiterite, 2))
                    .rareBlockChance(0.33f)
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Cassiterite)));

    private static final GTOreDefinition COAL_VEIN = create("coal_vein", vein -> vein
            .clusterSize(UniformInt.of(38, 44)).density(0.25f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, GLACIO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(10, 140)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Coal).size(2, 4))
                            .layer(l -> l.weight(3).mat(Coal).size(2, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Coal)));

    private static final GTOreDefinition COPPER_TIN_VEIN = create("copper_tin_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(50)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MERCURY, GANYMEDE, CERES, OTHERSIDE)
            .heightRangeUniform(-10, 160)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Chalcopyrite, 5))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Zeolite, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Cassiterite, 2))
                    .rareBlock(new VeinedVeinGenerator.VeinBlockDefinition(Realgar, 1))
                    .rareBlockChance(0.1f)
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Chalcopyrite)));

    private static final GTOreDefinition GALENA_VEIN = create("galena_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, VENUS, ENCELADUS, PLUTO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(-15, 45)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Galena).size(2, 4))
                            .layer(l -> l.weight(2).mat(Silver).size(1, 1))
                            .layer(l -> l.weight(1).mat(Lead).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Galena)));

    private static final GTOreDefinition GARNET_TIN_VEIN = create("garnet_tin_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.4f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MOON, TITAN, GLACIO, OTHERSIDE)
            .heightRangeUniform(30, 60)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(CassiteriteSand).size(2, 4))
                            .layer(l -> l.weight(2).mat(GarnetSand).size(1, 1))
                            .layer(l -> l.weight(2).mat(Asbestos).size(1, 1))
                            .layer(l -> l.weight(1).mat(Diatomite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(GarnetSand)));

    private static final GTOreDefinition GARNET_VEIN = create("garnet_vein", vein -> vein
            .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MARS, PLUTO, OTHERSIDE)
            .heightRangeUniform(-10, 50)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(GarnetRed, 3, -10, 50))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(GarnetYellow, 2, -10, 50))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Amethyst, 2, -10, 22))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Opal, 1, 18, 50)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(GarnetRed)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition IRON_VEIN = create("iron_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(120)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MARS, IO, TITAN, CERES, OTHERSIDE)
            .heightRangeUniform(-10, 60)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Goethite, 5))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(YellowLimonite, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Hematite, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Malachite, 1))
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Goethite)));

    private static final GTOreDefinition LUBRICANT_VEIN = create("lubricant_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MOON, GANYMEDE, CERES, OTHERSIDE)
            .heightRangeUniform(0, 50)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Soapstone).size(2, 4))
                            .layer(l -> l.weight(2).mat(Talc).size(1, 1))
                            .layer(l -> l.weight(2).mat(GlauconiteSand).size(1, 1))
                            .layer(l -> l.weight(1).mat(Pentlandite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Talc)));

    private static final GTOreDefinition MAGNETITE_VEIN = create("magnetite_vein", vein -> vein
            .clusterSize(UniformInt.of(38, 44)).density(0.15f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MOON, MARS, OTHERSIDE)
            .heightRangeUniform(10, 60)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Magnetite).size(2, 4))
                            .layer(l -> l.weight(2).mat(VanadiumMagnetite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Gold).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Magnetite)));

    private static final GTOreDefinition MINERAL_SAND_VEIN = create("mineral_sand_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MARS, IO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(15, 60)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(BasalticMineralSand).size(2, 4))
                            .layer(l -> l.weight(2).mat(GraniticMineralSand).size(1, 1))
                            .layer(l -> l.weight(2).mat(FullersEarth).size(1, 1))
                            .layer(l -> l.weight(1).mat(Gypsum).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(BasalticMineralSand)));

    private static final GTOreDefinition NICKEL_VEIN = create("nickel_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MERCURY, ANCIENT_WORLD, GANYMEDE, OTHERSIDE)
            .heightRangeUniform(-10, 60)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Garnierite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Nickel).size(1, 1))
                            .layer(l -> l.weight(2).mat(Cobaltite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Pentlandite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Nickel)));

    private static final GTOreDefinition SALTS_VEIN = create("salts_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(50)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, ANCIENT_WORLD, GLACIO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(30, 70)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(RockSalt).size(2, 4))
                            .layer(l -> l.weight(2).mat(Salt).size(1, 1))
                            .layer(l -> l.weight(1).mat(Lepidolite).size(1, 1))
                            .layer(l -> l.weight(1).mat(Spodumene).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Salt)));

    private static final GTOreDefinition OILSANDS_VEIN = create("oilsands_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 29)).density(0.3f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, GLACIO, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(30, 80)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Oilsands).size(2, 4))
                            .layer(l -> l.weight(2).mat(Oilsands).size(1, 1))
                            .layer(l -> l.weight(1).mat(Oilsands).size(1, 1))
                            .layer(l -> l.weight(1).mat(Oilsands).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Oilsands)));

    private static final GTOreDefinition COPPER_VEIN = create("copper_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(80)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, ANCIENT_WORLD, MARS, ENCELADUS, OTHERSIDE)
            .heightRangeUniform(-40, 10)
            .veinedVeinGenerator(generator -> generator
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Chalcopyrite, 5))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Iron, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Pyrite, 2))
                    .oreBlock(new VeinedVeinGenerator.VeinBlockDefinition(Copper, 2))
                    .veininessThreshold(0.01f)
                    .maxRichnessThreshold(0.175f)
                    .minRichness(0.7f)
                    .maxRichness(1.0f)
                    .edgeRoundoffBegin(3)
                    .maxEdgeRoundoff(0.1f))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Copper)));

    private static final GTOreDefinition DIAMOND_VEIN = create("diamond_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, VENUS, IO, OTHERSIDE)
            .heightRangeUniform(-55, -30)
            .classicVeinGenerator(generator -> generator
                    .primary(b -> b.mat(Graphite).size(4))
                    .secondary(b -> b.mat(Graphite).size(3))
                    .between(b -> b.mat(Diamond).size(3))
                    .sporadic(b -> b.mat(Coal)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Diamond)
                    .density(0.1f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                    .radius(2)));

    private static final GTOreDefinition LAPIS_VEIN = create("lapis_vein", vein -> vein
            .clusterSize(UniformInt.of(40, 52)).density(0.75f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, ANCIENT_WORLD, GLACIO, OTHERSIDE)
            .heightRangeUniform(-60, 10)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Lazurite, 3, -60, 10))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Sodalite, 2, -50, 0))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Lapis, 2, -50, 0))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(Calcite, 1, -40, 10)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Lapis)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                    .radius(3)));

    private static final GTOreDefinition MICA_VEIN = create("mica_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, MARS, PLUTO, GANYMEDE, GLACIO, OTHERSIDE)
            .heightRangeUniform(-40, -10)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Kyanite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Mica).size(1, 1))
                            .layer(l -> l.weight(1).mat(Pollucite).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Mica)
                    .radius(3)));

    private static final GTOreDefinition OLIVINE_VEIN = create("olivine_vein", vein -> vein
            .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, VENUS, IO, CERES, OTHERSIDE)
            .heightRangeUniform(-20, 10)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Bentonite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Magnetite).size(1, 1))
                            .layer(l -> l.weight(2).mat(Olivine).size(1, 1))
                            .layer(l -> l.weight(1).mat(GlauconiteSand).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Olivine)
                    .density(0.15f)
                    .radius(3)));

    private static final GTOreDefinition SAPPHIRE_VEIN = create("sapphire_vein", vein -> vein
            .clusterSize(UniformInt.of(25, 30)).density(0.25f).weight(60)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, ANCIENT_WORLD, TITAN, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(3).mat(Almandine).size(2, 4))
                            .layer(l -> l.weight(2).mat(Pyrope).size(1, 1))
                            .layer(l -> l.weight(1).mat(Sapphire).size(1, 1))
                            .layer(l -> l.weight(1).mat(GreenSapphire).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Sapphire)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                    .radius(3)));

    private static final GTOreDefinition CELESTINE_VEIN = create("celestine_vein", vein -> vein
            .clusterSize(UniformInt.of(20, 24)).density(0.15f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(IO, GLACIO, OTHERSIDE)
            .heightRangeUniform(0, 40)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Celestine).size(1, 4))
                            .layer(l -> l.weight(1).mat(Cooperite).size(2, 3))
                            .layer(l -> l.weight(2).mat(Trona).size(2, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Cooperite)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition DESH_VEIN = create("desh_vein", vein -> vein
            .clusterSize(UniformInt.of(20, 24)).density(0.15f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(VENUS, TITAN, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Desh).size(2, 4))
                            .layer(l -> l.weight(2).mat(Magnesite).size(1, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Magnesite)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CALORITE_VEIN = create("calorite_vein", vein -> vein
            .clusterSize(UniformInt.of(20, 24)).density(0.15f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(MERCURY, BARNARDA_C, OTHERSIDE)
            .heightRangeUniform(-40, -10)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Calorite).size(2, 4))
                            .layer(l -> l.weight(2).mat(Cobalt).size(1, 4))
                            .layer(l -> l.weight(1).mat(Pyrochlore).size(1, 1))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Cobalt)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition OSTRUM_VEIN = create("ostrum_vein", vein -> vein
            .clusterSize(UniformInt.of(20, 24)).density(0.1f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(CERES, GLACIO, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Ostrum).size(2, 4))
                            .layer(l -> l.weight(2).mat(Gold).size(1, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Gold)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition TITANIUM_VEIN = create("titanium_vein", vein -> vein
            .clusterSize(UniformInt.of(15, 25)).density(0.1f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(ENCELADUS, OTHERSIDE)
            .heightRangeUniform(-40, -20)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(2).mat(Ilmenite).size(2, 4))
                            .layer(l -> l.weight(1).mat(Titanium).size(1, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Ilmenite)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition ZIRCON_VEIN = create("zircon_vein", vein -> vein
            .clusterSize(UniformInt.of(24, 29)).density(0.2f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(PLUTO, GLACIO, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Zircon).size(1, 4))
                            .layer(l -> l.weight(1).mat(Tungsten).size(1, 2))
                            .layer(l -> l.weight(1).mat(Pyrolusite).size(2, 4))
                            .layer(l -> l.weight(1).mat(Tantalite).size(1, 2))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Grossular)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CRYSTAL_VEIN_WATER_FIRE = create("crystal_vein_water_fire", vein -> vein
            .clusterSize(UniformInt.of(20, 40)).density(0.95f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, OTHERSIDE)
            .heightRangeUniform(-50, 0)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(PerditioCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(SalamanderCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(PerditioCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(UndineCrystal, 1, -60, 20)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(PerditioCrystal)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static final GTOreDefinition CRYSTAL_VEIN_EARTH_WIND = create("crystal_vein_earth_wind", vein -> vein
            .clusterSize(UniformInt.of(20, 40)).density(0.95f).weight(20)
            .layer(ALL_LAYER)
            .dimensions(OVERWORLD, OTHERSIDE)
            .heightRangeUniform(-50, 0)
            .dikeVeinGenerator(generator -> generator
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(PerditioCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(GnomeCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(PerditioCrystal, 1, -60, 20))
                    .withBlock(new DikeVeinGenerator.DikeBlockDefinition(SylphCrystal, 1, -60, 20)))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(PerditioCrystal)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static GTOreDefinition create(String name, Consumer<GTOreDefinition> config) {
        ResourceLocation id = GTCEu.id(name);
        GTOreDefinition definition = GTOres.create(id, config);
        List<VeinGenerator.VeinEntry> entries = definition.veinGenerator().getAllEntries();
        Object2IntOpenHashMap<Material> materialMap = new O2IOpenCacheHashMap<>();
        List<WeightedMaterial> materials = new ArrayList<>(entries.size());
        for (VeinGenerator.VeinEntry entry : entries) {
            Material material = entry.vein().right().orElse(null);
            if (material != null) {
                int value = entry.chance();
                materialMap.put(material, value);
                materials.add(new WeightedMaterial(material, value));
            }
        }
        for (ResourceKey<Level> dimension : definition.dimensionFilter()) {
            Object2IntOpenHashMap<Material> materialIntegerMap = ALL_ORES.computeIfAbsent(dimension.location(), k -> new O2IOpenCacheHashMap<>());
            materialMap.forEach((material, amount) -> materialIntegerMap.mergeInt(material, amount, Math::max));
            ALL_ORES.put(dimension.location(), materialIntegerMap);
        }
        BedrockOreDefinition.builder(id).size(9).dimensions(definition.dimensionFilter()).weight(definition.weight()).materials(materials).yield(2, 8).depletedYield(1).depletionAmount(1).depletionChance(100).register();
        return definition;
    }

    public static Material selectMaterial(ResourceLocation dimension) {
        MaterialSelector selector = RANDOM_ORES.computeIfAbsent(dimension, k -> {
            Object2IntOpenHashMap<Material> ores = ALL_ORES.get(k);
            if (ores == null) return null;
            return new MaterialSelector(ores);
        });
        if (selector == null) return GTMaterials.NULL;
        return selector.selectMaterial();
    }

    private static final class MaterialSelector {

        private final List<Material> materialList;
        private final List<Integer> cumulativeWeights;
        private int totalWeight;

        private MaterialSelector(Object2IntOpenHashMap<Material> materials) {
            this.materialList = new ArrayList<>(materials.keySet());
            this.cumulativeWeights = new ArrayList<>();
            this.totalWeight = 0;
            for (Integer weight : materials.values()) {
                this.totalWeight += weight;
                this.cumulativeWeights.add(this.totalWeight);
            }
        }

        private Material selectMaterial() {
            int randomValue = GTValues.RNG.nextInt(this.totalWeight);
            int index = Collections.binarySearch(this.cumulativeWeights, randomValue);
            if (index < 0) {
                index = -index - 1;
            }
            return materialList.get(index);
        }
    }

    private static Map<Material, String> getMaterialStringMap() {
        Map<Material, String> materialFieldMap = new Reference2ObjectOpenHashMap<>();

        // 填充GTOMaterials类的静态字段
        for (java.lang.reflect.Field field : GTOMaterials.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Material.class.isAssignableFrom(field.getType())) {
                    try {
                        Material material = (Material) field.get(null);
                        if (material != null) {
                            materialFieldMap.put(material, "GTOMaterials." + field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        // 忽略无法访问的字段
                    }
                }
            }
        }

        // 填充GTMaterials类的静态字段
        for (java.lang.reflect.Field field : GTMaterials.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Material.class.isAssignableFrom(field.getType())) {
                    try {
                        Material material = (Material) field.get(null);
                        if (material != null && !materialFieldMap.containsKey(material)) {
                            materialFieldMap.put(material, "GTMaterials." + field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        // 忽略无法访问的字段
                    }
                }
            }
        }
        return materialFieldMap;
    }

    private static class DimensionOreInfo {

        final Dimension dimension;
        int totalVeinWeight = 0;
        Map<Material, OreData> ores = new Reference2ObjectOpenHashMap<>();

        DimensionOreInfo(Dimension dimension) {
            this.dimension = dimension;
        }
    }

    private static class OreData {

        double totalOreWeight;
        int totalVeinWeight;
        int combinedWeight;
        float density;

        public OreData(double totalOreWeight, int totalVeinWeight, int combinedWeight, float density) {
            this.totalOreWeight = totalOreWeight;
            this.totalVeinWeight = totalVeinWeight;
            this.combinedWeight = combinedWeight;
            this.density = density;
        }
    }
}
