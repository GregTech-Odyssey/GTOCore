package com.gto.gtocore.integration.jade;

import com.gto.gtocore.integration.jade.provider.ParallelProvider;

import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class GTOJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ParallelProvider(), BlockEntity.class);
    }
}
