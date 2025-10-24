package com.gtocore.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;

import java.util.List;

public class SlotBoostingItems extends Item {

    private static final String[] AVAILABLE_SLOTS = { "charm", "necklace", "ring", "bands" };
    private static final String SELECTED_SLOT_KEY = "SelectedSlotIndex";

    public SlotBoostingItems(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void verifyTagAfterLoad(@NotNull CompoundTag tag) {
        super.verifyTagAfterLoad(tag);
        if (!tag.contains(SELECTED_SLOT_KEY)) {
            tag.putInt(SELECTED_SLOT_KEY, 0);
        }
    }

    private CompoundTag getOrInitTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            tag = new CompoundTag();
            tag.putInt(SELECTED_SLOT_KEY, 0);
            stack.setTag(tag);
        }
        return tag;
    }

    private int getSelectedIndex(ItemStack stack) {
        return getOrInitTag(stack).getInt(SELECTED_SLOT_KEY);
    }

    private void cycleSelectedIndex(ItemStack stack) {
        CompoundTag tag = getOrInitTag(stack);
        int next = (tag.getInt(SELECTED_SLOT_KEY) + 1) % AVAILABLE_SLOTS.length;
        tag = new CompoundTag();
        tag.putInt(SELECTED_SLOT_KEY, next);
        stack.setTag(tag);
    }

    private String getSelectedSlot(ItemStack stack) {
        int validIndex = getSelectedIndex(stack) % AVAILABLE_SLOTS.length;
        return AVAILABLE_SLOTS[validIndex];
    }

    private boolean isSlotValid(String slot) {
        return LegacySlotManager.getIdsToMods().containsKey(slot) || CuriosSlotManager.SERVER.getModsFromSlots().containsKey(slot) || CuriosEntityManager.SERVER.getModsFromSlots().containsKey(slot);
    }

    private int getCurrentSlots(Player player, String slot) {
        return CuriosApi.getSlotHelper().getSlotsForType(player, slot);
    }

    private int getRequiredXp(Player player, String slot) {
        return getCurrentSlots(player, slot) * 2000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        String selectedSlot = getSelectedSlot(stack);

        if (!player.isShiftKeyDown()) {
            cycleSelectedIndex(stack);
            if (level.isClientSide) {
                String newSlot = getSelectedSlot(stack);
                int slotCount = getCurrentSlots(player, newSlot);
                player.displayClientMessage(Component.translatable("item.slot_boost.switch_hint", newSlot, slotCount, getRequiredXp(player, newSlot)).withStyle(style -> style.withColor(0x00FFFF)), true);
            }
            return InteractionResultHolder.success(stack);
        }

        if (player.isShiftKeyDown()) {
            if (!isSlotValid(selectedSlot)) {
                if (level.isClientSide) {
                    player.sendSystemMessage(Component.translatable("item.slot_boost.invalid_slot", selectedSlot));
                    return InteractionResultHolder.fail(stack);
                }
            }

            int requiredXp = getRequiredXp(player, selectedSlot);
            if (player.totalExperience < requiredXp) {
                player.displayClientMessage(Component.translatable("item.slot_boost.xp_shortage", requiredXp, player.totalExperience), true);
                return InteractionResultHolder.fail(stack);
            }

            player.giveExperiencePoints(-requiredXp);
            if (player instanceof ServerPlayer serverPlayer) CuriosApi.getSlotHelper().growSlotType(selectedSlot, 1, serverPlayer);

            player.displayClientMessage(Component.translatable("item.slot_boost.success", selectedSlot, getCurrentSlots(player, selectedSlot)), true);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String slot = getSelectedSlot(stack);
        return Component.translatable("item.slot_boost.name", slot);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.slot_boost.tooltip1").withStyle(style -> style.withColor(0xAAAAAA)));
        tooltip.add(Component.translatable("item.slot_boost.tooltip2").withStyle(style -> style.withColor(0xAAAAAA)));
        tooltip.add(Component.translatable("item.slot_boost.tooltip3", getSelectedSlot(stack)).withStyle(style -> style.withColor(0xAAAAAA)));
    }
}
