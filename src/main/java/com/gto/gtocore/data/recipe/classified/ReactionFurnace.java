package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

interface ReactionFurnace {

    static void init(Consumer<FinishedRecipe> provider) {
        registerBlastFurnaceRecipes(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("actinium_hydride_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumOxalate, 13)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 6)
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTOMaterials.CarbonTetrachloride.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.ActiniumHydride, 4)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 8)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(5000))
                .EUt(122880)
                .duration(400)
                .blastFurnaceTemp(10700)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("europium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.EuropiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Europium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("strontium_europium_aluminate_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Europium)
                .inputItems(TagPrefix.dust, GTMaterials.Strontium)
                .inputFluids(GTMaterials.Oxygen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.StrontiumEuropiumAluminate, 8)
                .EUt(120)
                .duration(340)
                .blastFurnaceTemp(1200)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("hafnium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.HafniumChloride, 5)
                .inputItems(TagPrefix.dust, GTMaterials.Magnesium, 2)
                .outputItems(TagPrefix.dust, GTMaterials.Hafnium)
                .outputItems(TagPrefix.dust, GTMaterials.MagnesiumChloride, 6)
                .EUt(120)
                .duration(300)
                .blastFurnaceTemp(3400)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("boron_carbide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .inputItems(TagPrefix.dust, GTMaterials.Boron, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.BoronCarbide, 7)
                .EUt(120)
                .duration(550)
                .blastFurnaceTemp(4000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("rhenium_chloride_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Chlorine.getFluid(5000))
                .outputItems(TagPrefix.dust, GTOMaterials.RheniumChloride, 6)
                .EUt(120)
                .duration(930)
                .blastFurnaceTemp(12500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("yttrium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.YttriumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Yttrium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("gadolinium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GadoliniumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Gadolinium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("hassium_chloride_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Hassium)
                .inputFluids(GTMaterials.Chlorine.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.HassiumChloride, 5)
                .EUt(120)
                .duration(930)
                .blastFurnaceTemp(12000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cubic_zirconia_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Zirconium)
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.CubicZirconia, 3)
                .EUt(480)
                .duration(360)
                .blastFurnaceTemp(2600)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("borocarbide_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.BoronFranciumCarbide, 15)
                .inputItems(TagPrefix.dust, GTOMaterials.MixedAstatideSalts, 14)
                .outputItems(TagPrefix.dust, GTOMaterials.Borocarbide, 29)
                .EUt(120)
                .duration(15000)
                .blastFurnaceTemp(11300)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cadmium_sulfide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Cadmium)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputItems(TagPrefix.dust, GTOMaterials.CadmiumSulfide, 2)
                .EUt(30)
                .duration(400)
                .blastFurnaceTemp(1200)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lanthanum_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.LanthanumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Lanthanum, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("sodium_thiosulfate_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 18)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumThiosulfate, 7)
                .outputItems(TagPrefix.dust, GTMaterials.SodiumSulfide, 6)
                .outputFluids(GTMaterials.Steam.getFluid(3000))
                .EUt(120)
                .duration(210)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("iridium_dioxide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.IridiumMetalResidue, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumChlorate, 5)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.IridiumDioxide, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .chancedOutput(TagPrefix.dust, GTMaterials.PlatinumSludgeResidue, 8000, 0)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid.getFluid(1000))
                .EUt(120)
                .duration(200)
                .blastFurnaceTemp(790)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("dysprosium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.DysprosiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Dysprosium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("erbium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ErbiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Erbium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("dust_cryotheum"))
                .inputItems(GTOItems.DUST_BLIZZ.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.Enderium)
                .outputItems(GTOItems.DUST_CRYOTHEUM.asStack(2))
                .EUt(7864320)
                .duration(160)
                .blastFurnaceTemp(8300)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("silver_iodide_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.SilverIodide, 4)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilverOxide, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Iodine, 2)
                .EUt(125)
                .duration(210)
                .blastFurnaceTemp(1100)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("germanium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumDioxide, 3)
                .inputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Germanium)
                .EUt(120)
                .duration(200)
                .blastFurnaceTemp(680)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("calcium_carbide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Quicklime, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.CalciumCarbide)
                .outputFluids(GTMaterials.Oxygen.getFluid(1000))
                .EUt(120)
                .duration(100)
                .blastFurnaceTemp(800)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("phosphorus_pentasulfide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Phosphorus, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.PhosphorusPentasulfide, 14)
                .EUt(480)
                .duration(190)
                .blastFurnaceTemp(900)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("thallium_thulium_doped_caesium_iodide_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CaesiumIodide)
                .inputItems(TagPrefix.dustSmall, GTMaterials.Thulium)
                .inputItems(TagPrefix.dustSmall, GTMaterials.Thallium)
                .outputItems(TagPrefix.dust, GTOMaterials.ThalliumThuliumDopedCaesiumIodide)
                .EUt(120)
                .duration(260)
                .blastFurnaceTemp(2853)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("blaze_casing"))
                .inputItems(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.asStack())
                .inputItems(TagPrefix.foil, GTMaterials.Tin, 32)
                .inputFluids(GTMaterials.Blaze.getFluid(1440))
                .inputFluids(GTMaterials.GalliumArsenide.getFluid(576))
                .inputFluids(GTMaterials.VanadiumGallium.getFluid(288))
                .outputItems(GTOBlocks.BLAZE_CASING.asStack())
                .EUt(1920)
                .duration(900)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("ytterbium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.YtterbiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Ytterbium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("aerographene"))
                .inputItems(TagPrefix.dust, GTOMaterials.DryGrapheneGel)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide.getFluid(1000))
                .outputItems(GTOItems.AEROGRAPHENE.asStack())
                .EUt(120)
                .duration(400)
                .blastFurnaceTemp(5000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("zylon_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PreZylon)
                .outputItems(TagPrefix.dust, GTOMaterials.Zylon)
                .outputFluids(GTMaterials.Propane.getFluid(2000))
                .EUt(120)
                .duration(16000)
                .blastFurnaceTemp(10000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("promethium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PromethiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Promethium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("molybdenum_flue"))
                .inputItems(TagPrefix.dust, GTOMaterials.MolybdenumConcentrate, 4)
                .inputFluids(GTMaterials.Oxygen.getFluid(7250))
                .outputItems(TagPrefix.dust, GTOMaterials.MolybdenumTrioxide, 4)
                .outputFluids(GTOMaterials.MolybdenumFlue.getFluid(3000))
                .EUt(120)
                .duration(340)
                .blastFurnaceTemp(2400)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lutetium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.LutetiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Lutetium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cadmium_tungstate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.TungstenTrioxide, 4)
                .inputItems(TagPrefix.dust, GTOMaterials.CadmiumSulfide, 2)
                .inputFluids(GTMaterials.Oxygen.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.CadmiumTungstate, 6)
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000))
                .EUt(120)
                .duration(320)
                .blastFurnaceTemp(2800)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("bismuth_germanate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumDioxide, 3)
                .inputFluids(GTOMaterials.BismuthNitrateSolution.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.BismuthGermanate, 33)
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(12000))
                .EUt(5000000)
                .duration(80)
                .blastFurnaceTemp(7600)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("scandium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ScandiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Scandium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("terbium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.TerbiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Terbium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("bisethylenedithiotetraselenafulvalene_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumThiinediselenide, 28)
                .inputItems(TagPrefix.dust, GTOMaterials.CyclopentadienylTitaniumTrichloride)
                .inputFluids(GTMaterials.Tetrafluoroethylene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumFluoride, 8)
                .outputItems(TagPrefix.dust, GTOMaterials.Bisethylenedithiotetraselenafulvalene, 26)
                .EUt(120)
                .duration(7680)
                .blastFurnaceTemp(4600)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("holmium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.HolmiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Holmium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("trinium_compound_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CrystallineNitricAcid, 40)
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumCompound, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumPerchlorate, 6)
                .inputFluids(GTMaterials.SulfurDioxide.getFluid(8000))
                .outputItems(TagPrefix.dust, GTMaterials.Astatine, 8)
                .outputItems(TagPrefix.dust, GTOMaterials.SeleniumOxide, 24)
                .outputFluids(GTOMaterials.NitratedTriniiteCompoundSolution.getFluid(4000))
                .EUt(122880)
                .duration(265)
                .blastFurnaceTemp(9400)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("dragon_strength_tritanium_casing"))
                .inputItems(GTOBlocks.EXTREME_STRENGTH_TRITANIUM_CASING.asStack(16))
                .inputItems(GTOBlocks.DRACONIUM_BLOCK_CHARGED.asStack(16))
                .outputItems(GTOBlocks.DRAGON_STRENGTH_TRITANIUM_CASING.asStack())
                .EUt(7864320)
                .duration(2000)
                .blastFurnaceTemp(8000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("bisethylenedithiotetraselenafulvalene_perrhenate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.Bisethylenedithiotetraselenafulvalene, 26)
                .inputFluids(GTOMaterials.AmmoniumPerrhenate.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.BisethylenedithiotetraselenafulvalenePerrhenate, 31)
                .outputFluids(GTMaterials.Ammonia.getFluid(1000))
                .EUt(120)
                .duration(9840)
                .blastFurnaceTemp(5000)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("cerium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CeriumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Cerium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("thulium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ThuliumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Thulium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("zeolite_sieving_pellets_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.SilicaAluminaGel)
                .inputItems(TagPrefix.dust, GTMaterials.Zeolite)
                .outputItems(TagPrefix.dust, GTOMaterials.ZeoliteSievingPellets)
                .EUt(120)
                .duration(400)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("francium_caesium_cadmium_bromide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Cadmium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Francium)
                .inputItems(TagPrefix.dust, GTMaterials.Caesium)
                .inputFluids(GTMaterials.Bromine.getFluid(6000))
                .outputItems(TagPrefix.dust, GTOMaterials.FranciumCaesiumCadmiumBromide, 10)
                .EUt(120)
                .duration(250)
                .blastFurnaceTemp(2200)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("samarium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.SamariumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Samarium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("tellurium_oxide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Tellurium)
                .inputFluids(GTMaterials.Oxygen.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.TelluriumOxide, 3)
                .EUt(128)
                .duration(100)
                .blastFurnaceTemp(1760)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("hot_titanium_50_ingot"))
                .inputItems(TagPrefix.dust, GTMaterials.Magnesium, 2)
                .inputFluids(GTOMaterials.Titanium50Tetrachloride.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Titanium50)
                .outputItems(TagPrefix.dust, GTMaterials.MagnesiumChloride, 6)
                .EUt(480)
                .duration(600)
                .blastFurnaceTemp(2142)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("fissioned_uranium_235_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Uranium235)
                .inputItems(TagPrefix.dustTiny, GTMaterials.Neutronium)
                .outputItems(TagPrefix.dust, GTOMaterials.FissionedUranium235)
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(3860)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("neodymium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.NeodymiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Neodymium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("praseodymium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PraseodymiumOxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Praseodymium, 4)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000))
                .EUt(480)
                .duration(200)
                .blastFurnaceTemp(2500)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("infused_obsidian"))
                .inputItems(GTOBlocks.ENDER_OBSIDIAN.asStack())
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack())
                .outputItems(GTOBlocks.INFUSED_OBSIDIAN.asStack())
                .EUt(7864320)
                .duration(200)
                .blastFurnaceTemp(11200)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("chalcopyrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Chalcopyrite)
                .inputItems(dust, SiliconDioxide)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dust, Ferrosilite)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("alumina_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.AluminiumHydroxide)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminaCeramic)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(100)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("tungsten_tetraboride_ceramics_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.TungstenBoronMixture)
                .outputItems(TagPrefix.dust, GTOMaterials.TungstenTetraborideCeramics)
                .EUt(480)
                .duration(100)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("tellurate_ceramics_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Tellurium)
                .inputFluids(GTMaterials.Oxygen.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.TellurateCeramics)
                .EUt(480)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("thulium_hexaboride_ceramics_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Thulium)
                .inputItems(TagPrefix.dust, GTMaterials.Boron, 6)
                .inputFluids(GTMaterials.Argon.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.ThuliumHexaborideCeramics)
                .EUt(960)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("silicon_nitride_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Silicon, 3)
                .circuitMeta(5)
                .inputFluids(GTMaterials.Nitrogen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.SiliconNitrideCeramic)
                .EUt(240)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lithium_oxide_ceramics_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Lithium, 2)
                .circuitMeta(5)
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumOxideCeramics, 2)
                .EUt(240)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("titanium_nitride_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Titanium, 2)
                .inputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.TitaniumNitrideCeramic, 2)
                .EUt(480)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("zirconia_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.Zircon, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.ZirconiaCeramic)
                .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 3)
                .EUt(960)
                .duration(5)
                .blastFurnaceTemp(2775)
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("carbon_dioxide"))
                .inputItems(TagPrefix.dust, GTMaterials.Magnesite, 5)
                .outputItems(TagPrefix.dust, GTMaterials.Magnesia, 2)
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .EUt(30)
                .duration(240)
                .blastFurnaceTemp(1255)
                .save(provider);
    }

    private static void registerBlastFurnaceRecipes(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_ruby_dust").duration(400).EUt(100).inputItems(dust, Ruby)
                .outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0).blastFurnaceTemp(1200)
                .save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_ruby_gem").duration(320).EUt(100).inputItems(gem, Ruby)
                .outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0).blastFurnaceTemp(1200)
                .save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_green_sapphire_dust").duration(400).EUt(100)
                .inputItems(dust, GreenSapphire).outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0)
                .blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_green_sapphire_gem").duration(320).EUt(100)
                .inputItems(gem, GreenSapphire).outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0)
                .blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_sapphire_dust").duration(400).EUt(100).inputItems(dust, Sapphire)
                .outputItems(nugget, Aluminium, 3).blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_sapphire_gem").duration(320).EUt(100).inputItems(gem, Sapphire)
                .outputItems(nugget, Aluminium, 3).blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("titanium_from_tetrachloride").duration(600).EUt(VA[HV])
                .inputItems(dust, Magnesium, 2).inputFluids(TitaniumTetrachloride.getFluid(1000))
                .outputItems(ingotHot, Titanium).outputItems(dust, MagnesiumChloride, 6)
                .blastFurnaceTemp(Titanium.getBlastTemperature() + 200).save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("rutile_from_ilmenite")
                .inputItems(dust, Ilmenite, 10)
                .inputItems(dust, Carbon, 4)
                .outputItems(ingot, WroughtIron, 2)
                .outputItems(dust, Rutile, 4)
                .outputFluids(CarbonDioxide.getFluid(2000))
                .blastFurnaceTemp(1700)
                .duration(1600).EUt(VA[HV]).save(provider);

        registerBlastFurnaceMetallurgyRecipes(provider);
    }

    private static void registerBlastFurnaceMetallurgyRecipes(Consumer<FinishedRecipe> provider) {
        createSulfurDioxideRecipe(provider, Stibnite, AntimonyTrioxide, 1500);
        createSulfurDioxideRecipe(provider, Sphalerite, Zincite, 1000);
        createSulfurDioxideRecipe(provider, Pyrite, Hematite, 2000);
        createSulfurDioxideRecipe(provider, Pentlandite, Garnierite, 1000);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("tetrahedrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Tetrahedrite)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dustTiny, AntimonyTrioxide, 3)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("cobaltite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Cobaltite)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CobaltOxide)
                .outputItems(dust, ArsenicTrioxide)
                .outputFluids(SulfurDioxide.getFluid(1000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("galena_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Galena)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, Massicot)
                .outputItems(nugget, Silver, 6)
                .outputFluids(SulfurDioxide.getFluid(1000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("chalcopyrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Chalcopyrite)
                .inputItems(dust, SiliconDioxide)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dust, Ferrosilite)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("blast_silicon_dioxide").duration(240).EUt(VA[MV]).blastFurnaceTemp(2273)
                .inputItems(dust, SiliconDioxide, 3)
                .inputItems(dust, Carbon, 2)
                .outputItems(ingotHot, Silicon)
                .chancedOutput(dust, Ash, "1/9", 0)
                .outputFluids(CarbonMonoxide.getFluid(2000))
                .save(provider);
    }

    private static void createSulfurDioxideRecipe(Consumer<FinishedRecipe> provider, Material inputMaterial,
                                                  Material outputMaterial, int sulfurDioxideAmount) {
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(inputMaterial.getName() + "_metallurgy").duration(120).EUt(VA[MV])
                .blastFurnaceTemp(1200)
                .inputItems(dust, inputMaterial)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, outputMaterial)
                .chancedOutput(dust, Ash, "1/9", 0)
                .outputFluids(SulfurDioxide.getFluid(sulfurDioxideAmount))
                .save(provider);
    }
}
