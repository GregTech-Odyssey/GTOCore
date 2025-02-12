package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.DustProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IngotProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.generated.PartsRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.api.data.tag.GTOTagPrefix.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.LASER_WELDER_RECIPES;
import static com.gto.gtocore.common.data.GTORecipeTypes.THREE_DIMENSIONAL_PRINTER_RECIPES;

public final class GTOPartsRecipeHandler {

    public static void init(Consumer<FinishedRecipe> provider) {
        rod.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processStick);
        rodLong.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processLongStick);
        plate.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandler::processPlate);
        plateDouble.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processPlateDouble);
        plateDense.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processPlateDense);

        turbineBlade.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processTurbine);
        rotor.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processRotor);
        bolt.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandler::processBolt);
        screw.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processScrew);
        wireFine.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processFineWire);
        foil.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processFoil);
        lens.executeHandler(provider, PropertyKey.GEM, PartsRecipeHandler::processLens);

        gear.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processGear);
        gearSmall.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processGear);
        ring.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processRing);
        springSmall.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processSpringSmall);
        spring.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processSpring);
        round.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processRound);
        contaminableNanites.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processManoswarm);
        curvedPlate.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processcurvedPlate);
        motorEnclosure.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processMotorEnclosure);
        pumpBarrel.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processPumpBarrel);
        pistonHousing.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processPistonHousing);
        emitterBases.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processEmitterBases);
        sensorCasing.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processSensorCasing);
        fieldGeneratorCasing.executeHandler(provider, PropertyKey.INGOT, GTOPartsRecipeHandler::processFieldGeneratorCasing);
        catalyst.executeHandler(provider, PropertyKey.DUST, GTOPartsRecipeHandler::processCatalyst);
    }

    private static void processScrew(TagPrefix screwPrefix, Material material, DustProperty property,
                                     Consumer<FinishedRecipe> provider) {
        ItemStack screwStack = ChemicalHelper.get(screwPrefix, material);
        int mass = (int) material.getMass();
        LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_bolt_to_screw")
                .inputItems(bolt, material)
                .outputItems(screwStack)
                .duration(Math.max(1, mass / 8))
                .EUt(4)
                .save(provider);

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("screw_%s", material.getName()),
                    screwStack, "fX", "X ",
                    'X', new UnificationEntry(bolt, material));
    }

    private static void processFoil(TagPrefix foilPrefix, Material material, IngotProperty property,
                                    Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (!material.hasFlag(NO_SMASHING) && mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("foil_%s", material.getName()),
                    ChemicalHelper.get(foilPrefix, material, 2),
                    "hP ", 'P', new UnificationEntry(plate, material));

        GTORecipeTypes.CLUSTER_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_foil")
                .inputItems(plate, material)
                .outputItems(foilPrefix, material, 4)
                .duration(mass)
                .EUt(24)
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_foil")
                    .inputItems(ingot, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_FOIL)
                    .outputItems(foilPrefix, material, 4)
                    .duration(mass << 1)
                    .EUt(96)
                    .save(provider);
        }
    }

    private static void processFineWire(TagPrefix fineWirePrefix, Material material, IngotProperty property,
                                        Consumer<FinishedRecipe> provider) {
        ItemStack fineWireStack = ChemicalHelper.get(fineWirePrefix, material);
        int mass = (int) material.getMass();
        if (!ChemicalHelper.get(foil, material).isEmpty() && mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapelessRecipe(provider, String.format("fine_wire_%s", material.getName()),
                    fineWireStack, 'x', new UnificationEntry(foil, material));

        if (material.hasProperty(PropertyKey.WIRE)) {
            WIREMILL_RECIPES.recipeBuilder("mill_" + material.getName() + "_wire_to_fine_wire")
                    .inputItems(wireGtSingle, material)
                    .outputItems(wireFine, material, 4)
                    .duration(mass * 3 / 2)
                    .EUt(VA[ULV])
                    .save(provider);
        } else {
            WIREMILL_RECIPES.recipeBuilder("mill_" + material.getName() + "ingot_to_fine_wire")
                    .inputItems(ingot, material)
                    .outputItems(wireFine, material, 8)
                    .duration(mass * 3)
                    .EUt(VA[ULV])
                    .save(provider);
        }
    }

    private static void processGear(TagPrefix gearPrefix, Material material, DustProperty property,
                                    Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack stack = ChemicalHelper.get(gearPrefix, material);
        if (gearPrefix == gear && material.hasProperty(PropertyKey.INGOT)) {
            int voltageMultiplier = GTOUtils.getVoltageMultiplier(material);
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_gear")
                    .inputItems(ingot, material, 4)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR)
                    .outputItems(gearPrefix, material)
                    .duration(mass * 10)
                    .EUt(8L * voltageMultiplier)
                    .save(provider);

            if (material.hasFlag(NO_SMASHING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_gear")
                        .inputItems(dust, material, 4)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR)
                        .outputItems(gearPrefix, material)
                        .duration(mass * 10)
                        .EUt(8L * voltageMultiplier)
                        .save(provider);
            }
        }

        if (material.hasFluid()) {
            boolean isSmall = gearPrefix == gearSmall;
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_" + gearPrefix.name)
                    .notConsumable(isSmall ? GTItems.SHAPE_MOLD_GEAR_SMALL : GTItems.SHAPE_MOLD_GEAR)
                    .inputFluids(material.getFluid(L * (isSmall ? 1 : 4)))
                    .outputItems(stack)
                    .duration(isSmall ? 20 : 100)
                    .EUt(VA[ULV])
                    .save(provider);
        }

        if (material.hasFlag(GENERATE_PLATE) && material.hasFlag(GENERATE_ROD)) {
            if (gearPrefix == gearSmall) {
                if (mass < 240 && material.getBlastTemperature() < 3600)
                    VanillaRecipeHelper.addShapedRecipe(provider, String.format("small_gear_%s", material.getName()),
                            ChemicalHelper.get(gearSmall, material),
                            " R ", "hPx", " R ", 'R', new UnificationEntry(rod, material), 'P',
                            new UnificationEntry(plate, material));

                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_small_gear")
                        .inputItems(ingot, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR_SMALL)
                        .outputItems(stack)
                        .duration(mass << 1)
                        .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                        .save(provider);

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_small_gear")
                            .inputItems(dust, material)
                            .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR_SMALL)
                            .outputItems(stack)
                            .duration(mass << 1)
                            .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                            .save(provider);
                }
            } else if (mass < 240 && material.getBlastTemperature() < 3600) {
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("gear_%s", material.getName()), stack,
                        "RPR", "PwP", "RPR",
                        'P', new UnificationEntry(plate, material),
                        'R', new UnificationEntry(rod, material));
            }
        }
    }

    private static void processPlateDouble(TagPrefix doublePrefix, Material material, IngotProperty property,
                                           Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasFlag(GENERATE_PLATE)) {
            if (!material.hasFlag(NO_SMASHING) && mass < 240 && material.getBlastTemperature() < 3600) {
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("plate_double_%s", material.getName()),
                        ChemicalHelper.get(doublePrefix, material),
                        "h", "P", "P", 'P', new UnificationEntry(plate, material));
            }

            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_double_plate")
                    .EUt(96).duration(mass << 1)
                    .inputItems(ingot, material, 2)
                    .outputItems(doublePrefix, material)
                    .circuitMeta(2)
                    .save(provider);
        }
    }

    private static void processPlateDense(TagPrefix tagPrefix, Material material, DustProperty property,
                                          Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.INGOT)) {
            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_block_to_dense_plate")
                    .inputItems(block, material)
                    .outputItems(tagPrefix, material)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save(provider);
        } else {
            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_plate_to_dense_plate")
                    .inputItems(plate, material, 9)
                    .outputItems(tagPrefix, material)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save(provider);
        }
    }

    private static void processRing(TagPrefix ringPrefix, Material material, IngotProperty property,
                                    Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_ring")
                .inputItems(ingot, material)
                .notConsumable(GTItems.SHAPE_EXTRUDER_RING)
                .outputItems(ringPrefix, material, 4)
                .duration(mass << 2)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(GENERATE_ROD)) {
            BENDER_RECIPES.recipeBuilder("bender_" + material.getName() + "_rod_to_ring")
                    .inputItems(rod, material)
                    .outputItems(ringPrefix, material, 2)
                    .duration(mass << 1)
                    .EUt(16)
                    .circuitMeta(2)
                    .save(provider);
        }

        if (!material.hasFlag(NO_SMASHING)) {
            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("ring_%s", material.getName()),
                        ChemicalHelper.get(ringPrefix, material),
                        "h ", " X",
                        'X', new UnificationEntry(rod, material));
        } else {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_ring")
                    .inputItems(dust, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_RING)
                    .outputItems(ringPrefix, material, 4)
                    .duration(mass << 2)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save(provider);
        }
    }

    private static void processSpringSmall(TagPrefix springPrefix, Material material, IngotProperty property,
                                           Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("spring_small_%s", material.getName()),
                    ChemicalHelper.get(springSmall, material),
                    " s ", "fRx", 'R', new UnificationEntry(rod, material));

        BENDER_RECIPES.recipeBuilder("bend_" + material.getName() + "_rod_to_small_spring")
                .duration(Math.max(1, mass / 2)).EUt(VA[ULV])
                .inputItems(rod, material)
                .outputItems(springSmall, material, 2)
                .circuitMeta(1)
                .save(provider);
    }

    private static void processSpring(TagPrefix springPrefix, Material material, IngotProperty property,
                                      Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        BENDER_RECIPES.recipeBuilder("bend_" + material.getName() + "_long_rod_to_spring")
                .inputItems(rodLong, material)
                .outputItems(spring, material)
                .circuitMeta(1)
                .duration(mass)
                .EUt(16)
                .save(provider);

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("spring_%s", material.getName()),
                    ChemicalHelper.get(spring, material),
                    " s ", "fRx", " R ", 'R', new UnificationEntry(rodLong, material));
    }

    private static void processRotor(TagPrefix rotorPrefix, Material material, IngotProperty property,
                                     Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack curvedPlateStack = ChemicalHelper.get(GTOTagPrefix.curvedPlate, material);
        ItemStack ringStack = ChemicalHelper.get(ring, material);
        ItemStack stack = ChemicalHelper.get(rotorPrefix, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("rotor_%s", material.getName()), stack,
                    "ChC", "SRf", "CdC",
                    'C', curvedPlateStack,
                    'S', new UnificationEntry(screw, material),
                    'R', ringStack);

        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_rotor")
                    .notConsumable(GTItems.SHAPE_MOLD_ROTOR)
                    .inputFluids(material.getFluid(L * 5))
                    .outputItems(GTUtil.copy(stack))
                    .duration(mass << 2)
                    .EUt(20)
                    .save(provider);
        }

        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_rotor")
                .inputItems(ingot, material, 5)
                .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                .outputItems(GTUtil.copy(stack))
                .duration(mass << 3)
                .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                .save(provider);

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_to_rotor")
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(ringStack)
                .circuitMeta(1)
                .outputItems(GTUtil.copy(stack))
                .duration(mass)
                .EUt(30)
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_rotor")
                    .inputItems(dust, material, 5)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                    .outputItems(GTUtil.copy(stack))
                    .duration(mass << 3)
                    .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                    .save(provider);
        }
    }

    private static void processStick(TagPrefix stickPrefix, Material material, DustProperty property,
                                     Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.GEM) || material.hasProperty(PropertyKey.INGOT)) {
            LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_to_rod")
                    .inputItems(material.hasProperty(PropertyKey.GEM) ? gem : ingot, material)
                    .outputItems(rod, material, 2)
                    .duration(mass << 1)
                    .EUt(16).save(provider);
        }

        if (material.hasFlag(GENERATE_BOLT_SCREW)) {
            ItemStack boltStack = ChemicalHelper.get(bolt, material);
            CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_rod_to_bolt")
                    .inputItems(stickPrefix, material)
                    .outputItems(GTUtil.copyAmount(4, boltStack))
                    .duration(mass << 1)
                    .EUt(4)
                    .save(provider);

            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("bolt_saw_%s", material.getName()),
                        GTUtil.copyAmount(2, boltStack),
                        "s ", " X",
                        'X', new UnificationEntry(rod, material));
        }
    }

    private static void processLongStick(TagPrefix longStickPrefix, Material material, DustProperty property,
                                         Consumer<FinishedRecipe> provider) {
        ItemStack stack = ChemicalHelper.get(longStickPrefix, material);
        ItemStack stickStack = ChemicalHelper.get(rod, material);
        int mass = (int) material.getMass();
        CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_long_rod_to_rod")
                .inputItems(longStickPrefix, material)
                .outputItems(GTUtil.copyAmount(2, stickStack))
                .duration(mass).EUt(4)
                .save(provider);

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("stick_long_%s", material.getName()),
                    GTUtil.copyAmount(2, stickStack),
                    "s", "X", 'X', new UnificationEntry(rodLong, material));

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("stick_long_stick_%s", material.getName()), stack,
                    "ShS",
                    'S', new UnificationEntry(rod, material));

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_rod_to_long_rod")
                .inputItems(rod, material, 2)
                .circuitMeta(2)
                .outputItems(stack)
                .duration(mass)
                .EUt(16)
                .save(provider);

        if (material.hasProperty(PropertyKey.INGOT)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_long_rod")
                    .inputItems(ingot, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_ROD_LONG)
                    .outputItems(stack)
                    .duration(mass << 1)
                    .EUt(64)
                    .save(provider);

            if (material.hasFlag(NO_SMASHING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_long_rod")
                        .inputItems(dust, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_ROD_LONG)
                        .outputItems(stack)
                        .duration(mass << 1)
                        .EUt(64)
                        .save(provider);
            }
        }
    }

    private static void processTurbine(TagPrefix toolPrefix, Material material, IngotProperty property,
                                       Consumer<FinishedRecipe> provider) {
        ItemStack rotorStack = GTItems.TURBINE_ROTOR.asStack();
        int mass = (int) material.getMass();
        // noinspection ConstantConditions
        TurbineRotorBehaviour.getBehaviour(rotorStack).setPartMaterial(rotorStack, material);

        ASSEMBLER_RECIPES.recipeBuilder("assemble_" + material.getName() + "_turbine_blade")
                .inputItems(turbineBlade, material, 8)
                .inputItems(rodLong, GTMaterials.Magnalium)
                .outputItems(rotorStack)
                .duration(200)
                .EUt(400)
                .save(provider);

        FORMING_PRESS_RECIPES.recipeBuilder("press_" + material.getName() + "_turbine_rotor")
                .inputItems(plateDouble, material, 5)
                .inputItems(screw, material, 2)
                .outputItems(toolPrefix, material)
                .duration(mass * 10)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 2)
                .save(provider);
    }

    private static void processRound(TagPrefix prefix, Material material, IngotProperty property,
                                     Consumer<FinishedRecipe> provider) {
        if (!material.hasFlag(NO_SMASHING) && material.getMass() < 222 && material.getBlastTemperature() < 6000) {
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("round_%s", material.getName()),
                    ChemicalHelper.get(round, material),
                    "fN", "Nh", 'N', new UnificationEntry(nugget, material));

            VanillaRecipeHelper.addShapedRecipe(provider, String.format("round_from_ingot_%s", material.getName()),
                    ChemicalHelper.get(round, material, 4),
                    "fIh", 'I', new UnificationEntry(ingot, material));
        }

        LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_nugget_to_round")
                .EUt(VA[ULV]).duration(Math.min(1, (int) material.getMass() / 9))
                .inputItems(nugget, material)
                .outputItems(round, material)
                .save(provider);
    }

    private static void processManoswarm(TagPrefix prefix, Material material, DustProperty property,
                                         Consumer<FinishedRecipe> provider) {
        CHEMICAL_BATH_RECIPES.recipeBuilder(material.getName() + "_nano_bath")
                .inputFluids(GTOMaterials.PiranhaSolution.getFluid((int) (10000 * Math.sqrt((double) material.getMass() / GTOMaterials.Eternity.getMass()))))
                .inputItems(contaminableNanites, material)
                .outputItems(nanites, material)
                .duration((int) material.getMass() << 4)
                .EUt(480)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processcurvedPlate(TagPrefix prefix, Material material, IngotProperty property,
                                           Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("curved_plate_%s", material.getName()),
                    curvedPlateStack, "hI", " h", 'I', plateStack);

        BENDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_curved_plate"))
                .inputItems(plateStack)
                .outputItems(curvedPlateStack)
                .circuitMeta(1)
                .duration(mass)
                .EUt(16)
                .save(provider);
    }

    private static void processMotorEnclosure(TagPrefix prefix, Material material, IngotProperty property,
                                              Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack motorEnclosureStack = ChemicalHelper.get(motorEnclosure, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("motor_enclosure_%s", material.getName()),
                    motorEnclosureStack, "IwI", 'I', curvedPlateStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_motor_enclosure"))
                .inputItems(curvedPlateStack.copyWithCount(2))
                .outputItems(motorEnclosureStack)
                .circuitMeta(3)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(motorEnclosureStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_motor_enclosure"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(motorEnclosureStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_motor_enclosure"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 1))
                .outputItems(motorEnclosureStack)
                .duration(mass << 1)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processPumpBarrel(TagPrefix prefix, Material material, IngotProperty property,
                                          Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack pumpBarrelStack = ChemicalHelper.get(pumpBarrel, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        ItemStack ringStack = ChemicalHelper.get(ring, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("pump_barrel_%s", material.getName()),
                    pumpBarrelStack, " I ", "RwR", " I ", 'I', curvedPlateStack, 'R', ringStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_pump_barrel"))
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(ringStack.copyWithCount(2))
                .circuitMeta(4)
                .outputItems(pumpBarrelStack)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(pumpBarrelStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_pump_barrel"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(pumpBarrelStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_pump_barrel"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 5 / 2))
                .outputItems(pumpBarrelStack)
                .duration(mass * 5 / 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processPistonHousing(TagPrefix prefix, Material material, IngotProperty property,
                                             Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack pistonHousingStack = ChemicalHelper.get(pistonHousing, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("piston_housing_%s", material.getName()),
                    pistonHousingStack, "IhI", " P ", 'I', curvedPlateStack, 'P', plateStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_piston_housing"))
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(plateStack)
                .circuitMeta(5)
                .outputItems(pistonHousingStack)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(pistonHousingStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_pump_barrel"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(pistonHousingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_pump_barrel"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 3))
                .outputItems(pistonHousingStack)
                .duration(mass * 3)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processEmitterBases(TagPrefix prefix, Material material, IngotProperty property,
                                            Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack emitterBasesStack = ChemicalHelper.get(emitterBases, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        ItemStack rodStack = ChemicalHelper.get(rod, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("emitter_base_%s", material.getName()),
                    emitterBasesStack, "IPI", "RhR", 'I', curvedPlateStack, 'P', plateStack, 'R', rodStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_emitter_base"))
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(rodStack.copyWithCount(2))
                .inputItems(plateStack)
                .outputItems(emitterBasesStack)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(emitterBasesStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_emitter_base"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(emitterBasesStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_emitter_base"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 2))
                .outputItems(emitterBasesStack)
                .duration(mass << 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processSensorCasing(TagPrefix prefix, Material material, IngotProperty property,
                                            Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack sensorCasingStack = ChemicalHelper.get(sensorCasing, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        ItemStack rodStack = ChemicalHelper.get(rod, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("sensor_casing_%s", material.getName()),
                    sensorCasingStack, "wIh", "IRI", " I ", 'I', curvedPlateStack, 'R', rodStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_sensor_casing"))
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(rodStack.copyWithCount(1))
                .circuitMeta(6)
                .outputItems(sensorCasingStack)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(sensorCasingStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_sensor_casing"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(sensorCasingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_sensor_casing"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 9 / 2))
                .outputItems(sensorCasingStack)
                .duration(mass * 9 / 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processFieldGeneratorCasing(TagPrefix prefix, Material material, IngotProperty property,
                                                    Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack fieldGeneratorCasingStack = ChemicalHelper.get(fieldGeneratorCasing, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(curvedPlate, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("field_generator_casing_%s", material.getName()),
                    fieldGeneratorCasingStack, "IPI", "PwP", "IPI", 'I', curvedPlateStack, 'P', plateStack);

        LASER_WELDER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_field_generator_casing"))
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(plateStack.copyWithCount(4))
                .circuitMeta(7)
                .outputItems(fieldGeneratorCasingStack)
                .duration(mass)
                .EUt(30)
                .save(provider);

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(fieldGeneratorCasingStack);

        SCANNER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_sensor_casing"))
                .inputItems(GTOItems.DATA_DISC.asStack())
                .inputItems(fieldGeneratorCasingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save(provider);

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_sensor_casing"))
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 3))
                .outputItems(fieldGeneratorCasingStack)
                .duration(mass << 3)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    private static void processCatalyst(TagPrefix prefix, Material material, DustProperty property, Consumer<FinishedRecipe> provider) {
        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_catalyst"))
                .inputItems(GTOItems.CATALYST_BASE.asStack())
                .inputItems(dust, material, 16)
                .outputItems(catalyst, material)
                .duration((int) material.getMass() << 2)
                .EUt(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }
}
