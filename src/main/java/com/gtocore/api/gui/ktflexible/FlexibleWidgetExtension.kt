package com.gtocore.api.gui.ktflexible

import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup

fun WidgetGroup.ktAddWidget(widget: Widget) {
    val saves = this.widgets
    this.clearAllWidgets()
    saves.forEach(::addWidget)
    addWidget(widget)
    this.initWidget()
}
fun WidgetGroup.ktRefreshWidget() {
    val saves = this.widgets
    this.clearAllWidgets()
    saves.forEach(::addWidget)
    this.initWidget()
}
