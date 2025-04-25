package com.deckerpw.apex.ui.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

interface Fill {

    fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int)

}

class SolidFill(private val color: Color) : Fill {

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        graphics2D.color = color
        graphics2D.fillRect(x, y, width, height)
    }

}

fun Color.asSolidFill(): Fill {
    return SolidFill(this)
}

abstract class SmartFill: Fill {

    private var bufferWidth: Int? = null
    private var bufferHeight: Int? = null
    private var bufferImage: BufferedImage? = null

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        if (bufferWidth != width || bufferHeight != height) {
            bufferWidth = width
            bufferHeight = height
            bufferImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics2D = bufferImage!!.createGraphics()
            paint(graphics2D, width, height)
            graphics2D.dispose()
        }
        graphics2D.drawImage(bufferImage, x, y, null)
    }

    abstract fun paint(graphics2D: Graphics2D, width: Int, height: Int)

}

class PatternFill(private val pattern: BufferedImage) : SmartFill() {

    override fun paint(graphics2D: Graphics2D, width: Int, height: Int) {
        val patternWidth = pattern.width
        val patternHeight = pattern.height
        for (x in 0 until width step patternWidth) {
            for (y in 0 until height step patternHeight) {
                graphics2D.drawImage(pattern, x, y, patternWidth, patternHeight, null)
            }
        }
    }

}

class GradientFill(private val color1: Int, private val color2: Int) : SmartFill() {

    override fun paint(graphics2D: Graphics2D, width: Int, height: Int) {
        val gradient = java.awt.GradientPaint(0f, 0f, Color(color1), width.toFloat(), height.toFloat(), Color(color2))
        graphics2D.paint = gradient
        graphics2D.fillRect(0, 0, width, height)
    }

}

class TextureFill(private val texture: BufferedImage) : Fill {

    override fun render(graphics2D: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
        graphics2D.drawImage(texture, x, y, width, height, null)
    }

}