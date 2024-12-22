package com.deckerpw.apex

import com.deckerpw.apex.machine.Machine
import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.JavaDrive
import com.deckerpw.apex.machine.util.translatedString

class Launcher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val machine = Machine(args[0])
            val superUser = machine.run()!!
            JavaDrive.javaDrive.apply {
                list("/")?.forEach {
                    println(it)
                }
            }

            //superUser.createUser("paul", "password")
            val user = machine.login("paul", "password")
            println(user == machine.currentUser)
            println(translatedString("machine.init"))
            UITest()
        }
    }
}