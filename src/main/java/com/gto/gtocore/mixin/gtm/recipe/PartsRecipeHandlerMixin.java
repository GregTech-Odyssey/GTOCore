package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(PartsRecipeHandler.class)
public class PartsRecipeHandlerMixin {

    @Shadow(remap = false)
    private static int getVoltageMultiplier(Material material) {
        return 0;
    }

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
        rod.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processStick);
        rodLong.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processLongStick);
        plate.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandler::processPlate);
        plateDouble.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processPlateDouble);
        plateDense.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processPlateDense);

        turbineBlade.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processTurbine);
        rotor.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processRotor);
        bolt.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandler::processBolt);
        screw.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processScrew);
        wireFine.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processFineWire);
        foil.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processFoil);
        lens.executeHandler(provider, PropertyKey.GEM, PartsRecipeHandler::processLens);

        gear.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processGear);
        gearSmall.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processGear);
        ring.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processRing);
        springSmall.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processSpringSmall);
        spring.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processSpring);
        round.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processRound);
        GTOTagPrefix.contaminableManoswarm.executeHandler(provider, PropertyKey.DUST, PartsRecipeHandlerMixin::gTLCore$processManoswarm);
        GTOTagPrefix.curvedPlate.executeHandler(provider, PropertyKey.INGOT, PartsRecipeHandlerMixin::gTLCore$processcurvedPlate);
    }

    @Unique
    private static void gTLCore$processScrew(TagPrefix screwPrefix, Material material, DustProperty property,
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

    @Unique
    private static void gTLCore$processFoil(TagPrefix foilPrefix, Material material, IngotProperty property,
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
                    .duration(mass * 2)
                    .EUt(96)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processFineWire(TagPrefix fineWirePrefix, Material material, IngotProperty property,
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

    @Unique
    private static void gTLCore$processGear(TagPrefix gearPrefix, Material material, DustProperty property,
                                            Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack stack = ChemicalHelper.get(gearPrefix, material);
        if (gearPrefix == gear && material.hasProperty(PropertyKey.INGOT)) {
            int voltageMultiplier = getVoltageMultiplier(material);
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
                        .duration(mass * 2)
                        .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                        .save(provider);

                if (material.hasFlag(NO_SMASHING)) {
                    EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_small_gear")
                            .inputItems(dust, material)
                            .notConsumable(GTItems.SHAPE_EXTRUDER_GEAR_SMALL)
                            .outputItems(stack)
                            .duration(mass * 2)
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

    @Unique
    private static void gTLCore$processPlateDouble(TagPrefix doublePrefix, Material material, IngotProperty property,
                                                   Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasFlag(GENERATE_PLATE)) {
            if (!material.hasFlag(NO_SMASHING) && mass < 240 && material.getBlastTemperature() < 3600) {
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("plate_double_%s", material.getName()),
                        ChemicalHelper.get(doublePrefix, material),
                        "h", "P", "P", 'P', new UnificationEntry(plate, material));
            }

            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_double_plate")
                    .EUt(96).duration(mass * 2)
                    .inputItems(ingot, material, 2)
                    .outputItems(doublePrefix, material)
                    .circuitMeta(2)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processPlateDense(TagPrefix tagPrefix, Material material, DustProperty property,
                                                  Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.INGOT)) {
            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("bend_" + material.getName() + "_block_to_dense_plate")
                    .inputItems(block, material)
                    .outputItems(tagPrefix, material)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save(provider);
        } else {
            GTORecipeTypes.ROLLING_RECIPES.recipeBuilder("bend_" + material.getName() + "_plate_to_dense_plate")
                    .inputItems(plate, material, 9)
                    .outputItems(tagPrefix, material)
                    .duration(mass * 11)
                    .circuitMeta(3)
                    .EUt(96)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processRing(TagPrefix ringPrefix, Material material, IngotProperty property,
                                            Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_ring")
                .inputItems(ingot, material)
                .notConsumable(GTItems.SHAPE_EXTRUDER_RING)
                .outputItems(ringPrefix, material, 4)
                .duration(mass * 4)
                .EUt(6L * getVoltageMultiplier(material))
                .save(provider);

        if (material.hasFlag(GENERATE_ROD)) {
            BENDER_RECIPES.recipeBuilder("bender_" + material.getName() + "_rod_to_ring")
                    .inputItems(rod, material)
                    .outputItems(ringPrefix, material, 2)
                    .duration(mass * 2)
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
                    .duration(mass * 4)
                    .EUt(6L * getVoltageMultiplier(material))
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processSpringSmall(TagPrefix springPrefix, Material material, IngotProperty property,
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

    @Unique
    private static void gTLCore$processSpring(TagPrefix springPrefix, Material material, IngotProperty property,
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

    @Unique
    private static void gTLCore$processRotor(TagPrefix rotorPrefix, Material material, IngotProperty property,
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
                    .duration(mass * 4)
                    .EUt(20)
                    .save(provider);
        }

        EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_rotor")
                .inputItems(ingot, material, 5)
                .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                .outputItems(GTUtil.copy(stack))
                .duration(mass * 8)
                .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                .save(provider);

        GTORecipeTypes.LASER_WELDER_RECIPES.recipeBuilder(material.getName() + "_to_rotor")
                .inputItems(ringStack)
                .inputItems(curvedPlateStack.copyWithCount(4))
                .outputItems(GTUtil.copy(stack))
                .duration(mass)
                .EUt(30)
                .save(provider);

        if (material.hasFlag(NO_SMASHING)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_rotor")
                    .inputItems(dust, material, 5)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_ROTOR)
                    .outputItems(GTUtil.copy(stack))
                    .duration(mass * 8)
                    .EUt(material.getBlastTemperature() >= 2800 ? 256 : 64)
                    .save(provider);
        }
    }

    @Unique
    private static void gTLCore$processStick(TagPrefix stickPrefix, Material material, DustProperty property,
                                             Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        if (material.hasProperty(PropertyKey.GEM) || material.hasProperty(PropertyKey.INGOT)) {
            LATHE_RECIPES.recipeBuilder("lathe_" + material.getName() + "_to_rod")
                    .inputItems(material.hasProperty(PropertyKey.GEM) ? gem : ingot, material)
                    .outputItems(rod, material, 2)
                    .duration(mass * 2)
                    .EUt(16).save(provider);
        }

        if (material.hasFlag(GENERATE_BOLT_SCREW)) {
            ItemStack boltStack = ChemicalHelper.get(bolt, material);
            CUTTER_RECIPES.recipeBuilder("cut_" + material.getName() + "_rod_to_bolt")
                    .inputItems(stickPrefix, material)
                    .outputItems(GTUtil.copyAmount(4, boltStack))
                    .duration(mass * 2)
                    .EUt(4)
                    .save(provider);

            if (mass < 240 && material.getBlastTemperature() < 3600)
                VanillaRecipeHelper.addShapedRecipe(provider, String.format("bolt_saw_%s", material.getName()),
                        GTUtil.copyAmount(2, boltStack),
                        "s ", " X",
                        'X', new UnificationEntry(rod, material));
        }
    }

    @Unique
    private static void gTLCore$processLongStick(TagPrefix longStickPrefix, Material material, DustProperty property,
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
                .outputItems(stack)
                .duration(mass)
                .EUt(16)
                .save(provider);

        if (material.hasProperty(PropertyKey.INGOT)) {
            EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_ingot_to_long_rod")
                    .inputItems(ingot, material)
                    .notConsumable(GTItems.SHAPE_EXTRUDER_ROD_LONG)
                    .outputItems(stack)
                    .duration(mass * 2)
                    .EUt(64)
                    .save(provider);

            if (material.hasFlag(NO_SMASHING)) {
                EXTRUDER_RECIPES.recipeBuilder("extrude_" + material.getName() + "_dust_to_long_rod")
                        .inputItems(dust, material)
                        .notConsumable(GTItems.SHAPE_EXTRUDER_ROD_LONG)
                        .outputItems(stack)
                        .duration(mass * 2)
                        .EUt(64)
                        .save(provider);
            }
        }
    }

    @Unique
    private static void gTLCore$processTurbine(TagPrefix toolPrefix, Material material, IngotProperty property,
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
                .EUt(120)
                .save(provider);
    }

    @Unique
    private static void gTLCore$processRound(TagPrefix roundPrefix, Material material, IngotProperty property,
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

    @Unique
    private static void gTLCore$processManoswarm(TagPrefix roundPrefix, Material material, DustProperty property,
                                                 Consumer<FinishedRecipe> provider) {
        CHEMICAL_BATH_RECIPES.recipeBuilder(material.getName() + "_nano_bath")
                .inputFluids(GTOMaterials.PiranhaSolution.getFluid((long) (10000 * Math.sqrt((double) material.getMass() / GTOMaterials.Eternity.getMass()))))
                .inputItems(GTOTagPrefix.contaminableManoswarm, material)
                .outputItems(GTOTagPrefix.nanoswarm, material)
                .duration((int) material.getMass() * 16)
                .EUt(480)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }

    @Unique
    private static void gTLCore$processcurvedPlate(TagPrefix roundPrefix, Material material, IngotProperty property,
                                                   Consumer<FinishedRecipe> provider) {
        int mass = (int) material.getMass();
        ItemStack plateStack = ChemicalHelper.get(plate, material);
        ItemStack curvedPlateStack = ChemicalHelper.get(GTOTagPrefix.curvedPlate, material);
        if (mass < 240 && material.getBlastTemperature() < 3600)
            VanillaRecipeHelper.addShapedRecipe(provider, String.format("curved_plate_%s", material.getName()),
                    curvedPlateStack, "hI", 'I', plateStack);

        BENDER_RECIPES.recipeBuilder(material.getName() + "_curved_plate")
                .inputItems(plateStack)
                .outputItems(curvedPlateStack)
                .circuitMeta(1)
                .duration(mass)
                .EUt(16)
                .save(provider);
    }
}
