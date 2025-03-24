package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.utils.StringIndex;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.generator.indicators.SurfaceIndicatorGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.DikeVeinGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.VeinedVeinGenerator;
import com.gregtechceu.gtceu.common.data.GTOres;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.*;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.api.data.GTODimensions.*;
import static com.gto.gtocore.api.data.GTOWorldGenLayers.ALL_LAYER;

@SuppressWarnings("unused")
public interface GTOOres {

    Map<ResourceKey<Level>, Map<Material, Integer>> ALL_ORES = new Object2ObjectOpenHashMap<>();
    Map<ResourceLocation, BedrockOre> BEDROCK_ORES = new Object2ObjectOpenHashMap<>();
    Map<ResourceLocation, BedrockOreDefinition> BEDROCK_ORES_DEFINITION = new Object2ObjectOpenHashMap<>();

    static void init() {
        if (false) {
            Map<ResourceLocation, Set<String>> ORE_MAP = new Object2ObjectOpenHashMap<>();
            ORE_MAP.put(THE_NETHER, Set.of("TagPrefix.oreNetherrack"));
            ORE_MAP.put(THE_END, Set.of("TagPrefix.oreEndstone"));
            ORE_MAP.put(MOON, Set.of("GTOTagPrefix.moon_stone"));
            ORE_MAP.put(MARS, Set.of("GTOTagPrefix.mars_stone"));
            ORE_MAP.put(VENUS, Set.of("GTOTagPrefix.venus_stone"));
            ORE_MAP.put(MERCURY, Set.of("GTOTagPrefix.mercury_stone"));
            ORE_MAP.put(GLACIO, Set.of("GTOTagPrefix.glacio_stone"));
            ORE_MAP.put(TITAN, Set.of("GTOTagPrefix.titan_stone"));
            ORE_MAP.put(PLUTO, Set.of("GTOTagPrefix.pluto_stone"));
            ORE_MAP.put(IO, Set.of("GTOTagPrefix.io_stone"));
            ORE_MAP.put(GANYMEDE, Set.of("GTOTagPrefix.ganymede_stone"));
            ORE_MAP.put(ENCELADUS, Set.of("GTOTagPrefix.enceladus_stone"));
            ORE_MAP.put(CERES, Set.of("GTOTagPrefix.ceres_stone"));
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, Set<String>> a = new Object2ObjectOpenHashMap<>();
            GTOOres.ALL_ORES.forEach((k, v) -> v.keySet().forEach(m -> {
                Set<String> b = a.computeIfAbsent(StringIndex.MATERIAL_MAP.get(m), m1 -> new HashSet<>());
                if (ORE_MAP.containsKey(k.location())) b.addAll(ORE_MAP.get(k.location()));
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

    GTOreDefinition BAUXITE_VEIN = create("bauxite_vein", vein -> vein
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

    GTOreDefinition CHROMITE_VEIN = create("chromite_vein", vein -> vein
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

    GTOreDefinition NAQUADAH_VEIN = create("naquadah_vein", vein -> vein
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

    GTOreDefinition PITCHBLENDE_VEIN = create("pitchblende_vein", vein -> vein
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

    GTOreDefinition SCHEELITE_VEIN = create("scheelite_vein", vein -> vein
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

    GTOreDefinition SHELDONITE_VEIN = create("sheldonite_vein", vein -> vein
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

    GTOreDefinition BANDED_IRON_VEIN = create("banded_iron_vein", vein -> vein
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

    GTOreDefinition BERYLLIUM_VEIN = create("beryllium_vein", vein -> vein
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

    GTOreDefinition CERTUS_QUARTZ_VEIN = create("certus_quartz", vein -> vein
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

    GTOreDefinition MANGANESE_VEIN = create("manganese_vein", vein -> vein
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

    GTOreDefinition MOLYBDENUM_VEIN = create("molybdenum_vein", vein -> vein
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

    GTOreDefinition MONAZITE_VEIN = create("monazite_vein", vein -> vein
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

    GTOreDefinition NETHER_QUARTZ_VEIN = create("nether_quartz_vein", vein -> vein
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

    GTOreDefinition REDSTONE_VEIN = create("redstone_vein", vein -> vein
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

    GTOreDefinition SALTPETER_VEIN = create("saltpeter_vein", vein -> vein
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

    GTOreDefinition SULFUR_VEIN = create("sulfur_vein", vein -> vein
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

    GTOreDefinition TETRAHEDRITE_VEIN = create("tetrahedrite_vein", vein -> vein
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

    GTOreDefinition TOPAZ_VEIN = create("topaz_vein", vein -> vein
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

    GTOreDefinition APATITE_VEIN = create("apatite_vein", vein -> vein
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

    GTOreDefinition CASSITERITE_VEIN = create("cassiterite_vein", vein -> vein
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

    GTOreDefinition COAL_VEIN = create("coal_vein", vein -> vein
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

    GTOreDefinition COPPER_TIN_VEIN = create("copper_tin_vein", vein -> vein
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

    GTOreDefinition GALENA_VEIN = create("galena_vein", vein -> vein
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

    GTOreDefinition GARNET_TIN_VEIN = create("garnet_tin_vein", vein -> vein
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

    GTOreDefinition GARNET_VEIN = create("garnet_vein", vein -> vein
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

    GTOreDefinition IRON_VEIN = create("iron_vein", vein -> vein
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

    GTOreDefinition LUBRICANT_VEIN = create("lubricant_vein", vein -> vein
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

    GTOreDefinition MAGNETITE_VEIN = create("magnetite_vein", vein -> vein
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

    GTOreDefinition MINERAL_SAND_VEIN = create("mineral_sand_vein", vein -> vein
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

    GTOreDefinition NICKEL_VEIN = create("nickel_vein", vein -> vein
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

    GTOreDefinition SALTS_VEIN = create("salts_vein", vein -> vein
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

    GTOreDefinition OILSANDS_VEIN = create("oilsands_vein", vein -> vein
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

    GTOreDefinition COPPER_VEIN = create("copper_vein", vein -> vein
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

    GTOreDefinition DIAMOND_VEIN = create("diamond_vein", vein -> vein
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

    GTOreDefinition LAPIS_VEIN = create("lapis_vein", vein -> vein
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

    GTOreDefinition MICA_VEIN = create("mica_vein", vein -> vein
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

    GTOreDefinition OLIVINE_VEIN = create("olivine_vein", vein -> vein
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

    GTOreDefinition SAPPHIRE_VEIN = create("sapphire_vein", vein -> vein
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

    GTOreDefinition CELESTINE_VEIN = create("celestine_vein", vein -> vein
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

    GTOreDefinition DESH_VEIN = create("desh_vein", vein -> vein
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

    GTOreDefinition CALORITE_VEIN = create("calorite_vein", vein -> vein
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

    GTOreDefinition OSTRUM_VEIN = create("ostrum_vein", vein -> vein
            .clusterSize(UniformInt.of(20, 24)).density(0.1f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(CERES, GLACIO, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Ostrum).size(2, 4))
                            .layer(l -> l.weight(1).mat(Tungsten).size(1, 2))
                            .layer(l -> l.weight(2).mat(Gold).size(1, 4))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Gold)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    GTOreDefinition TITANIUM_VEIN = create("titanium_vein", vein -> vein
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

    GTOreDefinition ZIRCON_VEIN = create("zircon_vein", vein -> vein
            .clusterSize(UniformInt.of(24, 29)).density(0.2f).weight(40)
            .layer(ALL_LAYER)
            .dimensions(PLUTO, GLACIO, OTHERSIDE)
            .heightRangeUniform(-40, 0)
            .layeredVeinGenerator(generator -> generator
                    .buildLayerPattern(config -> config
                            .layer(l -> l.weight(1).mat(GTOMaterials.Zircon).size(1, 4))
                            .layer(l -> l.weight(1).mat(Grossular).size(2, 4))
                            .layer(l -> l.weight(1).mat(Pyrolusite).size(2, 4))
                            .layer(l -> l.weight(1).mat(Tantalite).size(1, 2))
                            .build()))
            .surfaceIndicatorGenerator(indicator -> indicator
                    .surfaceRock(Grossular)
                    .density(0.15f)
                    .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

    private static GTOreDefinition create(String name, Consumer<GTOreDefinition> config) {
        ResourceLocation id = GTCEu.id(name);
        GTOreDefinition definition = GTOres.create(id, config);
        List<Map.Entry<Either<BlockState, Material>, Integer>> entries = definition.veinGenerator().getAllEntries();
        Object2IntOpenHashMap<Material> materialMap = new Object2IntOpenHashMap<>();
        List<Pair<Material, Integer>> materials = new ArrayList<>(entries.size());
        for (Map.Entry<Either<BlockState, Material>, Integer> entry : entries) {
            Material material = entry.getKey().right().orElse(null);
            if (material != null) {
                int value = entry.getValue();
                materialMap.put(material, value);
                materials.add(Pair.of(material, value));
            }
        }
        for (ResourceKey<Level> dimension : definition.dimensionFilter()) {
            Map<Material, Integer> materialIntegerMap = ALL_ORES.computeIfAbsent(dimension, k -> new Object2IntOpenHashMap<>());
            materialMap.forEach((material, amount) -> materialIntegerMap.merge(material, amount, Math::max));
            ALL_ORES.put(dimension, materialIntegerMap);
        }
        BEDROCK_ORES.put(id, new BedrockOre(definition.dimensionFilter(), definition.weight(), materials));
        return definition;
    }

    record BedrockOre(Set<ResourceKey<Level>> dimensions, int weight, List<Pair<Material, Integer>> materials) {}
}
