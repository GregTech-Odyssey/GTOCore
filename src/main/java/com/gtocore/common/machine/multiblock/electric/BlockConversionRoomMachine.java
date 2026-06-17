package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.part.BlockBusPartMachine;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
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
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class BlockConversionRoomMachine extends StorageMultiblockMachine implements ICustomRecipeLogicHolder {

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

    // 用来冒充巨构的代码，有了记得改回1个顺带把define那里limit也改回1
    private final List<BlockBusPartMachine> blockBusPartMachines = new ArrayList<>();

    public BlockConversionRoomMachine(MetaMachineBlockEntity holder, boolean isLarge) {
        super(holder, 1, i -> i.getItem() == GTOItems.CONVERSION_SIMULATE_CARD.get());
        am = isLarge ? 64 : 4;
        poses = isLarge ? poses2 : poses1;
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof BlockBusPartMachine busPartMachine && !blockBusPartMachines.contains(busPartMachine)) {
            blockBusPartMachines.add(busPartMachine);
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        blockBusPartMachines.clear();
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            int amount = getConversionAmount();
            if (!blockBusPartMachines.isEmpty() && getStorageStack().getItem() == GTOItems.CONVERSION_SIMULATE_CARD.get()) {
                int leftAmount = amount;
                for (BlockBusPartMachine blockBusPartMachine : blockBusPartMachines) {
                    leftAmount = convertBlockBusContents(blockBusPartMachine, leftAmount);
                    if (leftAmount <= 0) {
                        break;
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
    }

    // 用来冒充巨构的代码，有了巨构记得改
    private int convertBlockBusContents(BlockBusPartMachine blockBusPartMachine, int leftAmount) {
        CustomItemStackHandler stackTransfer = blockBusPartMachine.getInventory().storage;
        var slots = stackTransfer.getSlots();
        for (int i = 0; leftAmount > 0 && i < slots; i++) {
            ItemStack itemStack = stackTransfer.getStackInSlot(i);
            if (itemStack.getItem() instanceof BlockItem blockItem && COV_RECIPE.containsKey(blockItem.getBlock())) {
                int count = itemStack.getCount();
                leftAmount -= count;
                stackTransfer.setStackInSlot(i, new ItemStack(COV_RECIPE.get(blockItem.getBlock()).asItem(), count));
            }
        }
        return leftAmount;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.block_conversion_room.am", getConversionAmount()));
    }

    // 用来冒充巨构的代码，有了巨构记得改
    private int getConversionAmount() {
        int tier = getTier();
        boolean isLargeMachine = am == 64;
        int baseAmount = tier * am - (isLargeMachine ? 64 : 7);
        if (!isLargeMachine || tier <= GTValues.UHV) {
            return baseAmount;
        }
        int amountAtUhv = GTValues.UHV * am - 64;
        return amountAtUhv << (tier - GTValues.UHV);
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        return getRecipeBuilder().duration(400).EUt(GTValues.V[getTier()]).build();
    }
}
