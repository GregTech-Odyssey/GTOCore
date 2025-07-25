package com.gtocore.client;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.client.forge.GTOComponentHandler;
import com.gtocore.client.renderer.item.MonitorItemDecorations;
import com.gtocore.common.CommonProxy;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.forge.render.GTOComponentRegistry;
import com.gtocore.common.machine.monitor.MonitorBlockItem;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
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
        eventBus.addListener(ClientProxy::registerItemDeco);
        eventBus.register(GTOComponentRegistry.class);
        MinecraftForge.EVENT_BUS.register(ForgeClientEvent.class);
        MinecraftForge.EVENT_BUS.register(GTOComponentHandler.class);
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

    public static void registerItemDeco(RegisterItemDecorationsEvent event) {
        MonitorBlockItem.getItemList().forEach(item -> {
            if (item != null) {
                event.register(BuiltInRegistries.BLOCK.get(item), MonitorItemDecorations.DECORATOR);
            }
        });
    }
}
