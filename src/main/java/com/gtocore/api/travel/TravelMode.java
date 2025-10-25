package com.gtocore.api.travel;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

public enum TravelMode implements StringRepresentable {

    ALL("all", Component.translatable("gtocore.travel.mode.all")),
    ONE_PER_CHUNK("one_per_chunk", Component.translatable("gtocore.travel.mode.one_per_chunk")),
    FILTER_BY_BLOCK("filter_by_block", Component.translatable("gtocore.travel.mode.filter_by_block"));

    private final String name;
    private final Component displayName;

    TravelMode(String name, Component displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public TravelMode next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public static TravelMode fromString(String name) {
        for (TravelMode mode : values()) {
            if (mode.name.equals(name)) {
                return mode;
            }
        }
        return ALL;
    }
}
