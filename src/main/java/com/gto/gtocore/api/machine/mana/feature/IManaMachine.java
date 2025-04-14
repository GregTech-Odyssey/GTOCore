package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import com.google.common.base.Predicates;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;

public interface IManaMachine extends ManaCollector, IMachineFeature, SparkAttachable {

    @NotNull
    IManaContainer getManaContainer();

    @Override
    default Level getManaReceiverLevel() {
        return self().getLevel();
    }

    @Override
    default BlockPos getManaReceiverPos() {
        return self().getPos();
    }

    @Override
    default void onClientDisplayTick() {}

    @Override
    default float getManaYieldMultiplier(ManaBurst burst) {
        return 1;
    }

    @Override
    default int getMaxMana() {
        return GTMath.saturatedCast(getManaContainer().getMaxMana());
    }

    @Override
    default int getCurrentMana() {
        return GTMath.saturatedCast(getManaContainer().getCurrentMana());
    }

    @Override
    default boolean isFull() {
        return getManaContainer().getMaxMana() <= getManaContainer().getCurrentMana();
    }

    @Override
    default void receiveMana(int mana) {
        if (mana > 0) {
            getManaContainer().addMana(mana, 1, false);
        } else if (mana < 0) {
            getManaContainer().removeMana(-mana, 1, false);
        }
    }

    @Override
    default boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    default int getAvailableSpaceForMana() {
        Level level = self().getLevel();
        if (level == null) return 0;
        int space = Math.max(0, GTMath.saturatedCast(getManaContainer().getMaxMana() - getManaContainer().getCurrentMana()));
        if (space > 0) {
            return space;
        } else if (level.getBlockState(self().getPos().below()).is(BotaniaBlocks.manaVoid)) {
            return getMaxMana();
        } else {
            return 0;
        }
    }

    @Override
    default ManaSpark getAttachedSpark() {
        Level level = self().getLevel();
        if (level == null) return null;
        List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(self().getPos().above(), self().getPos().above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ManaSpark) e;
        }
        return null;
    }

    @Override
    default boolean areIncomingTranfersDone() {
        return false;
    }
}
