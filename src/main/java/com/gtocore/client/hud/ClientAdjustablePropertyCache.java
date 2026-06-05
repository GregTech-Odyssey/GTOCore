package com.gtocore.client.hud;

import com.gtolib.api.player.PlayerAttributes;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.BooleanSupplier;

@OnlyIn(Dist.CLIENT)
public final class ClientAdjustablePropertyCache {

    private static final Map<PlayerAttributes.AttributeDefinition, HUDPropertyEntry> ENTRIES = new Reference2ObjectArrayMap<>();

    private ClientAdjustablePropertyCache() {}

    public static List<HUDPropertyEntry> getEntries() {
        return List.copyOf(ENTRIES.values());
    }

    public static void remove(PlayerAttributes.AttributeDefinition id) {
        ENTRIES.remove(id);
    }

    public static void clear() {
        ENTRIES.clear();
    }

    public static void updateIntegerRangeFromServer(PlayerAttributes.AttributeDefinition id, int minValue, int maxValue) {
        if (ENTRIES.get(id) instanceof HUDPropertyEntry.IntegerEntry integerEntry) {
            integerEntry.setRange(minValue, maxValue);
        }
    }

    public static void updateIntegerVisibilityFromServer(PlayerAttributes.AttributeDefinition id, boolean visibleInGame) {
        if (ENTRIES.get(id) instanceof HUDPropertyEntry.IntegerEntry integerEntry) {
            integerEntry.setVisibleInGame(visibleInGame);
        }
    }

    public static void updateBooleanVisibilityFromServer(PlayerAttributes.AttributeDefinition id, boolean visibleInGame) {
        if (ENTRIES.get(id) instanceof HUDPropertyEntry.BooleanEntry booleanEntry) {
            booleanEntry.setVisibleInGame(visibleInGame);
        }
    }
}
