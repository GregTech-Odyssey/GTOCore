package com.gtocore.eio_travel.implementations;

import com.gtocore.eio_travel.EioTravelNbtKeys;
import com.gtocore.eio_travel.api.AbstractTravelTarget;

import com.gtolib.GTOCore;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class AnchorTravelTarget extends AbstractTravelTarget {

    public static final ResourceLocation SERIALIZED_NAME = GTOCore.id("eio_travel_anchor");

    public AnchorTravelTarget(BlockPos pos, String name, Item icon, boolean visible) {
        super(pos, name, icon, visible);
    }

    public static AnchorTravelTarget load(CompoundTag tag) {
        var pos = NbtUtils.readBlockPos(tag.getCompound(EioTravelNbtKeys.BLOCK_POS));
        var name = tag.getString(EioTravelNbtKeys.ANCHOR_NAME);
        String iconName = tag.getString(EioTravelNbtKeys.ANCHOR_ICON);
        var icon = iconName.isEmpty() ? Items.AIR : ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(iconName));
        var visible = tag.getBoolean(EioTravelNbtKeys.ANCHOR_VISIBILITY);
        return new AnchorTravelTarget(pos, name, icon, visible);
    }

    @Override
    public ResourceLocation getSerializationName() {
        return SERIALIZED_NAME;
    }
}
