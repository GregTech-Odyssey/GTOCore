package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.multiblock.IParallelMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class ParallelProvider implements IServerDataProvider<BlockAccessor> {

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity) {
            if (blockEntity.getMetaMachine() instanceof IParallelMachine parallelHatch) {
                compoundTag.putInt("parallel", parallelHatch.getParallel());
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id("parallel_info");
    }
}
