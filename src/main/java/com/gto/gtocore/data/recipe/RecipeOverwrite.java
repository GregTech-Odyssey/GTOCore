package com.gto.gtocore.data.recipe;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.RestrictedMachineCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface RecipeOverwrite {

    static void init(Consumer<FinishedRecipe> provider) {
        // 修改
        CHEMICAL_RECIPES.recipeBuilder("plastic_circuit_board_persulfate").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(GTOTagPrefix.flakes, GTOMaterials.AluminaCeramic, 2)
                .inputFluids(SodiumPersulfate.getFluid(500))
                .outputItems(PLASTIC_CIRCUIT_BOARD)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("plastic_circuit_board_iron3").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(GTOTagPrefix.flakes, GTOMaterials.AluminaCeramic, 2)
                .inputFluids(Iron3Chloride.getFluid(250))
                .outputItems(PLASTIC_CIRCUIT_BOARD)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("integrated_circuit_hv").EUt(VA[LV]).duration(600)
                .inputItems(GTOTagPrefix.flakes, GTOMaterials.AluminaCeramic, 1)
                .inputItems(INTEGRATED_CIRCUIT_MV, 2)
                .inputItems(INTEGRATED_LOGIC_CIRCUIT, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 2)
                .inputItems(CustomTags.TRANSISTORS, 4)
                .inputItems(screw, AnnealedCopper, 8)
                .outputItems(INTEGRATED_CIRCUIT_HV)
                .solderMultiplier(2)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("electronic_circuit_lv").EUt(16).duration(200)
                .inputItems(BASIC_CIRCUIT_BOARD)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(wireGtSingle, RedAlloy, 2)
                .inputItems(CustomTags.ULV_CIRCUITS, 2)
                .outputItems(ELECTRONIC_CIRCUIT_LV, 2)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("processor_assembly_hv")
                .EUt(VA[MV]).duration(200)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(PROCESSOR_MV, 2)
                .inputItems(CustomTags.INDUCTORS, 4)
                .inputItems(CustomTags.CAPACITORS, 8)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, RedAlloy, 8)
                .outputItems(PROCESSOR_ASSEMBLY_HV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("polyethylene_from_oxygen")
                .circuitMeta(1)
                .inputFluids(Oxygen.getFluid(1000))
                .inputFluids(Ethylene.getFluid(L))
                .outputFluids(Polyethylene.getFluid(144))
                .heat(600)
                .duration(160).EUt(VA[LV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("polyethylene_from_air")
                .notConsumable(TagPrefix.rod, GTMaterials.Ruby)
                .inputFluids(Air.getFluid(1000))
                .inputFluids(Ethylene.getFluid(L))
                .outputFluids(Polyethylene.getFluid(126))
                .heat(600)
                .duration(300).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.recipeBuilder("polyvinyl_chloride_from_oxygen")
                .notConsumable(TagPrefix.rod, GTMaterials.Ruby)
                .inputFluids(Oxygen.getFluid(1000))
                .inputFluids(VinylChloride.getFluid(L))
                .outputFluids(PolyvinylChloride.getFluid(144))
                .heat(700)
                .duration(180).EUt(VA[LV]).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("methanol_from_monoxide")
                .circuitMeta(1)
                .inputFluids(Hydrogen.getFluid(4000))
                .inputFluids(CarbonMonoxide.getFluid(1000))
                .outputFluids(Methanol.getFluid(1000))
                .duration(360).EUt(30).save();

        ASSEMBLER_RECIPES.recipeBuilder("bucket")
                .inputItems(GTOTagPrefix.curvedPlate, Iron, 2)
                .inputItems(plate, Iron)
                .outputItems(new ItemStack(Items.BUCKET))
                .duration(100).EUt(4)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_plain")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .circuitMeta(1)
                .outputItems(VACUUM_TUBE, 2)
                .addCondition(new VacuumCondition(1))
                .duration(120).EUt(VA[ULV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_red_alloy")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 4)
                .addCondition(new VacuumCondition(2))
                .duration(40).EUt(16).save();

        ASSEMBLER_RECIPES.recipeBuilder("vacuum_tube_red_alloy_annealed")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, AnnealedCopper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 6)
                .addCondition(new VacuumCondition(3))
                .duration(40).EUt(VA[LV]).save();

        BLAST_RECIPES.recipeBuilder("engraved_crystal_chip_from_olivine")
                .inputItems(plate, Olivine)
                .inputItems(RAW_CRYSTAL_CHIP)
                .inputFluids(Helium.getFluid(1000))
                .outputItems(ENGRAVED_CRYSTAL_CHIP)
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("quantum_star")
                .inputItems(gem, NetherStar)
                .inputFluids(Radon.getFluid(1250))
                .outputItems(QUANTUM_STAR)
                .duration(1920).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("gravi_star")
                .inputItems(QUANTUM_STAR)
                .inputFluids(Neutronium.getFluid(L << 1))
                .outputItems(GRAVI_STAR)
                .duration(480).EUt(VA[IV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("quantum_eye")
                .inputItems(gem, EnderEye)
                .inputFluids(Radon.getFluid(250))
                .outputItems(QUANTUM_EYE)
                .duration(480).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("formic_acid")
                .inputFluids(GTOMaterials.SodiumFormate.getFluid(2000))
                .inputFluids(SulfuricAcid.getFluid(1000))
                .circuitMeta(1)
                .outputFluids(FormicAcid.getFluid(2000))
                .outputItems(dust, GTOMaterials.SodiumSulfate, 7)
                .duration(15).EUt(VA[LV]).save();

        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_coal_block").inputItems(block, Iron)
                .inputItems(block, Coal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(12150)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_charcoal_block").inputItems(block, Iron)
                .inputItems(block, Charcoal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(12150)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_coke_block").inputItems(block, Iron)
                .inputItems(block, Coke).outputItems(ingot, Steel, 9).outputItems(dust, Ash).duration(10125)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_coal_block_wrought").inputItems(block, WroughtIron)
                .inputItems(block, Coal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(5400)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_charcoal_block_wrought")
                .inputItems(block, WroughtIron).inputItems(block, Charcoal, 2).outputItems(ingot, Steel, 9)
                .outputItems(dust, DarkAsh, 2).duration(5400).save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder("steel_from_coke_block_wrought").inputItems(block, WroughtIron)
                .inputItems(block, Coke).outputItems(ingot, Steel, 9).outputItems(dust, Ash).duration(4050).save();

        ARC_FURNACE_RECIPES.recipeBuilder("tempered_glass").duration(60).EUt(VA[LV])
                .inputItems(block, Glass)
                .outputItems(GTBlocks.CASING_TEMPERED_GLASS.asStack())
                .addCondition(RestrictedMachineCondition.multiblock())
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("silicon_cool_down")
                .inputItems(ingotHot, Silicon)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, Silicon)
                .duration(250).EUt(VA[MV]).save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("kanthal_cool_down")
                .inputItems(ingotHot, Kanthal)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, Kanthal)
                .duration(250).EUt(VA[MV]).save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("black_steel_cool_down")
                .inputItems(ingotHot, BlackSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, BlackSteel)
                .duration(125).EUt(VA[MV]).save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("red_steel_cool_down")
                .inputItems(ingotHot, RedSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, RedSteel)
                .duration(250).EUt(VA[MV]).save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("blue_steel_cool_down")
                .inputItems(ingotHot, BlueSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, BlueSteel)
                .duration(250).EUt(VA[MV]).save(provider);

        MIXER_RECIPES.recipeBuilder("pcb_coolant").duration(200).EUt(VA[HV])
                .inputFluids(PolychlorinatedBiphenyl.getFluid(750))
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(250))
                .outputFluids(PCBCoolant.getFluid(1000))
                .save(provider);

        // 修复冲突
        CHEMICAL_RECIPES.recipeBuilder("hypochlorous_acid_mercury")
                .circuitMeta(10)
                .inputFluids(Mercury.getFluid(1000))
                .inputFluids(Water.getFluid(10000))
                .inputFluids(Chlorine.getFluid(10000))
                .outputFluids(HypochlorousAcid.getFluid(10000))
                .duration(600).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.recipeBuilder("hypochlorous_acid")
                .circuitMeta(11)
                .inputFluids(Water.getFluid(1000))
                .inputFluids(Chlorine.getFluid(2000))
                .outputFluids(DilutedHydrochloricAcid.getFluid(1000))
                .outputFluids(HypochlorousAcid.getFluid(1000))
                .duration(120).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("benzene_from_biphenyl")
                .circuitMeta(1)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Hydrogen.getFluid(2000))
                .outputFluids(Benzene.getFluid(2000))
                .duration(400).EUt(VA[EV]).save();

        CHEMICAL_RECIPES.recipeBuilder("polychlorinated_biphenyl")
                .circuitMeta(2)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Chlorine.getFluid(4000))
                .outputFluids(PolychlorinatedBiphenyl.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .duration(200).EUt(VH[HV]).save();

        CHEMICAL_RECIPES.recipeBuilder("calcium_hydroxide")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(Water.getFluid(1000))
                .outputItems(dust, CalciumHydroxide, 3)
                .duration(100).EUt(VHA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("calcite_from_quicklime")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, Calcite, 5)
                .duration(80).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("ethylene_from_ethanol")
                .circuitMeta(1)
                .inputFluids(SulfuricAcid.getFluid(1000))
                .inputFluids(Ethanol.getFluid(1000))
                .outputFluids(Ethylene.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .duration(1200).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("dimethylchlorosilane_from_chloromethane")
                .circuitMeta(1)
                .inputItems(dust, Silicon)
                .inputFluids(Chloromethane.getFluid(2000))
                .outputFluids(Dimethyldichlorosilane.getFluid(1000))
                .duration(240).EUt(96).save();

        CHEMICAL_RECIPES.recipeBuilder("vinyl_chloride_from_ethane")
                .circuitMeta(1)
                .inputFluids(Chlorine.getFluid(4000))
                .inputFluids(Ethane.getFluid(1000))
                .outputFluids(VinylChloride.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(3000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("styrene_from_ethylbenzene")
                .circuitMeta(1)
                .inputFluids(Ethylbenzene.getFluid(1000))
                .outputFluids(Styrene.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .duration(30).EUt(VA[LV])
                .save();

        CHEMICAL_RECIPES.recipeBuilder("soda_ash_from_carbon_dioxide")
                .circuitMeta(2)
                .inputItems(dust, SodiumHydroxide, 6)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, SodaAsh, 6)
                .outputFluids(Water.getFluid(1000))
                .duration(80).EUt(VA[HV])
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("iron_2_chloride")
                .circuitMeta(1)
                .inputFluids(Iron3Chloride.getFluid(2000))
                .inputFluids(Chlorobenzene.getFluid(1000))
                .outputFluids(Iron2Chloride.getFluid(2000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(Dichlorobenzene.getFluid(1000))
                .duration(400).EUt(VA[MV])
                .save();

        MIXER_RECIPES.recipeBuilder("tantalum_carbide")
                .inputItems(dust, Tantalum)
                .inputItems(dust, Carbon)
                .circuitMeta(2)
                .outputItems(dust, TantalumCarbide, 2)
                .duration(150).EUt(VA[EV])
                .save();
    }
}
