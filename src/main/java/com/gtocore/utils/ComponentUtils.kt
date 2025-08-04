package com.gtocore.utils

import com.gtocore.common.data.translation.ComponentSlang

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

import com.google.common.base.Supplier
import com.gregtechceu.gtceu.client.util.TooltipHelper
import com.gtolib.api.annotation.component_builder.TranslationKeyProvider
import com.gtolib.utils.StringUtils

class ComponentListSupplier(var list: MutableList<ComponentSupplier> = mutableListOf()) : Supplier<List<Component>> {
    override fun get(): List<Component> {
        val result = list.map { it.get() }
        return result
    }
    fun getArray(): Array<Component> = get().toTypedArray()

    fun add(component: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { this }) {
        val styledComponent = style(component)
        list.add(styledComponent)
    }

    fun apply(tooltips: MutableList<Component>) {
        tooltips.addAll(get())
    }

    // ////////////////////////////////
    // ****** 便捷指令 ******//
    // //////////////////////////////
    fun editionByGTONormal(): ComponentListSupplier = this.apply {
        add(ComponentSlang.EditionByGTONormal)
    }
    fun editionByGTORemix(): ComponentListSupplier = this.apply {
        add(ComponentSlang.EditionByGTORemix)
    }
}
fun ComponentListSupplier(op: ComponentListSupplier.() -> Unit): ComponentListSupplier {
    val supplier = ComponentListSupplier()
    supplier.op()
    supplier.initialize()
    return supplier
}

class ComponentSupplier(var component: Component, private val delayed: MutableList<(MutableComponent) -> Unit> = mutableListOf(), private val transform: MutableList<(ComponentSupplier) -> ComponentSupplier> = mutableListOf()) : Supplier<Component> {
    override fun get(): MutableComponent {
        var supplier = this
        // 先应用所有的transform变换
        transform.forEach { transformation ->
            supplier = transformation(supplier)
        }
        val result = supplier.component.copy()
        // 再应用所有的delayed操作
        supplier.delayed.forEach { it(result) }
        return result
    }
    fun apply(tooltips: MutableList<Component>) {
        tooltips.add(get())
    }

    operator fun plus(other: ComponentSupplier): ComponentSupplier {
        val newSupplier = ComponentSupplier(component, delayed.toMutableList(), transform.toMutableList())
        newSupplier.delayed.add { result ->
            result.append(other.get())
        }
        return newSupplier
    }

    private fun operatorComponent(op: MutableComponent.() -> Unit): ComponentSupplier {
        val newSupplier = ComponentSupplier(component, delayed.toMutableList(), transform.toMutableList())
        newSupplier.delayed.add { result ->
            op.invoke(result)
        }
        return newSupplier
    }
    private fun transformComponent(trans: (ComponentSupplier) -> ComponentSupplier): ComponentSupplier {
        val newSupplier = ComponentSupplier(component, delayed.toMutableList(), transform.toMutableList())
        newSupplier.transform.add(trans)
        return newSupplier
    }

    // ////////////////////////////////
    // ****** 颜色 ******//
    // //////////////////////////////
    fun gray(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.GRAY) }
    }

    fun white(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.WHITE) }
    }

    fun gold(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.GOLD) }
    }

    fun red(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.RED) }
    }

    fun yellow(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.YELLOW) }
    }

    fun green(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.GREEN) }
    }

    fun orange(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(0xFFA500) }
    }

    fun aqua(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.AQUA) }
    }

    fun lightPurple(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.LIGHT_PURPLE) }
    }

    fun blue(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(ChatFormatting.BLUE) }
    }
    fun rainbow(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(TooltipHelper.RAINBOW.current) }
    }

    fun rainbowFast(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(TooltipHelper.RAINBOW_FAST.current) }
    }

    fun rainbowSlow(): ComponentSupplier = operatorComponent {
        withStyle { it.withColor(TooltipHelper.RAINBOW_SLOW.current) }
    }

    // ////////////////////////////////
    // ****** 滚动 ******//
    // //////////////////////////////
    fun scrollWhiteBlue(): ComponentSupplier = transformComponent { supplier ->
        StringUtils.white_blue(supplier.component.string).toLiteralSupplier()
    }
    fun scrollFullColor(): ComponentSupplier = transformComponent { supplier ->
        StringUtils.full_color(supplier.component.string).toLiteralSupplier()
    }

    // ////////////////////////////////
    // ****** 格式 ******//
    // //////////////////////////////
    fun italic(): ComponentSupplier {
        operatorComponent { withStyle { it.withItalic(true) } }
        return this
    }
    fun bold(): ComponentSupplier {
        operatorComponent { withStyle { it.withBold(true) } }
        return this
    }
    fun underline(): ComponentSupplier {
        operatorComponent { withStyle { it.withUnderlined(true) } }
        return this
    }
    fun strikethrough(): ComponentSupplier {
        operatorComponent { withStyle { it.withStrikethrough(true) } }
        return this
    }
    fun obfuscated(): ComponentSupplier {
        operatorComponent { withStyle { it.withObfuscated(true) } }
        return this
    }
    fun reset(): ComponentSupplier {
        operatorComponent { withStyle(ChatFormatting.RESET) }
        return this
    }
}
fun Component.toComponentSupplier() = ComponentSupplier(this.copy())
fun <T> T.toLiteralSupplier() = (Component.literal(this.toString())).toComponentSupplier()
infix fun String.translatedTo(other: String): ComponentSupplier {
    val translationKey = TranslationKeyProvider.getTranslationKey(this, other)
    return Component.translatable(translationKey).toComponentSupplier()
}

@JvmName("initialize")
fun ComponentSupplier.initialize(): ComponentSupplier = this.also { it.get() }

@JvmName("initializeList")
fun ComponentListSupplier.initialize(): ComponentListSupplier = this.also { it.get() }

@JvmName("initializeFloat")
fun ((Float) -> ComponentSupplier).initialize(): (Float) -> ComponentSupplier = this.also { it(0f) }

@JvmName("initializeInt")
fun ((Int) -> ComponentSupplier).initialize(): (Int) -> ComponentSupplier = this.also { it(0) }

@JvmName("initializeBoolean")
fun ((Boolean) -> ComponentSupplier).initialize(): (Boolean) -> ComponentSupplier = this.also { it(false) }

@JvmName("initializeString")
fun ((String) -> ComponentSupplier).initialize(): (String) -> ComponentSupplier = this.also { it("") }

@JvmName("initializeLong")
fun ((Long) -> ComponentSupplier).initialize(): (Long) -> ComponentSupplier = this.also { it(0L) }

@JvmName("initializeItem")
fun ((ComponentSupplier) -> ComponentSupplier).initialize(): (ComponentSupplier) -> ComponentSupplier = this.also { it("".toLiteralSupplier()) }
