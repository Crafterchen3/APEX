package com.deckerpw.apex.ui.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

val theme = Theme()

class Theme {

    var titleBarColor = Color(0xaa00000)
    var primaryColor = Color(0xff5555)
    var windowBGColor = Color(0x353535)

    val windowCreator = ThemeWindowCreator(this)
    val assetDir = "J:/assets"
}

abstract class WindowCreator {

    private val windowMatrix : MutableMap<Int, MutableMap<Int, BufferedImage>> = mutableMapOf()

    fun getWindowImage(width: Int, height: Int): BufferedImage {
        return windowMatrix[width]?.get(height) ?: run {
            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics2D = image.createGraphics()
            createWindow(graphics2D, width, height)
            graphics2D.dispose()
            if (windowMatrix[width] == null) windowMatrix[width] = mutableMapOf()
            windowMatrix[width]?.set(height, image)
            image
        }

    }

    abstract fun createWindow(graphics2D: Graphics2D, width: Int, height: Int)

}

class ThemeWindowCreator(private val ctheme: Theme = theme) : WindowCreator() {

    override fun createWindow(graphics2D: Graphics2D,width: Int, height: Int) {

        graphics2D.color = Color.WHITE
        graphics2D.drawRect(0,0,width-2,height-2)

        graphics2D.color = ctheme.windowBGColor
        graphics2D.fillRect(1, 1, width - 3, height - 3)

        graphics2D.color = ctheme.titleBarColor
        graphics2D.fillRect(1, 1, width-3, 13)

        graphics2D.color = Color(255,255,255,70)
        graphics2D.drawLine(1,1,width-3,1)
        graphics2D.drawLine(1,1,1,height-2)
        graphics2D.drawLine(1,height-3,width-3,height-3)
        graphics2D.drawLine(width-3,1,width-3,height-3)

        graphics2D.color = Color(0,0,0,70)
        graphics2D.drawLine(1,14,width-3,14)
        graphics2D.drawLine(1,height-1,width-1,height-1)
        graphics2D.drawLine(width-1,1,width-1,height-1)

    }

}