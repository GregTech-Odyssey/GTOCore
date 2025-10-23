package com.gtocore.common;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.*;
import com.gtocore.common.data.translation.GTOItemTooltips;
import com.gtocore.common.forge.ForgeCommonEvent;
import com.gtocore.config.GTOConfig;
import com.gtocore.config.SparkRange;
import com.gtocore.data.Data;
import com.gtocore.data.Datagen;
import com.gtocore.integration.ftbquests.EMIRecipeModHelper;
import com.gtocore.integration.ftbu.AreaShape;

import com.gtolib.GTOCore;
import com.gtolib.IItem;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.Wireless;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.registries.ScanningClass;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import appeng.api.features.GridLinkables;
import appeng.api.networking.pathing.ChannelMode;
import appeng.core.AEConfig;
import appeng.hotkeys.HotkeyActions;
import appeng.items.tools.powered.WirelessTerminalItem;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.TextConstants;
import de.mari_023.ae2wtlib.hotkeys.Ae2WTLibLocatingService;
import de.mari_023.ae2wtlib.terminal.IUniversalWirelessTerminalItem;
import de.mari_023.ae2wtlib.wut.WTDefinition;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.common.data.GTDimensionMarkers.createAndRegister;
import static com.gtolib.api.registries.GTORegistration.GTO;
import static de.mari_023.ae2wtlib.wut.WUTHandler.terminalNames;
import static de.mari_023.ae2wtlib.wut.WUTHandler.wirelessTerminals;

public class CommonProxy {

    public CommonProxy() {
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GTO.registerEventListeners(eventBus);
        GTOFluids.FLUID_TYPE.register(eventBus);
        GTOFluids.FLUID.register(eventBus);
        GTOEffects.init(eventBus);
        eventBus.addListener(CommonProxy::commonSetup);
        eventBus.addListener(CommonProxy::initMenu);
        eventBus.addListener(Datagen::onGatherData);
        eventBus.addListener(CommonProxy::modConstruct);
        eventBus.addGenericListener(DimensionMarker.class, CommonProxy::registerDimensionMarkers);
        eventBus.addGenericListener(GTRecipeCategory.class, CommonProxy::registerRecipeCategory);
        ForgeCommonEvent.init();
    }

    private static void init() {
        GTOCreativeModeTabs.init();
        ScanningClass.init();
        GTOEntityTypes.init();
        GTONet.init();
    }

    private static void modConstruct(FMLConstructModEvent event) {
        Datagen.init();
        event.enqueueWork(() -> HotkeyActions.register(new Ae2WTLibLocatingService(Wireless.ID), Wireless.ID + "_locating_service"));
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        BlockMap.build();
        GTOPartAbility.init();
        if (GTOCore.isExpert()) {
            AEConfig.instance().setChannelModel(ChannelMode.DEFAULT);
        } else {
            AEConfig.instance().setChannelModel(ChannelMode.INFINITE);
        }

        FusionReactorMachine.registerFusionTier(GTValues.UHV, " (MKIV)");
        FusionReactorMachine.registerFusionTier(GTValues.UEV, " (MKV)");

        AdAstraEvents.OxygenTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.AcidRainTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.TemperatureTickEvent.register(IEnhancedPlayer::spaceTick);
        AdAstraEvents.EntityGravityEvent.register(IEnhancedPlayer::gravity);

        initWTLib();

        if (GTCEu.isProd()) {
            AreaShape.register();
            EMIRecipeModHelper.setRecipeModHelper();
        }

        if (GTCEu.isClientSide()) {
            Thread thread = new Thread(Data::asyncInit, "GTOCore Data");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();

            Supplier<Component>[] tooltips = new Supplier[] { () -> Component.translatable(GTOTagPrefix.PIPE_TOOLTIP) };
            GTMaterialBlocks.ITEM_PIPE_BLOCKS.values().forEach(e -> ((IItem) e.get().asItem()).gtolib$setToolTips(tooltips));
            GTMaterialBlocks.FLUID_PIPE_BLOCKS.values().forEach(e -> ((IItem) e.get().asItem()).gtolib$setToolTips(tooltips));
        }
        GTOItemTooltips.INSTANCE.initLanguage();
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
        ScanningClass.VALUES = null;
        ModList.get().getAllScanData().clear();
        if (GTOConfig.INSTANCE.startSpark == SparkRange.MAIN_MENU) {
            SparkLaunchProfiler.stop("all");
        }
    }

    private static void initWTLib() {
        GridLinkables.register(GTOItems.WIRELESS_ME2IN1, WirelessTerminalItem.LINKABLE_HANDLER);
        ItemStack wut = new ItemStack(AE2wtlib.UNIVERSAL_TERMINAL);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(Wireless.ID, true);
        wut.setTag(tag);
        wirelessTerminals.put(Wireless.ID, new WTDefinition(
                ((IUniversalWirelessTerminalItem) GTOItems.WIRELESS_ME2IN1.get())::tryOpen, Wireless.Host::new, Wireless.TYPE, GTOItems.WIRELESS_ME2IN1.get(), wut,
                TextConstants.formatTerminalName("gtocore.ae.appeng.me2in1.wireless")));
        terminalNames.add(Wireless.ID);
    }

    private static void initMenu(RegisterEvent event) {
        // Initialize the menu registry
        if (event.getRegistryKey() == Registries.MENU) {
            Registry.<MenuType<?>>register(BuiltInRegistries.MENU, GTOCore.id("me2in1").toString(), Me2in1Menu.TYPE);
            Registry.<MenuType<?>>register(BuiltInRegistries.MENU, GTOCore.id("me2in1wireless").toString(), Wireless.TYPE);
        }
    }
}
