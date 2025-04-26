package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.getThemedImage
import com.deckerpw.apex.ui.graphics.WindowCreator
import com.deckerpw.apex.ui.graphics.text
import com.deckerpw.apex.ui.graphics.theme
import java.awt.Button
import java.awt.Graphics2D

open class Window(parent: WindowManager?, x: Int, y: Int, width: Int, height: Int,var title: String, private var windowCreator: WindowCreator? = null) : Container(parent, x, y, width, height) {

    init {
        add(TitleBar())
        add(mappedImageButton(this, width - 13, 3, 10, 10, image = getThemedImage("textures/buttons/close_button.png")!!) { _, _, _ ->
            parent?.remove(this)
            parent?.update()
        })
    }

    override fun paint(graphics2D: Graphics2D) {
        if (windowCreator != null)
            graphics2D.drawImage(windowCreator!!.getWindowImage(width, height), 0, 0, null)
        else
            graphics2D.drawImage(theme.windowCreator.getWindowImage(width, height), 0, 0, null)
        super.paint(graphics2D)
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        super.onMouseDown(x, y, button)
        return true
    }

    fun close() {
        (parent as? Container)?.remove(this)
        parent?.update()
    }

    private inner class TitleBar : MouseWidget(this, 0, 0, width, 14, update = false ) {
        private var offsetX = 0
        private var offsetY = 0

        override fun paint(graphics2D: Graphics2D) {
            graphics2D.text(title, 5, 1)
        }

        override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
            super.onMouseDown(x, y, button)
            offsetX = x
            offsetY = y
            return true
        }

        override fun onMouseDrag(x: Int, y: Int, button: Int) {
            super.onMouseDrag(x, y, button)
            this@Window.x += x - offsetX
            this@Window.y += y - offsetY
            this@Window.update()
        }

    }

}