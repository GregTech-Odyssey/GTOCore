package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.GTOCleanroomType;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import dev.shadowsoffire.apotheosis.ench.Ench;

import static com.gtocore.common.data.GTOMaterials.TheWaterFromTheWellOfWisdom;
import static com.gtocore.common.data.GTORecipeTypes.EXTRACTOR_RECIPES;

final class Extractor {

    public static void init() {
        EXTRACTOR_RECIPES.recipeBuilder("tannic")
                .inputItems(Blocks.NETHER_WART_BLOCK.asItem())
                .outputFluids(GTOMaterials.Tannic.getFluid(50))
                .EUt(30)
                .duration(200)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("xpjuice")
                .inputItems(TagPrefix.block, GTMaterials.Sculk)
                .outputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 100))
                .EUt(120)
                .duration(20)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("life_essence")
                .inputItems(Items.EXPERIENCE_BOTTLE)
                .outputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 250))
                .EUt(8)
                .duration(20)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("milk")
                .inputItems(Items.MILK_BUCKET.asItem())
                .outputItems(Items.BUCKET.asItem())
                .outputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(16)
                .duration(60)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("tcetieseaweedextract")
                .inputItems(GTOItems.TCETIEDANDELIONS.asItem(), 64)
                .outputItems(GTOItems.TCETIESEAWEEDEXTRACT.asItem())
                .EUt(16)
                .duration(200)
                .addCondition(new GravityCondition(false))
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("bones")
                .inputItems(Blocks.DIRT.asItem())
                .chancedOutput(TagPrefix.rod, GTMaterials.Bone, 25, 0)
                .EUt(16)
                .duration(100)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("dragon_breath")
                .inputItems(Ench.Items.INFUSED_BREATH.get(), 3)
                .outputItems(Items.GLASS_BOTTLE.asItem())
                .outputFluids(GTOMaterials.DragonBreath.getFluid(1000))
                .EUt(30)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("tin")
                .inputItems(TagPrefix.dust, GTMaterials.Tin)
                .outputFluids(GTMaterials.Tin.getFluid(144))
                .duration(240)
                .EUt(30)
                .heat(600)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("algae")
                .inputItems(TagUtils.createTag(GTOCore.id("algae")))
                .outputItems(TagPrefix.dust, GTOMaterials.AlgaeExtract)
                .duration(120)
                .EUt(30)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("liquid_sunshine")
                .inputItems(TagPrefix.dust, GTMaterials.Glowstone)
                .outputFluids(new FluidStack(EIOFluids.LIQUID_SUNSHINE.getSource(), 100))
                .EUt(120)
                .duration(400)
                .daytime()
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("blood")
                .inputItems(TagPrefix.dust, GTMaterials.Meat)
                .outputFluids(GTOMaterials.Blood.getFluid(100))
                .EUt(120)
                .duration(50)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("chitin")
                .inputItems(TagUtils.createTag(RLUtils.forge("mushrooms")))
                .outputFluids(GTOMaterials.Chitin.getFluid(100))
                .EUt(30)
                .duration(100)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("honey")
                .inputItems(Items.HONEY_BOTTLE)
                .outputItems(Items.GLASS_BOTTLE)
                .outputFluids(GTOMaterials.Honey.getFluid(250))
                .EUt(30)
                .duration(20)
                .save();

        EXTRACTOR_RECIPES.recipeBuilder("the_water_from_the_well_of_wisdom")
                .inputItems("mythicbotany:gjallar_horn_full")
                .outputItems("mythicbotany:gjallar_horn_empty")
                .outputFluids(TheWaterFromTheWellOfWisdom.getFluid(250))
                .EUt(8)
                .duration(20)
                .save();

        EXTRACTOR_RECIPES.builder("overworld_marker")
                .inputItems("ad_astra:earth_globe")
                .outputItems(new ItemStack(RegistriesUtils.getItem("gtceu:overworld_marker")).setHoverName(Component.translatable("item.gtocore.globe.earth")))
                .EUt(30)
                .duration(15000)
                .save();
    }
}
