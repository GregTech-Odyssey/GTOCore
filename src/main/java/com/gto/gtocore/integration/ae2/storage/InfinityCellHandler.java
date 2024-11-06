package com.gto.gtocore.integration.ae2.storage;

import com.gto.gtocore.integration.ae2.InfinityCell;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;

import java.util.List;

public class InfinityCellHandler implements ICellHandler {

    public static final InfinityCellHandler INSTANCE = new InfinityCellHandler();

    @Override
    public boolean isCell(ItemStack is) {
        return is.getItem() instanceof InfinityCell;
    }

    @Override
    public InfinityCellInventory getCellInventory(ItemStack is, ISaveProvider container) {
        return InfinityCellInventory.createInventory(is, container);
    }

    public void addCellInformationToTooltip(ItemStack stack, List<Component> lines, Level world) {
        InfinityCellInventory handler = getCellInventory(stack, null);
        CompoundTag tag = stack.getTag();
        if (tag != null && handler != null && handler.hasDiskUUID()) {
            if (world.getGameTime() % 20 == 0 || !tag.contains("TotalStorage")) {
                tag.putString("TotalStorage", handler.getTotalStorage());
            }
            lines.add(Component.literal("UUID: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(handler.getDiskUUID().toString()).withStyle(ChatFormatting.AQUA)));
            lines.add(Component.literal("Byte: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(tag.getString("TotalStorage")).withStyle(ChatFormatting.GREEN)));
        }
    }
}
