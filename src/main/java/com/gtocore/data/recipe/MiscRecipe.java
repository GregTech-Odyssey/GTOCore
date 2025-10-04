package com.gtocore.data.recipe;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.item.DimensionDataItem;
import com.gtocore.common.machine.multiblock.electric.BlockConversionRoomMachine;
import com.gtocore.common.recipe.condition.GravityCondition;
import com.gtocore.common.recipe.condition.RestrictedMachineCondition;
import com.gtocore.common.recipe.condition.VacuumCondition;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.item.MultiStepItemHelper;
import com.gtolib.api.item.tool.GTOToolType;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidContainerIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import earth.terrarium.adastra.common.registry.ModItems;

import java.util.Arrays;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOItems.PLANET_DATA_CHIP;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class MiscRecipe {

    public static void init() {
        int i = 0;
        for (ResourceLocation layer : Arrays.stream(Dimension.values()).filter(Dimension::canGenerate).map(Dimension::getLocation).toList()) {
            i++;
            ItemStack stack = GTOItems.DIMENSION_DATA.get().getDimensionData(layer);
            int tier = DimensionDataItem.getDimensionMarker(stack).tier + 1;
            WORLD_DATA_SCANNER_RECIPES.recipeBuilder(layer.getPath())
                    .inputItems(TOOL_DATA_STICK.asItem())
                    .circuitMeta(i)
                    .inputFluids(PCBCoolant.getFluid(1000 * tier))
                    .outputItems(stack)
                    .dimension(layer)
                    .EUt(VA[tier])
                    .duration(2400)
                    .save();

        }
        int i1 = 0;
        for (ResourceLocation hasGlobe : Arrays.stream(Dimension.values()).filter(d -> d.canGenerate() && d.isWithinGalaxy()).map(Dimension::getLocation).toList()) {

            Item globe = hasGlobe.getPath().equals("overworld") ? ModItems.EARTH_GLOBE.get() :
                    RegistriesUtils.getItem("ad_astra:" + hasGlobe.getPath() + "_globe");
            ItemStack stack = GTOItems.DIMENSION_DATA.get().getDimensionData(hasGlobe);
            int tier = DimensionDataItem.getDimensionMarker(stack).tier + 1;
            if (globe != Items.BARRIER) {
                WORLD_DATA_SCANNER_RECIPES.recipeBuilder(hasGlobe.withSuffix("_globe").getPath())
                        .inputItems(PLANET_DATA_CHIP.asItem())
                        .inputItems(frameGt, Steel)
                        .inputFluids(PolyvinylButyral.getFluid(576))
                        .inputFluids(CHEMICAL_DYES[i1++].getFluid(72))
                        .outputItems(globe)
                        .dimension(hasGlobe)
                        .EUt(VA[tier])
                        .duration(2400)
                        .save();
            }
        }

        BlockConversionRoomMachine.COV_RECIPE.forEach((a, b) -> BLOCK_CONVERSIONRECIPES.recipeBuilder(ItemUtils.getIdLocation(a).getPath())
                .inputItems(a.asItem())
                .outputItems(b.asItem())
                .duration(20)
                .save());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_bucket"), new ItemStack(Items.BUCKET), "ChC", " X ", 'X',
                new MaterialEntry(plate, Iron), 'C', new MaterialEntry(GTOTagPrefix.CURVED_PLATE, Iron));

        VanillaRecipeHelper.addBlastingRecipe(GTOCore.id("hot_iron_ingot"), ChemicalHelper.getTag(ingot, Iron), MultiStepItemHelper.toMultiStepItem(GTOItems.HOT_IRON_INGOT.asStack(), 1, 2), 0);

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("hot_iron_ingot"), MultiStepItemHelper.toMultiStepItem(GTOItems.HOT_IRON_INGOT.asStack(), 2, 2), "h", "H", 'H', MultiStepItemHelper.toMultiStepItem(GTOItems.HOT_IRON_INGOT.asStack(), 1, 2));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wrought_iron_ingot"), ChemicalHelper.get(ingot, WroughtIron), "h", "H", 'H', MultiStepItemHelper.toMultiStepItem(GTOItems.HOT_IRON_INGOT.asStack(), 2, 2));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("raw_vacuum_tube"), GTOItems.RAW_VACUUM_TUBE.asItem(),
                "PTP", "WWW",
                'P', new MaterialEntry(bolt, Steel),
                'T', GLASS_TUBE.asItem(),
                'W', new MaterialEntry(wireGtSingle, Copper));

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("air_vent"), GTOItems.AIR_VENT.asItem(),
                "RRR", "ROR", "RRR",
                'R', new MaterialEntry(rod, Steel),
                'O', new MaterialEntry(rotor, Iron));

        VanillaRecipeHelper.addShapedEnergyTransferRecipe(true, true, true, GTOCore.id("hv_vajra"), Ingredient.of(POWER_UNIT_HV.asItem()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.DarkSteel, GTOToolType.VAJRA_HV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_HV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asItem(), 'F', FIELD_GENERATOR_HV.asItem(), 'P', new MaterialEntry(plateDouble, GTOMaterials.DarkSteel), 'R', new MaterialEntry(plateDense, Steel), 'C', CARBON_FIBER_PLATE.asItem(), 'U', POWER_UNIT_HV.asItem());
        VanillaRecipeHelper.addShapedEnergyTransferRecipe(true, true, true, GTOCore.id("ev_vajra"), Ingredient.of(POWER_UNIT_EV.asItem()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.Ostrum, GTOToolType.VAJRA_EV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_EV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asItem(), 'F', FIELD_GENERATOR_EV.asItem(), 'P', new MaterialEntry(plateDouble, GTOMaterials.Ostrum), 'R', new MaterialEntry(plateDense, TungstenSteel), 'C', CARBON_FIBER_PLATE.asItem(), 'U', POWER_UNIT_EV.asItem());
        VanillaRecipeHelper.addShapedEnergyTransferRecipe(true, true, true, GTOCore.id("iv_vajra"), Ingredient.of(POWER_UNIT_IV.asItem()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.Enderium, GTOToolType.VAJRA_IV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_IV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asItem(), 'F', FIELD_GENERATOR_IV.asItem(), 'P', new MaterialEntry(plateDouble, GTOMaterials.Enderium), 'R', new MaterialEntry(plateDense, NaquadahAlloy), 'C', CARBON_FIBER_PLATE.asItem(), 'U', POWER_UNIT_IV.asItem());
        VanillaRecipeHelper.addShapedFluidContainerRecipe("bucket_of_concrete", new ItemStack(Concrete.getBucket()),
                "CBS", "CWQ", " L ",
                'C', new MaterialEntry(dust, Calcite),
                'S', new MaterialEntry(dust, Stone),
                'W', new FluidContainerIngredient(Water.getFluid(1000)),
                'Q', new MaterialEntry(dust, QuartzSand),
                'L', new MaterialEntry(dust, Clay),
                'B', new ItemStack(Items.BUCKET));

        VACUUM_PUMP_RECIPES.recipeBuilder("a")
                .notConsumable(pipeHugeFluid, Bronze)
                .EUt(7).duration(200)
                .addData("tier", 0)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder("b")
                .notConsumable(FLUID_REGULATOR_LV)
                .EUt(30).duration(200)
                .addData("tier", 1)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder("c")
                .notConsumable(FLUID_REGULATOR_MV)
                .EUt(120).duration(200)
                .addData("tier", 2)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder("d")
                .notConsumable(FLUID_REGULATOR_HV)
                .EUt(480).duration(200)
                .addData("tier", 3)
                .save();

        WOOD_DISTILLATION_RECIPES.builder("muhuayihaodianlu")
                .inputItems(ItemTags.LOGS, 16)
                .outputItems(TagPrefix.dust, GTMaterials.Ash, 2)
                .inputFluids(GTMaterials.Nitrogen, 1000)
                .outputFluids(GTMaterials.Benzene, 1050)
                .outputFluids(GTMaterials.Dimethylbenzene, 1000)
                .outputFluids(GTMaterials.Creosote, 900)
                .outputFluids(GTMaterials.CarbonDioxide, 490)
                .outputFluids(GTMaterials.Methanol, 480)
                .outputFluids(GTMaterials.CarbonMonoxide, 340)
                .outputFluids(GTMaterials.Phenol, 225)
                .outputFluids(GTMaterials.Toluene, 225)
                .outputFluids(GTMaterials.AceticAcid, 160)
                .outputFluids(GTMaterials.Methane, 130)
                .outputFluids(GTMaterials.Acetone, 80)
                .outputFluids(GTMaterials.Ethylene, 20)
                .outputFluids(GTMaterials.Hydrogen, 20)
                .outputFluids(GTMaterials.MethylAcetate, 16)
                .outputFluids(GTMaterials.Ethanol, 16)
                .circuitMeta(1)
                .EUt(120)
                .duration(300)
                .save();

        WOOD_DISTILLATION_RECIPES.builder("muhuaerhaodianlu")
                .inputItems(ItemTags.LOGS, 16)
                .outputItems(TagPrefix.dust, GTMaterials.Ash, 2)
                .inputFluids(GTMaterials.Steam, 1000)
                .outputFluids(GTMaterials.Ammonia, 600)
                .outputFluids(GTMaterials.CarbonDioxide, 500)
                .outputFluids(GTMaterials.Ethylbenzene, 500)
                .outputFluids(GTMaterials.Naphthalene, 410)
                .outputFluids(GTMaterials.HydrogenSulfide, 307)
                .outputFluids(GTMaterials.Creosote, 205)
                .outputFluids(GTMaterials.Phenol, 102)
                .circuitMeta(2)
                .EUt(120)
                .duration(300)
                .save();

        WOOD_DISTILLATION_RECIPES.builder("muhuasanhaodianlu")
                .inputItems(ItemTags.LOGS, 16)
                .outputItems(GTItems.FERTILIZER.asItem(), 6)
                .inputFluids(GTMaterials.Water, 6000)
                .outputFluids(GTMaterials.Methane, 3300)
                .outputFluids(GTMaterials.CarbonDioxide, 2200)
                .outputFluids(GTMaterials.Methanol, 825)
                .outputFluids(GTMaterials.Ethanol, 825)
                .outputFluids(GTMaterials.Creosote, 560)
                .outputFluids(GTMaterials.Ammonia, 550)
                .outputFluids(GTMaterials.AceticAcid, 137)
                .circuitMeta(3)
                .EUt(120)
                .duration(300)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("water_agar_mix").EUt(VA[HV]).duration(600)
                .chancedInput(GTOItems.RED_ALGAE_FIBER.asItem(), 1000, 0)
                .inputItems(dust, Gelatin)
                .inputFluids(DistilledWater.getFluid(1000))
                .outputFluids(GTOMaterials.WaterAgarMix.getFluid(1000))
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder("agar")
                .inputFluids(GTOMaterials.WaterAgarMix.getFluid(1000))
                .outputItems(dust, Agar, 1)
                .duration(420).EUt(VA[MV])
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_ev_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_EV.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asItem(), 1)
                .outputItems(LAPOTRON_CRYSTAL)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_iv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_IV.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I)
                .outputItems(ENERGY_LAPOTRONIC_ORB)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_luv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_LuV.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_LAPOTRONIC_ORB_CLUSTER)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_zpm_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_ZPM.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_MODULE)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_uv_lapotronic_battery")
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_UV.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ENERGY_CLUSTER)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder("unpackage_uhv_ultimate_battery")
                .inputItems(GTBlocks.BATTERY_ULTIMATE_UHV.asItem(), 1)
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ULTIMATE_BATTERY)
                .duration(200).EUt(VA[LV]).save();

        LOOM_RECIPES.recipeBuilder("wool_from_string")
                .inputItems(Items.STRING, 4)
                .circuitMeta(4)
                .outputItems(Blocks.WHITE_WOOL)
                .duration(100).EUt(4).save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("overworld_scrubber")
                .circuitMeta(1)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputItems(dustTiny, Ash)
                .duration(200)
                .EUt(VHA[LV])
                .dimension(GTODimensions.OVERWORLD)
                .save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("void_scrubber")
                .circuitMeta(11)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .duration(200)
                .EUt(VHA[LV])
                .dimension(GTODimensions.VOID)
                .save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("flat_scrubber")
                .circuitMeta(12)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .duration(200)
                .EUt(VHA[LV])
                .dimension(GTODimensions.FLAT)
                .save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("end_scrubber")
                .circuitMeta(3)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputFluids(NitricAcid.getFluid(500))
                .duration(200)
                .EUt(VHA[HV])
                .dimension(GTODimensions.THE_END)
                .save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("nether_scrubber")
                .circuitMeta(2)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputFluids(SulfuricAcid.getFluid(500))
                .duration(200)
                .EUt(VHA[MV])
                .dimension(GTODimensions.THE_NETHER)
                .save();

        AIR_SCRUBBER_RECIPES.recipeBuilder("otherside_scrubber")
                .circuitMeta(4)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputFluids(new FluidStack(EIOFluids.XP_JUICE.getSource(), 800))
                .duration(200)
                .EUt(VHA[EV])
                .dimension(GTODimensions.OTHERSIDE)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hopper_iron").EUt(2).inputItems(Tags.Items.CHESTS_WOODEN)
                .inputItems(plate, Iron, 5).circuitMeta(4).outputItems(Blocks.HOPPER).duration(800)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("minecart")
                .inputItems(plate, Iron, 3)
                .inputItems(ring, Iron, 4)
                .circuitMeta(10)
                .outputItems(Items.MINECART)
                .duration(100).EUt(4).save();

        ASSEMBLER_RECIPES.recipeBuilder("iron_minecart_wheels")
                .inputItems(rod, Iron)
                .inputItems(ring, Iron, 2)
                .outputItems(IRON_MINECART_WHEELS)
                .circuitMeta(10)
                .duration(100).EUt(20)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("steel_minecart_wheels")
                .inputItems(rod, Steel)
                .inputItems(ring, Steel, 2)
                .circuitMeta(10)
                .outputItems(STEEL_MINECART_WHEELS)
                .duration(60).EUt(20).save();

        ASSEMBLER_RECIPES.recipeBuilder("stonecutter")
                .inputItems(toolHeadBuzzSaw, Iron)
                .inputItems(Blocks.STONE_SLAB)
                .outputItems(Blocks.STONECUTTER)
                .circuitMeta(10)
                .duration(80).EUt(6).save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_item_voiding")
                .inputItems(screw, Steel, 2)
                .inputItems(COVER_ITEM_DETECTOR)
                .inputItems(pipeNormalItem, Brass)
                .inputItems(Items.ENDER_PEARL)
                .outputItems(COVER_ITEM_VOIDING)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_item_voiding_advanced")
                .inputItems(COVER_ITEM_VOIDING)
                .inputItems(CustomTags.MV_CIRCUITS, 1)
                .outputItems(COVER_ITEM_VOIDING_ADVANCED)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_fluid_voiding")
                .inputItems(screw, Steel, 2)
                .inputItems(COVER_FLUID_DETECTOR)
                .inputItems(pipeNormalFluid, Bronze)
                .inputItems(Items.ENDER_PEARL)
                .outputItems(COVER_FLUID_VOIDING)
                .duration(100).EUt(VA[LV]).save();

        ASSEMBLER_RECIPES.recipeBuilder("cover_fluid_voiding_advanced")
                .inputItems(COVER_FLUID_VOIDING)
                .inputItems(CustomTags.MV_CIRCUITS, 1)
                .outputItems(COVER_FLUID_VOIDING_ADVANCED)
                .duration(100).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("plastic_circuit_board_persulfate").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.AluminaCeramic, 2)
                .inputFluids(SodiumPersulfate.getFluid(500))
                .outputItems(PLASTIC_CIRCUIT_BOARD)
                .save();

        CHEMICAL_RECIPES.builder("plastic_circuit_board_iron3").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.AluminaCeramic, 2)
                .inputFluids(Iron3Chloride.getFluid(250))
                .outputItems(PLASTIC_CIRCUIT_BOARD)
                .save();

        CHEMICAL_RECIPES.builder("polyethylene_from_oxygen")
                .circuitMeta(4)
                .inputFluids(Oxygen.getFluid(1000))
                .inputFluids(Ethylene.getFluid(L))
                .outputFluids(Polyethylene.getFluid(144))
                .heat(600)
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("polyethylene_from_air")
                .notConsumable(TagPrefix.rod, GTMaterials.Ruby)
                .inputFluids(Air.getFluid(1000))
                .inputFluids(Ethylene.getFluid(L))
                .outputFluids(Polyethylene.getFluid(126))
                .heat(600)
                .duration(300).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("polyvinyl_chloride_from_oxygen")
                .notConsumable(TagPrefix.rod, GTMaterials.Ruby)
                .inputFluids(Oxygen.getFluid(1000))
                .inputFluids(VinylChloride.getFluid(L))
                .outputFluids(PolyvinylChloride.getFluid(144))
                .heat(700)
                .duration(180).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("methanol_from_monoxide")
                .circuitMeta(1)
                .inputFluids(Hydrogen.getFluid(4000))
                .inputFluids(CarbonMonoxide.getFluid(1000))
                .outputFluids(Methanol.getFluid(1000))
                .duration(360).EUt(30).save();

        ASSEMBLER_RECIPES.builder("vacuum_tube_plain")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .circuitMeta(1)
                .outputItems(VACUUM_TUBE, 2)
                .addCondition(new VacuumCondition(1))
                .duration(120).EUt(VA[ULV]).save();

        ASSEMBLER_RECIPES.builder("vacuum_tube_red_alloy")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, Copper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 4)
                .addCondition(new VacuumCondition(2))
                .duration(40).EUt(16).save();

        ASSEMBLER_RECIPES.builder("vacuum_tube_red_alloy_annealed")
                .inputItems(GLASS_TUBE)
                .inputItems(bolt, Steel)
                .inputItems(wireGtSingle, AnnealedCopper, 2)
                .inputFluids(RedAlloy.getFluid(18))
                .outputItems(VACUUM_TUBE, 6)
                .addCondition(new VacuumCondition(3))
                .duration(40).EUt(VA[LV]).save();

        BLAST_RECIPES.builder("engraved_crystal_chip_from_olivine")
                .inputItems(plate, Olivine)
                .inputItems(RAW_CRYSTAL_CHIP)
                .inputFluids(Helium.getFluid(1000))
                .outputItems(ENGRAVED_CRYSTAL_CHIP)
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_BATH_RECIPES.builder("quantum_star")
                .inputItems(gem, NetherStar)
                .inputFluids(Radon.getFluid(1250))
                .outputItems(QUANTUM_STAR)
                .duration(1920).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        AUTOCLAVE_RECIPES.builder("gravi_star")
                .inputItems(QUANTUM_STAR)
                .inputFluids(Neutronium.getFluid(L << 1))
                .outputItems(GRAVI_STAR)
                .duration(480).EUt(VA[IV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_BATH_RECIPES.builder("quantum_eye")
                .inputItems(gem, EnderEye)
                .inputFluids(Radon.getFluid(250))
                .outputItems(QUANTUM_EYE)
                .duration(480).EUt(VA[HV])
                .addCondition(new GravityCondition(true))
                .save();

        CHEMICAL_RECIPES.builder("formic_acid")
                .inputFluids(GTOMaterials.SodiumFormate.getFluid(2000))
                .inputFluids(SulfuricAcid.getFluid(1000))
                .circuitMeta(1)
                .outputFluids(FormicAcid.getFluid(2000))
                .outputItems(dust, GTOMaterials.SodiumSulfate, 7)
                .duration(15).EUt(VA[LV]).save();

        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_coal_block").inputItems(block, Iron)
                .inputItems(block, Coal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(12150)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_charcoal_block").inputItems(block, Iron)
                .inputItems(block, Charcoal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(12150)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_coke_block").inputItems(block, Iron)
                .inputItems(block, Coke).outputItems(ingot, Steel, 9).outputItems(dust, Ash).duration(10125)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_coal_block_wrought").inputItems(block, WroughtIron)
                .inputItems(block, Coal, 2).outputItems(ingot, Steel, 9).outputItems(dust, DarkAsh, 2).duration(5400)
                .save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_charcoal_block_wrought")
                .inputItems(block, WroughtIron).inputItems(block, Charcoal, 2).outputItems(ingot, Steel, 9)
                .outputItems(dust, DarkAsh, 2).duration(5400).save();
        PRIMITIVE_BLAST_FURNACE_RECIPES.builder("steel_from_coke_block_wrought").inputItems(block, WroughtIron)
                .inputItems(block, Coke).outputItems(ingot, Steel, 9).outputItems(dust, Ash).duration(4050).save();

        ARC_FURNACE_RECIPES.builder("tempered_glass").duration(60).EUt(VA[LV])
                .inputItems(block, Glass)
                .outputItems(GTBlocks.CASING_TEMPERED_GLASS.asItem())
                .addCondition(RestrictedMachineCondition.multiblock())
                .save();

        CHEMICAL_BATH_RECIPES.builder("silicon_cool_down")
                .inputItems(ingotHot, Silicon)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, Silicon)
                .duration(250).EUt(VA[MV]).save();

        CHEMICAL_BATH_RECIPES.builder("kanthal_cool_down")
                .inputItems(ingotHot, Kanthal)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, Kanthal)
                .duration(250).EUt(VA[MV]).save();

        CHEMICAL_BATH_RECIPES.builder("black_steel_cool_down")
                .inputItems(ingotHot, BlackSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, BlackSteel)
                .duration(125).EUt(VA[MV]).save();

        CHEMICAL_BATH_RECIPES.builder("red_steel_cool_down")
                .inputItems(ingotHot, RedSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, RedSteel)
                .duration(250).EUt(VA[MV]).save();

        CHEMICAL_BATH_RECIPES.builder("blue_steel_cool_down")
                .inputItems(ingotHot, BlueSteel)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(ingot, BlueSteel)
                .duration(250).EUt(VA[MV]).save();

        MIXER_RECIPES.builder("pcb_coolant").duration(200).EUt(VA[HV])
                .inputFluids(PolychlorinatedBiphenyl.getFluid(750))
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(250))
                .outputFluids(PCBCoolant.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.builder("hypochlorous_acid_mercury")
                .circuitMeta(10)
                .inputFluids(Mercury.getFluid(1000))
                .inputFluids(Water.getFluid(10000))
                .inputFluids(Chlorine.getFluid(10000))
                .outputFluids(HypochlorousAcid.getFluid(10000))
                .duration(600).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.builder("hypochlorous_acid")
                .circuitMeta(11)
                .inputFluids(Water.getFluid(1000))
                .inputFluids(Chlorine.getFluid(2000))
                .outputFluids(DilutedHydrochloricAcid.getFluid(1000))
                .outputFluids(HypochlorousAcid.getFluid(1000))
                .duration(120).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("benzene_from_biphenyl")
                .circuitMeta(1)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Hydrogen.getFluid(2000))
                .outputFluids(Benzene.getFluid(2000))
                .duration(400).EUt(VA[EV]).save();

        CHEMICAL_RECIPES.builder("polychlorinated_biphenyl")
                .circuitMeta(2)
                .inputItems(dust, Biphenyl, 2)
                .inputFluids(Chlorine.getFluid(4000))
                .outputFluids(PolychlorinatedBiphenyl.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .duration(200).EUt(VH[HV]).save();

        CHEMICAL_RECIPES.builder("calcium_hydroxide")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(Water.getFluid(1000))
                .outputItems(dust, CalciumHydroxide, 3)
                .duration(100).EUt(VHA[MV]).save();

        CHEMICAL_RECIPES.builder("calcite_from_quicklime")
                .circuitMeta(1)
                .inputItems(dust, Quicklime, 2)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, Calcite, 5)
                .duration(80).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("ethylene_from_ethanol")
                .circuitMeta(1)
                .inputFluids(SulfuricAcid.getFluid(1000))
                .inputFluids(Ethanol.getFluid(1000))
                .outputFluids(Ethylene.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .duration(1200).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.builder("dimethylchlorosilane_from_chloromethane")
                .circuitMeta(1)
                .inputItems(dust, Silicon)
                .inputFluids(Chloromethane.getFluid(2000))
                .outputFluids(Dimethyldichlorosilane.getFluid(1000))
                .duration(240).EUt(96).save();

        CHEMICAL_RECIPES.builder("vinyl_chloride_from_ethane")
                .circuitMeta(1)
                .inputFluids(Chlorine.getFluid(4000))
                .inputFluids(Ethane.getFluid(1000))
                .outputFluids(VinylChloride.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(3000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.builder("styrene_from_ethylbenzene")
                .circuitMeta(1)
                .inputFluids(Ethylbenzene.getFluid(1000))
                .outputFluids(Styrene.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .duration(30).EUt(VA[LV])
                .save();

        CHEMICAL_RECIPES.builder("soda_ash_from_carbon_dioxide")
                .circuitMeta(2)
                .inputItems(dust, SodiumHydroxide, 6)
                .inputFluids(CarbonDioxide.getFluid(1000))
                .outputItems(dust, SodaAsh, 6)
                .outputFluids(Water.getFluid(1000))
                .duration(80).EUt(VA[HV])
                .save();

        LARGE_CHEMICAL_RECIPES.builder("iron_2_chloride")
                .circuitMeta(1)
                .inputFluids(Iron3Chloride.getFluid(2000))
                .inputFluids(Chlorobenzene.getFluid(1000))
                .outputFluids(Iron2Chloride.getFluid(2000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(Dichlorobenzene.getFluid(1000))
                .duration(400).EUt(VA[MV])
                .save();

        MIXER_RECIPES.builder("tantalum_carbide")
                .inputItems(dust, Tantalum)
                .inputItems(dust, Carbon)
                .circuitMeta(2)
                .outputItems(dust, TantalumCarbide, 2)
                .duration(150).EUt(VA[EV])
                .save();

        ASSEMBLER_RECIPES.builder("casing_stainless_evaporation")
                .inputItems(GTBlocks.CASING_STAINLESS_CLEAN.asItem(), 1)
                .inputItems(wireGtDouble, AnnealedCopper, 4)
                .inputFluids(PolyvinylChloride, L << 1)
                .outputItems(GTOBlocks.STAINLESS_EVAPORATION_CASING.asItem())
                .duration(30).EUt(VA[HV]).save();

        ASSEMBLER_RECIPES.builder("cover_advanced_item_detector")
                .circuitMeta(3)
                .inputItems(COVER_ITEM_DETECTOR)
                .inputItems(SENSOR_HV)
                .inputFluids(SolderingAlloy, L / 2)
                .outputItems(COVER_ITEM_DETECTOR_ADVANCED)
                .EUt(16).duration(100).save();

        FLUID_SOLIDFICATION_RECIPES.builder("light_concrete_cobblestone")
                .outputItems("gtceu:light_concrete_cobblestone")
                .inputFluids(GTMaterials.Concrete, 144)
                .EUt(7)
                .duration(100)
                .save();
    }
}
