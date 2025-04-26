package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.getImage
import com.deckerpw.apex.ui.getThemedImage
import com.deckerpw.apex.ui.graphics.centeredText
import com.deckerpw.apex.ui.graphics.text
import com.deckerpw.apex.ui.graphics.theme
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.abs

class SelectEffect(parent: Widget?, x: Int, y: Int, width: Int, height: Int) :
    MouseWidget(parent, x, y, width, height, false) {

    private var startX = 0
    private var startY = 0
    private var endX = 0
    private var endY = 0
    private var dragging = false

    override fun paint(graphics2D: Graphics2D) {
        if (dragging) {
            graphics2D.color = java.awt.Color(0, 147, 255, 50)
            graphics2D.fillRect(
                minOf(startX, endX),
                minOf(startY, endY),
                abs(endX - startX),
                abs(endY - startY)
            )
            graphics2D.drawRect(
                minOf(startX, endX),
                minOf(startY, endY),
                abs(endX - startX) - 1,
                abs(endY - startY) - 1
            )
        }
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        super.onMouseDown(x, y, button)
        if (button != 1)
            return false
        startX = x
        startY = y
        return true
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        super.onMouseDrag(x, y, button)
        endX = x
        endY = y
        dragging = true
        update()
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        super.onMouseUp(x, y, button)
        dragging = false
        update()
    }
}

class Desktop(parent: Widget?, x: Int, y: Int, width: Int, height: Int) : Container(parent, x, y, width, height) {

    init {
        add(SelectEffect(this, 0, 0, width, height))
        loadDesktopFolder()

//        add(DesktopIcon(this, 0, 0, 64, 64, getThemedImage("textures/apps/hotel/icon.png")!!, "Hotel Game (1989)", "hotel"))
//        add(DesktopIcon(this, 64, 0, 64, 64, getThemedImage("textures/icons/file_explorer.png")!!, "File Explorer"))
//        add(DesktopIcon(this, 128, 0, 64, 64, getThemedImage("textures/icons/settings.png")!!, "Settings"))
//        add(DesktopIcon(this, 192, 0, 64, 64, getThemedImage("textures/icons/exit.png")!!, "Exit"))
    }

    fun addIcon(icon: BufferedImage, title: String, id: String, path: String,action: () -> Unit = {}) {
        val config = machine.currentUser?.config!!
        if (config.has("desktop.icons.$id")) {
            add(
                DesktopIcon(
                    this,
                    config.get("desktop.icons.$id.x"),
                    config.get("desktop.icons.$id.y"),
                    64,
                    64,
                    icon,
                    title,
                    id,
                    path,
                    action
                )
            )
        } else
            add(DesktopIcon(this, 0, 0, 64, 64, icon, title, id, path, action))
    }

    private fun loadDesktopFolder(){
        val path = machine.currentUser?.username?.let { "A:/home/$it/Desktop" }
        val files = machine.filesystem.list(path!!)
        if (files != null) {
            for (file in files) {
                //read one line at a time
                val lines = machine.filesystem.getFile("$path/$file").readLines()
                val program = lines[0]
                var image = getImage(lines[1])
                if (image == null){
                    image = getThemedImage("textures/os/desktop_default.png")
                }
                addIcon(image!!, file!!.substringBeforeLast('.'), file, "$path/$file") {
                    machine.runProgram(program)
                }
            }
        }
    }

    fun selectIcon(icon: DesktopIcon) {
        children.forEach {
            if (it is DesktopIcon) {
                it.selected = it == icon
                it.update()
            }
        }
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        val rv = super.onMouseDown(x, y, button)
        if (button == 3 && !rv) {
            createContextMenu(
                x, y, listOf(
                    ContextMenu.Action("New Shortcut") {
                        if (machine.windowManager != null){
                            val window = Window(machine.windowManager, 100, 100, 200, 120, "New Shortcut")
                            lateinit var program: TextField
                            val name = TextField(window, 20, 20, 160, "", placeholder = "Name", onEnterPressed = {
                                selectWidget(program)
                            })

                            fun addShortcut(){
                                val file = machine.filesystem.getFile("A:/home/${machine.currentUser?.username}/Desktop/${name.text}.desktop")
                                file.writeText(program.text+"\n")
                                addIcon(getThemedImage("textures/os/desktop_default.png")!!, name.text, "${name.text}.desktop", "A:/home/${machine.currentUser?.username}/Desktop/${name.text}.desktop")
                                window.close()
                            }

                            window.add(name)
                            program = TextField(window, 20, 50, 160, "", placeholder = "Program", onEnterPressed = {
                                addShortcut()
                            })
                            window.add(program)
                            window.add(
                                colorButton(
                                    window,
                                    20,
                                    80,
                                    160,
                                    20,
                                    "Create",
                                    arrayOf(java.awt.Color.WHITE, theme.primaryColor, theme.primaryColor)
                                ) { _, _, _ ->
                                    addShortcut()
                                }
                            )
                            machine.windowManager?.add(window)
                        }

                    },
                    ContextMenu.Action("Exit") { System.exit(0) },
                )
            )
            return true
        } else
            return rv
    }

    override fun paint(graphics2D: Graphics2D) {
        graphics2D.color = java.awt.Color.WHITE
        graphics2D.text("APEX", 10, height - 30)
        super.paint(graphics2D)
    }
}

class DesktopIcon(
    override val parent: Desktop,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    private val icon: BufferedImage,
    private val title: String,
    private val id: String,
    private val path: String,
    val action: () -> Unit = {}
) : MouseWidget(parent, x, y, width, height) {

    var startX = 0
    var startY = 0
    var dragging = false;
    var lastClicked: Long = 0

    override fun paint(graphics2D: Graphics2D) {
        if (selected) {
            graphics2D.color = java.awt.Color(0, 147, 255, 50)
            graphics2D.fillRect(0, 0, width, height)
        } else if (hovered) {
            graphics2D.color = java.awt.Color(0, 147, 255, 25)
            graphics2D.fillRect(0, 0, width, height)
        }

        graphics2D.drawImage(
            icon,
            (width - icon.width) / 2,
            (height - icon.height) / 2 - 11,
            icon.width,
            icon.height,
            null
        )
        graphics2D.color = java.awt.Color.WHITE
        graphics2D.centeredText(title, width / 2, height - 20)
    }

    override fun onMouseUp(x: Int, y: Int, button: Int) {
        super.onMouseUp(x, y, button)
        dragging = false
        if (System.currentTimeMillis() - lastClicked < 500) {
            action()
        }
        lastClicked = System.currentTimeMillis()
        savePosition()
    }

    fun savePosition() {
        machine.currentUser?.config?.set("desktop.icons.$id.x", x)
        machine.currentUser?.config?.set("desktop.icons.$id.y", y)
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        if (button == 3){
            val pair = getPositionOnScreen(x,y)
            createContextMenu(pair.first,pair.second, listOf(
                ContextMenu.Action("Delete") {
                    machine.filesystem.delete(path)
                    parent.remove(this)
                }
            ))
            return true
        }else {
            super.onMouseDown(x, y, button)
            startX = x
            startY = y
            parent.selectIcon(this)
            return true
        }
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        super.onMouseDrag(x, y, button)
        if (button != 3) {
            if (abs(startX - x) > 10 || abs(startY - y) > 10)
                dragging = true
            if (dragging) {
                this.x += x - startX
                this.y += y - startY
                update()
            }
        }
    }
}