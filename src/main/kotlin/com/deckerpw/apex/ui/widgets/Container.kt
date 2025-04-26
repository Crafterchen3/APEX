package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.Keyboard
import com.deckerpw.apex.ui.Mouse
import com.deckerpw.apex.ui.asMouse
import com.deckerpw.apex.ui.graphics.Fill
import java.awt.Graphics2D

open class Container(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val background: Fill? = null,
    var shouldReorder: Boolean = false
) : Widget(parent, x, y, width, height), Mouse, Keyboard {

    protected val children = mutableListOf<Widget>()
    internal var selectedWidget: Widget? = null
    private var hoveringWidget: Widget? = null
    private var clickedWidget: Widget? = null
    protected var offsetX = 0;
    protected var offsetY = 0;

    fun selectWidget(widget: Widget) {
        selectedWidget?.selected = false
        selectedWidget = widget
        selectedWidget?.selected = true
    }

    fun add(widget: Widget, autoUpdate: Boolean = true) {
        children.add(widget)
        if (autoUpdate)
            update()
    }

    fun remove(widget: Widget) {
        children.remove(widget)
        update()
    }

    override fun paint(graphics2D: Graphics2D) {
        background?.render(graphics2D, 0, 0, width, height)
        if (children.isEmpty())
            return
        val graphics2Db = graphics2D.create(offsetX,offsetY,width,height) as Graphics2D
        children.forEach { it.render(graphics2Db) }
    }

    override fun onDeselected() {
        selectedWidget?.selected = false
        selectedWidget = null
    }

    fun getWidgetAt(x: Int, y: Int): Widget? {
        val realX = x - offsetX
        val realY = y - offsetY
        return children.reversed().find { it.x <= realX && it.x + it.width >= realX && it.y <= realY && it.y + it.height >= realY }
    }

    fun getWidgets(): List<Widget> {
        return children.toList()
    }

    fun clear() {
        children.clear()
    }

    fun moveToTop(widget: Widget) {
        children.remove(widget)
        children.add(widget)
        update()
    }

    private fun checkHover(x: Int, y: Int):Boolean {
        val realX = x - offsetX
        val realY = y - offsetY
        for (widget in children.reversed()) {
            if (widget.x <= realX && widget.x + widget.width >= realX && widget.y <= realY && widget.y + widget.height >= realY)
                if (widget.asMouse()?.onMouseMove(realX - widget.x, realY - widget.y) == true) {
                    if (hoveringWidget != widget){
                        hoveringWidget?.asMouse()?.onExit()
                        hoveringWidget = widget
                        hoveringWidget?.asMouse()?.onEnter()
                    }
                    return true
                }
        }
        hoveringWidget?.asMouse()?.onExit()
        hoveringWidget = null
        return false
    }

    override fun onMouseMove(x: Int, y: Int): Boolean {
        return checkHover(x, y)
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        val realX = x - offsetX
        val realY = y - offsetY
        clickedWidget?.asMouse()?.onMouseDrag(realX - selectedWidget!!.x, realY - selectedWidget!!.y, button)
        checkHover(x, y)
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        val realX = x - offsetX
        val realY = y - offsetY
        clickedWidget = null
        for (widget in children.reversed()) {
            if (widget.x <= realX && widget.x + widget.width >= realX && widget.y <= realY && widget.y + widget.height >= realY)
                if (widget.asMouse()?.onMouseDown(realX - widget.x, realY - widget.y, button) == true) {
                    if (selectedWidget != widget){
                        selectedWidget?.selected = false
                        selectedWidget = widget
                        selectedWidget?.selected = true
                        if (shouldReorder)
                            moveToTop(widget)
                    }
                    clickedWidget = widget
                    return true
                }
        }
        selectedWidget?.selected = false
        selectedWidget = null
        return false
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        val realX = x - offsetX
        val realY = y - offsetY
        clickedWidget?.asMouse()?.onMouseUp(realX - selectedWidget!!.x, realY - selectedWidget!!.y, button)
        clickedWidget = null
    }

    override fun onMouseWheel(x: Int, y: Int, direction: Int) {
        val realX = x - offsetX
        val realY = y - offsetY
        checkHover(x, y)
        hoveringWidget?.asMouse()?.onMouseWheel(realX- hoveringWidget!!.x, realY - hoveringWidget!!.y, direction)
    }

    override fun onEnter() {
        // Do nothing
    }

    override fun onExit() {
        hoveringWidget?.asMouse()?.onExit()
        hoveringWidget = null
    }


    override fun onKeyDown(key: Int, char: Char) {
        (selectedWidget as? Keyboard)?.onKeyDown(key, char)
    }

    override fun onKeyUp(key: Int, char: Char) {
        (selectedWidget as? Keyboard)?.onKeyUp(key, char)
    }

}