package com.gtocore.utils

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item

import com.google.common.base.Supplier
import com.gtolib.IItem

fun Item.setTooltips(vararg components: Supplier<Component>) {
    (this as IItem).`gtolib$setToolTips`(*components)
}
fun Item.setTooltips(listSupplier: ComponentListSupplier) {
    this.setTooltips(*listSupplier.list.toTypedArray())
}
