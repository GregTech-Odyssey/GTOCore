package com.gtocore.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.google.common.collect.HashMultimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SlotBoostingItems extends Item {

    private static final String[] AVAILABLE_SLOTS = { "charm", "necklace", "ring", "bands" };
    private static final String SELECTED_SLOT_KEY = "SelectedSlotIndex";
    private static final String SLOT_BOOST_MODIFIER_NAME = "gtocore:slot_boost";

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
        tag.putInt(SELECTED_SLOT_KEY, next);
    }

    private String getSelectedSlot(ItemStack stack) {
        int validIndex = getSelectedIndex(stack) % AVAILABLE_SLOTS.length;
        return AVAILABLE_SLOTS[validIndex];
    }

    private boolean isSlotValid(Level level, Player player, String slot) {
        return CuriosApi.getSlot(slot, level).isPresent() && CuriosApi.getEntitySlots(player).containsKey(slot);
    }

    private int getCurrentSlots(Player player, String slot) {
        return CuriosApi.getCuriosInventory(player)
                .resolve()
                .flatMap(handler -> Optional.ofNullable(handler.getCurios().get(slot)))
                .map(ICurioStacksHandler::getSlots)
                .orElse(0);
    }

    private int getRequiredXp(Player player, String slot) {
        return getCurrentSlots(player, slot) * 2000;
    }

    private void addPermanentSlotBoost(ICuriosItemHandler curiosInventory, String slot) {
        var modifiers = HashMultimap.<String, AttributeModifier>create();
        modifiers.put(slot, new AttributeModifier(UUID.randomUUID(), SLOT_BOOST_MODIFIER_NAME, 1, AttributeModifier.Operation.ADDITION));
        curiosInventory.addPermanentSlotModifiers(modifiers);
        curiosInventory.getStacksHandler(slot).ifPresent(ICurioStacksHandler::update);
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
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.fail(stack);
        }
        if (!isSlotValid(level, serverPlayer, selectedSlot)) {
            serverPlayer.sendSystemMessage(Component.translatable("item.slot_boost.invalid_slot", selectedSlot));
            return InteractionResultHolder.fail(stack);
        }
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(serverPlayer).resolve();
        if (curiosInventory.isEmpty()) {
            serverPlayer.sendSystemMessage(Component.translatable("item.slot_boost.invalid_slot", selectedSlot));
            return InteractionResultHolder.fail(stack);
        }

        int requiredXp = getRequiredXp(serverPlayer, selectedSlot);
        if (serverPlayer.totalExperience < requiredXp) {
            serverPlayer.sendSystemMessage(Component.translatable("item.slot_boost.xp_shortage", requiredXp, serverPlayer.totalExperience));
            return InteractionResultHolder.fail(stack);
        }

        serverPlayer.giveExperiencePoints(-requiredXp);
        addPermanentSlotBoost(curiosInventory.get(), selectedSlot);

        serverPlayer.sendSystemMessage(Component.translatable("item.slot_boost.success", selectedSlot, getCurrentSlots(serverPlayer, selectedSlot)));
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
