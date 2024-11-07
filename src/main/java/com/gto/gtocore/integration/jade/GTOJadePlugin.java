package com.gto.gtocore.integration.jade;

import com.gto.gtocore.integration.jade.provider.ManaContainerBlockProvider;
import com.gto.gtocore.integration.jade.provider.ParallelProvider;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class GTOJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ParallelProvider(), BlockEntity.class);
        registration.registerBlockDataProvider(new ManaContainerBlockProvider(), BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ManaContainerBlockProvider(), Block.class);
    }
}
