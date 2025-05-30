package com.gto.gtocore.data.recipe.processing;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.api.data.tag.GTOTagPrefix.nanites;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public final class Lanthanidetreatment {

    public static void init(Consumer<FinishedRecipe> provider) {
        EXTRACTOR_RECIPES.recipeBuilder("monazite_extraction")
                .inputItems(gemExquisite, Monazite)
                .outputItems(dustSmall, RareEarth, 10)
                .outputFluids(Helium.getFluid(1000))
                .duration(640).EUt(256).save(provider);

        DIGESTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("monazite_rare_earth_turbid_liquid1"))
                .inputItems(dust, Monazite, 2)
                .inputFluids(NitricAcid.getFluid(400))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(MonaziteRareEarthTurbidLiquid.getFluid(400))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1440)
                .save(provider);

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("diluted_monazite_slurry2"))
                .inputItems(dust, Saltpeter, 90)
                .inputFluids(MonaziteRareEarthTurbidLiquid.getFluid(90000))
                .inputFluids(Water.getFluid(900000))
                .outputItems(dust, ThoritePowder, 90)
                .outputItems(dust, Monazite, 10)
                .outputFluids(DilutedMonaziteSlurry.getFluid(1000000))
                .duration(2000)
                .EUt(480)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("diluted_monazite_slurry3"))
                .inputFluids(DilutedMonaziteSlurry.getFluid(1000))
                .chancedOutput(dust, MonaziteSulfatePowder, 1, 9000, 0)
                .chancedOutput(dust, SiliconDioxide, 1, 7500, 0)
                .chancedOutput(dust, Rutile, 1, 2000, 0)
                .chancedOutput(dust, RedZirconPowder, 1, 500, 0)
                .chancedOutput(dust, Ilmenite, 1, 2000, 0)
                .duration(400)
                .EUt(120)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("red_zircon_powder4"))
                .inputItems(dust, RedZirconPowder, 4)
                .outputItems(dust, Zircon, 3)
                .outputItems(dust, GraniteRed, 2)
                .duration(320)
                .EUt(90)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("monazite_sulfate_powder5"))
                .inputItems(dust, MonaziteSulfatePowder, 1)
                .inputFluids(Water.getFluid(6000))
                .outputFluids(DilutedMonaziteSulfateSolution.getFluid(7000))
                .duration(240)
                .EUt(120)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("diluted_monazite_sulfate_solution6"))
                .inputFluids(DilutedMonaziteSulfateSolution.getFluid(9000))
                .inputFluids(AmmoniumNitrateSolution.getFluid(1800))
                .outputItems(dust, AcidicMonazitePowder, 3)
                .duration(160)
                .EUt(480)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("acidic_monazite_powder7"))
                .inputItems(dust, AcidicMonazitePowder, 1)
                .chancedOutput(dust, MonaziteRareEarthFilterResiduePowder, 1, 9000, 0)
                .chancedOutput(dust, ThoriumPhosphateFilterCakePowder, 1, 7000, 0)
                .duration(300)
                .EUt(256)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("thorium_phosphate_filter_cake_powder8"))
                .inputItems(dust, ThoriumPhosphateFilterCakePowder, 1)
                .outputItems(dust, ThoriumPhosphateRefinedPowder, 1)
                .duration(100)
                .EUt(120)
                .blastFurnaceTemp(1200)
                .save(provider);

        THERMAL_CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("thorium_phosphate_refined_powder9"))
                .inputItems(dust, ThoriumPhosphateRefinedPowder, 1)
                .outputItems(dust, Monazite, 1)
                .outputItems(dust, Phosphate, 1)
                .duration(200)
                .EUt(240)
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("monazite_rare_earth_filter_residue_powder10"))
                .inputItems(dust, MonaziteRareEarthFilterResiduePowder, 1)
                .inputFluids(AmmoniumNitrateSolution.getFluid(320))
                .outputItems(dust, NeutralizedMonaziteRareEarthFilterResiduePowder, 1)
                .duration(120)
                .EUt(240)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("neutralized_monazite_rare_earth_filter_residue_powder11"))
                .inputItems(dust, NeutralizedMonaziteRareEarthFilterResiduePowder, 1)
                .chancedOutput(dust, ConcentratedMonaziteRareEarthHydroxidePowder, 1, 9000, 0)
                .chancedOutput(dust, UraniumFilterResiduePowder, 1, 5000, 0)
                .chancedOutput(dust, UraniumFilterResiduePowder, 1, 4000, 0)
                .duration(160)
                .EUt(480)
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("uranium_filter_residue_powder12"))
                .inputItems(dust, UraniumFilterResiduePowder, 1)
                .inputFluids(HydrofluoricAcid.getFluid(100))
                .outputItems(dust, NeutralizedUraniumFilterResiduePowder, 1)
                .duration(180)
                .EUt(120)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("neutralized_uranium_filter_residue_powder13"))
                .inputItems(dust, NeutralizedUraniumFilterResiduePowder, 1)
                .chancedOutput(dust, Uranium235, 1, 4500, 0)
                .chancedOutput(dust, Uranium235, 1, 4000, 0)
                .chancedOutput(dust, Uranium235, 1, 3000, 0)
                .chancedOutput(dust, Uranium235, 1, 2000, 0)
                .chancedOutput(dust, Uranium235, 1, 1000, 0)
                .duration(240)
                .EUt(30)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("concentrated_monazite_rare_earth_hydroxide_powder14"))
                .inputItems(dust, ConcentratedMonaziteRareEarthHydroxidePowder, 1)
                .outputItems(dust, DriedConcentratedNitricMonaziteRareEarthPowder, 1)
                .duration(300)
                .EUt(120)
                .blastFurnaceTemp(1200)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("dried_concentrated_nitric_monazite_rare_earth_powder15"))
                .inputItems(dust, DriedConcentratedNitricMonaziteRareEarthPowder, 1)
                .inputFluids(NitricAcid.getFluid(500))
                .outputFluids(ConcentratedNitrideMonaziteRareEarthSolution.getFluid(500))
                .duration(240)
                .EUt(480)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("concentrated_nitride_monazite_rare_earth_solution16"))
                .inputItems(dust, CeriumRichMixturePowder, 3)
                .inputFluids(ConcentratedNitrideMonaziteRareEarthSolution.getFluid(1000))
                .outputFluids(NitricLeachateFromMonazite.getFluid(2000))
                .duration(240)
                .EUt(120)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("concentrated_nitride_monazite_rare_earth_solution17"))
                .inputFluids(Water.getFluid(1000))
                .inputFluids(ConcentratedNitrideMonaziteRareEarthSolution.getFluid(1000))
                .outputFluids(NitricLeachateFromMonazite.getFluid(1000))
                .duration(200)
                .EUt(120)
                .save(provider);

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("cerium_rich_mixture_powder18"))
                .inputItems(dust, Bastnasite, 6)
                .outputItems(dust, CeriumRichMixturePowder, 1)
                .outputItems(dust, Carbon, 1)
                .outputFluids(Oxygen.getFluid(3000))
                .outputFluids(Fluorine.getFluid(1000))
                .duration(120)
                .EUt(90)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("cerium_chloride_powder19"))
                .inputItems(dust, CeriumRichMixturePowder, 15)
                .inputFluids(HydrofluoricAcid.getFluid(750))
                .outputItems(dust, CeriumChloridePowder, 1)
                .outputItems(dust, Monazite, 1)
                .outputFluids(Water.getFluid(750))
                .duration(300)
                .EUt(480)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("cerium_oxalate_powder20"))
                .inputItems(dust, CeriumChloridePowder, 15)
                .inputFluids(OxalicAcid.getFluid(3000))
                .outputItems(dust, CeriumOxalatePowder, 1)
                .outputFluids(HydrochloricAcid.getFluid(6000))
                .duration(300)
                .EUt(480)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("oxalic_acid21"))
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputFluids(Oxygen.getFluid(40000))
                .inputFluids(Ethanol.getFluid(9000))
                .outputFluids(OxalicAcid.getFluid(9000))
                .outputFluids(Water.getFluid(20000))
                .duration(400)
                .EUt(240)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("oxalic_acid22"))
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputFluids(Oxygen.getFluid(27000))
                .inputFluids(Methanol.getFluid(9000))
                .inputFluids(CarbonMonoxide.getFluid(9000))
                .outputFluids(OxalicAcid.getFluid(9000))
                .outputFluids(Water.getFluid(20000))
                .duration(400)
                .EUt(240)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("oxalic_acid23"))
                .notConsumable(dust, VanadiumPentoxidePowder, 1)
                .inputItems(dust, Sugar, 24)
                .inputFluids(NitricAcid.getFluid(6000))
                .outputFluids(OxalicAcid.getFluid(3000))
                .outputFluids(NitricOxide.getFluid(6000))
                .duration(300)
                .EUt(120)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("vanadium_pentoxide_powder24"))
                .inputItems(dust, Vanadium, 2)
                .inputFluids(Oxygen.getFluid(5000))
                .outputItems(dust, VanadiumPentoxidePowder, 7)
                .duration(200)
                .EUt(120)
                .blastFurnaceTemp(2500)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cerium_oxalate_powder25"))
                .inputItems(dust, CeriumOxalatePowder, 5)
                .inputItems(dust, Carbon, 3)
                .outputItems(dust, CeriumOxide, 5)
                .outputFluids(CarbonMonoxide.getFluid(9000))
                .duration(200)
                .EUt(480)
                .blastFurnaceTemp(800)
                .save(provider);

        DIGESTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("concentrated_cerium_chloride_solution26"))
                .inputItems(dust, CeriumRichMixturePowder, 1)
                .inputFluids(Chlorine.getFluid(10000))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(ConcentratedCeriumChlorideSolution.getFluid(1000))
                .duration(40)
                .EUt(122880)
                .blastFurnaceTemp(3820)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("concentrated_nitric_leachate_from_monazite27"))
                .inputFluids(NitricLeachateFromMonazite.getFluid(1000))
                .chancedOutput(dust, CeriumOxide, 1, 1000, 100)
                .outputFluids(ConcentratedNitricLeachateFromMonazite.getFluid(1000))
                .duration(200)
                .EUt(240)
                .save(provider);

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("cooling_concentrated_nitric_monazite_rare_earth_powder28"))
                .inputFluids(ConcentratedNitricLeachateFromMonazite.getFluid(1000))
                .outputItems(dust, CoolingConcentratedNitricMonaziteRareEarthPowder, 1)
                .duration(100)
                .EUt(240)
                .save(provider);

        ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("monazite_rare_earth_precipitate_powder29"))
                .inputItems(dust, CoolingConcentratedNitricMonaziteRareEarthPowder, 1)
                .chancedOutput(dust, MonaziteRareEarthPrecipitatePowder, 1, 9000, 0)
                .chancedOutput(dust, EuropiumOxide, 1, 500, 0)
                .duration(100)
                .EUt(240)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("heterogeneous_halide_monazite_rare_earth_mixture_powder30"))
                .inputItems(dust, MonaziteRareEarthPrecipitatePowder, 1)
                .inputFluids(Chlorine.getFluid(1000))
                .outputItems(dust, HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
                .duration(250)
                .EUt(480)
                .blastFurnaceTemp(1200)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("saturated_monazite_rare_earth_powder30"))
                .inputItems(dust, HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
                .inputItems(dust, SamariumRefinedPowder, 2)
                .inputFluids(Acetone.getFluid(1000))
                .outputItems(dust, SaturatedMonaziteRareEarthPowder, 3)
                .duration(200)
                .EUt(240)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("saturated_monazite_rare_earth_powder32"))
                .inputItems(dust, HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
                .inputItems(dust, Salt, 1)
                .inputFluids(Acetone.getFluid(1000))
                .outputItems(dust, SaturatedMonaziteRareEarthPowder, 2)
                .duration(200)
                .EUt(240)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("samarium_precipitate_powder33"))
                .inputItems(dust, SaturatedMonaziteRareEarthPowder, 4)
                .outputItems(dust, RareEarth, 5)
                .outputItems(dust, SamariumPrecipitatePowder, 3)
                .outputFluids(Chloromethane.getFluid(400))
                .duration(160)
                .EUt(1920)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("samarium_precipitate_powder34"))
                .inputItems(dust, SamariumPrecipitatePowder, 3)
                .outputItems(dust, Samarium, 2)
                .chancedOutput(dust, Gadolinium, 1, 2000, 0)
                .duration(160)
                .EUt(1920)
                .save(provider);

        DIGESTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("samarium_rare_earth_turbid_liquid35"))
                .inputItems(dust, SamariumRefinedPowder, 16)
                .inputFluids(NitricAcid.getFluid(200))
                .outputItems(dust, ThoriumPhosphateRefinedPowder, 1)
                .outputFluids(SamariumRrareEearthTurbidLiquid.getFluid(800))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(2680)
                .save(provider);

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("samarium_rare_earth_turbid_liquid36"))
                .inputFluids(SamariumRrareEearthTurbidLiquid.getFluid(10000))
                .inputFluids(NitricAcid.getFluid(10000))
                .chancedOutput(dust, CeriumRichMixturePowder, 10, 6000, 0)
                .chancedOutput(dust, CeriumOxide, 10, 8000, 0)
                .outputFluids(SamariumRareEarthSlurry.getFluid(20000))
                .duration(800)
                .EUt(1920)
                .save(provider);

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("samarium_rare_earth_slurry37"))
                .inputFluids(SamariumRareEarthSlurry.getFluid(10000))
                .inputFluids(Water.getFluid(90000))
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 10, 9000, 0)
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 10, 6000, 0)
                .outputFluids(SamariumRareEarthDilutedSolution.getFluid(100000))
                .duration(1200)
                .EUt(1920)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("lanthanum_chloride38"))
                .inputItems(dust, NeodymiumRareEarthConcentratePowder, 1)
                .inputFluids(HydrochloricAcid.getFluid(2000))
                .outputItems(dust, LanthanumChloride, 1)
                .outputItems(dust, NeodymiumOxide, 1)
                .duration(450)
                .EUt(120)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("samarium_oxalate_with_impurities39"))
                .inputFluids(SamariumRareEarthDilutedSolution.getFluid(2000))
                .inputFluids(OxalicAcid.getFluid(3000))
                .outputItems(dust, SamariumOxalateWithImpurities, 5)
                .chancedOutput(dust, PhosphorusFreeSamariumConcentratePowder, 3, 1000, 0)
                .outputFluids(SamariumRrareEearthTurbidLiquid.getFluid(50))
                .duration(280)
                .EUt(480)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("samarium40"))
                .inputItems(dust, PhosphorusFreeSamariumConcentratePowder, 1)
                .chancedOutput(dust, Samarium, 1, 9000, 0)
                .chancedOutput(dust, ThoritePowder, 2, 8000, 0)
                .duration(100)
                .EUt(1920)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("samarium_chloride_concentrate_solution41"))
                .inputItems(dust, SamariumOxalateWithImpurities, 5)
                .inputFluids(HydrochloricAcid.getFluid(6000))
                .outputItems(dust, SamariumChlorideWithImpurities, 8)
                .outputFluids(CarbonMonoxide.getFluid(6000))
                .duration(100)
                .EUt(960)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("samarium_chloride_sodium_chloride_mixture_powder42"))
                .inputItems(dust, Salt, 1)
                .inputItems(dust, SamariumChlorideWithImpurities, 2)
                .outputItems(dust, SamariumChlorideSodiumChlorideMixturePowder, 3)
                .duration(80)
                .EUt(30)
                .save(provider);

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("samarium43"))
                .inputItems(dust, SamariumChlorideSodiumChlorideMixturePowder, 6)
                .outputItems(dust, Samarium, 1)
                .outputItems(dust, Sodium, 1)
                .outputFluids(Chlorine.getFluid(4000))
                .duration(20)
                .EUt(7680)
                .save(provider);

        DIGESTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("monazite_rare_earth_turbid_liquid_output44"))
                .inputItems(dust, SamariumRefinedPowder, 1)
                .inputFluids(Chlorine.getFluid(10000))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(SamariumChlorideConcentrateSolution.getFluid(1000))
                .duration(40)
                .EUt(122880)
                .blastFurnaceTemp(3820)
                .save(provider);

        DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("lanthanum_chloride_with_impurities45"))
                .inputItems(dust, Lanthanum, 9)
                .inputFluids(SamariumChlorideWithImpurities.getFluid(5184))
                .outputItems(dust, LanthanumChlorideWithImpurities, 36)
                .outputFluids(Samarium.getFluid(2304))
                .duration(100)
                .EUt(122880)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("lanthanum_chloride_with_impurities46"))
                .inputItems(dust, LanthanumChlorideWithImpurities, 36)
                .outputItems(dust, LanthanumChloride, 36)
                .duration(100)
                .EUt(1920)
                .save(provider);

        /* 氟碳镧铈线 */
        DIGESTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("fluoro_carbon_lanthanide_cerium_solution47"))
                .inputItems(dust, Bastnasite, 2)
                .inputFluids(NitricAcid.getFluid(400))
                .outputItems(dust, SiliconDioxide, 1)
                .outputFluids(FluoroCarbonLanthanideCeriumSolution.getFluid(400))
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1440)
                .save(provider);

        CRACKING_RECIPES.recipeBuilder(GTOCore.id("steam_cracked_fluoro_carbon_lanthanide_slurry48"))
                .inputFluids(FluoroCarbonLanthanideCeriumSolution.getFluid(1000))
                .inputFluids(Steam.getFluid(1000))
                .outputFluids(SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(2000))
                .duration(300)
                .EUt(480)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("fluoro_carbon_lanthanide_cerium_solution49"))
                .inputFluids(SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(1000))
                .inputFluids(NitricAcid.getFluid(400))
                .outputFluids(ModulatedFluoroCarbonLanthanideSlurry.getFluid(1320))
                .duration(200)
                .EUt(120)
                .save(provider);

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("diluted_fluoro_carbon_lanthanide_slurry51"))
                .inputItems(dust, Saltpeter, 10)
                .inputFluids(ModulatedFluoroCarbonLanthanideSlurry.getFluid(10000))
                .inputFluids(Water.getFluid(100000))
                .outputItems(dust, Stone, 10)
                .outputFluids(DilutedFluoroCarbonLanthanideSlurry.getFluid(100000))
                .duration(1600)
                .EUt(1920)
                .save(provider);

        SIFTER_RECIPES.recipeBuilder(GTOCore.id("filtered_fluoro_carbon_lanthanide_slurry52"))
                .inputFluids(DilutedFluoroCarbonLanthanideSlurry.getFluid(1000))
                .chancedOutput(dust, SiliconDioxide, 1, 9000, 0)
                .chancedOutput(dust, Rutile, 1, 7500, 0)
                .chancedOutput(dust, RedZirconPowder, 1, 1000, 0)
                .chancedOutput(dust, Ilmenite, 1, 500, 0)
                .outputFluids(FilteredFluoroCarbonLanthanideSlurry.getFluid(400))
                .duration(200)
                .EUt(240)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("fluoro_carbon_lanthanide_cerium_oxide_powder53"))
                .inputFluids(FilteredFluoroCarbonLanthanideSlurry.getFluid(1000))
                .outputItems(dust, FluoroCarbonLanthanideCeriumOxidePowder, 1)
                .duration(240)
                .EUt(480)
                .blastFurnaceTemp(1400)
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("acid_leached_fluoro_carbon_lanthanide_cerium_oxide_powder54"))
                .inputItems(dust, FluoroCarbonLanthanideCeriumOxidePowder, 1)
                .inputFluids(HydrochloricAcid.getFluid(500))
                .outputItems(dust, AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder, 1)
                .duration(200)
                .EUt(30)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("calcined_rare_earth_oxide_powder55"))
                .inputItems(dust, AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder, 1)
                .inputFluids(Oxygen.getFluid(1000))
                .outputItems(dust, CalcinedRareEarthOxidePowder, 1)
                .outputFluids(Fluorine.getFluid(13))
                .duration(240)
                .EUt(120)
                .blastFurnaceTemp(1200)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("wet_rare_earth_oxide_powder56"))
                .inputItems(dust, CalcinedRareEarthOxidePowder, 1)
                .inputFluids(Water.getFluid(200))
                .outputItems(dust, WetRareEarthOxidePowder, 1)
                .duration(100)
                .EUt(30)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("cerium_oxide_rare_earth_oxide_powder57"))
                .inputItems(dust, WetRareEarthOxidePowder, 1)
                .inputFluids(Fluorine.getFluid(4000))
                .outputItems(dust, CeriumOxideRareEarthOxidePowder, 1)
                .outputFluids(HydrofluoricAcid.getFluid(4000))
                .duration(300)
                .EUt(480)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("fluoro_carbon_lanthanide_cerium_rare_earth_oxide_powder58"))
                .inputItems(dust, CeriumOxideRareEarthOxidePowder, 1)
                .outputItems(dust, FluoroCarbonLanthanideCeriumRareEarthOxidePowder, 1)
                .outputItems(dust, CeriumOxide, 1)
                .duration(320)
                .EUt(480)
                .save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("nitrided_fluoro_carbon_lanthanide_cerium_rare_earth_oxide_solution59"))
                .inputItems(dust, FluoroCarbonLanthanideCeriumRareEarthOxidePowder, 1)
                .inputFluids(NitricAcid.getFluid(500))
                .outputFluids(NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution.getFluid(1000))
                .duration(150)
                .EUt(480)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("fluoro_carbon_lanthanide_cerium_rare_earth_suspension60"))
                .inputFluids(Acetone.getFluid(1000))
                .inputFluids(NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution.getFluid(1000))
                .outputFluids(FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000))
                .duration(350)
                .EUt(480)
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder(GTOCore.id("samarium_rare_earth_concentrate_powder61"))
                .inputFluids(FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000))
                .chancedOutput(dust, NeodymiumRareEarthConcentratePowder, 1, 8000, 0)
                .chancedOutput(dust, SamariumRareEarthConcentratePowder, 1, 5000, 0)
                .outputFluids(Acetone.getFluid(450))
                .duration(450)
                .EUt(120)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("fluorinated_samarium_concentrate_powder62"))
                .inputItems(dust, SamariumRareEarthConcentratePowder, 1)
                .inputFluids(HydrofluoricAcid.getFluid(2000))
                .outputItems(dust, RareEarth, 4)
                .outputItems(dust, FluorinatedSamariumConcentratePowder, 1)
                .duration(300)
                .EUt(480)
                .save(provider);

        REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("samarium_terbium_mixture_powder63"))
                .inputItems(dust, FluorinatedSamariumConcentratePowder, 4)
                .inputItems(dust, Calcium, 4)
                .outputItems(dust, Holmium, 1)
                .outputItems(dust, SamariumTerbiumMixturePowder, 4)
                .outputItems(dust, Fluorite, 12)
                .duration(200)
                .EUt(1920)
                .blastFurnaceTemp(1200)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("nitrided_samarium_terbium_mixture_powder64"))
                .inputItems(dust, SamariumTerbiumMixturePowder, 1)
                .inputItems(dust, AmmoniumChloride, 9)
                .outputItems(dust, NitridedSamariumTerbiumMixturePowder, 1)
                .duration(300)
                .EUt(480)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("terbium_nitrate_powder65"))
                .inputItems(dust, NitridedSamariumTerbiumMixturePowder, 4)
                .inputItems(dust, Copper, 1)
                .outputItems(dust, TerbiumNitratePowder, 2)
                .outputItems(dust, SamariumPrecipitatePowder, 2)
                .duration(320)
                .EUt(1920)
                .save(provider);

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("concentrated_cerium_chloride_solution66"))
                .inputFluids(ConcentratedCeriumChlorideSolution.getFluid(1000))
                .outputItems(dust, CeriumOxide, 4)
                .outputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                .duration(20)
                .EUt(491520)
                .save(provider);

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("samarium_chloride_concentrate_solution67"))
                .inputFluids(SamariumChlorideConcentrateSolution.getFluid(1000))
                .outputItems(dust, SamariumOxide, 4)
                .outputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                .duration(20)
                .EUt(491520)
                .save(provider);

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("samarium_chloride_concentrate_solution69"))
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
                .save(provider);

        RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("samarium_chloride_concentrate_solution70"))
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
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("make_p507_1"))
                .notConsumable(plate, Copper, 1)
                .inputFluids(SeedOil.getFluid(3000))
                .inputFluids(Hydrogen.getFluid(8000))
                .outputFluids(EthylHexanol.getFluid(1000))
                .duration(400)
                .EUt(480)
                .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("make_p507_2"))
                .inputItems(dust, Sodium, 2)
                .inputFluids(EthylHexanol.getFluid(2000))
                .inputFluids(PhosphoricAcid.getFluid(2000))
                .inputFluids(Ethanol.getFluid(2000))
                .outputFluids(P507.getFluid(1000))
                .duration(1200)
                .EUt(1920)
                .save(provider);

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

            LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("make_extraction_nano_resin_" + i))
                    .inputItems(nanites, Carbon, 1)
                    .notConsumable(lens, NetherStar, 1)
                    .inputFluids(rareEarthElement.getFluid(4000))
                    .inputFluids(P507.getFluid(4000))
                    .outputFluids(nanoResin.getFluid(1000))
                    .duration(1200)
                    .EUt(491520)
                    .addData("special", true)
                    .save(provider);

            LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("extraction_nano_resin_a_" + i))
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(ConcentratedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(EnrichedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(20)
                    .EUt(491520)
                    .save(provider);

            LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("extraction_nano_resin_b_" + i))
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(EnrichedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(80)
                    .EUt(491520)
                    .save(provider);

            LARGE_CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("extraction_nano_resin_c_" + i))
                    .inputFluids(nanoResin.getFluid(1000))
                    .inputFluids(DilutedRareEarthChlorideSolution.getFluid(1000))
                    .outputFluids(extractedNanoResin.getFluid(1000))
                    .duration(320)
                    .EUt(491520)
                    .save(provider);

            ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("break_down_extracted_nano_resin_" + i))
                    .inputFluids(extractedNanoResin.getFluid(1000))
                    .outputItems(dust, rareEarthElement, 1)
                    .outputFluids(nanoResin.getFluid(1000))
                    .outputFluids(Chlorine.getFluid(3000))
                    .duration(100)
                    .EUt(122880)
                    .save(provider);

            GTORecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("raw_adamantine"))
                    .inputItems(TagPrefix.dust, GTOMaterials.NaquadahContainRareEarth)
                    .chancedOutput(TagPrefix.rawOre, GTOMaterials.AdamantineCompounds, 5000, 1000)
                    .outputItems(TagPrefix.dust, GTMaterials.EnrichedNaquadahSulfate, 6)
                    .outputItems(TagPrefix.dust, GTMaterials.NaquadriaSulfate, 6)
                    .EUt(7864320)
                    .duration(200)
                    .save(provider);

            GTORecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES.recipeBuilder(GTOCore.id("rare_earth_centrifugal"))
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
                    .save(provider);
        }
    }
}
