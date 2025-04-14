package com.gto.gtocore;

import com.gto.gtocore.api.playerskill.api.dataGeneration.DataGeneration;
import com.gto.gtocore.client.ClientProxy;
import com.gto.gtocore.common.CommonProxy;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GTOCore.MOD_ID)
public final class GTOCore {

    public static final String MOD_ID = "gtocore";
    public static final String NAME = "GTO Core";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public GTOCore() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
