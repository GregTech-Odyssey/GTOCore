package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface NeutronCompressor {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_5"))
                .inputItems(TagPrefix.block, GTMaterials.HastelloyX, 64)
                .inputItems(TagPrefix.block, GTMaterials.RedSteel, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_5.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_6"))
                .inputItems(TagPrefix.block, GTOMaterials.HighDurabilityCompoundSteel, 64)
                .inputItems(TagPrefix.block, GTOMaterials.GermaniumTungstenNitride, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_6.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_7"))
                .inputItems(TagPrefix.block, GTMaterials.HSSE, 64)
                .inputItems(TagPrefix.block, GTMaterials.WatertightSteel, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_7.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_8"))
                .inputItems(TagPrefix.block, GTOMaterials.Pikyonium, 64)
                .inputItems(TagPrefix.block, GTOMaterials.AluminiumBronze, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_8.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_9"))
                .inputItems(TagPrefix.block, GTOMaterials.AbyssalAlloy, 64)
                .inputItems(TagPrefix.block, GTMaterials.SolderingAlloy, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_9.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_0"))
                .inputItems(TagPrefix.block, GTOMaterials.Lafium, 64)
                .inputItems(TagPrefix.block, GTMaterials.Potin, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_0.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_1"))
                .inputItems(TagPrefix.block, GTOMaterials.Enderite, 64)
                .inputItems(TagPrefix.block, GTMaterials.IndiumGalliumPhosphide, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_1.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_2"))
                .inputItems(TagPrefix.block, GTMaterials.RutheniumTriniumAmericiumNeutronate, 64)
                .inputItems(TagPrefix.block, GTMaterials.YttriumBariumCuprate, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_2.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_3"))
                .inputItems(TagPrefix.block, GTOMaterials.HastelloyK243, 64)
                .inputItems(TagPrefix.block, GTMaterials.CobaltBrass, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_3.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_4"))
                .inputItems(TagPrefix.block, GTOMaterials.TitanSteel, 64)
                .inputItems(TagPrefix.block, GTMaterials.UraniumRhodiumDinaquadide, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_4.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_12"))
                .inputItems(TagPrefix.block, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 64)
                .inputItems(TagPrefix.block, GTMaterials.RTMAlloy, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_12.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_13"))
                .inputItems(TagPrefix.block, GTMaterials.BlueSteel, 64)
                .inputItems(TagPrefix.block, GTMaterials.HastelloyC276, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_13.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_14"))
                .inputItems(TagPrefix.block, GTOMaterials.Cinobite, 64)
                .inputItems(TagPrefix.block, GTMaterials.Stellite100, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_14.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_15"))
                .inputItems(TagPrefix.block, GTMaterials.MaragingSteel300, 64)
                .inputItems(TagPrefix.block, GTOMaterials.Grisium, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_15.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_10"))
                .inputItems(TagPrefix.block, GTOMaterials.BlackTitanium, 64)
                .inputItems(TagPrefix.block, GTMaterials.NickelZincFerrite, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_10.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("combined_singularity_11"))
                .inputItems(TagPrefix.block, GTMaterials.Ultimet, 64)
                .inputItems(TagPrefix.block, GTMaterials.HSLASteel, 64)
                .outputItems(GTOItems.COMBINED_SINGULARITY_11.asStack())
                .EUt(2013265920)
                .duration(200)
                .save(provider);
    }
}
