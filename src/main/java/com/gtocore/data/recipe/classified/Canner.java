package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import dev.shadowsoffire.apotheosis.ench.Ench;

import static com.gtocore.common.data.GTORecipeTypes.CANNER_RECIPES;

final class Canner {

    public static void init() {
        CANNER_RECIPES.recipeBuilder("ender_air")
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .inputFluids(GTMaterials.EnderAir.getFluid(1000))
                .outputItems("botania:ender_air_bottle")
                .EUt(30)
                .duration(60)
                .save();

        CANNER_RECIPES.recipeBuilder("reactor_uranium_simple")
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238, 16)
                .inputFluids(GTMaterials.Uranium235.getFluid(96))
                .outputItems(GTOItems.REACTOR_URANIUM_SIMPLE.asItem())
                .EUt(1920)
                .duration(120)
                .save();

        CANNER_RECIPES.recipeBuilder("sculk")
                .inputItems(new ItemStack(Blocks.DIRT.asItem()))
                .inputItems(new ItemStack(Blocks.SCULK_VEIN.asItem()))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 10))
                .outputItems(TagPrefix.block, GTMaterials.Sculk)
                .EUt(480)
                .duration(600)
                .save();

        CANNER_RECIPES.recipeBuilder("dense_neutron_plasma_cell")
                .notConsumable(GTOTagPrefix.NANITES, GTMaterials.Neutronium)
                .inputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asItem())
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.DENSE_NEUTRON_PLASMA_CELL.asItem())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("reactor_mox_simple")
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238, 18)
                .inputFluids(GTMaterials.Plutonium239.getFluid(432))
                .outputItems(GTOItems.REACTOR_MOX_SIMPLE.asItem())
                .EUt(7680)
                .duration(120)
                .save();

        CANNER_RECIPES.recipeBuilder("reactor_thorium_simple")
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Thorium, 12)
                .outputItems(GTOItems.REACTOR_THORIUM_SIMPLE.asItem())
                .EUt(480)
                .duration(120)
                .save();

        CANNER_RECIPES.recipeBuilder("infused_breath")
                .inputItems(new ItemStack(Items.DRAGON_BREATH.asItem()))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 1000))
                .outputItems(Ench.Items.INFUSED_BREATH.get(), 3)
                .EUt(480)
                .duration(400)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("crystal_matrix_plasma_containment_cell")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Enderium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL.asItem())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("reactor_naquadah_simple")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 4)
                .inputFluids(GTMaterials.Thorium.getFluid(144))
                .outputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE.asItem())
                .EUt(30720)
                .duration(120)
                .save();

        CANNER_RECIPES.recipeBuilder("rhenium_plasma_containment_cell")
                .notConsumable(GTOTagPrefix.NANITES, GTMaterials.Naquadah)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL.asItem())
                .EUt(30720)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("chaos_containment_unit")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.CosmicNeutronium)
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem())
                .inputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.CHAOS_CONTAINMENT_UNIT.asItem())
                .EUt(503316480)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("bose_einstein_cooling_container")
                .notConsumable(GTOTagPrefix.NANITES, GTMaterials.Iridium)
                .inputItems(GTOItems.EMPTY_LASER_COOLING_CONTAINER.asItem())
                .inputFluids(GTMaterials.Rubidium.getFluid(288))
                .outputItems(GTOItems.BOSE_EINSTEIN_COOLING_CONTAINER.asItem())
                .EUt(90000)
                .duration(280)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("actinium_superhydride_plasma_containment_cell")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Infuscolium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL.asItem())
                .EUt(31457280)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("awakened_draconium_plasma_containment_cell")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Draconium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL.asItem())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("charged_triplet_neutronium_sphere")
                .notConsumable(GTOTagPrefix.NANITES, GTMaterials.Neutronium)
                .inputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asItem())
                .inputFluids(GTOMaterials.FreeAlphaGas.getFluid(1000))
                .outputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asItem())
                .EUt(500000)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CANNER_RECIPES.recipeBuilder("cosmic_mesh_containment_unit")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Uruium)
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem())
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_MESH_CONTAINMENT_UNIT.asItem())
                .EUt(503316480)
                .duration(20)
                .save();

        CANNER_RECIPES.builder("coolant_cell_10k")
                .inputItems(GTItems.FLUID_CELL.asStack())
                .outputItems(GTOItems.COOLANT_CELL_10K.asItem())
                .inputFluids(GTOMaterials.CoolantLiquid, 1000)
                .EUt(30)
                .duration(200)
                .save();

        CANNER_RECIPES.builder("vanilla_ink_sac")
                .inputItems(Items.LEATHER)
                .inputFluids(GTMaterials.DyeBlack.getFluid(144))
                .outputItems(Items.INK_SAC)
                .EUt(30)
                .duration(100)
                .save();

        CANNER_RECIPES.builder("vanilla_ink_sac2")
                .inputItems(Items.PHANTOM_MEMBRANE)
                .inputFluids(GTMaterials.DyeBlack.getFluid(144))
                .outputItems(Items.INK_SAC)
                .EUt(30)
                .duration(100)
                .save();

        CANNER_RECIPES.builder("vanilla_ink_sac3")
                .inputItems(ItemsRegistry.WILDEN_WING.asItem())
                .inputFluids(GTMaterials.DyeBlack.getFluid(144))
                .outputItems(Items.INK_SAC)
                .EUt(30)
                .duration(100)
                .save();

        CANNER_RECIPES.builder("thorium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Thorium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Thorium)
                .inputFluids(GTMaterials.Thorium, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("protactinium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Protactinium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Protactinium)
                .inputFluids(GTMaterials.Protactinium, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("uranium_235_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium235, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Uranium235)
                .inputFluids(GTMaterials.Uranium235, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("uranium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Uranium238)
                .inputFluids(GTMaterials.Uranium238, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("neptunium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Neptunium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Neptunium)
                .inputFluids(GTMaterials.Neptunium, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("plutonium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium239, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Plutonium239)
                .inputFluids(GTMaterials.Plutonium239, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("plutonium_241_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium241, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Plutonium241)
                .inputFluids(GTMaterials.Plutonium241, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("americium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Americium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Americium)
                .inputFluids(GTMaterials.Americium, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("berkelium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Berkelium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Berkelium)
                .inputFluids(GTMaterials.Berkelium, 576)
                .EUt(30720)
                .duration(160)
                .save();

        CANNER_RECIPES.builder("curium_breeder_rod")
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Curium, 12)
                .outputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Curium)
                .inputFluids(GTMaterials.Curium, 576)
                .EUt(30720)
                .duration(160)
                .save();
    }
}
