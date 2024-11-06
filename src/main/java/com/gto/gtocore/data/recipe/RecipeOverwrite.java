package com.gto.gtocore.data.recipe;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class RecipeOverwrite {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 修改
        ASSEMBLER_RECIPES.recipeBuilder("bucket")
                .inputItems(GTOTagPrefix.curvedPlate, Iron, 2)
                .inputItems(plate, Iron)
                .outputItems(new ItemStack(Items.BUCKET))
                .duration(100).EUt(4)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_plain")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .circuitMeta(1)
                .outputItems(VACUUM_TUBE, 2)
                .addCondition(new VacuumCondition(1))
                .duration(120).EUt(VA[ULV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_red_alloy")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 4)
                .addCondition(new VacuumCondition(2))
                .duration(40).EUt(16).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_red_alloy_annealed")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, AnnealedCopper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 6)
                .addCondition(new VacuumCondition(3))
                .duration(40).EUt(VA[LV]).save(provider);

        BLAST_RECIPES.recipeBuilder("engraved_crystal_chip_from_emerald")
                .inputItems(plate, Emerald)
                .inputItems(RAW_CRYSTAL_CHIP)
                .inputFluids(Helium.getFluid(1000))
                .outputItems(ENGRAVED_CRYSTAL_CHIP)
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save(provider);

        BLAST_RECIPES.recipeBuilder("engraved_crystal_chip_from_olivine")
                .inputItems(plate, Olivine)
                .inputItems(RAW_CRYSTAL_CHIP)
                .inputFluids(Helium.getFluid(1000))
                .outputItems(ENGRAVED_CRYSTAL_CHIP)
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("quantum_star")
                .inputItems(gem, NetherStar)
                .inputFluids(Radon.getFluid(1250))
                .outputItems(QUANTUM_STAR)
                .duration(1920).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save(provider);

        AUTOCLAVE_RECIPES.recipeBuilder("gravi_star")
                .inputItems(QUANTUM_STAR)
                .inputFluids(Neutronium.getFluid(L * 2))
                .outputItems(GRAVI_STAR)
                .duration(480).EUt(VA[IV])
                .addCondition(new GravityCondition(true))
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("quantum_eye")
                .inputItems(gem, EnderEye)
                .inputFluids(Radon.getFluid(250))
                .outputItems(QUANTUM_EYE)
                .duration(480).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save(provider);

        GTORecipeTypes.LIGHTNING_PROCESSOR_RECIPES.recipeBuilder("ender_pearl_dust").duration(400).EUt(VA[LV])
                .inputItems(dust, Beryllium)
                .inputItems(dust, Potassium, 4)
                .inputFluids(Nitrogen.getFluid(5000))
                .circuitMeta(1)
                .outputItems(dust, EnderPearl, 10)
                .addCondition(new VacuumCondition(3))
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("formic_acid")
                .inputFluids(GTOMaterials.SodiumFormate.getFluid(2000))
                .inputFluids(SulfuricAcid.getFluid(1000))
                .circuitMeta(1)
                .outputFluids(FormicAcid.getFluid(2000))
                .outputItems(dust, GTOMaterials.SodiumSulfate, 7)
                .duration(15).EUt(VA[LV]).save(provider);

        // 修复冲突
        CHEMICAL_RECIPES.recipeBuilder("hypochlorous_acid_mercury")
                .circuitMeta(10)
                .inputFluids(Mercury.getFluid(1000))
                .inputFluids(Water.getFluid(10000))
                .inputFluids(Chlorine.getFluid(10000))
                .outputFluids(HypochlorousAcid.getFluid(10000))
                .duration(600).EUt(VA[ULV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("hypochlorous_acid")
                .circuitMeta(11)
                .inputFluids(Water.getFluid(1000))
                .inputFluids(Chlorine.getFluid(2000))
                .outputFluids(DilutedHydrochloricAcid.getFluid(1000))
                .outputFluids(HypochlorousAcid.getFluid(1000))
                .duration(120).EUt(VA[LV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("benzene_from_biphenyl")
                .circuitMeta(1)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Hydrogen.getFluid(2000))
                .outputFluids(Benzene.getFluid(2000))
                .duration(400).EUt(VA[EV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("polychlorinated_biphenyl")
                .circuitMeta(2)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Chlorine.getFluid(4000))
                .outputFluids(PolychlorinatedBiphenyl.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .duration(200).EUt(VH[HV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("calcium_hydroxide")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(Water.getFluid(1000))
                .outputItems(dust, CalciumHydroxide, 3)
                .duration(100).EUt(VHA[MV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("calcite_from_quicklime")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, Calcite, 5)
                .duration(80).EUt(VA[LV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("ethylene_from_ethanol")
                .circuitMeta(1)
                .inputFluids(SulfuricAcid.getFluid(1000))
                .inputFluids(Ethanol.getFluid(1000))
                .outputFluids(Ethylene.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .duration(1200).EUt(VA[MV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("dimethylchlorosilane_from_chloromethane")
                .circuitMeta(1)
                .inputItems(dust, Silicon)
                .inputFluids(Chloromethane.getFluid(2000))
                .outputFluids(Dimethyldichlorosilane.getFluid(1000))
                .duration(240).EUt(96).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("vinyl_chloride_from_ethane")
                .circuitMeta(1)
                .inputFluids(Chlorine.getFluid(4000))
                .inputFluids(Ethane.getFluid(1000))
                .outputFluids(VinylChloride.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(3000))
                .duration(160).EUt(VA[LV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("styrene_from_ethylbenzene")
                .circuitMeta(1)
                .inputFluids(Ethylbenzene.getFluid(1000))
                .outputFluids(Styrene.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .duration(30).EUt(VA[LV])
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("soda_ash_from_carbon_dioxide")
                .circuitMeta(2)
                .inputItems(dust, SodiumHydroxide, 6)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, SodaAsh, 6)
                .outputFluids(Water.getFluid(1000))
                .duration(80).EUt(VA[HV])
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("iron_2_chloride")
                .circuitMeta(1)
                .inputFluids(Iron3Chloride.getFluid(2000))
                .inputFluids(Chlorobenzene.getFluid(1000))
                .outputFluids(Iron2Chloride.getFluid(2000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(Dichlorobenzene.getFluid(1000))
                .duration(400).EUt(VA[MV])
                .save(provider);

        MIXER_RECIPES.recipeBuilder("tungstencarbide").duration(200).EUt(VA[EV])
                .inputItems(dust, Tungsten)
                .inputItems(dust, Carbon)
                .circuitMeta(2)
                .outputItems(dust, TungstenCarbide, 2)
                .save(provider);

        MIXER_RECIPES.recipeBuilder("titanium_carbide")
                .inputItems(dust, Titanium)
                .inputItems(dust, Carbon)
                .circuitMeta(2)
                .outputItems(dust, TitaniumCarbide, 2)
                .duration(160).EUt(VA[EV])
                .save(provider);

        MIXER_RECIPES.recipeBuilder("tantalum_carbide")
                .inputItems(dust, Tantalum)
                .inputItems(dust, Carbon)
                .circuitMeta(2)
                .outputItems(dust, TantalumCarbide, 2)
                .duration(150).EUt(VA[EV])
                .save(provider);
    }
}
