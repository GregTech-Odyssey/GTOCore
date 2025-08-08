package com.gtocore.integration.gtmt;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.common.block.CoilBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.items.tools.powered.WirelessTerminalItem;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

import java.util.List;

class AdvancedBlockPattern extends BlockPattern {

    private AdvancedBlockPattern(BlockPattern pattern) {
        super(pattern.blockMatches, pattern.structureDir, pattern.aisleRepetitions, pattern.centerOffset);
    }

    static AdvancedBlockPattern getAdvancedBlockPattern(BlockPattern blockPattern) {
        return new AdvancedBlockPattern(blockPattern);
    }

    void autoBuild(Player player, MultiblockState worldState, AdvancedTerminalBehavior.AutoBuildSetting autoBuildSetting) {
        Level world = player.level();
        int minZ = -centerOffset[4];
        worldState.cleanCache();
        worldState.clean();
        IMultiController controller = worldState.controller;
        BlockPos centerPos = worldState.controllerPos;
        Direction facing = controller.self().getFrontFacing();
        Direction upwardsFacing = controller.self().getUpwardsFacing();
        boolean isFlipped = autoBuildSetting.isFlip == 1;
        var cacheGlobal = worldState.getGlobalCount();
        var cacheLayer = worldState.getLayerCount();
        Object2ObjectOpenHashMap<BlockPos, MetaMachine> machines = new Object2ObjectOpenHashMap<>();
        Long2ObjectOpenHashMap<Block> blocks = new Long2ObjectOpenHashMap<>();

        int[] repeat = new int[this.fingerLength];
        for (int h = 0; h < this.fingerLength; h++) {
            var minH = aisleRepetitions[h][0];
            var maxH = aisleRepetitions[h][1];
            if (minH != maxH) {
                repeat[h] = Math.max(minH, Math.min(maxH, autoBuildSetting.repeatCount));
            } else {
                repeat[h] = minH;
            }
        }

        for (int c = 0, z = minZ++, r; c < this.fingerLength; c++) {
            for (r = 0; r < repeat[c]; r++) {
                cacheLayer.clear();
                for (int b = 0, y = -centerOffset[1]; b < this.thumbLength; b++, y++) {
                    for (int a = 0, x = -centerOffset[0]; a < this.palmLength; a++, x++) {
                        TraceabilityPredicate predicate = this.blockMatches[c][b][a];
                        if (predicate == null) continue;
                        if (predicate.isAir()) continue;
                        BlockPos pos = setActualRelativeOffset(x, y, z, facing, upwardsFacing, isFlipped).offset(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                        worldState.update(pos, predicate);
                        long posLong = pos.asLong();
                        ItemStack coilItemStack = null;
                        BlockState blockState = world.getBlockState(pos);
                        Block block = blockState.getBlock();
                        if (block != Blocks.AIR) {
                            if (block instanceof CoilBlock coilBlock && autoBuildSetting.isReplaceCoilMode()) {
                                coilItemStack = coilBlock.asItem().getDefaultInstance();
                            } else {
                                blocks.put(posLong, block);
                                for (SimplePredicate limit : predicate.limited) {
                                    limit.testLimited(worldState);
                                }
                                continue;
                            }
                        }

                        boolean find = false;
                        Block[] infos = new Block[0];
                        for (SimplePredicate limit : predicate.limited) {
                            var candidates = limit.candidates == null ? null : limit.candidates.get();
                            if (candidates != null && limit.minLayerCount > 0 && autoBuildSetting.isPlaceHatch(candidates)) {
                                int curr = cacheLayer.getInt(limit);
                                if (curr < limit.minLayerCount &&
                                        (limit.maxLayerCount == -1 || curr < limit.maxLayerCount)) {
                                    cacheLayer.addTo(limit, 1);
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                            infos = candidates;
                            find = true;
                            break;
                        }
                        if (!find) {
                            for (SimplePredicate limit : predicate.limited) {
                                var candidates = limit.candidates == null ? null : limit.candidates.get();
                                if (candidates != null && limit.minCount > 0 && autoBuildSetting.isPlaceHatch(candidates)) {
                                    int curr = cacheGlobal.getInt(limit);
                                    if (curr < limit.minCount && (limit.maxCount == -1 || curr < limit.maxCount)) {
                                        cacheGlobal.addTo(limit, 1);
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                                infos = candidates;
                                find = true;
                                break;
                            }
                        }
                        if (!find) { // no limited
                            for (SimplePredicate limit : predicate.limited) {
                                var candidates = limit.candidates == null ? null : limit.candidates.get();
                                if (candidates == null) continue;
                                if (!autoBuildSetting.isPlaceHatch(candidates)) {
                                    continue;
                                }
                                if (limit.maxLayerCount != -1 &&
                                        cacheLayer.getOrDefault(limit, Integer.MAX_VALUE) == limit.maxLayerCount) {
                                    continue;
                                }
                                if (limit.maxCount != -1 &&
                                        cacheGlobal.getOrDefault(limit, Integer.MAX_VALUE) == limit.maxCount) {
                                    continue;
                                }
                                cacheLayer.addTo(limit, 1);
                                cacheGlobal.addTo(limit, 1);
                                infos = ArrayUtils.addAll(infos, candidates);
                            }
                            for (SimplePredicate common : predicate.common) {
                                var candidates = common.candidates == null ? null : common.candidates.get();
                                if (candidates == null) continue;
                                if (predicate.common.size() > 1 && !autoBuildSetting.isPlaceHatch(candidates)) {
                                    continue;
                                }
                                infos = ArrayUtils.addAll(infos, candidates);
                            }
                        }

                        List<ItemStack> candidates = autoBuildSetting.apply(infos);

                        if (autoBuildSetting.isReplaceCoilMode() && coilItemStack != null && ItemStack.isSameItem(candidates.get(0), coilItemStack)) continue;

                        // check inventory
                        Triplet<ItemStack, IItemHandler, Integer> result = foundItem(player, candidates, autoBuildSetting.isUseAE);
                        ItemStack found = result.getA();
                        IItemHandler handler = result.getB();
                        int foundSlot = result.getC();

                        if (found == null) continue;

                        // check can get old coilBlock
                        IItemHandler holderHandler = null;
                        int holderSlot = -1;
                        if (autoBuildSetting.isReplaceCoilMode() && coilItemStack != null) {
                            var holderResult = foundHolderSlot(player, coilItemStack);
                            holderHandler = holderResult.first();
                            holderSlot = holderResult.secondInt();

                            if (holderHandler != null && holderSlot < 0) {
                                continue;
                            }
                        }

                        if (autoBuildSetting.isReplaceCoilMode() && coilItemStack != null) {
                            world.removeBlock(pos, true);
                            if (holderHandler != null) holderHandler.insertItem(holderSlot, coilItemStack, false);
                        }

                        BlockItem itemBlock = (BlockItem) found.getItem();
                        BlockPlaceContext context = new BlockPlaceContext(world, player, InteractionHand.MAIN_HAND, found, BlockHitResult.miss(player.getEyePosition(0), Direction.UP, pos));
                        InteractionResult interactionResult = itemBlock.place(context);
                        if (interactionResult != InteractionResult.FAIL) {
                            if (handler != null) {
                                handler.extractItem(foundSlot, 1, false);
                            }
                            if (itemBlock instanceof MetaMachineItem && world.getBlockEntity(pos) instanceof MetaMachineBlockEntity machineBlockEntity) {
                                machines.put(pos, machineBlockEntity.metaMachine);
                            } else {
                                blocks.put(posLong, itemBlock.getBlock());
                            }
                        }

                    }
                }
                z++;
            }
        }
        Direction frontFacing = controller.self().getFrontFacing();
        for (var entry : machines.object2ObjectEntrySet()) {
            MetaMachine machine = entry.getValue();
            var pos = entry.getKey();
            resetFacing(pos, machine.getBlockState(), frontFacing, (p, f) -> {
                Object object = blocks.get(p.relative(f).asLong());
                if (object == null) {
                    return machine.isFacingValid(f);
                }
                return false;
            }, state -> world.setBlock(pos, state, 3));
        }
    }

    private static Triplet<ItemStack, IItemHandler, Integer> foundItem(Player player, List<ItemStack> candidates, int isUseAE) {
        ItemStack found = null;
        IItemHandler handler = null;
        int foundSlot = -1;
        if (!player.isCreative()) {
            var foundHandler = getMatchStackWithHandler(candidates,
                    player.getCapability(ForgeCapabilities.ITEM_HANDLER), player, isUseAE);
            if (foundHandler != null) {
                foundSlot = foundHandler.firstInt();
                handler = foundHandler.second();
                found = handler.getStackInSlot(foundSlot).copy();
            }
        } else {
            for (ItemStack candidate : candidates) {
                found = candidate.copy();
                if (!found.isEmpty() && found.getItem() instanceof BlockItem) {
                    break;
                }
                found = null;
            }
        }
        return new Triplet<>(found, handler, foundSlot);
    }

    private static ObjectIntPair<IItemHandler> foundHolderSlot(Player player, ItemStack coilItemStack) {
        IItemHandler handler = null;
        int foundSlot = -1;
        if (!player.isCreative()) {
            handler = player.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.isEmpty()) {
                    if (foundSlot < 0) {
                        foundSlot = i;
                    }
                } else if (ItemStack.isSameItemSameTags(coilItemStack, stack) && (stack.getCount() + 1) <= stack.getMaxStackSize()) {
                    foundSlot = i;
                }
            }
        }

        return ObjectIntPair.of(handler, foundSlot);
    }

    @Nullable
    private static IntObjectPair<IItemHandler> getMatchStackWithHandler(List<ItemStack> candidates, LazyOptional<IItemHandler> cap, Player player, int isUseAE) {
        IItemHandler handler = cap.resolve().orElse(null);
        if (handler == null) {
            return null;
        }
        for (int i = 0; i < handler.getSlots(); i++) {
            @NotNull
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            @NotNull
            LazyOptional<IItemHandler> stackCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (stackCap.isPresent()) {
                var rt = getMatchStackWithHandler(candidates, stackCap, player, isUseAE);
                if (rt != null) {
                    return rt;
                }
            } else if (isUseAE == 1 && stack.getItem() instanceof WirelessTerminalItem terminalItem && stack.hasTag() && stack.getTag().contains("accessPoint", 10)) {
                IGrid grid = terminalItem.getLinkedGrid(stack, player.level(), player);
                if (grid != null) {
                    MEStorage storage = grid.getStorageService().getInventory();
                    for (ItemStack candidate : candidates) {
                        if (storage.extract(AEItemKey.of(candidate), 1, Actionable.MODULATE, null) > 0) {
                            NonNullList<ItemStack> stacks = NonNullList.withSize(1, candidate);
                            IItemHandler handler1 = new ItemStackHandler(stacks);
                            return IntObjectPair.of(0, handler1);
                        }
                    }
                }

            } else if (candidates.stream().anyMatch(candidate -> ItemStack.isSameItemSameTags(candidate, stack)) &&
                    !stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                        return IntObjectPair.of(i, handler);
                    }
        }
        return null;
    }
}
