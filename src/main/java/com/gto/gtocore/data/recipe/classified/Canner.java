package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import dev.shadowsoffire.apotheosis.ench.Ench;

import java.util.function.Consumer;

interface Canner {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("reactor_uranium_simple"))
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238, 16)
                .inputFluids(GTMaterials.Uranium235.getFluid(96))
                .outputItems(GTOItems.REACTOR_URANIUM_SIMPLE.asStack())
                .EUt(1920)
                .duration(120)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("sculk"))
                .inputItems(new ItemStack(Blocks.DIRT.asItem()))
                .inputItems(new ItemStack(Blocks.SCULK_VEIN.asItem()))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 10))
                .outputItems(TagPrefix.block, GTMaterials.Sculk)
                .EUt(480)
                .duration(600)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("dense_neutron_plasma_cell"))
                .notConsumable(GTOTagPrefix.nanites, GTMaterials.Neutronium)
                .inputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asStack())
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.DENSE_NEUTRON_PLASMA_CELL.asStack())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("reactor_mox_simple"))
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238, 18)
                .inputFluids(GTMaterials.Plutonium239.getFluid(432))
                .outputItems(GTOItems.REACTOR_MOX_SIMPLE.asStack())
                .EUt(7680)
                .duration(120)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("reactor_thorium_simple"))
                .inputItems(GTOItems.REACTOR_FUEL_ROD.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Thorium, 12)
                .outputItems(GTOItems.REACTOR_THORIUM_SIMPLE.asStack())
                .EUt(480)
                .duration(120)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("infused_breath"))
                .inputItems(new ItemStack(Items.DRAGON_BREATH.asItem()))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 1000))
                .outputItems(Ench.Items.INFUSED_BREATH.get(), 3)
                .EUt(480)
                .duration(400)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("crystal_matrix_plasma_containment_cell"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Enderium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL.asStack())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("reactor_naquadah_simple"))
                .inputItems(GTOItems.TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 4)
                .inputFluids(GTMaterials.Thorium.getFluid(144))
                .outputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE.asStack())
                .EUt(30720)
                .duration(120)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("rhenium_plasma_containment_cell"))
                .notConsumable(GTOTagPrefix.nanites, GTMaterials.Naquadah)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL.asStack())
                .EUt(30720)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("chaos_containment_unit"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.CosmicNeutronium)
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .inputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.CHAOS_CONTAINMENT_UNIT.asStack())
                .EUt(503316480)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("bose_einstein_cooling_container"))
                .notConsumable(GTOTagPrefix.nanites, GTMaterials.Iridium)
                .inputItems(GTOItems.EMPTY_LASER_COOLING_CONTAINER.asStack())
                .inputFluids(GTMaterials.Rubidium.getFluid(288))
                .outputItems(GTOItems.BOSE_EINSTEIN_COOLING_CONTAINER.asStack())
                .EUt(90000)
                .duration(280)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("actinium_superhydride_plasma_containment_cell"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Infuscolium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL.asStack())
                .EUt(31457280)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("awakened_draconium_plasma_containment_cell"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Draconium)
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL.asStack())
                .EUt(125829120)
                .duration(20)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("charged_triplet_neutronium_sphere"))
                .notConsumable(GTOTagPrefix.nanites, GTMaterials.Neutronium)
                .inputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .inputFluids(GTOMaterials.FreeAlphaGas.getFluid(1000))
                .outputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .EUt(500000)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CANNER_RECIPES.recipeBuilder(GTOCore.id("cosmic_mesh_containment_unit"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Uruium)
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_MESH_CONTAINMENT_UNIT.asStack())
                .EUt(503316480)
                .duration(20)
                .save(provider);
    }
}
