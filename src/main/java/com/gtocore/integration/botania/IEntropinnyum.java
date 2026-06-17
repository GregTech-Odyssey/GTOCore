package com.gtocore.integration.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.Optional;

public interface IEntropinnyum {

    /// {@link EntropinnyumBlockEntity#RANGE}
    int RANGE = 12;
    int EXPLODE_EFFECT_EVENT = 0;
    int ANGRY_EFFECT_EVENT = 1;

    static boolean absorbBomb(Level level, BlockPos center, int amount) {
        if (level.isClientSide) {
            return false;
        }
        var firstEntropinnyum = BlockPos.betweenClosedStream(new AABB(center).inflate(RANGE))
                .map(pos -> level.getBlockEntity(pos, BotaniaFlowerBlocks.ENTROPINNYUM))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(f -> f.getMana() == 0)
                .findFirst();
        if (firstEntropinnyum.isPresent()) {
            IEntropinnyum entropinnyum = (IEntropinnyum) firstEntropinnyum.get();
            entropinnyum.gto$fillExceededMaxMana(amount);
            level.playSound(null, center, BotaniaSounds.entropinnyumHappy, SoundSource.BLOCKS);
            level.removeBlock(center, false);
            return true;
        }
        return false;
    }

    void gto$fillExceededMaxMana(int amount);
}
