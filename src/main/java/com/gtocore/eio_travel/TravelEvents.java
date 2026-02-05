package com.gtocore.eio_travel;

import com.gtocore.api.travel.TravelMode;
import com.gtocore.common.item.TravelStaffBehavior;
import com.gtocore.eio_travel.logic.TravelHandler;
import com.gtocore.eio_travel.logic.TravelSavedData;
import com.gtocore.eio_travel.logic.TravelUtils;
import com.gtocore.eio_travel.network.TravelNetworks;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber
public class TravelEvents {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof ComponentItem c && c.getComponents().stream().anyMatch(comp -> comp instanceof TravelStaffBehavior))) {
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
            tag.putString(TravelUtils.FILTER_BLOCK_TAG, blockId);

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
        return TravelMode.fromString(tag.getString(TravelUtils.MODE_TAG));
    }

    private static void gtocore$setTravelMode(ItemStack stack, TravelMode mode) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TravelUtils.MODE_TAG, mode.getSerializedName());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            TravelNetworks.syncTravelData(TravelSavedData.getTravelData(serverPlayer.level()).save(new CompoundTag()), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            TravelNetworks.syncTravelData(TravelSavedData.getTravelData(serverPlayer.level()).save(new CompoundTag()), serverPlayer);
        }
    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static class Client {

        private static boolean LAST_JUMPING = false;
        private static boolean LAST_SNEAKING = false;
        private static int JUMP_COOLDOWN = 0;

        @SubscribeEvent
        public static void movementInputUpdate(MovementInputUpdateEvent event) {
            Input input = event.getInput();
            Player player = event.getEntity();
            boolean isNewJump = input.jumping && !LAST_JUMPING;
            LAST_JUMPING = input.jumping;
            boolean isNewCrouch = input.shiftKeyDown && !LAST_SNEAKING;
            LAST_SNEAKING = input.shiftKeyDown;

            if (!player.onGround() || !TravelHandler.canBlockTeleport(player)) {
                JUMP_COOLDOWN = 0;
                return;
            }
            if (isNewJump) {
                boolean success = TravelHandler.blockElevatorTeleport(player.level(), player, Direction.UP, true);
                if (!success) {
                    success = TravelHandler.blockTeleport(player.level(), player, true);
                }
                if (success) {
                    JUMP_COOLDOWN = 7;
                } else {
                    JUMP_COOLDOWN = 0;
                }
            } else if (isNewCrouch) {
                boolean success = TravelHandler.blockElevatorTeleport(player.level(), player, Direction.DOWN, true);
                if (!success) {
                    TravelHandler.blockTeleport(player.level(), player, true);
                }
            }

            if (JUMP_COOLDOWN > 0) {
                JUMP_COOLDOWN -= 1;
                input.jumping = false;
            }
        }

        @SubscribeEvent
        public static void emptyClick(PlayerInteractEvent.RightClickEmpty event) {
            Player player = event.getEntity();
            // Credit to castcrafter/travel_anchors
            if (TravelHandler.canBlockTeleport(player) && !player.isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND && event
                    .getEntity()
                    .getItemInHand(InteractionHand.OFF_HAND)
                    .isEmpty() && event.getItemStack().isEmpty()) {
                if (TravelHandler.blockTeleport(event.getLevel(), event.getEntity(), true)) {
                    player.swing(event.getHand(), true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }

        @SubscribeEvent
        public static void blockClick(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            if (!TravelHandler.canBlockTeleport(player)) {
                return;
            }
            if (TravelHandler.blockTeleport(event.getLevel(), event.getEntity(), true)) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void itemClick(PlayerInteractEvent.RightClickItem event) {
            Player player = event.getEntity();
            if (!TravelHandler.canBlockTeleport(player)) {
                return;
            }
            if (TravelHandler.blockTeleport(event.getLevel(), event.getEntity(), true)) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
