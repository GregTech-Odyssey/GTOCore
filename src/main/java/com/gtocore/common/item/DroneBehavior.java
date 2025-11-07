package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public final class DroneBehavior extends ElectricStats {

    private final int range;

    public DroneBehavior(int tier) {
        super(100000L * (1L << (tier << 1)), tier, true, false);
        range = getRange(tier);
    }

    private static int getRange(int t) {
        return switch (t) {
            case 3 -> 32;
            case 4 -> 96;
            case 5 -> 288;
            default -> 0;
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {}

    @Override
    public void appendTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendTooltips(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable("gui.ae2.WirelessRange", range));
        if (stack.getOrCreateTag().getInt("work") > 0) {
            tooltipComponents.add(Component.translatable("gtceu.multiblock.large_miner.working"));
        }
    }

    @Nullable
    public static DroneBehavior getDroneBehavior(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ComponentItem componentItem) {
            for (IItemComponent component : componentItem.getComponents()) {
                if (component instanceof DroneBehavior droneBehavior) {
                    return droneBehavior;
                }
            }
        }
        return null;
    }
}
