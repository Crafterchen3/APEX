package com.deckerpw.apex.ui

import com.deckerpw.apex.ui.widgets.Widget

interface Mouse {

    fun onMouseMove(x: Int, y: Int): Boolean
    fun onMouseDrag(x: Int, y: Int, button: Int)
    fun onMouseDown(x: Int, y: Int, button: Int): Boolean
    fun onMouseUp(x: Int, y: Int, button: Int)
    fun onMouseWheel(x: Int, y: Int, direction: Int)
    fun onEnter()
    fun onExit()

}

fun Widget.asMouse(): Mouse? = this as? Mouse

interface Keyboard {

    fun onKeyDown(key: Int, char: Char)
    fun onKeyUp(key: Int, char: Char)

}

fun Widget.asKeyboard(): Keyboard? = this as? Keyboard