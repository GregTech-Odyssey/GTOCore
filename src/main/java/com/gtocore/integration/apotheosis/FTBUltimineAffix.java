package com.gtocore.integration.apotheosis;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import com.mojang.serialization.Codec;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.placebo.util.StepFunction;

import java.util.Map;

@DataGeneratorScanned
public class FTBUltimineAffix extends NumeralStatsAffix {

    @RegisterLanguage(cn = "该工具可以提升%s%%的连锁挖掘数量上限", en = "This tool increases the maximum number of blocks that can be mined in a chain by %s%%")
    private static final String DESC = "affix.gtocore.ftb_ultimine.desc";
    public static final Codec<FTBUltimineAffix> CODEC = NumeralStatsAffix.createCodec(FTBUltimineAffix::new);

    public FTBUltimineAffix(Map<LootRarity, StepFunction> values) {
        super(values);
    }

    @Override
    public MutableComponent getDescription(ItemStack stack, LootRarity rarity, float level) {
        return Component.translatable(DESC, FormattingUtil.formatNumber2Places(this.getBuff(rarity, level) * 100));
    }

    @Override
    public Codec<? extends dev.shadowsoffire.apotheosis.adventure.affix.Affix> getCodec() {
        return CODEC;
    }
}
