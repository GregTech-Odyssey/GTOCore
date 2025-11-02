package com.gtocore.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import lombok.Getter;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
public final class KineticRotorItem extends Item {

    private final int MinWind;
    private final int MaxWind;
    private final int material;

    public KineticRotorItem(Properties properties, int durability, int min, int max, int material) {
        super(properties.durability(durability));
        MinWind = min;
        MaxWind = max;
        this.material = material;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("gtocore.tooltip.item.kinetic_rotor.min", MinWind));
        list.add(Component.translatable("gtocore.tooltip.item.kinetic_rotor.max", MaxWind));
    }
}
