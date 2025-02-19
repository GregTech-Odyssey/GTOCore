package com.gto.gtocore.client;

import com.gto.gtocore.client.forge.ForgeClientEvent;
import com.gto.gtocore.common.CommonProxy;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOFluids;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public final class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ClientProxy::clientSetup);
        MinecraftForge.EVENT_BUS.register(ForgeClientEvent.class);
    }

    private static void init() {
        CraftingUnitModelProvider.initCraftingUnitModels();
        KeyBind.init();
    }

    @SuppressWarnings("all")
    private static void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_1M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_4M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_16M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_64M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_256M.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOBlocks.CRAFTING_STORAGE_MAX.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GTOFluids.GELID_CRYOTHEUM.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(GTOFluids.FLOWING_GELID_CRYOTHEUM.get(), RenderType.translucent());
    }
}
