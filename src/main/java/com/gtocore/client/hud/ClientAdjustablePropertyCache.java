package com.gtocore.client.hud;

import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.PlayerAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
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

        for (PlayerAttributes.AttributeDefinition attribute : PlayerAttributes.REGISTRY.values()) {
            HUDPropertyEntry entry = ENTRIES.get(attribute);
            if (entry != null) {
                syncEntryState(attribute, entry);
            }
        }
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

    private static void syncEntryState(PlayerAttributes.AttributeDefinition attribute, HUDPropertyEntry entry) {
        PlayerAttributes playerAttributes = getPlayerAttributes();

        if (attribute instanceof PlayerAttributes.IntAttribute intAttribute &&
                entry instanceof HUDPropertyEntry.IntegerEntry integerEntry) {
            var value = playerAttributes.getNumeric(intAttribute);
            integerEntry.setRange(Mth.floor(value.getMin()), Mth.ceil(value.getMax()));
            return;
        }

        if (attribute instanceof PlayerAttributes.NumericAttribute numericAttribute &&
                entry instanceof HUDPropertyEntry.FloatEntry floatEntry) {
            var value = playerAttributes.getNumeric(numericAttribute);
            floatEntry.setRange(value.getMin(), value.getMax());
        }
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
