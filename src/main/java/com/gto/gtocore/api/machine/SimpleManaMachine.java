package com.gto.gtocore.api.machine;

import com.gto.gtocore.api.machine.trait.NotifiableManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import lombok.Getter;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleManaMachine extends SimpleNoEnergyMachine implements IManaContainerMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleManaMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @DescSynced
    private final NotifiableManaContainer manaContainer;

    public SimpleManaMachine(IMachineBlockEntity holder, int tier, Int2LongFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        manaContainer = createManaContainer(args);
    }

    protected NotifiableManaContainer createManaContainer(Object... args) {
        long tierMana = (long) tier * tier;
        if (isManaEmitter()) {
            return new NotifiableManaContainer(this, IO.OUT, 64 * tierMana, tierMana);
        } else return new NotifiableManaContainer(this, IO.IN, 64 * tierMana, tierMana);
    }

    protected boolean isManaEmitter() {
        return false;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INFO_ICON, () -> List.of(Component.translatable("gtocore.machine.mana_stored", manaContainer.getManaStored())), () -> true, () -> null));
    }
}
