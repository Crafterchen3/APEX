package com.deckerpw.apex.machine

import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.Filesystem
import com.deckerpw.apex.machine.filesystem.JavaDrive
import com.deckerpw.apex.machine.filesystem.PathDrive
import com.google.gson.internal.LinkedTreeMap

class Machine(val path: String) {

    companion object {
        var instance: Machine? = null
    }

    val filesystem = Filesystem()
    internal val config: FileConfig
    internal val users = mutableListOf<User>()

    private var _currentUser: User? = null
    val currentUser: User?
        get() = _currentUser

    private var running = false
    val isRunning: Boolean
        get() = running

    init {
        if (instance != null) {
            throw RuntimeException("Machine already exists")
        }
        instance = this
        filesystem.mount(DriveLetter.A, PathDrive(path, "Root"))
        filesystem.mount(DriveLetter.J, JavaDrive.javaDrive)
        config = FileConfig(filesystem.getFile("A:config.json"))
        populateUsers()
    }

    private fun populateUsers() {
        if (!config.has("users")) {
            println("No users found in config")
            return
        }
        val usersJson = config.get<LinkedTreeMap<String, Any>>("users")
        for (user in usersJson.keys) {
            users.add(User(this, user))
        }
    }

    fun login(username: String, password: String): User? {
        if (currentUser != null) {
            println("User ${currentUser?.username} is already logged in")
            return null
        }
        val user = users.find { it.username == username }
        if (user == null) {
            println("User $username not found")
            return null
        }
        if (!user.checkCredentials(password)) {
            println("User $username not found")
            return null
        }
        _currentUser = user
        return user
    }

    fun logout() {
        if (currentUser == null) {
            println("No user is logged in")
            return
        }
        _currentUser = null
    }

    fun run(): SuperUser? {
        if (running) {
            println("Machine is already running")
            return null
        }
        running = true
        println("Machine is running at $path")
        return SuperUser(this)
    }
}

class User internal constructor(machine: Machine, val username: String) {

    private var _displayName: String by ConfigValue("users.$username.display_name", machine.config, username)
    val displayName: String
        get() = _displayName

    private var pass: Int by ConfigValue("users.$username.pass", machine.config, "".hashCode())
    val config = FileConfig(machine.filesystem.getFile("A:home/$username/config.json"))

    internal fun checkCredentials(password: String): Boolean {
        return pass == password.hashCode()
    }

    internal fun changePassword(password: String) {
        pass = password.hashCode()
    }

}

class SuperUser internal constructor(val machine: Machine){

    internal fun createUser(username: String, password: String, displayName: String? = null){
        machine.apply {
            if (users.find { it.username == username } != null) {
                println("User $username already exists")
                return
            }
            val userPath = "A:/home/$username"
            filesystem.createDirectory(userPath)
            config.set("users.$username.display_name", displayName ?: username)
            config.set("users.$username.pass", password.hashCode())
            users.add(User(this, username))
        }
    }

}