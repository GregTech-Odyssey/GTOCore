package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.CoilCustomParallelMultiblockMachine;
import com.gtolib.api.misc.ManaContainerList;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ManaAlloyBlastSmelterMachine extends CoilCustomParallelMultiblockMachine implements IManaMultiblock {

    private static final Map<Integer, Item> RUNES = Map.of(
            1, RegistriesUtils.getItem("botania:rune_water"),
            2, RegistriesUtils.getItem("botania:rune_fire"),
            3, RegistriesUtils.getItem("botania:rune_air"),
            4, RegistriesUtils.getItem("botania:rune_earth"),
            5, RegistriesUtils.getItem("botania:rune_spring"),
            6, RegistriesUtils.getItem("botania:rune_summer"),
            7, RegistriesUtils.getItem("botania:rune_autumn"),
            8, RegistriesUtils.getItem("botania:rune_winter"));

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaAlloyBlastSmelterMachine.class, CoilCustomParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private int tick;

    @Persisted
    private int time;

    @Persisted
    private int signal;

    private int mana;

    private final ManaTrait manaTrait;

    public ManaAlloyBlastSmelterMachine(MetaMachineBlockEntity holder) {
        super(holder, true, true, true, m -> 8);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        mana = 1 << getTier();
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        return signal;
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            tick++;
            if (time > 1) {
                time--;
                if (signal > 0) {
                    Item item = RUNES.get(signal);
                    AtomicBoolean success = new AtomicBoolean(false);
                    forEachInputItems(stack -> {
                        if (RUNES.containsValue(stack.getItem()) && inputItem(item.getDefaultInstance()) && stack.is(item)) {
                            success.set(true);
                            return true;
                        }
                        return false;
                    });
                    if (success.get()) {
                        signal = 0;
                        updateSignal();
                        mana = 1 << getTier();
                        time = 0;
                    }
                }
            } else if (time == 1) {
                mana <<= 2;
                time = 0;
            }
            if (tick > 1200) {
                tick = 0;
                signal = GTValues.RNG.nextInt(8) + 1;
                time = 200;
                updateSignal();
            }
            if (getOffsetTimer() % 20 == 0 && removeMana(mana, 1, true) != mana) return false;
            removeMana(mana, 1, false);
            return true;
        }
        return false;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        return super.beforeWorking(recipe) && removeMana(mana, 1, false) == mana;
    }

    @Override
    public void afterWorking() {
        signal = 0;
        updateSignal();
        super.afterWorking();
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }
}
