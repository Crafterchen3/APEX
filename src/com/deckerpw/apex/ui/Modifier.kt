package com.deckerpw.apex.ui

import java.awt.Color
import java.awt.Font

open class Modifier(protected val elements: MutableMap<String, Any> = mutableMapOf()) {

    fun addElement(key: String, value: Any): Modifier {

        return Modifier(elements.toMutableMap().apply { this[key] = value })
    }

    fun getElement(key: String): Any? {
        return elements[key]
    }

    fun <T> getElementAs(key: String): T? {
        return elements[key] as T?
    }

    companion object : Modifier()

}

fun Modifier.color(color: Color): Modifier {
    return addElement("color", color)
}

fun Modifier.getColor(): Color {
    return getElementAs("color") ?: Color.WHITE
}