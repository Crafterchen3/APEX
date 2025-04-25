package com.deckerpw.apex.ui.scenes

import com.deckerpw.apex.ui.Screen
import com.deckerpw.apex.ui.testPatternFill
import com.deckerpw.apex.ui.widgets.Container
import com.deckerpw.apex.ui.widgets.Desktop
import com.deckerpw.apex.ui.widgets.WindowManager
import com.deckerpw.apex.ui.widgets.contextMenuHost

class DesktopScene(parent: Screen, width: Int, height: Int) :
    Container(parent, 0, 0, width, height, background = testPatternFill), WindowManagerScene {

    val desktop: Desktop = Desktop(this, 0, 0, width, height)
    override val windowManager: WindowManager = WindowManager(this, 0, 0, width, height)

    init {
        add(desktop)
        add(windowManager)
        contextMenuHost = Container(this, 0, 0, width, height)
        add(contextMenuHost!!)
    }

    override fun onKeyDown(key: Int, char: Char) {
        super.onKeyDown(key, char)
        when (key) {
            27 -> {
                // Escape key
                System.exit(0)
            }
        }
    }

}