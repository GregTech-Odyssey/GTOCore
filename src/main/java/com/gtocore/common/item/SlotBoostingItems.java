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

    // 固定可添加的槽位组
    private static final String[] AVAILABLE_SLOTS = { "charm", "necklace", "ring", "bands" };
    // NBT键：保存当前选中的槽位索引
    private static final String SELECTED_SLOT_KEY = "SelectedSlotIndex";

    public SlotBoostingItems(Properties properties) {
        super(properties);
    }

    // 初始化默认NBT（确保新物品有默认选中索引）
    @Override
    public void verifyTagAfterLoad(@NotNull CompoundTag tag) {
        super.verifyTagAfterLoad(tag);
        if (!tag.contains(SELECTED_SLOT_KEY)) {
            tag.putInt(SELECTED_SLOT_KEY, 0);
        }
    }

    // 获取当前选中的槽位索引（确保NBT存在）
    private int getSelectedIndex(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag(); // 确保tag存在，无则创建
        return tag.getInt(SELECTED_SLOT_KEY);
    }

    // 切换选中的槽位索引（循环切换）
    private void cycleSelectedIndex(ItemStack stack) {
        int current = getSelectedIndex(stack);
        int next = (current + 1) % AVAILABLE_SLOTS.length;
        stack.getOrCreateTag().putInt(SELECTED_SLOT_KEY, next);
    }

    // 获取当前选中的槽位名称
    private String getSelectedSlot(ItemStack stack) {
        return AVAILABLE_SLOTS[getSelectedIndex(stack)];
    }

    // 切换槽位时显示提示（包含当前槽位数量）
    private void showSlotSwitchHint(Player player, String slot) {
        // 客户端获取当前槽位数量（Curios会同步数据到客户端）
        int slotCount = CuriosApi.getSlotHelper().getSlotsForType(player, slot);
        // 屏幕中央提示：包含槽位名称和当前数量
        player.displayClientMessage(
                Component.translatable("item.slot_boost.switch_hint", slot, slotCount)
                        .withStyle(style -> style.withColor(0x00FFFF)),
                true);
    }

    // 检查槽位是否已注册
    private boolean isSlotValid(String slot) {
        return LegacySlotManager.getIdsToMods().containsKey(slot) || CuriosSlotManager.SERVER.getModsFromSlots().containsKey(slot) || CuriosEntityManager.SERVER.getModsFromSlots().containsKey(slot);
    }

    // 计算需要消耗的经验（当前槽位数量 × 10）
    private int getRequiredXp(ServerPlayer player, String slot) {
        int currentSlots = CuriosApi.getSlotHelper().getSlotsForType(player, slot);
        return currentSlots * 10;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        String selectedSlot = getSelectedSlot(stack);

        // 客户端逻辑：处理槽位切换
        if (level.isClientSide) {
            if (!player.isShiftKeyDown()) {
                cycleSelectedIndex(stack);
                String newSlot = getSelectedSlot(stack);
                showSlotSwitchHint(player, newSlot); // 切换时提示包含数量
            }
            return InteractionResultHolder.success(stack);
        }

        // 服务端逻辑：处理添加槽位（Shift+点击）
        if (player.isShiftKeyDown() && player instanceof ServerPlayer serverPlayer) {
            // 检查槽位有效性
            if (!isSlotValid(selectedSlot)) {
                serverPlayer.sendSystemMessage(Component.translatable("item.slot_boost.invalid_slot", selectedSlot));
                return InteractionResultHolder.fail(stack);
            }

            // 计算所需经验并提前提示
            int currentCount = CuriosApi.getSlotHelper().getSlotsForType(serverPlayer, selectedSlot);
            int requiredXp = getRequiredXp(serverPlayer, selectedSlot);
            serverPlayer.sendSystemMessage(Component.translatable(
                    "item.slot_boost.cost_hint", selectedSlot, requiredXp, currentCount, currentCount + 1)); // 提示消耗和预期结果

            // 检查经验是否足够
            if (serverPlayer.totalExperience < requiredXp) {
                serverPlayer.sendSystemMessage(Component.translatable(
                        "item.slot_boost.xp_shortage", requiredXp, serverPlayer.totalExperience));
                return InteractionResultHolder.fail(stack);
            }

            // 消耗经验并添加槽位
            serverPlayer.giveExperiencePoints(-requiredXp);
            CuriosApi.getSlotHelper().growSlotType(selectedSlot, 1, serverPlayer);

            // 成功提示
            serverPlayer.sendSystemMessage(Component.translatable(
                    "item.slot_boost.success", selectedSlot, requiredXp,
                    CuriosApi.getSlotHelper().getSlotsForType(serverPlayer, selectedSlot)));
        }

        return InteractionResultHolder.success(stack);
    }

    // 物品名称显示当前选中的槽位
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String slot = getSelectedSlot(stack);
        return Component.translatable("item.slot_boost.name", slot);
    }

    // 物品描述添加操作提示
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        // 描述文本：操作方式说明
        tooltip.add(Component.translatable("item.slot_boost.tooltip1").withStyle(style -> style.withColor(0xAAAAAA)));
        tooltip.add(Component.translatable("item.slot_boost.tooltip2").withStyle(style -> style.withColor(0xAAAAAA)));
        tooltip.add(Component.translatable("item.slot_boost.tooltip3", getSelectedSlot(stack)).withStyle(style -> style.withColor(0xAAAAAA)));
    }
}
