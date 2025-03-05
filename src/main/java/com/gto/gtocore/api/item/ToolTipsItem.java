package com.gto.gtocore.api.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class ToolTipsItem extends Item {

    private final Supplier<Component>[] componentSupplier;

    public ToolTipsItem(Properties properties, Supplier<Component>[] componentSupplier) {
        super(properties);
        this.componentSupplier = componentSupplier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        for (Supplier<Component> component : componentSupplier) {
            list.add(component.get());
        }
    }
}
