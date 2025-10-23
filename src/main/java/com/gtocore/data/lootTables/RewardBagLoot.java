package com.gtocore.data.lootTables;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.lootTables.GTOLootItemFunction.CustomLogicFunction;
import com.gtocore.data.lootTables.GTOLootItemFunction.CustomLogicNumberProvider;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOLoots.addToot;
import static com.gtocore.common.data.GTOMaterials.RedstoneAlloy;
import static com.gtocore.data.lootTables.DemoLoot.createa;
import static com.gtocore.data.lootTables.GTOLootItemFunction.lootRegistrationTool.*;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public final class RewardBagLoot {

    // 引用的
    private static final ResourceLocation VANILLA_DUNGEON_REFERENCE = fromNamespaceAndPath("minecraft", "chests/simple_dungeon");

    // 注册的
    public static final ResourceLocation ADVENTURER_BAG_LOOT = GTOCore.id("reward_bags/adventurer");
    public static final ResourceLocation BASIC_RESOURCES_LOOT = GTOCore.id("reward_bags/basic_resources");

    public static void init() {
        addToot(BASIC_RESOURCES_LOOT, createBasicResourcesTable());
        addToot(ADVENTURER_BAG_LOOT, createAdventurerLootTable());
        addToot(LV_REWARD_BAG_LOOT, create_LV_REWARD_BAG_LOOT());
    }

    // 获取物品的时运等级
    public static final CustomLogicNumberProvider getFortuneLevel = new CustomLogicNumberProvider.Builder(
            (player, level, entity, pos, tool) -> {
                int fortune = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                return Math.max(1 + fortune, 1); // 确保至少1
            }
    ).build();
    // 获取物品的效率等级
    public static final CustomLogicNumberProvider getEfficiencyLuckLevel = new CustomLogicNumberProvider.Builder(
            (player, level, entity, pos, tool) -> {
                int efficiency = tool.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                int result = 1 + efficiency / 2;
                return Math.max(result, 1); // 确保至少1
            }
    ).build();

    // LV 战利品袋子
    public static final ResourceLocation LV_REWARD_BAG_LOOT = GTOCore.id("reward_bags/lv_reward_bag_loot");

    private static LootTable create_LV_REWARD_BAG_LOOT() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(getEfficiencyLuckLevel)
                .setBonusRolls(getFortuneLevel)
                .add(getLootItem(RegistriesUtils.getItem("farmersrespite:black_cod"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersrespite:tea_curry"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersrespite:blazing_chili"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersrespite:coffee_cake"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersrespite:rose_hip_pie"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersdelight:beef_patty"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersdelight:hamburger"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersdelight:roasted_mutton_chops"), 60, between(8, 16)))
                .add(getLootItem(RegistriesUtils.getItem("farmersdelight:hot_cocoa"), 60, between(8, 16)))

                .add(getLootItem(ChemicalHelper.getItem(ingot, Iron), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, Copper), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, BismuthBronze), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, RedAlloy), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, RedstoneAlloy), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, Steel), 80, exactly(1)))
                .add(getLootItem(ChemicalHelper.getItem(ingot, Aluminium), 80, exactly(1)))
                .add(getLootItem(Items.REDSTONE, 80, exactly(1)))
                .add(getLootItem(Items.COAL, 80, exactly(1)))
                .add(getLootItem(Items.ENDER_PEARL, 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("functionalstorage:copper_upgrade"), 60, between(8, 16)))
                .add(getLootItem(GTItems.FLUID_CELL_LARGE_STEEL.asItem(), 60, between(8, 16)))

                .add(getLootItem(RegistriesUtils.getItem("gtceu:steel_wrench"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("gtceu:steel_wire_cutter"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("gtceu:steel_hammer"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("gtceu:steel_screwdriver"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("gtceu:steel_file"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("gtceu:silicone_rubber_mallet"), 80, exactly(1)))

                .add(getLootItem(GTItems.ELECTRIC_MOTOR_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.CONVEYOR_MODULE_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ELECTRIC_PUMP_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.FLUID_REGULATOR_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ELECTRIC_PISTON_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ROBOT_ARM_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.EMITTER_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.SENSOR_LV.asItem(), 80, exactly(1)))
                .add(getLootItemWithQuality(GTItems.FIELD_GENERATOR_LV.asItem(), 5, exactly(1),10,getFortuneLevel))

                .add(getLootItem(GTItems.VOLTAGE_COIL_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.RESISTOR.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.DIODE.asItem(), 80, exactly(1)))
                .add(getLootItem(GTOItems.UNIVERSAL_CIRCUIT[ULV].asItem(), 80, exactly(1)))
                .add(getLootItem(GTOItems.UNIVERSAL_CIRCUIT[LV].asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.GLASS_TUBE.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.COATED_BOARD.asItem(), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("sophisticatedbackpacks:upgrade_base"), 80, exactly(1)))
                .add(getLootItem(RegistriesUtils.getItem("sophisticatedbackpacks:stack_upgrade_tier_1"), 80, exactly(1)));


        return LootTable.lootTable()
                .withPool(pool)
                .setParamSet(LootContextParamSets.FISHING)
                .build();
    }

    // 构建基础资源表
    private static LootTable createBasicResourcesTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .setBonusRolls(ConstantValue.exactly(0))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:stone"), 60, 3, 6))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:oak_planks"), 50, 2, 5))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:coal"), 45, 1, 4))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:iron_nugget"), 40, 2, 8));

        return LootTable.lootTable()
                .withPool(pool)
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }

    // 构建冒险家袋表
    private static LootTable createAdventurerLootTable() {
        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder dungeonPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 80).apply(new CustomLogicFunction.Builder((SPAWN_CHEST_LOGIC))));

        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootItemExactly(RegistriesUtils.getItem("minecraft:ender_pearl"), 30, 1))
                .add(getLootItemExactly(RegistriesUtils.getItem("minecraft:gold_nugget"), 20, 5))
                .add(getEnchantedLootItem(RegistriesUtils.getItem("minecraft:iron_axe"), 25, 1));

        return LootTable.lootTable()
                .withPool(basicPool)
                .withPool(dungeonPool)
                .withPool(rarePool)
                .withPool(createa())
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }
}
