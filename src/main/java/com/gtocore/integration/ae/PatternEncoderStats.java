package com.gtocore.integration.ae;

import com.gtocore.utils.PlayerNameUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.client.gui.me.patternaccess.PatternContainerRecord;
import appeng.core.definitions.AEItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PatternEncoderStats {

    private PatternEncoderStats() {}

    public static Stats collect(Iterable<PatternContainerRecord> records) {
        var byEncoder = new HashMap<UUID, Integer>();
        int totalPatterns = 0;
        int nonProcessingPatterns = 0;
        int processingWithoutEncoder = 0;

        for (var record : records) {
            for (ItemStack stack : record.getInventory()) {
                if (stack.isEmpty() || !PatternDetailsHelper.isEncodedPattern(stack)) {
                    continue;
                }

                int amount = stack.getCount();
                totalPatterns += amount;

                var tag = stack.getTag();
                boolean processing = stack.is(AEItems.PROCESSING_PATTERN.asItem());
                if (processing) {
                    if (tag != null && tag.hasUUID("uuid")) {
                        byEncoder.merge(tag.getUUID("uuid"), amount, Integer::sum);
                    } else {
                        processingWithoutEncoder += amount;
                    }
                } else {
                    nonProcessingPatterns += amount;
                    if (tag != null && tag.hasUUID("uuid")) {
                        byEncoder.merge(tag.getUUID("uuid"), amount, Integer::sum);
                    }
                }
            }
        }

        return new Stats(byEncoder, totalPatterns, nonProcessingPatterns, processingWithoutEncoder);
    }

    public record Stats(Map<UUID, Integer> byEncoder, int totalPatterns, int nonProcessingPatterns,
                        int processingWithoutEncoder) {

        public boolean isEmpty() {
            return totalPatterns == 0;
        }

        public int encoderCount() {
            return byEncoder.size();
        }

        public List<Component> lines() {
            var lines = new ArrayList<Component>(byEncoder.size() + 4);
            lines.add(Component.translatable("gtocore.pattern_encoder_stats.total",
                    Component.literal(Integer.toString(totalPatterns)).withStyle(ChatFormatting.LIGHT_PURPLE)));

            if (byEncoder.isEmpty()) {
                lines.add(Component.translatable("gtocore.pattern_encoder_stats.no_encoder")
                        .withStyle(ChatFormatting.GRAY));
            }

            var level = Minecraft.getInstance().level;
            byEncoder.entrySet().stream()
                    .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed()
                            .thenComparing(entry -> PlayerNameUtils.getLastKnownName(level, entry.getKey())))
                    .forEach(entry -> lines.add(Component.translatable("gtocore.pattern_encoder_stats.encoder_line",
                            Component.literal(PlayerNameUtils.getLastKnownName(level, entry.getKey()))
                                    .withStyle(ChatFormatting.BLUE),
                            Component.literal(Integer.toString(entry.getValue()))
                                    .withStyle(ChatFormatting.LIGHT_PURPLE))));

            lines.add(Component.translatable("gtocore.pattern_encoder_stats.non_processing",
                    Component.literal(Integer.toString(nonProcessingPatterns))
                            .withStyle(ChatFormatting.LIGHT_PURPLE)));

            if (processingWithoutEncoder > 0) {
                lines.add(Component.translatable("gtocore.pattern_encoder_stats.without_encoder",
                        Component.literal(Integer.toString(processingWithoutEncoder))
                                .withStyle(ChatFormatting.LIGHT_PURPLE)));
            }

            return lines;
        }
    }
}
