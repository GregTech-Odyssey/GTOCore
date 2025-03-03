package com.gto.gtocore.api.machine.mana.trait;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.trait.MultiblockTrait;
import com.gto.gtocore.common.machine.mana.part.ManaHatchPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Getter
public class ManaTrait extends MultiblockTrait {

    private final Set<IManaContainer> manaContainers = new ObjectOpenHashSet<>();

    public ManaTrait(MultiblockControllerMachine machine) {
        super((IMultiblockTraitHolder) machine);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        manaContainers.clear();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getMachine() instanceof WorkableMultiblockMachine workableMultiblockMachine) {
            if (((IManaMultiblock) machine).isGeneratorMana()) {
                List<IRecipeHandler<?>> capabilities = workableMultiblockMachine.getCapabilitiesProxy().get(IO.OUT, ManaRecipeCapability.CAP);
                if (capabilities != null) {
                    for (IRecipeHandler<?> handler : capabilities) {
                        if (handler instanceof IManaContainer container) {
                            manaContainers.add(container);
                        }
                    }
                }
            } else {
                List<IRecipeHandler<?>> capabilities = workableMultiblockMachine.getCapabilitiesProxy().get(IO.IN, ManaRecipeCapability.CAP);
                if (capabilities != null) {
                    for (IRecipeHandler<?> handler : capabilities) {
                        if (handler instanceof IManaContainer container) {
                            manaContainers.add(container);
                        }
                    }
                }
            }
        } else {
            for (IMultiPart part : getMachine().getParts()) {
                if (part instanceof ManaHatchPartMachine manaHatchPartMachine) {
                    NotifiableManaContainer container = manaHatchPartMachine.getManaContainer();
                    if (((IManaMultiblock) machine).isGeneratorMana()) {
                        if (container.getHandlerIO() == IO.OUT) manaContainers.add(manaHatchPartMachine.getManaContainer());
                    } else {
                        if (container.getHandlerIO() == IO.IN) manaContainers.add(manaHatchPartMachine.getManaContainer());
                    }
                }
            }
        }
    }

    private long getCurrentMana() {
        return manaContainers.stream().mapToLong(IManaContainer::getCurrentMana).sum();
    }

    private long getMaxMana() {
        return manaContainers.stream().mapToLong(IManaContainer::getMaxMana).sum();
    }

    private long getMaxConsumption() {
        return manaContainers.stream().mapToLong(IManaContainer::getMaxConsumption).sum();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.mana_stored", getCurrentMana(), getMaxMana()));
        textList.add(Component.translatable("gtocore.machine.mana_consumption", getMaxConsumption()));
    }
}
