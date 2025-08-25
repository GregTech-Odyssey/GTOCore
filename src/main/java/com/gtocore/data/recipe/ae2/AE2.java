package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public class AE2 {

    public static void init() {
        if (GTOCore.isExpert()) {

            ASSEMBLER_RECIPES.builder("blank_pattern_less")
                    .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 8)
                    .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 5)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 16)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(1))
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem()))
                    .EUt(GTValues.VA[2])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern")
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 8)
                    .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 5)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 16)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(2))
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 2))
                    .EUt(GTValues.VA[3])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern_better")
                    .inputItems(TagPrefix.plate, GTMaterials.TungstenSteel, 8)
                    .inputItems(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene, 5)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 32)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(4))
                    .inputItems(CustomTags.IV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 8))
                    .EUt(GTValues.VA[5])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern_best")
                    .inputItems(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium, 8)
                    .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole, 5)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 64)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 64)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(8))
                    .inputItems(CustomTags.LuV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 64))
                    .EUt(GTValues.VA[6])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("pattern_provider_magic")
                    .inputItems(new ItemStack(AEItems.FORMATION_CORE.asItem()))
                    .inputItems(new ItemStack(AEItems.ANNIHILATION_CORE.asItem()))
                    .inputItems(GTItems.ROBOT_ARM_HV.asStack(2))
                    .inputItems(TagPrefix.frameGt, GTMaterials.StainlessSteel, 1)
                    .inputItems(TagPrefix.plate, GTOMaterials.Alfsteel, 6)
                    .inputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                    .outputItems(AEBlocks.PATTERN_PROVIDER.block().asItem())
                    .inputFluids(GTMaterials.PolyvinylChloride, 576)
                    .EUt(480)
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("pattern_provider")
                    .inputItems(new ItemStack(AEItems.FORMATION_CORE.asItem()))
                    .inputItems(new ItemStack(AEItems.ANNIHILATION_CORE.asItem()))
                    .inputItems(GTItems.ROBOT_ARM_HV.asStack(2))
                    .inputItems(TagPrefix.frameGt, GTMaterials.StainlessSteel, 1)
                    .inputItems(TagPrefix.plate, GTMaterials.Titanium, 6)
                    .inputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                    .outputItems(AEBlocks.PATTERN_PROVIDER.block().asItem())
                    .inputFluids(GTMaterials.PolyvinylChloride, 576)
                    .EUt(480)
                    .duration(100)
                    .save();

        } else {
            ASSEMBLER_RECIPES.builder("blank_pattern_less")
                    .inputItems(TagPrefix.plate, GTOMaterials.Manasteel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.Polyethylene, 2)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 8)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(1))
                    .inputItems(CustomTags.LV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem()))
                    .EUt(GTValues.VA[2])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern")
                    .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.PolyvinylChloride, 2)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 16)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(2))
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 4))
                    .EUt(GTValues.VA[3])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern_better")
                    .inputItems(TagPrefix.plate, GTMaterials.TungstenSteel, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.Polytetrafluoroethylene, 2)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 32)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(4))
                    .inputItems(CustomTags.IV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 16))
                    .EUt(GTValues.VA[5])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("blank_pattern_best")
                    .inputItems(TagPrefix.plate, GTMaterials.RhodiumPlatedPalladium, 3)
                    .inputItems(TagPrefix.plate, GTMaterials.Polybenzimidazole, 2)
                    .inputItems(TagPrefix.foil, GTMaterials.Aluminium, 64)
                    .inputItems(GTItems.NAND_MEMORY_CHIP.asStack(8))
                    .inputItems(CustomTags.LuV_CIRCUITS)
                    .outputItems(new ItemStack(AEItems.BLANK_PATTERN.asItem(), 64))
                    .EUt(GTValues.VA[6])
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("pattern_provider")
                    .inputItems(new ItemStack(AEItems.FORMATION_CORE.asItem()))
                    .inputItems(new ItemStack(AEItems.ANNIHILATION_CORE.asItem()))
                    .inputItems(GTItems.ROBOT_ARM_MV.asStack(2))
                    .inputItems(TagPrefix.frameGt, GTMaterials.Aluminium)
                    .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 6)
                    .inputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                    .outputItems(AEBlocks.PATTERN_PROVIDER.block().asItem())
                    .inputFluids(GTMaterials.PolyvinylChloride, 576)
                    .EUt(480)
                    .duration(100)
                    .save();

            ASSEMBLER_RECIPES.builder("pattern_provider_magic")
                    .inputItems(new ItemStack(AEItems.FORMATION_CORE.asItem()))
                    .inputItems(new ItemStack(AEItems.ANNIHILATION_CORE.asItem()))
                    .inputItems(GTItems.ROBOT_ARM_HV.asStack(1))
                    .inputItems(TagPrefix.frameGt, GTMaterials.StainlessSteel, 1)
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 6)
                    .inputItems(new ItemStack(AEItems.ENGINEERING_PROCESSOR.asItem()))
                    .outputItems(AEBlocks.PATTERN_PROVIDER.block().asItem(), 4)
                    .inputFluids(GTMaterials.PolyvinylChloride, 576)
                    .EUt(480)
                    .duration(100)
                    .save();
        }
    }
}
