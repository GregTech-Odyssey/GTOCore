package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.machine.multiblock.part.BlockBusPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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

import java.util.ArrayList;
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
        COV_RECIPE = covRecipe.build();
    }

    private final int am;
    private final List<int[]> poses;

    private BlockBusPartMachine blockBusPartMachine;

    public BlockConversionRoomMachine(IMachineBlockEntity holder, boolean isLarge) {
        super(holder, 1, i -> i.getItem() == GTOItems.CONVERSION_SIMULATE_CARD.get());
        am = isLarge ? 64 : 4;
        poses = isLarge ? poses2 : poses1;
    }

    @Override
    public void onPartScan(IMultiPart part) {
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
                for (int i = 0; a > 0 && i < stackTransfer.getSlots(); i++) {
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
                        if (pos_0 != pos) {
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
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(400).EUt(getOverclockVoltage()).buildRawRecipe();
            if (recipe.matchRecipeContents(IO.IN, this, recipe.tickInputs, true).isSuccess()) return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public @NotNull CustomRecipeLogic getRecipeLogic() {
        return (CustomRecipeLogic) super.getRecipeLogic();
    }
}
