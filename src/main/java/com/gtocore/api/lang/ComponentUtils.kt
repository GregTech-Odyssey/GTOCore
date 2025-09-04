package com.gtocore.api.lang

import com.gtocore.common.data.translation.ComponentSlang

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

import com.google.common.base.Supplier
import com.gregtechceu.gtceu.client.util.TooltipHelper
import com.gtolib.api.annotation.NewDataAttributes
import com.gtolib.api.annotation.component_builder.TranslationKeyProvider
import com.gtolib.utils.StringUtils
class ComponentListSupplier(var list: MutableList<ComponentSupplier> = mutableListOf()) : Supplier<List<Component>> {
    var translationPrefix: String = ""
        private set
    var line: Int = 0

    override fun get(): List<Component> {
        val result = list.map { it.get() }
        return result
    }
    fun getSupplier(): Supplier<List<Component>> = this
    fun getArray(): Array<Component> = get().toTypedArray()

    fun add(component: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { this }) {
        val styledComponent = style(component)
        list.add(styledComponent)
        line += 1
    }
    fun add(other: ComponentListSupplier) {
        list.addAll(other.list)
    }
    fun add(other: ComponentListSupplier, style: ComponentSupplier.() -> ComponentSupplier = { this }) {
        for (supplier in other.list) {
            add(supplier, style)
        }
    }

    fun apply(tooltips: MutableList<Component>) {
        tooltips.addAll(get())
    }

    // ////////////////////////////////
    // ****** 翻译前缀 ******//
    // //////////////////////////////
    fun setTranslationPrefix(prefix: String) {
        this.translationPrefix = prefix
    }

    infix fun String.translatedTo(other: String): ComponentSupplier {
        if (this@translatedTo == other) return this.toLiteralSupplier()
        val prefix = if (translationPrefix.isNotEmpty()) "${NewDataAttributes.PREFIX}.$translationPrefix.$line" else "${NewDataAttributes.PREFIX}.$line"
        val translationKey = TranslationKeyProvider.getTranslationKey(this@translatedTo, other, prefix)
        return Component.translatable(translationKey).toComponentSupplier()
    }

    infix fun String.multiTranslatedToGray(other: String) = ComponentListSupplier {
        val cns = this@multiTranslatedToGray.lines()
        val ens = other.lines()
        require(cns.size == ens.size) { "翻译错误: 中文和英文数量不一致,于 $translationPrefix - $line 行" }
        for (i in cns.indices) {
            add(cns[i].translatedTo(ens[i])) { gray() }
        }
    }

    // ////////////////////////////////
    // ****** 便捷指令 ******//
    // //////////////////////////////
    fun editionByGTONormal(): ComponentListSupplier = this.apply {
        add(ComponentSlang.GTOSignal_Edition_ByGTONormal)
    }
    fun editionByGTORemix(): ComponentListSupplier = this.apply {
        add(ComponentSlang.GTOSignal_Edition_ByGTORemix)
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

    // 数据类 - 统一金色系
    fun InfoData() = transformComponent { text: ComponentSupplier -> text.gold() }
    fun InfoDataImportant() = transformComponent { text: ComponentSupplier -> text.gold().bold() }

    // 功能类 - 青色系
    fun InfoFunction() = transformComponent { text: ComponentSupplier -> text.aqua() }
    fun InfoFunctionPrimary() = transformComponent { text: ComponentSupplier -> text.aqua().bold() }

    // 安全类 - 绿色系
    fun InfoSafe() = transformComponent { text: ComponentSupplier -> text.green() }
    fun InfoRecommend() = transformComponent { text: ComponentSupplier -> text.green().bold() }

    // 警告类 - 黄色系
    fun InfoWarning() = transformComponent { text: ComponentSupplier -> text.yellow() }
    fun InfoCaution() = transformComponent { text: ComponentSupplier -> text.yellow().bold() }

    // 危险类 - 红色系
    fun InfoDanger() = transformComponent { text: ComponentSupplier -> text.red() }
    fun InfoCritical() = transformComponent { text: ComponentSupplier -> text.red().bold() }

    // 交互类 - 白色系
    fun InfoOperation() = transformComponent { text: ComponentSupplier -> text.white() }
    fun InfoOperationKey() = transformComponent { text: ComponentSupplier -> text.white().bold() }

    // 辅助类 - 灰色系
    fun InfoSupport() = transformComponent { text: ComponentSupplier -> text.gray() }
    fun InfoBrand() = transformComponent { text: ComponentSupplier -> text.gray().italic() }

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
        operatorComponent { withStyle(ChatFormatting.ITALIC) }
        return this
    }
    fun bold(): ComponentSupplier {
        operatorComponent { withStyle(ChatFormatting.BOLD) }
        return this
    }
    fun underline(): ComponentSupplier {
        operatorComponent { withStyle(ChatFormatting.UNDERLINE) }
        return this
    }
    fun strikethrough(): ComponentSupplier {
        operatorComponent { withStyle(ChatFormatting.STRIKETHROUGH) }
        return this
    }
    fun obfuscated(): ComponentSupplier {
        operatorComponent { withStyle(ChatFormatting.OBFUSCATED) }
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

@JvmName("initializeListInt")
fun ((Int) -> ComponentListSupplier).initialize(): (Int) -> ComponentListSupplier = this.also { it(0) }

@JvmName("initializeBoolean")
fun ((Boolean) -> ComponentSupplier).initialize(): (Boolean) -> ComponentSupplier = this.also { it(false) }

@JvmName("initializeString")
fun ((String) -> ComponentSupplier).initialize(): (String) -> ComponentSupplier = this.also { it("") }

@JvmName("initializeLong")
fun ((Long) -> ComponentSupplier).initialize(): (Long) -> ComponentSupplier = this.also { it(0L) }

@JvmName("initializeItem")
fun ((ComponentSupplier) -> ComponentSupplier).initialize(): (ComponentSupplier) -> ComponentSupplier = this.also { it("".toLiteralSupplier()) }

@JvmName("initializeListItem")
fun ((ComponentSupplier) -> ComponentListSupplier).initialize(): (ComponentSupplier) -> ComponentListSupplier = this.also { it("".toLiteralSupplier()) }

@JvmName("initializeListFloat")
fun ((Float) -> ComponentListSupplier).initialize(): (Float) -> ComponentListSupplier = this.also { it(0f) }

@JvmName("initializeListString")
fun ((String) -> ComponentListSupplier).initialize(): (String) -> ComponentListSupplier = this.also { it("") }

@JvmName("initializeListString2")
fun ((String, String) -> ComponentListSupplier).initialize(): (String, String) -> ComponentListSupplier = this.also { it("", "") }
