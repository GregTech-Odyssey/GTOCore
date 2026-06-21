package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OreVeinRecordTest {
    @Test
    void sameDefinitionAtDifferentCentersHasDifferentStableKeys() {
        ResourceLocation dimension = new ResourceLocation("minecraft", "overworld");
        ResourceLocation definition = new ResourceLocation("gtceu", "magnetite_vein");
        CompoundTag firstData = new CompoundTag();
        firstData.putLong("center", 1234L);
        CompoundTag secondData = new CompoundTag();
        secondData.putLong("center", 5678L);

        SharedEntry first = new OreVeinRecord(dimension, definition, 1, 1, firstData, 1, 0).asEntry();
        SharedEntry second = new OreVeinRecord(dimension, definition, 2, 2, secondData, 2, 0).asEntry();

        assertNotEquals(first.mapKey(), second.mapKey());
    }
}
