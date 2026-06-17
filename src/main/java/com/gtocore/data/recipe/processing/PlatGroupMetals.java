package com.gtocore.data.recipe.processing;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class PlatGroupMetals {

    public static void init() {
        CHEMICAL_RECIPES.recipeBuilder("pgs_from_chalcocite").duration(110).EUt(VA[LV])
                .inputItems(crushedPurified, Chalcocite)
                .inputFluids(NitricAcid, 1000)
                .outputItems(dust, PlatinumGroupSludge, 4)
                .outputFluids(SulfuricCopperSolution.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("pgs_from_bornite").duration(110).EUt(VA[LV])
                .inputItems(crushedPurified, Bornite)
                .inputFluids(NitricAcid, 1000)
                .outputItems(dust, PlatinumGroupSludge, 4)
                .outputFluids(SulfuricCopperSolution.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("pgs_from_tetrahedrite").duration(110).EUt(VA[LV])
                .inputItems(crushedPurified, Tetrahedrite)
                .inputFluids(NitricAcid, 1000)
                .outputItems(dust, PlatinumGroupSludge, 4)
                .outputFluids(SulfuricCopperSolution.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("pgs_from_cooperite").duration(150).EUt(VA[LV])
                .inputItems(crushedPurified, Cooperite)
                .inputFluids(NitricAcid, 1000)
                .outputItems(dust, PlatinumGroupSludge, 8)
                .outputFluids(SulfuricNickelSolution.getFluid(1000))
                .save();

        // Aqua Regia
        // HNO3 + HCl -> [HNO3 + HCl]
        MIXER_RECIPES.recipeBuilder("aqua_regia").duration(130).EUt(VA[LV])
                .inputFluids(NitricAcid, 1000)
                .inputFluids(HydrochloricAcid, 2000)
                .outputFluids(AquaRegia.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("pgs_separation").duration(150).EUt(VA[HV])
                .inputItems(dust, PlatinumGroupSludge, 6)
                .inputFluids(AquaRegia, 1200)
                .chancedOutput(dust, PlatinumSlag, 6, 7500, 500)
                .chancedOutput(dust, PalladiumRaw, 3, 8000, 500)
                .chancedOutput(dust, PlatinumRaw, 2, 8000, 500)
                .chancedOutput(dust, PlatinumSludgeResidue, 2, 8500, 500)
                .chancedOutput(dustTiny, InertMetalMixture, 4, 9000, 500)
                .chancedOutput(dustTiny, RarestMetalMixture, 1, 9000, 100)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("leach_residue")
                .inputItems(dust, PlatinumSlag, 6)
                .inputItems(dust, PotassiumPyrosulfate, 11)
                .inputFluids(Oxygen, 1000)
                .outputFluids(RhodiumSulfateGas.getFluid(500))
                .outputItems(dust, LeachResidue, 5)
                .outputItems(dust, PotassiumSulfate, 7)
                .blastFurnaceTemp(775)
                .duration(120).EUt(VA[MV]).save();

        REACTION_FURNACE_RECIPES.recipeBuilder("rarest_metal_mixture")
                .inputItems(dust, LeachResidue, 40)
                .inputItems(dust, SodaAsh, 18)
                .inputFluids(Oxygen, 3000)
                .outputFluids(CarbonMonoxide.getFluid(3000))
                .outputItems(dust, RarestMetalMixture, 20)
                .outputItems(dust, SodiumRutheniate, 21)
                .blastFurnaceTemp(775)
                .duration(220).EUt(VA[MV]).save();

        // Formic Acid
        CHEMICAL_RECIPES.recipeBuilder("sodium_formate")
                .inputItems(dust, SodiumHydroxide, 3)
                .inputFluids(CarbonMonoxide, 1000)
                .outputFluids(SodiumFormate.getFluid(1000))
                .duration(115).EUt(VA[LV]).save();

        // PLATINUM
        CHEMICAL_RECIPES.recipeBuilder("raw_platinum_separation")
                .inputItems(dust, PlatinumRaw, 3)
                .inputItems(dust, Calcium, 1)
                .outputItems(dust, Platinum, 1)
                .outputItems(dust, CalciumChloride, 3)
                .duration(130).EUt(VA[LV]).save();

        // PALLADIUM
        LARGE_CHEMICAL_RECIPES.recipeBuilder("raw_palladium_separation")
                .inputItems(dust, PalladiumRaw, 5)
                .inputFluids(FormicAcid, 1000)
                .outputItems(dust, Palladium, 1)
                .outputFluids(Ammonia.getFluid(1000))
                .outputFluids(CarbonMonoxide.getFluid(1000))
                .outputFluids(Water.getFluid(1000))
                .duration(140).EUt(VA[LV]).save();

        // RHODIUM / RUTHENIUM
        REACTION_FURNACE_RECIPES.recipeBuilder("leach_residue_one")
                .inputItems(dust, InertMetalMixture, 6)
                .inputItems(dust, SodiumNitrate, 10)
                .inputFluids(SulfuricAcid, 1000)
                .outputFluids(RhodiumSulfateGas.getFluid(600))
                .outputItems(dust, SodiumRutheniate, 6)
                .blastFurnaceTemp(775)
                .duration(70).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("ruthenium_tetroxide")
                .inputItems(dust, SodiumRutheniate, 7)
                .inputFluids(Chlorine, 3000)
                .outputFluids(RutheniumTetroxide.getFluid(3000))
                .outputItems(dust, Salt, 12)
                .duration(110).EUt(VA[LV]).save();

        CRACKING_RECIPES.recipeBuilder("hot_ruthenium_tetroxide")
                .inputFluids(RutheniumTetroxide, 1000)
                .inputFluids(Steam, 1000)
                .circuitMeta(1)
                .outputFluids(RutheniumTetroxideHot.getFluid(1000))
                .duration(50).EUt(VA[HV]).save();

        DISTILLATION_RECIPES.recipeBuilder("hot_ruthenium_tetroxide_distill")
                .inputFluids(RutheniumTetroxideHot, 3000)
                .outputItems(dust, Salt, 2)
                .outputFluids(Water.getFluid(1000))
                .outputFluids(RutheniumTetroxideLQ.getFluid(900))
                .duration(80).EUt(VA[HV]).save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder("ruthenium_tetroxide_dust")
                .inputFluids(RutheniumTetroxideLQ, 1000)
                .outputItems(dust, RutheniumTetroxide, 5)
                .duration(120).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("ruthenium_tetroxide_separation")
                .inputItems(dust, RutheniumTetroxide, 5)
                .inputFluids(HydrochloricAcid, 6000)
                .outputItems(dust, Ruthenium, 1)
                .outputFluids(Water.getFluid(2000))
                .outputFluids(Chlorine.getFluid(6000))
                .duration(100).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("rhodium_sulfate_to_solution")
                .inputFluids(Water, 1000)
                .inputFluids(RhodiumSulfateGas, 1000)
                .outputFluids(RhodiumSulfate.getFluid(1000))
                .duration(120).EUt(VA[LV]).save();

        DEHYDRATOR_RECIPES.recipeBuilder("crude_rhodium_metallic_powder")
                .inputItems(dust, Zinc, 1)
                .inputFluids(RhodiumSulfate, 1000)
                .outputItems(dust, ZincSulfate, 6)
                .outputItems(dust, RoughlyRhodiumMetal, 1)
                .duration(140).EUt(VA[LV]).save();

        REACTION_FURNACE_RECIPES.recipeBuilder("rhodium_salt_ebf")
                .inputItems(dust, RoughlyRhodiumMetal, 1)
                .inputItems(dust, Salt, 2)
                .inputFluids(Chlorine, 1000)
                .outputItems(dust, RhodiumSalt, 3)
                .blastFurnaceTemp(600)
                .duration(90).EUt(VA[MV]).save();

        MIXER_RECIPES.recipeBuilder("rhodium_salt_solution")
                .inputItems(dust, RhodiumSalt, 4)
                .inputFluids(Water, 1000)
                .outputFluids(RhodiumSaltSolution.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("sodium_nitrate")
                .inputItems(dust, Sodium)
                .inputFluids(NitricAcid, 1000)
                .outputFluids(Hydrogen.getFluid(1000))
                .outputItems(dust, SodiumNitrate, 5)
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("rhodium_nitrate")
                .inputItems(dust, SodiumNitrate, 5)
                .inputFluids(Oxygen, 2000)
                .inputFluids(NitrogenDioxide, 2000)
                .inputFluids(RhodiumSaltSolution, 1000)
                .outputItems(dust, RhodiumNitrate, 13)
                .outputItems(dust, Salt, 4)
                .duration(160).EUt(VA[LV]).save();

        SIFTER_RECIPES.recipeBuilder("rhodium_filter_cake")
                .inputItems(dust, RhodiumNitrate, 13)
                .chancedOutput(dust, RhodiumFilterCake, 8000, 200)
                .chancedOutput(dust, RhodiumFilterCake, 8000, 200)
                .chancedOutput(dust, RhodiumFilterCake, 6000, 100)
                .chancedOutput(dust, RhodiumFilterCake, 6000, 100)
                .chancedOutput(dust, RhodiumFilterCake, 4000, 50)
                .chancedOutput(dust, RhodiumFilterCake, 4000, 50)
                .duration(150).EUt(VA[LV]).save();

        MIXER_RECIPES.recipeBuilder("rhodium_filter_cake_solution")
                .inputItems(dust, RhodiumFilterCake, 6)
                .inputFluids(Water, 1000)
                .outputFluids(RhodiumFilterCakeSolution.getFluid(1000))
                .duration(140).EUt(VA[LV]).save();

        DISTILLERY_RECIPES.recipeBuilder("reprecipitated_rhodium")
                .inputFluids(RhodiumFilterCakeSolution, 1000)
                .circuitMeta(1)
                .outputFluids(Water.getFluid(500))
                .outputItems(dust, ReprecipitatedRhodium, 6)
                .duration(150).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("rhodium_dust")
                .inputItems(dust, ReprecipitatedRhodium, 7)
                .inputFluids(HydrochloricAcid, 1000)
                .outputItems(dust, Rhodium, 1)
                .outputItems(dust, AmmoniumChloride, 6)
                .outputFluids(Hydrogen.getFluid(1000))
                .duration(170).EUt(VA[LV]).save();

        // OSMIUM / IRIDIUM
        REACTION_FURNACE_RECIPES.recipeBuilder("rarest_metal_residue")
                .inputItems(dust, RarestMetalMixture, 7)
                .inputFluids(HydrochloricAcid, 4000)
                .outputItems(dust, IridiumMetalResidue, 5)
                .outputFluids(AcidicOsmiumSolution.getFluid(1600))
                .blastFurnaceTemp(775)
                .duration(90).EUt(VA[MV]).save();

        DISTILLATION_RECIPES.recipeBuilder("acidic_osmium_solution_separation")
                .duration(180).EUt(VA[LV])
                .inputFluids(AcidicOsmiumSolution, 2000)
                .outputItems(dust, OsmiumOxideMetal, 5)
                .outputFluids(Water.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("osmium_tetroxide")
                .inputItems(TagPrefix.dust, GTOMaterials.OsmiumOxideMetal, 7)
                .outputItems(TagPrefix.dust, GTMaterials.OsmiumTetroxide, 4)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .outputFluids(GTMaterials.DilutedSulfuricAcid, 2000)
                .EUt(30)
                .duration(140)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("iridium_dioxide_dissolving")
                .inputItems(dust, IridiumDioxide, 3)
                .inputFluids(HydrochloricAcid, 1000)
                .outputFluids(AcidicIridium.getFluid(1000))
                .duration(190).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("iridium_chloride")
                .inputItems(dust, AmmoniumChloride, 18)
                .inputFluids(AcidicIridium, 1000)
                .outputItems(dust, IridiumChloride, 4)
                .outputFluids(Ammonia.getFluid(3000))
                .duration(140).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("osmium_tetroxide_separation")
                .duration(110).EUt(VA[MV])
                .inputItems(dust, OsmiumTetroxide, 5)
                .inputFluids(Hydrogen, 4000)
                .outputItems(dust, Osmium)
                .outputFluids(Water.getFluid(4000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("iridium_chloride_separation")
                .duration(100).EUt(VA[MV])
                .inputItems(dust, IridiumChloride, 4)
                .inputFluids(Hydrogen, 3000)
                .outputItems(dust, Iridium)
                .outputFluids(HydrochloricAcid.getFluid(3000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("platinum_raw")
                .inputItems(TagPrefix.dust, GTOMaterials.PlatinumMetal)
                .outputItems(TagPrefix.dust, GTMaterials.PlatinumRaw, 3)
                .inputFluids(GTMaterials.HydrochloricAcid, 2000)
                .inputFluids(GTMaterials.HydrogenPeroxide, 1000)
                .outputFluids(GTMaterials.Water, 2000)
                .EUt(120)
                .duration(80)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("palladium_raw")
                .inputItems(TagPrefix.dust, GTOMaterials.PalladiumMetal)
                .inputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 6)
                .outputItems(TagPrefix.dust, GTMaterials.PalladiumRaw, 5)
                .inputFluids(GTMaterials.Water, 1000)
                .outputFluids(GTMaterials.DilutedHydrochloricAcid, 2000)
                .EUt(120)
                .duration(120)
                .save();
    }
}
