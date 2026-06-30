package com.gtocore.api.ae2.stacks;

import com.gtolib.utils.MathUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;

import appbot.ae2.ManaKey;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.mana.ManaPool;

import java.util.Optional;
import java.util.function.LongSupplier;

public final class AEManaKeyHandler implements ManaPool {

    @Setter
    private AEKeyMap<AEKey> map;
    @Setter
    private long capacity;
    @Setter
    private LongSupplier storageSupplier;
    @Setter
    private Runnable onChange;

    @Setter
    @Nullable
    private Level level;

    @Setter
    @Nullable
    private BlockPos pos;

    @Override
    public Level getManaReceiverLevel() {
        return this.level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.pos;
    }

    @Override
    public int getCurrentMana() {
        if (map == null) return 0;
        return MathUtil.saturatedCast(map.getAmount(ManaKey.KEY));
    }

    @Override
    public boolean isFull() {
        if (storageSupplier == null) return true;
        return storageSupplier.getAsLong() >= capacity;
    }

    @Override
    public void receiveMana(int mana) {
        if (map == null || mana == 0) return;
        if (mana > 0) {
            map.insert(ManaKey.KEY, mana);
        } else {
            map.extract(ManaKey.KEY, -mana);
        }
        onChange.run();
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        if (storageSupplier == null) return false;
        return storageSupplier.getAsLong() < capacity;
    }

    @Override
    public boolean isOutputtingPower() {
        return false;
    }

    @Override
    public int getMaxMana() {
        if (storageSupplier == null) return 0;
        return MathUtil.saturatedCast(Math.max(0, (capacity - storageSupplier.getAsLong()) * 62));
    }

    @Override
    public Optional<DyeColor> getColor() {
        return Optional.of(DyeColor.GRAY);
    }

    @Override
    public void setColor(Optional<DyeColor> color) {}
}
