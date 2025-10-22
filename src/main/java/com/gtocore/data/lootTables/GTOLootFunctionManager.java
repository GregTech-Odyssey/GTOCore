package com.gtocore.data.lootTables;

import com.gtolib.GTOCore;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

// GTO战利品函数管理器：统一注册所有自定义函数
public class GTOLootFunctionManager {

    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, GTOCore.MOD_ID);

    // 1. 状态效果函数
    // 2. 实体生成函数
    // 3. 执行命令函数

    // 初始化注册
    public static void register(IEventBus bus) {
        LOOT_FUNCTIONS.register(bus);
    }
}
