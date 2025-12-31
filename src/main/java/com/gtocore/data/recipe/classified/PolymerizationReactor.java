package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.POLYMERIZATION_REACTOR_RECIPES;

final class PolymerizationReactor {

    public static void init() {
        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("epoxy_from_bisphenol_a")
                .inputItems(dust, SodiumHydroxide, 3)
                .inputFluids(Epichlorohydrin, 1000)
                .inputFluids(BisphenolA, 1000)
                .outputFluids(Epoxy.getFluid(1000))
                .outputFluids(SaltWater.getFluid(1000))
                .duration(200).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("make_p507_2")
                .inputItems(dust, Sodium, 2)
                .inputFluids(EthylHexanol, 2000)
                .inputFluids(PhosphoricAcid, 2000)
                .inputFluids(Ethanol, 2000)
                .outputFluids(P507.getFluid(1000))
                .duration(1200)
                .EUt(1920)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("pre_zylon_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Terephthalaldehyde, 16)
                .inputFluids(GTOMaterials.Dinitrodipropanyloxybenzene, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.PreZylon)
                .outputFluids(GTMaterials.Oxygen, 6000)
                .EUt(7864320)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyetheretherketone")
                .inputItems(TagPrefix.dust, GTOMaterials.Difluorobenzophenone, 24)
                .inputItems(TagPrefix.dust, GTMaterials.SodaAsh, 6)
                .inputFluids(GTOMaterials.Hydroquinone, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 4)
                .outputFluids(GTOMaterials.Polyetheretherketone, 2592)
                .outputFluids(GTMaterials.CarbonDioxide, 1000)
                .EUt(122880)
                .duration(250)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyurethaneresin")
                .inputItems(TagPrefix.dust, GTOMaterials.Pentaerythritol)
                .inputItems(TagPrefix.dust, GTOMaterials.DiphenylmethaneDiisocyanate, 5)
                .inputFluids(GTOMaterials.EthyleneGlycol, 4000)
                .inputFluids(GTOMaterials.SiliconOil, 1000)
                .outputFluids(GTOMaterials.PolyurethaneResin, 1000)
                .EUt(7680)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyurethane")
                .inputFluids(GTMaterials.Oxygen, 1000)
                .inputFluids(GTOMaterials.EthyleneGlycol, 4000)
                .inputFluids(GTOMaterials.TolueneDiisocyanate, 1000)
                .outputFluids(GTOMaterials.Polyurethane, 1000)
                .outputFluids(GTMaterials.Water, 7000)
                .EUt(480)
                .duration(110)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("paa")
                .inputItems(TagPrefix.dust, GTOMaterials.PyromelliticDianhydride, 18)
                .inputFluids(GTOMaterials.Oxydianiline, 1000)
                .outputFluids(GTOMaterials.Paa, 1000)
                .EUt(122880)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyphenylene_sulfide_from_oxygen")
                .inputItems(dust, SodiumSulfide, 3)
                .inputFluids(Dichlorobenzene, 1000)
                .inputFluids(Oxygen, 8000)
                .outputItems(dust, Salt, 4)
                .outputFluids(PolyphenyleneSulfide.getFluid(1500))
                .duration(240).EUt(360).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polybenzimidazole").EUt(VA[IV]).duration(100)
                .inputFluids(Diaminobenzidine, 1000)
                .inputFluids(DiphenylIsophtalate, 1000)
                .outputFluids(Phenol.getFluid(1000))
                .outputFluids(Polybenzimidazole.getFluid(1008))
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polycaprolactam").EUt(VA[MV]).duration(450)
                .inputItems(dust, Caprolactam, 1)
                .inputFluids(Nitrogen, 1000)
                .outputItems(ingot, Polycaprolactam, 1)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyvinyl_butyral")
                .inputFluids(Butyraldehyde, 250)
                .inputFluids(PolyvinylAcetate.getFluid(L))
                .outputFluids(PolyvinylButyral.getFluid(L))
                .duration(400).EUt(VA[HV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("raw_sbr_from_oxygen")
                .circuitMeta(3)
                .inputFluids(Butadiene, 3000)
                .inputFluids(Styrene, 1000)
                .inputFluids(Oxygen, 15000)
                .outputItems(dust, RawStyreneButadieneRubber, 41)
                .duration(480).EUt(240).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("pva_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(VinylAcetate.getFluid(L))
                .outputFluids(PolyvinylAcetate.getFluid(216))
                .duration(160).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("pva_from_tetrachloride_oxygen")
                .circuitMeta(2)
                .inputFluids(Oxygen, 7500)
                .inputFluids(VinylAcetate, 2160)
                .inputFluids(TitaniumTetrachloride, 100)
                .outputFluids(PolyvinylAcetate.getFluid(4320))
                .duration(800).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyethylene_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(Ethylene.getFluid(L))
                .outputFluids(Polyethylene.getFluid(216))
                .duration(160).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyethylene_from_tetrachloride_oxygen")
                .circuitMeta(2)
                .inputFluids(Oxygen, 7500)
                .inputFluids(Ethylene, 2160)
                .inputFluids(TitaniumTetrachloride, 100)
                .outputFluids(Polyethylene.getFluid(4320))
                .duration(800).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyvinyl_chloride_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(VinylChloride.getFluid(L))
                .outputFluids(PolyvinylChloride.getFluid(216))
                .duration(160).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polyvinyl_chloride_from_tetrachloride_oxygen")
                .circuitMeta(2)
                .inputFluids(Oxygen, 7500)
                .inputFluids(VinylChloride, 2160)
                .inputFluids(TitaniumTetrachloride, 100)
                .outputFluids(PolyvinylChloride.getFluid(4320))
                .duration(800).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("ptfe_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(Tetrafluoroethylene.getFluid(L))
                .outputFluids(Polytetrafluoroethylene.getFluid(216))
                .duration(160).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("ptfe_from_tetrachloride_oxygen")
                .circuitMeta(2)
                .inputFluids(Oxygen, 7500)
                .inputFluids(Tetrafluoroethylene, 2160)
                .inputFluids(TitaniumTetrachloride, 100)
                .outputFluids(Polytetrafluoroethylene.getFluid(4320))
                .duration(800).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("styrene_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(Styrene.getFluid(L))
                .outputFluids(Polystyrene.getFluid(216))
                .duration(160).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.recipeBuilder("polystyrene_from_tetrachloride_oxygen")
                .circuitMeta(2)
                .inputFluids(Oxygen, 7500)
                .inputFluids(Styrene, 2160)
                .inputFluids(TitaniumTetrachloride, 100)
                .outputFluids(Polystyrene.getFluid(4320))
                .duration(800).EUt(VA[LV]).save();

        POLYMERIZATION_REACTOR_RECIPES.builder("abs")
                .inputFluids(GTOMaterials.Acrylonitrile, 1000)
                .inputFluids(GTMaterials.Butadiene, 1000)
                .inputFluids(GTMaterials.Styrene, 2000)
                .outputFluids(GTOMaterials.Abs, 4000)
                .EUt(480)
                .duration(420)
                .save();
        POLYMERIZATION_REACTOR_RECIPES.builder("polyvinylpyrrolidone")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
                .inputFluids(GTOMaterials.GammaButyrolactone, 1000)
                .inputFluids(GTOMaterials.Acetaldehyde, 1000)
                .inputFluids(GTMaterials.Ammonia, 1000)
                .outputFluids(GTOMaterials.Polyvinylpyrrolidone, 3000)
                .EUt(19200)
                .duration(1220)
                .save();
        POLYMERIZATION_REACTOR_RECIPES.builder("polous_polyolefin_sulfonate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride)
                .outputItems(TagPrefix.dust, GTOMaterials.PolousPolyolefinSulfonate, 8)
                .inputFluids(GTOMaterials.SodiumAllylsulfonate, 10000)
                .inputFluids(GTMaterials.Acetone, 1000)
                .EUt(19200)
                .duration(1200)
                .save();
        POLYMERIZATION_REACTOR_RECIPES.builder("poly_dopamine")
                .inputItems(TagPrefix.dust, GTOMaterials.BenzoylPeroxide)
                .inputItems(TagPrefix.dust, GTOMaterials.BromoSuccinamide)
                .inputFluids(GTOMaterials.Dopamine, 8000)
                .outputFluids(GTOMaterials.PolyDopamine, 6000)
                .EUt(1200)
                .duration(1200)
                .save();
        POLYMERIZATION_REACTOR_RECIPES.builder("perfluorosulfonic_acid_polytetrafluoroethylene_copolymer_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.BenzoylPeroxide)
                .inputItems(TagPrefix.dust, GTOMaterials.BromoSuccinamide)
                .outputItems(TagPrefix.dust, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer, 10)
                .inputFluids(GTMaterials.Tetrafluoroethylene, 6000)
                .inputFluids(GTOMaterials.PerfluorooctanesulfonicAcid, 4000)
                .EUt(72000)
                .duration(1200)
                .save();

        POLYMERIZATION_REACTOR_RECIPES.builder("dye_carrier_resin")
                .inputFluids(GTOMaterials.EmulsifiedDyeCarrierResin, 5000)
                .inputFluids(GTMaterials.Helium, 1000)
                .outputFluids(GTOMaterials.DyeCarrierResin, 5000)
                .EUt(510)
                .duration(150)
                .save();
    }
}
