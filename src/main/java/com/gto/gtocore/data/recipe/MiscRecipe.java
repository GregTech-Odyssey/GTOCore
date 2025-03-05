package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.item.tool.GTOToolType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.item.DimensionDataItem;
import com.gto.gtocore.common.machine.multiblock.electric.BlockConversionRoomMachine;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;
import static com.gto.gtocore.common.data.machines.MultiBlockB.PRIMITIVE_VOID_ORE;

public interface MiscRecipe {

    static void init(Consumer<FinishedRecipe> provider) {
        if (GTOConfig.INSTANCE.enablePrimitiveVoidOre) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("primitive_void_ore_recipes"),
                    PRIMITIVE_VOID_ORE.asStack(), "DCD", "CGC", "DCD",
                    'D', Blocks.DIRT.asItem(),
                    'C', Items.IRON_PICKAXE.asItem(),
                    'G', new UnificationEntry(block, Iron));
            PRIMITIVE_VOID_ORE_RECIPES.recipeBuilder(GTOCore.id("primitive_void_ore_recipes"))
                    .inputFluids(Steam.getFluid(1000))
                    .duration(200)
                    .save();
        }

        int i = 0;
        for (ResourceLocation layer : GTODimensions.ALL_LAYER_DIMENSION.keySet()) {
            i++;
            ItemStack stack = GTOItems.DIMENSION_DATA.get().getDimensionData(layer);
            int tier = DimensionDataItem.getDimensionMarker(stack).tier + 1;
            WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id(layer.getPath()))
                    .inputItems(TOOL_DATA_STICK.asStack())
                    .circuitMeta(i)
                    .inputFluids(PCBCoolant.getFluid(1000 * tier))
                    .outputItems(stack)
                    .dimension(layer)
                    .EUt(VA[tier])
                    .duration(2400)
                    .save();
        }

        BlockConversionRoomMachine.COV_RECIPE.forEach((a, b) -> BLOCK_CONVERSIONRECIPES.recipeBuilder(GTOCore.id(ItemUtils.getIdLocation(a).getPath()))
                .inputItems(a.asItem())
                .outputItems(b.asItem())
                .duration(20)
                .save());

        VOID_MINER_RECIPES.recipeBuilder(GTOCore.id("a"))
                .inputFluids(DrillingFluid.getFluid(1000))
                .EUt(VA[4])
                .duration(20)
                .save();

        VOID_FLUID_DRILLING_RIG_RECIPES.recipeBuilder(GTOCore.id("a"))
                .notConsumable(PROGRAMMED_CIRCUIT.get())
                .EUt(VA[6])
                .duration(20)
                .save();

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("iron_bucket"), new ItemStack(Items.BUCKET), "ChC", " X ", 'X',
                new UnificationEntry(plate, Iron), 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, Iron));

        VanillaRecipeHelper.addBlastingRecipe(provider, GTOCore.id("hot_iron_ingot"), ChemicalHelper.getTag(ingot, Iron), GTOItems.HOT_IRON_INGOT.asStack(), 0);

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("wrought_iron_ingot"), ChemicalHelper.get(ingot, WroughtIron), "h", "H", 'H', GTOItems.HOT_IRON_INGOT.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("raw_vacuum_tube"), GTOItems.RAW_VACUUM_TUBE.asStack(),
                "PTP", "WWW",
                'P', new UnificationEntry(bolt, Steel),
                'T', GLASS_TUBE.asStack(),
                'W', new UnificationEntry(wireGtSingle, Copper));

        VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("air_vent"), GTOItems.AIR_VENT.asStack(),
                "RRR", "ROR", "RRR",
                'R', new UnificationEntry(rod, Steel),
                'O', new UnificationEntry(rotor, Iron));

        VanillaRecipeHelper.addShapedEnergyTransferRecipe(provider, true, true, true, GTOCore.id("hv_vajra"), Ingredient.of(POWER_UNIT_HV.asStack()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.DarkSteel, GTOToolType.VAJRA_HV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_HV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asStack(), 'F', FIELD_GENERATOR_HV.asStack(), 'P', new UnificationEntry(plateDouble, GTOMaterials.DarkSteel), 'R', new UnificationEntry(plateDense, Steel), 'C', CARBON_FIBER_PLATE.asStack(), 'U', POWER_UNIT_HV.asStack());
        VanillaRecipeHelper.addShapedEnergyTransferRecipe(provider, true, true, true, GTOCore.id("ev_vajra"), Ingredient.of(POWER_UNIT_EV.asStack()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.Ostrum, GTOToolType.VAJRA_EV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_EV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asStack(), 'F', FIELD_GENERATOR_EV.asStack(), 'P', new UnificationEntry(plateDouble, GTOMaterials.Ostrum), 'R', new UnificationEntry(plateDense, TungstenSteel), 'C', CARBON_FIBER_PLATE.asStack(), 'U', POWER_UNIT_EV.asStack());
        VanillaRecipeHelper.addShapedEnergyTransferRecipe(provider, true, true, true, GTOCore.id("iv_vajra"), Ingredient.of(POWER_UNIT_IV.asStack()), GTMaterialItems.TOOL_ITEMS.get(GTOMaterials.Enderium, GTOToolType.VAJRA_IV).get().get(0, GTCapabilityHelper.getElectricItem(POWER_UNIT_IV.asStack()).getMaxCharge()), "PEP", "CFC", "RUR", 'E', EMITTER_HV.asStack(), 'F', FIELD_GENERATOR_IV.asStack(), 'P', new UnificationEntry(plateDouble, GTOMaterials.Enderium), 'R', new UnificationEntry(plateDense, NaquadahAlloy), 'C', CARBON_FIBER_PLATE.asStack(), 'U', POWER_UNIT_IV.asStack());

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("a"))
                .notConsumable(pipeHugeFluid, Bronze)
                .EUt(7).duration(200)
                .addData("tier", 0)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("b"))
                .notConsumable(FLUID_REGULATOR_LV)
                .EUt(30).duration(200)
                .addData("tier", 1)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("c"))
                .notConsumable(FLUID_REGULATOR_MV)
                .EUt(120).duration(200)
                .addData("tier", 2)
                .save();

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("d"))
                .notConsumable(FLUID_REGULATOR_HV)
                .EUt(480).duration(200)
                .addData("tier", 3)
                .save();

        WOOD_DISTILLATION_RECIPES.recipeBuilder(GTOCore.id("wood_distillation_recipes"))
                .inputItems(ItemTags.LOGS, 16)
                .inputFluids(Nitrogen.getFluid(1000))
                .outputItems(dust, DarkAsh, 8)
                .outputFluids(Water.getFluid(800))
                .outputFluids(Methanol.getFluid(480))
                .outputFluids(Benzene.getFluid(350))
                .outputFluids(CarbonMonoxide.getFluid(340))
                .outputFluids(Creosote.getFluid(300))
                .outputFluids(Dimethylbenzene.getFluid(240))
                .outputFluids(AceticAcid.getFluid(160))
                .outputFluids(Methane.getFluid(130))
                .outputFluids(Acetone.getFluid(80))
                .outputFluids(Phenol.getFluid(75))
                .outputFluids(Toluene.getFluid(75))
                .outputFluids(Ethylene.getFluid(20))
                .outputFluids(Hydrogen.getFluid(20))
                .outputFluids(MethylAcetate.getFluid(16))
                .outputFluids(Ethanol.getFluid(16))
                .duration(200).EUt(VA[MV])
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("water_agar_mix")).EUt(VA[HV]).duration(600)
                .inputItems(dust, Gelatin)
                .inputFluids(DistilledWater.getFluid(1000))
                .outputFluids(GTOMaterials.WaterAgarMix.getFluid(1000))
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("agar"))
                .inputFluids(GTOMaterials.WaterAgarMix.getFluid(1000))
                .outputItems(dust, Agar, 1)
                .duration(420).EUt(VA[MV])
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_cadmium_battery")).inputItems(BATTERY_LV_CADMIUM)
                .outputItems(BATTERY_HULL_LV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_lithium_battery")).inputItems(BATTERY_LV_LITHIUM)
                .outputItems(BATTERY_HULL_LV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_sodium_battery")).inputItems(BATTERY_LV_SODIUM)
                .outputItems(BATTERY_HULL_LV).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_cadmium_battery")).inputItems(BATTERY_MV_CADMIUM)
                .outputItems(BATTERY_HULL_MV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_lithium_battery")).inputItems(BATTERY_MV_LITHIUM)
                .outputItems(BATTERY_HULL_MV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_sodium_battery")).inputItems(BATTERY_MV_SODIUM)
                .outputItems(BATTERY_HULL_MV).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_cadmium_battery")).inputItems(BATTERY_HV_CADMIUM)
                .outputItems(BATTERY_HULL_HV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_lithium_battery")).inputItems(BATTERY_HV_LITHIUM)
                .outputItems(BATTERY_HULL_HV).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_sodium_battery")).inputItems(BATTERY_HV_SODIUM)
                .outputItems(BATTERY_HULL_HV).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_ev_vanadium_battery")).inputItems(BATTERY_EV_VANADIUM)
                .outputItems(BATTERY_HULL_SMALL_VANADIUM).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_iv_vanadium_battery")).inputItems(BATTERY_IV_VANADIUM)
                .outputItems(BATTERY_HULL_MEDIUM_VANADIUM).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_luv_vanadium_battery")).inputItems(BATTERY_LuV_VANADIUM)
                .outputItems(BATTERY_HULL_LARGE_VANADIUM).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_zpm_naquadria_battery")).inputItems(BATTERY_ZPM_NAQUADRIA)
                .outputItems(BATTERY_HULL_MEDIUM_NAQUADRIA).save();
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uv_naquadria_battery")).inputItems(BATTERY_UV_NAQUADRIA)
                .outputItems(BATTERY_HULL_LARGE_NAQUADRIA).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_ev_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_EV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asStack(1))
                .outputItems(LAPOTRON_CRYSTAL)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_iv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_IV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I)
                .outputItems(ENERGY_LAPOTRONIC_ORB)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_luv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_LuV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_LAPOTRONIC_ORB_CLUSTER)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_zpm_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_ZPM.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_MODULE)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_UV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ENERGY_CLUSTER)
                .duration(200).EUt(VA[LV]).save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uhv_ultimate_battery"))
                .inputItems(GTBlocks.BATTERY_ULTIMATE_UHV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ULTIMATE_BATTERY)
                .duration(200).EUt(VA[LV]).save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("wool_from_string"))
                .inputItems(new ItemStack(Items.STRING, 4))
                .circuitMeta(4)
                .outputItems(new ItemStack(Blocks.WHITE_WOOL))
                .duration(100).EUt(4).save();
    }
}
