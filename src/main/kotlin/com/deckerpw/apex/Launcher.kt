package com.deckerpw.apex

import com.deckerpw.apex.machine.Machine
import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.JavaDrive

class Launcher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val machine = Machine(args[0])
            machine.run()
            JavaDrive.javaDrive.apply {
                list("/")?.forEach {
                    println(it)
                }
                println()
                getURL("/assets/test.txt")?.openStream()?.use {
                    println(it.readBytes().toString(Charsets.UTF_8))
                }
            }
            println(machine.adminPassword)
            println(machine.paulPassword)
        }
    }
}