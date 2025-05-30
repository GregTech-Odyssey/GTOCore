package com.gto.gtocore.integration.ae2;

import com.gto.gtocore.common.data.GTOItems;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import appeng.items.storage.CreativeCellItem;
import appeng.util.ConfigInventory;

public final class VirtualItemProviderCellItem extends CreativeCellItem {

    public VirtualItemProviderCellItem(Properties props) {
        super(props);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        Holder holder = new Holder(is);
        holder.inv = ConfigInventory.configTypes(what -> what instanceof AEItemKey itemKey && itemKey.getItem() == GTOItems.VIRTUAL_ITEM_PROVIDER.asItem(), 63, holder::save);
        holder.load();
        return holder.inv;
    }

    private static class Holder {

        private final ItemStack stack;
        private ConfigInventory inv;

        Holder(ItemStack stack) {
            this.stack = stack;
        }

        void load() {
            if (stack.hasTag()) {
                inv.readFromChildTag(stack.getOrCreateTag(), "list");
            }
        }

        void save() {
            inv.writeToChildTag(stack.getOrCreateTag(), "list");
        }
    }
}
