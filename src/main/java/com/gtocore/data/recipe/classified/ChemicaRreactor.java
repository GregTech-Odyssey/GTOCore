package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.LARGE_CHEMICAL_RECIPES;

final class ChemicaRreactor {

    public static void init() {
        CHEMICAL_RECIPES.builder("sodium_fluoride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 2)
                .inputFluids(GTMaterials.HydrofluoricAcid, 1000)
                .outputFluids(GTMaterials.Hydrogen, 1000)
                .circuitMeta(2)
                .EUt(30)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.builder("iron_sulfate")
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .outputItems(TagPrefix.dust, GTOMaterials.IronSulfate, 6)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .outputFluids(GTMaterials.Hydrogen, 2000)
                .circuitMeta(4)
                .EUt(30)
                .duration(250)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("betaionone")
                .inputFluids(GTMaterials.Acetone.getFluid(1000))
                .inputFluids(GTOMaterials.Citral.getFluid(1000))
                .outputFluids(GTOMaterials.BetaIonone.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(250)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("citral")
                .inputItems(TagPrefix.dust, GTOMaterials.BetaPinene, 26)
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .inputFluids(GTMaterials.Isoprene.getFluid(2000))
                .outputFluids(GTOMaterials.Citral.getFluid(2000))
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("beta_pinene")
                .outputItems(TagPrefix.dust, GTOMaterials.BetaPinene, 26)
                .inputFluids(GTOMaterials.Turpentine.getFluid(1000))
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(1000))
                .EUt(480)
                .duration(110)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("propargyl_chloride")
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.PropargylAlcohol.getFluid(1000))
                .outputFluids(GTOMaterials.PropargylChloride.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("propargyl_alcohol")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Copper)
                .inputFluids(GTOMaterials.Acetylene.getFluid(1000))
                .inputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .outputFluids(GTOMaterials.PropargylAlcohol.getFluid(1000))
                .EUt(120)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("propadiene")
                .inputFluids(GTMaterials.Butene.getFluid(1000))
                .inputFluids(GTMaterials.Propene.getFluid(1000))
                .outputFluids(GTMaterials.Butane.getFluid(1000))
                .outputFluids(GTOMaterials.Propadiene.getFluid(1000))
                .EUt(480)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("copper_nitrate")
                .inputItems(TagPrefix.dust, GTMaterials.Copper)
                .outputItems(TagPrefix.dust, GTOMaterials.CopperNitrate, 9)
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .EUt(120)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("barium_nitrate")
                .inputItems(TagPrefix.dust, GTMaterials.BariumSulfide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.BariumNitrate, 9)
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .EUt(120)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("yttrium_nitrate")
                .inputItems(TagPrefix.dust, GTOMaterials.YttriumOxide, 5)
                .outputItems(TagPrefix.dust, GTOMaterials.YttriumNitrate, 26)
                .inputFluids(GTMaterials.NitricAcid.getFluid(6000))
                .EUt(120)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("yttrium_oxide")
                .inputItems(TagPrefix.dust, GTMaterials.Yttrium, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.YttriumOxide, 5)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000))
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("phenolic_board")
                .inputItems(TagPrefix.plate, TreatedWood)
                .inputItems(TagPrefix.foil, GTMaterials.Lead, 4)
                .outputItems(GTItems.PHENOLIC_BOARD.asItem())
                .inputFluids(GTOMaterials.PhenolicResin.getFluid(144))
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(250))
                .EUt(30)
                .duration(300)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("coated_board")
                .inputItems(TagPrefix.plate, GTMaterials.Wood)
                .inputItems(GTItems.STICKY_RESIN.asStack(2))
                .outputItems(GTItems.COATED_BOARD.asItem())
                .inputFluids(GTMaterials.Oxygen.getFluid(100))
                .EUt(7)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("phenolic_resin")
                .notConsumable(TagPrefix.dust, GTMaterials.SodiumHydroxide)
                .inputFluids(GTMaterials.Phenol.getFluid(1000))
                .inputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTOMaterials.PhenolicResin.getFluid(1000))
                .EUt(30)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sapphire_sodium_aluminate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sapphire, 5)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumAluminate, 8)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(100)
                .heat(650)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("greensapphire_sodium_aluminate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.GreenSapphire, 5)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumAluminate, 8)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(100)
                .heat(650)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("aluminium_trifluoride")
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .inputFluids(GTMaterials.Fluorine.getFluid(3000))
                .circuitMeta(6)
                .EUt(120)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("gold_cyanide")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTOMaterials.GoldCyanide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Gold)
                .outputFluids(GTOMaterials.SodiumCyanide.getFluid(1000))
                .EUt(1920)
                .duration(140)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("gold_depleted_molybdenite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Molybdenite, 3)
                .inputFluids(GTOMaterials.SodiumCyanide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.GoldDepletedMolybdenite, 3)
                .outputFluids(GTOMaterials.GoldCyanide.getFluid(1000))
                .EUt(7680)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("formaldehyde")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Silver)
                .inputFluids(GTMaterials.Methanol.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("formaldehyde_a")
                .notConsumable(GTOTagPrefix.dust, GTMaterials.Silver)
                .inputFluids(GTMaterials.Methanol.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.Formaldehyde.getFluid(500))
                .EUt(30)
                .duration(300)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("bismuth_tellurite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Bismuth, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Tellurium, 3)
                .outputItems(TagPrefix.dust, GTOMaterials.BismuthTellurite, 5)
                .EUt(120)
                .duration(760)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ammonium_perrhenate")
                .inputFluids(GTOMaterials.RheniumSulfuricSolution.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputFluids(GTOMaterials.AmmoniumPerrhenate.getFluid(1000))
                .outputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .EUt(480)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("phosgene")
                .inputFluids(GTMaterials.CarbonMonoxide.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.Phosgene.getFluid(1000))
                .EUt(1920)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("thallium_chloride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Thallium)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.ThalliumChloride, 2)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("silver_oxide_dust")
                .notConsumable(TagPrefix.dust, GTMaterials.SodiumHydroxide)
                .inputItems(TagPrefix.dust, GTOMaterials.SilverChloride, 4)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverOxide, 3)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(2000))
                .EUt(30)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_bromide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Potassium)
                .inputFluids(GTMaterials.Bromine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumBromide, 2)
                .EUt(120)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_bisulfite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.PotassiumCarbonate, 6)
                .inputFluids(GTMaterials.SulfurDioxide.getFluid(2000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumBisulfite, 12)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .EUt(480)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("aniline")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Zinc)
                .inputFluids(GTMaterials.Nitrobenzene.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(3000))
                .outputFluids(GTOMaterials.Aniline.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(1920)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_perchlorate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumChlorate, 5)
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumPerchlorate, 6)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(480)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dimethyl_sulfide")
                .inputFluids(GTMaterials.Methanol.getFluid(2000))
                .inputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .outputFluids(GTOMaterials.DimethylSulfide.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(1920)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrosonium_tetrafluoroborate_dust")
                .inputFluids(GTOMaterials.BoronFluoride.getFluid(2000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(2000))
                .inputFluids(GTMaterials.DinitrogenTetroxide.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.NitrosoniumTetrafluoroborate, 7)
                .outputFluids(GTMaterials.NitricAcid.getFluid(1000))
                .EUt(120)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("trimethylchlorosilane")
                .inputFluids(GTMaterials.Methane.getFluid(1000))
                .inputFluids(GTMaterials.Dimethyldichlorosilane.getFluid(1000))
                .outputFluids(GTOMaterials.Trimethylchlorosilane.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(1920)
                .duration(110)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrogen_pentoxide")
                .inputFluids(GTOMaterials.Ozone.getFluid(1000))
                .inputFluids(GTMaterials.NitrogenDioxide.getFluid(6000))
                .outputFluids(GTOMaterials.NitrogenPentoxide.getFluid(3000))
                .EUt(480)
                .duration(240)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("butyl_lithium")
                .inputItems(TagPrefix.dust, GTMaterials.Lithium)
                .inputFluids(GTMaterials.Butane.getFluid(1000))
                .outputFluids(GTOMaterials.ButylLithium.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(480)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_fluoride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Potassium)
                .inputFluids(GTMaterials.Fluorine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumFluoride, 2)
                .EUt(30)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hexanitrohexaaxaisowurtzitane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CrudeHexanitrohexaaxaisowurtzitane, 36)
                .inputItems(TagPrefix.dust, GTOMaterials.SilicaGel, 3)
                .inputFluids(GTOMaterials.Ethylenediamine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Hexanitrohexaaxaisowurtzitane, 36)
                .EUt(1920)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dmap_dust")
                .inputFluids(GTOMaterials.Pyridine.getFluid(1000))
                .inputFluids(GTMaterials.Dimethylamine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.DMAP)
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(7680)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrous_acid")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumNitrite, 4)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumBisulfate, 7)
                .outputFluids(GTOMaterials.NitrousAcid.getFluid(1000))
                .EUt(30)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fluorite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Calcium)
                .inputFluids(GTMaterials.Fluorine.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.Fluorite, 3)
                .EUt(30)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("lithium_aluminium_hydride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumAluminiumHydride, 6)
                .inputItems(TagPrefix.dust, GTMaterials.LithiumChloride, 2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumHydride, 6)
                .outputFluids(GTMaterials.SaltWater.getFluid(1000))
                .EUt(480)
                .duration(320)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ammonium_nitrate_solution")
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.AmmoniumNitrateSolution.getFluid(1000))
                .EUt(120)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrated_triniite_compound_solution")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 36)
                .inputFluids(GTOMaterials.NitratedTriniiteCompoundSolution.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumTriniumHydroxides, 29)
                .outputItems(TagPrefix.dust, GTMaterials.SodiumSulfide, 12)
                .outputFluids(GTOMaterials.ResidualTriniiteSolution.getFluid(2000))
                .EUt(480)
                .duration(190)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ammonium_chloride_dust")
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 6)
                .EUt(30)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("germanium_dioxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Germanium)
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.GermaniumDioxide, 3)
                .EUt(120)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_hypochlorite_dust")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 6)
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumHypochlorite, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("zirconiu_hafnium_oxychloride")
                .inputFluids(GTOMaterials.ZirconiumHafniumChloride.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTOMaterials.ZirconiumHafniumOxychloride.getFluid(1000))
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(2000))
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hexamethylenetetramine_dust")
                .circuitMeta(1)
                .inputFluids(GTMaterials.Formaldehyde.getFluid(4000))
                .inputFluids(GTMaterials.Ammonia.getFluid(6000))
                .outputItems(TagPrefix.dust, GTOMaterials.Hexamethylenetetramine, 22)
                .outputFluids(GTMaterials.Water.getFluid(6000))
                .EUt(30)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fluoroboric_acide")
                .inputFluids(GTOMaterials.BoricAcid.getFluid(1000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(4000))
                .outputFluids(GTOMaterials.FluoroboricAcid.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(13000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("titanium_tetrafluoride")
                .inputFluids(GTMaterials.TitaniumTetrachloride.getFluid(1000))
                .inputFluids(GTMaterials.Fluorine.getFluid(4000))
                .outputFluids(GTMaterials.Chlorine.getFluid(4000))
                .outputFluids(GTOMaterials.TitaniumTetrafluoride.getFluid(1000))
                .EUt(1920)
                .duration(800)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("isochloropropane")
                .inputFluids(GTMaterials.Propane.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.Isochloropropane.getFluid(1000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .EUt(30)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potash_dust")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Potassium, 2)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Potash, 3)
                .EUt(7)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("pyromellitic_dianhydride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Durene, 24)
                .inputFluids(GTMaterials.Oxygen.getFluid(12000))
                .outputItems(TagPrefix.dust, GTOMaterials.PyromelliticDianhydride, 18)
                .outputFluids(GTMaterials.Water.getFluid(6000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("uranium_sulfate_waste_solution")
                .inputItems(TagPrefix.dustImpure, GTMaterials.Uraninite)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Uraninite)
                .outputFluids(GTOMaterials.UraniumSulfateWasteSolution.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("quantum_dots")
                .inputItems(TagPrefix.dust, GTMaterials.Selenium)
                .inputItems(TagPrefix.dust, GTMaterials.Cadmium)
                .inputFluids(GTOMaterials.StearicAcid.getFluid(1000))
                .inputFluids(GTOMaterials.Tricotylphosphine.getFluid(1000))
                .outputFluids(GTOMaterials.QuantumDots.getFluid(1000))
                .EUt(5000000)
                .duration(160)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("benzyl_chloride")
                .notConsumable(GTItems.BLACKLIGHT.asItem())
                .inputFluids(GTMaterials.Toluene.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.BenzylChloride.getFluid(1000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hafnium_chloride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.HafniumOxide, 3)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputFluids(GTMaterials.Chlorine.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.HafniumChloride, 5)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("acetylene")
                .inputItems(TagPrefix.dust, GTOMaterials.CalciumCarbide, 3)
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.CalciumHydroxide, 5)
                .outputFluids(GTOMaterials.Acetylene.getFluid(1000))
                .EUt(7)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dibromomethylbenzene")
                .inputFluids(GTMaterials.Bromine.getFluid(2000))
                .inputFluids(GTMaterials.Toluene.getFluid(1000))
                .outputFluids(GTOMaterials.Dibromomethylbenzene.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(30)
                .duration(600)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tungsten_trioxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.TungstenCarbide, 2)
                .inputFluids(GTMaterials.Oxygen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.TungstenTrioxide, 4)
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("lithium_thiinediselenide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Selenium, 2)
                .inputFluids(GTOMaterials.Bromodihydrothiine.getFluid(1000))
                .inputFluids(GTOMaterials.ButylLithium.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumThiinediselenide, 14)
                .outputFluids(GTOMaterials.Bromobutane.getFluid(2000))
                .EUt(30720)
                .duration(290)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylamine")
                .notConsumable(TagPrefix.dust, GTOMaterials.SodiumAzanide)
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTMaterials.Ethylene.getFluid(1000))
                .outputFluids(GTOMaterials.Ethylamine.getFluid(1000))
                .EUt(480)
                .duration(130)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dibismuthhydroborat_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Bismuth, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Boron)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.DibismuthHydroborate, 4)
                .EUt(90)
                .duration(590)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_cyanide")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
                .inputFluids(GTMaterials.HydrogenCyanide.getFluid(2000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTOMaterials.SodiumCyanide.getFluid(2000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(1920)
                .duration(12)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("boron_fluoride")
                .inputItems(TagPrefix.dust, GTOMaterials.BoronTrioxide, 5)
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(6000))
                .outputFluids(GTOMaterials.BoronFluoride.getFluid(2000))
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("zirconium_hafnium_chloride")
                .inputItems(TagPrefix.dust, GTOMaterials.Zircon, 6)
                .inputFluids(GTMaterials.Chlorine.getFluid(8000))
                .outputFluids(GTOMaterials.ZirconiumHafniumChloride.getFluid(1000))
                .outputFluids(GTOMaterials.ZirconChlorinatingResidue.getFluid(1000))
                .EUt(120)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("benzenediazonium_tetrafluoroborate")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumNitrite, 4)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.FluoroboricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTOMaterials.BenzenediazoniumTetrafluoroborate.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(130)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("californium_cyclopentadienide")
                .inputItems(TagPrefix.dust, GTOMaterials.CaliforniumTrichloride, 4)
                .inputFluids(GTOMaterials.LithiumCyclopentadienide.getFluid(3000))
                .outputFluids(GTOMaterials.CaliforniumCyclopentadienide.getFluid(1000))
                .EUt(2000000)
                .duration(160)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("succinic_anhydride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinicAcid, 14)
                .inputFluids(GTMaterials.AceticAnhydride.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SuccinicAnhydride, 11)
                .outputFluids(GTMaterials.AceticAcid.getFluid(2000))
                .EUt(7680)
                .duration(20)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tetrahydrofuran")
                .inputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .inputFluids(GTMaterials.Formaldehyde.getFluid(2000))
                .inputFluids(GTOMaterials.Acetylene.getFluid(1000))
                .outputFluids(GTOMaterials.Tetrahydrofuran.getFluid(1000))
                .EUt(7680)
                .duration(75)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_hydroxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 2)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(30)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fluorotoluene")
                .notConsumable(GTItems.BLACKLIGHT.asItem())
                .inputFluids(GTMaterials.FluoroantimonicAcid.getFluid(1000))
                .inputFluids(GTMaterials.Methane.getFluid(1000))
                .inputFluids(GTOMaterials.FluoroBenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 4)
                .outputFluids(GTOMaterials.Fluorotoluene.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .EUt(480)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("antimony_trifluoride_dust_a")
                .inputItems(TagPrefix.dust, GTOMaterials.AntimonyTrichloride, 4)
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 4)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(3000))
                .EUt(480)
                .duration(210)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hydrobromic_acid")
                .inputFluids(GTMaterials.Bromine.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputFluids(GTOMaterials.HydrobromicAcid.getFluid(1000))
                .EUt(480)
                .duration(300)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("acrylic_acid")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .inputFluids(GTMaterials.AllylChloride.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTOMaterials.AcrylicAcid.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("n_hydroxysuccinimide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinicAnhydride, 66)
                .inputFluids(GTMaterials.Methanol.getFluid(40000))
                .inputFluids(GTMaterials.Toluene.getFluid(6000))
                .inputFluids(GTOMaterials.HydroxylamineHydrochloride.getFluid(6000))
                .outputItems(TagPrefix.dust, GTOMaterials.NHydroxysuccinimide, 13)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 12)
                .outputFluids(GTMaterials.Water.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .EUt(1920)
                .duration(220)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("caesium_iodide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Caesium)
                .inputItems(TagPrefix.dust, GTMaterials.Iodine)
                .outputItems(TagPrefix.dust, GTOMaterials.CaesiumIodide, 2)
                .EUt(30)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethyleneglycol")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.EthyleneOxide.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTOMaterials.EthyleneGlycol.getFluid(1000))
                .EUt(480)
                .duration(300)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("butane_1_4_diol")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.MolybdenumTrioxide)
                .inputItems(TagPrefix.dust, GTOMaterials.TelluriumOxide, 3)
                .inputFluids(GTMaterials.Butane.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Tellurium)
                .outputFluids(GTOMaterials.Butane14Diol.getFluid(1000))
                .EUt(1920)
                .duration(20)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("thionyl_chloride")
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .inputFluids(GTMaterials.SulfurTrioxide.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.ThionylChloride.getFluid(1000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("bromo_succinimide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Succinimide, 12)
                .inputFluids(GTMaterials.Bromine.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.BromoSuccinamide, 12)
                .outputFluids(GTOMaterials.HydrobromicAcid.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("perfluorobenzene")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Rhenium)
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumFluoride, 12)
                .inputFluids(GTMaterials.Benzene.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(6000))
                .outputItems(TagPrefix.dust, GTMaterials.RockSalt, 12)
                .outputFluids(GTOMaterials.Perfluorobenzene.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .EUt(120)
                .duration(185)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_nitrite_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.CoAcAbCatalyst)
                .inputFluids(GTOMaterials.SodiumNitrateSolution.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumNitrite, 4)
                .outputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(30)
                .duration(300)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("antimony_pentafluoride")
                .inputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 4)
                .inputFluids(GTMaterials.Fluorine.getFluid(2000))
                .outputFluids(GTOMaterials.AntimonyPentafluoride.getFluid(1000))
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("rhenium_dust")
                .inputFluids(GTOMaterials.AmmoniumPerrhenate.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(7000))
                .outputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .EUt(1920)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethyl_acrylate")
                .inputFluids(GTOMaterials.AcrylicAcid.getFluid(1000))
                .inputFluids(GTMaterials.Ethanol.getFluid(1000))
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.EthylAcrylate.getFluid(1000))
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(1000))
                .EUt(120)
                .duration(600)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("iodine_monochloride")
                .inputItems(TagPrefix.dust, GTMaterials.Iodine)
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .outputFluids(GTOMaterials.IodineMonochloride.getFluid(1000))
                .EUt(120)
                .duration(260)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("methylamine")
                .circuitMeta(1)
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTMaterials.Methanol.getFluid(1000))
                .outputFluids(GTOMaterials.Methylamine.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(1920)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_toluenesulfonate")
                .inputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Toluene.getFluid(1000))
                .outputFluids(GTOMaterials.SodiumToluenesulfonate.getFluid(1000))
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(1000))
                .EUt(950)
                .duration(220)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("acetyl_chloride")
                .inputFluids(GTMaterials.Ethenone.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.AcetylChloride.getFluid(1000))
                .EUt(120)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_hydroxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Potassium)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.PotassiumHydroxide, 2)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(30)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("silver_tetrafluoroborate")
                .inputItems(TagPrefix.dust, GTMaterials.Silver, 6)
                .inputFluids(GTOMaterials.BoronFluoride.getFluid(2000))
                .inputFluids(GTMaterials.Oxygen.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.BoronTrioxide, 5)
                .outputFluids(GTOMaterials.SilverTetrafluoroborate.getFluid(6000))
                .EUt(122880)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dichloromethane")
                .inputFluids(GTMaterials.Chloromethane.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.Dichloromethane.getFluid(1000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .EUt(30)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("acrylonitrile")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Platinum)
                .inputFluids(GTMaterials.Oxygen.getFluid(3000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTMaterials.Propene.getFluid(1000))
                .outputFluids(GTOMaterials.Acrylonitrile.getFluid(2000))
                .outputFluids(GTMaterials.Water.getFluid(3000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tetraethylammonium_bromide")
                .inputFluids(GTMaterials.Ethylene.getFluid(4000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTOMaterials.HydrobromicAcid.getFluid(1000))
                .outputFluids(GTOMaterials.TetraethylammoniumBromide.getFluid(1000))
                .EUt(1920)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("actinium_oxalate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Actinium)
                .inputFluids(GTOMaterials.OxalicAcid.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumOxalate, 13)
                .EUt(1920)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("n_difluorophenylpyrrole")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.PhosphorusPentoxide)
                .inputFluids(GTOMaterials.Succinaldehyde.getFluid(1000))
                .inputFluids(GTOMaterials.Difluoroaniline.getFluid(1000))
                .outputFluids(GTOMaterials.NDifluorophenylpyrrole.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(480)
                .duration(180)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("aluminium_hydride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .inputFluids(GTMaterials.Hydrogen.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumHydride, 4)
                .EUt(30)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("borane_dimethyl_sulfide")
                .inputFluids(GTOMaterials.Diborane.getFluid(1000))
                .inputFluids(GTOMaterials.DimethylSulfide.getFluid(2000))
                .outputFluids(GTOMaterials.BoraneDimethylSulfide.getFluid(2000))
                .EUt(125)
                .duration(165)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("highly_reflective_mirror")
                .inputItems(TagPrefix.plate, GTMaterials.Germanium)
                .inputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .inputFluids(GTMaterials.Zinc.getFluid(144))
                .outputItems(GTOItems.HIGHLY_REFLECTIVE_MIRROR.asItem())
                .EUt(710000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hydroiodic_acid")
                .inputItems(TagPrefix.dust, GTMaterials.Iodine, 4)
                .inputFluids(GTOMaterials.Monomethylhydrazine.getFluid(1000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(2000))
                .outputFluids(GTOMaterials.HydroiodicAcid.getFluid(4000))
                .EUt(500)
                .duration(210)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("soap_2")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .inputFluids(GTMaterials.Steam.getFluid(2000))
                .inputFluids(GTMaterials.SeedOil.getFluid(100))
                .outputFluids(GTOMaterials.Soap.getFluid(1000))
                .EUt(2000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("soap_1")
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .inputFluids(GTMaterials.Steam.getFluid(2000))
                .inputFluids(GTMaterials.FishOil.getFluid(100))
                .outputFluids(GTOMaterials.Soap.getFluid(1000))
                .EUt(2000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tert_butanol")
                .notConsumable(TagPrefix.dust, GTMaterials.MagnesiumChloride)
                .inputItems(TagPrefix.dust, GTOMaterials.ZeoliteSievingPellets)
                .inputFluids(GTMaterials.Methane.getFluid(1000))
                .inputFluids(GTMaterials.Acetone.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.WetZeoliteSievingPellets)
                .outputFluids(GTOMaterials.TertButanol.getFluid(1000))
                .EUt(120)
                .duration(126)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_oxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
                .circuitMeta(1)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumOxide, 3)
                .EUt(7)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("titanium_50_tetrachloride")
                .inputFluids(GTOMaterials.Titanium50Tetrafluoride.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(4000))
                .outputFluids(GTOMaterials.Titanium50Tetrachloride.getFluid(1000))
                .EUt(480)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("crude_hexanitrohexaaxaisowurtzitane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Tetraacetyldinitrosohexaazaisowurtzitane, 46)
                .inputItems(TagPrefix.dust, GTOMaterials.NitroniumTetrafluoroborate, 48)
                .inputFluids(GTMaterials.Water.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.CrudeHexanitrohexaaxaisowurtzitane, 36)
                .outputItems(TagPrefix.dust, GTOMaterials.NitrosoniumTetrafluoroborate, 14)
                .outputFluids(GTOMaterials.FluoroboricAcid.getFluid(4000))
                .outputFluids(GTMaterials.AceticAcid.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("prasiolite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Silicon, 5)
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputFluids(GTMaterials.Oxygen.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.Prasiolite)
                .EUt(480)
                .duration(270)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ditertbutyl_dicarbonate_dust")
                .notConsumableFluid(GTOMaterials.SodiumToluenesulfonate.getFluid(1000))
                .inputFluids(GTOMaterials.TertButanol.getFluid(2000))
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.DitertbutylDicarbonate, 33)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(480)
                .duration(260)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_hydroxylaminedisulfonate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumBisulfite, 12)
                .inputFluids(GTOMaterials.NitrousAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumHydroxylaminedisulfonate, 13)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(1920)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylanthrahydroquinone")
                .inputFluids(GTOMaterials.Ethylanthraquinone.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .outputFluids(GTOMaterials.Ethylanthrahydroquinone.getFluid(1000))
                .EUt(30)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylene_oxide")
                .circuitMeta(6)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .inputFluids(GTMaterials.Ethylene.getFluid(1000))
                .outputFluids(GTOMaterials.EthyleneOxide.getFluid(1000))
                .EUt(480)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("silver_nitrate_dust_chemical_compound")
                .inputItems(TagPrefix.dust, GTOMaterials.SilverOxide, 3)
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverNitrate, 10)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(125)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hydroxylammonium_sulfate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumHydroxylaminedisulfonate, 26)
                .inputFluids(GTMaterials.Water.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.HydroxylammoniumSulfate, 17)
                .outputItems(TagPrefix.dust, GTMaterials.PotassiumSulfate, 14)
                .outputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .EUt(1920)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("diborane")
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumHydride, 18)
                .inputFluids(GTOMaterials.BoronFluoride.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumFluoride, 18)
                .outputFluids(GTOMaterials.Diborane.getFluid(2000))
                .EUt(30)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fuming_nitric_acid")
                .inputFluids(GTMaterials.NitrogenDioxide.getFluid(1000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.FumingNitricAcid.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylanthraquinone")
                .inputItems(TagPrefix.dust, GTOMaterials.PhthalicAnhydride, 15)
                .inputFluids(GTMaterials.Ethylbenzene.getFluid(1000))
                .outputFluids(GTOMaterials.Ethylanthraquinone.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(480)
                .duration(800)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("succinic_acid_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.RhodiumTriphenylphosphineChloride)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .inputFluids(GTOMaterials.MaleicAnhydride.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.SuccinicAcid, 14)
                .EUt(1920)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("rhodium_triphenylphosphine_chloride")
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium, 1)
                .inputFluids(GTOMaterials.Triphenylphosphine.getFluid(3000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.RhodiumTriphenylphosphineChloride, 1)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(1920)
                .duration(300)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("lithium_chloride_dust")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Lithium)
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.LithiumChloride, 2)
                .EUt(120)
                .duration(125)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("carbon_tetrachloride")
                .circuitMeta(4)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputFluids(GTMaterials.Chlorine.getFluid(4000))
                .outputFluids(GTOMaterials.CarbonTetrachloride.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dimethylnaphthalene")
                .inputFluids(GTMaterials.Methanol.getFluid(2000))
                .inputFluids(GTMaterials.Naphthalene.getFluid(1000))
                .outputFluids(GTOMaterials.Dimethylnaphthalene.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("titanium_tetrachloride_1")
                .inputItems(TagPrefix.dust, GTMaterials.Titanium)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputFluids(GTMaterials.TitaniumTetrachloride.getFluid(1000))
                .EUt(480)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_ethylxanthate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylate, 9)
                .inputFluids(GTMaterials.Ethanol.getFluid(1000))
                .inputFluids(GTOMaterials.CarbonDisulfide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 12)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(480)
                .duration(40)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("succinimidyl_acetate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.NHydroxysuccinimide, 13)
                .inputFluids(GTMaterials.AceticAnhydride.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SuccinamidylAcetate, 18)
                .outputFluids(GTMaterials.AceticAcid.getFluid(1000))
                .EUt(7680)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_hydride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 2)
                .EUt(30)
                .duration(140)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("boron_francium_carbide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.FranciumCarbide, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.BoronCarbide, 7)
                .outputItems(TagPrefix.dust, GTOMaterials.BoronFranciumCarbide, 15)
                .EUt(7680)
                .duration(900)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("p_nitroaniline")
                .notConsumableFluid(GTMaterials.AceticAnhydride.getFluid(1000))
                .inputFluids(GTOMaterials.Aniline.getFluid(1000))
                .inputFluids(GTMaterials.NitrationMixture.getFluid(2000))
                .outputFluids(GTOMaterials.PNitroaniline.getFluid(1000))
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(1000))
                .EUt(1920)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("attuned_tengam_ingot")
                .inputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .inputItems(TagPrefix.dust, GTMaterials.SamariumMagnetic)
                .inputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 144))
                .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.AttunedTengam)
                .EUt(7864320)
                .duration(1600)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("rare_earth_chlorides")
                .inputFluids(GTOMaterials.RareEarthHydroxides.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .outputFluids(GTOMaterials.RareEarthChlorides.getFluid(1000))
                .EUt(30)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethanolamine")
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTOMaterials.EthyleneOxide.getFluid(1000))
                .outputFluids(GTOMaterials.Ethanolamine.getFluid(1000))
                .EUt(7680)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("aluminium_trifluoride_dust_a")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .outputFluids(GTOMaterials.SodiumHexafluoroaluminate.getFluid(1000))
                .EUt(120)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("oxydianiline")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Tin)
                .inputFluids(GTOMaterials.Aniline.getFluid(2000))
                .inputFluids(GTMaterials.Phenol.getFluid(1000))
                .outputFluids(GTOMaterials.Oxydianiline.getFluid(1000))
                .outputFluids(GTMaterials.Methane.getFluid(2000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("maleic_anhydride")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Bismuth)
                .inputFluids(GTMaterials.Oxygen.getFluid(7000))
                .inputFluids(GTMaterials.Butane.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .outputFluids(GTOMaterials.MaleicAnhydride.getFluid(1000))
                .EUt(480)
                .duration(280)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("molybdenum_concentrate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.GoldDepletedMolybdenite, 3)
                .inputFluids(GTMaterials.Iron3Chloride.getFluid(250))
                .outputItems(TagPrefix.dust, GTOMaterials.MolybdenumConcentrate, 4)
                .EUt(1920)
                .duration(10)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tungsten_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.TungstenTrioxide, 4)
                .inputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .outputItems(TagPrefix.dust, GTMaterials.Tungsten)
                .outputFluids(GTMaterials.Water.getFluid(3000))
                .EUt(120)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("lithium_cyclopentadienide")
                .inputFluids(GTOMaterials.ButylLithium.getFluid(1000))
                .inputFluids(GTOMaterials.Dimethoxyethane.getFluid(500))
                .inputFluids(GTOMaterials.Cyclopentadiene.getFluid(1000))
                .outputFluids(GTOMaterials.LithiumCyclopentadienide.getFluid(1000))
                .outputFluids(GTMaterials.Butane.getFluid(1000))
                .EUt(10000)
                .duration(460)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("barium_chloride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Barium, 6)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.BariumChloride, 3)
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(120)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("durene_dust")
                .inputFluids(GTMaterials.Chloromethane.getFluid(2000))
                .inputFluids(GTMaterials.Dimethylbenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Durene, 24)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylenediamine")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Palladium)
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTOMaterials.Ethanolamine.getFluid(1000))
                .outputFluids(GTOMaterials.Ethylenediamine.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(180)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("boric_acide")
                .inputItems(TagPrefix.dust, GTMaterials.Borax, 23)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 4)
                .outputFluids(GTOMaterials.BoricAcid.getFluid(4000))
                .outputFluids(GTMaterials.Water.getFluid(5000))
                .EUt(120)
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_thiocyanate")
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .inputFluids(GTOMaterials.SodiumCyanide.getFluid(1000))
                .outputFluids(GTOMaterials.SodiumThiocyanate.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("terephthalicacid")
                .notConsumable(TagPrefix.dust, GTMaterials.Cadmium)
                .notConsumable(TagPrefix.dust, GTMaterials.SodiumBisulfate)
                .notConsumableFluid(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTMaterials.PhthalicAcid.getFluid(1000))
                .outputFluids(GTOMaterials.TerephthalicAcid.getFluid(1000))
                .EUt(480)
                .duration(800)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("silver_chloride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Silver)
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverChloride, 2)
                .EUt(120)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("pyridine")
                .inputFluids(GTMaterials.Formaldehyde.getFluid(2000))
                .inputFluids(GTOMaterials.Acetaldehyde.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputFluids(GTOMaterials.Pyridine.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(1920)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("germanium_tetrachloride_solution")
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumAsh, 2)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .outputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hydrogen_peroxide")
                .notConsumableFluid(GTOMaterials.Anthracene.getFluid(1000))
                .inputFluids(GTOMaterials.Ethylanthrahydroquinone.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .outputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .outputFluids(GTOMaterials.Ethylanthraquinone.getFluid(1000))
                .EUt(480)
                .duration(600)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("krypton_difluoride")
                .notConsumable(GTItems.BLACKLIGHT.asItem())
                .inputFluids(GTMaterials.Fluorine.getFluid(2000))
                .inputFluids(GTMaterials.Krypton.getFluid(1000))
                .outputFluids(GTOMaterials.KryptonDifluoride.getFluid(1000))
                .EUt(480)
                .duration(190)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_azanide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumAzanide, 4)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(120)
                .duration(110)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("cyclooctadiene")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Nickel)
                .inputFluids(GTMaterials.Butadiene.getFluid(2000))
                .outputFluids(GTOMaterials.Cyclooctadiene.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("phosphorus_trichloride")
                .inputItems(TagPrefix.dust, GTMaterials.Phosphorus)
                .inputFluids(GTMaterials.Chlorine.getFluid(3000))
                .outputFluids(GTOMaterials.PhosphorusTrichloride.getFluid(1000))
                .EUt(30)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dimethylterephthalate")
                .inputFluids(GTOMaterials.TerephthalicAcid.getFluid(1000))
                .inputFluids(GTMaterials.Methanol.getFluid(2000))
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(2000))
                .outputFluids(GTOMaterials.DimethylTerephthalate.getFluid(1000))
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(2000))
                .EUt(480)
                .duration(210)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dimethylether")
                .notConsumable(TagPrefix.dust, GTMaterials.SiliconDioxide)
                .inputFluids(GTMaterials.Methanol.getFluid(2000))
                .outputFluids(GTOMaterials.Dimethylether.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(8000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nmethylpyrolidone")
                .inputFluids(GTOMaterials.GammaButyrolactone.getFluid(1000))
                .inputFluids(GTOMaterials.Methylamine.getFluid(1000))
                .outputFluids(GTOMaterials.NMPyrolidone.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(7680)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hexabenzylhexaazaisowurtzitane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Acetonitrile, 6)
                .inputFluids(GTOMaterials.Benzylamine.getFluid(6000))
                .inputFluids(GTOMaterials.Glyoxal.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.Hexabenzylhexaazaisowurtzitane, 102)
                .EUt(7680)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dichlorodicyanobenzoquinone_1")
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .inputFluids(GTOMaterials.Dichlorodicyanohydroquinone.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .outputFluids(GTOMaterials.Dichlorodicyanobenzoquinone.getFluid(1000))
                .EUt(480)
                .duration(250)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("trifluoroacetic_phosphate_ester_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylate, 9)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.EthyleneSulfide.getFluid(1000))
                .inputFluids(GTOMaterials.EthylTrifluoroacetate.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.TrifluoroaceticPhosphateEster, 4)
                .outputFluids(GTMaterials.Ethanol.getFluid(2000))
                .EUt(1920)
                .duration(220)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("selenium_oxide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SeleniumOxide, 3)
                .inputFluids(GTMaterials.SulfurDioxide.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.Selenium)
                .outputFluids(GTMaterials.SulfurTrioxide.getFluid(2000))
                .EUt(120)
                .duration(260)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("isopropyl_alcohol")
                .notConsumable(TagPrefix.dust, GTMaterials.Tungstate)
                .notConsumable(TagPrefix.dust, GTOMaterials.SodiumSeaborgate)
                .inputFluids(GTMaterials.Propene.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTOMaterials.IsopropylAlcohol.getFluid(1000))
                .EUt(480)
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitronium_tetrafluoroborate_dust")
                .inputFluids(GTOMaterials.BoronFluoride.getFluid(1000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(1000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.NitroniumTetrafluoroborate, 8)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(1920)
                .duration(40)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ethylene_sulfide")
                .inputItems(TagPrefix.dust, GTOMaterials.PhosphorusPentasulfide, 7)
                .inputFluids(GTOMaterials.AcetylChloride.getFluid(5000))
                .inputFluids(GTOMaterials.Succinaldehyde.getFluid(5000))
                .outputItems(TagPrefix.dust, GTMaterials.PhosphorusPentoxide, 7)
                .outputFluids(GTOMaterials.EthyleneSulfide.getFluid(5000))
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(10000))
                .EUt(480)
                .duration(210)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_ethylxanthate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylate, 3)
                .inputFluids(GTOMaterials.CarbonDisulfide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 12)
                .EUt(480)
                .duration(40)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("tricotylphosphine")
                .inputItems(TagPrefix.dust, GTMaterials.Phosphorus)
                .inputFluids(GTMaterials.Octane.getFluid(3000))
                .outputFluids(GTOMaterials.Tricotylphosphine.getFluid(1000))
                .EUt(2000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("difluoroaniline")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 8)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .inputFluids(GTMaterials.Dichlorobenzene.getFluid(1000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 8)
                .outputFluids(GTOMaterials.Difluoroaniline.getFluid(2000))
                .EUt(7680)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_tetrafluoroborate_dust")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumTetrafluoroborate, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 2)
                .outputFluids(GTOMaterials.BoronFluoride.getFluid(1000))
                .EUt(125)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("iodine_containing_slurry")
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .inputFluids(GTOMaterials.EnrichedPotassiumIodideSlurry.getFluid(1000))
                .outputFluids(GTOMaterials.IodineContainingSlurry.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("deglycerated_soap")
                .inputItems(TagPrefix.dust, GTMaterials.Salt)
                .inputFluids(GTOMaterials.Soap.getFluid(1000))
                .outputFluids(GTOMaterials.DeglyceratedSoap.getFluid(800))
                .outputFluids(GTMaterials.Glycerol.getFluid(200))
                .EUt(2000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("ferrocene")
                .inputItems(TagPrefix.dust, GTOMaterials.ZeoliteSievingPellets)
                .inputFluids(GTMaterials.Iron2Chloride.getFluid(1000))
                .inputFluids(GTOMaterials.Cyclopentadiene.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.WetZeoliteSievingPellets)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(2000))
                .outputFluids(GTOMaterials.Ferrocene.getFluid(1000))
                .EUt(30720)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("acetaldehyde")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.OsmiumTetroxide)
                .inputFluids(GTMaterials.Acetone.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .outputFluids(GTOMaterials.Acetaldehyde.getFluid(1000))
                .EUt(480)
                .duration(320)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("gammabutyrolactone")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Copper)
                .inputFluids(GTOMaterials.Butane14Diol.getFluid(1000))
                .outputFluids(GTOMaterials.GammaButyrolactone.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .EUt(1920)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_borohydride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 8)
                .notConsumableFluid(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Ethanol.getFluid(3000))
                .inputFluids(GTOMaterials.BoricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumBorohydride, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumEthylate, 27)
                .outputFluids(GTMaterials.Water.getFluid(3000))
                .EUt(480)
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("succinimide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinicAcid, 14)
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Succinimide, 12)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("francium_carbide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Francium, 2)
                .inputFluids(GTOMaterials.Acetylene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.FranciumCarbide, 4)
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(480)
                .duration(260)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("trimethylsilane")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 2)
                .inputFluids(GTOMaterials.Trimethylchlorosilane.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTOMaterials.Trimethylsilane.getFluid(1000))
                .EUt(1920)
                .duration(190)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hmxexplosive_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Hexamethylenetetramine, 22)
                .notConsumableFluid(GTMaterials.AceticAnhydride.getFluid(1000))
                .inputFluids(GTOMaterials.FumingNitricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.HmxExplosive)
                .EUt(1024)
                .duration(20)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("phenylsodium")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
                .inputFluids(GTOMaterials.FluoroBenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 2)
                .outputFluids(GTOMaterials.Phenylsodium.getFluid(1000))
                .EUt(480)
                .duration(210)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_azide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumAzanide, 8)
                .inputFluids(GTMaterials.NitrogenDioxide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumAzide, 4)
                .outputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(480)
                .duration(170)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dimethoxyethane")
                .inputFluids(GTOMaterials.Dimethylether.getFluid(1000))
                .inputFluids(GTOMaterials.EthyleneOxide.getFluid(1000))
                .outputFluids(GTOMaterials.Dimethoxyethane.getFluid(1000))
                .EUt(2000)
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("aluminium_chloride")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .inputFluids(GTMaterials.Chlorine.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumChloride, 4)
                .EUt(120)
                .duration(300)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dibenzyltetraacetylhexaazaisowurtzitane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinamidylAcetate, 72)
                .inputItems(TagPrefix.dust, GTOMaterials.Hexabenzylhexaazaisowurtzitane, 102)
                .notConsumableFluid(GTOMaterials.HydrobromicAcid.getFluid(10000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(8000))
                .outputItems(TagPrefix.dust, GTOMaterials.Dibenzyltetraacetylhexaazaisowurtzitane, 70)
                .outputFluids(GTMaterials.Toluene.getFluid(6000))
                .EUt(122880)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("tetracene_dust")
                .notConsumable(GTItems.BLACKLIGHT.asItem())
                .inputFluids(GTOMaterials.IsopropylAlcohol.getFluid(1000))
                .inputFluids(GTOMaterials.Dichlorodicyanobenzoquinone.getFluid(2000))
                .inputFluids(GTOMaterials.Dihydroiodotetracene.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.Tetracene, 60)
                .outputItems(TagPrefix.dust, GTMaterials.Iodine, 2)
                .outputFluids(GTOMaterials.Dichlorodicyanohydroquinone.getFluid(2000))
                .outputFluids(GTMaterials.Acetone.getFluid(1000))
                .EUt(491520)
                .duration(260)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("californium_trichloride_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Ferrosilite)
                .inputItems(TagPrefix.dust, GTMaterials.Californium)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(6000))
                .outputItems(TagPrefix.dust, GTOMaterials.CaliforniumTrichloride, 4)
                .outputFluids(GTMaterials.Hydrogen.getFluid(3000))
                .EUt(7680)
                .duration(150)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("zirconium_oxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 36)
                .inputItems(TagPrefix.dust, GTMaterials.PotassiumHydroxide, 9)
                .inputItems(TagPrefix.dust, GTMaterials.Hematite, 10)
                .inputFluids(GTOMaterials.ZirconiumHafniumOxychloride.getFluid(3000))
                .inputFluids(GTMaterials.SulfurTrioxide.getFluid(3000))
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(6000))
                .outputItems(TagPrefix.dust, GTMaterials.PotassiumSulfate, 21)
                .outputItems(TagPrefix.dust, GTOMaterials.ZirconiumOxide, 3)
                .chancedOutput(TagPrefix.dust, GTOMaterials.HafniumOxide, 3, 8000, 0)
                .outputFluids(GTMaterials.Ammonia.getFluid(6000))
                .outputFluids(GTMaterials.Iron3Chloride.getFluid(4000))
                .EUt(1920)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("mutated_living_solder")
                .notConsumable(GTItems.GRAVI_STAR.asItem())
                .inputItems(GTOItems.BIOLOGICAL_CELLS.asStack(16))
                .inputItems(GTItems.QUANTUM_STAR.asItem())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 2000))
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 2000))
                .inputFluids(GTMaterials.Tin.getFluid(2000))
                .inputFluids(GTMaterials.Carbon.getFluid(2000))
                .inputFluids(GTMaterials.Beryllium.getFluid(2000))
                .outputItems(TagPrefix.dustTiny, GTMaterials.NetherStar, 4)
                .outputFluids(GTOMaterials.MutatedLivingSolder.getFluid(2000))
                .EUt(491520)
                .duration(1200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea_hexaf_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ThalliumChloride, 2)
                .inputItems(TagPrefix.dust, GTOMaterials.HassiumChloride, 5)
                .inputItems(TagPrefix.dust, GTOMaterials.RheniumChloride, 6)
                .inputFluids(GTOMaterials.HexafluorophosphoricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.IsophthaloylBis.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf, 125)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(7000))
                .outputFluids(GTMaterials.Chlorine.getFluid(3000))
                .EUt(31457280)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("aluminium_sulfite_dust")
                .circuitMeta(24)
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium, 2)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.AluminiumSulfite, 14)
                .outputFluids(GTMaterials.Water.getFluid(3000))
                .EUt(30720)
                .duration(400)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("tertbuthylcarbonylazide")
                .inputItems(TagPrefix.dust, GTOMaterials.DitertbutylDicarbonate, 33)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumAzide, 8)
                .inputItems(TagPrefix.dust, GTMaterials.Potassium, 2)
                .outputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
                .outputItems(TagPrefix.dust, GTMaterials.Potash, 6)
                .outputFluids(GTOMaterials.Tertbuthylcarbonylazide.getFluid(2000))
                .EUt(480)
                .duration(110)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("pentaerythritol_dust")
                .notConsumable(TagPrefix.dust, GTMaterials.SodiumHydroxide)
                .inputFluids(GTMaterials.Formaldehyde.getFluid(4000))
                .inputFluids(GTOMaterials.Acetaldehyde.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Pentaerythritol, 21)
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000))
                .EUt(1920)
                .duration(1200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("succinaldehyde")
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinicAcid, 14)
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumHydride, 4)
                .outputItems(TagPrefix.dust, GTMaterials.Lithium)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .outputFluids(GTOMaterials.Succinaldehyde.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(1920)
                .duration(600)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dichlorocyclooctadieneplatinium_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Platinum)
                .inputItems(TagPrefix.dust, GTMaterials.Potassium, 2)
                .inputFluids(GTMaterials.NitricAcid.getFluid(4000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .inputFluids(GTOMaterials.Cyclooctadiene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Dichlorocyclooctadieneplatinum, 23)
                .outputItems(TagPrefix.dust, GTMaterials.RockSalt, 4)
                .EUt(491520)
                .duration(160)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("lithium_niobate_nanoparticles_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Hafnium)
                .inputItems(TagPrefix.dust, GTMaterials.Lithium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Niobium)
                .inputFluids(GTMaterials.Oxygen.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumNiobateNanoparticles, 6)
                .EUt(1966080)
                .duration(1200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("unfolded_fullerene_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.TiAlChloride)
                .inputItems(TagPrefix.dust, GTOMaterials.BenzophenanthrenylAcetonitrile, 102)
                .outputItems(TagPrefix.dust, GTOMaterials.UnfoldedFullerene)
                .EUt(7680)
                .duration(250)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("trimethyltin_chloride")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Magnesium)
                .inputItems(TagPrefix.dust, GTMaterials.Tin)
                .inputFluids(GTMaterials.Methane.getFluid(3000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.TrimethylTinChloride.getFluid(1000))
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(2000))
                .EUt(30720)
                .duration(320)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("liquidcrystalkevlar")
                .inputItems(TagPrefix.dust, GTMaterials.CalciumChloride)
                .inputItems(TagPrefix.dust, GTOMaterials.TerephthaloylChloride, 9)
                .inputItems(TagPrefix.dust, GTOMaterials.PPhenylenediamine, 9)
                .inputFluids(GTOMaterials.NMPyrolidone.getFluid(1000))
                .outputFluids(GTOMaterials.LiquidCrystalKevlar.getFluid(9000))
                .EUt(524288)
                .duration(160)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("toluene_diisocyanate")
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .inputFluids(GTOMaterials.Phosgene.getFluid(2000))
                .inputFluids(GTMaterials.Toluene.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputFluids(GTOMaterials.TolueneDiisocyanate.getFluid(2000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .outputFluids(GTMaterials.Water.getFluid(6000))
                .EUt(480)
                .duration(130)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("vibranium_unstable")
                .inputItems(TagPrefix.dust, GTOMaterials.Adamantine, 2)
                .inputFluids(GTOMaterials.BedrockGas.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.IridiumChloride, 4)
                .outputFluids(GTOMaterials.VibraniumUnstable.getFluid(500))
                .outputFluids(GTOMaterials.RareEarthChlorides.getFluid(1000))
                .EUt(7864320)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("fluorocarborane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CesiumCarborane, 50)
                .inputItems(TagPrefix.dust, GTOMaterials.SilverNitrate, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Iodine, 2)
                .inputFluids(GTOMaterials.Trimethylsilane.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Fluorine.getFluid(44000))
                .outputItems(TagPrefix.dust, GTOMaterials.Fluorocarborane, 50)
                .outputItems(TagPrefix.dust, GTOMaterials.CaesiumNitrate, 10)
                .outputItems(TagPrefix.dust, GTOMaterials.SilverIodide, 4)
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(22000))
                .outputFluids(GTOMaterials.Trimethylchlorosilane.getFluid(1000))
                .EUt(1920)
                .duration(320)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("ethyl_trifluoroacetate")
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(3000))
                .inputFluids(GTOMaterials.AcetylChloride.getFluid(1000))
                .inputFluids(GTMaterials.Ethanol.getFluid(1000))
                .outputFluids(GTOMaterials.EthylTrifluoroacetate.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .EUt(480)
                .duration(230)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("decaborane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumBorohydride, 51)
                .inputFluids(GTOMaterials.BoronTrifluorideAcetate.getFluid(10000))
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(2000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Decaborane, 24)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumTetrafluoroborate, 45)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(20000))
                .outputFluids(GTOMaterials.DiethylEther.getFluid(10000))
                .EUt(1920)
                .duration(380)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dibromoacrolein")
                .inputFluids(GTMaterials.FormicAcid.getFluid(2000))
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .inputFluids(GTMaterials.Bromine.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 12)
                .outputFluids(GTOMaterials.Dibromoacrolein.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(7680)
                .duration(360)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("aminated_fullerene")
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .inputFluids(GTMaterials.CarbonMonoxide.getFluid(4000))
                .inputFluids(GTMaterials.Water.getFluid(8000))
                .inputFluids(GTOMaterials.Tertbuthylcarbonylazide.getFluid(4000))
                .outputFluids(GTOMaterials.AminatedFullerene.getFluid(1000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(8000))
                .outputFluids(GTOMaterials.TertButanol.getFluid(4000))
                .EUt(30720)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cesium_carborane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CesiumCarboranePrecursor, 38)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 2)
                .notConsumableFluid(GTOMaterials.Tetrahydrofuran.getFluid(1000))
                .inputFluids(GTOMaterials.BoraneDimethylSulfide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CesiumCarborane, 25)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTOMaterials.Trimethylamine.getFluid(1000))
                .outputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .outputFluids(GTMaterials.Methane.getFluid(2000))
                .EUt(480)
                .duration(260)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("bromodihydrothiine")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumThiosulfate, 14)
                .inputFluids(GTMaterials.Ethane.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .inputFluids(GTOMaterials.Dibromoacrolein.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 4)
                .outputItems(TagPrefix.dust, GTMaterials.SodiumBisulfate, 14)
                .outputFluids(GTOMaterials.Bromodihydrothiine.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(7680)
                .duration(400)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diiodobiphenyl_dust")
                .notConsumable(GTItems.BLACKLIGHT.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Iodine, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Biphenyl, 22)
                .outputItems(TagPrefix.dust, GTOMaterials.Diiodobiphenyl, 22)
                .outputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .EUt(122880)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("terephthaloyl_chloride_dust")
                .inputFluids(GTOMaterials.ThionylChloride.getFluid(20000))
                .inputFluids(GTOMaterials.DimethylTerephthalate.getFluid(5000))
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(6000))
                .outputItems(TagPrefix.dust, GTOMaterials.TerephthaloylChloride, 64)
                .outputItems(TagPrefix.dust, GTOMaterials.TerephthaloylChloride, 48)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(20000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(20000))
                .EUt(1920)
                .duration(240)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dichlorodicyanobenzoquinone")
                .inputFluids(GTMaterials.HydrogenCyanide.getFluid(2000))
                .inputFluids(GTMaterials.Chlorine.getFluid(10000))
                .inputFluids(GTMaterials.Phenol.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(8000))
                .outputFluids(GTOMaterials.Dichlorodicyanobenzoquinone.getFluid(1000))
                .EUt(30720)
                .duration(250)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dihydroiodotetracene")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.RhodiumRheniumNaquadahCatalyst)
                .inputItems(TagPrefix.dust, GTOMaterials.BromoSuccinamide, 12)
                .inputFluids(GTOMaterials.IodineMonochloride.getFluid(1000))
                .inputFluids(GTOMaterials.AcetylatingReagent.getFluid(1000))
                .inputFluids(GTOMaterials.Dimethylnaphthalene.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.MagnesiumChlorideBromide, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.Succinimide, 12)
                .outputFluids(GTOMaterials.Dihydroiodotetracene.getFluid(1000))
                .outputFluids(GTOMaterials.Trimethylchlorosilane.getFluid(1000))
                .outputFluids(GTOMaterials.HydrobromicAcid.getFluid(1000))
                .EUt(122880)
                .duration(350)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phenylpentanoic_acid")
                .notConsumableFluid(GTOMaterials.TrimethylTinChloride.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .inputFluids(GTOMaterials.HydroiodicAcid.getFluid(1000))
                .inputFluids(GTOMaterials.Acrylonitrile.getFluid(1000))
                .inputFluids(GTMaterials.Styrene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumHydride, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumIodide, 2)
                .outputFluids(GTOMaterials.PhenylPentanoicAcid.getFluid(1000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(8000000)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("saturated_fullerene_sieving_matrix")
                .inputItems(GTOItems.SATURATED_FULLERENE_SIEVING_MATRIX.asItem())
                .inputFluids(GTOMaterials.KryptonDifluoride.getFluid(16000))
                .inputFluids(GTMaterials.FluoroantimonicAcid.getFluid(8000))
                .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 32)
                .outputItems(TagPrefix.dust, GTOMaterials.Fluorocarborane, 50)
                .outputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .outputFluids(GTMaterials.Krypton.getFluid(16000))
                .outputFluids(GTOMaterials.HeavilyFluorinatedTriniumSolution.getFluid(8000))
                .EUt(122880)
                .duration(180)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dinitrodipropanyloxybenzene")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumOxide, 3)
                .inputFluids(GTMaterials.AceticAnhydride.getFluid(1000))
                .inputFluids(GTOMaterials.Isochloropropane.getFluid(1000))
                .inputFluids(GTOMaterials.Resorcinol.getFluid(1000))
                .inputFluids(GTMaterials.Propene.getFluid(1000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .outputFluids(GTOMaterials.Dinitrodipropanyloxybenzene.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .outputFluids(GTMaterials.AceticAcid.getFluid(2000))
                .EUt(7680)
                .duration(50)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("hydrazine")
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(2000))
                .outputFluids(GTOMaterials.Hydrazine.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(30)
                .duration(320)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("adamantine_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.AdamantineCompounds, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Mirabilite, 17)
                .inputFluids(GTMaterials.NitrationMixture.getFluid(1000))
                .inputFluids(GTMaterials.AquaRegia.getFluid(2000))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(100))
                .outputItems(TagPrefix.dust, GTOMaterials.Adamantine)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumNitrate, 5)
                .outputItems(TagPrefix.dust, GTMaterials.SodiumBisulfate, 7)
                .outputFluids(GTOMaterials.RareEarthChlorides.getFluid(1000))
                .EUt(7864320)
                .duration(400)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("pcbs")
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene, 8)
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.DMAP)
                .inputFluids(GTOMaterials.PhenylPentanoicAcid.getFluid(8000))
                .inputFluids(GTOMaterials.Dichloromethane.getFluid(8000))
                .inputFluids(GTMaterials.Styrene.getFluid(7000))
                .inputFluids(GTMaterials.Chlorobenzene.getFluid(8000))
                .inputFluids(GTOMaterials.DimethylSulfide.getFluid(8000))
                .outputFluids(GTMaterials.Toluene.getFluid(8000))
                .outputFluids(GTMaterials.HydrogenSulfide.getFluid(8000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(24000))
                .outputFluids(GTOMaterials.PCBs.getFluid(8000))
                .EUt(31457280)
                .duration(80)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("benzylamine")
                .inputItems(TagPrefix.dust, GTOMaterials.Hexamethylenetetramine, 22)
                .inputFluids(GTMaterials.Water.getFluid(6000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .inputFluids(GTOMaterials.BenzylChloride.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 18)
                .outputFluids(GTOMaterials.Benzylamine.getFluid(1000))
                .outputFluids(GTMaterials.Formaldehyde.getFluid(6000))
                .EUt(7680)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("glyoxal")
                .inputFluids(GTOMaterials.Acetaldehyde.getFluid(2000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(2000))
                .outputFluids(GTMaterials.Water.getFluid(3000))
                .outputFluids(GTOMaterials.Glyoxal.getFluid(2000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000))
                .EUt(60)
                .duration(120)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("benzophenanthrenylacetonitrile_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Methylbenzophenanthrene, 33)
                .inputItems(TagPrefix.dust, GTOMaterials.BromoSuccinamide, 12)
                .inputItems(TagPrefix.dust, GTMaterials.PotassiumCyanide, 3)
                .outputItems(TagPrefix.dust, GTOMaterials.BenzophenanthrenylAcetonitrile, 34)
                .outputItems(TagPrefix.dust, GTOMaterials.Succinimide, 12)
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumBromide, 2)
                .EUt(1920)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cesium_carborane_precursor_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Decaborane, 24)
                .inputItems(TagPrefix.dust, GTOMaterials.CaesiumHydroxide, 3)
                .notConsumableFluid(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Methanol.getFluid(3000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .inputFluids(GTOMaterials.SodiumCyanide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CesiumCarboranePrecursor, 38)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .EUt(480)
                .duration(240)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("sodium_seaborgate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Seaborgium)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 6)
                .inputFluids(GTMaterials.Fluorine.getFluid(6000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumSeaborgate, 7)
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(6000))
                .EUt(1966080)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phenylenedioxydiacetic_acid")
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .inputFluids(GTMaterials.Phenol.getFluid(1000))
                .inputFluids(GTMaterials.Ethenone.getFluid(2000))
                .inputFluids(GTMaterials.Chlorine.getFluid(4000))
                .outputFluids(GTOMaterials.PhenylenedioxydiaceticAcid.getFluid(1000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .EUt(122880)
                .duration(320)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("hydroquinone")
                .circuitMeta(2)
                .inputFluids(GTMaterials.Benzene.getFluid(2000))
                .inputFluids(GTMaterials.Oxygen.getFluid(5000))
                .inputFluids(GTMaterials.Propene.getFluid(1000))
                .outputFluids(GTOMaterials.Hydroquinone.getFluid(1000))
                .outputFluids(GTOMaterials.Resorcinol.getFluid(1000))
                .outputFluids(GTMaterials.Acetone.getFluid(1000))
                .EUt(1920)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("p_phenylenediamine_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Palladium)
                .inputFluids(GTMaterials.NitrogenDioxide.getFluid(100))
                .inputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .inputFluids(GTOMaterials.PNitroaniline.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PPhenylenediamine, 16)
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(122880)
                .duration(60)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("acetonitrile_dust")
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .inputFluids(GTMaterials.CarbonMonoxide.getFluid(2000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.Acetonitrile, 6)
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(1966080)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("terephthalaldehyde_dust")
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.Dibromomethylbenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Terephthalaldehyde, 16)
                .outputFluids(GTMaterials.Bromine.getFluid(2000))
                .outputFluids(GTMaterials.HydrogenSulfide.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000))
                .EUt(7680)
                .duration(50)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diphenylmethanediisocyanatemixture")
                .inputFluids(GTOMaterials.DiaminodiphenylmethanMixture.getFluid(1000))
                .inputFluids(GTOMaterials.Phosgene.getFluid(2000))
                .outputFluids(GTOMaterials.DiphenylmethanediisocyanateMixture.getFluid(1000))
                .EUt(7680)
                .duration(600)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("barium_sulfide_dust")
                .circuitMeta(24)
                .inputItems(TagPrefix.dust, GTMaterials.Barite, 6)
                .inputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.BariumSulfide, 2)
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .EUt(7680)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("silver_perchlorate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SilverOxide, 3)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumPerchlorate, 12)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverPerchlorate, 12)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumOxide, 3)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(1000))
                .EUt(480)
                .duration(350)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("hydroxylamine_hydrochloride")
                .inputItems(TagPrefix.dust, GTOMaterials.HydroxylammoniumSulfate, 17)
                .inputItems(TagPrefix.dust, GTOMaterials.BariumChloride, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Barite, 6)
                .outputFluids(GTOMaterials.HydroxylamineHydrochloride.getFluid(2000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(480)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diethylthiourea")
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .inputFluids(GTOMaterials.Ethylamine.getFluid(2000))
                .inputFluids(GTOMaterials.SodiumThiocyanate.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .outputFluids(GTOMaterials.Diethylthiourea.getFluid(1000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(30720)
                .duration(210)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("acetylating_reagent")
                .inputItems(TagPrefix.dust, GTMaterials.MagnesiumChloride, 6)
                .inputFluids(GTMaterials.Bromine.getFluid(2000))
                .inputFluids(GTOMaterials.Trimethylchlorosilane.getFluid(1000))
                .inputFluids(GTOMaterials.Acetylene.getFluid(3000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(3000))
                .outputFluids(GTMaterials.Chlorine.getFluid(2000))
                .outputFluids(GTOMaterials.AcetylatingReagent.getFluid(1000))
                .EUt(480)
                .duration(350)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("siliconoil")
                .inputFluids(GTOMaterials.EthyleneOxide.getFluid(1000))
                .inputFluids(GTMaterials.Dimethyldichlorosilane.getFluid(4000))
                .inputFluids(GTMaterials.DistilledWater.getFluid(5000))
                .outputFluids(GTOMaterials.SiliconOil.getFluid(5000))
                .EUt(480)
                .duration(1000)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cosmic_computing_mixture")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Vibranium)
                .inputFluids(GTOMaterials.Gluons.getFluid(1000))
                .inputFluids(GTOMaterials.HeavyQuarks.getFluid(1000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .outputFluids(GTOMaterials.CosmicComputingMixture.getFluid(3000))
                .EUt(24000000)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cycloparaphenylene")
                .inputItems(TagPrefix.dust, GTOMaterials.Dichlorocyclooctadieneplatinum, 23)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Diiodobiphenyl, 4)
                .inputFluids(GTOMaterials.TrimethylTinChloride.getFluid(4000))
                .inputFluids(GTOMaterials.SilverTetrafluoroborate.getFluid(6000))
                .outputItems(TagPrefix.dust, GTMaterials.PlatinumRaw, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Silver, 6)
                .outputFluids(GTOMaterials.Cycloparaphenylene.getFluid(10000))
                .outputFluids(GTOMaterials.BoronFluoride.getFluid(2000))
                .outputFluids(GTOMaterials.OneOctene.getFluid(3000))
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(6000))
                .EUt(1966080)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("bismuth_nitrate_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Bismuth)
                .inputFluids(GTMaterials.NitricAcid.getFluid(6000))
                .outputFluids(GTOMaterials.BismuthNitrateSolution.getFluid(1000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(3000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30)
                .duration(350)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("methylbenzophenanthrene_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Iodine)
                .inputFluids(GTMaterials.Naphthalene.getFluid(1000))
                .inputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .inputFluids(GTMaterials.Ethylbenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Methylbenzophenanthrene, 33)
                .EUt(1920)
                .duration(600)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("hexafluorophosphoric_acid")
                .inputFluids(GTOMaterials.AntimonyPentafluoride.getFluid(1000))
                .inputFluids(GTOMaterials.PhosphorusTrichloride.getFluid(1000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.AntimonyTrichloride, 4)
                .outputFluids(GTOMaterials.HexafluorophosphoricAcid.getFluid(1000))
                .EUt(30720)
                .duration(280)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("difluorobenzophenone_dust")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.ZnFeAlClCatalyst)
                .inputFluids(GTOMaterials.Fluorotoluene.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(6000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .inputFluids(GTOMaterials.FluoroBenzene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Difluorobenzophenone, 24)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(6000))
                .EUt(1920)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("palladium_fullerene_matrix_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Sarcosine, 13)
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .inputItems(TagPrefix.dust, GTMaterials.Palladium)
                .inputFluids(GTOMaterials.Ferrocene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PalladiumFullereneMatrix)
                .EUt(31457280)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("glycerol_a")
                .circuitMeta(1)
                .notConsumable(TagPrefix.dust, GTMaterials.SodaAsh)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .notConsumableFluid(GTMaterials.CarbonDioxide.getFluid(10000))
                .inputFluids(GTMaterials.Epichlorohydrin.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .outputFluids(GTMaterials.Glycerol.getFluid(1000))
                .outputFluids(GTMaterials.SaltWater.getFluid(1000))
                .EUt(7680)
                .duration(150)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cyclopentadienyl_titanium_trichloride_dust")
                .inputFluids(GTMaterials.TitaniumTetrachloride.getFluid(1000))
                .inputFluids(GTOMaterials.Propadiene.getFluid(2000))
                .inputFluids(GTOMaterials.Acetylene.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.CyclopentadienylTitaniumTrichloride, 23)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .EUt(7680)
                .duration(780)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cosmic_superconductor")
                .inputItems(TagPrefix.dust, GTOMaterials.RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf, 125)
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumSuperhydride, 39)
                .inputItems(TagPrefix.dust, GTOMaterials.ChargedCaesiumCeriumCobaltIndium, 14)
                .inputFluids(GTOMaterials.LightQuarks.getFluid(10000))
                .inputFluids(GTOMaterials.FreeAlphaGas.getFluid(1000))
                .outputFluids(GTOMaterials.CosmicSuperconductor.getFluid(10000))
                .EUt(125829120)
                .duration(600)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("stellar_energy_rocket_fuel")
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched)
                .inputItems(TagPrefix.dust, GTOMaterials.HmxExplosive, 2)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(2000))
                .inputFluids(GTOMaterials.ExplosiveHydrazine.getFluid(3000))
                .inputFluids(GTMaterials.Nitrobenzene.getFluid(8000))
                .inputFluids(GTMaterials.DinitrogenTetroxide.getFluid(6000))
                .inputFluids(GTOMaterials.Kerosene.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Naquadah)
                .outputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(5000))
                .EUt(122880)
                .duration(120)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("charged_lepton_trap_crystal")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Starmetal)
                .inputItems(GTOItems.LEPTON_TRAP_CRYSTAL.asItem())
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Vibranium, 2)
                .inputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .outputItems(GTOItems.CHARGED_LEPTON_TRAP_CRYSTAL.asItem())
                .EUt(491520)
                .duration(240)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("potassium_ethylate_dust")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Potash, 3)
                .inputItems(TagPrefix.dust, GTMaterials.Quicklime, 5)
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(5000))
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylate, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.HighPurityCalciumCarbonate, 5)
                .EUt(120)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("photopolymer")
                .inputItems(TagPrefix.dust, GTOMaterials.CyclopentadienylTitaniumTrichloride, 69)
                .inputItems(TagPrefix.dust, GTMaterials.Ice, 42)
                .inputItems(TagPrefix.dust, GTOMaterials.SilverPerchlorate, 12)
                .inputFluids(GTOMaterials.Phenylsodium.getFluid(8000))
                .inputFluids(GTOMaterials.SilverTetrafluoroborate.getFluid(2000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .inputFluids(GTOMaterials.NDifluorophenylpyrrole.getFluid(6000))
                .inputFluids(GTOMaterials.TetraethylammoniumBromide.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverChloride, 8)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumBromide, 4)
                .outputFluids(GTMaterials.SaltWater.getFluid(6000))
                .outputFluids(GTOMaterials.Photopolymer.getFluid(8000))
                .EUt(30720)
                .duration(340)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("titanium_trifluoride_dust")
                .circuitMeta(24)
                .inputItems(TagPrefix.dust, GTMaterials.Titanium)
                .inputFluids(GTMaterials.Fluorine.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.TitaniumTrifluoride, 4)
                .EUt(30720)
                .duration(600)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diaminodiphenylmethanmixture")
                .inputFluids(GTMaterials.Formaldehyde.getFluid(1000))
                .inputFluids(GTOMaterials.Aniline.getFluid(2000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.DiaminodiphenylmethanMixture.getFluid(1000))
                .EUt(7680)
                .duration(320)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("tetraacetyldinitrosohexaazaisowurtzitane_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Dibenzyltetraacetylhexaazaisowurtzitane, 70)
                .inputItems(TagPrefix.dust, GTOMaterials.NitrosoniumTetrafluoroborate, 42)
                .inputFluids(GTMaterials.Water.getFluid(14000))
                .outputItems(TagPrefix.dust, GTOMaterials.Tetraacetyldinitrosohexaazaisowurtzitane, 46)
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(24000))
                .outputFluids(GTOMaterials.BoricAcid.getFluid(6000))
                .outputFluids(GTMaterials.NitricOxide.getFluid(4000))
                .outputFluids(GTOMaterials.Benzaldehyde.getFluid(2000))
                .EUt(7680)
                .duration(140)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("optical_wafer")
                .inputItems(TagPrefix.dust, GTOMaterials.FranciumCaesiumCadmiumBromide, 2)
                .inputItems(GTOItems.PHOTON_CARRYING_WAFER.asItem())
                .inputItems(GTOTagPrefix.NANITES, GTMaterials.Glowstone)
                .inputFluids(GTOMaterials.SeaborgiumDopedNanotubes.getFluid(144))
                .inputFluids(GTOMaterials.CarbonNanotubes.getFluid(144))
                .outputItems(GTOItems.OPTICAL_WAFER.asItem())
                .EUt(1966080)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("sarcosine_dust")
                .inputFluids(GTMaterials.AceticAcid.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(2000))
                .inputFluids(GTMaterials.Methanol.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Sarcosine, 13)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(3000))
                .EUt(7680)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("isophthaloylbis")
                .inputFluids(GTOMaterials.PhenylenedioxydiaceticAcid.getFluid(1000))
                .inputFluids(GTOMaterials.ThionylChloride.getFluid(2000))
                .inputFluids(GTOMaterials.Diethylthiourea.getFluid(2000))
                .outputFluids(GTOMaterials.IsophthaloylBis.getFluid(1000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(2000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .EUt(122880)
                .duration(250)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("trichloroflerane")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Ferrosilite)
                .inputItems(TagPrefix.dust, GTMaterials.Flerovium)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(3000))
                .outputFluids(GTOMaterials.Trichloroflerane.getFluid(1000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(3000))
                .EUt(7680)
                .duration(150)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("photoresist")
                .inputFluids(GTOMaterials.EthylAcrylate.getFluid(1000))
                .inputFluids(GTMaterials.Styrene.getFluid(1000))
                .inputFluids(GTMaterials.TitaniumTetrachloride.getFluid(100))
                .outputFluids(GTOMaterials.Photoresist.getFluid(1000))
                .EUt(1920)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("iridium_chloride_dust")
                .circuitMeta(24)
                .inputItems(TagPrefix.dust, GTMaterials.Iridium)
                .inputFluids(GTMaterials.Chlorine.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.IridiumChloride, 4)
                .EUt(30720)
                .duration(800)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("co_ac_ab_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Charcoal, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Cobalt)
                .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole)
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .inputFluids(GTOMaterials.Acetylene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CoAcAbCatalyst)
                .outputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000))
                .EUt(1966080)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("barium_titanate_ceramic_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.TitaniumDioxide, 3)
                .inputItems(TagPrefix.dust, GTOMaterials.BariumHydroxide, 5)
                .circuitMeta(5)
                .outputItems(TagPrefix.dust, GTOMaterials.BariumTitanateCeramic)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(240)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("barium_hydroxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Barium)
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.BariumHydroxide, 5)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(120)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("titanium_dioxide_dust")
                .circuitMeta(5)
                .inputFluids(GTMaterials.TitaniumTetrachloride.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.TitaniumDioxide, 3)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .EUt(240)
                .duration(100)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("undried_hydroxyapatite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Apatite, 4)
                .inputItems(TagPrefix.dust, GTOMaterials.HighPurityCalciumCarbonate)
                .circuitMeta(5)
                .inputFluids(GTMaterials.Steam.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.UndriedHydroxyapatite)
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("cobalt_oxide_ceramic_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Cobalt)
                .circuitMeta(5)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CobaltOxideCeramic)
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("magnesium_chloride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Magnesium)
                .circuitMeta(5)
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .outputFluids(Hydrogen.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.MagnesiumChloride, 3)
                .EUt(480)
                .duration(100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("strontium_carbonate_ceramic_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.StrontiumSulfate, 6)
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Hafnium)
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.StrontiumCarbonateCeramic)
                .outputFluids(GTMaterials.DilutedSulfuricAcid.getFluid(1000))
                .EUt(120)
                .duration(260)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("strontium_sulfate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Strontium)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.StrontiumSulfate, 6)
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .EUt(1920)
                .duration(50)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("silica_ceramic_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah)
                .inputItems(TagPrefix.dustTiny, GTMaterials.NaquadahEnriched)
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilicaCeramic)
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(120)
                .duration(260)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("advanced_circuit_board_persulfate").duration(900).EUt(VA[LV])
                .inputItems(EPOXY_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.BariumTitanateCeramic, 3)
                .inputItems(foil, Electrum, 8)
                .inputFluids(SodiumPersulfate.getFluid(1000))
                .outputItems(ADVANCED_CIRCUIT_BOARD)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("advanced_circuit_board_iron3").duration(900).EUt(VA[LV])
                .inputItems(EPOXY_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.BariumTitanateCeramic, 3)
                .inputItems(foil, Electrum, 8)
                .inputFluids(Iron3Chloride.getFluid(500))
                .outputItems(ADVANCED_CIRCUIT_BOARD)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("extreme_circuit_board_persulfate").duration(1200).EUt(VA[LV])
                .inputItems(FIBER_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.TungstenTetraborideCeramics, 4)
                .inputItems(foil, AnnealedCopper, 12)
                .inputFluids(SodiumPersulfate.getFluid(2000))
                .outputItems(EXTREME_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("extreme_circuit_board_iron3").duration(1200).EUt(VA[LV])
                .inputItems(FIBER_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.TungstenTetraborideCeramics, 4)
                .inputItems(foil, AnnealedCopper, 12)
                .inputFluids(Iron3Chloride.getFluid(1000))
                .outputItems(EXTREME_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("elite_circuit_board_persulfate").duration(1500).EUt(VA[MV])
                .inputItems(MULTILAYER_FIBER_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.SilicaCeramic, 4)
                .inputItems(foil, Platinum, 8)
                .inputFluids(SodiumPersulfate.getFluid(4000))
                .outputItems(ELITE_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("elite_circuit_board_iron3").duration(1500).EUt(VA[MV])
                .inputItems(MULTILAYER_FIBER_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.SilicaCeramic, 4)
                .inputItems(foil, Platinum, 8)
                .inputFluids(Iron3Chloride.getFluid(2000))
                .outputItems(ELITE_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("wetware_circuit_board_persulfate").duration(1800).EUt(VA[HV])
                .inputItems(WETWARE_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.HydroxyapatiteCeramic, 4)
                .inputItems(foil, NiobiumTitanium, 32)
                .inputFluids(SodiumPersulfate.getFluid(10000))
                .outputItems(WETWARE_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("wetware_circuit_board_iron3").duration(1800).EUt(VA[HV])
                .inputItems(WETWARE_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.HydroxyapatiteCeramic, 4)
                .inputItems(foil, NiobiumTitanium, 32)
                .inputFluids(Iron3Chloride.getFluid(5000))
                .outputItems(WETWARE_CIRCUIT_BOARD)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("bioware_printed_circuit_board")
                .inputItems(GTOItems.BIOWARE_CIRCUIT_BOARD.asItem())
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.TellurateCeramics, 4)
                .inputItems(TagPrefix.foil, GTMaterials.VanadiumGallium, 32)
                .inputFluids(GTMaterials.Iron3Chloride.getFluid(10000))
                .outputItems(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD.asItem())
                .EUt(1920)
                .duration(2100)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("bioware_printed_circuit_board1")
                .inputItems(GTOItems.BIOWARE_CIRCUIT_BOARD.asItem())
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.TellurateCeramics, 4)
                .inputItems(TagPrefix.foil, GTMaterials.VanadiumGallium, 32)
                .inputFluids(GTMaterials.SodiumPersulfate.getFluid(20000))
                .outputItems(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD.asItem())
                .EUt(1920)
                .duration(2100)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("citric_acid")
                .inputItems(TagPrefix.dustTiny, GTMaterials.PotassiumDichromate)
                .outputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 18)
                .inputFluids(GTMaterials.HypochlorousAcid.getFluid(1000))
                .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000))
                .inputFluids(GTMaterials.Glycerol.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(3000))
                .inputFluids(GTMaterials.HydrogenCyanide.getFluid(3000))
                .outputFluids(GTOMaterials.CitricAcid.getFluid(1000))
                .circuitMeta(4)
                .EUt(1920)
                .duration(240)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("well_mixed_ybcoxides")
                .inputItems(TagPrefix.dust, GTOMaterials.CopperNitrate, 27)
                .inputItems(TagPrefix.dust, GTOMaterials.BariumNitrate, 18)
                .inputItems(TagPrefix.dust, GTOMaterials.YttriumNitrate, 13)
                .outputItems(TagPrefix.dust, GTOMaterials.WellMixedYbcOxides, 12)
                .inputFluids(GTOMaterials.CitricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Ammonia.getFluid(2000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(15000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(6000))
                .outputFluids(GTMaterials.Water.getFluid(4000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(6000))
                .EUt(1920)
                .duration(260)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("b27supplement")
                .inputFluids(GTOMaterials.Catalase.getFluid(1000))
                .inputFluids(GTOMaterials.LinoleicAcid.getFluid(1000))
                .inputFluids(GTOMaterials.Biotin.getFluid(1000))
                .inputFluids(GTOMaterials.Ethanolamine.getFluid(1000))
                .inputFluids(GTOMaterials.VitaminA.getFluid(1000))
                .outputFluids(GTOMaterials.B27Supplement.getFluid(5000))
                .EUt(7680)
                .duration(150)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("vitamina")
                .inputFluids(GTOMaterials.PropargylChloride.getFluid(5000))
                .inputFluids(GTOMaterials.BetaIonone.getFluid(25000))
                .outputFluids(GTOMaterials.VitaminA.getFluid(17000))
                .outputFluids(GTMaterials.Oxygen.getFluid(8000))
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(5000))
                .EUt(480)
                .duration(150)
                .save();

        LARGE_CHEMICAL_RECIPES.builder("pedot")
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.IronSulfate)
                .outputItems(TagPrefix.dust, GTOMaterials.Pedot)
                .notConsumableFluid(GTMaterials.SodiumPersulfate, 1000)
                .inputFluids(GTOMaterials.Ethylenedioxythiophene, 1000)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTMaterials.Styrene, 1000)
                .outputFluids(GTMaterials.DilutedSulfuricAcid, 1000)
                .EUt(1920)
                .duration(320)
                .save();

        LARGE_CHEMICAL_RECIPES.builder("dietoxythiophene")
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Zinc)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylate, 18)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumBromide, 8)
                .inputFluids(GTOMaterials.Perbromothiophene, 1000)
                .inputFluids(GTMaterials.AceticAcid, 1000)
                .inputFluids(GTMaterials.Water, 1000)
                .outputFluids(GTOMaterials.Dietoxythiophene, 1000)
                .outputFluids(GTOMaterials.HydrobromicAcid, 2000)
                .outputFluids(GTMaterials.CarbonDioxide, 1000)
                .EUt(480)
                .duration(80)
                .save();

        LARGE_CHEMICAL_RECIPES.builder("perbromothiophene")
                .inputItems(TagPrefix.dust, GTOMaterials.SuccinicAcid, 14)
                .inputFluids(GTMaterials.HydrogenSulfide, 1000)
                .inputFluids(GTMaterials.Bromine, 1000)
                .outputFluids(GTOMaterials.Perbromothiophene, 1000)
                .outputFluids(GTMaterials.Water, 4000)
                .EUt(1920)
                .duration(140)
                .save();

        LARGE_CHEMICAL_RECIPES.builder("ethylenedioxythiophene")
                .notConsumable(GTItems.GELLED_TOLUENE.asStack())
                .inputFluids(GTOMaterials.Dietoxythiophene, 1000)
                .inputFluids(GTOMaterials.EthyleneGlycol, 1000)
                .outputFluids(GTOMaterials.Ethylenedioxythiophene, 1000)
                .outputFluids(GTMaterials.Ethanol, 1000)
                .EUt(120)
                .duration(140)
                .save();

        CHEMICAL_RECIPES.builder("desulfurize_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel.getFluid(4000))
                .inputFluids(Hydrogen.getFluid(1000))
                .outputFluids(HydrogenSulfide.getFluid(480))
                .outputFluids(HeavyFuel.getFluid(4000))
                .duration(400).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("desulfurize_light_fuel")
                .inputFluids(SulfuricLightFuel.getFluid(6000))
                .inputFluids(Hydrogen.getFluid(1000))
                .outputFluids(HydrogenSulfide.getFluid(480))
                .outputFluids(LightFuel.getFluid(6000))
                .duration(400).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("desulfurize_naphtha")
                .inputFluids(SulfuricNaphtha.getFluid(6000))
                .inputFluids(Hydrogen.getFluid(1000))
                .outputFluids(HydrogenSulfide.getFluid(480))
                .outputFluids(Naphtha.getFluid(6000))
                .duration(400).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("desulfurize_refinery_gas")
                .inputFluids(SulfuricGas.getFluid(8000))
                .inputFluids(Hydrogen.getFluid(1000))
                .outputFluids(HydrogenSulfide.getFluid(480))
                .outputFluids(RefineryGas.getFluid(8000))
                .duration(400).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("chromic_acid_waste_recycle")
                .inputFluids(GTOMaterials.ChromicAcidWaste.getFluid(2500))
                .inputItems(dust, Zinc, 4)
                .inputItems(dust, Iron, 4)
                .outputFluids(Iron3Chloride.getFluid(3000))
                .outputItems(dust, GTOMaterials.ZincSulfate, 3)
                .outputItems(dustSmall, Chromium, 2)
                .duration(400).EUt(VA[HV]).save();

        CHEMICAL_RECIPES.builder("triphenylphosphine")
                .inputFluids(Benzene.getFluid(3000))
                .inputFluids(GTOMaterials.PhosphorusTrichloride.getFluid(1000))
                .inputItems(dust, Zinc, 4)
                .outputFluids(GTOMaterials.Triphenylphosphine.getFluid(1000))
                .outputItems(dust, GTOMaterials.ZincChloride, 3)
                .duration(400).EUt(VA[HV]).save();
    }
}
