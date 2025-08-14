package com.gtocore.common;

import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.*;
import com.gtocore.common.forge.ForgeCommonEvent;
import com.gtocore.config.GTOConfig;
import com.gtocore.config.SparkRange;
import com.gtocore.data.Data;
import com.gtocore.data.Datagen;
import com.gtocore.integration.ftbquests.EMIRecipeModHelper;
import com.gtocore.integration.ftbu.AreaShape;
import com.gtocore.integration.gtmt.AdvancedTerminalBehavior;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.registries.ScanningClass;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import com.gregtechceu.gtceu.common.unification.material.MaterialRegistryManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import appeng.api.networking.pathing.ChannelMode;
import appeng.core.AEConfig;
import com.hepdd.gtmthings.data.CustomItems;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

import java.util.Arrays;

import static com.gregtechceu.gtceu.common.data.GTDimensionMarkers.createAndRegister;
import static com.gtolib.api.registries.GTORegistration.GTO;

public class CommonProxy {

    private static Throwable exception;

    public CommonProxy() {
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GTO.registerEventListeners(eventBus);
        GTOFluids.FLUID_TYPE.register(eventBus);
        GTOFluids.FLUID.register(eventBus);
        GTOEffects.init(eventBus);
        GTORecipeSerializers.init(eventBus);
        eventBus.addListener(CommonProxy::commonSetup);
        eventBus.addListener(CommonProxy::addMaterials);
        eventBus.addListener(CommonProxy::registerMaterialRegistry);
        eventBus.addListener(Datagen::onGatherData);
        eventBus.addGenericListener(DimensionMarker.class, CommonProxy::registerDimensionMarkers);
        eventBus.addGenericListener(GTRecipeCategory.class, CommonProxy::registerRecipeCategory);
        MinecraftForge.EVENT_BUS.register(ForgeCommonEvent.class);
    }

    private static void init() {
        GTOCreativeModeTabs.init();
        GTOConfig.init();
        ScanningClass.init();
        GTOEntityTypes.init();
        GTONet.init();
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        BlockMap.build();
        if (GTOCore.isExpert()) AEConfig.instance().setChannelModel(ChannelMode.DEFAULT);

        CustomItems.ADVANCED_TERMINAL.get().getComponents().clear();
        CustomItems.ADVANCED_TERMINAL.get().getComponents().add(new AdvancedTerminalBehavior());

        FusionReactorMachine.registerFusionTier(GTValues.UHV, " (MKIV)");
        FusionReactorMachine.registerFusionTier(GTValues.UEV, " (MKV)");

        AdAstraEvents.OxygenTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.AcidRainTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.TemperatureTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.EntityGravityEvent.register(IEnhancedPlayer::gravity);

        if (GTCEu.isProd()) {
            AreaShape.register();
            EMIRecipeModHelper.setRecipeModHelper();
        }

        if (GTCEu.isClientSide()) {
            Thread thread = new Thread(Data::asyncInit, "GTOCore Data");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    private static void addMaterials(MaterialEvent event) {
        GTOMaterials.init();
    }

    private static void registerMaterialRegistry(MaterialRegistryEvent event) {
        MaterialRegistryManager.getInstance().createRegistry(GTOCore.MOD_ID);
    }

    private static void registerDimensionMarkers(GTCEuAPI.RegisterEvent<ResourceLocation, DimensionMarker> event) {
        Arrays.stream(Dimension.values()).filter(d -> !d.getLocation().getNamespace().equals("minecraft")).forEach(d -> {
            createAndRegister(d.getLocation(), d.getTier(), d.getItemKey(), "gtocore.dimension." + d.getLocation().getPath());
        });
    }

    private static void registerRecipeCategory(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeCategory> event) {
        GTORecipeCategories.init();
    }

    public static void afterStartup() {
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        ScanningClass.VALUES = null;
        ModList.get().getAllScanData().clear();
        if (GTOConfig.INSTANCE.startSpark == SparkRange.MAIN_MENU) {
            SparkLaunchProfiler.stop("all");
        }
    }

    public static void setException(Exception e) {
        exception = e;
    }
}
