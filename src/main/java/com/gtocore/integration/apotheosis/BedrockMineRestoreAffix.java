package com.gtocore.integration.apotheosis;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreVeinSavedData;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import lombok.Getter;

import java.util.Map;

import static net.minecraftforge.common.Tags.Blocks.ORES;

@DataGeneratorScanned
public class BedrockMineRestoreAffix extends NumeralStatsAffix {

    @RegisterLanguage(cn = "每次开采矿石可以在当前区块勘探出相当于一般储量%s‰的额外基岩矿脉储量", en = "Each time you mine ore, you can explore an additional bedrock ore vein reserve equivalent to %s‰ of the average reserve in the current chunk")
    private static final String DESC = "affix.gtocore.bedrock_mine_restore.desc";
    @RegisterLanguage(cn = "每次开采矿石可以在当前区块勘探出相当于一般储量%s‰的额外基岩流体矿脉储量", en = "Each time you mine ore, you can explore an additional bedrock fluid vein reserve equivalent to %s‰ of the average reserve in the current chunk")
    private static final String FLUID_DESC = "affix.gtocore.bedrock_mine_restore.fluid.desc";
    public static final Codec<BedrockMineRestoreAffix> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    GemBonus.VALUES_CODEC.fieldOf("values").forGetter(BedrockMineRestoreAffix::getValues),
                    Codec.BOOL.fieldOf("fluid").forGetter(BedrockMineRestoreAffix::isFluidMode))

            .apply(inst, BedrockMineRestoreAffix::new));
    @Getter
    private final boolean isFluidMode;

    public BedrockMineRestoreAffix(Map<LootRarity, StepFunction> values, boolean isFluidMode) {
        super(values);
        this.isFluidMode = isFluidMode;
    }

    @Override
    public MutableComponent getDescription(ItemStack stack, LootRarity rarity, float level) {
        var desc = this.isFluidMode ? FLUID_DESC : DESC;
        return Component.translatable(desc, FormattingUtil.formatNumber2Places(this.getBuff(rarity, level) * 0.01));
    }

    @Override
    public Codec<? extends dev.shadowsoffire.apotheosis.adventure.affix.Affix> getCodec() {
        return CODEC;
    }

    @Override
    public void onBlockBreak(ItemStack stack, LootRarity rarity, float level, Player player, LevelAccessor world, BlockPos pos, BlockState state) {
        if (world instanceof ServerLevel serverLevel && state.is(ORES)) {
            ChunkPos chunkPos = new ChunkPos(pos);
            if (this.isFluidMode) {
                var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
                if (data.getFluidVeinWorldEntry(chunkPos.x, chunkPos.z).getDefinition() != null) {
                    data.depleteVein(chunkPos.x, chunkPos.z, -Math.round(this.getBuff(rarity, level)), true);
                }
            } else {
                var data = BedrockOreVeinSavedData.getOrCreate(serverLevel);
                if (data.getOreVeinWorldEntry(chunkPos.x, chunkPos.z).getDefinition() != null) {
                    data.depleteVein(chunkPos.x, chunkPos.z, -Math.round(this.getBuff(rarity, level)), true);
                }
            }
        }
    }
}
