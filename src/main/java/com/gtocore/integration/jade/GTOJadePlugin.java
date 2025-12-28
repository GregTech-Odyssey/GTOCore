package com.gtocore.integration.jade;

import com.gtocore.integration.jade.provider.*;
import com.gtocore.integration.jade.provider.MachineModeProvider;
import com.gtocore.integration.jade.provider.MultiblockStructureProvider;
import com.gtocore.integration.jade.provider.ParallelProvider;
import com.gtocore.integration.jade.provider.RecipeLogicProvider;
import com.gtocore.integration.jade.provider.RecipeOutputProvider;

import com.gtolib.api.blockentity.ManaMachineBlockEntity;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.integration.jade.provider.*;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.hepdd.gtmthings.integration.jade.provider.WirelessEnergyHatchProvider;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.addon.harvest.SimpleToolHandler;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

import java.util.Objects;

public final class GTOJadePlugin implements IWailaPlugin {

    public static float getProgress(long progress, long maxProgress) {
        return maxProgress == 0 ? 0 : (float) ((double) progress / maxProgress);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new UpgradeModuleProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new WirelessInteractorMachineProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ElectricContainerBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new WorkableBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ControllableBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(BlockEntityProvider.INSTANCE, BlockEntity.class);
        registration.registerBlockDataProvider(new RecipeLogicProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ParallelProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new RecipeOutputProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new MultiblockStructureProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new MaintenanceBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ExhaustVentBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new AutoOutputBlockProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new MachineModeProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new StainedColorProvider(), MetaMachineBlockEntity.class);

        registration.registerItemStorage(GTItemStorageProvider.INSTANCE, MetaMachineBlockEntity.class);
        registration.registerFluidStorage(GTFluidStorageProvider.INSTANCE, MetaMachineBlockEntity.class);

        registration.registerBlockDataProvider(new WirelessEnergyHatchProvider(), MetaMachineBlockEntity.class);

        registration.registerBlockDataProvider(new TemperatureProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new VacuumTierProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ManaContainerBlockProvider(), ManaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new AccelerateBlockProvider(), BlockEntity.class);
        registration.registerBlockDataProvider(new WirelessGridProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new MaintenanceHatchProvider(), MetaMachineBlockEntity.class);

        registration.registerBlockDataProvider(new AEGridProvider(), BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new DestroyTimeProvider(), Block.class);
        registration.registerBlockComponent(new UpgradeModuleProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new WirelessInteractorMachineProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new ElectricContainerBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new WorkableBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new ControllableBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(BlockEntityProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(new RecipeLogicProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new ParallelProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new RecipeOutputProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new MultiblockStructureProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new MaintenanceBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new ExhaustVentBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new AutoOutputBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new MachineModeProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new StainedColorProvider(), MetaMachineBlock.class);

        registration.registerItemStorageClient(GTItemStorageProvider.INSTANCE);
        registration.registerFluidStorageClient(GTFluidStorageProvider.INSTANCE);

        registration.registerBlockComponent(new WirelessEnergyHatchProvider(), MetaMachineBlock.class);

        registration.registerBlockComponent(new TemperatureProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new VacuumTierProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new ManaContainerBlockProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new AccelerateBlockProvider(), Block.class);
        registration.registerBlockComponent(new WirelessGridProvider(), MetaMachineBlock.class);
        registration.registerBlockComponent(new MaintenanceHatchProvider(), MetaMachineBlock.class);

        registration.registerBlockComponent(new AEGridProvider(), Block.class);

        registration.registerBlockComponent(AEItemAmountProvider.INSTANCE, Block.class);
        registration.registerEntityComponent(AEItemAmountProvider.INSTANCE, ItemEntity.class);
    }

    static {
        GTMaterialItems.TOOL_ITEMS.columnMap().forEach((type, map) -> {
            if (type.harvestTags.isEmpty() || type.harvestTags.get(0).location().getNamespace().equals("minecraft")) return;
            HarvestToolProvider.registerHandler(new SimpleToolHandler(type.name, type.harvestTags.get(0), map.values().stream().filter(Objects::nonNull).filter(ItemProviderEntry::isPresent).map(ItemProviderEntry::asItem).toArray(Item[]::new)));
        });
    }
}
