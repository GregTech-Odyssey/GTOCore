package com.gtocore.api.gui.ktflexible

sealed class FlexibleColorStyle {
    abstract val color: Int
    class HEX(override val color: Int) : FlexibleColorStyle() {
        constructor(color: Long) : this(color.toInt())
    }
}
