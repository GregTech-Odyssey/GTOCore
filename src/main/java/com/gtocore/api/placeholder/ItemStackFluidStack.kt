package com.gtocore.api.placeholder

import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

sealed interface ItemStackFluidStack {
    /**
     * data class 自动提供了 equals(), hashCode(), toString() 等方法。
     * 这里的 `val stack` 是一个不可变的公共属性。
     */
    data class Item(val stack: ItemStack) : ItemStackFluidStack
    data class Fluid(val stack: FluidStack) : ItemStackFluidStack
}