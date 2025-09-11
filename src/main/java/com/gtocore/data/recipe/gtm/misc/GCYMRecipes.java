package com.gtocore.data.recipe.gtm.misc;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTItems.ELECTRIC_MOTOR_IV;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class GCYMRecipes {

    private GCYMRecipes() {}

    public static void init() {
        registerManualRecipes();
        registerMachineRecipes();
    }

    private static void registerManualRecipes() {
        registerFormulaic();
        registerManual();

        EXTRUDER_RECIPES.recipeBuilder("graphene_to_foil")
                .inputItems(dust, Graphene)
                .notConsumable(GTItems.SHAPE_EXTRUDER_FOIL)
                .outputItems(foil, Graphene, 4)
                .duration((int) Graphene.getMass())
                .EUt(24)
                .save();

        ELECTROPLATING_RECIPES.recipeBuilder("palladium_plating")
                .inputItems(plate, Palladium, 6)
                .inputItems(plateDouble, Rhodium, 1)
                .inputItems(dust, AmmoniumChloride, 2)
                .inputFluids(NitricAcid, 1000)
                .outputItems(ingot, RhodiumPlatedPalladium, 8)
                .outputFluids(DilutedHydrochloricAcid, 1000)
                .duration(2400).EUt(VA[IV])
                .save();
    }

    private static void registerMachineRecipes() {
        registerAssemblerRecipes();
        registerMixerRecipes();
    }

    private static void registerAssemblerRecipes() {
        ASSEMBLER_RECIPES.recipeBuilder("crushing_wheels")
                .inputItems(gearSmall, TungstenCarbide, 2)
                .inputItems(gear, Ultimet, 3)
                .inputItems(CASING_SECURE_MACERATION.asStack())
                .inputItems(ELECTRIC_MOTOR_IV.asStack())
                .outputItems(CRUSHING_WHEELS.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("slicing_blades")
                .inputItems(plate, TungstenCarbide, 2)
                .inputItems(gear, Ultimet, 3)
                .inputItems(CASING_SHOCK_PROOF.asStack())
                .inputItems(ELECTRIC_MOTOR_IV.asStack())
                .outputItems(SLICING_BLADES.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("electrolytic_cell")
                .inputItems(wireGtDouble, Platinum, 4)
                .inputItems(cableGtSingle, Tungsten, 1)
                .inputItems(CASING_NONCONDUCTING.asStack())
                .inputItems(CustomTags.IV_CIRCUITS)
                .outputItems(ELECTROLYTIC_CELL.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("mds_coil_block")
                .inputItems(ring, MolybdenumDisilicide, 32)
                .inputItems(foil, Graphene, 16)
                .inputFluids(HSLASteel, L)
                .outputItems(MOLYBDENUM_DISILICIDE_COIL_BLOCK.asStack(1))
                .duration(500).EUt(1920)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("heat_vent")
                .inputItems(plate, TantalumCarbide, 3)
                .inputItems(plateDouble, MolybdenumDisilicide, 2)
                .inputItems(rotor, Titanium, 1)
                .inputItems(rodLong, MolybdenumDisilicide, 1)
                .outputItems(HEAT_VENT.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_hsla_nonconducting")
                .inputItems(plate, HSLASteel, 6)
                .inputItems(frameGt, HSLASteel)
                .circuitMeta(6)
                .outputItems(CASING_NONCONDUCTING.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_incoloy_vibration_safe")
                .inputItems(plate, IncoloyMA956, 6)
                .inputItems(frameGt, IncoloyMA956)
                .circuitMeta(6)
                .outputItems(CASING_VIBRATION_SAFE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .EUt(16).duration(50)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_watertight")
                .inputItems(plate, WatertightSteel, 6)
                .inputItems(frameGt, WatertightSteel)
                .circuitMeta(6)
                .outputItems(CASING_WATERTIGHT.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_secure_maceration")
                .inputItems(plate, Zeron100, 6)
                .inputItems(frameGt, Titanium)
                .circuitMeta(6)
                .outputItems(CASING_SECURE_MACERATION.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .EUt(16).duration(50)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_high_temperature_smelting")
                .inputItems(plate, TitaniumCarbide, 4)
                .inputItems(plate, HSLASteel, 2)
                .inputItems(frameGt, TungstenCarbide)
                .circuitMeta(6)
                .outputItems(CASING_HIGH_TEMPERATURE_SMELTING.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_reaction_safe_mixing")
                .inputItems(TagPrefix.plate, GTMaterials.HastelloyX, 6)
                .inputItems(TagPrefix.frameGt, GTMaterials.MaragingSteel300)
                .circuitMeta(6)
                .outputItems(CASING_REACTION_SAFE.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_laser_safe_engraving")
                .inputItems(plate, TitaniumTungstenCarbide, 6)
                .inputItems(frameGt, Titanium).circuitMeta(6)
                .outputItems(CASING_LASER_SAFE_ENGRAVING.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_large_scale_assembling")
                .inputItems(plate, Stellite100, 6)
                .inputItems(frameGt, Tungsten)
                .circuitMeta(6)
                .outputItems(CASING_LARGE_SCALE_ASSEMBLING.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_shock_proof")
                .inputItems(plate, HastelloyC276, 6)
                .inputItems(frameGt, HastelloyC276)
                .circuitMeta(6)
                .outputItems(CASING_SHOCK_PROOF.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_corrosion_proof")
                .inputItems(plate, CobaltBrass, 6)
                .inputItems(frameGt, HSLASteel)
                .circuitMeta(6)
                .outputItems(CASING_CORROSION_PROOF.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("casing_stress_proof")
                .inputItems(plate, MaragingSteel300, 6)
                .inputItems(frameGt, StainlessSteel)
                .circuitMeta(6)
                .outputItems(CASING_STRESS_PROOF.asStack(ConfigHolder.INSTANCE.recipes.casingsPerCraft))
                .duration(50).EUt(16)
                .save();
    }

    private static void registerMixerRecipes() {
        MIXER_RECIPES.recipeBuilder("hsla_steel")
                .inputItems(dust, Invar, 2)
                .inputItems(dust, Vanadium)
                .inputItems(dust, Titanium)
                .inputItems(dust, Molybdenum)
                .outputItems(dust, HSLASteel, 5)
                .duration(140).EUt(VA[HV])
                .save();

        MIXER_RECIPES.recipeBuilder("molybdenum_disilicide")
                .inputItems(dust, Molybdenum)
                .inputItems(dust, Silicon, 2)
                .outputItems(dust, MolybdenumDisilicide, 3)
                .duration(180).EUt(VA[EV])
                .save();

        MIXER_RECIPES.recipeBuilder("titanium_carbide")
                .inputItems(dust, Titanium)
                .inputItems(dust, Carbon)
                .circuitMeta(3)
                .outputItems(dust, TitaniumCarbide, 2)
                .duration(160).EUt(VA[EV])
                .save();
    }

    private static void registerFormulaic() {
        registerBinaryAlloy(GTMaterials.Copper, 3, GTMaterials.Tin, 1,
                GTMaterials.Bronze, 4, 400);
        registerBinaryAlloy(GTMaterials.Copper, 3, GTMaterials.Zinc, 1,
                GTMaterials.Brass, 4, 400);
        registerBinaryAlloy(GTMaterials.Copper, 1, GTMaterials.Nickel, 1,
                GTMaterials.Cupronickel, 2, 200);
        registerBinaryAlloy(GTMaterials.Copper, 1, GTMaterials.Redstone, 4,
                GTMaterials.RedAlloy, 1, 100);

        registerBinaryAlloy(GTMaterials.Iron, 1, GTMaterials.Tin, 1,
                GTMaterials.TinAlloy, 2, 100);
        registerBinaryAlloy(GTMaterials.Iron, 2, GTMaterials.Nickel, 1,
                GTMaterials.Invar, 3, 300);
        registerBinaryAlloy(GTMaterials.Lead, 4, GTMaterials.Antimony, 1,
                GTMaterials.BatteryAlloy, 5, 250);
        registerBinaryAlloy(GTMaterials.Magnesium, 1, GTMaterials.Aluminium, 2,
                GTMaterials.Magnalium, 3, 150);
        registerBinaryAlloy(GTMaterials.Silver, 1, GTMaterials.Electrotine, 4,
                GTMaterials.BlueAlloy, 1, 100);
        registerBinaryAlloy(GTMaterials.Glass, 7, GTMaterials.Boron, 1,
                GTMaterials.BorosilicateGlass, 8, 200);

        registerTrinaryAlloy(GTMaterials.Brass, 7, GTMaterials.Aluminium, 1,
                GTMaterials.Cobalt, 1, GTMaterials.CobaltBrass, 9, 900);
        registerTrinaryAlloy(GTMaterials.Tin, 6, GTMaterials.Lead, 3,
                GTMaterials.Antimony, 1, GTMaterials.SolderingAlloy, 10, 200);
        registerTrinaryAlloy(GTMaterials.Copper, 6, GTMaterials.Tin, 2,
                GTMaterials.Lead, 1, GTMaterials.Potin, 9, 400);
    }

    private static void registerManual() {
        // NZF
        ALLOY_BLAST_RECIPES.recipeBuilder("nickel_zinc_ferrite")
                .inputItems(TagPrefix.dust, GTMaterials.Nickel)
                .inputItems(TagPrefix.dust, GTMaterials.Zinc)
                .inputItems(TagPrefix.dust, GTMaterials.Iron, 4)
                .circuitMeta(6)
                .inputFluids(GTMaterials.Oxygen.getFluid(8000))
                .outputFluids(GTMaterials.NickelZincFerrite.getFluid(GTValues.L * 6))
                .duration(2400 * 3 / 4)
                .EUt(GTValues.VA[GTValues.MV])
                .blastFurnaceTemp(1500)
                .save();
    }

    private static void registerBinaryAlloy(@NotNull Material input1, int input1Amount,
                                            @NotNull Material input2, int input2Amount,
                                            @NotNull Material output, int outputAmount,
                                            int duration) {
        ALLOY_BLAST_RECIPES.recipeBuilder(output.getName())
                .inputItems(TagPrefix.dust, input1, input1Amount)
                .inputItems(TagPrefix.dust, input2, input2Amount)
                .circuitMeta(input1Amount + input2Amount)
                .outputFluids(output.getFluid(GTValues.L * outputAmount))
                .duration(duration * 3 / 4)
                .EUt(16)
                .blastFurnaceTemp(output.getFluid().getFluidType().getTemperature())
                .save();
    }

    @SuppressWarnings("SameParameterValue")
    private static void registerTrinaryAlloy(@NotNull Material input1, int input1Amount,
                                             @NotNull Material input2, int input2Amount,
                                             @NotNull Material input3, int input3Amount,
                                             @NotNull Material output, int outputAmount,
                                             int duration) {
        ALLOY_BLAST_RECIPES.recipeBuilder(output.getName())
                .inputItems(TagPrefix.dust, input1, input1Amount)
                .inputItems(TagPrefix.dust, input2, input2Amount)
                .inputItems(TagPrefix.dust, input3, input3Amount)
                .circuitMeta(input1Amount + input2Amount + input3Amount)
                .outputFluids(output.getFluid(GTValues.L * outputAmount))
                .duration(duration * 3 / 4)
                .EUt(16)
                .blastFurnaceTemp(output.getFluid().getFluidType().getTemperature())
                .save();
    }
}
