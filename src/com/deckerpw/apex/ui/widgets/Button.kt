package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.graphics.*
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class SolidButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val label: String? = null,
    private val palette: List<Fill>,
    val drawBorder: Boolean,
    var action: (button: SolidButton, x: Int, y: Int) -> Unit
) : MouseWidget(parent, x, y, width, height) {

    //pallette[0] = normal
    //pallette[1] = hovered
    //pallette[2] = pressed or pressed and hovered if no 3rd color
    //pallette[3] = pressed and hovered

    override fun paint(graphics2D: Graphics2D) {
        val fill = if (pressed) {
            if (hovered) {
                if (palette.size < 4) {
                    palette[2]
                } else {
                    palette[3]
                }
            } else {
                palette[2]
            }
        } else {
            if (hovered) {
                palette[1]
            } else {
                palette[0]
            }
        }
        val borderColor = Color(0,0,0,140)
        fill.render(graphics2D, 0, 0, width, height)
        if (drawBorder){
            graphics2D.color = borderColor
            graphics2D.stroke = BasicStroke(2f)
            graphics2D.drawRect(1, 1, width - 2, height - 2)
        }
        if (label != null) {
            graphics2D.color = Color.BLACK
            graphics2D.centeredText(label, width/2, 9)
        }
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        super.onMouseUp(x, y, button)
        if (hovered) {
            action(this, x, y)
        }
    }

}

fun colorButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    label: String? = null,
    palette: Array<Color>,
    drawBorder: Boolean = true,
    action: (button: SolidButton, x: Int, y: Int) -> Unit
): SolidButton {
    return SolidButton(parent, x, y, width, height, label, palette.map { it.asSolidFill() }, drawBorder, action)
}

fun imageButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    label: String? = null,
    palette: List<BufferedImage>,
    drawBorder: Boolean = true,
    action: (button: SolidButton, x: Int, y: Int) -> Unit
): SolidButton {
    return SolidButton(parent, x, y, width, height, label, palette.map { TextureFill(it) },drawBorder, action)
}

fun mappedImageButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    label: String? = null,
    image: BufferedImage,
    drawBorder: Boolean = false,
    action: (button: SolidButton, x: Int, y: Int) -> Unit
): SolidButton {
    val palette = mutableListOf<Fill>()
    if (image.width >= width * 4) {
        for (i in 0 until 4) {
            palette.add(TextureFill(image.getSubimage(i * width, 0, width, height)))
        }
    } else if (image.width >= width * 3) {
        for (i in 0 until 3) {
            palette.add(TextureFill(image.getSubimage(i * width, 0, width, height)))
        }
    } else if (image.width >= width * 2) {
        palette.add(TextureFill(image.getSubimage(0, 0, width, height)))
        palette.add(TextureFill(image.getSubimage(0, 0, width, height)))
        palette.add(TextureFill(image.getSubimage(width, 0, width, height)))
    } else {
        throw IllegalArgumentException("Image is too small to be a mapped image button")
    }
    return SolidButton(parent, x, y, width, height, label, palette, drawBorder, action)
}