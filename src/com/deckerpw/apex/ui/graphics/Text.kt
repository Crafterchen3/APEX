package com.deckerpw.apex.ui.graphics

import com.deckerpw.apex.ui.Modifier
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage

private data class TextProps(
    val text: String,
    val fontName: String,
    val fontStyle: Int,
    val fontSize: Int,
    val color: Color
)

private val textPropsCache = mutableMapOf<TextProps, BufferedImage>()

fun Graphics2D.text(
    text: String,
    x: Int,
    y: Int,
    fontName: String = "Verdana",
    fontStyle: Int = Font.PLAIN,
    fontSize: Int = 10,
    maxWidth: Int = Int.MAX_VALUE,
) {
    if (text.isNotEmpty()) {
        val textProps = TextProps(text, fontName, fontStyle, fontSize, color)
        var image = textPropsCache[textProps]
        if (image == null) {
            genText(textProps, this)
            image = textPropsCache[textProps]
        }
        if (image!!.width > maxWidth) {
            drawImage(image, x - (image.width - maxWidth), y, null)
        } else {
            drawImage(image, x, y, null)
        }
    }
}


fun Graphics2D.centeredText(
    text: String,
    x: Int,
    y: Int,
    fontName: String = "Verdana",
    fontStyle: Int = Font.PLAIN,
    fontSize: Int = 10,
) {
    if (text.isNotEmpty()) {
        val textProps = TextProps(text, fontName, fontStyle, fontSize, color)
        if (textPropsCache[textProps] == null) {
            genText(textProps, this)
        }
        drawImage(
            textPropsCache[textProps],
            x - textPropsCache[textProps]!!.width / 2,
            y - textPropsCache[textProps]!!.height / 2,
            null
        )
    }
}

private fun genText(text: TextProps, graphics2D: Graphics2D) {
    val bounds = graphics2D.getFontMetrics(Font(text.fontName, text.fontStyle, text.fontSize))
        .getStringBounds(text.text, graphics2D)
    val image = BufferedImage(bounds.width.toInt(), bounds.height.toInt(), BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    g.color = text.color
    g.font = Font(text.fontName, text.fontStyle, text.fontSize)
    g.drawString(text.text, 0, -bounds.y.toInt())
    textPropsCache[text] = image
}

fun Modifier.fontName(fontName: String): Modifier {
    return addElement("fontName", fontName)
}

fun Modifier.getFontName(): String {
    return getElementAs("fontName") ?: "Verdana"
}

fun Modifier.fontSize(fontSize: Int): Modifier {
    return addElement("fontSize", fontSize)
}

fun Modifier.getFontSize(): Int {
    return getElementAs("fontSize") ?: 10
}

fun Modifier.fontStyle(fontStyle: Int): Modifier {
    return addElement("fontStyle", fontStyle)
}

fun Modifier.getFontStyle(): Int {
    return getElementAs("fontStyle") ?: Font.PLAIN
}