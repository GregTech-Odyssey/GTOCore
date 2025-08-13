package com.gtocore.client;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.client.forge.GTOComponentHandler;
import com.gtocore.client.forge.GTOComponentRegistry;
import com.gtocore.client.forge.GTORender;
import com.gtocore.client.renderer.item.MonitorItemDecorations;
import com.gtocore.common.CommonProxy;
import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.machine.monitor.MonitorBlockItem;

import com.gtolib.ae2.me2in1.*;

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

import appeng.api.parts.PartModels;
import appeng.init.client.InitScreens;

@OnlyIn(Dist.CLIENT)
public final class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ClientProxy::clientSetup);
        eventBus.addListener(ClientProxy::registerItemDeco);
        eventBus.addListener(ClientProxy::registerMenuScreen);
        eventBus.register(GTOComponentRegistry.class);
        MinecraftForge.EVENT_BUS.register(ForgeClientEvent.class);
        MinecraftForge.EVENT_BUS.register(GTOComponentHandler.class);
        MinecraftForge.EVENT_BUS.register(GTORender.class);
        registerAEModels();
    }

    private static void init() {
        KeyBind.init();
    }

    @SuppressWarnings("all")
    private static void clientSetup(FMLClientSetupEvent event) {
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

    private static void registerMenuScreen(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            InitScreens.register(
                    Me2in1Menu.TYPE,
                    Me2in1Screen<Me2in1Menu>::new,
                    "/screens/ex_pattern_access_terminal.json");
            InitScreens.register(
                    Wireless.TYPE,
                    Wireless.Screen::new,
                    "/screens/ex_pattern_access_terminal.json");
        });
    }

    private static void registerAEModels() {
        PartModels.registerModels(Me2in1TerminalPart.MODELS);
    }
}
