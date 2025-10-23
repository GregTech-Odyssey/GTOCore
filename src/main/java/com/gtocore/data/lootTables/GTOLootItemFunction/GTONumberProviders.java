package com.gtocore.data.lootTables.GTOLootItemFunction;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GTONumberProviders {

    // 1. 获取 LOOT_NUMBER_PROVIDER_TYPE 注册表的资源键
    private static final ResourceKey<Registry<LootNumberProviderType>> REGISTRY_KEY = (ResourceKey<Registry<LootNumberProviderType>>) BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.key();

    // 2. 用资源键创建 DeferredRegister（修正参数）
    public static final DeferredRegister<LootNumberProviderType> NUMBER_PROVIDERS = DeferredRegister.create(REGISTRY_KEY, "gtocore");

    // 3. 注册自定义类型（不变）
    public static final RegistryObject<LootNumberProviderType> CUSTOM_LOGIC = NUMBER_PROVIDERS.register(
            "custom_logic",
            () -> new LootNumberProviderType(CustomLogicNumberProvider.Serializer.INSTANCE));
}
