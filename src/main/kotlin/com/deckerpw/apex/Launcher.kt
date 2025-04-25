package com.deckerpw.apex

import com.deckerpw.apex.machine.Machine
import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.JavaDrive
import com.deckerpw.apex.machine.util.translatedString
import com.deckerpw.apex.ui.Screen
import com.deckerpw.apex.ui.scenes.DesktopScene
import com.deckerpw.apex.ui.scenes.LoginScene
import com.deckerpw.apex.ui.screenHeight
import com.deckerpw.apex.ui.screenWidth

class Launcher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Check if we should use OpenGL rendering
            val useOpenGL = args.size > 1 && args[1] == "--opengl"

            val machine = Machine(args[0], useOpenGL)
            val superUser = machine.run()!!
            JavaDrive.javaDrive.apply {
                list("/")?.forEach {
                    println(it)
                }
            }

//            superUser.createUser("paul", "password")
//            val user = machine.login("paul", "password")
//            println(user == machine.currentUser)
//            println(translatedString("machine.init"))
        }
    }

}
