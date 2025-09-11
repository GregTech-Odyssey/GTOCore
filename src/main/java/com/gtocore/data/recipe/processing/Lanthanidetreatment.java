package com.gtocore.data.recipe.processing;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.NANITES;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class Lanthanidetreatment {

    public static void init() {
        DIGESTION_TREATMENT_RECIPES.recipeBuilder("monazite_rare_earth_turbid_liquid1")
                .inputItems(dust, Monazite, 2)
                .inputFluids(NitricAcid.getFluid(400))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(MonaziteRareEarthTurbidLiquid.getFluid(400))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1440)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("diluted_monazite_slurry2")
                .inputItems(dust, Saltpeter, 90)
                .inputFluids(MonaziteRareEarthTurbidLiquid.getFluid(90000))
                .inputFluids(Water.getFluid(100000))
                .outputItems(dust, ThoritePowder, 90)
                .outputFluids(DilutedMonaziteSlurry.getFluid(100000))
                .duration(1600)
                .EUt(480)
                .save();

        SIFTER_RECIPES.recipeBuilder("diluted_monazite_slurry3")
                .inputFluids(DilutedMonaziteSlurry.getFluid(1000))
                .chancedOutput(dust, AcidicMonazitePowder, 1, 9000, 0)
                .chancedOutput(dust, SiliconDioxide, 1, 7500, 0)
                .chancedOutput(dust, Rutile, 1, 2000, 0)
                .chancedOutput(dust, RedZirconPowder, 1, 500, 0)
                .chancedOutput(dust, Ilmenite, 1, 2000, 0)
                .duration(400)
                .EUt(120)
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("red_zircon_powder4")
                .inputItems(dust, RedZirconPowder, 4)
                .outputItems(dust, Zircon, 3)
                .outputItems(dust, GraniteRed, 2)
                .duration(320)
                .EUt(90)
                .save();

        SIFTER_RECIPES.recipeBuilder("acidic_monazite_powder7")
                .inputItems(dust, AcidicMonazitePowder, 1)
                .chancedOutput(dust, ConcentratedMonaziteRareEarthHydroxidePowder, 1, 8000, 0)
                .chancedOutput(dust, ThoriumPhosphateRefinedPowder, 1, 7000, 0)
                .chancedOutput(dust, UraniumFilterResiduePowder, 1, 4000, 0)
                .chancedOutput(dust, UraniumFilterResiduePowder, 1, 2000, 0)
                .duration(300)
                .EUt(256)
                .save();

        THERMAL_CENTRIFUGE_RECIPES.recipeBuilder("thorium_phosphate_refined_powder9")
                .inputItems(dust, ThoriumPhosphateRefinedPowder, 1)
                .outputItems(dust, Phosphate, 1)
                .chancedOutput(dust, Monazite, 1, 3000, 300)
                .duration(200)
                .EUt(240)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("uranium_filter_residue_powder12")
                .inputItems(dust, UraniumFilterResiduePowder, 1)
                .inputFluids(HydrofluoricAcid.getFluid(100))
                .chancedOutput(dust, Uranium235, 1, 4000, 0)
                .chancedOutput(dust, Uranium235, 1, 3000, 0)
                .chancedOutput(dust, Uranium235, 1, 2000, 0)
                .chancedOutput(dust, Uranium235, 1, 1000, 0)
                .duration(180)
                .EUt(120)
                .save();

        MIXER_RECIPES.recipeBuilder("concentrated_nitride_monazite_rare_earth_solution16")
                .inputItems(dust, ConcentratedMonaziteRareEarthHydroxidePowder, 2)
                .inputItems(dust, CeriumRichMixturePowder, 3)
                .circuitMeta(2)
                .inputFluids(NitricAcid.getFluid(1000))
                .outputFluids(NitricLeachateFromMonazite.getFluid(2000))
                .duration(240)
                .EUt(120)
                .save();

        MIXER_RECIPES.recipeBuilder("concentrated_nitride_monazite_rare_earth_solution17")
                .inputItems(dust, ConcentratedMonaziteRareEarthHydroxidePowder, 2)
                .circuitMeta(1)
                .inputFluids(NitricAcid.getFluid(1000))
                .outputFluids(NitricLeachateFromMonazite.getFluid(1000))
                .duration(200)
                .EUt(120)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("cerium_rich_mixture_powder18")
                .inputItems(dust, Bastnasite, 6)
                .outputItems(dust, CeriumRichMixturePowder, 1)
                .outputItems(dust, Carbon, 1)
                .outputFluids(Oxygen.getFluid(3000))
                .outputFluids(Fluorine.getFluid(1000))
                .duration(120)
                .EUt(90)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("cerium_chloride_powder19")
                .inputItems(dust, CeriumRichMixturePowder, 15)
                .inputFluids(HydrofluoricAcid.getFluid(750))
                .outputItems(dust, CeriumChloridePowder, 1)
                .outputItems(dust, Monazite, 1)
                .outputFluids(Water.getFluid(750))
                .duration(300)
                .EUt(480)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("cerium_oxalate_powder20")
                .inputItems(dust, CeriumChloridePowder, 15)
                .inputFluids(OxalicAcid.getFluid(3000))
                .outputItems(dust, CeriumOxalatePowder, 1)
                .outputFluids(HydrochloricAcid.getFluid(6000))
                .duration(300)
                .EUt(480)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("oxalic_acid21")
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputFluids(Oxygen.getFluid(40000))
                .inputFluids(Ethanol.getFluid(9000))
                .outputFluids(OxalicAcid.getFluid(9000))
                .outputFluids(Water.getFluid(20000))
                .duration(400)
                .EUt(240)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("oxalic_acid22")
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputFluids(Oxygen.getFluid(27000))
                .inputFluids(Methanol.getFluid(9000))
                .inputFluids(CarbonMonoxide.getFluid(9000))
                .outputFluids(OxalicAcid.getFluid(9000))
                .outputFluids(Water.getFluid(20000))
                .duration(400)
                .EUt(240)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("oxalic_acid23")
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputItems(dust, Sugar, 24)
                .inputFluids(NitricAcid.getFluid(6000))
                .outputFluids(OxalicAcid.getFluid(3000))
                .outputFluids(NitricOxide.getFluid(6000))
                .duration(300)
                .EUt(120)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("vanadium_pentoxide_powder24")
                .inputItems(dust, Vanadium, 2)
                .inputFluids(Oxygen.getFluid(5000))
                .outputItems(dust, VanadiumPentoxidePowder, 7)
                .duration(200)
                .EUt(120)
                .blastFurnaceTemp(2500)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("cerium_oxalate_powder25")
                .inputItems(dust, CeriumOxalatePowder, 5)
                .inputItems(dust, Carbon, 3)
                .outputItems(dust, CeriumOxide, 5)
                .outputFluids(CarbonMonoxide.getFluid(9000))
                .duration(200)
                .EUt(480)
                .blastFurnaceTemp(800)
                .save();

        DIGESTION_TREATMENT_RECIPES.recipeBuilder("concentrated_cerium_chloride_solution26")
                .inputItems(dust, CeriumRichMixturePowder, 1)
                .inputFluids(Chlorine.getFluid(10000))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(ConcentratedCeriumChlorideSolution.getFluid(1000))
                .duration(40)
                .EUt(122880)
                .blastFurnaceTemp(3820)
                .save();

        SIFTER_RECIPES.recipeBuilder("concentrated_nitric_leachate_from_monazite27")
                .inputFluids(NitricLeachateFromMonazite.getFluid(1000))
                .chancedOutput(dust, MonaziteRareEarthPrecipitatePowder, 1, 9000, 0)
                .chancedOutput(dust, CeriumOxide, 1, 1000, 100)
                .chancedOutput(dust, EuropiumOxide, 1, 500, 0)
                .duration(200)
                .EUt(240)
                .save();

        MIXER_RECIPES.recipeBuilder("saturated_monazite_rare_earth_powder30")
                .inputItems(dust, MonaziteRareEarthPrecipitatePowder, 1)
                .inputItems(dust, SamariumRefinedPowder, 2)
                .inputFluids(Acetone.getFluid(1000))
                .outputItems(dust, SaturatedMonaziteRareEarthPowder, 3)
                .duration(200)
                .EUt(240)
                .save();

        MIXER_RECIPES.recipeBuilder("saturated_monazite_rare_earth_powder32")
                .inputItems(dust, MonaziteRareEarthPrecipitatePowder, 1)
                .inputItems(dust, Salt, 1)
                .inputFluids(Acetone.getFluid(1000))
                .outputItems(dust, SaturatedMonaziteRareEarthPowder, 2)
                .duration(200)
                .EUt(240)
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("samarium_precipitate_powder33")
                .inputItems(dust, SaturatedMonaziteRareEarthPowder, 4)
                .outputItems(dust, RareEarth, 5)
                .outputItems(dust, SamariumPrecipitatePowder, 3)
                .outputFluids(Chloromethane.getFluid(400))
                .duration(160)
                .EUt(1920)
                .save();

        SIFTER_RECIPES.recipeBuilder("samarium_precipitate_powder34")
                .inputItems(dust, SamariumPrecipitatePowder, 3)
                .outputItems(dust, Samarium, 2)
                .chancedOutput(dust, Gadolinium, 1, 2000, 0)
                .duration(160)
                .EUt(1920)
                .save();

        DIGESTION_TREATMENT_RECIPES.recipeBuilder("samarium_rare_earth_turbid_liquid35")
                .inputItems(dust, SamariumRefinedPowder, 16)
                .inputFluids(NitricAcid.getFluid(200))
                .outputItems(dust, ThoriumPhosphateRefinedPowder, 1)
                .outputFluids(SamariumRrareEearthTurbidLiquid.getFluid(800))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(2680)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("samarium_rare_earth_turbid_liquid36")
                .inputFluids(SamariumRrareEearthTurbidLiquid.getFluid(10000))
                .inputFluids(NitricAcid.getFluid(10000))
                .chancedOutput(dust, CeriumRichMixturePowder, 10, 6000, 0)
                .chancedOutput(dust, CeriumOxide, 10, 8000, 0)
                .outputFluids(SamariumRareEarthSlurry.getFluid(20000))
                .duration(800)
                .EUt(1920)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("samarium_rare_earth_slurry37")
                .inputFluids(SamariumRareEarthSlurry.getFluid(10000))
                .inputFluids(Water.getFluid(10000))
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 10, 9000, 0)
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 10, 6000, 0)
                .outputFluids(SamariumRareEarthDilutedSolution.getFluid(10000))
                .duration(1200)
                .EUt(1920)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("lanthanum_chloride38")
                .inputItems(dust, NeodymiumRareEarthConcentratePowder, 1)
                .inputFluids(HydrochloricAcid.getFluid(2000))
                .outputItems(dust, LanthanumChloride, 1)
                .outputItems(dust, NeodymiumOxide, 1)
                .duration(450)
                .EUt(120)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("samarium_oxalate_with_impurities39")
                .inputFluids(SamariumRareEarthDilutedSolution.getFluid(2000))
                .inputFluids(OxalicAcid.getFluid(3000))
                .outputItems(dust, SamariumOxalateWithImpurities, 5)
                .chancedOutput(dust, PhosphorusFreeSamariumConcentratePowder, 3, 1000, 0)
                .outputFluids(SamariumRrareEearthTurbidLiquid.getFluid(50))
                .duration(280)
                .EUt(480)
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("samarium40")
                .inputItems(dust, PhosphorusFreeSamariumConcentratePowder, 1)
                .chancedOutput(dust, Samarium, 1, 9000, 0)
                .chancedOutput(dust, ThoritePowder, 2, 8000, 0)
                .duration(100)
                .EUt(1920)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("samarium_chloride_concentrate_solution41")
                .inputItems(dust, SamariumOxalateWithImpurities, 5)
                .inputFluids(HydrochloricAcid.getFluid(6000))
                .outputItems(dust, SamariumChlorideWithImpurities, 8)
                .outputFluids(CarbonMonoxide.getFluid(6000))
                .duration(100)
                .EUt(960)
                .save();

        MIXER_RECIPES.recipeBuilder("samarium_chloride_sodium_chloride_mixture_powder42")
                .inputItems(dust, Salt, 1)
                .inputItems(dust, SamariumChlorideWithImpurities, 2)
                .outputItems(dust, SamariumChlorideSodiumChlorideMixturePowder, 3)
                .duration(80)
                .EUt(30)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("samarium43")
                .inputItems(dust, SamariumChlorideSodiumChlorideMixturePowder, 6)
                .outputItems(dust, Samarium, 1)
                .outputItems(dust, Sodium, 1)
                .outputFluids(Chlorine.getFluid(4000))
                .duration(20)
                .EUt(7680)
                .save();

        DIGESTION_TREATMENT_RECIPES.recipeBuilder("monazite_rare_earth_turbid_liquid_output44")
                .inputItems(dust, SamariumRefinedPowder, 1)
                .inputFluids(Chlorine.getFluid(10000))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(SamariumChlorideConcentrateSolution.getFluid(1000))
                .duration(40)
                .EUt(122880)
                .blastFurnaceTemp(3820)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("lanthanum_chloride_with_impurities45")
                .inputItems(dust, Lanthanum, 9)
                .inputFluids(SamariumChlorideWithImpurities.getFluid(5184))
                .outputItems(dust, LanthanumChlorideWithImpurities, 36)
                .outputFluids(Samarium.getFluid(2304))
                .duration(100)
                .EUt(122880)
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("lanthanum_chloride_with_impurities46")
                .inputItems(dust, LanthanumChlorideWithImpurities, 36)
                .outputItems(dust, LanthanumChloride, 36)
                .duration(100)
                .EUt(1920)
                .save();

        /* 氟碳镧铈线 */
        DIGESTION_TREATMENT_RECIPES.recipeBuilder("fluoro_carbon_lanthanide_cerium_solution47")
                .inputItems(dust, Bastnasite, 2)
                .inputFluids(NitricAcid.getFluid(400))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(FluoroCarbonLanthanideCeriumSolution.getFluid(400))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1440)
                .save();

        CRACKING_RECIPES.recipeBuilder("steam_cracked_fluoro_carbon_lanthanide_slurry48")
                .inputFluids(FluoroCarbonLanthanideCeriumSolution.getFluid(1000))
                .inputFluids(Steam.getFluid(1000))
                .outputFluids(SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(2000))
                .duration(300)
                .EUt(480)
                .save();

        MIXER_RECIPES.recipeBuilder("fluoro_carbon_lanthanide_cerium_solution49")
                .inputFluids(SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(1000))
                .inputFluids(NitricAcid.getFluid(400))
                .outputFluids(ModulatedFluoroCarbonLanthanideSlurry.getFluid(1320))
                .duration(200)
                .EUt(120)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("diluted_fluoro_carbon_lanthanide_slurry51")
                .inputItems(dust, Saltpeter, 10)
                .inputFluids(ModulatedFluoroCarbonLanthanideSlurry.getFluid(10000))
                .inputFluids(Water.getFluid(10000))
                .outputItems(dust, Stone, 10)
                .outputFluids(DilutedFluoroCarbonLanthanideSlurry.getFluid(20000))
                .duration(800)
                .EUt(1920)
                .save();

        SIFTER_RECIPES.recipeBuilder("filtered_fluoro_carbon_lanthanide_slurry52")
                .inputFluids(DilutedFluoroCarbonLanthanideSlurry.getFluid(1000))
                .chancedOutput(dust, SiliconDioxide, 1, 9000, 0)
                .chancedOutput(dust, Rutile, 1, 7500, 0)
                .chancedOutput(dust, FluoroCarbonLanthanideCeriumOxidePowder, 1, 5000, 0)
                .chancedOutput(dust, RedZirconPowder, 1, 1000, 0)
                .chancedOutput(dust, Ilmenite, 1, 500, 0)
                .duration(200)
                .EUt(240)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("calcined_rare_earth_oxide_powder55")
                .inputItems(dust, FluoroCarbonLanthanideCeriumOxidePowder, 1)
                .inputFluids(HydrochloricAcid.getFluid(500))
                .inputFluids(Acetone.getFluid(1000))
                .outputFluids(FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000))
                .duration(240)
                .EUt(120)
                .blastFurnaceTemp(1200)
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("samarium_rare_earth_concentrate_powder61")
                .inputFluids(FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000))
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 1, 8000, 0)
                .chancedOutput(dust, SamariumRareEarthConcentratePowder, 1, 5000, 0)
                .outputFluids(Acetone.getFluid(450))
                .duration(450)
                .EUt(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fluorinated_samarium_concentrate_powder62")
                .inputItems(dust, SamariumRareEarthConcentratePowder, 1)
                .inputFluids(HydrofluoricAcid.getFluid(2000))
                .outputItems(dust, RareEarth, 4)
                .outputItems(dust, FluorinatedSamariumConcentratePowder, 1)
                .duration(300)
                .EUt(480)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("samarium_terbium_mixture_powder63")
                .inputItems(dust, FluorinatedSamariumConcentratePowder, 4)
                .inputItems(dust, Calcium, 4)
                .outputItems(dust, Holmium, 1)
                .outputItems(dust, SamariumTerbiumMixturePowder, 4)
                .outputItems(dust, Fluorite, 12)
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1200)
                .save();

        MIXER_RECIPES.recipeBuilder("nitrided_samarium_terbium_mixture_powder64")
                .inputItems(dust, SamariumTerbiumMixturePowder, 1)
                .inputItems(dust, AmmoniumChloride, 9)
                .outputItems(dust, NitridedSamariumTerbiumMixturePowder, 1)
                .duration(300)
                .EUt(480)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("terbium_nitrate_powder65")
                .inputItems(dust, NitridedSamariumTerbiumMixturePowder, 4)
                .inputItems(dust, Copper, 1)
                .outputItems(dust, TerbiumNitratePowder, 2)
                .outputItems(dust, SamariumPrecipitatePowder, 2)
                .duration(320)
                .EUt(1920)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("concentrated_cerium_chloride_solution66")
                .inputFluids(ConcentratedCeriumChlorideSolution.getFluid(1000))
                .outputItems(dust, CeriumOxide, 4)
                .outputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                .duration(20)
                .EUt(491520)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("samarium_chloride_concentrate_solution67")
                .inputFluids(SamariumChlorideConcentrateSolution.getFluid(1000))
                .outputItems(dust, SamariumOxide, 4)
                .outputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                .duration(20)
                .EUt(491520)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("samarium_chloride_concentrate_solution69")
                .inputFluids(EnrichedRareEarthChlorideSolution.getFluid(1000))
                .outputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                .outputItems(dustSmall, LanthanumOxide, 1)
                .outputItems(dustSmall, PraseodymiumOxide, 1)
                .outputItems(dustSmall, NeodymiumOxide, 1)
                .outputItems(dustSmall, CeriumOxide, 1)
                .outputItems(dustSmall, EuropiumOxide, 1)
                .outputItems(dustSmall, GadoliniumOxide, 1)
                .outputItems(dustSmall, SamariumOxide, 1)
                .outputItems(dustSmall, TerbiumOxide, 1)
                .outputItems(dustSmall, DysprosiumOxide, 1)
                .outputItems(dustSmall, HolmiumOxide, 1)
                .outputItems(dustSmall, ErbiumOxide, 1)
                .outputItems(dustSmall, ThuliumOxide, 1)
                .outputItems(dustSmall, YtterbiumOxide, 1)
                .outputItems(dustSmall, LutetiumOxide, 1)
                .outputItems(dustSmall, ScandiumOxide, 1)
                .outputItems(dustSmall, YttriumOxide, 1)
                .outputItems(dustSmall, PromethiumOxide, 1)
                .duration(20)
                .EUt(491520)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("samarium_chloride_concentrate_solution70")
                .inputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                .outputFluids(RedMud.getFluid(1000))
                .outputItems(dustSmall, LanthanumOxide, 1)
                .outputItems(dustSmall, PraseodymiumOxide, 1)
                .outputItems(dustSmall, NeodymiumOxide, 1)
                .outputItems(dustSmall, CeriumOxide, 1)
                .outputItems(dustSmall, EuropiumOxide, 1)
                .outputItems(dustSmall, GadoliniumOxide, 1)
                .outputItems(dustSmall, SamariumOxide, 1)
                .outputItems(dustSmall, TerbiumOxide, 1)
                .outputItems(dustSmall, DysprosiumOxide, 1)
                .outputItems(dustSmall, HolmiumOxide, 1)
                .outputItems(dustSmall, ErbiumOxide, 1)
                .outputItems(dustSmall, ThuliumOxide, 1)
                .outputItems(dustSmall, YtterbiumOxide, 1)
                .outputItems(dustSmall, LutetiumOxide, 1)
                .outputItems(dustSmall, ScandiumOxide, 1)
                .outputItems(dustSmall, YttriumOxide, 1)
                .outputItems(dustSmall, PromethiumOxide, 1)
                .duration(20)
                .EUt(491520)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("raw_adamantine")
                .inputItems(TagPrefix.dust, GTOMaterials.NaquadahContainRareEarth)
                .chancedOutput(TagPrefix.rawOre, GTOMaterials.AdamantineCompounds, 5000, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.EnrichedNaquadahSulfate, 6)
                .outputItems(TagPrefix.dust, GTMaterials.NaquadriaSulfate, 6)
                .EUt(7864320)
                .duration(200)
                .save();

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder("rare_earth_centrifugal")
                .inputItems(TagPrefix.dust, GTOMaterials.RareEarthMetal)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Lanthanum)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Cerium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Neodymium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Promethium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Samarium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Europium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Praseodymium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Gadolinium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Terbium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Dysprosium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Holmium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Erbium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Thulium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Ytterbium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Scandium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Lutetium)
                .outputItems(TagPrefix.dustSmall, GTMaterials.Yttrium)
                .EUt(491520)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("make_p507_1")
                .inputItems(GTOTagPrefix.CATALYST, Copper)
                .inputFluids(SeedOil.getFluid(3000))
                .inputFluids(Hydrogen.getFluid(8000))
                .outputFluids(EthylHexanol.getFluid(1000))
                .duration(400)
                .EUt(480)
                .save();

        Material[] rareEarthElements = {
                Lanthanum,
                Cerium,
                Praseodymium,
                Neodymium,
                Promethium,
                Samarium,
                Europium,
                Gadolinium,
                Terbium,
                Dysprosium,
                Holmium,
                Erbium,
                Thulium,
                Ytterbium,
                Lutetium,
                Scandium,
                Yttrium
        };

        Material[] extractionNanoResins = {
                LanthanumExtractionNanoResin,
                CeriumExtractionNanoResin,
                PraseodymiumExtractionNanoResin,
                NeodymiumExtractionNanoResin,
                PromethiumExtractionNanoResin,
                SamariumExtractionNanoResin,
                EuropiumExtractionNanoResin,
                GadoliniumExtractionNanoResin,
                TerbiumExtractionNanoResin,
                DysprosiumExtractionNanoResin,
                HolmiumExtractionNanoResin,
                ErbiumExtractionNanoResin,
                ThuliumExtractionNanoResin,
                YtterbiumExtractionNanoResin,
                LutetiumExtractionNanoResin,
                ScandiumExtractionNanoResin,
                YttriumExtractionNanoResin
        };

        Material[] extractedNanoResins = {
                LanthanumExtractedNanoResin,
                CeriumExtractedNanoResin,
                PraseodymiumExtractedNanoResin,
                NeodymiumExtractedNanoResin,
                PromethiumExtractedNanoResin,
                SamariumExtractedNanoResin,
                EuropiumExtractedNanoResin,
                GadoliniumExtractedNanoResin,
                TerbiumExtractedNanoResin,
                DysprosiumExtractedNanoResin,
                HolmiumExtractedNanoResin,
                ErbiumExtractedNanoResin,
                ThuliumExtractedNanoResin,
                YtterbiumExtractedNanoResin,
                LutetiumExtractedNanoResin,
                ScandiumExtractedNanoResin,
                YttriumExtractedNanoResin
        };

        for (int i = 0; i < rareEarthElements.length; i++) {
            Material rareEarthElement = rareEarthElements[i];
            Material nanoResin = extractionNanoResins[i];
            Material extractedNanoResin = extractedNanoResins[i];

            LASER_ENGRAVER_RECIPES.recipeBuilder("make_extraction_nano_resin_" + i)
                    .inputItems(NANITES, Carbon, 1)
                    .notConsumable(lens, NetherStar, 1)
                    .inputFluids(rareEarthElement.getFluid(4000))
                    .inputFluids(P507.getFluid(4000))
                    .outputFluids(nanoResin.getFluid(1000))
                    .duration(1200)
                    .EUt(491520)
                    .addData("special", true)
                    .save();

            DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("make_extraction_nano_resin_" + i)
                    .inputItems(NANITES, Carbon, 1)
                    .notConsumable(lens, NetherStar, 1)
                    .inputFluids(rareEarthElement.getFluid(4000))
                    .inputFluids(P507.getFluid(4000))
                    .outputFluids(nanoResin.getFluid(1000))
                    .duration(1200)
                    .EUt(491520)
                    .save();

            LARGE_CHEMICAL_RECIPES.recipeBuilder("extraction_nano_resin_a_" + i)
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(EnrichedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(20)
                    .EUt(491520)
                    .save();

            LARGE_CHEMICAL_RECIPES.recipeBuilder("extraction_nano_resin_b_" + i)
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(EnrichedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(80)
                    .EUt(491520)
                    .save();

            LARGE_CHEMICAL_RECIPES.recipeBuilder("extraction_nano_resin_c_" + i)
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(320)
                    .EUt(491520)
                    .save();

            ELECTROLYZER_RECIPES.recipeBuilder("break_down_extracted_nano_resin_" + i)
                    .inputFluids(extractedNanoResin.getFluid(1000))
                    .outputItems(dust, rareEarthElement, 1)
                    .outputFluids(nanoResin.getFluid(1000))
                    .outputFluids(Chlorine.getFluid(3000))
                    .duration(100)
                    .EUt(122880)
                    .save();
        }
    }
}
