package com.gtocore.common.data

import com.gtocore.integration.ae.ExchangeStorageMonitorPart
import com.gtocore.integration.ae.PatternContentAccessTerminalPart
import com.gtocore.integration.ae.SimpleCraftingTerminal

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item

import appeng.api.parts.IPart
import appeng.api.parts.IPartItem
import appeng.api.parts.PartModels
import appeng.core.definitions.ItemDefinition
import appeng.items.parts.PartItem
import appeng.items.parts.PartModelsHelper
import com.gtolib.GTOCore
import com.gtolib.api.ae2.me2in1.Me2in1TerminalPart
import com.gtolib.api.annotation.component_builder.ComponentBuilder
import com.gtolib.utils.register.ItemRegisterUtils.item
import com.tterrag.registrate.util.entry.ItemEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer

import java.util.function.Function
import java.util.function.Supplier

object GTOAEParts {
    fun init() {
    }

    val EXCHANGE_STORAGE_MONITOR: Supplier<ItemDefinition<PartItem<ExchangeStorageMonitorPart>>> = createPart(
        id = "exchange_storage_monitor",
        en = "Exchange Storage Monitor",
        cn = "交换率存储监控器",
        partClass = ExchangeStorageMonitorPart::class.java,
        factory = ::ExchangeStorageMonitorPart,
        tooltips = listOf(
            ComponentBuilder.create("此物品可以监控物品的交换速率", "This item can monitor the exchange rate of items") { p -> p }
                .buildSingle(),
            ComponentBuilder.create("锁定状态下右击可切换监控间隔", "In locked state, right click to switch monitoring interval") { p -> p }
                .buildSingle(),
        ),
    )

    val SIMPLE_CRAFTING_TERMINAL: Supplier<ItemDefinition<PartItem<SimpleCraftingTerminal>>> = createPart(
        id = "simple_crafting_terminal",
        en = "Simple Crafting Terminal",
        cn = "简易合成终端",
        partClass = SimpleCraftingTerminal::class.java,
        factory = ::SimpleCraftingTerminal,
        tooltips = listOf(
            ComponentBuilder.create("将终端贴在箱子、存储器的某一面，打开合成面板就可以使用箱子内的物品进行合成。", "Attach the Terminal to any side of a chest or storage device, then open the crafting interface to use items from the container for crafting.") { p -> p }
                .buildSingle(),
        ),
    )

    val ME_2IN1_TERMINAL: Supplier<ItemDefinition<PartItem<Me2in1TerminalPart>>> = createPart(
        id = "me_2in1_terminal",
        en = "ME 2-in-1 Terminal",
        cn = "ME 2合1终端",
        partClass = Me2in1TerminalPart::class.java,
        factory = ::Me2in1TerminalPart,
        tooltips = listOf(
            ComponentBuilder.create("整合了ME样板编码终端与样板管理终端的功能，", "Integrates the functions of ME Pattern Encoding Terminal and Pattern Access Terminal, ") { p -> p }
                .buildSingle(),
            ComponentBuilder.create("支持编码、管理样板，并支持批量编码、自动填充配方等功能。", "supports encoding and managing patterns, batch encoding, auto-filling recipes, and more.") { p -> p }
                .buildSingle(),
        ),
    )

    val Pattern_Content_Access_Terminal: Supplier<ItemDefinition<PartItem<PatternContentAccessTerminalPart>>> = createPart(
        id = "pattern_content_access_terminal",
        en = "Pattern Content Access Terminal",
        cn = "样板内容管理终端",
        partClass = PatternContentAccessTerminalPart::class.java,
        factory = ::PatternContentAccessTerminalPart,
        tooltips = ComponentBuilder.create("动态替换样板内容", "Dynamic Pattern Content Replacement") { p -> p.setAqua() }
            .addCommentLines("只需要将此机器连入ME网络，然后样板在被调用时，其内容就会按照你配置的优先级被同一行匹配替换。", "Simply connect this machine to the ME network, and when a pattern is called, its content will be replaced according to your configured priorities on the same line.") { p -> p.setGray() }
            .addCommentLines("每组配置中，替换的顺序为从右到左。", "In each configuration group, the replacement order is from right to left.") { p -> p.setGray() }
            .addCommentLines("例如，按顺序填入“橡木木板，白桦木板，云杉木板”的配置，那么当有样板原料中用到白桦或云杉木板时，就会动态修改样板，使用橡木木板作为原料。", "For example, if you fill in the configuration with 'Oak Planks, Birch Planks, Spruce Planks' in order, then when a pattern's ingredient uses Birch or Spruce Planks, the pattern will be dynamically modified to use Oak Planks as the ingredient.") { p -> p.setGray().setItalic() }
            .buildComponents(),
    )

    private fun <T : IPart> createPart(id: String, en: String, cn: String, partClass: Class<T>, factory: Function<IPartItem<T>, T>, tooltips: List<Component> = listOf()): Supplier<ItemDefinition<PartItem<T>>> {
        PartModels.registerModels(PartModelsHelper.createModels(partClass))
        val function: (Item.Properties) -> PartItem<T> = { p -> PartItem(p, partClass, factory) }
        val item: ItemEntry<PartItem<T>> = item(id, cn, function)
            .toolTips(*tooltips.toTypedArray())
            .lang(en)
            .model(NonNullBiConsumer.noop())
            .register()
        val definition = Supplier { ItemDefinition(en, GTOCore.id(id), item.get()) }
        return definition
    }
}
