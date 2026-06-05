package com.gtocore.client.hud;

import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.PlayerAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class ClientAdjustablePropertyCache {

    private static final Map<PlayerAttributes.AttributeDefinition, HUDPropertyEntry> ENTRIES = new Reference2ObjectArrayMap<>();

    private static boolean init;

    private ClientAdjustablePropertyCache() {}

    public static List<HUDPropertyEntry> getEntries() {
        syncFromPlayer();

        return List.copyOf(ENTRIES.values());
    }

    private static void syncFromPlayer() {
        if (init) {
            return;
        }
        init = true;
        for (var attribute : PlayerAttributes.REGISTRY.values()) {
            HUDPropertyEntry entry = createEntry(attribute);
            if (entry != null) {
                ENTRIES.put(attribute, entry);
            }
        }
    }

    private static HUDPropertyEntry createEntry(PlayerAttributes.AttributeDefinition attribute) {
        Component label = Component.translatable(attribute.getLangKey());

        return switch (attribute) {
            case PlayerAttributes.BooleanAttribute booleanAttribute -> new HUDPropertyEntry.BooleanEntry(attribute.getName(), label, booleanAttribute);
            case PlayerAttributes.IntAttribute intAttribute -> new HUDPropertyEntry.IntegerEntry(attribute.getName(), label, intAttribute);
            case PlayerAttributes.NumericAttribute numericAttribute -> new HUDPropertyEntry.FloatEntry(attribute.getName(), label, numericAttribute);
            default -> null;
        };
    }

    public static PlayerAttributes getPlayerAttributes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return null;
        }
        IEnhancedPlayer enhancedPlayer = IEnhancedPlayer.of(mc.player);
        return enhancedPlayer.getPlayerData().getPlayerAttributes();
    }
}
