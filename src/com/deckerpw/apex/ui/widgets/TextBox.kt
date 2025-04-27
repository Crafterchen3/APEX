package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.Modifier
import com.deckerpw.apex.ui.getColor
import com.deckerpw.apex.ui.graphics.*
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class TextBox(
    parent: Widget?, x: Int, y: Int, width: Int, height: Int,
    var text: String,
    val modifier: Modifier = Modifier,
) : Widget(parent, x, y, width, height) {
    override fun paint(graphics2D: Graphics2D) {
        graphics2D.color = modifier.getColor()
        if (modifier.isCenteredString())
            graphics2D.centeredText(text, width/2, (height/2)+(modifier.getFontSize()/2), modifier.getFontName(), modifier.getFontStyle(), modifier.getFontSize())
        else
            graphics2D.text(text, 0, (height/2)+(modifier.getFontSize()/2), modifier.getFontName(), modifier.getFontStyle(), modifier.getFontSize(), width)
    }
}

fun Modifier.centerString(centered: Boolean): Modifier {
    return addElement("centerString", centered)
}

fun Modifier.isCenteredString(): Boolean {
    return getElementAs("centerString") ?: false
}