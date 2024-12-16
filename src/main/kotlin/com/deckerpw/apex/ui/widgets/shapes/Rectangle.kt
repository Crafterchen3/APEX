package com.deckerpw.apex.ui.widgets.shapes

import com.deckerpw.apex.ui.graphics.Fill
import com.deckerpw.apex.ui.widgets.Widget
import java.awt.Graphics2D

class Rectangle(
    parent: Widget,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val fill: Fill
) : Widget(parent, x, y, width, height) {

    override fun paint(graphics2D: Graphics2D) {
        fill.render(graphics2D, x, y, width, height)
    }

}