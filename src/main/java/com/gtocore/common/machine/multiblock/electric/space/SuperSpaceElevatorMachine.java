package com.gtocore.common.machine.multiblock.electric.space;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.utils.DummyWorld;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SuperSpaceElevatorMachine extends SpaceElevatorMachine {

    private int megaModuleCount;

    public SuperSpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.module.base", moduleCount - megaModuleCount));
        textList.add(Component.translatable("gtocore.machine.module.mega", megaModuleCount));
    }

    @Override
    protected void updateModuleCount() {
        moduleCount = 0;
        megaModuleCount = 0;
        for (var module : modules) {
            if (module instanceof SpaceElevatorModuleMachine moduleMachine && moduleMachine.getController() == this && moduleMachine.isFormed()) {
                moduleCount++;
                if (moduleMachine instanceof MegaSpaceElevatorModuleMachine) {
                    megaModuleCount++;
                }
            }
        }
    }

    @Override
    public int getMaxSpoolCount() {
        return 1024;
    }

    @Override
    int getBaseHigh() {
        return getLevel() instanceof DummyWorld ? 300 : 80;
    }

    @Override
    void clientTick() {
        super.clientTick();
        if (getRecipeLogic().isWorking()) high = 8 * getBaseHigh() + 400 + ((400 + getBaseHigh()) * MathUtil.sin(getOffsetTimer() / 160.0F));
    }
}
