package com.deckerpw.apex.ui.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

interface Fill {

    fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int)

}

class SolidFill(private val color: Int) : Fill {

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        graphics2D.color = Color(color)
        graphics2D.fillRect(x, y, width, height)
    }

}

fun Color.asSolidFill(): Fill {
    return SolidFill(rgb)
}

class GradientFill(private val color1: Int, private val color2: Int) : Fill {

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        val gradient = java.awt.GradientPaint(0f, 0f, Color(color1), width.toFloat(), height.toFloat(), Color(color2))
        graphics2D.paint = gradient
        graphics2D.fillRect(x, y, width, height)
    }

}

class TextureFill(private val texture: BufferedImage) : Fill {

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        graphics2D.drawImage(texture, x, y, width, height, null)
    }

}