package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.graphics.Fill
import com.deckerpw.apex.ui.graphics.TextureFill
import com.deckerpw.apex.ui.graphics.asSolidFill
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class SolidButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    private val palette: List<Fill>,
    var action: Runnable
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
        fill.render(graphics2D, 0, 0, width, height)
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        super.onMouseUp(x, y, button)
        if (hovered) {
            action.run()
        }
    }

}

fun colorButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    palette: Array<Color>,
    action: Runnable
): SolidButton {
    return SolidButton(parent, x, y, width, height, palette.map { it.asSolidFill() }, action)
}

fun imageButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    palette: List<BufferedImage>,
    action: Runnable
): SolidButton {
    return SolidButton(parent, x, y, width, height, palette.map { TextureFill(it) }, action)
}

fun mappedImageButton(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    image: BufferedImage,
    action: Runnable
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
    return SolidButton(parent, x, y, width, height, palette, action)
}