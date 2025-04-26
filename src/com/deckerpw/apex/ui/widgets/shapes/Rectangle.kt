package com.deckerpw.apex.ui.widgets.shapes

import com.deckerpw.apex.ui.graphics.Fill
import com.deckerpw.apex.ui.graphics.TextureFill
import com.deckerpw.apex.ui.widgets.Widget
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Rectangle(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    var fill: Fill
) : Widget(parent, x, y, width, height) {

    override fun paint(graphics2D: Graphics2D) {
        fill.render(graphics2D, 0, 0, width, height)
    }

}

fun BufferedImage.createRectangle(parent: Widget?, x: Int, y: Int, width: Int? = null, height: Int? = null): Rectangle {
    val w = width ?: this.width
    val h = height ?: this.height
    val textureFill = TextureFill(this)
    return Rectangle(parent, x, y, w, h, textureFill)
}