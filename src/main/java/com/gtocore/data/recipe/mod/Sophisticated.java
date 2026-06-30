package com.gtocore.data.recipe.mod;

import com.gtocore.common.data.machines.MultiBlockG;
import com.gtocore.data.recipe.builder.vanilla.SmithingRecipeBuilder;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class Sophisticated {

    public static void backpack() {
        if (GTOCore.isEasy()) return;
        if (Mods.SOPHISTICATEDBACKPACKS.isLoaded()) {
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), GTMachines.SUPER_CHEST[GTValues.MV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), GTItems.ELECTRIC_PISTON_MV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.COVER_ITEM_VOIDING.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_MAGNET_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_1"), GTMachines.SUPER_CHEST[GTValues.HV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), GTItems.ITEM_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), GTItems.ROBOT_ARM_MV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:tank_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), RegistriesUtils.getItemStack("gtceu:bronze_drum"));
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ITEM_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_magnet_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:magnet_upgrade"), GTItems.ITEM_MAGNET_HV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:refill_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ROBOT_ARM_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_filter_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:filter_upgrade"), GTItems.TAG_FILTER.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_starter_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.SUPER_CHEST[GTValues.LV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_void_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:void_upgrade"), GTItems.COVER_ITEM_VOIDING_ADVANCED.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_blasting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:blasting_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_omega_tier"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), GTMachines.QUANTUM_CHEST[GTValues.UHV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_4"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), GTMachines.QUANTUM_CHEST[GTValues.IV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_3"), RegistriesUtils.getItemStack("sophisticatedbackpacks:stack_upgrade_tier_2"), GTMachines.SUPER_CHEST[GTValues.EV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PUMP_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:auto_smoking_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:smoking_upgrade"), GTItems.CONVEYOR_MODULE_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:compacting_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTItems.ELECTRIC_PISTON_LV.asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pickup_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:upgrade_base"), GTMachines.ITEM_COLLECTOR[GTValues.LV].asItem());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("advanced_pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:advanced_pump_upgrade"), RegistriesUtils.getItemStack("sophisticatedbackpacks:pump_upgrade"), GTItems.ELECTRIC_PUMP_MV.asStack());
            VanillaRecipeHelper.addShapelessRecipe(GTOCore.id("xp_pump_upgrade"), RegistriesUtils.getItem("sophisticatedbackpacks:xp_pump_upgrade"), RegistriesUtils.getItem("sophisticatedbackpacks:upgrade_base"), RegistriesUtils.getItem("gtocore:exp_obelisk"), GTItems.FLUID_REGULATOR_MV.asItem());

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("backpack"), RegistriesUtils.getItemStack("sophisticatedbackpacks:backpack"),
                    "ABA",
                    "BCB",
                    "DBD",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.WroughtIron), 'B', new ItemStack(Items.LEATHER.asItem()), 'C', RegistriesUtils.getItemStack("gtceu:wood_crate"), 'D', new ItemStack(Items.STRING.asItem()));

            GTDynamicDataPack.addJsonRecipe(GTOCore.id("copper_backpack"), """
                    {
                      "type": "sophisticatedbackpacks:backpack_upgrade",
                      "conditions": [
                        {
                          "type": "sophisticatedcore:item_enabled",
                          "itemRegistryName": "sophisticatedbackpacks:copper_backpack"
                        }
                      ],
                      "key": {
                        "B": {
                          "item": "sophisticatedbackpacks:backpack"
                        },
                        "C": {
                          "tag": "c:plates/copper"
                        }
                      },
                      "pattern": [
                        "CCC",
                        "CBC",
                        "CCC"
                      ],
                      "result": {
                        "item": "sophisticatedbackpacks:copper_backpack"
                      }
                    }""");
            GTDynamicDataPack.addJsonRecipe(GTOCore.id("diamond_backpack"), """
                    {
                      "type": "sophisticatedbackpacks:backpack_upgrade",
                      "conditions": [
                        {
                          "type": "sophisticatedcore:item_enabled",
                          "itemRegistryName": "sophisticatedbackpacks:diamond_backpack"
                        }
                      ],
                      "key": {
                        "B": {
                          "item": "sophisticatedbackpacks:gold_backpack"
                        },
                        "D": {
                          "tag": "c:plates/diamond"
                        }
                      },
                      "pattern": [
                        "DDD",
                        "DBD",
                        "DDD"
                      ],
                      "result": {
                        "item": "sophisticatedbackpacks:diamond_backpack"
                      }
                    }""");
            GTDynamicDataPack.addJsonRecipe(GTOCore.id("gold_backpack"), """
                    {
                         "type": "sophisticatedbackpacks:backpack_upgrade",
                         "conditions": [
                           {
                             "type": "sophisticatedcore:item_enabled",
                             "itemRegistryName": "sophisticatedbackpacks:gold_backpack"
                           }
                         ],
                         "key": {
                           "B": {
                             "item": "sophisticatedbackpacks:iron_backpack"
                           },
                           "G": {
                             "tag": "c:plates/gold"
                           }
                         },
                         "pattern": [
                           "GGG",
                           "GBG",
                           "GGG"
                         ],
                         "result": {
                           "item": "sophisticatedbackpacks:gold_backpack"
                         }
                       }""");
            GTDynamicDataPack.addJsonRecipe(GTOCore.id("iron_backpack"), """
                    {
                           "type": "sophisticatedbackpacks:backpack_upgrade",
                           "conditions": [
                             {
                               "type": "sophisticatedcore:item_enabled",
                               "itemRegistryName": "sophisticatedbackpacks:iron_backpack"
                             }
                           ],
                           "key": {
                             "B": {
                               "item": "sophisticatedbackpacks:backpack"
                             },
                             "I": {
                               "tag": "c:plates/iron"
                             }
                           },
                           "pattern": [
                             "III",
                             "IBI",
                             "III"
                           ],
                           "result": {
                             "item": "sophisticatedbackpacks:iron_backpack"
                           }
                         }""");
            GTDynamicDataPack.addJsonRecipe(GTOCore.id("iron_backpack_from_copper"), """
                    {
                           "type": "sophisticatedbackpacks:backpack_upgrade",
                           "conditions": [
                             {
                               "type": "sophisticatedcore:item_enabled",
                               "itemRegistryName": "sophisticatedbackpacks:iron_backpack"
                             }
                           ],
                           "key": {
                             "B": {
                               "item": "sophisticatedbackpacks:copper_backpack"
                             },
                             "I": {
                               "tag": "c:plates/iron"
                             }
                           },
                           "pattern": [
                             " I ",
                             "IBI",
                             " I "
                           ],
                           "result": {
                             "item": "sophisticatedbackpacks:iron_backpack"
                           }
                         }""");
        }

        if (Mods.SOPHISTICATEDSTORAGE.isLoaded()) {
            /* 工作台"拾取升级","升级基板 基础物品收集器" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("pickup_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:pickup_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTMachines.ITEM_COLLECTOR[GTValues.LV].asItem());

            /* 工作台"高级拾取升级","拾取升级 物品过滤卡" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_pickup_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:advanced_pickup_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:pickup_upgrade"), 'B', GTItems.ITEM_FILTER.asItem());

            /* 工作台"过滤升级","升级基板 物品过滤卡" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("filter_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:filter_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTItems.ITEM_FILTER.asItem());

            /* 工作台"高级过滤升级","过滤升级 物品标签过滤卡" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_filter_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:advanced_filter_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:filter_upgrade"), 'B', GTItems.TAG_FILTER.asItem());

            /* 工作台"磁铁升级","升级基板 LV物品磁铁" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("magnet_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:magnet_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTItems.ITEM_MAGNET_LV.asItem());

            /* 工作台"高级磁铁升级","磁铁升级 HV物品磁铁" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_magnet_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:advanced_magnet_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:magnet_upgrade"), 'B', GTItems.ITEM_MAGNET_HV.asItem());

            /* 工作台"压制升级","升级基板 LV电力活塞" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("compacting_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:compacting_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTItems.ELECTRIC_PISTON_LV.asItem());

            /* 工作台"高级压制升级","压制升级 MV电力活塞" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_compacting_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:advanced_compacting_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:compacting_upgrade"), 'B', GTItems.ELECTRIC_PISTON_MV.asItem());

            /* 工作台"虚空升级","升级基板 物品销毁覆盖版" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("void_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:void_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTItems.COVER_ITEM_VOIDING.asItem());

            /* 工作台"高级虚空升级","虚空升级 进阶物品销毁覆盖版" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("advanced_void_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:advanced_void_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:void_upgrade"), 'B', GTItems.COVER_ITEM_VOIDING_ADVANCED.asItem());

            /* 工作台"自动熔炼升级","熔炼升级 LV传送带" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("auto_smelting_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:auto_smelting_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:smelting_upgrade"), 'B', GTItems.CONVEYOR_MODULE_LV.asItem());

            /* 工作台"自动高炉升级","高炉升级 LV传送带" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("auto_blasting_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:auto_blasting_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:blasting_upgrade"), 'B', GTItems.CONVEYOR_MODULE_LV.asItem());

            /* 工作台"自动烟熏升级","烟熏升级 LV传送带" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("auto_smoking_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:auto_smoking_upgrade"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:smoking_upgrade"), 'B', GTItems.CONVEYOR_MODULE_LV.asItem());

            /* 拿不定强度，两个方案 */
            /* 工作台"堆叠升级T1","升级基板 青铜板条箱 */
            if (GTOCore.isNormal()) {
                VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_1"),
                        "AB ",
                        "   ",
                        "   ",
                        'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', GTMachines.BRONZE_CRATE.asItem());
            } else {
                /* 工作台"堆叠升级T1","升级基板 多方块板条箱" */
                VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_1"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_1"),
                        "AB ",
                        "   ",
                        "   ",
                        'A', RegistriesUtils.getItem("sophisticatedstorage:upgrade_base"), 'B', MultiBlockG.ITEM_VAULT.asItem());
            }
            /* 工作台"堆叠升级T1P","堆叠升级T1 超级箱I" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_1_plus"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_1_plus"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_1"), 'B', GTMachines.SUPER_CHEST[GTValues.LV].asItem());

            /* 工作台"堆叠升级T2","堆叠升级T1P 超级箱II" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_2"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_2"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_1_plus"), 'B', GTMachines.SUPER_CHEST[GTValues.MV].asItem());

            /* 工作台"堆叠升级T3","堆叠升级T2 超级箱III" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_3"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_3"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_2"), 'B', GTMachines.SUPER_CHEST[GTValues.HV].asItem());

            /* 工作台"堆叠升级T4","堆叠升级T3 超级箱IV" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_4"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_4"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_3"), 'B', GTMachines.SUPER_CHEST[GTValues.EV].asItem());

            /* 工作台"堆叠升级T5","堆叠升级T4 量子箱V" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_tier_5"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_5"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_4"), 'B', GTMachines.QUANTUM_CHEST[GTValues.IV].asItem());

            /* 工作台"堆叠升级OMG","堆叠升级T5 量子箱IX" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("stack_upgrade_omega_tier"), RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_omega_tier"),
                    "AB ",
                    "   ",
                    "   ",
                    'A', RegistriesUtils.getItem("sophisticatedstorage:stack_upgrade_tier_5"), 'B', GTMachines.QUANTUM_CHEST[GTValues.UHV].asItem());

            /* 以下是箱子升级配方 */
            /* 木桶 */
            /* 工作台"铜桶","木桶 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_barrel"), RegistriesUtils.getItem("sophisticatedstorage:copper_barrel"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:barrel"));

            /* 工作台"铁桶","铜桶 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_barrel"), RegistriesUtils.getItem("sophisticatedstorage:iron_barrel"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:copper_barrel"));

            /* 工作台"金桶","铁桶 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gold_barrel"), RegistriesUtils.getItem("sophisticatedstorage:gold_barrel"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:iron_barrel"));

            /* 工作台"钻石桶","金桶 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("diamond_barrel"), RegistriesUtils.getItem("sophisticatedstorage:diamond_barrel"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:gold_barrel"));

            /* 锻造台"下届合金桶","钻石桶 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:netherite_barrel"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:diamond_barrel"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 限类木桶I */
            /* 工作台"限类铜桶I","限类木桶I 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_copper_barrel_1"), RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_1"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_barrel_1"));

            /* 工作台"限类铁桶I","限类铜桶I 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_iron_barrel_1"), RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_1"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_1"));

            /* 工作台"限类金桶I","限类铁桶I 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_gold_barrel_1"), RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_1"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_1"));

            /* 工作台"钻限类石桶I","限类金桶I 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_diamond_barrel_1"), RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_1"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_1"));

            /* 锻造台"限类下届合金桶I","限类钻石桶I 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:limited_netherite_barrel_1"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_1"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 限类木桶II */
            /* 工作台"限类铜桶II","限类木桶II 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_copper_barrel_2"), RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_2"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_barrel_2"));

            /* 工作台"限类铁桶II","限类铜桶II 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_iron_barrel_2"), RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_2"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_2"));

            /* 工作台"限类金桶II","限类铁桶II 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_gold_barrel_2"), RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_2"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_2"));

            /* 工作台"限类钻石桶II","限类金桶II 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_diamond_barrel_2"), RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_2"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_2"));

            /* 锻造台"限类下届合金桶II","限类钻石桶II 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:limited_netherite_barrel_2"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_2"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 限类木桶III */
            /* 工作台"限类铜桶III","限类木桶III 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_copper_barrel_3"), RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_3"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_barrel_3"));

            /* 工作台"限类铁桶III","限类铜桶III 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_iron_barrel_3"), RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_3"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_3"));

            /* 工作台"限类金桶III","限类铁桶III 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_gold_barrel_3"), RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_3"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_3"));

            /* 工作台"限类钻石桶III","限类金桶III 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_diamond_barrel_3"), RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_3"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_3"));

            /* 锻造台"限类下届合金桶III","限类钻石桶III 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:limited_netherite_barrel_3"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_3"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 限类木桶IV */
            /* 工作台"限类铜桶IV","限类木桶IV 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_copper_barrel_4"), RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_4"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_barrel_4"));

            /* 工作台"限类铁桶IV","限类铜桶IV 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_iron_barrel_4"), RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_4"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_copper_barrel_4"));

            /* 工作台"限类金桶IV","限类铁桶IV 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_gold_barrel_4"), RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_4"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_iron_barrel_4"));

            /* 工作台"限类钻石桶IV","限类金桶IV 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("limited_diamond_barrel_4"), RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_4"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:limited_gold_barrel_4"));

            /* 锻造台"限类下届合金桶IV","限类钻石桶IV 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:limited_netherite_barrel_4"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:limited_diamond_barrel_4"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 箱子 */
            /* 工作台"铜箱子","木箱子 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_chest"), RegistriesUtils.getItem("sophisticatedstorage:copper_chest"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:chest"));

            /* 工作台"铁箱子","铜箱子 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_chest"), RegistriesUtils.getItem("sophisticatedstorage:iron_chest"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:copper_chest"));

            /* 工作台"金箱子","铁箱子 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gold_chest"), RegistriesUtils.getItem("sophisticatedstorage:gold_chest"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:iron_chest"));

            /* 工作台"钻石箱子","金箱子 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("diamond_chest"), RegistriesUtils.getItem("sophisticatedstorage:diamond_chest"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:gold_chest"));

            /* 锻造台"下届合金箱子","钻石箱子 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:netherite_chest"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:diamond_chest"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 潜影盒 */
            /* 工作台"铜潜影盒","潜影盒 铜板 铜螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_shulker_box"), RegistriesUtils.getItem("sophisticatedstorage:copper_shulker_box"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', RegistriesUtils.getItem("sophisticatedstorage:shulker_box"));

            /* 工作台"铁潜影盒","铜潜影盒 铁板 铁螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_shulker_box"), RegistriesUtils.getItem("sophisticatedstorage:iron_shulker_box"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', RegistriesUtils.getItem("sophisticatedstorage:copper_shulker_box"));

            /* 工作台"金潜影盒","铁潜影盒 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gold_shulker_box"), RegistriesUtils.getItem("sophisticatedstorage:gold_shulker_box"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:iron_shulker_box"));

            /* 工作台"钻石潜影盒","金潜影盒 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("diamond_shulker_box"), RegistriesUtils.getItem("sophisticatedstorage:diamond_shulker_box"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:gold_shulker_box"));

            /* 锻造台"下届合金潜影盒","钻石潜影盒 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:netherite_shulker_box"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:diamond_shulker_box"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 工作台"基础铜升级","拉杆 铜螺丝 铜板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_to_copper_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:basic_to_copper_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Copper), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Copper), 'C', Items.LEVER);

            /* 工作台"基础铁升级","拉杆 铁螺丝 铁板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_to_iron_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:basic_to_iron_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Iron), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'C', Items.LEVER);

            /* 工作台"基础铁升级","基础铜升级 铁板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_to_iron_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:basic_to_iron_tier_upgrade"),
                    " A ",
                    "ABA",
                    " A ",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'B', RegistriesUtils.getItem("sophisticatedstorage:basic_to_copper_tier_upgrade"));

            /* 工作台"基础金升级","基础铁升级 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_to_gold_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:basic_to_gold_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:basic_to_iron_tier_upgrade"));

            /* 工作台"基础钻升级","基础金升级 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_to_diamond_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:basic_to_diamond_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:basic_to_gold_tier_upgrade"));

            /* 锻造台"基础下界合金升级","基础钻升级 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:basic_to_netherite_tier_upgrade"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:basic_to_diamond_tier_upgrade"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 工作台"铜铁升级","拉杆 铁板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_to_iron_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:copper_to_iron_tier_upgrade"),
                    " A ",
                    "ABA",
                    " A ",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Iron), 'B', Items.LEVER);

            /* 工作台"铜金升级","铜铁升级 金螺丝 金板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_to_gold_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:copper_to_gold_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', RegistriesUtils.getItem("sophisticatedstorage:copper_to_iron_tier_upgrade"));

            /* 工作台"铜钻升级","铜金升级 钻石螺丝 钻石板" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("copper_to_diamond_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:copper_to_diamond_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:copper_to_gold_tier_upgrade"));

            /* 锻造台"铜下界合金升级","铜钻升级 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:copper_to_netherite_tier_upgrade"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:copper_to_diamond_tier_upgrade"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 工作台"铁金升级","拉杆 金板 金螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_to_gold_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:iron_to_gold_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Gold), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Gold), 'C', Items.LEVER);

            /* 工作台"铁钻升级","铁金升级 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("iron_to_diamond_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:iron_to_diamond_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', RegistriesUtils.getItem("sophisticatedstorage:iron_to_gold_tier_upgrade"));

            /* 锻造台"铁下界合金升级","铁钻升级 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:iron_to_netherite_tier_upgrade"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:iron_to_diamond_tier_upgrade"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 工作台"金钻升级","拉杆 钻石板 钻石螺丝" */
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("gold_to_diamond_tier_upgrade"), RegistriesUtils.getItem("sophisticatedstorage:gold_to_diamond_tier_upgrade"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.screw, GTMaterials.Diamond), 'B', new MaterialEntry(TagPrefix.plate, GTMaterials.Diamond), 'C', Items.LEVER);

            /* 锻造台"金下界合金升级","金钻升级 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:gold_to_netherite_tier_upgrade"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(RegistriesUtils.getItem("sophisticatedstorage:gold_to_diamond_tier_upgrade"))
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();

            /* 锻造台"钻下界合金升级","拉杆 锻造模板 下界合金锭" */
            SmithingRecipeBuilder.builder()
                    .output(RegistriesUtils.getItem("sophisticatedstorage:diamond_to_netherite_tier_upgrade"))
                    .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input(Items.LEVER)
                    .addition(TagPrefix.ingot, GTMaterials.Netherite)
                    .save();
        }
    }

    public static void backpackFilter(Set<ResourceLocation> filters) {
        if (GTOCore.isEasy()) return;
        if (Mods.SOPHISTICATEDBACKPACKS.isLoaded()) {
            filters.add(RLUtils.sp("backpack"));
            filters.add(RLUtils.sp("pickup_upgrade"));
            filters.add(RLUtils.sp("filter_upgrade"));
            filters.add(RLUtils.sp("advanced_pickup_upgrade"));
            filters.add(RLUtils.sp("advanced_filter_upgrade"));
            filters.add(RLUtils.sp("magnet_upgrade"));
            filters.add(RLUtils.sp("advanced_magnet_upgrade"));
            filters.add(RLUtils.sp("advanced_magnet_upgrade_from_basic"));
            filters.add(RLUtils.sp("compacting_upgrade"));
            filters.add(RLUtils.sp("advanced_compacting_upgrade"));
            filters.add(RLUtils.sp("void_upgrade"));
            filters.add(RLUtils.sp("advanced_void_upgrade"));
            filters.add(RLUtils.sp("pump_upgrade"));
            filters.add(RLUtils.sp("advanced_pump_upgrade"));
            filters.add(RLUtils.sp("xp_pump_upgrade"));
            filters.add(RLUtils.sp("battery_upgrade"));
            filters.add(RLUtils.sp("tank_upgrade"));
            filters.add(RLUtils.sp("refill_upgrade"));
            filters.add(RLUtils.sp("advanced_refill_upgrade"));
            filters.add(RLUtils.sp("inception_upgrade"));
            filters.add(RLUtils.sp("auto_smelting_upgrade"));
            filters.add(RLUtils.sp("auto_smoking_upgrade"));
            filters.add(RLUtils.sp("auto_smoking_upgrade_from_auto_smelting_upgrade"));
            filters.add(RLUtils.sp("auto_blasting_upgrade"));
            filters.add(RLUtils.sp("auto_blasting_upgrade_from_auto_smelting_upgrade"));
            filters.add(RLUtils.sp("stack_upgrade_starter_tier"));
            filters.add(RLUtils.sp("stack_upgrade_tier_1"));
            filters.add(RLUtils.sp("stack_upgrade_tier_1_from_starter"));
            filters.add(RLUtils.sp("stack_upgrade_tier_2"));
            filters.add(RLUtils.sp("stack_upgrade_tier_3"));
            filters.add(RLUtils.sp("stack_upgrade_tier_4"));
            filters.add(RLUtils.sp("stack_upgrade_omega_tier"));
            filters.add(RLUtils.sp("copper_backpack"));
            filters.add(RLUtils.sp("diamond_backpack"));
            filters.add(RLUtils.sp("gold_backpack"));
            filters.add(RLUtils.sp("iron_backpack"));
            filters.add(RLUtils.sp("iron_backpack_from_copper"));
        }
        if (Mods.SOPHISTICATEDSTORAGE.isLoaded()) {
            add(filters, "sophisticatedstorage:filter_upgrade");
            add(filters, "sophisticatedstorage:pickup_upgrade");
            add(filters, "sophisticatedstorage:advanced_pickup_upgrade");
            add(filters, "sophisticatedstorage:advanced_filter_upgrade");
            add(filters, "sophisticatedstorage:storage_magnet_upgrade_from_backpack_magnet_upgrade");
            add(filters, "sophisticatedstorage:magnet_upgrade");
            add(filters, "sophisticatedstorage:advanced_magnet_upgrade_from_basic");
            add(filters, "sophisticatedstorage:compacting_upgrade");
            add(filters, "sophisticatedstorage:advanced_compacting_upgrade");
            add(filters, "sophisticatedstorage:void_upgrade");
            add(filters, "sophisticatedstorage:auto_smelting_upgrade");
            add(filters, "sophisticatedstorage:auto_smoking_upgrade");
            add(filters, "sophisticatedstorage:auto_blasting_upgrade");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_1");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_1_plus");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_2");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_3");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_4");
            add(filters, "sophisticatedstorage:stack_upgrade_tier_5");
            add(filters, "sophisticatedstorage:stack_upgrade_omega_tier");
            add(filters, "sophisticatedstorage:copper_barrel");
            add(filters, "sophisticatedstorage:iron_barrel_from_copper_barrel");
            add(filters, "sophisticatedstorage:iron_barrel");
            add(filters, "sophisticatedstorage:gold_barrel");
            add(filters, "sophisticatedstorage:diamond_barrel");
            add(filters, "sophisticatedstorage:netherite_barrel");
            add(filters, "sophisticatedstorage:limited_copper_barrel_1");
            add(filters, "sophisticatedstorage:limited_copper_barrel_2");
            add(filters, "sophisticatedstorage:limited_copper_barrel_3");
            add(filters, "sophisticatedstorage:limited_copper_barrel_4");
            add(filters, "sophisticatedstorage:limited_iron_barrel_1_from_limited_copper_barrel_1");
            add(filters, "sophisticatedstorage:limited_iron_barrel_1");
            add(filters, "sophisticatedstorage:limited_iron_barrel_2_from_limited_copper_barrel_2");
            add(filters, "sophisticatedstorage:limited_iron_barrel_2");
            add(filters, "sophisticatedstorage:limited_iron_barrel_3_from_limited_copper_barrel_3");
            add(filters, "sophisticatedstorage:limited_iron_barrel_3");
            add(filters, "sophisticatedstorage:limited_iron_barrel_4_from_limited_copper_barrel_4");
            add(filters, "sophisticatedstorage:limited_iron_barrel_4");
            add(filters, "sophisticatedstorage:limited_gold_barrel_1");
            add(filters, "sophisticatedstorage:limited_gold_barrel_2");
            add(filters, "sophisticatedstorage:limited_gold_barrel_3");
            add(filters, "sophisticatedstorage:limited_gold_barrel_4");
            add(filters, "sophisticatedstorage:limited_diamond_barrel_1");
            add(filters, "sophisticatedstorage:limited_diamond_barrel_2");
            add(filters, "sophisticatedstorage:limited_diamond_barrel_3");
            add(filters, "sophisticatedstorage:limited_diamond_barrel_4");
            add(filters, "sophisticatedstorage:limited_netherite_barrel_1");
            add(filters, "sophisticatedstorage:limited_netherite_barrel_2");
            add(filters, "sophisticatedstorage:limited_netherite_barrel_3");
            add(filters, "sophisticatedstorage:limited_netherite_barrel_4");
            add(filters, "sophisticatedstorage:double_copper_chest");
            add(filters, "sophisticatedstorage:copper_chest");
            add(filters, "sophisticatedstorage:double_iron_chest");
            add(filters, "sophisticatedstorage:iron_chest_from_copper_chest");
            add(filters, "sophisticatedstorage:double_iron_chest_from_copper_chest");
            add(filters, "sophisticatedstorage:iron_chest");
            add(filters, "sophisticatedstorage:double_gold_chest");
            add(filters, "sophisticatedstorage:gold_chest");
            add(filters, "sophisticatedstorage:double_diamond_chest");
            add(filters, "sophisticatedstorage:diamond_chest");
            add(filters, "sophisticatedstorage:double_netherite_chest");
            add(filters, "sophisticatedstorage:netherite_chest");
            add(filters, "sophisticatedstorage:copper_shulker_box");
            add(filters, "sophisticatedstorage:iron_shulker_box_from_copper_shulker_box");
            add(filters, "sophisticatedstorage:iron_shulker_box");
            add(filters, "sophisticatedstorage:gold_shulker_box");
            add(filters, "sophisticatedstorage:diamond_shulker_box");
            add(filters, "sophisticatedstorage:netherite_shulker_box");
            add(filters, "sophisticatedstorage:basic_to_copper_tier_upgrade");
            add(filters, "sophisticatedstorage:basic_to_iron_tier_from_basic_to_copper_tier");
            add(filters, "sophisticatedstorage:basic_to_iron_tier_upgrade");
            add(filters, "sophisticatedstorage:basic_to_gold_tier_upgrade");
            add(filters, "sophisticatedstorage:basic_to_diamond_tier_upgrade");
            add(filters, "sophisticatedstorage:basic_to_netherite_tier_upgrade");
            add(filters, "sophisticatedstorage:copper_to_iron_tier_upgrade");
            add(filters, "sophisticatedstorage:copper_to_gold_tier_upgrade");
            add(filters, "sophisticatedstorage:copper_to_diamond_tier_upgrade");
            add(filters, "sophisticatedstorage:copper_to_netherite_tier_upgrade");
            add(filters, "sophisticatedstorage:iron_to_gold_tier_upgrade");
            add(filters, "sophisticatedstorage:iron_to_diamond_tier_upgrade");
            add(filters, "sophisticatedstorage:iron_to_netherite_tier_upgrade");
            add(filters, "sophisticatedstorage:gold_to_diamond_tier_upgrade");
            add(filters, "sophisticatedstorage:gold_to_netherite_tier_upgrade");
            add(filters, "sophisticatedstorage:diamond_to_netherite_tier_upgrade");
        }
    }

    private static void add(Set<ResourceLocation> filters, String id) {
        filters.add(RLUtils.parse(id));
    }
}
