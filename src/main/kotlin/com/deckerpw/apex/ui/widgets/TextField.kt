package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.Keyboard
import com.deckerpw.apex.ui.graphics.text
import com.deckerpw.apex.ui.graphics.theme
import java.awt.Color
import java.awt.Graphics2D

class TextField(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    var text: String,
    val reChar: Char? = null,
    val placeholder: String? = null,
    val onEnterPressed : () -> Unit = {}
) : MouseWidget(parent, x, y, width, 20), Keyboard {

    val allowedChars =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=[]{}|;':,.<>/?`~\\\"".toCharArray()

    override fun paint(graphics2D: Graphics2D) {
        graphics2D.color = Color.BLACK
        graphics2D.fillRect(0, 0, width, 20)
        if (selected)
            graphics2D.color = theme.primaryColor
        else
            graphics2D.color = Color.WHITE
        graphics2D.drawRect(0, 0, width - 1, 19)
        graphics2D.color = Color.WHITE
        if (text.isEmpty()) {
            graphics2D.color = Color.GRAY
            graphics2D.text(placeholder ?: "", 4, 4, maxWidth = width - 8)
        } else if (reChar != null) {
            graphics2D.text(reChar.toString().repeat(text.length), 4, 4, maxWidth = width - 8)
        } else
            graphics2D.text(text, 4, 4, maxWidth = width - 8)
    }

    override fun onSelected() {
        super.onSelected()
        update()
    }

    override fun onDeselected() {
        super.onDeselected()
        update()
    }

    override fun onKeyDown(key: Int, char: Char) {
        when (key) {
            10 -> {
                onEnterPressed()
            }

            8 -> {
                if (text.isNotEmpty()) {
                    text = text.substring(0, text.length - 1)
                    update()
                }
            }

            else -> {
                if (allowedChars.contains(char)) {
                    text += char
                    update()
                }
            }
        }
    }

    override fun onKeyUp(key: Int, char: Char) {
    }
}