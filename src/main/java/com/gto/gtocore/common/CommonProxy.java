package com.gto.gtocore.common;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.entity.IEnhancedPlayer;
import com.gto.gtocore.common.data.*;
import com.gto.gtocore.common.forge.ForgeCommonEvent;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.data.Data;
import com.gto.gtocore.integration.ae2.InfinityCellGuiHandler;
import com.gto.gtocore.integration.ae2.storage.InfinityCellHandler;
import com.gto.gtocore.integration.ftbquests.EMIRecipeModHelper;
import com.gto.gtocore.utils.register.ItemRegisterUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import com.gregtechceu.gtceu.common.unification.material.MaterialRegistryManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import appeng.api.networking.pathing.ChannelMode;
import appeng.api.storage.StorageCells;
import appeng.core.AEConfig;
import com.hepdd.gtmthings.common.item.AdvancedTerminalBehavior;
import earth.terrarium.adastra.api.events.AdAstraEvents;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class CommonProxy {

    public CommonProxy() {
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(eventBus);
        GTOFluids.FLUID_TYPE.register(eventBus);
        GTOFluids.FLUID.register(eventBus);
        eventBus.addListener(CommonProxy::commonSetup);
        eventBus.addListener(CommonProxy::addMaterials);
        eventBus.addListener(CommonProxy::registerMaterialRegistry);
        eventBus.addGenericListener(RecipeConditionType.class, CommonProxy::registerRecipeConditions);
        eventBus.addGenericListener(DimensionMarker.class, CommonProxy::registerDimensionMarkers);
        MinecraftForge.EVENT_BUS.register(ForgeCommonEvent.class);
        MinecraftForge.EVENT_BUS.register(ForgeCommonEvent.FoodHurtAnimalLogic.class);
    }

    private static void init() {
        GTOCreativeModeTabs.init();
        GTOConfig.init();
        GTOEntityTypes.init();
        GTONet.init();
        if (GTCEu.isDev() || GTCEu.isDataGen()) {
            GTOConfig.INSTANCE.dev = true;
            GTOConfig.INSTANCE.enablePrimitiveVoidOre = true;
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ItemRegisterUtils::InitUpgrades);
        StorageCells.addCellHandler(InfinityCellHandler.INSTANCE);
        StorageCells.addCellGuiHandler(new InfinityCellGuiHandler());
        if (GTOConfig.getDifficulty() == 3) AEConfig.instance().setChannelModel(ChannelMode.DEFAULT);

        FusionReactorMachine.registerFusionTier(GTValues.UHV, " (MKIV)");
        FusionReactorMachine.registerFusionTier(GTValues.UEV, " (MKV)");

        AdAstraEvents.OxygenTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.AcidRainTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.TemperatureTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.EntityGravityEvent.register(IEnhancedPlayer::gravity);

        AdvancedTerminalBehavior.AutoBuildSetting.HATCH_NAMES.add("thread_hatch");
        AdvancedTerminalBehavior.AutoBuildSetting.HATCH_NAMES.add("accelerate_hatch");
        AdvancedTerminalBehavior.AutoBuildSetting.HATCH_NAMES.add("programmablec_hatch");
        AdvancedTerminalBehavior.AutoBuildSetting.HATCH_NAMES.add("gravity_hatch");
        AdvancedTerminalBehavior.AutoBuildSetting.HATCH_NAMES.add("vacuum_hatch");

        if (GTCEu.isProd() && GTCEu.Mods.isEMILoaded()) EMIRecipeModHelper.setRecipeModHelper();

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

    private static void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType<?>> event) {
        GTORecipeConditions.init();
    }

    private static void registerDimensionMarkers(GTCEuAPI.RegisterEvent<ResourceLocation, DimensionMarker> event) {
        GTODimensionMarkers.init();
    }
}
