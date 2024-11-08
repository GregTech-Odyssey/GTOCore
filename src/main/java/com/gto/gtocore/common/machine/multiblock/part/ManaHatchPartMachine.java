package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.machine.IManaContainerMachine;
import com.gto.gtocore.api.machine.trait.NotifiableManaContainer;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ManaHatchPartMachine extends TieredIOPartMachine implements IManaContainerMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    public final NotifiableManaContainer manaContainer;

    public ManaHatchPartMachine(IMachineBlockEntity holder, int tier, IO io, int rate, Object... args) {
        super(holder, tier, io);
        this.manaContainer = createManaContainer(rate);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected NotifiableManaContainer createManaContainer(int rate) {
        long tierMana = (long) tier * tier * rate;
        if (io == IO.OUT) {
            return new NotifiableManaContainer(this, IO.OUT, 64 * tierMana, tierMana);
        } else return new NotifiableManaContainer(this, IO.IN, 64 * tierMana, tierMana);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }
}
