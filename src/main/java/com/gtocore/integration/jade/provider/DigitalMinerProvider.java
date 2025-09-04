package com.gtocore.integration.jade.provider;

import com.gtocore.common.machine.multiblock.electric.miner.DigitalMiner;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@DataGeneratorScanned
public class DigitalMinerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {}

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity && blockEntity.getMetaMachine() instanceof DigitalMiner miner) {
            var progressNbt = new CompoundTag();
            progressNbt.putBoolean("Active", miner.isActive());
            progressNbt.putInt("Progress", miner.getRecipeLogic().isWaiting() ? 0 :
                    (int) (miner.getOffsetTimer() % miner.getSpeed()));
            progressNbt.putInt("MaxProgress", miner.getSpeed());
            var wrapped = new CompoundTag();
            wrapped.put("null", progressNbt);
            compoundTag.put(GTCEu.id("workable_provider").toString(), wrapped);

            compoundTag.putInt("parallel", miner.getParallelMining());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id(DigitalMinerProvider.class.getSimpleName());
    }
}
