package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.Mouse
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

abstract class AbstractSelectionList(parent: Widget?, x: Int, y: Int, width: Int, height: Int, var itemHeight: Int = 0) : Widget(parent, x, y, width,
    height
), Mouse {
    var entries: MutableList<Entry?> = mutableListOf()
    var offset: Int = 0
    protected var selectedEntry: Entry? = null
    private val changeEvents: MutableList<() -> Unit> = mutableListOf()

    override fun onMouseDown(mouseX: Int, mouseY: Int, button: Int): Boolean {
        val i = floor(((mouseY - offset) / itemHeight).toDouble()).toInt()
        select(i)
        return true
    }

    fun addEventListener(r: () -> Unit) {
        changeEvents.add(r)
    }

    private fun select(i: Int) {
        if (entries.isEmpty()) return
        if (selectedEntry != null) selectedEntry!!.selected = false
        selectedEntry = entries[i]
        selectedEntry!!.selected = true
        update()
        for (changeEvent in changeEvents) {
            changeEvent()
        }
    }

    fun getSelectedIndex(): Int {
        return entries.indexOf(selectedEntry)
    }

    override fun onMouseUp(mouseX: Int, mouseY: Int, button: Int) {}

    override fun onMouseDrag(mouseX: Int, mouseY: Int, button: Int) {}

    override fun onMouseMove(mouseX: Int, mouseY: Int): Boolean {
        return true
    }

    override fun onEnter() {

    }

    override fun onExit() {

    }

    override fun onMouseWheel(mouseX: Int, mouseY: Int, scrollAmount: Int) {
        val max: Int = (entries.size * itemHeight - height) * -1
        val newOff = offset - scrollAmount * 10
        offset = min(0, max(max, newOff))
        update()
        return
    }

    override fun paint(graphics: Graphics2D) {
        graphics.color = Color.BLACK
        graphics.fillRect(0,0,width,height)
        val start = floor(((offset * -1) / itemHeight).toDouble()).toInt()
        val max = ceil(height.toDouble() / itemHeight.toDouble()).toInt() + 2
        for (i in start..<min(start + max, entries.size)) {
            entries[i]!!.renderEntry(graphics, i * itemHeight + offset)
        }
        graphics.color = Color.WHITE
        graphics.drawRect(0,0,width-1,height-1)
    }

    abstract class Entry(parent: Widget?, width: Int, height: Int) : Widget(parent, 0, 0, width, height) {
        override fun onSelected() {
            super.onSelected()
            update()
        }

        override fun onDeselected() {
            super.onDeselected()
            update()
        }

        fun renderEntry(graphics: Graphics2D, y: Int) {
            if (buffer == null)
                update()
            graphics.drawImage(buffer, 0, y, width, height,null)
        }
    }
}