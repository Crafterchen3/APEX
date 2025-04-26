package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.Mouse
import java.awt.Graphics2D
import java.awt.image.BufferedImage

abstract class Widget(
    internal open val parent: Widget?,
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int
) {

    protected var buffer: BufferedImage? = null
    var selected: Boolean = false
        set(value) {
            field = value
            if (value)
                onSelected()
            else
                onDeselected()
        }

    open fun update(first: Boolean = false) {
        if (first){
            Thread{
                buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
                val graphics2D = buffer!!.createGraphics()
                paint(graphics2D)
                graphics2D.dispose()

                parent?.update(false)
            }.start()
        }else{
            buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics2D = buffer!!.createGraphics()
            paint(graphics2D)
            graphics2D.dispose()

            parent?.update(false)
        }

    }

    fun render(graphics2D: Graphics2D) {
        if (buffer == null)
            update()
        graphics2D.drawImage(buffer, x, y, null)
    }

    protected open fun onSelected() {
        // Do nothing by default
    }

    protected open fun onDeselected() {
        // Do nothing by default
    }

    protected abstract fun paint(graphics2D: Graphics2D)

    fun getPositionOnScreen(x: Int, y: Int) : Pair<Int, Int> {
        return parent?.getPositionOnScreen(x + this.x, y + this.y) ?: Pair(x + this.x, y + this.y)
    }

}

abstract class MouseWidget(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    protected open var update: Boolean = true
) : Widget(parent, x, y, width, height), Mouse {

    protected var hovered = false
    protected var pressed = false

    override fun onMouseMove(x: Int, y: Int): Boolean {
        return true
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        // Do nothing
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        pressed = true
        if (update)
            update()
        return true
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        pressed = false
        if (update)
            update()
    }

    override fun onMouseWheel(x: Int, y: Int, direction: Int) {
        // Do nothing
    }

    override fun onEnter() {
        hovered = true
        if (update)
            update()
    }

    override fun onExit() {
        hovered = false
        if (update)
            update()
    }

}