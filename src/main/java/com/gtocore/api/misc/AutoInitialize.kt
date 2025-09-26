package com.gtocore.api.misc

import com.gtocore.common.data.translation.*

import kotlin.reflect.KProperty1

open class AutoInitialize<T> {
    fun originInit() {
        GTOItemTooltips.init()
        OrganTranslation.init()
        GTOMachineStories.init()
        ComponentSlang.init()
        EnumTranslation.init()
        MachineSlang.init()
        MultiblockSlang.init()
        GTOTarotArcanumTooltips.init()
    }
    open fun init() {}
    init {
        // 自动初始化所有非 const 的 val 属性
        this::class.members
            .filterIsInstance<KProperty1<T, *>>()
            .filter { !it.isConst }
            .forEach { property ->
                try {
                    @Suppress("UNCHECKED_CAST")
                    property.get(this as T)
                } catch (ignore: Exception) {
                }
            }
    }
}
object AutoInitializeImpl : AutoInitialize<AutoInitializeImpl>()
