package com.gto.gtocore.common.saved;

import com.gto.gtocore.common.wireless.WirelessManaContainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
public class WirelessManaSavaedData extends SavedData {

    public static WirelessManaSavaedData INSTANCE = new WirelessManaSavaedData();

    public final Map<UUID, WirelessManaContainer> containerMap = new Object2ObjectOpenHashMap<>();

    public WirelessManaSavaedData(CompoundTag tag) {
        ListTag all = tag.getList("all", Tag.TAG_COMPOUND);
        for (int i = 0; i < all.size(); i++) {
            WirelessManaContainer container = readTag(all.getCompound(i));
            containerMap.put(container.getUuid(), container);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag all = new ListTag();
        for (WirelessManaContainer container : containerMap.values()) {
            CompoundTag tag = toTag(container);
            if (tag.isEmpty()) continue;
            all.add(tag);
        }
        compoundTag.put("all", all);
        return compoundTag;
    }

    private static WirelessManaContainer readTag(CompoundTag engTag) {
        String mana = engTag.getString("mana");
        return new WirelessManaContainer(engTag.getUUID("uuid"), new BigInteger(mana.isEmpty() ? "0" : mana));
    }

    private static CompoundTag toTag(WirelessManaContainer container) {
        CompoundTag engTag = new CompoundTag();
        BigInteger storage = container.getStorage();
        if (!Objects.equals(storage, BigInteger.ZERO)) {
            engTag.putString("mana", storage.toString());
        }
        if (!engTag.isEmpty()) engTag.putUUID("uuid", container.getUuid());
        return engTag;
    }
}
