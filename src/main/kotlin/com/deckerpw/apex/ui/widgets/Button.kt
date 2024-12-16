package com.deckerpw.apex.ui.widgets

import java.awt.Color
import java.awt.Graphics2D

class SolidButton(parent: Widget?, x: Int, y: Int, width: Int, height: Int, private val palette: Array<Color>,var action: Runnable) : MouseWidget(parent, x, y, width, height) {

    override fun paint(graphics2D: Graphics2D) {
        graphics2D.color = if (pressed) {
            if (hovered) {
                palette[2]
            } else {
                if (palette.size < 4) {
                    palette[2]
                } else {
                    palette[3]
                }
            }
        } else {
            if (hovered) {
                palette[1]
            } else {
                palette[0]
            }
        }
        graphics2D.fillRect(0, 0, width, height)
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        super.onMouseUp(x, y, button)
        if (hovered) {
            action.run()
        }
    }

}