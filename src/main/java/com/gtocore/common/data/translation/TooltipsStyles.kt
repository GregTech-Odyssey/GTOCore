package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier

fun ComponentListSupplier.highlight(info: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { gold() }) {
    add(ComponentSlang.Star(1) + info, style)
}

fun ComponentListSupplier.story(story: ComponentSupplier) {
    add(story) { gray() }
}

fun ComponentListSupplier.section(title: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { gold() }, prefix: ComponentSupplier = ComponentSlang.Bar(1)) {
    add(prefix + title, style)
}

fun ComponentListSupplier.content(content: ComponentSupplier, style: ComponentSupplier.() -> ComponentSupplier = { white() }, prefix: ComponentSupplier = ComponentSlang.Circle(2)) {
    add(prefix + content, style)
}

fun ComponentListSupplier.info(info: ComponentSupplier) {
    content(info, { gray() }, ComponentSlang.OutTopic(2))
}

fun ComponentListSupplier.ok(info: ComponentSupplier) {
    content(info, { green() }, ComponentSlang.Right(2))
}

fun ComponentListSupplier.error(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Wrong(2))
}

fun ComponentListSupplier.command(info: ComponentSupplier) {
    content(info, { yellow() })
}

fun ComponentListSupplier.important(info: ComponentSupplier) {
    content(info, { red() })
}

fun ComponentListSupplier.function(info: ComponentSupplier) {
    content(info, { aqua() })
}

fun ComponentListSupplier.guide(info: ComponentSupplier) {
    content(info, { lightPurple() }, ComponentSlang.Asterisk(2))
}

fun ComponentListSupplier.increase(info: ComponentSupplier) {
    content(info, { green() }, ComponentSlang.Plus(2))
}

fun ComponentListSupplier.decrease(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Bar(2))
}

fun ComponentListSupplier.danger(info: ComponentSupplier) {
    content(info, { red() }, ComponentSlang.Warning(1))
}

// 固定搭配
fun ComponentListSupplier.miraculousTools(name: ComponentSupplier) {
    highlight(("妙妙工具: " translatedTo "Miraculous Tools: ") + name) { rainbow() }
}
