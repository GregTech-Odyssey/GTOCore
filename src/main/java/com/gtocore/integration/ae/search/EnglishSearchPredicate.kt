package com.gtocore.integration.ae.search

import com.gtocore.client.forge.GTOComponentHandler.englishLanguage

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.TranslatableContents

import appeng.menu.me.common.GridInventoryEntry
import me.lucko.spark.lib.adventure.text.TranslatableComponent

import java.util.*
import java.util.function.Predicate

@JvmRecord
data class EnglishSearchPredicate(val term: String?) : Predicate<GridInventoryEntry?> {
    override fun test(gridInventoryEntry: GridInventoryEntry?): Boolean {
        englishLanguage ?: return false
        val entryInfo = gridInventoryEntry!!.what
        val displayName: MutableComponent = entryInfo!!.displayName as? MutableComponent
            ?: return false
        val foundIds = mutableListOf<String>()
        findAllTranslatableComponent(displayName, foundIds)
        for (id in foundIds) {
            val translated = englishLanguage?.getOrDefault(id, entryInfo.displayName.string)
            if (translated != null && translated.lowercase(Locale.getDefault())
                    .contains(term!!.lowercase(Locale.getDefault()))
            ) {
                return true
            }
        }
        return false
    }
}

fun findAllTranslatableComponent(component: Component, foundIds: MutableList<String>) {
    if (component is TranslatableComponent) {
        foundIds.add(component.key())
    }
    if (component is MutableComponent) {
        if (component.contents is TranslatableContents) {
            val content: TranslatableContents = component.contents as TranslatableContents
            foundIds.add(content.key)
            for (arg in content.args) {
                if (arg is Component) {
                    findAllTranslatableComponent(arg, foundIds)
                }
            }
        }
    }
    for (sibling in component.siblings) {
        findAllTranslatableComponent(sibling, foundIds)
    }
}
