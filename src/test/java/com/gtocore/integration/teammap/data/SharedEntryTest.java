package com.gtocore.integration.teammap.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SharedEntryTest {
    @Test
    void nbtRoundTripPreservesIdentityAndPayload() {
        CompoundTag payload = new CompoundTag();
        payload.putString("fluid", "gtceu:oil");
        payload.putInt("yield", 120);
        SharedEntry original = new SharedEntry(SharedKind.BEDROCK_FLUID,
                ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"), -17, 34, "-17,34",
                SharedEntry.hash(payload), 42, payload);

        SharedEntry decoded = SharedEntry.fromTag(original.toTag());
        assertEquals(original, decoded);
        assertEquals("BEDROCK_FLUID|minecraft:overworld|-17,34", decoded.mapKey());
    }
}
