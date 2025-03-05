package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.recipe.condition.GravityCondition;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import dev.shadowsoffire.apotheosis.ench.Ench;

interface Extractor {

    static void init() {
        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("tannic"))
                .inputItems(new ItemStack(Blocks.NETHER_WART_BLOCK.asItem()))
                .outputFluids(GTOMaterials.Tannic.getFluid(50))
                .EUt(30)
                .duration(200)
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("xpjuice"))
                .inputItems(TagPrefix.block, GTMaterials.Sculk)
                .outputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 100))
                .EUt(120)
                .duration(20)
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("milk"))
                .inputItems(new ItemStack(Items.MILK_BUCKET.asItem()))
                .outputItems(new ItemStack(Items.BUCKET.asItem()))
                .outputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(16)
                .duration(60)
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("tcetieseaweedextract"))
                .inputItems(GTOItems.TCETIEDANDELIONS.asStack(64))
                .outputItems(GTOItems.TCETIESEAWEEDEXTRACT.asStack())
                .EUt(16)
                .duration(200)
                .addCondition(new GravityCondition(false))
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("milk1"))
                .notConsumable(new ItemStack(Items.COW_SPAWN_EGG.asItem()))
                .outputFluids(GTMaterials.Milk.getFluid(100))
                .EUt(30)
                .duration(20)
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("bones"))
                .inputItems(new ItemStack(Blocks.DIRT.asItem()))
                .chancedOutput(TagPrefix.rod, GTMaterials.Bone, 25, 0)
                .EUt(16)
                .duration(100)
                .save();

        GTORecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("dragon_breath"))
                .inputItems(Ench.Items.INFUSED_BREATH.get(), 3)
                .outputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputFluids(GTOMaterials.DragonBreath.getFluid(1000))
                .EUt(30)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();
    }
}
