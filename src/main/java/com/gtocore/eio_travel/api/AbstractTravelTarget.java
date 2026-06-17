package com.gtocore.eio_travel.api;

import com.gtocore.config.GTOConfig;
import com.gtocore.eio_travel.EioTravelNbtKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class AbstractTravelTarget implements ITravelTarget {

    private final BlockPos pos;
    private String name;
    private Item icon;
    private boolean visible;

    public AbstractTravelTarget(BlockPos pos, String name, Item icon, boolean visible) {
        this.pos = pos;
        this.name = name;
        this.icon = icon;
        this.visible = visible;
    }

    @Override
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.put(EioTravelNbtKeys.BLOCK_POS, NbtUtils.writeBlockPos(getPos()));
        nbt.putString(EioTravelNbtKeys.ANCHOR_NAME, getName());
        nbt.putString(EioTravelNbtKeys.ANCHOR_ICON, String.valueOf(ForgeRegistries.ITEMS.getKey(getIcon())));
        nbt.putBoolean(EioTravelNbtKeys.ANCHOR_VISIBILITY, getVisibility());
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractTravelTarget other)) {
            return false;
        }

        return pos.equals(other.pos) && name.equals(other.name) && visible == (other.visible) && Objects.equals(icon, other.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, name, icon);
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    public boolean getVisibility() {
        return visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean canTeleportTo() {
        return getVisibility();
    }

    @Override
    public int getItem2BlockRange() {
        return GTOConfig.INSTANCE.travelConfig.blockRange;
    }

    @Override
    public int getBlock2BlockRange() {
        return GTOConfig.INSTANCE.travelConfig.itemRange;
    }
}
