package com.deckerpw.apex.machine

import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.Filesystem
import com.deckerpw.apex.machine.filesystem.JavaDrive
import com.deckerpw.apex.machine.filesystem.PathDrive
import com.google.gson.internal.LinkedTreeMap
import java.io.File

class Machine(val path: String) {

    companion object {
        var instance: Machine? = null
    }

    val filesystem = Filesystem()
    private var running = false

    private val config = FileConfig(File("$path/config.json"))
    val adminPassword: String by ConfigValue("admin.password", config,"admin")
    val paulPassword: String by ConfigValue("users.paul.password", config,"paul")

    val isRunning: Boolean
        get() = running

    init {
        if (instance != null) {
            throw RuntimeException("Machine already exists")
        }
        instance = this
        filesystem.mount(DriveLetter.A, PathDrive(path, "Root"))
        filesystem.mount(DriveLetter.J, JavaDrive.javaDrive)
    }

    fun run() {
        if (running) {
            println("Machine is already running")
            return
        }
        running = true
        println("Machine is running at $path")
    }
}