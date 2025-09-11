package com.gtocore.data.recipe.generated;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.data.chemical.material.GTOMaterialBuilder;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.item.tool.CoatedTurbineRotorBehaviour;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.api.data.tag.GTOTagPrefix.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

final class GTOPartsRecipeHandler {

    static List<Material> rotors = null;

    static void run(@NotNull Material material) {
        if (GTOUtils.isGeneration(rod, material)) {
            processStick(material);
            if (GTOUtils.isGeneration(rodLong, material)) {
                processLongStick(material);
            }
        }
        if (GTOUtils.isGeneration(plate, material)) {
            processPlate(material);
            processPlateDouble(material);
            processPlateDense(material);
        }

        if (rotors == null) {
            rotors = GTCEuAPI.materialManager.getRegisteredMaterials().stream()
                    .filter(m -> GTOUtils.isGeneration(turbineRotor, m)).toList();
        }
        if (GTOUtils.isGeneration(turbineBlade, material)) {
            processTurbine(material);
        }

        if (GTOUtils.isGeneration(rotor, material)) {
            processRotor(material);
        }
        if (GTOUtils.isGeneration(bolt, material)) {
            processBolt(material);
            processScrew(material);
        }
        if (GTOUtils.isGeneration(wireFine, material)) {
            processFineWire(material);
        }
        if (GTOUtils.isGeneration(foil, material)) {
            processFoil(material);
        }
        if (GTOUtils.isGeneration(lens, material)) {
            processLens(material);
        }
        if (GTOUtils.isGeneration(gear, material)) {
            processGear(gear, material);
        }
        if (GTOUtils.isGeneration(gearSmall, material)) {
            processGear(gearSmall, material);
        }
        if (GTOUtils.isGeneration(ring, material)) {
            processRing(material);
        }
        if (GTOUtils.isGeneration(springSmall, material)) {
            processSpringSmall(material);
        }
        if (GTOUtils.isGeneration(spring, material)) {
            processSpring(material);
        }
        if (GTOUtils.isGeneration(round, material)) {
            processRound(material);
        }
        if (GTOUtils.isGeneration(NANITES, material)) {
            processManoswarm(material);
        }
        if (GTOUtils.isGeneration(CURVED_PLATE, material)) {
            processcurvedPlate(material);
        }
        if (material.hasFlag(GTOMaterialFlags.GENERATE_COMPONENT)) {
            processMotorEnclosure(material);
            processPumpBarrel(material);
            processPistonHousing(material);
            processEmitterBases(material);
            processSensorCasing(material);
            processFieldGeneratorCasing(material);
        }
        if (GTOUtils.isGeneration(CATALYST, material)) {
            processCatalyst(material);
        }
        if (GTOUtils.isGeneration(ROUGH_BLANK, material)) {
            processroughBlank(material);
        }
        if (GTOUtils.isGeneration(ARTIFICIAL_GEM, material)) {
            processCrystallization(material);
        }
    }

    private static void processScrew(Material material) {
        ItemStack screwStack = ChemicalHelper.get(TagPrefix.screw, material);
        if (screwStack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack stack = ChemicalHelper.get(bolt, material);
        LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_bolt_to_screw")
                .inputItems(stack)
                .outputItems(screwStack)
                .duration(Math.max(1, mass / 8))
                .EUt(4)
                .save();

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("screw_%s", material.getName()), screwStack, "fX", "X ", 'X', stack);
    }

    private static void processFoil(Material material) {
        ItemStack stack = ChemicalHelper.get(TagPrefix.foil, material, 4);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack stack1 = ChemicalHelper.get(TagPrefix.plate, material);
        if (!material.hasFlag(NO_SMASHING) && mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("foil_%s", material.getName()),
                    stack.copyWithCount(2), "hP ", 'P', stack1);

        CLUSTER_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_foil")
                .inputItems(stack1)
                .outputItems(stack)
                .duration(mass)
                .EUt(24)
                .save();

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_foil")
                    .inputItems(ingot, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_FOIL)
                    .outputItems(stack)
                    .duration(mass)
                    .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                    .save();
        }
    }

    private static void processLens(@NotNull Material material) {
        ItemStack stack = ChemicalHelper.get(lens, material);
        if (stack.isEmpty()) return;
        LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_plate_to_lens")
                .inputItems(plate, material)
                .outputItems(stack)
                .outputItems(dustSmall, material)
                .duration(1200).EUt(120).save();

        if (!ChemicalHelper.get(gemExquisite, material).isEmpty()) {
            LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_gem_to_lens")
                    .inputItems(gemExquisite, material)
                    .outputItems(stack)
                    .outputItems(dust, material, 2)
                    .duration(2400).EUt(30).save();
        }
    }

    private static void processFineWire(Material material) {
        ItemStack fineWireStack = ChemicalHelper.get(TagPrefix.wireFine, material);
        if (fineWireStack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (!ChemicalHelper.get(foil, material).isEmpty() && mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapelessRecipe(String.format("fine_wire_%s", material.getName()),
                    fineWireStack, 'x', new MaterialEntry(foil, material));
        int voltageMultiplier = GTOUtils.getVoltageMultiplier(material);
        if (material.hasProperty(PropertyKey.WIRE)) {
            WIREMILL_RECIPES.recipeBuilder("mill_" + material.getName() + "_wire_to_fine_wire")
                    .inputItems(wireGtSingle, material)
                    .outputItems(fineWireStack.copyWithCount(4))
                    .duration(mass * 3 / 2)
                    .EUt(voltageMultiplier)
                    .save();
        } else {
            WIREMILL_RECIPES.recipeBuilder("mill_" + material.getName() + "ingot_to_fine_wire")
                    .inputItems(ingot, material)
                    .outputItems(fineWireStack.copyWithCount(8))
                    .duration(mass * 3)
                    .EUt(voltageMultiplier)
                    .save();
        }
    }

    private static void processGear(TagPrefix gearPrefix, Material material) {
        ItemStack stack = ChemicalHelper.get(gearPrefix, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (gearPrefix == gear && material.hasProperty(PropertyKey.INGOT)) {
            int voltageMultiplier = GTOUtils.getVoltageMultiplier(material);
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_gear")
                    .inputItems(ingot, material, 4)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR)
                    .outputItems(gearPrefix, material)
                    .duration(mass * 5)
                    .EUt(8L * voltageMultiplier)
                    .save();

            if (material.hasFlag(NO_SMASHING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_gear")
                        .inputItems(dust, material, 4)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR)
                        .outputItems(gearPrefix, material)
                        .duration(mass * 5)
                        .EUt(8L * voltageMultiplier)
                        .save();
            }
        }

        if (material.hasFluid()) {
            boolean isSmall = gearPrefix == gearSmall;
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_" + gearPrefix.name)
                    .notConsumable(isSmall ? GTItems.SHAPE_MOLD_GEAR_SMALL : GTItems.SHAPE_MOLD_GEAR)
                    .inputFluids(material.getFluid(L * (isSmall ? 1 : 4)))
                    .outputItems(stack)
                    .duration(mass * (isSmall ? 2 : 8))
                    .EUt(VA[LV])
                    .save();
        }

        if (material.hasFlag(GENERATE_PLATE) && material.hasFlag(GENERATE_ROD)) {
            if (gearPrefix == gearSmall) {
                if (mass < 240 && material.getBlastTemperature() < 3600)
                    VanillaRecipeHelper.addShapedRecipe(String.format("small_gear_%s", material.getName()),
                            ChemicalHelper.get(gearSmall, material),
                            " R ", "hPx", " R ", 'R', new MaterialEntry(rod, material), 'P',
                            new MaterialEntry(plate, material));

                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_small_gear")
                        .inputItems(ingot, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR_SMALL)
                        .outputItems(stack)
                        .duration(mass)
                        .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                        .save();

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_small_gear")
                            .inputItems(dust, material)
                            .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR_SMALL)
                            .outputItems(stack)
                            .duration(mass)
                            .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                            .save();
                }
            } else if (mass < 240 && material.getBlastTemperature() < 3600) {
                VanillaRecipeHelper.addShapedRecipe(String.format("gear_%s", material.getName()), stack,
                        "RPR", "PwP", "RPR",
                        'P', new MaterialEntry(plate, material),
                        'R', new MaterialEntry(rod, material));
            }
        }
    }

    private static void processPlate(Material material) {
        ItemStack stack = ChemicalHelper.get(plate, material);
        if (stack.isEmpty()) return;
        if (material.hasFluid()) {
            FluidStack fluidStack = material.getProperty(PropertyKey.FLUID).solidifiesFrom(L);
            if (!fluidStack.isEmpty()) {
                FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_plate")
                        .notConsumable(GTItems.SHAPE_MOLD_PLATE)
                        .inputFluids(fluidStack)
                        .outputItems(stack)
                        .duration((int) material.getMass())
                        .EUt(VA[ULV])
                        .save();
            }
        }
    }

    private static void processPlateDouble(Material material) {
        ItemStack stack = ChemicalHelper.get(TagPrefix.plateDouble, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (!material.hasFlag(NO_SMASHING) && mass < 240 && material.getBlastTemperature() < 3600) {
            VanillaRecipeHelper.addShapedRecipe(String.format("plate_double_%s", material.getName()),
                    stack, "h", "P", "P", 'P', new MaterialEntry(plate, material));
        }

        ROLLING_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_double_plate")
                .EUt(96).duration(mass << 1)
                .inputItems(ingot, material, 2)
                .outputItems(stack)
                .circuitMeta(2)
                .save();
    }

    private static void processPlateDense(Material material) {
        if (!material.hasFlag(MaterialFlags.GENERATE_DENSE)) return;
        ItemStack stack = ChemicalHelper.get(TagPrefix.plateDense, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.INGOT)) {
            ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_block_to_dense_plate")
                    .inputItems(block, material)
                    .outputItems(stack)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save();
        } else {
            ROLLING_RECIPES.recipeBuilder("rolling_" + material.getName() + "_plate_to_dense_plate")
                    .inputItems(plate, material, 9)
                    .outputItems(stack)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save();
        }
    }

    private static void processRing(Material material) {
        ItemStack stack = ChemicalHelper.get(TagPrefix.ring, material, 4);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_ring")
                .inputItems(ingot, material)
                .notConsumable(GTItems.SHAPE_EXTRUDER_RING)
                .outputItems(stack)
                .duration(mass << 1)
                .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                .save();

        if (material.hasFlag(GENERATE_ROD)) {
            BENDER_RECIPES.recipeBuilder("bender_" + material.getName() + "_rod_to_ring")
                    .inputItems(rod, material)
                    .outputItems(stack.copyWithCount(2))
                    .duration(mass)
                    .EUt(16)
                    .circuitMeta(2)
                    .save();
        }

        if (!material.hasFlag(NO_SMASHING)) {
            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(String.format("ring_%s", material.getName()),
                        stack.copyWithCount(1),
                        "h ", " X",
                        'X', new MaterialEntry(rod, material));
        } else {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_ring")
                    .inputItems(dust, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_RING)
                    .outputItems(stack)
                    .duration(mass << 1)
                    .EUt(6L * GTOUtils.getVoltageMultiplier(material))
                    .save();
        }
    }

    private static void processSpringSmall(Material material) {
        ItemStack springstack = ChemicalHelper.get(TagPrefix.springSmall, material);
        if (springstack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack stack = ChemicalHelper.get(rod, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("spring_small_%s", material.getName()),
                    springstack,
                    " s ", "fRx", 'R', stack);

        BENDER_RECIPES.recipeBuilder("bend_" + material.getName() + "_rod_to_small_spring")
                .duration(Math.max(1, mass / 2)).EUt(VA[ULV])
                .inputItems(stack)
                .outputItems(springstack.copyWithCount(2))
                .circuitMeta(1)
                .save();
    }

    private static void processSpring(Material material) {
        ItemStack springstack = ChemicalHelper.get(TagPrefix.spring, material);
        if (springstack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack stack = ChemicalHelper.get(rodLong, material);
        BENDER_RECIPES.recipeBuilder(material.getName() + "_long_rod_to_spring")
                .inputItems(stack)
                .outputItems(springstack)
                .circuitMeta(1)
                .duration(mass)
                .EUt(16)
                .save();

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("spring_%s", material.getName()),
                    springstack, " s ", "fRx", " R ", 'R', stack);
    }

    private static void processRotor(Material material) {
        ItemStack stack = ChemicalHelper.get(TagPrefix.rotor, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack curvedPlateStack = ChemicalHelper.get(GTOTagPrefix.CURVED_PLATE, material);
        ItemStack ringStack = ChemicalHelper.get(ring, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("rotor_%s", material.getName()), stack,
                    "ChC", "SRf", "CdC",
                    'C', curvedPlateStack,
                    'S', new MaterialEntry(screw, material),
                    'R', ringStack);

        if (material.hasFluid()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_to_rotor")
                    .notConsumable(GTItems.SHAPE_MOLD_ROTOR)
                    .inputFluids(material.getFluid(L * 5))
                    .outputItems(stack)
                    .duration(mass * 6)
                    .EUt(VA[LV])
                    .save();
        }

        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_rotor")
                .inputItems(ingot, material, 5)
                .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                .outputItems(stack)
                .duration(mass << 2)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                .save();

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_to_rotor")
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(ringStack)
                .circuitMeta(1)
                .outputItems(stack)
                .duration(mass)
                .EUt(30)
                .save();

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_rotor")
                    .inputItems(dust, material, 5)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                    .outputItems(stack)
                    .duration(mass << 2)
                    .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                    .save();
        }
    }

    private static void processBolt(@NotNull Material material) {
        if (!material.shouldGenerateRecipesFor(bolt)) {
            return;
        }

        ItemStack boltStack = ChemicalHelper.get(bolt,
                material.hasFlag(IS_MAGNETIC) && material.hasProperty(PropertyKey.INGOT) ?
                        material.getProperty(PropertyKey.INGOT).getMacerateInto() : material,
                8);
        ItemStack ingotStack = ChemicalHelper.get(ingot, material);

        CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_screw_to_bolt")
                .inputItems(screw, material)
                .outputItems(boltStack.copyWithCount(1))
                .duration(20)
                .EUt(24)
                .save();

        if (!boltStack.isEmpty() && !ingotStack.isEmpty()) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_bolt")
                    .inputItems(ingot, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_BOLT)
                    .outputItems(boltStack)
                    .duration(15)
                    .EUt((long) GTOUtils.getVoltageMultiplier(material) << 2)
                    .save();

            if (material.hasFlag(NO_SMASHING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_bolt")
                        .inputItems(dust, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_BOLT)
                        .outputItems(boltStack)
                        .duration(15)
                        .EUt((long) GTOUtils.getVoltageMultiplier(material) << 2)
                        .save();
            }
        }
    }

    private static void processStick(Material material) {
        ItemStack stack = ChemicalHelper.get(rod, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.GEM) || material.hasProperty(PropertyKey.INGOT)) {
            LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_to_rod")
                    .inputItems(material.hasProperty(PropertyKey.GEM) ? gem : ingot, material)
                    .outputItems(stack.copyWithCount(2))
                    .duration(mass << 1)
                    .EUt(GTOUtils.getVoltageMultiplier(material)).save();
        }

        if (material.hasFlag(GENERATE_BOLT_SCREW)) {
            ItemStack boltStack = ChemicalHelper.get(bolt, material, 4);
            CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_rod_to_bolt")
                    .inputItems(stack)
                    .outputItems(boltStack)
                    .duration(mass << 1)
                    .EUt(4)
                    .save();

            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(String.format("bolt_saw_%s", material.getName()), boltStack.copyWithCount(2), "s ", " X", 'X', stack);
        }
    }

    private static void processLongStick(Material material) {
        ItemStack stack = ChemicalHelper.get(TagPrefix.rodLong, material);
        if (stack.isEmpty()) return;
        ItemStack stickStack = ChemicalHelper.get(rod, material, 2);
        int mass = (int) material.getMass();
        RecipeBuilder builder = CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_long_rod_to_rod")
                .inputItems(stack)
                .duration(mass).EUt(4);
        if (ConfigHolder.INSTANCE.recipes.harderRods) {
            builder.outputItems(stickStack.copyWithCount(1));
            builder.outputItems(dustSmall, material, 2);
        } else {
            builder.outputItems(stickStack);
        }
        builder.save();

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("stick_long_%s", material.getName()), stickStack, "s", "X", 'X', stack);

        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("stick_long_stick_%s", material.getName()), stack, "ShS", 'S', stickStack.copyWithCount(1));

        if (!material.hasProperty(PropertyKey.WOOD)) {
            LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_rod_to_long_rod")
                    .inputItems(stickStack)
                    .circuitMeta(2)
                    .outputItems(stack)
                    .duration(mass)
                    .EUt(16)
                    .save();

            if (material.hasProperty(PropertyKey.INGOT)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_long_rod")
                        .inputItems(ingot, material)
                        .notConsumable(GTOItems.SHAPE_EXTRUDER_ROD_LONG)
                        .outputItems(stack)
                        .duration(mass)
                        .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                        .save();

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_long_rod")
                            .inputItems(dust, material)
                            .notConsumable(GTOItems.SHAPE_EXTRUDER_ROD_LONG)
                            .outputItems(stack)
                            .duration(mass)
                            .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                            .save();
                }
            }
        } else {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_long_rod")
                    .inputItems(dust, material)
                    .notConsumable(GTOItems.SHAPE_EXTRUDER_ROD_LONG)
                    .outputItems(stack)
                    .duration(mass)
                    .EUt((long) GTOUtils.getVoltageMultiplier(material) << 3)
                    .save();
        }
    }

    private static void processTurbine(Material material) {
        ItemStack stack = ChemicalHelper.get(turbineBlade, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        // noinspection ConstantConditions

        ASSEMBLER_RECIPES.recipeBuilder("assemble_" + material.getName() + "_turbine_blade")
                .inputItems(stack.copyWithCount(8))
                .inputItems(rodLong, GTMaterials.Magnalium)
                .outputItems(turbineRotor, material)
                .duration(200)
                .EUt(400)
                .save();

        FORMING_PRESS_RECIPES.recipeBuilder("press_" + material.getName() + "_turbine_rotor")
                .inputItems(plateDouble, material, 5)
                .inputItems(screw, material, 2)
                .outputItems(stack)
                .duration(mass << 4)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 2)
                .save();

        rotors.forEach(plating -> {
            if (plating == material) return;
            ItemStack rotorStack = ChemicalHelper.get(TagPrefix.turbineRotorCoated, material);
            CoatedTurbineRotorBehaviour.setCoatMaterial(rotorStack, plating);
            ELECTROPLATING_RECIPES.recipeBuilder("electroplate_%s_%s_turbine_rotor".formatted(material.getName(), plating.getName()))
                    .inputItems(turbineRotor, material)
                    .inputItems(plateDouble, plating, 5)
                    .inputFluids(DistilledWater.getFluid(1000))
                    .inputFluids(GTOMaterials.ChromicAcid.getFluid(250))
                    .outputItems(rotorStack)
                    .outputFluids(GTOMaterials.ChromicAcidWaste.getFluid(1000))
                    .duration((int) plating.getMass() << 4)
                    .EUt(480)
                    .cleanroom(CleanroomType.CLEANROOM)
                    .save();
        });
    }

    private static void processRound(Material material) {
        ItemStack stack1 = ChemicalHelper.get(round, material);
        if (stack1.isEmpty()) return;
        ItemStack stack = ChemicalHelper.get(nugget, material);
        if (!material.hasFlag(NO_SMASHING) && material.getMass() < 222 && material.getBlastTemperature() < 6000) {
            VanillaRecipeHelper.addShapedRecipe(String.format("round_%s", material.getName()),
                    stack1, "fN", "Nh", 'N', stack);

            VanillaRecipeHelper.addShapedRecipe(String.format("round_from_ingot_%s", material.getName()),
                    stack1.copyWithCount(4), "fIh", 'I', new MaterialEntry(ingot, material));
        }

        LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_nugget_to_round")
                .EUt(GTOUtils.getVoltageMultiplier(material)).duration(Math.max(1, (int) material.getMass() / 9))
                .inputItems(stack)
                .outputItems(stack1)
                .save();
    }

    private static void processManoswarm(Material material) {
        ItemStack stack = ChemicalHelper.get(NANITES, material);
        if (stack.isEmpty()) return;
        CHEMICAL_BATH_RECIPES.recipeBuilder(material.getName() + "_nano_bath")
                .inputFluids(GTOMaterials.PiranhaSolution.getFluid((int) (10000 * Math.sqrt((double) material.getMass() / GTOMaterials.Eternity.getMass()))))
                .inputItems(CONTAMINABLE_NANITES, material)
                .outputItems(stack)
                .duration((int) material.getMass() << 4)
                .EUt(480)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processcurvedPlate(Material material) {
        ItemStack stack = ChemicalHelper.get(CURVED_PLATE, material);
        if (stack.isEmpty()) return;
        int mass = (int) material.getMass();
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("curved_plate_%s", material.getName()),
                    stack, "hI", " h", 'I', plateStack);

        BENDER_RECIPES.recipeBuilder(material.getName() + "_curved_plate")
                .inputItems(plateStack)
                .outputItems(stack)
                .circuitMeta(1)
                .duration(mass)
                .EUt(16)
                .save();
    }

    private static void processMotorEnclosure(Material material) {
        int mass = (int) material.getMass();
        ItemStack motorEnclosureStack = ChemicalHelper.get(MOTOR_ENCLOSURE, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack ringStack = ChemicalHelper.get(ring, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("motor_enclosure_%s", material.getName()),
                    motorEnclosureStack, "SwS", "IRI", " h ", 'I', curvedPlateStack, 'S', new MaterialEntry(screw, material), 'R', ringStack);

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_motor_enclosure")
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(ringStack)
                .outputItems(motorEnclosureStack)
                .circuitMeta(3)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(motorEnclosureStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_motor_enclosure")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(motorEnclosureStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_motor_enclosure")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 1))
                .outputItems(motorEnclosureStack)
                .duration(mass << 1)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processPumpBarrel(Material material) {
        int mass = (int) material.getMass();
        ItemStack pumpBarrelStack = ChemicalHelper.get(PUMP_BARREL, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack ringStack = ChemicalHelper.get(ring, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("pump_barrel_%s", material.getName()),
                    pumpBarrelStack, "wIw", "ShS", "RIR", 'I', curvedPlateStack, 'R', ringStack, 'S', new MaterialEntry(screw, material));

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_pump_barrel")
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(ringStack.copyWithCount(2))
                .circuitMeta(4)
                .outputItems(pumpBarrelStack)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(pumpBarrelStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_pump_barrel")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(pumpBarrelStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_pump_barrel")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 5 / 2))
                .outputItems(pumpBarrelStack)
                .duration(mass * 5 / 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processPistonHousing(Material material) {
        int mass = (int) material.getMass();
        ItemStack pistonHousingStack = ChemicalHelper.get(PISTON_HOUSING, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("piston_housing_%s", material.getName()),
                    pistonHousingStack, "IhI", "SPS", "whw", 'I', curvedPlateStack, 'P', plateStack, 'S', new MaterialEntry(screw, material));

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_piston_housing")
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(plateStack)
                .circuitMeta(5)
                .outputItems(pistonHousingStack)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(pistonHousingStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_piston_housing")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(pistonHousingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_piston_housing")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 3))
                .outputItems(pistonHousingStack)
                .duration(mass * 3)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processEmitterBases(Material material) {
        int mass = (int) material.getMass();
        ItemStack emitterBasesStack = ChemicalHelper.get(EMITTER_BASES, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        ItemStack rodStack = ChemicalHelper.get(rod, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("emitter_base_%s", material.getName()),
                    emitterBasesStack, "SwS", "IPI", "RhR", 'I', curvedPlateStack, 'P', plateStack, 'R', rodStack, 'S', new MaterialEntry(screw, material));

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_emitter_base")
                .inputItems(curvedPlateStack.copyWithCount(2))
                .inputItems(rodStack.copyWithCount(2))
                .inputItems(plateStack)
                .outputItems(emitterBasesStack)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(emitterBasesStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_emitter_base")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(emitterBasesStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_emitter_base")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 2))
                .outputItems(emitterBasesStack)
                .duration(mass << 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processSensorCasing(Material material) {
        int mass = (int) material.getMass();
        ItemStack sensorCasingStack = ChemicalHelper.get(SENSOR_CASING, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack rodStack = ChemicalHelper.get(rod, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("sensor_casing_%s", material.getName()),
                    sensorCasingStack, "wIh", "IRI", "SIS", 'I', curvedPlateStack, 'R', rodStack, 'S', new MaterialEntry(screw, material));

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_sensor_casing")
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(rodStack.copyWithCount(1))
                .circuitMeta(6)
                .outputItems(sensorCasingStack)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(sensorCasingStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_sensor_casing")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(sensorCasingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_sensor_casing")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L * 9 / 2))
                .outputItems(sensorCasingStack)
                .duration(mass * 9 / 2)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processFieldGeneratorCasing(Material material) {
        int mass = (int) material.getMass();
        ItemStack fieldGeneratorCasingStack = ChemicalHelper.get(FIELD_GENERATOR_CASING, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(CURVED_PLATE, material);
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(String.format("field_generator_casing_%s", material.getName()),
                    fieldGeneratorCasingStack, "IPI", "PwP", "IPI", 'I', curvedPlateStack, 'P', plateStack);

        LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_field_generator_casing")
                .inputItems(curvedPlateStack.copyWithCount(4))
                .inputItems(plateStack.copyWithCount(4))
                .circuitMeta(7)
                .outputItems(fieldGeneratorCasingStack)
                .duration(mass)
                .EUt(30)
                .save();

        ItemStack data = GTOItems.DATA_DISC.get().getDisc(fieldGeneratorCasingStack);

        SCANNER_RECIPES.recipeBuilder(material.getName() + "_field_generator_casing")
                .inputItems(GTOItems.DATA_DISC.asItem())
                .inputItems(fieldGeneratorCasingStack)
                .outputItems(data)
                .EUt((long) GTOUtils.getVoltageMultiplier(material) << 4)
                .duration(mass)
                .save();

        THREE_DIMENSIONAL_PRINTER_RECIPES.recipeBuilder(material.getName() + "_field_generator_casing")
                .notConsumable(data)
                .inputFluids(material.getFluid(GTValues.L << 3))
                .outputItems(fieldGeneratorCasingStack)
                .duration(mass << 3)
                .EUt(16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processCatalyst(Material material) {
        ItemStack stack = ChemicalHelper.get(CATALYST, material);
        if (stack.isEmpty()) return;
        ASSEMBLER_RECIPES.recipeBuilder(material.getName() + "_catalyst")
                .inputItems(GTOItems.CATALYST_BASE.asItem())
                .inputItems(dust, material, 16)
                .outputItems(stack)
                .duration((int) material.getMass() << 2)
                .EUt(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }

    private static void processroughBlank(Material material) {
        ItemStack stack = ChemicalHelper.get(ROUGH_BLANK, material);
        if (stack.isEmpty()) return;
        ItemStack stack1 = ChemicalHelper.get(block, material);
        ItemStack stack2 = ChemicalHelper.get(BRICK, material);
        SINTERING_FURNACE_RECIPES.recipeBuilder(material.getName() + "_rough_blank")
                .inputItems(stack)
                .outputItems(stack1)
                .duration(400)
                .EUt(120)
                .blastFurnaceTemp(GTOMaterialBuilder.getTemp(material))
                .save();

        CUTTER_RECIPES.recipeBuilder(material.getName() + "_brick")
                .inputItems(stack1)
                .outputItems(stack2.copyWithCount(9))
                .duration(300)
                .EUt(120)
                .save();

        CUTTER_RECIPES.recipeBuilder(material.getName() + "_flakes")
                .inputItems(stack2)
                .outputItems(FLAKES, material, 4)
                .duration(200)
                .EUt(30)
                .save();
    }

    private static void processCrystallization(Material material) {
        ItemStack stack = ChemicalHelper.get(CRYSTAL_SEED, material, 2);
        ItemStack stack1 = ChemicalHelper.get(gemExquisite, material);
        if (stack1.isEmpty()) return;
        CUTTER_RECIPES.recipeBuilder("%s_gem".formatted(material.getName()))
                .inputItems(ARTIFICIAL_GEM, material)
                .outputItems(stack1)
                .outputItems(stack)
                .duration((int) (material.getMass() << 1))
                .EUt(16)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("%s_seed".formatted(material.getName()))
                .inputItems(stack1)
                .inputFluids(DistilledWater.getFluid(800))
                .outputItems(stack)
                .duration((int) (material.getMass() << 2))
                .EUt(7)
                .save();
    }
}
