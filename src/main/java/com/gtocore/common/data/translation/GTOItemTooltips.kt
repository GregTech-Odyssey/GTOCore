package com.gtocore.common.data.translation

import com.gtocore.api.misc.AutoInitialize
import com.gtocore.utils.ComponentListSupplier
import com.gtocore.utils.setTooltips
import com.gtocore.utils.translatedTo

import appeng.core.definitions.AEItems
import appeng.core.definitions.AEParts
import com.glodblock.github.extendedae.common.EPPItemAndBlock

object GTOItemTooltips : AutoInitialize<GTOItemTooltips>() {
    val PatternModifierTooltips = ComponentListSupplier {
        add("可以便捷地修改样板" translatedTo "Can modify patterns easily") { aqua() }
        add("PRO版本可以批量应用" translatedTo "PRO version can apply in batch") { aqua() }
        add(ComponentSlang.RecommendedToUse("样板总成" translatedTo "Pattern Buffer")) { aqua() }
        add("它可以自动翻倍样板，不用任何操作" translatedTo "It can automatically double the pattern, no operation is required") { aqua() }
    }
    init {
        listOf(AEParts.STORAGE_BUS.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    add("此面板可以与存储设备进行交互" translatedTo "This part can interact with storage devices") { aqua() }
                    add(("经过优化，吞吐量性能卓越" translatedTo "Throughput performance is excellent")) { aqua() }
                    add(("仅支持读取" translatedTo "Only supports reading ") + ("物品和流体" translatedTo "items and fluids ").gold() + ("两种类型" translatedTo "two types")) { aqua() }
                }.editionByGTONormal(),
            )
        }
        listOf(EPPItemAndBlock.TAG_STORAGE_BUS.asItem(), EPPItemAndBlock.MOD_STORAGE_BUS.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    add("此面板可以与存储设备进行交互" translatedTo "This part can interact with storage devices") { aqua() }
                    add(("仅支持读取" translatedTo "Only supports reading ") + ("物品和流体" translatedTo "items and fluids ").gold() + ("两种类型" translatedTo "two types")) { aqua() }
                    add(("自动化中，" translatedTo "In automation, ") + ComponentSlang.RecommendedToUse("存储总线" translatedTo "Storage Bus")) { aqua() }
                    add("经过优化，自动化性能卓越" translatedTo "Throughput performance is excellent in automation") { aqua() }
                },
            )
        }
        listOf(EPPItemAndBlock.PATTERN_MODIFIER.asItem()).forEach {
            it.setTooltips(PatternModifierTooltips)
        }
//        EPPItemAndBlock.WIRELESS_CONNECTOR.asItem().setTooltips(ComponentListSupplier {
//            add(ComponentSlang.RecommendedToUse("ME连接器" translatedTo "ME Connector"))
//            add("支持一对多连接，并支持ME舱室自动连接" translatedTo "Supports one-to-many connections and automatic connection to ME Hatch") { aqua() }
//        })  一取消注释打开创造物品栏就崩，原理不明
        listOf(AEItems.ITEM_CELL_256K.asItem(), AEItems.FLUID_CELL_256K.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    add("你走到了单个存储元件的尽头" translatedTo "You've reached the end of a single storage cell") { aqua() }
                    add(ComponentSlang.RecommendedToUse("ME存储器 (多方块结构)" translatedTo "ME Storage (MultiBlock)")) { aqua() }
                    add("他甚至可以不限类型地最高无限存储" translatedTo "It can even store unlimited amounts of items and fluids without type limit") { aqua() }
                }.editionByGTONormal(),
            )
        }
    }
}
