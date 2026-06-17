package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.IMPLOSION_RECIPES;

final class ImplosionCompressor {

    public static void init() {
        var neutron_nugget = ChemicalHelper.getItem(TagPrefix.dust, GTOMaterials.Neutron);

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_tnt")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(GTOItems.TWO_WAY_FOIL)
                .inputItems(Blocks.TNT.asItem(), 4)
                .outputItems(GTOItems.COMMAND_BLOCK_CORE)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_tnt")
                .inputItems(GTOItems.NEUTRON_PILE.get(), 1024)
                .inputItems(Blocks.TNT.asItem(), 4)
                .outputItems(neutron_nugget)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_dynamite")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTItems.DYNAMITE, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_dynamite")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(GTOItems.TWO_WAY_FOIL)
                .inputItems(GTItems.DYNAMITE, 2)
                .outputItems(GTOItems.COMMAND_BLOCK_CORE)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_powderbarrel")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(GTOItems.TWO_WAY_FOIL)
                .inputItems(GTBlocks.POWDERBARREL.asItem(), 8)
                .outputItems(GTOItems.COMMAND_BLOCK_CORE)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_powderbarrel")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTBlocks.POWDERBARREL.asItem(), 8)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_dynamite")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTItems.DYNAMITE, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_itnt")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_powderbarrel")
                .inputItems(AEItems.SINGULARITY.asItem())
                .inputItems(GTOItems.WARPED_ENDER_PEARL)
                .inputItems(GTBlocks.POWDERBARREL.asItem(), 8)
                .outputItems(GTOItems.ENTANGLED_SINGULARITY)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_itnt")
                .inputItems(AEItems.SINGULARITY.asItem())
                .inputItems(GTOItems.WARPED_ENDER_PEARL)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.ENTANGLED_SINGULARITY)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_tnt")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(Blocks.TNT.asItem(), 4)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_powderbarrel")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTBlocks.POWDERBARREL.asItem(), 8)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_3_itnt")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_powderbarrel")
                .inputItems(GTOItems.NEUTRON_PILE.get(), 1024)
                .inputItems(GTBlocks.POWDERBARREL.asItem(), 8)
                .outputItems(neutron_nugget)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_itnt")
                .inputItems(GTOItems.NEUTRON_PILE.get(), 1024)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(neutron_nugget)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_dynamite")
                .inputItems(AEItems.SINGULARITY.asItem())
                .inputItems(GTOItems.WARPED_ENDER_PEARL)
                .inputItems(GTItems.DYNAMITE, 2)
                .outputItems(GTOItems.ENTANGLED_SINGULARITY)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("neutron_nugget_dynamite")
                .inputItems(GTOItems.NEUTRON_PILE.get(), 1024)
                .inputItems(GTItems.DYNAMITE, 2)
                .outputItems(neutron_nugget)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("entangled_singularity_tnt")
                .inputItems(AEItems.SINGULARITY.asItem())
                .inputItems(GTOItems.WARPED_ENDER_PEARL)
                .inputItems(Blocks.TNT.asItem(), 4)
                .outputItems(GTOItems.ENTANGLED_SINGULARITY)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("heavy_duty_plate_1_tnt")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .inputItems(Blocks.TNT.asItem(), 4)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        IMPLOSION_RECIPES.recipeBuilder("command_block_core_itnt")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(GTOItems.TWO_WAY_FOIL)
                .inputItems(GTBlocks.INDUSTRIAL_TNT.asItem())
                .outputItems(GTOItems.COMMAND_BLOCK_CORE)
                .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
                .EUt(30)
                .duration(20)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("eternal_singularity")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Eternity, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.SpaceTime)
                .outputItems(GTOItems.INFINITY_SINGULARITY.get())
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("command_block_core")
                .inputItems(Blocks.COMMAND_BLOCK.asItem())
                .inputItems(GTOItems.TWO_WAY_FOIL)
                .outputItems(GTOItems.COMMAND_BLOCK_CORE)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("neutron_nugget")
                .inputItems(GTOItems.NEUTRON_PILE.get(), 1024)
                .outputItems(neutron_nugget)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("heavy_duty_plate_3")
                .inputItems(GTOItems.HEAVY_DUTY_PLATE_2, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.TungstenSteel, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_3)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("heavy_duty_plate_1")
                .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 4)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 2)
                .outputItems(GTOItems.HEAVY_DUTY_PLATE_1)
                .save();

        ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder("entangled_singularity")
                .inputItems(AEItems.SINGULARITY.asItem())
                .inputItems(GTOItems.WARPED_ENDER_PEARL)
                .outputItems(GTOItems.ENTANGLED_SINGULARITY)
                .save();
    }
}
