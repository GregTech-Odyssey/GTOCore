package com.gtocore.common;

import com.gtocore.api.data.Algae;
import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.client.KeyMessage;
import com.gtocore.client.Message;
import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.*;
import com.gtocore.common.data.translation.GTOItemTooltips;
import com.gtocore.common.forge.ForgeCommonEvent;
import com.gtocore.common.machine.tesseract.TesseractDirectedTarget;
import com.gtocore.common.syncdata.GTORecipePayload;
import com.gtocore.common.syncdata.GenericStackPayload;
import com.gtocore.config.GTOConfig;
import com.gtocore.config.SparkRange;
import com.gtocore.data.Data;
import com.gtocore.data.Datagen;
import com.gtocore.data.lootTables.GTOLootTool.GTONumberProviders;
import com.gtocore.eio_travel.api.TravelRegistry;
import com.gtocore.eio_travel.client.travel.TravelAnchorRenderers;
import com.gtocore.eio_travel.implementations.AnchorTravelTarget;
import com.gtocore.eio_travel.implementations.PatternTravelTarget;
import com.gtocore.integration.Mods;
import com.gtocore.integration.ae.PatternContentAccessTerminalMenu;
import com.gtocore.integration.construction_wand.ConstructionWandRegistrar;
import com.gtocore.integration.ftbquests.EMIRecipeModHelper;
import com.gtocore.integration.ftbquests.GTOQuestTypes;
import com.gtocore.integration.ftbu.AreaShape;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.Wireless;
import com.gtolib.api.ae2.me2in1.emi.CategoryMappingSubMenu;
import com.gtolib.api.ae2.stacks.TagPrefixKeyType;
import com.gtolib.api.item.IItem;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.registries.ScanningClass;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTSyncedFieldAccessors;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import com.gregtechceu.gtceu.syncdata.*;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import appeng.api.features.GridLinkables;
import appeng.api.networking.pathing.ChannelMode;
import appeng.api.stacks.AEKeyTypes;
import appeng.api.stacks.GenericStack;
import appeng.core.AEConfig;
import appeng.hotkeys.HotkeyActions;
import appeng.items.tools.powered.WirelessTerminalItem;
import com.lowdragmc.lowdraglib.syncdata.payload.FriendlyBufPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.TextConstants;
import de.mari_023.ae2wtlib.hotkeys.Ae2WTLibLocatingService;
import de.mari_023.ae2wtlib.terminal.IUniversalWirelessTerminalItem;
import de.mari_023.ae2wtlib.wut.WTDefinition;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

import java.util.function.Supplier;

import static com.gtolib.api.registries.GTORegistration.GTO;
import static com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries.register;
import static com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries.registerSimple;
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
        GTONumberProviders.NUMBER_PROVIDERS.register(eventBus);
        eventBus.addListener(EventPriority.HIGHEST, CommonProxy::commonSetup);
        eventBus.addListener(CommonProxy::initMenu);
        eventBus.addListener(Datagen::onGatherData);
        eventBus.addListener(CommonProxy::modConstruct);
        ForgeCommonEvent.init();
    }

    public static void earlyStartup() {
        GTSyncedFieldAccessors.EVENT.removeListener(GTSyncedFieldAccessors.class);
        GTSyncedFieldAccessors.EVENT.addListener(CommonProxy.class, () -> {
            register(FriendlyBufPayload.class, FriendlyBufPayload::new, GTSyncedFieldAccessors.GT_RECIPE_TYPE_ACCESSOR, 1000);
            register(NbtTagPayload.class, NbtTagPayload::new, VirtualTankAccessor.INSTANCE, 2);
            register(NbtTagPayload.class, NbtTagPayload::new, VirtualItemStorageAccessor.INSTANCE, 2);
            register(NbtTagPayload.class, NbtTagPayload::new, VirtualRedstoneAccessor.INSTANCE, 2);
            registerSimple(MaterialPayload.class, MaterialPayload::new, Material.class, 1);
            registerSimple(GTORecipePayload.class, GTORecipePayload::new, GTRecipe.class, 100);
            registerSimple(FluidStackPayload.class, FluidStackPayload::new, FluidStack.class, -1);
            registerSimple(TesseractDirectedTarget.Payload.class, TesseractDirectedTarget.Payload::new, TesseractDirectedTarget.class, 10);
            registerSimple(GenericStackPayload.class, GenericStackPayload::new, GenericStack.class, 10);
        });
    }

    private static void init() {
        GTOCreativeModeTabs.init();
        GTOEntityTypes.init();
        if (!GTCEu.isDataGen() && Mods.FTBQUESTS.isLoaded()) {
            GTOQuestTypes.init();
        }
    }

    private static void modConstruct(FMLConstructModEvent event) {
        Datagen.init();
        event.enqueueWork(() -> HotkeyActions.register(new Ae2WTLibLocatingService(Wireless.ID), Wireless.ID + "_locating_service"));
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        Data.init();
        BlockMap.build();
        GTOPartAbility.init();
        Algae.init();
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
        if (Mods.CONSTRUCTION_WAND.isLoaded()) {
            ConstructionWandRegistrar.register();
        }
        TravelRegistry.addTravelEntry(AnchorTravelTarget.SERIALIZED_NAME, AnchorTravelTarget::load, () -> TravelAnchorRenderers::getRenderer);
        TravelRegistry.addTravelEntry(PatternTravelTarget.SERIALIZED_NAME, PatternTravelTarget::loadClientTarget, () -> TravelAnchorRenderers::getRenderer);

        if (GTCEu.isProd()) {
            AreaShape.register();
            EMIRecipeModHelper.setRecipeModHelper();
        }

        if (GTCEu.isClientSide()) {
            Supplier<Component>[] tooltips = new Supplier[] { () -> Component.translatable(GTOTagPrefix.PIPE_TOOLTIP) };
            GTMaterialBlocks.ITEM_PIPE_BLOCKS.values().forEach(e -> ((IItem) e.get().asItem()).gtolib$setToolTips(tooltips));
            GTMaterialBlocks.FLUID_PIPE_BLOCKS.values().forEach(e -> ((IItem) e.get().asItem()).gtolib$setToolTips(tooltips));
        } else {
            KeyMessage.init();
            Message.init();
        }
        GTOItemTooltips.INSTANCE.initLanguage();
    }

    public static void afterStartup() {
        ScanningClass.VALUES = null;
        ModList.get().getAllScanData().clear();
        if (GTOConfig.INSTANCE.devMode.startSpark == SparkRange.MAIN_MENU) {
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
            Registry.<MenuType<?>>register(BuiltInRegistries.MENU, GTOCore.id("pattern_content_access_terminal").toString(), PatternContentAccessTerminalMenu.TYPE);
            Registry.<MenuType<?>>register(BuiltInRegistries.MENU, GTOCore.id("me2in1wireless").toString(), Wireless.TYPE);
            Registry.<MenuType<?>>register(BuiltInRegistries.MENU, GTOCore.id("category_mapping_sub_menu").toString(), CategoryMappingSubMenu.TYPE);
        }
        if (event.getRegistryKey() == Registries.BLOCK) {
            AEKeyTypes.register(TagPrefixKeyType.TYPE);
        }
    }
}
