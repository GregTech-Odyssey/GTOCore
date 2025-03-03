package com.gto.gtocore.api.machine;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.machines.MultiBlockD;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface IMultiblockMachineDefinition {

    Set<MachineDefinition> DYNAMIC_STRUCTURE = Set.of(MultiBlockD.NANO_FORGE);

    void gtocore$clear();

    Pattern[] gtocore$getPatterns();

    void gtocore$setPatterns(Pattern[] patterns);

    static void init() {
        long time = System.currentTimeMillis();
        for (MachineDefinition machine : GTRegistries.MACHINES.values()) {
            if (machine instanceof MultiblockMachineDefinition definition && definition.isRenderXEIPreview() && definition instanceof IMultiblockMachineDefinition machineDefinition) {
                List<MultiblockShapeInfo> shapes = definition.getMatchingShapes();
                Pattern[] patterns;
                if (DYNAMIC_STRUCTURE.contains(definition) || !GTOConfig.INSTANCE.fastMultiBlockPage) {
                    patterns = new Pattern[shapes.size()];
                    for (int i = 0; i < shapes.size(); i++) {
                        patterns[i] = initializePattern(shapes.get(i));
                    }
                } else {
                    int amount = shapes.size() - 1;
                    boolean repetition = amount > 0;
                    patterns = new Pattern[repetition ? 2 : 1];
                    patterns[0] = initializePattern(shapes.get(0));
                    if (repetition) patterns[1] = initializePattern(shapes.get(amount));
                }
                machineDefinition.gtocore$setPatterns(patterns);
            }
        }
        GTOCore.LOGGER.info("Pre initialization of multiBlock took {}ms", System.currentTimeMillis() - time);
    }

    private static Pattern initializePattern(MultiblockShapeInfo info) {
        Map<BlockPos, BlockInfo> blockMap = new Object2ObjectOpenHashMap<>();
        Map<Item, PartInfo> partsMap = new Object2ObjectOpenHashMap<>();
        IMultiController multiController = null;
        BlockPos multiPos = PatternPreviewWidget.locateNextRegion(500);
        BlockInfo[][][] blocks = info.getBlocks();
        int lengthX = blocks.length;
        for (int x = 0; x < lengthX; x++) {
            BlockInfo[][] aisle = blocks[x];
            int lengthY = aisle.length;
            for (int y = 0; y < lengthY; y++) {
                BlockInfo[] column = aisle[y];
                int lengthZ = column.length;
                for (int z = 0; z < lengthZ; z++) {
                    boolean isController;
                    BlockInfo blockInfo = column[z];
                    BlockState blockState = blockInfo.getBlockState();
                    if (blockState.isAir()) continue;
                    BlockPos pos = multiPos.offset(x, y, z);
                    if (multiController == null && blockInfo.getBlockEntity(pos) instanceof IMachineBlockEntity holder && holder.getMetaMachine() instanceof IMultiController controller) {
                        multiController = controller;
                        isController = true;
                    } else {
                        isController = false;
                    }
                    blockMap.put(pos, blockInfo);
                    Item item;
                    if (!blockState.getFluidState().isEmpty()) {
                        item = blockState.getFluidState().getType().getBucket();
                    } else {
                        item = blockState.getBlock().asItem();
                    }
                    if (item == Items.AIR) continue;
                    partsMap.computeIfAbsent(item, key -> new PartInfo(key, blockInfo, isController)).amount++;
                }
            }
        }
        return new Pattern(multiController, blockMap, partsMap.values().stream().sorted((one, two) -> {
            if (one.isController) return -1;
            if (two.isController) return +1;
            if (one.isTile && !two.isTile) return -1;
            if (two.isTile && !one.isTile) return +1;
            if (one.blockId != two.blockId) return two.blockId - one.blockId;
            return two.amount - one.amount;
        }).map(PartInfo::getItemStack).filter(list -> !list.isEmpty()).collect(Collectors.toList()));
    }

    record Pattern(@Nullable IMultiController multiController, Map<BlockPos, BlockInfo> blockMap, List<ItemStack> parts) {}

    final class PartInfo {

        private final Item item;
        private final boolean isController;
        private final boolean isTile;
        private final int blockId;
        private int amount;

        private PartInfo(Item item, BlockInfo blockInfo, boolean isController) {
            this.item = item;
            this.isController = isController;
            blockId = Block.getId(blockInfo.getBlockState());
            isTile = blockInfo.hasBlockEntity();
        }

        private ItemStack getItemStack() {
            return new ItemStack(item, amount);
        }
    }
}
