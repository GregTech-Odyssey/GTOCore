package com.gtocore.data.lootTables.GTOLootTool;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GTONumberProviders {

    private static final ResourceKey<Registry<LootNumberProviderType>> REGISTRY_KEY = (ResourceKey<Registry<LootNumberProviderType>>) BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.key();

    public static final DeferredRegister<LootNumberProviderType> NUMBER_PROVIDERS = DeferredRegister.create(REGISTRY_KEY, "gtocore");

    public static final RegistryObject<LootNumberProviderType> CUSTOM_LOGIC = NUMBER_PROVIDERS.register(
            "custom_logic",
            () -> new LootNumberProviderType(CustomLogicNumberProvider.Serializer.INSTANCE));
}
