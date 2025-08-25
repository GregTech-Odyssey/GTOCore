package com.gtocore.common.data.machines;

import com.gtocore.api.lang.ComponentListSupplier;
import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.AdvancedHyperRenderer;
import com.gtocore.client.renderer.machine.AnnihilateGeneratorRenderer;
import com.gtocore.client.renderer.machine.ArrayMachineRenderer;
import com.gtocore.common.data.*;
import com.gtocore.common.data.machines.structure.AnnihilateGeneratorA;
import com.gtocore.common.data.machines.structure.AnnihilateGeneratorB;
import com.gtocore.common.data.translation.GTOMachineTranslation;
import com.gtocore.common.machine.multiblock.electric.space.DysonSphereLaunchSiloMachine;
import com.gtocore.common.machine.multiblock.electric.space.DysonSphereReceivingStationMcahine;
import com.gtocore.common.machine.multiblock.generator.*;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MultiBlockFileReader;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;

import com.tterrag.registrate.util.entry.BlockEntry;

import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtolib.api.registries.GTORegistration.GTO;
import static com.gtolib.utils.register.MachineRegisterUtils.*;

public final class GeneratorMultiblock {

    public static void init() {}

    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_ENERGETIC = registerPhotovoltaicPowerStation("energetic", "充能", 1, GTBlocks.CASING_STEEL_SOLID, GTOBlocks.ENERGETIC_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_solid_steel"));
    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_PULSATING = registerPhotovoltaicPowerStation("pulsating", "脉冲", 4, GTBlocks.CASING_TITANIUM_STABLE, GTOBlocks.PULSATING_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_stable_titanium"));
    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_VIBRANT = registerPhotovoltaicPowerStation("vibrant", "振动", 16, GTBlocks.CASING_TUNGSTENSTEEL_ROBUST, GTOBlocks.VIBRANT_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"));

    private static MultiblockMachineDefinition registerPhotovoltaicPowerStation(String name, String cn, int basicRate, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock, ResourceLocation texture) {
        ComponentListSupplier tooltips;
        if (basicRate == 1) tooltips = GTOMachineTranslation.INSTANCE.getPhotovoltaicPlant11Tooltips();
        else if (basicRate == 4) tooltips = GTOMachineTranslation.INSTANCE.getPhotovoltaicPlant12Tooltips();
        else if (basicRate == 16) tooltips = GTOMachineTranslation.INSTANCE.getPhotovoltaicPlant13Tooltips();
        else tooltips = GTOMachineTranslation.INSTANCE.getPhotovoltaicPlant11Tooltips();

        return multiblock(name + "_photovoltaic_power_station", cn + "光伏电站", holder -> new PhotovoltaicPowerStationMachine(holder, basicRate))
                .nonYAxisRotation()
                .tooltips(tooltips.getSupplier())
                .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
                .generator()
                .tooltipsText("根据维度和天气输出EU或魔力", "Output EU or Mana based on dimensions and weather")
                .tooltipsText("在空间站运行时可保持最大功率，但需提供每秒功率/4mB的蒸馏水保持运行", "The space station can maintain full power operation, requires a distilled water supply of Power/4 mB per second")
                .block(casing)
                .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.LEFT)
                        .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                        .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("  ~CD  ", "   C   ", "   C   ", " AACAA ", "ABBCBBA")
                        .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("   D   ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("       ", "       ", "       ", "       ", "ABBCBBA")
                        .aisle("       ", "       ", "       ", "       ", "AAAAAAA")
                        .where('A', frames(GTMaterials.Aluminium))
                        .where('B', blocks(photovoltaicBlock.get()))
                        .where('C', blocks(casing.get()))
                        .where('D', blocks(casing.get())
                                .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                                .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                                .or(abilities(GTOPartAbility.OUTPUT_MANA).setMaxGlobalLimited(4))
                                .or(abilities(MAINTENANCE).setExactLimit(1)))
                        .where('~', controller(blocks(definition.get())))
                        .where(' ', any())
                        .build())
                .workableCasingRenderer(texture, GTCEu.id("block/multiblock/generator/large_steam_turbine"))
                .register();
    }

    public static final MultiblockMachineDefinition MAGNETIC_FLUID_GENERATOR = multiblock("magnetic_fluid_generator", "磁流体发电机", MagneticFluidGeneratorMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.PLASMA_GENERATOR_FUELS)
            .tooltipsText("实际产出由等离子热值决定", "Actual output is determined by plasma heat value")
            .tooltipsText("玻璃等级限制了能量输出仓等级", "The glass tier limits the energy output hatch tier")
            .tooltipsText("如果使用激光仓，则提升发电量x1.2^等级", "If a laser hatch is used, power generation is increased by x1.2^tier")
            .tooltipsText("等离子体洪流带着磅礴的能量奔涌", "A torrent of plasma rushes forward with majestic energy")
            .laserTooltips()
            .generator()
            .block(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("AAAABBBBBBBBBAAAA", "CCCCBBBBBBBBBCCCC", "CDDCBEEEEEEEBCCCC", "CDDCBBBBBBBBBCCCC", "CDDCBEEEEEEEBCCCC", "CCCCBBBBBBBBBCCCC", "AAAABBBBBBBBBAAAA")
                    .aisle("CCCCBBBBBBBBBCCCC", "IFFFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IFFFFGHGHGHGFFFFI", "CCCCBBBBBBBBBCCCC")
                    .aisle("CDDCBEEEEEEEBCCCC", "IJJFFGHGHGHGFFFFI", "KKKKKKKKKKKKKKKKI", "KKKKKKKKKKKKKKKKI", "KKKKKKKKKKKKKKKKI", "IJJFFGHGHGHGFFFFI", "CDDCBEEEEEEEBCCCC")
                    .aisle("CDDCBBBBBBBBBCCCC", "IJJFFGHGHGHGFFFFI", "KKKKKKKKKKKKKKKKI", "K              AL", "KKKKKKKKKKKKKKKKI", "IJJFFGHGHGHGFFFFI", "CDDCBBBBBBBBBCCCC")
                    .aisle("CDDCBEEEEEEEBCCCC", "IJJFFGHGHGHGFFFFI", "KKKKKKKKKKKKKKKKI", "KKKKKKKKKKKKKKKKI", "KKKKKKKKKKKKKKKKI", "IJJFFGHGHGHGFFFFI", "CDDCBEEEEEEEBCCCC")
                    .aisle("CCCCBBBBBBBBBCCCC", "IFFFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IJJFFGHGHGHGFFFFI", "IFFFFGHGHGHGFFFFI", "CCCCBBBBBBBBBCCCC")
                    .aisle("AAAABBBBBBBBBAAAA", "CCCCBBBBBBBBBCCCC", "CDDCBEEEEEEEBCCCC", "CDDCBBBBBBBBBCCCC", "CDDCBEEEEEEEBCCCC", "CCCCBBBBBBBBBCCCC", "AAAABBBBBBBBBAAAA")
                    .where('A', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Tungsten)))
                    .where('B', blocks(GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get()))
                    .where('C', GTOPredicates.absBlocks())
                    .where('D', GTOPredicates.glass())
                    .where('E', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where('F', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()))
                    .where('G', blocks(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.NeodymiumMagnetic)))
                    .where('H', blocks(GTBlocks.SUPERCONDUCTING_COIL.get()))
                    .where('J', blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where('K', blocks(GTOBlocks.BORON_CARBIDE_CERAMIC_RADIATION_RESISTANT_MECHANICAL_CUBE.get()))
                    .where('L', controller(blocks(definition.get())))
                    .where('I', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get())
                            .or(abilities(OUTPUT_LASER).setExactLimit(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"), GTCEu.id("block/multiblock/generator/extreme_combustion_engine"))
            .register();

    public static final MultiblockMachineDefinition LARGE_SEMI_FLUID_GENERATOR = registerLargeCombustionEngine(GTO,
            "large_semi_fluid_generator", "大型半流质发电机", EV, GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS,
            GTBlocks.CASING_TITANIUM_STABLE, GTBlocks.CASING_STEEL_GEARBOX, GTBlocks.CASING_ENGINE_INTAKE,
            GTCEu.id("block/casings/solid/machine_casing_stable_titanium"),
            GTCEu.id("block/multiblock/generator/large_combustion_engine"), false);

    public static final MultiblockMachineDefinition CHEMICAL_ENERGY_DEVOURER = multiblock("chemical_energy_devourer", "化学能吞噬者", ChemicalEnergyDevourerMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.CHEMICAL_ENERGY_DEVOURER_FUELS)
            .generator()
            .tooltipsKey("gtceu.universal.tooltip.base_production_eut", V[ZPM])
            .tooltipsKey("gtceu.universal.tooltip.uses_per_hour_lubricant", 10000)
            .tooltipsText("提供§f320mB/s§7的液态氧，并消耗§f双倍§7燃料以产生高达§f%s§7EU/t的功率", "Provides §f320mB/s§f of Liquid Oxygen and consumes §fdouble§f fuel to produce up to §f%s§7 EU/t of power.", V[UV])
            .tooltipsText("再额外提供§f480mB/s§7的四氧化二氮，并消耗§f四倍§7燃料以产生高达§f%s§7EU/t的功率", "Also provides §f480mB/s§f of Dinitrogen Tetroxide and consumes §fquadruple§f fuel to produce up to §f%s§f EU/t of power.", V[UHV])
            .combinedRecipeTooltips()
            .block(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('A', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()))
                    .where('B', blocks(GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get()))
                    .where('C', blocks(GTBlocks.MACHINE_CASING_LuV.get()))
                    .where('D', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.TungstenSteel)))
                    .where('E', blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
                    .where('F', blocks(GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.get()))
                    .where('G', blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where('H', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('I', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('J', blocks(GTBlocks.FILTER_CASING.get()))
                    .where('K', blocks(GTBlocks.CASING_STEEL_GEARBOX.get()))
                    .where('L', blocks(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()))
                    .where('M', blocks(GTBlocks.FIREBOX_TUNGSTENSTEEL.get()))
                    .where('N', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get())
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(4, 4)))
                    .where('n', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get())
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(4, 4)))
                    .where('O', blocks(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get()))
                    .where('P', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                    .where('Q', controller(blocks(definition.get())))
                    .where('R', blocks(GTBlocks.CASING_STEEL_TURBINE.get()))
                    .where('r', blocks(GTBlocks.CASING_STEEL_TURBINE.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(4, 4)))
                    .where('S', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('T', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)))
                    .where('U', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where('V', blocks(GTBlocks.MACHINE_CASING_HV.get()))
                    .where('W', abilities(MUFFLER))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTCEu.id("block/multiblock/generator/extreme_combustion_engine"), false)
            .register();

    private static MultiblockMachineDefinition registerMegaTurbine(String name, String cn, int tier, boolean special, GTRecipeType recipeType,
                                                                   Supplier<Block> casing, Supplier<Block> gear, ResourceLocation baseCasing,
                                                                   ResourceLocation overlayModel) {
        return multiblock(name, "特大" + cn, holder -> new TurbineMachine(holder, tier, special, true))
                .addTooltipsFromClass(TurbineMachine.class)
                .nonYAxisRotation()
                .recipeTypes(recipeType)
                .generator()
                .tooltipsKey("gtocore.machine.mega_steam_turbine.tooltip.0")
                .tooltipsKey("gtocore.machine.mega_steam_turbine.tooltip.1")
                .tooltipsKey("gtceu.universal.tooltip.base_production_eut", V[tier] * (special ? 12 : 8))
                .tooltipsKey("gtceu.multiblock.turbine.efficiency_tooltip", VNF[tier])
                .block(casing)
                .pattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   A A   A", "AAAA A AAAA", "A   A A   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBEBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   AAA   A", "AAAAAEAAAAA", "A   AAA   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   A A   A", "AAAA A AAAA", "A   A A   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBEBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   AAA   A", "AAAAAEAAAAA", "A   AAA   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "    AAA    ", "   A A A   ", "  A AAA A  ", "  AAAEAAA  ", "  A AAA A  ", "   A A A   ", "    AAA    ", "           ", "           ")
                        .aisle("           ", "           ", "    ABA    ", "   BBBBB   ", "  ABBBBBA  ", "  BBBEBBB  ", "  ABBBBBA  ", "   BBBBB   ", "    ABA    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B  E  B  ", "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  BGAEAGB  ", "  HGEIEGH  ", "  BGAEAGB  ", "   BGGGB   ", "    BHB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  H     H  ", "  H     H  ", "  H     H  ", "   B   B   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  HGAEAGH  ", "  HGEIEGH  ", "  HGAEAGH  ", "   BGGGB   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  HGAEAGH  ", "  HGEIEGH  ", "  HGAEAGH  ", "   BGGGB   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  H     H  ", "  H     H  ", "  H     H  ", "   B   B   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  BGAEAGB  ", "  HGEIEGH  ", "  BGAEAGB  ", "   BGGGB   ", "    BHB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B  E  B  ", "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           ")
                        .aisle("           ", "           ", "    ABA    ", "   BBBBB   ", "  ABBBBBA  ", "  BBBEBBB  ", "  ABBBBBA  ", "   BBBBB   ", "    ABA    ", "           ", "           ")
                        .aisle("           ", "           ", "    AAA    ", "   A A A   ", "  A AAA A  ", "  AAAEAAA  ", "  A AAA A  ", "   A A A   ", "    AAA    ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "     A     ", "    AEA    ", "     A     ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "     A     ", "    AEA    ", "     A     ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "   AAAAA   ", "   ABBBA   ", "   BBBBB   ", "   BBEBB   ", "   BBBBB   ", "   ABBBA   ", "   AAAAA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABBBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABFBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABBBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   AAAAA   ", "   ABBBA   ", "   BCCCB   ", "   BCDCB   ", "   BCCCB   ", "   ABBBA   ", "   AAAAA   ", "           ", "           ")
                        .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                        .where('B', blocks(casing.get()))
                        .where('C', blocks(casing.get())
                                .or(abilities(MAINTENANCE).setExactLimit(1))
                                .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(8))
                                .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(2))
                                .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(4))
                                .or(blocks(GTOMachines.ROTOR_HATCH.getBlock()).setMaxGlobalLimited(1)))
                        .where('D', controller(blocks(definition.get())))
                        .where('E', blocks(gear.get()))
                        .where('F', abilities(MUFFLER))
                        .where('G', heatingCoils())
                        .where('H', GTOPredicates.glass())
                        .where('I', GTOPredicates.RotorBlock(tier))
                        .where(' ', any())
                        .build())
                .workableCasingRenderer(baseCasing, overlayModel)
                .register();
    }

    public static final MultiblockMachineDefinition ROCKET_LARGE_TURBINE = registerLargeTurbine(GTO,
            "rocket_large_turbine", "大型火箭引擎涡轮", EV, true,
            GTORecipeTypes.ROCKET_ENGINE_FUELS,
            GTBlocks.CASING_TITANIUM_TURBINE, GTBlocks.CASING_TITANIUM_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_titanium"),
            GTCEu.id("block/multiblock/generator/large_gas_turbine"), false);

    public static final MultiblockMachineDefinition SUPERCRITICAL_STEAM_TURBINE = registerLargeTurbine(GTO,
            "supercritical_steam_turbine", "超临界蒸汽涡轮", IV, false,
            GTORecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS,
            GTOBlocks.SUPERCRITICAL_TURBINE_CASING, GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX,
            GTOCore.id("block/casings/supercritical_turbine_casing"),
            GTCEu.id("block/multiblock/generator/large_plasma_turbine"), false);

    public static final MultiblockMachineDefinition STEAM_MEGA_TURBINE = registerMegaTurbine("steam_mega_turbine", "蒸汽涡轮", EV, false, GTRecipeTypes.STEAM_TURBINE_FUELS, GTBlocks.CASING_STEEL_TURBINE, GTBlocks.CASING_STEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_steel"), GTCEu.id("block/multiblock/generator/large_steam_turbine"));
    public static final MultiblockMachineDefinition GAS_MEGA_TURBINE = registerMegaTurbine("gas_mega_turbine", "燃气涡轮", IV, false, GTRecipeTypes.GAS_TURBINE_FUELS, GTBlocks.CASING_STAINLESS_TURBINE, GTBlocks.CASING_STAINLESS_STEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_stainless_steel"), GTCEu.id("block/multiblock/generator/large_gas_turbine"));
    public static final MultiblockMachineDefinition ROCKET_MEGA_TURBINE = registerMegaTurbine("rocket_mega_turbine", "火箭引擎涡轮", IV, true, GTORecipeTypes.ROCKET_ENGINE_FUELS, GTBlocks.CASING_TITANIUM_TURBINE, GTBlocks.CASING_TITANIUM_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_titanium"), GTCEu.id("block/multiblock/generator/large_gas_turbine"));
    public static final MultiblockMachineDefinition SUPERCRITICAL_MEGA_STEAM_TURBINE = registerMegaTurbine("supercritical_mega_steam_turbine", "超临界蒸汽涡轮", LuV, false, GTORecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS, GTOBlocks.SUPERCRITICAL_TURBINE_CASING, GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX,
            GTOCore.id("block/casings/supercritical_turbine_casing"), GTCEu.id("block/multiblock/generator/large_plasma_turbine"));

    public static final MultiblockMachineDefinition DYSON_SPHERE_LAUNCH_SILO = multiblock("dyson_sphere_launch_silo", "戴森球发射井", DysonSphereLaunchSiloMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.DYSON_SPHERE_RECIPES)
            .block(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                    .where('B', blocks(GTOBlocks.SPACE_ELEVATOR_INTERNAL_SUPPORT.get()))
                    .where('C', blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where('D', blocks(GTOBlocks.SPACE_ELEVATOR_SUPPORT.get()))
                    .where('E', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('F', blocks(GTOBlocks.DYSON_DEPLOYMENT_MAGNET.get()))
                    .where('G', blocks(GTOBlocks.GRAVITON_FIELD_CONSTRAINT_CASING.get()))
                    .where('H', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('I', blocks(GTOBlocks.SPACE_ELEVATOR_POWER_MODULE_5.get()))
                    .where('J', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where('K', blocks(GTOBlocks.DIMENSION_INJECTION_CASING.get()))
                    .where('L', blocks(GTOBlocks.DYSON_DEPLOYMENT_CASING.get()))
                    .where('M', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Quantanium)))
                    .where('N', blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where('O', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Mithril)))
                    .where('P', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)))
                    .where('Q', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('R', blocks(GTOBlocks.FUSION_CASING_MK5.get()))
                    .where('S', blocks(GTOBlocks.DYSON_CONTROL_CASING.get()))
                    .where('T', blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where('U', blocks(GTOBlocks.ACCELERATED_PIPELINE.get()))
                    .where('V', blocks(GTOBlocks.DYSON_CONTROL_TOROID.get()))
                    .where('W', blocks(GTOBlocks.DYSON_DEPLOYMENT_CORE.get()))
                    .where('X', blocks(GTOBlocks.SPACETIME_ASSEMBLY_LINE_UNIT.get()))
                    .where('Y', blocks(GTBlocks.CASING_PALLADIUM_SUBSTATION.get()))
                    .where('[', blocks(GTOBlocks.HIGH_ENERGY_ULTRAVIOLET_EMITTER_CASING.get()))
                    .where('a', blocks(GTOBlocks.DYSON_CONTROL_CASING.get())
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(autoAbilities(definition.getRecipeTypes())))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/space_elevator_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition DYSON_SPHERE_RECEIVING_STATION = multiblock("dyson_sphere_receiving_station", "戴森球接收站", DysonSphereReceivingStationMcahine::new)
            .nonYAxisRotation()
            .generator()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .tooltipsText("发射戴森球模块后开始工作", "Starts working after launching Dyson Sphere modules")
            .tooltipsText("每次运行都有(模块数量/128 + 1)%的概率损坏一次模块", "Each run has a (Module Count / 128 + 1)% chance to damage a module")
            .tooltipsText("当损坏高于60%时，输出效率随损坏值由100%逐渐降低到20%，并输出随损坏值增强的红石信号", "When damage exceeds 60%, output efficiency gradually decreases from 100% to 20% with damage value, and outputs a redstone signal enhanced by the damage value")
            .tooltipsText("当损坏达到100%时减少一次模块发射数量，并重制损坏值", "When damage reaches 100%, it reduces the number of module launches by one and resets the damage value")
            .tooltipsText("在损坏值高于60%时发射不会增加发射次数，但会重制损坏值", "When damage value is above 60%, launching will not increase the launch count but will reset the damage value")
            .tooltipsText("产能功率，和需求算力由发射的模块数量决定", "Power capacity and demand computing power are determined by the number of launched modules")
            .tooltipsText("每次发射可使功率增加1A MAX", "Each launch can increase power by 1A MAX")
            .block(GTBlocks.HIGH_POWER_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where('B', blocks(GTOBlocks.DYSON_CONTROL_TOROID.get()))
                    .where('C', blocks(GTOBlocks.RADIATION_ABSORBENT_CASING.get()))
                    .where('D', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                    .where('E', blocks(GTOBlocks.SPACE_ELEVATOR_INTERNAL_SUPPORT.get()))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)))
                    .where('G', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('H', blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where('I', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('a', blocks(GTBlocks.HIGH_POWER_CASING.get())
                            .or(abilities(IMPORT_FLUIDS).setExactLimit(1))
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(abilities(OUTPUT_LASER).setExactLimit(1)))
                    .where('K', blocks(GTOBlocks.SPACE_ELEVATOR_SUPPORT.get()))
                    .where('L', blocks(GTOBlocks.MOLECULAR_COIL.get()))
                    .where('M', blocks(GTOBlocks.DYSON_RECEIVER_CASING.get()))
                    .where('N', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition LARGE_NAQUADAH_REACTOR = multiblock("large_naquadah_reactor", "大型硅岩反应堆", ElectricMultiblockMachine::new)
            .allRotation()
            .recipeTypes(GTORecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES)
            .generator()
            .block(GTOBlocks.HYPER_MECHANICAL_CASING)
            .recipeModifier(RecipeModifierFunction.GENERATOR_OVERCLOCKING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           a~a           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           AAA           ", "          aBBBa          ", "           AAA           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "          CCCCC          ", "        CC     CC        ", "      CC         CC      ", "    CC             CC    ", "    C               C    ", "   C                 C   ", "   C                 C   ", "  C                   C  ", "  C                   C  ", " C         AAA         C ", " C        A   A        C ", " C       aB   Ba       C ", " C        A   A        C ", " C         AAA         C ", "  C                   C  ", "  C                   C  ", "   C                 C   ", "   C                 C   ", "    C               C    ", "    CC             CC    ", "      CC         CC      ", "        CC     CC        ", "          CCCCC          ", "                         ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  CCCCC  CC      ", "    CC  DD  C  DD  CC    ", "   C  DD    C    DD  C   ", "   C D             D C   ", "  C D               D C  ", "  C D               D C  ", " C D        A        D C ", " C D        A        D C ", "C C       AAAAA       C C", "C C      A E E A      C C", "C CCC   aB E E Ba   CCC C", "C C      A E E A      C C", "C C       AAAAA       C C", " C D        A        D C ", " C D        A        D C ", "  C D               D C  ", "  C D               D C  ", "   C D             D C   ", "   C  DD    C    DD  C   ", "    CC  DD  C  DD  CC    ", "      CC  CCCCC  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  DDFDD CCC      ", "    CC  DDGCFCGDD  CC    ", "   C  DD  GCFCG  DD  C   ", "   C D    G F G    D C   ", "  C D       F       D C  ", "  C D       F       D C  ", " C D       A A       D C ", " C D       A A       D C ", "C DGGG    AA AA    GGGD C", "C DCC    A     A    CCD C", "C DAAAAAAB     BAAAAAAD C", "C DCC    A     A    CCD C", "C DGGG    AA AA    GGGD C", " C D       A A       D C ", " C D       A A       D C ", "  C D       F       D C  ", "  C D       F       D C  ", "   C D    G F G    D C   ", "   CC DD  GCFCG  DD  C   ", "    CC  DDGCFCGDD  CC    ", "      CC  DDFDD  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  CCCCC  CC      ", "    CC  DD  C  DD  CC    ", "   C  DD    C    DD  C   ", "   C D             D C   ", "  C D               D C  ", "  C D               D C  ", " C D        A        D C ", " C D        A        D C ", "C C       AAAAA       C C", "C C      A E E A      C C", "C CCC   aB E E Ba   CCC C", "C C      A E E A      C C", "C C       AAAAA       C C", " C D        A        D C ", " C D        A        D C ", "  C D               D C  ", "  C D               D C  ", "   C D             D C   ", "   CC DD    C    DD  C   ", "    CC  DD  C  DD  CC    ", "      CC  CCCCC  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("                         ", "          CCCCC          ", "        CC     CC        ", "      CC         CC      ", "    CC             CC    ", "    C               C    ", "   C                 C   ", "   C                 C   ", "  C                   C  ", "  C                   C  ", " C         AAA         C ", " C        A   A        C ", " C       aB   Ba       C ", " C        A   A        C ", " C         AAA         C ", "  C                   C  ", "  C                   C  ", "   C                 C   ", "   C                 C   ", "    C               C    ", "    CC             CC    ", "      CC         CC      ", "        CC     CC        ", "          CCCCC          ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           AAA           ", "          aBBBa          ", "           AAA           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           aaa           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where('B', blocks(GTOBlocks.AMPROSIUM_GEARBOX.get()))
                    .where('C', blocks(GTOBlocks.EXTREME_STRENGTH_TRITANIUM_CASING.get()))
                    .where('D', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('E', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria)))
                    .where('F', blocks(GTOBlocks.AMPROSIUM_PIPE_CASING.get()))
                    .where('G', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Trinium)))
                    .where('a', blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(abilities(OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition ADVANCED_HYPER_REACTOR = multiblock("advanced_hyper_reactor", "进阶超能反应堆", ElectricMultiblockMachine::new)
            .nonYAxisRotation()
            .tooltipsText("提供不同等离子体获得不同并行", "Provides different plasmas to achieve different parallelism")
            .tooltipsText("星辉：8，致密中子：16", "Starmetal: 8, Dense Neutrons: 16")
            .recipeTypes(GTORecipeTypes.ADVANCED_HYPER_REACTOR_RECIPES)
            .generator()
            .recipeModifier((machine, recipe) -> {
                if (machine instanceof ElectricMultiblockMachine workableElectricMultiblockMachine) {
                    int p = 1;
                    if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1))) {
                        p = 8;
                    }
                    if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1))) {
                        p = 16;
                    }
                    return RecipeModifierFunction.generatorOverclocking(workableElectricMultiblockMachine, ParallelLogic.accurateParallel(machine, recipe, p));
                }
                return recipe;
            })
            .block(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('B', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria)))
                    .where('D', blocks(GTOBlocks.ECHO_CASING.get()))
                    .where('E', blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where('F', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('G', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('H', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('a', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(abilities(OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(' ', any())
                    .build())
            .renderer(AdvancedHyperRenderer::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition HYPER_REACTOR = multiblock("hyper_reactor", "超能反应堆", ElectricMultiblockMachine::new)
            .allRotation()
            .recipeTypes(GTORecipeTypes.HYPER_REACTOR_RECIPES)
            .tooltipsText("每次运行前提供额外的1mb等离子体将获得16的并行", "Providing additional 1mb plasma before each run will obtain 16 parallelism")
            .tooltipsText("不同燃料所需的等离子体不同", "Different fuels required different plasmas")
            .tooltipsText("从1-4顺序为: 山铜, 末影素, 魔金, 亚稳态\ud872\udf76", "The order from 1-4 is: Orichalcum, Enderium, Infuscolium, Metastable Hassium")
            .generator()
            .recipeModifier((machine, recipe) -> {
                if (machine instanceof ElectricMultiblockMachine workableElectricMultiblockMachine) {
                    int p = 1;
                    long outputEUt = recipe.getOutputEUt();
                    if (outputEUt == V[UEV]) {
                        if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == V[UIV]) {
                        if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == V[UXV]) {
                        if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == V[OpV]) {
                        if (MachineUtils.inputFluid(workableElectricMultiblockMachine, GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    }
                    return RecipeModifierFunction.generatorOverclocking(workableElectricMultiblockMachine, ParallelLogic.accurateParallel(machine, recipe, p));
                }
                return recipe;
            })
            .block(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "A                         A", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "ABB                     BBA", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "ABB                     BBA", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "A                         A", "ABB                     BBA", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "ABB                     BBA", "A                         A", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A   CC               CC   A", "A   CC               CC   A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A   CC               CC   A", "A   CC               CC   A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ", "                           ")
                    .aisle("                           ", "                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ", "                           ")
                    .aisle("                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      CDDDDDDDDDDDC      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      CDDDDDDDDDDDC      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ")
                    .aisle("                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      CAE       EAC      A", "A      C    aaa    C      A", "A      C    a~a    C      A", "A      C    aaa    C      A", "A      CAE       EAC      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ")
                    .aisle("A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C   CCCCC   C      A", "A      C  aa   aa  C      A", "A      CFFFaC CaFFFC      A", "A      C  aa   aa  C      A", "A      C   CCCCC   C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A")
                    .aisle("A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C  CGGGGGC  C      A", "A      CCC       CCC      A", "A      CC    H    CC      A", "A      CCC       CCC      A", "A      C  CGGGGGC  C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A")
                    .aisle("A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      CFFFGGGGGFFFC      A", "A      CCC   H   CCC      A", "A      II   HHH   II      A", "A      CCC   H   CCC      A", "A      CFFFGGGGGFFFC      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A")
                    .aisle("A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C  CGGGGGC  C      A", "A      CCC       CCC      A", "A      CC    H    CC      A", "A      CCC       CCC      A", "A      C  CGGGGGC  C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A")
                    .aisle("A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C   CCCCCC  C      A", "A      C  aC   Ca  C      A", "A      CFFFaC CaFFFC      A", "A      C  aa   aa  C      A", "A      C   CCCCC   C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A")
                    .aisle("                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      CAE       EAC      A", "A      C    aaa    C      A", "A      C    aaa    C      A", "A      C    aaa    C      A", "A      CAE       EAC      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ")
                    .aisle("                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A      CDDDDDDDDDDDC      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      CDDDDDDDDDDDC      A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ")
                    .aisle("                           ", "                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A   CC               CC   A", "A     CC           CC     A", "A     CC           CC     A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A      C           C      A", "A     CC           CC     A", "A     CC           CC     A", "A   CC               CC   A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ", "                           ")
                    .aisle("                           ", "                           ", "A                         A", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A   CC               CC   A", "A   CC               CC   A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A     CC           CC     A", "A   CC               CC   A", "A   CC               CC   A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "A                         A", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "A                         A", "ABB                     BBA", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A   CC               CC   A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "ABB                     BBA", "A                         A", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "ABB                     BBA", "ABB                     BBA", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "A  BB                 BB  A", "ABB                     BBA", "ABB                     BBA", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "ABB                     BBA", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ")
                    .aisle("                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "A                         A", "A                         A", "A                         A", "A                         A", "A                         A", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ", "                           ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where('B', blocks(GTOBlocks.PIKYONIUM_MACHINE_CASING.get()))
                    .where('C', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where('D', frames(GTMaterials.Neutronium))
                    .where('E', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('F', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('G', blocks(GTBlocks.FUSION_GLASS.get()))
                    .where('H', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('I', blocks(GTOBlocks.AMPROSIUM_PIPE_CASING.get()))
                    .where('a', blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(abilities(OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/enhance_hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition GENERATOR_ARRAY = multiblock("generator_array", "发电阵列", GeneratorArrayMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .generator()
            .addTooltipsFromClass(GeneratorArrayMachine.class)
            .tooltips(NewDataAttributes.RECIPES_TYPE.create(
                    Component.empty().append(Component.translatable("gtceu.steam_turbine"))
                            .append(", ").append(Component.translatable("gtceu.combustion_generator"))
                            .append(", ").append(Component.translatable("gtceu.gas_turbine"))
                            .append(", ").append(Component.translatable("gtceu.semi_fluid_generator"))
                            .append(", ").append(Component.translatable("gtceu.rocket_engine"))
                            .append(", ").append(Component.translatable("gtceu.naquadah_reactor"))))
            .block(GTBlocks.CASING_STEEL_SOLID)
            .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
            .shape(Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999))
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXX", "CCC", "XXX")
                    .aisle("XXX", "C#C", "XXX")
                    .aisle("XSX", "CCC", "XXX")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('X', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(4))
                            .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('#', air())
                    .build())
            .renderer(() -> new ArrayMachineRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/processing_array")))
            .register();

    public static final MultiblockMachineDefinition ANNIHILATE_GENERATOR = multiblock("annihilate_generator", "人造恒星", ElectricMultiblockMachine::new)
            .nonYAxisRotation()
            .langValue("Artificial Star")
            .recipeTypes(GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES)
            .generator()
            .block(GTBlocks.HIGH_POWER_CASING)
            .recipeModifier(RecipeModifierFunction.GENERATOR_OVERCLOCKING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle(AnnihilateGeneratorB.A_1)
                    .aisle(AnnihilateGeneratorB.A_2)
                    .aisle(AnnihilateGeneratorB.A_3)
                    .aisle(AnnihilateGeneratorB.A_4)
                    .aisle(AnnihilateGeneratorB.A_5)
                    .aisle(AnnihilateGeneratorB.A_6)
                    .aisle(AnnihilateGeneratorB.A_7)
                    .aisle(AnnihilateGeneratorB.A_8)
                    .aisle(AnnihilateGeneratorB.A_9)
                    .aisle(AnnihilateGeneratorB.A_10)
                    .aisle(AnnihilateGeneratorB.A_11)
                    .aisle(AnnihilateGeneratorB.A_12)
                    .aisle(AnnihilateGeneratorB.A_13)
                    .aisle(AnnihilateGeneratorB.A_14)
                    .aisle(AnnihilateGeneratorB.A_15)
                    .aisle(AnnihilateGeneratorB.A_16)
                    .aisle(AnnihilateGeneratorB.A_17)
                    .aisle(AnnihilateGeneratorB.A_18)
                    .aisle(AnnihilateGeneratorB.A_19)
                    .aisle(AnnihilateGeneratorB.A_20)
                    .aisle(AnnihilateGeneratorB.A_21)
                    .aisle(AnnihilateGeneratorB.A_22)
                    .aisle(AnnihilateGeneratorB.A_23)
                    .aisle(AnnihilateGeneratorB.A_24)
                    .aisle(AnnihilateGeneratorB.A_25)
                    .aisle(AnnihilateGeneratorB.A_26)
                    .aisle(AnnihilateGeneratorB.A_27)
                    .aisle(AnnihilateGeneratorB.A_28)
                    .aisle(AnnihilateGeneratorB.A_29)
                    .aisle(AnnihilateGeneratorB.A_30)
                    .aisle(AnnihilateGeneratorB.A_31)
                    .aisle(AnnihilateGeneratorB.A_32)
                    .aisle(AnnihilateGeneratorB.A_33)
                    .aisle(AnnihilateGeneratorB.A_34)
                    .aisle(AnnihilateGeneratorB.A_35)
                    .aisle(AnnihilateGeneratorB.A_36)
                    .aisle(AnnihilateGeneratorB.A_37)
                    .aisle(AnnihilateGeneratorB.A_38)
                    .aisle(AnnihilateGeneratorB.A_39)
                    .aisle(AnnihilateGeneratorB.A_40)
                    .aisle(AnnihilateGeneratorB.A_41)
                    .aisle(AnnihilateGeneratorB.A_42)
                    .aisle(AnnihilateGeneratorB.A_43)
                    .aisle(AnnihilateGeneratorB.A_44)
                    .aisle(AnnihilateGeneratorB.A_45)
                    .aisle(AnnihilateGeneratorB.A_46)
                    .aisle(AnnihilateGeneratorB.A_47)
                    .aisle(AnnihilateGeneratorB.A_48)
                    .aisle(AnnihilateGeneratorB.A_49)
                    .aisle(AnnihilateGeneratorB.A_50)
                    .aisle(AnnihilateGeneratorB.A_51)
                    .aisle(AnnihilateGeneratorB.A_52)
                    .aisle(AnnihilateGeneratorB.A_53)
                    .aisle(AnnihilateGeneratorA.A_54)
                    .aisle(AnnihilateGeneratorA.A_55)
                    .aisle(AnnihilateGeneratorA.A_56)
                    .aisle(AnnihilateGeneratorA.A_57)
                    .aisle(AnnihilateGeneratorA.A_58)
                    .aisle(AnnihilateGeneratorA.A_59)
                    .aisle(AnnihilateGeneratorA.A_60)
                    .aisle(AnnihilateGeneratorA.A_61)
                    .aisle(AnnihilateGeneratorA.A_62)
                    .aisle(AnnihilateGeneratorA.A_63)
                    .aisle(AnnihilateGeneratorA.A_64)
                    .aisle(AnnihilateGeneratorA.A_65)
                    .aisle(AnnihilateGeneratorA.A_66)
                    .aisle(AnnihilateGeneratorA.A_67)
                    .aisle(AnnihilateGeneratorA.A_68)
                    .aisle(AnnihilateGeneratorA.A_69)
                    .aisle(AnnihilateGeneratorA.A_70)
                    .aisle(AnnihilateGeneratorA.A_71)
                    .aisle(AnnihilateGeneratorA.A_72)
                    .aisle(AnnihilateGeneratorA.A_73)
                    .aisle(AnnihilateGeneratorA.A_74)
                    .aisle(AnnihilateGeneratorA.A_75)
                    .aisle(AnnihilateGeneratorA.A_76)
                    .aisle(AnnihilateGeneratorA.A_77)
                    .aisle(AnnihilateGeneratorA.A_78)
                    .aisle(AnnihilateGeneratorA.A_79)
                    .aisle(AnnihilateGeneratorA.A_80)
                    .aisle(AnnihilateGeneratorA.A_81)
                    .aisle(AnnihilateGeneratorA.A_82)
                    .aisle(AnnihilateGeneratorA.A_83)
                    .aisle(AnnihilateGeneratorA.A_84)
                    .aisle(AnnihilateGeneratorA.A_85)
                    .aisle(AnnihilateGeneratorA.A_86)
                    .aisle(AnnihilateGeneratorA.A_87)
                    .aisle(AnnihilateGeneratorA.A_88)
                    .aisle(AnnihilateGeneratorA.A_89)
                    .aisle(AnnihilateGeneratorA.A_90)
                    .aisle(AnnihilateGeneratorA.A_91)
                    .aisle(AnnihilateGeneratorA.A_92)
                    .aisle(AnnihilateGeneratorA.A_93)
                    .aisle(AnnihilateGeneratorA.A_94)
                    .aisle(AnnihilateGeneratorA.A_95)
                    .aisle(AnnihilateGeneratorA.A_96)
                    .aisle(AnnihilateGeneratorA.A_97)
                    .aisle(AnnihilateGeneratorA.A_98)
                    .aisle(AnnihilateGeneratorA.A_99)
                    .aisle(AnnihilateGeneratorA.A_100)
                    .aisle(AnnihilateGeneratorA.A_101)
                    .aisle(AnnihilateGeneratorA.A_102)
                    .aisle(AnnihilateGeneratorA.A_103)
                    .aisle(AnnihilateGeneratorA.A_104)
                    .aisle(AnnihilateGeneratorA.A_105)
                    .aisle(AnnihilateGeneratorA.A_106)
                    .aisle(AnnihilateGeneratorA.A_107)
                    .aisle(AnnihilateGeneratorA.A_108)
                    .aisle(AnnihilateGeneratorA.A_109)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.GRAVITON_FIELD_CONSTRAINT_CASING.get()))
                    .where('B', blocks(GTOBlocks.ANNIHILATE_CORE.get()))
                    .where('C', blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where('D', blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where('E', blocks(GTOBlocks.NAQUADAH_ALLOY_CASING.get()))
                    .where('F', blocks(GTOBlocks.ANTIMATTER_CONTAINMENT_CASING.get()))
                    .where('G', blocks(GTOBlocks.DYSON_CONTROL_TOROID.get()))
                    .where('H', blocks(GTOBlocks.RHENIUM_REINFORCED_ENERGY_GLASS.get()))
                    .where('P', blocks(GTOBlocks.DYSON_CONTROL_CASING.get()))
                    .where('S', blocks(GTBlocks.HIGH_POWER_CASING.get())
                            .or(abilities(OUTPUT_LASER))
                            .or(abilities(IMPORT_ITEMS))
                            .or(abilities(EXPORT_ITEMS)))
                    .where('T', blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where('R', blocks(GTOBlocks.DYSON_RECEIVER_CASING.get()))
                    .where(' ', any())
                    .build())
            .renderer(AnnihilateGeneratorRenderer::new)
            .hasTESR(true)
            .register();
}
