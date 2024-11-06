package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.client.renderer.machine.AnnihilateGeneratorRenderer;
import com.gto.gtocore.common.data.*;
import com.gto.gtocore.common.data.machines.structure.AnnihilateGeneratorA;
import com.gto.gtocore.common.data.machines.structure.AnnihilateGeneratorB;
import com.gto.gtocore.common.machine.multiblock.generator.*;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.*;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import com.tterrag.registrate.util.entry.BlockEntry;

import java.util.function.Supplier;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GeneratorMachine {

    public static void init() {}

    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_ENERGETIC = registerPhotovoltaicPowerStation("energetic", 1, GTBlocks.CASING_STEEL_SOLID, GTOBlocks.ENERGETIC_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_solid_steel"));
    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_PULSATING = registerPhotovoltaicPowerStation("pulsating", 4, GTBlocks.CASING_TITANIUM_STABLE, GTOBlocks.PULSATING_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_stable_titanium"));
    public static final MultiblockMachineDefinition PHOTOVOLTAIC_POWER_STATION_VIBRANT = registerPhotovoltaicPowerStation("vibrant", 16, GTBlocks.CASING_TUNGSTENSTEEL_ROBUST, GTOBlocks.VIBRANT_PHOTOVOLTAIC_BLOCK, GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"));

    private static MultiblockMachineDefinition registerPhotovoltaicPowerStation(String name, int basicRate, Supplier<? extends Block> casing, BlockEntry<?> photovoltaicBlock, ResourceLocation texture) {
        return REGISTRATE.multiblock(name + "_photovoltaic_power_station", holder -> new PhotovoltaicPowerStationMachine(holder, basicRate))
                .rotationState(RotationState.NON_Y_AXIS)
                .generator(true)
                .recipeType(GTORecipeTypes.PHOTOVOLTAIC_POWER_RECIPES)
                .tooltips(Component.translatable("gtocore.machine.photovoltaic_power_station.tooltip.0"))
                .tooltips(Component.translatable("gtocore.machine.photovoltaic_power_station.tooltip.1"))
                .recipeModifier(PhotovoltaicPowerStationMachine::recipeModifier)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("AAAAAAA")
                        .aisle("ABBBBBA")
                        .aisle("ABBBBBA")
                        .aisle("ABBBBBA")
                        .aisle("ABBBBBA")
                        .aisle("ABBBBBA")
                        .aisle("AAA~AAA")
                        .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                        .where("A", Predicates.blocks(casing.get())
                                .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                                .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setExactLimit(1))
                                .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(2)))
                        .where("B", Predicates.blocks(photovoltaicBlock.get()))
                        .build())
                .workableCasingRenderer(texture, GTCEu.id("block/multiblock/generator/large_steam_turbine"))
                .register();
    }

    public static final MultiblockMachineDefinition LARGE_SEMI_FLUID_GENERATOR = GTOMachines.registerLargeCombustionEngine(REGISTRATE,
            "large_semi_fluid_generator", GTValues.EV, GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS,
            GTBlocks.CASING_TITANIUM_STABLE, GTBlocks.CASING_STEEL_GEARBOX, GTBlocks.CASING_ENGINE_INTAKE,
            GTCEu.id("block/casings/solid/machine_casing_stable_titanium"),
            GTCEu.id("block/multiblock/generator/large_combustion_engine"), false);

    public final static MultiblockMachineDefinition CHEMICAL_ENERGY_DEVOURER = REGISTRATE
            .multiblock("chemical_energy_devourer", ChemicalEnergyDevourerMachine::new)
            .rotationState(RotationState.ALL)
            .recipeTypes(GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS,
                    GTRecipeTypes.GAS_TURBINE_FUELS, GTORecipeTypes.ROCKET_ENGINE_FUELS)
            .generator(true)
            .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", GTValues.V[GTValues.ZPM]),
                    Component.translatable("gtceu.universal.tooltip.uses_per_hour_lubricant", 10000),
                    Component.translatable("gtocore.machine.chemical_energy_devourer.tooltip.0", GTValues.V[GTValues.UV]),
                    Component.translatable("gtocore.machine.chemical_energy_devourer.tooltip.1", GTValues.V[GTValues.UHV]))
            .recipeModifier(ChemicalEnergyDevourerMachine::recipeModifier, true)
            .appearanceBlock(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BBBBBBB", "BBBBBBB", "BBPBPBB", "BBBBBBB", "BBPBPBB", "BBBBBBB", "BBBBBBB")
                    .aisle("BBBBBBB", "BDDDDDB", "BEHHHEB", "BGHEHGB", "BEHHHEB", "BDDDDDB", "BBBBBBB")
                    .aisle("BBBBBBB", "BDDDDDB", "BEHHHEB", "CGHEHGC", "BEHHHEB", "BDDDDDB", "BBBBBBB")
                    .aisle("BBBBBBB", "FDDDDDF", "BEHHHEB", "CGHEHGC", "BEHHHEB", "FDDDDDF", "BBIBIBB")
                    .aisle("BBBBBBB", "FDDDDDF", "BEHHHEB", "CGHEHGC", "BEHHHEB", "FDDDDDF", "BBIBIBB")
                    .aisle("BBBBBBB", "FDDDDDF", "BEHHHEB", "CGHEHGC", "BEHHHEB", "FDDDDDF", "BBIBIBB")
                    .aisle("BBBBBBB", "FDDDDDF", "BEHHHEB", "CGHEHGC", "BEHHHEB", "FDDDDDF", "BBIBIBB")
                    .aisle("BBBBBBB", "BDDDDDB", "BEHHHEB", "CGHEHGC", "BEHHHEB", "BDDDDDB", "BBIBIBB")
                    .aisle("BBBBBBB", "BDDDDDB", "BEHHHEB", "BGHEHGB", "BEHHHEB", "BDDDDDB", "BBBBBBB")
                    .aisle("AAAAAAA", "AAAAAAA", "AABBBAA", "AABSBAA", "AABBBAA", "AAAAAAA", "AAAAAAA")
                    .where("S", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GTBlocks.CASING_EXTREME_ENGINE_INTAKE.get()))
                    .where("B", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()))
                    .where("F", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get())
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(4)))
                    .where("C", Predicates.blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where("G", Predicates.blocks(GCyMBlocks.ELECTROLYTIC_CELL.get()))
                    .where("D", Predicates.blocks(GTBlocks.FIREBOX_TITANIUM.get()))
                    .where("E", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where("H", Predicates.blocks(GTBlocks.CASING_TITANIUM_GEARBOX.get()))
                    .where("P", Predicates.abilities(PartAbility.OUTPUT_ENERGY))
                    .where("I", Predicates.abilities(PartAbility.MUFFLER))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTCEu.id("block/multiblock/generator/extreme_combustion_engine"), false)
            .register();

    public static MultiblockMachineDefinition registerMegaTurbine(String name, int tier, boolean special, GTRecipeType recipeType,
                                                                  Supplier<Block> casing, Supplier<Block> gear, ResourceLocation baseCasing,
                                                                  ResourceLocation overlayModel) {
        return REGISTRATE.multiblock(name, holder -> new TurbineMachine(holder, tier, special, true))
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .tooltips(Component.translatable("gtocore.machine.mega_turbine.tooltip.0"))
                .tooltips(Component.translatable("gtocore.machine.mega_turbine.tooltip.1"))
                .tooltips(Component.translatable("gtocore.machine.mega_turbine.tooltip.2"))
                .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", GTValues.V[tier] * (special ? 12 : 8)))
                .tooltips(Component.translatable("gtceu.multiblock.turbine.efficiency_tooltip", GTValues.VNF[tier]))
                .recipeModifier(TurbineMachine::recipeModifier)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("AAAAAAA", "AAAAAAA", "AAEAEAA", "AAAAAAA", "AAEAEAA", "AAAAAAA", "AAAAAAA")
                        .aisle("AAAAAAA", "BDDDDDB", "AAAAAAA", "AAAAAAA", "AAAAAAA", "BDDDDDB", "AAAAAAA")
                        .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA")
                        .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA")
                        .aisle("AAAAAAA", "BDDDDDB", "AAAAAAA", "AAAAAAA", "AAAAAAA", "BDDDDDB", "AAAMAAA")
                        .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA")
                        .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA")
                        .aisle("AAAAAAA", "BDDDDDB", "AAAAAAA", "AAAAAAA", "AAAAAAA", "BDDDDDB", "AAAAAAA")
                        .aisle("AAAAAAA", "AAAAAAA", "AAACAAA", "AACSCAA", "AAACAAA", "AAAAAAA", "AAAAAAA")
                        .where("S", Predicates.controller(Predicates.blocks(definition.get())))
                        .where("A", Predicates.blocks(casing.get())
                                .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                                .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(8))
                                .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(2)))
                        .where("B", GTOPredicates.RotorBlock(tier))
                        .where("C", Predicates.blocks(casing.get()).or(Predicates.blocks(GTOMachines.ROTOR_HATCH.getBlock()).setMaxGlobalLimited(1)))
                        .where("D", Predicates.blocks(gear.get()))
                        .where("E", Predicates.abilities(PartAbility.OUTPUT_ENERGY).or(Predicates.abilities(PartAbility.SUBSTATION_OUTPUT_ENERGY)))
                        .where("M", Predicates.abilities(PartAbility.MUFFLER))
                        .build())
                .workableCasingRenderer(baseCasing, overlayModel)
                .register();
    }

    public final static MultiblockMachineDefinition ROCKET_LARGE_TURBINE = GTOMachines.registerLargeTurbine(REGISTRATE,
            "rocket_large_turbine", GTValues.EV, true,
            GTORecipeTypes.ROCKET_ENGINE_FUELS,
            GTBlocks.CASING_TITANIUM_TURBINE, GTBlocks.CASING_TITANIUM_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_titanium"),
            GTCEu.id("block/multiblock/generator/large_gas_turbine"), false);

    public final static MultiblockMachineDefinition SUPERCRITICAL_STEAM_TURBINE = GTOMachines.registerLargeTurbine(REGISTRATE,
            "supercritical_steam_turbine", GTValues.IV, true,
            GTORecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS,
            GTOBlocks.CASING_SUPERCRITICAL_TURBINE, GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX,
            GTOCore.id("block/supercritical_turbine_casing"),
            GTCEu.id("block/multiblock/generator/large_plasma_turbine"), false);

    public final static MultiblockMachineDefinition STEAM_MEGA_TURBINE = registerMegaTurbine("steam_mega_turbine", GTValues.EV, false, GTRecipeTypes.STEAM_TURBINE_FUELS, GTBlocks.CASING_STEEL_TURBINE, GTBlocks.CASING_STEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_steel"), GTCEu.id("block/multiblock/generator/large_steam_turbine"));
    public final static MultiblockMachineDefinition GAS_MEGA_TURBINE = registerMegaTurbine("gas_mega_turbine", GTValues.IV, false, GTRecipeTypes.GAS_TURBINE_FUELS, GTBlocks.CASING_STAINLESS_TURBINE, GTBlocks.CASING_STAINLESS_STEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_stainless_steel"), GTCEu.id("block/multiblock/generator/large_gas_turbine"));
    public final static MultiblockMachineDefinition ROCKET_MEGA_TURBINE = registerMegaTurbine("rocket_mega_turbine", GTValues.IV, true, GTORecipeTypes.ROCKET_ENGINE_FUELS, GTBlocks.CASING_TITANIUM_TURBINE, GTBlocks.CASING_STAINLESS_STEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_titanium"), GTCEu.id("block/multiblock/generator/large_gas_turbine"));
    public final static MultiblockMachineDefinition PLASMA_MEGA_TURBINE = registerMegaTurbine("plasma_mega_turbine", GTValues.LuV, false, GTRecipeTypes.PLASMA_GENERATOR_FUELS, GTBlocks.CASING_TUNGSTENSTEEL_TURBINE, GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX,
            GTCEu.id("block/casings/mechanic/machine_casing_turbine_tungstensteel"), GTCEu.id("block/multiblock/generator/large_plasma_turbine"));
    public final static MultiblockMachineDefinition SUPERCRITICAL_MEGA_STEAM_TURBINE = registerMegaTurbine("supercritical_mega_steam_turbine", GTValues.LuV, true, GTORecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS, GTOBlocks.CASING_SUPERCRITICAL_TURBINE, GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX,
            GTOCore.id("block/supercritical_turbine_casing"), GTCEu.id("block/multiblock/generator/large_plasma_turbine"));

    public final static MultiblockMachineDefinition DYSON_SPHERE = REGISTRATE.multiblock("dyson_sphere", DysonSphereMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .allowExtendedFacing(false)
            .recipeType(GTORecipeTypes.DYSON_SPHERE_RECIPES)
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.0"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.1"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.2"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.3"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.4"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.5"))
            .tooltips(Component.translatable("gtocore.machine.dyson_sphere.tooltip.6"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.dyson_sphere")))
            .recipeModifier((machine, recipe, params, result) -> DysonSphereMachine.recipeModifier(machine, recipe))
            .appearanceBlock(GTOBlocks.DYSON_RECEIVER_CASING)
            .pattern((definition) -> FactoryBlockPattern.start()
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B            B     JJJJJJJJJ   ", "B            B     JJJJJJJJJ   ", "B            B     JJJJJJJJJ   ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B CCCCCCCCCC B     JJJJJJJJJ   ", "B CCCCCCCCCC B     JJJJJJJJJ   ", "B CCCCCCCCCC B     JJJNNNJJJ   ", "BBBBBBBBBBBBBB        NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                      NNN      ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C        C B     JJJJJJJJJ   ", "B C        C B     JJJPQPJJJ   ", "B C        C B     JJNPQPNJJ   ", "BBBBBBBBBBBBBB       NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "                      RPR      ", "                      RPR      ", "                      RPR      ", "                      RPR      ", "                      RPR      ", "                      RPR      ", "                      RRR      ", "                       R       ", "                       R       ", "                       R       ", "                       R       ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C        C B     JJJJJJJJJ   ", "B C        C B     JJJQTQJJJ   ", "B C        C B     JJNQ QNJJ   ", "BBBBBBBBBBBBBB       NQ QN     ", "    DDDDD            NQ QN     ", "                     NQ QN     ", "                     NQ QN     ", "    EEEEE            NQ QN     ", "                     NQ QN     ", "    EEEEE            NQ QN     ", "                     NQ QN     ", "    EEEEE            NQ QN     ", "                     NQ QN     ", "    EEEEE            NQ QN     ", "                      P P      ", "                      P P      ", "                      P P      ", "    EEEEE             P P      ", "    EEEEE             P P      ", "    EEEEE             P P      ", "    EEEEE             R R      ", "                      R R      ", "                      R R      ", "                      R R      ", "                      R R      ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C FFFFF  C B     JJJJJJJJJ   ", "B C FFFFF  C B     JJJPQPJJJ   ", "B C FFFFF  C B     JJNPQPNJJ   ", "BBBBBBBBBBBBBB       NPQPN     ", "   DDDDDDD           NPQPN     ", "                     NPQPN     ", "                     NPQPN     ", "   E  D  E           NPQPN     ", "                     NPQPN     ", "   E  D  E           NPQPN     ", "                     NPQPN     ", "   E  D  E           NPQPN     ", "                     NPQPN     ", "   E  D  E           NPQPN     ", "                      RPR      ", "                      RPR      ", "    EEEEE             RPR      ", "   EEEEEEE            RPR      ", "   EEEEEEE            RPR      ", "   EEEEEEE            RPR      ", "   EEEEEEE            RRR      ", "    EEEEE              R       ", "                       R       ", "                       R       ", "                       R       ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C F   F  C B     JJJJJJJJJ   ", "B C F   F  C B     JJJJJJJJJ   ", "B C F   F  C B     JJJNNNJJJ   ", "BBBBBDDDBBBBBB        NNN      ", "   DD D DD            NNN      ", "      D               NNN      ", "      D               NNN      ", "   E DDD E            NNN      ", "      D               NNN      ", "   E DDD E            NNN      ", "      D               NNN      ", "   E DDD E            NNN      ", "      D               NNN      ", "   E DDD E            NNN      ", "      D                        ", "      D                        ", "    EEDEE                      ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "    EEEEE                      ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C F G F  C B     JJJJJJJJJ   ", "B C F G F  C B     JJJJJJJJJ   ", "B C F G F  C B     JJJJJJJJJ   ", "BBBBBDGDBBBBBB                 ", "   DDDGDDD                     ", "     DGD                       ", "     DGD                       ", "   EDDGDDE                     ", "     DGD                       ", "   EDDGDDE                     ", "     DGD                       ", "   EDDGDDE                     ", "     DGD                       ", "   EDDGDDE                     ", "     DGD                       ", "     DGD                       ", "    EDGDE                      ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "    EEEEE                      ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB     JJJJJJJJJ   ", "B C F   F  C B     JJJJJJJJJ   ", "B C F   F  C B     JJJJJJJJJ   ", "B C F   F  C B     JJJJJJJJJ   ", "BBBBBDDDBBBBBB                 ", "   DD D DD                     ", "      D                        ", "      D                        ", "   E DDD E                     ", "      D                        ", "   E DDD E                     ", "      D                        ", "   E DDD E                     ", "      D                        ", "   E DDD E                     ", "      D                        ", "      D                        ", "    EEDEE                      ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "    EEEEE                      ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "B C FFFFF  C B                 ", "B C FFFFF  C B                 ", "B C FFFFF  C B                 ", "BBBBBBBBBBBBBB                 ", "   DDDDDDD                     ", "                               ", "                               ", "   E  D  E                     ", "                               ", "   E  D  E                     ", "                               ", "   E  D  E                     ", "                               ", "   E  D  E                     ", "                               ", "                               ", "    EEEEE                      ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "   EEEEEEE                     ", "    EEEEE                      ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "B C        C B                 ", "B C        C B                 ", "B C        C B                 ", "BBBBBBBBBBBBBB                 ", "    DDDDD                      ", "                               ", "                               ", "    EEEEE                      ", "                               ", "    EEEEE                      ", "                               ", "    EEEEE                      ", "                               ", "    EEEEE                      ", "                               ", "                               ", "                               ", "    EEEEE                      ", "    EEEEE                      ", "    EEEEE                      ", "    EEEEE                      ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "B C        C B                 ", "B C        C B                 ", "B C        C B                 ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "B CCCCCCCCCC B                 ", "B CCCCCCCCCC B                 ", "B CCCCCCCCCC B                 ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "B            B                 ", "B            B                 ", "B            B                 ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBB                 ", "BBBBBBBBBBBBBB                 ", "BBBBBBBBBBBBBB                 ", "BBBBBBBBBBBBBB                 ", "BBBBBBBBBBBBBB                 ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                  HHHHHHHHH    ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                   HHHHHHH     ", "                 HHHHHHHHHHH   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                    HHHHH      ", "                  HHHHHHHHH    ", "                HH         HH  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                    HHHHH      ", "                   HHHHHHH     ", "                 HH       HH   ", "               HH           HH ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                               ", "                               ", "                               ", "                               ", "                               ", "                   HHHHHHH     ", "                  HH     HH    ", "                HH         HH  ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IKKKKKKKI    ", "                  IKKKKKKKI    ", "                  IKKKKKKKI    ", "                  IIIIIIIII    ", "                    L   L      ", "                    L   L      ", "                    L   L      ", "                    L   L      ", "                    LHHHL      ", "                  HHHHHHHHH    ", "                 HH       HH   ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IK     KI    ", "                  IK     KI    ", "                  IK     KI    ", "                  IIIIIIIII    ", "                   L     L     ", "                   L     L     ", "                   L     L     ", "                   L     L     ", "                   LHHHHHL     ", "                 HHHH   HHHH   ", "                HH         HH  ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IK  M  KI    ", "                  IK  M  KI    ", "                  IK  M  KI    ", "                  IIIIIIIII    ", "                     L L       ", "                     L L       ", "                     L L       ", "                     L L       ", "                   HHHHHHH     ", "                 HHH     HHH   ", "                HH         HH  ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IK MIM KI    ", "                  IK MIM KI    ", "                  IK MIM KI    ", "                  IIIIIIIII    ", "                      I        ", "                      I        ", "                      I        ", "                      I        ", "                   HHHIHHH     ", "                 HHH  I  HHH   ", "                HH    I    HH  ", "               HH     I     HH ", "              HH      I      HH", "                      I        ", "                      S        ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IK  M  KI    ", "                  IK  M  KI    ", "                  IK  M  KI    ", "                  IIIIIIIII    ", "                     L L       ", "                     L L       ", "                     L L       ", "                     L L       ", "                   HHHHHHH     ", "                 HHH     HHH   ", "                HH         HH  ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IK     KI    ", "                  IK     KI    ", "                  IK     KI    ", "                  IIIIIIIII    ", "                   L     L     ", "                   L     L     ", "                   L     L     ", "                   L     L     ", "                   LHHHHHL     ", "                 HHHH   HHHH   ", "                HH         HH  ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIIIIII    ", "                  IKKKKKKKI    ", "                  IKKKKKKKI    ", "                  IKKKKKKKI    ", "                  IIIIIIIII    ", "                    L   L      ", "                    L   L      ", "                    L   L      ", "                    L   L      ", "                    LHHHL      ", "                  HHHHHHHHH    ", "                 HH       HH   ", "               HH           HH ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                  IIIIOIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                  IIIIIIIII    ", "                               ", "                               ", "                               ", "                               ", "                               ", "                   HHHHHHH     ", "                  HH     HH    ", "                HH         HH  ", "              HH             HH", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                    HHHHH      ", "                   HHHHHHH     ", "                 HH       HH   ", "               HH           HH ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                    HHHHH      ", "                  HHHHHHHHH    ", "                HH         HH  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                   HHHHHHH     ", "                 HHHHHHHHHHH   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .aisle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                  HHHHHHHHH    ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ")
                    .where("O", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("I", Predicates.blocks(GTOBlocks.DYSON_RECEIVER_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("A", Predicates.blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                    .where("B", Predicates.blocks(GTOBlocks.DYSON_CONTROL_CASING.get()))
                    .where("C", Predicates.blocks(GCyMBlocks.HEAT_VENT.get()))
                    .where("D", Predicates.blocks(GTBlocks.CASING_PALLADIUM_SUBSTATION.get()))
                    .where("E", Predicates.blocks(GTOBlocks.DYSON_CONTROL_TOROID.get()))
                    .where("F", Predicates.blocks(GTOBlocks.SPACETIME_ASSEMBLY_LINE_UNIT.get()))
                    .where("G", Predicates.blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where("H", Predicates.blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where("J", Predicates.blocks(GTOBlocks.DYSON_DEPLOYMENT_CASING.get()))
                    .where("K", Predicates.blocks(GTOBlocks.COIL_ADAMANTINE.get()))
                    .where("L", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Adamantine)))
                    .where("M", Predicates.blocks(GTOBlocks.HYPER_CORE.get()))
                    .where("N", Predicates.blocks(GTOBlocks.ECHO_CASING.get()))
                    .where("P", Predicates.blocks(GTOBlocks.DYSON_DEPLOYMENT_MAGNET.get()))
                    .where("Q", Predicates.blocks(GTOBlocks.POWER_MODULE_5.get()))
                    .where("R", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Vibranium)))
                    .where("S", Predicates.blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where("T", Predicates.blocks(GTOBlocks.DYSON_DEPLOYMENT_CORE.get()))
                    .where(" ", Predicates.any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/dyson_receiver_casing/top"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public final static MultiblockMachineDefinition LARGE_NAQUADAH_REACTOR = REGISTRATE.multiblock("large_naquadah_reactor", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .allowExtendedFacing(false)
            .recipeType(GTORecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES)
            .tooltips(Component.translatable("gtceu.machine.perfect_oc"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.large_naquadah_reactor")))
            .generator(true)
            .recipeModifier((machine, recipe, params, result) -> GTORecipeModifiers.standardOverclocking((WorkableElectricMultiblockMachine) machine, recipe))
            .appearanceBlock(GTOBlocks.HYPER_MECHANICAL_CASING)
            .pattern((definition) -> FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           a~a           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           AAA           ", "          aBBBa          ", "           AAA           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "          CCCCC          ", "        CC     CC        ", "      CC         CC      ", "    CC             CC    ", "    C               C    ", "   C                 C   ", "   C                 C   ", "  C                   C  ", "  C                   C  ", " C         AAA         C ", " C        A   A        C ", " C       aB   Ba       C ", " C        A   A        C ", " C         AAA         C ", "  C                   C  ", "  C                   C  ", "   C                 C   ", "   C                 C   ", "    C               C    ", "    CC             CC    ", "      CC         CC      ", "        CC     CC        ", "          CCCCC          ", "                         ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  CCCCC  CC      ", "    CC  DD  C  DD  CC    ", "   C  DD    C    DD  C   ", "   C D             D C   ", "  C D               D C  ", "  C D               D C  ", " C D        A        D C ", " C D        A        D C ", "C C       AAAAA       C C", "C C      A E E A      C C", "C CCC   aB E E Ba   CCC C", "C C      A E E A      C C", "C C       AAAAA       C C", " C D        A        D C ", " C D        A        D C ", "  C D               D C  ", "  C D               D C  ", "   C D             D C   ", "   C  DD    C    DD  C   ", "    CC  DD  C  DD  CC    ", "      CC  CCCCC  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  DDFDD CCC      ", "    CC  DDGCFCGDD  CC    ", "   C  DD  GCFCG  DD  C   ", "   C D    G F G    D C   ", "  C D       F       D C  ", "  C D       F       D C  ", " C D       A A       D C ", " C D       A A       D C ", "C DGGG    AA AA    GGGD C", "C DCC    A     A    CCD C", "C DAAAAAAB     BAAAAAAD C", "C DCC    A     A    CCD C", "C DGGG    AA AA    GGGD C", " C D       A A       D C ", " C D       A A       D C ", "  C D       F       D C  ", "  C D       F       D C  ", "   C D    G F G    D C   ", "   CC DD  GCFCG  DD  C   ", "    CC  DDGCFCGDD  CC    ", "      CC  DDFDD  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("          CCCCC          ", "        CC     CC        ", "      CC  CCCCC  CC      ", "    CC  DD  C  DD  CC    ", "   C  DD    C    DD  C   ", "   C D             D C   ", "  C D               D C  ", "  C D               D C  ", " C D        A        D C ", " C D        A        D C ", "C C       AAAAA       C C", "C C      A E E A      C C", "C CCC   aB E E Ba   CCC C", "C C      A E E A      C C", "C C       AAAAA       C C", " C D        A        D C ", " C D        A        D C ", "  C D               D C  ", "  C D               D C  ", "   C D             D C   ", "   CC DD    C    DD  C   ", "    CC  DD  C  DD  CC    ", "      CC  CCCCC  CC      ", "        CC     CC        ", "          CCCCC          ")
                    .aisle("                         ", "          CCCCC          ", "        CC     CC        ", "      CC         CC      ", "    CC             CC    ", "    C               C    ", "   C                 C   ", "   C                 C   ", "  C                   C  ", "  C                   C  ", " C         AAA         C ", " C        A   A        C ", " C       aB   Ba       C ", " C        A   A        C ", " C         AAA         C ", "  C                   C  ", "  C                   C  ", "   C                 C   ", "   C                 C   ", "    C               C    ", "    CC             CC    ", "      CC         CC      ", "        CC     CC        ", "          CCCCC          ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           AAA           ", "          aBBBa          ", "           AAA           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .aisle("                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "           aaa           ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ", "                         ")
                    .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where("B", Predicates.blocks(GTOBlocks.NEUTRONIUM_GEARBOX.get()))
                    .where("C", Predicates.blocks(GTOBlocks.EXTREME_STRENGTH_TRITANIUM_CASING.get()))
                    .where("D", Predicates.blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where("E", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria)))
                    .where("F", Predicates.blocks(GTOBlocks.NEUTRONIUM_PIPE_CASING.get()))
                    .where("G", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Trinium)))
                    .where("a", Predicates.blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(" ", Predicates.any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public final static MultiblockMachineDefinition ADVANCED_HYPER_REACTOR = REGISTRATE.multiblock("advanced_hyper_reactor", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .allowExtendedFacing(false)
            .recipeType(GTORecipeTypes.ADVANCED_HYPER_REACTOR_RECIPES)
            .tooltips(Component.translatable("gtocore.machine.advanced_hyper_reactor.tooltip.0"))
            .tooltips(Component.translatable("gtocore.machine.advanced_hyper_reactor.tooltip.1"))
            .tooltips(Component.translatable("gtceu.machine.perfect_oc"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.advanced_hyper_reactor")))
            .generator(true)
            .recipeModifier((machine, recipe, params, result) -> {
                if (machine instanceof WorkableElectricMultiblockMachine workableElectricMultiblockMachine) {
                    int p = 1;
                    if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1))) {
                        p = 8;
                    }
                    if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1))) {
                        p = 16;
                    }
                    return GTORecipeModifiers.standardOverclocking(workableElectricMultiblockMachine, GTRecipeModifiers.fastParallel(machine, recipe, p, false).getFirst());
                }
                return recipe;
            })
            .appearanceBlock(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING)
            .pattern((definition) -> FactoryBlockPattern.start()
                    .aisle("               aaaaa               ", "               aaaaa               ", "               aa~aa               ", "                aaa                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("               BAAAB               ", "               BAAAB               ", "               BAAAB               ", "                AAA                ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("               ABBBA               ", "               ACCCA               ", "               A   A               ", "               A   A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                AAA                ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("             AAABBBAAA             ", "               ACBCA               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A   A               ", "               A   A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("           AAABABBBABAAA           ", "              BACCCAB              ", "             AAA C AAA             ", "               A C A               ", "               A C A               ", "               A C A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("         AAABBDDDDDDDBBAAA         ", "            BBDDDDDDDBB            ", "           AAA  AAA  AAA           ", "                AAA                ", "                AAA                ", "                AAA                ", "               A C A               ", "               A C A               ", "               A C A               ", "               A C A               ", "               A B A               ", "               A B A               ", "                ABA                ", "                 A                 ", "                 A                 ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("        AABBDDBBBBBBBDDBBAA        ", "          BBDDEEEEEEEDDBB          ", "         AAA           AAA         ", "                                   ", "                                   ", "                                   ", "                AAA                ", "                AAA                ", "                AAA                ", "            AAAAAAAAAAA            ", "               A C A               ", "               A C A               ", "                ACA                ", "                ABA                ", "                ABA                ", "                 A                 ", "                 F                 ", "                                   ", "                                   ")
                    .aisle("       AABDDBBBBBBBBBBBDDBAA       ", "         BDDEEEEEEEEEEEDDB         ", "        AA               AA        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "           AA         AA           ", "                AAA                ", "                AAA                ", "                 A                 ", "                 B                 ", "                BBB                ", "                 B                 ", "                                   ", "                                   ", "                                   ")
                    .aisle("      AABDBBBBBBBBBBBBBBBDBAA      ", "        BDEEEEEEEEEEEEEEEDB        ", "       AA                 AA       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "           G           G           ", "         AAF           FAA         ", "           G           G           ", "                                   ", "                                   ", "                                   ", "                 B                 ", "                BBB                ", "                 B                 ", "                                   ", "                                   ")
                    .aisle("     AABDBBBBBBBBBBBBBBBBBDBAA     ", "       BDEEEEEEEEEEEEEEEEEDB       ", "      AA                   AA      ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        AA               AA        ", "                                   ", "                                   ", "                                   ", "                                   ", "                 C                 ", "                 B                 ", "               CBBBC               ", "                 B                 ", "                 C                 ")
                    .aisle("    AABDBBBBBBBBBBBBBBBBBBBDBAA    ", "      BDEEEEEEEEEEEEEEEEEEEDB      ", "     AA                     AA     ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       AA                 AA       ", "                                   ", "                                   ", "                                   ", "                                   ", "                 G                 ", "                                   ", "               C   C               ", "                                   ", "                 C                 ")
                    .aisle("    ABDBBBBBBBBBBBBBBBBBBBBBDBA    ", "     BDEEEEEEEEEEEEEEEEEEEEEDB     ", "     A                       A     ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       A                   A       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               G   G               ", "                                   ", "                 C                 ")
                    .aisle("   AABDBBBBBBBBBBBBBBBBBBBBBDBAA   ", "     BDEEEEEEEEEEEEEEEEEEEEEDB     ", "    AA                       AA    ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       G                   G       ", "      AF                   FA      ", "       G                   G       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                 G                 ")
                    .aisle("   ABDBBBBBBBBBBBBBBBBBBBBBBBDBA   ", "    BDEEEEEEEEEEEEEEEEEEEEEEEDB    ", "    A                         A    ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                HHH                ", "     AA        HHHHH        AA     ", "               HHHHH               ", "               HHHHH               ", "                HHH                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("  AABDBBBBBBBBBBBBBBBBBBBBBBBDBAA  ", "    BDEEEEEEEEEEEEEEEEEEEEEEEDB    ", "   AA                         AA   ", "                                   ", "                                   ", "                                   ", "                                   ", "               HHHHH               ", "              HH   HH              ", "     A        H     H        A     ", "              H     H              ", "              H     H              ", "              HH   HH              ", "               HHHHH               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("  ABDBBBBBBBBBBBBBBBBBBBBBBBBBDBA  ", "   BDEEEEEEEEEEEEEEEEEEEEEEEEEDB   ", "   A                           A   ", "                                   ", "                                   ", "                                   ", "               HHHHH               ", "              H     H              ", "             H       H             ", "     A       H       H       A     ", "             H       H             ", "             H       H             ", "             H       H             ", "              H     H              ", "               HHHHH               ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle(" AAADBBBBBBBBBBBBBBBBBBBBBBBBBDAAA ", " AAADEEEEEEEEEEEEEEEEEEEEEEEEEDAAA ", " AAA                           AAA ", " AAA                           AAA ", " AAA                           AAA ", " AAA            HHH            AAA ", "  AAA         HH   HH         AAA  ", "  AAA        H       H        AAA  ", "  AAA        H       H        AAA  ", "  AAAA      H         H      AAAA  ", "   AAA      H         H      AAA   ", "   AAA      H         H      AAA   ", "             H       H             ", "             H       H             ", "              HH   HH              ", "                HHH                ", "        CCG             GCC        ", "                                   ", "                                   ")
                    .aisle("ABBBDBBBBBBBBBBBBBBBBBBBBBBBBBDBBBA", "ACCCDEEEEEEEEEEEEEEEEEEEEEEEEEDCCCA", "A   A                         A   A", "A   A                         A   A", "A   A                         A   A", "A   A          HHHHH          A   A", " A   A        H     H        A   A ", " A   A       H       H       A   A ", " A   A      H         H      A   A ", " A   A      H         H      A   A ", "  A   A     H         H     A   A  ", "  A   A     H         H     A   A  ", "   AAA      H         H      AAA   ", "     A       H       H       A     ", "     AB       H     H       BA     ", "       B       HHHHH       B       ", "        B                 B        ", "                                   ", "                                   ")
                    .aisle("ABBBDBBBBBBBBBBBBBBBBBBBBBBBBBDBBBA", "ACBCDEEEEEEEEEEEEEEEEEEEEEEEEEDCBCA", "A BCA                         ACB A", "A BCA                         ACB A", "A BCA                         ACB A", "A BCA          HHHHH          ACB A", " A BCA        H     H        ACB A ", " A BCA       H       H       ACB A ", " A BCA      H         H      ACB A ", " A BCA      H         H      ACB A ", "  A BCA     H         H     ACB A  ", "  A BCA     H         H     ACB A  ", "   ABCA     H         H     ACBA   ", "    ABB      H       H      BBA    ", "    ABBBCG    H     H    GCBBBA    ", "     ABBB      HHHHH      BBBA     ", "     F BB                 BB F     ", "        B                 B        ", "        CCCG           GCCC        ")
                    .aisle("ABBBDBBBBBBBBBBBBBBBBBBBBBBBBBDBBBA", "ACCCDEEEEEEEEEEEEEEEEEEEEEEEEEDCCCA", "A   A                         A   A", "A   A                         A   A", "A   A                         A   A", "A   A          HHHHH          A   A", " A   A        H     H        A   A ", " A   A       H       H       A   A ", " A   A      H         H      A   A ", " A   A      H         H      A   A ", "  A   A     H         H     A   A  ", "  A   A     H         H     A   A  ", "   AAA      H         H      AAA   ", "     A       H       H       A     ", "     AB       H     H       BA     ", "       B       HHHHH       B       ", "        B                 B        ", "                                   ", "                                   ")
                    .aisle(" AAADBBBBBBBBBBBBBBBBBBBBBBBBBDAAA ", " AAADEEEEEEEEEEEEEEEEEEEEEEEEEDAAA ", " AAA                           AAA ", " AAA                           AAA ", " AAA                           AAA ", " AAA            HHH            AAA ", "  AAA         HH   HH         AAA  ", "  AAA        H       H        AAA  ", "  AAA        H       H        AAA  ", "  AAAA      H         H      AAAA  ", "   AAA      H         H      AAA   ", "   AAA      H         H      AAA   ", "             H       H             ", "             H       H             ", "              HH   HH              ", "                HHH                ", "        CCG             GCC        ", "                                   ", "                                   ")
                    .aisle("  ABDBBBBBBBBBBBBBBBBBBBBBBBBBDBA  ", "   BDEEEEEEEEEEEEEEEEEEEEEEEEEDB   ", "   A                           A   ", "                                   ", "                                   ", "                                   ", "               HHHHH               ", "              H     H              ", "             H       H             ", "     A       H       H       A     ", "             H       H             ", "             H       H             ", "             H       H             ", "              H     H              ", "               HHHHH               ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("  AABDBBBBBBBBBBBBBBBBBBBBBBBDBAA  ", "    BDEEEEEEEEEEEEEEEEEEEEEEEDB    ", "   AA                         AA   ", "                                   ", "                                   ", "                                   ", "                                   ", "               HHHHH               ", "              HH   HH              ", "     A        H     H        A     ", "              H     H              ", "              H     H              ", "              HH   HH              ", "               HHHHH               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("   ABDBBBBBBBBBBBBBBBBBBBBBBBDBA   ", "    BDEEEEEEEEEEEEEEEEEEEEEEEDB    ", "    A                         A    ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                HHH                ", "     AA        HHHHH        AA     ", "               HHHHH               ", "               HHHHH               ", "                HHH                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("   AABDBBBBBBBBBBBBBBBBBBBBBDBAA   ", "     BDEEEEEEEEEEEEEEEEEEEEEDB     ", "    AA                       AA    ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       G                   G       ", "      AF                   FA      ", "       G                   G       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                 G                 ")
                    .aisle("    ABDBBBBBBBBBBBBBBBBBBBBBDBA    ", "     BDEEEEEEEEEEEEEEEEEEEEEDB     ", "     A                       A     ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       A                   A       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               G   G               ", "                                   ", "                 C                 ")
                    .aisle("    AABDBBBBBBBBBBBBBBBBBBBDBAA    ", "      BDEEEEEEEEEEEEEEEEEEEDB      ", "     AA                     AA     ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "       AA                 AA       ", "                                   ", "                                   ", "                                   ", "                                   ", "                 G                 ", "                                   ", "               C   C               ", "                                   ", "                 C                 ")
                    .aisle("     AABDBBBBBBBBBBBBBBBBBDBAA     ", "       BDEEEEEEEEEEEEEEEEEDB       ", "      AA                   AA      ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        AA               AA        ", "                                   ", "                                   ", "                                   ", "                                   ", "                 C                 ", "                 B                 ", "               CBBBC               ", "                 B                 ", "                 C                 ")
                    .aisle("      AABDBBBBBBBBBBBBBBBDBAA      ", "        BDEEEEEEEEEEEEEEEDB        ", "       AA                 AA       ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "           G           G           ", "         AAF           FAA         ", "           G           G           ", "                                   ", "                                   ", "                                   ", "                 B                 ", "                BBB                ", "                 B                 ", "                                   ", "                                   ")
                    .aisle("       AABDDBBBBBBBBBBBDDBAA       ", "         BDDEEEEEEEEEEEDDB         ", "        AA               AA        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "           AA         AA           ", "                AAA                ", "                AAA                ", "                 A                 ", "                 B                 ", "                BBB                ", "                 B                 ", "                                   ", "                                   ", "                                   ")
                    .aisle("        AABBDDBBBBBBBDDBBAA        ", "          BBDDEEEEEEEDDBB          ", "         AAA           AAA         ", "                                   ", "                                   ", "                                   ", "                AAA                ", "                AAA                ", "                AAA                ", "            AAAAAAAAAAA            ", "               A C A               ", "               A C A               ", "                ACA                ", "                ABA                ", "                ABA                ", "                 A                 ", "                 F                 ", "                                   ", "                                   ")
                    .aisle("         AAABBDDDDDDDBBAAA         ", "            BBDDDDDDDBB            ", "           AAA  AAA  AAA           ", "                AAA                ", "                AAA                ", "                AAA                ", "               A C A               ", "               A C A               ", "               A C A               ", "               A C A               ", "               A B A               ", "               A B A               ", "                ABA                ", "                 A                 ", "                 A                 ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("           AAABABBBABAAA           ", "              BACCCAB              ", "             AAA C AAA             ", "               A C A               ", "               A C A               ", "               A C A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("             AAABBBAAA             ", "               ACBCA               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A B A               ", "               A   A               ", "               A   A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("               ABBBA               ", "               ACCCA               ", "               A   A               ", "               A   A               ", "               A   A               ", "               A   A               ", "                AAA                ", "                AAA                ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .aisle("                AAA                ", "                AAA                ", "                AAA                ", "                AAA                ", "                AAA                ", "                AAA                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ")
                    .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where("B", Predicates.blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where("C", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria)))
                    .where("D", Predicates.blocks(GTOBlocks.ECHO_CASING.get()))
                    .where("E", Predicates.blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where("F", Predicates.blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where("G", Predicates.blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where("H", Predicates.blocks(GTOBlocks.HYPER_CORE.get()))
                    .where("a", Predicates.blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(" ", Predicates.any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/enhance_hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public final static MultiblockMachineDefinition HYPER_REACTOR = REGISTRATE.multiblock("hyper_reactor", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GTORecipeTypes.HYPER_REACTOR_RECIPES)
            .tooltips(Component.translatable("gtocore.machine.hyper_reactor.tooltip.0"))
            .tooltips(Component.translatable("gtocore.machine.hyper_reactor.tooltip.1"))
            .tooltips(Component.translatable("gtocore.machine.hyper_reactor.tooltip.2"))
            .tooltips(Component.translatable("gtceu.machine.perfect_oc"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.hyper_reactor")))
            .generator(true)
            .recipeModifier((machine, recipe, params, result) -> {
                if (machine instanceof WorkableElectricMultiblockMachine workableElectricMultiblockMachine) {
                    int p = 1;
                    long outputEUt = RecipeHelper.getOutputEUt(recipe);
                    if (outputEUt == GTValues.V[GTValues.UEV]) {
                        if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == GTValues.V[GTValues.UIV]) {
                        if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == GTValues.V[GTValues.UXV]) {
                        if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    } else if (outputEUt == GTValues.V[GTValues.OpV]) {
                        if (MachineUtil.inputFluid(workableElectricMultiblockMachine, GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 1))) {
                            p = 16;
                        }
                    }
                    return GTORecipeModifiers.standardOverclocking(workableElectricMultiblockMachine, GTRecipeModifiers.fastParallel(machine, recipe, p, false).getFirst());
                }
                return recipe;
            })
            .appearanceBlock(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING)
            .pattern((definition) -> FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
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
                    .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where("B", Predicates.blocks(GTOBlocks.PIKYONIUM_MACHINE_CASING.get()))
                    .where("C", Predicates.blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get()))
                    .where("D", Predicates.blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium)))
                    .where("E", Predicates.blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where("F", Predicates.blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where("G", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
                    .where("H", Predicates.blocks(GTOBlocks.HYPER_CORE.get()))
                    .where("I", Predicates.blocks(GTOBlocks.NEUTRONIUM_PIPE_CASING.get()))
                    .where("a", Predicates.blocks(GTOBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_LASER).setMaxGlobalLimited(1)))
                    .where(" ", Predicates.any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/enhance_hyper_mechanical_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition GENERATOR_ARRAY = REGISTRATE.multiblock("generator_array", GeneratorArrayMachine::new)
            .rotationState(RotationState.ALL)
            .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .generator(true)
            .tooltips(Component.translatable("gtocore.machine.generator_array.tooltip.0"))
            .tooltips(Component.translatable("gtocore.machine.generator_array.tooltip.1"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_6.tooltip",
                    Component.translatable("gtceu.steam_turbine"),
                    Component.translatable("gtceu.combustion_generator"),
                    Component.translatable("gtceu.gas_turbine"),
                    Component.translatable("gtceu.semi_fluid_generator"),
                    Component.translatable("gtceu.rocket_engine"),
                    Component.translatable("gtceu.naquadah_reactor")))
            .recipeModifier(GeneratorArrayMachine::recipeModifier, true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "CCC", "XXX")
                    .aisle("XXX", "C#C", "XXX")
                    .aisle("XSX", "CCC", "XXX")
                    .where('S', Predicates.controller(Predicates.blocks(definition.getBlock())))
                    .where('X', Predicates.blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1))
                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where('C', Predicates.blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('#', Predicates.air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/processing_array"))
            .register();

    public final static MultiblockMachineDefinition ANNIHILATE_GENERATOR = REGISTRATE.multiblock("annihilate_generator", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES)
            .tooltips(Component.translatable("gtceu.machine.perfect_oc"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.annihilate_generator")))
            .generator(true)
            .recipeModifier((machine, recipe, params, result) -> GTORecipeModifiers.standardOverclocking((WorkableElectricMultiblockMachine) machine, recipe))
            .appearanceBlock(GTBlocks.HIGH_POWER_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
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
                    .where("~", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("A", Predicates.blocks(GTOBlocks.GRAVITON_FIELD_CONSTRAINT_CASING.get()))
                    .where("B", Predicates.blocks(GTOBlocks.HYPER_CORE.get()))
                    .where("C", Predicates.blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where("D", Predicates.blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where("E", Predicates.blocks(GTOBlocks.NAQUADAH_ALLOY_CASING.get()))
                    .where("F", Predicates.blocks(GTBlocks.FUSION_GLASS.get()))
                    .where("G", Predicates.blocks(GTOBlocks.DYSON_CONTROL_TOROID.get()))
                    .where("H", Predicates.blocks(GTOBlocks.RHENIUM_REINFORCED_ENERGY_GLASS.get()))
                    .where("P", Predicates.blocks(GTOBlocks.DYSON_CONTROL_CASING.get()))
                    .where("S", Predicates.blocks(GTBlocks.HIGH_POWER_CASING.get())
                            .or(Predicates.abilities(PartAbility.OUTPUT_LASER))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS)))
                    .where("T", Predicates.blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where("R", Predicates.blocks(GTOBlocks.DYSON_RECEIVER_CASING.get()))
                    .where(" ", Predicates.any())
                    .build())
            .multiblockPreviewRenderer(true, false)
            .renderer(AnnihilateGeneratorRenderer::new)
            .hasTESR(true)
            .register();
}
