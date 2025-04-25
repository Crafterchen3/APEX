package com.deckerpw.apex.ui

import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.graphics.PatternFill
import com.deckerpw.apex.ui.graphics.centeredText
import com.deckerpw.apex.ui.widgets.Container
import com.deckerpw.apex.ui.widgets.Widget
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.*
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.ceil

private const val fullscreen = true
private const val scale: Double = 2.0
var screenWidth = 800
var screenHeight = 600

open class TetheredContainer internal constructor(): Container(null, 0, 0, screenWidth, screenHeight){

    val window = Display(this)

    override fun update(first: Boolean) {
        // capture frame time
        var currentTime = System.nanoTime()
        super.update(first)
        SwingUtilities.invokeLater {
            window.repaint()
            println("Took ${(System.nanoTime() - currentTime) / 1000000.0}ns to update")
        }
    }
}

val testPatternFill = PatternFill(getTestPattern(49))

private fun getTestPattern(size: Int): BufferedImage {
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    g.color = Color(0x171717)
    g.fillRect(0, 0, size, size)
    g.color = Color(0x1b1b1b)
    g.fillRect(0, size / 2, size, 1)
    g.fillRect(size / 2, 0, 1, size)
    g.color = Color(0x1f1f1f)
    g.fillRect(size / 2, size / 2, 1, 1)
    return image
}

class Display internal constructor(private val container: Container) : JFrame() {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        if (fullscreen) {
            isUndecorated = true
            extendedState = MAXIMIZED_BOTH
        } else {
            setLocation(5, 5)
            setSize((screenWidth* scale).toInt(), (screenHeight* scale).toInt())
        }
        isVisible = true
        //createBufferStrategy(2)
        screenWidth = ceil((width / scale)).toInt()
        screenHeight = ceil((height / scale)).toInt()
        container.width = screenWidth
        container.height = screenHeight
        addMouseListener(Listener(container))
        addMouseMotionListener(Listener(container))
        addMouseWheelListener(Listener(container))
        addKeyListener(Listener(container))
    }

    override fun paint(g: Graphics?) {
        screenWidth = ceil((width / scale)).toInt()
        screenHeight = ceil((height / scale)).toInt()
        val image = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
        container.render(image.createGraphics())
        g?.drawImage(image, 0, 0, (screenWidth * scale).toInt(), (screenHeight * scale).toInt(), null)
//        //pick a random color every frame
//        g?.color = Color((Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt())
//        g?.fillRect(width - 50, height - 50, 50, 50)
    }

    class Listener(val container: Container) : MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
        override fun mouseClicked(e: MouseEvent?) {
            //Do nothing
        }

        override fun mousePressed(e: MouseEvent?) {
            container.onMouseDown(((e?.x ?: 0) / scale).toInt(), ((e?.y ?: 0) / scale).toInt(), e?.button ?: 0)
        }

        override fun mouseReleased(e: MouseEvent?) {
            container.onMouseUp(((e?.x ?: 0) / scale).toInt(), ((e?.y ?: 0) / scale).toInt(), e?.button ?: 0)
        }

        override fun mouseEntered(e: MouseEvent?) {
            //Do nothing
        }

        override fun mouseExited(e: MouseEvent?) {
            //Do nothing
        }

        override fun keyTyped(e: KeyEvent?) {
            //Do nothing
        }

        override fun keyPressed(e: KeyEvent?) {
            container.onKeyDown(e?.keyCode ?: 0, e?.keyChar ?: 0.toChar())
        }

        override fun keyReleased(e: KeyEvent?) {
            container.onKeyUp(e?.keyCode ?: 0, e?.keyChar ?: 0.toChar())
        }

        override fun mouseDragged(e: MouseEvent?) {
            container.onMouseDrag(((e?.x ?: 0) / scale).toInt(), ((e?.y ?: 0) / scale).toInt(), e?.button ?: 0)
        }

        override fun mouseMoved(e: MouseEvent?) {
            container.onMouseMove(((e?.x ?: 0) / scale).toInt(), ((e?.y ?: 0) / scale).toInt())
        }

        override fun mouseWheelMoved(e: MouseWheelEvent?) {
            container.onMouseWheel(((e?.x ?: 0) / scale).toInt(), ((e?.y ?: 0) / scale).toInt(), e?.scrollAmount ?: 0)
        }

    }

}

private class ErrorScene(parent: Container) : Widget(parent, 0, 0, screenWidth, screenHeight) {
    override fun paint(graphics2D: Graphics2D) {
        testPatternFill.render(graphics2D,0,0,width,height)
        graphics2D.color = Color.RED
        graphics2D.centeredText("You shouldn't be seeing this.", width / 2, height / 2-12, fontSize = 20)
        graphics2D.centeredText("Something went wrong.", width / 2, height / 2+12, fontSize = 20)
    }
}

class Screen : TetheredContainer() {

    val currentScene: Widget
        get() = children[0]
    val initTime = System.currentTimeMillis()

    init {
        if (machine.screen != null) {
            throw IllegalStateException("Screen already exists")
        }
        add(ErrorScene(this))
    }

    fun setScene(scene: Widget) {
        clear()
        add(scene)
        selectWidget(scene)
        update()
    }

    override fun update(first: Boolean) {
        if (children.isEmpty() && System.currentTimeMillis() - initTime > 1000) {
            add(ErrorScene(this))
        }
        super.update(first)
    }

}