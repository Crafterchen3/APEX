package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.graphics.centeredText
import com.deckerpw.apex.ui.graphics.text
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class TextBox(
    parent: Widget?, x: Int, y: Int, width: Int, height: Int,
    var color: Color,
    var centered: Boolean = false,
    var text: String,
    var fontName: String = "Verdana",
    var fontStyle: Int = Font.PLAIN,
    var fontSize: Int = 10,
) : Widget(parent, x, y, width, height) {
    override fun paint(graphics2D: Graphics2D) {
        graphics2D.color = color
        if (centered)
            graphics2D.centeredText(text, width/2, (height/2)+(fontSize/2), fontName, fontStyle, fontSize)
        else
            graphics2D.text(text, 0, (height/2)+(fontSize/2), fontName, fontStyle, fontSize, width)
    }
}