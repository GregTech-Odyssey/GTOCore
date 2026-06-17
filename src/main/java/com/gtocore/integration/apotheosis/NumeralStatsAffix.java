package com.gtocore.integration.apotheosis;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

public abstract class NumeralStatsAffix extends Affix {

    @Getter
    protected final Map<LootRarity, StepFunction> values;

    protected NumeralStatsAffix(Map<LootRarity, StepFunction> values) {
        super(AffixType.STAT);
        this.values = values;
    }

    @Override
    public boolean canApplyTo(ItemStack stack, LootCategory cat, LootRarity rarity) {
        return cat.isBreaker() && this.values.containsKey(rarity);
    }

    public float getBuff(LootRarity rarity, float level) {
        return this.values.get(rarity).get(level);
    }

    @Override
    public Component getAugmentingText(ItemStack stack, LootRarity rarity, float level) {
        MutableComponent comp = this.getDescription(stack, rarity, level);

        Component minComp = Component.literal(fmt(this.getBuff(rarity, 0)));
        Component maxComp = Component.literal(fmt(this.getBuff(rarity, 1)));
        return comp.append(valueBounds(minComp, maxComp));
    }

    public static <T extends NumeralStatsAffix> Codec<T> createCodec(Function<Map<LootRarity, StepFunction>, T> constructor) {
        return RecordCodecBuilder.create(inst -> inst
                .group(
                        GemBonus.VALUES_CODEC.fieldOf("values").forGetter(NumeralStatsAffix::getValues))
                .apply(inst, constructor));
    }

    // Subclasses must provide their specific codec
    public abstract Codec<? extends Affix> getCodec();
}
