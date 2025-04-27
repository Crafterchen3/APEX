package com.deckerpw.apex.ui.scenes

import com.deckerpw.apex.machine.Machine
import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.*
import com.deckerpw.apex.ui.graphics.fontSize
import com.deckerpw.apex.ui.graphics.theme
import com.deckerpw.apex.ui.widgets.*
import com.deckerpw.apex.ui.widgets.shapes.createRectangle
import java.awt.Color

class LoginScene(parent: Screen, width: Int, height: Int) :
    Container(parent, 0, 0, width, height, background = testPatternFill), WindowManagerScene {

    override val windowManager: WindowManager = WindowManager(this, 0, 0, width, height)
    lateinit var password: TextField
    val username: TextField
    val window: Window

    init {
        add(windowManager)
        window = Window(windowManager, (width - 200) / 2, (height - 200) / 2, 200, 180, "Login to APEX")
        windowManager.add(window)
        window.apply {
//            add(
//                getThemedImage("textures/os/profile_picture.png")!!
//                    .createRectangle(window, (200 - 64) / 2, 20, 64, 64)
//            )
            add(TextBox(window, 5, 0, 190, 64, "APEX", modifier = Modifier.fontSize(30).centerString(true)))
            username = TextField(window, 20, 80, 160, "", placeholder = "Username", onEnterPressed = {
                selectWidget(password)
            })
            add(username)
            password = TextField(window, 20, 110, 160, "", '*', placeholder = "Password", onEnterPressed = {
                login()
            })
            add(password)
            add(
                colorButton(
                    window,
                    20,
                    140,
                    160,
                    20,
                    "Login",
                    arrayOf(Color.WHITE, theme.primaryColor, theme.primaryColor)
                ) { _, _, _ ->
                    login()
                })
            selectWidget(username)
        }
        windowManager.selectWidget(window)
        selectWidget(windowManager)
    }

    fun login(){
        val user = machine.login(username.text, password.text)
        if (user != null) {
            (parent as Screen).setScene(DesktopScene(parent, width, height))
        } else {
            println("Invalid login")
            username.text = ""
            username.update()
            password.text = ""
            password.update()
            window.selectWidget(username)
        }
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): Boolean {
        super.onMouseDown(x, y, button)
        return true
    }

    override fun onKeyDown(key: Int, char: Char) {
        super.onKeyDown(key, char)
        when (key) {
            27 -> { // Escape key
                System.exit(0)
            }
        }
    }

}