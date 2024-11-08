package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.network.chat.Component;

import java.util.*;

public class WorkableManaMultiblockMachine extends WorkableNoEnergyMultiblockMachine {

    protected final Set<IManaContainer> manaContainers = new HashSet<>();

    public WorkableManaMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.manaContainers.clear();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (isGenerator()) {
            List<IRecipeHandler<?>> capabilities = capabilitiesProxy.get(IO.OUT, ManaRecipeCapability.CAP);
            if (capabilities != null) {
                for (IRecipeHandler<?> handler : capabilities) {
                    if (handler instanceof IManaContainer container) {
                        manaContainers.add(container);
                    }
                }
            }
        } else {
            List<IRecipeHandler<?>> capabilities = capabilitiesProxy.get(IO.IN, ManaRecipeCapability.CAP);
            if (capabilities != null) {
                for (IRecipeHandler<?> handler : capabilities) {
                    if (handler instanceof IManaContainer container) {
                        manaContainers.add(container);
                    }
                }
            }
        }
    }

    @Override
    public void onPartUnload() {
        super.onPartUnload();
        this.manaContainers.clear();
    }

    protected long getManaStored() {
        return manaContainers.stream().mapToLong(IManaContainer::getManaStored).sum();
    }

    protected long getMaxCapacity() {
        return manaContainers.stream().mapToLong(IManaContainer::getMaxCapacity).sum();
    }

    protected long getMaxConsumption() {
        return manaContainers.stream().mapToLong(IManaContainer::getMaxConsumption).sum();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (isFormed()) {
            textList.add(Component.translatable("gtocore.machine.mana_stored", getManaStored(), getMaxCapacity()));
            textList.add(Component.translatable("gtocore.machine.mana_consumption", getMaxConsumption()));
        }
        super.addDisplayText(textList);
    }

    public boolean isGenerator() {
        return getDefinition().isGenerator();
    }
}
