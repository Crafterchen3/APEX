package com.deckerpw.apex

import com.deckerpw.apex.ui.graphics.GradientFill
import com.deckerpw.apex.ui.graphics.theme
import com.deckerpw.apex.ui.widgets.*
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.awt.image.BufferedImage
import javax.swing.JFrame

class TetheredContainer() : Container(null, 0, 0, 800, 600, GradientFill(0x00C6FF,0x0094FF)) {

    val window: TestWindow = TestWindow(this)

    override fun update() {
        super.update()
        window.repaint()
    }
}

fun main() {
    println("Hello, world!")
    val image = theme.windowCreator.getWindowImage(100, 100)
    val container = TetheredContainer()
    val windowManager = WindowManager(container, 0, 0, 800, 600)
    container.add(SolidButton(container, 10, 10, 50, 50, arrayOf(Color.RED, Color.GREEN, Color.BLUE)) {
        println("Button clicked!")
    })
    container.add(windowManager)
    val window = Window(windowManager, 100, 100, 200, 200)
    windowManager.add(window)
    window.add(SolidButton(window, 10, 10, 50, 50, arrayOf(Color.RED, Color.GREEN, Color.BLUE)) {
        println("Button clicked!")
    })
    //window.update()
}

private const val scale = 2

class TestWindow(private val container: Container) : JFrame() {

    init {
        setSize(800* scale, 600* scale)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        addMouseListener(Listener(container))
        addMouseMotionListener(Listener(container))
        addMouseWheelListener(Listener(container))
        addKeyListener(Listener(container))
        rootPane.addKeyListener(Listener(container))
    }

    override fun paint(g: Graphics?) {
        val image = BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)
        container.render(image.createGraphics())
        g?.drawImage(image, 0, 0,800* scale,600* scale, null)
    }

    class Listener(val container: Container) : MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
        override fun mouseClicked(e: MouseEvent?) {
            //Do nothing
        }

        override fun mousePressed(e: MouseEvent?) {
            container.onMouseDown((e?.x ?: 0)/ scale, (e?.y ?: 0)/ scale, e?.button ?: 0)
        }

        override fun mouseReleased(e: MouseEvent?) {
            container.onMouseUp((e?.x ?: 0)/ scale, (e?.y ?: 0)/ scale, e?.button ?: 0)
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
            container.onKeyDown(e?.keyCode ?: 0)
        }

        override fun keyReleased(e: KeyEvent?) {
            container.onKeyUp(e?.keyCode ?: 0)
        }

        override fun mouseDragged(e: MouseEvent?) {
            container.onMouseDrag((e?.x ?: 0)/ scale, (e?.y ?: 0)/ scale, e?.button ?: 0)
        }

        override fun mouseMoved(e: MouseEvent?) {
            container.onMouseMove((e?.x ?: 0)/ scale, (e?.y ?: 0)/ scale)
        }

        override fun mouseWheelMoved(e: MouseWheelEvent?) {
            container.onMouseWheel((e?.x ?: 0)/ scale, (e?.y ?: 0)/ scale, e?.scrollAmount ?: 0)
        }

    }

}
