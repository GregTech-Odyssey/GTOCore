package com.gto.gtocore.client;

import com.gto.gtocore.common.CommonProxy;
import com.gto.gtocore.common.data.GTOEntityTypes;

import com.gregtechceu.gtceu.client.renderer.entity.GTExplosiveRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
    }

    public static void init() {
        CraftingUnitModelProvider.initCraftingUnitModels();
        KeyBind.init();
    }

    @SubscribeEvent
    public void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GTOEntityTypes.NUKE_BOMB.get(), GTExplosiveRenderer::new);
    }
}
