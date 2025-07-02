package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;

import static com.gtocore.common.data.GTORecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.IMPLOSION_RECIPES;

final class ImplosionCompressor {

    public static void init() {
        IMPLOSION_RECIPES.recipeBuilder("diamond_lattice_tnt")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(ModItems.diamond_lattice.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_tnt")
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(GTOItems.TWO_WAY_FOIL.asItem())
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_tnt")
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(ModItems.neutron_nugget.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("crystal_matrix_itnt")
                .inputItems(ModBlocks.diamond_lattice_block.get().asItem())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(ModBlocks.crystal_matrix.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("diamond_lattice_dynamite")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(ModItems.diamond_lattice.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("double_compressed_crafting_table_powderbarrel")
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(ModBlocks.double_compressed_crafting_table.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("diamond_lattice_itnt")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(ModItems.diamond_lattice.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_dynamite")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("double_compressed_crafting_table_itnt")
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(ModBlocks.double_compressed_crafting_table.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_dynamite")
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(GTOItems.TWO_WAY_FOIL.asItem())
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_powderbarrel")
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(GTOItems.TWO_WAY_FOIL.asItem())
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_powderbarrel")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_dynamite")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack(4))
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_itnt")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_powderbarrel")
                .inputItems(new ItemStack(AEItems.SINGULARITY.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(GTOItems.ENTANGLED_SINGULARITY.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("crystal_matrix_tnt")
                .inputItems(ModBlocks.diamond_lattice_block.get().asItem())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(ModBlocks.crystal_matrix.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_itnt")
                .inputItems(new ItemStack(AEItems.SINGULARITY.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.ENTANGLED_SINGULARITY.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_tnt")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack(4))
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("diamond_lattice_powderbarrel")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(ModItems.diamond_lattice.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_powderbarrel")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack(4))
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("crystal_matrix_dynamite")
                .inputItems(ModBlocks.diamond_lattice_block.get().asItem())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(ModBlocks.crystal_matrix.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_itnt")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack(4))
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_powderbarrel")
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(ModItems.neutron_nugget.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_itnt")
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(ModItems.neutron_nugget.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("double_compressed_crafting_table_dynamite")
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(ModBlocks.double_compressed_crafting_table.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("double_compressed_crafting_table_tnt")
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(ModBlocks.double_compressed_crafting_table.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_dynamite")
                .inputItems(new ItemStack(AEItems.SINGULARITY.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(GTOItems.ENTANGLED_SINGULARITY.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_dynamite")
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(GTItems.DYNAMITE.asStack(2))
                .outputItems(ModItems.neutron_nugget.get())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_tnt")
                .inputItems(new ItemStack(AEItems.SINGULARITY.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(GTOItems.ENTANGLED_SINGULARITY.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_tnt")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(new ItemStack(Blocks.TNT.asItem(), 4))
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("crystal_matrix_powderbarrel")
                .inputItems(ModBlocks.diamond_lattice_block.get().asItem())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .inputItems(GTBlocks.POWDERBARREL.asStack(8))
                .outputItems(ModBlocks.crystal_matrix.get().asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_itnt")
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(GTOItems.TWO_WAY_FOIL.asItem())
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("double_compressed_crafting_table")
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .inputItems(new ItemStack(ModBlocks.compressed_crafting_table.get().asItem(), 64))
                .outputItems(ModBlocks.double_compressed_crafting_table.get().asItem())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("eternal_singularity")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Eternity, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.SpaceTime)
                .outputItems(ModItems.eternal_singularity.get())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("crystal_matrix")
                .inputItems(ModBlocks.diamond_lattice_block.get().asItem())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .outputItems(ModBlocks.crystal_matrix.get().asItem())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("command_block_core")
                .inputItems(new ItemStack(Blocks.COMMAND_BLOCK.asItem()))
                .inputItems(GTOItems.TWO_WAY_FOIL.asItem())
                .outputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("neutron_nugget")
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .inputItems(new ItemStack(ModItems.neutron_pile.get(), 64))
                .outputItems(ModItems.neutron_nugget.get())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("diamond_lattice")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .outputItems(ModItems.diamond_lattice.get())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("heavy_duty_plate_3")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2.asStack(4))
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3.asItem())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("heavy_duty_plate_1")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1.asItem())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("entangled_singularity")
                .inputItems(new ItemStack(AEItems.SINGULARITY.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asItem())
                .outputItems(GTOItems.ENTANGLED_SINGULARITY.asItem())
                .save();
    }
}
