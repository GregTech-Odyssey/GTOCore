package com.gtocore.mixin.avaritia;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import committee.nova.mods.avaritia.common.item.misc.InfinityTotemItem;
import committee.nova.mods.avaritia.common.item.resources.ResourceItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(InfinityTotemItem.class)
public class InfinityToemNerf extends ResourceItem {

    public InfinityToemNerf(Rarity rarity, String registryName, boolean needsTooltip) {
        super(rarity, registryName, needsTooltip);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (damage > stack.getDamageValue()) super.setDamage(stack, damage);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> components, @NotNull TooltipFlag p_41424_) {
        super.appendHoverText(pStack, pLevel, components, p_41424_);
        components.add(Component.translatable("tooltip.gtocore.infinity_totem_nerf"));
        components.add(Component.translatable("tooltip.gtocore.infinity_totem_nerf.1"));
    }
}
