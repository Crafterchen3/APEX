package com.deckerpw.apex.ui.widgets

abstract class ScrollContainer(parent: Widget?, x: Int, y: Int, width: Int, height: Int) : Container(parent, x, y, width, height) {

    override fun onMouseWheel(x: Int, y: Int, direction: Int) {
        if (canScroll(newOffsetY = offsetY - (direction * 10))) {
            offsetY -= (direction * 10)
            update()
        }
    }

    override fun onMouseMove(x: Int, y: Int): Boolean {
        super.onMouseMove(x, y)
        return true
    }

    abstract fun canScroll(newOffsetY: Int): Boolean

}