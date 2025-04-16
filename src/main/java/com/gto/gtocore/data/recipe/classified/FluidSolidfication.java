package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.tags.TagKey;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

import static com.gregtechceu.gtceu.api.GTValues.L;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Steel;
import static com.gto.gtocore.common.data.GTOItems.*;
import static com.gto.gtocore.common.data.GTOItems.DISPOSABLE_SAW_MOLD;
import static com.gto.gtocore.common.data.GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES;

interface FluidSolidfication {

    static void init() {
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("memory_foam_block"))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK.asItem())
                .inputFluids(GTOMaterials.ViscoelasticPolyurethaneFoam.getFluid(1000))
                .outputItems(GTOItems.MEMORY_FOAM_BLOCK.asItem())
                .EUt(30)
                .duration(60)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("lumin_essence_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.HighEnergyMixture, 2)
                .inputFluids(GTMaterials.PhosphoricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.LuminEssence)
                .EUt(480)
                .duration(200)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("pellet_antimatter"))
                .notConsumable(GTOItems.BALL_FIELD_SHAPE.asItem())
                .inputFluids(GTOMaterials.Antimatter.getFluid(1000))
                .outputItems(GTOItems.PELLET_ANTIMATTER.asItem())
                .EUt(491520)
                .duration(800)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("kevlar_fiber"))
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET.asItem())
                .inputFluids(GTOMaterials.LiquidCrystalKevlar.getFluid(72))
                .outputItems(GTOItems.KEVLAR_FIBER.asItem())
                .EUt(30)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("xenoxene_crystal_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Perlite, 3)
                .inputFluids(GTOMaterials.XenoxeneMixture.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.XenoxeneCrystal, 3)
                .EUt(1920)
                .duration(200)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium_plate"))
                .notConsumable(GTItems.SHAPE_MOLD_PLATE.asItem())
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.LIQUID, 144))
                .outputItems(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .EUt(7)
                .duration(400)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("rhodium_plated_palladium"))
                .inputItems(TagPrefix.ingotHot, GTMaterials.Palladium, 3)
                .inputFluids(GTMaterials.Rhodium.getFluid(144))
                .outputItems(TagPrefix.ingotHot, GTMaterials.RhodiumPlatedPalladium, 4)
                .EUt(7680)
                .duration(800)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("superheavy_mix"))
                .outputItems(TagPrefix.dust, GTOMaterials.SuperheavyMix)
                .inputFluids(GTOMaterials.SuperheavyMix.getFluid(144))
                .EUt(122880)
                .duration(800)
                .save();

        Map<ItemEntry, TagKey> toolToMoldMap = Map.of(
                DISPOSABLE_FILE_MOLD, CustomTags.FILES,
                DISPOSABLE_WRENCH_MOLD, CustomTags.WRENCHES,
                DISPOSABLE_CROWBAR_MOLD, CustomTags.CROWBARS,
                DISPOSABLE_WIRE_CUTTER_MOLD, CustomTags.WIRE_CUTTERS,
                DISPOSABLE_HAMMER_MOLD, CustomTags.HAMMERS,
                DISPOSABLE_MALLET_MOLD, CustomTags.MALLETS,
                DISPOSABLE_SCREWDRIVER_MOLD, CustomTags.SCREWDRIVERS,
                DISPOSABLE_SAW_MOLD, CustomTags.SAWS);
//        TODO: 配方有问题
        for (Map.Entry<ItemEntry, TagKey> disposableMold : toolToMoldMap.entrySet()) {
            TagKey tagKey = disposableMold.getValue();
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("disposable_mold_" + disposableMold.getKey()))
                    .inputItems(tagKey)
                    .inputFluids(GTOMaterials.SuperheavyMix.getFluid(144))
                    .outputItems(disposableMold.getKey().asItem())
                    .EUt(30)
                    .duration(800)
                    .save();
        }
    }
}
