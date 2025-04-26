package com.deckerpw.apex

import com.deckerpw.apex.ui.getThemedImage
import com.deckerpw.apex.ui.graphics.PatternFill
import com.deckerpw.apex.ui.graphics.theme
import com.deckerpw.apex.ui.widgets.*
import java.awt.Color
import java.awt.Graphics
import java.awt.event.*
import java.awt.image.BufferedImage
import javax.swing.JFrame

//fun UITest() {
//    println("Hello, world!")
//    val image = theme.windowCreator.getWindowImage(100, 100)
//    val container = TetheredContainer()
//    val desktop = Desktop(container, 0, 0, 800, 600)
//    container.add(desktop)
//
//    val windowManager = WindowManager(container, 0, 0, 800, 600)
//    fun addWin() {
//        val window = Window(windowManager, 100, 100, 200, 200, "Test Window")
//        windowManager.add(window)
//        window.add(
//            colorButton(
//                window, 10, 16, 50, 50, palette = arrayOf(Color.RED, Color.GREEN, Color.BLUE)
//            ) { button, x, y ->
//                val pair = button.getPositionOnScreen(x, y)
//                createContextMenu(pair.first, pair.second, listOf(
//                    ContextMenu.Action("Test", { println("Test") }),
//                    ContextMenu.Action("Test", { println("Test") }),
//                    ContextMenu.Action("Test", { println("Test") })
//                ))
//            })
//    }
//    desktop.addIcon(getThemedImage("textures/apps/hotel/icon.png")!!, "Hotel Game (1986)", "hotel"){
//        addWin()
//    }
//    container.add(windowManager)
//    contextMenuHost = Container(container, 0, 0, 800, 600)
//    container.add(contextMenuHost!!)
//
//
//    //window.update()
//}