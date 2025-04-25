package com.deckerpw.apex.ui.widgets

class WindowManager(parent: Widget?, x: Int, y: Int, width: Int, height: Int) : Container(parent, x, y, width, height, shouldReorder = true) {

    fun add(window: Window, allowDuplicates: Boolean = false) {
        val clazz = window.javaClass
        if (!allowDuplicates && children.find { it.javaClass.equals(clazz) } != null) {
            return
        }
        add(window as Widget)
    }

}