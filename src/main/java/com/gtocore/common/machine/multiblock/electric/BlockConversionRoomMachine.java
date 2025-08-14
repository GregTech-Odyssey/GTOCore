package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.part.BlockBusPartMachine;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class BlockConversionRoomMachine extends StorageMultiblockMachine {

    private static final List<int[]> poses1 = new ArrayList<>();
    private static final List<int[]> poses2 = new ArrayList<>();
    public static final Map<Block, Block> COV_RECIPE;

    static {
        for (int i = -2; i <= 2; i++) {
            for (int j = -1; j >= -5; j--) {
                for (int k = -2; k <= 2; k++) {
                    poses1.add(new int[] { i, j, k });
                }
            }
        }
        for (int i = -4; i <= 4; i++) {
            for (int j = -1; j >= -7; j--) {
                for (int k = -4; k <= 4; k++) {
                    poses2.add(new int[] { i, j, k });
                }
            }
        }
        ImmutableMap.Builder<Block, Block> covRecipe = ImmutableMap.builder();
        covRecipe.put(Blocks.BONE_BLOCK, GTOBlocks.ESSENCE_BLOCK.get());
        covRecipe.put(Blocks.OAK_LOG, Blocks.CRIMSON_STEM);
        covRecipe.put(Blocks.BIRCH_LOG, Blocks.WARPED_STEM);
        covRecipe.put(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.Calcium), Blocks.BONE_BLOCK);
        covRecipe.put(Blocks.MOSS_BLOCK, Blocks.SCULK);
        covRecipe.put(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK);
        covRecipe.put(GTOBlocks.INFUSED_OBSIDIAN.get(), GTOBlocks.DRACONIUM_BLOCK_CHARGED.get());
        covRecipe.put(ModBlocks.ORGANIC_COMPOST.get(), ModBlocks.RICH_SOIL.get());
        COV_RECIPE = covRecipe.build();
    }

    private final int am;
    private final List<int[]> poses;

    private BlockBusPartMachine blockBusPartMachine;

    public BlockConversionRoomMachine(MetaMachineBlockEntity holder, boolean isLarge) {
        super(holder, 1, i -> i.getItem() == GTOItems.CONVERSION_SIMULATE_CARD.get());
        am = isLarge ? 64 : 4;
        poses = isLarge ? poses2 : poses1;
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof BlockBusPartMachine busPartMachine) {
            blockBusPartMachine = busPartMachine;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        blockBusPartMachine = null;
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            int amount = getTier() * am - 7;
            if (blockBusPartMachine != null && getStorageStack().getItem() == GTOItems.CONVERSION_SIMULATE_CARD.get()) {
                CustomItemStackHandler stackTransfer = blockBusPartMachine.getInventory().storage;
                int a = amount;
                var slots = stackTransfer.getSlots();
                for (int i = 0; a > 0 && i < slots; i++) {
                    ItemStack itemStack = stackTransfer.getStackInSlot(i);
                    if (itemStack.getItem() instanceof BlockItem blockItem && COV_RECIPE.containsKey(blockItem.getBlock())) {
                        int count = itemStack.getCount();
                        a -= count;
                        stackTransfer.setStackInSlot(i, new ItemStack(COV_RECIPE.get(blockItem.getBlock()).asItem(), count));
                    }
                }
            } else {
                Level level = getLevel();
                if (level != null) {
                    int[] pos = {};
                    for (int i = 0; i < amount; i++) {
                        int[] pos_0 = poses.get((int) (Math.random() * poses.size()));
                        if (!Arrays.equals(pos_0, pos)) {
                            pos = pos_0;
                            BlockPos blockPos = getPos().offset(pos[0], pos[1], pos[2]);
                            Block block = level.getBlockState(blockPos).getBlock();
                            if (COV_RECIPE.containsKey(block)) {
                                level.setBlockAndUpdate(blockPos, COV_RECIPE.get(block).defaultBlockState());
                            }
                        } else {
                            i--;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.block_conversion_room.am", (getTier() * am - 7)));
    }

    @Nullable
    private Recipe getRecipe() {
        Recipe recipe = getRecipeBuilder().duration(400).EUt(getOverclockVoltage()).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
