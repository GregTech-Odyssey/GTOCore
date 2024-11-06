package com.gto.gtocore.integration.ae2;

import com.gto.gtocore.integration.ae2.storage.InfinityCellHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InfinityCell extends Item {

    private final AEKeyType keyType;

    public InfinityCell(AEKeyType keyType) {
        super(new Properties().stacksTo(1).fireResistant());
        this.keyType = keyType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        Preconditions.checkArgument(stack.getItem() == this);
        InfinityCellHandler.INSTANCE.addCellInformationToTooltip(stack, tooltip, world);
    }
}
