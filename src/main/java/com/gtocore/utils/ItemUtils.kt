package com.gtocore.utils

import com.gtocore.api.lang.ComponentListSupplier

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item

import com.gtolib.api.item.IItem

import java.util.function.Supplier

fun Item.setTooltips(vararg components: Supplier<Component>) {
    (this as IItem).`gtolib$setToolTips`(*components)
}
fun Item.setTooltips(listSupplier: ComponentListSupplier) {
    this.setTooltips(*listSupplier.list.toTypedArray())
}
