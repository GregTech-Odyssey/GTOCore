package com.gto.gtocore.common;

import com.gto.gtocore.common.data.*;
import com.gto.gtocore.config.GTOConfigHolder;
import com.gto.gtocore.integration.ae2.InfinityCellGuiHandler;
import com.gto.gtocore.integration.ae2.storage.InfinityCellHandler;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import appeng.api.storage.StorageCells;
import appeng.core.AELog;
import earth.terrarium.adastra.api.events.AdAstraEvents;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class CommonProxy {

    public CommonProxy() {
        CommonProxy.init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(eventBus);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::addMaterials);
        eventBus.addGenericListener(RecipeConditionType.class, this::registerRecipeConditions);
        eventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        eventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
    }

    public static void init() {
        GTOCreativeModeTabs.init();
        GTOConfigHolder.init();
        GTOEntityTypes.init();

        AdAstraEvents.OxygenTickEvent.register((level, entity) -> !entity.getPersistentData().getBoolean("space_state"));
        AdAstraEvents.AcidRainTickEvent.register((level, entity) -> !entity.getPersistentData().getBoolean("space_state"));
        AdAstraEvents.TemperatureTickEvent.register((level, entity) -> !entity.getPersistentData().getBoolean("space_state"));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        StorageCells.addCellHandler(InfinityCellHandler.INSTANCE);
        StorageCells.addCellGuiHandler(new InfinityCellGuiHandler());
        event.enqueueWork(this::postRegistrationInitialization).whenComplete((res, err) -> {
            if (err != null) {
                AELog.warn(err);
            }
        });
    }

    public void postRegistrationInitialization() {
        GTOItems.InitUpgrades();
    }

    private void addMaterials(MaterialEvent event) {
        GTOMaterials.init();
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GTORecipeTypes.init();
    }

    private void registerRecipeConditions(GTCEuAPI.RegisterEvent<ResourceLocation, RecipeConditionType<?>> event) {
        GTORecipeConditions.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        GTOItems.init();
        GTOBlocks.init();
        GTOMachines.init();
    }
}
