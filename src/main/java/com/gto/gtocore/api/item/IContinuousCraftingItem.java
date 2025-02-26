package com.gto.gtocore.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import static com.gregtechceu.gtceu.api.item.tool.ToolHelper.damageItemWhenCrafting;

public interface IContinuousCraftingItem {

    default ItemStack definition$getCraftingRemainingItem(ItemStack stack) {
        stack = stack.copy();
        Player player = ForgeHooks.getCraftingPlayer();
        damageItemWhenCrafting(stack, player);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
