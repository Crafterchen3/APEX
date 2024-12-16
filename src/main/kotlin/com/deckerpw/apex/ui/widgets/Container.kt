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
    private var selectedWidget: Widget? = null
    private var hoveringWidget: Widget? = null
    private var clickedWidget: Widget? = null

    fun add(widget: Widget, autoUpdate: Boolean = true) {
        children.add(widget)
        if (autoUpdate)
            update()
    }

    fun remove(widget: Widget) {
        children.remove(widget)
    }

    override fun paint(graphics2D: Graphics2D) {
        background?.render(graphics2D, x, y, width, height)
        if (children.isEmpty())
            return
        children.forEach { it.render(graphics2D) }
    }

    override fun onDeselected() {
        selectedWidget?.selected = false
        selectedWidget = null
    }

    fun getWidgetAt(x: Int, y: Int): Widget? {
        return children.reversed().find { it.x <= x && it.x + it.width >= x && it.y <= y && it.y + it.height >= y }
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
    }

    private fun checkHover(x: Int, y: Int):Boolean {
        for (widget in children.reversed()) {
            if (widget.x <= x && widget.x + widget.width >= x && widget.y <= y && widget.y + widget.height >= y)
                if (widget.asMouse()?.onMouseMove(x - widget.x, y - widget.y) == true) {
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
        clickedWidget?.asMouse()?.onMouseDrag(x - selectedWidget!!.x, y - selectedWidget!!.y, button)
        checkHover(x, y)
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        clickedWidget = null
        for (widget in children.reversed()) {
            if (widget.x <= x && widget.x + widget.width >= x && widget.y <= y && widget.y + widget.height >= y)
                if (widget.asMouse()?.onMouseDown(x - widget.x, y - widget.y, button) == true) {
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
        clickedWidget?.asMouse()?.onMouseUp(x - selectedWidget!!.x, y - selectedWidget!!.y, button)
        clickedWidget = null
    }

    override fun onMouseWheel(x: Int, y: Int, direction: Int) {
        val widget = getWidgetAt(x, y)
        widget?.asMouse()?.onMouseWheel(x - widget.x, y - widget.y, direction)
    }

    override fun onEnter() {
        // Do nothing
    }

    override fun onExit() {
        hoveringWidget?.asMouse()?.onExit()
        hoveringWidget = null
    }


    override fun onKeyDown(key: Int) {
        (selectedWidget as? Keyboard)?.onKeyDown(key)
    }

    override fun onKeyUp(key: Int) {
        (selectedWidget as? Keyboard)?.onKeyUp(key)
    }

}