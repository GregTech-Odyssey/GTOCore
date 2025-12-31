package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.PCB_FACTORY_RECIPES;

final class PCBFactory {

    public static void init() {
        PCB_FACTORY_RECIPES.recipeBuilder("wetware_printed_circuit_board")
                .inputItems(GTItems.WETWARE_BOARD)
                .inputItems(TagPrefix.foil, GTMaterials.NiobiumTitanium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 1000)
                .inputFluids(GTMaterials.Iron3Chloride, 500)
                .outputItems(GTItems.WETWARE_CIRCUIT_BOARD)
                .EUt(1920)
                .duration(450)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("epoxy_printed_circuit_board")
                .inputItems(TagPrefix.plate, GTMaterials.Epoxy, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Electrum, 128)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.Iron3Chloride, 1000)
                .outputItems(GTItems.ADVANCED_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polytetrafluoroethylene_plate1")
                .inputItems(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 160)
                .inputFluids(GTMaterials.SulfuricAcid, 4000)
                .inputFluids(GTMaterials.SodiumPersulfate, 4000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 32)
                .EUt(120)
                .duration(1600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("fiber_reinforced_printed_circuit_board1")
                .inputItems(TagPrefix.plate, GTMaterials.ReinforcedEpoxyResin, 8)
                .inputItems(TagPrefix.foil, GTMaterials.AnnealedCopper, 160)
                .inputFluids(GTMaterials.SulfuricAcid, 500)
                .inputFluids(GTMaterials.SodiumPersulfate, 4000)
                .outputItems(GTItems.EXTREME_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polyvinyl_chloride_plate1")
                .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 96)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.SodiumPersulfate, 2000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 16)
                .EUt(120)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("supracausal_circuit_board")
                .inputItems(GTOItems.COSMIC_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTOMaterials.Echoite, 32)
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.SUPRACAUSAL_CIRCUIT_BOARD)
                .EUt(1966080)
                .duration(1500)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polybenzimidazole_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 288)
                .inputFluids(GTMaterials.SulfuricAcid, 8000)
                .inputFluids(GTMaterials.Iron3Chloride, 4000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 64)
                .EUt(120)
                .duration(3200)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("bioware_printed_circuit_board")
                .inputItems(GTOItems.BIOWARE_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTMaterials.VanadiumGallium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 2000)
                .inputFluids(GTMaterials.Iron3Chloride, 1000)
                .outputItems(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD)
                .EUt(7680)
                .duration(525)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("optical_circuit_board")
                .inputItems(TagPrefix.plate, GTOMaterials.Kevlar)
                .inputItems(TagPrefix.foil, GTMaterials.Rhodium, 32)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.OPTICAL_CIRCUIT_BOARD)
                .EUt(30720)
                .duration(600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polyethylene_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Polyethylene, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 64)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTMaterials.Iron3Chloride, 500)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(400)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("multilayer_fiber_reinforced_printed_circuit_board1")
                .inputItems(GTItems.FIBER_BOARD, 16)
                .inputItems(TagPrefix.foil, GTMaterials.Platinum, 128)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.SodiumPersulfate, 8000)
                .outputItems(GTItems.ELITE_CIRCUIT_BOARD, 8)
                .EUt(480)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("epoxy_printed_circuit_board1")
                .inputItems(TagPrefix.plate, GTMaterials.Epoxy, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Electrum, 128)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.SodiumPersulfate, 2000)
                .outputItems(GTItems.ADVANCED_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polytetrafluoroethylene_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 160)
                .inputFluids(GTMaterials.SulfuricAcid, 4000)
                .inputFluids(GTMaterials.Iron3Chloride, 2000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 32)
                .EUt(120)
                .duration(1600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polyethylene_plate1")
                .inputItems(TagPrefix.plate, GTMaterials.Polyethylene, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 64)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTMaterials.SodiumPersulfate, 1000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(400)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("exotic_circuit_board")
                .inputItems(TagPrefix.plate, GTOMaterials.Kevlar, 2)
                .inputItems(TagPrefix.foil, GTOMaterials.Enderium, 32)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.EXOTIC_CIRCUIT_BOARD)
                .EUt(122880)
                .duration(900)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("optical_printed_circuit_board")
                .inputItems(GTOItems.OPTICAL_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTMaterials.Ruthenium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 4000)
                .inputFluids(GTMaterials.Iron3Chloride, 2000)
                .outputItems(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD)
                .EUt(30720)
                .duration(600)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("multilayer_fiber_reinforced_printed_circuit_board")
                .inputItems(GTItems.FIBER_BOARD, 16)
                .inputItems(TagPrefix.foil, GTMaterials.Platinum, 128)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.Iron3Chloride, 1000)
                .outputItems(GTItems.ELITE_CIRCUIT_BOARD, 8)
                .EUt(480)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polybenzimidazole_plate1")
                .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 288)
                .inputFluids(GTMaterials.SulfuricAcid, 8000)
                .inputFluids(GTMaterials.SodiumPersulfate, 8000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 64)
                .EUt(120)
                .duration(3200)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("polyvinyl_chloride_plate")
                .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 8)
                .inputItems(TagPrefix.foil, GTMaterials.Copper, 96)
                .inputFluids(GTMaterials.SulfuricAcid, 2000)
                .inputFluids(GTMaterials.Iron3Chloride, 1000)
                .outputItems(GTItems.PLASTIC_CIRCUIT_BOARD, 16)
                .EUt(120)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("exotic_printed_circuit_board")
                .inputItems(GTOItems.EXOTIC_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTMaterials.Americium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 8000)
                .inputFluids(GTMaterials.Iron3Chloride, 4000)
                .outputItems(GTOItems.EXOTIC_PRINTED_CIRCUIT_BOARD)
                .EUt(122880)
                .duration(900)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("cosmic_printed_circuit_board")
                .inputItems(GTOItems.COSMIC_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTOMaterials.Uruium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 8000)
                .inputFluids(GTMaterials.Iron3Chloride, 4000)
                .outputItems(GTOItems.COSMIC_PRINTED_CIRCUIT_BOARD)
                .EUt(491520)
                .duration(1200)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("fiber_reinforced_printed_circuit_board")
                .inputItems(TagPrefix.plate, GTMaterials.ReinforcedEpoxyResin, 8)
                .inputItems(TagPrefix.foil, GTMaterials.AnnealedCopper, 160)
                .inputFluids(GTMaterials.SulfuricAcid, 500)
                .inputFluids(GTMaterials.Iron3Chloride, 2000)
                .outputItems(GTItems.EXTREME_CIRCUIT_BOARD, 8)
                .EUt(120)
                .duration(800)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("supracausal_printed_circuit_board")
                .inputItems(GTOItems.SUPRACAUSAL_CIRCUIT_BOARD)
                .inputItems(TagPrefix.foil, GTOMaterials.Legendarium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate, 16000)
                .inputFluids(GTMaterials.Iron3Chloride, 8000)
                .outputItems(GTOItems.SUPRACAUSAL_PRINTED_CIRCUIT_BOARD)
                .EUt(1966080)
                .duration(1500)
                .save();

        PCB_FACTORY_RECIPES.recipeBuilder("cosmic_circuit_board")
                .inputItems(TagPrefix.plate, GTOMaterials.Kevlar, 4)
                .inputItems(TagPrefix.foil, GTOMaterials.HeavyQuarkDegenerateMatter, 32)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTOMaterials.MetastableHassium.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputItems(GTOItems.COSMIC_CIRCUIT_BOARD)
                .EUt(491520)
                .duration(1200)
                .save();
    }
}
