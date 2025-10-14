package com.gtocore.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientSideHandler {

    /**
     * 这个方法将被我们的 Mixin 通过反射调用。
     * 它包含了所有我们想在客户端执行的代码。
     */
    public static void runClientSideInitialization() {
        appbot.forge.client.AppliedBotanicsClient.initialize();
    }
}
