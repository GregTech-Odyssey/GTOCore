package com.gtocore;

import com.gtocore.client.ClientProxy;
import com.gtocore.common.CommonProxy;

import com.gtolib.GTOCore;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(GTOCore.MOD_ID)
public final class Core {

    public Core() {
        DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
