package com.gtocore.common.item;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@DataGeneratorScanned
public final class StructureDetectBehavior extends TooltipBehavior implements IToolBehavior, IInteractionItem {

    public static final StructureDetectBehavior INSTANCE = new StructureDetectBehavior(lines -> {
        lines.add(Component.translatable("item.gtocore.structure_detect.tooltip.0"));
        lines.add(Component.translatable("item.gtocore.structure_detect.tooltip.1"));
    });

    @RegisterLanguage(cn = "可能的模块错误", en = "Possible module error")
    private static final String MODULE = "gtocore.structure_detect.module";

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

    private static void addPos(ItemStack stack, BlockPos pos) {
        var tag = stack.getOrCreateTagElement("error_pos");
        if (tag.contains("pos", Tag.TAG_LIST)) {
            var list = tag.getList("pos", Tag.TAG_COMPOUND);
            list.add(pos2tag(pos));
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
            stack.setTag(new CompoundTag());
            BlockPos blockPos = context.getClickedPos();
            if (MetaMachine.getMachine(level, blockPos) instanceof IMultiController controller) {
                if (controller.isFormed()) {
                    player.sendSystemMessage(Component.translatable("gtceu.top.valid_structure").withStyle(ChatFormatting.GREEN));
                    var subs = controller.getSubMultiblockState();
                    if (subs != null) {
                        for (var s : subs) {
                            if (s.error != null) {
                                player.sendSystemMessage(Component.translatable(MODULE).withStyle(ChatFormatting.AQUA));
                                showError(player, s.error, stack);
                                for (var error : s.errorRecord) {
                                    showError(player, error, stack);
                                }
                            }
                        }
                    }
                } else {
                    MultiblockState multiblockState = controller.getMultiblockState();
                    if (multiblockState.error != null) {
                        showError(player, multiblockState.error, stack);
                    }
                    for (var error : controller.getMultiblockState().errorRecord) {
                        showError(player, error, stack);
                    }
                    var subs = controller.getSubMultiblockState();
                    if (subs != null) {
                        for (var s : subs) {
                            if (s.error != null) {
                                player.sendSystemMessage(Component.translatable(MODULE).withStyle(ChatFormatting.AQUA));
                                showError(player, s.error, stack);
                                for (var error : s.errorRecord) {
                                    showError(player, error, stack);
                                }
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private static void showError(Player player, PatternError error, ItemStack stack) {
        analysis(error).forEach(player::sendSystemMessage);
        addPos(stack, error.getPos());
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
