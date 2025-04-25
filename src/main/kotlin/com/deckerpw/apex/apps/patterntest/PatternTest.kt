package com.deckerpw.apex.apps.patterntest

import com.deckerpw.apex.machine.Executable
import com.deckerpw.apex.machine.Program
import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.graphics.PatternFill
import com.deckerpw.apex.ui.widgets.Window
import com.deckerpw.apex.ui.widgets.WindowManager
import com.deckerpw.apex.ui.widgets.shapes.Rectangle
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.Timer

@Program
class PatternTest : Executable {
    override fun onStart() {
        machine.windowManager?.add(PatternTestWindow(machine.windowManager, 100, 100))
    }
}

class PatternTestWindow(parent: WindowManager?, x: Int, y: Int) : Window(parent, x, y, 300, 150, "Pattern Test") {

    init {
        val patternFill = PatternFill(getTestPattern(10,0))
        val rectangle = Rectangle(this, 1, 15, width-3, height-17, patternFill)
        add(rectangle)
        var offset = 0
        Timer(50){
            if (offset++ >= 9) offset = 0
            rectangle.fill = PatternFill(getTestPattern(10,offset))
            rectangle.update()
        }.start()
    }

}

private fun getTestPattern(size: Int,offset: Int): BufferedImage {
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    g.color = Color(0xA6CAEC)
    g.fillRect(0, 0, size, size)
    g.color = Color(0xDCEAF7)
    g.stroke = BasicStroke(2f)
    g.drawLine(-size+offset,size, offset, 0)
    g.drawLine(offset, size, size+offset, 0)
    return image
}