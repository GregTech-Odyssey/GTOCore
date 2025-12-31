package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOItems;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.loading.FMLLoader;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public class GTOInfCells {

    public static final Block[] CONCRETE_BLOCKS = new Block[] {
            Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE,
            Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE,
            Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE,
            Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE
    };
    // will be set to null after emi loaded
    public static Set<ItemStack> AddedInfCells;

    static void init() {
        for (var i = 0; i < 16; ++i) {
            ASSEMBLER_RECIPES.builder("concrete_infinity_cell_" + i)
                    .inputItems(GTOItems.CELL_COMPONENT_1M)
                    .inputItems(GTMachines.ROCK_CRUSHER[GTValues.EV].asItem(), 4)
                    .inputItems(GTMachines.MACERATOR[GTValues.EV].asItem(), 4)
                    .inputItems(GTItems.COVER_INFINITE_WATER, 4)
                    .outputItems(infCell(CONCRETE_BLOCKS[i]))
                    .inputFluids(GTMaterials.CHEMICAL_DYES[i], 64 * 144)
                    .duration(400)
                    .euVATier(GTValues.EV)
                    .save();
        }
        ASSEMBLER_RECIPES.builder("infinity_cell")
                .inputItems(GTOItems.CELL_COMPONENT_1M)
                .inputItems(GTMachines.ROCK_CRUSHER[GTValues.EV].asItem(), 4)
                .inputItems("easy_villagers:iron_farm", 64)
                .inputItems("easy_villagers:villager", 6)
                .outputItems(infCell(RegistriesUtils.getItem("factory_blocks:factory")))
                .duration(400)
                .euVATier(GTValues.EV)
                .save();

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("infinity_cell"), infCell(Blocks.COBBLESTONE),
                "ABA",
                "CDE",
                "ABA",
                'A', RegistriesUtils.getItem("botania:rune_earth"),
                'B', GTMachines.ROCK_CRUSHER[GTValues.EV].asItem(),
                'C', Items.WATER_BUCKET.asItem(),
                'D', GTOItems.CELL_COMPONENT_1M.asItem(),
                'E', Items.LAVA_BUCKET.asItem());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("water_infinity_cell"), infCell(Fluids.WATER),
                "ABA",
                "BCB",
                "ABA",
                'A', RegistriesUtils.getItemStack("botania:rune_water"), 'B', GTItems.COVER_INFINITE_WATER.asItem(), 'C', new ItemStack(GTOItems.CELL_COMPONENT_1M.asItem()));
    }

    static ItemStack infCell(ItemLike item) {
        if (FMLLoader.getDist().isDedicatedServer()) return EPPItemAndBlock.INFINITY_CELL.getRecordCell(AEItemKey.of(item));
        if (AddedInfCells == null) {
            AddedInfCells = new ReferenceOpenHashSet<>();
        }
        var cell = EPPItemAndBlock.INFINITY_CELL.getRecordCell(AEItemKey.of(item));
        AddedInfCells.add(cell);
        return cell;
    }

    static ItemStack infCell(Fluid fluid) {
        if (FMLLoader.getDist().isDedicatedServer()) return EPPItemAndBlock.INFINITY_CELL.getRecordCell(AEFluidKey.of(fluid));
        if (AddedInfCells == null) {
            AddedInfCells = new ReferenceOpenHashSet<>();
        }
        var cell = EPPItemAndBlock.INFINITY_CELL.getRecordCell(AEFluidKey.of(fluid));
        AddedInfCells.add(cell);
        return cell;
    }
}
