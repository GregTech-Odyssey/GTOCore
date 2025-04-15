package com.gto.gtocore.api.capability;

import com.gto.gtocore.api.machine.IIWirelessInteractorMachine;
import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Set;

public interface IManaContainer extends IIWirelessInteractorMachine<ManaDistributorMachine> {

    @Override
    default Level getLevel() {
        return getMachine().getLevel();
    }

    @Override
    default Map<ResourceLocation, Set<ManaDistributorMachine>> getMachineNet() {
        return ManaDistributorMachine.NETWORK;
    }

    @Override
    default boolean firstTestMachine(ManaDistributorMachine machine) {
        if (!acceptDistributor()) return false;
        BlockPos pos = getMachine().getPos();
        Level level = machine.getLevel();
        if (level == null) return false;
        return machine.isFormed() && machine.add(pos);
    }

    @Override
    default boolean testMachine(ManaDistributorMachine machine) {
        return machine.isFormed() && machine.isWorkingEnabled();
    }

    @Override
    default void removeNetMachineCache() {
        ManaDistributorMachine distributor = getNetMachineCache();
        if (distributor != null) {
            distributor.remove();
            setNetMachineCache(null);
        }
    }

    boolean acceptDistributor();

    MetaMachine getMachine();

    long getMaxMana();

    long getCurrentMana();

    /**
     * {@link IManaContainer} 接口内部使用
     */
    void setCurrentMana(long mana);

    default long getMaxProductionRate() {
        return getMaxMana();
    }

    default long getMaxConsumptionRate() {
        return getMaxMana();
    }

    /**
     * 尝试增加魔力，结果不超过最大魔力总量
     * 
     * @param amount         欲输入量
     * @param rateMultiplier 速率乘数
     * @param simulate       是否模拟
     * @return 变化量
     */
    default long addMana(long amount, int rateMultiplier, boolean simulate) {
        long change = Math.min(getMaxMana() - getCurrentMana(), Math.min((long) rateMultiplier * getMaxProductionRate(), amount));
        if (change <= 0) return 0;
        if (!simulate) setCurrentMana(getCurrentMana() + change);
        return change;
    }

    /**
     * 尝试减少魔力，结果不低于0
     * 
     * @param amount         欲输出量
     * @param rateMultiplier 速率乘数
     * @param simulate       是否模拟
     * @return 变化量
     */
    default long removeMana(long amount, int rateMultiplier, boolean simulate) {
        long change = Math.min(getCurrentMana(), Math.min((long) rateMultiplier * getMaxConsumptionRate(), amount));
        if (change <= 0) return 0;
        if (!simulate) setCurrentMana(getCurrentMana() - change);
        return change;
    }
}
