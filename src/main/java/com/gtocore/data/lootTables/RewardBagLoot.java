package com.gtocore.data.lootTables;

import com.gtocore.common.data.GTOItems;
import com.gtocore.data.lootTables.GTOLootTool.CustomLogicFunction;
import com.gtocore.data.lootTables.GTOLootTool.CustomLogicNumberProvider;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOLoots.addToot;
import static com.gtocore.common.data.GTOMaterials.RedstoneAlloy;
import static com.gtocore.data.lootTables.GTOLootTool.LootRegistrationTool.*;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public final class RewardBagLoot {

    public static void init() {
        addToot(LV_REWARD_BAG_LOOT, create_LV_REWARD_BAG_LOOT());

        getEfficiencyLuckLevel = null;
    }

    // 获取物品的效率等级
    public static CustomLogicNumberProvider getEfficiencyLuckLevel = new CustomLogicNumberProvider.Builder(
            (thisEntity, lastDamagePlayer, damageSource, killerEntity, directKiller,
             origin, blockState, blockEntity, tool, explosionRadius, level) -> {
                int result = tool.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                return between(Mth.clamp(result - 2, 1, 18), Mth.clamp(result + 4, 1, 18));
            }).build();

    // 根据时运等级获得稀有物品
    public static @NotNull LootItem.Builder<?> getLootItemRelyFortune(Item item, int weight, int count) {
        return LootItem.lootTableItem(Items.AIR)
                .setWeight(weight)
                .apply(
                        new CustomLogicFunction.Builder((
                                                         level, thisEntity, lastDamagePlayer, damageSource, killerEntity, directKiller,
                                                         origin, blockState, blockEntity, tool, explosionRadius, stack) -> {
                            if (origin != null) {
                                float probability = 0.2f * (1 + tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE));
                                if (probability >= 1 || level.random.nextFloat() < probability) {
                                    ItemEntity itemEntity = new ItemEntity(level, origin.x(), origin.y(), origin.z(), new ItemStack(item, count));
                                    itemEntity.setNoPickUpDelay();
                                    level.addFreshEntity(itemEntity);
                                }
                            }
                        }));
    }

    // LV 战利品袋子
    public static final ResourceLocation LV_REWARD_BAG_LOOT = GTOCore.id("reward_bags/lv_reward_bag_loot");

    private static LootTable create_LV_REWARD_BAG_LOOT() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(getEfficiencyLuckLevel)
                .setBonusRolls(between(0, 3))
                .add(getLootItem("farmersrespite:black_cod", 60, between(8, 16)))
                .add(getLootItem("farmersrespite:tea_curry", 60, between(8, 16)))
                .add(getLootItem("farmersrespite:blazing_chili", 60, between(8, 16)))
                .add(getLootItem("farmersrespite:coffee_cake", 60, between(8, 16)))
                .add(getLootItem("farmersrespite:rose_hip_pie", 60, between(8, 16)))
                .add(getLootItem("farmersdelight:beef_patty", 60, between(8, 16)))
                .add(getLootItem("farmersdelight:hamburger", 60, between(8, 16)))
                .add(getLootItem("farmersdelight:roasted_mutton_chops", 60, between(8, 16)))
                .add(getLootItem("farmersdelight:hot_cocoa", 60, between(8, 16)))

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
                .add(getLootItem("functionalstorage:copper_upgrade", 60, exactly(1)))
                .add(getLootItem(GTItems.FLUID_CELL_LARGE_STEEL.asItem(), 60, between(8, 16)))

                .add(getLootItem("gtceu:steel_wrench", 80, exactly(1)))
                .add(getLootItem("gtceu:steel_wire_cutter", 80, exactly(1)))
                .add(getLootItem("gtceu:steel_hammer", 80, exactly(1)))
                .add(getLootItem("gtceu:steel_screwdriver", 80, exactly(1)))
                .add(getLootItem("gtceu:steel_file", 80, exactly(1)))
                .add(getLootItem("gtceu:silicone_rubber_mallet", 80, exactly(1)))

                .add(getLootItem(GTItems.ELECTRIC_MOTOR_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.CONVEYOR_MODULE_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ELECTRIC_PUMP_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.FLUID_REGULATOR_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ELECTRIC_PISTON_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.ROBOT_ARM_LV.asItem(), 80, exactly(1)))
                .add(getLootItemRelyFortune(GTItems.EMITTER_LV.asItem(), 80, 1))
                .add(getLootItemRelyFortune(GTItems.SENSOR_LV.asItem(), 80, 1))
                .add(getLootItemRelyFortune(GTItems.FIELD_GENERATOR_LV.asItem(), 5, 1))

                .add(getLootItem(GTItems.VOLTAGE_COIL_LV.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.RESISTOR.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.DIODE.asItem(), 80, exactly(1)))
                .add(getLootItem(GTOItems.UNIVERSAL_CIRCUIT[ULV].asItem(), 80, exactly(1)))
                .add(getLootItem(GTOItems.UNIVERSAL_CIRCUIT[LV].asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.GLASS_TUBE.asItem(), 80, exactly(1)))
                .add(getLootItem(GTItems.COATED_BOARD.asItem(), 80, exactly(1)))
                .add(getLootItem("sophisticatedbackpacks:upgrade_base", 80, exactly(1)))
                .add(getLootItem("sophisticatedbackpacks:stack_upgrade_tier_1", 80, exactly(1)));

        return LootTable.lootTable()
                .withPool(pool)
                .setParamSet(LootContextParamSets.FISHING)
                .build();
    }
}
