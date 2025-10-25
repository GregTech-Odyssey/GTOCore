package com.gtocore.common.forge;

import com.gtocore.api.travel.TravelMode;
import com.gtocore.integration.eio.ITravelHandlerHook;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import com.enderio.base.common.item.tool.TravelStaffItem;

import java.util.Objects;

public class TravelStaffItemMixinLikeForge {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof TravelStaffItem)) {
            return;
        }

        event.setCanceled(true);

        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        TravelMode currentMode = gtocore$getTravelMode(stack);
        TravelMode nextMode = currentMode.next();
        gtocore$setTravelMode(stack, nextMode);

        if (nextMode == TravelMode.FILTER_BY_BLOCK) {
            BlockPos pos = event.getPos();
            BlockState blockState = level.getBlockState(pos);
            String blockId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockState.getBlock())).toString();

            CompoundTag tag = stack.getOrCreateTag();
            tag.putString(ITravelHandlerHook.FILTER_BLOCK_TAG, blockId);

            player.displayClientMessage(
                    Component.translatable("gtocore.travel.mode.switched")
                            .append(": ")
                            .append(nextMode.getDisplayName())
                            .append(" [")
                            .append(Component.literal(blockId).withStyle(ChatFormatting.YELLOW))
                            .append("]"),
                    true);
        } else {
            player.displayClientMessage(
                    Component.translatable("gtocore.travel.mode.switched")
                            .append(": ")
                            .append(nextMode.getDisplayName()),
                    true);
        }
    }

    private static TravelMode gtocore$getTravelMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return TravelMode.fromString(tag.getString(ITravelHandlerHook.MODE_TAG));
    }

    private static void gtocore$setTravelMode(ItemStack stack, TravelMode mode) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(ITravelHandlerHook.MODE_TAG, mode.getSerializedName());
    }
}
