package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import appeng.api.stacks.AEItemKey;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public class GTOInfCells {

    public static final Block[] CONCRETE_BLOCKS = new Block[] {
            Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE,
            Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE,
            Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE,
            Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE
    };

    static void init() {
        for (var i = 0; i < 16; ++i) {
            ASSEMBLER_RECIPES.builder("concrete_infinity_cell_" + i)
                    .inputItems(GTOItems.CELL_COMPONENT_1M.asItem())
                    .inputItems(GTMachines.ROCK_CRUSHER[GTValues.EV].asItem(), 4)
                    .inputItems(GTMachines.MACERATOR[GTValues.EV].asItem(), 4)
                    .inputItems(GTItems.COVER_INFINITE_WATER.asItem(), 4)
                    .outputItems(infCell(CONCRETE_BLOCKS[i]))
                    .inputFluids(GTMaterials.CHEMICAL_DYES[i], 64 * 144)
                    .duration(400)
                    .euVATier(GTValues.EV)
                    .save();
        }
        ASSEMBLER_RECIPES.builder("infinity_cell")
                .inputItems(GTOItems.CELL_COMPONENT_1M.asItem())
                .inputItems(GTMachines.ROCK_CRUSHER[GTValues.EV].asItem(), 4)
                .inputItems("easy_villagers:iron_farm", 64)
                .inputItems("easy_villagers:villager", 6)
                .outputItems(infCell(RegistriesUtils.getItemStack("factory_blocks:factory").getItem()))
                .duration(400)
                .euVATier(GTValues.EV)
                .save();
    }

    static ItemStack infCell(ItemLike item) {
        return EPPItemAndBlock.INFINITY_CELL.getRecordCell(AEItemKey.of(item));
    }
}
