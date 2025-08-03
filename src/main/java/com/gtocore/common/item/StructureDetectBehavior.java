package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class StructureDetectBehavior extends TooltipBehavior implements IToolBehavior, IInteractionItem {

    public static final StructureDetectBehavior INSTANCE = new StructureDetectBehavior(lines -> {
        lines.add(Component.translatable("item.gtocore.structure_detect.tooltip.0"));
        lines.add(Component.translatable("item.gtocore.structure_detect.tooltip.1"));
    });

    private StructureDetectBehavior(@NotNull Consumer<List<Component>> tooltips) {
        super(tooltips);
    }

    public static BlockPos[] getPos(ItemStack stack) {
        var tag = stack.getOrCreateTagElement("error_pos");
        if (tag.contains("pos", Tag.TAG_LIST)) {
            return tag.getList("pos", Tag.TAG_COMPOUND)
                    .stream()
                    .map(p -> {
                        var pos = (CompoundTag) p;
                        return new BlockPos(pos.getInt("x"), pos.getInt("y"), pos.getInt("z"));
                    })
                    .toArray(BlockPos[]::new);
        }
        return null;
    }

    private static void addPos(ItemStack stack, int index, BlockPos pos) {
        var tag = stack.getOrCreateTagElement("error_pos");
        if (tag.contains("pos", Tag.TAG_LIST)) {
            var list = tag.getList("pos", Tag.TAG_COMPOUND);
            if (list.size() <= 2) {
                if (index < 2) {
                    list.set(index, pos2tag(pos));
                } else {
                    list.add(index, pos2tag(pos));
                }
            }
        } else {
            ListTag list = new ListTag();
            list.add(pos2tag(pos));
            tag.put("pos", list);
        }
    }

    private static CompoundTag pos2tag(BlockPos pos) {
        var tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            Level level = context.getLevel();
            if (level.isClientSide()) return InteractionResult.PASS;
            BlockPos blockPos = context.getClickedPos();
            if (MetaMachine.getMachine(level, blockPos) instanceof IMultiController controller) {
                if (controller.isFormed()) {
                    player.sendSystemMessage(Component.translatable("gtceu.top.valid_structure").withStyle(ChatFormatting.GREEN));
                } else {
                    if (!controller.self().allowFlip()) {
                        MultiblockState multiblockState = controller.getMultiblockState();
                        PatternError error = multiblockState.error;
                        if (error != null) {
                            showError(player, error, 0, stack);
                        }
                    } else {
                        ((ServerLevel) level).getServer().execute(() -> {
                            controller.getPatternLock().lock();
                            try {
                                BlockPattern pattern = controller.getPattern();
                                var result = check(controller, pattern);
                                for (int i = 0; i < result.size(); i++) {
                                    showError(player, result.get(i), i, stack);
                                }
                            } finally {
                                controller.getPatternLock().unlock();
                            }
                        });
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static List<PatternError> check(IMultiController controller, BlockPattern pattern) {
        List<PatternError> errors = new ArrayList<>();
        if (controller == null) {
            errors.add(new PatternStringError("no controller found"));
            return errors;
        }
        BlockPos centerPos = controller.self().getPos();
        Direction frontFacing = controller.self().getFrontFacing();
        Direction[] facings = controller.hasFrontFacing() ? new Direction[] { frontFacing } :
                new Direction[] { Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST };
        Direction upwardsFacing = controller.self().getUpwardsFacing();
        boolean allowsFlip = controller.self().allowFlip();
        MultiblockState worldState = new MultiblockState(controller, controller.self().getLevel(), controller.self().getPos());
        for (Direction direction : facings) {
            pattern.checkPatternAt(worldState, centerPos, direction, upwardsFacing, false, false);
            if (worldState.hasError()) {
                errors.add(worldState.error);
            }
            if (allowsFlip) {
                worldState = new MultiblockState(controller, worldState.getWorld(), worldState.getPos());
                pattern.checkPatternAt(worldState, centerPos, direction, upwardsFacing, true, false);
                if (worldState.hasError()) {
                    errors.add(worldState.error);
                }
            }
        }
        worldState.cleanCache();
        return errors;
    }

    private static void showError(Player player, PatternError error, int index, ItemStack stack) {
        analysis(error).forEach(player::sendSystemMessage);
        addPos(stack, index, error.getPos());
    }

    public static List<Component> analysis(PatternError error) {
        List<Component> show = new ArrayList<>();
        if (error instanceof PatternStringError pe) {
            show.add(pe.getErrorInfo());
        } else {
            var pos = error.getPos();
            show.add(Component.translatable("item.gtocore.structure_detect.error.1", pos.getX(), pos.getY(), pos.getZ()));
            for (List<ItemStack> candidate : error.getCandidates()) {
                if (!candidate.isEmpty()) {
                    show.add(Component.literal(" - ").append(candidate.get(0).getDisplayName()));
                }
            }
        }
        return show;
    }

    public static boolean isItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() instanceof ComponentItem item) {
            return item.getComponents().contains(INSTANCE);
        }
        return false;
    }
}
