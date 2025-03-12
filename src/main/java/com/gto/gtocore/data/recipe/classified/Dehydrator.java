package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.DEHYDRATOR_RECIPES;

interface Dehydrator {

    static void init() {
        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("sodiumhydroxidesolution"))
                .outputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .inputFluids(GTOMaterials.SodiumHydroxideSolution.getFluid(1000))
                .EUt(30)
                .duration(140)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("er_lu_oxides_solution"))
                .inputFluids(GTOMaterials.ErLuOxidesSolution.getFluid(4000))
                .chancedOutput(TagPrefix.dust, GTOMaterials.ErbiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.ThuliumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.YtterbiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.LutetiumOxide, 5, 4300, 275)
                .EUt(480)
                .duration(220)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("tb_ho_oxides_solution"))
                .inputFluids(GTOMaterials.TbHoOxidesSolution.getFluid(4000))
                .chancedOutput(TagPrefix.dust, GTOMaterials.YttriumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.TerbiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.DysprosiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.HolmiumOxide, 5, 4300, 275)
                .EUt(480)
                .duration(220)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("silica_gel_dust"))
                .inputFluids(GTOMaterials.SilicaGelBase.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.SilicaGel, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .EUt(480)
                .duration(130)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("boron_trioxide_dust"))
                .inputFluids(GTOMaterials.BoricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.BoronTrioxide, 5)
                .EUt(480)
                .duration(400)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("residual_triniite_solution"))
                .inputFluids(GTOMaterials.ResidualTriniiteSolution.getFluid(2000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumNitrate, 20)
                .chancedOutput(TagPrefix.dust, GTMaterials.Naquadria, 2, 4800, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.NaquadahEnriched, 5200, 0)
                .EUt(7680)
                .duration(190)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("monomethylhydrazine"))
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputFluids(GTOMaterials.Hydrazine.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(2000))
                .outputFluids(GTOMaterials.Monomethylhydrazine.getFluid(1000))
                .EUt(240)
                .duration(240)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("la_nd_oxides_solution"))
                .inputFluids(GTOMaterials.LaNdOxidesSolution.getFluid(4000))
                .chancedOutput(TagPrefix.dust, GTOMaterials.LanthanumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.CeriumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.PraseodymiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.NeodymiumOxide, 5, 4300, 275)
                .EUt(480)
                .duration(220)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("zeolite_sieving_pellets_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.WetZeoliteSievingPellets)
                .outputItems(TagPrefix.dust, GTOMaterials.ZeoliteSievingPellets)
                .EUt(120)
                .duration(50)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("polyimide"))
                .inputFluids(GTOMaterials.Paa.getFluid(144))
                .outputFluids(GTOMaterials.Polyimide.getFluid(144))
                .EUt(30)
                .duration(270)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("germanium_dioxide_dust"))
                .inputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(4000))
                .outputItems(TagPrefix.dust, GTOMaterials.GermaniumDioxide, 3)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(4000))
                .EUt(30)
                .duration(800)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("tungsten_trioxide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.TungsticAcid, 7)
                .outputItems(TagPrefix.dust, GTOMaterials.TungstenTrioxide, 4)
                .EUt(120)
                .duration(150)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("graphene_iron_plate"))
                .notConsumable(TagPrefix.rodLong, GTMaterials.YttriumBariumCuprate)
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 3)
                .inputFluids(GTOMaterials.GlucoseIronSolution.getFluid(1000))
                .outputItems(GTOItems.GRAPHENE_IRON_PLATE.asItem())
                .outputItems(TagPrefix.gem, GTMaterials.Salt, 6)
                .EUt(120)
                .duration(40)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("stearic_acid"))
                .inputFluids(GTOMaterials.DeglyceratedSoap.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt)
                .outputFluids(GTOMaterials.StearicAcid.getFluid(800))
                .EUt(2000)
                .duration(160)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("sm_gd_oxides_solution"))
                .inputFluids(GTOMaterials.SmGdOxidesSolution.getFluid(4000))
                .chancedOutput(TagPrefix.dust, GTOMaterials.ScandiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.SamariumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.EuropiumOxide, 5, 4300, 275)
                .chancedOutput(TagPrefix.dust, GTOMaterials.GadoliniumOxide, 5, 4300, 275)
                .EUt(480)
                .duration(220)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("salt_dust"))
                .inputFluids(GTMaterials.SaltWater.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
                .EUt(30)
                .duration(160)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("phthalic_anhydride_dust"))
                .inputFluids(GTMaterials.PhthalicAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.PhthalicAnhydride, 15)
                .EUt(480)
                .duration(400)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("diethyl_ether"))
                .circuitMeta(1)
                .notConsumableFluid(GTMaterials.SulfuricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Ethanol.getFluid(2000))
                .outputFluids(GTOMaterials.DiethylEther.getFluid(1000))
                .EUt(750)
                .duration(120)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("hydroxyapatite_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.UndriedHydroxyapatite)
                .outputItems(TagPrefix.dust, GTOMaterials.HydroxyapatiteCeramic)
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(120)
                .duration(10)
                .save();
    }
}
