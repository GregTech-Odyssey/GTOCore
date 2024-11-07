package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.config.GTOConfigHolder;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.AUTOCLAVE_RECIPES;
import static com.gto.gtocore.common.data.GTOMaterials.WaterAgarMix;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;
import static com.gto.gtocore.common.data.machines.MultiBlockMachineB.PRIMITIVE_VOID_ORE;

public class MiscRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        if (GTOConfigHolder.INSTANCE.enablePrimitiveVoidOre) {
            VanillaRecipeHelper.addShapedRecipe(provider, true, GTOCore.id("primitive_void_ore_recipes"),
                    PRIMITIVE_VOID_ORE.asStack(), "DCD", "CGC", "DCD",
                    'D', Blocks.DIRT.asItem(),
                    'C', Items.STONE_PICKAXE.asItem(),
                    'G', new UnificationEntry(TagPrefix.block, GTMaterials.Iron));
            PRIMITIVE_VOID_ORE_RECIPES.recipeBuilder(GTOCore.id("primitive_void_ore_recipes"))
                    .inputFluids(GTMaterials.Steam.getFluid(1000))
                    .duration(200)
                    .save(provider);
        }

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("iron_bucket"), new ItemStack(Items.BUCKET), "ChC", " X ", 'X',
                new UnificationEntry(TagPrefix.plate, GTMaterials.Iron), 'C', new UnificationEntry(GTOTagPrefix.curvedPlate, GTMaterials.Iron));

        VanillaRecipeHelper.addBlastingRecipe(provider, GTOCore.id("hot_iron_ingot"), ChemicalHelper.getTag(ingot, Iron), GTOItems.HOT_IRON_INGOT.asStack(), 0);

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("wrought_iron_ingot"), ChemicalHelper.get(ingot, WroughtIron), "h", "H", 'H', GTOItems.HOT_IRON_INGOT.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("raw_vacuum_tube"), GTOItems.RAW_VACUUM_TUBE.asStack(),
                "PTP", "WWW",
                'P', new UnificationEntry(bolt, Steel),
                'T', GLASS_TUBE.asStack(),
                'W', new UnificationEntry(wireGtSingle, Copper));

        PHOTOVOLTAIC_POWER_RECIPES.recipeBuilder(GTOCore.id("a"))
                .circuitMeta(1)
                .duration(20)
                .save(provider);

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("a"))
                .notConsumable(pipeHugeFluid, Bronze)
                .EUt(7).duration(200)
                .addData("tier", 0)
                .save(provider);

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("b"))
                .notConsumable(FLUID_REGULATOR_LV)
                .EUt(30).duration(200)
                .addData("tier", 1)
                .save(provider);

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("c"))
                .notConsumable(FLUID_REGULATOR_MV)
                .EUt(120).duration(200)
                .addData("tier", 2)
                .save(provider);

        VACUUM_PUMP_RECIPES.recipeBuilder(GTOCore.id("d"))
                .notConsumable(FLUID_REGULATOR_HV)
                .EUt(480).duration(200)
                .addData("tier", 3)
                .save(provider);

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
                .save(provider);

        AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("water_agar_mix")).EUt(VA[HV]).duration(600)
                .inputItems(dust, Gelatin)
                .inputFluids(DistilledWater.getFluid(1000))
                .outputFluids(WaterAgarMix.getFluid(1000))
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save(provider);

        DEHYDRATOR_RECIPES.recipeBuilder(GTOCore.id("agar"))
                .inputFluids(WaterAgarMix.getFluid(1000))
                .outputItems(dust, Agar, 1)
                .duration(420).EUt(VA[MV])
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_cadmium_battery")).inputItems(BATTERY_LV_CADMIUM)
                .outputItems(BATTERY_HULL_LV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_lithium_battery")).inputItems(BATTERY_LV_LITHIUM)
                .outputItems(BATTERY_HULL_LV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_lv_sodium_battery")).inputItems(BATTERY_LV_SODIUM)
                .outputItems(BATTERY_HULL_LV).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_cadmium_battery")).inputItems(BATTERY_MV_CADMIUM)
                .outputItems(BATTERY_HULL_MV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_lithium_battery")).inputItems(BATTERY_MV_LITHIUM)
                .outputItems(BATTERY_HULL_MV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_mv_sodium_battery")).inputItems(BATTERY_MV_SODIUM)
                .outputItems(BATTERY_HULL_MV).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_cadmium_battery")).inputItems(BATTERY_HV_CADMIUM)
                .outputItems(BATTERY_HULL_HV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_lithium_battery")).inputItems(BATTERY_HV_LITHIUM)
                .outputItems(BATTERY_HULL_HV).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_hv_sodium_battery")).inputItems(BATTERY_HV_SODIUM)
                .outputItems(BATTERY_HULL_HV).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_ev_vanadium_battery")).inputItems(BATTERY_EV_VANADIUM)
                .outputItems(BATTERY_HULL_SMALL_VANADIUM).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_iv_vanadium_battery")).inputItems(BATTERY_IV_VANADIUM)
                .outputItems(BATTERY_HULL_MEDIUM_VANADIUM).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_luv_vanadium_battery")).inputItems(BATTERY_LUV_VANADIUM)
                .outputItems(BATTERY_HULL_LARGE_VANADIUM).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_zpm_naquadria_battery")).inputItems(BATTERY_ZPM_NAQUADRIA)
                .outputItems(BATTERY_HULL_MEDIUM_NAQUADRIA).save(provider);
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uv_naquadria_battery")).inputItems(BATTERY_UV_NAQUADRIA)
                .outputItems(BATTERY_HULL_LARGE_NAQUADRIA).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_ev_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_EV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I.asStack(1))
                .outputItems(LAPOTRON_CRYSTAL)
                .duration(200).EUt(VA[LV]).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_iv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_IV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_I)
                .outputItems(ENERGY_LAPOTRONIC_ORB)
                .duration(200).EUt(VA[LV]).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_luv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_LuV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_LAPOTRONIC_ORB_CLUSTER)
                .duration(200).EUt(VA[LV]).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_zpm_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_ZPM.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_II)
                .outputItems(ENERGY_MODULE)
                .duration(200).EUt(VA[LV]).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uv_lapotronic_battery"))
                .inputItems(GTBlocks.BATTERY_LAPOTRONIC_UV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ENERGY_CLUSTER)
                .duration(200).EUt(VA[LV]).save(provider);

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("unpackage_uhv_ultimate_battery"))
                .inputItems(GTBlocks.BATTERY_ULTIMATE_UHV.asStack(1))
                .outputItems(GTBlocks.BATTERY_EMPTY_TIER_III)
                .outputItems(ULTIMATE_BATTERY)
                .duration(200).EUt(VA[LV]).save(provider);

        LOOM_RECIPES.recipeBuilder(GTOCore.id("wool_from_string"))
                .inputItems(new ItemStack(Items.STRING, 4))
                .circuitMeta(4)
                .outputItems(new ItemStack(Blocks.WHITE_WOOL))
                .duration(100).EUt(4).save(provider);

        MAGIC_SYNTHESIS_RECIPES.recipeBuilder(GTOCore.id("magic_synthesis"))
                .inputItems(new ItemStack(Items.STRING, 4))
                .outputItems(new ItemStack(Blocks.WHITE_WOOL))
                .perTick(true)
                .input(ManaRecipeCapability.CAP, 1L)
                .perTick(false)
                .duration(100).save(provider);
    }
}
